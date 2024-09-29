/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.TextModification;

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
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.TextModification} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TextModificationItemProvider extends ModelElementItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TextModificationItemProvider(AdapterFactory adapterFactory)
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

      addPatternPropertyDescriptor(object);
      addSubstitutionsPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Pattern feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addPatternPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TextModification_pattern_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_TextModification_pattern_feature", "_UI_TextModification_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        SetupPackage.Literals.TEXT_MODIFICATION__PATTERN, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Substitutions feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addSubstitutionsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TextModification_substitutions_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_TextModification_substitutions_feature", "_UI_TextModification_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        SetupPackage.Literals.TEXT_MODIFICATION__SUBSTITUTIONS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
      childrenFeatures.add(SetupPackage.Literals.TEXT_MODIFICATION__SUBSTITUTIONS);
    }
    return childrenFeatures;
  }

  @Override
  protected Object createWrapper(EObject object, EStructuralFeature feature, Object value, int index)
  {
    return feature == SetupPackage.Literals.TEXT_MODIFICATION__SUBSTITUTIONS
        ? new AttributeValueWrapperItemProvider(value, object, (EAttribute)feature, index, adapterFactory, getResourceLocator())
        {
          @Override
          public Object getImage(Object object)
          {
            return getResourceLocator().getImage("full/obj16/Substitution.png"); //$NON-NLS-1$
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
   * This returns TextModification.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/TextModification")); //$NON-NLS-1$
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
    String label = ((TextModification)object).getPattern();
    return label == null || label.length() == 0 ? getString("_UI_TextModification_type") : //$NON-NLS-1$
        getString("_UI_TextModification_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
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

    switch (notification.getFeatureID(TextModification.class))
    {
      case SetupPackage.TEXT_MODIFICATION__PATTERN:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case SetupPackage.TEXT_MODIFICATION__SUBSTITUTIONS:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
        return;
    }
    super.notifyChanged(notification);
  }

  @Override
  public Object getCreateChildImage(Object owner, Object feature, Object child, Collection<?> selection)
  {
    if (feature == SetupPackage.Literals.TEXT_MODIFICATION__SUBSTITUTIONS)
    {
      return getResourceLocator().getImage("full/obj16/Substitution.png"); //$NON-NLS-1$
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
  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.TEXT_MODIFICATION__SUBSTITUTIONS, "")); //$NON-NLS-1$
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
