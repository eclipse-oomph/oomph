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
package org.eclipse.oomph.setup.doc;

import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.LocationCatalog;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.doc.Everything.Infrastructure.Index;
import org.eclipse.oomph.setup.doc.Everything.Scope.Installation;
import org.eclipse.oomph.setup.doc.Everything.Scope.ProductCatalog.Product;
import org.eclipse.oomph.setup.doc.Everything.Scope.ProductCatalog.Product.Version;
import org.eclipse.oomph.setup.doc.Everything.Scope.ProjectCatalog;
import org.eclipse.oomph.setup.doc.Everything.Scope.ProjectCatalog.Project;
import org.eclipse.oomph.setup.doc.Everything.Scope.ProjectCatalog.Project.Stream;
import org.eclipse.oomph.setup.doc.Everything.Scope.User;
import org.eclipse.oomph.setup.doc.Everything.Scope.Workspace;
import org.eclipse.oomph.setup.doc.Everything.SetupResource.CatalogSelectionResource;
import org.eclipse.oomph.setup.doc.Everything.SetupResource.IndexResource.ProductCatalogResource;
import org.eclipse.oomph.setup.doc.Everything.SetupResource.IndexResource.ProjectCatalogResource;
import org.eclipse.oomph.setup.doc.Everything.SetupResource.IndexResource.ProjectCatalogResource.ProjectResource;
import org.eclipse.oomph.setup.doc.Everything.SetupResource.InstallationResource;
import org.eclipse.oomph.setup.doc.Everything.SetupResource.LocationResource;
import org.eclipse.oomph.setup.doc.Everything.SetupResource.WorkspaceResource;
import org.eclipse.oomph.setup.doc.Everything.SetupWizard.ConfirmationPage;
import org.eclipse.oomph.setup.doc.Everything.SetupWizard.ExecutionWizard;
import org.eclipse.oomph.setup.doc.Everything.SetupWizard.InstallerWizard;
import org.eclipse.oomph.setup.doc.Everything.SetupWizard.ProductPage;
import org.eclipse.oomph.setup.doc.Everything.SetupWizard.ProjectPage;
import org.eclipse.oomph.setup.doc.Everything.SetupWizard.ProjectWizard;
import org.eclipse.oomph.setup.doc.Everything.SetupWizard.PromptPage;
import org.eclipse.oomph.setup.doc.Everything.Task.Compound;
import org.eclipse.oomph.setup.doc.Everything.Task.P2;
import org.eclipse.oomph.setup.doc.Everything.Task.Targlet;
import org.eclipse.oomph.setup.doc.Everything.Task.Trigger;
import org.eclipse.oomph.setup.doc.Everything.Task.Variable;
import org.eclipse.oomph.setup.doc.Everything.TaskComposition.TaskList.Consolidation;
import org.eclipse.oomph.setup.doc.Everything.TaskComposition.TaskList.Filter;
import org.eclipse.oomph.setup.doc.Everything.TaskComposition.TaskList.InitialPhase;
import org.eclipse.oomph.setup.doc.Everything.TaskComposition.TaskList.Reorder;

/**
 * Outline
 *
 *<p>
 * Consider the steps involved in setting up a fresh Eclipse development environment to work with the source code of a particular version of a specific project:
 * <ul>
 * <li>
 * Install a project-specific IDE with appropriate tooling,
 * i.e., an IDE configured with all the settings and tools needed for editing, compiling, debugging, and testing the project's artifacts.
 * </li>
 * <li>
 * Materialize the workspace with the project's bundles and features by importing their workspace projects from various source code repositories,
 * organizing those workspace projects into meaningful working sets.
 * </li>
 * <li>
 * Materialize the target platform to contain all the bundles and features needed to compile the project's source code, run the project's tests, and exercise the project's end-user functionality.
 * </li>
 * </ul>
 *
 * The instructions for all these steps must be well-documented and generally evolve from release to release.
 * As such, one should consider the following salient questions:
 * <ul>
 * <li>
 * Who writes and maintains these instructions?
 * </li>
 * <li>
 * How do contributors find these instructions for each specific project?
 * </li>
 * <li>
 * How many contributors must follow these instructions, how often, and with how much invested time?
 * </li>
 * </ul>
 * The most fundamental question is, why is anyone doing this manually?
 * The overall process is tedious, error-prone, and time consuming,
 * i.e., it's a complete waste of human resource.
 * With Oomph, all these instructions are formalized as setup {@linkplain Task tasks} that are {@linkplain TaskExecution performed} automatically.
 * </p>
 * <p>
 * In addition to the project-specific setup process,
 * each user typically has her own personal favorite tools she wants installed
 * as well as has her own personal preferences,
 * such as key bindings,
 * she wants uniformly applied to each installation and workspace.
 * With Oomph Setup, this too is formalized and automated.
 * </p>
 *
 * <p>
 * The process of setting up a development environment involves two phases.
 * The first, or bootstrap phase, involves installing a functional product on disk.
 * Using the traditional manual approach one achieves this goal by as follows:
 * <ul>
 * <li>
 * Download a product, i.e., a package, from Eclipse's download page.
 * </li>
 * <li>
 * Unzip that download to the desired location on disk using an unzip tool that can handle Eclipse's zip files.
 * </li>
 * <li>
 * Ensure that a bit-appropriate JRE version is installed and visible on the execution path.
 * </li>
 * <li>
 * Run the executable.
 * </li>
 * </ul>
 * </p>
 * <p>
 * The second phase involves activities performed in the running product itself:
 * <ul>
 * <li>
 * Install additional tools not included in the downloaded product.
 * </li>
 * <li>
 * Configure various preferences appropriately, ideally by importing those preferences from a carefully-crafted Eclipse preference file.
 * </li>
 * <li>
 * Import workspace projects, ideally by importing those projects from a carefully-crafted project set file.
 * </li>
 * <li>
 * Activate the required target platform, ideally by activating a carefully-crafted target platform description file.
 * </li>
 * </ul>
 * </p>
 * <p>
 * To automate this setup process, Oomph provides the following:
 * <ul>
 * <li>
 * An {@linkplain InstallerWizard installer wizard} that automates the both phases of setting up a development environment.
 * </li>
 * <li>
 * A {@linkplain ProjectWizard project wizard} that automates the second phase of setting up a development environment.
 * <li>
 * An {@linkplain ProjectWizard execution wizard} that automates updating the development environment,
 * including the provisioning of personal favorite tools and personal preferences.
 * </li>
 * </ul>
 * Oomph helps ensure that each developer works with the same uniformly-provisioned development environment
 * and that each user has her personal tools and preferences uniformly available across all installations and workspaces.
 * Note that Oomph can be installed into an existing IDE, i.e., one not installed by Oomph,
 * via Oomph's <a rel="nofollow" href="http://download.eclipse.org/oomph/updates">update site</a>
 * or via Oomph's <a rel="nofollow" href="http://download.eclipse.org/oomph/updates/org.eclipse.oomph.site.zip">site archive</a>.</p>
 * </p>
 * <p>
 * To understand how best to exploit Oomph in order to save time and spare aggravation,
 * some basic concepts need to be well understood.
 * Automation is achieved by specifying setup {@linkplain Task tasks} organized into {@linkplain Scope scopes} stored in {@linkplain SetupResource resources}.
 * </p>
 * <p>
 * An important footnote is that Oomph makes heavy use of {@link BundlePool bundle pooling}
 * so it highly disk-space efficient to install many products and provision many target platforms
 * because they can all share a common bundle pool such that for each installable unit, there is only on jar file on disk.
 * Not only is this space efficient,
 * it also dramatically improves installation and provisioning performance
 * because once an installable unit is in the pool,
 * it never again needs to be downloaded from the Internet.
 *
 * @number 2
 */
public class Everything
{
  /**
   * Tasks
   * <p>
   * A setup {@link SetupTask task} is the fine-grained unit of work in the overall setup process.
   * A task is declarative in nature,
   * i.e., it describes what needs to be achieved rather than how it's achieved.
   * Each task is contained within a {@linkplain Scope scope}.
   * The overall setup process involves {@linkplain TaskComposition gathering} tasks from various scopes
   * and then {@linkplain TaskExecution performing} those tasks.
   * </p>
   */
  public static class Task
  {
    /**
     * <p>
     * Each task has the following properties:
     * <ul>
     */
    public static String properties;

    /**
     * <li>
     * An optional {@link SetupTask#getID() ID} attribute.
     * The value of this attribute,
     * if present,
     * must be unique within the containing {@link SetupResource resource}.
     * If the task has an ID,
     * a {@link Variable variable} is inferred for each populated String-typed attribute of that task.
     * This variable behaves as if it were logically contained just before the task in the task's container.
     * </li>
     */
    public static String id;

    /**
     * <p>
     * An {@link SetupTask#getExcludedTriggers() excluded} {@linkplain Trigger triggers} attribute
     * that specifies the triggers for which that task is not applicable.
     * This affects which tasks will be {@linkplain TaskComposition gathered}.
     * Most task implementations exclude the bootstrap trigger
     * because they must execute within a running product where the task implementation along with the supporting infrastructure are installed.
     * As such, task implementations hard-code the {@link SetupTask#getValidTriggers() permissible} triggers
     * and task authors can further exclude triggers from those that are permissible.
     * In the end,
     * by logical exclusion,
     * this induces an overall set of {@link SetupTask#getTriggers() allowed} triggers.
     * </li>
     */
    public static String excludedTriggers;

    /**
     * <li>
     * A {@link SetupTask#isDisabled() disabled} attribute that specifies whether or not the task should be gathered.
     * </li>
     */
    public static String disabled;

    /**
     * <li>
     * An optional {@link SetupTask#getRestrictions() restrictions} reference
     * that specifies the set of {@linkplain Scope scopes} to which the task inclusively applies.
     * TODO
     * </li>
     */
    public static String restrictions;

    /**
     * <li>
     * Both a {@link SetupTask#getPredecessors() predecessors} references and a {@link SetupTask#getSuccessors() successors} reference,
     * that specify that tasks that must be performed, respectively, before or after, the task itself is performed.
     * Furthermore, a task has an intrinsic {@link SetupTask#getPriority() priority} that is determined by the task implementor.
     * All these properties affect the {@linkplain Reorder order} in which tasks are performed.
     * </li>
     */
    public static String order;

    /**
     * </ul>
     * <p>
     * Each task supports the following operations:
     * <ul>
     */
    public static String operations;

    /**
     * <li>
     * Logic for determining whether the task {@link SetupTask#isNeeded(org.eclipse.oomph.setup.SetupTaskContext) needs} to perform
     * as well the logic for {@link SetupTask#perform(org.eclipse.oomph.setup.SetupTaskContext) performing} the task.
     * The determination of whether that the task needs to perform
     * is influenced by the {@link Trigger trigger},
     * i.e.,
     * it affects how detailed will be the analysis for determining whether the task needs to be performed
     * with the general goal being to {@link TaskExecution perform} only those tasks strictly needed for the {@link Trigger#startup startup} trigger.
     * This behavior is documented on a per-task basis.
     * </li>
     */
    public static String performance;

    /**
     * <li>
     * A task, in general, can be {@link SetupTask#overrideFor(SetupTask) overridden} by other tasks during the process of {@link TaskComposition gathering} tasks.
     * Which tasks are overridden by which other tasks,
     * and how that override merges two tasks into a single combined task
     * is specific to each task implementation and is therefore documented on a per-task basis.
     * A task implement supports overrides by yielding a so called {@link SetupTask#getOverrideToken() override token}.
     * Two tasks that yield equal override tokens are merged during the {@link InitialPhase initial} task gathering phase.
     * </li>
     */
    public static String override;

    /**
     * <li>
     * A task, in general, can be {@link SetupTask#consolidate() consolidated}.
     * At the end of the task gathering phase,
     * each task is {@link Consolidation consolidated} to optimize its representation in preparation before the task is {@linkplain TaskExecution performed}.
     * </li>
     */
    public static String consolidate;

    /**
     * </ul>
     * </p>
     */
    public static String footer;

    /**
     * Trigger
     */
    public static class Trigger
    {
      /**
       * <p>
       * There are three types of {@link org.eclipse.oomph.setup.Trigger triggers}:
       * <ul>
       */
      public static String introduction;

      /**
       * <li>
       * {@link org.eclipse.oomph.setup.Trigger#BOOTSTRAP Bootstrap} applies when using the {@link InstallerWizard installer wizard}.
       * </li>
       */
      public static String bootstrap;

      /**
       * <li>
       * {@link org.eclipse.oomph.setup.Trigger#STARTUP Startup} applies when a product is first launched, automated task performance is enabled, and there are tasks that need to be performed,
       * at which point the {@linkplain ExecutionWizard execution wizard} will be opened on the {@link ConfirmationPage confirmation page}.
       * </li>
       */
      public static String startup;

      /**
       * <li>
       * {@link org.eclipse.oomph.setup.Trigger#MANUAL Manual} applies when invoking the {@linkplain ProjectWizard project wizard} or directly invoking {@linkplain ExecutionWizard execution wizard}.
       * </li>
       */
      public static String manual;

      /**
       * </ul>
       * <p>
       * Each task specifies its {@link Task#excludedTriggers valid} triggers
       * determining whether that task is {@link Filter filtered} when tasks are gathered.
       * </p>
       */
      public static String summary;
    }

    public static class Variable
    {
    }

    public static class Compound
    {
    }

    public static class P2
    {
    }

    public static class Targlet
    {
    }
  }

  /**
   * Scopes
   * <p>
   * A {@link org.eclipse.oomph.setup.Scope scope} is the course-grained unit for grouping related {@linkplain Task tasks}.
   * Scope are hierarchically structured and tasks are {@linkplain TaskComposition gathered} from them based on this hierarchical structure.
   * They are stored in {@link SetupResource resources}.
   */
  public static class Scope
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
     * which description can be arbitrary descriptive text.
     * </li>
     */
    public static String description;

    /**
     * </p>
     */
    public static String footer;

    /**
     * Product Catalogs
     * <p>
     * A {@link org.eclipse.oomph.setup.ProductCatalog product catalog} is a {@link org.eclipse.oomph.setup.ProductCatalog#getProducts() container} for {@linkplain Product products}
     * as well as a {@link org.eclipse.oomph.setup.ProductCatalog#getSetupTasks() container} for all the {@link Task tasks} to install any of those products.
     */
    public static class ProductCatalog
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
       * but wasn't actually {@linkplain SetupWizard.InstallerWizard installed} by Oomph,
       * there always exists a synthetic so called self product catalog
       * that contains the {@link Product#self self product}.
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
       * A {@link org.eclipse.oomph.setup.Product product} is a {@link org.eclipse.oomph.setup.Product#getVersions() container} for product {@linkplain Version versions}
       * as well as a {@link org.eclipse.oomph.setup.Product#getSetupTasks() container} for all the {@link Task tasks} to install any of those product versions.
       */
      public static class Product
      {
        /**
         * For example,
         * there is a product in the {@link ProductCatalog#example Eclipse} product catalog
         * for each Eclipse <a href="http://www.eclipse.org/downloads/packages/release/Luna/R">package</a>.
         */
        public static String example;

        /**
         * In the case of a product that has Oomph installed,
         * but wasn't actually {@linkplain SetupWizard.InstallerWizard installed} by Oomph,
         * there always exists a synthetic so called self product in the {@link ProductCatalog#self self product catalog}
         * that contains a {@link Version#self self product version}.
         */
        public static String self;

        /**
         * </p>
         */
        public static String footer;

        /**
         * Product Versions
         * <p>
         * A product {@link ProductVersion version} is a {@link ProductVersion#getSetupTasks() container} for all the {@link Task tasks} to install a specific version of a product.
         */
        public static class Version
        {
          /**
           * For example,
           * there is a product version in the {@link Product#example Eclipse Standard product} in the {@link ProductCatalog#example Eclipse product catalog}
           * for installing the Eclipse Standard <a href="https://www.eclipse.org/downloads/packages/eclipse-standard-44/lunar">Luna</a> package.
           * </p>
           */
          public static String example;

          /**
           * In the case of a product that has Oomph installed
           * but wasn't actually {@linkplain SetupWizard.InstallerWizard installed} by Oomph,
           * there always exists a synthetic so called product version in the {@link Product#self self product} int the {@link ProductCatalog#self self product catalog}.
           * It captures,
           * via a {@link P2 p2 task},
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
     * A {@link org.eclipse.oomph.setup.Project project catalog} is a {@link org.eclipse.oomph.setup.ProjectCatalog#getProjects() container} for {@linkplain Project projects}
     * as well as a {@link org.eclipse.oomph.setup.ProjectCatalog#getSetupTasks() container} for both
     * the tasks to provision a workspace with any of those projects
     * and the tasks to install the tools needed to work with any of those projects.
     * For example,
     * there is an Eclipse project catalog for provisioning various projects hosted at Eclipse.org.
     * It is stored in a {@link ProjectCatalogResource project catalog resource}.
     * </p>
     */
    public static class ProjectCatalog
    {
      /**
       * Projects
       * <p>
       * A {@link org.eclipse.oomph.setup.Project project} is a {@link org.eclipse.oomph.setup.Project#getSetupTasks() container} for {@linkplain Stream streams}
       * as well as a {@link org.eclipse.oomph.setup.Project#getSetupTasks() container} for both
       * the tasks to provision a workspace with any of those streams
       * and the tasks to install the tools needed to work with any of those streams.
       * For example,
       * there is a project in the Eclipse project catalog for provisioning the Oomph project itself.
       * Projects can optionally {contain nested projects.
       * It is stored in a {@link ProjectResource project resource}.
       * </p>
       */
      public static class Project
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
        public static class Stream
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
     * is that it specifies a reference to a product {@linkplain Version version}.
     * The tasks {@linkplain TaskComposition gathered} for that product version are {@link TaskExecution performed} to that installed product.
     * The {@linkplain SetupWizard.InstallerWizard installer wizard} creates an instance based on the selected product version on the {@link ProductPage product page}.
     * Even if the product installation hasn't been installed by Oomph,
     * when Oomph is present,
     * an installation instance is automatically created
     * to refer to the synthetic {@link Version#self self} product version.
     * </p>
     */
    public static class Installation
    {
    }

    /**
     * Workspaces
     * <p>
     * A {@link org.eclipse.oomph.setup.Workspace workspace} is a {@link org.eclipse.oomph.setup.Workspace#getSetupTasks() container} for the tasks to provision a specific Eclipse workspace.
     * Its primary function is that it specifies zero or more references to project {@linkplain Stream streams}.
     * The tasks {@linkplain TaskComposition gathered} for each of those project streams are {@link TaskExecution performed} for that workspace
     * as well as for the installed product used to open that workspace.
     * The {@linkplain SetupWizard.InstallerWizard installer wizard} creates an instance based on the selected project streams on the {@link ProjectPage project page}.
     * The {@linkplain SetupWizard.InstallerWizard installer} and {@linkplain SetupWizard.ProjectWizard project} wizards
     * </p>
     */
    public static class Workspace
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
    public static class User
    {
    }
  }

  /**
   * Infrastructure
   * <p>
   * In addition to {@linkplain Scope scopes} there are several other important structural elements for organizing and maintaining setup information.
   * </p>
   */
  public static class Infrastructure
  {
    /**
     * Index
     * <p>
     * An {@link org.eclipse.oomph.setup.Index index} is a {@link org.eclipse.oomph.setup.Index#getProductCatalogs() container} for {@linkplain ProductCatalog product catalogs}
     * as well as a {@link org.eclipse.oomph.setup.Index#getProjectCatalogs() container} for {@linkplain ProductCatalog project catalogs}.
     * It also {@link org.eclipse.oomph.setup.Index#getDiscoverablePackages() provides access} to all task models.
     * All available {@linkplain Product products} and {@link Project projects} are reachable from the index.
     * All the {@linkplain SetupWizard setup wizards} are driven by this information.
     * The index also {@link org.eclipse.oomph.setup.Index#getDiscoverablePackages() maintains references} to dynamic Ecore models that provide additional {@linkplain Task task} implementations.
     * It is stored in the {@linkplain InstallationResource installation resource}.
     * </p>
     */
    public static class Index
    {
    }

    /**
     * Location Catalog
     * <p>
     * A {@link org.eclipse.oomph.setup.LocationCatalog location catalog} maintains an index of all {@link Installation installations} and {@link Workspace workspaces}
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
     * It is stored in the {@linkplain LocationResource location resource}.
     * </p>
     */
    public static class LocationCatalog
    {
    }

    /**
     * Catalog Selection
     * <p>
     * A {@link org.eclipse.oomph.setup.CatalogSelection catalog selection} maintains an installation and provisioning history for the {@link SetupWizard setup wizards}.
     * It maintains the following:
     * <ul>
     * <li>
     * The {@link org.eclipse.oomph.setup.CatalogSelection#getProductCatalogs() selected subset} of the {@linkplain Index index}'s available {@linkplain ProductCatalog product catalogs} displayed in the {@linkplain ProductPage product page}.
     * </li>
     * The {@link org.eclipse.oomph.setup.CatalogSelection#getDefaultProductVersions() most recently selected} {@link Version product version} of each {@linkplain Product product} displayed in the {@linkplain ProductPage product product page}.
     * </li>
     * <li>
     * The {@link org.eclipse.oomph.setup.CatalogSelection#getProjectCatalogs() selected subset} of the {@link Index index}'s available {@link ProjectCatalog project catalogs} displayed in the {@linkplain ProjectPage project page}.
     * </li>
     * The {@link org.eclipse.oomph.setup.CatalogSelection#getDefaultStreams() most recently selected} {@link Stream stream} of each {@linkplain Project project} every chosen in the {@linkplain ProjectPage project  page}.
     * </li>
     * </li>
     * The {@link org.eclipse.oomph.setup.CatalogSelection#getSelectedStreams() most recently selected} {@link Stream streams} chosen by the {@linkplain ProjectPage project  page}.
     * </li>
     * </ul>
     * In other words, it maintains a persistent initial state for all the {@link SetupWizard setup wizards}.
     * It is stored in the {@link CatalogSelectionResource catalog selection resource}.
     * </p>
     */
    public static class CatalogSelection
    {
    }
  }

  /**
   * Setup Resources
   * <p>
   * All the information for managing the automated installation and provisioning is maintained in resources.
   * Because Oomph is modeled using <a href="http://www.eclipse.org/emf">EMF</a>,
   * these resources are stored in XMI format.
   * </p>
   */
  public static class SetupResource
  {
    /**
     * Index
     * <p>
     * The <code><a href="http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/org.eclipse.setup">index:/org.eclipse.setup</a></code> resource maintains the {@linkplain Index index},
     * where <code>index:/</code> is redirected to <code>http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/</code>.
     * It generally consists of references to {@link ProductCatalogResource product catalog} resources and {@link ProjectCatalogResource project catalog} resources.
     * </p>
     */
    public static class IndexResource
    {
      /**
       * Product Catalogs
       * <p>
       * A product catalog resource maintains a {@linkplain ProductCatalog product catalog}.
       * For example,
       * the <a href="http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/org.eclipse.products.setup">Eclipse Products Catalog</a> resource
       * contains all the products available at Eclipse,
       * i.e., the equivalents of downloading and installing Eclipse packages.
       * Each product catalog resource is generally assumed to contain a {@link org.eclipse.oomph.setup.doc.Everything.Scope.ProductCatalog#self self} product catalog
       * so that any product with Oomph installed, but not installed by Oomph,
       * will behave consistently as if it where installed by Oomph,
       * in particular,
       * a {@linkplain InstallationResource installation resource} will be demand created to refer to the {@link Version#self self} {@linkplain Version product version}.
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
       * A project catalog resource maintains a {@linkplain ProjectCatalog project catalog}.
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
       * Projects can be added to or removed from this resource using the {@linkplain ProjectPage project page}.
       * This feature allows a developer to author her own setup project and to logically add it to an existing read-only project catalog without modifying that project catalog.
       * </p>
       */
      public static class ProjectCatalogResource
      {
        /**
         * Project Resources
         * <p>
         * A project resource maintains a {@linkplain Project project}.
         * Project resources are generally hosted on the Internet or on the local file system.
         * For example
         * the <a href="http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/Oomph.setup">Oomph.setup</a> resource
         * contains the {@linkplain Stream streams} and {@link Task tasks} for the Oomph project itself.
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
    public static class LocationResource
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
    public static class CatalogSelectionResource
    {
    }

    /**
     * Installation
     * <p>
     * An installation resource maintains an {@linkplain Installation installation}.
     * The physical URI of this resource is <code>org.eclipse.oomph.setup/installation.setup</code>
     * relatively within the <code>configuration</code> folder of the installed product.
     * </p>
     */
    public static class InstallationResource
    {
    }

    /**
     * Workspace
     * <p>
     * An workspace resource maintains a {@linkplain Workspace workspace}.
     * The physical URI of this resource is <code>.metadata/.plugins/org.eclipse.oomph.setup/workspace.setup</code>
     * relatively within the running product's phyiscal <code>workspace</code> folder.
     * </p>
     */
    public static class WorkspaceResource
    {
    }

    /**
     * User
     * <p>
     * The user resource maintain the {@linkplain User user}.
     * The logical URI for this resource is <code>user:/user.setup</code>
     * where <code>user:/</code> is redirected to <code>~/.eclipse/org.eclipse.oomph.setup/setups/</code>.
     * </p>
     */
    public static class UserResource
    {
    }
  }

  /**
   * Bundle Pools
   * <p>
   * Eclipse's p2 technology supports the concept of a <a href="http://wiki.eclipse.org/Equinox/p2/Getting_Started#Bundle_pooling">bundle pool</a>.
   * Bundle pools help to dramatically reduce disk footprint
   * and to eliminate repeated downloads of the same bundles and features,
   * thereby dramatically improving the performance of software updates and target platform provisioning.
   * Oomph makes heavy use of this technology for both its {@link P2 p2} tasks and it {@link Targlet targlet} tasks.
   * Further,
   * Oomph provides a technology layer on top of p2
   * to improve the behavior and performance of caching
   * allowing Oomph to provide offline support for installing and updating installations and target platforms.
   * </p>
   */
  public static class BundlePool
  {
  }

  /**
   * Task Composition
   * <p>
   * The installation and provisioning process is driven by gathering a {@linkplain Task task} list from the available {@linkplain Scope scopes}
   * in preparation for {@link TaskExecution task execution}.
   * </p>
   */
  public static class TaskComposition
  {
    /**
     * Scope List
     * <p>
     * In preparation for gathering a task list,
     * an ordered list of {@link Scope scopes} is collected.
     */
    public static class ScopeList
    {
      /**
       * The following process of collecting scopes is repeated once for each {@link Stream stream}
       * specified in the {@linkplain ProjectPage project page} or in the {@linkplain Workspace workspace}:
       * <ul>
       *
       * <li>
       * Visit the {@link Version product version}
       * as selected in the {@linkplain ProductPage product page} or in the {@linkplain Installation installation}.
       * <ul>
       * <li>
       * Add that product version's containing {@linkplain Product products}'s containing {@linkplain ProductCatalog product catalog} to the list.
       * Add that product version's containing product to the list.
       * Add that product version itself to the list.
       * </li>
       * </ul>
       * </li>
       *
       * <li>
       * Visit a {@link Stream stream}
       * as selected in the {@linkplain ProjectPage project page} or in the {@linkplain Workspace workspace}, if there is one.
       * <ul>
       * <li>
       * Add that stream's containing {@linkplain Project projects}'s containing {@linkplain ProjectCatalog project catalog} to the list.
       * Add that stream's outer-most containing project to the list
       * and proceed down through the nested projects up to the stream's directly containing project,
       * adding each project to the list along the way.
       * Add that stream itself to the list.
       * </li>
       * </ul>
       * </li>
       *
       * <li>
       * Add the {@link Installation installation} to the list.
       * </li>
       *
       * <li>
       * Add the {@link Workspace workspace} to the list.
       * </li>
       *
       * <li>
       * Add the {@link User user} to the list.
       * </li>
       * </ul>
       * </p>
       */
      public static String scopeList;

      /**
       * <p>
       * For each list of scopes, an ordered list of tasks is collected.
       * Initially, for each scope in the list,
       * three {@link Variable variables} are induced,
       * one each for the {@link Scope#name name}, {@link Scope#label label}, and {@link Scope#description description}
       * attributes of the scope
       * where the variable name is prefixed with the {@link ScopeType scope type} as follows:
       * <ul>
       * <li>
       * <code>scope.product.catalog</code>
       * </li>
       * <li>
       *  <code>scope.product</code>
       * </li>
       * <li>
       *  <code>scope.product.version</code>
       * </li>
       * <li>
       * <code>scope.project.catalog</code>
       * </li>
       * <li>
       * <code>scope.project</code>
       * </li>
       * <li>
       * <code>scope.project.stream</code>
       * </li>
       * <li>
       * <code>scope.installation</code>
       * </li>
       * <li>
       * <code>scope.workspace</code>
       * </li>
       * <li>
       *  <code>scope.user</code>
       * </li>
       * </ul>
       * The value of each such variable will be the value of that attribute for the scope.
       * If the scope's label is <code>null</code>, the name is used as the label value
       * and if the scope's description is <code>null</code>, the scope's label is used as the description value.
       * In addition, to the <code>name</code> variable,
       * for each product, product version, project, and project stream
       * an additional variable with the name suffix <code>.qualifier</code> is induced
       * where the value is the {@link org.eclipse.oomph.setup.Scope#getQualifiedName() qualified name} of the scope.
       * For example,
       * the value of the <code>scope.project.stream.name.qualified</code> variable of the Oomph.setup project's master stream
       * is <code>org.eclipse.oomph.master</code>
       * All these induced variables are added, in scope order, to the initial gathered list of tasks.
       * </p>
       */
      public static String scopeVariables;

      /**
       * <p>
       * Additional tasks are gathered into the task list from the ordered scopes by visiting each contained task of each scope as follows:
       * <ul>
       * <li>
       * If the task is disabled,
       * ignore the task.
       * </li>
       * <li>
       * If the list of scopes doesn't contain all of the task's restrictions,
       * ignore the task.
       * </li>
       * <li>
       * If the task is {@link Compound compound},
       * recursively visit each of that compound task's contained tasks.
       * </li>
       * <li>
       * Otherwise,
       * add the task to the list of tasks.
       * In other works,
       * the gathered task list will only contain leaf tasks.
       * </li>
       * </ul>
       * At the end of scope processing,
       * the gathered task list contains
       * all of the per-stream tasks,
       * or just task list if there is no stream.
       * Each task list (or the one task list) is further processed in two phases.
       * The initial phase is applied for each task list separately.
       * The per-stream task lists are then concatenated into a single list and the second phase is applied to that final composed list,
       * or the one task list is further processed by the second phase.
       * The processing of the the task list in each phase is roughly same.
       * </p>
       */
      public static String footer;
    }

    /**
     * Task List
     * <p>
     * The task list is processed to induce additional tasks,
     * to {@link Task#override override and merge} tasks,
     * to evaluate and expand variables
     * and to {@link Task#order reorder} tasks.
     * The members of the task list that are {@link Variable variables} induce an initial set of keys,
     * i.e., a set of all variable names.
     * Oomph tasks are modeled with EMF,
     * so each task instance knows it corresponding EMF class.
     * During the initial phase processing,
     * the list of tasks is analyzed to determine the set of EMF classes that are required to implement all the tasks in the list.
     * Each EMF class is processed as follows:
     * <ul>
     * <li>
     * If the class contains enablement annotations,
     * induce {@linkplain P2 p2 tasks},
     * i.e., tasks to install the necessary implementations in the installed product,
     * and add these induced tasks to the head of the task list.
     * </li>
     * <li>
     * If the class contains variable annotations,
     * induce the {@linkplain Variable variables}
     * and add them to the head of the task list.
     * </li>
     *
     * <li>
     * Visit each attribute of the class as follows:
     * <ul>
     * If the attribute's type isn't String,
     * ignore it.
     * <li>
     * If the attribute has both a variable annotation and a variable rule annotation,
     * visit each instance of that attribute's class in the task list as follows:
     * <ul>
     * <li>
     * If the attribute's value in that instance task is not empty,
     * the rule isn't needed,
     * so ignore it.
     * </li>
     * <li>
     * If the instance task doesn't have an ID,
     * or it does,
     * but the key composed from the ID value, with '.' and the attribute name appended,
     * is already in the list of induced keys,
     * the rule isn't needed,
     * so ignore it.
     * </li>
     * <li>
     * Induce a rule variable for that attribute,
     * if that rule variable hasn't already been induced for another task instance.
     * </li>
     * </ul>
     * A mapping from rule variables to their inducing attribute is maintained.
     * <li>
     * </li>
     * </li>
     * </ul>
     * </li>
     * </ul>
     * At the end of this processing for the first phase,
     * the list of tasks includes additional induced variables and tasks.
     * For the final phase,
     * the composed list of tasks contains no variables, because they've all been evaluated and expanded,
     * and the induced tasks are already present.
     * </p>
     */
    public static class TaskList
    {
      /**
       * <p>
       * Further processing proceeds as follows:
       * <ul>
       * <li>
       * Build a substitution map,
       * i.e., task-to-task map of substitutions by visiting each task as follows:
       * <ul>
       * <li>
       * Put the task's {@link Task#override override token} into an token-to-task map,
       * and if another task for that token is already present in the token-to-task map,
       * the visited task is a substitution for that other task so add that to the substitution map.
       * As such,
       * tasks later in the list can override tasks earlier in the list.
       * </li>
       * The substitution map is then further processed to follow the substitution mappings so as to represent the direct substitutions.
       * </ul>
       * </li>
       * </ul>
       * </p>
       */
      public static String substitutions;

      /**
       * Initial Phase
       * <p>
       * For the initial phase processing,
       * all the tasks are efficiently copied,
       * including the copying of the containing scopes.
       * The copying process takes the task-to-task {@ink TaskList#substitutions substitution} map into account,
       * i.e., each task is logically replaced in the copy by its {@link Task#override merged override}.
       * As such,
       * only the final overridden merged task remains in the resulting task list copy
       * and all references to the overridden and overriding tasks will reference the final merged override instead.
       * Further processing of the task list proceeds with this copied task list.
       * </p>
       * <p>
       * An explicit key map,
       * i.e., a map from variable name to variable,
       * is computed by visiting each variable in the task list.
       * Note that the preceding copying process will have eliminated duplicate variables.
       * The initial phase processing then proceeds by visiting each task with a non-empty ID attribute as follows:
       * <ul>
       * <li>
       * Visit each attribute of the task's EMF class
       * with respect to the variable name induced by appending '.' and the attribute name to the value of the task's ID attribute:
       * <ul>
       * <li>
       * If the attribute is the ID attribute of a task,
       * or the attribute isn't a single-valued String-typed attribute,
       * ignore it.
       * </li>
       * <li>
       * If the explicit keys contains the induced variable's name,
       * the task's value for the attribute is empty,
       * and the attribute has a variable annotation,
       * change the task's value for the attribute to be a reference to the explicit variable.
       * </li>
       * <li>
       * If the explicit keys do not include the induced variable name,
       * induce a new variable
       * record that it's an explicit key,
       * add it to the task list,
       * and also do the following:
       * <ul>
       * <li>
       * If the task's value for the attribute is empty,
       * and the attribute has a variable annotation,
       * change the task's value for the attribute to be a reference to the induced variable.
       * </li>
       * <li>
       * Conversely,
       * if the task's value for the attribute is non-empty,
       * set the induced variable's value to that value,
       * and as for the empty value case,
       * if the attribute has a variable annotation,
       * change the task's value for the attribute to be a reference to the induced variable.
       * </li>
       * <li>
       * Also induce rule variables for each of the attribute's rule annotations,
       * recording them as explicit keys.
       * </li>
       * <li>
       * And finally,
       * if the attribute has a variable annotation,
       * and the induced variable's value is a self reference that would lead to circular evaluation,
       * induce a yet another variable with the induced variable's name suffixed with <code>.explicit</code>
       * from the explicit annotations of the attribute,
       * and also change the self-referencing variable's value to refer to that explicit variable.
       * </li>
       * </ul>
       * </li>
       * </ul>
       * </li>
       * <li>
       * Visit each task to handle its so called active annotations.
       * Such annotations can be used on variables to compose a set of choices
       * from choices defined in other variables,
       * or to induce choices from the value of another variable.
       * TODO examples
       * </li>
       * <li>
       * Visit each variable task to build a key-to-value map.
       * Analyze that map to determine the variable references,
       * reordering the map base on this dependency analysis.
       * And expand the variables in the ordered map.
       * </li>
       * <li>
       * Expand the values of all attributes, of all tasks based on the key-to-value map, except those attributes marked as not expandable.
       * E.g., the name attribute of a variable itself is not expanded.
       * </li>
       * <li>
       * Recall that the gathering of tasks effectively {@link ScopeList#footer ignore} {@linkplain Compound compound tasks}.
       * But those compound tasks can specify {@link Task#order predecessors and successors} and well as {@link Task#restrictions restrictions}.
       * As such,
       * those predecessors and successors are expanded to become references to the leaf tasks.
       * Furthermore,
       * the resulting expanded predecessors and successors,
       * as well at the restrictions,
       * are propagated down to the leaf tasks.
       * </li>
       * </ul>
       * <p>
       */
      public static class InitialPhase
      {
      }

      /**
       * Final Phase
       * <p>
       * The final phase processes a task list that is a concatenation of task lists produced from the initial phase,
       * or just the one task list already processed by the initial phase.
       * As such,
       * it's working with task copies for which all variables have been expanded and eliminated.
       * The processing for this phase augments the substitution map
       * by analyzing the task list for structural duplicates.
       * It then applies those substitutions,
       * i.e., overriding and merging duplicate tasks,
       * thereby reducing the task list before further processing.
       * </p>
       */
      public static class FinalPhase
      {
      }

      /**
       * Reordering
       * <p>
       * The processing of the task list,
       * particularly task {@link Task#override overriding} and merging,
       * affects the overall order of the task list such that it's different from the original authored order gathered from the scopes.
       * Not only that,
       * when multiple streams are involved,
       * {@linkplain FinalPhase final phase} processing is dealing with a concatenated list in which the tasks must be properly reordered.
       * To support that,
       * each task has an intrinsic {@link Task#order priority};
       * the task list is primarily sorted according to that priority.
       * Each task also specifies {@link Task#order predecessors and successors};
       * the task list is secondarily sorted to respect that induced partial order.
       * After these two sorting steps,
       * the tasks in the list are modified to clear both the predecessors and successors
       * and then the predecessors are set to form a chain that induces an overall order that's exactly this final order of sorted task list;
       * this chain excludes variables.
       * This chain of dependencies ensures that the final phase processing,
       * which deals with the concatenated task lists,
       * will properly interleave the tasks (because of the priority sorting)
       * while also respecting the per-stream order of the multiple streams.
       * <p>
       */
      public static class Reorder
      {
      }

      /**
       * Trigger Filtering
       * <p>
       * Each task that {@link Task#excludedTriggers excludes} the current {@link Trigger trigger} is removed from the task list.
       * Note that the task list gathering process gathers <b>all</b> tasks
       * because the task list is analyzed to determine which tasks need to be installed for all possible triggers.
       * So for {@link Trigger#bootstrap bootstrap bootstrap }trigger,
       * even the tasks that can't execute until they're running in an installed product are analyzed to ensure that,
       * once the product is installed,
       * the tasks that will need to perform in that installation,
       * i.e., for {@link Trigger#startup startup} or {@link Trigger#manual manual} trigger,
       * are properly installed.
       * The processing of all tasks also implies that at bootstrap time,
       * all  variables that will be needed in the running installed product will be {@link PromptPage prompted} early
       * and hence will already be available in the running installed product.
       * </p>
       */
      public static class Filter
      {
      }

      /**
       * Consolidation
       * <p>
       * The final task list processing step
       * removes all variables from the task list
       * and {@link Task#consolidate consolidates} each remaining task.
       * At this point,
       * the tasks in the list are ready to be {@link TaskExecution performed}.
       * </p>
       */
      public static class Consolidation
      {
      }
    }
  }

  /**
   * Variable Recording
   * <p>
   * </p>
   */
  public static class VariableRecording
  {

  }

  /**
   * Task Execution
   * <p>
   * The {@link TaskComposition gathered} task list is prepared to {@link Task#performance perform} the specified tasks.
   * This list is presented to the user on the setup wizard's {@link ConfirmationPage confirmation page}.
   * Initially,
   * each task in the list is visited
   * to determine if it needs to perform.
   * That determination is generally influenced by the {@link Trigger trigger}.
   * Tasks that don't need to perform are logically removed from the list
   * to induce the so called needed task list.
   * The confirmation page allows the user to see either the full list or the needed task list.
   *
   * Each task remaining in the reduced list of needed tasks
   * is then performed.
   * </p>
   */
  public static class TaskExecution
  {
  }

  /**
   * Setup Wizards
   * <p>
   * Oomph provides three wizards to drive the automated installation and provisioning process.
   * These wizards reuse the same underlying pages.
   * They makes heavy use of Internet-hosted {@linkplain SetupResource resources},
   * but these resources are cached locally on each download,
   * so once cached,
   * the wizards can function offline based on these cached instances.
   * </p>
   */
  public static class SetupWizard
  {
    /**
     * <p>
     * All the wizard's footers contain the following:
     * <ul>
     * <li>
     * A <em>help</em> button
     * to bring up this help.
     * </li>
     * <li>
     * The usual wizard navigation buttons for back, next, finish, and cancel.
     * </li>
     * </ul>
     * </p>
     */
    public static String wizardFooter;

    /**
     * Installer Wizard
     * <p>
     * The installer wizard is the basis for Oomph's bootstrap-{@link Trigger triggered}, automated installation and provisioning process.
     * The following downloads are available for this Eclipse RCP application.
     * <ul>
     * <li>
     * <a rel="nofollow" href="http://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-win32.win32.x86_64.zip">Windows 64 bit</a>
     * </li>
     * <li>
     * <a rel="nofollow" href="http://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-win32.win32.x86.zip">Windows 32 bit</a>
     * </li>
     * <li>
     * <a rel="nofollow" href="http://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-macosx.cocoa.x86_64.tar.gz">Mac OS 64 bit</a>
     * </li>
     * <li>
     * <a rel="nofollow" class="external text" href="http://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-linux.gtk.x86_64.zip">Linux 64 bit</a>
     * </li>
     * <a rel="nofollow" class="external text" href="http://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-linux.gtk.x86.zip">Linux 32 bit</a>
     * </li>
     * </ul>
     * This download can be "installed" just like <a href="https://wiki.eclipse.org/Eclipse/Installation">Eclipse itself</a>.
     * </p>
     */
    public static class InstallerWizard
    {
      /**
       * <p>
       * In addition to {@link SetupWizard#wizardFooter usual} controls,
       * installer's footer contains the following:
       * <ul>
       * <li>
       * A <em>network proxy settings</em> button
       * to bring up the equivalent of Eclipse's <em>Window->Preferences...->General->Network Connections</em> preferences.
       * Configuration the network proxies is a necessary first step for users working in an environment behind a fire wall,
       * none of Oomph's Internet-hosed resources will be accessible without that configuration.
       * Note that all the configured network settings will be propagated to the installed product.
       * </li>
       * <li>
       * An <em>SSH2 settings</em> button
       * to bring up the equivalent of Eclipse's <em>Window->Preferences...->General->Network Connections->SSH2</em> preferences.
       * Confirming the SSH2 settings during initial installation is a good idea when using any technologies that are based on SSH access,
       * i.e., if you plan to clone Git projects via public-key encryption.
       * Note that all the configured SSH2 settings will be propagated to the installed product.
       * </li>
       * <li>
       * An <em>self update</em> button
       * to update the installer itself to the last Internet-hosted version.
       * The wizard always check whether updates are available.
       * If so, the button is animated, otherwise it's disabled.
       * </li>
       * <li>
       * A <em>build identifier</em> so you know which version you're currently using and can provide that information for problem reports.
       * </li>
       * </ul>
       * </p>
       */
      public static String wizardFooter;
    }

    /**
     * Project Import Wizard
     * <p>
     * The project wizard is the basis for Oomph's manually-{@link Trigger triggered}, updates to the automated installation and provisioning process.
     * It is available in any running Eclipse product with Oomph installed
     * via the <em>File->Import...->Oomph->Projects into Workspace</em>
     * and the <em>execution</em> tool bar button.
     * It's purpose is to augment the {@link WorkspaceResource workspace}'s set of {@link Stream streams}.
     * </p>
     */
    public static class ProjectWizard
    {
    }

    /**
     * Execution Wizard
     * <p>
     * The execution wizard is the basis for Oomph's manually-{@link Trigger triggered}, automated installation and provisioning process.
     * It's also used for startup-triggered, installation and provisioning,
     * i.e., when the Eclipse product starts
     * and the <em>Window->Preferences...->Oomph->Setup->Skip automatic task execution at startup time</em> preference permits,
     * tasks are {@linkplain TaskComposition gathered},
     * and if any tasks {@linkplain TaskExecution need to perform},
     * the execution wizard is opened on the {@link ConfirmationPage confirmation page}.
     * </p>
     */
    public static class ExecutionWizard
    {
    }

    /**
     * Product Page
     * <p>
     * The primary purpose of the product page is to create an {@linkplain InstallationResource installation}
     * with a specific {@link Version product version}.
     * Its secondary purpose is to manage Oomph's {@linkplain BundlePool bundle pools}.
     * The product page is used exclusively by the {@linkplain Installation installation wizard}.
     * </p>
     * <p>
     * The tree viewer of the page displays the {@linkplain CatalogSelection selected} {@linkplain ProductCatalog product catalogs}.
     * The <em>product catalog selection</em> tool bar button
     * is used to choose which of the product catalogs available in the {@link Index index} are displayed.
     * The <em>filter text</em> can be used to filter which of the {@link Product products} are displayed.
     * The <em>collapse button</em> can be used to collapse the tree;
     * initially it's fully expanded.
     * The <em>refresh</em> button can be used to update the locally-cached versions of all the {@link SetupResource resources} used in the wizard.
     * </p>
     * <p>
     * Selecting a specific product displays its ID, name, and version.
     * The <em>Product Version</em> drop-down determines which specific version of the available product versions to install.
     * The {@link CatalogSelectionResource selected product} is remembered,
     * as well as the selected version of that product,
     * so when the wizard comes up the next time,
     * these will be the defaults initially available.
     * Double-clicking a product automatically advances the {@linkplain ProjectPage project page}.
     * </p>
     * <p>
     * The <em>Bundle Pool</em> check-box determines whether the installation will use {@link BundlePool bundle pools},
     * and the adjacent drop-down determines which specific bundle pool to use.
     * The <em>Manage Bundle Pools</em> button brings up the {@linkplain BundlePoolManager bundle pool management} dialog.
     * </p>
     */
    public static class ProductPage
    {
    }

    /**
     * Project Page
     * <p>
     * The primary purpose of the project page is to create (or update) a {@linkplain WorkspaceResource workspace}
     * with one or more specific {@link Stream project streams}.
     * </p>
     * <p>
     * The tree viewer of the page displays the {@linkplain CatalogSelection selected} {@linkplain ProjectCatalog project catalogs}.
     * The <em>project catalog selection</em> tool bar button
     * is used to choose which of the project catalogs available in the {@link Index index} are displayed.
     * The <em>filter text</em> can be used to filter which of the {@link Project projects} are displayed.
     * The <em>collapse button</em> can be used to collapse the tree;
     * initially it's expanded to the second level.
     * The <em>refresh</em> button can be used to update the locally-cached versions of all the {@link SetupResource resources} used in the wizard.
     * TODO need buttons to add and remove user projects and explain how they work
     * </p>
     * <p>
     * Choosing a specific project displays it in the table viewer.
     * There are two ways add a project to the table viewer:
     * <ul>
     * <li>
     * Double-clicking the project.
     * </li>
     * <li>
     * Selecting one or more projects
     * and then clicking  the <em>down arrow</em>.
     * </li>
     * </ul>
     * </p>
     * </p>
     * Selecting streams is optional, except in {@linkplain ProjectWizard project import wizard}.
     * In the {@link InstallerWizard installer wizard},
     * the <em>Skip project selection</em> check-box can be used enabled advancement to the {@link PromptPage prompt page}
     * without selecting any project streams.
     * This is useful for installing a product without workspace provisioning.
     * <p>
     * The table viewer determines which stream of the project will be provisioned.
     * The cell editor of the <em>Stream</em> column selects which specific stream of the project's available streams to provision.
     * There are two ways remove a project from the table viewer:
     * <ul>
     * <li>
     * Double-clicking the project.
     * </li>
     * <li>
     * Selecting one or more projects
     * and then clicking the <em>up arrow</em>.
     * </li>
     * </ul>
     * </p>
     */
    public static class ProjectPage
    {
    }

    /**
     * Prompt Page
     * <p>
     * The primary purpose of the prompt page is to specify the values for {@link Variable variables}.
     * At this point in the wizard's work flow,
     * a task list has been {@link TaskComposition gathered}.
     * That process induces variables and evaluates and expands variables.
     * Any variable with an empty value requires the user's input.
     * Information related to those variables is displayed on this page in a three column format:
     * <ul>
     * <li>
     * A <em>label</em> column displaying the label for the variable
     * with hover help that describes the purpose of the variable.
     * </li>
     * <li>
     * A field of some sort for specifying the value.
     * In the case of a drop-down field,
     * the hover help displays the corresponding label of the selected choice.
     * </li>
     * <li>
     * An optional action button to help specify the value.
     * </li>
     * </ul>
     * The values of the variables are {@linkplain VariableRecording recorded}.
     * The <em>Show all variables</em> check-box determines whether or not those already-recorded variables
     * are displayed on the page.
     * All variables must specify a non-empty value in order to advance to the {@link ConfirmationPage confirmation page}.
     * </p>
     */
    public static class PromptPage
    {
    }

    /**
     * Confirmation
     * <p>
     * The primary purpose of the confirmation page is to review that {@link TaskComposition gathered} task list.
     * </p>
     * <p>
     * The left check-box tree viewer of the page displays the {@link Trigger trigger}
     * with all the triggered tasks as children.
     * The <em>Show all triggered tasks</em> check-button determines whether
     * all triggered tasks are displayed
     * or only the triggered tasks that need to be performed are displayed.
     * Tasks that aren't needed will be grayed-out.
     * Using the tree's check-boxes,
     * the tasks that will actually be performed can be selectively enabled and disabled.
     * A task can be double-clicked to enable only that one task.
     * </p>
     * <p>
     * The top right tree viewer displays the nested elements of the selected task in the left check-box tree viewer.
     * Similarly,
     * The bottom right table viewer displays the properties of
     * either the selected task in the left check-box tree viewer,
     * or the selected nested element in the top right tree viewer.
     * These viewers are useful for understanding exactly what the task will do when it's performed.
     * </p>
     * </p>
     * Before proceeding the the {@link ProgressPage progress page},
     * it's important to specify the setting that affect how the tasks will be performed.
     * The <em>Offline</em> check-button determines whether Internet-hosted resources will be used
     * or whether execution will proceed to the best extent possible using already-cached resources.
     * The <em>Mirror</em> check-button determines whether Internet-hosted resource mirrors will be used
     * or whether execution will proceed using the primary Internet host.
     * These settings are particularly relevant for {@linkplain P2 p2} and {@link Targlet targlet} tasks
     * which make heavy use of p2 update sites.
     * If the progress page detects that the tasks will overwrite an existing product installation,
     * the <em>Overwrite</em> check-box will be displayed,
     * and the user must confirm that they do indeed wish to reinstall, i.e., overwrite, an existing installation.
     * </p>
     */
    public static class ConfirmationPage
    {
    }

    /**
     * Progress Page
     * <p>
     * The primary purpose of the progress page is to manage the tasks while they are {@link TaskExecution performing}.
     * </p>
     * <p>
     * The tree viewer of the page displays the tasks being performed
     * and the text viewer of the page displays a progress log.
     * The task currently being performed is automatically selected in the tree viewer.
     * While tasks are executing,
     * the bottom of the page displays a progress monitor.
     * The cancel button of that monitor can be used to interrupt task execution.
     * The text viewer automatically scrolls to the button, unless the <em>Scroll lock</em> check-box is enabled.
     * Scrolling the text viewer manually will automatically enable the scroll lock.
     * Selecting an already-performed task in the tree viewer
     * will select the text viewer's text associated with that tasks monitored progress.
     * Once task execution terminates,
     * either because it has been completed successfully, has been partially successful but requires a restart, has terminated in failure, or has been canceled,
     * the progress monitor will be hidden.
     * The page banner provides important feedback with regard to the actions to be taken upon task execution termination.
     * The <em>Dismiss automatically</em> check-box,
     * when checked,
     * will result in the automatic dismissal of the dialog when task execution terminates successfully.
     * The <em>Launch automatically</em> check-box, which is only available in the {@link InstallerWizard installer wizard},
     * when checked,
     * will result in the installed product being automatically launched when the wizard completes.
     * The <em>Restart automatically</em> check-box, which is only available in the {@link ProjectWizard project wizard} and the {@link ExecutionWizard execution wizard},
     * when checked,
     * will result in the installed product being automatically restarted when task execution terminates with partially successful results that require a restart,
     * i.e., because the product's 'ini' file has been modified or new software has been installed.
     * The restarted product will automatically open the {@link ExecutionWizard execution wizard},
     * proceeding directly to the progress page,
     * to continue performing the remaining tasks.
     * </p>
     */
    public static class ProgressPage
    {
    }
  }

  /**
   * Bundle Pool Management
   * <p>
   * </p>
   */
  public static class BundlePoolManager
  {

  }
}
