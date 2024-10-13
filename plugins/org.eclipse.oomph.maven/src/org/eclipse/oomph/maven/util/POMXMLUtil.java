/*
 * Copyright (c) 2024 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.util;

import org.eclipse.oomph.maven.util.MavenValidator.ElementEdit;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.emf.common.util.SegmentSequence;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class POMXMLUtil
{
  private static final String USER_DATA_LOCATION_KEY = "location"; //$NON-NLS-1$

  private static final String USER_DATA_PARENT_ELEMENT_KEY = "parent"; //$NON-NLS-1$

  private static final DocumentBuilder DOCUMENT_BUILDER;

  static
  {
    try
    {
      DOCUMENT_BUILDER = XMLUtil.createDocumentBuilder();
    }
    catch (ParserConfigurationException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  public static final Document parseDocument(Path path) throws IOException, SAXException
  {
    try (InputStream inputStream = Files.newInputStream(path))
    {
      Document document = DOCUMENT_BUILDER.parse(inputStream);
      document.setUserData(USER_DATA_LOCATION_KEY, path, null);
      return document;
    }
  }

  public static final Path getLocation(Element element)
  {
    return getLocation(element.getOwnerDocument());
  }

  public static final Path getLocation(Document document)
  {
    return (Path)document.getUserData(USER_DATA_LOCATION_KEY);
  }

  private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();

  private static XPath newXPath(Document document)
  {
    XPath xPath = XPATH_FACTORY.newXPath();
    xPath.setNamespaceContext(new NamespaceContext()
    {
      @Override
      public String getNamespaceURI(String prefix)
      {
        if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX))
        {
          return document.lookupNamespaceURI(null);
        }

        String result = document.lookupNamespaceURI(prefix);
        if (result == null)
        {
          result = document.lookupNamespaceURI(null);
        }

        return result;
      }

      @Override
      public Iterator<String> getPrefixes(String val)
      {
        return null;
      }

      @Override
      public String getPrefix(String namespaceURI)
      {
        return document.lookupPrefix(namespaceURI);
      }
    });

    return xPath;
  }

  private static final Pattern ELEMENT_NAME_PATTERN = Pattern.compile("^[a-zA-Z]+$"); //$NON-NLS-1$

  private static final String XPATH_DELIMITER = "/"; //$NON-NLS-1$

  private static final String POM_PREFIX_REPLACEMENT = "pom:$0"; //$NON-NLS-1$

  public static SegmentSequence xpath(String... segments)
  {
    return SegmentSequence.create(XPATH_DELIMITER,
        Stream.of(segments).map(segment -> ELEMENT_NAME_PATTERN.matcher(segment).replaceAll(POM_PREFIX_REPLACEMENT)).toArray(String[]::new));
  }

  public static Element getElement(Node node, SegmentSequence xpath)
  {
    List<Element> elements = getElements(node, xpath);
    return elements.isEmpty() ? null : elements.get(0);
  }

  public static List<Element> getElements(Node node)
  {
    return getElements(node, POMXMLUtil.xpath("*")); //$NON-NLS-1$
  }

  public static List<Element> getElements(Node node, SegmentSequence xpath)
  {
    XPath xPath = newXPath(node.getOwnerDocument());

    try
    {
      var nodeList = (NodeList)xPath.compile(xpath.toString()).evaluate(node, XPathConstants.NODESET);
      var result = new ArrayList<Element>();
      for (int i = 0, length = nodeList.getLength(); i < length; ++i)
      {
        Node item = nodeList.item(i);
        if (item instanceof Element element)
        {
          result.add(element);
        }
      }

      return result;
    }
    catch (XPathExpressionException e)
    {
      throw new IllegalArgumentException(xpath.toString());
    }

  }

  // private static String evaluateText(Node node, String expression)
  // {
  // XPath xPath = newXPath(node.getOwnerDocument());
  //
  // try
  // {
  // var value = (String)xPath.compile(expression).evaluate(node, XPathConstants.STRING);
  // return value;
  // }
  // catch (XPathExpressionException e)
  // {
  // throw new IllegalArgumentException(expression);
  // }
  // }

  public static String toString(Document document) throws TransformerException
  {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();

    StringWriter out = new StringWriter();
    transformer.transform(new DOMSource(document), new StreamResult(out));
    return out.toString();
  }

  private static final String START_MARKER = "\u00ab"; //$NON-NLS-1$

  private static final String END_MARKER = "\u00bb"; //$NON-NLS-1$

  private static Pattern START_ELEMENT_PATTERN = Pattern.compile(START_MARKER + "<([^ \r\n\t/>]+)"); //$NON-NLS-1$

  public static record TextRegion(int start, int end) implements Comparable<TextRegion>
  {
    public int length()
    {
      return end - start;
    }

    @Override
    public int compareTo(TextRegion o)
    {
      int result = Integer.compare(start, o.start);
      if (result == 0)
      {
        result = Integer.compare(end, o.end);
      }
      return result;
    }
  }

  public static Charset getEncoding(Document document)
  {
    String xmlEncoding = document.getXmlEncoding();
    return xmlEncoding == null ? StandardCharsets.UTF_8 : Charset.forName(xmlEncoding);
  }

  public static String getLocationContent(Document document)
  {
    Path location = getLocation(document);
    if (location != null)
    {
      try
      {
        String originalContent = Files.readString(location, getEncoding(document));
        return originalContent;
      }
      catch (IOException ex)
      {
        //$FALL-THROUGH$
      }
    }

    return null;
  }

  public static Element createChildElement(Element parent, String elementName)
  {
    Element element = parent.getOwnerDocument().createElement(elementName);
    element.setUserData(USER_DATA_PARENT_ELEMENT_KEY, parent, null);
    return element;
  }

  private static Element getParent(Node node)
  {
    return (Element)node.getUserData(USER_DATA_PARENT_ELEMENT_KEY);
  }

  @SuppressWarnings("nls")
  public static TextRegion getSelection(Element element)
  {
    if (element != null)
    {
      Document document = element.getOwnerDocument();
      String originalContent = getLocationContent(document);
      if (originalContent != null)
      {
        Node parentNode = element.getParentNode();
        if (parentNode == null)
        {
          TextRegion selection = getSelection(getParent(element));
          if (selection != null)
          {
            int elementStart = originalContent.lastIndexOf('<', selection.end);
            if (elementStart != -1)
            {
              int previousElementEnd = originalContent.lastIndexOf('>', elementStart);
              if (previousElementEnd != -1)
              {
                // Select the whitespace in which the element should be inserted.
                return new TextRegion(previousElementEnd + 1, elementStart);
              }
            }
          }
          return selection;
        }

        Text startMarker = document.createTextNode(START_MARKER);
        parentNode.insertBefore(startMarker, element);
        Text endMarker = document.createTextNode(END_MARKER);
        parentNode.insertBefore(endMarker, element.getNextSibling());

        try
        {
          String serializedContent = POMXMLUtil.toString(document);

          int start = serializedContent.indexOf('\u00ab');
          int end = serializedContent.indexOf('\u00bb');
          if (start != -1 && end != -1)
          {
            Matcher matcher = START_ELEMENT_PATTERN.matcher(serializedContent);
            if (matcher.find(start))
            {
              String elementName = matcher.group(1);
              Pattern targetPattern = Pattern.compile("<" + elementName);
              int count = 0;
              for (Matcher matcher2 = targetPattern.matcher(serializedContent); matcher2.find();)
              {
                count++;
                if (matcher2.start() >= start)
                {
                  break;
                }
              }

              int count2 = 0;
              for (Matcher matcher2 = targetPattern.matcher(originalContent); matcher2.find();)
              {
                count2++;
                if (count2 == count)
                {
                  int endElement = originalContent.indexOf('>', matcher2.end(0));
                  if (originalContent.charAt(endElement - 1) != '/')
                  {
                    String endElementContent = "</" + elementName + ">";
                    int indexOf = originalContent.indexOf(endElementContent, endElement);
                    if (indexOf != -1)
                    {
                      return new TextRegion(matcher2.start(), indexOf + endElementContent.length());
                    }
                  }

                  return new TextRegion(matcher2.start(), endElement + 1);
                }
              }
            }
          }
        }
        catch (TransformerException ex)
        {
          //$FALL-THROUGH$
        }
        finally
        {
          parentNode.removeChild(startMarker);
          parentNode.removeChild(endMarker);
        }
      }
    }

    return null;
  }

  public static Map<Document, String> applyElementEdits(Map<Document, Map<TextRegion, ElementEdit>> elementEdits)
  {
    Map<Document, String> result = new LinkedHashMap<>();
    for (Map.Entry<Document, Map<TextRegion, ElementEdit>> entry : elementEdits.entrySet())
    {
      Document document = entry.getKey();
      String content = POMXMLUtil.getLocationContent(document);
      if (content != null)
      {
        int index = 0;
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<TextRegion, ElementEdit> editEntry : new TreeMap<>(entry.getValue()).entrySet())
        {
          TextRegion textRegion = editEntry.getKey();
          builder.append(content, index, textRegion.start());
          index = textRegion.end();

          String replacedContent = content.substring(textRegion.start(), index);
          String indent = ""; //$NON-NLS-1$
          String nestedIdent = ""; //$NON-NLS-1$
          if (replacedContent.isBlank())
          {
            int newLineIndex = replacedContent.lastIndexOf('\n');
            if (newLineIndex != -1)
            {
              indent = replacedContent;
              if (indent.indexOf('\t') != -1)
              {
                nestedIdent = indent + '\t';
              }
              else
              {
                nestedIdent = indent + "  "; //$NON-NLS-1$
              }
            }
          }

          ElementEdit elementEdit = editEntry.getValue();
          Element element = elementEdit.element();
          String elementName = element.getNodeName();
          builder.append(nestedIdent).append('<').append(elementName).append('>');
          builder.append(elementEdit.value());
          builder.append("</").append(elementName).append('>').append(indent); //$NON-NLS-1$
        }

        builder.append(content.substring(index));

        result.put(document, builder.toString());
      }
    }

    return result;
  }
}
