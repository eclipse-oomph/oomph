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
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.ui.ImageURIRegistry;
import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.text.AbstractHoverInformationControlManager;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class ToolTipLabelProvider extends DecoratingColumLabelProvider
{
  private AdapterFactoryItemDelegator itemDelegator;

  private final ColumnViewerInformationControlToolTipSupport toolTipSupport;

  public ToolTipLabelProvider(ComposedAdapterFactory adapterFactory, ColumnViewerInformationControlToolTipSupport toolTipSupport)
  {
    super(new AdapterFactoryLabelProvider(adapterFactory), new ILabelDecorator()
    {
      public void removeListener(ILabelProviderListener listener)
      {
      }

      public boolean isLabelProperty(Object element, String property)
      {
        return true;
      }

      public void dispose()
      {
      }

      public void addListener(ILabelProviderListener listener)
      {
      }

      public String decorateText(String text, Object element)
      {
        return text;
      }

      public Image decorateImage(Image image, Object element)
      {
        return image;
      }
    });

    this.toolTipSupport = toolTipSupport;
    itemDelegator = new AdapterFactoryItemDelegator(adapterFactory);
  }

  @Override
  public String getToolTipText(Object element)
  {
    List<IItemPropertyDescriptor> propertyDescriptors = new ArrayList<IItemPropertyDescriptor>();
    List<IItemPropertyDescriptor> underlyingPropertyDescriptors = itemDelegator.getPropertyDescriptors(element);
    if (underlyingPropertyDescriptors != null)
    {
      propertyDescriptors.addAll(underlyingPropertyDescriptors);
    }

    for (Iterator<IItemPropertyDescriptor> it = propertyDescriptors.iterator(); it.hasNext();)
    {
      IItemPropertyDescriptor itemPropertyDescriptor = it.next();
      Object propertyValue = itemPropertyDescriptor.getPropertyValue(element);

      // Filter out any properties that have no value.
      String valueLabel = itemPropertyDescriptor.getLabelProvider(element).getText(propertyValue);
      if (StringUtil.isEmpty(valueLabel))
      {
        it.remove();
      }
    }

    if (!propertyDescriptors.isEmpty())
    {
      String finalText = renderHTML(propertyDescriptors, element, false);
      try
      {
        AbstractHoverInformationControlManager hoverInformationControlManager = ReflectUtil.getValue("hoverInformationControlManager", toolTipSupport);
        Point size = UIUtil.caclcuateSize(finalText);
        hoverInformationControlManager.setSizeConstraints(size.x, size.y + (propertyDescriptors.size() + 1) / 5 + 1, true, false);
      }
      catch (Throwable throwable)
      {
        // Ignore.
      }

      return finalText;
    }

    return null;
  }

  public static String renderHTML(AdapterFactoryItemDelegator itemDelegator, Object element)
  {
    List<IItemPropertyDescriptor> propertyDescriptors = itemDelegator.getPropertyDescriptors(element);
    if (propertyDescriptors != null)
    {
      return renderHTML(propertyDescriptors, element, false);
    }

    return null;
  }

  public static String renderHTML(List<IItemPropertyDescriptor> propertyDescriptors, Object element, boolean links)
  {
    if (propertyDescriptors.size() > 0)
    {
      StringBuilder result = new StringBuilder();
      result.append("<table style='word-wrap: break-word; word-break: break-all; " + (links ? "" : "margin-top: 3pt; ") + "border-collapse: collapse;'>");
      for (IItemPropertyDescriptor propertyDescriptor : propertyDescriptors)
      {
        result.append("<tr>");

        String displayName = propertyDescriptor.getDisplayName(element);
        result.append("<td style='word-break: keep-all; padding-left: 4pt; padding-right: 4pt; border: 1px solid gray;'>");
        if (links)
        {
          result.append("<a style='text-decoration: none; color: inherit;' href='property:/").append(URI.encodeSegment(displayName, false)).append("'>");
        }

        result.append(DiagnosticDecorator.escapeContent(displayName));

        if (links)
        {
          result.append("</a>");
        }

        result.append("</td>");

        result.append("<td style='padding-right: 4pt; border: 1px solid gray;'>");
        IItemLabelProvider propertyLabelProvider = propertyDescriptor.getLabelProvider(element);
        Object propertyValue = propertyDescriptor.getPropertyValue(element);
        if (propertyValue instanceof IItemPropertySource)
        {
          propertyValue = ((IItemPropertySource)propertyValue).getEditableValue(element);
        }

        if (propertyValue instanceof EList<?>)
        {
          EList<?> propertyValues = (EList<?>)propertyValue;
          boolean needsBreak = false;
          for (Object value : propertyValues)
          {
            if (needsBreak)
            {
              result.append("<br/>\n");

            }
            else
            {
              needsBreak = true;
            }

            renderHTMLPropertyValue(result, propertyLabelProvider, value, links);
          }
        }
        else
        {
          renderHTMLPropertyValue(result, propertyLabelProvider, propertyValue, links);
        }

        result.append("</td>");
        result.append("</tr>\n");
      }
      result.append("</table>");
      return result.toString();
    }

    return null;
  }

  public static void renderHTMLPropertyValue(StringBuilder result, IItemLabelProvider propertyLabelProvider, Object propertyValue, boolean links)
  {
    String href = null;
    if (links)
    {
      Object unwrappedValue = AdapterFactoryEditingDomain.unwrap(propertyValue);
      if (unwrappedValue instanceof EObject)
      {
        EObject eObject = (EObject)unwrappedValue;
        URI uri = EcoreUtil.getURI(eObject);
        if (!uri.isCurrentDocumentReference())
        {
          href = uri.toString();
        }
      }
      else if (unwrappedValue instanceof Resource)
      {
        Resource resource = (Resource)unwrappedValue;
        URI uri = resource.getURI();
        href = uri.toString();
      }

      if (href != null)
      {
        result.append("<a href=\"").append(href).append("\">");
      }
    }

    String valueText = propertyLabelProvider.getText(propertyValue);
    if (valueText.endsWith("...") && propertyValue instanceof String)
    {
      String[] lines = propertyValue.toString().split("\r?\n");
      if (lines.length > 1)
      {
        for (String line : lines)
        {
          result.append(DiagnosticDecorator.escapeContent(line)).append("<br/>");
        }
      }
      else
      {
        result.append(DiagnosticDecorator.escapeContent(valueText));
      }
    }
    else
    {
      Object image = propertyLabelProvider.getImage(propertyValue);
      if (image != null)
      {
        result.append("<img style='margin-bottom: -2px; margin-right: 4px;' src='"
            + ImageURIRegistry.INSTANCE.getImageURI(ExtendedImageRegistry.INSTANCE.getImage(image)) + "'/>");
      }

      if (!StringUtil.isEmpty(valueText))
      {
        result.append(StringUtil.isEmpty(valueText) ? "&nbsp;" : DiagnosticDecorator.escapeContent(valueText));
      }
    }

    if (href != null)
    {
      result.append("<a/>");
    }
  }
}
