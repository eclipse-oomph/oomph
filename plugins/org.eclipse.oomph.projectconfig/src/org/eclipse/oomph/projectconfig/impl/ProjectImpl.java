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
package org.eclipse.oomph.projectconfig.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.projectconfig.PreferenceProfile;
import org.eclipse.oomph.projectconfig.Project;
import org.eclipse.oomph.projectconfig.ProjectConfigPackage;
import org.eclipse.oomph.projectconfig.WorkspaceConfiguration;

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
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.projectconfig.impl.ProjectImpl#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link org.eclipse.oomph.projectconfig.impl.ProjectImpl#getPreferenceProfiles <em>Preference Profiles</em>}</li>
 *   <li>{@link org.eclipse.oomph.projectconfig.impl.ProjectImpl#getPreferenceNode <em>Preference Node</em>}</li>
 *   <li>{@link org.eclipse.oomph.projectconfig.impl.ProjectImpl#getPreferenceProfileReferences <em>Preference Profile References</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProjectImpl extends ModelElementImpl implements Project
{
  /**
   * The cached value of the '{@link #getPreferenceProfiles() <em>Preference Profiles</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPreferenceProfiles()
   * @generated
   * @ordered
   */
  protected EList<PreferenceProfile> preferenceProfiles;

  /**
   * The cached value of the '{@link #getPreferenceNode() <em>Preference Node</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPreferenceNode()
   * @generated
   * @ordered
   */
  protected PreferenceNode preferenceNode;

  /**
   * The cached value of the '{@link #getPreferenceProfileReferences() <em>Preference Profile References</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPreferenceProfileReferences()
   * @generated
   * @ordered
   */
  protected EList<PreferenceProfile> preferenceProfileReferences;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProjectImpl()
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
    return ProjectConfigPackage.Literals.PROJECT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WorkspaceConfiguration getConfiguration()
  {
    if (eContainerFeatureID() != ProjectConfigPackage.PROJECT__CONFIGURATION)
    {
      return null;
    }
    return (WorkspaceConfiguration)eInternalContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetConfiguration(WorkspaceConfiguration newConfiguration, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newConfiguration, ProjectConfigPackage.PROJECT__CONFIGURATION, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setConfiguration(WorkspaceConfiguration newConfiguration)
  {
    if (newConfiguration != eInternalContainer() || eContainerFeatureID() != ProjectConfigPackage.PROJECT__CONFIGURATION && newConfiguration != null)
    {
      if (EcoreUtil.isAncestor(this, newConfiguration))
      {
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      }
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
      {
        msgs = eBasicRemoveFromContainer(msgs);
      }
      if (newConfiguration != null)
      {
        msgs = ((InternalEObject)newConfiguration).eInverseAdd(this, ProjectConfigPackage.WORKSPACE_CONFIGURATION__PROJECTS, WorkspaceConfiguration.class,
            msgs);
      }
      msgs = basicSetConfiguration(newConfiguration, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ProjectConfigPackage.PROJECT__CONFIGURATION, newConfiguration, newConfiguration));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<PreferenceProfile> getPreferenceProfiles()
  {
    if (preferenceProfiles == null)
    {
      preferenceProfiles = new EObjectContainmentWithInverseEList<PreferenceProfile>(PreferenceProfile.class, this,
          ProjectConfigPackage.PROJECT__PREFERENCE_PROFILES, ProjectConfigPackage.PREFERENCE_PROFILE__PROJECT);
    }
    return preferenceProfiles;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PreferenceNode getPreferenceNode()
  {
    if (preferenceNode != null && preferenceNode.eIsProxy())
    {
      InternalEObject oldPreferenceNode = (InternalEObject)preferenceNode;
      preferenceNode = (PreferenceNode)eResolveProxy(oldPreferenceNode);
      if (preferenceNode != oldPreferenceNode)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, ProjectConfigPackage.PROJECT__PREFERENCE_NODE, oldPreferenceNode, preferenceNode));
        }
      }
    }
    return preferenceNode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PreferenceNode basicGetPreferenceNode()
  {
    return preferenceNode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPreferenceNode(PreferenceNode newPreferenceNode)
  {
    PreferenceNode oldPreferenceNode = preferenceNode;
    preferenceNode = newPreferenceNode;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, ProjectConfigPackage.PROJECT__PREFERENCE_NODE, oldPreferenceNode, preferenceNode));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<PreferenceProfile> getPreferenceProfileReferences()
  {
    if (preferenceProfileReferences == null)
    {
      preferenceProfileReferences = new EObjectWithInverseResolvingEList.ManyInverse<PreferenceProfile>(PreferenceProfile.class, this,
          ProjectConfigPackage.PROJECT__PREFERENCE_PROFILE_REFERENCES, ProjectConfigPackage.PREFERENCE_PROFILE__REFERENT_PROJECTS);
    }
    return preferenceProfileReferences;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Property getProperty(URI path)
  {
    for (PreferenceProfile preferenceProfile : getPreferenceProfiles())
    {
      Property property = preferenceProfile.getProperty(path);
      if (property != null)
      {
        return property;
      }
    }

    for (PreferenceProfile preferenceProfile : getPreferenceProfileReferences())
    {
      Property property = preferenceProfile.getProperty(path);
      if (property != null)
      {
        return property;
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
      case ProjectConfigPackage.PROJECT__CONFIGURATION:
        if (eInternalContainer() != null)
        {
          msgs = eBasicRemoveFromContainer(msgs);
        }
        return basicSetConfiguration((WorkspaceConfiguration)otherEnd, msgs);
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getPreferenceProfiles()).basicAdd(otherEnd, msgs);
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILE_REFERENCES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getPreferenceProfileReferences()).basicAdd(otherEnd, msgs);
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
      case ProjectConfigPackage.PROJECT__CONFIGURATION:
        return basicSetConfiguration(null, msgs);
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILES:
        return ((InternalEList<?>)getPreferenceProfiles()).basicRemove(otherEnd, msgs);
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILE_REFERENCES:
        return ((InternalEList<?>)getPreferenceProfileReferences()).basicRemove(otherEnd, msgs);
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
      case ProjectConfigPackage.PROJECT__CONFIGURATION:
        return eInternalContainer().eInverseRemove(this, ProjectConfigPackage.WORKSPACE_CONFIGURATION__PROJECTS, WorkspaceConfiguration.class, msgs);
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
      case ProjectConfigPackage.PROJECT__CONFIGURATION:
        return getConfiguration();
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILES:
        return getPreferenceProfiles();
      case ProjectConfigPackage.PROJECT__PREFERENCE_NODE:
        if (resolve)
        {
          return getPreferenceNode();
        }
        return basicGetPreferenceNode();
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILE_REFERENCES:
        return getPreferenceProfileReferences();
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
      case ProjectConfigPackage.PROJECT__CONFIGURATION:
        setConfiguration((WorkspaceConfiguration)newValue);
        return;
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILES:
        getPreferenceProfiles().clear();
        getPreferenceProfiles().addAll((Collection<? extends PreferenceProfile>)newValue);
        return;
      case ProjectConfigPackage.PROJECT__PREFERENCE_NODE:
        setPreferenceNode((PreferenceNode)newValue);
        return;
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILE_REFERENCES:
        getPreferenceProfileReferences().clear();
        getPreferenceProfileReferences().addAll((Collection<? extends PreferenceProfile>)newValue);
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
      case ProjectConfigPackage.PROJECT__CONFIGURATION:
        setConfiguration((WorkspaceConfiguration)null);
        return;
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILES:
        getPreferenceProfiles().clear();
        return;
      case ProjectConfigPackage.PROJECT__PREFERENCE_NODE:
        setPreferenceNode((PreferenceNode)null);
        return;
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILE_REFERENCES:
        getPreferenceProfileReferences().clear();
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
      case ProjectConfigPackage.PROJECT__CONFIGURATION:
        return getConfiguration() != null;
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILES:
        return preferenceProfiles != null && !preferenceProfiles.isEmpty();
      case ProjectConfigPackage.PROJECT__PREFERENCE_NODE:
        return preferenceNode != null;
      case ProjectConfigPackage.PROJECT__PREFERENCE_PROFILE_REFERENCES:
        return preferenceProfileReferences != null && !preferenceProfileReferences.isEmpty();
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
      case ProjectConfigPackage.PROJECT___GET_PROPERTY__URI:
        return getProperty((URI)arguments.get(0));
    }
    return super.eInvoke(operationID, arguments);
  }

  @Override
  public String eURIFragmentSegment(EStructuralFeature eStructuralFeature, EObject eObject)
  {
    if (eStructuralFeature == ProjectConfigPackage.Literals.PROJECT__PREFERENCE_PROFILES)
    {
      PreferenceProfile child = (PreferenceProfile)eObject;
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

    return super.eURIFragmentSegment(eStructuralFeature, eObject);
  }

  @Override
  public EObject eObjectForURIFragmentSegment(String uriFragmentSegment)
  {
    if (!uriFragmentSegment.startsWith("@"))
    {
      String preferenceProfileName = URI.decode(uriFragmentSegment);
      for (PreferenceProfile preferenceProfile : getPreferenceProfiles())
      {
        if (preferenceProfileName.equals(preferenceProfile.getName()))
        {
          return preferenceProfile;
        }
      }
      return null;
    }
    return super.eObjectForURIFragmentSegment(uriFragmentSegment);
  }
} // ProjectImpl
