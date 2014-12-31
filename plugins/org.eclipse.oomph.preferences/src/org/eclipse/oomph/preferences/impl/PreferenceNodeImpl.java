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
package org.eclipse.oomph.preferences.impl;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesPackage;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.preferences.util.PreferencesUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.runtime.IPath;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Preference Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PreferenceNodeImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PreferenceNodeImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PreferenceNodeImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.eclipse.oomph.preferences.impl.PreferenceNodeImpl#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PreferenceNodeImpl extends PreferenceItemImpl implements PreferenceNode
{
  /**
   * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChildren()
   * @generated
   * @ordered
   */
  protected EList<PreferenceNode> children;

  /**
   * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProperties()
   * @generated
   * @ordered
   */
  protected EList<Property> properties;

  /**
   * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected static final String LOCATION_EDEFAULT = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PreferenceNodeImpl()
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
    return PreferencesPackage.Literals.PREFERENCE_NODE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<PreferenceNode> getChildren()
  {
    if (children == null)
    {
      children = new EObjectContainmentWithInverseEList<PreferenceNode>(PreferenceNode.class, this, PreferencesPackage.PREFERENCE_NODE__CHILDREN,
          PreferencesPackage.PREFERENCE_NODE__PARENT);
    }
    return children;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PreferenceNode getParent()
  {
    if (eContainerFeatureID() != PreferencesPackage.PREFERENCE_NODE__PARENT)
    {
      return null;
    }
    return (PreferenceNode)eInternalContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetParent(PreferenceNode newParent, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newParent, PreferencesPackage.PREFERENCE_NODE__PARENT, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setParent(PreferenceNode newParent)
  {
    if (newParent != eInternalContainer() || eContainerFeatureID() != PreferencesPackage.PREFERENCE_NODE__PARENT && newParent != null)
    {
      if (EcoreUtil.isAncestor(this, newParent))
      {
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      }
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
      {
        msgs = eBasicRemoveFromContainer(msgs);
      }
      if (newParent != null)
      {
        msgs = ((InternalEObject)newParent).eInverseAdd(this, PreferencesPackage.PREFERENCE_NODE__CHILDREN, PreferenceNode.class, msgs);
      }
      msgs = basicSetParent(newParent, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PreferencesPackage.PREFERENCE_NODE__PARENT, newParent, newParent));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Property> getProperties()
  {
    if (properties == null)
    {
      properties = new EObjectContainmentWithInverseEList<Property>(Property.class, this, PreferencesPackage.PREFERENCE_NODE__PROPERTIES,
          PreferencesPackage.PROPERTY__PARENT);
    }
    return properties;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String getLocation()
  {
    try
    {
      Preferences preferences = PreferencesUtil.getPreferences(this, false);
      IPath path = PreferencesUtil.getLocation(preferences);
      return path == null ? null : path.toString();
    }
    catch (BackingStoreException ex)
    {
      // Ignore
      return null;
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public PreferenceNode getNode(String name)
  {
    for (PreferenceNode node : getChildren())
    {
      if (name.equals(node.getName()))
      {
        return node;
      }
    }

    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public PreferenceNode getNode(URI path)
  {
    PreferenceNode preferenceNode = this;

    String authority = path.authority();
    if (authority != null)
    {
      preferenceNode = getRoot();
      if (!"".equals(authority))
      {
        preferenceNode = preferenceNode.getNode(URI.decode(authority));
      }
    }

    if (preferenceNode == null)
    {
      return null;
    }

    for (String segment : path.segments())
    {
      preferenceNode = preferenceNode.getNode(URI.decode(segment));
      if (preferenceNode == null)
      {
        return null;
      }
    }

    return preferenceNode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Property getProperty(String name)
  {
    for (Property property : getProperties())
    {
      if (name.equals(property.getName()))
      {
        return property;
      }
    }

    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Property getProperty(URI path)
  {
    if (path.segmentCount() == 0)
    {
      return null;
    }

    PreferenceNode preferenceNode = getNode(path.trimSegments(1));
    if (preferenceNode == null)
    {
      return null;
    }

    return preferenceNode.getProperty(URI.decode(path.lastSegment()));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public PreferenceNode getAncestor()
  {
    String name = getName();
    if (name == null)
    {
      return null;
    }

    URI absolutePath = getAbsolutePath();
    if (absolutePath == null)
    {
      return null;
    }

    URI relativePath = getRelativePath();
    if (relativePath == null)
    {
      return null;
    }

    PreferenceNode root = getRoot();
    if (root == null)
    {
      return null;
    }

    boolean matched = false;
    String authority = absolutePath.authority();
    if ("project".equals(authority))
    {
      matched = true;

      PreferenceNode instancePreferenceNode = root.getNode("instance");
      if (instancePreferenceNode != null)
      {
        PreferenceNode preferenceNode = instancePreferenceNode.getNode(relativePath);
        if (preferenceNode != null)
        {
          return preferenceNode;
        }
      }
    }

    if (matched || "instance".equals(authority))
    {
      matched = true;

      PreferenceNode instancePreferenceNode = root.getNode("configuration");
      if (instancePreferenceNode != null)
      {
        PreferenceNode preferenceNode = instancePreferenceNode.getNode(relativePath);
        if (preferenceNode != null)
        {
          return preferenceNode;
        }
      }
    }

    if (matched || "configuration".equals(authority))
    {
      PreferenceNode instancePreferenceNode = root.getNode("default");
      if (instancePreferenceNode != null)
      {
        PreferenceNode preferenceNode = instancePreferenceNode.getNode(relativePath);
        if (preferenceNode != null)
        {
          return preferenceNode;
        }
      }
    }

    if (matched || "default".equals(authority))
    {
      PreferenceNode instancePreferenceNode = root.getNode("bundle_default");
      if (instancePreferenceNode != null)
      {
        PreferenceNode preferenceNode = instancePreferenceNode.getNode(relativePath);
        if (preferenceNode != null)
        {
          return preferenceNode;
        }
      }
    }

    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case PreferencesPackage.PREFERENCE_NODE__PARENT:
        if (eInternalContainer() != null)
        {
          msgs = eBasicRemoveFromContainer(msgs);
        }
        return basicSetParent((PreferenceNode)otherEnd, msgs);
      case PreferencesPackage.PREFERENCE_NODE__CHILDREN:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildren()).basicAdd(otherEnd, msgs);
      case PreferencesPackage.PREFERENCE_NODE__PROPERTIES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getProperties()).basicAdd(otherEnd, msgs);
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
      case PreferencesPackage.PREFERENCE_NODE__PARENT:
        return basicSetParent(null, msgs);
      case PreferencesPackage.PREFERENCE_NODE__CHILDREN:
        return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
      case PreferencesPackage.PREFERENCE_NODE__PROPERTIES:
        return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
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
      case PreferencesPackage.PREFERENCE_NODE__PARENT:
        return eInternalContainer().eInverseRemove(this, PreferencesPackage.PREFERENCE_NODE__CHILDREN, PreferenceNode.class, msgs);
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
      case PreferencesPackage.PREFERENCE_NODE__PARENT:
        return getParent();
      case PreferencesPackage.PREFERENCE_NODE__CHILDREN:
        return getChildren();
      case PreferencesPackage.PREFERENCE_NODE__PROPERTIES:
        return getProperties();
      case PreferencesPackage.PREFERENCE_NODE__LOCATION:
        return getLocation();
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
      case PreferencesPackage.PREFERENCE_NODE__PARENT:
        setParent((PreferenceNode)newValue);
        return;
      case PreferencesPackage.PREFERENCE_NODE__CHILDREN:
        getChildren().clear();
        getChildren().addAll((Collection<? extends PreferenceNode>)newValue);
        return;
      case PreferencesPackage.PREFERENCE_NODE__PROPERTIES:
        getProperties().clear();
        getProperties().addAll((Collection<? extends Property>)newValue);
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
      case PreferencesPackage.PREFERENCE_NODE__PARENT:
        setParent((PreferenceNode)null);
        return;
      case PreferencesPackage.PREFERENCE_NODE__CHILDREN:
        getChildren().clear();
        return;
      case PreferencesPackage.PREFERENCE_NODE__PROPERTIES:
        getProperties().clear();
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
      case PreferencesPackage.PREFERENCE_NODE__PARENT:
        return getParent() != null;
      case PreferencesPackage.PREFERENCE_NODE__CHILDREN:
        return children != null && !children.isEmpty();
      case PreferencesPackage.PREFERENCE_NODE__PROPERTIES:
        return properties != null && !properties.isEmpty();
      case PreferencesPackage.PREFERENCE_NODE__LOCATION:
        return LOCATION_EDEFAULT == null ? getLocation() != null : !LOCATION_EDEFAULT.equals(getLocation());
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
      case PreferencesPackage.PREFERENCE_NODE___GET_NODE__STRING:
        return getNode((String)arguments.get(0));
      case PreferencesPackage.PREFERENCE_NODE___GET_NODE__URI:
        return getNode((URI)arguments.get(0));
      case PreferencesPackage.PREFERENCE_NODE___GET_PROPERTY__STRING:
        return getProperty((String)arguments.get(0));
      case PreferencesPackage.PREFERENCE_NODE___GET_PROPERTY__URI:
        return getProperty((URI)arguments.get(0));
      case PreferencesPackage.PREFERENCE_NODE___GET_ANCESTOR:
        return getAncestor();
    }
    return super.eInvoke(operationID, arguments);
  }

  @Override
  public String eURIFragmentSegment(EStructuralFeature eStructuralFeature, EObject eObject)
  {
    if (eStructuralFeature == PreferencesPackage.Literals.PREFERENCE_NODE__CHILDREN)
    {
      PreferenceNode child = (PreferenceNode)eObject;
      String name = child.getName();
      if (name != null)
      {
        String encodedName = URI.encodeSegment(name, false);
        if (encodedName.startsWith("@"))
        {
          encodedName = "%40" + encodedName.substring(1);
        }
        return encodedName;
      }
    }

    if (eStructuralFeature == PreferencesPackage.Literals.PREFERENCE_NODE__PROPERTIES)
    {
      Property property = (Property)eObject;
      String name = property.getName();
      if (name != null)
      {
        return "^" + URI.encodeSegment(name, false);
      }
    }

    return super.eURIFragmentSegment(eStructuralFeature, eObject);
  }

  @Override
  public EObject eObjectForURIFragmentSegment(String uriFragmentSegment)
  {
    if (uriFragmentSegment.startsWith("^"))
    {
      String preferenceNodeName = URI.decode(uriFragmentSegment.substring(1));
      return getProperty(preferenceNodeName);
    }

    if (!uriFragmentSegment.startsWith("@"))
    {
      String preferenceNodeName = URI.decode(uriFragmentSegment);
      return getNode(preferenceNodeName);
    }

    return super.eObjectForURIFragmentSegment(uriFragmentSegment);
  }

  @Override
  public URI getAbsolutePath()
  {
    String name = getName();
    if (name == null)
    {
      return null;
    }

    PreferenceNode parent = getParent();
    if (parent == null)
    {
      return URI.createHierarchicalURI(null, URI.encodeAuthority(name, false), null, null, null);
    }

    URI parentAbsolutePath = parent.getAbsolutePath();
    if (parentAbsolutePath == null)
    {
      return null;
    }

    if ("".equals(parentAbsolutePath.authority()))
    {
      return URI.createHierarchicalURI(null, URI.encodeAuthority(name, false), null, null, null);
    }

    return parentAbsolutePath.appendSegment(URI.encodeSegment(name, false));
  }

  @Override
  public URI getRelativePath()
  {
    String name = getName();
    if (name == null)
    {
      return null;
    }

    PreferenceNode scope = getScope();
    if (this == scope)
    {
      return URI.createURI("");
    }

    URI parentRelativePath = getParent().getRelativePath();
    if (parentRelativePath == null)
    {
      return null;
    }

    return parentRelativePath.appendSegment(URI.encodeSegment(name, false));
  }

  @Override
  public PreferenceNode getScope()
  {
    PreferenceNode result = this;

    for (PreferenceNode parent = getParent(); parent != null;)
    {
      PreferenceNode grandParent = parent.getParent();
      if (grandParent == null)
      {
        break;
      }

      PreferenceNode greatGrandParent = grandParent.getParent();
      if (greatGrandParent == null)
      {
        if ("project".equals(parent.getName()))
        {
          break;
        }
      }

      result = parent;
      parent = grandParent;
    }

    return result;
  }

} // PreferenceNodeImpl
