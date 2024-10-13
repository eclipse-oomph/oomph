/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.impl;

import org.eclipse.oomph.maven.Coordinate;
import org.eclipse.oomph.maven.DOMElement;
import org.eclipse.oomph.maven.Dependency;
import org.eclipse.oomph.maven.MavenFactory;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Parent;
import org.eclipse.oomph.maven.Project;
import org.eclipse.oomph.maven.Property;
import org.eclipse.oomph.maven.PropertyReference;
import org.eclipse.oomph.maven.Realm;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.w3c.dom.Element;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.impl.ProjectImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.ProjectImpl#getRealm <em>Realm</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.ProjectImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.ProjectImpl#getDependencies <em>Dependencies</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.ProjectImpl#getManagedDependencies <em>Managed Dependencies</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.ProjectImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.ProjectImpl#getIncomingParentReferences <em>Incoming Parent References</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.ProjectImpl#getIncomingDependencyReferences <em>Incoming Dependency References</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProjectImpl extends CoordinateImpl implements Project
{
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
   * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected String location = LOCATION_EDEFAULT;

  /**
   * The cached value of the '{@link #getParent() <em>Parent</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParent()
   * @generated
   * @ordered
   */
  protected Parent parent;

  /**
   * The cached value of the '{@link #getDependencies() <em>Dependencies</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDependencies()
   * @generated
   * @ordered
   */
  protected EList<Dependency> dependencies;

  /**
   * The cached value of the '{@link #getManagedDependencies() <em>Managed Dependencies</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getManagedDependencies()
   * @generated
   * @ordered
   */
  protected EList<Dependency> managedDependencies;

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
   * The cached value of the '{@link #getIncomingParentReferences() <em>Incoming Parent References</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIncomingParentReferences()
   * @generated
   * @ordered
   */
  protected EList<Parent> incomingParentReferences;

  /**
   * The cached value of the '{@link #getIncomingDependencyReferences() <em>Incoming Dependency References</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIncomingDependencyReferences()
   * @generated
   * @ordered
   */
  protected EList<Dependency> incomingDependencyReferences;

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
    return MavenPackage.Literals.PROJECT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getLocation()
  {
    return location;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setLocation(String newLocation)
  {
    String oldLocation = location;
    location = newLocation;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MavenPackage.PROJECT__LOCATION, oldLocation, location));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Realm getRealm()
  {
    if (eContainerFeatureID() != MavenPackage.PROJECT__REALM)
    {
      return null;
    }
    return (Realm)eInternalContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Parent getParent()
  {
    return parent;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetParent(Parent newParent, NotificationChain msgs)
  {
    Parent oldParent = parent;
    parent = newParent;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MavenPackage.PROJECT__PARENT, oldParent, newParent);
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
  public void setParent(Parent newParent)
  {
    if (newParent != parent)
    {
      NotificationChain msgs = null;
      if (parent != null)
      {
        msgs = ((InternalEObject)parent).eInverseRemove(this, MavenPackage.PARENT__PROJECT, Parent.class, msgs);
      }
      if (newParent != null)
      {
        msgs = ((InternalEObject)newParent).eInverseAdd(this, MavenPackage.PARENT__PROJECT, Parent.class, msgs);
      }
      msgs = basicSetParent(newParent, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MavenPackage.PROJECT__PARENT, newParent, newParent));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Dependency> getDependencies()
  {
    if (dependencies == null)
    {
      dependencies = new EObjectContainmentEList<>(Dependency.class, this, MavenPackage.PROJECT__DEPENDENCIES);
    }
    return dependencies;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Dependency> getManagedDependencies()
  {
    if (managedDependencies == null)
    {
      managedDependencies = new EObjectContainmentEList<>(Dependency.class, this, MavenPackage.PROJECT__MANAGED_DEPENDENCIES);
    }
    return managedDependencies;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Property> getProperties()
  {
    if (properties == null)
    {
      properties = new EObjectContainmentEList<>(Property.class, this, MavenPackage.PROJECT__PROPERTIES);
    }
    return properties;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Parent> getIncomingParentReferences()
  {
    if (incomingParentReferences == null)
    {
      incomingParentReferences = new EObjectWithInverseResolvingEList<>(Parent.class, this, MavenPackage.PROJECT__INCOMING_PARENT_REFERENCES,
          MavenPackage.PARENT__RESOLVED_PROJECT);
    }
    return incomingParentReferences;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Dependency> getIncomingDependencyReferences()
  {
    if (incomingDependencyReferences == null)
    {
      incomingDependencyReferences = new EObjectWithInverseResolvingEList<>(Dependency.class, this,
          MavenPackage.PROJECT__INCOMING_DEPENDENCY_REFERENCES, MavenPackage.DEPENDENCY__RESOLVED_PROJECT);
    }
    return incomingDependencyReferences;
  }

  private final Map<String, Property> propertiesByName = new LinkedHashMap<>();

  private boolean recursive;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Property getProperty(String key)
  {
    if (recursive)
    {
      return null;
    }

    EList<Property> properties = getProperties();
    if (!properties.isEmpty() && propertiesByName.isEmpty())
    {
      for (Property property : properties)
      {
        propertiesByName.put(property.getKey(), property);
      }
    }

    Property property = propertiesByName.get(key);
    if (property == null)
    {
      Parent parent = getParent();
      if (parent != null)
      {
        Project parentProject = parent.getResolvedProject();
        if (parentProject != null)
        {
          try
          {
            recursive = true;
            property = parentProject.getProperty(key);
          }
          finally
          {
            recursive = false;
          }
        }
      }
    }

    return property;
  }

  private final Map<Coordinate, Dependency> managedDependenciesByGroupArtifact = new TreeMap<>(Coordinate.COMPARATOR_IGNORE_VERSION);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Dependency getManagedDependency(Dependency dependency)
  {
    if (recursive)
    {
      return null;
    }

    EList<Dependency> managedDependencies = getManagedDependencies();
    if (!managedDependencies.isEmpty() && managedDependenciesByGroupArtifact.isEmpty())
    {
      for (Dependency managedDependency : managedDependencies)
      {
        managedDependenciesByGroupArtifact.put(managedDependency, managedDependency);
      }
    }

    Dependency managedDependency = managedDependenciesByGroupArtifact.get(dependency);
    if (managedDependency == null)
    {
      Parent parent = getParent();
      if (parent != null)
      {
        Project parentProject = parent.getResolvedProject();
        if (parentProject != null)
        {
          try
          {
            recursive = true;
            managedDependency = parentProject.getManagedDependency(dependency);
          }
          finally
          {
            recursive = false;
          }
        }
      }
    }

    return managedDependency;
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
      case MavenPackage.PROJECT__REALM:
        if (eInternalContainer() != null)
        {
          msgs = eBasicRemoveFromContainer(msgs);
        }
        return eBasicSetContainer(otherEnd, MavenPackage.PROJECT__REALM, msgs);
      case MavenPackage.PROJECT__PARENT:
        if (parent != null)
        {
          msgs = ((InternalEObject)parent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MavenPackage.PROJECT__PARENT, null, msgs);
        }
        return basicSetParent((Parent)otherEnd, msgs);
      case MavenPackage.PROJECT__INCOMING_PARENT_REFERENCES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getIncomingParentReferences()).basicAdd(otherEnd, msgs);
      case MavenPackage.PROJECT__INCOMING_DEPENDENCY_REFERENCES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getIncomingDependencyReferences()).basicAdd(otherEnd, msgs);
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
      case MavenPackage.PROJECT__REALM:
        return eBasicSetContainer(null, MavenPackage.PROJECT__REALM, msgs);
      case MavenPackage.PROJECT__PARENT:
        return basicSetParent(null, msgs);
      case MavenPackage.PROJECT__DEPENDENCIES:
        return ((InternalEList<?>)getDependencies()).basicRemove(otherEnd, msgs);
      case MavenPackage.PROJECT__MANAGED_DEPENDENCIES:
        return ((InternalEList<?>)getManagedDependencies()).basicRemove(otherEnd, msgs);
      case MavenPackage.PROJECT__PROPERTIES:
        return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
      case MavenPackage.PROJECT__INCOMING_PARENT_REFERENCES:
        return ((InternalEList<?>)getIncomingParentReferences()).basicRemove(otherEnd, msgs);
      case MavenPackage.PROJECT__INCOMING_DEPENDENCY_REFERENCES:
        return ((InternalEList<?>)getIncomingDependencyReferences()).basicRemove(otherEnd, msgs);
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
      case MavenPackage.PROJECT__REALM:
        return eInternalContainer().eInverseRemove(this, MavenPackage.REALM__PROJECTS, Realm.class, msgs);
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
      case MavenPackage.PROJECT__LOCATION:
        return getLocation();
      case MavenPackage.PROJECT__REALM:
        return getRealm();
      case MavenPackage.PROJECT__PARENT:
        return getParent();
      case MavenPackage.PROJECT__DEPENDENCIES:
        return getDependencies();
      case MavenPackage.PROJECT__MANAGED_DEPENDENCIES:
        return getManagedDependencies();
      case MavenPackage.PROJECT__PROPERTIES:
        return getProperties();
      case MavenPackage.PROJECT__INCOMING_PARENT_REFERENCES:
        return getIncomingParentReferences();
      case MavenPackage.PROJECT__INCOMING_DEPENDENCY_REFERENCES:
        return getIncomingDependencyReferences();
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
      case MavenPackage.PROJECT__LOCATION:
        setLocation((String)newValue);
        return;
      case MavenPackage.PROJECT__PARENT:
        setParent((Parent)newValue);
        return;
      case MavenPackage.PROJECT__DEPENDENCIES:
        getDependencies().clear();
        getDependencies().addAll((Collection<? extends Dependency>)newValue);
        return;
      case MavenPackage.PROJECT__MANAGED_DEPENDENCIES:
        getManagedDependencies().clear();
        getManagedDependencies().addAll((Collection<? extends Dependency>)newValue);
        return;
      case MavenPackage.PROJECT__PROPERTIES:
        getProperties().clear();
        getProperties().addAll((Collection<? extends Property>)newValue);
        return;
      case MavenPackage.PROJECT__INCOMING_PARENT_REFERENCES:
        getIncomingParentReferences().clear();
        getIncomingParentReferences().addAll((Collection<? extends Parent>)newValue);
        return;
      case MavenPackage.PROJECT__INCOMING_DEPENDENCY_REFERENCES:
        getIncomingDependencyReferences().clear();
        getIncomingDependencyReferences().addAll((Collection<? extends Dependency>)newValue);
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
      case MavenPackage.PROJECT__LOCATION:
        setLocation(LOCATION_EDEFAULT);
        return;
      case MavenPackage.PROJECT__PARENT:
        setParent((Parent)null);
        return;
      case MavenPackage.PROJECT__DEPENDENCIES:
        getDependencies().clear();
        return;
      case MavenPackage.PROJECT__MANAGED_DEPENDENCIES:
        getManagedDependencies().clear();
        return;
      case MavenPackage.PROJECT__PROPERTIES:
        getProperties().clear();
        return;
      case MavenPackage.PROJECT__INCOMING_PARENT_REFERENCES:
        getIncomingParentReferences().clear();
        return;
      case MavenPackage.PROJECT__INCOMING_DEPENDENCY_REFERENCES:
        getIncomingDependencyReferences().clear();
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
      case MavenPackage.PROJECT__LOCATION:
        return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
      case MavenPackage.PROJECT__REALM:
        return getRealm() != null;
      case MavenPackage.PROJECT__PARENT:
        return parent != null;
      case MavenPackage.PROJECT__DEPENDENCIES:
        return dependencies != null && !dependencies.isEmpty();
      case MavenPackage.PROJECT__MANAGED_DEPENDENCIES:
        return managedDependencies != null && !managedDependencies.isEmpty();
      case MavenPackage.PROJECT__PROPERTIES:
        return properties != null && !properties.isEmpty();
      case MavenPackage.PROJECT__INCOMING_PARENT_REFERENCES:
        return incomingParentReferences != null && !incomingParentReferences.isEmpty();
      case MavenPackage.PROJECT__INCOMING_DEPENDENCY_REFERENCES:
        return incomingDependencyReferences != null && !incomingDependencyReferences.isEmpty();
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
      case MavenPackage.PROJECT___GET_PROPERTY__STRING:
        return getProperty((String)arguments.get(0));
      case MavenPackage.PROJECT___GET_MANAGED_DEPENDENCY__DEPENDENCY:
        return getManagedDependency((Dependency)arguments.get(0));
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
    result.append(" (location: "); //$NON-NLS-1$
    result.append(location);
    result.append(')');
    return result.toString();
  }

  // {
  private static final Pattern PROPERTY_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}"); //$NON-NLS-1$

  private final Set<String> keys = new LinkedHashSet<>();

  protected String expandProperties(DOMElement host, Element hostElement, String value)
  {
    Matcher matcher = PROPERTY_PATTERN.matcher(value);
    if (matcher.find())
    {
      StringBuilder builder = new StringBuilder();
      do
      {
        String replacement = null;
        String key = matcher.group(1);
        try
        {
          // Prevent stack overflow.
          if (keys.add(key))
          {
            Property property = getProperty(key);
            if (property != null)
            {
              replacement = property.getExpandedValue();
            }

            createPropertyReference(host, hostElement, key, property);
          }
        }
        finally
        {
          keys.remove(key);
        }

        matcher.appendReplacement(builder, Matcher.quoteReplacement(replacement == null ? matcher.group() : replacement));
      } while (matcher.find());

      matcher.appendTail(builder);

      return builder.toString();
    }

    return value;
  }

  private void createPropertyReference(DOMElement host, Element hostElement, String key, Property property)
  {
    PropertyReference propertyReference = MavenFactory.eINSTANCE.createPropertyReference();
    propertyReference.setElement(hostElement);
    propertyReference.setName(key);
    propertyReference.setResolvedProperty(property);
    host.getPropertyReferences().add(propertyReference);
  }

} // ProjectImpl
