/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.doc.concepts;

import org.eclipse.oomph.setup.doc.user.wizard.DocImportWizard;
import org.eclipse.oomph.setup.doc.user.wizard.DocInstallWizard;

/**
 * Context
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
 * With Oomph, all these instructions are formalized as setup {@linkplain DocTask tasks} that are {@linkplain DocTaskExecution performed} automatically.
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
 * An {@linkplain DocInstallWizard installer wizard} that automates the both phases of setting up a development environment.
 * </li>
 * <li>
 * A {@linkplain DocImportWizard project wizard} that automates the second phase of setting up a development environment.
 * <li>
 * An {@linkplain DocImportWizard execution wizard} that automates updating the development environment,
 * including the provisioning of personal favorite tools and personal preferences.
 * </li>
 * </ul>
 * Oomph helps ensure that each developer works with the same uniformly-provisioned development environment
 * and that each user has her personal tools and preferences uniformly available across all installations and workspaces.
 * Note that Oomph can be installed into an existing IDE, i.e., one not installed by Oomph,
 * via Oomph's <a rel="nofollow" href="https://download.eclipse.org/oomph/updates/milestone/latest">update site</a>
 * or via Oomph's <a rel="nofollow" href="https://download.eclipse.org/oomph/updates/milestone/latest/org.eclipse.oomph.site.zip">site archive</a>.</p>
 * </p>
 * <p>
 * To understand how best to exploit Oomph in order to save time and spare aggravation,
 * some basic concepts need to be well understood.
 * Automation is achieved by specifying setup {@linkplain DocTask tasks} organized into {@linkplain DocScope scopes} stored in {@linkplain DocSetupResource resources}.
 * </p>
 * <p>
 * An important footnote is that Oomph makes heavy use of {@link DocBundlePool bundle pooling}
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
 * @number 100
 */
public abstract class DocConceptOverview
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
}
