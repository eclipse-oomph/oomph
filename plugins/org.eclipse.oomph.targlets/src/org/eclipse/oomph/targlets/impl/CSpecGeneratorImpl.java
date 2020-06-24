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
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.targlets.CSpecGenerator;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.XMLUtil;
import org.eclipse.oomph.util.XMLUtil.ElementHandler;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CSpec Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class CSpecGeneratorImpl extends ModelElementImpl implements CSpecGenerator
{
  private static final IPath CSPEC_PATH = new Path("buckminster.cspec"); //$NON-NLS-1$

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CSpecGeneratorImpl()
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
    return TargletPackage.Literals.CSPEC_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void generateIUs(IProject project, String qualifierReplacement, Map<String, Version> iuVersions, EList<IInstallableUnit> result) throws Exception
  {
    IFile file = project.getFile(CSPEC_PATH);
    if (file.exists())
    {
      InputStream inputStream = null;

      try
      {
        inputStream = file.getContents();

        DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder(); // TODO Cache it somewhere?
        Element rootElement = XMLUtil.loadRootElement(documentBuilder, inputStream);
        String id = BuckminsterDependencyHandler.getP2ID(rootElement.getAttribute("name"), rootElement.getAttribute("componentType")); //$NON-NLS-1$ //$NON-NLS-2$
        if (!StringUtil.isEmpty(id))
        {
          ComponentDefinition componentDefinition = TargletFactory.eINSTANCE.createComponentDefinition();
          componentDefinition.setID(id);
          componentDefinition.setVersion(Version.create(rootElement.getAttribute("version"))); //$NON-NLS-1$

          handleBuckminsterDependencies(rootElement, componentDefinition);

          IInstallableUnit iu = ComponentDefGeneratorImpl.generateIU(componentDefinition, qualifierReplacement);
          result.add(iu);
        }
      }
      finally
      {
        IOUtil.closeSilent(inputStream);
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
      case TargletPackage.CSPEC_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP_ELIST:
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

  static void handleBuckminsterDependencies(Element rootElement, final ComponentExtension componentExtension) throws Exception
  {
    new BuckminsterDependencyHandler()
    {
      @Override
      protected void handleDependency(String id, String versionDesignator) throws Exception
      {
        Requirement requirement = P2Factory.eINSTANCE.createRequirement(id);
        if (versionDesignator != null)
        {
          requirement.setVersionRange(new VersionRange(versionDesignator));
        }

        componentExtension.getRequirements().add(requirement);
      }
    }.handleDependencies(rootElement);
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class BuckminsterDependencyHandler
  {
    public void handleDependencies(Element rootElement) throws Exception
    {
      XMLUtil.handleElementsByTagName(rootElement, "cs:dependencies", new ElementHandler() //$NON-NLS-1$
      {
        public void handleElement(Element dependencies) throws Exception
        {
          XMLUtil.handleElementsByTagName(dependencies, "cs:dependency", new ElementHandler() //$NON-NLS-1$
          {
            public void handleElement(Element dependency) throws Exception
            {
              String id = dependency.getAttribute("name"); //$NON-NLS-1$
              String type = dependency.getAttribute("componentType"); //$NON-NLS-1$
              String versionDesignator = dependency.getAttribute("versionDesignator"); //$NON-NLS-1$

              handleDependency(id, type, versionDesignator);
            }
          });
        }
      });
    }

    protected void handleDependency(String id, String type, String versionDesignator) throws Exception
    {
      id = getP2ID(id, type);
      if (id != null)
      {
        handleDependency(id, versionDesignator);
      }
    }

    protected void handleDependency(String id, String versionDesignator) throws Exception
    {
    }

    public static String getP2ID(String id, String type)
    {
      if (id != null && type != null)
      {
        if (type.equals("eclipse.feature")) //$NON-NLS-1$
        {
          return id + Requirement.FEATURE_SUFFIX;
        }

        return id;
      }

      return null;
    }
  }

} // CSpecGeneratorImpl
