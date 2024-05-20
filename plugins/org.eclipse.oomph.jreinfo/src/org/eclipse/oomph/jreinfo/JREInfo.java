/*
 * Copyright (c) 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.jreinfo;

import org.eclipse.oomph.internal.jreinfo.JREInfoPlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.osgi.util.NLS;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stepper
 */
public final class JREInfo
{
  private static final boolean SKIP_USER_HOME = PropertiesUtil.isProperty("oomph.jreinfo.skip.user.home"); //$NON-NLS-1$

  private static final String[] EXTRA_SEARCH_PATH = PropertiesUtil.getProperty("oomph.jreinfo.extra.search.path", "").split(File.pathSeparator); //$NON-NLS-1$ //$NON-NLS-2$

  private static final Pattern JAVA_HOME_REGISTRY_VALUE_PATTERN = Pattern.compile("\\s*JavaHome\\s*REG_SZ\\s*(.*)\\s*", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

  public String javaHome;

  public int jdk;

  public JREInfo next;

  public static JREInfo getAll()
  {
    JREInfo jreInfo = null;

    try
    {
      switch (JREManager.OS_TYPE)
      {
        case Win:
          jreInfo = getAllWin();
          break;

        case Mac:
          jreInfo = getAllMac();
          break;

        case Linux:
          jreInfo = getAllLinux();
          break;

        default:
          //$FALL-THROUGH$
      }

      String javaHome = System.getProperty("java.home"); //$NON-NLS-1$
      jreInfo = createJREInfo(jreInfo, javaHome);

      if (!SKIP_USER_HOME)
      {
        String userHome = PropertiesUtil.getUserHome();
        jreInfo = searchFolder(jreInfo, userHome);
        jreInfo = searchFolder(jreInfo, userHome + "/java"); //$NON-NLS-1$
        jreInfo = searchFolder(jreInfo, userHome + "/jvm"); //$NON-NLS-1$
      }

      for (int i = 0; i < EXTRA_SEARCH_PATH.length; i++)
      {
        String search = EXTRA_SEARCH_PATH[i];
        jreInfo = searchFolder(jreInfo, search);
      }
    }
    catch (Exception ex)
    {
      JREInfoPlugin.INSTANCE.log(ex);
    }

    return jreInfo;
  }

  private static JREInfo createJREInfo(JREInfo jreInfo, String javaHome)
  {
    if (javaHome != null)
    {
      File javaHomeFolder = new File(javaHome);
      if (javaHomeFolder.isDirectory())
      {
        int jdk = 0;
        try
        {
          jdk = isJDK(javaHomeFolder);
        }
        catch (FileNotFoundException ex)
        {
          //$FALL-THROUGH$
        }

        JREInfo info = new JREInfo();
        info.javaHome = javaHomeFolder.getAbsolutePath();
        info.jdk = jdk;
        info.next = jreInfo;
        return info;
      }
    }

    return jreInfo;
  }

  @SuppressWarnings("nls")
  private static JREInfo getAllWin()
  {
    JREInfo jreInfo = null;

    try
    {
      String getenv = System.getenv("ProgramFiles");
      jreInfo = searchFolder(null, getenv + "/Java");
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    String systemRoot = System.getenv("SystemRoot");
    if (systemRoot != null)
    {
      File system32 = new File(systemRoot, "system32");
      if (system32.isDirectory())
      {
        File regExecutable = new File(system32, "reg.exe");
        if (regExecutable.isFile())
        {
          try
          {
            String executable = regExecutable.toString();
            jreInfo = searchRegistry(jreInfo, executable, "QUERY", "HKLM\\Software\\JavaSoft", "/s", "/t", "REG_SZ", "/f", "JavaHome");
            jreInfo = searchRegistry(jreInfo, executable, "QUERY", "HKLM\\Software\\Wow6432Node\\JavaSoft", "/s", "/t", "REG_SZ", "/f", "JavaHome");
          }
          catch (IOException | InterruptedException ex)
          {
            //$FALL-THROUGH$
          }
        }
      }
    }

    return jreInfo;
  }

  private static JREInfo getAllMac()
  {
    final JREInfo[] jreInfo = { null };

    try
    {
      ProcessBuilder builder = new ProcessBuilder();
      builder.command("/usr/libexec/java_home", "-X"); //$NON-NLS-1$ //$NON-NLS-2$

      Process process = builder.start();

      DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
      documentBuilder.setEntityResolver(new EntityResolver()
      {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
        {
          return new InputSource(new StringReader("")); //$NON-NLS-1$
        }
      });

      Element rootElement = XMLUtil.loadRootElement(documentBuilder, process.getInputStream());
      XMLUtil.handleElementsByTagName(rootElement, "key", new XMLUtil.ElementHandler() //$NON-NLS-1$
      {
        @Override
        public void handleElement(Element element) throws Exception
        {
          try
          {
            String text = element.getTextContent();
            if (text != null && "JVMHomePath".equals(text.trim())) //$NON-NLS-1$
            {
              Node siblingNode = element.getNextSibling();
              while (siblingNode != null)
              {
                if (siblingNode instanceof Element)
                {
                  Element sibling = (Element)siblingNode;
                  if ("string".equals(sibling.getNodeName())) //$NON-NLS-1$
                  {
                    String javaHome = sibling.getTextContent();
                    if (javaHome != null)
                    {
                      JREInfo info = new JREInfo();
                      info.javaHome = javaHome;
                      info.jdk = isJDK(new File(javaHome));
                      info.next = jreInfo[0];

                      jreInfo[0] = info;
                    }
                  }

                  break;
                }

                siblingNode = siblingNode.getNextSibling();
              }
            }
          }
          catch (FileNotFoundException ex)
          {
            //$FALL-THROUGH$
          }
          catch (Exception ex)
          {
            JREInfoPlugin.INSTANCE.log(ex);
          }
        }
      });
    }
    catch (Exception ex)
    {
      JREInfoPlugin.INSTANCE.log(ex);
    }

    return jreInfo[0];
  }

  private static JREInfo getAllLinux()
  {
    JREInfo jreInfo = null;
    jreInfo = searchFolder(jreInfo, "/usr/java"); //$NON-NLS-1$
    jreInfo = searchFolder(jreInfo, "/usr/lib/jvm"); //$NON-NLS-1$
    jreInfo = searchFolder(jreInfo, "/usr/lib64"); //$NON-NLS-1$
    jreInfo = searchFolder(jreInfo, "/usr/lib64/jvm"); //$NON-NLS-1$
    return jreInfo;
  }

  private static JREInfo searchFolder(JREInfo jreInfo, String folder)
  {
    File[] children = new File(folder).listFiles();
    if (children != null)
    {
      for (int i = 0; i < children.length; i++)
      {
        try
        {
          File javaHome = children[i];
          int jdk = isJDK(javaHome);

          JREInfo info = new JREInfo();
          info.javaHome = javaHome.getAbsolutePath();
          info.jdk = jdk;
          info.next = jreInfo;

          jreInfo = info;
        }
        catch (Exception ex)
        {
          //$FALL-THROUGH$
        }
      }
    }

    return jreInfo;
  }

  static int isJDK(File javaHome) throws FileNotFoundException
  {
    File binFolder = new File(javaHome, "bin"); //$NON-NLS-1$
    if (!binFolder.isDirectory())
    {
      throw new FileNotFoundException(NLS.bind(Messages.JREInfo_FolderDoesNotExist_exception, binFolder));
    }

    if (new File(binFolder, JREManager.JAVA_COMPILER).isFile())
    {
      return 1;
    }

    return 0;
  }

  private static JREInfo searchRegistry(JREInfo jreInfo, String... args) throws IOException, InterruptedException
  {
    ProcessBuilder builder = new ProcessBuilder(args);
    Process process = builder.start();
    OutputStream stdin = process.getOutputStream();
    stdin.close();

    // Discard error input.
    InputStream stderr = process.getErrorStream();
    new StreamHandler(stderr, line -> {
    }).start();

    Set<File> javaHomes = new LinkedHashSet<File>();
    InputStream stdout = process.getInputStream();
    new StreamHandler(stdout, line -> {
      Matcher matcher = JAVA_HOME_REGISTRY_VALUE_PATTERN.matcher(line);
      if (matcher.matches())
      {
        try
        {
          javaHomes.add(new File(matcher.group(1)).getCanonicalFile());
        }
        catch (IOException ex)
        {
          //$FALL-THROUGH$
        }
      }
    }).start();

    process.waitFor(5, TimeUnit.SECONDS);

    for (File javaHome : javaHomes)
    {
      jreInfo = createJREInfo(jreInfo, javaHome.toString());
    }

    return jreInfo;
  }

  private static class StreamHandler extends Thread
  {
    private final InputStream input;

    private final Consumer<String> consumer;

    public StreamHandler(InputStream input, Consumer<String> consumer)
    {
      this.input = input;
      this.consumer = consumer;
    }

    @Override
    public void run()
    {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(input, IOUtil.getNativeEncoding())))
      {
        reader.lines().forEach(consumer);
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }
    }
  }
}
