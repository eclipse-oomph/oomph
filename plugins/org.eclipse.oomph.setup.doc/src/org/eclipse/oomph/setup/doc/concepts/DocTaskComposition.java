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

import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocInstallation;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog.DocProduct;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProductCatalog.DocProduct.DocVersion;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog.DocProject;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog.DocProject.DocStream;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocUser;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocWorkspace;
import org.eclipse.oomph.setup.doc.concepts.DocTask.DocCompoundTask;
import org.eclipse.oomph.setup.doc.concepts.DocTask.DocP2Task;
import org.eclipse.oomph.setup.doc.concepts.DocTask.DocTrigger;
import org.eclipse.oomph.setup.doc.concepts.DocTask.DocVariableTask;
import org.eclipse.oomph.setup.doc.user.wizard.DocProductPage;
import org.eclipse.oomph.setup.doc.user.wizard.DocProjectPage;
import org.eclipse.oomph.setup.doc.user.wizard.DocVariablePage;

/**
 * Task Composition
 * <p>
 * The installation and provisioning process is driven by gathering a {@linkplain DocTask task} list from the available {@linkplain DocScope scopes}
 * in preparation for {@link DocTaskExecution task execution}.
 * </p>
 *
 * @number 500
 */
public class DocTaskComposition
{
  /**
   * Scope List
   * <p>
   * In preparation for gathering a task list,
   * an ordered list of {@link DocScope scopes} is collected.
   */
  public static class ScopeList
  {
    /**
     * The following process of collecting scopes is repeated once for each {@link DocStream stream}
     * specified in the {@linkplain DocProjectPage project page} or in the {@linkplain DocWorkspace workspace}:
     * <ul>
     *
     * <li>
     * Visit the {@link DocVersion product version}
     * as selected in the {@linkplain DocProductPage product page} or in the {@linkplain DocInstallation installation}.
     * <ul>
     * <li>
     * Add that product version's containing {@linkplain DocProduct products}'s containing {@linkplain DocProductCatalog product catalog} to the list.
     * Add that product version's containing product to the list.
     * Add that product version itself to the list.
     * </li>
     * </ul>
     * </li>
     *
     * <li>
     * Visit a {@link DocStream stream}
     * as selected in the {@linkplain DocProjectPage project page} or in the {@linkplain DocWorkspace workspace}, if there is one.
     * <ul>
     * <li>
     * Add that stream's containing {@linkplain DocProject projects}'s containing {@linkplain DocProjectCatalog project catalog} to the list.
     * Add that stream's outer-most containing project to the list
     * and proceed down through the nested projects up to the stream's directly containing project,
     * adding each project to the list along the way.
     * Add that stream itself to the list.
     * </li>
     * </ul>
     * </li>
     *
     * <li>
     * Add the {@link DocInstallation installation} to the list.
     * </li>
     *
     * <li>
     * Add the {@link DocWorkspace workspace} to the list.
     * </li>
     *
     * <li>
     * Add the {@link DocUser user} to the list.
     * </li>
     * </ul>
     * </p>
     */
    public static String scopeList;

    /**
     * <p>
     * For each list of scopes, an ordered list of tasks is collected.
     * Initially, for each scope in the list,
     * three {@link DocVariableTask variables} are induced,
     * one each for the {@link DocScope#name name}, {@link DocScope#label label}, and {@link DocScope#description description}
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
     * If the task is {@link DocCompoundTask compound},
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
   * to {@link DocTask#override override and merge} tasks,
   * to evaluate and expand variables
   * and to {@link DocTask#order reorder} tasks.
   * The members of the task list that are {@link DocVariableTask variables} induce an initial set of keys,
   * i.e., a set of all variable names.
   * Oomph tasks are modeled with EMF,
   * so each task instance knows it corresponding EMF class.
   * During the initial phase processing,
   * the list of tasks is analyzed to determine the set of EMF classes that are required to implement all the tasks in the list.
   * Each EMF class is processed as follows:
   * <ul>
   * <li>
   * If the class contains enablement annotations,
   * induce {@linkplain DocP2Task p2 tasks},
   * i.e., tasks to install the necessary implementations in the installed product,
   * and add these induced tasks to the head of the task list.
   * </li>
   * <li>
   * If the class contains variable annotations,
   * induce the {@linkplain DocVariableTask variables}
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
     * Put the task's {@link DocTask#override override token} into an token-to-task map,
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
     * i.e., each task is logically replaced in the copy by its {@link DocTask#override merged override}.
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
     * Recall that the gathering of tasks effectively {@link ScopeList#footer ignore} {@linkplain DocCompoundTask compound tasks}.
     * But those compound tasks can specify {@link DocTask#order predecessors and successors} and well as {@link DocTask#restrictions restrictions}.
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
     * particularly task {@link DocTask#override overriding} and merging,
     * affects the overall order of the task list such that it's different from the original authored order gathered from the scopes.
     * Not only that,
     * when multiple streams are involved,
     * {@linkplain FinalPhase final phase} processing is dealing with a concatenated list in which the tasks must be properly reordered.
     * To support that,
     * each task has an intrinsic {@link DocTask#order priority};
     * the task list is primarily sorted according to that priority.
     * Each task also specifies {@link DocTask#order predecessors and successors};
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
     * Each task that {@link DocTask#excludedTriggers excludes} the current {@link DocTrigger trigger} is removed from the task list.
     * Note that the task list gathering process gathers <b>all</b> tasks
     * because the task list is analyzed to determine which tasks need to be installed for all possible triggers.
     * So for {@link DocTrigger#bootstrap bootstrap bootstrap }trigger,
     * even the tasks that can't execute until they're running in an installed product are analyzed to ensure that,
     * once the product is installed,
     * the tasks that will need to perform in that installation,
     * i.e., for {@link DocTrigger#startup startup} or {@link DocTrigger#manual manual} trigger,
     * are properly installed.
     * The processing of all tasks also implies that at bootstrap time,
     * all  variables that will be needed in the running installed product will be {@link DocVariablePage prompted} early
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
     * and {@link DocTask#consolidate consolidates} each remaining task.
     * At this point,
     * the tasks in the list are ready to be {@link DocTaskExecution performed}.
     * </p>
     */
    public static class Consolidation
    {
    }
  }
}
