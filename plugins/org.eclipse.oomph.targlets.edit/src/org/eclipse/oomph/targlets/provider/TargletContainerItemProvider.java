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
package org.eclipse.oomph.targlets.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.targlets.TargletContainer;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.AttributeValueWrapperItemProvider;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.core.runtime.IProgressMonitor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.targlets.TargletContainer} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TargletContainerItemProvider extends ModelElementItemProvider
{
  private static final String SEPARATOR = " - "; //$NON-NLS-1$

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletContainerItemProvider(AdapterFactory adapterFactory)
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
      addComposedTargetsPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
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
        getString("_UI_TargletContainer_iD_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_TargletContainer_iD_feature", "_UI_TargletContainer_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        TargletPackage.Literals.TARGLET_CONTAINER__ID, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Composed Targets feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addComposedTargetsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletContainer_composedTargets_feature"), //$NON-NLS-1$
        getString("_UI_TargletContainer_composedTargets_description"), //$NON-NLS-1$
        TargletPackage.Literals.TARGLET_CONTAINER__COMPOSED_TARGETS, true, false, true, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  @Override
  protected Collection<?> filterChoices(Collection<?> choices, EStructuralFeature feature, Object object)
  {
    if (feature == TargletPackage.Literals.TARGLET_CONTAINER__COMPOSED_TARGETS)
    {
      Set<String> targetDefinitionNames = getTargetDefinitionNames();
      if (targetDefinitionNames != null)
      {
        return targetDefinitionNames;
      }
    }

    return super.filterChoices(choices, feature, object);
  }

  @Override
  protected boolean isChoiceArbitrary(EStructuralFeature feature, Object object)
  {
    return feature == TargletPackage.Literals.TARGLET_CONTAINER__COMPOSED_TARGETS || super.isChoiceArbitrary(feature, object);
  }

  @Override
  protected IItemLabelProvider getLabelProvider(IItemLabelProvider itemLabelProvider, EStructuralFeature feature, Object object)
  {
    if (feature == TargletPackage.Literals.TARGLET_CONTAINER__COMPOSED_TARGETS)
    {
      return new ComposedTargetItemLabelProvider();
    }

    return super.getLabelProvider(itemLabelProvider, feature, object);
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
      childrenFeatures.add(TargletPackage.Literals.TARGLET_CONTAINER__COMPOSED_TARGETS);
      childrenFeatures.add(TargletPackage.Literals.TARGLET_CONTAINER__TARGLETS);
    }
    return childrenFeatures;
  }

  @Override
  protected Object createWrapper(EObject object, EStructuralFeature feature, Object value, int index)
  {
    if (feature == TargletPackage.Literals.TARGLET_CONTAINER__COMPOSED_TARGETS)
    {
      return createComposedTargetWrapper(object, TargletPackage.Literals.TARGLET_CONTAINER__COMPOSED_TARGETS, (String)value, index, adapterFactory,
          getResourceLocator());
    }

    return super.createWrapper(object, feature, value, index);
  }

  @Override
  protected Command createCreateChildCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Object value, int index, Collection<?> collection)
  {
    if (feature == TargletPackage.Literals.TARGLET_CONTAINER__COMPOSED_TARGETS)
    {
      return createCreateChildComposedTargetCommand(domain, owner, TargletPackage.Literals.TARGLET_CONTAINER__COMPOSED_TARGETS, value, index, collection, this);
    }

    return super.createCreateChildCommand(domain, owner, feature, value, index, collection);
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
   * This returns TargletContainer.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/TargletContainer")); //$NON-NLS-1$
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
    String label = ((TargletContainer)object).getID();
    return label == null || label.length() == 0 ? getString("_UI_TargletContainer_type") : //$NON-NLS-1$
        getString("_UI_TargletContainer_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
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

    switch (notification.getFeatureID(TargletContainer.class))
    {
      case TargletPackage.TARGLET_CONTAINER__ID:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case TargletPackage.TARGLET_CONTAINER__COMPOSED_TARGETS:
      case TargletPackage.TARGLET_CONTAINER__TARGLETS:
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
   * @generated NOT
   */
  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors.add(createChildParameter(TargletPackage.Literals.TARGLET_CONTAINER__TARGLETS, TargletFactory.eINSTANCE.createTarglet()));

    collectNewChildComposedTargetDescriptors(newChildDescriptors, object, TargletPackage.Literals.TARGLET_CONTAINER__COMPOSED_TARGETS);
  }

  public static Set<String> getTargetDefinitionNames(EObject eObject, EStructuralFeature feature)
  {
    Set<String> targetDefinitionNames = getTargetDefinitionNames();
    if (targetDefinitionNames != null)
    {
      targetDefinitionNames.removeAll((Collection<?>)eObject.eGet(feature));
    }
    return targetDefinitionNames;
  }

  public static Set<String> getTargetDefinitionNames()
  {
    Map<String, String> targetDefinitions = getTargetDefinitions();
    if (targetDefinitions != null)
    {
      return new TreeSet<>(targetDefinitions.keySet());
    }

    return null;
  }

  private static long timestamp;

  private static Map<String, String> targetDefinitionInfo;

  private static Map<String, String> getTargetDefinitions()
  {
    long now = System.currentTimeMillis();
    if (targetDefinitionInfo != null && now - timestamp < 5000)
    {
      return targetDefinitionInfo;
    }

    Map<String, String> result = new TreeMap<>();
    try
    {
      Object[] targetDefinitions = (Object[])ReflectUtil
          .getMethod(CommonPlugin.loadClass("org.eclipse.oomph.util.pde", "org.eclipse.oomph.util.pde.TargetPlatformUtil"), "getTargetDefinitions", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
              IProgressMonitor.class)
          .invoke(null, (Object)null);
      for (Object targetDefinition : targetDefinitions)
      {
        String name = ReflectUtil.invokeMethod("getName", targetDefinition); //$NON-NLS-1$
        Object handle = ReflectUtil.invokeMethod("getHandle", targetDefinition); //$NON-NLS-1$
        String memento = ReflectUtil.invokeMethod("getMemento", handle); //$NON-NLS-1$
        result.put(name, memento);
      }

      timestamp = now;
      return targetDefinitionInfo = result;
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public static Object createComposedTargetWrapper(EObject eObject, EAttribute feature, String value, int index, AdapterFactory adapterFactory,
      ResourceLocator resourceLocator)
  {
    return new AttributeValueWrapperItemProvider(value, eObject, feature, index, adapterFactory, resourceLocator)
    {
      @Override
      public String getText(Object object)
      {
        return ComposedTargetItemLabelProvider.getTargetPlatformText(object);
      }

      @Override
      public Object getImage(Object object)
      {
        return ComposedTargetItemLabelProvider.getTargetPlatformImage();
      }

      @Override
      public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
      {
        if (propertyDescriptors == null)
        {
          propertyDescriptors = Collections.<IItemPropertyDescriptor> singletonList(new WrapperItemPropertyDescriptor(resourceLocator, feature)
          {
            {
              itemDelegator = new ItemDelegator(adapterFactory, resourceLocator)
              {
                @Override
                public String getText(Object object)
                {
                  return ComposedTargetItemLabelProvider.getTargetPlatformText(object);
                }

                @Override
                public Object getImage(Object otherObject)
                {
                  return ComposedTargetItemLabelProvider.getTargetPlatformImage();
                }
              };

              staticImage = null;
              displayName = TargletEditPlugin.INSTANCE.getString("_UI_TargletContainer_composedTarget_label"); //$NON-NLS-1$
            }

            @Override
            public Collection<?> getChoiceOfValues(Object object)
            {
              Set<String> targetDefinitionNames = TargletContainerItemProvider.getTargetDefinitionNames(eObject, feature);
              if (object != null)
              {
                targetDefinitionNames.add(getEditableValue(object).toString());
              }
              return targetDefinitionNames;
            }
          });
        }

        return propertyDescriptors;
      }
    };
  }

  public static Command createCreateChildComposedTargetCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Object value, int index,
      Collection<?> collection, CreateChildCommand.Helper helper)
  {
    return new CreateChildCommand(domain, owner, feature, value, index, collection, helper)
    {
      @Override
      public String getText()
      {
        return ComposedTargetItemLabelProvider.getTargetPlatformText(value);
      }

      @Override
      public Object getImage()
      {
        return ComposedTargetItemLabelProvider.getTargetPlatformImage();
      }
    };
  }

  public static void collectNewChildComposedTargetDescriptors(Collection<Object> newChildDescriptors, Object object, EAttribute feature)
  {
    Collection<?> choices = getTargetDefinitionNames((EObject)object, feature);
    if (choices != null && !choices.isEmpty())
    {
      for (Object choice : choices)
      {
        newChildDescriptors
            .add(new GroupingChildCommandParameter(TargletEditPlugin.INSTANCE.getString("_UI_TargletContainer_composedTarget_label"), feature, choice)); //$NON-NLS-1$
      }
    }
    else
    {
      newChildDescriptors.add(new CommandParameter(null, feature, "")); //$NON-NLS-1$
    }
  }

  public static class ComposedTargetItemLabelProvider implements IItemLabelProvider
  {
    @Override
    public String getText(Object object)
    {
      return getTargetPlatformText(object);
    }

    public static String getTargetPlatformText(Object object)
    {
      if (object instanceof List<?>)
      {
        return ((List<?>)object).stream().map(ComposedTargetItemLabelProvider::getTargetPlatformText).collect(Collectors.joining("")); //$NON-NLS-1$
      }

      if (object instanceof String)
      {
        return getResolvedText(object.toString());
      }

      if (object instanceof IWrapperItemProvider)
      {
        return getTargetPlatformText(((IWrapperItemProvider)object).getValue());
      }

      return ""; //$NON-NLS-1$
    }

    private static String getResolvedText(String result)
    {
      Map<String, String> targetDefinitions = getTargetDefinitions();
      if (targetDefinitions != null)
      {
        String resolution = targetDefinitions.get(result);
        result += SEPARATOR + (resolution == null ? TargletEditPlugin.INSTANCE.getString("_UI_TargletContainer_unresolved_label") : URI.decode(resolution)); //$NON-NLS-1$
      }
      return result;
    }

    @Override
    public Object getImage(Object object)
    {
      return getTargetPlatformImage();
    }

    public static Object getTargetPlatformImage()
    {
      return TargletEditPlugin.INSTANCE.getImage("full/obj16/TargetPlatform"); //$NON-NLS-1$
    }
  }
}
