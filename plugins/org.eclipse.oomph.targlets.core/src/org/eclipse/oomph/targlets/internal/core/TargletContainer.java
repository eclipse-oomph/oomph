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
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.targlets.FeatureGenerator;
import org.eclipse.oomph.targlets.IUGenerator;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.IDChangedEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.ProfileUpdateFailedEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.ProfileUpdateSucceededEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.TargletsChangedEvent;
import org.eclipse.oomph.targlets.internal.core.TargletContainerDescriptor.UpdateProblem;
import org.eclipse.oomph.util.HexUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.SubMonitor;
import org.eclipse.oomph.util.pde.TargetPlatformRunnable;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

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
import org.eclipse.equinox.internal.p2.engine.DownloadManager;
import org.eclipse.equinox.internal.p2.engine.InstallableUnitOperand;
import org.eclipse.equinox.internal.p2.engine.InstallableUnitPhase;
import org.eclipse.equinox.internal.p2.engine.Phase;
import org.eclipse.equinox.internal.p2.engine.PhaseSet;
import org.eclipse.equinox.internal.p2.engine.phases.Collect;
import org.eclipse.equinox.internal.p2.engine.phases.Install;
import org.eclipse.equinox.internal.p2.engine.phases.Property;
import org.eclipse.equinox.internal.p2.engine.phases.Uninstall;
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
import org.eclipse.equinox.p2.planner.IProfileChangeRequest;
import org.eclipse.equinox.p2.query.CollectionResult;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRequest;
import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
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
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class TargletContainer extends AbstractBundleContainer
{
  public static final String TYPE = "Targlet";

  public static final String IU_PROPERTY_SOURCE = "org.eclipse.oomph.targlet.source";

  private static final ThreadLocal<Boolean> FORCE_UPDATE = new ThreadLocal<Boolean>();

  private static final ThreadLocal<Boolean> MIRRORS = new ThreadLocal<Boolean>();

  private static final String A_PDE_TARGET_PLATFORM = "A.PDE.Target.Platform";

  private static final String A_PDE_TARGET_PLATFORM_LOWER_CASE = A_PDE_TARGET_PLATFORM.toLowerCase();

  private static final String FOLLOW_ARTIFACT_REPOSITORY_REFERENCES = "org.eclipse.equinox.p2.director.followArtifactRepositoryReferences";

  private static final String FEATURE_SUFFIX = ".feature.group";

  private static final byte[] BUFFER = new byte[8192];

  private static final String PROP_ARCH = "osgi.arch"; //$NON-NLS-1$

  private static final String PROP_OS = "osgi.os"; //$NON-NLS-1$

  private static final String PROP_WS = "osgi.ws"; //$NON-NLS-1$

  private String id;

  private ITargetDefinition targetDefinition;

  private final EList<Targlet> targlets = new BasicEList<Targlet>();

  public TargletContainer(String id)
  {
    this.id = id;

    // Make the Targlets UI active.
    Platform.getAdapterManager().loadAdapter(this, "org.eclipse.jface.viewers.ITreeContentProvider");
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
      TargletContainerListenerRegistryImpl.INSTANCE.notifyListeners(new IDChangedEvent(this, descriptor, oldID), new NullProgressMonitor());
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
    TargletContainerListenerRegistryImpl.INSTANCE.notifyListeners(new TargletsChangedEvent(this, descriptor), new NullProgressMonitor());
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
    return descriptor.getPoolLocation().getAbsolutePath();
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

      Profile profile = descriptor.getWorkingProfile();
      if (profile == null || //
          !descriptor.getWorkingDigest().equals(digest) && descriptor.getUpdateProblem() == null || //
          FORCE_UPDATE.get() == Boolean.TRUE)
      {
        try
        {
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
        TargetBundle bundle = new TargetBundle(file);
        bundles.add(bundle);
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
        TargetFeature feature = new TargetFeature(file);
        features.add(feature);
      }
    }
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
        if (targetLocation instanceof TargletContainer)
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

    try
    {
      ProfileDefinition profileDefinition = transaction.getProfileDefinition();
      profileDefinition.setIncludeSourceBundles(isIncludeSources());

      final EList<Requirement> rootRequirements = profileDefinition.getRequirements();
      rootRequirements.clear();

      for (Targlet targlet : targlets)
      {
        rootRequirements.addAll(EcoreUtil.copyAll(targlet.getRequirements()));
      }

      if (rootRequirements.isEmpty())
      {
        // TODO Should descriptor.rollbackUpdateTransaction() be called?
        return null;
      }

      final WorkspaceIUAnalyzer workspaceIUAnalyzer = new WorkspaceIUAnalyzer(rootRequirements);
      final Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = workspaceIUAnalyzer.getWorkspaceIUInfos();

      EList<Repository> repositories = profileDefinition.getRepositories();
      repositories.clear();

      for (Targlet targlet : targlets)
      {
        EList<IUGenerator> effectiveIUGenerators = effectiveIUGenerators(targlet);

        for (SourceLocator sourceLocator : targlet.getSourceLocators())
        {
          workspaceIUAnalyzer.analyze(sourceLocator, effectiveIUGenerators, progress.newChild());
        }

        repositories.addAll(EcoreUtil.copyAll(targlet.getActiveRepositories()));
      }

      TargletsCorePlugin.INSTANCE.coreException(workspaceIUAnalyzer.getStatus());

      TargletCommitContext commitContext = new TargletCommitContext(profile, workspaceIUAnalyzer);
      transaction.commit(commitContext, progress.newChild());

      Map<IInstallableUnit, WorkspaceIUInfo> requiredProjects = getRequiredProjects(profile, workspaceIUInfos, progress.newChild());
      descriptor.commitUpdateTransaction(digest, requiredProjects.values(), progress.newChild());

      ProfileUpdateSucceededEvent event = new ProfileUpdateSucceededEvent(this, descriptor, profile, commitContext.getMetadataRepositories(),
          commitContext.getProvisioningPlan(), requiredProjects);
      TargletContainerListenerRegistryImpl.INSTANCE.notifyListeners(event, progress.newChild());

      monitor.subTask("Targlet container profile update completed");
    }
    catch (Throwable t)
    {
      descriptor.rollbackUpdateTransaction(t, progress.isCanceled() ? new NullProgressMonitor() : progress.newChild());

      UpdateProblem updateProblem = descriptor.getUpdateProblem();
      if (updateProblem != null)
      {
        TargletContainerListenerRegistryImpl.INSTANCE.notifyListeners(new ProfileUpdateFailedEvent(this, descriptor, updateProblem),
            progress.isCanceled() ? new NullProgressMonitor() : progress.newChild());
      }

      TargletsCorePlugin.INSTANCE.coreException(t);
    }

    progress.done();
    return profile;
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
        result.put(iu, info);
      }
    }

    return result;
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

    private List<IMetadataRepository> metadataRepositories;

    public TargletCommitContext(Profile profile, WorkspaceIUAnalyzer workspaceIUAnalyzer)
    {
      this.profile = profile;
      this.workspaceIUAnalyzer = workspaceIUAnalyzer;
    }

    public IProvisioningPlan getProvisioningPlan()
    {
      return provisioningPlan;
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

            List<IInstallableUnit> ius = new ArrayList<IInstallableUnit>();
            Map<String, IInstallableUnit> idToIUMap = new HashMap<String, IInstallableUnit>();
            generateWorkspaceSourceIUs(ius, workspaceIUVersions, idToIUMap, monitor);

            ius.add(createPDETargetPlatformIU());

            IQueryResult<IInstallableUnit> metadataResult = super.getMetadata(monitor).query(QueryUtil.createIUAnyQuery(), monitor);
            Set<IRequirement> licenseRequirements = new HashSet<IRequirement>();
            for (IInstallableUnit iu : P2Util.asIterable(metadataResult))
            {
              TargletsCorePlugin.checkCancelation(monitor);

              ius.add(iu);

              // If the binary IU corresponds to a synthetic source IU...
              String id = iu.getId();
              IInstallableUnit workspaceIU = idToIUMap.get(id);
              if (workspaceIU != null)
              {
                // Ensure that if this binary IU is resolved that the corresponding source file is imported in the workspace.
                WorkspaceIUInfo info = workspaceIUInfos.get(workspaceIU);
                workspaceIUInfos.put(iu, info);

                // And that binary IU is in the qualifier range of the synthetic IU.
                if (P2Factory.eINSTANCE.createVersionRange(workspaceIU.getVersion(), VersionSegment.MICRO).isIncluded(iu.getVersion()))
                {
                  // We can remove our synthetic IU to ensure that, whenever possible, a binary resolution for it is included in the TP.
                  ius.remove(workspaceIU);
                }

                // If there this workspace IU has a license...
                String licenseFeatureID = workspaceIU.getProperty(FeatureGenerator.PROP_REQUIRED_LICENCSE_FEATURE_ID);
                if (licenseFeatureID != null)
                {
                  // Keep a requirement for this IU because binary IUs are generally not installed for license feature dependencies.
                  VersionRange versionRange = new VersionRange(workspaceIU.getProperty(FeatureGenerator.PROP_REQUIRED_LICENCSE_FEATURE_VERSION_RANGE));
                  IRequirement requirement = MetadataFactory.createRequirement(IInstallableUnit.NAMESPACE_IU_ID, licenseFeatureID, versionRange, null, false,
                      false);
                  licenseRequirements.add(requirement);
                }
              }
            }

            // If we need license requirements.
            if (!licenseRequirements.isEmpty())
            {
              // Build an artificial unit that requires all the license features.
              InstallableUnitDescription requiredLicensesDescription = new InstallableUnitDescription();
              requiredLicensesDescription.setId("required_licenses");
              requiredLicensesDescription.setVersion(Version.createOSGi(1, 0, 0));
              requiredLicensesDescription.setArtifacts(new IArtifactKey[0]);
              requiredLicensesDescription.setProperty(InstallableUnitDescription.PROP_TYPE_GROUP, Boolean.TRUE.toString());
              requiredLicensesDescription.setCapabilities(new IProvidedCapability[] { MetadataFactory.createProvidedCapability(
                  IInstallableUnit.NAMESPACE_IU_ID, requiredLicensesDescription.getId(), requiredLicensesDescription.getVersion()) });
              requiredLicensesDescription.addRequirements(licenseRequirements);

              IInstallableUnit requiredLicensesIU = MetadataFactory.createInstallableUnit(requiredLicensesDescription);
              ius.add(requiredLicensesIU);

              profileChangeRequest.add(requiredLicensesIU);
            }

            metadata = new CollectionResult<IInstallableUnit>(ius);
          }

          return metadata;
        }

        private void generateWorkspaceSourceIUs(List<IInstallableUnit> ius, Map<String, Version> ids, Map<String, IInstallableUnit> idToIUMap,
            IProgressMonitor monitor)
        {
          Map<IInstallableUnit, WorkspaceIUInfo> workspaceSourceIUInfos = new HashMap<IInstallableUnit, WorkspaceIUInfo>();
          Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = workspaceIUAnalyzer.getWorkspaceIUInfos();

          for (IInstallableUnit iu : workspaceIUInfos.keySet())
          {
            TargletsCorePlugin.checkCancelation(monitor);

            String id = iu.getId();
            ius.add(iu);
            idToIUMap.put(id, iu);

            String suffix = "";
            if (id.endsWith(FEATURE_SUFFIX))
            {
              id = id.substring(0, id.length() - FEATURE_SUFFIX.length());
              suffix = FEATURE_SUFFIX;
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
              String value = property.getValue();

              if ("org.eclipse.equinox.p2.name".equals(key))
              {
                value = "Source for " + value;
              }

              description.setProperty(key, value);
            }

            description.setProperty(IU_PROPERTY_SOURCE, Boolean.TRUE.toString());
            description.addProvidedCapabilities(Collections.singleton(MetadataFactory.createProvidedCapability(IInstallableUnit.NAMESPACE_IU_ID,
                description.getId(), description.getVersion())));

            IInstallableUnit workspaceSourceIU = MetadataFactory.createInstallableUnit(description);
            ius.add(workspaceSourceIU);
            ids.put(workspaceSourceID, version);

            idToIUMap.put(workspaceSourceID, workspaceSourceIU);
            WorkspaceIUInfo info = workspaceIUInfos.get(iu);
            workspaceSourceIUInfos.put(workspaceSourceIU, info);
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
    public boolean handleProvisioningPlan(IProvisioningPlan provisioningPlan, Map<IInstallableUnit, DeltaType> iuDeltas,
        Map<IInstallableUnit, Map<String, Pair<Object, Object>>> propertyDeltas, List<IMetadataRepository> metadataRepositories) throws CoreException
    {
      this.provisioningPlan = provisioningPlan;
      this.metadataRepositories = metadataRepositories;
      return true;
    }

    @Override
    public IPhaseSet getPhaseSet(ProfileTransaction transaction)
    {
      List<Phase> phases = new ArrayList<Phase>(4);
      phases.add(new Collect(100));
      phases.add(new Property(1));
      phases.add(new Uninstall(50, true));
      phases.add(new Install(50));
      phases.add(new CollectNativesPhase(profile.getBundlePool(), 100));

      return new PhaseSet(phases.toArray(new Phase[phases.size()]));
    }

    private static IInstallableUnit createPDETargetPlatformIU()
    {
      InstallableUnitDescription description = new InstallableUnitDescription();
      description.setId(A_PDE_TARGET_PLATFORM_LOWER_CASE);
      Version version = Version.createOSGi(1, 0, 0);
      description.setVersion(version);
      description.addProvidedCapabilities(Collections.singleton(MetadataFactory.createProvidedCapability(A_PDE_TARGET_PLATFORM,
          "Cannot be installed into the IDE", version)));
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

    public TargletContainer getTargetLocation(String type, String serializedXML) throws CoreException
    {
      if (TYPE.equals(type))
      {
        return fromXML(serializedXML);
      }

      return null;
    }

    public static TargletContainer fromXML(String xml) throws CoreException
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
}
