/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.internal.ui.UIPlugin;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
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
    this(parent, style, BaseEditUtil.createAdapterFactory());
  }

  public PropertiesViewer(Composite parent, int style, AdapterFactory adapterFactory)
  {
    super(parent, style | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

    final Table table = getTable();
    UIUtil.applyGridData(table).heightHint = 64;

    setLabelProvider(new DecoratingPropertiesLabelProvider(new PropertiesLabelProvider(), labelDecorator, table));
    setContentProvider(new PropertiesContentProvider(adapterFactory));

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
      private boolean resizing;

      @Override
      public void controlResized(ControlEvent e)
      {
        if (!resizing)
        {
          try
          {
            table.setRedraw(false);
            resizing = true;

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
            resizing = false;
          }
        }
      }
    };

    table.addControlListener(columnResizer);
    UIUtil.asyncExec(table, new Runnable()
    {
      public void run()
      {
        columnResizer.controlResized(null);
      }
    });

    addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        final Object[] element = (Object[])selection.getFirstElement();
        if (element != null && (element[5] != null || element[1] != null))
        {
          final String value = (String)(element[5] == null ? element[1] : element[5]);
          UIUtil.asyncExec(table, new Runnable()
          {
            public void run()
            {
              PopupDialog popupDialog = new InformationPopupDialog(table.getShell(), (String)element[0], value);
              popupDialog.open();
            }
          });
        }
      }
    });
  }

  public DelegatingLabelDecorator getDelegatingLabelDecorator()
  {
    return labelDecorator;
  }

  /**
   * @author Ed Merks
   */
  private final class InformationPopupDialog extends PopupDialog
  {
    private final String content;

    public InformationPopupDialog(Shell parent, String titleText, String content)
    {
      super(parent, PopupDialog.INFOPOPUPRESIZE_SHELLSTYLE, true, true, true, true, false, titleText, null);
      this.content = content;
    }

    @Override
    protected void showDialogMenu()
    {
      super.showDialogMenu();

      // On Linux this listenToDeactivate is set to false when showing the menu, apparently as a workaround for bad behavior in event handling.
      if (Util.isGtk())
      {
        // Set it back to true after the menu is already showing.
        // Otherwise the pop up won't disappear upon losing focus.
        UIUtil.asyncExec(getShell(), new Runnable()
        {
          public void run()
          {
            ReflectUtil.setValue("listenToDeactivate", InformationPopupDialog.this, true);
          }
        });
      }
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
      Composite composite = (Composite)super.createDialogArea(parent);

      StyledText text = new StyledText(composite, SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
      text.setAlwaysShowScrollBars(false);
      Dialog.applyDialogFont(text);

      StringBuilder minimalText = new StringBuilder();
      for (int i = 0; i < 6; ++i)
      {
        for (int j = 0; j < 30; ++j)
        {
          minimalText.append('M');
        }
        minimalText.append('\n');
      }

      text.setText(minimalText.toString());
      Point size = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
      GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
      data.widthHint = size.x;
      data.heightHint = size.y;
      text.setLayoutData(data);

      text.setText(content);

      return composite;
    }

    @Override
    protected IDialogSettings getDialogSettings()
    {
      return UIPlugin.INSTANCE.getDialogSettings("InformationPopup");
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class PropertiesContentProvider implements IStructuredContentProvider
  {
    private final AdapterFactory adapterFactory;

    private final AdapterFactoryItemDelegator itemDelegator;

    public PropertiesContentProvider(AdapterFactory adapterFactory)
    {
      this.adapterFactory = adapterFactory;
      itemDelegator = new AdapterFactoryItemDelegator(adapterFactory);
    }

    public void dispose()
    {
      if (adapterFactory instanceof IDisposable)
      {
        ((IDisposable)adapterFactory).dispose();
      }
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
      UIUtil.asyncExec(viewer.getControl(), new Runnable()
      {
        public void run()
        {
          columnResizer.controlResized(null);
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
          String fullValueText = null;
          Object feature = propertyDescriptor.getFeature(element);
          if (feature instanceof EStructuralFeature)
          {
            EStructuralFeature eStructuralFeature = (EStructuralFeature)feature;
            Object object = AdapterFactoryEditingDomain.unwrap(element);
            if (object instanceof EObject)
            {
              setting = ((InternalEObject)object).eSetting(eStructuralFeature);
            }

            if (propertyValue != null)
            {
              if (eStructuralFeature instanceof EAttribute)
              {
                EAttribute eAttribute = (EAttribute)eStructuralFeature;
                EDataType eDataType = eAttribute.getEAttributeType();
                if (eDataType.isSerializable())
                {
                  if (eAttribute.isMany())
                  {
                    List<?> values = (List<?>)propertyValue;
                    StringBuilder result = new StringBuilder();
                    for (Object item : values)
                    {
                      String stringValue = EcoreUtil.convertToString(eDataType, item);
                      if (stringValue != null)
                      {
                        if (result.length() != 0)
                        {
                          result.append(StringUtil.NL);
                        }

                        result.append(stringValue);
                      }
                    }

                    fullValueText = result.toString();
                  }
                  else
                  {
                    fullValueText = EcoreUtil.convertToString(eDataType, propertyValue);
                  }
                }
              }
            }
          }

          if (isExpertProperty(propertyDescriptor, element))
          {
            expertProperties.add(new Object[] { displayName, valueText, image, true, setting, fullValueText });
          }
          else
          {
            properties.add(new Object[] { displayName, valueText, image, false, setting, fullValueText });
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

  private final class DecoratingPropertiesLabelProvider extends DecoratingColumLabelProvider
      implements ITableLabelProvider, ITableColorProvider, ITableFontProvider
  {
    private PropertiesLabelProvider propertiesLabelProvider;

    private LabelDecorator labelDecorator;

    private Control control;

    public DecoratingPropertiesLabelProvider(PropertiesLabelProvider labelProvider, LabelDecorator labelDecorator, Control control)
    {
      super(labelProvider, labelDecorator);
      propertiesLabelProvider = labelProvider;
      this.labelDecorator = labelDecorator;
      this.control = control;
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
      return labelDecorator.decorateFont(control.getFont(), ((Object[])element)[4]);
    }

    public Color getForeground(Object element, int columnIndex)
    {
      return labelDecorator.decorateForeground(control.getForeground(), ((Object[])element)[4]);
    }

    public Color getBackground(Object element, int columnIndex)
    {
      return labelDecorator.decorateBackground(control.getBackground(), ((Object[])element)[4]);
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
