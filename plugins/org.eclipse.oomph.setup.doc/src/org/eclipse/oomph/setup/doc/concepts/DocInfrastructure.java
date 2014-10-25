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
package org.eclipse.oomph.setup.doc.concepts;

import org.eclipse.oomph.setup.doc.concepts.DocScope.DocInstallation;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog.DocProduct;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog.DocProduct.DocVersion;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog.DocProject;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog.DocProject.DocStream;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocWorkspace;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource.DocCatalogSelectionResource;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource.DocInstallationResource;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource.DocLocationResource;
import org.eclipse.oomph.setup.doc.user.wizard.DocProductPage;
import org.eclipse.oomph.setup.doc.user.wizard.DocProjectPage;

/**
 * Infrastructure
 * <p>
 * In addition to {@linkplain DocScope scopes} there are several other important structural elements for organizing and maintaining setup information.
 * </p>
 *
 * @number 300
 */
public abstract class DocInfrastructure
{
  /**
   * Index
   * <p>
   * An {@link org.eclipse.oomph.setup.Index index} is a {@link org.eclipse.oomph.setup.Index#getProductCatalogs() container} for {@linkplain DocProductCatalog product catalogs}
   * as well as a {@link org.eclipse.oomph.setup.Index#getProjectCatalogs() container} for {@linkplain DocProductCatalog project catalogs}.
   * It also {@link org.eclipse.oomph.setup.Index#getDiscoverablePackages() provides access} to all task models.
   * All available {@linkplain DocProduct products} and {@link DocProject projects} are reachable from the index.
   * All the {@linkplain org.eclipse.oomph.setup.doc.user.wizard setup wizards} are driven by this information.
   * The index also {@link org.eclipse.oomph.setup.Index#getDiscoverablePackages() maintains references} to dynamic Ecore models that provide additional {@linkplain DocTask task} implementations.
   * It is stored in the {@link DocInstallationResource installation resource}.
   * </p>
   */
  public static class DocIndex
  {
  }

  /**
   * Location Catalog
   * <p>
   * A {@link org.eclipse.oomph.setup.LocationCatalog location catalog} maintains an index of all {@link DocInstallation installations} and {@link DocWorkspace workspaces}
   * installed and provisioned by Oomph,
   * including any installation where Oomph is installed,
   * and any workspace opened by any installation with Oomph installed.
   * It maintains a two-way map,
   * i.e., for each installation, the workspaces it has opened are {@link org.eclipse.oomph.setup.LocationCatalog#getInstallations() recorded}
   * and for each workspace, the installations that have opened it are {@link org.eclipse.oomph.setup.LocationCatalog#getWorkspaces() recorded}.
   * Each time a product with Oomph installed is started,
   * its installation-workspace pair is added to the maps.
   * The installation-to-workspace map is ordered,
   * so the most recently installed or started installation is moved to the front
   * and its workspace is moved to the front of the list associated with that installation.
   * Similarly, the workspace-to-installation map is ordered,
   * so the most recently provisioned or opened workspace is moved to the front
   * and the installation that opened it is moved to the front of the list associated with that workspace.
   * It is stored in the {@linkplain DocLocationResource location resource}.
   * </p>
   */
  public static class DocLocationCatalog
  {
  }

  /**
   * Catalog Selection
   * <p>
   * A {@link org.eclipse.oomph.setup.CatalogSelection catalog selection} maintains an installation and provisioning history for the {@link org.eclipse.oomph.setup.doc.user.wizard setup wizards}.
   * It maintains the following:
   * <ul>
   * <li>
   * The {@link org.eclipse.oomph.setup.CatalogSelection#getProductCatalogs() selected subset} of the {@linkplain DocIndex index}'s available {@linkplain DocProductCatalog product catalogs} displayed in the {@linkplain DocProductPage product page}.
   * </li>
   * The {@link org.eclipse.oomph.setup.CatalogSelection#getDefaultProductVersions() most recently selected} {@link DocVersion product version} of each {@linkplain DocProduct product} displayed in the {@linkplain DocProductPage product product page}.
   * </li>
   * <li>
   * The {@link org.eclipse.oomph.setup.CatalogSelection#getProjectCatalogs() selected subset} of the {@link DocIndex index}'s available {@link DocProjectCatalog project catalogs} displayed in the {@linkplain DocProjectPage project page}.
   * </li>
   * The {@link org.eclipse.oomph.setup.CatalogSelection#getDefaultStreams() most recently selected} {@link DocStream stream} of each {@linkplain DocProject project} ever chosen in the {@linkplain DocProjectPage project  page}.
   * </li>
   * </li>
   * The {@link org.eclipse.oomph.setup.CatalogSelection#getSelectedStreams() most recently selected} {@link DocStream streams} chosen by the {@linkplain DocProjectPage project  page}.
   * </li>
   * </ul>
   * In other words, it maintains a persistent initial state for all the {@link org.eclipse.oomph.setup.doc.user.wizard setup wizards}.
   * It is stored in the {@link DocCatalogSelectionResource catalog selection resource}.
   * </p>
   */
  public static class DocCatalogSelection
  {
  }
}
