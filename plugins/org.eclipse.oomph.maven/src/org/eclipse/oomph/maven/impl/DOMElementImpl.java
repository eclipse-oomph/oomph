/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.maven.DOMElement;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Parent;
import org.eclipse.oomph.maven.PropertyReference;
import org.eclipse.oomph.maven.util.MavenValidator;
import org.eclipse.oomph.maven.util.MavenValidator.ElementEdit;
import org.eclipse.oomph.maven.util.POMXMLUtil;
import org.eclipse.oomph.maven.util.POMXMLUtil.TextRegion;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DOM Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.impl.DOMElementImpl#getElement <em>Element</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.DOMElementImpl#getPropertyReferences <em>Property References</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class DOMElementImpl extends ModelElementImpl implements DOMElement
{
  /**
   * The default value of the '{@link #getElement() <em>Element</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElement()
   * @generated
   * @ordered
   */
  protected static final Element ELEMENT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getElement() <em>Element</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElement()
   * @generated
   * @ordered
   */
  protected Element element = ELEMENT_EDEFAULT;

  /**
   * The cached value of the '{@link #getPropertyReferences() <em>Property References</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPropertyReferences()
   * @generated
   * @ordered
   */
  protected EList<PropertyReference> propertyReferences;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DOMElementImpl()
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
    return MavenPackage.Literals.DOM_ELEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Element getElement()
  {
    return element;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setElement(Element newElement)
  {
    Element oldElement = element;
    element = newElement;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MavenPackage.DOM_ELEMENT__ELEMENT, oldElement, element));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<PropertyReference> getPropertyReferences()
  {
    if (propertyReferences == null)
    {
      propertyReferences = new EObjectContainmentEList<>(PropertyReference.class, this, MavenPackage.DOM_ELEMENT__PROPERTY_REFERENCES);
    }
    return propertyReferences;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Element getElement(SegmentSequence xpath)
  {
    return POMXMLUtil.getElement(getElement(), xpath);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Map<Document, Map<POMXMLUtil.TextRegion, MavenValidator.ElementEdit>> getElementEdits()
  {
    Diagnostic diagnostic = Diagnostician.INSTANCE.validate(this);
    Map<Document, Map<TextRegion, ElementEdit>> result = new LinkedHashMap<>();
    for (Diagnostic child : diagnostic.getChildren())
    {
      ElementEdit elementEdit = MavenValidator.ElementEdit.of(child);
      if (elementEdit != null)
      {
        Element element = elementEdit.element();
        TextRegion selection = POMXMLUtil.getSelection(element);
        if (selection != null)
        {
          Map<TextRegion, ElementEdit> edits = result.computeIfAbsent(element.getOwnerDocument(), key -> new TreeMap<>());
          edits.put(selection, elementEdit);
        }
      }
    }

    return result;
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
      case MavenPackage.DOM_ELEMENT__PROPERTY_REFERENCES:
        return ((InternalEList<?>)getPropertyReferences()).basicRemove(otherEnd, msgs);
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
      case MavenPackage.DOM_ELEMENT__ELEMENT:
        return getElement();
      case MavenPackage.DOM_ELEMENT__PROPERTY_REFERENCES:
        return getPropertyReferences();
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
      case MavenPackage.DOM_ELEMENT__ELEMENT:
        setElement((Element)newValue);
        return;
      case MavenPackage.DOM_ELEMENT__PROPERTY_REFERENCES:
        getPropertyReferences().clear();
        getPropertyReferences().addAll((Collection<? extends PropertyReference>)newValue);
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
      case MavenPackage.DOM_ELEMENT__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case MavenPackage.DOM_ELEMENT__PROPERTY_REFERENCES:
        getPropertyReferences().clear();
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
      case MavenPackage.DOM_ELEMENT__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case MavenPackage.DOM_ELEMENT__PROPERTY_REFERENCES:
        return propertyReferences != null && !propertyReferences.isEmpty();
    }
    return super.eIsSet(featureID);
  }

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
      case MavenPackage.DOM_ELEMENT___GET_ELEMENT__SEGMENTSEQUENCE:
        return getElement((SegmentSequence)arguments.get(0));
      case MavenPackage.DOM_ELEMENT___GET_ELEMENT_EDITS:
        return getElementEdits();
    }
    return super.eInvoke(operationID, arguments);
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
    result.append(" (element: "); //$NON-NLS-1$
    result.append(element);
    result.append(')');
    return result.toString();
  }

  protected String getElementText(SegmentSequence xpath)
  {
    Element element = getElement(xpath);
    return element == null ? "" : element.getTextContent().strip(); //$NON-NLS-1$
  }

  protected Element getElementWithParent(String elementName)
  {
    Element element = getElement(POMXMLUtil.xpath(elementName));
    if (element == null)
    {
      element = getElement(POMXMLUtil.xpath(Parent.PARENT, elementName));
    }
    return element;
  }

  protected String getElementTextWithParent(String elementName)
  {
    Element element = getElementWithParent(elementName);
    return element == null ? "" : element.getTextContent().strip(); //$NON-NLS-1$
  }

  protected String expandProperties(Element hostElement, String value)
  {
    for (EObject eObject = this; eObject != null; eObject = eObject.eContainer())
    {
      if (eObject instanceof ProjectImpl project)
      {
        return project.expandProperties(this, hostElement, value);
      }
    }

    return value;
  }

} // DOMElementImpl
