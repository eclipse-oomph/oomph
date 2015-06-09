/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates.impl;

import org.eclipse.oomph.predicates.FilePredicate;
import org.eclipse.oomph.predicates.PredicatesPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>File Predicate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.predicates.impl.FilePredicateImpl#getFilePattern <em>File Pattern</em>}</li>
 *   <li>{@link org.eclipse.oomph.predicates.impl.FilePredicateImpl#getContentPattern <em>Content Pattern</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FilePredicateImpl extends PredicateImpl implements FilePredicate
{
  /**
   * The default value of the '{@link #getFilePattern() <em>File Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilePattern()
   * @generated
   * @ordered
   */
  protected static final String FILE_PATTERN_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFilePattern() <em>File Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilePattern()
   * @generated
   * @ordered
   */
  protected String filePattern = FILE_PATTERN_EDEFAULT;

  /**
   * The default value of the '{@link #getContentPattern() <em>Content Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContentPattern()
   * @generated
   * @ordered
   */
  protected static final String CONTENT_PATTERN_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getContentPattern() <em>Content Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContentPattern()
   * @generated
   * @ordered
   */
  protected String contentPattern = CONTENT_PATTERN_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FilePredicateImpl()
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
    return PredicatesPackage.Literals.FILE_PREDICATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getFilePattern()
  {
    return filePattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFilePattern(String newFilePattern)
  {
    String oldFilePattern = filePattern;
    filePattern = newFilePattern;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PredicatesPackage.FILE_PREDICATE__FILE_PATTERN, oldFilePattern, filePattern));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getContentPattern()
  {
    return contentPattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setContentPattern(String newContentPattern)
  {
    String oldContentPattern = contentPattern;
    contentPattern = newContentPattern;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PredicatesPackage.FILE_PREDICATE__CONTENT_PATTERN, oldContentPattern, contentPattern));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public boolean matches(IResource resource)
  {
    String filePattern = getFilePattern();
    if (filePattern != null)
    {
      StringBuilder pattern = new StringBuilder();
      for (int i = 0, length = filePattern.length(); i < length; ++i)
      {
        char character = filePattern.charAt(i);
        if (!Character.isJavaIdentifierPart(character))
        {
          if (character == '*')
          {
            if (i + 1 < length && filePattern.charAt(i + 1) == '*')
            {
              ++i;
              pattern.append(".*");
            }
            else
            {
              pattern.append("[^/]*");
            }
          }
          else if (character == '?')
          {
            pattern.append("[^/]");
          }
          else
          {
            pattern.append('\\');
            pattern.append(character);
          }
        }
        else
        {
          pattern.append(character);
        }
      }

      try
      {
        final Pattern regex = Pattern.compile(pattern.toString());
        final CoreException matched = new CoreException(Status.OK_STATUS);
        final Pattern contentPattern = getContentPattern() == null ? null : Pattern.compile(getContentPattern());
        try
        {
          resource.accept(new IResourceVisitor()
          {
            public boolean visit(IResource resource) throws CoreException
            {
              String path = resource.getProjectRelativePath().toString();
              if (regex.matcher(path).matches())
              {
                if (contentPattern == null)
                {
                  throw matched;
                }

                if (resource.getType() == IResource.FILE)
                {
                  IFile file = (IFile)resource;
                  String charset = file.getCharset();
                  InputStream inputStream = null;
                  try
                  {
                    inputStream = file.getContents();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    byte[] input = new byte[bufferedInputStream.available()];
                    bufferedInputStream.read(input);
                    String contents = charset == null ? new String(input) : new String(input, charset);
                    if (contentPattern.matcher(contents).find())
                    {
                      throw matched;
                    }
                  }
                  catch (IOException ex)
                  {
                    // Ignore.
                  }
                  finally
                  {
                    if (inputStream != null)
                    {
                      try
                      {
                        inputStream.close();
                      }
                      catch (IOException ex)
                      {
                        // Ignore.
                      }
                    }
                  }
                }
              }
              return true;
            }
          });
        }
        catch (CoreException ex)
        {
          if (ex == matched)
          {
            return true;
          }
        }
      }
      catch (PatternSyntaxException exception)
      {
        // Ignore
      }
    }
    return false;
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
      case PredicatesPackage.FILE_PREDICATE__FILE_PATTERN:
        return getFilePattern();
      case PredicatesPackage.FILE_PREDICATE__CONTENT_PATTERN:
        return getContentPattern();
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
      case PredicatesPackage.FILE_PREDICATE__FILE_PATTERN:
        setFilePattern((String)newValue);
        return;
      case PredicatesPackage.FILE_PREDICATE__CONTENT_PATTERN:
        setContentPattern((String)newValue);
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
      case PredicatesPackage.FILE_PREDICATE__FILE_PATTERN:
        setFilePattern(FILE_PATTERN_EDEFAULT);
        return;
      case PredicatesPackage.FILE_PREDICATE__CONTENT_PATTERN:
        setContentPattern(CONTENT_PATTERN_EDEFAULT);
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
      case PredicatesPackage.FILE_PREDICATE__FILE_PATTERN:
        return FILE_PATTERN_EDEFAULT == null ? filePattern != null : !FILE_PATTERN_EDEFAULT.equals(filePattern);
      case PredicatesPackage.FILE_PREDICATE__CONTENT_PATTERN:
        return CONTENT_PATTERN_EDEFAULT == null ? contentPattern != null : !CONTENT_PATTERN_EDEFAULT.equals(contentPattern);
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
    result.append(" (filePattern: ");
    result.append(filePattern);
    result.append(", contentPattern: ");
    result.append(contentPattern);
    result.append(')');
    return result.toString();
  }

} // FilePredicateImpl
