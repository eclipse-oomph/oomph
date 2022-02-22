/*
 * Copyright (c) 2015 Ed Merks(Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks- initial API and implementation
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ProjectNameGenerator;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IProject;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

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
   * @generated NOT
   */
  @Override
  public void generateIUs(IProject project, String qualifierReplacement, Map<String, Version> iuVersions, EList<IInstallableUnit> result) throws Exception
  {
    ComponentDefinition componentDefinition = TargletFactory.eINSTANCE.createComponentDefinition();
    String name = project.getDescription().getName();
    componentDefinition.setID(name + Requirement.PROJECT_SUFFIX);
    componentDefinition.setVersion(Version.createOSGi(1, 0, 0, "qualifier")); //$NON-NLS-1$

    EList<Requirement> requirements = componentDefinition.getRequirements();

    for (IProject referencedProject : project.getReferencedProjects())
    {
      String id = referencedProject.getName() + Requirement.PROJECT_SUFFIX;
      requirements.add(P2Factory.eINSTANCE.createRequirement(id, VersionRange.emptyRange, true));
    }

    IInstallableUnit iu = ComponentDefGeneratorImpl.generateIU(componentDefinition, qualifierReplacement);
    result.add(iu);
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
      case TargletPackage.PROJECT_NAME_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST:
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

} // ProjectNameGeneratorImpl
