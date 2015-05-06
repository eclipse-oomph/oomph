/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
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

import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.Installer;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Eike Stepper
 */
public abstract class SimpleInstallerPage extends Composite
{
  public static final RGB HOVER_RGB = new RGB(175, 187, 220);

  public static final RGB ACTIVE_RGB = new RGB(196, 211, 254);

  public static final Color COLOR_PAGE_BORDER = SetupInstallerPlugin.getColor(238, 238, 238);

  protected static final Font FONT_LABEL = SimpleInstallerDialog.getDefaultFont();

  protected final Installer installer;

  protected final SimpleInstallerDialog dialog;

  private final FlatButton backButton;

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
      buttonContainer.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 33).create());
      buttonContainer.setBackgroundMode(SWT.INHERIT_FORCE);
      buttonContainer.setBackground(SetupInstallerPlugin.COLOR_WHITE);

      backButton = new BackButton(buttonContainer);
      backButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).indent(15, 0).create());
      backButton.setToolTipText("Back");
      backButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          dialog.backSelected();
        }
      });
    }
    else
    {
      backButton = null;
    }
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
    applyComboOrTextStyle(textField);

    return textField;
  }

  protected CCombo createComboBox(Composite parent, int style)
  {
    Composite comboContainer = createInputFieldWrapper(parent, 0, 0, 0, 7);
    applyComboOrTextStyle(comboContainer);

    CCombo combo = new CCombo(comboContainer, style);
    combo.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, true).create());
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

  public void aboutToShow()
  {
    // Subclasses may implement.
  }

  public void aboutToHide()
  {
    // Subclasses may implement.
  }

  protected void applyComboOrTextStyle(Control control)
  {
    control.setFont(SetupInstallerPlugin.getFont(FONT_LABEL, URI.createURI("font:///10/normal")));
    control.setForeground(SetupInstallerPlugin.COLOR_LABEL_FOREGROUND);
    control.setBackground(SetupInstallerPlugin.COLOR_LIGHTEST_GRAY);
  }

  protected Label createLabel(Composite parent, String text)
  {
    Label label = new Label(parent, SWT.NONE);
    label.setLayoutData(GridDataFactory.swtDefaults().create());
    label.setText(text);
    label.setFont(FONT_LABEL);
    label.setForeground(SetupInstallerPlugin.COLOR_LABEL_FOREGROUND);
    return label;
  }

  @Override
  protected void checkSubclass()
  {
    // Allow subclassing.
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
  private static class BackButton extends ImageHoverButton
  {
    private static final Image ARROW_LEFT = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/arrow_left.png");

    private static final Image ARROW_LEFT_HOVER = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/arrow_left_hover.png");

    private static final Image ARROW_LEFT_DISABLED = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/arrow_left_disabled.png");

    private static final Font FONT = SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///10/bold"));

    public BackButton(Composite parent)
    {
      super(parent, SWT.PUSH, ARROW_LEFT, ARROW_LEFT_HOVER, ARROW_LEFT_DISABLED);
      setIconTextGap(16);
      setText("BACK");
      setForeground(SetupInstallerPlugin.COLOR_LABEL_FOREGROUND);
      setFont(FONT);
    }
  }
}
