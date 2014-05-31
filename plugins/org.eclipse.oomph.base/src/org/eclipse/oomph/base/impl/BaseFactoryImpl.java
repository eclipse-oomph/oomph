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
package org.eclipse.oomph.base.impl;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class BaseFactoryImpl extends EFactoryImpl implements BaseFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static BaseFactory init()
  {
    try
    {
      BaseFactory theBaseFactory = (BaseFactory)EPackage.Registry.INSTANCE.getEFactory(BasePackage.eNS_URI);
      if (theBaseFactory != null)
      {
        return theBaseFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new BaseFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BaseFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EObject createGen(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case BasePackage.ANNOTATION:
        return createAnnotation();
      case BasePackage.STRING_TO_STRING_MAP_ENTRY:
        return (EObject)createStringToStringMapEntry();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  @Override
  public EObject create(EClass eClass)
  {
    if (eClass == BasePackage.Literals.MODEL_ELEMENT)
    {
      return new ModelElementImpl()
      {
      };
    }

    return createGen(eClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case BasePackage.URI:
        return createURIFromString(eDataType, initialValue);
      case BasePackage.EXCEPTION:
        return createExceptionFromString(eDataType, initialValue);
      case BasePackage.TEXT:
        return createTextFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case BasePackage.URI:
        return convertURIToString(eDataType, instanceValue);
      case BasePackage.EXCEPTION:
        return convertExceptionToString(eDataType, instanceValue);
      case BasePackage.TEXT:
        return convertTextToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Annotation createAnnotation()
  {
    AnnotationImpl annotation = new AnnotationImpl();
    return annotation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Map.Entry<String, String> createStringToStringMapEntry()
  {
    StringToStringMapEntryImpl stringToStringMapEntry = new StringToStringMapEntryImpl();
    return stringToStringMapEntry;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public URI createURI(String literal)
  {
    return (URI)super.createFromString(BasePackage.Literals.URI, literal);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public URI createURIFromString(EDataType eDataType, String initialValue)
  {
    return URI.createURI(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertURI(URI instanceValue)
  {
    return super.convertToString(BasePackage.Literals.URI, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertURIToString(EDataType eDataType, Object instanceValue)
  {
    return convertURI((URI)instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Exception createException(String literal)
  {
    return (Exception)super.createFromString(BasePackage.Literals.EXCEPTION, literal);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Exception createExceptionFromString(EDataType eDataType, String initialValue)
  {
    return createException(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertException(Exception instanceValue)
  {
    return super.convertToString(BasePackage.Literals.EXCEPTION, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertExceptionToString(EDataType eDataType, Object instanceValue)
  {
    return convertException((Exception)instanceValue);
  }

  private static final Pattern OS_SPECIFIC_LINE_SEPARATOR_PATTERN = Pattern.compile(StringUtil.NL);

  private static final Pattern NORMALIZED_LINE_SEPARATOR_PATTERN = Pattern.compile("\n");

  private static final boolean NEEDS_CONVERSION = !"\n".equals(StringUtil.NL);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String createText(String literal)
  {
    return literal == null ? null : NEEDS_CONVERSION ? OS_SPECIFIC_LINE_SEPARATOR_PATTERN.matcher(literal).replaceAll("\n") : literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String createTextFromString(EDataType eDataType, String initialValue)
  {
    return createText(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String convertText(String instanceValue)
  {
    return instanceValue == null ? null : NEEDS_CONVERSION ? NORMALIZED_LINE_SEPARATOR_PATTERN.matcher(instanceValue).replaceAll(StringUtil.NL) : instanceValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTextToString(EDataType eDataType, Object instanceValue)
  {
    return convertText((String)instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BasePackage getBasePackage()
  {
    return (BasePackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static BasePackage getPackage()
  {
    return BasePackage.eINSTANCE;
  }

} // BaseFactoryImpl
