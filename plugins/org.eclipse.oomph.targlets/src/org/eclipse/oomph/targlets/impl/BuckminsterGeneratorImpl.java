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
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.targlets.BuckminsterGenerator;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.XMLUtil;
import org.eclipse.oomph.util.XMLUtil.ElementHandler;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

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
 * An implementation of the model object '<em><b>Buckminster Generator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.targlets.impl.BuckminsterGeneratorImpl#isSaveAsComponent <em>Save As Component</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BuckminsterGeneratorImpl extends ModelElementImpl implements BuckminsterGenerator
{
  private static final IPath CSPEC_PATH = new Path("buckminster.cspec");

  private static final IPath CSPEX_PATH = new Path("buckminster.cspex");

  /**
   * The default value of the '{@link #isSaveAsComponent() <em>Save As Component</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSaveAsComponent()
   * @generated
   * @ordered
   */
  protected static final boolean SAVE_AS_COMPONENT_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isSaveAsComponent() <em>Save As Component</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSaveAsComponent()
   * @generated
   * @ordered
   */
  protected boolean saveAsComponent = SAVE_AS_COMPONENT_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BuckminsterGeneratorImpl()
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
    return TargletPackage.Literals.BUCKMINSTER_GENERATOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isSaveAsComponent()
  {
    return saveAsComponent;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSaveAsComponent(boolean newSaveAsComponent)
  {
    boolean oldSaveAsComponent = saveAsComponent;
    saveAsComponent = newSaveAsComponent;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.BUCKMINSTER_GENERATOR__SAVE_AS_COMPONENT, oldSaveAsComponent, saveAsComponent));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public EList<IInstallableUnit> generateIUs(IProject project, String qualifierReplacement, Map<String, Version> iuVersions) throws Exception
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
        String id = BuckminsterDependencyHandler.getP2ID(rootElement.getAttribute("name"), rootElement.getAttribute("componentType"));
        if (id == null)
        {
          return null;
        }

        ComponentDefinition componentDefinition = TargletFactory.eINSTANCE.createComponentDefinition();
        componentDefinition.setID(id);
        componentDefinition.setVersion(Version.create(rootElement.getAttribute("version")));

        handleBuckminsterDependencies(rootElement, componentDefinition);

        IInstallableUnit iu = ComponentGeneratorImpl.generateIU(componentDefinition, qualifierReplacement);
        return ECollections.singletonEList(iu);
      }
      finally
      {
        IOUtil.closeSilent(inputStream);
      }
    }

    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void modifyIU(IInstallableUnit iu, IProject project, String qualifierReplacement, Map<String, Version> iuVersions) throws Exception
  {
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
        handleBuckminsterDependencies(rootElement, componentExtension);

        ComponentGeneratorImpl.modifyIU(componentExtension, iu);
      }
      finally
      {
        IOUtil.closeSilent(inputStream);
      }
    }
  }

  private void handleBuckminsterDependencies(Element rootElement, final ComponentExtension componentExtension) throws Exception
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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case TargletPackage.BUCKMINSTER_GENERATOR__SAVE_AS_COMPONENT:
        return isSaveAsComponent();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case TargletPackage.BUCKMINSTER_GENERATOR__SAVE_AS_COMPONENT:
        setSaveAsComponent((Boolean)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case TargletPackage.BUCKMINSTER_GENERATOR__SAVE_AS_COMPONENT:
        setSaveAsComponent(SAVE_AS_COMPONENT_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case TargletPackage.BUCKMINSTER_GENERATOR__SAVE_AS_COMPONENT:
        return saveAsComponent != SAVE_AS_COMPONENT_EDEFAULT;
    }
    return super.eIsSet(featureID);
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
      case TargletPackage.BUCKMINSTER_GENERATOR___GENERATE_IUS__IPROJECT_STRING_MAP:
        try
        {
          return generateIUs((IProject)arguments.get(0), (String)arguments.get(1), (Map<String, Version>)arguments.get(2));
        }
        catch (Throwable throwable)
        {
          throw new InvocationTargetException(throwable);
        }
      case TargletPackage.BUCKMINSTER_GENERATOR___MODIFY_IU__IINSTALLABLEUNIT_IPROJECT_STRING_MAP:
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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (saveAsComponent: ");
    result.append(saveAsComponent);
    result.append(')');
    return result.toString();
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class BuckminsterDependencyHandler
  {
    public void handleDependencies(Element rootElement) throws Exception
    {
      XMLUtil.handleElementsByTagName(rootElement, "cs:dependencies", new ElementHandler()
      {
        public void handleElement(Element dependencies) throws Exception
        {
          XMLUtil.handleElementsByTagName(dependencies, "cs:dependency", new ElementHandler()
          {
            public void handleElement(Element dependency) throws Exception
            {
              String id = dependency.getAttribute("name");
              String type = dependency.getAttribute("componentType");
              String versionDesignator = dependency.getAttribute("versionDesignator");

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
        if (type.equals("eclipse.feature"))
        {
          return id + ".feature.group";
        }

        return id;
      }

      return null;
    }
  }

} // BuckminsterGeneratorImpl
