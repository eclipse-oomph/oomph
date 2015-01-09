/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.core.runtime.Platform;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Stepper
 */
public final class JREInfo
{
  private static final OSType OS_TYPE = getOSType();

  private static final String JAVA_COMPILER = OS_TYPE == OSType.Win ? "javac.exe" : "javac";

  public String javaHome;

  public int jdk;

  public JREInfo next;

  public static JREInfo getAll()
  {
    switch (OS_TYPE)
    {
      case Win:
        return getAllWin();

      case Mac:
        return getAllMac();

      case Linux:
        return getAllLinux();

      default:
        //$FALL-THROUGH$
    }

    return null;
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
                      info.jdk = isJDK(javaHome);
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
    // TODO Auto-generated method stub
    return null;
  }

  private static OSType getOSType()
  {
    String os = Platform.getOS();

    if (Platform.OS_WIN32.equals(os))
    {
      System.loadLibrary("jreinfo.dll");
      return OSType.Win;
    }

    if (Platform.OS_MACOSX.equals(os))
    {
      return OSType.Mac;
    }

    if (Platform.OS_LINUX.equals(os))
    {
      return OSType.Linux;
    }

    return OSType.Unsupported;
  }

  private static int isJDK(String javaHome) throws FileNotFoundException
  {
    File binFolder = new File(javaHome, "bin");
    if (!binFolder.isDirectory())
    {
      throw new FileNotFoundException("Folder does not exist: " + binFolder);
    }

    if (new File(binFolder, JAVA_COMPILER).isFile())
    {
      return 1;
    }

    return 0;
  }
}
