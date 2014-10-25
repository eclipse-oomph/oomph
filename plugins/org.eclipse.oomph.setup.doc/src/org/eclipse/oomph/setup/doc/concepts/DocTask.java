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

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.doc.concepts.DocTaskComposition.TaskList.Consolidation;
import org.eclipse.oomph.setup.doc.concepts.DocTaskComposition.TaskList.Filter;
import org.eclipse.oomph.setup.doc.concepts.DocTaskComposition.TaskList.InitialPhase;
import org.eclipse.oomph.setup.doc.concepts.DocTaskComposition.TaskList.Reorder;
import org.eclipse.oomph.setup.doc.user.wizard.DocConfirmationPage;
import org.eclipse.oomph.setup.doc.user.wizard.DocImportWizard;
import org.eclipse.oomph.setup.doc.user.wizard.DocInstallWizard;
import org.eclipse.oomph.setup.doc.user.wizard.DocUpdateWizard;

/**
 * Tasks
 * <p>
 * A setup {@link SetupTask task} is the fine-grained unit of work in the overall setup process.
 * A task is declarative in nature,
 * i.e., it describes what needs to be achieved rather than how it's achieved.
 * Each task is contained within a {@linkplain DocScope scope}.
 * The overall setup process involves {@linkplain DocTaskComposition gathering} tasks from various scopes
 * and then {@linkplain DocTaskExecution performing} those tasks.
 * </p>
 *
 * @number 700
 */
public class DocTask
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
   * must be unique within the containing {@link DocSetupResource resource}.
   * If the task has an ID,
   * a {@link DocVariableTask variable} is inferred for each populated String-typed attribute of that task.
   * This variable behaves as if it were logically contained just before the task in the task's container.
   * </li>
   */
  public static String id;

  /**
   * <p>
   * An {@link SetupTask#getExcludedTriggers() excluded} {@linkplain DocTrigger triggers} attribute
   * that specifies the triggers for which that task is not applicable.
   * This affects which tasks will be {@linkplain DocTaskComposition gathered}.
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
   * that specifies the set of {@linkplain DocScope scopes} to which the task inclusively applies.
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
   * is influenced by the {@link DocTrigger trigger},
   * i.e.,
   * it affects how detailed will be the analysis for determining whether the task needs to be performed
   * with the general goal being to {@link DocTaskExecution perform} only those tasks strictly needed for the {@link DocTrigger#startup startup} trigger.
   * This behavior is documented on a per-task basis.
   * </li>
   */
  public static String performance;

  /**
   * <li>
   * A task, in general, can be {@link SetupTask#overrideFor(SetupTask) overridden} by other tasks during the process of {@link DocTaskComposition gathering} tasks.
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
   * each task is {@link Consolidation consolidated} to optimize its representation in preparation before the task is {@linkplain DocTaskExecution performed}.
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
  public static class DocTrigger
  {
    /**
     * <p>
     * There are three types of {@link org.eclipse.oomph.setup.Trigger triggers}:
     * <ul>
     */
    public static String introduction;

    /**
     * <li>
     * {@link org.eclipse.oomph.setup.Trigger#BOOTSTRAP Bootstrap} applies when using the {@link DocInstallWizard installer wizard}.
     * </li>
     */
    public static String bootstrap;

    /**
     * <li>
     * {@link org.eclipse.oomph.setup.Trigger#STARTUP Startup} applies when a product is first launched, automated task performance is enabled, and there are tasks that need to be performed,
     * at which point the {@linkplain DocUpdateWizard execution wizard} will be opened on the {@link DocConfirmationPage progressPage page}.
     * </li>
     */
    public static String startup;

    /**
     * <li>
     * {@link org.eclipse.oomph.setup.Trigger#MANUAL Manual} applies when invoking the {@linkplain DocImportWizard project wizard} or directly invoking {@linkplain DocUpdateWizard execution wizard}.
     * </li>
     */
    public static String manual;

    /**
     * </ul>
     * <p>
     * Each task specifies its {@link DocTask#excludedTriggers valid} triggers
     * determining whether that task is {@link Filter filtered} when tasks are gathered.
     * </p>
     */
    public static String summary;
  }

  /**
   * Variable Task
   */
  public static class DocVariableTask
  {
  }

  /**
   * Compound Task
   */
  public static class DocCompoundTask
  {
  }

  /**
   * P2 Task
   */
  public static class DocP2Task
  {
  }

  /**
   * Targlet Task
   */
  public static class DocTargletTask
  {
  }
}
