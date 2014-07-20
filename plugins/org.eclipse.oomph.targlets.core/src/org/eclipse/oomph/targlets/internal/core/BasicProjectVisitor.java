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
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.TargletPackage;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.XMLUtil;
import org.eclipse.oomph.util.XMLUtil.ElementHandler;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import org.eclipse.core.runtime.IProgressMonitor;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.util.jar.Manifest;

/**
 * @author Eike Stepper
 */
public class BasicProjectVisitor<T> implements ProjectVisitor<T>
{
  private DocumentBuilder documentBuilder;

  private ResourceSet resourceSet;

  public T visitPlugin(File manifestFile, IProgressMonitor monitor) throws Exception
  {
    FileInputStream manifestFileInputStream = null;

    try
    {
      manifestFileInputStream = new FileInputStream(manifestFile);
      Manifest manifest = new Manifest(manifestFileInputStream);
      return visitPlugin(manifest, monitor);
    }
    finally
    {
      IOUtil.close(manifestFileInputStream);
    }
  }

  public T visitFeature(File featureFile, IProgressMonitor monitor) throws Exception
  {
    Element rootElement = XMLUtil.loadRootElement(getDocumentBuilder(), featureFile);
    return visitFeature(rootElement, monitor);
  }

  public T visitComponentDefinition(File cdefFile, IProgressMonitor monitor) throws Exception
  {
    Resource resource = getResourceSet().getResource(URI.createFileURI(cdefFile.getAbsolutePath()), true);
    ComponentDefinition componentDefinition = BaseUtil.getObjectByType(resource.getContents(), TargletPackage.Literals.COMPONENT_DEFINITION);
    if (componentDefinition == null)
    {
      return null;
    }

    return visitComponentDefinition(componentDefinition, monitor);
  }

  public void visitComponentExtension(File cextFile, T host, IProgressMonitor monitor) throws Exception
  {
    Resource resource = getResourceSet().getResource(URI.createFileURI(cextFile.getAbsolutePath()), true);
    ComponentExtension componentExtension = BaseUtil.getObjectByType(resource.getContents(), TargletPackage.Literals.COMPONENT_EXTENSION);
    if (componentExtension != null)
    {
      visitComponentExtension(componentExtension, host, monitor);
    }
  }

  public T visitCSpec(File cspecFile, IProgressMonitor monitor) throws Exception
  {
    Element rootElement = XMLUtil.loadRootElement(getDocumentBuilder(), cspecFile);
    return visitCSpec(rootElement, monitor);
  }

  public void visitCSpex(File cspexFile, T host, IProgressMonitor monitor) throws Exception
  {
    Element rootElement = XMLUtil.loadRootElement(getDocumentBuilder(), cspexFile);
    visitCSpex(rootElement, host, monitor);
  }

  protected final DocumentBuilder getDocumentBuilder() throws ParserConfigurationException
  {
    if (documentBuilder == null)
    {
      documentBuilder = XMLUtil.createDocumentBuilder();
    }

    return documentBuilder;
  }

  protected final ResourceSet getResourceSet()
  {
    if (resourceSet == null)
    {
      resourceSet = new ResourceSetImpl();
    }

    return resourceSet;
  }

  protected T visitPlugin(Manifest manifest, IProgressMonitor monitor) throws Exception
  {
    return null;
  }

  protected T visitFeature(Element rootElement, IProgressMonitor monitor) throws Exception
  {
    return null;
  }

  protected T visitComponentDefinition(ComponentDefinition componentDefinition, IProgressMonitor monitor) throws Exception
  {
    return null;
  }

  protected void visitComponentExtension(ComponentExtension componentExtension, T host, IProgressMonitor monitor) throws Exception
  {
  }

  protected T visitCSpec(Element rootElement, IProgressMonitor monitor) throws Exception
  {
    return null;
  }

  protected void visitCSpex(Element rootElement, T host, IProgressMonitor monitor) throws Exception
  {
  }

  /**
   * @author Eike Stepper
   */
  public static abstract class BuckminsterDependencyHandler
  {
    public void handleDependencies(Element rootElement, IProgressMonitor monitor) throws Exception
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
}
