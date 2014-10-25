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

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.internal.ui.Capture;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.LocationCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.ResourceCreationTask;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.doc.Everything.Infrastructure.Index;
import org.eclipse.oomph.setup.doc.Everything.Scope.Installation;
import org.eclipse.oomph.setup.doc.Everything.Scope.ProductCatalog;
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
import org.eclipse.oomph.setup.doc.Everything.TaskComposition.ScopeList;
import org.eclipse.oomph.setup.doc.Everything.TaskComposition.TaskList.Consolidation;
import org.eclipse.oomph.setup.doc.Everything.TaskComposition.TaskList.Filter;
import org.eclipse.oomph.setup.doc.Everything.TaskComposition.TaskList.InitialPhase;
import org.eclipse.oomph.setup.doc.Everything.TaskComposition.TaskList.Reorder;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.installer.InstallerDialog;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
 * </p>
 * <p>
 * This document provides a general overview of the concepts that underly Oomph's Setup model:
 * {@link #setupOverviewDiagram()}
 * </p>
 *
 * @number 2
 */
@SuppressWarnings("restriction")
public abstract class Everything
{
  /*
   * // TODO
   * @image SetupOverview.svg
   * /org.eclipse.oomph.setup/model/Setup.aird?editor://org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID?diagram#_hZdg0dbXEeOQ1Kyul5ZC9g
   */
  protected abstract void setupOverviewDiagram();

  /*
   * @snippet tree org.eclipse.setup.tree /setups/org.eclipse.setup?prune:/org.eclipse.oomph.setup.Project
   * @title org.eclipse.setup
   */
  protected abstract void index();

  /*
   * @snippet tree Oomph.setup.tree /setups/Oomph.setup?editor://org.eclipse.oomph.setup.presentation.SetupEditorID/Outline?/org.eclipse.oomph.setup.Project
   * @title Oomph.setup
   */
  protected abstract void oomphSetup();

  /*
   * @snippet tree Setup.genmodel.tree /org.eclipse.oomph.setup/model/Setup.genmodel#//setup/SetupTask
   * /org.eclipse.oomph.setup/model/Setup.genmodel?editor://org.eclipse.emf.codegen.ecore.genmodel.presentation.GenModelEditorID
   * @title Setup.genmodel
   */
  protected abstract void setupGenModel();

  /*
   * @snippet tree Setup.ecore.tree /org.eclipse.oomph.setup/model/Setup.ecore?editor://org.eclipse.emf.ecore.presentation.EcoreEditorID
   * @title Setup.genmodel
   */
  protected abstract void setupEcoreModel();

  /*
   * @snippet tree PackageExplorer.tree
   * viewer://org.eclipse.jdt.ui.PackageExplorer?/org.eclipse.jdt.core:org.eclipse.jdt.internal.core.JarPackageFragmentRoot/org
   * .eclipse.jdt.core:org.eclipse.jdt.internal.core.ExternalPackageFragmentRoot
   * @title Package Explorer
   */
  protected abstract void packageExplorer();

  /*
   * @image PackageExplorerView.jpg viewer://org.eclipse.jdt.ui.PackageExplorer
   */
  protected abstract void packageExplorerView();

  /*
   * @image SetupOverviewEditor.png
   * /org.eclipse.oomph.setup/model/Setup.aird?editor://org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditorID#_hZdg0dbXEeOQ1Kyul5ZC9g
   */
  protected abstract void setupOverviewDiagramEditor();

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
       * at which point the {@linkplain ExecutionWizard execution wizard} will be opened on the {@link ConfirmationPage progressPage page}.
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
   * Scope are hierarchically structured and tasks are {@linkplain ScopeList#scopeList gathered} from them based on this hierarchical structure.
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
     * The {@link org.eclipse.oomph.setup.CatalogSelection#getDefaultStreams() most recently selected} {@link Stream stream} of each {@linkplain Project project} ever chosen in the {@linkplain ProjectPage project  page}.
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
   * these resources are stored in XMI format,
   * for example,
   * The {@link IndexResource index resource} is serialized as follows:
   * {@link #indexXMI()}
   * </p>
   */
  public abstract static class SetupResource
  {
    /**
     * @snippet xml org.eclipse.setup /setups/org.eclipse.setup
     */
    public abstract void indexXMI();

    /**
     * Index
     * <p>
     * The <code><a href="http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/org.eclipse.setup">index:/org.eclipse.setup</a></code> resource maintains the {@linkplain Index index},
     * where <code>index:/</code> is redirected to <code>http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/</code>.
     * It generally consists of references to {@link ProductCatalogResource product catalog} resources and {@link ProjectCatalogResource project catalog} resources.
     * {@link #index()}
     * </p>
     */
    public abstract static class IndexResource
    {
      /**
       * @snippet tree org.eclipse.setup.tree /setups/org.eclipse.setup?prune:/org.eclipse.oomph.setup.Project#/
       * @title org.eclipse.setup
       * @expandTo 1
       */
      protected abstract void index();

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
       * The copying process takes the task-to-task {@link TaskList#substitutions substitution} map into account,
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
   * This list is presented to the user on the setup wizard's {@link ConfirmationPage progressPage page}.
   * Initially,
   * each task in the list is visited
   * to determine if it needs to perform.
   * That determination is generally influenced by the {@link Trigger trigger}.
   * Tasks that don't need to perform are logically removed from the list
   * to induce the so called needed task list.
   * The progressPage page allows the user to see either the full list or the needed task list.
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
   * These wizards reuse the same underlying pages as follows:
   * <figure>
   * {@image WizardDiagram.svg}
   * </figure>
   * They makes heavy use of Internet-hosted {@linkplain SetupResource resources},
   * but these resources are cached locally on each download,
   * so once cached,
   * the wizards can function offline based on these cached instances.
   * </p>
   */
  public abstract static class SetupWizard
  {
    /**
     * @snippet image WizardFooter.images
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
      InstallerWizard.CaptureInstallerWizard footer = InstallerWizard.CaptureInstallerWizard.getInstance();
      return new Image[] { null, footer.helpImage, footer.backImage, footer.nextImage, footer.finishImage, footer.cancelImage };
    }

    /**
     * @snippet html
     * @description
     * Brings up this help.
     */
    public abstract void helpDescription();

    /**
     * @snippet html
     * @description
     * Navigates to the previous page.
     */
    public abstract void backDescription();

    /**
     * @snippet html
     * @description
     * Navigates to the next page.
     */
    public abstract void nextDescription();

    /**
     * @snippet html
     * @description
     * Completes the wizard, performing any final actions.
     */
    public abstract void finishDescription();

    /**
     * @snippet html
     * @description
     * Closes the wizard, taking no further action.
     */
    public abstract void cancelDescription();

    /**
     * @ignore
     */
    public static abstract class CaptureSetupWizard extends Capture.Window<WizardDialog>
    {
      protected org.eclipse.oomph.setup.ui.wizards.SetupWizard getSetupWizard(WizardDialog wizardDialog)
      {
        return (org.eclipse.oomph.setup.ui.wizards.SetupWizard)wizardDialog.getCurrentPage().getWizard();
      }

      protected SetupContext getSetupContext(WizardDialog wizardDialog)
      {
        return getSetupWizard(wizardDialog).getSetupContext();
      }

      protected ResourceSet getResourceSet(WizardDialog wizardDialog)
      {
        return getSetupWizard(wizardDialog).getResourceSet();
      }

      protected SetupTaskPerformer getPerformer(WizardDialog wizardDialog)
      {
        return getSetupWizard(wizardDialog).getPerformer();
      }

      protected void advanceToNextPage(WizardDialog wizardDialog)
      {
        ((org.eclipse.oomph.setup.ui.wizards.SetupWizardPage)wizardDialog.getCurrentPage()).advanceToNextPage();
      }

      @SuppressWarnings("unchecked")
      protected <T extends Viewer> T getViewer(WizardDialog wizardDialog, String fieldName)
      {
        return (T)ReflectUtil.getValue(fieldName, wizardDialog.getCurrentPage());
      }

      @SuppressWarnings("unchecked")
      protected <T extends Control> T getViewerControl(WizardDialog wizardDialog, String fieldName)
      {
        return (T)getViewer(wizardDialog, fieldName).getControl();
      }

      protected void postProcessProductPage(WizardDialog wizardDialog)
      {
        getViewerControl(wizardDialog, "productViewer").setFocus();

        ResourceSet resourceSet = getResourceSet(wizardDialog);
        ProductVersion luna = (ProductVersion)resourceSet
            .getEObject(
                URI.createURI("index:/org.eclipse.setup#//@productCatalogs[name='org.eclipse.products']/@products[name='epp.package.standard']/@versions[name='luna']"),
                false);

        TreeViewer productViewer = getViewer(wizardDialog, "productViewer");
        productViewer.setSelection(new StructuredSelection(luna.getProduct()));

        AccessUtil.busyWait(10);

        ComboViewer versionComboViewer = getViewer(wizardDialog, "versionComboViewer");
        versionComboViewer.setSelection(new StructuredSelection(luna));

        ComboViewer poolComboViewer = getViewer(wizardDialog, "poolComboViewer");
        poolComboViewer.getCombo().select(0);

        Link link = getWidget(wizardDialog, "version");
        link.setText("<a>1.0.0 Build 1234</a>");
        link.getParent().layout(true);
      }

      protected void postProcessProjectPage(WizardDialog wizardDialog)
      {
        ResourceSet resourceSet = getResourceSet(wizardDialog);
        org.eclipse.oomph.setup.ProjectCatalog projectCatalog = (org.eclipse.oomph.setup.ProjectCatalog)resourceSet
            .getResource(URI.createURI("index:/org.eclipse.projects.setup"), false).getContents().get(0);

        TreeViewer projectViewer = getViewer(wizardDialog, "projectViewer");
        projectViewer.getControl().setFocus();

        for (Iterator<org.eclipse.oomph.setup.Project> it = projectCatalog.getProjects().iterator(); it.hasNext();)
        {
          org.eclipse.oomph.setup.Project project = it.next();
          String label = project.getLabel();
          if (!"Oomph".equals(label))
          {
            if (!"<User>".equals(label))
            {
              it.remove();
            }
          }
          else
          {
            projectViewer.setSelection(new StructuredSelection(project));
          }
        }

        ReflectUtil.invokeMethod(ReflectUtil.getMethod(projectViewer, "fireDoubleClick", DoubleClickEvent.class), projectViewer, (Object)null);
      }

      protected void postProcessPromptPage(WizardDialog wizardDialog, String installationID)
      {
        List<EObject> objectsToRemove = new ArrayList<EObject>();
        for (TreeIterator<EObject> it = getSetupContext(wizardDialog).getUser().eAllContents(); it.hasNext();)
        {
          EObject eObject = it.next();
          if (eObject instanceof SetupTask && !(eObject instanceof CompoundTask || eObject instanceof VariableTask))
          {
            objectsToRemove.add(eObject);
            it.prune();
          }
        }

        for (EObject eObject : objectsToRemove)
        {
          EcoreUtil.remove(eObject);
        }

        Button showAllButton = getWidget(wizardDialog, "showAll");
        showAllButton.setSelection(true);
        showAllButton.notifyListeners(SWT.Selection, new Event());
        AccessUtil.busyWait(100);

        Text text = getWidget(wizardDialog, "installation.id.control");
        text.setText(installationID);
        text.notifyListeners(SWT.Modify, null);

        Combo combo = getWidget(wizardDialog, "git.clone.oomph.remoteURI.control");
        combo.setText("ssh://${git.user.id}@git.eclipse.org:29418/oomph/org.eclipse.oomph");
        combo.notifyListeners(SWT.Modify, null);
        AccessUtil.busyWait(100);
      }

      protected void postProcessConfirmationPage(WizardDialog wizardDialog)
      {
        AccessUtil.busyWait(100);

        CheckboxTreeViewer taskViewer = getViewer(wizardDialog, "viewer");
        taskViewer.getControl().setFocus();

        Button showAll = getWidget(wizardDialog, "showAllTasks");
        showAll.setSelection(true);
        showAll.notifyListeners(SWT.Selection, new Event());

        Button overwrite = getWidget(wizardDialog, "overwrite");
        overwrite.setSelection(true);
        overwrite.notifyListeners(SWT.Selection, new Event());
        AccessUtil.busyWait(10);

        {
          ITreeContentProvider provider = (ITreeContentProvider)taskViewer.getContentProvider();
          Object[] children = provider.getChildren(taskViewer.getInput());
          for (Object object : provider.getChildren(children[0]))
          {
            if (object instanceof P2Task)
            {
              taskViewer.setSelection(new StructuredSelection(object));
              break;
            }
          }
        }

        AccessUtil.busyWait(10);

        TreeViewer childrenViewer = getViewer(wizardDialog, "childrenViewer");
        childrenViewer.getControl().setFocus();

        {
          ITreeContentProvider provider = (ITreeContentProvider)childrenViewer.getContentProvider();
          Object[] children = provider.getChildren(childrenViewer.getInput());
          childrenViewer.setSelection(new StructuredSelection(children[0]));
        }

        AccessUtil.busyWait(10);
      }

      protected Image getCalloutImage(int index)
      {
        Image image = ExtendedImageRegistry.INSTANCE.getImage(URI
            .createPlatformPluginURI("org.eclipse.oomph.setup.doc/images/callout-" + index + ".png", false));
        return new Image(image.getDevice(), image.getImageData());
      }
    }

    /**
     * Installer Wizard
     * <p>
     * The installer wizard is the basis for Oomph's bootstrap-{@link Trigger triggered}, automated installation and provisioning process.
     * {@link #installer()}
     * </p>
     * <p>
     * {@link #installerFooter()}
     * </p>
     * <p>
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
     * <a rel="nofollow" class="external log" href="http://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-linux.gtk.x86_64.zip">Linux 64 bit</a>
     * </li>
     * <li>
     * <a rel="nofollow" class="external log" href="http://download.eclipse.org/oomph/products/org.eclipse.oomph.setup.installer.product-linux.gtk.x86.zip">Linux 32 bit</a>
     * </li>
     * </ul>
     * This download can be "installed" just like <a href="https://wiki.eclipse.org/Eclipse/Installation">Eclipse itself</a>.
     * </p>
     */
    public static class InstallerWizard
    {
      /**
       * @snippet image InstallerFooter.images
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
       * Brings up the equivalent of Eclipse's <em>Window->Preferences...->General->Network Connections</em> preferences.
       * Configuration the network proxies is a necessary first step for users working in an environment behind a fire wall,
       * none of Oomph's Internet-hosed resources will be accessible without that configuration.
       * Note that all the configured network settings will be propagated to the installed product.
       * @callout
       * Brings up the equivalent of Eclipse's <em>Window->Preferences...->General->Network Connections->SSH2</em> preferences.
       * Confirming the SSH2 settings during initial installation is a good idea when using any technologies that are based on SSH access,
       * i.e., if you plan to clone Git projects via public-key encryption.
       * Note that all the configured SSH2 settings will be propagated to the installed product.
       * @callout
       * Updates the installer itself to the last Internet-hosted version.
       * The wizard always checks whether updates are available.
       * If so, the button is animated, otherwise it's disabled.
       * @callout
       * Indicates which version, along with which build of that version, you're currently using.
       * This is particularly useful when reporting problems.
       */
      public static Image[] installerFooter()
      {
        InstallerWizard.CaptureInstallerWizard instance = InstallerWizard.CaptureInstallerWizard.getInstance();
        return new Image[] { null, instance.helpImage, instance.backImage, instance.nextImage, instance.finishImage, instance.cancelImage, instance.proxyImage,
            instance.sshImage, instance.updateImage, instance.versionImage };
      }

      /**
       * @ignore
       */
      private static class CaptureInstallerWizard extends CaptureSetupWizard
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
       * @snippet image InstallerWizard.images
       */
      public static Image installer()
      {
        return CaptureInstallerWizard.getInstance().dialogImage;
      }
    }

    /**
     * Project Import Wizard
     * <p>
     * The project wizard is the basis for Oomph's manually-{@link Trigger triggered}, updates to the automated installation and provisioning process.
     * {@link #projectImporter()}
     * <p>
     * {@link #wizardFooter()}
     * </p>
     * <p>
     * The project wizard is available in any running Eclipse product with Oomph installed
     * via the <em>File->Import...->Oomph->Projects into Workspace</em>
     * and the <em>execution</em> tool bar button.
     * Its purpose is to augment the {@link WorkspaceResource workspace}'s set of {@link Stream streams}.
     * </p>
     */
    public static class ProjectWizard
    {
      /**
       * @snippet image ProjectImportWizard.images
       */
      public static Image projectImporter()
      {
        return CaptureProjectWizard.getInstance().projectWizard;
      }

      /**
       * @ignore
       */
      public static class CaptureProjectWizard extends CaptureSetupWizard
      {
        private static CaptureProjectWizard instance;

        public Image projectWizard;

        public static CaptureProjectWizard getInstance()
        {
          if (instance == null)
          {
            instance = new CaptureProjectWizard();
            instance.projectWizard = instance.capture();
          }
          return instance;
        }

        @Override
        protected WizardDialog create(Shell shell)
        {
          return new WizardDialog(shell, new org.eclipse.oomph.setup.ui.wizards.SetupWizard.Importer());
        }

        @Override
        protected void postProcess(WizardDialog wizardDialog)
        {
          super.postProcess(wizardDialog);

          postProcessProjectPage(wizardDialog);
        }
      }
    }

    /**
     * Execution Wizard
     * <p>
     * The execution wizard is the basis for Oomph's startup- and manually-{@link Trigger triggered}, automated installation and provisioning process.
     * {@link #updater()}
     * </p>
     * <p>
     * {@link #wizardFooter()}
     * </p>
     * <p>
     * The execution wizard is also used for startup-triggered, installation and provisioning,
     * i.e., when the Eclipse product starts
     * and the <em>Window->Preferences...->Oomph->Setup->Skip automatic task execution at startup time</em> preference permits,
     * tasks are {@linkplain TaskComposition gathered},
     * and if any tasks {@linkplain TaskExecution need to perform},
     * the execution wizard is opened on the {@link ConfirmationPage progressPage page}.
     * </p>
     */
    public static class ExecutionWizard
    {
      /**
       * @image UpdaterWizard.png invoke://
       */
      public static Image updater()
      {
        return new Capture.Window<WizardDialog>()
        {
          @Override
          protected WizardDialog create(Shell shell)
          {
            return new WizardDialog(shell, new org.eclipse.oomph.setup.ui.wizards.SetupWizard.Updater(true));
          }
        }.capture();
      }
    }

    /**
     * Product Page
     * <p>
     * The primary purpose of the product page is to create an {@linkplain InstallationResource installation}
     * with a specific {@link Version product version}.
     * Its secondary purpose is to manage Oomph's {@linkplain BundlePool bundle pools}.
     * The product page is used exclusively by the {@linkplain Installation installation wizard}.
     * {@link #productPage()}
     * </p>
     */
    public static class ProductPage
    {
      /**
       * @snippet image ProductPage.images
       * @style box
       * @description The page contains the following controls:
       * @callout
       * Filters which of the {@link Product products} are displayed.
       * @callout
       * Collapses the tree;
       * initially the tree is fully expanded.
       * @callout
       * Updates the locally-cached versions of all the {@link SetupResource resources} used in the wizard.
       * @callout
       * Chooses which of the product catalogs available in the {@link Index index} to display.
       * @callout
       * Displays the {@linkplain CatalogSelection selected} {@linkplain ProductCatalog product catalogs}.
       * Double-clicking a product automatically advances to the {@linkplain ProjectPage project page}.
       * @callout
       * Displays the p2 installable unit ID of the selected product.
       * @callout
       * Displays the p2 installable unit name of the selected product.
       * @callout
       * Displays the currently selected product version.
       * @callout
       * Determines which specific version of the available product versions to install.
       * @callout
       * Determines whether the installation will use {@link BundlePool bundle pools}.
       * @callout
       * Determines which specific bundle pool to use.
       * @callout
       * Brings up the {@linkplain BundlePoolManager bundle pool management} dialog.
       */
      public static Image[] productPage()
      {
        InstallerWizard.CaptureInstallerWizard instance = InstallerWizard.CaptureInstallerWizard.getInstance();
        return new Image[] { instance.productPage, instance.filterImage, instance.collapseImage, instance.refreshImage, instance.catalogsImage,
            instance.treeImageDecoration, instance.productIDImage, instance.productNameImage, instance.productVersionImage, instance.versionChoiceImage,
            instance.poolsImage, instance.poolChoiceImage, instance.managePoolsImage };
      }
    }

    /**
     * Project Page
     * <p>
     * The primary purpose of the project page is to create (or update) a {@linkplain WorkspaceResource workspace}
     * with one or more specific {@link Stream project streams}.
     * {@link #projectPage()}
     * </p>
     */
    public abstract static class ProjectPage
    {
      /**
       * @snippet image ProjectPage.images
       * @style box
       * @description
       * The page contains the following controls:
       * @callout
       * Filters which of the {@link Project projects} are displayed.
       * @callout
       * Adds a project to the &lt;User> project of selected {@link ProjectCatalogResource project catalog}.
       * If the &lt;User> project is empty, it's not displayed in the tree,
       * but you can still add a project to the catalog,
       * and then it will be displayed.
       * @callout
       * Removes the selected project from the<&lt;User> project.
       * It is only enabled for a project within a &lt;User> project.
       * @callout
       * Collapses the tree;
       * initially the tree roots are expanded.
       * @callout
       * Updates the locally-cached versions of all the {@link SetupResource resources} used in the wizard.
       * @callout
       * Chooses which of the project catalogs available in the {@link Index index} to display.
       * @callout
       * Displays the {@linkplain CatalogSelection selected} {@linkplain ProductCatalog project catalogs}.
       * Double-clicking a project automatically adds its stream to the table of chosen streams,
       * or, if one of it's streams is already in the table of chosen streams,
       * removes it from the table.
       * The project of the chosen streams are shown in bold font.
       * Of course only a project with at least one stream can be added to the table of chosen streams.
       * For projects without streams,
       * presumably projects that contain only nested projects,
       * double-clicking expands or collapses the project in the tree.
       * @callout
       * Adds the steams of selected projects of the tree to the table of chosen streams.
       * @callout
       * Removes the selected streams of the table of chosen streams from the table,
       * and leaves them their corresponding projects selected in the tree.
       * @callout
       * Displays the chosen streams.
       * Double clicking removes the chosen stream from the table
       * and leaves its corresponding project selected in the tree.
       * The stream column supports cell editing:
       * use it to choose which stream of the project's available streams to provision.
       * @callout
       * Determines whether any streams are to be provisioned.
       * This control is visible only in the {@link InstallerWizard installer} wizard.
       * When enabled,
       * any selected streams are removed from the table
       * and the wizard's next button is enabled to proceed
       * without choosing any streams to provision.
       */
      public static Image[] projectPage()
      {
        CaptureProjectPage instance = CaptureProjectPage.getInstance();
        return new Image[] { instance.projectPage, instance.filter, instance.addProject, instance.removeProject, instance.collapse, instance.refresh,
            instance.catalogs, instance.treeImageDecoration, instance.choose, instance.unchoose, instance.tableImageDecoration, instance.skip };
      }

      /**
       * @ignore
       */
      public static class CaptureProjectPage extends CaptureSetupWizard
      {
        private static CaptureProjectPage instance;

        private Image addProject;

        private Image removeProject;

        private Image collapse;

        private Image refresh;

        private Image catalogs;

        private Image treeImageDecoration;

        private Image projectPage;

        private Image filter;

        private Image choose;

        private Image unchoose;

        private Image tableImageDecoration;

        private Image skip;

        public static CaptureProjectPage getInstance()
        {
          if (instance == null)
          {
            instance = new CaptureProjectPage();
            instance.projectPage = instance.capture();
          }
          return instance;
        }

        @Override
        protected WizardDialog create(Shell shell)
        {
          return new InstallerDialog(shell, false);
        }

        @Override
        protected void postProcess(WizardDialog wizardDialog)
        {
          super.postProcess(wizardDialog);

          postProcessProductPage(wizardDialog);

          advanceToNextPage(wizardDialog);

          postProcessProjectPage(wizardDialog);
        }

        @Override
        protected Image capture(WizardDialog wizardDialog)
        {
          IWizardPage page = wizardDialog.getCurrentPage();

          treeImageDecoration = getCalloutImage(1);
          Control tree = getViewerControl(wizardDialog, "projectViewer");

          tableImageDecoration = getCalloutImage(2);
          Control table = getViewerControl(wizardDialog, "streamViewer");
          Event event = new Event();
          event.button = 1;
          event.count = 1;
          event.type = 3;
          event.x = 605;
          event.y = 40;
          table.notifyListeners(SWT.MouseDown, event);
          AccessUtil.busyWait(100);

          Map<Control, Image> decorations = new LinkedHashMap<Control, Image>();
          decorations.put(tree, treeImageDecoration);
          decorations.put(table, tableImageDecoration);
          Image result = capture(page, decorations);

          filter = getImage(wizardDialog, "filter");

          addProject = getImage(wizardDialog, "addProject");
          removeProject = getImage(wizardDialog, "removeProject");
          collapse = getImage(wizardDialog, "collapse");
          refresh = getImage(wizardDialog, "refresh");
          catalogs = getImage(wizardDialog, "catalogs");

          choose = getImage(wizardDialog, "choose");
          unchoose = getImage(wizardDialog, "unchoose");

          skip = getImage(wizardDialog, "skip");

          return result;
        }
      }
    }

    /**
     * Prompt Page
     * <p>
     * The primary purpose of the prompt page is to specify the values for {@link Variable variables}.
     * At this point in the wizard's work flow,
     * a task list has been {@link TaskComposition gathered}.
     * That process induces variables and evaluates and expands variables.
     * Any variable with an empty value requires the user's input.
     * All variables must specify a non-empty value in order to advance to the {@link ConfirmationPage progressPage page}.
     * Information related to those variables is displayed on this page in a three column format:
     * {@link #promptPage()}
     * </p>
     */
    public static class PromptPage
    {
      /**
       * @snippet image PromptPage.images
       * @style box
       * @description
       * The page has the following controls:
       * @callout
       * Displays the label for the rule that determines where installations are installed.
       * @callout
       * Displays the value for the installation location rule.
       * In this case, each installation will end up in a uniquely named subfolder of the specified root folder.
       * @callout
       * Displays the label for the uniquely named subfolder for the installation.
       * @callout
       * Displays the value for the subfolder name.
       * @callout
       * Displays the label for the root folder for all installations.
       * @callout
       * Displays the value for the root folder.
       * In this case the installation will end up in the 'D:/sandbox/oomph' folder.
       * @callout
       * Browses the file system for a root folder location.
       * @callout
       * Displays the label for the rule that determines where workspaces are provisioned.
       * @callout
       * Displays the value of the workspace location rule.
       * In this case, the workspace will be located in a folder named 'ws' nested in the installation folder.
       * @callout
       * Displays the label for the rule that determines where Git clones will be provisioned.
       * @callout
       * Displays the value of the git location rule.
       * In this case the clone is stored in the "git" subfolder of the installation folder with a name derived from the repository URI.
       * @callout
       * Displays the label for the target platform choice.
       * @callout
       * Displays the value for the target platform.
       * Project authors are encouraged to make use of this common variable so that multiple projects will materialize a cohesive target platform.
       * @callout
       * Displays the label for the choice of Oomph's Git remote URI.
       * There are typically several different URIs for accessing the same underlying repository
       * depending on whether one wants Gerrit access or direct Git access,
       * whether ones wants to use SSH, HTTPS, or anonymous access.
       * @callout
       * Displays the value for the remote URI choice.
       * In this case, Gerrit access via SSH is chosen.
       * @callout
       * Displays the label for the JRE location.
       * Standard variables are defined for various levels of the JDK.
       * @callout
       * Displays the value for the JRE location.
       * The value specicified should be compatible with the verion specified in the label.
       * Generally a JDK is preferred over a JRE.
       * In this case, a Java 1.7 JDK is specified.
       * @callout
       * Browses the file system for a JRE or JDK location.
       * @callout
       * Displays the label for the Bugzilla/Hudson ID.
       * @callout
       * Displays the value for the ID.
       * This is generally an email address.
       * If one doesn't have such a registered ID, 'anonymous' should be specified.
       * @callout
       * Displays the label for the Eclipse password.
       * @callout
       * Displays the obscurred value of the password.
       * @callout
       * Authenticates that the password is valid with respect to the Bugzilla/Hudson ID and the Git/ID.
       * @callout
       * Displays the label for the Git/Gerrit user ID.
       * @callout
       * Displays the value of the ID.
       * If one doesn't have such a registered ID, 'anonymous' should be specified.
       * @callout
       * Determines whether all variables are displayed
       * or just the ones that are strictly required to proceed.
       * In this case, all variables are being shown.
       */
      public static Image[] promptPage()
      {
        CapturePromptPage instance = CapturePromptPage.getInstance();
        return new Image[] { instance.progressPage, instance.installationRuleLabel, instance.installationRuleControl, instance.installationIDLabel,
            instance.installationIDControl, instance.installationRootLabel, instance.installationRootControl, instance.installationRootHelper,
            instance.workspaceRuleLabel, instance.workspaceRuleControl, instance.gitCloneRuleLabel, instance.gitCloneRuleControl, instance.targetPlatformLabel,
            instance.targetPlatformControl, instance.oomphRemoteURILabel, instance.oomphRemoteURIControl, instance.jreLocationLabel,
            instance.jreLocationControl, instance.jreLocationHelper, instance.bugzillaLabel, instance.bugzillaControl, instance.passwordLabel,
            instance.passwordControl, instance.passwordHelper, instance.userIDLabel, instance.userIDControl, instance.showAll };
      }

      /**
       * @ignore
       */
      public static class CapturePromptPage extends CaptureSetupWizard
      {
        private static CapturePromptPage instance;

        private Image progressPage;

        private Image showAll;

        private Image installationIDLabel;

        private Image installationIDControl;

        private Image gitCloneRuleLabel;

        private Image gitCloneRuleControl;

        private Image installationRuleLabel;

        private Image installationRuleControl;

        private Image installationRootLabel;

        private Image installationRootControl;

        private Image workspaceRuleLabel;

        private Image workspaceRuleControl;

        private Image installationRootHelper;

        private Image targetPlatformLabel;

        private Image targetPlatformControl;

        private Image oomphRemoteURILabel;

        private Image oomphRemoteURIControl;

        private Image jreLocationLabel;

        private Image jreLocationControl;

        private Image jreLocationHelper;

        private Image bugzillaLabel;

        private Image bugzillaControl;

        private Image passwordLabel;

        private Image passwordControl;

        private Image passwordHelper;

        private Image userIDLabel;

        private Image userIDControl;

        public static CapturePromptPage getInstance()
        {
          if (instance == null)
          {
            instance = new CapturePromptPage();
            instance.progressPage = instance.capture();
          }
          return instance;
        }

        @Override
        protected WizardDialog create(Shell shell)
        {
          return new InstallerDialog(shell, false);
        }

        @Override
        protected void postProcess(WizardDialog wizardDialog)
        {
          super.postProcess(wizardDialog);

          postProcessProductPage(wizardDialog);

          advanceToNextPage(wizardDialog);

          postProcessProjectPage(wizardDialog);

          advanceToNextPage(wizardDialog);

          postProcessPromptPage(wizardDialog, "oomph");
        }

        @Override
        protected Image capture(WizardDialog wizardDialog)
        {
          IWizardPage page = wizardDialog.getCurrentPage();

          Image result = capture(page, null);

          installationRuleLabel = getImage(wizardDialog, "InstallationTask.label");
          installationRuleControl = getImage(wizardDialog, "InstallationTask.control");

          installationIDLabel = getImage(wizardDialog, "installation.id.label");
          installationIDControl = getImage(wizardDialog, "installation.id.control");

          installationRootLabel = getImage(wizardDialog, "install.root.label");
          installationRootControl = getImage(wizardDialog, "install.root.control");
          installationRootHelper = getImage(wizardDialog, "install.root.helper");

          workspaceRuleLabel = getImage(wizardDialog, "WorkspaceTask.label");
          workspaceRuleControl = getImage(wizardDialog, "WorkspaceTask.control");

          gitCloneRuleLabel = getImage(wizardDialog, "GitCloneTask.label");
          gitCloneRuleControl = getImage(wizardDialog, "GitCloneTask.control");

          targetPlatformLabel = getImage(wizardDialog, "eclipse.target.platform.label");
          targetPlatformControl = getImage(wizardDialog, "eclipse.target.platform.control");

          oomphRemoteURILabel = getImage(wizardDialog, "git.clone.oomph.remoteURI.label");
          oomphRemoteURIControl = getImage(wizardDialog, "git.clone.oomph.remoteURI.control");

          jreLocationLabel = getImage(wizardDialog, "jre.location-1.5.label");
          jreLocationControl = getImage(wizardDialog, "jre.location-1.5.control");
          jreLocationHelper = getImage(wizardDialog, "jre.location-1.5.helper");

          bugzillaLabel = getImage(wizardDialog, "bugzilla.id.label");
          bugzillaControl = getImage(wizardDialog, "bugzilla.id.control");

          passwordLabel = getImage(wizardDialog, "eclipse.user.password.label");
          passwordControl = getImage(wizardDialog, "eclipse.user.password.control");
          passwordHelper = getImage(wizardDialog, "eclipse.user.password.helper");

          userIDLabel = getImage(wizardDialog, "git.user.id.label");
          userIDControl = getImage(wizardDialog, "git.user.id.control");

          showAll = getImage(wizardDialog, "showAll");

          return result;
        }
      }
    }

    /**
     * Confirmation
     * <p>
     * The primary purpose of the progressPage page is to review that {@link TaskComposition gathered} task list.
     * <br>
     * {@link #confirmationPage()}
     * </p>
     */
    public static class ConfirmationPage
    {
      /**
       * @snippet image ConfirmationPage.images
       * @style box
       * @description
       * The page has the following controls:
       * @callout
       * Displays the tasks to be performed.
       * The root object shows the trigger that started the wizard.
       * The check boxes allow tasks to be selectively chosen for execution.
       * Double clicking a task will enable that task and disable all other tasks.
       * The view can either show all tasks, including the ones that don't need to perform,
       * or only the tasks that need to be performed.
       * Tasks that don't need to perform are shown grayed out.
       * Selecting a task will display its contained children in the nested elements viewer
       * and will display its properties in the properties viewer.
       * @callout
       * Displays the nested children of the selected tasks in the tasks viewer.
       * Selecting a child will display its properties in the properties viewer.
       * @callout
       * Displays the properties of the selected task or the selected nested element.
       * @callout
       * Determines whether to show all tasks or only the tasks that need to perform.
       * @callout
       * Determines whether the installation and provisioning process will,
       * as much as possible,
       * proceed using locally cached resources rather than using internet access to get the latest information.
       * @callout
       * Determines whether p2 mirrors will be used during installation and provisioning,
       * or just the primary internet host.
       * This setting is particularly relevant for {@linkplain P2 p2} and {@link Targlet targlet} tasks
       * which make heavy use of p2 update sites.
       * @callout
       * Determines whether to ovewrite and existing installation.
       * This is displayed only in the installer wizard
       * when it detects an attempt to install into a location where an installation already exists.
       * In this case, the title area will display and error,
       * and it's only possible to proceed if one elects to reinstall, i.e., overwrite an existing installation.
       */
      public static Image[] confirmationPage()
      {
        CaptureConfirmationPage instance = CaptureConfirmationPage.getInstance();
        return new Image[] { instance.confirmationPage, instance.viewerDecoration, instance.childrenViewerDecoration, instance.propertiesViewerDecoration,
            instance.showAll, instance.offline, instance.mirrors, instance.overwrite };
      }

      /**
       * @ignore
       */
      public static class CaptureConfirmationPage extends CaptureSetupWizard
      {
        private static CaptureConfirmationPage instance;

        private Image confirmationPage;

        private Image showAll;

        private Image offline;

        private Image mirrors;

        private Image viewerDecoration;

        private Image childrenViewerDecoration;

        private Image propertiesViewerDecoration;

        private Image overwrite;

        public static CaptureConfirmationPage getInstance()
        {
          if (instance == null)
          {
            instance = new CaptureConfirmationPage();
            instance.confirmationPage = instance.capture();
          }
          return instance;
        }

        @Override
        protected WizardDialog create(Shell shell)
        {
          return new InstallerDialog(shell, false);
        }

        @Override
        protected void postProcess(WizardDialog wizardDialog)
        {
          super.postProcess(wizardDialog);

          postProcessProductPage(wizardDialog);

          advanceToNextPage(wizardDialog);

          postProcessProjectPage(wizardDialog);

          advanceToNextPage(wizardDialog);

          postProcessPromptPage(wizardDialog, "oomph");

          advanceToNextPage(wizardDialog);

          postProcessConfirmationPage(wizardDialog);
        }

        @Override
        protected Image capture(WizardDialog wizardDialog)
        {
          IWizardPage page = wizardDialog.getCurrentPage();

          Map<Control, Image> decorations = new LinkedHashMap<Control, Image>();
          viewerDecoration = getCalloutImage(1);
          Control viewer = getViewerControl(wizardDialog, "viewer");
          decorations.put(viewer, viewerDecoration);

          childrenViewerDecoration = getCalloutImage(2);
          Control childrenViewer = getViewerControl(wizardDialog, "childrenViewer");
          decorations.put(childrenViewer, childrenViewerDecoration);

          propertiesViewerDecoration = getCalloutImage(3);
          Control propertiesViewer = getViewerControl(wizardDialog, "propertiesViewer");
          decorations.put(propertiesViewer, propertiesViewerDecoration);

          Image result = capture(page, decorations);

          showAll = getImage(wizardDialog, "showAllTasks");
          offline = getImage(wizardDialog, "offline");
          mirrors = getImage(wizardDialog, "mirrors");
          overwrite = getImage(wizardDialog, "overwrite");

          return result;
        }
      }
    }

    /**
     * Progress Page
     * <p>
     * The primary purpose of the progress page is to manage the tasks while they are {@link TaskExecution performing}.
     * {@link #progressPage()}
     * </p>
     */
    public static class ProgressPage
    {
      /**
       * @snippet image ProgressPage.images
       * @style box
       * @description
       * The page contains the following controls:
       * @callout
       * Displays the tasks being performed.
       * The task currently being performed is automatically selected in this view.
       * A user's selection in this view selects the corresponding logged output associated with the selected task.
       * @callout
       * Displays a progress log of the tasks being performed.
       * It scrolls automatically unless that is disabled.
       * If the user scrolls this view, automatic scrolling is be disabled.
       * @callout
       * Determines whether the log automatically scrolls.
       * @callout
       * Determines whether the wizard is automatically dismissed when task execution complete successfully.
       * @callout
       * Determines with the installed product is automatically launched upon successful completion,
       * or, if not already launched, when the wizard is finished.
       * This control is available only in the installer wizard.
       * @callout
       * Determines whether the IDE will automatically restart if needed upon successful completion, e.g., if new bundles are installed.
       * Tasks that may require a restart are generally performed early,
       * and once those types of tasks are completed,
       * the IDE needs to be restarted before the remaining tasks will be performered.
       * That can either happen automatically or the user will be prompted to restart.
       * After the IDE restarts,
       * it will automatically being performing the remaining tasks via the updater wizard.
       * This control is only available in the importer and updater wizards,
       * i.e., in a running IDE.
       * @callout
       * Displays graphically the overall progress of the tasks.
       * Task execution can be canceled via this control.
       * All the wizard's navigation controls are disabled while tasks are performing.
       * Once task execution terminates,
       * either because it has been completed successfully, has been partially successful but requires a restart, has terminated in failure, or has been canceled,
       * the progress monitor will be hidden.
       * The page banner provides important feedback with regard to the actions to be taken upon task execution termination.
       */
      public static Image[] progressPage()
      {
        CaptureProgressPage instance = CaptureProgressPage.getInstance();
        return new Image[] { instance.progressPage, instance.tree, instance.log, instance.lock, instance.dismiss, instance.launch, instance.restart,
            instance.progress };
      }

      /**
       * @ignore
       */
      public static class CaptureProgressPage extends CaptureSetupWizard
      {
        private static CaptureProgressPage instance;

        private Image progressPage;

        private Image tree;

        private Image log;

        private Image lock;

        private Image dismiss;

        private Image launch;

        private Image progress;

        private Image restart;

        public static CaptureProgressPage getInstance()
        {
          if (instance == null)
          {
            instance = new CaptureProgressPage();
            instance.progressPage = instance.capture();
          }
          return instance;
        }

        @Override
        protected WizardDialog create(Shell shell)
        {
          return new InstallerDialog(shell, false);
        }

        @Override
        protected void postProcess(WizardDialog wizardDialog)
        {
          super.postProcess(wizardDialog);

          postProcessProductPage(wizardDialog);

          advanceToNextPage(wizardDialog);

          postProcessProjectPage(wizardDialog);

          advanceToNextPage(wizardDialog);

          postProcessPromptPage(wizardDialog, "tmp\\oomph.capture");

          advanceToNextPage(wizardDialog);

          SetupTaskPerformer performer = getPerformer(wizardDialog);
          File installationLocation = performer.getInstallationLocation();

          if (!installationLocation.toString().contains("tmp\\oomph.capture"))
          {
            throw new RuntimeException("Bad install location");
          }

          IOUtil.deleteBestEffort(installationLocation);

          ReflectUtil.invokeMethod("backPressed", wizardDialog);

          postProcessPromptPage(wizardDialog, "tmp\\oomph.capture");

          advanceToNextPage(wizardDialog);

          postProcessConfirmationPage(wizardDialog);

          ReflectUtil.setValue("progressLogWrapper", wizardDialog.getCurrentPage().getNextPage(),
              ReflectUtil.getConstructor(ProgressLogWrapper.class, ProgressLog.class));

          advanceToNextPage(wizardDialog);

          while (!ProgressLogWrapper.instance.done)
          {
            AccessUtil.busyWait(1000);
          }

          TreeViewer treeViewer = getViewer(wizardDialog, "treeViewer");
          ITreeContentProvider provider = (ITreeContentProvider)treeViewer.getContentProvider();
          for (Object object : provider.getElements(treeViewer.getInput()))
          {
            if (object instanceof ResourceCreationTask)
            {
              ResourceCreationTask resourceCreationTask = (ResourceCreationTask)object;
              resourceCreationTask.setTargetURL(resourceCreationTask.getTargetURL().replace("tmp/oomph.capture", "oomph"));
              AccessUtil.busyWait(10);
            }
          }

          SashForm sashForm = getWidget(wizardDialog, "sash");
          sashForm.setWeights(new int[] { 40, 60 });
        }

        @Override
        protected Image capture(WizardDialog wizardDialog)
        {
          IWizardPage page = wizardDialog.getCurrentPage();

          Map<Control, Image> decorations = new LinkedHashMap<Control, Image>();
          tree = getCalloutImage(1);
          decorations.put(getViewerControl(wizardDialog, "treeViewer"), tree);
          log = getCalloutImage(2);
          decorations.put((Control)getWidget(wizardDialog, "log"), log);
          Image result = capture(page, decorations);

          lock = getImage(wizardDialog, "lock");
          dismiss = getImage(wizardDialog, "dismiss");
          launch = getImage(wizardDialog, "launch");

          Button button = getWidget(wizardDialog, "launch");
          button.setText("Restart if needed");
          restart = getImage(button);

          progress = getImage(wizardDialog, "progress");

          ProgressLogWrapper.instance.cancel = true;

          return result;
        }

        /**
         * @ignore
         */
        private static final class ProgressLogWrapper implements ProgressLog
        {
          private static ProgressLogWrapper instance;

          private boolean done;

          private boolean isP2Task;

          private final ProgressLog log;

          private boolean cancel;

          private ProgressLogWrapper(ProgressLog log)
          {
            this.log = log;
            instance = this;
          }

          public void task(SetupTask setupTask)
          {
            log.task(setupTask);

            if (setupTask instanceof P2Task)
            {
              isP2Task = true;
            }
          }

          public void setTerminating()
          {
            log.setTerminating();
          }

          public void log(Throwable t)
          {
            log.log(t);
          }

          public void log(IStatus status)
          {
            log.log(status);
          }

          public void log(String line, boolean filter)
          {
            log.log(line, filter);
            if (isP2Task)
            {
              done = true;
              while (!cancel)
              {
                try
                {
                  Thread.sleep(1000);
                }
                catch (InterruptedException ex)
                {
                  ex.printStackTrace();
                }
              }

              throw new RuntimeException("Canceled");
            }
          }

          public void log(String line)
          {
            log.log(line);
          }

          public boolean isCanceled()
          {
            return log.isCanceled();
          }
        }
      }
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
