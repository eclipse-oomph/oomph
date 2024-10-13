/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.provider;

import org.eclipse.oomph.maven.MavenFactory;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Project;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemStyledLabelProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.StyledString;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.maven.Project} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ProjectItemProvider extends CoordinateItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectItemProvider(AdapterFactory adapterFactory)
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

      addLocationPropertyDescriptor(object);
      addIncomingParentReferencesPropertyDescriptor(object);
      addIncomingDependencyReferencesPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
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
        getString("_UI_Project_location_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Project_location_feature", "_UI_Project_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.PROJECT__LOCATION, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Incoming Parent References feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIncomingParentReferencesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Project_incomingParentReferences_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Project_incomingParentReferences_feature", "_UI_Project_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.PROJECT__INCOMING_PARENT_REFERENCES, false, false, true, null, null, null));
  }

  /**
   * This adds a property descriptor for the Incoming Dependency References feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIncomingDependencyReferencesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Project_incomingDependencyReferences_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Project_incomingDependencyReferences_feature", "_UI_Project_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.PROJECT__INCOMING_DEPENDENCY_REFERENCES, false, false, true, null, null, null));
  }

  /**
   * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
   * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
   * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Collection<? extends EStructuralFeature> getChildrenFeaturesGen(Object object)
  {
    if (childrenFeatures == null)
    {
      super.getChildrenFeatures(object);
      childrenFeatures.add(MavenPackage.Literals.PROJECT__PARENT);
      childrenFeatures.add(MavenPackage.Literals.PROJECT__DEPENDENCIES);
      childrenFeatures.add(MavenPackage.Literals.PROJECT__MANAGED_DEPENDENCIES);
      childrenFeatures.add(MavenPackage.Literals.PROJECT__PROPERTIES);
    }
    return childrenFeatures;
  }

  protected Collection<? extends EStructuralFeature> getActualChildrenFeatures(Object object)
  {
    List<EStructuralFeature> actualChildrenFeatures = new ArrayList<>(getChildrenFeaturesGen(object));
    actualChildrenFeatures.remove(MavenPackage.Literals.PROJECT__DEPENDENCIES);
    actualChildrenFeatures.remove(MavenPackage.Literals.PROJECT__MANAGED_DEPENDENCIES);
    actualChildrenFeatures.remove(MavenPackage.Literals.PROJECT__PROPERTIES);
    return actualChildrenFeatures;
  }

  @Override
  public final Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
  {
    return getActualChildrenFeatures(object);
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

  public Collection<?> basicGetChildren(Object object)
  {
    return super.getChildren(object);
  }

  @Override
  public Collection<?> getChildren(Object object)
  {
    Collection<Object> children = new ArrayList<>(basicGetChildren(object));
    Project project = (Project)object;
    if (!project.getDependencies().isEmpty())
    {
      children.add(new GroupedItemProvider(getAdapterFactory(), project, MavenPackage.Literals.PROJECT__DEPENDENCIES));
    }

    if (!project.getManagedDependencies().isEmpty())
    {
      children.add(new GroupedItemProvider(getAdapterFactory(), project, MavenPackage.Literals.PROJECT__MANAGED_DEPENDENCIES));
    }

    if (!project.getProperties().isEmpty())
    {
      children.add(new GroupedItemProvider(getAdapterFactory(), project, MavenPackage.Literals.PROJECT__PROPERTIES));
    }

    if (!project.getIncomingParentReferences().isEmpty())
    {
      children.add(new GroupedItemProvider(getAdapterFactory(), project, MavenPackage.Literals.PROJECT__INCOMING_PARENT_REFERENCES));
    }

    if (!project.getIncomingDependencyReferences().isEmpty())
    {
      children.add(new GroupedItemProvider(getAdapterFactory(), project, MavenPackage.Literals.PROJECT__INCOMING_DEPENDENCY_REFERENCES));
    }

    return children;
  }

  /**
   * This returns Project.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/Project")); //$NON-NLS-1$
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
    return getStyledText(object).getString();
  }

  /**
   * This returns the label styled text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public StyledString getStyledText(Object object)
  {
    StyledString styledLabel = super.getStyledText(object);

    Project project = (Project)object;
    String location = project.getLocation();
    styledLabel.append(" - ", StyledString.Style.QUALIFIER_STYLER); //$NON-NLS-1$
    styledLabel.append(location, StyledString.Style.DECORATIONS_STYLER);

    return styledLabel;
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

    switch (notification.getFeatureID(Project.class))
    {
      case MavenPackage.PROJECT__LOCATION:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case MavenPackage.PROJECT__PARENT:
      case MavenPackage.PROJECT__DEPENDENCIES:
      case MavenPackage.PROJECT__MANAGED_DEPENDENCIES:
      case MavenPackage.PROJECT__PROPERTIES:
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

    newChildDescriptors.add(createChildParameter(MavenPackage.Literals.PROJECT__PARENT, MavenFactory.eINSTANCE.createParent()));

    newChildDescriptors.add(createChildParameter(MavenPackage.Literals.PROJECT__DEPENDENCIES, MavenFactory.eINSTANCE.createDependency()));

    newChildDescriptors.add(createChildParameter(MavenPackage.Literals.PROJECT__MANAGED_DEPENDENCIES, MavenFactory.eINSTANCE.createDependency()));

    newChildDescriptors.add(createChildParameter(MavenPackage.Literals.PROJECT__PROPERTIES, MavenFactory.eINSTANCE.createProperty()));
  }

  /**
   * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection)
  {
    Object childFeature = feature;
    Object childObject = child;

    boolean qualify = childFeature == MavenPackage.Literals.PROJECT__DEPENDENCIES || childFeature == MavenPackage.Literals.PROJECT__MANAGED_DEPENDENCIES;

    if (qualify)
    {
      return getString("_UI_CreateChild_text2", //$NON-NLS-1$
          new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
    }
    return super.getCreateChildText(owner, feature, child, selection);
  }

  private static class GroupedItemProvider extends ProjectItemProvider implements ReferenceGroup
  {
    private final Project project;

    private final EReference eReference;

    private GroupedItemProvider(AdapterFactory adapterFactory, Project project, EReference eStructuralFeature)
    {
      super(adapterFactory);
      this.project = project;
      eReference = eStructuralFeature;
    }

    @Override
    public EReference getFeature()
    {
      return eReference;
    }

    @Override
    protected Collection<? extends EStructuralFeature> getActualChildrenFeatures(Object object)
    {
      return List.of(eReference);
    }

    @Override
    public Collection<?> getChildren(Object object)
    {
      return basicGetChildren(project);
    }

    @Override
    protected Object createWrapper(EObject object, EStructuralFeature feature, Object value, int index)
    {
      if (!eReference.isContainment())
      {
        return new Wrapper(value, object, feature, index, adapterFactory);
      }

      return value;
    }

    @Override
    public Object getImage(Object object)
    {
      return super.getImage(object);
    }

    @Override
    public StyledString getStyledText(Object object)
    {
      StyledString styledLabel = new StyledString();
      styledLabel.append(getFeatureText(eReference), StyledString.Style.DECORATIONS_STYLER);
      return styledLabel;
    }

    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
    {
      return List.of();
    }

    @Override
    public IItemPropertyDescriptor getPropertyDescriptor(Object object, Object propertyId)
    {
      return null;
    }

    @Override
    public Object getParent(Object object)
    {
      return project;
    }

    @Override
    public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain, Object sibling)
    {
      return List.of();
    }

    @Override
    public Command createCommand(Object object, EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter)
    {
      return UnexecutableCommand.INSTANCE;
    }

    @Override
    public int hashCode()
    {
      return Objects.hash(project, eReference);
    }

    @Override
    public boolean equals(Object other)
    {
      // Needed so the tree view can find this object which is newly created for each call to getChildren.
      if (other instanceof GroupedItemProvider groupedItemProvider)
      {
        if (groupedItemProvider.eReference == eReference && groupedItemProvider.project == project)
        {
          return true;
        }
      }
      return false;
    }
  }

  private static class Wrapper extends DelegatingWrapperItemProvider implements IItemStyledLabelProvider
  {
    public Wrapper(Object value, Object owner, EStructuralFeature feature, int index, AdapterFactory adapterFactory)
    {
      super(value, owner, feature, index, adapterFactory);
    }

    @Override
    public Object getStyledText(Object object)
    {
      EObject delegateValue = (EObject)getDelegateValue();
      EObject eContainer = delegateValue.eContainer();
      IItemStyledLabelProvider itemStyledLabelProvider = (IItemStyledLabelProvider)getRootAdapterFactory().adapt(eContainer, IItemStyledLabelProvider.class);
      return itemStyledLabelProvider.getStyledText(eContainer);
    }

    @Override
    public boolean hasChildren(Object object)
    {
      return false;
    }

    @Override
    public Collection<?> getChildren(Object object)
    {
      return List.of();
    }
  }

}
