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

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

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

  private DelegatingLabelDecorator labelDecorator = new DelegatingLabelDecorator();

  public PropertiesViewer(Composite parent, int style)
  {
    super(parent, style | SWT.H_SCROLL | SWT.V_SCROLL);

    final Table table = getTable();
    UIUtil.applyGridData(table).heightHint = 64;

    setLabelProvider(new DecoratingPropertiesLabelProvider(new PropertiesLabelProvider(), labelDecorator, table.getFont(), table.getBackground(),
        table.getForeground()));
    setContentProvider(new PropertiesContentProvider());

    propertyColumn = new TableColumn(table, SWT.NONE);
    propertyColumn.setText("Property");
    propertyColumn.setWidth(200);
    propertyColumn.setResizable(false);

    valueColumn = new TableColumn(table, SWT.NONE);
    valueColumn.setText("Value");
    valueColumn.setWidth(400);
    valueColumn.setResizable(false);

    table.setHeaderVisible(true);
    table.setLinesVisible(true);

    columnResizer = new ControlAdapter()
    {
      @Override
      public void controlResized(ControlEvent e)
      {
        try
        {
          table.setRedraw(false);

          Rectangle clientArea = table.getClientArea();
          int clientWidth = clientArea.width - clientArea.x;

          TableItem[] items = table.getItems();
          if (items.length == 0)
          {
            propertyColumn.setWidth(clientWidth / 2);
            valueColumn.setWidth(clientWidth - clientWidth / 2);
          }
          else
          {
            propertyColumn.pack();
            int propertyColumnWidth = propertyColumn.getWidth();
            propertyColumn.setWidth(propertyColumnWidth += 20);

            valueColumn.pack();
            int valueColumnWidth = valueColumn.getWidth();

            if (propertyColumnWidth + valueColumnWidth < clientWidth)
            {
              valueColumn.setWidth(clientWidth - propertyColumnWidth);
            }
          }
        }
        finally
        {
          table.setRedraw(true);
        }
      }
    };

    table.addControlListener(columnResizer);
    table.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        columnResizer.controlResized(null);
      }
    });
  }

  public DelegatingLabelDecorator getDelegatingLabelDecorator()
  {
    return labelDecorator;
  }

  /**
   * @author Eike Stepper
   */
  private final class PropertiesContentProvider implements IStructuredContentProvider
  {
    private final ComposedAdapterFactory adapterFactory = BaseEditUtil.createAdapterFactory();

    private final AdapterFactoryItemDelegator itemDelegator = new AdapterFactoryItemDelegator(adapterFactory);

    public void dispose()
    {
      adapterFactory.dispose();
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
      final Control control = viewer.getControl();
      control.getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          if (!control.isDisposed())
          {
            columnResizer.controlResized(null);
          }
        }
      });
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
          Object propertyValue = itemDelegator.getEditableValue(propertyDescriptor.getPropertyValue(element));
          Object imageURL = propertyLabelProvider.getImage(propertyValue);
          Image image = imageURL == null ? null : ExtendedImageRegistry.INSTANCE.getImage(imageURL);

          String valueText = propertyLabelProvider.getText(propertyValue);
          if (StringUtil.isEmpty(valueText))
          {
            valueText = "";
          }

          EStructuralFeature.Setting setting = null;
          Object feature = propertyDescriptor.getFeature(element);
          if (feature instanceof EStructuralFeature)
          {
            Object object = AdapterFactoryEditingDomain.unwrap(element);
            if (object instanceof EObject)
            {
              setting = ((InternalEObject)object).eSetting((EStructuralFeature)feature);
            }
          }

          if (isExpertProperty(propertyDescriptor, element))
          {
            expertProperties.add(new Object[] { displayName, valueText, image, true, setting });
          }
          else
          {
            properties.add(new Object[] { displayName, valueText, image, false, setting });
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

  private final class DecoratingPropertiesLabelProvider extends DecoratingColumLabelProvider implements ITableLabelProvider, ITableColorProvider,
      ITableFontProvider
  {
    private PropertiesLabelProvider propertiesLabelProvider;

    private LabelDecorator labelDecorator;

    private Font font;

    private Color background;

    private Color foreground;

    public DecoratingPropertiesLabelProvider(PropertiesLabelProvider labelProvider, LabelDecorator labelDecorator, Font font, Color background, Color foreground)
    {
      super(labelProvider, labelDecorator);
      propertiesLabelProvider = labelProvider;
      this.labelDecorator = labelDecorator;
      this.font = font;
      this.background = background;
      this.foreground = foreground;
    }

    public Image getColumnImage(Object element, int columnIndex)
    {
      return propertiesLabelProvider.getColumnImage(element, columnIndex);
    }

    public String getColumnText(Object element, int columnIndex)
    {
      return propertiesLabelProvider.getColumnText(element, columnIndex);
    }

    public Font getFont(Object element, int columnIndex)
    {
      return labelDecorator.decorateFont(font, ((Object[])element)[4]);
    }

    public Color getForeground(Object element, int columnIndex)
    {
      return labelDecorator.decorateForeground(foreground, ((Object[])element)[4]);
    }

    public Color getBackground(Object element, int columnIndex)
    {
      return labelDecorator.decorateBackground(background, ((Object[])element)[4]);
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class PropertiesLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public PropertiesLabelProvider()
    {
    }

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
  }
}
