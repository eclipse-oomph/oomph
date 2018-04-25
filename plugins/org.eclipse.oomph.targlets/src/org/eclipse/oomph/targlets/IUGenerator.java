/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
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
  public static final EList<IUGenerator> DEFAULTS = ECollections.asEList(//
      TargletFactory.eINSTANCE.createPluginGenerator(), //
      TargletFactory.eINSTANCE.createFeatureGenerator(), //
      TargletFactory.eINSTANCE.createCategoryGenerator(), //
      TargletFactory.eINSTANCE.createSiteGenerator(), //
      TargletFactory.eINSTANCE.createProductGenerator(), //
      TargletFactory.eINSTANCE.createComponentDefGenerator(), //
      TargletFactory.eINSTANCE.createProjectNameGenerator(), //
      TargletFactory.eINSTANCE.createComponentExtGenerator() //
  );

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model exceptions="org.eclipse.oomph.base.Exception" projectDataType="org.eclipse.oomph.predicates.Project" iuVersionsDataType="org.eclipse.oomph.targlets.StringToVersionMap" resultDataType="org.eclipse.oomph.targlets.InstallableUnit" resultMany="true"
   * @generated
   */
  void generateIUs(IProject project, String qualifierReplacement, Map<String, Version> iuVersions, EList<IInstallableUnit> result) throws Exception;

} // InstallableUnitGenerator
