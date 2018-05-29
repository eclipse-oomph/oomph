/*
 * Copyright (c) 2014, 2015, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.TextModification;
import org.eclipse.oomph.setup.TextModifyTask;
import org.eclipse.oomph.setup.util.DownloadUtil;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Text Modify Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.TextModifyTaskImpl#getURL <em>URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.TextModifyTaskImpl#getModifications <em>Modifications</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.TextModifyTaskImpl#getEncoding <em>Encoding</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TextModifyTaskImpl extends SetupTaskImpl implements TextModifyTask
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

  /**
   * The cached value of the '{@link #getModifications() <em>Modifications</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getModifications()
   * @generated
   * @ordered
   */
  protected EList<TextModification> modifications;

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
  protected TextModifyTaskImpl()
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
    return SetupPackage.Literals.TEXT_MODIFY_TASK;
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
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.TEXT_MODIFY_TASK__URL, oldURL, uRL));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<TextModification> getModifications()
  {
    if (modifications == null)
    {
      modifications = new EObjectContainmentEList.Resolving<TextModification>(TextModification.class, this, SetupPackage.TEXT_MODIFY_TASK__MODIFICATIONS);
    }
    return modifications;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getEncoding()
  {
    return encoding;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEncoding(String newEncoding)
  {
    String oldEncoding = encoding;
    encoding = newEncoding;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.TEXT_MODIFY_TASK__ENCODING, oldEncoding, encoding));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SetupPackage.TEXT_MODIFY_TASK__MODIFICATIONS:
        return ((InternalEList<?>)getModifications()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case SetupPackage.TEXT_MODIFY_TASK__URL:
        return getURL();
      case SetupPackage.TEXT_MODIFY_TASK__MODIFICATIONS:
        return getModifications();
      case SetupPackage.TEXT_MODIFY_TASK__ENCODING:
        return getEncoding();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SetupPackage.TEXT_MODIFY_TASK__URL:
        setURL((String)newValue);
        return;
      case SetupPackage.TEXT_MODIFY_TASK__MODIFICATIONS:
        getModifications().clear();
        getModifications().addAll((Collection<? extends TextModification>)newValue);
        return;
      case SetupPackage.TEXT_MODIFY_TASK__ENCODING:
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
      case SetupPackage.TEXT_MODIFY_TASK__URL:
        setURL(URL_EDEFAULT);
        return;
      case SetupPackage.TEXT_MODIFY_TASK__MODIFICATIONS:
        getModifications().clear();
        return;
      case SetupPackage.TEXT_MODIFY_TASK__ENCODING:
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
      case SetupPackage.TEXT_MODIFY_TASK__URL:
        return URL_EDEFAULT == null ? uRL != null : !URL_EDEFAULT.equals(uRL);
      case SetupPackage.TEXT_MODIFY_TASK__MODIFICATIONS:
        return modifications != null && !modifications.isEmpty();
      case SetupPackage.TEXT_MODIFY_TASK__ENCODING:
        return ENCODING_EDEFAULT == null ? encoding != null : !ENCODING_EDEFAULT.equals(encoding);
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
    result.append(" (uRL: ");
    result.append(uRL);
    result.append(", encoding: ");
    result.append(encoding);
    result.append(')');
    return result.toString();
  }

  private String getText(SetupTaskContext context) throws IOException
  {
    return DownloadUtil.load(context.getURIConverter(), URI.createURI(getURL()), encoding);
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    if (getURL() == null)
    {
      return false;
    }

    try
    {
      String text = getText(context);
      for (TextModification modification : getModifications())
      {
        Pattern pattern = Pattern.compile(modification.getPattern());
        Matcher matcher = pattern.matcher(text);
        if (matcher.find())
        {
          return true;
        }
      }

      return false;
    }
    catch (IOException exception)
    {
      // The file might not exist yet.
      return true;
    }
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    URI uri = createResolvedURI(getURL());
    URIConverter uriConverter = context.getURIConverter();

    context.log("Modifying " + uriConverter.normalize(uri));

    String text = getText(context);
    for (TextModification modification : getModifications())
    {
      int index = 0;
      StringBuilder result = new StringBuilder();
      Pattern pattern = Pattern.compile(modification.getPattern());
      for (Matcher matcher = pattern.matcher(text); matcher.find();)
      {
        result.append(text, index, index = matcher.start());

        Map<String, String> captures = new HashMap<String, String>();
        for (int i = 1, count = matcher.groupCount(); i <= count; ++i)
        {
          captures.put("\\" + i, matcher.group(i));
        }

        for (int i = 1, count = matcher.groupCount(); i <= count; ++i)
        {
          result.append(text, index, matcher.start(i));
          String substitution = modification.getSubstitutions().get(i - 1);
          for (Map.Entry<String, String> entry : captures.entrySet())
          {
            substitution = substitution.replace(entry.getKey(), entry.getValue());
          }

          result.append(substitution);
          index = matcher.end(i);
        }

        result.append(text, index, index = matcher.end());
      }

      result.append(text, index, text.length());
      text = result.toString();
    }

    PrintStream printStream = null;
    try
    {
      OutputStream outputStream = uriConverter.createOutputStream(uri);
      printStream = encoding == null ? new PrintStream(outputStream) : new PrintStream(outputStream, false, encoding);
      printStream.print(text);
    }
    finally
    {
      IOUtil.close(printStream);
    }
  }
} // TextModifyTaskImpl
