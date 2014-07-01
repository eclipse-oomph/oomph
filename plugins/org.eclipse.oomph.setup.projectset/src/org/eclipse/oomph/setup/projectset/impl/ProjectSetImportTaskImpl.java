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
package org.eclipse.oomph.setup.projectset.impl;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.log.ProgressLogMonitor;
import org.eclipse.oomph.setup.projectset.ProjectSetImportTask;
import org.eclipse.oomph.setup.projectset.ProjectSetPackage;
import org.eclipse.oomph.setup.projectset.ProjectSetPlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.XMLUtil;

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

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project Set Import Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.projectset.impl.ProjectSetImportTaskImpl#getURL <em>URL</em>}</li>
 * </ul>
 * </p>
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
  public String getURL()
  {
    return uRL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
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

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (uRL: ");
    result.append(uRL);
    result.append(')');
    return result.toString();
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    URI uri = createResolvedURI(getURL());
    helper = new Helper(uri);
    return helper.isNeeded(context);
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    helper.perform(context);
  }

  private static class Helper
  {
    private static final IWorkspaceRoot ROOT = EcorePlugin.getWorkspaceRoot();

    private static final File HISTORY = ProjectSetPlugin.INSTANCE.getStateLocation().append("import-history.properties").toFile();

    private URI uri;

    private String content;

    public Helper(URI uri)
    {
      this.uri = uri;
    }

    public boolean isNeeded(SetupTaskContext context) throws Exception
    {
      content = getXMLContent(context.getURIConverter());
      if (content != null)
      {
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

      return false;
    }

    @SuppressWarnings("restriction")
    public void perform(SetupTaskContext context) throws Exception
    {
      IProject[] projects = new org.eclipse.team.internal.ui.wizards.ImportProjectSetOperation(null, content, uri.toString(), new IWorkingSet[0])
      {
        public IProject[] perform(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          return org.eclipse.team.internal.ui.ProjectSetImporter.importProjectSetFromString(content, uri.toString(), getShell(), monitor);
        }
      }.perform(new ProgressLogMonitor(context));

      setProjects(uri, content, projects);
    }

    private String getXMLContent(URIConverter uriConverter) throws Exception
    {
      InputStream inputStream = null;
      try
      {
        DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
        inputStream = uriConverter.createInputStream(uri, null);
        Document document = documentBuilder.parse(inputStream);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StringWriter out = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(out));
        out.close();
        return out.toString();
      }
      finally
      {
        IOUtil.close(inputStream);
      }
    }

    private IProject[] getProjects(URI uri, String content)
    {
      Map<String, String> properties = loadProperties();
      String key = uri.toString();
      String value = properties.get(key);
      if (value != null)
      {
        String digest = getDigest(content);
        List<IProject> projects = new ArrayList<IProject>();
        boolean confirm = true;
        for (String element : XMLTypeFactory.eINSTANCE.createNMTOKENS(value))
        {
          if (confirm)
          {
            if (!digest.equals(element))
            {
              properties.remove(key);
              saveProperties(properties);
              return null;
            }

            confirm = false;
          }
          else
          {
            projects.add(ROOT.getProject(URI.decode(element)));
          }
        }

        IProject[] result = projects.toArray(new IProject[projects.size()]);
        setProjects(uri, content, result);
        return result;
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
      Map<String, String> properties = loadProperties();
      String key = uri.toString();
      StringBuilder value = new StringBuilder(getDigest(content));
      for (IProject project : projects)
      {
        value.append(' ');
        value.append(URI.encodeSegment(project.getName(), false));
      }

      properties.put(key, value.toString());
      saveProperties(properties);
    }

    private Map<String, String> loadProperties()
    {
      try
      {
        if (HISTORY.exists())
        {
          return PropertiesUtil.loadProperties(HISTORY);
        }
      }
      catch (RuntimeException ex)
      {
        // Ignore.
      }

      return new LinkedHashMap<String, String>();
    }

    private void saveProperties(Map<String, String> properties)
    {
      try
      {
        PropertiesUtil.saveProperties(HISTORY, properties, true);
      }
      catch (RuntimeException ex)
      {
        ProjectSetPlugin.INSTANCE.log(ex);
      }
    }
  }
} // ProjectSetImportTaskImpl
