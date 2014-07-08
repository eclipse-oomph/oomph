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
package org.eclipse.oomph.base.provider;

import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.base.ModelElement} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelElementItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider,
    ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelElementItemProvider(AdapterFactory adapterFactory)
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
      childrenFeatures.add(BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS);
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
  public boolean hasChildren(Object object)
  {
    return hasChildren(object, true);
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
    return getString("_UI_ModelElement_type");
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

    switch (notification.getFeatureID(ModelElement.class))
    {
      case BasePackage.MODEL_ELEMENT__ANNOTATIONS:
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

    newChildDescriptors.add(createChildParameter(BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS, BaseFactory.eINSTANCE.createAnnotation()));
  }

  @Override
  protected Command createDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> collection)
  {
    return new DragAndDropCommand(domain, owner, location, operations, operation, collection)
    {
      @Override
      protected boolean prepareDropLinkOn()
      {
        dragCommand = IdentityCommand.INSTANCE;
        dropCommand = SetCommand.create(domain, owner, null, collection);

        // If we can't set the collection, try setting use the single value of the collection.
        //
        if (!dropCommand.canExecute() && collection.size() == 1)
        {
          dropCommand.dispose();
          dropCommand = SetCommand.create(domain, owner, null, collection.iterator().next());
        }

        if (!dropCommand.canExecute() || !analyzeForDropLinkEnablement(dropCommand))
        {
          dropCommand.dispose();
          dropCommand = AddCommand.create(domain, owner, null, collection);
          if (!analyzeForDropLinkEnablement(dropCommand))
          {
            dropCommand.dispose();
            dropCommand = UnexecutableCommand.INSTANCE;
          }
        }

        boolean result = dropCommand.canExecute();
        return result;
      }

      protected boolean analyzeForDropLinkEnablement(Command command)
      {
        if (command instanceof AddCommand)
        {
          AddCommand addCommand = (AddCommand)command;
          if (isNonContainment(addCommand.getFeature()))
          {
            return true;
          }

          // If it's an add command on a proxy resolving containment reference and the objects being added all have no container then we can link them.
          EList<?> ownerList = addCommand.getOwnerList();
          if (ownerList != null)
          {
            if (((EReference)addCommand.getFeature()).isResolveProxies())
            {
              for (Object value : addCommand.getCollection())
              {
                EObject eObject = (EObject)value;
                if (eObject.eContainer() != null)
                {
                  return false;
                }
              }

              return true;
            }
          }

          return false;
        }
        else if (command instanceof SetCommand)
        {
          return isNonContainment(((SetCommand)command).getFeature());
        }
        else if (command instanceof CommandWrapper)
        {
          return analyzeForDropLinkEnablement(((CommandWrapper)command).getCommand());
        }
        else if (command instanceof CompoundCommand)
        {
          for (Command childCommand : ((CompoundCommand)command).getCommandList())
          {
            if (analyzeForDropLinkEnablement(childCommand))
            {
              return true;
            }
          }
        }

        return false;
      }
    };
  }

  @Override
  protected ItemPropertyDescriptor createItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName,
      String description, EStructuralFeature feature, boolean isSettable, boolean multiLine, boolean sortChoices, Object staticImage, String category,
      String[] filterFlags)
  {
    if (feature instanceof EReference)
    {
      return new HierarchicalPropertyDescriptor(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices,
          staticImage, category, filterFlags)
      {
        @Override
        protected Object filterParent(AdapterFactoryItemDelegator itemDelegator, EStructuralFeature feature, Object object)
        {
          return ModelElementItemProvider.this.filterParent(itemDelegator, feature, object);
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

  protected Object filterParent(AdapterFactoryItemDelegator itemDelegator, EStructuralFeature feature, Object object)
  {
    return object;
  }

  protected Collection<?> filterChoices(Collection<?> choices, EStructuralFeature feature, Object object)
  {
    return choices;
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
    return ((IChildCreationExtender)adapterFactory).getResourceLocator();
  }

  /**
   * @author Ed Merks
   */
  public static class HierarchicalPropertyDescriptor extends ItemPropertyDescriptor
  {
    protected IItemLabelProvider labelProvider;

    public HierarchicalPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
        EStructuralFeature feature, boolean isSettable, boolean multiLine, boolean sortChoices, Object staticImage, String category, String[] filterFlags)
    {
      super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices, staticImage, category, filterFlags);
    }

    protected IItemLabelProvider createLabelProvider()
    {
      return new HierarchicalItemLabelProvider(itemDelegator)
      {
        @Override
        protected Object getParent(Object object)
        {
          Object parent = filterParent(itemDelegator, feature, super.getParent(object));
          return parent;
        }
      };
    }

    protected Object filterParent(AdapterFactoryItemDelegator itemDelegator, EStructuralFeature feature, Object object)
    {
      return object;
    }

    @Override
    public IItemLabelProvider getLabelProvider(Object object)
    {
      if (labelProvider == null)
      {
        labelProvider = createLabelProvider();
      }

      return labelProvider;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class HierarchicalItemLabelProvider implements IItemLabelProvider
  {
    protected AdapterFactoryItemDelegator itemDelegator;

    public HierarchicalItemLabelProvider(AdapterFactoryItemDelegator itemDelegator)
    {
      this.itemDelegator = itemDelegator;
    }

    public String getText(Object object)
    {
      if (object instanceof EList<?>)
      {
        StringBuffer result = new StringBuffer();
        for (Object child : (List<?>)object)
        {
          if (result.length() != 0)
          {
            result.append(", ");
          }
          result.append(getText(child));
        }

        return result.toString();
      }

      StringBuilder builder = new StringBuilder(getBasicText(object));
      int index = builder.length();
      object = getParent(object);
      while (object != null)
      {
        if (builder.length() == index)
        {
          builder.insert(index, " (");
          index += 2;
        }
        else
        {
          builder.insert(index, " - ");
        }

        String text = getQualifierText(object);
        builder.insert(index, text);

        object = getParent(object);
      }

      if (builder.length() != index)
      {
        builder.append(")");
      }

      return builder.toString();
    }

    protected String getBasicText(Object object)
    {
      return itemDelegator.getText(object);
    }

    protected String getQualifierText(Object object)
    {
      return itemDelegator.getText(object);
    }

    protected Object getParent(Object object)
    {
      Object parent = itemDelegator.getParent(object);
      if (parent instanceof Resource)
      {
        Object unwrappedObject = AdapterFactoryEditingDomain.unwrap(object);
        if (unwrappedObject instanceof EObject)
        {
          return ((EObject)unwrappedObject).eContainer();
        }

        return null;
      }

      return parent;
    }

    public Object getImage(Object object)
    {
      return itemDelegator.getImage(object);
    }
  }
}
