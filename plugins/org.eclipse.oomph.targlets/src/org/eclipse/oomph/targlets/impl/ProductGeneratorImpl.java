/**
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ProductGenerator;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.util.Predicate;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.internal.p2.publisher.eclipse.IProductDescriptor;
import org.eclipse.equinox.internal.p2.publisher.eclipse.ProductFile;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IVersionedId;
import org.eclipse.equinox.p2.metadata.Version;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Product Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ProductGeneratorImpl extends ModelElementImpl implements ProductGenerator
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProductGeneratorImpl()
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
    return TargletPackage.Literals.PRODUCT_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void generateIUs(IProject project, final String qualifierReplacement, Map<String, Version> iuVersions, final EList<IInstallableUnit> result)
      throws Exception
  {
    Predicate<IFile> filter = new Predicate<IFile>()
    {
      public boolean apply(IFile file)
      {
        return "product".equals(file.getFileExtension());
      }
    };

    ResourcesUtil.runWithFiles(project, Path.EMPTY, filter, new ResourcesUtil.RunnableWithFile()
    {
      public void run(File projectFolder, File file) throws Exception
      {
        IProductDescriptor productDescriptor = new ProductFile(file.getAbsolutePath());

        ComponentDefinition componentDefinition = TargletFactory.eINSTANCE.createComponentDefinition();
        componentDefinition.setID(productDescriptor.getId());
        componentDefinition.setVersion(Version.create(productDescriptor.getVersion()));

        if (productDescriptor.useFeatures())
        {
          addRequirements(componentDefinition, productDescriptor.getFeatures(), Requirement.FEATURE_SUFFIX);
        }
        else
        {
          addRequirements(componentDefinition, productDescriptor.getBundles(false), "");
        }

        addRequirements(componentDefinition, productDescriptor.getFragments(), "");

        IInstallableUnit iu = ComponentDefGeneratorImpl.generateIU(componentDefinition, qualifierReplacement);
        result.add(iu);
      }

      private void addRequirements(ComponentDefinition componentDefinition, List<IVersionedId> versionedIds, String idSuffix)
      {
        EList<Requirement> requirements = componentDefinition.getRequirements();

        for (IVersionedId versionedId : versionedIds)
        {
          String id = versionedId.getId() + idSuffix;
          Version version = versionedId.getVersion();

          Requirement requirement = P2Factory.eINSTANCE.createRequirement(id, version, true);
          requirements.add(requirement);
        }
      }
    });
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
      case TargletPackage.PRODUCT_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST:
        try
        {
          generateIUs((IProject)arguments.get(0), (String)arguments.get(1), (Map<String, Version>)arguments.get(2), (EList<IInstallableUnit>)arguments.get(3));
          return null;
        }
        catch (Throwable throwable)
        {
          throw new InvocationTargetException(throwable);
        }
    }
    return super.eInvoke(operationID, arguments);
  }

} // ProductGeneratorImpl
