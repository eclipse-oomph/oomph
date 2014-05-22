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
package org.eclipse.oomph.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public final class XMLUtil
{
  private XMLUtil()
  {
  }

  public static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException
  {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setNamespaceAware(true);
    documentBuilderFactory.setValidating(false);
    return documentBuilderFactory.newDocumentBuilder();
  }

  public static Element loadRootElement(DocumentBuilder documentBuilder, File file) throws Exception
  {
    Document document = loadDocument(documentBuilder, file);
    return document.getDocumentElement();
  }

  public static Document loadDocument(DocumentBuilder documentBuilder, File file) throws SAXException, IOException
  {
    return documentBuilder.parse(file);
  }

  public static int handleElements(NodeList nodeList, ElementHandler handler) throws Exception
  {
    int count = 0;
    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node node = nodeList.item(i);
      if (node instanceof Element)
      {
        Element element = (Element)node;
        handler.handleElement(element);
        ++count;
      }
    }

    return count;
  }

  public static int handleChildElements(Element rootElement, ElementHandler handler) throws Exception
  {
    NodeList childNodes = rootElement.getChildNodes();
    return handleElements(childNodes, handler);
  }

  public static int handleElementsByTagName(Element rootElement, String tagName, ElementHandler handler) throws Exception
  {
    int count = 0;

    NodeList nodeList = rootElement.getElementsByTagName(tagName);
    count += handleElements(nodeList, handler);

    int pos = tagName.indexOf(':');
    if (pos != -1)
    {
      tagName = tagName.substring(pos + 1);
      nodeList = rootElement.getElementsByTagName(tagName);
      count += handleElements(nodeList, handler);
    }

    return count;
  }

  /**
   * @author Eike Stepper
   */
  public interface ElementHandler
  {
    public void handleElement(Element element) throws Exception;
  }
}
