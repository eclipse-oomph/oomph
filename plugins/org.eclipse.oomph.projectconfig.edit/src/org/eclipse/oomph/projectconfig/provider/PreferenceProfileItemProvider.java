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
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.predicates.PredicatesFactory;
import org.eclipse.oomph.projectconfig.PreferenceProfile;
import org.eclipse.oomph.projectconfig.Project;
import org.eclipse.oomph.projectconfig.ProjectConfigFactory;
import org.eclipse.oomph.projectconfig.ProjectConfigPackage;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.EMFEditPlugin;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IViewerNotification;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.projectconfig.PreferenceProfile} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PreferenceProfileItemProvider extends ModelElementItemProvider
{
  private static final IWorkspaceRoot WORKSPACE_ROOT = ResourcesPlugin.getWorkspace().getRoot();

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PreferenceProfileItemProvider(AdapterFactory adapterFactory)
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

      addReferentProjectsPropertyDescriptor(object);
      addNamePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Referent Projects feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addReferentProjectsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceProfile_referentProjects_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceProfile_referentProjects_feature", "_UI_PreferenceProfile_type"),
        ProjectConfigPackage.Literals.PREFERENCE_PROFILE__REFERENT_PROJECTS, true, false, true, null, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        Collection<?> allProjects = super.getChoiceOfValues(object);

        PreferenceProfile preferenceProfile = (PreferenceProfile)object;
        EList<Predicate> predicates = preferenceProfile.getPredicates();
        if (predicates.isEmpty())
        {
          return allProjects;
        }

        Collection<Project> result = new ArrayList<Project>();
        for (Object value : allProjects)
        {
          Project project = (Project)value;
          String projectName = project.getPreferenceNode().getName();
          IProject iProject = WORKSPACE_ROOT.getProject(projectName);
          if (preferenceProfile.matches(iProject))
          {
            result.add(project);
          }
        }

        return result;
      }
    });
  }

  /**
   * This adds a property descriptor for the Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceProfile_name_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceProfile_name_feature", "_UI_PreferenceProfile_type"),
        ProjectConfigPackage.Literals.PREFERENCE_PROFILE__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
      childrenFeatures.add(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREFERENCE_FILTERS);
      childrenFeatures.add(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES);
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
    return hasChildren(object, false);
  }

  /**
   * This returns PreferenceProfile.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/PreferenceProfile"));
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
    String label = ((PreferenceProfile)object).getName();
    return label == null ? "" : label;
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

    switch (notification.getFeatureID(PreferenceProfile.class))
    {
      case ProjectConfigPackage.PREFERENCE_PROFILE__NAME:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case ProjectConfigPackage.PREFERENCE_PROFILE__PREDICATES:
      case ProjectConfigPackage.PREFERENCE_PROFILE__PREFERENCE_FILTERS:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
        if (referents != null)
        {
          referents.update();
        }
        return;
    }
    super.notifyChanged(notification);
  }

  private class Referents extends ItemProvider implements IEditingDomainItemProvider
  {
    PreferenceProfile preferenceProfile;

    public Referents(AdapterFactory adapterFactory, String text, Object image, PreferenceProfile preferenceProfile)
    {
      super(adapterFactory, text, image, preferenceProfile);
      this.preferenceProfile = preferenceProfile;
    }

    Map<Project, IWrapperItemProvider> wrappers = new HashMap<Project, IWrapperItemProvider>();

    public void update()
    {
      /*
       * for (Object child : getChildren()) { ((IDisposable)child).dispose(); }
       */
      List<Object> children = new ArrayList<Object>();
      EList<Project> referentProjects = preferenceProfile.getReferentProjects();
      for (int i = 0, size = referentProjects.size(); i < size; ++i)
      {
        Project project = referentProjects.get(i);
        IWrapperItemProvider wrapper = wrap(i, project);
        children.add(wrapper);
      }
      ECollections.setEList(getChildren(), children);
    }

    private IWrapperItemProvider wrap(int i, Project project)
    {
      IWrapperItemProvider wrapper = wrappers.get(project);
      if (wrapper == null)
      {
        wrapper = new DelegatingWrapperItemProvider(project, preferenceProfile, ProjectConfigPackage.Literals.PREFERENCE_PROFILE__REFERENT_PROJECTS, i,
            adapterFactory)
        {
          @Override
          public Object getParent(Object object)
          {
            return Referents.this;
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

          @Override
          public void notifyChanged(Notification notification)
          {
            if (notification instanceof IViewerNotification && ((IViewerNotification)notification).isLabelUpdate())
            {
              super.notifyChanged(notification);
            }
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
    public Command createCommand(Object object, EditingDomain editingDomain, Class<? extends Command> commandClass, CommandParameter commandParameter)
    {
      final Collection<?> originalCollection = commandParameter.getCollection();
      commandParameter = unwrapCommandValues(commandParameter, commandClass);
      Collection<?> collection = commandParameter.getCollection();
      if (commandClass == RemoveCommand.class)
      {
        if (preferenceProfile.getReferentProjects().containsAll(collection))
        {
          return new RemoveCommand(editingDomain, preferenceProfile, ProjectConfigPackage.Literals.PREFERENCE_PROFILE__REFERENT_PROJECTS, collection)
          {
            @Override
            public void doExecute()
            {
              super.doExecute();
              update();
              affectedObjects = Collections.singleton(Referents.this);
            }

            @Override
            public void doUndo()
            {
              super.doUndo();
              update();
              affectedObjects = Collections.singleton(Referents.this);
            }

            @Override
            public void doRedo()
            {
              super.doRedo();
              update();
              affectedObjects = Collections.singleton(Referents.this);
            }
          };
        }
      }
      else if (commandClass == AddCommand.class && collection != null)
      {
        final Collection<Object> wrappers = new ArrayList<Object>();
        for (Object value : collection)
        {
          if (value instanceof Project)
          {
            wrappers.add(wrap(-1, (Project)value));
          }
          else
          {
            return UnexecutableCommand.INSTANCE;
          }
        }
        return new AddCommand(editingDomain, preferenceProfile, ProjectConfigPackage.Literals.PREFERENCE_PROFILE__REFERENT_PROJECTS, collection)
        {
          @Override
          public void doExecute()
          {
            super.doExecute();
            update();
            affectedObjects = wrappers;
          }

          @Override
          public void doUndo()
          {
            super.doUndo();
            update();
            affectedObjects = Collections.singleton(Referents.this);
          }

          @Override
          public void doRedo()
          {
            super.doRedo();
            update();
            affectedObjects = wrappers;
          }
        };
      }
      else if (commandClass == DragAndDropCommand.class)
      {
        DragAndDropCommand.Detail detail = (DragAndDropCommand.Detail)commandParameter.getFeature();
        return new DragAndDropCommand(editingDomain, preferenceProfile, detail.location, detail.operations, detail.operation, commandParameter.getCollection())
        {
          @Override
          protected boolean prepareDropLinkOn()
          {
            dragCommand = IdentityCommand.INSTANCE;
            dropCommand = AddCommand.create(domain, Referents.this, ProjectConfigPackage.Literals.PREFERENCE_PROFILE__REFERENT_PROJECTS, originalCollection);
            return dropCommand.canExecute();
          }
        };
      }
      return super.createCommand(object, editingDomain, commandClass, commandParameter);
    }
  }

  @Override
  public Command createCommand(Object object, EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter)
  {
    Collection<?> collection = commandParameter.getCollection();
    if (collection != null)
    {
      for (Object value : collection)
      {
        if (value instanceof Project)
        {
          return referents.createCommand(referents, domain, commandClass, commandParameter);
        }
      }
    }
    return super.createCommand(object, domain, commandClass, commandParameter);
  }

  private Referents referents;

  @Override
  public Collection<?> getChildren(Object object)
  {
    Collection<Object> result = new ArrayList<Object>(super.getChildren(object));
    PreferenceProfile preferenceProfile = (PreferenceProfile)object;
    if (referents == null)
    {
      referents = new Referents(adapterFactory, "Referents", getResourceLocator().getImage("full/obj16/IncomingLinks"), preferenceProfile);
    }
    referents.update();
    result.add(referents);
    return result;
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

    newChildDescriptors.add(
        createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREFERENCE_FILTERS, ProjectConfigFactory.eINSTANCE.createPreferenceFilter()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, ProjectConfigFactory.eINSTANCE.createInclusionPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, ProjectConfigFactory.eINSTANCE.createExclusionPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, PredicatesFactory.eINSTANCE.createNamePredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, PredicatesFactory.eINSTANCE.createCommentPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, PredicatesFactory.eINSTANCE.createLocationPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, PredicatesFactory.eINSTANCE.createRepositoryPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, PredicatesFactory.eINSTANCE.createAndPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, PredicatesFactory.eINSTANCE.createOrPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, PredicatesFactory.eINSTANCE.createNotPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, PredicatesFactory.eINSTANCE.createNaturePredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, PredicatesFactory.eINSTANCE.createBuilderPredicate()));

    newChildDescriptors
        .add(createChildParameter(ProjectConfigPackage.Literals.PREFERENCE_PROFILE__PREDICATES, PredicatesFactory.eINSTANCE.createFilePredicate()));
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
