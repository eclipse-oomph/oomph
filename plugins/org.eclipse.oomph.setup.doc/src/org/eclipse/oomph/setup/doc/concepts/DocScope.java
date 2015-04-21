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

import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog.DocProduct.DocVersion;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog.DocProject.DocStream;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource.DocIndexResource.ProductCatalogResource;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource.DocIndexResource.ProjectCatalogResource;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource.DocIndexResource.ProjectCatalogResource.ProjectResource;
import org.eclipse.oomph.setup.doc.concepts.DocTask.DocP2Task;
import org.eclipse.oomph.setup.doc.concepts.DocTaskComposition.ScopeList;
import org.eclipse.oomph.setup.doc.user.wizard.DocImportWizard;
import org.eclipse.oomph.setup.doc.user.wizard.DocInstallWizard;
import org.eclipse.oomph.setup.doc.user.wizard.DocProductPage;
import org.eclipse.oomph.setup.doc.user.wizard.DocProjectPage;

/**
 * Scopes
 * <p>
 * A {@link org.eclipse.oomph.setup.Scope scope} is the course-grained unit for grouping related {@linkplain DocTask tasks}.
 * Scope are hierarchically structured and tasks are {@linkplain ScopeList#scopeList gathered} from them based on this hierarchical structure.
 * They are stored in {@link DocSetupResource resources}.
 *
 * @number 200
 */
public class DocScope
{
  /**
   * Scopes
   * <p>
   * Each scope has the following properties:
   * <ul>
   */
  public static String properties;

  /**
   * <li>
   * A required {@link org.eclipse.oomph.setup.Scope#getName() attribute}
   * which should be a lower case name,
   * i.e., like a single component of a qualified Java package name.
   * The hierarchical {@link org.eclipse.oomph.setup.Scope#getParentScope() containment} structure of scopes
   * induces a {@link org.eclipse.oomph.setup.Scope#getQualifiedName() qualified} name attribute,
   * based on the '.' separated names of the scopes,
   * hence the recommendation to use a lower case scope name.
   * </li>
   */
  public static String name;

  /**
   * <li>
   * An optional {@link org.eclipse.oomph.setup.Scope#getLabel() label} attribute,
   * which should be a title case name that may include spaces and punctuation.
   * </li>
   */
  public static String label;

  /**
   * <li>
   * An optional {@link org.eclipse.oomph.setup.Scope#getDescription() description} attribute
   * which description can be arbitrary descriptive log.
   * </li>
   */
  public static String description;

  /**
   * </ul>
   * </p>
   */
  public static String footer;

  /**
   * Product Catalogs
   * <p>
   * A {@link org.eclipse.oomph.setup.ProductCatalog product catalog} is a {@link org.eclipse.oomph.setup.ProductCatalog#getProducts() container} for {@linkplain DocProduct products}
   * as well as a {@link org.eclipse.oomph.setup.ProductCatalog#getSetupTasks() container} for all the {@link DocTask tasks} to install any of those products.
   */
  public static class DocProductCatalog
  {
    /**
     * For example,
     * there is an Eclipse product catalog
     * generated for all the packages available on <a href="https://www.eclipse.org/downloads/">Eclipse's download page</a>.
     * In other words, you can use Oomph to install any version of any package that you'd normally download and unzip from Eclipse.org.
     */
    public static String example;

    /**
     * In the case of a product that has Oomph installed,
     * but wasn't actually {@linkplain DocInstallWizard installed} by Oomph,
     * there always exists a synthetic so called self product catalog
     * that contains the {@link DocProduct#self self product}.
     */
    public static String self;

    /**
     * It is stored in a {@link ProductCatalogResource project catalog resource}.
     * </p>
     */
    public static String footer;

    /**
     * Products
     * <p>
     * A {@link org.eclipse.oomph.setup.Product product} is a {@link org.eclipse.oomph.setup.Product#getVersions() container} for product {@linkplain DocVersion versions}
     * as well as a {@link org.eclipse.oomph.setup.Product#getSetupTasks() container} for all the {@link DocTask tasks} to install any of those product versions.
     */
    public static class DocProduct
    {
      /**
       * For example,
       * there is a product in the {@link DocProductCatalog#example Eclipse} product catalog
       * for each Eclipse <a href="http://www.eclipse.org/downloads/packages/release/Luna/R">package</a>.
       */
      public static String example;

      /**
       * In the case of a product that has Oomph installed,
       * but wasn't actually {@linkplain DocInstallWizard installed} by Oomph,
       * there always exists a synthetic so called self product in the {@link DocProductCatalog#self self product catalog}
       * that contains a {@link DocVersion#self self product version}.
       */
      public static String self;

      /**
       * </p>
       */
      public static String footer;

      /**
       * Product Versions
       * <p>
       * A product {@link ProductVersion version} is a {@link ProductVersion#getSetupTasks() container} for all the {@link DocTask tasks} to install a specific version of a product.
       */
      public static class DocVersion
      {
        /**
         * For example,
         * there is a product version in the {@link DocProduct#example Eclipse Standard product} in the {@link DocProductCatalog#example Eclipse product catalog}
         * for installing the Eclipse Standard <a href="https://www.eclipse.org/downloads/packages/eclipse-standard-44/lunar">Luna</a> package.
         * </p>
         */
        public static String example;

        /**
         * In the case of a product that has Oomph installed
         * but wasn't actually {@linkplain DocInstallWizard installed} by Oomph,
         * there always exists a synthetic so called product version in the {@link DocProduct#self self product} int the {@link DocProductCatalog#self self product catalog}.
         * It captures,
         * via a {@link DocP2Task p2 task},
         * all the information about the installable units that of that product installation
         * as well as all the update p2 sites that are available for updating those units within that product installation.
         * </p>
         */
        public static String self;
      }
    }
  }

  /**
   * Project Catalogs
   * <p>
   * A {@link org.eclipse.oomph.setup.Project project catalog} is a {@link org.eclipse.oomph.setup.ProjectCatalog#getProjects() container} for {@linkplain DocProject projects}
   * as well as a {@link org.eclipse.oomph.setup.ProjectCatalog#getSetupTasks() container} for both
   * the tasks to provision a workspace with any of those projects
   * and the tasks to install the tools needed to work with any of those projects.
   * For example,
   * there is an Eclipse project catalog for provisioning various projects hosted at Eclipse.org.
   * It is stored in a {@link ProjectCatalogResource project catalog resource}.
   * </p>
   */
  public static class DocProjectCatalog
  {
    /**
     * Projects
     * <p>
     * A {@link org.eclipse.oomph.setup.Project project} is a {@link org.eclipse.oomph.setup.Project#getSetupTasks() container} for {@linkplain DocStream streams}
     * as well as a {@link org.eclipse.oomph.setup.Project#getSetupTasks() container} for both
     * the tasks to provision a workspace with any of those streams
     * and the tasks to install the tools needed to work with any of those streams.
     * For example,
     * there is a project in the Eclipse project catalog for provisioning the Oomph project itself.
     * Projects can optionally {contain nested projects.
     * It is stored in a {@link ProjectResource project resource}.
     * </p>
     */
    public static class DocProject
    {
      /**
       * Projects can optionally contain {@link org.eclipse.oomph.setup.Project#getProjects() nested} projects
       * which may be stored in a separate {@link ProjectResource project resource} or in the same resource as the {@link org.eclipse.oomph.setup.Project#getParentProject() containing} project.
       * </p>
       */
      public static String nested;

      /**
       * Streams
       * <p>
       * A {@link org.eclipse.oomph.setup.Stream stream} is a {@link org.eclipse.oomph.setup.Stream#getSetupTasks() container} for both
       * the tasks to provision a workspace with for that particular stream
       * and the tasks to install the tools needed to work with that particular stream.
       * For example,
       * there is a stream in the Oomph project in the Eclipse project catalog
       * for provisioning the source code of the Git <a href="http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/">master</a> branch of the Oomph project.
       */
      public static class DocStream
      {
      }
    }
  }

  /**
   * Installations
   * <p>
   * An {@link org.eclipse.oomph.setup.Installation installation} is a {@link org.eclipse.oomph.setup.Installation#getSetupTasks() container} for the tasks to provision a specific installed Eclipse product.
   * Its primary function,
   * however,
   * is that it specifies a reference to a product {@linkplain DocVersion version}.
   * The tasks {@linkplain DocTaskComposition gathered} for that product version are {@link DocTaskExecution performed} to that installed product.
   * The {@linkplain DocInstallWizard installer wizard} creates an instance based on the selected product version on the {@link DocProductPage product page}.
   * Even if the product installation hasn't been installed by Oomph,
   * when Oomph is present,
   * an installation instance is automatically created
   * to refer to the synthetic {@link DocVersion#self self} product version.
   * </p>
   */
  public static class DocInstallation
  {
  }

  /**
   * Workspaces
   * <p>
   * A {@link org.eclipse.oomph.setup.Workspace workspace} is a {@link org.eclipse.oomph.setup.Workspace#getSetupTasks() container} for the tasks to provision a specific Eclipse workspace.
   * Its primary function is that it specifies zero or more references to project {@linkplain DocStream streams}.
   * The tasks {@linkplain DocTaskComposition gathered} for each of those project streams are {@link DocTaskExecution performed} for that workspace
   * as well as for the installed product used to open that workspace.
   * The {@linkplain DocInstallWizard installer wizard} creates an instance based on the selected project streams on the {@link DocProjectPage project page}.
   * The {@linkplain DocInstallWizard installer} and {@linkplain DocImportWizard project} wizards
   * </p>
   */
  public static class DocWorkspace
  {
  }

  /**
   * Users
   * <p>
   * A {@link org.eclipse.oomph.setup.User user} is a {@link org.eclipse.oomph.setup.User#getSetupTasks() container} for the tasks to provision
   * every installed Eclipse product and every opened workspace ever used by the end-user.
   * It provides an opportunity to customize all aspects of a user's experience with all their Eclipse products and workspaces.
   * It also provides support for the following:
   * <ul>
   * <li>
   * Recording the {@link org.eclipse.oomph.setup.User#getAcceptedLicenses() accepted license} of each of the tools automatically installed tools,
   * i.e., once a license is accepted,
   * it can be recorded to avoid any future prompts involving that particular license.
   * </li>
   * Recording the {@link org.eclipse.oomph.setup.User#getUnsignedPolicy() policy} with regard to handling of unsigned content,
   * i.e., once unsigned content is accepted,
   * that acceptance can be recorded to avoid any future prompts involving unsigned content.
   * </li>
   * <li>
   * Recording configuration options, so called {@link org.eclipse.oomph.setup.User#getAttributeRules() attribute rules},
   * that tailor where and how repositories, workspaces, and installations are physically stored.
   * </li>
   * </ul>
   */
  public static class DocUser
  {
  }
}
