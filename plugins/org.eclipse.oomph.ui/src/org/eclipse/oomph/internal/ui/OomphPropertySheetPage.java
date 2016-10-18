/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetSorter;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;

/**
 * @author Ed Merks
 */
public class OomphPropertySheetPage extends ExtendedPropertySheetPage
{
  private Tree tree;

  private ControlAdapter columnResizer;

  private CopyValuePropertyAction copyPropertyValueAction;

  private Clipboard clipboard;

  private IWorkbenchPart part;

  public OomphPropertySheetPage(AdapterFactoryEditingDomain editingDomain, Decoration decoration, IDialogSettings dialogSettings)
  {
    super(editingDomain, decoration, dialogSettings);

    setSorter(new PropertySheetSorter()
    {
      @Override
      public void sort(IPropertySheetEntry[] entries)
      {
        // Intentionally left empty
      }
    });
  }

  @Override
  public void setRootEntry(IPropertySheetEntry entry)
  {
    getCopyPropertyValueAction();
    super.setRootEntry(entry);
  }

  @Override
  public void createControl(Composite parent)
  {
    super.createControl(parent);
    addColumnResizer();

    Menu menu = getControl().getMenu();
    IMenuManager menuManager = (IMenuManager)menu.getData("org.eclipse.jface.action.MenuManager.managerKey");
    menuManager.insertAfter("copy", getCopyPropertyValueAction());
  }

  private Action getCopyPropertyValueAction()
  {
    if (copyPropertyValueAction == null)
    {
      Shell shell = getControl().getShell();
      clipboard = new Clipboard(shell.getDisplay());
      copyPropertyValueAction = new CopyValuePropertyAction(clipboard);
    }

    return copyPropertyValueAction;
  }

  private void addColumnResizer()
  {
    tree = (Tree)getControl();

    tree.addTreeListener(new TreeListener()
    {
      Runnable runnable = new Runnable()
      {
        public void run()
        {
          resizeColumns();
        }
      };

      public void treeExpanded(TreeEvent e)
      {
        UIUtil.asyncExec(tree, runnable);
      }

      public void treeCollapsed(TreeEvent e)
      {
        UIUtil.asyncExec(tree, runnable);
      }
    });

    final TreeColumn propertyColumn = tree.getColumn(0);
    propertyColumn.setResizable(false);

    final TreeColumn valueColumn = tree.getColumn(1);
    valueColumn.setResizable(false);

    columnResizer = new ControlAdapter()
    {
      private int clientWidth = -1;

      private int propertyWidth = -1;

      private int valueWidth = -1;

      private boolean resizing;

      @Override
      public void controlResized(ControlEvent e)
      {
        if (resizing)
        {
          return;
        }

        Rectangle clientArea = tree.getClientArea();
        int clientWidth = clientArea.width - clientArea.x;
        ScrollBar bar = tree.getVerticalBar();
        if (bar != null && bar.isVisible())
        {
          clientWidth -= bar.getSize().x;
        }

        int propertyWidth = propertyColumn.getWidth();
        int valueWidth = valueColumn.getWidth();

        boolean inputChanged = e == null;
        if (inputChanged || clientWidth != this.clientWidth || propertyWidth != this.propertyWidth || valueWidth != this.valueWidth)
        {
          try
          {
            resizing = true;
            tree.setRedraw(false);

            TreeItem[] items = tree.getItems();
            if (items.length == 0)
            {
              propertyWidth = clientWidth / 2;
              propertyColumn.setWidth(propertyWidth);
              valueColumn.setWidth(clientWidth - propertyWidth);
            }
            else
            {
              propertyColumn.pack();
              propertyWidth = propertyColumn.getWidth() + 20;
              propertyColumn.setWidth(propertyWidth);

              valueColumn.pack();
              valueWidth = valueColumn.getWidth();

              if (propertyWidth + valueWidth < clientWidth)
              {
                valueWidth = clientWidth - propertyWidth;
                valueColumn.setWidth(valueWidth);
              }
            }
          }
          finally
          {
            this.clientWidth = clientWidth;
            this.propertyWidth = propertyWidth;
            this.valueWidth = valueWidth;

            tree.setRedraw(true);
            resizing = false;
          }
        }
      }
    };

    tree.addControlListener(columnResizer);
    propertyColumn.addControlListener(columnResizer);
    valueColumn.addControlListener(columnResizer);
    resizeColumns();
  }

  private void resizeColumns()
  {
    columnResizer.controlResized(null);
  }

  @Override
  public void selectionChanged(IWorkbenchPart part, ISelection selection)
  {
    this.part = part;
    super.selectionChanged(part, selection);
    resizeColumns();
  }

  @Override
  public void handleEntrySelection(ISelection selection)
  {
    super.handleEntrySelection(selection);

    copyPropertyValueAction.selectionChanged((IStructuredSelection)selection);
  }

  @Override
  public <T> T getAdapter(Class<T> adapter)
  {
    if (adapter == IFindReplaceTarget.class)
    {
      return part.getAdapter(adapter);
    }

    return super.getAdapter(adapter);
  }

  @Override
  public void setPropertySourceProvider(IPropertySourceProvider newProvider)
  {
    if (newProvider instanceof AdapterFactoryContentProvider)
    {
      final AdapterFactoryContentProvider adapterFactoryContentProvider = (AdapterFactoryContentProvider)newProvider;
      super.setPropertySourceProvider(new IPropertySourceProvider()
      {
        public IPropertySource getPropertySource(Object object)
        {
          IPropertySource propertySource = adapterFactoryContentProvider.getPropertySource(object);
          if (propertySource instanceof PropertySource && propertySource.getClass() == PropertySource.class)
          {
            return new PropertySource(object, ReflectUtil.<IItemPropertySource> getValue("itemPropertySource", propertySource))
            {
              @Override
              protected IPropertyDescriptor createPropertyDescriptor(IItemPropertyDescriptor itemPropertyDescriptor)
              {
                return new OomphPropertyDescriptor(object, itemPropertyDescriptor);
              }
            };
          }

          return propertySource;
        }
      });
    }
    else
    {
      super.setPropertySourceProvider(newProvider);
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();

    clipboard.dispose();
  }

  /**
   * @author Eike Stepper
   */
  public static class OomphPropertyDescriptor extends PropertyDescriptor
  {
    private static final EDataTypeValueHandler NO_OP_VALUE_HANDLER = new EDataTypeValueHandler(EcorePackage.Literals.ESTRING);

    public OomphPropertyDescriptor(Object object, IItemPropertyDescriptor itemPropertyDescriptor)
    {
      super(object, itemPropertyDescriptor);
    }

    @Override
    protected CellEditor createEDataTypeCellEditor(EDataType eDataType, Composite composite)
    {
      if (itemPropertyDescriptor.isMultiLine(object))
      {
        return new EDataTypeCellEditor(eDataType, composite)
        {
          private Text text;

          private Button button;

          private boolean doEscape;

          private String value;

          private MouseListener mouseListener = new MouseAdapter()
          {
            @Override
            public void mouseUp(MouseEvent e)
            {
              showDialog();
            }

            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
              showDialog();
            }
          };

          private VerifyListener verifyListener = new VerifyListener()
          {
            public void verifyText(VerifyEvent e)
            {
              e.doit = false;
              showDialog();
            }
          };

          private void showDialog()
          {
            button.setFocus();
            UIUtil.asyncExec(new Runnable()
            {
              public void run()
              {
                button.notifyListeners(SWT.Selection, null);
              }
            });
          }

          @Override
          public Object doGetValue()
          {
            String str = value != null ? value : (String)super.doGetValue();

            if (doEscape)
            {
              str = StringUtil.unescape(str);
            }

            return str;
          }

          @Override
          public void doSetValue(Object value)
          {
            text.removeMouseListener(mouseListener);
            text.removeVerifyListener(verifyListener);

            String str = valueHandler.toString(value);
            if (str != null)
            {
              for (char c : str.toCharArray())
              {
                if (Character.isISOControl(c))
                {
                  doEscape = true;

                  str = StringUtil.escape(str);
                  break;
                }
              }
            }

            EDataTypeValueHandler oldValueHandler = valueHandler;
            try
            {
              boolean isVeryLong = str.length() > 2000;
              if (isVeryLong)
              {
                this.value = str;
                str = str.substring(0, 2000);
              }
              else
              {
                this.value = null;
              }

              valueHandler = NO_OP_VALUE_HANDLER;
              super.doSetValue(str);

              if (isVeryLong)
              {
                text.addMouseListener(mouseListener);
                text.addVerifyListener(verifyListener);
              }
            }
            finally
            {
              valueHandler = oldValueHandler;
            }
          }

          @Override
          protected Control createControl(Composite parent)
          {
            GridLayout layout = new GridLayout(2, false);
            layout.marginWidth = 0;
            layout.marginHeight = 0;
            layout.horizontalSpacing = 0;

            final Composite composite = new Composite(parent, SWT.NONE);
            composite.setLayout(layout);
            composite.setFont(parent.getFont());
            composite.setBackground(parent.getBackground());

            text = (Text)super.createControl(composite);
            text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

            button = new Button(composite, SWT.PUSH);
            button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
            button.setText("...");
            button.addSelectionListener(new SelectionAdapter()
            {
              @Override
              public void widgetSelected(SelectionEvent e)
              {
                final String className = "org.eclipse.emf.edit.ui.provider.PropertyDescriptor$MultiLineInputDialog";
                Dialog dialog;

                String stringValue = valueHandler.toString(getValue());
                boolean containsNull = stringValue.indexOf('\u0000') != -1;
                if (containsNull)
                {
                  stringValue = stringValue.replace('\u0000', '\n');
                }

                try
                {
                  Class<?> c = CommonPlugin.loadClass("org.eclipse.emf.edit.ui", className);
                  if (c == null)
                  {
                    throw new Exception("Could not find class " + className);
                  }

                  Constructor<?> constructor = c.getConstructor(Shell.class, String.class, String.class, String.class, IInputValidator.class);
                  if (constructor == null)
                  {
                    throw new Exception("Could not find constructor of " + className);
                  }

                  constructor.setAccessible(true);

                  dialog = (Dialog)constructor.newInstance(composite.getShell(),
                      EMFEditUIPlugin.INSTANCE.getString("_UI_FeatureEditorDialog_title",
                          new Object[] { getDisplayName(), getEditLabelProvider().getText(object) }),
                      EMFEditUIPlugin.INSTANCE.getString("_UI_MultiLineInputDialog_message"), stringValue, valueHandler);

                  if (dialog == null)
                  {
                    throw new Exception("Could not open dialog " + className);
                  }
                }
                catch (Throwable ex)
                {
                  UIPlugin.INSTANCE.log(ex);
                  return;
                }

                int shellStyle = (Integer)ReflectUtil.invokeMethod("getShellStyle", dialog);
                ReflectUtil.invokeMethod(ReflectUtil.getMethod(dialog, "setShellStyle", int.class), dialog, new Integer(shellStyle | SWT.MAX));

                if (dialog.open() == Window.OK)
                {
                  String value = (String)ReflectUtil.invokeMethod("getValue", dialog);

                  value = value.replaceAll("\r\n", "\n");
                  if (containsNull)
                  {
                    value = value.replace('\n', '\u0000');
                  }

                  Object newValue = valueHandler.toValue(value);
                  if (newValue != null)
                  {
                    boolean newValidState = isCorrect(newValue);
                    if (newValidState)
                    {
                      markDirty();
                      doSetValue(newValue);
                      fireApplyEditorValue();
                    }
                    else
                    {
                      // try to insert the current value into the error message.
                      setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { newValue.toString() }));
                    }
                  }
                }
                else
                {
                  fireCancelEditor();
                }
              }
            });

            button.addKeyListener(new KeyAdapter()
            {
              @Override
              public void keyReleased(KeyEvent e)
              {
                if (e.character == '\u001b')
                {
                  fireCancelEditor();
                }
              }
            });

            return composite;
          }

          @Override
          protected void focusLost()
          {
            if (isActivated())
            {
              UIUtil.asyncExec(new Runnable()
              {
                public void run()
                {
                  try
                  {
                    if (button.isFocusControl())
                    {
                      return;
                    }
                  }
                  catch (Exception ex)
                  {
                    //$FALL-THROUGH$
                  }

                  try
                  {
                    fireApplyEditorValue();
                    deactivate();
                  }
                  catch (Exception ex)
                  {
                    //$FALL-THROUGH$
                  }
                }
              });
            }
          }
        };
      }

      return super.createEDataTypeCellEditor(eDataType, composite);
    }
  }

  /**
   * @author Ed Merks
   */
  private static class CopyValuePropertyAction extends Action
  {
    private Clipboard clipboard;

    private IStructuredSelection selection;

    public CopyValuePropertyAction(Clipboard clipboard)
    {
      super("Copy &Value");
      this.clipboard = clipboard;
      setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
    }

    @Override
    public void run()
    {
      if (selection != null && !selection.isEmpty())
      {
        IPropertySheetEntry entry = (IPropertySheetEntry)selection.getFirstElement();
        String value = entry.getValueAsString();
        if (value != null)
        {
          setClipboard(value);
        }
      }
    }

    public void selectionChanged(IStructuredSelection selection)
    {
      this.selection = selection;
      setEnabled(!selection.isEmpty());
    }

    private void setClipboard(String text)
    {
      try
      {
        Object[] data = new Object[] { text };
        Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
        clipboard.setContents(data, transferTypes);
      }
      catch (SWTError ex)
      {
        UIPlugin.INSTANCE.log(ex);
      }
    }
  }
}
