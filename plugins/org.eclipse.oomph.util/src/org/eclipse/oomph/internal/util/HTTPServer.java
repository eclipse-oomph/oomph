/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.util;

import org.eclipse.oomph.util.HexUtil;
import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OomphPlugin.BundleFile;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.Platform;

import org.osgi.framework.Bundle;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Eike Stepper
 */
public final class HTTPServer
{
  private static final boolean DEBUG = false;

  private static final boolean DEBUG_REQUEST = false;

  private static final boolean DEBUG_RESPONSE = false;

  private static final Map<String, String> CONTENT_TYPES = new HashMap<String, String>();

  private static final ImageContext IMAGE_CONTEXT = new ImageContext();

  private static final String PATH_SEPARATOR = "/";

  private static final String STATUS_OK = "200 OK";

  private static final String STATUS_SEE_OTHER = "303 See Other";

  private static final String STATUS_BAD_REQUEST = "400 Bad Request";

  private static final String STATUS_FORBIDDEN = "403 Forbidden";

  private static final String STATUS_NOT_FOUND = "404 Not Found";

  private static final String STATUS_INTERNAL_SERVER_ERROR = "500 Internal Server Error";

  private static final String STATUS_NOT_IMPLEMENTED = "501 Not Implemented";

  private final List<Context> contexts = new ArrayList<Context>();

  private final ExecutorService threadPool = Executors.newCachedThreadPool();

  private Acceptor acceptor;

  public HTTPServer() throws IOException
  {
    this(15000, 50000);
  }

  public HTTPServer(int minPort, int maxPort) throws IOException
  {
    addContext(IMAGE_CONTEXT);

    for (int port = minPort; port <= maxPort; port++)
    {
      try
      {
        ServerSocket serverSocket = new ServerSocket(port, 500);
        acceptor = new Acceptor(serverSocket);
        return;
      }
      catch (BindException ex)
      {
        // Try next port.
      }
      catch (InterruptedException ex)
      {
        throw new IOExceptionWithCause("Start interrupted", ex);
      }
    }

    throw new IOException("No port available between " + minPort + " and " + maxPort);
  }

  public int getPort()
  {
    if (acceptor != null)
    {
      return acceptor.getPort();
    }

    return 0;
  }

  public String getURL()
  {
    int port = acceptor != null ? acceptor.getPort() : 0;
    return "http://127.0.0.1:" + port;
  }

  public synchronized void addContext(Context context)
  {
    contexts.add(context);
    Collections.sort(contexts);
  }

  public synchronized void removeContext(Context context)
  {
    contexts.remove(context);
  }

  public synchronized Context getContext(String path)
  {
    for (Context context : contexts)
    {
      if (path.startsWith(context.getPath()))
      {
        return context;
      }
    }

    return null;
  }

  public void stop() throws IOException
  {
    if (acceptor != null)
    {
      acceptor.interrupt();
      threadPool.shutdown();
    }
  }

  @Override
  public String toString()
  {
    return getURL();
  }

  private static void registerContentType(String contentType, String... extensions)
  {
    for (String extension : extensions)
    {
      CONTENT_TYPES.put(extension, contentType);
    }
  }

  static
  {
    registerContentType("application/java-archive", "jar");
    registerContentType("application/javascript", "js");
    registerContentType("application/json", "json");
    registerContentType("application/jsonml+json", "jsonml");
    registerContentType("application/pdf", "pdf");
    registerContentType("application/xaml+xml", "xaml");
    registerContentType("application/xhtml+xml", "xhtml", "xht");
    registerContentType("application/xml", "xml", "xsl");
    registerContentType("application/xml-dtd", "dtd");
    registerContentType("application/xslt+xml", "xslt");
    registerContentType("application/zip", "zip");

    registerContentType("image/bmp", "bmp");
    registerContentType("image/gif", "gif");
    registerContentType("image/jpeg", "jpeg", "jpg", "jpe");
    registerContentType("image/png", "png");
    registerContentType("image/svg+xml", "svg", "svgz");
    registerContentType("image/tiff", "tiff", "tif");
    registerContentType("image/x-icon", "ico");

    registerContentType("text/css", "css");
    registerContentType("text/html", "html", "htm");
    registerContentType("text/plain", "txt", "text", "conf", "def", "list", "log", "in");
    registerContentType("text/x-java-source", "java");
  }

  public static void main(String[] args) throws Exception
  {
    HTTPServer server = new HTTPServer(80, 100);
    server.addContext(new FileContext("/file/c", true, new File("C:")));
    server.addContext(new FileContext("/file/e", true, new File("E:")));

    System.out.println("http://localhost:" + server.getPort());
    System.out.println();
    while (System.in.available() == 0)
    {
      Thread.sleep(50);
    }

    server.stop();
  }

  /**
   * @author Eike Stepper
   */
  private final class Acceptor extends Thread
  {
    private final ServerSocket serverSocket;

    private final CountDownLatch started = new CountDownLatch(1);

    public Acceptor(ServerSocket serverSocket) throws InterruptedException
    {
      super("Httpd");
      this.serverSocket = serverSocket;
      setDaemon(true);

      start();
      started.await();
    }

    public int getPort()
    {
      return serverSocket.getLocalPort();
    }

    @Override
    public void run()
    {
      started.countDown();

      while (true)
      {
        try
        {
          Socket socket = serverSocket.accept();
          RequestHandler requestHandler = new RequestHandler(socket);
          threadPool.execute(requestHandler);
        }
        catch (Exception ex)
        {
          if (interrupted())
          {
            return;
          }

          UtilPlugin.INSTANCE.log(ex);
        }
      }
    }

    @Override
    public void interrupt()
    {
      try
      {
        super.interrupt();
      }
      finally
      {
        IOUtil.closeSilent(serverSocket);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class RequestHandler implements Runnable
  {
    private final Socket socket;

    public RequestHandler(Socket socket)
    {
      this.socket = socket;
    }

    public void run()
    {
      InputStream inputStream = null;
      OutputStream outputStream = null;

      try
      {
        try
        {
          inputStream = socket.getInputStream();
          outputStream = socket.getOutputStream();
        }
        catch (Exception ex)
        {
          if (isBadState())
          {
            return;
          }

          UtilPlugin.INSTANCE.log(ex);
        }

        if (inputStream != null && outputStream != null)
        {
          BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
          DataOutputStream output = new DataOutputStream(outputStream);

          try
          {
            handleRequest(input, output);
          }
          catch (Exception ex)
          {
            if (isBadState())
            {
              return;
            }

            if (DEBUG || !(ex instanceof SocketException))
            {
              UtilPlugin.INSTANCE.log(ex);
            }

            Context.sendResponse(output, STATUS_INTERNAL_SERVER_ERROR, null, 0, true);
          }

          try
          {
            output.flush();
          }
          catch (IOException ex)
          {
            if (DEBUG)
            {
              UtilPlugin.INSTANCE.log(ex);
            }
          }
        }
      }
      finally
      {
        IOUtil.closeSilent(outputStream);
        IOUtil.closeSilent(inputStream);
        IOUtil.closeSilent(socket);
      }
    }

    private void handleRequest(BufferedReader input, DataOutputStream output) throws Exception
    {
      String line = input.readLine();
      if (line == null)
      {
        return;
      }

      if (DEBUG_REQUEST)
      {
        System.out.println();
        String l = line;
        while (!StringUtil.isEmpty(l))
        {
          System.out.println(l);
          l = input.readLine();
        }
      }

      line = line.replace(" ", " ");
      String[] tokens = line.split(" ");
      if (tokens.length < 2)
      {
        Context.sendResponse(output, STATUS_BAD_REQUEST, null, 0, false);
        return;
      }

      String method = tokens[0];
      boolean head = "HEAD".equalsIgnoreCase(method);

      if (!head && !"GET".equalsIgnoreCase(method))
      {
        Context.sendResponse(output, STATUS_NOT_IMPLEMENTED, null, 0, false);
        return;
      }

      URI uri = new URI("xxx:" + tokens[1]);
      String path = Context.decodePath(uri.getPath());

      Context context = getContext(path);
      if (context == null)
      {
        if (PATH_SEPARATOR.equals(path))
        {
          Context.sendResponse(output, STATUS_OK, "index.html", 0, false);

          for (Context c : contexts)
          {
            if (c != IMAGE_CONTEXT)
            {
              String href = c.getPath();
              output.writeBytes("<img src=\"" + ImageContext.CONTEXT_PATH + ImageContext.NAME_CONTEXT + "\" valign=\"middle\"/> <a href=\"" + href
                  + PATH_SEPARATOR + "\">" + href + "</a><br>\r\n");
            }
          }

          return;
        }

        Context.sendResponse(output, STATUS_NOT_FOUND, null, 0, false);
        return;
      }

      path = path.substring(context.getPath().length());
      if (!path.startsWith(PATH_SEPARATOR))
      {
        path = PATH_SEPARATOR + path;
      }

      if (DEBUG)
      {
        System.out.println(context + " " + path);
      }

      context.handleRequest(path, output, !head);
    }

    private boolean isBadState()
    {
      return socket.isClosed() || !socket.isConnected() || socket.isInputShutdown() || socket.isOutputShutdown();
    }
  }

  /**
   * @author Eike Stepper
   */
  public static abstract class Context implements Comparable<Context>
  {
    private static final String URL_ENCODING = "UTF-8";

    private final String path;

    private final boolean allowDirectory;

    protected Context(String path, boolean allowDirectory)
    {
      if (!path.startsWith(PATH_SEPARATOR))
      {
        throw new IllegalArgumentException("Path must start with a slash: " + path);
      }

      this.path = path;
      this.allowDirectory = allowDirectory;
    }

    public final String getPath()
    {
      return path;
    }

    public final boolean isAllowDirectory()
    {
      return allowDirectory;
    }

    public final int compareTo(Context o)
    {
      return o.path.length() - path.length();
    }

    public Object getRoot()
    {
      return null;
    }

    public String getURL(HTTPServer server)
    {
      return server.getURL() + path;
    }

    @Override
    public final String toString()
    {
      String string = getClass().getSimpleName() + "[" + path;
      Object root = getRoot();
      if (root != null)
      {
        string += " --> " + root;
      }

      return string + "]";
    }

    protected void handleRequest(String path, DataOutputStream output, boolean responseBody) throws IOException
    {
      if (isDirectory(path))
      {
        if (!allowDirectory)
        {
          Context.sendResponse(output, STATUS_FORBIDDEN, null, 0, false);
        }
        else
        {
          if (!path.endsWith(PATH_SEPARATOR))
          {
            path += PATH_SEPARATOR;
            Context.sendResponse(output, STATUS_SEE_OTHER, path, 0, false);
          }
          else
          {
            Context.sendResponse(output, STATUS_OK, "index.html", 0, false);
          }

          if (path.length() > 1)
          {
            output
                .writeBytes("<img src=\"" + ImageContext.CONTEXT_PATH + ImageContext.NAME_FOLDER_UP + "\" valign=\"middle\"/> <a href=\"../\">..</a><br>\r\n");
          }

          String[] children = getChildren(path);
          if (children != null)
          {
            final String finalPath = path;
            Arrays.sort(children, new Comparator<String>()
            {
              public int compare(String n1, String n2)
              {
                int t1 = getType(n1);
                int t2 = getType(n2);

                int result = t1 - t2;
                if (result == 0)
                {
                  result = StringUtil.safe(n1).toLowerCase().compareTo(StringUtil.safe(n2).toLowerCase());
                }

                return result;
              }

              private int getType(String child)
              {
                return isDirectory(finalPath + child) ? 1 : 2;
              }
            });

            for (String child : children)
            {
              boolean directory = isDirectory(path + child);
              output.writeBytes(
                  "<img src=\"" + ImageContext.CONTEXT_PATH + (directory ? ImageContext.NAME_FOLDER : ImageContext.NAME_FILE) + "\" valign=\"middle\"/> ");

              String trailingSlash = directory ? PATH_SEPARATOR : "";
              output.writeBytes("<a href=\"" + encodePath(child) + trailingSlash + "\">" + child + trailingSlash + "</a><br>\r\n");
            }
          }
        }

        return;
      }

      if (!isFile(path))
      {
        Context.sendResponse(output, STATUS_NOT_FOUND, null, 0, false);
        return;
      }

      long lastModified = getLastModified(path);
      Context.sendResponse(output, STATUS_OK, path, lastModified, false);

      if (responseBody)
      {
        InputStream stream = null;

        try
        {
          stream = getContents(path);
          IOUtil.copy(stream, output);
        }
        finally
        {
          IOUtil.close(stream);
        }
      }
    }

    protected abstract boolean isDirectory(String path);

    protected abstract boolean isFile(String path);

    protected abstract String[] getChildren(String path) throws IOException;

    protected abstract InputStream getContents(String path) throws IOException;

    protected long getLastModified(String path) throws IOException
    {
      return System.currentTimeMillis();
    }

    protected static String encodePath(String path) throws UnsupportedEncodingException
    {
      StringBuilder builder = new StringBuilder();
      StringTokenizer tokenizer = new StringTokenizer(path, PATH_SEPARATOR);
      while (tokenizer.hasMoreTokens())
      {
        if (builder.length() != 0)
        {
          builder.append(PATH_SEPARATOR);
        }

        String token = tokenizer.nextToken();
        builder.append(URLEncoder.encode(token, URL_ENCODING));
      }

      return builder.toString();
    }

    protected static String decodePath(String path) throws UnsupportedEncodingException
    {
      return URLDecoder.decode(path, URL_ENCODING);
    }

    @SuppressWarnings("all")
    protected static String formatDate(long lastModified)
    {
      return org.apache.http.impl.cookie.DateUtils.formatDate(new Date(lastModified));
    }

    protected static void sendResponse(DataOutputStream output, String status, String fileName, long lastModified, boolean ignoreExceptions)
    {
      try
      {
        output.writeBytes("HTTP/1.0 ");
        output.writeBytes(status);
        output.writeBytes("\r\nConnection: close\r\nServer: ");
        output.writeBytes(HTTPServer.class.getName());
        output.writeBytes("\r\n");

        String location = null;
        if (status == STATUS_SEE_OTHER)
        {
          location = fileName;
          fileName = "index.html";
        }

        int lastDot = fileName == null ? -1 : fileName.lastIndexOf('.');
        String extension = lastDot == -1 ? "txt" : fileName.substring(lastDot + 1);

        String contentType = CONTENT_TYPES.get(extension);
        if (contentType == null)
        {
          contentType = CONTENT_TYPES.get("txt");
        }

        output.writeBytes("Content-Type: ");
        output.writeBytes(contentType);
        output.writeBytes("\r\n");

        if (lastModified != 0)
        {
          output.writeBytes("Last-Modified: ");
          output.writeBytes(formatDate(lastModified));
          output.writeBytes("\r\n");
        }

        if (location != null)
        {
          output.writeBytes("Location: /file/c");
          output.writeBytes(location);
          output.writeBytes("\r\n");
          output.writeBytes("\r\n");
          return;
        }

        output.writeBytes("\r\n");

        if (status == STATUS_OK)
        {
          return;
        }

        output.writeBytes(status);
        output.writeBytes("\r\n");
      }
      catch (IOException ex)
      {
        if (ignoreExceptions)
        {
          return;
        }

        if (ex instanceof SocketException && ex.getMessage().equals("Software caused connection abort: socket write error"))
        {
          return;
        }

        UtilPlugin.INSTANCE.log(ex);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ImageContext extends Context
  {
    public static final String CONTEXT_PATH = PATH_SEPARATOR + "~";

    public static final String NAME_CONTEXT = PATH_SEPARATOR + "context";

    public static final String NAME_FOLDER_UP = PATH_SEPARATOR + "folderup";

    public static final String NAME_FOLDER = PATH_SEPARATOR + "folder";

    public static final String NAME_FILE = PATH_SEPARATOR + "file";

    private static final byte[] ICON_CONTEXT = HexUtil.hexToBytes(
        "47494638396110001000e600002d2b4f3c4967f2f6ff3c4a672e4162304262394a68002e77002b723146672f4363344a6d304362384d6f374c6d384c6d394b6a384a683d5070b3bdcd002f77003177344a6c324767304463354b6c384e6eb2bdcdb4bfceb4bcc7b3bfceb8c3d0246ebd2770be2870be2971be4e69874f69874f6a87b4bdc7f2f8ff256fbd2973c14e6c8af1f8fff3f9fff7fbff4e6d8aeef7fff0f8fff4faffeff8fff1f9fff6fbff448fc3448ec2448fc2f2fafff4fbfff7fcff529bbf519abe579cbff9fdfffcfeff539cbf549cbf579dbff8fefffdfffffeffffa1c3bea2c3bea1c2bda3c3bea2c2bdd1e8e4a3c3bdcbefc2cbeec2cbecbeccedbeeaefe7e9eee5eaeee5e9ede3e6eae0eaeee3ffffc4ffffcbffffd2ffffd7ffffdcebebe2c8c498c7c498cac59aecebe2e6e3d2e6e2cfe6e2d0e5e1cfe7e3d2e5e1d0f4f0e3f3efe3ffffffffffff00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000021f9040100006b002c00000000100010000007b3806b8283848583001f524c2a2636244169861f1a19604f62106749213e8552195c0b1b6a096a6a50013d844c600b5b161e5fa60447295d832a4e130b5a0d0e5346456a493c8326666a1c0d59120755406a6524833810175e0f12582b08450a1137832464443b54142f151d353fd2833c49322839570827562d2ec4835d2050053066e8a011830192146108f508a084c50c0c33044419a0aad090114dc61818b344c4104382d0082971a30424902853aa141408003b");

    private static final byte[] ICON_FOLDER_UP = HexUtil.hexToBytes(
        "47494638396110001000841d009c6a3c41913b4a9045ac7a44ac7a4cac825cb48a54bc9264bc926cc4a274ccaa74d4b27cdcb2745edb63e4c27c72e57f93e596ecd28cfcd27cf4da949df3a7fcda8cfce29cfce2a4aff8b7fcea9cfceab4fcf2ccfcfefcffffffffffffffffff21f904010a001f002c000000001000100000056de0278e64699ee7b2280c3a1a5ccc19ae925951e42809df93898d7023180a13408de62280082e4b0d0178b1081e108ae0c0e5220e1f4345d0c05230024bc53289800d128120e094572a39f067203938e80212120e126e230007824d81827d2505908090902e8b2e2502979a2421003b");

    private static final byte[] ICON_FOLDER = HexUtil.hexToBytes(
        "47494638396110001000c40000f8e898f8f0c8f8e8b0e8d088f0d890f8e098f8e0a0f8d888f8d078e0c078f8d890f8d080d8b070bc8532c38b36b47f32a56c24ad722bbc7f32c38536ad6c249e6627ad722f9e5f1d9e5f208f5219ffffffffffff00000000000000000000000021f9040100001b002c0000000010001000000555e0268e64699ea7a33ae84869b046b513001043c24c3c4f3681a070d8204904c8a412437a189e068b744add4414584b61cbdd0e2c1bc8e160199bcd5fd1056159b8dfeeb4286341d8ef76f9a8c2effb5b8081828021003b");

    private static final byte[] ICON_FILE = HexUtil.hexToBytes(
        "47494638396110001000a529007b744f7e754e7e764e84784c867b4c8b7c4a8d7e4a9280489b83459d85449e8544a38842a38843a98a41ad8c3fb18f3eb4903dd4b268d4b269d5b269dabd7ce0c88fdde8f7e1ebf8e4edf9e8effaecf1faedf2faeef3faf0f4fbf3f7fbf5f7fbf6f7fcf5f8fbf5f8fcf6f8fbf7f9fbf8f9fbf8f9fcf7fafcf9fafcffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff21f904010a003f002c0000000010001000000669c09f10422c1285482404c56c4a2049e4c3442d9d4894c923fa73843e20d04854a1441cd18667cd6637a28c8e7c3e5f44159bfc86a3df24ee1a8182827f490819888989075106188f909005510417969797035102169d9e9e019b9fa3a14900a7a8a9005cacadae4941003b");

    private static final byte[] ICON_UNKNOWN = HexUtil.hexToBytes(
        "47494638396110001000d50000f7f8fcf3f5fbf8f9fcf5f7fcf3f5faf1f4fbeef2faf2f5fbf5f7fbebf0f9f0f4fbe5ecf8e7eef9e9eff9edf2faf4f7fce5edf9e4ecf8e8eff9ebf1faf0f4faf6f8fbe3ecf8e6eef9e5edf8ebf1f9eef3fae4edf8e6eef8eaf1faf2f6fbf1f5fadbe4eee8f0f9edf3faf0f5fa2862964476a45d89b15f8ab16a92b76c94b8799dbe95b2cca2bbd35080aa7b745081774e928048897a4c85784d9782478e7d4aa68942a287439d8445b4903db18e3eaf8d3fab8b40ffffff00000000000000000021f9040100003c002c000000001000100000068d409e70482c0e71c8a41249cc099ed0688ea88b5a053ae20e5001954860107747ac211e27526a05460c6a44dbe1c0521d022d12c16323de280a2305232624261f0a3744331a06060e852822061a3344301913131d609a09133044342112a52c2c210d120d3444311c171c0c60b017173144320b18181110bf181b1132442f16c7c8c92f442ecdcecfcd46d24541003b");

    public ImageContext()
    {
      super(CONTEXT_PATH, false);
    }

    @Override
    protected boolean isDirectory(String path)
    {
      return false;
    }

    @Override
    protected boolean isFile(String path)
    {
      return true;
    }

    @Override
    protected String[] getChildren(String path) throws IOException
    {
      return null;
    }

    @Override
    protected InputStream getContents(String path) throws IOException
    {
      byte[] bytes = getImage(path);
      return new ByteArrayInputStream(bytes);
    }

    private byte[] getImage(String path)
    {
      if (NAME_CONTEXT.equals(path))
      {
        return ICON_CONTEXT;
      }

      if (NAME_FOLDER_UP.equals(path))
      {
        return ICON_FOLDER_UP;
      }

      if (NAME_FOLDER.equals(path))
      {
        return ICON_FOLDER;
      }

      if (NAME_FILE.equals(path))
      {
        return ICON_FILE;
      }

      return ICON_UNKNOWN;
    }

    public static void main(String[] args)
    {
      System.out.println("private static final byte[] ICON_CONTEXT = HexUtil.hexToBytes(\""
          + HexUtil.bytesToHex(IOUtil.readFile(new File("/develop/icons/configuration_obj.gif"))) + "\");");

      System.out.println("private static final byte[] ICON_FOLDER_UP = HexUtil.hexToBytes(\""
          + HexUtil.bytesToHex(IOUtil.readFile(new File("/develop/icons/FolderUp.gif"))) + "\");");

      System.out.println("private static final byte[] ICON_FOLDER = HexUtil.hexToBytes(\""
          + HexUtil.bytesToHex(IOUtil.readFile(new File("/develop/icons/folder7.gif"))) + "\");");

      System.out.println(
          "private static final byte[] ICON_FILE = HexUtil.hexToBytes(\"" + HexUtil.bytesToHex(IOUtil.readFile(new File("/develop/icons/file.gif"))) + "\");");

      System.out.println("private static final byte[] ICON_UNKNOWN = HexUtil.hexToBytes(\""
          + HexUtil.bytesToHex(IOUtil.readFile(new File("/develop/icons/unknown_obj.gif"))) + "\");");
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class FileContext extends Context
  {
    private final File root;

    public FileContext(String path, boolean allowDirectory, File root)
    {
      super(path, allowDirectory);
      this.root = root;
    }

    @Override
    public final File getRoot()
    {
      return root;
    }

    @Override
    protected boolean isDirectory(String path)
    {
      return getFile(path).isDirectory();
    }

    @Override
    protected boolean isFile(String path)
    {
      return getFile(path).isFile();
    }

    @Override
    protected String[] getChildren(String path) throws IOException
    {
      return getFile(path).list();
    }

    @Override
    protected InputStream getContents(String path) throws IOException
    {
      File file = getFile(path);
      if (DEBUG_RESPONSE /* && path.endsWith(".html") */)
      {
        try
        {
          String contents = IOUtil.readUTF8(file);
          System.out.println(contents);
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }

      return new FileInputStream(file);
    }

    @Override
    protected long getLastModified(String path) throws IOException
    {
      File file = getFile(path);
      return file.lastModified();
    }

    private File getFile(String path)
    {
      if (root == null)
      {
        return new File(path);
      }

      return new File(root, path);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class PluginContext extends Context
  {
    public PluginContext(String path, boolean allowDirectory)
    {
      super(path, allowDirectory);
    }

    @Override
    protected boolean isDirectory(String path)
    {
      if (path.length() == 1)
      {
        // The context root is a directory of the resolved plugins.
        return true;
      }

      try
      {
        BundleFile file = getFile(path);
        return file.isDirectory();
      }
      catch (FileNotFoundException ex)
      {
        return false;
      }
    }

    @Override
    protected boolean isFile(String path)
    {
      try
      {
        BundleFile file = getFile(path);
        return !file.isDirectory();
      }
      catch (FileNotFoundException ex)
      {
        return false;
      }
    }

    @Override
    protected String[] getChildren(String path) throws IOException
    {
      String[] result;
      if (path.length() == 1)
      {
        // The context root is a directory of the resolved plugins.
        Bundle[] bundles = UtilPlugin.INSTANCE.getBundleContext().getBundles();

        result = new String[bundles.length];
        for (int i = 0; i < bundles.length; i++)
        {
          result[i] = bundles[i].getSymbolicName();
        }
      }
      else
      {
        BundleFile file = getFile(path);
        List<BundleFile> children = file.getChildren();

        result = new String[children.size()];
        for (int i = 0; i < result.length; i++)
        {
          result[i] = children.get(i).getName();
        }
      }

      return result;
    }

    @Override
    protected InputStream getContents(String path) throws IOException
    {
      BundleFile file = getFile(path);
      if (DEBUG_RESPONSE /* && path.endsWith(".html") */)
      {
        try
        {
          String contents = file.getContentsString();
          System.out.println(contents);
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }

      return file.getContents();
    }

    @Override
    protected long getLastModified(String path) throws IOException
    {
      BundleFile file = getFile(path);
      return file.getBundle().getLastModified();
    }

    private BundleFile getFile(String path) throws FileNotFoundException
    {
      String[] segments = path.split(PATH_SEPARATOR);

      Bundle bundle = Platform.getBundle(segments[1]);
      BundleFile file = new RootBundleFile(bundle);

      for (int i = 2; file != null && i < segments.length; i++)
      {
        String segment = segments[i];
        file = file.getChild(segment);
      }

      if (file == null)
      {
        throw new FileNotFoundException(path);
      }

      return file;
    }

    /**
     * @author Eike Stepper
     */
    private static class RootBundleFile extends BundleFile
    {
      private Bundle bundle;

      public RootBundleFile(Bundle bundle)
      {
        super("", true, null);
        this.bundle = bundle;
      }

      @Override
      public Bundle getBundle()
      {
        return bundle;
      }

      @Override
      public String getPath()
      {
        return "";
      }
    }
  }
}
