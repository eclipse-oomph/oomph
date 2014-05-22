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
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class PropertiesViewer extends TableViewer
{
  private TableColumn propertyColumn;

  private TableColumn valueColumn;

  private ControlAdapter columnResizer;

  public PropertiesViewer(Composite parent, int style)
  {
    super(parent, style | SWT.NO_SCROLL | SWT.V_SCROLL);
    setLabelProvider(new PropertiesLabelProvider());
    setContentProvider(new PropertiesContentProvider());

    final Table table = getTable();
    UIUtil.applyGridData(table).heightHint = 64;

    propertyColumn = new TableColumn(table, SWT.NONE);
    propertyColumn.setText("Property");
    propertyColumn.setWidth(200);

    valueColumn = new TableColumn(table, SWT.NONE);
    valueColumn.setText("Value");
    valueColumn.setWidth(400);

    table.setHeaderVisible(true);
    table.setLinesVisible(true);

    columnResizer = new ControlAdapter()
    {
      @Override
      public void controlResized(ControlEvent e)
      {
        Point size = table.getSize();
        ScrollBar bar = table.getVerticalBar();
        if (bar != null && bar.isVisible())
        {
          size.x -= bar.getSize().x;
        }

        valueColumn.setWidth(size.x - propertyColumn.getWidth());
      }
    };

    // TODO Can lead to temporary UI freeze
    // table.addControlListener(columnResizer);
  }

  /**
   * @author Eike Stepper
   */
  private final class PropertiesContentProvider implements IStructuredContentProvider
  {
    private final ComposedAdapterFactory adapterFactory = EMFUtil.createAdapterFactory();

    private final AdapterFactoryItemDelegator itemDelegator = new AdapterFactoryItemDelegator(adapterFactory);

    public void dispose()
    {
      adapterFactory.dispose();
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
      Class<?> oldClass = oldInput == null ? null : oldInput.getClass();
      Class<?> newClass = newInput == null ? null : newInput.getClass();
      if (oldClass != newClass)
      {
        getTable().getDisplay().asyncExec(new Runnable()
        {
          public void run()
          {
            if (!propertyColumn.isDisposed())
            {
              propertyColumn.pack();
              propertyColumn.setWidth(propertyColumn.getWidth() + 20);

              columnResizer.controlResized(null);
            }
          }
        });
      }
    }

    public Object[] getElements(Object element)
    {
      List<Object[]> properties = new ArrayList<Object[]>();
      List<Object[]> expertProperties = new ArrayList<Object[]>();

      List<IItemPropertyDescriptor> propertyDescriptors = itemDelegator.getPropertyDescriptors(element);
      if (propertyDescriptors != null)
      {
        for (IItemPropertyDescriptor propertyDescriptor : propertyDescriptors)
        {
          String displayName = propertyDescriptor.getDisplayName(element);

          IItemLabelProvider propertyLabelProvider = propertyDescriptor.getLabelProvider(element);
          Object propertyValue = propertyDescriptor.getPropertyValue(element);
          Object imageURL = propertyLabelProvider.getImage(propertyValue);
          Image image = imageURL == null ? null : ExtendedImageRegistry.INSTANCE.getImage(imageURL);

          String valueText = propertyLabelProvider.getText(propertyValue);
          if (StringUtil.isEmpty(valueText))
          {
            valueText = "";
          }

          if (isExpertProperty(propertyDescriptor, element))
          {
            expertProperties.add(new Object[] { displayName, valueText, image, true });
          }
          else
          {
            properties.add(new Object[] { displayName, valueText, image, false });
          }
        }
      }

      properties.addAll(expertProperties);
      return properties.toArray();
    }

    private boolean isExpertProperty(IItemPropertyDescriptor propertyDescriptor, Object element)
    {
      String[] filterFlags = propertyDescriptor.getFilterFlags(element);
      if (filterFlags != null)
      {
        for (String filterFlag : filterFlags)
        {
          if (PropertiesUtil.EXPERT_FILTER[0].equals(filterFlag))
          {
            return true;
          }
        }
      }

      return false;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class PropertiesLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider
  {
    public String getColumnText(Object element, int columnIndex)
    {
      return (String)((Object[])element)[columnIndex];
    }

    public Image getColumnImage(Object element, int columnIndex)
    {
      if (columnIndex == 1)
      {
        return (Image)((Object[])element)[2];
      }

      return null;
    }

    public Color getForeground(Object element)
    {
      boolean expert = (Boolean)((Object[])element)[3];
      if (expert)
      {
        return getTable().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
      }

      return null;
    }

    public Color getBackground(Object element)
    {
      return null;
    }
  }
}
