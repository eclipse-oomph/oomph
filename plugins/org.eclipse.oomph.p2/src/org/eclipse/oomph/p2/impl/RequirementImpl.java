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
package org.eclipse.oomph.p2.impl;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseAnnotationConstants;
import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;

import java.lang.reflect.InvocationTargetException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Requirement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#getID <em>ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#getVersionRange <em>Version Range</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#isOptional <em>Optional</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#isFeature <em>Feature</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#getFilter <em>Filter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequirementImpl extends ModelElementImpl implements Requirement
{
  private static final String ANNOTATION_KEY = "platform:/plugin/org.eclipse.oomph.base/model/legacy/setup.ecore#//Component/type";

  /**
   * The default value of the '{@link #getID() <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getID()
   * @generated
   * @ordered
   */
  protected static final String ID_EDEFAULT = null;

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
   * The default value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamespace()
   * @generated
   * @ordered
   */
  protected static final String NAMESPACE_EDEFAULT = "org.eclipse.equinox.p2.iu";

  /**
   * The cached value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamespace()
   * @generated
   * @ordered
   */
  protected String namespace = NAMESPACE_EDEFAULT;

  /**
   * The default value of the '{@link #getVersionRange() <em>Version Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersionRange()
   * @generated
   * @ordered
   */
  protected static final VersionRange VERSION_RANGE_EDEFAULT = (VersionRange)P2Factory.eINSTANCE.createFromString(P2Package.eINSTANCE.getVersionRange(),
      "0.0.0");

  /**
   * The cached value of the '{@link #getVersionRange() <em>Version Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersionRange()
   * @generated
   * @ordered
   */
  protected VersionRange versionRange = VERSION_RANGE_EDEFAULT;

  /**
   * The default value of the '{@link #isOptional() <em>Optional</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOptional()
   * @generated
   * @ordered
   */
  protected static final boolean OPTIONAL_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isOptional() <em>Optional</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOptional()
   * @generated
   * @ordered
   */
  protected boolean optional = OPTIONAL_EDEFAULT;

  /**
   * The default value of the '{@link #isFeature() <em>Feature</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isFeature()
   * @generated
   * @ordered
   */
  protected static final boolean FEATURE_EDEFAULT = false;

  /**
   * The default value of the '{@link #getFilter() <em>Filter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilter()
   * @generated
   * @ordered
   */
  protected static final String FILTER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFilter() <em>Filter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilter()
   * @generated
   * @ordered
   */
  protected String filter = FILTER_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RequirementImpl()
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
    return P2Package.Literals.REQUIREMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String getID()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setID(String newID)
  {
    setName(newID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.REQUIREMENT__NAME, oldName, name));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getNamespace()
  {
    return namespace;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNamespace(String newNamespace)
  {
    String oldNamespace = namespace;
    namespace = newNamespace;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.REQUIREMENT__NAMESPACE, oldNamespace, namespace));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public VersionRange getVersionRange()
  {
    return versionRange;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVersionRange(VersionRange newVersionRange)
  {
    VersionRange oldVersionRange = versionRange;
    versionRange = newVersionRange;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.REQUIREMENT__VERSION_RANGE, oldVersionRange, versionRange));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setVersionRange(Version version, VersionSegment segment)
  {
    setVersionRange(P2Factory.eINSTANCE.createVersionRange(version, segment));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isOptional()
  {
    return optional;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOptional(boolean newOptional)
  {
    boolean oldOptional = optional;
    optional = newOptional;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.REQUIREMENT__OPTIONAL, oldOptional, optional));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean isFeature()
  {
    String id = getName();
    return id != null && id.endsWith(".feature.group");
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getFilter()
  {
    return filter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFilter(String newFilter)
  {
    String oldFilter = filter;
    filter = newFilter;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.REQUIREMENT__FILTER, oldFilter, filter));
    }
  }

  public IMatchExpression<IInstallableUnit> getMatchExpression()
  {
    String filter = getFilter();
    return parseMatchExpression(filter);
  }

  public void setMatchExpression(IMatchExpression<IInstallableUnit> matchExpression)
  {
    String filter = formatMatchExpression(matchExpression);
    setFilter(filter);
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
      case P2Package.REQUIREMENT__ID:
        return getID();
      case P2Package.REQUIREMENT__NAME:
        return getName();
      case P2Package.REQUIREMENT__NAMESPACE:
        return getNamespace();
      case P2Package.REQUIREMENT__VERSION_RANGE:
        return getVersionRange();
      case P2Package.REQUIREMENT__OPTIONAL:
        return isOptional();
      case P2Package.REQUIREMENT__FEATURE:
        return isFeature();
      case P2Package.REQUIREMENT__FILTER:
        return getFilter();
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
      case P2Package.REQUIREMENT__ID:
        setID((String)newValue);
        return;
      case P2Package.REQUIREMENT__NAME:
        setName((String)newValue);
        return;
      case P2Package.REQUIREMENT__NAMESPACE:
        setNamespace((String)newValue);
        return;
      case P2Package.REQUIREMENT__VERSION_RANGE:
        setVersionRange((VersionRange)newValue);
        return;
      case P2Package.REQUIREMENT__OPTIONAL:
        setOptional((Boolean)newValue);
        return;
      case P2Package.REQUIREMENT__FILTER:
        setFilter((String)newValue);
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
      case P2Package.REQUIREMENT__ID:
        setID(ID_EDEFAULT);
        return;
      case P2Package.REQUIREMENT__NAME:
        setName(NAME_EDEFAULT);
        return;
      case P2Package.REQUIREMENT__NAMESPACE:
        setNamespace(NAMESPACE_EDEFAULT);
        return;
      case P2Package.REQUIREMENT__VERSION_RANGE:
        setVersionRange(VERSION_RANGE_EDEFAULT);
        return;
      case P2Package.REQUIREMENT__OPTIONAL:
        setOptional(OPTIONAL_EDEFAULT);
        return;
      case P2Package.REQUIREMENT__FILTER:
        setFilter(FILTER_EDEFAULT);
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
      case P2Package.REQUIREMENT__ID:
        return ID_EDEFAULT == null ? getID() != null : !ID_EDEFAULT.equals(getID());
      case P2Package.REQUIREMENT__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case P2Package.REQUIREMENT__NAMESPACE:
        return NAMESPACE_EDEFAULT == null ? namespace != null : !NAMESPACE_EDEFAULT.equals(namespace);
      case P2Package.REQUIREMENT__VERSION_RANGE:
        return VERSION_RANGE_EDEFAULT == null ? versionRange != null : !VERSION_RANGE_EDEFAULT.equals(versionRange);
      case P2Package.REQUIREMENT__OPTIONAL:
        return optional != OPTIONAL_EDEFAULT;
      case P2Package.REQUIREMENT__FEATURE:
        return isFeature() != FEATURE_EDEFAULT;
      case P2Package.REQUIREMENT__FILTER:
        return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
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
      case P2Package.REQUIREMENT___SET_VERSION_RANGE__VERSION_VERSIONSEGMENT:
        setVersionRange((Version)arguments.get(0), (VersionSegment)arguments.get(1));
        return null;
    }
    return super.eInvoke(operationID, arguments);
  }

  protected void eMigrate()
  {
    Annotation annotation = getAnnotation(BaseAnnotationConstants.ANNOTATION_SOURCE);
    if (annotation != null)
    {
      EMap<String, String> details = annotation.getDetails();
      String value = details.get(ANNOTATION_KEY);
      boolean remove = false;
      if ("eclipse.feature".equals(value))
      {
        String id = getName();
        if (id != null)
        {
          setName(id + ".feature.group");
          remove = true;
        }
      }
      else if ("osgi.bundle".equals(value))
      {
        remove = true;
      }

      if (remove)
      {
        details.remove(ANNOTATION_KEY);
        if (annotation.getDetails().isEmpty())
        {
          getAnnotations().remove(annotation);
        }
      }
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer();
    result.append(name);

    if (versionRange != null && !versionRange.equals(VersionRange.emptyRange))
    {
      result.append(" ");
      result.append(versionRange);
    }

    if (optional)
    {
      result.append(" (optional)");
    }

    return result.toString();
  }

  public static IMatchExpression<IInstallableUnit> parseMatchExpression(String filter)
  {
    if (filter == null)
    {
      return null;
    }

    return InstallableUnit.parseFilter(filter);
  }

  public static String formatMatchExpression(IMatchExpression<IInstallableUnit> matchExpression)
  {
    if (matchExpression == null)
    {
      return null;
    }

    return matchExpression.getParameters()[0].toString();
    // StringBuffer buffer = new StringBuffer();
    // matchExpression.toLDAPString(buffer);
    // return buffer.toString();
  }

} // RequirementImpl
