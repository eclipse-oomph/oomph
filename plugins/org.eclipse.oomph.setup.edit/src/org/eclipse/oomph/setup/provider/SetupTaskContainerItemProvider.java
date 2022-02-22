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
import org.eclipse.oomph.edit.BaseAdapterFactoryEditingDomain;
import org.eclipse.oomph.setup.Argument;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.Parameter;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.WorkspaceTask;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.SetupTaskContainer} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SetupTaskContainerItemProvider extends ModelElementItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupTaskContainerItemProvider(AdapterFactory adapterFactory)
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

    }
    return itemPropertyDescriptors;
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
      childrenFeatures.add(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS);
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
    return getString("_UI_SetupTaskContainer_type"); //$NON-NLS-1$
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

    switch (notification.getFeatureID(SetupTaskContainer.class))
    {
      case SetupPackage.SETUP_TASK_CONTAINER__SETUP_TASKS:
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
  private void collectNewChildDescriptorsGen(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createInstallationTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createWorkspaceTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createCompoundTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createVariableTask()));

    newChildDescriptors
        .add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createStringSubstitutionTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createRedirectionTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createEclipseIniTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createLinkLocationTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createPreferenceTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createResourceCopyTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createResourceCreationTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createTextModifyTask()));

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS, SetupFactory.eINSTANCE.createMacroTask()));
  }

  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    collectNewChildDescriptorsGen(newChildDescriptors, object);
    removeUnwantedTasks(newChildDescriptors, object);
  }

  public static void removeUnwantedTasks(Collection<Object> newChildDescriptors, Object object)
  {
    boolean catalog = object instanceof ProductCatalog || object instanceof ProjectCatalog;

    for (Iterator<Object> it = newChildDescriptors.iterator(); it.hasNext();)
    {
      Object newChildDescriptor = it.next();
      if (newChildDescriptor instanceof CommandParameter)
      {
        Object value = ((CommandParameter)newChildDescriptor).getValue();
        if (isDeprecated(value))
        {
          it.remove();
        }
        else if (!catalog)
        {
          if (value instanceof InstallationTask || value instanceof WorkspaceTask)
          {
            it.remove();
          }
        }
      }
    }
  }

  public static boolean isDeprecated(Object value)
  {
    if (value != null)
    {
      Deprecated annotation = value.getClass().getAnnotation(Deprecated.class);
      if (annotation != null)
      {
        return true;
      }
    }

    return false;
  }

  @Override
  protected Command factorAddCommand(EditingDomain domain, CommandParameter commandParameter)
  {
    return super.factorAddCommand(domain, transformCommandParameter(domain, commandParameter));
  }

  @Override
  protected Command createAddCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Collection<?> collection, int index)
  {
    return createAddCommandWithMacroTaskSupport(domain, owner, feature, collection, index);
  }

  public static Command createAddCommandWithMacroTaskSupport(EditingDomain domain, EObject owner, EStructuralFeature feature, Collection<?> collection,
      int index)
  {
    return new AddCommand(domain, owner, feature, collection, index)
    {
      @Override
      public void doExecute()
      {
        super.doExecute();

        if (domain instanceof BaseAdapterFactoryEditingDomain)
        {
          ((BaseAdapterFactoryEditingDomain)domain).handledAdditions(collection);
        }
      }
    };
  }

  public static CommandParameter transformCommandParameter(EditingDomain domain, CommandParameter commandParameter)
  {
    Collection<?> collection = commandParameter.getCollection();
    if (collection != null && !collection.isEmpty())
    {
      EObject eOwner = commandParameter.getEOwner();
      if (eOwner != null)
      {
        List<Object> augmentedCollection = new ArrayList<>();
        for (Object object : collection)
        {
          Object unwrappedObject = AdapterFactoryEditingDomain.unwrap(object);
          if (unwrappedObject instanceof Macro)
          {
            Macro macro = (Macro)unwrappedObject;
            if (macro.eResource() != null)
            {
              MacroTask macroTask = createMacroTask((SetupTaskContainer)eOwner, macro);
              augmentedCollection.add(macroTask);
            }
            else
            {
              augmentedCollection.add(macro);
            }
          }
          else
          {
            augmentedCollection.add(object);
          }
        }

        return new CommandParameter(commandParameter.getOwner(), commandParameter.getFeature(), commandParameter.getValue(), augmentedCollection,
            commandParameter.getIndex());
      }
    }

    return commandParameter;
  }

  @SuppressWarnings("nls")
  private static String getID(String label)
  {
    if (StringUtil.isEmpty(label))
    {
      return "macro";
    }

    String lowerCaseLabel = label.toLowerCase();
    List<String> explode = StringUtil.explode(lowerCaseLabel.replaceAll("[^\\p{IsAlphabetic}\\p{Digit}]", "."), ".");
    explode.removeAll(Collections.singleton(""));
    String implode = StringUtil.implode(explode, '.');
    if (StringUtil.isEmpty(implode))
    {
      return "macro";
    }

    if (!Character.isAlphabetic(implode.charAt(0)))
    {
      return "_" + implode;
    }

    return implode;
  }

  public static MacroTask createMacroTask(SetupTaskContainer setupTaskContainer, Macro macro)
  {
    MacroTask macroTask = SetupFactory.eINSTANCE.createMacroTask();
    macroTask.setMacro(macro);

    String id = getID(macro.getName());
    String uniqueID = id;
    Resource resource = setupTaskContainer.eResource();
    if (resource != null)
    {
      int count = 0;
      while (resource.getEObject(uniqueID) != null)
      {
        if (Character.isDigit(id.charAt(id.length() - 1)))
        {
          uniqueID = id + "_" + ++count; //$NON-NLS-1$
        }
        else
        {
          uniqueID = id + ++count;
        }
      }
    }

    macroTask.setID(uniqueID);

    EList<Parameter> parameters = macro.getParameters();
    if (!parameters.isEmpty())
    {
      List<Argument> arguments = macroTask.getArguments();
      for (Parameter parameter : parameters)
      {
        Argument argument = SetupFactory.eINSTANCE.createArgument();
        argument.setParameter(parameter);
        if (parameter.getDefaultValue() == null)
        {
          String parameterName = parameter.getName();
          argument.setValue(parameterName + "_value"); //$NON-NLS-1$
        }

        arguments.add(argument);
      }
    }

    return macroTask;
  }
}
