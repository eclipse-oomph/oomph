/**
 */
package org.eclipse.oomph.resources.impl;

import org.eclipse.oomph.internal.resources.ExternalProject;
import org.eclipse.oomph.internal.resources.ExternalProject.Description;
import org.eclipse.oomph.internal.resources.ResourcesPlugin;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.resources.XMLProjectFactory;
import org.eclipse.oomph.resources.backend.BackendContainer;
import org.eclipse.oomph.resources.backend.BackendFile;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>XML Project Factory</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class XMLProjectFactoryImpl extends ProjectFactoryImpl implements XMLProjectFactory
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected XMLProjectFactoryImpl()
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
    return ResourcesPackage.Literals.XML_PROJECT_FACTORY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public final IProject doCreateProject(BackendContainer rootContainer, BackendContainer backendContainer, IProgressMonitor monitor)
  {
    IPath xmlFilePath = new Path(getXMLFileName());
    BackendFile xmlFile = null;

    try
    {
      xmlFile = backendContainer.getFile(xmlFilePath);

      byte[] bytes = xmlFile.getContentBytes(monitor);
      if (bytes.length > 0)
      {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
        Element rootElement = XMLUtil.loadRootElement(documentBuilder, bais);

        Description description = new Description(backendContainer);
        fillDescription(description, rootElement);

        return new ExternalProject(backendContainer, description);
      }
    }
    catch (FileNotFoundException ex)
    {
      //$FALL-THROUGH$
    }
    catch (Exception ex)
    {
      String message = "Problem parsing " + (xmlFile != null ? xmlFile : xmlFilePath);
      ResourcesPlugin.INSTANCE.log(new Exception(message, ex));
    }

    return null;
  }

  protected abstract String getXMLFileName();

  protected abstract void fillDescription(Description description, Element rootElement) throws Exception;

} // XMLProjectFactoryImpl
