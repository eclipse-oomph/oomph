/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.base.util.BaseResourceImpl;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.ui.OomphPreferencePage;
import org.eclipse.oomph.internal.ui.TaskItemDecorator;
import org.eclipse.oomph.jreinfo.ui.JREInfoUIPlugin;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.internal.ui.P2UIPlugin;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.VariableType;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.CacheHandling;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.p2.provider.SetupP2EditPlugin;
import org.eclipse.oomph.setup.p2.util.P2TaskUISevices;
import org.eclipse.oomph.setup.provider.SetupEditPlugin;
import org.eclipse.oomph.setup.ui.recorder.RecorderManager;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager.SynchronizationController;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerPreferencePage;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.MonitorUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLHelper;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.query.CollectionResult;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.text.templates.SimpleTemplateVariableResolver;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateVariableResolver;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.intro.IIntroManager;

import org.osgi.framework.BundleContext;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Eike Stepper
 */
public final class SetupUIPlugin extends OomphUIPlugin
{
  public static final SetupUIPlugin INSTANCE = new SetupUIPlugin();

  public static final String PLUGIN_ID = INSTANCE.getSymbolicName();

  public static final String PREF_HEADLESS = "headless.startup"; //$NON-NLS-1$

  public static final String PREF_SKIP_STARTUP_TASKS = "skip.startup.tasks"; //$NON-NLS-1$

  public static final String PREF_P2_STARTUP_TASKS = "p2.startup.tasks"; //$NON-NLS-1$

  public static final String PREF_ENABLE_PREFERENCE_RECORDER = "enable.preference.recorder"; //$NON-NLS-1$

  public static final String PREF_PREFERENCE_RECORDER_TARGET = "preference.recorder.target"; //$NON-NLS-1$

  public static final String PREF_INITIALIZED_PREFERENCE_PAGES = "initialized.preference.pages"; //$NON-NLS-1$

  public static final String PREF_IGNORED_PREFERENCE_PAGES = "ingored.preference.pages"; //$NON-NLS-1$

  public static final boolean QUESTIONNAIRE_SKIP = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_QUESTIONNAIRE_SKIP);

  private static final String RESTARTING_FILE_NAME = "restarting"; //$NON-NLS-1$

  private static final String ANNOTATION_SOURCE_INITIAL = "initial"; //$NON-NLS-1$

  private static final String ANNOTATION_DETAILS_KEY_OFFLINE = "offline"; //$NON-NLS-1$

  private static final String ANNOTATION_DETAILS_KEY_MIRRORS = "mirrors"; //$NON-NLS-1$

  private static final boolean SETUP_SKIP = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_SKIP);

  private static Implementation plugin;

  public SetupUIPlugin()
  {
    super(new ResourceLocator[] { JREInfoUIPlugin.INSTANCE, SetupEditPlugin.INSTANCE, SetupCorePlugin.INSTANCE, SetupP2EditPlugin.INSTANCE,
        P2UIPlugin.INSTANCE });
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  public static void initialStart(File ws, boolean offline, boolean mirrors)
  {
    Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
    annotation.setSource(ANNOTATION_SOURCE_INITIAL);
    annotation.getDetails().put(ANNOTATION_DETAILS_KEY_OFFLINE, Boolean.toString(offline));
    annotation.getDetails().put(ANNOTATION_DETAILS_KEY_MIRRORS, Boolean.toString(mirrors));

    File file = new File(ws, ".metadata/.plugins/" + SetupUIPlugin.INSTANCE.getSymbolicName() + "/" + RESTARTING_FILE_NAME); //$NON-NLS-1$ //$NON-NLS-2$
    saveRestartFile(file, annotation);
  }

  public static void restart(Trigger trigger, EList<SetupTask> setupTasks)
  {
    if (!setupTasks.isEmpty())
    {
      Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
      annotation.setSource(trigger.toString());
      annotation.getReferences().addAll(setupTasks);

      saveRestartFile(getRestartingFile(), annotation);
    }

    PlatformUI.getWorkbench().restart();
  }

  private static void saveRestartFile(File file, Annotation annotation)
  {
    try
    {
      Resource resource = SetupCoreUtil.createResourceSet().createResource(URI.createFileURI(file.toString()));
      resource.getContents().add(annotation);
      resource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
    }
    catch (Exception ex)
    {
      // Ignore
    }
  }

  public static boolean isSkipStartupTasks()
  {
    return plugin.getPreferenceStore().getBoolean(PREF_SKIP_STARTUP_TASKS);
  }

  private static File getRestartingFile()
  {
    return new File(INSTANCE.getStateLocation().toString(), RESTARTING_FILE_NAME);
  }

  static void performStartup()
  {
    if (!PropertiesUtil.isProperty(PREF_HEADLESS) && !SetupUtil.SETUP_ARCHIVER_APPLICATION)
    {
      // These are only to force class loading on a background thread.
      final boolean synchronizerAvailable = SynchronizerManager.Availability.AVAILABLE;
      SetupTaskPerformer.RULE_VARIABLE_ADAPTER.toString();
      RecorderManager.INSTANCE.toString();

      final Display display = Display.getDefault();
      display.asyncExec(new Runnable()
      {
        @Override
        public void run()
        {
          if (!SetupUtil.INSTALLER_APPLICATION)
          {
            initJDTTemplateVariables();
            SetupPropertyTester.setStarting(true);

            final IWorkbench workbench = PlatformUI.getWorkbench();
            IExtensionTracker extensionTracker = workbench.getExtensionTracker();
            if (extensionTracker == null || workbench.getWorkbenchWindowCount() == 0)
            {
              display.timerExec(1000, this);
            }
            else
            {
              if (!synchronizerAvailable || !SynchronizerManager.ENABLED)
              {
                PreferenceManager preferenceManager = workbench.getPreferenceManager();
                preferenceManager.remove("/" + OomphPreferencePage.ID + "/" + SetupPreferencePage.ID + "/" + SynchronizerPreferencePage.ID); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
              }

              if (SetupTaskPerformer.REMOTE_DEBUG)
              {
                MessageDialog.openInformation(UIUtil.getShell(), Messages.SetupUIPlugin_remoteDebugPauseDialog_title,
                    Messages.SetupUIPlugin_remoteDebugPauseDialog_message);
              }

              if (Platform.OS_MACOSX.equals(Platform.getOS()))
              {
                new TaskItemDecorator();
              }

              RecorderManager.Lifecycle.start(display);
              plugin.stopRecorder = true;

              if (!SETUP_SKIP && (!isSkipStartupTasks() || getRestartingFile().exists()))
              {
                new Job(Messages.SetupUIPlugin_setupCheckJob_name)
                {
                  @Override
                  protected IStatus run(IProgressMonitor monitor)
                  {
                    try
                    {
                      performStartup(workbench, monitor);
                      return Status.OK_STATUS;
                    }
                    finally
                    {
                      SetupPropertyTester.setStarting(false);
                    }
                  }
                }.schedule();
              }
              else
              {
                Job mirrorJob = new Job(Messages.SetupUIPlugin_initSetupModelsJob_name)
                {
                  @Override
                  protected IStatus run(IProgressMonitor monitor)
                  {
                    try
                    {
                      monitor.beginTask(Messages.SetupUIPlugin_initSetupModelsJob_taskName, 10);

                      ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
                      resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
                      mirror(resourceSet, monitor, 10);
                      SetupContext.setSelf(SetupContext.createSelf(resourceSet));
                      handleBrandingNotificationURI(resourceSet);

                      return Status.OK_STATUS;
                    }
                    finally
                    {
                      SetupPropertyTester.setStarting(false);
                    }
                  }
                };

                mirrorJob.schedule();
              }
            }
          }
        }
      });
    }
  }

  private static void handleBrandingNotificationURI(ResourceSet resourceSet)
  {
    EObject selfProductVersion = resourceSet
        .getEObject(URI.createURI("catalog:/self-product-catalog.setup#//@products[name='product']/@versions[name='version']"), false); //$NON-NLS-1$
    if (selfProductVersion instanceof ProductVersion)
    {
      ProductVersion productVersion = (ProductVersion)selfProductVersion;
      Product product = productVersion.getProduct();
      if (product != null)
      {
        ProductCatalog productCatalog = product.getProductCatalog();
        if (productCatalog != null)
        {
          Index index = productCatalog.getIndex();
          if (index != null)
          {
            String notificationURI = BaseUtil.getAnnotation(index, AnnotationConstants.ANNOTATION_BRANDING_INFO, AnnotationConstants.KEY_NOTIFICATION_URI);
            if (notificationURI != null)
            {
              String productLabel = product.getLabel();
              String productVersionLabel = productVersion.getLabel();
              if (productLabel != null && productLabel.startsWith("Eclipse") && productVersionLabel != null) //$NON-NLS-1$
              {
                handleBrandingNotificationURI(notificationURI, productLabel, productVersionLabel);
              }
            }

            try
            {
              handleProblemAnnotations(productVersion);
            }
            catch (Exception ex)
            {
              INSTANCE.log(ex, IStatus.WARNING);
            }

            try
            {
              handleNotificationAnnotations(productVersion);
            }
            catch (Exception ex)
            {
              INSTANCE.log(ex, IStatus.WARNING);
            }
          }
        }
      }
    }
  }

  private static void handleBrandingNotificationURI(String notificationURI, String scope, String version)
  {
    if ("true".equals(PropertiesUtil.getProperty("org.eclipse.oomph.setup.donate", Boolean.toString(!Platform.inDevelopmentMode())))) //$NON-NLS-1$//$NON-NLS-2$
    {
      try
      {
        Version versionValue = Version.create(version);
        if (versionValue.compareTo(Version.create("4.16")) >= 0) //$NON-NLS-1$
        {
          String resolvedURI = notificationURI.toString().replace("${scope.version}", URI.encodeQuery(StringUtil.safe(version), false)).replace("${scope}", //$NON-NLS-1$ //$NON-NLS-2$
              URI.encodeQuery(StringUtil.safe(scope), false).replace("+", "%2B")); //$NON-NLS-1$ //$NON-NLS-2$
          SetupPropertyTester.setDonating(resolvedURI);
          if (rememberBrandingNotificationURI(notificationURI))
          {
            UIUtil.asyncExec(new Runnable()
            {
              @Override
              public void run()
              {
                ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
                IHandlerService handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
                Command donateCommand = commandService.getCommand("org.eclipse.oomph.setup.donate"); //$NON-NLS-1$
                if (donateCommand != null)
                {
                  if (donateCommand.isEnabled())
                  {
                    try
                    {
                      donateCommand.executeWithChecks(handlerService.createExecutionEvent(donateCommand, null));
                    }
                    catch (Exception ex)
                    {
                      INSTANCE.log(ex, IStatus.WARNING);
                    }
                  }
                }
              }
            });
          }
        }
      }
      catch (Exception ex)
      {
        INSTANCE.log(ex, IStatus.WARNING);
      }
    }
  }

  private static boolean rememberBrandingNotificationURI(String notificationURI)
  {
    File file = new File(SetupContext.GLOBAL_SETUPS_LOCATION_URI.toFileString(), "brandingNotificationURIs.txt"); //$NON-NLS-1$
    Set<String> brandingNotificationURIs = new LinkedHashSet<>();
    try
    {
      if (file.isFile())
      {
        brandingNotificationURIs.addAll(IOUtil.readLines(file, "UTF-8")); //$NON-NLS-1$
      }
    }
    catch (RuntimeException ex)
    {
      // Ignore.
    }

    boolean added = brandingNotificationURIs.add(notificationURI);
    if (added)
    {
      try
      {
        IOUtil.writeLines(file, "UTF-8", new ArrayList<>(brandingNotificationURIs)); //$NON-NLS-1$
      }
      catch (RuntimeException ex)
      {
        // Ignore.
      }
    }

    return added;
  }

  private static List<Annotation> getAllAnnotations(ProductVersion productVersion, String source)
  {
    List<Annotation> annotations = new ArrayList<>();
    annotations.addAll(getAnnotations(productVersion, source));

    Product product = productVersion.getProduct();
    annotations.addAll(getAnnotations(product, source));

    ProductCatalog productCatalog = product.getProductCatalog();
    annotations.addAll(getAnnotations(productCatalog, source));

    Index index = productCatalog.getIndex();
    annotations.addAll(getAnnotations(index, source));
    return annotations;
  }

  private static List<Annotation> getAnnotations(ModelElement modelElement, String source)
  {
    List<Annotation> result = new ArrayList<>();
    for (Annotation annotation : modelElement.getAnnotations())
    {
      if (source.equals(annotation.getSource()))
      {
        result.add(annotation);
      }
    }
    return result;
  }

  private static void handleProblemAnnotations(ProductVersion productVersion)
  {
    List<Annotation> problemAnnotations = getAllAnnotations(productVersion, AnnotationConstants.ANNOTATION_PROBLEM);
    List<Annotation> applicableNotificationAnnotations = gatherApplicableAnnotations(productVersion, problemAnnotations, null);
    if (!applicableNotificationAnnotations.isEmpty())
    {
      SetupPropertyTester.setProblem(applicableNotificationAnnotations.get(0).getDetails().get(AnnotationConstants.KEY_URI));
    }
  }

  private static void handleNotificationAnnotations(ProductVersion productVersion)
  {
    // Default to false if we are in development mode.
    if ("true".equals(PropertiesUtil.getProperty("org.eclipse.oomph.setup.notification", Boolean.toString(!Platform.inDevelopmentMode())))) //$NON-NLS-1$//$NON-NLS-2$
    {
      List<Annotation> notificationAnnotations = getAllAnnotations(productVersion, AnnotationConstants.ANNOTATION_NOTIFICATION);
      List<Annotation> applicableNotificationAnnotations = gatherApplicableAnnotations(productVersion, notificationAnnotations,
          SetupUIPlugin::isRememberedNotificationURI);
      if (!applicableNotificationAnnotations.isEmpty())
      {
        SetupPropertyTester.setNotifications(applicableNotificationAnnotations);

        UIUtil.asyncExec(() -> {
          IIntroManager introManager = PlatformUI.getWorkbench().getIntroManager();
          introManager.closeIntro(introManager.getIntro());

          UIUtil.asyncExec(() -> {
            ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
            IHandlerService handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
            Command notificationsCommand = commandService.getCommand("org.eclipse.oomph.setup.notifications"); //$NON-NLS-1$
            if (notificationsCommand != null)
            {
              if (notificationsCommand.isEnabled())
              {
                try
                {
                  notificationsCommand.executeWithChecks(handlerService.createExecutionEvent(notificationsCommand, null));
                }
                catch (Exception ex)
                {
                  INSTANCE.log(ex, IStatus.WARNING);
                }
              }
            }
          });
        });
      }
    }
  }

  private static List<Annotation> gatherApplicableAnnotations(ProductVersion productVersion, List<Annotation> annotations, Predicate<String> ignore)
  {
    Agent agent = P2Util.getAgentManager().getCurrentAgent();
    Profile profile = agent == null ? null : agent.getCurrentProfile();
    IQueryable<IInstallableUnit> queriable = null;
    SetupTaskPerformer performer = null;

    List<Annotation> applicableNotificationAnnotations = new ArrayList<>();
    LOOP: for (Annotation notificationAnnotation : annotations)
    {
      String uri = notificationAnnotation.getDetails().get(AnnotationConstants.KEY_URI);
      if (uri == null || ignore != null && ignore.test(uri))
      {
        continue;
      }

      // Ensure that the annotation's requirements are all satisfied by the profile.
      for (EObject eObject : notificationAnnotation.getContents())
      {
        if (eObject instanceof Requirement)
        {
          if (profile == null)
          {
            continue LOOP;
          }

          if (queriable == null)
          {
            List<IInstallableUnit> extraIUs = new ArrayList<>();
            extraIUs.add(P2Util.createJREIU("jre")); //$NON-NLS-1$
            extraIUs.addAll(getInstalledJREs());
            queriable = QueryUtil.compoundQueryable(profile, new CollectionResult<>(extraIUs));
          }

          Requirement requirement = (Requirement)eObject;

          String filter = requirement.getFilter();
          if (filter != null)
          {
            if (performer == null)
            {
              try
              {
                performer = createSetupTaskPerformer(productVersion.getProduct().getProductCatalog().getIndex().eResource().getResourceSet(), Trigger.MANUAL);
              }
              catch (Exception ex)
              {
                continue LOOP;
              }
            }

            if (!performer.matchesFilterContext(filter))
            {
              continue;
            }
          }

          // A normal requirement is satisfied if its IU is present while a negative requirement is satisfied if the IU is not present.
          IQueryResult<IInstallableUnit> result = queriable.query(QueryUtil.createMatchQuery(requirement.toIRequirement().getMatches()), null);
          if (result.isEmpty() ? requirement.getMin() > 0 : requirement.getMax() == 0)
          {
            continue LOOP;
          }
        }
        else
        {
          continue LOOP;
        }
      }

      applicableNotificationAnnotations.add(notificationAnnotation);
    }

    return applicableNotificationAnnotations;
  }

  @SuppressWarnings("nls")
  private static List<IInstallableUnit> getInstalledJREs()
  {
    List<IInstallableUnit> result = new ArrayList<>();
    try
    {
      Class<?> javaRuntimeClass = CommonPlugin.loadClass("org.eclipse.jdt.launching", "org.eclipse.jdt.launching.JavaRuntime");
      Object[] vmInstallTypes = ReflectUtil.invokeMethod("getVMInstallTypes", javaRuntimeClass);
      for (Object vmInstallType : vmInstallTypes)
      {
        String id = ReflectUtil.invokeMethod("getId", vmInstallType);
        if ("org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType".equals(id))
        {
          Object[] vmInstalls = ReflectUtil.invokeMethod("getVMInstalls", vmInstallType);
          Class<?> vmStandinClass = CommonPlugin.loadClass("org.eclipse.jdt.launching", "org.eclipse.jdt.launching.VMStandin");
          Constructor<?> constructor = ReflectUtil.getConstructor(vmStandinClass, vmInstalls.getClass().getComponentType());
          for (Object vmInstall : vmInstalls)
          {
            Object vmStandin = constructor.newInstance(vmInstall);
            String javaVersion = ReflectUtil.invokeMethod("getJavaVersion", vmStandin);
            if (javaVersion != null)
            {
              try
              {
                Version version = Version.create(javaVersion);
                MetadataFactory.InstallableUnitDescription iuDescription = new MetadataFactory.InstallableUnitDescription();
                iuDescription.setId("installed.jre");
                iuDescription.setVersion(version);
                iuDescription.addProvidedCapabilities(List.of(MetadataFactory.createProvidedCapability("org.eclipse.equinox.p2.iu", "installed.jre", version)));
                result.add(MetadataFactory.createInstallableUnit(iuDescription));
              }
              catch (RuntimeException ex)
              {
                //$FALL-THROUGH$
              }
            }
          }
        }
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    return result;
  }

  public static boolean isRememberedNotificationURI(String notificationURI)
  {
    return !rememberNotificationURI(notificationURI, SetupContext.GLOBAL_SETUPS_LOCATION_URI, false)
        || !rememberNotificationURI(notificationURI, SetupContext.CONFIGURATION_STATE_LOCATION_URI, false)
        || !rememberNotificationURI(notificationURI, SetupContext.WORKSPACE_STATE_LOCATION_URI, false);

  }

  public static void rememberNotificationURI(String notificationURI, URI baseLocation)
  {
    rememberNotificationURI(notificationURI, baseLocation, true);
  }

  private static boolean rememberNotificationURI(String notificationURI, URI baseLocation, boolean save)
  {
    File file = new File(baseLocation.toFileString(), "notificationURIs.txt"); //$NON-NLS-1$
    Set<String> notifications = new LinkedHashSet<>();
    try
    {
      if (file.isFile())
      {
        notifications.addAll(IOUtil.readLines(file, "UTF-8")); //$NON-NLS-1$
      }
    }
    catch (RuntimeException ex)
    {
      // Ignore.
    }

    boolean added = notifications.add(notificationURI);
    if (added && save)
    {
      try
      {
        ArrayList<String> lines = new ArrayList<>(notifications);
        IOUtil.writeLines(file, "UTF-8", lines.subList(0, Math.min(lines.size(), 500))); //$NON-NLS-1$
      }
      catch (RuntimeException ex)
      {
        // Ignore.
      }
    }

    return added;
  }

  private static void initJDTTemplateVariables()
  {
    // Modify the JDT's variable resolvers so that ${user} expands to a property author name,
    // not simply to the System.getProperty("user.name") which is the account name and generally not appropriate as the author name.
    try
    {
      boolean hasJDTUserName = !StringUtil.isEmpty(System.getProperty("jdt.user.name")); //$NON-NLS-1$
      Class<?> javaUIPluginClass = CommonPlugin.loadClass("org.eclipse.jdt.ui", "org.eclipse.jdt.internal.ui.JavaPlugin"); //$NON-NLS-1$ //$NON-NLS-2$
      Object javaUIPlugin = ReflectUtil.invokeMethod("getDefault", javaUIPluginClass); //$NON-NLS-1$
      Object codeTemplateContextRegistry = ReflectUtil.invokeMethod("getCodeTemplateContextRegistry", javaUIPlugin); //$NON-NLS-1$

      for (@SuppressWarnings("all")
      Iterator<TemplateContextType> it = ReflectUtil.invokeMethod("contextTypes", codeTemplateContextRegistry); it.hasNext();)
      {
        TemplateContextType templateContextType = it.next();
        for (@SuppressWarnings("all")
        Iterator<TemplateVariableResolver> it2 = templateContextType.resolvers(); it2.hasNext();)
        {
          TemplateVariableResolver templateVariableResolver = it2.next();
          if ("user".equals(templateVariableResolver.getType())) //$NON-NLS-1$
          {
            if (hasJDTUserName)
            {
              templateContextType.addResolver(new SimpleTemplateVariableResolver(templateVariableResolver.getType(), templateVariableResolver.getDescription())
              {
                @Override
                protected String resolve(TemplateContext context)
                {
                  return PropertiesUtil.getProperty("jdt.user.name", PropertiesUtil.getProperty("user.name")); //$NON-NLS-1$ //$NON-NLS-2$
                }
              });
            }

            templateContextType.addResolver(new SimpleTemplateVariableResolver("location", Messages.SetupUIPlugin_locationVariableResolver_description) //$NON-NLS-1$
            {
              @Override
              protected String resolve(TemplateContext context)
              {
                String result = PropertiesUtil.getProperty("jdt.user.location", ""); //$NON-NLS-1$ //$NON-NLS-2$
                if (!StringUtil.isEmpty(result) && !result.startsWith(" ")) //$NON-NLS-1$
                {
                  result = " " + result; //$NON-NLS-1$
                }

                return result;
              }
            });

            break;
          }
        }
      }
    }
    catch (Throwable ex)
    {
      // Ignore if anything goes wrong registering our variable resolvers.
    }
  }

  private static void mirror(final ResourceSet resourceSet, IProgressMonitor monitor, int work)
  {
    ResourceMirror resourceMirror = new ResourceMirror(resourceSet)
    {
      @Override
      protected void run(String taskName, IProgressMonitor monitor)
      {
        List<URI> uris = new ArrayList<>();
        URIConverter uriConverter = resourceSet.getURIConverter();
        for (URI uri : new URI[] { SetupContext.INSTALLATION_SETUP_URI, SetupContext.WORKSPACE_SETUP_URI, SetupContext.USER_SETUP_URI })
        {
          if (uri != null && uriConverter.exists(uri, null))
          {
            uris.add(uri);
          }
        }

        perform(uris);
        resolveProxies();
      }
    };

    resourceMirror.begin(MonitorUtil.create(monitor, work));
  }

  private static Set<? extends EObject> checkCrossReferences(ResourceSet resourceSet, URI uri)
  {
    Set<EObject> result = new HashSet<>();

    Resource resource = resourceSet.getResource(uri, false);
    if (resource != null)
    {
      EList<EObject> contents = resource.getContents();
      if (!contents.isEmpty())
      {
        EObject eObject = contents.get(0);
        for (EObject eCrossReference : eObject.eCrossReferences())
        {
          Resource eResource = eCrossReference.eResource();
          if (eResource != null)
          {
            for (EObject content : eResource.getContents())
            {
              EObject eContainer = content.eContainer();
              if (eContainer != null)
              {
                result.add(eContainer);
              }
            }

            eResource.unload();
          }
        }
      }
    }

    return result;
  }

  private static void performStartup(final IWorkbench workbench, IProgressMonitor monitor)
  {
    // TODO Remove the following try/catch block after bug 485018 has been fixed.
    try
    {
      Agent currentAgent = P2Util.getAgentManager().getCurrentAgent();
      if (currentAgent != null)
      {
        Profile currentProfile = currentAgent.getCurrentProfile();
        if (currentProfile != null)
        {
          BundlePool bundlePool = currentProfile.getBundlePool();
          if (bundlePool != null)
          {
            File bundlePoolLocation = bundlePool.getLocation();
            if (bundlePoolLocation != null)
            {
              String installFolderLocation = currentProfile.getProperty(IProfile.PROP_INSTALL_FOLDER);
              if (installFolderLocation != null && !bundlePoolLocation.equals(new File(installFolderLocation)))
              {
                File eclipseExtensionFeaturesFolder = new File(bundlePoolLocation, ".eclipseextension/features"); //$NON-NLS-1$
                eclipseExtensionFeaturesFolder.mkdirs();
              }
            }
          }
        }
      }

      Profile currentProfile = P2Util.getAgentManager().getCurrentAgent().getCurrentProfile();
      String installFolderLocation = currentProfile != null ? currentProfile.getProperty(IProfile.PROP_INSTALL_FOLDER) : null;
      if (installFolderLocation != null)
      {
        BundlePool bundlePool = currentProfile.getBundlePool();
        File bundlePoolLocation = bundlePool.getLocation();

        if (!bundlePoolLocation.equals(new File(installFolderLocation)))
        {
          File eclipseExtensionFeaturesFolder = new File(bundlePoolLocation, ".eclipseextension/features"); //$NON-NLS-1$
          eclipseExtensionFeaturesFolder.mkdirs();
        }
      }
    }
    catch (Throwable throwable)
    {
      SetupUIPlugin.INSTANCE.log(throwable, IStatus.WARNING);
    }

    monitor.beginTask("", 105); //$NON-NLS-1$
    Trigger trigger = Trigger.STARTUP;
    boolean restarting = false;
    Set<URI> neededRestartTasks = new HashSet<>();

    try
    {
      File restartingFile = getRestartingFile();
      if (restartingFile.exists())
      {
        monitor.setTaskName(NLS.bind(Messages.SetupUIPlugin_loadRestartTasks_taskName, restartingFile));
        Resource resource = SetupCoreUtil.createResourceSet().getResource(URI.createFileURI(restartingFile.toString()), true);

        Annotation annotation = (Annotation)EcoreUtil.getObjectByType(resource.getContents(), BasePackage.Literals.ANNOTATION);
        resource.getContents().remove(annotation);

        if (ANNOTATION_SOURCE_INITIAL.equals(annotation.getSource()))
        {
          if ("true".equals(annotation.getDetails().get(ANNOTATION_DETAILS_KEY_OFFLINE))) //$NON-NLS-1$
          {
            System.setProperty(SetupProperties.PROP_SETUP_OFFLINE_STARTUP, "true"); //$NON-NLS-1$
          }

          if ("true".equals(annotation.getDetails().get(ANNOTATION_DETAILS_KEY_MIRRORS)))//$NON-NLS-1$
          {
            System.setProperty(SetupProperties.PROP_SETUP_MIRRORS_STARTUP, "true"); //$NON-NLS-1$
          }
        }
        else
        {
          for (EObject eObject : annotation.getReferences())
          {
            neededRestartTasks.add(EcoreUtil.getURI(eObject));
          }

          trigger = Trigger.get(annotation.getSource());
          restarting = true;
        }

        IOUtil.deleteBestEffort(restartingFile);
      }
    }
    catch (Exception ex)
    {
      // Ignore
    }

    monitor.worked(1);

    // Disabled for bug 459486:
    // if (!QUESTIONNAIRE_SKIP)
    // {
    // Questionnaire.perform(UIUtil.getShell(), false);
    // }

    // This performer is only used to detect a need to update or to open the setup wizard.
    SetupTaskPerformer performer = null;
    final ResourceSet resourceSet = SetupCoreUtil.createResourceSet();

    SynchronizationController synchronizationController = null;

    if (SynchronizerManager.Availability.AVAILABLE)
    {
      try
      {
        synchronizationController = SynchronizerManager.INSTANCE.startSynchronization(false, false, false);
      }
      catch (Exception ex)
      {
        INSTANCE.log(ex);
      }
    }

    monitor.setTaskName(Messages.SetupUIPlugin_createPerformerTask_name);

    try
    {
      try
      {
        if (synchronizationController != null)
        {
          Synchronization synchronization = synchronizationController.await();
          if (synchronization != null)
          {
            SynchronizerManager.INSTANCE.performSynchronization(synchronization, false, false);
          }
        }
      }
      catch (Exception ex)
      {
        INSTANCE.log(ex);
      }

      // Ensure that the demand created resources for the installation, workspace, and user are loaded and created.
      // Load the resource set quickly without doing ETag checking.
      resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
      mirror(resourceSet, monitor, 25);

      // Check the installation and workspace resources for cross references.
      // This unloads the cross referenced resources and returns the container objects of the root object(s) of those resources.
      Set<EObject> eContainers = new HashSet<>();
      eContainers.addAll(checkCrossReferences(resourceSet, SetupContext.INSTALLATION_SETUP_URI));
      if (SetupContext.WORKSPACE_SETUP_URI != null)
      {
        eContainers.addAll(checkCrossReferences(resourceSet, SetupContext.WORKSPACE_SETUP_URI));
      }

      if (!eContainers.isEmpty())
      {
        // Reload any resources that have been unloaded, this time with ETag checking.
        resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, CacheHandling.CACHE_WITH_ETAG_CHECKING);
        mirror(resourceSet, monitor, 75);

        // Resolve the containment proxies of the containers.
        for (EObject eContainer : eContainers)
        {
          for (@SuppressWarnings("unused")
          EObject eObject : eContainer.eContents())
          {
            // Resolve all containment proxies.
          }
        }
      }
      else
      {
        monitor.worked(75);
      }

      // Create the performer with a fully populated resource set.
      performer = createSetupTaskPerformer(resourceSet, trigger);

      // If we have a performer...
      if (performer != null)
      {
        // And it has unresolved variables.
        List<VariableTask> unresolvedVariables = performer.getUnresolvedVariables();
        if (!unresolvedVariables.isEmpty())
        {
          // They must be password variables that have been resolved to the encrypted value for " "
          performer.getResolvedVariables().addAll(unresolvedVariables);

          // If we don't clear them, the performer will throw an exception.
          unresolvedVariables.clear();
        }
      }
    }
    catch (OperationCanceledException ex)
    {
      //$FALL-THROUGH$
    }
    catch (Throwable ex)
    {
      INSTANCE.log(ex);
      return;
    }
    finally
    {
      SetupContext.setSelf(SetupContext.createSelf(resourceSet));
      handleBrandingNotificationURI(resourceSet);
    }

    monitor.worked(1);

    if (performer != null)
    {
      monitor.setTaskName(Messages.SetupUIPlugin_initPerformerTask_name);

      try
      {
        XMLHelper helper = new BaseResourceImpl.BaseHelperImpl(null);

        // At this point we know that no prompt was needed.
        performer.put(P2TaskUISevices.class, new P2TaskUIServicesPrompter());
        EList<SetupTask> neededTasks = performer.initNeededSetupTasks(MonitorUtil.create(monitor, 2));
        if (restarting)
        {
          for (Iterator<SetupTask> it = neededTasks.iterator(); it.hasNext();)
          {
            SetupTask setupTask = it.next();
            if (setupTask.getPriority() != SetupTask.PRIORITY_INSTALLATION)
            {
              String href = helper.getHREF(setupTask);
              if (href != null && neededRestartTasks.contains(URI.createURI(href)))
              {
                continue;
              }
            }

            it.remove();
          }
        }

        if (neededTasks.isEmpty())
        {
          // No tasks are needed, either. Nothing to do.
          return;
        }

        performer.setSkipConfirmation(true);
      }
      catch (Throwable ex)
      {
        INSTANCE.log(ex);
        return;
      }
    }
    else
    {
      monitor.worked(2);
    }

    monitor.worked(1);
    monitor.setTaskName(Messages.SetupUIPlugin_launchSetupWizardTask_name);

    final SetupTaskPerformer finalPerfomer = performer;
    UIUtil.asyncExec(new Runnable()
    {
      @Override
      public void run()
      {
        if (finalPerfomer != null)
        {
          resourceSet.getResources().add(finalPerfomer.getUser().eResource());
        }

        IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
        if (workbenchWindow == null)
        {
          IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
          if (workbenchWindows.length > 0)
          {
            workbenchWindow = workbenchWindows[0];
          }
        }

        if (workbenchWindow != null)
        {
          SetupWizard updater = finalPerfomer != null ? new SetupWizard.Updater(finalPerfomer)
              : new SetupWizard.Updater(SetupContext.createInstallationWorkspaceAndUser(resourceSet));
          updater.openDialog(workbenchWindow.getShell());
        }
      }
    });

    monitor.worked(1);
  }

  private static SetupTaskPerformer createSetupTaskPerformer(ResourceSet resourceSet, Trigger trigger) throws Exception
  {
    // Create a prompter that generally cancels except if all prompted variables are passwords.
    SetupPrompter prompter = new SetupPrompter()
    {
      @Override
      public OS getOS()
      {
        return OS.INSTANCE;
      }

      @Override
      public String getVMPath()
      {
        return null;
      }

      @Override
      public boolean promptVariables(List<? extends SetupTaskContext> contexts)
      {
        @SuppressWarnings("unchecked")
        List<SetupTaskPerformer> performers = (List<SetupTaskPerformer>)contexts;
        for (SetupTaskPerformer performer : performers)
        {
          List<VariableTask> unresolvedVariables = performer.getUnresolvedVariables();
          for (VariableTask variable : unresolvedVariables)
          {
            if (variable.getType() == VariableType.PASSWORD)
            {
              variable.setValue(PreferencesUtil.encrypt(" ")); //$NON-NLS-1$
            }
            else
            {
              return false;
            }
          }
        }

        return true;
      }

      @Override
      public String getValue(VariableTask variable)
      {
        return null;
      }

      @Override
      public UserCallback getUserCallback()
      {
        return null;
      }
    };

    return SetupTaskPerformer.createForIDE(resourceSet, prompter, trigger);
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipseUIPlugin
  {
    private boolean stopRecorder;

    public Implementation()
    {
      plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
      if (stopRecorder)
      {
        RecorderManager.Lifecycle.stop();
      }

      super.stop(context);
    }
  }
}
