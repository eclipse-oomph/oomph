/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.git.provider;

import org.eclipse.oomph.setup.git.GitCloneTask;
import org.eclipse.oomph.setup.git.GitFactory;
import org.eclipse.oomph.setup.git.GitPackage;
import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.git.GitCloneTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class GitCloneTaskItemProvider extends SetupTaskItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GitCloneTaskItemProvider(AdapterFactory adapterFactory)
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

      addLocationPropertyDescriptor(object);
      addRemoteNamePropertyDescriptor(object);
      addRemoteURIPropertyDescriptor(object);
      addPushURIPropertyDescriptor(object);
      addCheckoutBranchPropertyDescriptor(object);
      addRecursivePropertyDescriptor(object);
      addRestrictToCheckoutBranchPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Location feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addLocationPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_GitCloneTask_location_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_GitCloneTask_location_feature", "_UI_GitCloneTask_type"),
        GitPackage.Literals.GIT_CLONE_TASK__LOCATION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Remote Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRemoteNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_GitCloneTask_remoteName_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_GitCloneTask_remoteName_feature", "_UI_GitCloneTask_type"),
        GitPackage.Literals.GIT_CLONE_TASK__REMOTE_NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Remote URI feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRemoteURIPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_GitCloneTask_remoteURI_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_GitCloneTask_remoteURI_feature", "_UI_GitCloneTask_type"),
        GitPackage.Literals.GIT_CLONE_TASK__REMOTE_URI, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Push URI feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addPushURIPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_GitCloneTask_pushURI_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_GitCloneTask_pushURI_feature", "_UI_GitCloneTask_type"),
        GitPackage.Literals.GIT_CLONE_TASK__PUSH_URI, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Checkout Branch feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addCheckoutBranchPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_GitCloneTask_checkoutBranch_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_GitCloneTask_checkoutBranch_feature", "_UI_GitCloneTask_type"),
        GitPackage.Literals.GIT_CLONE_TASK__CHECKOUT_BRANCH, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Recursive feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRecursivePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_GitCloneTask_recursive_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_GitCloneTask_recursive_feature", "_UI_GitCloneTask_type"),
        GitPackage.Literals.GIT_CLONE_TASK__RECURSIVE, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Restrict To Checkout Branch feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRestrictToCheckoutBranchPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_GitCloneTask_restrictToCheckoutBranch_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_GitCloneTask_restrictToCheckoutBranch_feature", "_UI_GitCloneTask_type"),
        GitPackage.Literals.GIT_CLONE_TASK__RESTRICT_TO_CHECKOUT_BRANCH, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
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
      childrenFeatures.add(GitPackage.Literals.GIT_CLONE_TASK__CONFIG_SECTIONS);
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
   * This returns GitCloneTask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/GitCloneTask"));
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
    String uri = ((GitCloneTask)object).getRemoteURI();
    String branch = ((GitCloneTask)object).getCheckoutBranch();
    if ((uri == null || uri.length() == 0) && (branch == null || branch.length() == 0))
    {
      return getString("_UI_GitCloneTask_type");
    }

    return "" + uri + "  (" + branch + ")";
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

    switch (notification.getFeatureID(GitCloneTask.class))
    {
      case GitPackage.GIT_CLONE_TASK__LOCATION:
      case GitPackage.GIT_CLONE_TASK__REMOTE_NAME:
      case GitPackage.GIT_CLONE_TASK__REMOTE_URI:
      case GitPackage.GIT_CLONE_TASK__PUSH_URI:
      case GitPackage.GIT_CLONE_TASK__CHECKOUT_BRANCH:
      case GitPackage.GIT_CLONE_TASK__RECURSIVE:
      case GitPackage.GIT_CLONE_TASK__RESTRICT_TO_CHECKOUT_BRANCH:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case GitPackage.GIT_CLONE_TASK__CONFIG_SECTIONS:
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

    newChildDescriptors.add(createChildParameter(GitPackage.Literals.GIT_CLONE_TASK__CONFIG_SECTIONS, GitFactory.eINSTANCE.createConfigSection()));
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
    return GitEditPlugin.INSTANCE;
  }

}
