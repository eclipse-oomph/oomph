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
package org.eclipse.oomph.base.provider;

import org.eclipse.oomph.base.BaseAnnotationConstants;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.edit.BasePasteCommand;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.base.ModelElement} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelElementItemProvider extends ItemProviderAdapter
    implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource
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
   * @generated NOT
   */
  @Override
  public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
  {
    if (itemPropertyDescriptors == null)
    {
      itemPropertyDescriptors = new ArrayList<IItemPropertyDescriptor>()
      {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean add(IItemPropertyDescriptor itemPropertyDescriptor)
        {
          int size = size();
          if (size == 0)
          {
            super.add(itemPropertyDescriptor);
          }
          else
          {
            super.add(size - 1, itemPropertyDescriptor);
          }

          return true;
        }
      };

      itemPropertyDescriptors.add(new EClassPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator()));

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
  public Command createCommand(Object object, EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter)
  {
    if (commandClass == BasePasteCommand.class)
    {
      CommandParameter oldCommandParameter = commandParameter;
      commandParameter = unwrapCommandValues(commandParameter, commandClass);

      Collection<?> collection = commandParameter.getCollection();
      if (collection == null)
      {
        return UnexecutableCommand.INSTANCE;
      }

      Command result = createPasteCommand(domain, commandParameter.getEOwner(), commandParameter.getEStructuralFeature(), collection,
          commandParameter.getIndex());
      return wrapCommand(result, object, commandClass, commandParameter, oldCommandParameter);
    }

    return super.createCommand(object, domain, commandClass, commandParameter);
  }

  /**
   * A specialized form of {@link #createAddCommand(EditingDomain, EObject, EStructuralFeature, Collection, int)}
   */
  protected Command createPasteCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Collection<?> collection, int index)
  {
    return new PasteAlternativeVisitor(domain, owner, feature, collection, index)
    {
      @Override
      protected Collection<?> filterAlternatives(Collection<?> alternativeCollection)
      {
        return ModelElementItemProvider.this.filterAlternatives(domain, owner, alternativeCollection);
      }
    }.createCommand();
  }

  protected Collection<?> filterAlternatives(EditingDomain domain, Object owner, Collection<?> alternatives)
  {
    return alternatives;
  }

  @Override
  protected final Command createDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> collection)
  {
    Command dragAndDropCommand = createPrimaryDragAndDropCommand(domain, owner, location, operations, operation, collection);
    if (!dragAndDropCommand.canExecute())
    {
      Command alternativeDragAndDropCommand = createAlternativeDragAndDropCommand(domain, owner, location, operations, operation, collection);
      if (alternativeDragAndDropCommand.canExecute())
      {
        dragAndDropCommand.dispose();
        dragAndDropCommand = alternativeDragAndDropCommand;
      }
      else
      {
        alternativeDragAndDropCommand.dispose();
      }
    }

    return dragAndDropCommand;
  }

  protected Command createPrimaryDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> collection)
  {
    return new BaseDragAndDropCommand(domain, owner, location, operations, operation, collection);
  }

  protected Command createAlternativeDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation,
      Collection<?> collection)
  {
    return new DragAndDropAlternativeVisitor(domain, owner, location, operations, operation, collection)
    {
      @Override
      protected Collection<?> filterAlternatives(Collection<?> alternativeCollection)
      {
        return ModelElementItemProvider.this.filterAlternatives(domain, owner, location, operations, operation, alternativeCollection);
      }
    }.createCommand();
  }

  protected Collection<?> filterAlternatives(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> alternatives)
  {
    return filterAlternatives(domain, owner, alternatives);
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

    return new ItemPropertyDescriptor(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices, staticImage,
        category, filterFlags)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        return filterChoices(super.getChoiceOfValues(object), feature, object);
      }
    };
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
  public static class BaseDragAndDropCommand extends DragAndDropCommand
  {
    public BaseDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> collection)
    {
      super(domain, owner, location, operations, operation, collection);
    }

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
          return filterParent(itemDelegator, feature, super.getParent(object));
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

  /**
   *
   * @author Ed Merks
   */
  public abstract static class CommandAlternativeVisitor
  {
    protected EditingDomain domain;

    protected Object owner;

    protected Collection<?> collection;

    public CommandAlternativeVisitor(EditingDomain domain, Object owner, Collection<?> collection)
    {
      this.domain = domain;
      this.owner = owner;
      this.collection = collection;
    }

    public Command createCommand()
    {
      List<Object> alternativeCollection = new ArrayList<Object>();
      Map<EObject, EObject> fullConversionMap = new HashMap<EObject, EObject>();
      for (Object object : collection)
      {
        if (object instanceof EObject)
        {
          EObject eObject = (EObject)object;
          EClass eClass = eObject.eClass();
          for (EAnnotation eAnnotation : eClass.getEAnnotations())
          {
            if (BaseAnnotationConstants.ANNOTATION_CONVERSION.equals(eAnnotation.getSource()))
            {
              EMap<String, String> details = eAnnotation.getDetails();
              String conversionEClassURI = details.get(BaseAnnotationConstants.KEY_ECLASS);
              if (conversionEClassURI != null)
              {
                EClass conversionEClass = getConversionEClass(domain, owner, URI.createURI(conversionEClassURI));
                if (conversionEClass != null)
                {
                  Map<EObject, EObject> conversionMap = new HashMap<EObject, EObject>();
                  conversionMap.put(eClass, conversionEClass);

                  for (EStructuralFeature eStructuralFeature : eClass.getEAllStructuralFeatures())
                  {
                    String name = eStructuralFeature.getName();
                    EStructuralFeature conversionEStructuralFeature = conversionEClass.getEStructuralFeature(name);
                    conversionMap.put(eStructuralFeature, conversionEStructuralFeature);
                  }

                  for (Map.Entry<String, String> entry : details)
                  {
                    String key = entry.getKey();
                    EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(key);
                    if (eStructuralFeature != null)
                    {
                      String value = entry.getValue();
                      EStructuralFeature conversionEStructuralFeature = conversionEClass.getEStructuralFeature(value);
                      conversionMap.put(eStructuralFeature, conversionEStructuralFeature);
                    }
                  }

                  ConversionCopier copier = new ConversionCopier(conversionMap);
                  EObject copy = copier.copy(eObject);
                  copier.copyReferences();
                  Command probeCommand = createCommand(Collections.singleton(copy));
                  if (probeCommand.canExecute())
                  {
                    alternativeCollection.add(eObject);
                    fullConversionMap.putAll(conversionMap);
                  }

                  probeCommand.dispose();
                }
              }
            }
          }
        }
      }

      if (!alternativeCollection.isEmpty())
      {
        ConversionCopier copier = new ConversionCopier(fullConversionMap);
        final Collection<?> alternatives = filterAlternatives(copier.copyAll(alternativeCollection));
        copier.copyReferences();

        if (!alternatives.isEmpty())
        {
          Command command = createCommand(alternatives);
          if (command.canExecute())
          {
            return command;
          }

          command.dispose();
        }
      }

      return UnexecutableCommand.INSTANCE;
    }

    protected EClass getConversionEClass(EditingDomain domain, Object owner, URI uri)
    {
      ResourceSet resourceSet = domain.getResourceSet();
      if (resourceSet != null)
      {
        EObject eObject = resourceSet.getEObject(uri, false);
        if (eObject instanceof EClass)
        {
          return (EClass)eObject;
        }

        try
        {
          ResourceSet temporaryResourceSet = new ResourceSetImpl();
          eObject = temporaryResourceSet.getEObject(uri, true);
          if (eObject instanceof EClass)
          {
            eObject = resourceSet.getEObject(uri, true);
            if (eObject instanceof EClass)
            {
              return (EClass)eObject;
            }
          }
        }
        catch (RuntimeException ex)
        {
          // Ignore.
        }
      }

      return null;
    }

    protected abstract Command createCommand(Collection<?> alternatives);

    protected Collection<?> filterAlternatives(Collection<?> alternatives)
    {
      return alternatives;
    }
  }

  /**
   * @author Ed Merks
   */
  public static class PasteAlternativeVisitor extends CommandAlternativeVisitor
  {
    protected Object feature;

    protected int index;

    public PasteAlternativeVisitor(EditingDomain domain, Object owner, Object feature, Collection<?> collection, int index)
    {
      super(domain, owner, collection);
      this.feature = feature;
      this.index = index;
    }

    @Override
    protected Command createCommand(Collection<?> alternatives)
    {
      return AddCommand.create(domain, owner, feature, alternatives, index);
    }
  }

  /**
   *
   * @author Ed Merks
   */
  public static class DragAndDropAlternativeVisitor extends CommandAlternativeVisitor
  {
    protected float location;

    protected int operations;

    protected int operation;

    public DragAndDropAlternativeVisitor(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> collection)
    {
      super(domain, owner, collection);
      this.location = location;
      this.operations = operations;
      this.operation = operation;
    }

    @Override
    protected Command createCommand(final Collection<?> alternatives)
    {
      return new BaseDragAndDropCommand(domain, owner, location, operations, operation, alternatives)
      {
        @Override
        public boolean validate(Object owner, float location, int operations, int operation, java.util.Collection<?> collection)
        {
          // If the original collection is the same as the collection being validated, then reuse the already computed alternatives again.
          if (DragAndDropAlternativeVisitor.this.collection.equals(collection))
          {
            collection = alternatives;
          }

          return super.validate(owner, location, operations, operation, collection);
        }
      };
    }
  }

  /**
   *
   * @author Ed Merks
   */
  public static class ConversionCopier extends EcoreUtil.Copier
  {
    private static final long serialVersionUID = 1L;

    private Map<EObject, EObject> conversionMap;

    public ConversionCopier(Map<EObject, EObject> conversionMap)
    {
      this.conversionMap = conversionMap;
    }

    @Override
    protected EClass getTarget(EClass eClass)
    {
      return (EClass)conversionMap.get(eClass);
    }

    @Override
    protected EStructuralFeature getTarget(EStructuralFeature eStructuralFeature)
    {
      return (EStructuralFeature)conversionMap.get(eStructuralFeature);
    }

    @Override
    protected void copyAttributeValue(EAttribute eAttribute, EObject eObject, Object value, Setting setting)
    {
      if (value != null)
      {
        // Do data conversion to a string representation.
        EDataType eAttributeType = eAttribute.getEAttributeType();
        EDataType eType = (EDataType)setting.getEStructuralFeature().getEType();
        Class<?> instanceClass = eType.getInstanceClass();
        Class<?> instanceClass2 = eAttributeType.getInstanceClass();
        if (instanceClass != instanceClass2 || instanceClass == null)
        {
          if (eAttribute.isMany())
          {
            List<Object> values = new ArrayList<Object>();
            for (Object element : (Collection<?>)value)
            {
              values.add(EcoreUtil.createFromString(eType, EcoreUtil.convertToString(eAttributeType, element)));
            }

            value = values;
          }
          else
          {
            value = EcoreUtil.createFromString(eType, EcoreUtil.convertToString(eAttributeType, value));
          }
        }
      }

      super.copyAttributeValue(eAttribute, eObject, value, setting);
    }
  }

  /**
   * @author Ed Merks
   */
  public static class EClassPropertyDescriptor implements IItemPropertyDescriptor
  {
    private static Object image;

    private static final IItemLabelProvider LABEL_PROVIDER = new IItemLabelProvider()
    {
      public String getText(Object object)
      {
        EClass eClass = (EClass)object;
        String instanceTypeName = eClass.getInstanceTypeName();
        if (instanceTypeName != null)
        {
          return instanceTypeName;
        }

        return EcoreUtil.getURI(eClass).toString();
      }

      public Object getImage(Object object)
      {
        return image;
      }
    };

    public EClassPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator)
    {
      if (image == null)
      {
        new AdapterFactoryItemDelegator(adapterFactory).getImage(EcorePackage.Literals.ECLASS);
      }
    }

    public IItemLabelProvider getLabelProvider(Object object)
    {
      return LABEL_PROVIDER;
    }

    public Object getPropertyValue(Object object)
    {
      return ((EObject)object).eClass();
    }

    public boolean isPropertySet(Object object)
    {
      return true;
    }

    public boolean canSetProperty(Object object)
    {
      return false;
    }

    public void resetPropertyValue(Object object)
    {
    }

    public void setPropertyValue(Object object, Object value)
    {
    }

    public String getCategory(Object object)
    {
      return null;
    }

    public String getDescription(Object object)
    {
      return "The model class of this object";
    }

    public String getDisplayName(Object object)
    {
      return "Model Class";
    }

    public String[] getFilterFlags(Object object)
    {
      return PropertiesUtil.EXPERT_FILTER;
    }

    public Object getHelpContextIds(Object object)
    {
      return null;
    }

    public String getId(Object object)
    {
      return getDisplayName(object);
    }

    public boolean isCompatibleWith(Object object, Object anotherObject, IItemPropertyDescriptor anotherPropertyDescriptor)
    {
      return false;
    }

    public Object getFeature(Object object)
    {
      return "eClass";
    }

    public boolean isMany(Object object)
    {
      return false;
    }

    public Collection<?> getChoiceOfValues(Object object)
    {
      return Collections.emptyList();
    }

    public boolean isMultiLine(Object object)
    {
      return false;
    }

    public boolean isSortChoices(Object object)
    {
      return false;
    }
  }
}
