/**
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.targlets.PluginGenerator;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.targlets.util.VersionGenerator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.publisher.PublisherInfo;
import org.eclipse.equinox.p2.publisher.eclipse.BundlesAction;
import org.eclipse.osgi.service.resolver.BundleDescription;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Plugin Generator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class PluginGeneratorImpl extends ModelElementImpl implements PluginGenerator
{
  private static final IPath MANIFEST_PATH = new Path(JarFile.MANIFEST_NAME);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PluginGeneratorImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return TargletPackage.Literals.PLUGIN_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public IInstallableUnit generateIU(IProject project, final String qualifierReplacement, final Map<String, Version> iuVersions) throws Exception
  {
    final IInstallableUnit[] result = { null };

    ResourcesUtil.runWithFile(project, MANIFEST_PATH, new ResourcesUtil.RunnableWithFile()
    {
      public void run(File projectFolder, File file) throws Exception
      {
        result[0] = BundleGeneratorAction.INSTANCE.generateIU(projectFolder, qualifierReplacement, iuVersions);
      }
    });

    return result[0];
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void modifyIU(IInstallableUnit iu, IProject project, String qualifierReplacement, Map<String, Version> iuVersions) throws Exception
  {
    // Do nothing
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  @SuppressWarnings("unchecked")
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case TargletPackage.PLUGIN_GENERATOR___GENERATE_IU__IPROJECT_STRING_MAP:
        try
        {
          return generateIU((IProject)arguments.get(0), (String)arguments.get(1), (Map<String, Version>)arguments.get(2));
        }
        catch (Throwable throwable)
        {
          throw new InvocationTargetException(throwable);
        }
      case TargletPackage.PLUGIN_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP:
        try
        {
          modifyIU((IInstallableUnit)arguments.get(0), (IProject)arguments.get(1), (String)arguments.get(2), (Map<String, Version>)arguments.get(3));
          return null;
        }
        catch (Throwable throwable)
        {
          throw new InvocationTargetException(throwable);
        }
    }
    return super.eInvoke(operationID, arguments);
  }

  /**
   * @author Eike Stepper
   */
  private static final class BundleGeneratorAction extends BundlesAction
  {
    public static final BundleGeneratorAction INSTANCE = new BundleGeneratorAction();

    private BundleGeneratorAction()
    {
      super((File[])null);
      setPublisherInfo(new PublisherInfo());
    }

    public IInstallableUnit generateIU(File projectFolder, String qualifierReplacement, Map<String, Version> ius) throws Exception
    {
      Dictionary<String, String> manifest = loadManifest(projectFolder);
      if (manifest == null)
      {
        return null;
      }

      String version = manifest.get(org.osgi.framework.Constants.BUNDLE_VERSION);
      manifest.put(org.osgi.framework.Constants.BUNDLE_VERSION, VersionGenerator.replaceQualifier(version, qualifierReplacement));

      BundleDescription description = createBundleDescription(manifest, projectFolder);
      if (description == null)
      {
        return null;
      }

      IInstallableUnit iu = createBundleIU(description, null, info);
      if (iu instanceof InstallableUnit)
      {
        ((InstallableUnit)iu).setArtifacts(new IArtifactKey[0]);
      }

      return iu;
    }
  }

} // PluginGeneratorImpl
