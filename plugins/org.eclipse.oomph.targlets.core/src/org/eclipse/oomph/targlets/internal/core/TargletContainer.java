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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
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
import org.eclipse.equinox.internal.p2.metadata.expression.LDAPFilter;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.engine.spi.ProvisioningAction;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IInstallableUnitFragment;
import org.eclipse.equinox.p2.metadata.IInstallableUnitPatch;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.IRequirementChange;
import org.eclipse.equinox.p2.metadata.ITouchpointData;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.metadata.expression.ExpressionUtil;
import org.eclipse.equinox.p2.metadata.expression.IExpression;
import org.eclipse.equinox.p2.metadata.expression.IFilterExpression;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;
import org.eclipse.equinox.p2.planner.IProfileChangeRequest;
import org.eclipse.equinox.p2.publisher.eclipse.BundlesAction;
import org.eclipse.equinox.p2.query.CollectionResult;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRequest;
import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.spi.p2.publisher.PublisherHelper;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetLocationFactory;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.pde.core.target.TargetBundle;
import org.eclipse.pde.core.target.TargetFeature;
import org.eclipse.pde.internal.core.target.AbstractBundleContainer;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class TargletContainer extends AbstractBundleContainer implements ITargletContainer
{
  public static final String TYPE = "Targlet";

  public static final String IU_PROPERTY_SOURCE = "org.eclipse.oomph.targlet.source";

  private static final ThreadLocal<Boolean> FORCE_UPDATE = new ThreadLocal<Boolean>();

  private static final ThreadLocal<Boolean> MIRRORS = new ThreadLocal<Boolean>();

  private static final String A_PDE_TARGET_PLATFORM = "A.PDE.Target.Platform";

  private static final String A_PDE_TARGET_PLATFORM_LOWER_CASE = A_PDE_TARGET_PLATFORM.toLowerCase();

  private static final String FOLLOW_ARTIFACT_REPOSITORY_REFERENCES = "org.eclipse.equinox.p2.director.followArtifactRepositoryReferences";

  private static final byte[] BUFFER = new byte[8192];

  private static final String PROP_ARCH = "osgi.arch"; //$NON-NLS-1$

  private static final String PROP_OS = "osgi.os"; //$NON-NLS-1$

  private static final String PROP_WS = "osgi.ws"; //$NON-NLS-1$

  private static final String IU_FILTER = PropertiesUtil.getProperty("oomph.targlets.iu.filter");

  private static final Pattern IU_FILTER_PATTERN = IU_FILTER == null ? null : Pattern.compile(IU_FILTER);

  private String id;

  private ITargetDefinition targetDefinition;

  private final EList<Targlet> targlets = new BasicEList<Targlet>();

  public TargletContainer(String id)
  {
    this.id = id;

    // Make the Targlets UI active so that PDE can use Platform.getAdapterManager() to load our registered adapters.
    try
    {
      CommonPlugin.loadClass("org.eclipse.oomph.targlets.ui", "org.eclipse.oomph.targlets.internal.ui.TargletsUIPlugin");
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
  private TargletContainer(String id, Collection<? extends Targlet> targlets)
  {
    this(id);
    basicSetTarglets(targlets);
  }

  @Override
  protected int getResolveBundlesWork()
  {
    return 999;
  }

  @Override
  protected int getResolveFeaturesWork()
  {
    return 1;
  }

  public boolean isContentEqual(AbstractBundleContainer container)
  {
    if (container instanceof TargletContainer)
    {
      TargletContainer targletContainer = (TargletContainer)container;
      if (targlets.size() != targletContainer.targlets.size())
      {
        return false;
      }

      for (Targlet targlet : targletContainer.targlets)
      {
        Targlet existingTarglet = getTarglet(targlet.getName());
        if (existingTarglet == null || !EcoreUtil.equals(existingTarglet, targlet))
        {
          return false;
        }
      }

      return true;
    }

    return false;
  }

  public String getID()
  {
    return id;
  }

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
        public Object run(ITargetPlatformService service) throws CoreException
        {
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

  public ITargetDefinition getTargetDefinition()
  {
    return targetDefinition;
  }

  /**
   * Returns a copy of the targlet with the given name in this targlet container. This copy can be freely modified but the modifications won't have an impact
   * on a targlet container unless the copy is set back into a container via {@link #setTarglets(Collection)}.
   */
  public Targlet getTarglet(String name)
  {
    int index = getTargletIndex(name);
    if (index != -1)
    {
      return TargletFactory.eINSTANCE.copyTarglet(targlets.get(index));
    }

    return null;
  }

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

  public boolean hasTarglet(String name)
  {
    return getTargletIndex(name) != -1;
  }

  /**
   * Returns a copy of the targlets in this targlet container. This copy can be freely modified but the modifications won't have an impact
   * on a targlet container unless the copy is set back into a container via {@link #setTarglets(Collection)}.
   */
  public EList<Targlet> getTarglets()
  {
    return TargletFactory.eINSTANCE.copyTarglets(targlets);
  }

  /**
   * Copies the passed targlets into this targlet container. Modifications of the passed targlets after the call
   * to this method won't have an impact on this targlet container.
   */
  public void setTarglets(Collection<? extends Targlet> targlets) throws CoreException
  {
    basicSetTarglets(targlets);

    TargletContainerDescriptor descriptor = updateTargetDefinition();
    TargletContainerEvent event = new TargletContainerEvent.TargletsChangedEvent(this, descriptor);
    TargletContainerListenerRegistry.INSTANCE.notifyListeners(event, new NullProgressMonitor());
  }

  private void basicSetTarglets(Collection<? extends Targlet> targlets)
  {
    Set<String> names = new HashSet<String>();
    for (Targlet targlet : targlets)
    {
      String name = targlet.getName();
      if (!names.add(name))
      {
        throw new IllegalArgumentException("Duplicate targlet name: " + name);
      }
    }

    this.targlets.clear();
    this.targlets.addAll(TargletFactory.eINSTANCE.copyTarglets(targlets));
  }

  @Override
  public String serialize()
  {
    try
    {
      return Persistence.toXML(id, targlets).toString();
    }
    catch (Exception ex)
    {
      TargletsCorePlugin.INSTANCE.log(ex);
      return null;
    }
  }

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

    builder.append(",org.eclipse.swt.buildtime=true");

    return builder.toString();
  }

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
    return "Targlet Container " + id;
  }

  @Override
  public String getLocation(boolean resolve) throws CoreException
  {
    TargletContainerDescriptorManager manager = TargletContainerDescriptorManager.getInstance();
    TargletContainerDescriptor descriptor = manager.getDescriptor(id, new NullProgressMonitor());
    return descriptor.getInstallLocation().getAbsolutePath();
  }

  public String getDigest()
  {
    String environmentProperties = getEnvironmentProperties();
    String nlProperty = getNLProperty();
    return createDigest(id, environmentProperties, nlProperty, targlets);
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
    resolveUnits(monitor);
    return fBundles;
  }

  @Override
  protected TargetFeature[] resolveFeatures(ITargetDefinition target, IProgressMonitor monitor) throws CoreException
  {
    // All work has been done in resolveBundles() already.
    return fFeatures;
  }

  private void resolveUnits(IProgressMonitor monitor) throws CoreException
  {
    try
    {
      SubMonitor progress = SubMonitor.convert(monitor, 100).detectCancelation();
      TargletContainerDescriptorManager manager = TargletContainerDescriptorManager.getInstance();

      String environmentProperties = getEnvironmentProperties();
      String nlProperty = getNLProperty();
      String digest = createDigest(id, environmentProperties, nlProperty, targlets);

      TargletContainerDescriptor descriptor = manager.getDescriptor(id, progress.newChild(4));
      progress.childDone();

      boolean isDisplayThread = isDisplayThread();
      Profile profile = descriptor.getWorkingProfile();
      if (profile == null || //
          !descriptor.getWorkingDigest().equals(digest) && !isDisplayThread && descriptor.getUpdateProblem() == null || //
          FORCE_UPDATE.get() == Boolean.TRUE)
      {
        try
        {
          if (isDisplayThread)
          {
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
        catch (CoreException ex)
        {
          if (profile == null)
          {
            // This just leads to logging further down.
            throw ex;
          }
        }
      }
      else
      {
        progress.skipped(86);
      }

      generateUnits(descriptor, profile, progress.newChild(10));
      progress.done();
    }
    catch (Throwable t)
    {
      TargletsCorePlugin.INSTANCE.coreException(t);
    }
  }

  private void generateUnits(TargletContainerDescriptor descriptor, IProfile profile, IProgressMonitor monitor) throws CoreException
  {
    SubMonitor progress = SubMonitor.convert(monitor, 100).detectCancelation();

    List<TargetBundle> bundles = new ArrayList<TargetBundle>();
    List<TargetFeature> features = new ArrayList<TargetFeature>();

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

    fBundles = bundles.toArray(new TargetBundle[bundles.size()]);
    fFeatures = features.toArray(new TargetFeature[features.size()]);
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
            if (file.getName().endsWith(".jar"))
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

  public void forceUpdate(boolean activateTargetDefinition, boolean mirrors, IProgressMonitor monitor) throws CoreException
  {
    try
    {
      FORCE_UPDATE.set(Boolean.TRUE);
      MIRRORS.set(mirrors ? Boolean.TRUE : false);

      // Clear the resolution statuses of the involved targlet containers.
      for (ITargetLocation targetLocation : targetDefinition.getTargetLocations())
      {
        if (targetLocation instanceof ITargletContainer)
        {
          TargletContainer targletContainer = (TargletContainer)targetLocation;
          targletContainer.clearResolutionStatus();
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

  public IStatus updateProfile(IProgressMonitor monitor)
  {
    try
    {
      String environmentProperties = getEnvironmentProperties();
      String nlProperty = getNLProperty();
      String digest = createDigest(id, environmentProperties, nlProperty, targlets);

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

    final Profile profile = descriptor.startUpdateTransaction(environmentProperties, nlProperty, digest, progress.newChild());

    ProfileTransaction transaction = profile.change().setRemoveExistingInstallableUnits(true);
    transaction.setMirrors(MIRRORS.get() == Boolean.TRUE);

    IProvisioningAgent provisioningAgent = profile.getAgent().getProvisioningAgent();
    CacheUsageConfirmer cacheUsageConfirmer = TargletsCorePlugin.INSTANCE.getCacheUsageConfirmer();
    CacheUsageConfirmer oldCacheUsageConfirmer = (CacheUsageConfirmer)provisioningAgent.getService(CacheUsageConfirmer.SERVICE_NAME);

    IEclipsePreferences garbageCollectorPreferences = ConfigurationScope.INSTANCE.getNode("org.eclipse.equinox.p2.garbagecollector");
    String oldGCEnabled = garbageCollectorPreferences.get("gc_enabled", null);
    garbageCollectorPreferences.putBoolean("gc_enabled", false);

    boolean originalBetterMirrorSelection = CachingRepositoryManager.enableBetterMirrorSelection();

    try
    {
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

      WorkspaceIUAnalyzer workspaceIUAnalyzer = analyzeWorkspaceIUs(rootRequirements, repositories, progress);

      if (rootRequirements.isEmpty())
      {
        descriptor.rollbackUpdateTransaction(null, new NullProgressMonitor());
        return null;
      }

      TargletCommitContext commitContext = new TargletCommitContext(profile, workspaceIUAnalyzer, isIncludeAllPlatforms(), isIncludeAllRequirements());
      transaction.commit(commitContext, progress.newChild());

      Map<IInstallableUnit, WorkspaceIUInfo> requiredProjects = getRequiredProjects(profile, workspaceIUAnalyzer.getWorkspaceIUInfos(), progress.newChild());
      descriptor.commitUpdateTransaction(digest, requiredProjects.values(), progress.newChild());

      TargletContainerEvent event = new TargletContainerEvent.ProfileUpdateSucceededEvent(this, descriptor, profile, commitContext.getArtificialRoot(),
          commitContext.getMetadataRepositories(), commitContext.getProvisioningPlan(), requiredProjects);
      TargletContainerListenerRegistry.INSTANCE.notifyListeners(event, progress.newChild());

      monitor.subTask("Targlet container profile update completed");
    }
    catch (Throwable t)
    {
      descriptor.rollbackUpdateTransaction(t, new NullProgressMonitor());

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
        garbageCollectorPreferences.remove("gc_enabled");
      }
      else
      {
        garbageCollectorPreferences.put("gc_enabled", oldGCEnabled);
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

      EList<IInstallableUnit> ius = new BasicEList<IInstallableUnit>();
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
        if ("*".equals(name) && IInstallableUnit.NAMESPACE_IU_ID.equals(namespace))
        {
          // Preprocess all the IUs to build a map from workspace IU Info to filter.
          // This ensures that a requirement on a plain.project will be filtered appropriately.
          Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = workspaceIUAnalyzer.getWorkspaceIUInfos();
          Map<WorkspaceIUInfo, IMatchExpression<IInstallableUnit>> filters = new HashMap<WorkspaceIUInfo, IMatchExpression<IInstallableUnit>>();
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
            if ("true".equalsIgnoreCase(iu.getProperty("org.eclipse.equinox.p2.type.category")))
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

  private static String createDigest(String id, String environmentProperties, String nlProperty, EList<Targlet> targlets)
  {
    InputStream stream = null;

    try
    {
      Writer writer = Persistence.toXML(id, targlets);
      writer.write("\n<!-- Environment Properties: ");
      writer.write(environmentProperties);
      writer.write(" -->");
      writer.write("\n<!-- NL Property: ");
      writer.write(nlProperty);
      writer.write(" -->\n");

      final MessageDigest digest = MessageDigest.getInstance("SHA-1");
      stream = new FilterInputStream(new ByteArrayInputStream(writer.toString().getBytes("UTF-8")))
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
    return providesNamespace(unit, "osgi.bundle");
  }

  private static boolean isFeatureJar(IInstallableUnit unit)
  {
    return providesNamespace(unit, "org.eclipse.update.feature");
  }

  private static boolean isDisplayThread()
  {
    try
    {
      Class<?> displayClass = CommonPlugin.loadClass("org.eclipse.swt", "org.eclipse.swt.widgets.Display");
      Method getCurrentMethod = ReflectUtil.getMethod(displayClass, "getCurrent");
      return ReflectUtil.invokeMethod(getCurrentMethod, null) != null;
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    return false;
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
    Map<WorkspaceIUInfo, IInstallableUnit> mainIUs = new HashMap<WorkspaceIUInfo, IInstallableUnit>();
    for (Map.Entry<IInstallableUnit, WorkspaceIUInfo> entry : allProjects.entrySet())
    {
      IInstallableUnit iu = entry.getKey();
      if ("true".equals(iu.getProperty(WorkspaceIUAnalyzer.IU_PROPERTY_WORKSPACE_MAIN)))
      {
        mainIUs.put(entry.getValue(), iu);
      }
    }

    Map<IInstallableUnit, WorkspaceIUInfo> result = new HashMap<IInstallableUnit, WorkspaceIUInfo>();
    for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUAnyQuery(), monitor)))
    {
      if ("true".equals(iu.getProperty(TargletContainer.IU_PROPERTY_SOURCE)))
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

  /**
   * @author Eike Stepper
   */
  private static final class TargletCommitContext extends CommitContext
  {
    private static final Pattern OSGI_PROPERTY_FILTER = Pattern.compile("(!?)\\((osgi.arch|osgi.os|osgi.ws)=([^)]+)\\)");

    private static final String NATIVE_ARTIFACTS = "nativeArtifacts"; //$NON-NLS-1$

    private final Profile profile;

    private final WorkspaceIUAnalyzer workspaceIUAnalyzer;

    private IProvisioningPlan provisioningPlan;

    private IInstallableUnit artificialRoot;

    private List<IMetadataRepository> metadataRepositories;

    private boolean isIncludeAllPlatforms;

    private boolean isIncludeAllRequirements;

    public TargletCommitContext(Profile profile, WorkspaceIUAnalyzer workspaceIUAnalyzer, boolean isIncludeAllPlatforms, boolean isIncludeAllRequirements)
    {
      this.profile = profile;
      this.workspaceIUAnalyzer = workspaceIUAnalyzer;
      this.isIncludeAllPlatforms = isIncludeAllPlatforms;
      this.isIncludeAllRequirements = isIncludeAllRequirements;
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

            Set<IInstallableUnit> originalWorkspaceIUs = new HashSet<IInstallableUnit>(workspaceIUInfos.keySet());

            Set<IInstallableUnit> ius = new LinkedHashSet<IInstallableUnit>();
            Map<IU, IInstallableUnit> idToIUMap = new HashMap<IU, IInstallableUnit>();
            generateWorkspaceSourceIUs(ius, workspaceIUVersions, idToIUMap, monitor);

            ius.add(createPDETargetPlatformIU());

            IQueryResult<IInstallableUnit> metadataResult = super.getMetadata(monitor).query(QueryUtil.createIUAnyQuery(), monitor);
            for (IInstallableUnit iu : P2Util.asIterable(metadataResult))
            {
              TargletsCorePlugin.checkCancelation(monitor);

              ius.add(createGeneralizedIU(iu));

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
                if (workspaceIU.isSingleton() || "true".equals(workspaceIU.getProperty(InstallableUnitDescription.PROP_TYPE_GROUP)))
                {
                  ius.remove(workspaceIU);
                }

                // If the workspaceIU has any requirements not in the binary IU, then include those.
                List<IRequirement> extraRequirements = new ArrayList<IRequirement>();
                LOOP: for (IRequirement workspaceRequirement : workspaceIU.getRequirements())
                {
                  if (workspaceRequirement instanceof IRequiredCapability)
                  {
                    IRequiredCapability workspaceRequiredCapability = (IRequiredCapability)workspaceRequirement;
                    String namespace = workspaceRequiredCapability.getNamespace();
                    String name = workspaceRequiredCapability.getName();
                    for (IRequirement requirement : iu.getRequirements())
                    {
                      if (requirement instanceof IRequiredCapability)
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
                      // If it resolves to a workspace IU that's a singleton, generalize the requirement to include any version of that IU, because resolving to
                      // any version will result in the import of the project.
                      IInstallableUnit requiredWorkspaceIU = idToIUMap.get(new IU(name, Version.emptyVersion));
                      if (requiredWorkspaceIU != null
                          && (requiredWorkspaceIU.isSingleton() || "true".equals(requiredWorkspaceIU.getProperty(InstallableUnitDescription.PROP_TYPE_GROUP))))
                      {
                        extraRequirements
                            .add(MetadataFactory.createRequirement(namespace, name, VersionRange.emptyRange, workspaceRequiredCapability.getFilter(),
                                workspaceRequiredCapability.getMin(), workspaceRequiredCapability.getMax(), workspaceRequiredCapability.isGreedy()));
                        continue LOOP;
                      }
                    }
                  }

                  // Otherwise add the requirement as is.
                  extraRequirements.add(workspaceRequirement);
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

            Set<IInstallableUnit> remainingWorkspaceIUs = new HashSet<IInstallableUnit>(ius);
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
                if (workspaceRequirement instanceof IRequiredCapability)
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
                          && (requiredWorkspaceIU.isSingleton() || "true".equals(requiredWorkspaceIU.getProperty(InstallableUnitDescription.PROP_TYPE_GROUP))))
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

            metadata = new CollectionResult<IInstallableUnit>(ius);
          }

          return metadata;
        }

        private IInstallableUnit createGeneralizedIU(IInstallableUnit iu)
        {
          // If we're not including all platform, no generalization is needed.
          if (!isIncludeAllPlatforms && isIncludeAllRequirements)
          {
            return iu;
          }

          // Determine the generalized IU filter.
          IMatchExpression<IInstallableUnit> filter = iu.getFilter();
          IMatchExpression<IInstallableUnit> generalizedFilter = isIncludeAllPlatforms ? generalize(filter) : filter;
          boolean needsGeneralization = filter != generalizedFilter;

          // Determine the generalized requirement filters.
          Collection<IRequirement> requirements = iu.getRequirements();
          IRequirement[] generalizedRequirements = requirements.toArray(new IRequirement[requirements.size()]);
          for (int i = 0; i < generalizedRequirements.length; ++i)
          {
            IRequirement requirement = generalizedRequirements[i];
            IMatchExpression<IInstallableUnit> requirementFilter = requirement.getFilter();
            IMatchExpression<IInstallableUnit> generalizedRequirementFilter = isIncludeAllPlatforms ? generalize(requirementFilter) : filter;

            // If the filter needs generalization, create a clone of the requirement, with the generalized filter replacement.
            if (requirementFilter != filter || !isIncludeAllRequirements && requirement.getMin() != 0)
            {
              needsGeneralization = true;
              // IRequirement generalizedRequirement = MetadataFactory.createRequirement(requirement.getMatches(), generalizedRequirementFilter,
              // requirement.getMin(), requirement.getMax(), requirement.isGreedy(), requirement.getDescription());
              IRequirement generalizedRequirement = MetadataFactory.createRequirement(requirement.getMatches(), generalizedRequirementFilter, 0,
                  requirement.getMax(), true, requirement.getDescription());
              generalizedRequirements[i] = generalizedRequirement;
            }
          }

          // If none of the filters or slicer-mode lower bounds need generalization, the original IU can be used.
          if (!needsGeneralization)
          {
            return iu;
          }

          // Create a description that clones the IU with the generalized filter and slicer-mode lower bound replacements.
          InstallableUnitDescription description;

          if (iu instanceof IInstallableUnitFragment)
          {
            IInstallableUnitFragment installableUnitFragment = (IInstallableUnitFragment)iu;
            MetadataFactory.InstallableUnitFragmentDescription fragmentDescription = new MetadataFactory.InstallableUnitFragmentDescription();
            Collection<IRequirement> host = installableUnitFragment.getHost();
            fragmentDescription.setHost(host.toArray(new IRequirement[host.size()]));
            description = fragmentDescription;
          }
          else if (iu instanceof IInstallableUnitPatch)
          {
            IInstallableUnitPatch installableUnitPatch = (IInstallableUnitPatch)iu;
            MetadataFactory.InstallableUnitPatchDescription patchDescription = new MetadataFactory.InstallableUnitPatchDescription();
            patchDescription.setApplicabilityScope(installableUnitPatch.getApplicabilityScope());
            patchDescription.setLifeCycle(installableUnitPatch.getLifeCycle());
            List<IRequirementChange> requirementsChange = installableUnitPatch.getRequirementsChange();
            patchDescription.setRequirementChanges(requirementsChange.toArray(new IRequirementChange[requirementsChange.size()]));
            description = patchDescription;
          }
          else
          {
            description = new MetadataFactory.InstallableUnitDescription();
          }

          description.setId(iu.getId());

          description.setVersion(iu.getVersion());

          Collection<IArtifactKey> artifacts = iu.getArtifacts();
          description.setArtifacts(artifacts.toArray(new IArtifactKey[artifacts.size()]));

          Collection<IProvidedCapability> providedCapabilities = iu.getProvidedCapabilities();
          description.setCapabilities(providedCapabilities.toArray(new IProvidedCapability[providedCapabilities.size()]));

          description.setCopyright(iu.getCopyright());

          description.setFilter(generalizedFilter);

          Collection<ILicense> licenses = iu.getLicenses();
          description.setLicenses(licenses.toArray(new ILicense[licenses.size()]));

          Collection<IRequirement> metaRequirements = iu.getMetaRequirements();
          description.setMetaRequirements(metaRequirements.toArray(new IRequirement[metaRequirements.size()]));

          description.setRequirements(generalizedRequirements);

          description.setSingleton(iu.isSingleton());

          description.setTouchpointType(iu.getTouchpointType());
          description.setUpdateDescriptor(iu.getUpdateDescriptor());

          for (Iterator<Map.Entry<String, String>> iterator = iu.getProperties().entrySet().iterator(); iterator.hasNext();)
          {
            Map.Entry<String, String> entry = iterator.next();
            description.setProperty(entry.getKey(), entry.getValue());
          }

          for (ITouchpointData touchpointData : iu.getTouchpointData())
          {
            description.addTouchpointData(touchpointData);
          }

          return MetadataFactory.createInstallableUnit(description);
        }

        private IMatchExpression<IInstallableUnit> generalize(IMatchExpression<IInstallableUnit> filter)
        {
          if (filter == null)
          {
            return null;
          }

          // Lazily determine if any parameter needs generalization.
          Object[] generalizedParameters = null;
          Object[] parameters = filter.getParameters();
          for (int i = 0; i < parameters.length; ++i)
          {
            Object parameter = parameters[i];
            if (parameter instanceof LDAPFilter)
            {
              String value = parameter.toString();
              Matcher matcher = OSGI_PROPERTY_FILTER.matcher(value);
              if (matcher.find())
              {
                // If the pattern matches, we need to generalize the parameters.
                if (generalizedParameters == null)
                {
                  // Copy over all the parameters.
                  // The ones that need generalization will be replaced.
                  generalizedParameters = new Object[parameters.length];
                  System.arraycopy(parameters, 0, generalizedParameters, 0, parameters.length);
                }

                // Build the replacement expression
                StringBuffer result = new StringBuffer();
                if (matcher.group(1).length() == 0)
                {
                  matcher.appendReplacement(result, "($2=*)");
                }
                else
                {
                  matcher.appendReplacement(result, "!($2=nothing)");
                }

                // Handle all the remaining matches the same way.
                while (matcher.find())
                {
                  if (matcher.group(1).length() == 0)
                  {
                    matcher.appendReplacement(result, "($2=*)");
                  }
                  else
                  {
                    matcher.appendReplacement(result, "!($2=nothing)");
                  }
                }

                // Complete the replacements, parse it back into an LDAP filter, and replace this parameter.
                matcher.appendTail(result);
                IFilterExpression ldap = ExpressionUtil.parseLDAP(result.toString());
                generalizedParameters[i] = ldap;
              }
            }
          }

          // If one of the parameters needed to be generalized...
          if (generalizedParameters != null)
          {
            // Parse the filter expression and create a new match expressions with the same filter but the generalized parameters.
            IExpression expression = ExpressionUtil.parse(filter.toString());
            return ExpressionUtil.getFactory().matchExpression(expression, generalizedParameters);
          }

          // Otherwise, return the original filter.
          return filter;
        }

        private void generateWorkspaceSourceIUs(Set<IInstallableUnit> ius, Map<String, Version> ids, Map<IU, IInstallableUnit> idToIUMap,
            IProgressMonitor monitor)
        {
          Map<IInstallableUnit, WorkspaceIUInfo> workspaceSourceIUInfos = new HashMap<IInstallableUnit, WorkspaceIUInfo>();
          Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = workspaceIUAnalyzer.getWorkspaceIUInfos();

          for (IInstallableUnit iu : workspaceIUInfos.keySet())
          {
            TargletsCorePlugin.checkCancelation(monitor);

            String id = iu.getId();
            ius.add(createGeneralizedIU(iu));
            idToIUMap.put(new IU(iu), iu);

            if (id.endsWith(Requirement.PROJECT_SUFFIX))
            {
              continue;
            }

            String suffix = "";
            if (id.endsWith(Requirement.FEATURE_SUFFIX))
            {
              id = id.substring(0, id.length() - Requirement.FEATURE_SUFFIX.length());
              suffix = Requirement.FEATURE_SUFFIX;
            }

            InstallableUnitDescription description = new MetadataFactory.InstallableUnitDescription();
            String workspaceSourceID = id + ".source" + suffix;
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

                if ("org.eclipse.equinox.p2.name".equals(key))
                {
                  value = "Source for " + value;
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
      List<Phase> phases = new ArrayList<Phase>(4);
      phases.add(new Collect(100));
      phases.add(new Property(1));
      phases.add(new Uninstall(50, true)
      {
        @Override
        protected java.util.List<ProvisioningAction> getActions(InstallableUnitOperand currentOperand)
        {
          // If a product IU is provisioned, it pulls in tooling fragments that try to work with the non-existing artifacts of our workspace-based IUs.
          // So we'd remove them for the purpose of this phase.
          IInstallableUnit first = currentOperand.first();
          if (first != null)
          {
            if ("true".equals(first.getProperty(WorkspaceIUAnalyzer.IU_PROPERTY_WORKSPACE)) && first instanceof ResolvedInstallableUnit)
            {
              return super.getActions(new InstallableUnitOperand(
                  new ResolvedInstallableUnit((IInstallableUnit)((ResolvedInstallableUnit)first).getMember(ResolvedInstallableUnit.MEMBER_ORIGINAL)), null));
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
          // So we'd remove them for the purpose of this phase.
          IInstallableUnit second = currentOperand.second();
          if (second != null)
          {
            if ("true".equals(second.getProperty(WorkspaceIUAnalyzer.IU_PROPERTY_WORKSPACE)) && second instanceof ResolvedInstallableUnit)
            {
              return super.getActions(new InstallableUnitOperand(null,
                  new ResolvedInstallableUnit((IInstallableUnit)((ResolvedInstallableUnit)second).getMember(ResolvedInstallableUnit.MEMBER_ORIGINAL))));
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
          Collections.singleton(MetadataFactory.createProvidedCapability(A_PDE_TARGET_PLATFORM, "Cannot be installed into the IDE", version)));
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
          List<ProvisioningAction> list = new ArrayList<ProvisioningAction>(1);
          list.add(new CollectNativesAction(bundlePool));
          return list;
        }

        return null;
      }

      @Override
      protected IStatus initializePhase(IProgressMonitor monitor, IProfile profile, Map<String, Object> parameters)
      {
        parameters.put(NATIVE_ARTIFACTS, new ArrayList<Object>());
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

    private static final EStructuralFeature LOCATION_TYPE_FEATURE = EXTENDED_META_DATA.demandFeature(null, "type", false);

    private static final EStructuralFeature LOCATION_ID_FEATURE = EXTENDED_META_DATA.demandFeature(null, "id", false);

    private static final EStructuralFeature LOCATION_FEATURE = EXTENDED_META_DATA.demandFeature(null, "location", true);

    private static final EStructuralFeature TARGLET_FEATURE = EXTENDED_META_DATA.demandFeature(null, "targlet", true);

    private static final EClass DOCUMENT_ROOT_CLASS = LOCATION_FEATURE.getEContainingClass();

    static
    {
      XMLOptions xmlOptions = new XMLOptionsImpl();
      xmlOptions.setProcessAnyXML(true);
      xmlOptions.setProcessSchemaLocations(true);

      Map<Object, Object> options = new HashMap<Object, Object>();
      options.put(XMLResource.OPTION_DECLARE_XML, Boolean.FALSE);
      options.put(XMLResource.OPTION_EXTENDED_META_DATA, EXTENDED_META_DATA);
      options.put(XMLResource.OPTION_LAX_FEATURE_PROCESSING, Boolean.TRUE);
      options.put(XMLResource.OPTION_XML_OPTIONS, xmlOptions);

      XML_OPTIONS = Collections.unmodifiableMap(options);
    }

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

        EList<Targlet> targlets = new BasicEList<Targlet>();
        for (EObject eObject : location.eContents())
        {
          targlets.add((Targlet)eObject);
        }

        return new TargletContainer(id, targlets);
      }
      catch (Exception ex)
      {
        TargletsCorePlugin.INSTANCE.coreException(ex);
      }

      return null;
    }

    public static Writer toXML(String id, List<Targlet> targlets) throws Exception
    {
      AnyType location = XMLTypeFactory.eINSTANCE.createAnyType();
      location.eSet(LOCATION_ID_FEATURE, id);
      location.eSet(LOCATION_TYPE_FEATURE, TYPE);

      EObject documentRoot = EcoreUtil.create(DOCUMENT_ROOT_CLASS);
      documentRoot.eSet(LOCATION_FEATURE, location);

      FeatureMap targletFeatureMap = location.getAny();
      FeatureMapUtil.addText(targletFeatureMap, "\n  ");

      EList<Targlet> copy = TargletFactory.eINSTANCE.copyTarglets(targlets);
      for (Targlet targlet : copy)
      {
        targletFeatureMap.add(TARGLET_FEATURE, targlet);
      }

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
