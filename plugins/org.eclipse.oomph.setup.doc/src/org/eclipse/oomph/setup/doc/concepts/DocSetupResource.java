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
package org.eclipse.oomph.setup.doc.concepts;

import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.LocationCatalog;
import org.eclipse.oomph.setup.doc.concepts.DocInfrastructure.DocIndex;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocInstallation;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog.DocProduct.DocVersion;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog.DocProject;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog.DocProject.DocStream;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocUser;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocWorkspace;
import org.eclipse.oomph.setup.doc.user.wizard.DocProjectPage;

/**
 * Setup Resources
 * <p>
 * All the information for managing the automated installation and provisioning is maintained in resources.
 * Because Oomph is modeled using <a href="http://www.eclipse.org/emf">EMF</a>,
 * these resources are stored in XMI format,
 * for example,
 * The {@link DocIndexResource index resource} is serialized as follows:
 * {@link #indexXMI()}
 * </p>
 *
 * @number 400
 */
public abstract class DocSetupResource
{
  /**
   * @snip xml org.eclipse.setup /setups/org.eclipse.setup
   */
  public abstract void indexXMI();

  /**
   * Index
   * <p>
   * The <code><a href="http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/org.eclipse.setup">index:/org.eclipse.setup</a></code> resource maintains the {@linkplain DocIndex index},
   * where <code>index:/</code> is redirected to <code>http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/</code>.
   * It generally consists of references to {@link ProductCatalogResource product catalog} resources and {@link ProjectCatalogResource project catalog} resources.
   * {@link #index()}
   * </p>
   */
  public abstract static class DocIndexResource
  {
    /**
     * @snip tree org.eclipse.setup.tree /setups/org.eclipse.setup?prune:/org.eclipse.oomph.setup.Project#/
     * @title org.eclipse.setup
     * @expandTo 1
     */
    protected abstract void index();

    /**
     * Product Catalogs
     * <p>
     * A product catalog resource maintains a {@linkplain DocProductCatalog product catalog}.
     * For example,
     * the <a href="http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/org.eclipse.products.setup">Eclipse Products Catalog</a> resource
     * contains all the products available at Eclipse,
     * i.e., the equivalents of downloading and installing Eclipse packages.
     * Each product catalog resource is generally assumed to contain a {@link org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog#self self} product catalog
     * so that any product with Oomph installed, but not installed by Oomph,
     * will behave consistently as if it where installed by Oomph,
     * in particular,
     * a {@linkplain DocInstallationResource installation resource} will be demand created to refer to the {@link DocVersion#self self} {@linkplain DocVersion product version}.
     * The logical URI for this resource is <code>catalog:/self-product-catalog.setup</code>.
     * It does not correspond to any physical artifact located in the file system.
     * </p>
     */
    public static class ProductCatalogResource
    {
    }

    /**
     * Project Catalogs
     * <p>
     * A project catalog resource maintains a {@linkplain DocProjectCatalog project catalog}.
     * It generally contains references to various separately-maintained {@link ProjectResource project resources}.
     * For example,
     * the <a href="http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/org.eclipse.projects.setup">Eclipse Projects Catalog</a> resource
     * references all the Oomph-enabled Eclipse projects
     * and the <a href="http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/com.github.projects.setup">Github Projects Catalog</a> resource
     * references all the Oomph-enabled Github projects.
     * Each project catalog resource is generally assumed to maintain a so called &lt;User> project resource.
     * For example,
     * the Eclipse Projects Catalog contains a reference to <code>user:/org.eclipse.projects.setup</code>
     * where <code>user:/</code> is redirected to <code>~/.eclipse/org.eclipse.oomph.setup/setups/</code>.
     * Projects can be added to or removed from this resource using the {@linkplain DocProjectPage project page}.
     * This feature allows a developer to author her own setup project and to logically add it to an existing read-only project catalog without modifying that project catalog.
     * </p>
     */
    public static class ProjectCatalogResource
    {
      /**
       * Project Resources
       * <p>
       * A project resource maintains a {@linkplain DocProject project}.
       * Project resources are generally hosted on the Internet or on the local file system.
       * For example
       * the <a href="http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/Oomph.setup">Oomph.setup</a> resource
       * contains the {@linkplain DocStream streams} and {@link DocTask tasks} for the Oomph project itself.
       * </p>
       */
      public static class ProjectResource
      {
      }
    }
  }

  /**
   * Location Catalog
   * <p>
   * The location resource maintains the {@linkplain LocationCatalog location catalog}.
   * The logical URI for this resource is <code>user:/locations.setup</code>
   * where <code>user:/</code> is redirected to <code>~/.eclipse/org.eclipse.oomph.setup/setups/</code>.
   * </p>
   */
  public static class DocLocationResource
  {
  }

  /**
   * Catalog Selection
   * <p>
   * The catalog selection resource maintains the {@linkplain CatalogSelection catalog selection}.
   * The logical URI for this resource is <code>user:/catalogs.setup</code>
   * where <code>user:/</code> is redirected to <code>~/.eclipse/org.eclipse.oomph.setup/setups/</code>.
   * </p>
   */
  public static class DocCatalogSelectionResource
  {
  }

  /**
   * Installation
   * <p>
   * An installation resource maintains an {@linkplain DocInstallation installation}.
   * The physical URI of this resource is <code>org.eclipse.oomph.setup/installation.setup</code>
   * relatively within the <code>configuration</code> folder of the installed product.
   * </p>
   */
  public static class DocInstallationResource
  {
  }

  /**
   * Workspace
   * <p>
   * An workspace resource maintains a {@linkplain DocWorkspace workspace}.
   * The physical URI of this resource is <code>.metadata/.plugins/org.eclipse.oomph.setup/workspace.setup</code>
   * relatively within the running product's phyiscal <code>workspace</code> folder.
   * </p>
   */
  public static class DocWorkspaceResource
  {
  }

  /**
   * User
   * <p>
   * The user resource maintain the {@linkplain DocUser user}.
   * The logical URI for this resource is <code>user:/user.setup</code>
   * where <code>user:/</code> is redirected to <code>~/.eclipse/org.eclipse.oomph.setup/setups/</code>.
   * </p>
   */
  public static class DocUserResource
  {
  }
}
