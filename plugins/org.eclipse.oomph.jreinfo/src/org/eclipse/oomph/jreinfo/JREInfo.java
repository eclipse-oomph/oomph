/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.jreinfo;

import org.eclipse.oomph.internal.jreinfo.JREInfoPlugin;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.XMLUtil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author Stepper
 */
public final class JREInfo
{
  private static final boolean SKIP_USER_HOME = PropertiesUtil.isProperty("oomph.jreinfo.skip.user.home");

  private static final String[] EXTRA_SEARCH_PATH = PropertiesUtil.getProperty("oomph.jreinfo.extra.search.path", "").split(File.pathSeparator);

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

      String javaHome = System.getProperty("java.home");
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

          jreInfo = info;
        }
      }

      if (!SKIP_USER_HOME)
      {
        String userHome = PropertiesUtil.getUserHome();
        jreInfo = searchFolder(jreInfo, userHome);
        jreInfo = searchFolder(jreInfo, userHome + "/java");
        jreInfo = searchFolder(jreInfo, userHome + "/jvm");
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

  private static native JREInfo getAllWin();

  private static JREInfo getAllMac()
  {
    final JREInfo[] jreInfo = { null };

    try
    {
      ProcessBuilder builder = new ProcessBuilder();
      builder.command("/usr/libexec/java_home", "-X");

      Process process = builder.start();

      DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
      documentBuilder.setEntityResolver(new EntityResolver()
      {
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
        {
          return new InputSource(new StringReader(""));
        }
      });

      Element rootElement = XMLUtil.loadRootElement(documentBuilder, process.getInputStream());
      XMLUtil.handleElementsByTagName(rootElement, "key", new XMLUtil.ElementHandler()
      {
        public void handleElement(Element element) throws Exception
        {
          try
          {
            String text = element.getTextContent();
            if (text != null && "JVMHomePath".equals(text.trim()))
            {
              Node siblingNode = element.getNextSibling();
              while (siblingNode != null)
              {
                if (siblingNode instanceof Element)
                {
                  Element sibling = (Element)siblingNode;
                  if ("string".equals(sibling.getNodeName()))
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
    jreInfo = searchFolder(jreInfo, "/usr/java");
    jreInfo = searchFolder(jreInfo, "/usr/lib/jvm");
    jreInfo = searchFolder(jreInfo, "/usr/lib64");
    jreInfo = searchFolder(jreInfo, "/usr/lib64/jvm");
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
    File binFolder = new File(javaHome, "bin");
    if (!binFolder.isDirectory())
    {
      throw new FileNotFoundException("Folder does not exist: " + binFolder);
    }

    if (new File(binFolder, JREManager.JAVA_COMPILER).isFile())
    {
      return 1;
    }

    return 0;
  }
}
