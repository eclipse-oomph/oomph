/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.base;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.base.BaseFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.base.edit/icons/full/obj16'"
 * @generated
 */
public interface BasePackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "base";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/base/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "base";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  BasePackage eINSTANCE = org.eclipse.oomph.base.impl.BasePackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.base.impl.ModelElementImpl <em>Model Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.base.impl.ModelElementImpl
   * @see org.eclipse.oomph.base.impl.BasePackageImpl#getModelElement()
   * @generated
   */
  int MODEL_ELEMENT = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ELEMENT__ANNOTATIONS = 0;

  /**
   * The number of structural features of the '<em>Model Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ELEMENT_FEATURE_COUNT = 1;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ELEMENT___GET_ANNOTATION__STRING = 0;

  /**
   * The number of operations of the '<em>Model Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ELEMENT_OPERATION_COUNT = 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.base.impl.AnnotationImpl <em>Annotation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.base.impl.AnnotationImpl
   * @see org.eclipse.oomph.base.impl.BasePackageImpl#getAnnotation()
   * @generated
   */
  int ANNOTATION = 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION__ANNOTATIONS = MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Model Element</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION__MODEL_ELEMENT = MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Source</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION__SOURCE = MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Details</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION__DETAILS = MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Contents</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION__CONTENTS = MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>References</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION__REFERENCES = MODEL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>Annotation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION_FEATURE_COUNT = MODEL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The operation id for the '<em>Get Annotation</em>' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION___GET_ANNOTATION__STRING = MODEL_ELEMENT___GET_ANNOTATION__STRING;

  /**
   * The number of operations of the '<em>Annotation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION_OPERATION_COUNT = MODEL_ELEMENT_OPERATION_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.base.impl.StringToStringMapEntryImpl <em>String To String Map Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.base.impl.StringToStringMapEntryImpl
   * @see org.eclipse.oomph.base.impl.BasePackageImpl#getStringToStringMapEntry()
   * @generated
   */
  int STRING_TO_STRING_MAP_ENTRY = 2;

  /**
   * The feature id for the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_TO_STRING_MAP_ENTRY__KEY = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_TO_STRING_MAP_ENTRY__VALUE = 1;

  /**
   * The number of structural features of the '<em>String To String Map Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_TO_STRING_MAP_ENTRY_FEATURE_COUNT = 2;

  /**
   * The number of operations of the '<em>String To String Map Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_TO_STRING_MAP_ENTRY_OPERATION_COUNT = 0;

  /**
   * The meta object id for the '<em>URI</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.common.util.URI
   * @see org.eclipse.oomph.base.impl.BasePackageImpl#getURI()
   * @generated
   */
  int URI = 3;

  /**
   * The meta object id for the '<em>Exception</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.lang.Exception
   * @see org.eclipse.oomph.base.impl.BasePackageImpl#getException()
   * @generated
   */
  int EXCEPTION = 4;

  /**
   * The meta object id for the '<em>Text</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.lang.String
   * @see org.eclipse.oomph.base.impl.BasePackageImpl#getText()
   * @generated
   */
  int TEXT = 5;

  /**
   * The meta object id for the '<em>ID</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.lang.String
   * @see org.eclipse.oomph.base.impl.BasePackageImpl#getID()
   * @generated
   */
  int ID = 6;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.base.ModelElement <em>Model Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model Element</em>'.
   * @see org.eclipse.oomph.base.ModelElement
   * @generated
   */
  EClass getModelElement();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.base.ModelElement#getAnnotations <em>Annotations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Annotations</em>'.
   * @see org.eclipse.oomph.base.ModelElement#getAnnotations()
   * @see #getModelElement()
   * @generated
   */
  EReference getModelElement_Annotations();

  /**
   * Returns the meta object for the '{@link org.eclipse.oomph.base.ModelElement#getAnnotation(java.lang.String) <em>Get Annotation</em>}' operation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the '<em>Get Annotation</em>' operation.
   * @see org.eclipse.oomph.base.ModelElement#getAnnotation(java.lang.String)
   * @generated
   */
  EOperation getModelElement__GetAnnotation__String();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.base.Annotation <em>Annotation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Annotation</em>'.
   * @see org.eclipse.oomph.base.Annotation
   * @generated
   */
  EClass getAnnotation();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.base.Annotation#getModelElement <em>Model Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Model Element</em>'.
   * @see org.eclipse.oomph.base.Annotation#getModelElement()
   * @see #getAnnotation()
   * @generated
   */
  EReference getAnnotation_ModelElement();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.base.Annotation#getSource <em>Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Source</em>'.
   * @see org.eclipse.oomph.base.Annotation#getSource()
   * @see #getAnnotation()
   * @generated
   */
  EAttribute getAnnotation_Source();

  /**
   * Returns the meta object for the map '{@link org.eclipse.oomph.base.Annotation#getDetails <em>Details</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>Details</em>'.
   * @see org.eclipse.oomph.base.Annotation#getDetails()
   * @see #getAnnotation()
   * @generated
   */
  EReference getAnnotation_Details();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.base.Annotation#getContents <em>Contents</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Contents</em>'.
   * @see org.eclipse.oomph.base.Annotation#getContents()
   * @see #getAnnotation()
   * @generated
   */
  EReference getAnnotation_Contents();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.base.Annotation#getReferences <em>References</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>References</em>'.
   * @see org.eclipse.oomph.base.Annotation#getReferences()
   * @see #getAnnotation()
   * @generated
   */
  EReference getAnnotation_References();

  /**
   * Returns the meta object for class '{@link java.util.Map.Entry <em>String To String Map Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String To String Map Entry</em>'.
   * @see java.util.Map.Entry
   * @model keyDataType="org.eclipse.emf.ecore.EString"
   *        valueDataType="org.eclipse.oomph.base.Text"
   *        valueExtendedMetaData="kind='element'"
   * @generated
   */
  EClass getStringToStringMapEntry();

  /**
   * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Key</em>'.
   * @see java.util.Map.Entry
   * @see #getStringToStringMapEntry()
   * @generated
   */
  EAttribute getStringToStringMapEntry_Key();

  /**
   * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see java.util.Map.Entry
   * @see #getStringToStringMapEntry()
   * @generated
   */
  EAttribute getStringToStringMapEntry_Value();

  /**
   * Returns the meta object for data type '{@link org.eclipse.emf.common.util.URI <em>URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>URI</em>'.
   * @see org.eclipse.emf.common.util.URI
   * @model instanceClass="org.eclipse.emf.common.util.URI"
   * @generated
   */
  EDataType getURI();

  /**
   * Returns the meta object for data type '{@link java.lang.Exception <em>Exception</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Exception</em>'.
   * @see java.lang.Exception
   * @model instanceClass="java.lang.Exception"
   * @generated
   */
  EDataType getException();

  /**
   * Returns the meta object for data type '{@link java.lang.String <em>Text</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Text</em>'.
   * @see java.lang.String
   * @model instanceClass="java.lang.String"
   * @generated
   */
  EDataType getText();

  /**
   * Returns the meta object for data type '{@link java.lang.String <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>ID</em>'.
   * @see java.lang.String
   * @model instanceClass="java.lang.String"
   *        extendedMetaData="pattern='[\\i-[:]][\\c-[:]]*'"
   * @generated
   */
  EDataType getID();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  BaseFactory getBaseFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each operation of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipse.oomph.base.impl.ModelElementImpl <em>Model Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.base.impl.ModelElementImpl
     * @see org.eclipse.oomph.base.impl.BasePackageImpl#getModelElement()
     * @generated
     */
    EClass MODEL_ELEMENT = eINSTANCE.getModelElement();

    /**
     * The meta object literal for the '<em><b>Annotations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL_ELEMENT__ANNOTATIONS = eINSTANCE.getModelElement_Annotations();

    /**
     * The meta object literal for the '<em><b>Get Annotation</b></em>' operation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EOperation MODEL_ELEMENT___GET_ANNOTATION__STRING = eINSTANCE.getModelElement__GetAnnotation__String();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.base.impl.AnnotationImpl <em>Annotation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.base.impl.AnnotationImpl
     * @see org.eclipse.oomph.base.impl.BasePackageImpl#getAnnotation()
     * @generated
     */
    EClass ANNOTATION = eINSTANCE.getAnnotation();

    /**
     * The meta object literal for the '<em><b>Model Element</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ANNOTATION__MODEL_ELEMENT = eINSTANCE.getAnnotation_ModelElement();

    /**
     * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ANNOTATION__SOURCE = eINSTANCE.getAnnotation_Source();

    /**
     * The meta object literal for the '<em><b>Details</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ANNOTATION__DETAILS = eINSTANCE.getAnnotation_Details();

    /**
     * The meta object literal for the '<em><b>Contents</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ANNOTATION__CONTENTS = eINSTANCE.getAnnotation_Contents();

    /**
     * The meta object literal for the '<em><b>References</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ANNOTATION__REFERENCES = eINSTANCE.getAnnotation_References();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.base.impl.StringToStringMapEntryImpl <em>String To String Map Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.base.impl.StringToStringMapEntryImpl
     * @see org.eclipse.oomph.base.impl.BasePackageImpl#getStringToStringMapEntry()
     * @generated
     */
    EClass STRING_TO_STRING_MAP_ENTRY = eINSTANCE.getStringToStringMapEntry();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_TO_STRING_MAP_ENTRY__KEY = eINSTANCE.getStringToStringMapEntry_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_TO_STRING_MAP_ENTRY__VALUE = eINSTANCE.getStringToStringMapEntry_Value();

    /**
     * The meta object literal for the '<em>URI</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.common.util.URI
     * @see org.eclipse.oomph.base.impl.BasePackageImpl#getURI()
     * @generated
     */
    EDataType URI = eINSTANCE.getURI();

    /**
     * The meta object literal for the '<em>Exception</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.Exception
     * @see org.eclipse.oomph.base.impl.BasePackageImpl#getException()
     * @generated
     */
    EDataType EXCEPTION = eINSTANCE.getException();

    /**
     * The meta object literal for the '<em>Text</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see org.eclipse.oomph.base.impl.BasePackageImpl#getText()
     * @generated
     */
    EDataType TEXT = eINSTANCE.getText();

    /**
     * The meta object literal for the '<em>ID</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.lang.String
     * @see org.eclipse.oomph.base.impl.BasePackageImpl#getID()
     * @generated
     */
    EDataType ID = eINSTANCE.getID();

  }

} // BasePackage
