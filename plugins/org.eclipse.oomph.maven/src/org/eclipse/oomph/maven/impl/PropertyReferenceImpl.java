/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.impl;

import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Property;
import org.eclipse.oomph.maven.PropertyReference;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.impl.PropertyReferenceImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.PropertyReferenceImpl#getResolvedProperty <em>Resolved Property</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PropertyReferenceImpl extends DOMElementImpl implements PropertyReference
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getResolvedProperty() <em>Resolved Property</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResolvedProperty()
   * @generated
   * @ordered
   */
  protected Property resolvedProperty;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PropertyReferenceImpl()
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
    return MavenPackage.Literals.PROPERTY_REFERENCE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MavenPackage.PROPERTY_REFERENCE__NAME, oldName, name));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Property getResolvedProperty()
  {
    if (resolvedProperty != null && resolvedProperty.eIsProxy())
    {
      InternalEObject oldResolvedProperty = (InternalEObject)resolvedProperty;
      resolvedProperty = (Property)eResolveProxy(oldResolvedProperty);
      if (resolvedProperty != oldResolvedProperty)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, MavenPackage.PROPERTY_REFERENCE__RESOLVED_PROPERTY, oldResolvedProperty, resolvedProperty));
        }
      }
    }
    return resolvedProperty;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Property basicGetResolvedProperty()
  {
    return resolvedProperty;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetResolvedProperty(Property newResolvedProperty, NotificationChain msgs)
  {
    Property oldResolvedProperty = resolvedProperty;
    resolvedProperty = newResolvedProperty;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MavenPackage.PROPERTY_REFERENCE__RESOLVED_PROPERTY, oldResolvedProperty,
          newResolvedProperty);
      if (msgs == null)
      {
        msgs = notification;
      }
      else
      {
        msgs.add(notification);
      }
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setResolvedProperty(Property newResolvedProperty)
  {
    if (newResolvedProperty != resolvedProperty)
    {
      NotificationChain msgs = null;
      if (resolvedProperty != null)
      {
        msgs = ((InternalEObject)resolvedProperty).eInverseRemove(this, MavenPackage.PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES, Property.class, msgs);
      }
      if (newResolvedProperty != null)
      {
        msgs = ((InternalEObject)newResolvedProperty).eInverseAdd(this, MavenPackage.PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES, Property.class, msgs);
      }
      msgs = basicSetResolvedProperty(newResolvedProperty, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MavenPackage.PROPERTY_REFERENCE__RESOLVED_PROPERTY, newResolvedProperty, newResolvedProperty));
    }
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
      case MavenPackage.PROPERTY_REFERENCE__RESOLVED_PROPERTY:
        if (resolvedProperty != null)
        {
          msgs = ((InternalEObject)resolvedProperty).eInverseRemove(this, MavenPackage.PROPERTY__INCOMING_RESOLVED_PROPERTY_REFERENCES, Property.class, msgs);
        }
        return basicSetResolvedProperty((Property)otherEnd, msgs);
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
      case MavenPackage.PROPERTY_REFERENCE__RESOLVED_PROPERTY:
        return basicSetResolvedProperty(null, msgs);
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
      case MavenPackage.PROPERTY_REFERENCE__NAME:
        return getName();
      case MavenPackage.PROPERTY_REFERENCE__RESOLVED_PROPERTY:
        if (resolve)
        {
          return getResolvedProperty();
        }
        return basicGetResolvedProperty();
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
      case MavenPackage.PROPERTY_REFERENCE__NAME:
        setName((String)newValue);
        return;
      case MavenPackage.PROPERTY_REFERENCE__RESOLVED_PROPERTY:
        setResolvedProperty((Property)newValue);
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
      case MavenPackage.PROPERTY_REFERENCE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case MavenPackage.PROPERTY_REFERENCE__RESOLVED_PROPERTY:
        setResolvedProperty((Property)null);
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
      case MavenPackage.PROPERTY_REFERENCE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case MavenPackage.PROPERTY_REFERENCE__RESOLVED_PROPERTY:
        return resolvedProperty != null;
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
    result.append(" (name: "); //$NON-NLS-1$
    result.append(name);
    result.append(')');
    return result.toString();
  }

} // PropertyReferenceImpl
