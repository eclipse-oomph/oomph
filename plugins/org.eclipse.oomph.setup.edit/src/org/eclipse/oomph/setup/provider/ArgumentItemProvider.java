/*
 * Copyright (c) 2019 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.setup.Argument;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.Parameter;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.Argument} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ArgumentItemProvider extends ModelElementItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ArgumentItemProvider(AdapterFactory adapterFactory)
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

      addParameterPropertyDescriptor(object);
      addValuePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Parameter feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addParameterPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Argument_parameter_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Argument_parameter_feature", "_UI_Argument_type"),
        SetupPackage.Literals.ARGUMENT__PARAMETER, true, false, true, null, null, null));
  }

  @Override
  protected ItemPropertyDescriptor createItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName,
      String description, EStructuralFeature feature, boolean isSettable, boolean multiLine, boolean sortChoices, Object staticImage, String category,
      String[] filterFlags)
  {
    if (feature == SetupPackage.Literals.ARGUMENT__PARAMETER)
    {
      return new HierarchicalPropertyDescriptor(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices,
          staticImage, category, filterFlags)
      {
        @Override
        protected IItemLabelProvider createLabelProvider()
        {
          return new HierarchicalItemLabelProvider(itemDelegator)
          {
            @Override
            public String getText(Object object)
            {
              String text = super.getText(object);
              Parameter parameter = (Parameter)object;
              if (parameter != null)
              {
                String description = parameter.getDescription();
                if (!StringUtil.isEmpty(description))
                {
                  text += " - " + description;
                }
              }

              return text;
            }

            @Override
            protected Object getParent(Object object)
            {
              return null;
            }
          };
        }

        @Override
        public Collection<?> getChoiceOfValues(Object object)
        {
          return filterChoices(super.getChoiceOfValues(object), feature, object);
        }
      };
    }

    return super.createItemPropertyDescriptor(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices,
        staticImage, category, filterFlags);
  }

  @Override
  protected Collection<?> filterChoices(Collection<?> choices, EStructuralFeature feature, Object object)
  {
    if (feature == SetupPackage.Literals.ARGUMENT__PARAMETER)
    {
      Argument argument = (Argument)object;
      EObject eContainer = argument.eContainer();
      if (eContainer instanceof MacroTask)
      {
        MacroTask macroTask = (MacroTask)eContainer;
        Macro macro = macroTask.getMacro();
        if (macro != null)
        {
          List<Object> result = new ArrayList<Object>(macro.getParameters());
          EList<Argument> arguments = macroTask.getArguments();
          for (Argument otherArgument : arguments)
          {
            if (otherArgument != argument)
            {
              result.remove(otherArgument.getParameter());
            }
          }

          return result;
        }
      }
    }

    return super.filterChoices(choices, feature, object);
  }

  /**
   * This adds a property descriptor for the Value feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addValuePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Argument_value_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Argument_value_feature", "_UI_Argument_type"),
        SetupPackage.Literals.ARGUMENT__VALUE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This returns Argument.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/Argument"));
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
    Argument argument = (Argument)object;
    Parameter parameter = argument.getParameter();
    String label = parameter == null ? null : parameter.getName();
    if (StringUtil.isEmpty(label))
    {
      label = "<no-parameter>";
    }

    String value = argument.getValue();
    if (value != null)
    {
      if (value.length() == 0)
      {
        label += " = \"\"";
      }
      else
      {
        label += " = " + value;
      }
    }
    else
    {
      String defaultValue = parameter == null ? null : parameter.getDefaultValue();
      if (defaultValue != null)
      {
        if (defaultValue.length() == 0)
        {
          label += " (default: \"\")";
        }
        else
        {
          label += " (default: " + defaultValue + ")";
        }
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

    switch (notification.getFeatureID(Argument.class))
    {
      case SetupPackage.ARGUMENT__PARAMETER:
      case SetupPackage.ARGUMENT__VALUE:
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
