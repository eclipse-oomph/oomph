/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.targlets.provider;

import org.eclipse.oomph.p2.Configuration;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;
import org.eclipse.oomph.setup.targlets.SetupTargletsFactory;
import org.eclipse.oomph.setup.targlets.SetupTargletsPackage;
import org.eclipse.oomph.setup.targlets.TargletTask;
import org.eclipse.oomph.targlets.IUGenerator;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.core.WorkspaceIUInfo;
import org.eclipse.oomph.targlets.internal.core.WorkspaceIUAnalyzer;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.pde.TargetPlatformRunnable;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.query.CollectionResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetPlatformService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.targlets.TargletTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TargletTaskItemProvider extends SetupTaskItemProvider
{
  private static final Pattern VERSION_PATTERN = Pattern.compile("([1-9]+\\.[0-9]+)");

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletTaskItemProvider(AdapterFactory adapterFactory)
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

      addTargletURIsPropertyDescriptor(object);
      addOperatingSystemPropertyDescriptor(object);
      addWindowingSystemPropertyDescriptor(object);
      addArchitecturePropertyDescriptor(object);
      addLocalePropertyDescriptor(object);
      addProgramArgumentsPropertyDescriptor(object);
      addVMArgumentsPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Targlet UR Is feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTargletURIsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_targletURIs_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_targletURIs_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__TARGLET_UR_IS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Operating System feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addOperatingSystemPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_operatingSystem_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_operatingSystem_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__OPERATING_SYSTEM, true, false, true, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        return Configuration.Choices.forOS();
      }
    });
  }

  /**
   * This adds a property descriptor for the Windowing System feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addWindowingSystemPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_windowingSystem_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_windowingSystem_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__WINDOWING_SYSTEM, true, false, true, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        return Configuration.Choices.forWS();
      }
    });
  }

  /**
   * This adds a property descriptor for the Architecture feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addArchitecturePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_architecture_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_architecture_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__ARCHITECTURE, true, false, true, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        return Configuration.Choices.forArch();
      }
    });
  }

  /**
   * This adds a property descriptor for the Locale feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addLocalePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_locale_feature"), getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_locale_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__LOCALE, true, false, true, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      private static final String NL_EXTRA = "org.eclipse.pde.nl.extra";

      private LocaleItemLabelProvider labelProvider;

      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        LocaleItemLabelProvider labelProvider = getLabelProvider(object);
        Set<String> locales = labelProvider.getLocaleMap().keySet();
        return getChoices(locales.toArray(new String[locales.size()]), NL_EXTRA);
      }

      @Override
      public LocaleItemLabelProvider getLabelProvider(Object object)
      {
        if (labelProvider == null)
        {
          labelProvider = new LocaleItemLabelProvider(itemDelegator);
        }

        return labelProvider;
      }
    });
  }

  /**
   * This adds a property descriptor for the Program Arguments feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addProgramArgumentsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_programArguments_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_programArguments_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__PROGRAM_ARGUMENTS, true, true, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the VM Arguments feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addVMArgumentsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_TargletTask_vMArguments_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_TargletTask_vMArguments_feature", "_UI_TargletTask_type"),
        SetupTargletsPackage.Literals.TARGLET_TASK__VM_ARGUMENTS, true, true, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  private static Set<String> getChoices(String[] values, String extraValuesPreference)
  {
    Set<String> result = new HashSet<String>();
    result.addAll(Arrays.asList(values));

    IEclipsePreferences node = InstanceScope.INSTANCE.getNode("org.eclipse.pde.core");
    String extraValues = node.get(extraValuesPreference, null);
    if (!StringUtil.isEmpty(extraValues))
    {
      StringTokenizer tokenizer = new StringTokenizer(extraValues, ",");
      while (tokenizer.hasMoreTokens())
      {
        String extraValue = tokenizer.nextToken().trim();
        result.add(extraValue);
      }
    }

    return result;
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
      childrenFeatures.add(SetupTargletsPackage.Literals.TARGLET_TASK__TARGLETS);
      childrenFeatures.add(SetupTargletsPackage.Literals.TARGLET_TASK__IMPLICIT_DEPENDENCIES);
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
   * This returns TargletTask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/TargletTask"));
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
    TargletTask targletTask = (TargletTask)object;

    StringBuilder builder = new StringBuilder();
    Set<String> added = new HashSet<String>();

    for (Targlet targlet : targletTask.getTarglets())
    {
      String name = targlet.getName();
      if (added.add(name))
      {
        if (builder.length() != 0)
        {
          builder.append(" + ");
        }

        builder.append(name);
      }
    }

    String label = getString("_UI_TargletTask_type");
    if (builder.length() != 0)
    {
      label += " (" + builder + ")";
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

    switch (notification.getFeatureID(TargletTask.class))
    {
      case SetupTargletsPackage.TARGLET_TASK__TARGLET_UR_IS:
      case SetupTargletsPackage.TARGLET_TASK__OPERATING_SYSTEM:
      case SetupTargletsPackage.TARGLET_TASK__WINDOWING_SYSTEM:
      case SetupTargletsPackage.TARGLET_TASK__ARCHITECTURE:
      case SetupTargletsPackage.TARGLET_TASK__LOCALE:
      case SetupTargletsPackage.TARGLET_TASK__PROGRAM_ARGUMENTS:
      case SetupTargletsPackage.TARGLET_TASK__VM_ARGUMENTS:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case SetupTargletsPackage.TARGLET_TASK__TARGLETS:
      case SetupTargletsPackage.TARGLET_TASK__IMPLICIT_DEPENDENCIES:
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

    newChildDescriptors.add(createChildParameter(SetupTargletsPackage.Literals.TARGLET_TASK__TARGLETS, TargletFactory.eINSTANCE.createTarglet()));

    newChildDescriptors.add(
        createChildParameter(SetupTargletsPackage.Literals.TARGLET_TASK__IMPLICIT_DEPENDENCIES, SetupTargletsFactory.eINSTANCE.createImplicitDependency()));
  }

  @Override
  public Command createCommand(Object object, final EditingDomain domain, Class<? extends Command> commandClass, final CommandParameter commandParameter)
  {
    if (commandClass == DragAndDropCommand.class)
    {
      Collection<?> collection = commandParameter.getCollection();
      if (collection.size() == 1)
      {
        Object value = collection.iterator().next();
        if (value instanceof URI)
        {
          final URI uri = (URI)value;
          if ("target".equals(uri.fileExtension()) && uri.isPlatformResource())
          {
            final Path path = new Path(uri.toPlatformString(true));
            try
            {
              Command result = TargetPlatformUtil.runWithTargetPlatformService(new TargetPlatformRunnable<Command>()
              {
                @SuppressWarnings("restriction")
                public Command run(ITargetPlatformService service) throws CoreException
                {
                  for (ITargetHandle targetHandle : service.getTargets(new NullProgressMonitor()))
                  {
                    if (targetHandle instanceof org.eclipse.pde.internal.core.target.WorkspaceFileTargetHandle)
                    {
                      org.eclipse.pde.internal.core.target.WorkspaceFileTargetHandle workspaceFileTargetHandle = (org.eclipse.pde.internal.core.target.WorkspaceFileTargetHandle)targetHandle;
                      IFile targetFile = workspaceFileTargetHandle.getTargetFile();
                      if (path.equals(targetFile.getFullPath()))
                      {
                        final Set<Requirement> requirements = new TreeSet<Requirement>(new Comparator<Requirement>()
                        {
                          public int compare(Requirement requirement1, Requirement requirement2)
                          {
                            String name1 = requirement1.getName();
                            String name2 = requirement2.getName();
                            return name1.compareTo(name2);
                          }
                        });
                        final Set<Repository> repos = new TreeSet<Repository>(new Comparator<Repository>()
                        {
                          public int compare(Repository repository1, Repository repository2)
                          {
                            String url1 = repository1.getURL();
                            String url2 = repository2.getURL();
                            return url1.compareTo(url2);
                          }
                        });

                        ITargetDefinition targetDefinition = targetHandle.getTargetDefinition();
                        final String arch = targetDefinition.getArch();
                        final String os = targetDefinition.getOS();
                        final String ws = targetDefinition.getWS();
                        final String nl = targetDefinition.getNL();
                        final String programArguments = targetDefinition.getProgramArguments();
                        final String vmArguments = targetDefinition.getVMArguments();

                        for (ITargetLocation targetLocation : targetDefinition.getTargetLocations())
                        {
                          if (targetLocation instanceof org.eclipse.pde.internal.core.target.IUBundleContainer)
                          {
                            org.eclipse.pde.internal.core.target.IUBundleContainer iuBundleContainer = (org.eclipse.pde.internal.core.target.IUBundleContainer)targetLocation;

                            java.net.URI[] repositories = iuBundleContainer.getRepositories();
                            for (java.net.URI repo : repositories)
                            {
                              URI repoURI = URI.createURI(repo.toString());
                              if (repoURI.hasTrailingPathSeparator())
                              {
                                repoURI = repoURI.trimSegments(1);
                              }

                              repos.add(P2Factory.eINSTANCE.createRepository(repoURI.toString()));
                            }

                            String[] ids = (String[])ReflectUtil.invokeMethod("getIds", iuBundleContainer);
                            Version[] versions = (Version[])ReflectUtil.invokeMethod("getVersions", iuBundleContainer);

                            for (int i = 0, length = ids.length; i < length; ++i)
                            {
                              requirements.add(P2Factory.eINSTANCE.createRequirement(ids[i], versions[i]));
                            }
                          }
                        }

                        DragAndDropCommand.Detail detail = (DragAndDropCommand.Detail)commandParameter.getFeature();
                        return new TargletDragAndDropCommand(domain, commandParameter.getOwner(), detail.location, detail.operations, detail.operation,
                            commandParameter.getCollection())
                        {
                          @Override
                          protected boolean prepareDropCopyOn()
                          {
                            dragCommand = IdentityCommand.INSTANCE;

                            CompoundCommand compoundCommand = new CompoundCommand(0);
                            dropCommand = compoundCommand;

                            Targlet targlet = TargletFactory.eINSTANCE.createTarglet();
                            targlet.setName(getTargletName(StringUtil.cap(uri.trimFileExtension().lastSegment())) + "Target Platform");

                            targlet.setActiveRepositoryListName("${eclipse.target.platform}");
                            targlet.getRequirements().addAll(requirements);
                            EList<RepositoryList> repositoryLists = targlet.getRepositoryLists();
                            RepositoryList repositoryList = P2Factory.eINSTANCE.createRepositoryList();
                            repositoryList.setName(getRepositoryListName(StringUtil.cap(uri.trimFileExtension().lastSegment())));
                            repositoryLists.add(repositoryList);
                            repositoryList.getRepositories().addAll(repos);

                            TargletTask targletTask = (TargletTask)owner;

                            compoundCommand.append(new AddCommand(domain, targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__TARGLETS, targlet));

                            if (StringUtil.isEmpty(targletTask.getArchitecture()) && !StringUtil.isEmpty(arch))
                            {
                              compoundCommand.append(new SetCommand(domain, targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__ARCHITECTURE, arch));
                            }

                            if (StringUtil.isEmpty(targletTask.getOperatingSystem()) && !StringUtil.isEmpty(os))
                            {
                              compoundCommand.append(new SetCommand(domain, targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__OPERATING_SYSTEM, os));
                            }

                            if (StringUtil.isEmpty(targletTask.getWindowingSystem()) && !StringUtil.isEmpty(ws))
                            {
                              compoundCommand.append(new SetCommand(domain, targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__WINDOWING_SYSTEM, ws));
                            }

                            if (StringUtil.isEmpty(targletTask.getLocale()) && !StringUtil.isEmpty(nl))
                            {
                              compoundCommand.append(new SetCommand(domain, targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__LOCALE, nl));
                            }

                            if (!StringUtil.isEmpty(vmArguments))
                            {
                              String newVmArguments = targletTask.getVMArguments();
                              if (StringUtil.isEmpty(newVmArguments))
                              {
                                newVmArguments = "";
                              }
                              else
                              {
                                newVmArguments += "\n";
                              }

                              newVmArguments += vmArguments;

                              compoundCommand.append(new SetCommand(domain, targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__VM_ARGUMENTS,
                                  newVmArguments.replaceAll("[\n\r]+", "\n").trim()));
                            }

                            if (!StringUtil.isEmpty(programArguments))
                            {
                              String newProgramArguments = targletTask.getProgramArguments();
                              if (StringUtil.isEmpty(newProgramArguments))
                              {
                                newProgramArguments = "";
                              }
                              else
                              {
                                newProgramArguments += "\n";
                              }

                              newProgramArguments += programArguments;

                              compoundCommand.append(new SetCommand(domain, targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__PROGRAM_ARGUMENTS,
                                  newProgramArguments.replaceAll("[\n\r]+", "\n").trim()));
                            }

                            return dropCommand.canExecute();
                          }

                          private String getRepositoryListName(String defaultName)
                          {
                            String lowerCaseDefaultName = defaultName.toLowerCase();

                            TargletTask targletTask = (TargletTask)owner;
                            for (Scope scope = targletTask.getScope(); scope != null; scope = scope.getParentScope())
                            {
                              if (scope instanceof ProjectCatalog)
                              {
                                for (Iterator<EObject> it = EcoreUtil.getAllProperContents(scope, false); it.hasNext();)
                                {
                                  EObject child = it.next();
                                  if (child instanceof VariableTask)
                                  {
                                    VariableTask variableTask = (VariableTask)child;
                                    if ("eclipse.target.platform".equals(variableTask.getName()))
                                    {
                                      String candidate = null;
                                      for (VariableChoice variableChoice : variableTask.getChoices())
                                      {
                                        String value = variableChoice.getValue();
                                        if (lowerCaseDefaultName.contains(value.toLowerCase()))
                                        {
                                          return value;
                                        }

                                        String label = variableChoice.getLabel();
                                        if (label != null)
                                        {
                                          Matcher matcher = VERSION_PATTERN.matcher(label);
                                          if (matcher.find() && defaultName.indexOf(matcher.group(1)) != -1)
                                          {
                                            return value;
                                          }
                                        }

                                        if (candidate == null)
                                        {
                                          candidate = value;
                                        }
                                      }

                                      return candidate;
                                    }
                                  }
                                }
                                break;
                              }
                            }

                            return defaultName;
                          }
                        };
                      }
                    }
                  }

                  return null;
                }
              });

              if (result != null)
              {
                return result;
              }
            }
            catch (CoreException ex)
            {
              // Ignore.
            }
          }
        }
      }

      return super.createCommand(object, domain, commandClass, commandParameter);
    }

    return super.createCommand(object, domain, commandClass, commandParameter);
  }

  @SuppressWarnings("restriction")
  @Override
  protected Command createPrimaryDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> collection)
  {
    final List<IPath> paths = new ArrayList<IPath>();
    for (Object value : collection)
    {
      if ("org.eclipse.egit.ui.internal.repository.tree.RepositoryNode".equals(value.getClass().getName()))
      {
        paths.add((IPath)ReflectUtil.invokeMethod("getPath", value));
      }
    }

    if (collection.size() == paths.size())
    {
      return new TargletDragAndDropCommand(domain, owner, location, operations, operation, collection)
      {
        @Override
        protected boolean prepareDropCopyOn()
        {
          dragCommand = IdentityCommand.INSTANCE;

          dropCommand = new CompoundCommand(CompoundCommand.MERGE_COMMAND_ALL)
          {
            @Override
            public void execute()
            {
              Targlet targlet = TargletFactory.eINSTANCE.createTarglet();
              targlet.getInstallableUnitGenerators();

              IPath path = paths.get(0);

              final SourceLocator sourceLocator = ResourcesFactory.eINSTANCE.createSourceLocator(path.toString(), true);
              WorkspaceIUAnalyzer workspaceIUAnalyzer = new WorkspaceIUAnalyzer(null);
              workspaceIUAnalyzer.analyze(sourceLocator, IUGenerator.DEFAULTS, new NullProgressMonitor());
              Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = workspaceIUAnalyzer.getWorkspaceIUInfos();

              final TargletTask targletTask = (TargletTask)owner;

              List<Requirement> requirements = new ArrayList<Requirement>();
              final Set<Requirement> redundant = new HashSet<Requirement>();
              for (Targlet otherTarglet : targletTask.getTarglets())
              {
                for (Requirement requirement : otherTarglet.getRequirements())
                {
                  requirements.add(requirement);
                }
              }

              final Set<IInstallableUnit> ius = workspaceIUInfos.keySet();
              HashSet<IInstallableUnit> ius2 = new HashSet<IInstallableUnit>(ius);
              IQueryable<IInstallableUnit> queryable = new CollectionResult<IInstallableUnit>(ius2);
              for (IInstallableUnit rootIU : ius2)
              {
                for (IRequirement requirement : rootIU.getRequirements())
                {
                  if (requirement instanceof org.eclipse.equinox.internal.p2.metadata.IRequiredCapability)
                  {
                    org.eclipse.equinox.internal.p2.metadata.IRequiredCapability requiredCapability = (org.eclipse.equinox.internal.p2.metadata.IRequiredCapability)requirement;
                    String namespace = requiredCapability.getNamespace();
                    if (IInstallableUnit.NAMESPACE_IU_ID.equals(namespace))
                    {
                      String name = requiredCapability.getName();
                      for (Requirement requirement2 : requirements)
                      {
                        if (name.equals(requirement2.getName()))
                        {
                          redundant.add(requirement2);
                        }
                      }
                    }

                    for (IInstallableUnit iu : P2Util.asIterable(queryable.query(QueryUtil.createMatchQuery(requirement.getMatches()), null)))
                    {
                      ius.remove(iu);
                    }
                  }
                }
              }

              targlet.setName(getTargletName("Default"));
              EList<Requirement> targletRequirements = targlet.getRequirements();
              for (IInstallableUnit iu : new TreeSet<IInstallableUnit>(ius))
              {
                targletRequirements.add(P2Factory.eINSTANCE.createRequirement(iu.getId()));
              }

              sourceLocator.setRootFolder(getSourceLocation(path));
              targlet.getSourceLocators().add(sourceLocator);

              appendAndExecute(new AddCommand(domain, targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__TARGLETS, targlet));
              if (!redundant.isEmpty())
              {
                appendAndExecute(DeleteCommand.create(domain, redundant));
              }
            }

            private String getSourceLocation(IPath path)
            {
              TargletTask targletTask = (TargletTask)owner;
              Resource resource = targletTask.eResource();
              if (resource != null)
              {
                String candidate = null;
                for (Iterator<EObject> it = resource.getAllContents(); it.hasNext();)
                {
                  EObject eObject = it.next();
                  EClass eClass = eObject.eClass();
                  if ("GitCloneTask".equals(eClass.getName()))
                  {
                    EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature("remoteURI");
                    if (eStructuralFeature != null)
                    {
                      String remoteURI = (String)eObject.eGet(eStructuralFeature);
                      try
                      {
                        URI uri = URI.createURI(remoteURI);
                        if (!uri.isHierarchical())
                        {
                          uri = URI.createURI(uri.opaquePart());
                        }

                        if ("git".equals(uri.fileExtension()))
                        {
                          uri = uri.trimFileExtension();
                        }

                        String baseName = URI.decode(uri.lastSegment());
                        if (path.toString().endsWith("/" + baseName))
                        {
                          String id = ((SetupTask)eObject).getID();
                          if (id != null)
                          {
                            String result = "${" + id + ".location}";
                            if (candidate != null)
                            {
                              candidate = result;
                            }

                            return result;
                          }
                        }
                      }
                      catch (Throwable throwable)
                      {
                        // Ignore.
                      }
                    }
                  }
                }

                if (candidate != null)
                {
                  return candidate;
                }
              }

              return path.toString();
            }

            @Override
            protected boolean prepare()
            {
              return true;
            }
          };

          return dropCommand.canExecute();
        }
      };
    }

    return super.createPrimaryDragAndDropCommand(domain, owner, location, operations, operation, collection);
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
    return SetupTargletsEditPlugin.INSTANCE;
  }

  /**
   * @author Ed Merks
   */
  private static class TargletDragAndDropCommand extends DragAndDropCommand
  {
    public TargletDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> collection)
    {
      super(domain, owner, location, operations, operation, collection, true);
    }

    protected String getTargletName(String defaultName)
    {
      TargletTask targletTask = (TargletTask)owner;
      for (Scope scope = targletTask.getScope(); scope != null; scope = scope.getParentScope())
      {
        if (scope instanceof Project)
        {
          String label = scope.getLabel();
          if (StringUtil.isEmpty(label))
          {
            label = StringUtil.cap(scope.getName());
          }

          return label;
        }
      }

      return defaultName;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class LocaleItemLabelProvider implements IItemLabelProvider
  {
    private final AdapterFactoryItemDelegator itemDelegator;

    private Map<String, String> localeMap;

    public LocaleItemLabelProvider(AdapterFactoryItemDelegator itemDelegator)
    {
      this.itemDelegator = itemDelegator;
    }

    public Object getImage(Object object)
    {
      return itemDelegator.getImage(object);
    }

    public String getText(Object object)
    {
      return getLocaleMap().get(object);
    }

    public Map<String, String> getLocaleMap()
    {
      if (localeMap == null)
      {
        localeMap = new HashMap<String, String>();
        for (Locale locale : Locale.getAvailableLocales())
        {
          localeMap.put(locale.toString(), locale.toString() + " - " + locale.getDisplayName());
        }
      }

      return localeMap;
    }
  }
}
