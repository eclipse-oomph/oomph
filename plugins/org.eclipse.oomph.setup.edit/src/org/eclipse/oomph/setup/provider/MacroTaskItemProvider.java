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

import org.eclipse.oomph.setup.Argument;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.Parameter;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.MacroTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MacroTaskItemProvider extends SetupTaskItemProvider
{
  private static final Method EXPAND_MACRO_METHOD;

  static
  {
    Method expandMacroTaskMethod = null;
    try
    {
      Class<?> setupTaskPerformer = CommonPlugin.loadClass("org.eclipse.oomph.setup.core", "org.eclipse.oomph.setup.internal.core.SetupTaskPerformer"); //$NON-NLS-1$ //$NON-NLS-2$
      expandMacroTaskMethod = setupTaskPerformer.getMethod("expand", MacroTask.class); //$NON-NLS-1$
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    EXPAND_MACRO_METHOD = expandMacroTaskMethod;
  }

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MacroTaskItemProvider(AdapterFactory adapterFactory)
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

      addMacroPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Macro feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addMacroPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_MacroTask_macro_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_MacroTask_macro_feature", "_UI_MacroTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        SetupPackage.Literals.MACRO_TASK__MACRO, true, false, true, null, null, null));
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
      childrenFeatures.add(SetupPackage.Literals.MACRO_TASK__ARGUMENTS);
      childrenFeatures.add(SetupPackage.Literals.MACRO_TASK__MACRO);
    }
    return childrenFeatures;
  }

  @Override
  protected boolean isWrappingNeeded(Object object)
  {
    return true;
  }

  @Override
  public boolean hasChildren(Object object)
  {
    return !isDirectlyRecursive(object) && super.hasChildren(object);
  }

  protected boolean isDirectlyRecursive(Object object)
  {
    MacroTask macroTask = (MacroTask)object;
    EObject rootContainer = EcoreUtil.getRootContainer(macroTask);
    return rootContainer == macroTask.getMacro();
  }

  @Override
  public Collection<?> getChildren(Object object)
  {
    if (isDirectlyRecursive(object))
    {
      return Collections.emptyList();
    }

    Collection<?> children = super.getChildren(object);

    CompoundTask expandedMacroTask = expandMacroTask(object);
    if (expandedMacroTask != null)
    {
      List<Object> extendedChildren = new ArrayList<>(children);
      String name = expandedMacroTask.getName();
      expandedMacroTask
          .setName(StringUtil.isEmpty(name) ? getString("_UI_MacroPreview_label") : getString("_UI_MacroPrefixedPreview_label", new Object[] { name })); //$NON-NLS-1$ //$NON-NLS-2$

      extendedChildren.add(new PreviewDelegatingWrapper(expandedMacroTask, object, adapterFactory));
      children = extendedChildren;
    }

    return children;
  }

  protected CompoundTask expandMacroTask(Object object)
  {
    if (EXPAND_MACRO_METHOD != null)
    {
      CompoundTask expandedMacro = ReflectUtil.invokeMethod(EXPAND_MACRO_METHOD, null, object);
      if (expandedMacro != null)
      {
        EObject eContainer = expandedMacro.eContainer();
        if (eContainer instanceof Macro)
        {
          Macro macro = (Macro)eContainer;
          macro.setLogicalContainer((MacroTask)object);
        }
      }

      return expandedMacro;
    }

    return null;
  }

  @Override
  protected Object createWrapper(EObject object, EStructuralFeature feature, Object value, int index)
  {
    if (feature == SetupPackage.Literals.MACRO_TASK__MACRO)
    {
      return new DelegatingWrapperItemProvider(value, object, feature, index, adapterFactory)
      {
        @Override
        public Object getImage(Object object)
        {
          Object image = super.getImage(object);
          List<Object> images = new ArrayList<>(2);
          images.add(image);
          images.add(SetupEditPlugin.INSTANCE.getImage("MacroOverlay.png")); //$NON-NLS-1$
          images.add(SetupEditPlugin.INSTANCE.getImage("LinkOverlay")); //$NON-NLS-1$
          return new ComposedImage(images);
        }

        @Override
        public Object getFont(Object object)
        {
          return ITALIC_FONT;
        }

        @Override
        public Object getForeground(Object object)
        {
          Object foreground = super.getForeground(object);
          return foreground == null ? RecursionSafeDelegatingWrapper.FOREGROUND_COLOR : foreground;
        }

        @Override
        protected IWrapperItemProvider createWrapper(Object value, Object owner, AdapterFactory adapterFactory)
        {
          return new RecursionSafeDelegatingWrapper(value, owner, adapterFactory);
        }
      };
    }

    return super.createWrapper(object, feature, value, index);
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
   * This returns MacroTask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private Object getImageGen(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/MacroTask")); //$NON-NLS-1$
  }

  @Override
  public Object getImage(Object object)
  {
    Object image = getImageGen(object);
    return getRecursiveOverlayImage(isDirectlyRecursive(object), false, image);
  }

  protected static Object getRecursiveOverlayImage(boolean isRecursive, boolean isMacro, Object image)
  {
    List<Object> images = new ArrayList<>(2);
    images.add(image);
    if (isRecursive)
    {
      images.add(SetupEditPlugin.INSTANCE.getImage("DeletedOverlay")); //$NON-NLS-1$
    }

    if (isMacro)
    {
      images.add(SetupEditPlugin.INSTANCE.getImage("MacroOverlay.png")); //$NON-NLS-1$
    }

    return images.size() == 1 ? image : new ComposedImage(images);
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
  @SuppressWarnings("nls")
  public String getText(Object object)
  {
    MacroTask macroTask = (MacroTask)object;
    String id = macroTask.getID();
    Macro macro = macroTask.getMacro();
    String label = macro != null ? macro.getLabel() : null;
    return (StringUtil.isEmpty(label) ? getString("_UI_MacroTask_type") : label) + " : " + (StringUtil.isEmpty(id) ? "?" : id);
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private void notifyChangedGen(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(MacroTask.class))
    {
      case SetupPackage.MACRO_TASK__ARGUMENTS:
      case SetupPackage.MACRO_TASK__MACRO:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
        return;
    }
    super.notifyChanged(notification);
  }

  @Override
  public void notifyChanged(Notification notification)
  {
    notifyChangedGen(notification);
    switch (notification.getFeatureID(MacroTask.class))
    {
      case SetupPackage.MACRO_TASK__ID:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, true));
        return;
    }
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unused")
  private void collectNewChildDescriptorsGen(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.MACRO_TASK__ARGUMENTS, SetupFactory.eINSTANCE.createArgument()));
  }

  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    MacroTask macroTask = (MacroTask)object;
    Macro macro = macroTask.getMacro();
    if (macro != null)
    {
      Set<Parameter> boundParameters = new HashSet<>();
      for (Argument argument : macroTask.getArguments())
      {
        boundParameters.add(argument.getParameter());
      }

      for (Parameter parameter : macro.getParameters())
      {
        if (!boundParameters.contains(parameter))
        {
          Argument argument = SetupFactory.eINSTANCE.createArgument();
          argument.setParameter(parameter);
          String defaultValue = parameter.getDefaultValue();
          if (defaultValue == null)
          {
            argument.setValue("" + parameter.getName() + "_value"); //$NON-NLS-1$ //$NON-NLS-2$
          }
          newChildDescriptors.add(createChildParameter(SetupPackage.Literals.MACRO_TASK__ARGUMENTS, argument));
        }
      }
    }
  }

  @Override
  public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection)
  {
    String result = super.getCreateChildText(owner, feature, child, selection);
    if (feature == SetupPackage.Literals.MACRO_TASK__ARGUMENTS)
    {
      Argument argument = (Argument)child;
      Parameter parameter = argument.getParameter();
      if (parameter != null)
      {
        String name = parameter.getName();
        if (!StringUtil.isEmpty(name))
        {
          return result += ' ' + getString("_UI_MacroArgumentSuffix_label", new Object[] { name }); //$NON-NLS-1$
        }
      }
    }

    return result;
  }

  private static class RecursionSafeDelegatingWrapper extends DelegatingWrapperItemProvider
  {
    private static final URI FOREGROUND_COLOR = URI.createURI("color://rgb/138/110/85"); //$NON-NLS-1$

    @SuppressWarnings("deprecation")
    public RecursionSafeDelegatingWrapper(Object value, Object owner, AdapterFactory adapterFactory)
    {
      super(value, owner, adapterFactory);
    }

    @Override
    protected IWrapperItemProvider createWrapper(Object value, Object owner, AdapterFactory adapterFactory)
    {
      return new RecursionSafeDelegatingWrapper(value, owner, adapterFactory);
    }

    @Override
    public Object getFont(Object object)
    {
      return ITALIC_FONT;
    }

    @Override
    public Object getForeground(Object object)
    {
      return FOREGROUND_COLOR;
    }

    @Override
    public Object getImage(Object object)
    {
      Object image = super.getImage(object);
      return getRecursiveOverlayImage(isRecursive(), true, image);
    }

    public boolean isRecursive()
    {
      Object unwrappedValue = AdapterFactoryEditingDomain.unwrap(value);
      boolean isRecursive = isRecursive(owner, unwrappedValue);
      if (!isRecursive && unwrappedValue instanceof EObject)
      {
        EObject eContainer = ((EObject)unwrappedValue).eContainer();
        if (eContainer instanceof Macro)
        {
          Macro macro = (Macro)eContainer;
          isRecursive = isRecursive(owner, macro.getLogicalContainer());
        }
      }

      return isRecursive;
    }

    protected boolean isRecursive(Object parent, Object guard)
    {
      Object unwrap = AdapterFactoryEditingDomain.unwrap(parent);
      if (unwrap == guard)
      {
        return true;
      }

      if (parent instanceof ITreeItemContentProvider)
      {
        Object grandParent = ((ITreeItemContentProvider)parent).getParent(parent);
        return isRecursive(grandParent, guard);
      }

      return false;
    }

    @Override
    public boolean hasChildren(Object object)
    {
      return !isRecursive() && super.hasChildren(object);
    }

    @Override
    protected void updateChildren()
    {
      if (!isRecursive())
      {
        super.updateChildren();
      }
      else
      {
        delegateChildren = Collections.emptyList();
      }
    }

    @Override
    public Collection<?> getChildren(Object object)
    {
      Collection<?> children = super.getChildren(object);
      for (Iterator<?> it = children.iterator(); it.hasNext();)
      {
        Object child = it.next();
        Object value = ((RecursionSafeDelegatingWrapper)child).getValue();
        if (value instanceof PreviewDelegatingWrapper)
        {
          it.remove();
          break;
        }
      }
      return children;
    }
  }

  private static class PreviewDelegatingWrapper extends DelegatingWrapperItemProvider
  {
    private static final URI FOREGROUND_COLOR = URI.createURI("color://rgb/85/113/138"); //$NON-NLS-1$

    @SuppressWarnings("deprecation")
    public PreviewDelegatingWrapper(Object value, Object owner, AdapterFactory adapterFactory)
    {
      super(value, owner, adapterFactory);
    }

    @Override
    public Object getFont(Object object)
    {
      return ITALIC_FONT;
    }

    @Override
    public Object getForeground(Object object)
    {
      return FOREGROUND_COLOR;
    }

    @Override
    public Object getImage(Object object)
    {
      Object image = super.getImage(object);
      List<Object> images = new ArrayList<>(2);
      images.add(image);
      images.add(SetupEditPlugin.INSTANCE.getImage("PreviewOverlay.png")); //$NON-NLS-1$
      return new ComposedImage(images);
    }

    @Override
    protected IWrapperItemProvider createWrapper(Object value, Object owner, AdapterFactory adapterFactory)
    {
      return new PreviewDelegatingWrapper(value, owner, adapterFactory);
    }
  }
}
