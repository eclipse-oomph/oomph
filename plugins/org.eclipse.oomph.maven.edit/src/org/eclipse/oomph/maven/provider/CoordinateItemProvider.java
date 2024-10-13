/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.provider;

import org.eclipse.oomph.maven.Coordinate;
import org.eclipse.oomph.maven.MavenPackage;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.StyledString;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.maven.Coordinate} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CoordinateItemProvider extends DOMElementItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CoordinateItemProvider(AdapterFactory adapterFactory)
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

      addGroupIdPropertyDescriptor(object);
      addArtifactIdPropertyDescriptor(object);
      addVersionPropertyDescriptor(object);
      addExpandedGroupIdPropertyDescriptor(object);
      addExpandedVersionPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Group Id feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addGroupIdPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Coordinate_groupId_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Coordinate_groupId_feature", "_UI_Coordinate_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.COORDINATE__GROUP_ID, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Artifact Id feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addArtifactIdPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Coordinate_artifactId_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Coordinate_artifactId_feature", "_UI_Coordinate_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.COORDINATE__ARTIFACT_ID, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Version feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addVersionPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Coordinate_version_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Coordinate_version_feature", "_UI_Coordinate_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.COORDINATE__VERSION, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Expanded Group Id feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addExpandedGroupIdPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Coordinate_expandedGroupId_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Coordinate_expandedGroupId_feature", "_UI_Coordinate_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.COORDINATE__EXPANDED_GROUP_ID, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Expanded Version feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addExpandedVersionPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Coordinate_expandedVersion_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Coordinate_expandedVersion_feature", "_UI_Coordinate_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.COORDINATE__EXPANDED_VERSION, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This returns Coordinate.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/Coordinate")); //$NON-NLS-1$
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
    return getStyledText(object).getString();
  }

  /**
   * This returns the label styled text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public StyledString getStyledText(Object object)
  {
    Coordinate coordinate = (Coordinate)object;
    StyledString styledLabel = new StyledString();

    String groupId = coordinate.getGroupId();
    String expandedGroupId = coordinate.getExpandedGroupId();
    if (Objects.equals(groupId, expandedGroupId))
    {
      styledLabel.append(groupId);
    }
    else
    {
      styledLabel.append(groupId, StyledString.Style.QUALIFIER_STYLER);
      styledLabel.append(" = ", StyledString.Style.QUALIFIER_STYLER); //$NON-NLS-1$
      styledLabel.append(expandedGroupId);
    }

    styledLabel.append(" : ", StyledString.Style.QUALIFIER_STYLER); //$NON-NLS-1$
    styledLabel.append(coordinate.getArtifactId());

    String version = coordinate.getVersion();
    if (!version.isBlank())
    {
      styledLabel.append(" : ", StyledString.Style.QUALIFIER_STYLER); //$NON-NLS-1$

      String expandedVersion = coordinate.getExpandedVersion();
      if (version.equals(expandedVersion))
      {
        styledLabel.append(version);
      }
      else
      {
        styledLabel.append(version, StyledString.Style.QUALIFIER_STYLER);
        styledLabel.append(" = ", StyledString.Style.QUALIFIER_STYLER); //$NON-NLS-1$
        styledLabel.append(expandedVersion);

      }
    }

    return styledLabel;
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

    switch (notification.getFeatureID(Coordinate.class))
    {
      case MavenPackage.COORDINATE__GROUP_ID:
      case MavenPackage.COORDINATE__ARTIFACT_ID:
      case MavenPackage.COORDINATE__VERSION:
      case MavenPackage.COORDINATE__EXPANDED_GROUP_ID:
      case MavenPackage.COORDINATE__EXPANDED_VERSION:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
  }

}
