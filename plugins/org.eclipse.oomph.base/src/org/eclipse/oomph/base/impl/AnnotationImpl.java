/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.base.impl;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Annotation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.base.impl.AnnotationImpl#getModelElement <em>Model Element</em>}</li>
 *   <li>{@link org.eclipse.oomph.base.impl.AnnotationImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.oomph.base.impl.AnnotationImpl#getDetails <em>Details</em>}</li>
 *   <li>{@link org.eclipse.oomph.base.impl.AnnotationImpl#getContents <em>Contents</em>}</li>
 *   <li>{@link org.eclipse.oomph.base.impl.AnnotationImpl#getReferences <em>References</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AnnotationImpl extends ModelElementImpl implements Annotation
{
  /**
   * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSource()
   * @generated
   * @ordered
   */
  protected static final String SOURCE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSource() <em>Source</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSource()
   * @generated
   * @ordered
   */
  protected String source = SOURCE_EDEFAULT;

  /**
   * The cached value of the '{@link #getDetails() <em>Details</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDetails()
   * @generated
   * @ordered
   */
  protected EMap<String, String> details;

  /**
   * The cached value of the '{@link #getContents() <em>Contents</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContents()
   * @generated
   * @ordered
   */
  protected EList<EObject> contents;

  /**
   * The cached value of the '{@link #getReferences() <em>References</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReferences()
   * @generated
   * @ordered
   */
  protected EList<EObject> references;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AnnotationImpl()
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
    return BasePackage.Literals.ANNOTATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ModelElement getModelElement()
  {
    if (eContainerFeatureID() != BasePackage.ANNOTATION__MODEL_ELEMENT)
    {
      return null;
    }
    return (ModelElement)eInternalContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetModelElement(ModelElement newModelElement, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newModelElement, BasePackage.ANNOTATION__MODEL_ELEMENT, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setModelElement(ModelElement newModelElement)
  {
    if (newModelElement != eInternalContainer() || eContainerFeatureID() != BasePackage.ANNOTATION__MODEL_ELEMENT && newModelElement != null)
    {
      if (EcoreUtil.isAncestor(this, newModelElement))
      {
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString()); //$NON-NLS-1$
      }
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
      {
        msgs = eBasicRemoveFromContainer(msgs);
      }
      if (newModelElement != null)
      {
        msgs = ((InternalEObject)newModelElement).eInverseAdd(this, BasePackage.MODEL_ELEMENT__ANNOTATIONS, ModelElement.class, msgs);
      }
      msgs = basicSetModelElement(newModelElement, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, BasePackage.ANNOTATION__MODEL_ELEMENT, newModelElement, newModelElement));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getSource()
  {
    return source;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSource(String newSource)
  {
    String oldSource = source;
    source = newSource;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, BasePackage.ANNOTATION__SOURCE, oldSource, source));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EMap<String, String> getDetails()
  {
    if (details == null)
    {
      details = new EcoreEMap<>(BasePackage.Literals.STRING_TO_STRING_MAP_ENTRY, StringToStringMapEntryImpl.class, this,
          BasePackage.ANNOTATION__DETAILS);
    }
    return details;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<EObject> getContents()
  {
    if (contents == null)
    {
      contents = new EObjectContainmentEList<>(EObject.class, this, BasePackage.ANNOTATION__CONTENTS);
    }
    return contents;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<EObject> getReferences()
  {
    if (references == null)
    {
      references = new EObjectResolvingEList<>(EObject.class, this, BasePackage.ANNOTATION__REFERENCES);
    }
    return references;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case BasePackage.ANNOTATION__MODEL_ELEMENT:
        if (eInternalContainer() != null)
        {
          msgs = eBasicRemoveFromContainer(msgs);
        }
        return basicSetModelElement((ModelElement)otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
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
      case BasePackage.ANNOTATION__MODEL_ELEMENT:
        return basicSetModelElement(null, msgs);
      case BasePackage.ANNOTATION__DETAILS:
        return ((InternalEList<?>)getDetails()).basicRemove(otherEnd, msgs);
      case BasePackage.ANNOTATION__CONTENTS:
        return ((InternalEList<?>)getContents()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs)
  {
    switch (eContainerFeatureID())
    {
      case BasePackage.ANNOTATION__MODEL_ELEMENT:
        return eInternalContainer().eInverseRemove(this, BasePackage.MODEL_ELEMENT__ANNOTATIONS, ModelElement.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
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
      case BasePackage.ANNOTATION__MODEL_ELEMENT:
        return getModelElement();
      case BasePackage.ANNOTATION__SOURCE:
        return getSource();
      case BasePackage.ANNOTATION__DETAILS:
        if (coreType)
        {
          return getDetails();
        }
        else
        {
          return getDetails().map();
        }
      case BasePackage.ANNOTATION__CONTENTS:
        return getContents();
      case BasePackage.ANNOTATION__REFERENCES:
        return getReferences();
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
      case BasePackage.ANNOTATION__MODEL_ELEMENT:
        setModelElement((ModelElement)newValue);
        return;
      case BasePackage.ANNOTATION__SOURCE:
        setSource((String)newValue);
        return;
      case BasePackage.ANNOTATION__DETAILS:
        ((EStructuralFeature.Setting)getDetails()).set(newValue);
        return;
      case BasePackage.ANNOTATION__CONTENTS:
        getContents().clear();
        getContents().addAll((Collection<? extends EObject>)newValue);
        return;
      case BasePackage.ANNOTATION__REFERENCES:
        getReferences().clear();
        getReferences().addAll((Collection<? extends EObject>)newValue);
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
      case BasePackage.ANNOTATION__MODEL_ELEMENT:
        setModelElement((ModelElement)null);
        return;
      case BasePackage.ANNOTATION__SOURCE:
        setSource(SOURCE_EDEFAULT);
        return;
      case BasePackage.ANNOTATION__DETAILS:
        getDetails().clear();
        return;
      case BasePackage.ANNOTATION__CONTENTS:
        getContents().clear();
        return;
      case BasePackage.ANNOTATION__REFERENCES:
        getReferences().clear();
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
      case BasePackage.ANNOTATION__MODEL_ELEMENT:
        return getModelElement() != null;
      case BasePackage.ANNOTATION__SOURCE:
        return SOURCE_EDEFAULT == null ? source != null : !SOURCE_EDEFAULT.equals(source);
      case BasePackage.ANNOTATION__DETAILS:
        return details != null && !details.isEmpty();
      case BasePackage.ANNOTATION__CONTENTS:
        return contents != null && !contents.isEmpty();
      case BasePackage.ANNOTATION__REFERENCES:
        return references != null && !references.isEmpty();
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
    result.append(" (source: "); //$NON-NLS-1$
    result.append(source);
    result.append(')');
    return result.toString();
  }

} // AnnotationImpl
