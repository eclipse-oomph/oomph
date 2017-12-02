/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.jdt.provider;

import org.eclipse.oomph.setup.jdt.JDTFactory;
import org.eclipse.oomph.setup.jdt.JDTPackage;
import org.eclipse.oomph.setup.jdt.JRETask;
import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.jdt.JRETask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class JRETaskItemProvider extends SetupTaskItemProvider
{
  private static Map<String, String> VERSION_VARIABLES = new LinkedHashMap<String, String>();

  static
  {
    VERSION_VARIABLES.put("JRE-1.1", "${jre.location-1.1}");
    VERSION_VARIABLES.put("J2SE-1.2", "${jre.location-1.2}");
    VERSION_VARIABLES.put("J2SE-1.3", "${jre.location-1.3}");
    VERSION_VARIABLES.put("J2SE-1.4", "${jre.location-1.4}");
    VERSION_VARIABLES.put("J2SE-1.5", "${jre.location-1.5}");
    VERSION_VARIABLES.put("JavaSE-1.6", "${jre.location-1.6}");
    VERSION_VARIABLES.put("JavaSE-1.7", "${jre.location-1.7}");
    VERSION_VARIABLES.put("JavaSE-1.8", "${jre.location-1.8}");
    VERSION_VARIABLES.put("JavaSE-9", "${jre.location-9}");
  }

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JRETaskItemProvider(AdapterFactory adapterFactory)
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

      addVersionPropertyDescriptor(object);
      addLocationPropertyDescriptor(object);
      addNamePropertyDescriptor(object);
      addVMInstallTypePropertyDescriptor(object);
      addExecutionEnvironmentDefaultPropertyDescriptor(object);
      addVMArgumentsPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Version feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addVersionPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_JRETask_version_feature"), getString("_UI_PropertyDescriptor_description", "_UI_JRETask_version_feature", "_UI_JRETask_type"),
        JDTPackage.Literals.JRE_TASK__VERSION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        return VERSION_VARIABLES.keySet();
      }
    });
  }

  @Override
  protected Command createSetCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Object value)
  {
    Command result = super.createSetCommand(domain, owner, feature, value);
    if (feature == JDTPackage.Literals.JRE_TASK__VERSION)
    {
      CompoundCommand compoundCommand = new CompoundCommand(0);
      compoundCommand.append(result);
      compoundCommand.append(createSetCommand(domain, owner, JDTPackage.Literals.JRE_TASK__LOCATION, VERSION_VARIABLES.get(value)));
      result = compoundCommand;
    }

    return result;
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
        getString("_UI_JRETask_location_feature"), getString("_UI_PropertyDescriptor_description", "_UI_JRETask_location_feature", "_UI_JRETask_type"),
        JDTPackage.Literals.JRE_TASK__LOCATION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_JRETask_name_feature"), getString("_UI_PropertyDescriptor_description", "_UI_JRETask_name_feature", "_UI_JRETask_type"),
        JDTPackage.Literals.JRE_TASK__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the VM Install Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addVMInstallTypePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_JRETask_vMInstallType_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_JRETask_vMInstallType_feature", "_UI_JRETask_type"), JDTPackage.Literals.JRE_TASK__VM_INSTALL_TYPE,
        true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        List<String> result = new UniqueEList<String>();
        IVMInstallType[] vmInstallTypes = JavaRuntime.getVMInstallTypes();
        for (IVMInstallType vmInstallType : vmInstallTypes)
        {
          result.add(vmInstallType.getId());
        }

        JRETask jreTask = (JRETask)object;
        String vmInstallType = jreTask.getVMInstallType();
        if (vmInstallType != null)
        {
          result.add(vmInstallType);
        }

        return result;
      }
    });
  }

  /**
   * This adds a property descriptor for the Execution Environment Default feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addExecutionEnvironmentDefaultPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_JRETask_executionEnvironmentDefault_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_JRETask_executionEnvironmentDefault_feature", "_UI_JRETask_type"),
        JDTPackage.Literals.JRE_TASK__EXECUTION_ENVIRONMENT_DEFAULT, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the VM Arguments feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addVMArgumentsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_JRETask_vMArguments_feature"), getString("_UI_PropertyDescriptor_description", "_UI_JRETask_vMArguments_feature", "_UI_JRETask_type"),
        JDTPackage.Literals.JRE_TASK__VM_ARGUMENTS, true, true, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
      childrenFeatures.add(JDTPackage.Literals.JRE_TASK__JRE_LIBRARIES);
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
   * This returns JRETask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/JRETask"));
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
    JRETask jre = (JRETask)object;
    String name = jre.getName();
    String location = jre.getLocation();

    if (StringUtil.isEmpty(name))
    {
      return getString("_UI_JRETask_type");
    }

    String label = name;
    if (location != null)
    {
      if (location.length() == 0)
      {
        label += " = \"\"";
      }
      else
      {
        label += " = " + location;
      }
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

    switch (notification.getFeatureID(JRETask.class))
    {
      case JDTPackage.JRE_TASK__VERSION:
      case JDTPackage.JRE_TASK__LOCATION:
      case JDTPackage.JRE_TASK__NAME:
      case JDTPackage.JRE_TASK__VM_INSTALL_TYPE:
      case JDTPackage.JRE_TASK__EXECUTION_ENVIRONMENT_DEFAULT:
      case JDTPackage.JRE_TASK__VM_ARGUMENTS:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case JDTPackage.JRE_TASK__JRE_LIBRARIES:
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

    newChildDescriptors.add(createChildParameter(JDTPackage.Literals.JRE_TASK__JRE_LIBRARIES, JDTFactory.eINSTANCE.createJRELibrary()));
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
    return JDTEditPlugin.INSTANCE;
  }

}
