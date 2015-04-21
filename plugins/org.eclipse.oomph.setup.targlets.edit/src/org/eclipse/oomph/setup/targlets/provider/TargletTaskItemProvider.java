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
package org.eclipse.oomph.setup.targlets.provider;

import org.eclipse.oomph.p2.Configuration;
import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;
import org.eclipse.oomph.setup.targlets.SetupTargletsFactory;
import org.eclipse.oomph.setup.targlets.SetupTargletsPackage;
import org.eclipse.oomph.setup.targlets.TargletTask;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.targlets.TargletTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TargletTaskItemProvider extends SetupTaskItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletTaskItemProvider(AdapterFactory adapterFactory)
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

      addTargletURIsPropertyDescriptor(object);
      addOperatingSystemPropertyDescriptor(object);
      addWindowingSystemPropertyDescriptor(object);
      addArchitecturePropertyDescriptor(object);
      addLocalePropertyDescriptor(object);
      addProgramArgumentsPropertyDescriptor(object);
      addVMArgumentsPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Targlet UR Is feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTargletURIsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_targletURIs_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_targletURIs_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__TARGLET_UR_IS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Operating System feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addOperatingSystemPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_operatingSystem_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_operatingSystem_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__OPERATING_SYSTEM, true, false, true, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        return Configuration.Choices.forOS();
      }
    });
  }

  /**
   * This adds a property descriptor for the Windowing System feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addWindowingSystemPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_windowingSystem_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_windowingSystem_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__WINDOWING_SYSTEM, true, false, true, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        return Configuration.Choices.forWS();
      }
    });
  }

  /**
   * This adds a property descriptor for the Architecture feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addArchitecturePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_architecture_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_architecture_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__ARCHITECTURE, true, false, true, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        return Configuration.Choices.forArch();
      }
    });
  }

  /**
   * This adds a property descriptor for the Locale feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addLocalePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_locale_feature"), getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_locale_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__LOCALE, true, false, true, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      private static final String NL_EXTRA = "org.eclipse.pde.nl.extra";

      private LocaleItemLabelProvider labelProvider;

      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        LocaleItemLabelProvider labelProvider = getLabelProvider(object);
        Set<String> locales = labelProvider.getLocaleMap().keySet();
        return getChoices(locales.toArray(new String[locales.size()]), NL_EXTRA);
      }

      @Override
      public LocaleItemLabelProvider getLabelProvider(Object object)
      {
        if (labelProvider == null)
        {
          labelProvider = new LocaleItemLabelProvider(itemDelegator);
        }

        return labelProvider;
      }
    });
  }

  /**
   * This adds a property descriptor for the Program Arguments feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addProgramArgumentsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_programArguments_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_programArguments_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__PROGRAM_ARGUMENTS, true, true, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
        getString("_UI_TargletTask_vMArguments_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_vMArguments_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__VM_ARGUMENTS, true, true, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  private static Set<String> getChoices(String[] values, String extraValuesPreference)
  {
    Set<String> result = new HashSet<String>();
    result.addAll(Arrays.asList(values));

    IEclipsePreferences node = InstanceScope.INSTANCE.getNode("org.eclipse.pde.core");
    String extraValues = node.get(extraValuesPreference, null);
    if (!StringUtil.isEmpty(extraValues))
    {
      StringTokenizer tokenizer = new StringTokenizer(extraValues, ",");
      while (tokenizer.hasMoreTokens())
      {
        String extraValue = tokenizer.nextToken().trim();
        result.add(extraValue);
      }
    }

    return result;
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
      childrenFeatures.add(SetupTargletsPackage.Literals.TARGLET_TASK__TARGLETS);
      childrenFeatures.add(SetupTargletsPackage.Literals.TARGLET_TASK__IMPLICIT_DEPENDENCIES);
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
   * This returns TargletTask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/TargletTask"));
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
    TargletTask targletTask = (TargletTask)object;

    StringBuilder builder = new StringBuilder();
    Set<String> added = new HashSet<String>();

    for (Targlet targlet : targletTask.getTarglets())
    {
      String name = targlet.getName();
      if (added.add(name))
      {
        if (builder.length() != 0)
        {
          builder.append(" + ");
        }

        builder.append(name);
      }
    }

    String label = getString("_UI_TargletTask_type");
    if (builder.length() != 0)
    {
      label += " (" + builder + ")";
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

    switch (notification.getFeatureID(TargletTask.class))
    {
      case SetupTargletsPackage.TARGLET_TASK__TARGLET_UR_IS:
      case SetupTargletsPackage.TARGLET_TASK__OPERATING_SYSTEM:
      case SetupTargletsPackage.TARGLET_TASK__WINDOWING_SYSTEM:
      case SetupTargletsPackage.TARGLET_TASK__ARCHITECTURE:
      case SetupTargletsPackage.TARGLET_TASK__LOCALE:
      case SetupTargletsPackage.TARGLET_TASK__PROGRAM_ARGUMENTS:
      case SetupTargletsPackage.TARGLET_TASK__VM_ARGUMENTS:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case SetupTargletsPackage.TARGLET_TASK__TARGLETS:
      case SetupTargletsPackage.TARGLET_TASK__IMPLICIT_DEPENDENCIES:
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

    newChildDescriptors.add(createChildParameter(SetupTargletsPackage.Literals.TARGLET_TASK__TARGLETS, TargletFactory.eINSTANCE.createTarglet()));

    newChildDescriptors.add(
        createChildParameter(SetupTargletsPackage.Literals.TARGLET_TASK__IMPLICIT_DEPENDENCIES, SetupTargletsFactory.eINSTANCE.createImplicitDependency()));
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
    return SetupTargletsEditPlugin.INSTANCE;
  }

  /**
   * @author Eike Stepper
   */
  private static final class LocaleItemLabelProvider implements IItemLabelProvider
  {
    private final AdapterFactoryItemDelegator itemDelegator;

    private Map<String, String> localeMap;

    public LocaleItemLabelProvider(AdapterFactoryItemDelegator itemDelegator)
    {
      this.itemDelegator = itemDelegator;
    }

    public Object getImage(Object object)
    {
      return itemDelegator.getImage(object);
    }

    public String getText(Object object)
    {
      return getLocaleMap().get(object);
    }

    public Map<String, String> getLocaleMap()
    {
      if (localeMap == null)
      {
        localeMap = new HashMap<String, String>();
        for (Locale locale : Locale.getAvailableLocales())
        {
          localeMap.put(locale.toString(), locale.toString() + " - " + locale.getDisplayName());
        }
      }

      return localeMap;
    }
  }
}
