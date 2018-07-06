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

import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.doc.concepts.DocBundlePool;
import org.eclipse.oomph.setup.doc.concepts.DocInfrastructure.DocIndex;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog.DocProduct;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog.DocProduct.DocVersion;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource.DocInstallationResource;
import org.eclipse.oomph.setup.doc.user.DocBundlePoolManager;

import org.eclipse.swt.graphics.Image;

/**
 * Product Page
 * <p>
 * The primary purpose of the product page is to create an {@linkplain DocInstallationResource installation}
 * with a specific {@link DocVersion product version}.
 * Its secondary purpose is to manage Oomph's {@linkplain DocBundlePool bundle pools}.
 * The product page is used exclusively by the {@linkplain DocInstallWizard installation wizard}.
 * {@link #productPage()}
 * </p>
 *
 * @number 400
 */
public class DocProductPage
{
  /**
   * @snippet image ProductPage.images
   * @style box
   * @description The page contains the following controls:
   * @callout
   * Filters which of the {@link DocProduct products} are displayed.
   * @callout
   * Collapses the tree;
   * initially the tree is fully expanded.
   * @callout
   * Updates the locally-cached versions of all the {@link DocSetupResource resources} used in the wizard.
   * @callout
   * Chooses which of the product catalogs available in the {@link DocIndex index} to display.
   * @callout
   * Displays the {@linkplain CatalogSelection selected} {@linkplain DocProductCatalog product catalogs}.
   * Double-clicking a product automatically advances to the {@linkplain DocProjectPage project page}.
   * @callout
   * Displays the p2 installable unit ID of the selected product.
   * @callout
   * Displays the p2 installable unit name of the selected product.
   * @callout
   * Displays the currently selected product version.
   * @callout
   * Determines which specific version of the available product versions to install.
   * @callout
   * Determines whether the installation will use {@link DocBundlePool bundle pools}.
   * @callout
   * Determines which specific bundle pool to use.
   * @callout
   * Brings up the {@linkplain DocBundlePoolManager bundle pool management} dialog.
   */
  public static Image[] productPage()
  {
    DocInstallWizard.CaptureInstallerWizard instance = DocInstallWizard.CaptureInstallerWizard.getInstance();
    return new Image[] { instance.productPage, instance.filterImage, instance.collapseImage, instance.refreshImage, instance.catalogsImage,
        instance.treeImageDecoration, instance.productIDImage, instance.productNameImage, instance.productVersionImage, instance.versionChoiceImage,
        instance.poolsImage, instance.poolChoiceImage, instance.managePoolsImage };
  }
}
