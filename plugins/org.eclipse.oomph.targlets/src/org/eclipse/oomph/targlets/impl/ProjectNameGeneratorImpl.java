/*
 * Copyright (c) 2014 Ed Merks(Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks- initial API and implementation
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ProjectNameGenerator;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IProject;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project Name Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ProjectNameGeneratorImpl extends ModelElementImpl implements ProjectNameGenerator
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProjectNameGeneratorImpl()
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
    return TargletPackage.Literals.PROJECT_NAME_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<IInstallableUnit> generateIUs(IProject project, String qualifierReplacement, Map<String, Version> iuVersions) throws Exception
  {
    ComponentDefinition componentDefinition = TargletFactory.eINSTANCE.createComponentDefinition();
    String name = project.getDescription().getName();
    componentDefinition.setID(name);
    componentDefinition.setVersion(Version.createOSGi(1, 0, 0, "qualifier"));

    IInstallableUnit iu = ComponentGeneratorImpl.generateIU(componentDefinition, qualifierReplacement);
    return ECollections.singletonEList(iu);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void modifyIU(IInstallableUnit iu, IProject project, String qualifierReplacement, Map<String, Version> iuVersions) throws Exception
  {
    // Do nothing.
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
      case TargletPackage.PROJECT_NAME_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP:
        try
        {
          return generateIUs((IProject)arguments.get(0), (String)arguments.get(1), (Map<String, Version>)arguments.get(2));
        }
        catch (Throwable throwable)
        {
          throw new InvocationTargetException(throwable);
        }
      case TargletPackage.PROJECT_NAME_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP:
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

} // ProjectNameGeneratorImpl
