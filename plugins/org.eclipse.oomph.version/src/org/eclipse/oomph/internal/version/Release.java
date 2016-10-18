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
package org.eclipse.oomph.internal.version;

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.version.IElement;
import org.eclipse.oomph.version.IRelease;
import org.eclipse.oomph.version.Markers;
import org.eclipse.oomph.version.VersionUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.osgi.framework.Version;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class Release implements IRelease
{
  public static final String RELEASE_TAG = "release";

  public static final String FEATURE_TAG = "feature";

  public static final String PLUGIN_TAG = "plugin";

  public static final String PRODUCT_TAG = "product";

  public static final String NAME_ATTRIBUTE = "name";

  public static final String VERSION_ATTRIBUTE = "version";

  public static final String FRAGMENT_ATTRIBUTE = "fragment";

  public static final String LICENSE_ATTRIBUTE = "license";

  private static final String INDENT = "\t";

  private IFile file;

  private byte[] digest;

  private Map<IElement, IElement> elements = new HashMap<IElement, IElement>();

  public Release(IFile file)
  {
    this.file = file;
  }

  public Release(SAXParser parser, IFile file) throws CoreException, IOException, SAXException, NoSuchAlgorithmException
  {
    this.file = file;

    XMLHandler handler = new XMLHandler();
    InputStream contents = null;

    try
    {
      contents = file.getContents();
      parser.parse(contents, handler);

      if (!handler.hasReleaseTag())
      {
        throw new IOException("Release specification file does not contain a <release> element: " + file.getFullPath());
      }

      digest = VersionUtil.getSHA1(file);
    }
    finally
    {
      IOUtil.closeSilent(contents);
    }
  }

  public IFile getFile()
  {
    return file;
  }

  public byte[] getDigest()
  {
    return digest;
  }

  public Map<IElement, IElement> getElements()
  {
    return elements;
  }

  public IElement resolveElement(IElement key)
  {
    return elements.get(key);
  }

  public int getSize()
  {
    return elements.size();
  }

  public void write() throws IOException, CoreException, NoSuchAlgorithmException
  {
    StringBuilder builder = new StringBuilder();
    writeRelease(builder);

    String xml = builder.toString();
    xml = xml.replace("\n", VersionUtil.getLineDelimiter(file));
    ByteArrayInputStream contents = new ByteArrayInputStream(xml.getBytes("UTF-8"));
    if (file.exists())
    {
      file.setContents(contents, true, true, new NullProgressMonitor());
    }
    else
    {
      file.create(contents, true, new NullProgressMonitor());
    }

    digest = VersionUtil.getSHA1(file);
  }

  private void writeRelease(StringBuilder builder)
  {
    builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    builder.append("<" + RELEASE_TAG + ">\n");

    List<IElement> list = new ArrayList<IElement>(elements.keySet());
    Collections.sort(list, new Comparator<IElement>()
    {
      public int compare(IElement o1, IElement o2)
      {
        int result = o1.getType().compareTo(o2.getType());
        if (result == 0)
        {
          result = StringUtil.safe(o1.getName()).compareTo(StringUtil.safe(o2.getName()));
        }

        if (result == 0)
        {
          result = o1.getVersion().compareTo(o2.getVersion());
        }

        return result;
      }
    });

    for (IElement element : list)
    {
      writeElement(builder, element, INDENT);
    }

    builder.append("</" + RELEASE_TAG + ">\n");
  }

  private void writeElement(StringBuilder builder, IElement element, String indent)
  {
    String name = element.getName();
    Version version = element.getVersion();

    builder
        .append(indent + "<" + element.getTag() + " " + NAME_ATTRIBUTE + "=\"" + name + "\" " + (element.isFragment() ? FRAGMENT_ATTRIBUTE + "=\"true\" " : "")
            + VERSION_ATTRIBUTE + "=\"" + version + "\"" + (element.isLicenseFeature() ? " license=\"true\"" : ""));

    List<IElement> content = element.getChildren();
    if (content.isEmpty())
    {
      builder.append("/");
      writeElementEnd(builder, element);
    }
    else
    {
      writeElementEnd(builder, element);

      for (IElement child : content)
      {
        writeElement(builder, child, indent + INDENT);
      }

      builder.append(indent + "</" + element.getTag() + ">\n");
    }
  }

  private void writeElementEnd(StringBuilder builder, IElement element)
  {
    builder.append(">");
    if (element.getVersion().equals(Version.emptyVersion))
    {
      builder.append(" <!-- UNRESOLVED -->");
    }

    builder.append("\n");
  }

  /**
   * @author Eike Stepper
   */
  public class XMLHandler extends DefaultHandler
  {
    private IElement parent;

    private int level;

    public XMLHandler()
    {
    }

    public boolean hasReleaseTag()
    {
      return level == 1;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      if (RELEASE_TAG.equalsIgnoreCase(qName))
      {
        ++level;
      }
      else if (FEATURE_TAG.equalsIgnoreCase(qName))
      {
        if (level == 0)
        {
          return;
        }

        IElement element = createElement(IElement.Type.FEATURE, attributes);
        if (++level == 2)
        {
          elements.put(element, element);
          parent = element;
        }
        else
        {
          parent.getChildren().add(element);
        }
      }
      else if (PRODUCT_TAG.equalsIgnoreCase(qName))
      {
        if (level == 0)
        {
          return;
        }

        IElement element = createElement(IElement.Type.PRODUCT, attributes);
        if (++level == 2)
        {
          elements.put(element, element);
          parent = element;
        }
        else
        {
          parent.getChildren().add(element);
        }
      }
      else if (PLUGIN_TAG.equalsIgnoreCase(qName))
      {
        if (level == 0)
        {
          return;
        }

        IElement element = createElement(IElement.Type.PLUGIN, attributes);
        if (++level == 2)
        {
          elements.put(element, element);
          parent = element;
        }
        else
        {
          parent.getChildren().add(element);
        }
      }
    }

    private IElement createElement(IElement.Type type, Attributes attributes) throws SAXException
    {
      String name = getString(attributes, NAME_ATTRIBUTE);
      Version version = new Version(getString(attributes, VERSION_ATTRIBUTE));
      boolean isFragment = "true".equals(getString(attributes, FRAGMENT_ATTRIBUTE));
      Element element = new Element(type, name, version, isFragment);

      String license = getString(attributes, LICENSE_ATTRIBUTE);
      if ("true".equals(license))
      {
        element.setLicenseFeature(true);
      }

      return element;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
      if (FEATURE_TAG.equalsIgnoreCase(qName) || PLUGIN_TAG.equalsIgnoreCase(qName) || PRODUCT_TAG.equalsIgnoreCase(qName))
      {
        --level;
      }
    }

    private String getString(Attributes attributes, String name) throws SAXException
    {
      String value = attributes.getValue(name);
      if (value != null || LICENSE_ATTRIBUTE.equals(name) || FRAGMENT_ATTRIBUTE.equals(name))
      {
        return value;
      }

      throw new SAXException("Illegal value for " + name);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException
    {
      addMarker(exception, IMarker.SEVERITY_ERROR);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException
    {
      addMarker(exception, IMarker.SEVERITY_ERROR);
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException
    {
      addMarker(exception, IMarker.SEVERITY_WARNING);
    }

    private void addMarker(SAXParseException e, int severity)
    {
      try
      {
        Markers.addMarker(file, e.getMessage(), severity, e.getLineNumber());
      }
      catch (Exception ex)
      {
        Activator.log(ex);
      }
    }
  }
}
