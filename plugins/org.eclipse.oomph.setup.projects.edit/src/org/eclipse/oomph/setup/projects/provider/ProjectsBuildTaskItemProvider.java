/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.projects.provider;

import org.eclipse.oomph.predicates.PredicatesFactory;
import org.eclipse.oomph.setup.projects.ProjectsBuildTask;
import org.eclipse.oomph.setup.projects.ProjectsPackage;
import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.projects.ProjectsBuildTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ProjectsBuildTaskItemProvider extends SetupTaskItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectsBuildTaskItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  /**
   * This returns the property descriptors for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
  {
    if (itemPropertyDescriptors == null)
    {
      super.getPropertyDescriptors(object);

      addOnlyNewProjectsPropertyDescriptor(object);
      addRefreshPropertyDescriptor(object);
      addCleanPropertyDescriptor(object);
      addBuildPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Only New Projects feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addOnlyNewProjectsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_ProjectsBuildTask_onlyNewProjects_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_ProjectsBuildTask_onlyNewProjects_feature", "_UI_ProjectsBuildTask_type"),
        ProjectsPackage.Literals.PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Refresh feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRefreshPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_ProjectsBuildTask_refresh_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_ProjectsBuildTask_refresh_feature", "_UI_ProjectsBuildTask_type"),
        ProjectsPackage.Literals.PROJECTS_BUILD_TASK__REFRESH, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Clean feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addCleanPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_ProjectsBuildTask_clean_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_ProjectsBuildTask_clean_feature", "_UI_ProjectsBuildTask_type"),
        ProjectsPackage.Literals.PROJECTS_BUILD_TASK__CLEAN, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Build feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addBuildPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_ProjectsBuildTask_build_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_ProjectsBuildTask_build_feature", "_UI_ProjectsBuildTask_type"),
        ProjectsPackage.Literals.PROJECTS_BUILD_TASK__BUILD, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
   * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
   * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
  {
    if (childrenFeatures == null)
    {
      super.getChildrenFeatures(object);
      childrenFeatures.add(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES);
    }
    return childrenFeatures;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EStructuralFeature getChildFeature(Object object, Object child)
  {
    // Check the type of the specified child object and return the proper feature to use for
    // adding (see {@link AddCommand}) it as a child.

    return super.getChildFeature(object, child);
  }

  /**
   * This returns ProjectsBuildTask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/ProjectsBuildTask"));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected boolean shouldComposeCreationImage()
  {
    return true;
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getText(Object object)
  {
    ProjectsBuildTask projectsBuildTask = (ProjectsBuildTask)object;
    String label = getString("_UI_ProjectsBuildTask_type");
    List<String> tags = new ArrayList<String>();
    if (projectsBuildTask.isOnlyNewProjects())
    {
      tags.add(getFeatureText(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS));
    }

    if (projectsBuildTask.isRefresh())
    {
      tags.add(getFeatureText(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__REFRESH));
    }

    if (projectsBuildTask.isClean())
    {
      tags.add(getFeatureText(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__CLEAN));
    }

    if (projectsBuildTask.isBuild())
    {
      tags.add(getFeatureText(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__BUILD));
    }

    if (!tags.isEmpty())
    {
      return label + " (" + StringUtil.implode(tags, ',').replace(",", ", ") + ")";
    }

    return label;
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void notifyChanged(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(ProjectsBuildTask.class))
    {
      case ProjectsPackage.PROJECTS_BUILD_TASK__ONLY_NEW_PROJECTS:
      case ProjectsPackage.PROJECTS_BUILD_TASK__REFRESH:
      case ProjectsPackage.PROJECTS_BUILD_TASK__CLEAN:
      case ProjectsPackage.PROJECTS_BUILD_TASK__BUILD:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case ProjectsPackage.PROJECTS_BUILD_TASK__PREDICATES:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
        return;
    }
    super.notifyChanged(notification);
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors.add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createNamePredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createCommentPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createLocationPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createRepositoryPredicate()));

    newChildDescriptors.add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createAndPredicate()));

    newChildDescriptors.add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createOrPredicate()));

    newChildDescriptors.add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createNotPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createNaturePredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createBuilderPredicate()));

    newChildDescriptors.add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createFilePredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectsPackage.Literals.PROJECTS_BUILD_TASK__PREDICATES, PredicatesFactory.eINSTANCE.createImportedPredicate()));
  }

  /**
   * Return the resource locator for this item provider's resources.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceLocator getResourceLocator()
  {
    return ProjectsEditPlugin.INSTANCE;
  }

}
