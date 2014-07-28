/**
 */
package org.eclipse.oomph.resources.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.internal.resources.ExternalProject;
import org.eclipse.oomph.internal.resources.ExternalProject.Description;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.resources.XMLProjectFactory;
import org.eclipse.oomph.resources.backend.BackendContainer;
import org.eclipse.oomph.resources.backend.BackendFile;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>XML Project Factory</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class XMLProjectFactoryImpl extends ModelElementImpl implements XMLProjectFactory
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
  public final IProject createProject(BackendContainer backendContainer, IProgressMonitor monitor)
  {
    IPath xmlFilePath = new Path(getXMLFileName());
    BackendFile xmlFile = null;

    try
    {
      xmlFile = backendContainer.getFile(xmlFilePath);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      IOUtil.copy(xmlFile.getContents(monitor), baos);

      if (baos.size() > 0)
      {
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
        Element rootElement = XMLUtil.loadRootElement(documentBuilder, bais);

        Description description = new Description(backendContainer);
        fillDescription(description, rootElement);

        return new ExternalProject(backendContainer, description);
      }
    }
    catch (Exception ex)
    {
      // String message = "Problem parsing " + (xmlFile != null ? xmlFile : xmlFilePath);
      // ResourcesPlugin.INSTANCE.log(new Exception(message, ex));
    }

    return null;
  }

  protected abstract String getXMLFileName();

  protected abstract void fillDescription(Description description, Element rootElement) throws Exception;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case ResourcesPackage.XML_PROJECT_FACTORY___CREATE_PROJECT__BACKENDCONTAINER_IPROGRESSMONITOR:
        return createProject((BackendContainer)arguments.get(0), (IProgressMonitor)arguments.get(1));
    }
    return super.eInvoke(operationID, arguments);
  }

} // XMLProjectFactoryImpl
