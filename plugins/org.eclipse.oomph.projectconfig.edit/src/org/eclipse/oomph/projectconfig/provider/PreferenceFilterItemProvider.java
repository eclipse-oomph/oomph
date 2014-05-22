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
package org.eclipse.oomph.projectconfig.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.preferences.provider.PreferenceItemItemProvider;
import org.eclipse.oomph.projectconfig.PreferenceFilter;
import org.eclipse.oomph.projectconfig.PreferenceProfile;
import org.eclipse.oomph.projectconfig.Project;
import org.eclipse.oomph.projectconfig.ProjectConfigPackage;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.EMFEditPlugin;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.projectconfig.PreferenceFilter} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PreferenceFilterItemProvider extends ModelElementItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PreferenceFilterItemProvider(AdapterFactory adapterFactory)
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

      addPreferenceNodePropertyDescriptor(object);
      addInclusionsPropertyDescriptor(object);
      addExclusionsPropertyDescriptor(object);
      addPropertiesPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Preference Node feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addPreferenceNodePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new PreferenceItemItemProvider.PreferenceItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory)
        .getRootAdapterFactory(), getResourceLocator(), getString("_UI_PreferenceFilter_preferenceNode_feature"), getString(
        "_UI_PropertyDescriptor_description", "_UI_PreferenceFilter_preferenceNode_feature", "_UI_PreferenceFilter_type"),
        ProjectConfigPackage.Literals.PREFERENCE_FILTER__PREFERENCE_NODE, true, false, true, null, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        Collection<Object> result = new ArrayList<Object>();
        PreferenceFilter preferenceFilter = (PreferenceFilter)object;
        PreferenceProfile preferenceProfile = preferenceFilter.getPreferenceProfile();
        if (preferenceProfile != null)
        {
          Project project = preferenceProfile.getProject();
          if (project != null)
          {
            PreferenceNode preferenceNode = project.getPreferenceNode();
            if (preferenceNode != null)
            {
              for (Iterator<EObject> it = preferenceNode.eAllContents(); it.hasNext();)
              {
                EObject eObject = it.next();
                if (eObject instanceof PreferenceNode)
                {
                  result.add(eObject);
                }
              }
            }
          }
        }

        return result;
      }
    });
  }

  /**
   * This adds a property descriptor for the Inclusions feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addInclusionsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceFilter_inclusions_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceFilter_inclusions_feature", "_UI_PreferenceFilter_type"),
        ProjectConfigPackage.Literals.PREFERENCE_FILTER__INCLUSIONS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Exclusions feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addExclusionsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceFilter_exclusions_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceFilter_exclusions_feature", "_UI_PreferenceFilter_type"),
        ProjectConfigPackage.Literals.PREFERENCE_FILTER__EXCLUSIONS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Properties feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addPropertiesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceFilter_properties_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceFilter_properties_feature", "_UI_PreferenceFilter_type"),
        ProjectConfigPackage.Literals.PREFERENCE_FILTER__PROPERTIES, false, false, false, null, null, null));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean hasChildren(Object object)
  {
    return hasChildren(object, false);
  }

  /**
   * This returns PreferenceFilter.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/PreferenceFilter"));
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
    PreferenceFilter preferenceFilter = (PreferenceFilter)object;
    String label = preferenceFilter.getInclusions().toString();
    String exclusions = preferenceFilter.getExclusions().toString();
    if (!"".equals(exclusions))
    {
      label += " - " + exclusions;
    }

    if (label == null)
    {
      label = "<unnamed>";
    }

    PreferenceNode preferenceNode = preferenceFilter.getPreferenceNode();
    if (preferenceNode != null)
    {
      String name = preferenceNode.getName();
      if (name != null)
      {
        label = name + " -> " + label;
      }
    }

    return label;
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public void notifyChanged(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(PreferenceFilter.class))
    {
      case ProjectConfigPackage.PREFERENCE_FILTER__INCLUSIONS:
      case ProjectConfigPackage.PREFERENCE_FILTER__EXCLUSIONS:
      case ProjectConfigPackage.PREFERENCE_FILTER__PREFERENCE_NODE:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, true));
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

  Map<Property, IWrapperItemProvider> wrappers = new HashMap<Property, IWrapperItemProvider>();

  private IWrapperItemProvider wrap(final PreferenceFilter preferenceFilter, int i, Property project)
  {
    IWrapperItemProvider wrapper = wrappers.get(project);
    if (wrapper == null)
    {
      wrapper = new DelegatingWrapperItemProvider(project, preferenceFilter, null, i, adapterFactory)
      {
        @Override
        public Object getParent(Object object)
        {
          return preferenceFilter;
        }

        @Override
        public Object getImage(Object object)
        {
          Object image = super.getImage(object);
          List<Object> images = new ArrayList<Object>(2);
          images.add(image);
          images.add(EMFEditPlugin.INSTANCE.getImage("full/ovr16/ControlledObject"));
          return image = new ComposedImage(images);
        }

        @Override
        public boolean hasChildren(Object object)
        {
          return false;
        }

        @Override
        public Collection<?> getChildren(Object object)
        {
          return Collections.emptyList();
        }
      };
      wrappers.put(project, wrapper);
    }
    else
    {
      wrapper.setIndex(i);
    }
    return wrapper;
  }

  @Override
  protected Command factorRemoveCommand(EditingDomain domain, CommandParameter commandParameter)
  {
    if (commandParameter.getFeature() == null)
    {
      Collection<?> collection = commandParameter.getCollection();
      List<Property> properties = new ArrayList<Property>();
      for (Object value : collection)
      {
        if (value instanceof Property)
        {
          properties.add((Property)value);
        }
      }

      if (properties.size() == collection.size())
      {
        PreferenceFilter preferenceFilter = (PreferenceFilter)commandParameter.getOwner();
        Pattern exclusions = preferenceFilter.getExclusions();
        StringBuilder newExclusions = new StringBuilder();
        if (exclusions != null)
        {
          newExclusions.append(exclusions);
        }

        Pattern inclusions = preferenceFilter.getInclusions();
        StringBuilder newInclusions = new StringBuilder();
        if (inclusions != null)
        {
          newInclusions.append(inclusions);
        }

        for (Property property : properties)
        {
          String name = property.getName();
          name = name.replace(".", "\\.");

          int index = newInclusions.indexOf(name);
          if (index != -1)
          {
            boolean matched = true;
            int start = index;
            int end = start + name.length();
            if (start != 0)
            {
              if (newInclusions.charAt(start - 1) == '|')
              {
                --start;
              }
              else
              {
                matched = false;
              }
            }

            if (matched)
            {
              if (end != newInclusions.length())
              {
                if (newInclusions.charAt(end) == '|')
                {
                  ++end;
                }
                else
                {
                  matched = false;
                }
              }
            }

            if (matched)
            {
              newInclusions.delete(start, end);
              continue;
            }
          }

          if (newExclusions.length() != 0)
          {
            newExclusions.append("|");
          }

          newExclusions.append(name);
        }

        CompoundCommand command = new CompoundCommand("Update filter patterns");
        command.append(SetCommand.create(domain, preferenceFilter, ProjectConfigPackage.Literals.PREFERENCE_FILTER__INCLUSIONS,
            Pattern.compile(newInclusions.toString())));
        command.append(SetCommand.create(domain, preferenceFilter, ProjectConfigPackage.Literals.PREFERENCE_FILTER__EXCLUSIONS,
            Pattern.compile(newExclusions.toString())));
        return command;
      }
    }

    return super.factorRemoveCommand(domain, commandParameter);
  }

  @Override
  public Collection<?> getChildren(Object object)
  {
    Collection<Object> result = new ArrayList<Object>();
    PreferenceFilter preferenceFilter = (PreferenceFilter)object;
    for (Property property : preferenceFilter.getProperties())
    {
      result.add(wrap(preferenceFilter, -1, property));
    }

    return result;
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
    return ProjectConfigEditPlugin.INSTANCE;
  }

}
