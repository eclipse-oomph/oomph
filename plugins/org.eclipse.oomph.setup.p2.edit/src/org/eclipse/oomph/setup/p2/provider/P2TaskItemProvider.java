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
package org.eclipse.oomph.setup.p2.provider;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.provider.RequirementItemProvider;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Package;
import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.p2.P2Task} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class P2TaskItemProvider extends SetupTaskItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public P2TaskItemProvider(AdapterFactory adapterFactory)
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

      addLabelPropertyDescriptor(object);
      addLicenseConfirmationDisabledPropertyDescriptor(object);
      addMergeDisabledPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Label feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addLabelPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_P2Task_label_feature"), getString("_UI_PropertyDescriptor_description", "_UI_P2Task_label_feature", "_UI_P2Task_type"),
        SetupP2Package.Literals.P2_TASK__LABEL, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the License Confirmation Disabled feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addLicenseConfirmationDisabledPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_P2Task_licenseConfirmationDisabled_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_P2Task_licenseConfirmationDisabled_feature", "_UI_P2Task_type"),
        SetupP2Package.Literals.P2_TASK__LICENSE_CONFIRMATION_DISABLED, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Merge Disabled feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addMergeDisabledPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_P2Task_mergeDisabled_feature"), getString("_UI_PropertyDescriptor_description", "_UI_P2Task_mergeDisabled_feature", "_UI_P2Task_type"),
        SetupP2Package.Literals.P2_TASK__MERGE_DISABLED, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
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
      childrenFeatures.add(SetupP2Package.Literals.P2_TASK__REQUIREMENTS);
      childrenFeatures.add(SetupP2Package.Literals.P2_TASK__REPOSITORIES);
    }
    return childrenFeatures;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  protected EStructuralFeature getChildFeature(Object object, Object child)
  {
    // Check the type of the specified child object and return the proper feature to use for
    // adding (see {@link AddCommand}) it as a child.

    if (child instanceof URI)
    {
      return SetupP2Package.Literals.P2_TASK__REPOSITORIES;
    }

    return super.getChildFeature(object, child);
  }

  /**
   * This returns P2Task.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/P2Task"));
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
    String label = ((P2Task)object).getLabel();
    return label == null || label.length() == 0 ? getString("_UI_P2Task_type") : getString("_UI_P2Task_type") + " (" + label + ")";
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

    switch (notification.getFeatureID(P2Task.class))
    {
      case SetupP2Package.P2_TASK__LABEL:
      case SetupP2Package.P2_TASK__LICENSE_CONFIRMATION_DISABLED:
      case SetupP2Package.P2_TASK__MERGE_DISABLED:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case SetupP2Package.P2_TASK__REQUIREMENTS:
      case SetupP2Package.P2_TASK__REPOSITORIES:
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

    newChildDescriptors.add(createChildParameter(SetupP2Package.Literals.P2_TASK__REQUIREMENTS, P2Factory.eINSTANCE.createRequirement()));

    newChildDescriptors.add(createChildParameter(SetupP2Package.Literals.P2_TASK__REPOSITORIES, P2Factory.eINSTANCE.createRepository()));
  }

  @Override
  protected Collection<?> filterAlternatives(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> alternatives)
  {
    return super.filterAlternatives(domain, owner, location, operations, operation, RequirementItemProvider.filterAlternatives(alternatives));
  }

  @Override
  public Command createCommand(Object object, EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter)
  {
    if (commandClass == DragAndDropCommand.class)
    {
      DragAndDropCommand.Detail detail = (DragAndDropCommand.Detail)commandParameter.getFeature();
      Command command = createDragAndDropCommand(domain, commandParameter.getOwner(), detail.location, detail.operations, detail.operation,
          commandParameter.getCollection());
      if (command != null)
      {
        if (command.canExecute())
        {
          return command;
        }

        command.dispose();
      }
    }

    return super.createCommand(object, domain, commandClass, commandParameter);
  }

  @Override
  protected Command createAddCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Collection<?> collection, int index)
  {
    List<Object> filteredCollection = new ArrayList<Object>();
    if (collection != null)
    {
      for (Object object : collection)
      {
        if (object instanceof URI)
        {
          filteredCollection.add(P2Factory.eINSTANCE.createRepository(object.toString()));
        }
        else
        {
          filteredCollection.add(object);
        }
      }
    }

    return super.createAddCommand(domain, owner, feature, filteredCollection, index);
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
    return SetupP2EditPlugin.INSTANCE;
  }

}
