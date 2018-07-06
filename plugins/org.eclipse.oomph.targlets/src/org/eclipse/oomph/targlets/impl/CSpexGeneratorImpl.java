/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.targlets.CSpexGenerator;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CSpex Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class CSpexGeneratorImpl extends ModelElementImpl implements CSpexGenerator
{
  private static final IPath CSPEX_PATH = new Path("buckminster.cspex");

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CSpexGeneratorImpl()
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
    return TargletPackage.Literals.CSPEX_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void generateIUs(IProject project, String qualifierReplacement, Map<String, Version> iuVersions, EList<IInstallableUnit> result) throws Exception
  {
    if (!result.isEmpty())
    {
      IInstallableUnit mainIU = result.get(0);

      IFile file = project.getFile(CSPEX_PATH);
      if (file.exists())
      {
        InputStream inputStream = null;

        try
        {
          inputStream = file.getContents();

          ComponentExtension componentExtension = TargletFactory.eINSTANCE.createComponentExtension();

          DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder(); // TODO Cache it somewhere?
          Element rootElement = XMLUtil.loadRootElement(documentBuilder, inputStream);
          CSpecGeneratorImpl.handleBuckminsterDependencies(rootElement, componentExtension);

          ComponentExtGeneratorImpl.modifyIU(componentExtension, mainIU);
        }
        finally
        {
          IOUtil.closeSilent(inputStream);
        }
      }
    }
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
      case TargletPackage.CSPEX_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST:
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

} // CSpexGeneratorImpl
