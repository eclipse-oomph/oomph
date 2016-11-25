/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.SetupTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SetupTaskItemProvider extends ModelElementItemProvider
{
  private static final Map<Set<Trigger>, IItemLabelProvider> EXCLUSION_LABEL_PROVIDERS = new HashMap<Set<Trigger>, IItemLabelProvider>();

  static
  {
    for (Set<Trigger> validTriggers : Trigger.LITERALS.keySet())
    {
      final Map<Set<Trigger>, String> exclusionLabels = new HashMap<Set<Trigger>, String>();

      for (Set<Trigger> triggers : Trigger.LITERALS.keySet())
      {
        Set<Trigger> compliment = new LinkedHashSet<Trigger>(validTriggers);
        compliment.removeAll(triggers);
        exclusionLabels.put(triggers, Trigger.LITERALS.get(compliment));
      }

      EXCLUSION_LABEL_PROVIDERS.put(validTriggers, new IItemLabelProvider()
      {
        public String getText(Object object)
        {
          return exclusionLabels.get(object);
        }

        public Object getImage(Object object)
        {
          return null;
        }
      });
    }
  }

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupTaskItemProvider(AdapterFactory adapterFactory)
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

      addIDPropertyDescriptor(object);
      addDescriptionPropertyDescriptor(object);
      addScopeTypePropertyDescriptor(object);
      addExcludedTriggersPropertyDescriptor(object);
      addDisabledPropertyDescriptor(object);
      addPredecessorsPropertyDescriptor(object);
      addSuccessorsPropertyDescriptor(object);
      addRestrictionsPropertyDescriptor(object);
      addFilterPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Predecessors feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addPredecessorsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SetupTask_predecessors_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_SetupTask_predecessors_feature", "_UI_SetupTask_type"),
        SetupPackage.Literals.SETUP_TASK__PREDECESSORS, true, false, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert.conditional" }));
  }

  /**
   * This adds a property descriptor for the Successors feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addSuccessorsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SetupTask_successors_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_SetupTask_successors_feature", "_UI_SetupTask_type"), SetupPackage.Literals.SETUP_TASK__SUCCESSORS,
        true, false, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert.conditional" }));
  }

  /**
   * This adds a property descriptor for the ID feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIDPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SetupTask_iD_feature"), getString("_UI_PropertyDescriptor_description", "_UI_SetupTask_iD_feature", "_UI_SetupTask_type"),
        SetupPackage.Literals.SETUP_TASK__ID, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Restrictions feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRestrictionsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SetupTask_restrictions_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_SetupTask_restrictions_feature", "_UI_SetupTask_type"),
        SetupPackage.Literals.SETUP_TASK__RESTRICTIONS, true, false, true, null, null, new String[] { "org.eclipse.ui.views.properties.expert.conditional" }));
  }

  /**
   * This adds a property descriptor for the Filter feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addFilterPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SetupTask_filter_feature"), getString("_UI_PropertyDescriptor_description", "_UI_SetupTask_filter_feature", "_UI_SetupTask_type"),
        SetupPackage.Literals.SETUP_TASK__FILTER, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null,
        new String[] { "org.eclipse.ui.views.properties.expert.conditional" }));
  }

  @Override
  protected Object filterParent(AdapterFactoryItemDelegator itemDelegator, EStructuralFeature feature, Object object)
  {
    Object result = super.filterParent(itemDelegator, feature, object);
    if (result instanceof Index)
    {
      return null;
    }

    return result;
  }

  @Override
  protected Collection<?> filterChoices(Collection<?> choices, EStructuralFeature feature, Object object)
  {
    SetupTask setupTask = (SetupTask)object;
    int featureID = setupTask.eClass().getFeatureID(feature);

    switch (featureID)
    {
      case SetupPackage.SETUP_TASK__RESTRICTIONS:
      {
        ScopeType scope = setupTask.getScopeType();

        Collection<?> result = new ArrayList<Object>(choices);
        for (Iterator<?> it = result.iterator(); it.hasNext();)
        {
          Object value = it.next();
          if (value instanceof Scope)
          {
            Scope restriction = (Scope)value;

            switch (scope)
            {
              case USER:
              {
                continue;
              }

              case STREAM:
              case PROJECT:
              {
                if (!(restriction instanceof ProductVersion))
                {
                  break;
                }

                continue;
              }
            }

            it.remove();
          }
        }

        return result;
      }

      case SetupPackage.SETUP_TASK__PREDECESSORS:
      case SetupPackage.SETUP_TASK__SUCCESSORS:
      {
        boolean direction = featureID == SetupPackage.SETUP_TASK__PREDECESSORS;
        List<Scope> scopes = getScopes(setupTask);
        int scopesSize = scopes.size();
        Collection<?> result = new ArrayList<Object>(choices);
        LOOP: for (Iterator<?> it = result.iterator(); it.hasNext();)
        {
          Object value = it.next();
          if (value instanceof SetupTask)
          {
            SetupTask targetSetupTask = (SetupTask)value;
            if (!targetSetupTask.eIsProxy() && (direction ? !targetSetupTask.requires(setupTask) : !setupTask.requires(targetSetupTask))
                && !onlyHasVariables(targetSetupTask))
            {
              if (scopesSize == 1)
              {
                Scope scope = scopes.get(0);
                if (scope instanceof User)
                {
                  continue;
                }
                else if (scope instanceof Installation)
                {
                  Installation installation = (Installation)scope;
                  ProductVersion productVersion = installation.getProductVersion();
                  if (productVersion != null)
                  {
                    List<Scope> installationScopes = getScopes(productVersion);
                    int installationScopesSize = installationScopes.size();
                    List<Scope> targetScopes = getScopes(targetSetupTask);
                    int targetScopesSize = targetScopes.size();
                    if (targetScopesSize <= installationScopesSize && installationScopes.subList(0, targetScopesSize).equals(targetScopes))
                    {
                      continue;
                    }
                  }
                }
                else if (scope instanceof Workspace)
                {
                  List<Scope> targetScopes = getScopes(targetSetupTask);
                  int targetScopesSize = targetScopes.size();

                  Workspace workspace = (Workspace)scope;
                  for (Stream stream : workspace.getStreams())
                  {
                    List<Scope> workspaceScopes = getScopes(stream);
                    int workspaceScopesSize = workspaceScopes.size();
                    if (targetScopesSize <= workspaceScopesSize && workspaceScopes.subList(0, targetScopesSize).equals(targetScopes))
                    {
                      continue LOOP;
                    }
                  }
                }
              }

              List<Scope> targetScopes = getScopes(targetSetupTask);
              int targetScopesSize = targetScopes.size();
              if (targetScopesSize <= scopesSize && scopes.subList(0, targetScopesSize).equals(targetScopes))
              {
                continue;
              }
            }

            it.remove();
          }
        }

        return result;
      }

      default:
      {
        return super.filterChoices(choices, feature, object);
      }
    }
  }

  private List<Scope> getScopes(SetupTask setupTask)
  {
    return getScopes(setupTask.getScope());
  }

  private List<Scope> getScopes(Scope scope)
  {
    List<Scope> scopes = new ArrayList<Scope>();
    for (; scope != null; scope = scope.getParentScope())
    {
      scopes.add(0, scope);
    }

    return scopes;
  }

  private boolean onlyHasVariables(SetupTask setupTask)
  {
    if (setupTask instanceof VariableTask)
    {
      return true;
    }

    if (setupTask instanceof CompoundTask)
    {
      CompoundTask compoundTask = (CompoundTask)setupTask;
      for (SetupTask childSetupTask : compoundTask.getSetupTasks())
      {
        if (!onlyHasVariables(childSetupTask))
        {
          return false;
        }
      }

      return true;
    }

    return false;
  }

  /**
   * This adds a property descriptor for the Scope Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addScopeTypePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SetupTask_scopeType_feature"), getString("_UI_PropertyDescriptor_description", "_UI_SetupTask_scopeType_feature", "_UI_SetupTask_type"),
        SetupPackage.Literals.SETUP_TASK__SCOPE_TYPE, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null,
        new String[] { "org.eclipse.ui.views.properties.expert" }));
  }

  /**
   * This adds a property descriptor for the Excluded Triggers feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addExcludedTriggersPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SetupTask_excludedTriggers_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_SetupTask_excludedTriggers_feature", "_UI_SetupTask_type"),
        SetupPackage.Literals.SETUP_TASK__EXCLUDED_TRIGGERS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, PropertiesUtil.EXPERT_FILTER)
    {
      @Override
      public IItemLabelProvider getLabelProvider(Object object)
      {
        final SetupTask setupTask = (SetupTask)object;
        return EXCLUSION_LABEL_PROVIDERS.get(setupTask.getValidTriggers());
      }

      @Override
      public String getDisplayName(Object object)
      {
        return "Triggers";
      }

      @Override
      public String getDescription(Object object)
      {
        return "The triggers for which the task is applicable";
      }

      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        SetupTask setupTask = (SetupTask)object;
        Set<Trigger> validTriggers = setupTask.getValidTriggers();
        List<Set<Trigger>> result = new ArrayList<Set<Trigger>>(Trigger.LITERALS.keySet());
        for (Iterator<Set<Trigger>> it = result.iterator(); it.hasNext();)
        {
          if (!validTriggers.containsAll(it.next()))
          {
            it.remove();
          }
        }
        Collections.reverse(result);
        return result;
      }
    });
  }

  /**
   * This adds a property descriptor for the Description feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addDescriptionPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SetupTask_description_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_SetupTask_description_feature", "_UI_SetupTask_type"),
        SetupPackage.Literals.SETUP_TASK__DESCRIPTION, true, true, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Disabled feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addDisabledPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_SetupTask_disabled_feature"), getString("_UI_PropertyDescriptor_description", "_UI_SetupTask_disabled_feature", "_UI_SetupTask_type"),
        SetupPackage.Literals.SETUP_TASK__DISABLED, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null,
        new String[] { "org.eclipse.ui.views.properties.expert" }));
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
    String label = ((SetupTask)object).getID();
    return label == null || label.length() == 0 ? getString("_UI_SetupTask_type") : getString("_UI_SetupTask_type") + " " + label;
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

    switch (notification.getFeatureID(SetupTask.class))
    {
      case SetupPackage.SETUP_TASK__ID:
      case SetupPackage.SETUP_TASK__DESCRIPTION:
      case SetupPackage.SETUP_TASK__SCOPE_TYPE:
      case SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS:
      case SetupPackage.SETUP_TASK__DISABLED:
      case SetupPackage.SETUP_TASK__RESTRICTIONS:
      case SetupPackage.SETUP_TASK__FILTER:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
    }
    super.notifyChanged(notification);
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
    switch (notification.getFeatureID(SetupTask.class))
    {
      case SetupPackage.SETUP_TASK__DISABLED:
        updateChildren(notification);
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, true));
        return;
      default:
        super.notifyChanged(notification);
    }
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
