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
package org.eclipse.oomph.resources.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.predicates.PredicatesFactory;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.resources.SourceLocator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.AttributeValueWrapperItemProvider;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
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
      addExcludedPathsPropertyDescriptor(object);
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
        getString("_UI_SourceLocator_rootFolder_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_SourceLocator_rootFolder_feature", "_UI_SourceLocator_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ResourcesPackage.Literals.SOURCE_LOCATOR__ROOT_FOLDER, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Excluded Paths feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addExcludedPathsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SourceLocator_excludedPaths_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_SourceLocator_excludedPaths_feature", "_UI_SourceLocator_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ResourcesPackage.Literals.SOURCE_LOCATOR__EXCLUDED_PATHS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
        getString("_UI_SourceLocator_locateNestedProjects_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_SourceLocator_locateNestedProjects_feature", "_UI_SourceLocator_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
  public Collection<? extends EStructuralFeature> getChildrenFeaturesGen(Object object)
  {
    if (childrenFeatures == null)
    {
      super.getChildrenFeatures(object);
      childrenFeatures.add(ResourcesPackage.Literals.SOURCE_LOCATOR__PROJECT_FACTORIES);
      childrenFeatures.add(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES);
    }
    return childrenFeatures;
  }

  @Override
  public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
  {
    if (childrenFeatures == null)
    {
      getChildrenFeaturesGen(object);
      childrenFeatures.add(2, ResourcesPackage.Literals.SOURCE_LOCATOR__EXCLUDED_PATHS);
    }

    return childrenFeatures;
  }

  @Override
  protected Object createWrapper(EObject object, EStructuralFeature feature, Object value, int index)
  {
    return feature == ResourcesPackage.Literals.SOURCE_LOCATOR__EXCLUDED_PATHS
        ? new AttributeValueWrapperItemProvider(value, object, (EAttribute)feature, index, adapterFactory, getResourceLocator())
        {
          @Override
          public Object getImage(Object object)
          {
            return getResourceLocator().getImage("full/obj16/ExcludedPath.png"); //$NON-NLS-1$
          }
        }
        : super.createWrapper(object, feature, value, index);
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
    return overlayImage(object, getResourceLocator().getImage("full/obj16/SourceLocator")); //$NON-NLS-1$
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
    return label == null || label.length() == 0 ? getString("_UI_SourceLocator_type") : //$NON-NLS-1$
        getString("_UI_SourceLocator_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void notifyChangedGen(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(SourceLocator.class))
    {
      case ResourcesPackage.SOURCE_LOCATOR__ROOT_FOLDER:
      case ResourcesPackage.SOURCE_LOCATOR__EXCLUDED_PATHS:
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

  @Override
  public void notifyChanged(Notification notification)
  {
    if (notification.getFeatureID(SourceLocator.class) == ResourcesPackage.SOURCE_LOCATOR__EXCLUDED_PATHS)
    {
      updateChildren(notification);
      fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
    }
    else
    {
      notifyChangedGen(notification);
    }
  }

  @Override
  public Object getCreateChildImage(Object owner, Object feature, Object child, Collection<?> selection)
  {
    if (feature == ResourcesPackage.Literals.SOURCE_LOCATOR__EXCLUDED_PATHS)
    {
      return getResourceLocator().getImage("full/obj16/ExcludedPath.png"); //$NON-NLS-1$
    }

    return super.getCreateChildImage(owner, feature, child, selection);
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void collectNewChildDescriptorsGen(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors
        .add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PROJECT_FACTORIES, ResourcesFactory.eINSTANCE.createEclipseProjectFactory()));

    newChildDescriptors
        .add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PROJECT_FACTORIES, ResourcesFactory.eINSTANCE.createMavenProjectFactory()));

    newChildDescriptors
        .add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PROJECT_FACTORIES, ResourcesFactory.eINSTANCE.createDynamicMavenProjectFactory()));

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

    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__PREDICATES, PredicatesFactory.eINSTANCE.createImportedPredicate()));
  }

  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    newChildDescriptors.add(createChildParameter(ResourcesPackage.Literals.SOURCE_LOCATOR__EXCLUDED_PATHS, "")); //$NON-NLS-1$

    collectNewChildDescriptorsGen(newChildDescriptors, object);
  }

  @Override
  public Command createCommand(Object object, EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter)
  {
    if (commandParameter.feature == null && commandClass != CreateChildCommand.class)
    {
      Collection<?> collection = commandParameter.getCollection();
      if (collection != null)
      {
        for (Object value : collection)
        {
          if (value instanceof IWrapperItemProvider)
          {
            EStructuralFeature feature = ((IWrapperItemProvider)value).getFeature();
            if (feature != null)
            {
              return super.createCommand(object, domain, commandClass, new CommandParameter(commandParameter.getOwner(), feature, collection));
            }
          }
        }
      }
    }

    return super.createCommand(object, domain, commandClass, commandParameter);
  }
}
