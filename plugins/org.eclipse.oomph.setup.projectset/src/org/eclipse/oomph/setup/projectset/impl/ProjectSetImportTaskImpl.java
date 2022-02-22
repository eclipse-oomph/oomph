/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.projectset.impl;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.projectset.ProjectSetImportTask;
import org.eclipse.oomph.setup.projectset.ProjectSetPackage;
import org.eclipse.oomph.setup.projectset.ProjectSetPlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertyFile;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IWorkingSet;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project Set Import Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.projectset.impl.ProjectSetImportTaskImpl#getURL <em>URL</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProjectSetImportTaskImpl extends SetupTaskImpl implements ProjectSetImportTask
{
  /**
   * The default value of the '{@link #getURL() <em>URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getURL()
   * @generated
   * @ordered
   */
  protected static final String URL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getURL() <em>URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getURL()
   * @generated
   * @ordered
   */
  protected String uRL = URL_EDEFAULT;

  private Helper helper;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProjectSetImportTaskImpl()
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
    return ProjectSetPackage.Literals.PROJECT_SET_IMPORT_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getURL()
  {
    return uRL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setURL(String newURL)
  {
    String oldURL = uRL;
    uRL = newURL;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ProjectSetPackage.PROJECT_SET_IMPORT_TASK__URL, oldURL, uRL));
    }
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
      case ProjectSetPackage.PROJECT_SET_IMPORT_TASK__URL:
        return getURL();
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
      case ProjectSetPackage.PROJECT_SET_IMPORT_TASK__URL:
        setURL((String)newValue);
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
      case ProjectSetPackage.PROJECT_SET_IMPORT_TASK__URL:
        setURL(URL_EDEFAULT);
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
      case ProjectSetPackage.PROJECT_SET_IMPORT_TASK__URL:
        return URL_EDEFAULT == null ? uRL != null : !URL_EDEFAULT.equals(uRL);
    }
    return super.eIsSet(featureID);
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

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (uRL: "); //$NON-NLS-1$
    result.append(uRL);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 100;
  }

  @Override
  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    URI uri = createResolvedURI(getURL());
    helper = new Helper(uri);
    return helper.isNeeded(context);
  }

  @Override
  public void perform(SetupTaskContext context) throws Exception
  {
    helper.perform(context);
  }

  private static class Helper extends PropertyFile
  {
    private static final IWorkspaceRoot ROOT = EcorePlugin.getWorkspaceRoot();

    private static final File HISTORY = ProjectSetPlugin.INSTANCE.getStateLocation().append("import-history.properties").toFile(); //$NON-NLS-1$

    private URI uri;

    private String content;

    public Helper(URI uri)
    {
      super(HISTORY);
      this.uri = uri;
    }

    public boolean isNeeded(SetupTaskContext context) throws Exception
    {
      try
      {
        content = getXMLContent(context.getURIConverter());
        IProject[] projects = getProjects(uri, content);
        if (projects == null)
        {
          return true;
        }

        for (IProject project : projects)
        {
          if (!project.exists())
          {
            return true;
          }
        }
      }
      catch (Exception ex)
      {
        // The content might not be available until perform time.
        return true;
      }

      return false;
    }

    @SuppressWarnings("restriction")
    public void perform(SetupTaskContext context) throws Exception
    {
      if (content == null)
      {
        // Try to get the content again if it wasn't available during isNeeded testing.
        content = getXMLContent(context.getURIConverter());
      }

      IProject[] projects = new org.eclipse.team.internal.ui.wizards.ImportProjectSetOperation(null, content, uri.toString(), new IWorkingSet[0])
      {
        public IProject[] perform(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          return org.eclipse.team.internal.ui.ProjectSetImporter.importProjectSetFromString(content, uri.toString(), getShell(), monitor);
        }
      }.perform(context.getProgressMonitor(true));

      setProjects(uri, content, projects);
    }

    private String getXMLContent(URIConverter uriConverter) throws Exception
    {
      InputStream inputStream = null;
      try
      {
        inputStream = uriConverter.createInputStream(uri, null);
        return IOUtil.readXML(inputStream);
      }
      finally
      {
        IOUtil.close(inputStream);
      }
    }

    private IProject[] getProjects(URI uri, String content)
    {
      String key = uri.toString();
      String value = getProperty(key, null);
      if (value != null)
      {
        String digest = getDigest(content);
        List<IProject> projects = new ArrayList<>();
        boolean confirm = true;
        for (String element : XMLTypeFactory.eINSTANCE.createNMTOKENS(value))
        {
          if (confirm)
          {
            if (!digest.equals(element))
            {
              removeProperty(key);
              return null;
            }

            confirm = false;
          }
          else
          {
            projects.add(ROOT.getProject(URI.decode(element)));
          }
        }

        return projects.toArray(new IProject[projects.size()]);
      }

      return null;
    }

    private String getDigest(String contents)
    {
      try
      {
        return XMLTypeFactory.eINSTANCE.convertBase64Binary(IOUtil.getSHA1(contents));
      }
      catch (Exception ex)
      {
        ProjectSetPlugin.INSTANCE.log(ex);
        return null;
      }
    }

    private void setProjects(URI uri, String content, IProject[] projects)
    {
      String key = uri.toString();
      StringBuilder value = new StringBuilder(getDigest(content));
      for (IProject project : projects)
      {
        value.append(' ');
        value.append(URI.encodeSegment(project.getName(), false));
      }

      setProperty(key, value.toString());
    }
  }
} // ProjectSetImportTaskImpl
