/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.setup.LicenseInfo;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class LicenseDialog extends AbstractConfirmDialog
{
  private static final String LIST_WEIGHT = "ListSashWeight"; //$NON-NLS-1$

  private static final String LICENSE_WEIGHT = "LicenseSashWeight"; //$NON-NLS-1$

  private static final int PRIMARY_COLUMN_WIDTH = 60;

  private static final int COLUMN_WIDTH = 40;

  private static final int TABLE_HEIGHT = 10;

  private final Map<ILicense, List<IInstallableUnit>> licensesToIUs;

  private SashForm sashForm;

  private TreeViewer viewer;

  private Text licenseTextBox;

  public LicenseDialog(Map<ILicense, List<IInstallableUnit>> licensesToIUs)
  {
    super("Licenses", 1000, 600, "Remember accepted licenses");
    this.licensesToIUs = licensesToIUs;
  }

  @Override
  protected String getShellText()
  {
    return "Oomph License Confirmation";
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Review and accept licenses before the software can be installed.";
  }

  @Override
  protected void createUI(Composite parent)
  {
    initializeDialogUnits(parent);

    List<IInstallableUnit> ius;
    Control control;

    if (licensesToIUs.size() == 1 && (ius = licensesToIUs.values().iterator().next()).size() == 1)
    {
      control = createLicenseContentSection(parent, ius.get(0));
    }
    else
    {
      sashForm = new SashForm(parent, SWT.SMOOTH | SWT.HORIZONTAL);
      sashForm.setLayout(new GridLayout());
      GridData gd = new GridData(GridData.FILL_BOTH);
      sashForm.setLayoutData(gd);

      createLicenseListSection(sashForm);
      createLicenseContentSection(sashForm, null);
      sashForm.setWeights(getSashWeights());

      control = sashForm;
    }

    Dialog.applyDialogFont(control);
  }

  private void createLicenseListSection(Composite parent)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    composite.setLayout(layout);
    GridData gd = new GridData(GridData.FILL_BOTH);
    composite.setLayoutData(gd);

    viewer = new TreeViewer(composite, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
    viewer.setContentProvider(new LicenseContentProvider());
    viewer.setLabelProvider(new LicenseLabelProvider());
    viewer.setComparator(new ViewerComparator());
    viewer.setInput(licensesToIUs);

    viewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        handleSelectionChanged((IStructuredSelection)event.getSelection());
      }
    });

    gd = new GridData(GridData.FILL_BOTH);
    gd.widthHint = convertWidthInCharsToPixels(PRIMARY_COLUMN_WIDTH);
    gd.heightHint = convertHeightInCharsToPixels(TABLE_HEIGHT);

    viewer.getControl().setLayoutData(gd);
  }

  private Composite createLicenseContentSection(Composite parent, IInstallableUnit singleIU)
  {
    Composite composite = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    composite.setLayout(layout);
    GridData gd = new GridData(GridData.FILL_BOTH);
    composite.setLayoutData(gd);

    licenseTextBox = new Text(composite, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.READ_ONLY);
    licenseTextBox.setBackground(licenseTextBox.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    initializeDialogUnits(licenseTextBox);
    gd = new GridData(GridData.FILL_BOTH);
    gd.heightHint = convertHeightInCharsToPixels(TABLE_HEIGHT);
    gd.widthHint = convertWidthInCharsToPixels(COLUMN_WIDTH);
    licenseTextBox.setLayoutData(gd);

    if (singleIU != null)
    {
      String licenseBody = ""; //$NON-NLS-1$

      Iterator<ILicense> licenses = singleIU.getLicenses(null).iterator();
      ILicense license = licenses.hasNext() ? licenses.next() : null;
      if (license != null && license.getBody() != null)
      {
        licenseBody = license.getBody();
      }

      licenseTextBox.setText(licenseBody);
    }

    return composite;
  }

  @SuppressWarnings("restriction")
  private void handleSelectionChanged(IStructuredSelection selection)
  {
    if (!selection.isEmpty())
    {
      Object selected = selection.getFirstElement();
      if (selected instanceof org.eclipse.equinox.internal.p2.metadata.License)
      {
        licenseTextBox.setText(((org.eclipse.equinox.internal.p2.metadata.License)selected).getBody());
      }
      else if (selected instanceof IUWithLicenseParent)
      {
        licenseTextBox.setText(((IUWithLicenseParent)selected).license.getBody());
      }
    }
  }

  private int[] getSashWeights()
  {
    try
    {
      int[] weights = new int[2];

      IDialogSettings settings = getDialogSettings();
      if (settings.get(LIST_WEIGHT) != null)
      {
        weights[0] = settings.getInt(LIST_WEIGHT);
        if (settings.get(LICENSE_WEIGHT) != null)
        {
          weights[1] = settings.getInt(LICENSE_WEIGHT);
          return weights;
        }
      }
    }
    catch (NumberFormatException ex)
    {
      // Ignore if there actually was a value that didn't parse.
    }

    return new int[] { 50, 50 };
  }

  private static String getIUName(IInstallableUnit iu)
  {
    StringBuffer buf = new StringBuffer();
    String name = iu.getProperty(IInstallableUnit.PROP_NAME, null);
    if (name != null)
    {
      buf.append(name);
    }
    else
    {
      buf.append(iu.getId());
    }

    buf.append(" "); //$NON-NLS-1$
    buf.append(iu.getVersion().toString());
    return buf.toString();
  }

  /**
   * @author Eike Stepper
   */
  private final class IUWithLicenseParent
  {
    public final IInstallableUnit iu;

    public final ILicense license;

    public IUWithLicenseParent(ILicense license, IInstallableUnit iu)
    {
      this.license = license;
      this.iu = iu;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class LicenseContentProvider implements ITreeContentProvider
  {
    public Object[] getChildren(Object element)
    {
      if (element instanceof ILicense)
      {
        if (licensesToIUs.containsKey(element))
        {
          List<IInstallableUnit> iusWithLicense = licensesToIUs.get(element);
          IInstallableUnit[] ius = iusWithLicense.toArray(new IInstallableUnit[iusWithLicense.size()]);
          IUWithLicenseParent[] children = new IUWithLicenseParent[ius.length];
          for (int i = 0; i < ius.length; i++)
          {
            children[i] = new IUWithLicenseParent((ILicense)element, ius[i]);
          }

          return children;
        }
      }

      return new Object[0];
    }

    public Object getParent(Object element)
    {
      if (element instanceof IUWithLicenseParent)
      {
        return ((IUWithLicenseParent)element).license;
      }

      return null;
    }

    public boolean hasChildren(Object element)
    {
      return licensesToIUs.containsKey(element);
    }

    public Object[] getElements(Object inputElement)
    {
      return licensesToIUs.keySet().toArray();
    }

    public void dispose()
    {
      // Nothing to do
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
      // Nothing to do
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class LicenseLabelProvider extends LabelProvider
  {
    @Override
    @SuppressWarnings("restriction")
    public Image getImage(Object element)
    {
      if (element instanceof org.eclipse.equinox.internal.p2.metadata.License)
      {
        return SetupUIPlugin.INSTANCE.getSWTImage("license");
      }

      if (element instanceof IUWithLicenseParent || element instanceof IInstallableUnit)
      {
        return SetupUIPlugin.INSTANCE.getSWTImage("feature");
      }

      return null;
    }

    @Override
    @SuppressWarnings("restriction")
    public String getText(Object element)
    {
      if (element instanceof org.eclipse.equinox.internal.p2.metadata.License)
      {
        return LicenseInfo.getFirstLine(((org.eclipse.equinox.internal.p2.metadata.License)element).getBody());
      }

      if (element instanceof IUWithLicenseParent)
      {
        return getIUName(((IUWithLicenseParent)element).iu);
      }

      if (element instanceof IInstallableUnit)
      {
        return getIUName((IInstallableUnit)element);
      }

      return ""; //$NON-NLS-1$
    }
  }
}
