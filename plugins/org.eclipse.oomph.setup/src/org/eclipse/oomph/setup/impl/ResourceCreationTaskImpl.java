/*
 * Copyright (c) 2014-2016, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.ResourceCreationTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTaskContext;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.osgi.util.NLS;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Objects;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Creation Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.ResourceCreationTaskImpl#isForce <em>Force</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ResourceCreationTaskImpl#getContent <em>Content</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ResourceCreationTaskImpl#getTargetURL <em>Target URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ResourceCreationTaskImpl#getEncoding <em>Encoding</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResourceCreationTaskImpl extends SetupTaskImpl implements ResourceCreationTask
{
  /**
   * The default value of the '{@link #isForce() <em>Force</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isForce()
   * @generated
   * @ordered
   */
  protected static final boolean FORCE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isForce() <em>Force</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isForce()
   * @generated
   * @ordered
   */
  protected boolean force = FORCE_EDEFAULT;

  /**
   * The default value of the '{@link #getContent() <em>Content</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContent()
   * @generated
   * @ordered
   */
  protected static final String CONTENT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getContent() <em>Content</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContent()
   * @generated
   * @ordered
   */
  protected String content = CONTENT_EDEFAULT;

  /**
   * The default value of the '{@link #getTargetURL() <em>Target URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetURL()
   * @generated
   * @ordered
   */
  protected static final String TARGET_URL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTargetURL() <em>Target URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetURL()
   * @generated
   * @ordered
   */
  protected String targetURL = TARGET_URL_EDEFAULT;

  /**
   * The default value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEncoding()
   * @generated
   * @ordered
   */
  protected static final String ENCODING_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEncoding()
   * @generated
   * @ordered
   */
  protected String encoding = ENCODING_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ResourceCreationTaskImpl()
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
    return SetupPackage.Literals.RESOURCE_CREATION_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isForce()
  {
    return force;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setForce(boolean newForce)
  {
    boolean oldForce = force;
    force = newForce;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.RESOURCE_CREATION_TASK__FORCE, oldForce, force));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getContent()
  {
    return content;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setContent(String newContent)
  {
    String oldContent = content;
    content = newContent;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.RESOURCE_CREATION_TASK__CONTENT, oldContent, content));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getTargetURL()
  {
    return targetURL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setTargetURL(String newTargetURL)
  {
    String oldTargetURL = targetURL;
    targetURL = newTargetURL;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.RESOURCE_CREATION_TASK__TARGET_URL, oldTargetURL, targetURL));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getEncoding()
  {
    return encoding;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setEncoding(String newEncoding)
  {
    String oldEncoding = encoding;
    encoding = newEncoding;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.RESOURCE_CREATION_TASK__ENCODING, oldEncoding, encoding));
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
      case SetupPackage.RESOURCE_CREATION_TASK__FORCE:
        return isForce();
      case SetupPackage.RESOURCE_CREATION_TASK__CONTENT:
        return getContent();
      case SetupPackage.RESOURCE_CREATION_TASK__TARGET_URL:
        return getTargetURL();
      case SetupPackage.RESOURCE_CREATION_TASK__ENCODING:
        return getEncoding();
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
      case SetupPackage.RESOURCE_CREATION_TASK__FORCE:
        setForce((Boolean)newValue);
        return;
      case SetupPackage.RESOURCE_CREATION_TASK__CONTENT:
        setContent((String)newValue);
        return;
      case SetupPackage.RESOURCE_CREATION_TASK__TARGET_URL:
        setTargetURL((String)newValue);
        return;
      case SetupPackage.RESOURCE_CREATION_TASK__ENCODING:
        setEncoding((String)newValue);
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
      case SetupPackage.RESOURCE_CREATION_TASK__FORCE:
        setForce(FORCE_EDEFAULT);
        return;
      case SetupPackage.RESOURCE_CREATION_TASK__CONTENT:
        setContent(CONTENT_EDEFAULT);
        return;
      case SetupPackage.RESOURCE_CREATION_TASK__TARGET_URL:
        setTargetURL(TARGET_URL_EDEFAULT);
        return;
      case SetupPackage.RESOURCE_CREATION_TASK__ENCODING:
        setEncoding(ENCODING_EDEFAULT);
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
      case SetupPackage.RESOURCE_CREATION_TASK__FORCE:
        return force != FORCE_EDEFAULT;
      case SetupPackage.RESOURCE_CREATION_TASK__CONTENT:
        return CONTENT_EDEFAULT == null ? content != null : !CONTENT_EDEFAULT.equals(content);
      case SetupPackage.RESOURCE_CREATION_TASK__TARGET_URL:
        return TARGET_URL_EDEFAULT == null ? targetURL != null : !TARGET_URL_EDEFAULT.equals(targetURL);
      case SetupPackage.RESOURCE_CREATION_TASK__ENCODING:
        return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    String line = content;
    if (line != null)
    {
      int newLine = line.indexOf('\n');
      int lineFeed = line.indexOf('\r');

      int pos = newLine != -1 && lineFeed != -1 ? Math.min(newLine, lineFeed) : newLine != -1 ? newLine : lineFeed;
      if (pos != -1)
      {
        line = line.substring(0, pos);
      }

      if (line.length() > 100)
      {
        line = line.substring(0, 100);
      }
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (content: "); //$NON-NLS-1$
    result.append(line);
    result.append(", targetURL: "); //$NON-NLS-1$
    result.append(targetURL);
    result.append(", encoding: "); //$NON-NLS-1$
    result.append(encoding);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 2;
  }

  @Override
  public Object getOverrideToken()
  {
    return createToken(getTargetURL());
  }

  @Override
  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    URI targetURI = createResolvedURI(getTargetURL());
    return targetURI != null && (isForceAndHasContentChange(context, targetURI) || !context.getURIConverter().exists(targetURI, null));
  }

  private boolean isForceAndHasContentChange(SetupTaskContext context, URI targetURI)
  {
    if (isForce())
    {
      try
      {
        URIConverter uriConverter = context.getURIConverter();
        InputStream targetStream = uriConverter.createInputStream(targetURI);
        byte[] bytes = targetStream.readAllBytes();
        targetStream.close();
        String existingContent = "base64".equals(encoding) ? XMLTypeFactory.eINSTANCE.convertBase64Binary(bytes) : new String(bytes, encoding); //$NON-NLS-1$
        return !Objects.equals(content, existingContent);
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }
    }

    return false;
  }

  @Override
  public void perform(SetupTaskContext context) throws Exception
  {
    URI targetURI = createResolvedURI(getTargetURL());
    URIConverter uriConverter = context.getURIConverter();

    context.log(NLS.bind(Messages.ResourceCreationTaskImpl_Creating_message, uriConverter.normalize(targetURI)));

    String content = getContent();
    OutputStream outputStream = uriConverter.createOutputStream(targetURI);
    String encoding = getEncoding();
    if ("base64".equals(encoding)) //$NON-NLS-1$
    {
      // Remove all whitespace.
      content = content.replaceAll("\\s", ""); //$NON-NLS-1$ //$NON-NLS-2$
      byte[] bytes = XMLTypeFactory.eINSTANCE.createBase64Binary(content);
      outputStream.write(bytes);
    }
    else
    {
      Writer writer = encoding == null ? new OutputStreamWriter(outputStream) : new OutputStreamWriter(outputStream, encoding);
      writer.write(content);
      writer.close();
    }

    outputStream.close();
  }

} // ResourceCreationTaskImpl
