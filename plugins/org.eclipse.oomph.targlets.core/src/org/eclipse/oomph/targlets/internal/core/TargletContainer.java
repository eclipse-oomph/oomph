/*
 * Copyright (c) 2014-2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.CommitContext;
import org.eclipse.oomph.p2.internal.core.CacheUsageConfirmer;
import org.eclipse.oomph.p2.internal.core.CachingRepositoryManager;
import org.eclipse.oomph.p2.internal.core.ProfileTransactionImpl;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.targlets.DropinLocation;
import org.eclipse.oomph.targlets.FeatureGenerator;
import org.eclipse.oomph.targlets.IUGenerator;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.core.ITargletContainerDescriptor.UpdateProblem;
import org.eclipse.oomph.targlets.core.TargletContainerEvent;
import org.eclipse.oomph.targlets.core.WorkspaceIUInfo;
import org.eclipse.oomph.util.HexUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.SubMonitor;
import org.eclipse.oomph.util.pde.TargetPlatformRunnable;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xmi.XMLOptions;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLOptionsImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.equinox.frameworkadmin.BundleInfo;
import org.eclipse.equinox.internal.p2.engine.DownloadManager;
import org.eclipse.equinox.internal.p2.engine.InstallableUnitOperand;
import org.eclipse.equinox.internal.p2.engine.InstallableUnitPhase;
import org.eclipse.equinox.internal.p2.engine.Phase;
import org.eclipse.equinox.internal.p2.engine.PhaseSet;
import org.eclipse.equinox.internal.p2.engine.phases.Collect;
import org.eclipse.equinox.internal.p2.engine.phases.Install;
import org.eclipse.equinox.internal.p2.engine.phases.Property;
import org.eclipse.equinox.internal.p2.engine.phases.Uninstall;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.internal.p2.metadata.ResolvedInstallableUnit;
import org.eclipse.equinox.internal.p2.touchpoint.natives.NativeTouchpoint;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.engine.spi.ProvisioningAction;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;
import org.eclipse.equinox.p2.planner.IProfileChangeRequest;
import org.eclipse.equinox.p2.publisher.PublisherInfo;
import org.eclipse.equinox.p2.publisher.PublisherResult;
import org.eclipse.equinox.p2.publisher.eclipse.BundlesAction;
import org.eclipse.equinox.p2.publisher.eclipse.FeaturesAction;
import org.eclipse.equinox.p2.query.CollectionResult;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRequest;
import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.spi.p2.publisher.PublisherHelper;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetLocationFactory;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.pde.core.target.LoadTargetDefinitionJob;
import org.eclipse.pde.core.target.TargetBundle;
import org.eclipse.pde.core.target.TargetFeature;
import org.eclipse.pde.internal.core.target.AbstractBundleContainer;
import org.eclipse.pde.internal.core.target.P2TargetUtils;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class TargletContainer extends AbstractBundleContainer implements ITargletContainer
{
  public static final String TYPE = "Targlet"; //$NON-NLS-1$

  public static final String IU_PROPERTY_SOURCE = "org.eclipse.oomph.targlet.source"; //$NON-NLS-1$

  private static final ThreadLocal<Boolean> FORCE_UPDATE = new ThreadLocal<>();

  private static final ThreadLocal<Boolean> MIRRORS = new ThreadLocal<>();

  private static final String A_PDE_TARGET_PLATFORM = "A.PDE.Target.Platform"; //$NON-NLS-1$

  private static final String A_PDE_TARGET_PLATFORM_LOWER_CASE = A_PDE_TARGET_PLATFORM.toLowerCase();

  private static final String FOLLOW_ARTIFACT_REPOSITORY_REFERENCES = "org.eclipse.equinox.p2.director.followArtifactRepositoryReferences"; //$NON-NLS-1$

  private static final byte[] BUFFER = new byte[8192];

  private static final String PROP_ARCH = "osgi.arch"; //$NON-NLS-1$

  private static final String PROP_OS = "osgi.os"; //$NON-NLS-1$

  private static final String PROP_WS = "osgi.ws"; //$NON-NLS-1$

  private static final String IU_FILTER = PropertiesUtil.getProperty("oomph.targlets.iu.filter"); //$NON-NLS-1$

  private static final Pattern IU_FILTER_PATTERN = IU_FILTER == null ? null : Pattern.compile(IU_FILTER);

  private static final boolean TARGLET_DEBUG = PropertiesUtil.isProperty("oomph.targlets.debug"); //$NON-NLS-1$

  private String id;

  private ITargetDefinition targetDefinition;

  private final EList<Targlet> targlets = new BasicEList<>();

  private final EList<String> composedTargets = new BasicEList<>();

  private ComposedTargetContent composedTargetContent;

  public TargletContainer(String id)
  {
    this.id = id;

    // Make the Targlets UI active so that PDE can use Platform.getAdapterManager() to load our registered adapters.
    try
    {
      CommonPlugin.loadClass("org.eclipse.oomph.targlets.ui", "org.eclipse.oomph.targlets.internal.ui.TargletsUIPlugin"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    catch (Throwable ex)
    {
      // Ignore.
    }
  }

  /**
   * Copies the passed targlets into this targlet container. Modifications of the passed targlets after the call
   * to this constructor won't have an impact on this targlet container.
   */
  private TargletContainer(String id, Collection<? extends Targlet> targlets, Collection<? extends String> composedTargets)
  {
    this(id);
    basicSetTarglets(targlets);
    basicSetComposedTargets(composedTargets);
  }

  @Override
  @SuppressWarnings("all")
  protected int getResolveBundlesWork()
  {
    return 999;
  }

  @Override
  @SuppressWarnings("all")
  protected int getResolveFeaturesWork()
  {
    return 1;
  }

  @SuppressWarnings("all")
  public boolean isContentEqual(AbstractBundleContainer container)
  {
    if (container instanceof TargletContainer)
    {
      TargletContainer targletContainer = (TargletContainer)container;
      return EcoreUtil.equals(targlets, targletContainer.targlets);
    }

    return false;
  }

  @Override
  public String getID()
  {
    return id;
  }

  @Override
  public void setID(String newID) throws CoreException
  {
    if (!ObjectUtil.equals(newID, id))
    {
      String oldID = id;
      id = newID;

      TargletContainerDescriptor descriptor = updateTargetDefinition();
      TargletContainerEvent event = new TargletContainerEvent.IDChangedEvent(this, descriptor, oldID);
      TargletContainerListenerRegistry.INSTANCE.notifyListeners(event, new NullProgressMonitor());
    }
  }

  private TargletContainerDescriptor updateTargetDefinition() throws CoreException
  {
    clearResolutionStatus();

    TargletContainerDescriptor descriptor = getDescriptor();
    if (descriptor != null)
    {
      descriptor.resetUpdateProblem();
    }

    if (targetDefinition != null)
    {
      TargetPlatformUtil.runWithTargetPlatformService(new TargetPlatformRunnable<Object>()
      {
        @Override
        public Object run(ITargetPlatformService service) throws CoreException
        {
          // Internally the latest PDE implementation represent state via org.eclipse.pde.internal.core.target.TargetDefinition.getDocument()
          // This is only updated when setTargetLocations is called.
          // So the following forces the DOM representation to be updated before we save.
          targetDefinition.setTargetLocations(targetDefinition.getTargetLocations());

          service.saveTargetDefinition(targetDefinition);
          return null;
        }
      });
    }
    return descriptor;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  @Override
  public TargletContainerDescriptor getDescriptor()
  {
    try
    {
      TargletContainerDescriptorManager manager = TargletContainerDescriptorManager.getInstance();
      return manager.getDescriptor(id, new NullProgressMonitor());
    }
    catch (Exception ex)
    {
      TargletsCorePlugin.INSTANCE.log(ex);
      return null;
    }
  }

  @Override
  public ITargetDefinition getTargetDefinition()
  {
    return targetDefinition;
  }

  @Override
  public Targlet getTarglet(String name)
  {
    int index = getTargletIndex(name);
    if (index != -1)
    {
      return TargletFactory.eINSTANCE.copyTarglet(targlets.get(index));
    }

    return null;
  }

  @Override
  public int getTargletIndex(String name)
  {
    for (int i = 0; i < targlets.size(); i++)
    {
      Targlet targlet = targlets.get(i);
      if (ObjectUtil.equals(targlet.getName(), name))
      {
        return i;
      }
    }

    return -1;
  }

  @Override
  public boolean hasTarglet(String name)
  {
    return getTargletIndex(name) != -1;
  }

  @Override
  public EList<Targlet> getTarglets()
  {
    return TargletFactory.eINSTANCE.copyTarglets(targlets);
  }

  @Override
  public EList<String> getComposedTargets()
  {
    return new BasicEList<>(composedTargets);
  }

  @Override
  public void setTarglets(Collection<? extends Targlet> targlets, Collection<? extends String> composedTargets) throws CoreException
  {
    basicSetTarglets(targlets);
    basicSetComposedTargets(composedTargets);

    TargletContainerDescriptor descriptor = updateTargetDefinition();
    TargletContainerEvent event = new TargletContainerEvent.TargletsChangedEvent(this, descriptor);
    TargletContainerListenerRegistry.INSTANCE.notifyListeners(event, new NullProgressMonitor());
  }

  private void basicSetTarglets(Collection<? extends Targlet> targlets)
  {
    this.targlets.clear();
    this.targlets.addAll(TargletFactory.eINSTANCE.copyTarglets(targlets));
  }

  private void basicSetComposedTargets(Collection<? extends String> composedTargets)
  {
    this.composedTargets.clear();
    this.composedTargets.addAll(composedTargets);
  }

  @Override
  public String serialize()
  {
    try
    {
      // Remove all leading indentation and line endings because the latest PDE target location support produces nicely indented content without these
      // and yet add bogus blank lines with these things.
      return Persistence.toXML(id, targlets, composedTargets).toString().replaceAll("(?m)^ +|\r?\n", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }
    catch (Exception ex)
    {
      TargletsCorePlugin.INSTANCE.log(ex);
      return null;
    }
  }

  @Override
  public boolean isIncludeSources()
  {
    for (Targlet targlet : targlets)
    {
      if (targlet.isIncludeSources())
      {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean isIncludeAllPlatforms()
  {
    for (Targlet targlet : targlets)
    {
      if (targlet.isIncludeAllPlatforms())
      {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean isIncludeAllRequirements()
  {
    for (Targlet targlet : targlets)
    {
      if (!targlet.isIncludeAllRequirements())
      {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean isIncludeBinaryEquivalents()
  {
    for (Targlet targlet : targlets)
    {
      if (!targlet.isIncludeBinaryEquivalents())
      {
        return false;
      }
    }

    return true;
  }

  @Override
  public String getProfileProperties()
  {
    StringBuilder result = new StringBuilder();
    for (Targlet targlet : targlets)
    {
      String profileProperties = targlet.getProfileProperties();
      if (!StringUtil.isEmpty(profileProperties))
      {
        if (result.length() != 0)
        {
          result.append(',');
        }

        result.append(profileProperties);
      }
    }

    return result.toString();
  }

  @Override
  public String getEnvironmentProperties()
  {
    StringBuilder builder = new StringBuilder();
    String ws = targetDefinition.getWS();
    if (ws == null)
    {
      ws = Platform.getWS();
    }

    builder.append(PROP_WS);
    builder.append("="); //$NON-NLS-1$
    builder.append(ws);
    builder.append(","); //$NON-NLS-1$
    String os = targetDefinition.getOS();
    if (os == null)
    {
      os = Platform.getOS();
    }

    builder.append(PROP_OS);
    builder.append("="); //$NON-NLS-1$
    builder.append(os);
    builder.append(","); //$NON-NLS-1$
    String arch = targetDefinition.getArch();
    if (arch == null)
    {
      arch = Platform.getOSArch();
    }

    builder.append(PROP_ARCH);
    builder.append("="); //$NON-NLS-1$
    builder.append(arch);

    builder.append(",org.eclipse.swt.buildtime=true"); //$NON-NLS-1$

    return builder.toString();
  }

  @Override
  public String getNLProperty()
  {
    String nl = targetDefinition.getNL();
    if (nl == null)
    {
      nl = Platform.getNL();
    }

    return nl;
  }

  @Override
  public String toString()
  {
    return "Targlet Container " + id; //$NON-NLS-1$
  }

  @Override
  public String getLocation(boolean resolve) throws CoreException
  {
    TargletContainerDescriptorManager manager = TargletContainerDescriptorManager.getInstance();
    TargletContainerDescriptor descriptor = manager.getDescriptor(id, new NullProgressMonitor());
    return descriptor.getInstallLocation().getAbsolutePath();
  }

  @Override
  public String getDigest()
  {
    String environmentProperties = getEnvironmentProperties();
    String nlProperty = getNLProperty();
    return createDigest(id, environmentProperties, nlProperty, targlets, composedTargets);
  }

  @Override
  protected void associateWithTarget(ITargetDefinition target)
  {
    super.associateWithTarget(target);
    targetDefinition = target;
  }

  @Override
  protected TargetBundle[] resolveBundles(ITargetDefinition target, IProgressMonitor monitor) throws CoreException
  {
    Boolean oldForce = FORCE_UPDATE.get();
    boolean force = Boolean.TRUE.equals(oldForce);
    if (!force)
    {
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      for (int i = 2; i < stackTrace.length; i++)
      {
        String className = stackTrace[i].getClassName();
        // System.err.println(className + '.' + stackTrace[i].getMethodName());

        // This is kind of a hack because some of the actions like Update and Reload
        // from the preference dialog and the editor are supposed to clean up the profiles,
        // but for targlets they don't do that so the expected updates don't really happen.
        if (className.equals("org.eclipse.pde.internal.ui.preferences.TargetPlatformPreferencePage") || //$NON-NLS-1$
            className.startsWith("org.eclipse.pde.internal.ui.editor.targetdefinition.TargetEditor$TargetChangedListener") || //$NON-NLS-1$
            className.startsWith("org.eclipse.oomph.ui.internal.pde.model.TargetSnapshot")) //$NON-NLS-1$
        {
          force = true;
          break;
        }
      }
    }

    resolveUnits(force, monitor);
    return fBundles;
  }

  @Override
  protected TargetFeature[] resolveFeatures(ITargetDefinition target, IProgressMonitor monitor) throws CoreException
  {
    // All work has been done in resolveBundles() already.
    return fFeatures;
  }

  private void resolveUnits(boolean force, IProgressMonitor monitor) throws CoreException
  {
    try
    {
      SubMonitor progress = SubMonitor.convert(monitor, 100).detectCancelation();
      TargletContainerDescriptorManager manager = TargletContainerDescriptorManager.getInstance();

      String environmentProperties = getEnvironmentProperties();
      String nlProperty = getNLProperty();
      String digest = createDigest(id, environmentProperties, nlProperty, targlets, composedTargets);

      TargletContainerDescriptor descriptor = manager.getDescriptor(id, progress.newChild(4));
      progress.childDone();

      boolean canResolve = canResolve();
      Profile profile = descriptor.getWorkingProfile();
      if (profile == null || //
          force || //
          !descriptor.getWorkingDigest().equals(digest) && canResolve && descriptor.getUpdateProblem() == null)
      {
        if (!canResolve)
        {
          // Defer resolution to a Job that happens on a background thread later in the lifecyle.
          LoadTargetDefinitionJob.load(targetDefinition);
          throw new CoreException(Status.CANCEL_STATUS);
        }

        Profile newProfile = updateProfile(environmentProperties, nlProperty, digest, progress.newChild(86));
        if (newProfile != null)
        {
          if (profile != null && !profile.getProfileId().equals(newProfile.getProfileId()))
          {
            profile.delete();
          }

          profile = newProfile;
        }
      }
      else
      {
        progress.skipped(86);
      }

      generateUnits(force, canResolve, descriptor, profile, progress.newChild(10));
      progress.done();
    }
    catch (CoreException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      TargletsCorePlugin.INSTANCE.coreException(t);
    }
  }

  private void generateUnits(boolean force, boolean canResolve, TargletContainerDescriptor descriptor, Profile profile, IProgressMonitor monitor)
      throws CoreException
  {
    SubMonitor progress = SubMonitor.convert(monitor, 100).detectCancelation();

    List<TargetBundle> bundles = new ArrayList<>();
    List<TargetFeature> features = new ArrayList<>();

    if (profile != null)
    {
      IQueryResult<IInstallableUnit> result = profile.query(QueryUtil.createIUAnyQuery(), progress.newChild(2));
      generateUnits(descriptor, result.toUnmodifiableSet(), bundles, features, progress.newChild(98));
    }

    for (Targlet targlet : targlets)
    {
      for (DropinLocation dropinLocation : targlet.getDropinLocations())
      {
        analyzeDropinLocation(dropinLocation, bundles, features);
      }
    }

    if (composedTargetContent == null)
    {
      composedTargetContent = new ComposedTargetContent(profile, targetDefinition, composedTargets);
      try
      {
        // Try to load it first, but fail of some bundle or feature is missing.
        // Unless we can't resolve right now in which case we should do a best effort load.
        composedTargetContent.load(!canResolve);
      }
      catch (Exception ex)
      {
        if (canResolve)
        {
          if (TARGLET_DEBUG)
          {
            TargletsCorePlugin.INSTANCE.log("Resolving composed target after load failure"); //$NON-NLS-1$
          }

          // Try to resolve it if we can't just load it.
          IStatus status = composedTargetContent.resolve(progress.newChild(), false);
          try
          {
            TargletsCorePlugin.INSTANCE.coreException(status);
          }
          catch (Exception ex1)
          {
            try
            {
              // If resolve also fails, try to load bundles on a best effort basis.
              // It's better to have missing content than no content.
              if (TARGLET_DEBUG)
              {
                TargletsCorePlugin.INSTANCE.log("Loading composed target after resolve failure"); //$NON-NLS-1$
              }

              composedTargetContent.load(true);
            }
            catch (Exception ex2)
            {
              //$FALL-THROUGH$
            }
          }
        }
      }
    }

    bundles.addAll(composedTargetContent.getBundles());
    features.addAll(composedTargetContent.getFeatures());

    Map<String, TargetBundle> bundleMap = new LinkedHashMap<>();
    for (TargetBundle bundle : bundles)
    {
      BundleInfo bundleInfo = bundle.getBundleInfo();
      bundleMap.putIfAbsent(bundleInfo.getSymbolicName() + '_' + bundleInfo.getVersion(), bundle);
    }

    Map<String, TargetFeature> featureMap = new LinkedHashMap<>();
    for (TargetFeature feature : features)
    {
      featureMap.putIfAbsent(feature.getId() + '_' + feature.getVersion(), feature);
    }

    fBundles = bundleMap.values().toArray(new TargetBundle[bundleMap.size()]);
    fFeatures = featureMap.values().toArray(new TargetFeature[featureMap.size()]);
    progress.done();
  }

  private void generateUnits(TargletContainerDescriptor descriptor, Set<IInstallableUnit> units, List<TargetBundle> bundles, List<TargetFeature> features,
      IProgressMonitor monitor) throws CoreException
  {
    SubMonitor progress = SubMonitor.convert(monitor, units.size()).detectCancelation();
    IFileArtifactRepository cache = descriptor.getBundlePool().getFileArtifactRepository();

    for (IInstallableUnit unit : units)
    {
      if (IU_FILTER_PATTERN != null && IU_FILTER_PATTERN.matcher(unit.getId()).matches())
      {
        continue;
      }

      if (isOSGiBundle(unit))
      {
        generateBundle(unit, cache, bundles);
      }
      else if (isFeatureJar(unit))
      {
        generateFeature(unit, cache, features);
      }

      progress.worked();
    }

    progress.done();
  }

  private void generateBundle(IInstallableUnit unit, IFileArtifactRepository repo, List<TargetBundle> bundles) throws CoreException
  {
    Collection<IArtifactKey> artifacts = unit.getArtifacts();
    for (Iterator<IArtifactKey> it = artifacts.iterator(); it.hasNext();)
    {
      File file = repo.getArtifactFile(it.next());
      if (file != null)
      {
        addTargetBundle(file, bundles);
      }
    }
  }

  private void generateFeature(IInstallableUnit unit, IFileArtifactRepository repo, List<TargetFeature> features) throws CoreException
  {
    Collection<IArtifactKey> artifacts = unit.getArtifacts();
    for (Iterator<IArtifactKey> it = artifacts.iterator(); it.hasNext();)
    {
      File file = repo.getArtifactFile(it.next());
      if (file != null)
      {
        addTargetFeature(file, features);
      }
    }
  }

  private void analyzeDropinLocation(DropinLocation dropinLocation, List<TargetBundle> bundles, List<TargetFeature> features)
  {
    String rootFolder = dropinLocation.getRootFolder();
    if (!StringUtil.isEmpty(rootFolder))
    {
      File folder = new File(rootFolder);
      if (folder.isDirectory())
      {
        analyzeDropinLocation(folder, dropinLocation.isRecursive(), bundles, features);
      }
    }
  }

  private void analyzeDropinLocation(File folder, boolean recursive, List<TargetBundle> bundles, List<TargetFeature> features)
  {
    File[] files = folder.listFiles();
    if (files != null)
    {
      for (File file : files)
      {
        JarFile jarFile = null;

        try
        {
          if (file.isFile())
          {
            if (file.getName().endsWith(".jar")) //$NON-NLS-1$
            {
              jarFile = new JarFile(file);
              if (jarFile.getJarEntry(FeatureGenerator.FEATURE_XML) != null)
              {
                addTargetFeature(file, features);
              }
              else if (jarFile.getJarEntry(JarFile.MANIFEST_NAME) != null)
              {
                addTargetBundle(file, bundles);
              }
            }
          }
          else if (file.isDirectory())
          {
            if (new File(file, FeatureGenerator.FEATURE_XML).isFile())
            {
              addTargetFeature(file, features);
            }
            else if (new File(file, JarFile.MANIFEST_NAME).isFile())
            {
              addTargetBundle(file, bundles);
            }
            else if (recursive)
            {
              analyzeDropinLocation(file, true, bundles, features);
            }
          }
        }
        catch (Exception ex)
        {
          TargletsCorePlugin.INSTANCE.log(ex, IStatus.WARNING);
        }
        finally
        {
          IOUtil.close(jarFile);
        }
      }
    }
  }

  private void addTargetBundle(File file, List<TargetBundle> bundles) throws CoreException
  {
    TargetBundle bundle = new TargetBundle(file);

    if (IU_FILTER_PATTERN != null && IU_FILTER_PATTERN.matcher(bundle.getBundleInfo().getSymbolicName()).matches())
    {
      return;
    }

    bundles.add(bundle);
  }

  private void addTargetFeature(File file, List<TargetFeature> features) throws CoreException
  {
    TargetFeature feature = new TargetFeature(file);

    if (IU_FILTER_PATTERN != null && IU_FILTER_PATTERN.matcher(feature.getId() + Requirement.FEATURE_SUFFIX).matches())
    {
      return;
    }

    features.add(feature);
  }

  @Override
  public void forceUpdate(boolean activateTargetDefinition, boolean mirrors, IProgressMonitor monitor) throws CoreException
  {
    try
    {
      FORCE_UPDATE.set(Boolean.TRUE);
      MIRRORS.set(mirrors ? Boolean.TRUE : false);

      // Clear the resolution statuses of the involved targlet containers.
      ITargetLocation[] targetLocations = targetDefinition.getTargetLocations();
      if (targetLocations != null)
      {
        for (ITargetLocation targetLocation : targetLocations)
        {
          if (targetLocation instanceof ITargletContainer)
          {
            TargletContainer targletContainer = (TargletContainer)targetLocation;
            targletContainer.clearResolutionStatus();
          }
        }
      }

      if (activateTargetDefinition || TargetPlatformUtil.isActiveTargetDefinition(targetDefinition))
      {
        // If the target definition is currently active then use PDE's LoadTargetDefinitionJob so that the WorkspaceIUImporter gets triggered.
        TargetPlatformUtil.activateTargetDefinition(targetDefinition, monitor);
      }
      else
      {
        // Otherwise just update the profile and resolve.
        targetDefinition.resolve(monitor);
      }

      TargletContainerDescriptorManager manager = TargletContainerDescriptorManager.getInstance();
      TargletContainerDescriptor descriptor = manager.getDescriptor(id, monitor);

      UpdateProblem updateProblem = descriptor.getUpdateProblem();
      if (updateProblem != null)
      {
        TargletsCorePlugin.INSTANCE.coreException(new CoreException(updateProblem));
      }
    }
    finally
    {
      MIRRORS.remove();
      FORCE_UPDATE.remove();
    }
  }

  @Override
  public IStatus updateProfile(IProgressMonitor monitor)
  {
    try
    {
      String environmentProperties = getEnvironmentProperties();
      String nlProperty = getNLProperty();
      String digest = createDigest(id, environmentProperties, nlProperty, targlets, composedTargets);

      updateProfile(environmentProperties, nlProperty, digest, monitor);
      return Status.OK_STATUS;
    }
    catch (CoreException ex)
    {
      return ex.getStatus();
    }
  }

  private Profile updateProfile(String environmentProperties, String nlProperty, String digest, IProgressMonitor monitor) throws CoreException
  {
    SubMonitor progress = SubMonitor.convert(monitor, 100).detectCancelation();

    TargletContainerDescriptorManager manager = TargletContainerDescriptorManager.getInstance();
    TargletContainerDescriptor descriptor = manager.getDescriptor(id, progress.newChild());
    descriptor.resetUpdateProblem();

    final Profile profile = descriptor.startUpdateTransaction(environmentProperties, nlProperty, digest, progress.newChild());

    ProfileTransaction transaction = profile.change().setRemoveExistingInstallableUnits(true);
    transaction.setMirrors(MIRRORS.get() == Boolean.TRUE);

    IProvisioningAgent provisioningAgent = profile.getAgent().getProvisioningAgent();
    CacheUsageConfirmer cacheUsageConfirmer = TargletsCorePlugin.INSTANCE.getCacheUsageConfirmer();
    CacheUsageConfirmer oldCacheUsageConfirmer = (CacheUsageConfirmer)provisioningAgent.getService(CacheUsageConfirmer.SERVICE_NAME);

    IEclipsePreferences garbageCollectorPreferences = ConfigurationScope.INSTANCE.getNode("org.eclipse.equinox.p2.garbagecollector"); //$NON-NLS-1$
    String oldGCEnabled = garbageCollectorPreferences.get("gc_enabled", null); //$NON-NLS-1$
    garbageCollectorPreferences.putBoolean("gc_enabled", false); //$NON-NLS-1$

    boolean originalBetterMirrorSelection = CachingRepositoryManager.enableBetterMirrorSelection();

    MultiStatus composedTargetContentStatus = null;
    try
    {
      composedTargetContent = new ComposedTargetContent(profile, targetDefinition, composedTargets);
      composedTargetContentStatus = composedTargetContent.resolve(progress.newChild(composedTargets.size(), SubMonitor.SUPPRESS_BEGINTASK), true);

      if (cacheUsageConfirmer != null)
      {
        provisioningAgent.registerService(CacheUsageConfirmer.SERVICE_NAME, cacheUsageConfirmer);
      }

      ProfileDefinition profileDefinition = transaction.getProfileDefinition();
      profileDefinition.setIncludeSourceBundles(isIncludeSources());

      final EList<Requirement> rootRequirements = profileDefinition.getRequirements();
      rootRequirements.clear();

      EList<Repository> repositories = profileDefinition.getRepositories();
      repositories.clear();

      String profileProperties = getProfileProperties();
      profileDefinition.setProfileProperties(StringUtil.isEmpty(profileProperties) ? null : profileProperties);

      WorkspaceIUAnalyzer workspaceIUAnalyzer = analyzeWorkspaceIUs(rootRequirements, repositories, progress);

      if (rootRequirements.isEmpty())
      {
        descriptor.rollbackUpdateTransaction(null, new NullProgressMonitor());
        return null;
      }

      TargletCommitContext commitContext = new TargletCommitContext(profile, workspaceIUAnalyzer, isIncludeAllPlatforms(), isIncludeAllRequirements(),
          isIncludeBinaryEquivalents(), composedTargetContent.getAdditionalIUs());
      transaction.commit(commitContext, progress.newChild());

      TargletsCorePlugin.INSTANCE.coreException(composedTargetContentStatus);

      composedTargetContent.save();

      Map<IInstallableUnit, WorkspaceIUInfo> requiredProjects = getRequiredProjects(profile, workspaceIUAnalyzer.getWorkspaceIUInfos(), progress.newChild());
      descriptor.commitUpdateTransaction(digest, requiredProjects.values(), progress.newChild());

      TargletContainerEvent event = new TargletContainerEvent.ProfileUpdateSucceededEvent(this, descriptor, profile, commitContext.getArtificialRoot(),
          commitContext.getMetadataRepositories(), commitContext.getProvisioningPlan(), requiredProjects);
      TargletContainerListenerRegistry.INSTANCE.notifyListeners(event, progress.newChild());

      monitor.subTask(Messages.TargletContainer_UpdateComplete_task);
    }
    catch (Throwable t)
    {
      if (composedTargetContentStatus != null && !composedTargetContentStatus.isOK() && t instanceof CoreException
          && ((CoreException)t).getStatus() != composedTargetContentStatus)
      {
        composedTargetContentStatus.add(((CoreException)t).getStatus());
        t = new CoreException(composedTargetContentStatus);
      }

      descriptor.rollbackUpdateTransaction(t, new NullProgressMonitor());

      try
      {
        composedTargetContent = new ComposedTargetContent(descriptor.getWorkingProfile(), targetDefinition, composedTargets);
        composedTargetContent.load(true);
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }

      UpdateProblem updateProblem = descriptor.getUpdateProblem();
      if (updateProblem != null)
      {
        TargletContainerEvent event = new TargletContainerEvent.ProfileUpdateFailedEvent(this, descriptor, updateProblem);
        TargletContainerListenerRegistry.INSTANCE.notifyListeners(event, new NullProgressMonitor());
      }

      TargletsCorePlugin.INSTANCE.coreException(t);
    }
    finally
    {
      CachingRepositoryManager.setBetterMirrorSelection(originalBetterMirrorSelection);

      if (oldGCEnabled == null)
      {
        garbageCollectorPreferences.remove("gc_enabled"); //$NON-NLS-1$
      }
      else
      {
        garbageCollectorPreferences.put("gc_enabled", oldGCEnabled); //$NON-NLS-1$
      }

      if (cacheUsageConfirmer != null && oldCacheUsageConfirmer != null)
      {
        provisioningAgent.registerService(CacheUsageConfirmer.SERVICE_NAME, oldCacheUsageConfirmer);
      }
    }

    progress.done();
    return profile;
  }

  private WorkspaceIUAnalyzer analyzeWorkspaceIUs(final EList<Requirement> rootRequirements, EList<Repository> repositories, SubMonitor progress)
      throws CoreException
  {
    WorkspaceIUAnalyzer workspaceIUAnalyzer = new WorkspaceIUAnalyzer();

    for (Targlet targlet : targlets)
    {
      EList<IUGenerator> effectiveIUGenerators = effectiveIUGenerators(targlet);

      EList<IInstallableUnit> ius = new BasicEList<>();
      for (SourceLocator sourceLocator : targlet.getSourceLocators())
      {
        ius.addAll(workspaceIUAnalyzer.analyze(sourceLocator, effectiveIUGenerators, progress.newChild()));
      }

      for (Requirement requirement : EcoreUtil.copyAll(targlet.getRequirements()))
      {
        String namespace = requirement.getNamespace();
        String name = requirement.getName();

        // Ignore bogus requirements.
        if (StringUtil.isEmpty(namespace) || StringUtil.isEmpty(name) || requirement.getVersionRange() == null)
        {
          continue;
        }

        // If this is a wildcard requirement, we want to expand it to all the IUs in the targlet's source locator.
        if ("*".equals(name) && IInstallableUnit.NAMESPACE_IU_ID.equals(namespace)) //$NON-NLS-1$
        {
          // Preprocess all the IUs to build a map from workspace IU Info to filter.
          // This ensures that a requirement on a plain.project will be filtered appropriately.
          Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = workspaceIUAnalyzer.getWorkspaceIUInfos();
          Map<WorkspaceIUInfo, IMatchExpression<IInstallableUnit>> filters = new HashMap<>();
          for (IInstallableUnit iu : ius)
          {
            IMatchExpression<IInstallableUnit> filter = iu.getFilter();
            if (filter != null)
            {
              WorkspaceIUInfo workspaceIUInfo = workspaceIUInfos.get(iu);
              filters.put(workspaceIUInfo, filter);
            }
          }

          for (IInstallableUnit iu : ius)
          {
            // Ignore categories.
            if ("true".equalsIgnoreCase(iu.getProperty("org.eclipse.equinox.p2.type.category"))) //$NON-NLS-1$ //$NON-NLS-2$
            {
              continue;
            }

            Requirement expandedRequirement = P2Factory.eINSTANCE.createRequirement(iu.getId(), requirement.getVersionRange(), requirement.isOptional(),
                requirement.isGreedy());

            // If there is a filter associated with the IU, set it as the filter of the requirement.
            IMatchExpression<IInstallableUnit> filter = filters.get(workspaceIUInfos.get(iu));
            if (filter != null)
            {
              expandedRequirement.setMatchExpression(filter);
            }

            rootRequirements.add(expandedRequirement);
          }

          continue;
        }

        rootRequirements.add(requirement);
      }

      for (Repository repository : EcoreUtil.copyAll(targlet.getActiveRepositories()))
      {
        if (!StringUtil.isEmpty(repository.getURL()))
        {
          repositories.add(repository);
        }
      }
    }

    workspaceIUAnalyzer.adjustOmniRootRequirements(rootRequirements);

    IStatus status = workspaceIUAnalyzer.getStatus();
    TargletsCorePlugin.INSTANCE.coreException(status);

    return workspaceIUAnalyzer;
  }

  private static EList<IUGenerator> effectiveIUGenerators(Targlet targlet)
  {
    EList<IUGenerator> effectiveInstallableUnitGenerators = targlet.getInstallableUnitGenerators();
    if (effectiveInstallableUnitGenerators.isEmpty())
    {
      effectiveInstallableUnitGenerators = IUGenerator.DEFAULTS;
    }

    return effectiveInstallableUnitGenerators;
  }

  private static String createDigest(String id, String environmentProperties, String nlProperty, EList<Targlet> targlets, EList<String> composedTargets)
  {
    InputStream stream = null;

    try
    {
      Writer writer = Persistence.toXML(id, targlets, composedTargets);
      writer.write("\n<!-- Environment Properties: "); //$NON-NLS-1$
      writer.write(environmentProperties);
      writer.write(" -->"); //$NON-NLS-1$
      writer.write("\n<!-- NL Property: "); //$NON-NLS-1$
      writer.write(nlProperty);
      writer.write(" -->\n"); //$NON-NLS-1$
      if (!composedTargets.isEmpty())
      {
        try
        {
          TransformerFactory transformerFactory = TransformerFactory.newInstance();
          Transformer transformer = transformerFactory.newTransformer();
          for (ITargetDefinition targetDefinition : TargetPlatformUtil.getTargetDefinitions(new NullProgressMonitor()))
          {
            if (composedTargets.contains(targetDefinition.getName()))
            {
              Document document = targetDefinition.getDocument();
              transformer.transform(new DOMSource(document), new StreamResult(writer));
            }
          }
        }
        catch (Exception ex)
        {
          TargletsCorePlugin.INSTANCE.log(ex);
        }
      }

      final MessageDigest digest = MessageDigest.getInstance("SHA-1"); //$NON-NLS-1$
      stream = new FilterInputStream(new ByteArrayInputStream(writer.toString().getBytes("UTF-8"))) //$NON-NLS-1$
      {
        @Override
        public int read() throws IOException
        {
          for (;;)
          {
            int ch = super.read();
            switch (ch)
            {
              case -1:
                return -1;

              case 10:
              case 13:
                continue;
            }

            digest.update((byte)ch);
            return ch;
          }
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException
        {
          int read = super.read(b, off, len);
          if (read == -1)
          {
            return -1;
          }

          for (int i = off; i < off + read; i++)
          {
            byte c = b[i];
            if (c == 10 || c == 13)
            {
              if (i + 1 < off + read)
              {
                System.arraycopy(b, i + 1, b, i, read - i - 1);
                --i;
              }

              --read;
            }
          }

          digest.update(b, off, read);
          return read;
        }
      };

      synchronized (BUFFER)
      {
        while (stream.read(BUFFER) != -1)
        {
          // Do nothing
        }
      }

      return HexUtil.bytesToHex(digest.digest());
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
    finally
    {
      IOUtil.close(stream);
    }
  }

  private static boolean isOSGiBundle(IInstallableUnit unit)
  {
    return providesNamespace(unit, "osgi.bundle"); //$NON-NLS-1$
  }

  private static boolean isFeatureJar(IInstallableUnit unit)
  {
    return providesNamespace(unit, "org.eclipse.update.feature"); //$NON-NLS-1$
  }

  private static boolean canResolve()
  {
    boolean isJobManagerSuspended = Job.getJobManager().isSuspended();
    if (isJobManagerSuspended)
    {
      // We can't expect scheduled jobs to run, and we need that to load repositories.
      // So we can't resolve in this case.
      return false;
    }

    try
    {
      Class<?> displayClass = CommonPlugin.loadClass("org.eclipse.swt", "org.eclipse.swt.widgets.Display"); //$NON-NLS-1$ //$NON-NLS-2$
      Method getCurrentMethod = ReflectUtil.getMethod(displayClass, "getCurrent"); //$NON-NLS-1$
      boolean isDisplayThread = ReflectUtil.invokeMethod(getCurrentMethod, null) != null;
      if (isDisplayThread)
      {
        // We should never do a resolve in the display thread.
        // The UI will hang for a long time, and it won't be possible to cancel progress.
        // So we can't resolve in this case.
        return false;
      }

      Class<?> platformUIClass = CommonPlugin.loadClass("org.eclipse.ui.workbench", "org.eclipse.ui.PlatformUI"); //$NON-NLS-1$ //$NON-NLS-2$
      Object workbench = ReflectUtil.invokeMethod("getWorkbench", platformUIClass); //$NON-NLS-1$
      boolean isWorkbenchStarting = (Boolean)ReflectUtil.invokeMethod("isStarting", workbench); //$NON-NLS-1$
      if (isWorkbenchStarting)
      {
        // In Oxygen, the new Java indexer starts very early.
        // At this point the job manager will be suspended, or is is about to be suspended because the workbench is starting its life cycle.
        // So we can't resolve in this case.
        return false;
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    // All is good and we can resolve now on this thread.
    return true;
  }

  private static boolean providesNamespace(IInstallableUnit unit, String namespace)
  {
    for (IProvidedCapability providedCapability : unit.getProvidedCapabilities())
    {
      if (namespace.equals(providedCapability.getNamespace()))
      {
        return true;
      }
    }

    return false;
  }

  private static Map<IInstallableUnit, WorkspaceIUInfo> getRequiredProjects(IProfile profile, Map<IInstallableUnit, WorkspaceIUInfo> allProjects,
      IProgressMonitor monitor)
  {
    Map<WorkspaceIUInfo, IInstallableUnit> mainIUs = new HashMap<>();
    for (Map.Entry<IInstallableUnit, WorkspaceIUInfo> entry : allProjects.entrySet())
    {
      IInstallableUnit iu = entry.getKey();
      if ("true".equals(iu.getProperty(WorkspaceIUAnalyzer.IU_PROPERTY_WORKSPACE_MAIN))) //$NON-NLS-1$
      {
        mainIUs.put(entry.getValue(), iu);
      }
    }

    Map<IInstallableUnit, WorkspaceIUInfo> result = new HashMap<>();
    for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUAnyQuery(), monitor)))
    {
      if ("true".equals(iu.getProperty(TargletContainer.IU_PROPERTY_SOURCE))) //$NON-NLS-1$
      {
        continue;
      }

      WorkspaceIUInfo info = allProjects.get(iu);
      if (info != null)
      {
        IInstallableUnit mainIU = mainIUs.get(info);
        if (mainIU != null)
        {
          result.put(mainIU, info);
        }
      }
    }

    return result;
  }

  private static final class ComposedTargetContent
  {
    // The same folder as org.eclipse.equinox.internal.p2.engine.SimpleProfileRegistry.DATA_EXT
    private static final String DATA = ".data"; //$NON-NLS-1$

    private static final String COMPOSED_BUNDLES = "composed-bundles.txt"; //$NON-NLS-1$

    private static final String COMPOSED_FEATURES = "composed-features.txt"; //$NON-NLS-1$

    private final Profile profile;

    private final List<String> composedTargets;

    private final ITargetDefinition targetDefinition;

    private final Set<IInstallableUnit> additionalIUs = new LinkedHashSet<>();

    private final List<TargetBundle> bundles = new ArrayList<>();

    private final List<TargetFeature> features = new ArrayList<>();

    public ComposedTargetContent(Profile profile, ITargetDefinition targetDefinition, List<String> composedTargets)
    {
      this.profile = profile;
      this.targetDefinition = targetDefinition;
      this.composedTargets = composedTargets;
    }

    public MultiStatus resolve(SubMonitor progress, boolean publishIUs)
    {
      clear();

      Set<File> bundleFiles = new LinkedHashSet<>();
      Set<File> featureFiles = new LinkedHashSet<>();

      MultiStatus rootStatus = new MultiStatus(TargletsCorePlugin.INSTANCE.getSymbolicName(), 0, Messages.TargletContainer_ResolutionProblems, null);

      for (String composedTarget : composedTargets)
      {
        MultiStatus childStatus = new MultiStatus(TargletsCorePlugin.INSTANCE.getSymbolicName(), 0,
            NLS.bind(Messages.TargletContainer_ComposedTargetResolutionProblem, composedTarget), null);

        if (TARGLET_DEBUG)
        {
          TargletsCorePlugin.INSTANCE.log("Resolving composed target " + composedTarget); //$NON-NLS-1$
        }

        ITargetDefinition targetDefinition = TargetPlatformUtil.getTargetDefinition(composedTarget); // $NON-NLS-1$
        if (targetDefinition == null)
        {
          childStatus.add(
              new Status(IStatus.ERROR, TargletsCorePlugin.INSTANCE.getSymbolicName(), NLS.bind(Messages.TargletContainer_NoTargetDefinition, composedTarget)));
          rootStatus.add(childStatus);
          continue;
        }

        if (targetDefinition.getHandle().equals(this.targetDefinition.getHandle()))
        {
          childStatus.add(new Status(IStatus.ERROR, TargletsCorePlugin.INSTANCE.getSymbolicName(),
              NLS.bind(Messages.TargletContainer_InvalidSelfReference, composedTarget)));
          rootStatus.add(childStatus);
          continue;
        }

        IStatus resolve = targetDefinition.resolve(progress.newChild());
        if (!resolve.isOK())
        {
          P2TargetUtils.forceCheckTarget(targetDefinition);
          try
          {
            P2TargetUtils.deleteProfile(targetDefinition.getHandle());
          }
          catch (CoreException ex)
          {
            TargletsCorePlugin.INSTANCE.log(ex);
          }

          resolve = targetDefinition.resolve(progress.newChild());
        }

        if (!resolve.isOK())
        {
          childStatus.add(resolve);
          rootStatus.add(childStatus);
          continue;
        }

        TargetBundle[] allBundles = targetDefinition.getAllBundles();
        bundles.addAll(Arrays.asList(allBundles));

        TargetFeature[] allFeatures = targetDefinition.getAllFeatures();
        features.addAll(Arrays.asList(allFeatures));

        if (publishIUs)
        {
          for (TargetBundle targetBundle : allBundles)
          {
            try
            {
              bundleFiles.add(new File(targetBundle.getBundleInfo().getLocation()));
            }
            catch (Exception ex)
            {
              childStatus.add(TargletsCorePlugin.INSTANCE.getStatus(ex, IStatus.ERROR));
            }
          }

          for (TargetFeature targetFeature : allFeatures)
          {
            try
            {
              featureFiles.add(new File(targetFeature.getLocation()));
            }
            catch (Exception ex)
            {
              childStatus.add(TargletsCorePlugin.INSTANCE.getStatus(ex, IStatus.ERROR));
            }
          }
        }

        if (!childStatus.isOK())
        {
          rootStatus.add(childStatus);
        }
      }

      if (publishIUs)
      {
        PublisherInfo publisherInfo = new PublisherInfo();
        PublisherResult publisherResult = new PublisherResult();
        if (!bundleFiles.isEmpty())
        {
          BundlesAction bundlesAction = new BundlesAction(bundleFiles.toArray(File[]::new));
          IStatus status = bundlesAction.perform(publisherInfo, publisherResult, progress.newChild());
          if (!status.isOK())
          {
            rootStatus.add(status);
          }
        }

        if (!featureFiles.isEmpty())
        {
          FeaturesAction featuresAction = new FeaturesAction(featureFiles.toArray(File[]::new));
          IStatus status = featuresAction.perform(publisherInfo, publisherResult, progress.newChild());
          if (!status.isOK())
          {
            rootStatus.add(status);
          }
        }

        IArtifactKey[] NONE = new IArtifactKey[0];
        for (Iterator<IInstallableUnit> everything = publisherResult.everything(); everything.hasNext();)
        {
          InstallableUnit iu = (InstallableUnit)everything.next();
          iu.setArtifacts(NONE);
          iu.setProperty(ProfileTransactionImpl.EXCLUDE_IU_PROPERTY, "true"); //$NON-NLS-1$
          additionalIUs.add(iu);
        }
      }

      return rootStatus;
    }

    public Set<IInstallableUnit> getAdditionalIUs()
    {
      return additionalIUs;
    }

    public List<TargetBundle> getBundles()
    {
      return bundles;
    }

    public List<TargetFeature> getFeatures()
    {
      return features;
    }

    public void load(boolean bestEffort) throws IOException, CoreException
    {
      clear();

      Path dataFolder = getDataFolder();
      if (TARGLET_DEBUG)
      {
        TargletsCorePlugin.INSTANCE.log("Loading from " + dataFolder); //$NON-NLS-1$
      }

      Path composedBundles = dataFolder.resolve(COMPOSED_BUNDLES);
      if (Files.exists(composedBundles))
      {
        List<String> bundleLocations = Files.readAllLines(composedBundles);
        for (String bundleLocation : bundleLocations)
        {
          try
          {
            bundles.add(new TargetBundle(new File(bundleLocation)));
          }
          catch (CoreException ex)
          {
            if (!bestEffort)
            {
              throw ex;
            }
          }
        }
      }

      if (TARGLET_DEBUG)
      {
        TargletsCorePlugin.INSTANCE.log("Loaded " + bundles.size() + " bundles"); //$NON-NLS-1$ //$NON-NLS-2$
      }

      Path composedFeatures = dataFolder.resolve(COMPOSED_FEATURES);
      if (Files.exists(composedFeatures))
      {
        List<String> featureLocations = Files.readAllLines(composedFeatures);
        for (String featureLocation : featureLocations)
        {
          try
          {
            features.add(new TargetFeature(new File(featureLocation)));
          }
          catch (CoreException ex)
          {
            if (!bestEffort)
            {
              throw ex;
            }
          }
        }
      }

      if (TARGLET_DEBUG)
      {
        TargletsCorePlugin.INSTANCE.log("Loaded " + features.size() + " features"); //$NON-NLS-1$ //$NON-NLS-2$
      }
    }

    public void save() throws IOException
    {
      Path dataFolder = getDataFolder();
      if (TARGLET_DEBUG)
      {
        TargletsCorePlugin.INSTANCE.log("Saving to " + dataFolder); //$NON-NLS-1$
      }

      Set<String> bundleLocations = new TreeSet<>();
      for (TargetBundle targetBundle : bundles)
      {
        BundleInfo bundleInfo = targetBundle.getBundleInfo();
        try
        {
          File file = new File(bundleInfo.getLocation());
          bundleLocations.add(file.toString());
        }
        catch (RuntimeException ex)
        {
          //$FALL-THROUGH$
        }
      }

      Files.write(dataFolder.resolve(COMPOSED_BUNDLES), bundleLocations);
      if (TARGLET_DEBUG)
      {
        TargletsCorePlugin.INSTANCE.log("Saved " + bundleLocations.size() + " bundles"); //$NON-NLS-1$ //$NON-NLS-2$
      }

      Set<String> featureLocations = new TreeSet<String>();
      for (TargetFeature targetFeature : features)
      {
        String location = targetFeature.getLocation();
        try
        {
          featureLocations.add(new File(location).toString());
        }
        catch (RuntimeException ex)
        {
          //$FALL-THROUGH$
        }
      }

      Files.write(dataFolder.resolve(COMPOSED_FEATURES), featureLocations);
      if (TARGLET_DEBUG)
      {
        TargletsCorePlugin.INSTANCE.log("Saved " + featureLocations.size() + " features"); //$NON-NLS-1$ //$NON-NLS-2$
      }
    }

    private void clear()
    {
      additionalIUs.clear();
      features.clear();
      bundles.clear();
    }

    private Path getDataFolder() throws IOException
    {
      Path dataFolder = new File(profile.getLocation(), DATA).toPath();
      Files.createDirectories(dataFolder);
      return dataFolder; // $NON-NLS-1$
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class TargletCommitContext extends CommitContext
  {
    private static final String NATIVE_ARTIFACTS = "nativeArtifacts"; //$NON-NLS-1$

    private final Profile profile;

    private final WorkspaceIUAnalyzer workspaceIUAnalyzer;

    private IProvisioningPlan provisioningPlan;

    private IInstallableUnit artificialRoot;

    private List<IMetadataRepository> metadataRepositories;

    private boolean isIncludeAllPlatforms;

    private boolean isIncludeAllRequirements;

    private boolean isIncludeBinaryEquivalents;

    private Set<IInstallableUnit> additionalIUs;

    public TargletCommitContext(Profile profile, WorkspaceIUAnalyzer workspaceIUAnalyzer, boolean isIncludeAllPlatforms, boolean isIncludeAllRequirements,
        boolean isIncludeBinaryEquivalents, Set<IInstallableUnit> additionalIUs)
    {
      this.profile = profile;
      this.workspaceIUAnalyzer = workspaceIUAnalyzer;
      this.isIncludeAllPlatforms = isIncludeAllPlatforms;
      this.isIncludeAllRequirements = isIncludeAllRequirements;
      this.isIncludeBinaryEquivalents = isIncludeBinaryEquivalents;
      this.additionalIUs = additionalIUs;
    }

    public IProvisioningPlan getProvisioningPlan()
    {
      return provisioningPlan;
    }

    public final IInstallableUnit getArtificialRoot()
    {
      return artificialRoot;
    }

    public List<IMetadataRepository> getMetadataRepositories()
    {
      return metadataRepositories;
    }

    @Override
    public ProvisioningContext createProvisioningContext(ProfileTransaction transaction, final IProfileChangeRequest profileChangeRequest) throws CoreException
    {
      IProvisioningAgent provisioningAgent = transaction.getProfile().getAgent().getProvisioningAgent();
      ProvisioningContext provisioningContext = new ProvisioningContext(provisioningAgent)
      {
        private CollectionResult<IInstallableUnit> metadata;

        @Override
        public IQueryable<IInstallableUnit> getMetadata(IProgressMonitor monitor)
        {
          if (metadata == null)
          {
            Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = workspaceIUAnalyzer.getWorkspaceIUInfos();
            Map<String, Version> workspaceIUVersions = workspaceIUAnalyzer.getIUVersions();

            Set<IInstallableUnit> originalWorkspaceIUs = new HashSet<>(workspaceIUInfos.keySet());

            Set<IInstallableUnit> ius = new LinkedHashSet<>();
            Map<IU, IInstallableUnit> idToIUMap = new HashMap<>();
            generateWorkspaceSourceIUs(ius, workspaceIUVersions, idToIUMap, monitor);

            ius.add(createPDETargetPlatformIU());

            IQueryResult<IInstallableUnit> metadataResult = super.getMetadata(monitor).query(QueryUtil.createIUAnyQuery(), monitor);
            Set<IInstallableUnit> metadataIUs = new LinkedHashSet<>();
            for (IInstallableUnit iu : P2Util.asIterable(metadataResult))
            {
              metadataIUs.add(iu);
            }

            metadataIUs.addAll(additionalIUs);

            for (IInstallableUnit iu : metadataIUs)
            {
              TargletsCorePlugin.checkCancelation(monitor);

              ius.add(P2Util.createGeneralizedIU(iu, isIncludeAllPlatforms, isIncludeAllRequirements, true));

              if (isIncludeBinaryEquivalents)
              {
                // If the binary IU corresponds to a synthetic source IU...
                IInstallableUnit workspaceIU = idToIUMap.get(new IU(iu));
                if (workspaceIU == null)
                {
                  workspaceIU = idToIUMap.get(new IU(iu.getId(), Version.emptyVersion));
                }

                if (workspaceIU != null)
                {
                  // Ensure that if this binary IU is resolved that the corresponding source file is imported in the workspace.
                  WorkspaceIUInfo info = workspaceIUInfos.get(workspaceIU);
                  workspaceIUInfos.put(iu, info);

                  // We can remove our synthetic IU to ensure that, whenever possible, a binary resolution for it is included in the TP.
                  // That's only necessary if the IU is a singleton.
                  if (workspaceIU.isSingleton() || "true".equals(workspaceIU.getProperty(InstallableUnitDescription.PROP_TYPE_GROUP))) //$NON-NLS-1$
                  {
                    ius.remove(workspaceIU);
                  }

                  // If the workspaceIU has any requirements not in the binary IU, then include those.
                  List<IRequirement> extraRequirements = new ArrayList<>();
                  LOOP: for (IRequirement workspaceRequirement : workspaceIU.getRequirements())
                  {
                    if (P2Util.isSimpleRequiredCapability(workspaceRequirement))
                    {
                      IRequiredCapability workspaceRequiredCapability = (IRequiredCapability)workspaceRequirement;
                      String namespace = workspaceRequiredCapability.getNamespace();
                      String name = workspaceRequiredCapability.getName();
                      for (IRequirement requirement : iu.getRequirements())
                      {
                        if (P2Util.isSimpleRequiredCapability(requirement))
                        {
                          IRequiredCapability requiredCapability = (IRequiredCapability)requirement;
                          if (namespace.equals(requiredCapability.getNamespace()) && name.equals(requiredCapability.getName()))
                          {
                            // It's already included, perhaps with a different version range, but we'll ignore it.
                            continue LOOP;
                          }
                        }
                      }

                      // If it's an IU or bundle requirement...
                      if (IInstallableUnit.NAMESPACE_IU_ID.equals(namespace) || BundlesAction.CAPABILITY_NS_OSGI_BUNDLE.equals(namespace))
                      {
                        // If it resolves to a workspace IU that's a singleton, generalize the requirement to include any version of that IU, because resolving
                        // to any version will result in the import of the project.
                        IInstallableUnit requiredWorkspaceIU = idToIUMap.get(new IU(name, Version.emptyVersion));
                        if (requiredWorkspaceIU != null && (requiredWorkspaceIU.isSingleton()
                            || "true".equals(requiredWorkspaceIU.getProperty(InstallableUnitDescription.PROP_TYPE_GROUP)))) //$NON-NLS-1$
                        {
                          extraRequirements
                              .add(MetadataFactory.createRequirement(namespace, name, VersionRange.emptyRange, workspaceRequiredCapability.getFilter(),
                                  workspaceRequiredCapability.getMin(), workspaceRequiredCapability.getMax(), workspaceRequiredCapability.isGreedy()));
                          continue LOOP;
                        }
                      }

                      extraRequirements.add(workspaceRequirement);
                    }
                    else
                    {
                      if (workspaceRequirement.getMin() > 0)
                      {
                        try
                        {
                          // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=532569 for why we relax this requirement.
                          extraRequirements.add(MetadataFactory.createRequirement(workspaceRequirement.getMatches(), workspaceRequirement.getFilter(), 0,
                              workspaceRequirement.getMax(), true));
                        }
                        catch (RuntimeException exception)
                        {
                          //$FALL-THROUGH$
                        }
                      }
                      else
                      {
                        extraRequirements.add(workspaceRequirement);
                      }
                    }
                  }

                  // If there this workspace IU has a license...
                  String licenseFeatureID = workspaceIU.getProperty(FeatureGenerator.PROP_REQUIRED_LICENCSE_FEATURE_ID);
                  if (licenseFeatureID != null)
                  {
                    // Keep a requirement for this IU because binary IUs are generally not installed for license feature dependencies.
                    VersionRange versionRange = new VersionRange(workspaceIU.getProperty(FeatureGenerator.PROP_REQUIRED_LICENCSE_FEATURE_VERSION_RANGE));
                    IRequirement requirement = MetadataFactory.createRequirement(IInstallableUnit.NAMESPACE_IU_ID, licenseFeatureID, versionRange, null, false,
                        false);
                    extraRequirements.add(requirement);
                  }

                  if (!extraRequirements.isEmpty() && iu instanceof InstallableUnit)
                  {
                    InstallableUnit binaryIU = (InstallableUnit)iu;
                    extraRequirements.addAll(0, binaryIU.getRequirements());
                    binaryIU.setRequiredCapabilities(extraRequirements.toArray(new IRequirement[extraRequirements.size()]));
                  }
                }
              }
            }

            Set<IInstallableUnit> remainingWorkspaceIUs = new HashSet<>(ius);
            remainingWorkspaceIUs.retainAll(originalWorkspaceIUs);

            for (IInstallableUnit iu : remainingWorkspaceIUs)
            {
              Collection<IRequirement> requirements = iu.getRequirements();
              int size = requirements.size();
              IRequirement[] generalizedRequirements = requirements.toArray(new IRequirement[size]);
              boolean needsGeneralization = false;
              for (int i = 0; i < size; ++i)
              {
                IRequirement workspaceRequirement = generalizedRequirements[i];
                if (P2Util.isSimpleRequiredCapability(workspaceRequirement))
                {
                  IRequiredCapability workspaceRequiredCapability = (IRequiredCapability)workspaceRequirement;
                  VersionRange versionRange = workspaceRequiredCapability.getRange();
                  if (!VersionRange.emptyRange.equals(versionRange))
                  {
                    String namespace = workspaceRequiredCapability.getNamespace();
                    String name = workspaceRequiredCapability.getName();
                    if (IInstallableUnit.NAMESPACE_IU_ID.equals(namespace) || BundlesAction.CAPABILITY_NS_OSGI_BUNDLE.equals(namespace))
                    {
                      IInstallableUnit requiredWorkspaceIU = idToIUMap.get(new IU(name, Version.emptyVersion));
                      if (requiredWorkspaceIU != null && !ius.contains(requiredWorkspaceIU)
                          && (requiredWorkspaceIU.isSingleton() || "true".equals(requiredWorkspaceIU.getProperty(InstallableUnitDescription.PROP_TYPE_GROUP)))) //$NON-NLS-1$
                      {
                        needsGeneralization = true;
                        generalizedRequirements[i] = MetadataFactory.createRequirement(namespace, name, VersionRange.emptyRange,
                            workspaceRequiredCapability.getFilter(), workspaceRequiredCapability.getMin(), workspaceRequiredCapability.getMax(),
                            workspaceRequiredCapability.isGreedy());
                      }
                    }
                  }
                }
              }

              if (needsGeneralization)
              {
                ((InstallableUnit)iu).setRequiredCapabilities(generalizedRequirements);
              }
            }

            metadata = new CollectionResult<>(ius);
          }

          return metadata;
        }

        private void generateWorkspaceSourceIUs(Set<IInstallableUnit> ius, Map<String, Version> ids, Map<IU, IInstallableUnit> idToIUMap,
            IProgressMonitor monitor)
        {
          Map<IInstallableUnit, WorkspaceIUInfo> workspaceSourceIUInfos = new HashMap<>();
          Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = workspaceIUAnalyzer.getWorkspaceIUInfos();

          for (IInstallableUnit iu : workspaceIUInfos.keySet())
          {
            TargletsCorePlugin.checkCancelation(monitor);

            String id = iu.getId();
            ius.add(P2Util.createGeneralizedIU(iu, isIncludeAllPlatforms, isIncludeAllRequirements, true));
            idToIUMap.put(new IU(iu), iu);

            if (id.endsWith(Requirement.PROJECT_SUFFIX))
            {
              continue;
            }

            String suffix = ""; //$NON-NLS-1$
            if (id.endsWith(Requirement.FEATURE_SUFFIX))
            {
              id = id.substring(0, id.length() - Requirement.FEATURE_SUFFIX.length());
              suffix = Requirement.FEATURE_SUFFIX;
            }

            InstallableUnitDescription description = new MetadataFactory.InstallableUnitDescription();
            String workspaceSourceID = id + ".source" + suffix; //$NON-NLS-1$
            description.setId(workspaceSourceID);
            Version version = iu.getVersion();
            description.setVersion(version);

            for (Map.Entry<String, String> property : iu.getProperties().entrySet())
            {
              TargletsCorePlugin.checkCancelation(monitor);

              String key = property.getKey();
              if (!WorkspaceIUAnalyzer.IU_PROPERTY_WORKSPACE_MAIN.equals(key))
              {
                String value = property.getValue();

                if ("org.eclipse.equinox.p2.name".equals(key)) //$NON-NLS-1$
                {
                  value = "Source for " + value; //$NON-NLS-1$
                }

                description.setProperty(key, value);
              }
            }

            description.setTouchpointType(PublisherHelper.TOUCHPOINT_OSGI);
            description.setProperty(IU_PROPERTY_SOURCE, Boolean.TRUE.toString());
            description.addProvidedCapabilities(Collections
                .singleton(MetadataFactory.createProvidedCapability(IInstallableUnit.NAMESPACE_IU_ID, description.getId(), description.getVersion())));

            IInstallableUnit workspaceSourceIU = MetadataFactory.createInstallableUnit(description);
            if (!workspaceIUInfos.containsKey(workspaceSourceIU))
            {
              ius.add(workspaceSourceIU);
              ids.put(workspaceSourceID, version);

              idToIUMap.put(new IU(workspaceSourceIU), workspaceSourceIU);
              WorkspaceIUInfo info = workspaceIUInfos.get(iu);
              workspaceSourceIUInfos.put(workspaceSourceIU, info);
            }
          }

          // Include all source IUs in the map.
          workspaceIUInfos.putAll(workspaceSourceIUInfos);
        }
      };

      provisioningContext.setProperty(ProvisioningContext.FOLLOW_REPOSITORY_REFERENCES, Boolean.FALSE.toString());
      provisioningContext.setProperty(FOLLOW_ARTIFACT_REPOSITORY_REFERENCES, Boolean.FALSE.toString());
      return provisioningContext;
    }

    @Override
    public boolean handleProvisioningPlan(ResolutionInfo info) throws CoreException
    {
      provisioningPlan = info.getProvisioningPlan();
      artificialRoot = info.getArtificialRoot();
      metadataRepositories = info.getMetadataRepositories();
      return true;
    }

    @Override
    public IPhaseSet getPhaseSet(ProfileTransaction transaction)
    {
      List<Phase> phases = new ArrayList<>(4);
      phases.add(new Collect(100));
      phases.add(new Property(1));
      phases.add(new Uninstall(50, true)
      {
        @Override
        protected java.util.List<ProvisioningAction> getActions(InstallableUnitOperand currentOperand)
        {
          // If a product IU is provisioned, it pulls in tooling fragments that try to work with the non-existing artifacts of our workspace-based IUs.
          // Also for p2.inf touchpoints such as chmod, we cannot resolve the artifact location, and don't want do that because it would modify the source.
          // So we remove them for the purpose of this phase.
          IInstallableUnit first = currentOperand.first();
          if (first != null)
          {
            if ("true".equals(first.getProperty(WorkspaceIUAnalyzer.IU_PROPERTY_WORKSPACE)) && first instanceof ResolvedInstallableUnit) //$NON-NLS-1$
            {
              List<ProvisioningAction> actions = new ArrayList<>(super.getActions(new InstallableUnitOperand(
                  new ResolvedInstallableUnit((IInstallableUnit)((ResolvedInstallableUnit)first).getMember(ResolvedInstallableUnit.MEMBER_ORIGINAL)), null)));
              for (Iterator<ProvisioningAction> it = actions.iterator(); it.hasNext();)
              {
                ProvisioningAction provisioningAction = it.next();
                if (provisioningAction.getTouchpoint() instanceof NativeTouchpoint)
                {
                  it.remove();
                }
              }

              return actions;
            }
          }

          return super.getActions(currentOperand);
        }
      });
      phases.add(new Install(50)
      {
        @Override
        protected java.util.List<ProvisioningAction> getActions(InstallableUnitOperand currentOperand)
        {
          // If a product IU is provisioned, it pulls in tooling fragments that try to work with the non-existing artifacts of our workspace-based IUs.
          // Also for p2.inf touchpoints such as chmod, we cannot resolve the artifact location, and don't want do that because it would modify the source.
          // So we remove them for the purpose of this phase.
          IInstallableUnit second = currentOperand.second();
          if (second != null)
          {
            if ("true".equals(second.getProperty(WorkspaceIUAnalyzer.IU_PROPERTY_WORKSPACE)) && second instanceof ResolvedInstallableUnit) //$NON-NLS-1$
            {
              List<ProvisioningAction> actions = new ArrayList<>(super.getActions(new InstallableUnitOperand(null,
                  new ResolvedInstallableUnit((IInstallableUnit)((ResolvedInstallableUnit)second).getMember(ResolvedInstallableUnit.MEMBER_ORIGINAL)))));
              for (Iterator<ProvisioningAction> it = actions.iterator(); it.hasNext();)
              {
                ProvisioningAction provisioningAction = it.next();
                if (provisioningAction.getTouchpoint() instanceof NativeTouchpoint)
                {
                  it.remove();
                }
              }

              return actions;
            }
          }

          return super.getActions(currentOperand);
        }
      });

      phases.add(new CollectNativesPhase(profile.getBundlePool(), 100));

      return new PhaseSet(phases.toArray(new Phase[phases.size()]));
    }

    private static IInstallableUnit createPDETargetPlatformIU()
    {
      InstallableUnitDescription description = new InstallableUnitDescription();
      description.setId(A_PDE_TARGET_PLATFORM_LOWER_CASE);
      Version version = Version.createOSGi(1, 0, 0);
      description.setVersion(version);
      description.addProvidedCapabilities(
          Collections.singleton(MetadataFactory.createProvidedCapability(A_PDE_TARGET_PLATFORM, Messages.TargletContainer_CannotInstall_message, version)));
      description.setTouchpointType(org.eclipse.equinox.spi.p2.publisher.PublisherHelper.TOUCHPOINT_OSGI);
      description.setArtifacts(new IArtifactKey[0]);
      return MetadataFactory.createInstallableUnit(description);
    }

    /**
     * @author Pascal Rapicault
     */
    private static final class CollectNativesPhase extends InstallableUnitPhase
    {
      private static final String NATIVE_TYPE = "org.eclipse.equinox.p2.native"; //$NON-NLS-1$

      private final BundlePool bundlePool;

      public CollectNativesPhase(BundlePool bundlePool, int weight)
      {
        super(NATIVE_ARTIFACTS, weight);
        this.bundlePool = bundlePool;
      }

      @Override
      protected List<ProvisioningAction> getActions(InstallableUnitOperand operand)
      {
        IInstallableUnit installableUnit = operand.second();
        if (installableUnit != null && installableUnit.getTouchpointType().getId().equals(NATIVE_TYPE))
        {
          List<ProvisioningAction> list = new ArrayList<>(1);
          list.add(new CollectNativesAction(bundlePool));
          return list;
        }

        return null;
      }

      @Override
      protected IStatus initializePhase(IProgressMonitor monitor, IProfile profile, Map<String, Object> parameters)
      {
        parameters.put(NATIVE_ARTIFACTS, new ArrayList<>());
        parameters.put(PARM_PROFILE, profile);
        return null;
      }

      @Override
      protected IStatus completePhase(IProgressMonitor monitor, IProfile profile, Map<String, Object> parameters)
      {
        @SuppressWarnings("unchecked")
        List<IArtifactRequest> artifactRequests = (List<IArtifactRequest>)parameters.get(NATIVE_ARTIFACTS);
        ProvisioningContext context = (ProvisioningContext)parameters.get(PARM_CONTEXT);
        IProvisioningAgent agent = (IProvisioningAgent)parameters.get(PARM_AGENT);
        DownloadManager downloadManager = new DownloadManager(context, agent);
        for (Iterator<IArtifactRequest> it = artifactRequests.iterator(); it.hasNext();)
        {
          downloadManager.add(it.next());
        }

        return downloadManager.start(monitor);
      }
    }

    /**
     * @author Pascal Rapicault
     */
    private static final class CollectNativesAction extends ProvisioningAction
    {
      private static final String PARM_OPERAND = "operand"; //$NON-NLS-1$

      private final BundlePool bundlePool;

      public CollectNativesAction(BundlePool bundlePool)
      {
        this.bundlePool = bundlePool;
      }

      @Override
      public IStatus execute(Map<String, Object> parameters)
      {
        InstallableUnitOperand operand = (InstallableUnitOperand)parameters.get(PARM_OPERAND);
        IInstallableUnit installableUnit = operand.second();
        if (installableUnit == null)
        {
          return Status.OK_STATUS;
        }

        try
        {
          Collection<?> toDownload = installableUnit.getArtifacts();
          if (toDownload == null)
          {
            return Status.OK_STATUS;
          }

          @SuppressWarnings("unchecked")
          List<IArtifactRequest> artifactRequests = (List<IArtifactRequest>)parameters.get(NATIVE_ARTIFACTS);

          IArtifactRepositoryManager manager = bundlePool.getAgent().getArtifactRepositoryManager();
          IFileArtifactRepository fileArtifactRepository = bundlePool.getFileArtifactRepository();

          for (Iterator<?> it = toDownload.iterator(); it.hasNext();)
          {
            IArtifactKey keyToDownload = (IArtifactKey)it.next();
            IArtifactRequest request = manager.createMirrorRequest(keyToDownload, fileArtifactRepository, null, null);
            artifactRequests.add(request);
          }
        }
        catch (Exception ex)
        {
          return TargletsCorePlugin.INSTANCE.getStatus(ex);
        }

        return Status.OK_STATUS;
      }

      @Override
      public IStatus undo(Map<String, Object> parameters)
      {
        // Nothing to do for now.
        return Status.OK_STATUS;
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Persistence implements ITargetLocationFactory
  {
    private static final Map<Object, Object> XML_OPTIONS;

    private static final BasicExtendedMetaData EXTENDED_META_DATA = new BasicExtendedMetaData()
    {
      @Override
      public EStructuralFeature getElement(EClass eClass, String namespace, String name)
      {
        EStructuralFeature eStructuralFeature = super.getElement(eClass, namespace, name);
        if (eStructuralFeature == null)
        {
          eStructuralFeature = super.getElement(eClass, namespace, name.substring(0, name.length() - 1));
        }

        if (eStructuralFeature == null)
        {
          eStructuralFeature = eClass.getEStructuralFeature(name);
        }

        return eStructuralFeature;
      }
    };

    private static final EStructuralFeature LOCATION_TYPE_FEATURE = EXTENDED_META_DATA.demandFeature(null, "type", false); //$NON-NLS-1$

    private static final EStructuralFeature LOCATION_ID_FEATURE = EXTENDED_META_DATA.demandFeature(null, "id", false); //$NON-NLS-1$

    private static final EStructuralFeature LOCATION_FEATURE = EXTENDED_META_DATA.demandFeature(null, "location", true); //$NON-NLS-1$

    private static final EStructuralFeature TARGLET_FEATURE = EXTENDED_META_DATA.demandFeature(null, "targlet", true); //$NON-NLS-1$

    private static final EStructuralFeature COMPOSED_TARGET_FEATURE = EXTENDED_META_DATA.demandFeature(null, "composedTarget", true, false); //$NON-NLS-1$

    private static final EClass DOCUMENT_ROOT_CLASS = LOCATION_FEATURE.getEContainingClass();

    static
    {
      XMLOptions xmlOptions = new XMLOptionsImpl();
      xmlOptions.setProcessAnyXML(true);
      xmlOptions.setProcessSchemaLocations(true);

      Map<Object, Object> options = new HashMap<>();
      options.put(XMLResource.OPTION_DECLARE_XML, Boolean.FALSE);
      options.put(XMLResource.OPTION_EXTENDED_META_DATA, EXTENDED_META_DATA);
      options.put(XMLResource.OPTION_LAX_FEATURE_PROCESSING, Boolean.TRUE);
      options.put(XMLResource.OPTION_XML_OPTIONS, xmlOptions);

      XML_OPTIONS = Collections.unmodifiableMap(options);
    }

    @Override
    public ITargletContainer getTargetLocation(String type, String serializedXML) throws CoreException
    {
      if (TYPE.equals(type))
      {
        return fromXML(serializedXML);
      }

      return null;
    }

    public static ITargletContainer fromXML(String xml) throws CoreException
    {
      try
      {
        XMLResource resource = new XMLResourceImpl();
        resource.load(new InputSource(new StringReader(xml)), XML_OPTIONS);

        EObject documentRoot = resource.getContents().get(0);
        AnyType location = (AnyType)documentRoot.eContents().get(0);
        String id = (String)location.eGet(LOCATION_ID_FEATURE);

        @SuppressWarnings("unchecked")
        EList<String> composedTargets = (EList<String>)location.eGet(COMPOSED_TARGET_FEATURE);

        EList<Targlet> targlets = new BasicEList<>();
        for (EObject eObject : location.eContents())
        {
          targlets.add((Targlet)eObject);
        }

        return new TargletContainer(id, targlets, composedTargets);
      }
      catch (Exception ex)
      {
        TargletsCorePlugin.INSTANCE.coreException(ex);
      }

      return null;
    }

    public static Writer toXML(String id, List<Targlet> targlets, List<String> composedTargets) throws Exception
    {
      AnyType location = XMLTypeFactory.eINSTANCE.createAnyType();
      location.eSet(LOCATION_ID_FEATURE, id);
      location.eSet(LOCATION_TYPE_FEATURE, TYPE);

      EObject documentRoot = EcoreUtil.create(DOCUMENT_ROOT_CLASS);
      documentRoot.eSet(LOCATION_FEATURE, location);

      FeatureMap targletFeatureMap = location.getAny();

      for (String composedTarget : composedTargets)
      {
        FeatureMapUtil.addText(targletFeatureMap, "\n  "); //$NON-NLS-1$
        targletFeatureMap.add(COMPOSED_TARGET_FEATURE, composedTarget);
      }

      EList<Targlet> copy = TargletFactory.eINSTANCE.copyTarglets(targlets);
      for (Targlet targlet : copy)
      {
        FeatureMapUtil.addText(targletFeatureMap, "\n  "); //$NON-NLS-1$
        targletFeatureMap.add(TARGLET_FEATURE, targlet);
      }

      FeatureMapUtil.addText(targletFeatureMap, "\n"); //$NON-NLS-1$

      StringWriter writer = new StringWriter();

      XMLResource resource = new XMLResourceImpl();
      resource.getContents().add(documentRoot);
      resource.save(writer, XML_OPTIONS);

      return writer;
    }
  }

  /**
   * @author Ed Merks
   */
  private static class IU
  {
    private final String id;

    private final Version version;

    public IU(IInstallableUnit iu)
    {
      this(iu.getId(), iu.getVersion());
    }

    public IU(String id, Version version)
    {
      this.id = id;
      this.version = P2Factory.eINSTANCE.createVersionRange(version, VersionSegment.MICRO).getMinimum();
    }

    public String getId()
    {
      return id;
    }

    public Version getVersion()
    {
      return version;
    }

    @Override
    public int hashCode()
    {
      return id.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj instanceof IU)
      {
        IU iu = (IU)obj;

        if (id.equals(iu.getId()))
        {
          Version version = iu.getVersion();
          if (this.version.equals(version) || Version.emptyVersion.equals(version) || Version.emptyVersion.equals(this.version))
          {
            return true;
          }
        }
      }

      return false;
    }
  }
}
