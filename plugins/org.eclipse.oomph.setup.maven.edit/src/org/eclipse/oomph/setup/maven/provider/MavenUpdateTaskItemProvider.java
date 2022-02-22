/*
 * Copyright (c) 2020 Martin Schreiber (Bachmann electronic GmbH) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Martin Schreiber - initial API and implementation
 */
package org.eclipse.oomph.setup.maven.provider;

import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.setup.maven.MavenPackage;
import org.eclipse.oomph.setup.maven.MavenUpdateTask;
import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.maven.MavenUpdateTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MavenUpdateTaskItemProvider extends SetupTaskItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MavenUpdateTaskItemProvider(AdapterFactory adapterFactory)
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

      addProjectNamePatternsPropertyDescriptor(object);
      addOfflinePropertyDescriptor(object);
      addUpdateSnapshotsPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Project Name Patterns feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addProjectNamePatternsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_MavenUpdateTask_projectNamePatterns_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_MavenUpdateTask_projectNamePatterns_feature", "_UI_MavenUpdateTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.MAVEN_UPDATE_TASK__PROJECT_NAME_PATTERNS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Offline feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addOfflinePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_MavenUpdateTask_offline_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_MavenUpdateTask_offline_feature", "_UI_MavenUpdateTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.MAVEN_UPDATE_TASK__OFFLINE, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Update Snapshots feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addUpdateSnapshotsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_MavenUpdateTask_updateSnapshots_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_MavenUpdateTask_updateSnapshots_feature", "_UI_MavenUpdateTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.MAVEN_UPDATE_TASK__UPDATE_SNAPSHOTS, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This returns MavenUpdateTask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/MavenUpdateTask")); //$NON-NLS-1$
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
    MavenUpdateTask mavenUpdateTask = (MavenUpdateTask)object;
    EList<String> projectNamePatterns = mavenUpdateTask.getProjectNamePatterns();
    StringBuilder label = new StringBuilder(getString("_UI_MavenUpdateTask_type")); //$NON-NLS-1$
    if (projectNamePatterns.isEmpty())
    {
      label.append(" all"); //$NON-NLS-1$
    }
    else
    {
      label.append(" (").append(StringUtil.implode(projectNamePatterns, '|')).append(')'); //$NON-NLS-1$
    }

    if (mavenUpdateTask.isOffline())
    {
      label.append(" - offline"); //$NON-NLS-1$
    }

    if (mavenUpdateTask.isUpdateSnapshots())
    {
      label.append(" - snapshots"); //$NON-NLS-1$
    }

    return label.toString();
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

    switch (notification.getFeatureID(MavenUpdateTask.class))
    {
      case MavenPackage.MAVEN_UPDATE_TASK__PROJECT_NAME_PATTERNS:
      case MavenPackage.MAVEN_UPDATE_TASK__OFFLINE:
      case MavenPackage.MAVEN_UPDATE_TASK__UPDATE_SNAPSHOTS:
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

    newChildDescriptors.add(createChildParameter(BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS, BaseFactory.eINSTANCE.createAnnotation()));
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
    return MavenEditPlugin.INSTANCE;
  }

}
