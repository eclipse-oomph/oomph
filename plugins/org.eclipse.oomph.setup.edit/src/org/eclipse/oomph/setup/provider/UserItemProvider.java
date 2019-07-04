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
package org.eclipse.oomph.setup.provider;

import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.User;

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
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.User} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class UserItemProvider extends ScopeItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public UserItemProvider(AdapterFactory adapterFactory)
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

      addAcceptedLicensesPropertyDescriptor(object);
      addAcceptedCertificatesPropertyDescriptor(object);
      addUnsignedPolicyPropertyDescriptor(object);
      addCertificatePolicyPropertyDescriptor(object);
      addQuestionnaireDatePropertyDescriptor(object);
      addPreferenceRecorderDefaultPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Accepted Licenses feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addAcceptedLicensesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_User_acceptedLicenses_feature"), getString("_UI_PropertyDescriptor_description", "_UI_User_acceptedLicenses_feature", "_UI_User_type"),
        SetupPackage.Literals.USER__ACCEPTED_LICENSES, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null,
        new String[] { "org.eclipse.ui.views.properties.expert" }));
  }

  /**
   * This adds a property descriptor for the Accepted Certificates feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addAcceptedCertificatesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_User_acceptedCertificates_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_User_acceptedCertificates_feature", "_UI_User_type"),
        SetupPackage.Literals.USER__ACCEPTED_CERTIFICATES, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null,
        new String[] { "org.eclipse.ui.views.properties.expert" }));
  }

  /**
   * This adds a property descriptor for the Unsigned Policy feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addUnsignedPolicyPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_User_unsignedPolicy_feature"), getString("_UI_PropertyDescriptor_description", "_UI_User_unsignedPolicy_feature", "_UI_User_type"),
        SetupPackage.Literals.USER__UNSIGNED_POLICY, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Certificate Policy feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addCertificatePolicyPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_User_certificatePolicy_feature"), getString("_UI_PropertyDescriptor_description", "_UI_User_certificatePolicy_feature", "_UI_User_type"),
        SetupPackage.Literals.USER__CERTIFICATE_POLICY, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Questionnaire Date feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addQuestionnaireDatePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_User_questionnaireDate_feature"), getString("_UI_PropertyDescriptor_description", "_UI_User_questionnaireDate_feature", "_UI_User_type"),
        SetupPackage.Literals.USER__QUESTIONNAIRE_DATE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null,
        new String[] { "org.eclipse.ui.views.properties.expert" }));
  }

  /**
   * This adds a property descriptor for the Preference Recorder Default feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addPreferenceRecorderDefaultPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_User_preferenceRecorderDefault_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_User_preferenceRecorderDefault_feature", "_UI_User_type"),
        SetupPackage.Literals.USER__PREFERENCE_RECORDER_DEFAULT, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null,
        new String[] { "org.eclipse.ui.views.properties.expert" }));
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
      childrenFeatures.add(SetupPackage.Literals.USER__ATTRIBUTE_RULES);
    }
    return childrenFeatures;
  }

  @Override
  public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
  {
    getChildrenFeaturesGen(object);
    childrenFeatures.remove(SetupPackage.Literals.USER__ATTRIBUTE_RULES);
    childrenFeatures.add(0, SetupPackage.Literals.USER__ATTRIBUTE_RULES);
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
   * This returns User.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/User"));
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
    return getString("_UI_User_type");
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

    switch (notification.getFeatureID(User.class))
    {
      case SetupPackage.USER__ACCEPTED_LICENSES:
      case SetupPackage.USER__ACCEPTED_CERTIFICATES:
      case SetupPackage.USER__UNSIGNED_POLICY:
      case SetupPackage.USER__CERTIFICATE_POLICY:
      case SetupPackage.USER__QUESTIONNAIRE_DATE:
      case SetupPackage.USER__PREFERENCE_RECORDER_DEFAULT:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case SetupPackage.USER__ATTRIBUTE_RULES:
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

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.USER__ATTRIBUTE_RULES, SetupFactory.eINSTANCE.createAttributeRule()));
  }

}
