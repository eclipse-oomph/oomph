/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.doc.user.wizard;

import org.eclipse.oomph.setup.doc.concepts.DocTask.DocTrigger;
import org.eclipse.oomph.setup.internal.installer.InstallerDialog;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import java.util.Collections;

/**
 * Install Wizard
 * <p>
 * The install wizard is the basis for Oomph's bootstrap-{@link DocTrigger triggered}, automated installation and provisioning process.
 * {@link #installer()}
 * </p>
 * <p>
 * {@link #installerFooter()}
 * </p>
 * <p>
 * The following downloads are available for this Eclipse RCP application.
 * <ul>
 * <li>
 * <a rel="nofollow" href="https://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-win32.win32.x86_64.zip">Windows 64 bit</a>
 * </li>
 * <li>
 * <a rel="nofollow" href="https://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-win32.win32.x86.zip">Windows 32 bit</a>
 * </li>
 * <li>
 * <a rel="nofollow" href="https://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-macosx.cocoa.x86_64.tar.gz">Mac OS 64 bit</a>
 * </li>
 * <li>
 * <a rel="nofollow" class="external log" href="https://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-linux.gtk.x86_64.zip">Linux 64 bit</a>
 * </li>
 * <li>
 * <a rel="nofollow" class="external log" href="https://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-linux.gtk.x86.zip">Linux 32 bit</a>
 * </li>
 * </ul>
 * This download can be "installed" just like <a href="https://wiki.eclipse.org/Eclipse/Installation">Eclipse itself</a>.
 * </p>
 *
 * @number 100
 */
@SuppressWarnings("nls")
public abstract class DocInstallWizard
{
  /**
   * @snip image InstallerFooter.images
   * @description The wizards's footer contains the following:
   * @callout
   * {@link #helpDescription()}
   * @callout
   * {@link #backDescription()}
   * @callout
   * {@link #nextDescription()}
   * @callout
   * {@link #finishDescription()}
   * @callout
   * {@link #cancelDescription()}
   * @callout
   * Brings up the equivalent of Eclipse's {@select Window|Preferences...|General|Network Connections} preferences.
   * Configuration of the network proxies is a necessary first step for users working in an environment behind a fire wall;
   * none of Oomph's Internet-hosed resources will be accessible without that configuration.
   * All the configured network settings will be propagated to the installed product.
   * @callout
   * Brings up the equivalent of Eclipse's {@select Window|Preferences...|General|Network Connections|SSH2} preferences.
   * Confirming the SSH2 settings during initial installation is a good idea when using any technologies that are based on SSH access,
   * for example, if you plan to clone Git projects via public-key encryption.
   * All the configured SSH2 settings will be propagated to the installed product.
   * @callout
   * Updates the installer itself to the last Internet-hosted version.
   * The wizard always checks whether updates are available.
   * If so, this button is animated, otherwise it's disabled.
   * @callout
   * Indicates which version, along with which build of that version, you're currently using.
   * This is particularly useful when reporting problems.
   * It acts as a link that brings up the about dialog.
   */
  public static Image[] installerFooter()
  {
    DocInstallWizard.CaptureInstallerWizard instance = DocInstallWizard.CaptureInstallerWizard.getInstance();
    return new Image[] { null, instance.helpImage, instance.backImage, instance.nextImage, instance.finishImage, instance.cancelImage, instance.proxyImage,
        instance.sshImage, instance.updateImage, instance.versionImage };
  }

  /**
   * @ignore
   */
  static class CaptureInstallerWizard extends CaptureSetupWizard
  {
    private static CaptureInstallerWizard instance;

    public Image dialogImage;

    public Image helpImage;

    public Image backImage;

    public Image nextImage;

    public Image finishImage;

    public Image cancelImage;

    public Image proxyImage;

    public Image sshImage;

    public Image updateImage;

    public Image versionImage;

    public Image productPage;

    protected Image refreshImage;

    protected Image collapseImage;

    protected Image catalogsImage;

    protected Image filterImage;

    protected Image versionChoiceImage;

    protected Image poolsImage;

    protected Image poolChoiceImage;

    protected Image managePoolsImage;

    protected Image productVersionImage;

    protected Image productIDImage;

    protected Image productNameImage;

    protected Image treeImageDecoration;

    public static CaptureInstallerWizard getInstance()
    {
      if (instance == null)
      {
        instance = new CaptureInstallerWizard();
        instance.dialogImage = instance.capture();
      }

      return instance;
    }

    @Override
    protected InstallerDialog create(Shell shell)
    {
      return new InstallerDialog(shell, false);
    }

    @Override
    protected void postProcess(WizardDialog wizardDialog)
    {
      super.postProcess(wizardDialog);

      postProcessProductPage(wizardDialog);
    }

    @Override
    protected Image capture(WizardDialog wizardDialog)
    {
      Image result = super.capture(wizardDialog);

      IWizardPage page = wizardDialog.getCurrentPage();

      treeImageDecoration = getCalloutImage(1);
      Control tree = getViewerControl(wizardDialog, "productViewer");
      productPage = capture(page, Collections.singletonMap(tree, instance.treeImageDecoration));

      helpImage = getImage(wizardDialog, "help");

      backImage = getImage(wizardDialog, "back");
      nextImage = getImage(wizardDialog, "next");
      finishImage = getImage(wizardDialog, "finish");
      cancelImage = getImage(wizardDialog, "cancel");

      proxyImage = getImage(wizardDialog, "proxy");
      sshImage = getImage(wizardDialog, "ssh");
      updateImage = getImage(wizardDialog, "update");

      versionImage = getImage(wizardDialog, "version");

      filterImage = getImage(wizardDialog, "filter");

      collapseImage = getImage(wizardDialog, "collapse");
      refreshImage = getImage(wizardDialog, "refresh");
      catalogsImage = getImage(wizardDialog, "catalogs");

      productIDImage = getImage(wizardDialog, "productID");
      productVersionImage = getImage(wizardDialog, "productVersion");
      productNameImage = getImage(wizardDialog, "productName");

      versionChoiceImage = getImage(wizardDialog, "versionChoice");
      poolsImage = getImage(wizardDialog, "pools");
      poolChoiceImage = getImage(wizardDialog, "poolChoice");
      managePoolsImage = getImage(wizardDialog, "managePools");

      return result;
    }
  }

  /**
   * @snip image InstallerWizard.images
   */
  public static Image installer()
  {
    return CaptureInstallerWizard.getInstance().dialogImage;
  }

  /**
   * @snip image WizardFooter.images
   * @description The wizard's footer contain the following controls:
   * @callout
   * {@link #helpDescription()}
   * @callout
   * {@link #backDescription()}
   * @callout
   * {@link #nextDescription()}
   * @callout
   * {@link #finishDescription()}
   * @callout
   * {@link #cancelDescription()}
   */
  public static Image[] wizardFooter()
  {
    CaptureInstallerWizard footer = CaptureInstallerWizard.getInstance();
    return new Image[] { null, footer.helpImage, footer.backImage, footer.nextImage, footer.finishImage, footer.cancelImage };
  }

  /**
   * @snip html
   * @description
   * Brings up this help.
   */
  public abstract void helpDescription();

  /**
   * @snip html
   * @description
   * Navigates to the previous page.
   */
  public abstract void backDescription();

  /**
   * @snip html
   * @description
   * Navigates to the next page.
   */
  public abstract void nextDescription();

  /**
   * @snip html
   * @description
   * Completes the wizard, performing any final actions.
   */
  public abstract void finishDescription();

  /**
   * @snip html
   * @description
   * Closes the wizard, taking no further action.
   */
  public abstract void cancelDescription();
}
