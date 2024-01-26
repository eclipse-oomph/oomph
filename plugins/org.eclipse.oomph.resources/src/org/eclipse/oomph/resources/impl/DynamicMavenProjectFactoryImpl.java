/**
 */
package org.eclipse.oomph.resources.impl;

import org.eclipse.oomph.resources.DynamicMavenProjectFactory;
import org.eclipse.oomph.resources.ResourcesPackage;

import org.eclipse.emf.ecore.EClass;

import java.util.Objects;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dynamic Maven Project Factory</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class DynamicMavenProjectFactoryImpl extends MavenProjectFactoryImpl implements DynamicMavenProjectFactory
{

  private String xmlFileName;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DynamicMavenProjectFactoryImpl()
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
    return ResourcesPackage.Literals.DYNAMIC_MAVEN_PROJECT_FACTORY;
  }

  @Override
  public void setXMLFileName(String xmlFileName)
  {
    this.xmlFileName = xmlFileName;
  }

  @Override
  public String getXMLFileName()
  {
    return Objects.requireNonNullElse(xmlFileName, super.getXMLFileName());
  }

} // DynamicMavenProjectFactoryImpl
