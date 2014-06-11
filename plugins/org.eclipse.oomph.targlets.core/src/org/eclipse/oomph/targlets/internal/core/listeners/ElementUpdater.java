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
package org.eclipse.oomph.targlets.internal.core.listeners;

import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.XMLUtil;

import org.w3c.dom.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public final class ElementUpdater
{
  private final String tagName;

  private String textValue;

  private int index;

  public ElementUpdater(final Element rootElement, String tagName) throws Exception
  {
    this.tagName = tagName;
    XMLUtil.handleElementsByTagName(rootElement, tagName, new XMLUtil.ElementHandler()
    {
      public void handleElement(Element element) throws Exception
      {
        if (element.getParentNode() == rootElement)
        {
          textValue = element.getTextContent();
        }

        if (textValue == null)
        {
          ++index;
        }
      }
    });
  }

  public String update(String text, String newValue)
  {
    if (!ObjectUtil.equals(textValue, newValue))
    {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < index; i++)
      {
        builder.append(".*?<" + tagName + ">.*?</" + tagName + ">");
      }

      builder.append(".*?<" + tagName + ">(.*?)</" + tagName + ">.*");

      Pattern pattern = Pattern.compile(builder.toString(), Pattern.DOTALL);
      Matcher matcher = pattern.matcher(text);
      if (matcher.matches())
      {
        text = text.substring(0, matcher.start(1)) + newValue + text.substring(matcher.end(1));
      }
    }

    return text;
  }
}
