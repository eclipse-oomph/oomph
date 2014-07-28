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
package org.eclipse.oomph.resources.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.predicates.PredicatesFactory;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.resources.SourceLocator;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.resources.SourceLocator} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SourceLocatorItemProvider extends ModelElementItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SourceLocatorItemProvider(AdapterFactory adapterFactory)
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

      addRootFolderPropertyDescriptor(object);
      addLocateNestedProjectsPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Root Folder feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRootFolderPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SourceLocator_rootFolder_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_SourceLocator_rootFolder_feature", "_UI_SourceLocator_type"),
        ResourcesPackage.Literals.SOURCE_LOCATOR__ROOT_FOLDER, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Locate Nested Projects feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addLocateNestedProjectsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SourceLocator_locateNestedProjects_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_SourceLocator_locateNestedProjects_feature", "_UI_SourceLocator_type"),
        ResourcesPackage.Literals.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
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
      childrenFeatures.add(ResourcesPackage.Literals.SOURCE_LOCATOR__PROJECT_FACTORIES);
      childrenFeatures.add(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES);
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
   * This returns SourceLocator.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/SourceLocator"));
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
   * @generated
   */
  @Override
  public String getText(Object object)
  {
    String label = ((SourceLocator)object).getRootFolder();
    return label == null || label.length() == 0 ? getString("_UI_SourceLocator_type") : getString("_UI_SourceLocator_type") + " " + label;
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

    switch (notification.getFeatureID(SourceLocator.class))
    {
      case ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER:
      case ResourcesPackage.SOURCE_LOCATOR__LOCATE_NESTED_PROJECTS:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case ResourcesPackage.SOURCE_LOCATOR__PROJECT_FACTORIES:
      case ResourcesPackage.SOURCE_LOCATOR__PREDICATES:
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

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PROJECT_FACTORIES,
        ResourcesFactory.eINSTANCE.createEclipseProjectFactory()));

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PROJECT_FACTORIES,
        ResourcesFactory.eINSTANCE.createMavenProjectFactory()));

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createNamePredicate()));

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createCommentPredicate()));

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createLocationPredicate()));

    newChildDescriptors
        .add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createRepositoryPredicate()));

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createAndPredicate()));

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createOrPredicate()));

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createNotPredicate()));

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createNaturePredicate()));

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createBuilderPredicate()));

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createFilePredicate()));
  }

}
