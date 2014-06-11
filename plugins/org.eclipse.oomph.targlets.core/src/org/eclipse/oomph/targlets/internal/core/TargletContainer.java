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
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.util.HexUtil;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.SubMonitor;
import org.eclipse.oomph.util.XMLUtil;
import org.eclipse.oomph.util.XMLUtil.ElementHandler;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xmi.XMLOptions;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLOptionsImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.CollectionResult;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetLocationFactory;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.pde.core.target.LoadTargetDefinitionJob;
import org.eclipse.pde.core.target.TargetBundle;
import org.eclipse.pde.core.target.TargetFeature;
import org.eclipse.pde.internal.core.target.AbstractBundleContainer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class TargletContainer extends AbstractBundleContainer
{
  public static final String TYPE = "Targlet";

  private static final ThreadLocal<Boolean> FORCE_UPDATE = new ThreadLocal<Boolean>();

  private static final ThreadLocal<Boolean> OFFLINE = new ThreadLocal<Boolean>();

  private static final ThreadLocal<Boolean> MIRRORS = new ThreadLocal<Boolean>();

  private static final String FOLLOW_ARTIFACT_REPOSITORY_REFERENCES = "org.eclipse.equinox.p2.director.followArtifactRepositoryReferences";

  private static final String FEATURE_SUFFIX = ".feature.group";

  private static final byte[] BUFFER = new byte[8192];

  private static final String PROP_ARCH = "osgi.arch"; //$NON-NLS-1$

  private static final String PROP_OS = "osgi.os"; //$NON-NLS-1$

  private static final String PROP_WS = "osgi.ws"; //$NON-NLS-1$

  private final String id;

  private ITargetDefinition target;

  private EList<Targlet> targlets = new BasicEList<Targlet>();

  public TargletContainer(String id)
  {
    this.id = id;

    // Make the Targlets UI active
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

  public String getID()
  {
    return id;
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
      TargletContainerManager manager = TargletContainerManager.getInstance();
      return manager.getDescriptor(id, new NullProgressMonitor());
    }
    catch (Exception ex)
    {
      TargletsCorePlugin.INSTANCE.log(ex);
      return null;
    }
  }

  public ITargetDefinition getTarget()
  {
    return target;
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

    TargletContainerDescriptor descriptor = getDescriptor();
    if (descriptor != null)
    {
      descriptor.resetUpdateProblem();
    }

    ITargetPlatformService service = null;

    try
    {
      service = TargletsCorePlugin.INSTANCE.getService(ITargetPlatformService.class);
      service.saveTargetDefinition(target);
    }
    finally
    {
      TargletsCorePlugin.INSTANCE.ungetService(service);
    }
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

    this.targlets = TargletFactory.eINSTANCE.copyTarglets(targlets);
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
    String ws = target.getWS();
    if (ws == null)
    {
      ws = Platform.getWS();
    }

    builder.append(PROP_WS);
    builder.append("="); //$NON-NLS-1$
    builder.append(ws);
    builder.append(","); //$NON-NLS-1$
    String os = target.getOS();
    if (os == null)
    {
      os = Platform.getOS();
    }

    builder.append(PROP_OS);
    builder.append("="); //$NON-NLS-1$
    builder.append(os);
    builder.append(","); //$NON-NLS-1$
    String arch = target.getArch();
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
    String nl = target.getNL();
    if (nl == null)
    {
      nl = Platform.getNL();
    }

    return nl;
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    // for (Targlet targlet : targlets)
    // {
    // if (builder.length() == 0)
    // {
    // builder.append(" for ");
    // }
    // else
    // {
    // builder.append(", ");
    // }
    //
    // builder.append(targlet.getName());
    // }

    return "Targlet Container" + builder;
  }

  @Override
  public String getLocation(boolean resolve) throws CoreException
  {
    TargletContainerManager manager = TargletContainerManager.getInstance();
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
    this.target = target;
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

  @Override
  protected TargetBundle[] resolveBundles(ITargetDefinition target, IProgressMonitor monitor) throws CoreException
  {
    resolveUnits(monitor);
    return fBundles;
  }

  @Override
  protected TargetFeature[] resolveFeatures(ITargetDefinition target, IProgressMonitor monitor) throws CoreException
  {
    // All work has been done in resolveBundles() already
    return fFeatures;
  }

  private void resolveUnits(IProgressMonitor monitor) throws CoreException
  {
    try
    {
      SubMonitor progress = SubMonitor.convert(monitor, 100).detectCancelation();
      TargletContainerManager manager = TargletContainerManager.getInstance();

      String environmentProperties = getEnvironmentProperties();
      String nlProperty = getNLProperty();
      String digest = createDigest(id, environmentProperties, nlProperty, targlets);

      Profile profile = null;

      TargletContainerDescriptor descriptor = manager.getDescriptor(id, progress.newChild(2));
      progress.childDone();
      String workingDigest = descriptor.getWorkingDigest();
      if (workingDigest != null)
      {
        profile = descriptor.getProfile(workingDigest, progress.newChild(2));
        progress.childDone();
      }
      else
      {
        progress.skipped(2);
      }

      if (profile == null || //
          !workingDigest.equals(digest) && descriptor.getUpdateProblem() == null || //
          FORCE_UPDATE.get() == Boolean.TRUE)
      {
        try
        {
          Profile newProfile = updateProfile(environmentProperties, nlProperty, digest, progress.newChild(86));
          if (newProfile != null)
          {
            profile = newProfile;
          }
        }
        catch (CoreException ex)
        {
          if (profile == null)
          {
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

  public void forceUpdate(boolean offline, boolean mirrors, IProgressMonitor monitor) throws CoreException
  {
    try
    {
      FORCE_UPDATE.set(Boolean.TRUE);
      OFFLINE.set(offline ? Boolean.TRUE : false);
      MIRRORS.set(mirrors ? Boolean.TRUE : false);

      IStatus status = target.resolve(monitor);
      if (!status.isOK())
      {
        TargletsCorePlugin.INSTANCE.coreException(new CoreException(status));
      }
    }
    finally
    {
      MIRRORS.remove();
      OFFLINE.remove();
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

    TargletContainerManager manager = TargletContainerManager.getInstance();
    TargletContainerDescriptor descriptor = manager.getDescriptor(id, progress.newChild());

    final Profile profile = descriptor.startUpdateTransaction(environmentProperties, nlProperty, digest, progress.newChild());

    ProfileTransaction transaction = profile.change().setRemoveExistingInstallableUnits(true);
    transaction.setOffline(OFFLINE.get() == Boolean.TRUE);
    transaction.setMirrors(MIRRORS.get() == Boolean.TRUE);

    try
    {
      ProfileDefinition profileDefinition = transaction.getProfileDefinition();
      profileDefinition.setIncludeSourceBundles(isIncludeSources());

      EList<Requirement> roots = profileDefinition.getRequirements();
      roots.clear();

      for (Targlet targlet : targlets)
      {
        roots.addAll(EcoreUtil.copyAll(targlet.getRequirements()));
      }

      if (roots.isEmpty())
      {
        return null;
      }

      final IUAnalyzer analyzer = new IUAnalyzer();
      final Map<IInstallableUnit, File> sources = new HashMap<IInstallableUnit, File>();

      EList<Repository> repositories = profileDefinition.getRepositories();
      repositories.clear();

      for (Targlet targlet : targlets)
      {
        for (SourceLocator sourceLocator : targlet.getSourceLocators())
        {
          File rootFolder = new File(sourceLocator.getRootFolder());
          EList<Predicate> predicates = sourceLocator.getPredicates();
          boolean locateNestedProjects = sourceLocator.isLocateNestedProjects();

          Map<IInstallableUnit, File> ius = analyzer.analyze(rootFolder, predicates, locateNestedProjects, progress.newChild());
          sources.putAll(ius);
        }

        repositories.addAll(EcoreUtil.copyAll(targlet.getActiveRepositories()));
      }

      final AtomicReference<IProvisioningPlan> provisioningPlanRef = new AtomicReference<IProvisioningPlan>();
      final AtomicReference<List<IMetadataRepository>> metadataRepositoriesRef = new AtomicReference<List<IMetadataRepository>>();

      transaction.commit(new ProfileTransaction.CommitContext()
      {
        @Override
        public ProvisioningContext createProvisioningContext(ProfileTransaction transaction)
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
                Set<String> sourceIDs = analyzer.getIDs();

                List<IInstallableUnit> ius = new ArrayList<IInstallableUnit>();
                prepareSources(ius, sourceIDs, monitor);

                IQueryResult<IInstallableUnit> metadataResult = super.getMetadata(monitor).query(QueryUtil.createIUAnyQuery(), monitor);
                for (Iterator<IInstallableUnit> it = metadataResult.iterator(); it.hasNext();)
                {
                  TargletsCorePlugin.checkCancelation(monitor);

                  IInstallableUnit iu = it.next();
                  if (!sourceIDs.contains(iu.getId()))
                  {
                    ius.add(iu);
                  }
                }

                metadata = new CollectionResult<IInstallableUnit>(ius);
              }

              return metadata;
            }

            private void prepareSources(List<IInstallableUnit> ius, Set<String> ids, IProgressMonitor monitor)
            {
              for (IInstallableUnit iu : sources.keySet())
              {
                TargletsCorePlugin.checkCancelation(monitor);
                ius.add(iu);

                // TODO Should we create source IUs for source projects only if needed (i.e. required by feature content)?
                String id = iu.getId();
                String suffix = "";

                if (id.endsWith(FEATURE_SUFFIX))
                {
                  id = id.substring(0, id.length() - FEATURE_SUFFIX.length());
                  suffix = FEATURE_SUFFIX;
                }

                InstallableUnitDescription description = new MetadataFactory.InstallableUnitDescription();
                description.setId(id + ".source" + suffix);
                description.setVersion(iu.getVersion());

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

                description.addProvidedCapabilities(Collections.singleton(MetadataFactory.createProvidedCapability(IInstallableUnit.NAMESPACE_IU_ID,
                    description.getId(), description.getVersion())));

                IInstallableUnit sourceIU = MetadataFactory.createInstallableUnit(description);
                ius.add(sourceIU);
                ids.add(sourceIU.getId());
              }
            }
          };

          provisioningContext.setProperty(ProvisioningContext.FOLLOW_REPOSITORY_REFERENCES, Boolean.FALSE.toString());
          provisioningContext.setProperty(FOLLOW_ARTIFACT_REPOSITORY_REFERENCES, Boolean.FALSE.toString());
          return provisioningContext;
        }

        @Override
        public void handleProvisioningPlan(IProvisioningPlan provisioningPlan, List<IMetadataRepository> metadataRepositories) throws CoreException
        {
          provisioningPlanRef.set(provisioningPlan);
          metadataRepositoriesRef.set(metadataRepositories);
        }

        @Override
        public IPhaseSet getPhaseSet(ProfileTransaction transaction)
        {
          return TargletContainerManager.createPhaseSet(profile.getBundlePool());
        }
      }, progress.newChild());

      Set<File> projectLocations = getProjectLocations(profile, sources, progress.newChild());
      descriptor.commitUpdateTransaction(digest, projectLocations, progress.newChild());

      IProvisioningPlan provisioningPlan = provisioningPlanRef.get();
      List<IMetadataRepository> metadataRepositories = metadataRepositoriesRef.get();

      for (Targlet targlet : targlets)
      {
        TargletListenerRegistryImpl.INSTANCE.notifyProfileUpdate(targlet, profile, metadataRepositories, provisioningPlan, sources);
      }
    }
    catch (Throwable t)
    {
      descriptor.rollbackUpdateTransaction(t, monitor);
      TargletsCorePlugin.INSTANCE.log(t);
      TargletsCorePlugin.INSTANCE.coreException(t);
    }

    progress.done();
    return profile;
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

  private static Set<File> getProjectLocations(IProfile profile, Map<IInstallableUnit, File> sources, IProgressMonitor monitor)
  {
    Set<File> projectLocations = new HashSet<File>();
    for (IInstallableUnit iu : profile.query(QueryUtil.createIUAnyQuery(), monitor))
    {
      File folder = sources.get(iu);
      if (folder != null)
      {
        projectLocations.add(folder);
      }
    }

    return projectLocations;
  }

  public static void updateWorkspace(IProgressMonitor monitor) throws CoreException
  {
    ITargetPlatformService service = null;

    try
    {
      service = TargletsCorePlugin.INSTANCE.getService(ITargetPlatformService.class);
      Set<File> projectLocations = new HashSet<File>();
      ITargetDefinition target = null;

      try
      {
        target = service.getWorkspaceTargetDefinition();
      }
      catch (NoSuchMethodError ex)
      {
        // Handle gracefully that getWorkspaceTargetDefinition() has been added in Eclipse 4.4
        ITargetHandle handle = service.getWorkspaceTargetHandle();
        if (handle != null)
        {
          target = handle.getTargetDefinition();
        }
      }

      if (target != null)
      {
        ITargetLocation[] targetLocations = target.getTargetLocations();
        if (targetLocations != null)
        {
          for (ITargetLocation location : targetLocations)
          {
            if (location instanceof TargletContainer)
            {
              TargletContainer container = (TargletContainer)location;
              TargletContainerDescriptor descriptor = container.getDescriptor();
              if (descriptor != null)
              {
                Set<File> workingProjects = descriptor.getWorkingProjects();
                if (workingProjects != null)
                {
                  projectLocations.addAll(workingProjects);
                }
              }
            }
          }
        }
      }

      updateWorkspace(projectLocations, monitor);
    }
    catch (Exception ex)
    {
      TargletsCorePlugin.INSTANCE.coreException(ex);
    }
    finally
    {
      TargletsCorePlugin.INSTANCE.ungetService(service);
    }
  }

  private static void updateWorkspace(final Set<File> projectLocations, IProgressMonitor monitor) throws CoreException
  {
    final IWorkspace workspace = ResourcesPlugin.getWorkspace();
    workspace.run(new IWorkspaceRunnable()
    {
      public void run(IProgressMonitor monitor) throws CoreException
      {
        try
        {
          DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
          IWorkspaceRoot root = workspace.getRoot();

          for (File folder : projectLocations)
          {
            try
            {
              final AtomicReference<String> projectName = new AtomicReference<String>();

              Element rootElement = XMLUtil.loadRootElement(documentBuilder, new File(folder, ".project"));
              XMLUtil.handleChildElements(rootElement, new ElementHandler()
              {
                public void handleElement(Element element) throws Exception
                {
                  if ("name".equals(element.getTagName()))
                  {
                    projectName.set(element.getTextContent().trim());
                  }
                }
              });

              String name = projectName.get();
              if (name != null && name.length() != 0)
              {
                File location = folder.getCanonicalFile();

                IProject project = root.getProject(name);
                if (project.exists())
                {
                  File existingLocation = new File(project.getLocation().toOSString()).getCanonicalFile();
                  if (!existingLocation.equals(location))
                  {
                    TargletsCorePlugin.INSTANCE.log("Project " + name + " exists in different location: " + existingLocation);
                    continue;
                  }
                }
                else
                {
                  monitor.setTaskName("Importing project " + projectName);

                  IProjectDescription projectDescription = workspace.newProjectDescription(name);
                  projectDescription.setLocation(new Path(location.getAbsolutePath()));
                  project.create(projectDescription, monitor);
                }

                if (!project.isOpen())
                {
                  project.open(monitor);
                }
              }
            }
            catch (Exception ex)
            {
              TargletsCorePlugin.INSTANCE.log(ex);
              monitor.subTask("Failed to import project from " + folder + " (see error log for details)");
            }
          }
        }
        catch (Exception ex)
        {
          TargletsCorePlugin.INSTANCE.coreException(ex);
        }
      }
    }, monitor);
  }

  static
  {
    Job.getJobManager().addJobChangeListener(new JobChangeAdapter()
    {
      @Override
      public void done(IJobChangeEvent event)
      {
        if (event.getJob() instanceof LoadTargetDefinitionJob)
        {
          new Job("Import projects")
          {
            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
              try
              {
                updateWorkspace(monitor);
                return Status.OK_STATUS;
              }
              catch (Exception ex)
              {
                return TargletsCorePlugin.INSTANCE.getStatus(ex);
              }
            }
          }.schedule();
        }
      }
    });
  }

  /**
   * @author Eike Stepper
   */
  public static class Persistence implements ITargetLocationFactory
  {
    private static final String LOCATION = "location";

    private static final String LOCATION_ID = "id";

    private static final String LOCATION_TYPE = "type";

    private static final String TARGLET = "targlet";

    private static final Map<Object, Object> XML_OPTIONS;

    static
    {
      XMLOptions xmlOptions = new XMLOptionsImpl();
      xmlOptions.setProcessAnyXML(true);
      xmlOptions.setProcessSchemaLocations(true);

      Map<Object, Object> options = new HashMap<Object, Object>();
      options.put(XMLResource.OPTION_DECLARE_XML, Boolean.FALSE);
      options.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
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
        if (!xml.contains("xmlns:xsi"))
        {
          return Compatibility.fromXML(xml);
        }

        XMLResource resource = new XMLResourceImpl();
        resource.load(new InputSource(new StringReader(xml)), XML_OPTIONS);

        EObject documentRoot = resource.getContents().get(0);
        AnyType location = (AnyType)documentRoot.eContents().get(0);
        String id = (String)location.eGet(ExtendedMetaData.INSTANCE.demandFeature(null, LOCATION_ID, false));

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
      EStructuralFeature locationFeature = ExtendedMetaData.INSTANCE.demandFeature(null, LOCATION, true);
      EStructuralFeature targletFeature = ExtendedMetaData.INSTANCE.demandFeature(null, TARGLET, true);

      AnyType location = XMLTypeFactory.eINSTANCE.createAnyType();
      location.eSet(ExtendedMetaData.INSTANCE.demandFeature(null, LOCATION_ID, false), id);
      location.eSet(ExtendedMetaData.INSTANCE.demandFeature(null, LOCATION_TYPE, false), TYPE);

      EClass documentRootClass = locationFeature.getEContainingClass();
      EObject documentRoot = EcoreUtil.create(documentRootClass);
      documentRoot.eSet(locationFeature, location);

      FeatureMap targletFeatureMap = location.getAny();
      FeatureMapUtil.addText(targletFeatureMap, "\n  ");

      EList<Targlet> copy = TargletFactory.eINSTANCE.copyTarglets(targlets);
      for (Targlet targlet : copy)
      {
        targletFeatureMap.add(targletFeature, targlet);
      }

      StringWriter writer = new StringWriter();

      XMLResource resource = new XMLResourceImpl();
      resource.getContents().add(documentRoot);
      resource.save(writer, XML_OPTIONS);

      return writer;
    }

    /**
     * Reads and writes the old, DOM-based format.
     *
     * @deprecated Kept for backwards compatibility.
     *
     * @author Eike Stepper
     */
    @Deprecated
    private static final class Compatibility
    {
      private static final String TARGLET_NAME = "name";

      private static final String TARGLET_ACTIVE_REPOSITORY_LIST = "activeRepositoryList";

      private static final String TARGLET_INCLUDE_SOURCES = "includeSources";

      private static final String TARGLET_INCLUDE_ALL_PLATFORMS = "includeAllPlatforms";

      private static final String REQUIREMENT = "root";

      private static final String REQUIREMENT_ID = "id";

      private static final String REQUIREMENT_VERSION_RANGE = "versionRange";

      private static final String SOURCE_LOCATOR = "sourceLocator";

      private static final String SOURCE_LOCATOR_ROOT_FOLDER = "rootFolder";

      private static final String SOURCE_LOCATOR_LOCATE_NESTED_PROJECTS = "locateNestedProjects";

      private static final String PREDICATE = "predicate";

      private static final String PREDICATE_HEX = "hex";

      private static final String REPOSITORY_LIST = "repositoryList";

      private static final String REPOSITORY_LIST_NAME = "name";

      private static final String REPOSITORY = "repository";

      private static final String REPOSITORY_URL = "url";

      public static TargletContainer fromXML(String xml) throws Exception
      {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

        Element containerElement = document.getDocumentElement();
        if (containerElement != null)
        {
          String locationType = containerElement.getAttribute(LOCATION_TYPE);
          if (locationType.equals(TYPE))
          {
            String locationID = containerElement.getAttribute(LOCATION_ID);
            final EList<Targlet> targlets = new BasicEList<Targlet>();

            XMLUtil.handleChildElements(containerElement, new ElementHandler()
            {
              public void handleElement(Element targletElement) throws Exception
              {
                final Targlet targlet = TargletFactory.eINSTANCE.createTarglet();
                targlet.setName(targletElement.getAttribute(TARGLET_NAME));
                targlet.setActiveRepositoryList(targletElement.getAttribute(TARGLET_ACTIVE_REPOSITORY_LIST));
                targlet.setIncludeSources(Boolean.valueOf(targletElement.getAttribute(TARGLET_INCLUDE_SOURCES)));
                targlet.setIncludeAllPlatforms( //
                    Boolean.valueOf(targletElement.getAttribute(TARGLET_INCLUDE_ALL_PLATFORMS)));
                targlets.add(targlet);

                XMLUtil.handleChildElements(targletElement, new ElementHandler()
                {
                  public void handleElement(Element childElement) throws Exception
                  {
                    String tag = childElement.getTagName();
                    if (REQUIREMENT.equals(tag))
                    {
                      Requirement root = P2Factory.eINSTANCE.createRequirement(childElement.getAttribute(REQUIREMENT_ID),
                          new VersionRange(childElement.getAttribute(REQUIREMENT_VERSION_RANGE)));
                      targlet.getRequirements().add(root);
                    }
                    else if (SOURCE_LOCATOR.equals(tag))
                    {
                      final SourceLocator sourceLocator = ResourcesFactory.eINSTANCE.createSourceLocator();
                      sourceLocator.setRootFolder(childElement.getAttribute(SOURCE_LOCATOR_ROOT_FOLDER));
                      sourceLocator.setLocateNestedProjects(Boolean.valueOf(childElement.getAttribute(SOURCE_LOCATOR_LOCATE_NESTED_PROJECTS)));
                      targlet.getSourceLocators().add(sourceLocator);

                      XMLUtil.handleChildElements(childElement, new ElementHandler()
                      {
                        public void handleElement(Element predicateElement) throws Exception
                        {
                          String hex = predicateElement.getAttribute(PREDICATE_HEX);
                          Predicate predicate = fromHex(hex);
                          sourceLocator.getPredicates().add(predicate);
                        }
                      });
                    }
                    else if (REPOSITORY_LIST.equals(tag))
                    {
                      final RepositoryList repositoryList = P2Factory.eINSTANCE.createRepositoryList();
                      repositoryList.setName(childElement.getAttribute(REPOSITORY_LIST_NAME));
                      targlet.getRepositoryLists().add(repositoryList);

                      XMLUtil.handleChildElements(childElement, new ElementHandler()
                      {
                        public void handleElement(Element repositoryElement) throws Exception
                        {
                          Repository repository = P2Factory.eINSTANCE.createRepository(repositoryElement.getAttribute(REPOSITORY_URL));
                          repositoryList.getRepositories().add(repository);
                        }
                      });
                    }
                  }
                });
              }
            });

            return new TargletContainer(locationID, targlets);
          }
        }

        return null;
      }

      private static <T extends EObject> T fromHex(String hex)
      {
        try
        {
          byte[] bytes = HexUtil.hexToBytes(hex);

          Resource resource = new BinaryResourceImpl();
          resource.load(new ByteArrayInputStream(bytes), null);

          @SuppressWarnings("unchecked")
          T root = (T)resource.getContents().get(0);
          resource.getContents().clear();
          return root;
        }
        catch (IOException ex)
        {
          throw new IORuntimeException(ex);
        }
      }

      @SuppressWarnings("unused")
      public static Writer toXML(String id, List<Targlet> targlets) throws ParserConfigurationException, TransformerException
      {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.newDocument();

        Element containerElement = document.createElement(LOCATION);
        containerElement.setAttribute(LOCATION_TYPE, TYPE);
        containerElement.setAttribute(LOCATION_ID, id);
        document.appendChild(containerElement);

        for (Targlet targlet : targlets)
        {
          Element targletElement = document.createElement(TARGLET);
          targletElement.setAttribute(TARGLET_NAME, targlet.getName());
          targletElement.setAttribute(TARGLET_ACTIVE_REPOSITORY_LIST, targlet.getActiveRepositoryList());
          targletElement.setAttribute(TARGLET_INCLUDE_SOURCES, Boolean.toString(targlet.isIncludeSources()));
          targletElement.setAttribute(TARGLET_INCLUDE_ALL_PLATFORMS, Boolean.toString(targlet.isIncludeAllPlatforms()));
          containerElement.appendChild(targletElement);

          for (Requirement requirement : targlet.getRequirements())
          {
            Element rootElement = document.createElement(REQUIREMENT);
            rootElement.setAttribute(REQUIREMENT_ID, requirement.getID());
            rootElement.setAttribute(REQUIREMENT_VERSION_RANGE, requirement.getVersionRange().toString());
            targletElement.appendChild(rootElement);
          }

          for (SourceLocator sourceLocator : targlet.getSourceLocators())
          {
            Element sourceLocatorElement = document.createElement(SOURCE_LOCATOR);
            sourceLocatorElement.setAttribute(SOURCE_LOCATOR_ROOT_FOLDER, sourceLocator.getRootFolder());
            sourceLocatorElement.setAttribute(SOURCE_LOCATOR_LOCATE_NESTED_PROJECTS, Boolean.toString(sourceLocator.isLocateNestedProjects()));
            targletElement.appendChild(sourceLocatorElement);

            for (Predicate predicate : sourceLocator.getPredicates())
            {
              String hex = toHex(predicate);

              Element predicateElement = document.createElement(PREDICATE);
              predicateElement.setAttribute(PREDICATE_HEX, hex);
              sourceLocatorElement.appendChild(predicateElement);
            }
          }

          for (RepositoryList repositoryList : targlet.getRepositoryLists())
          {
            Element repositoryListElement = document.createElement(REPOSITORY_LIST);
            repositoryListElement.setAttribute(REPOSITORY_LIST_NAME, repositoryList.getName());
            targletElement.appendChild(repositoryListElement);

            for (Repository repository : repositoryList.getRepositories())
            {
              Element repositoryElement = document.createElement(REPOSITORY);
              repositoryElement.setAttribute(REPOSITORY_URL, repository.getURL());
              repositoryListElement.appendChild(repositoryElement);
            }
          }
        }

        StringWriter writer = new StringWriter();

        StreamResult result = new StreamResult(writer);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(document), result);

        return writer;
      }

      private static String toHex(EObject object)
      {
        try
        {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();

          Resource resource = new BinaryResourceImpl();
          resource.getContents().add(EcoreUtil.copy(object));
          resource.save(baos, null);

          return HexUtil.bytesToHex(baos.toByteArray());
        }
        catch (IOException ex)
        {
          throw new IORuntimeException(ex);
        }
      }
    }
  }
}
