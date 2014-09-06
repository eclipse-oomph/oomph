/**
 */
package org.eclipse.oomph.targlets;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;

import org.eclipse.core.resources.IProject;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Installable Unit Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.oomph.targlets.TargletPackage#getIUGenerator()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface IUGenerator extends ModelElement
{
  public static final EList<IUGenerator> DEFAULTS = ECollections.asEList(TargletFactory.eINSTANCE.createPluginGenerator(),
      TargletFactory.eINSTANCE.createFeatureGenerator(), TargletFactory.eINSTANCE.createComponentGenerator());

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model dataType="org.eclipse.oomph.targlets.InstallableUnit" exceptions="org.eclipse.oomph.base.Exception" projectDataType="org.eclipse.oomph.predicates.Project" iuVersionsDataType="org.eclipse.oomph.targlets.StringToVersionMap"
   * @generated
   */
  EList<IInstallableUnit> generateIUs(IProject project, String qualifierReplacement, Map<String, Version> iuVersions) throws Exception;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model exceptions="org.eclipse.oomph.base.Exception" iuDataType="org.eclipse.oomph.targlets.InstallableUnit" projectDataType="org.eclipse.oomph.predicates.Project" iuVersionsDataType="org.eclipse.oomph.targlets.StringToVersionMap"
   * @generated
   */
  void modifyIU(IInstallableUnit iu, IProject project, String qualifierReplacement, Map<String, Version> iuVersions) throws Exception;

} // InstallableUnitGenerator
