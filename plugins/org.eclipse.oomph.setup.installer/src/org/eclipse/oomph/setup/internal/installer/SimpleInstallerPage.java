/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Yatta Solutions - [466264] Enhance UX in simple installer
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.internal.core.util.SelfProductCatalogURIHandlerImpl;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public abstract class SimpleInstallerPage extends Composite
{
  public static final RGB HOVER_RGB = new RGB(175, 187, 220);

  public static final RGB ACTIVE_RGB = new RGB(196, 211, 254);

  public static final Color COLOR_PAGE_BORDER = SetupInstallerPlugin.getColor(238, 238, 238);

  protected static final Font FONT_LABEL = SimpleInstallerDialog.getDefaultFont();

  protected static final Pattern PRODUCT_CATALOG_FILTER = Pattern.compile(PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_PRODUCT_CATALOG_FILTER, ""));

  protected static final Pattern PRODUCT_FILTER = Pattern.compile(PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_PRODUCT_FILTER, ""));

  protected static final Pattern PRODUCT_VERSION_FILTER = Pattern.compile(PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_PRODUCT_VERSION_FILTER, ""));

  protected final Installer installer;

  protected final SimpleInstallerDialog dialog;

  protected final FlatButton backButton;

  public SimpleInstallerPage(final Composite parent, final SimpleInstallerDialog dialog, boolean withBackButton)
  {
    super(parent, SWT.NONE);

    installer = dialog.getInstaller();
    this.dialog = dialog;

    GridLayout layout = new GridLayout(1, false);
    layout.marginWidth = 3;
    layout.marginHeight = 4;
    layout.verticalSpacing = 0;
    setLayout(layout);

    Composite container = new Composite(this, SWT.NONE);
    GridLayout containerLayout = UIUtil.createGridLayout(1);
    containerLayout.verticalSpacing = 0;
    container.setLayout(containerLayout);
    container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
    container.setBackground(COLOR_PAGE_BORDER);

    createContent(container);

    if (withBackButton)
    {
      Composite buttonContainer = new Composite(this, SWT.NONE);
      buttonContainer.setLayout(UIUtil.createGridLayout(1));
      buttonContainer.setBackgroundMode(SWT.INHERIT_FORCE);
      buttonContainer.setBackground(AbstractSimpleDialog.COLOR_WHITE);

      backButton = new BackButton(buttonContainer);
      backButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).indent(15, 0).create());
      backButton.setToolTipText("Back");
      backButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          backSelected();
        }
      });

      Point defaultBackButtonSize = backButton.computeSize(SWT.DEFAULT, SWT.DEFAULT);
      buttonContainer.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, defaultBackButtonSize.y + 11).create());
    }
    else
    {
      backButton = null;
    }
  }

  public ResourceSet getResourceSet()
  {
    return installer.getResourceSet();
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    if (backButton != null)
    {
      backButton.setEnabled(enabled);
    }
  }

  protected Text createTextField(Composite parent)
  {
    Composite textContainer = createInputFieldWrapper(parent, 0, 7, 0, 7);
    applyComboOrTextStyle(textContainer);

    Text textField = new Text(textContainer, SWT.NONE | SWT.SINGLE);
    textField.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, true).create());
    FocusSelectionAdapter focusSelectionAdapter = new FocusSelectionAdapter();
    textField.addFocusListener(focusSelectionAdapter);
    textField.setData(FocusSelectionAdapter.ADAPTER_KEY, focusSelectionAdapter);
    applyComboOrTextStyle(textField);

    return textField;
  }

  protected CCombo createComboBox(Composite parent, int style)
  {
    Composite comboContainer = createInputFieldWrapper(parent, 0, 0, 0, 7);
    applyComboOrTextStyle(comboContainer);

    CCombo combo = new CCombo(comboContainer, style);
    combo.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, true).create());
    FocusSelectionAdapter focusSelectionAdapter = new FocusSelectionAdapter();
    combo.addFocusListener(focusSelectionAdapter);
    combo.setData(FocusSelectionAdapter.ADAPTER_KEY, focusSelectionAdapter);
    applyComboOrTextStyle(combo);

    return combo;
  }

  private Composite createInputFieldWrapper(Composite parent, int marginTop, int marginRight, int marginBottom, int marginLeft)
  {
    GridLayout textContainerLayout = new GridLayout();
    textContainerLayout.marginHeight = 0;
    textContainerLayout.marginWidth = 0;
    textContainerLayout.marginTop = marginTop;
    textContainerLayout.marginRight = marginRight;
    textContainerLayout.marginBottom = marginBottom;
    textContainerLayout.marginLeft = marginLeft;

    Composite textContainer = new Composite(parent, SWT.NONE);
    textContainer.setLayout(textContainerLayout);
    textContainer.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 30).create());
    textContainer.setBackgroundMode(SWT.INHERIT_FORCE);
    return textContainer;
  }

  protected abstract void createContent(Composite container);

  public boolean isTop()
  {
    return dialog.getTopPage() == this;
  }

  public void aboutToShow()
  {
    // Subclasses may override.
  }

  public void aboutToHide()
  {
    // Subclasses may override.
  }

  protected void menuAboutToShow(SimpleInstallerMenu menu)
  {
    // This is called for the top page just before the menu is shown.
    // Subclasses may override.
  }

  protected void applyComboOrTextStyle(Control control)
  {
    control.setFont(SimpleInstallerDialog.getFont(1, "normal"));
    control.setForeground(AbstractSimpleDialog.COLOR_LABEL_FOREGROUND);
    control.setBackground(AbstractSimpleDialog.COLOR_LIGHTEST_GRAY);
  }

  protected Label createLabel(Composite parent, String text)
  {
    Label label = new Label(parent, SWT.NONE);
    label.setLayoutData(GridDataFactory.swtDefaults().create());
    label.setText(text);
    label.setFont(SimpleInstallerDialog.getFont(1, "bold"));
    label.setForeground(AbstractSimpleDialog.COLOR_LABEL_FOREGROUND);
    return label;
  }

  @Override
  protected void checkSubclass()
  {
    // Allow subclassing.
  }

  protected void backSelected()
  {
    dialog.backSelected();
  }

  public static boolean isIncluded(ProductCatalog productCatalog)
  {
    if (productCatalog.eIsProxy())
    {
      return false;
    }

    String name = productCatalog.getName();
    if (SelfProductCatalogURIHandlerImpl.SELF_PRODUCT_CATALOG_NAME.equals(name) || "redirectable".equals(name) || productCatalog.getProducts().isEmpty())
    {
      return false;
    }

    if (name == null || StringUtil.isEmpty(PRODUCT_CATALOG_FILTER.pattern()) || PRODUCT_CATALOG_FILTER.matcher(name).matches())
    {
      for (Product product : productCatalog.getProducts())
      {
        if (isIncluded(product))
        {
          return true;
        }
      }
    }

    return false;
  }

  public static boolean isIncluded(Product product)
  {
    String name = product.getQualifiedName();
    if (name == null || StringUtil.isEmpty(PRODUCT_FILTER.pattern()) || PRODUCT_FILTER.matcher(name).matches())
    {
      return !ProductPage.getValidProductVersions(product, PRODUCT_VERSION_FILTER).isEmpty();
    }

    return false;
  }

  protected static Control spacer(Composite parent)
  {
    return new Label(parent, SWT.NONE);
  }

  public static String hex(RGB color)
  {
    return hex(color.red) + hex(color.green) + hex(color.blue);
  }

  public static String hex(int byteValue)
  {
    String hexString = Integer.toHexString(byteValue);
    if (hexString.length() == 1)
    {
      hexString = "0" + hexString;
    }

    return hexString;
  }

  /**
   * @author Andreas Scharf
   */
  public static final class FocusSelectionAdapter extends FocusAdapter
  {
    public static final String ADAPTER_KEY = "focusSelectionAdapter";

    private Point nextSelectionRange;

    public Point getNextSelectionRange()
    {
      return nextSelectionRange;
    }

    public void setNextSelectionRange(Point nextSelectionRange)
    {
      this.nextSelectionRange = nextSelectionRange;
    }

    @Override
    public void focusLost(FocusEvent e)
    {
      UIUtil.setSelectionToEnd(e.widget);
    }

    @Override
    public void focusGained(FocusEvent e)
    {
      if (nextSelectionRange != null)
      {
        try
        {
          UIUtil.setSelectionTo(e.widget, nextSelectionRange);
        }
        finally
        {
          nextSelectionRange = null;
        }
      }
      else
      {
        UIUtil.selectAllText(e.widget);
      }
    }
  }

  /**
   * @author Andreas Scharf
   */
  private static class BackButton extends ImageHoverButton
  {
    private static final Image ARROW_LEFT = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/arrow_left.png");

    private static final Image ARROW_LEFT_HOVER = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/arrow_left_hover.png");

    private static final Image ARROW_LEFT_DISABLED = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/arrow_left_disabled.png");

    private static final Font FONT = SimpleInstallerDialog.getFont(1, "bold");

    public BackButton(Composite parent)
    {
      super(parent, SWT.PUSH, ARROW_LEFT, ARROW_LEFT_HOVER, ARROW_LEFT_DISABLED);
      setIconTextGap(16);
      setText("BACK");
      setForeground(AbstractSimpleDialog.COLOR_LABEL_FOREGROUND);
      setFont(FONT);
    }
  }
}
