/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.RequirementType;
import org.eclipse.oomph.p2.VersionSegment;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
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
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#isGreedy <em>Greedy</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#getFilter <em>Filter</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#getMin <em>Min</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#getMax <em>Max</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.RequirementImpl#getDescription <em>Description</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequirementImpl extends ModelElementImpl implements Requirement
{
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
  protected static final String NAMESPACE_EDEFAULT = "org.eclipse.equinox.p2.iu"; //$NON-NLS-1$

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
      "0.0.0"); //$NON-NLS-1$

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
   * The default value of the '{@link #isGreedy() <em>Greedy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isGreedy()
   * @generated
   * @ordered
   */
  protected static final boolean GREEDY_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isGreedy() <em>Greedy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isGreedy()
   * @generated
   * @ordered
   */
  protected boolean greedy = GREEDY_EDEFAULT;

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
   * The default value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected static final RequirementType TYPE_EDEFAULT = RequirementType.NONE;

  /**
   * The default value of the '{@link #getMin() <em>Min</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMin()
   * @generated
   * @ordered
   */
  protected static final int MIN_EDEFAULT = 1;

  /**
   * The cached value of the '{@link #getMin() <em>Min</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMin()
   * @generated
   * @ordered
   */
  protected int min = MIN_EDEFAULT;

  /**
   * The default value of the '{@link #getMax() <em>Max</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMax()
   * @generated
   * @ordered
   */
  protected static final int MAX_EDEFAULT = 1;

  /**
   * The cached value of the '{@link #getMax() <em>Max</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMax()
   * @generated
   * @ordered
   */
  protected int max = MAX_EDEFAULT;

  /**
   * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDescription()
   * @generated
   * @ordered
   */
  protected static final String DESCRIPTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDescription()
   * @generated
   * @ordered
   */
  protected String description = DESCRIPTION_EDEFAULT;

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

  public IRequirement toIRequirement()
  {
    String namespace = getNamespace();
    String name = getName();
    VersionRange versionRange = getVersionRange();
    IMatchExpression<IInstallableUnit> filter = getMatchExpression();
    int min = getMin();
    boolean greedy = isGreedy();
    int max = getMax();
    String description = null;
    IRequirement requirement = name == null || !name.startsWith("(") //$NON-NLS-1$
        ? MetadataFactory.createRequirement(namespace, name, versionRange, filter, min, max, greedy, description)
        : MetadataFactory.createRequirement(namespace, name, filter, min, max, greedy, description);
    return requirement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean isOptional()
  {
    return min <= 0;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setOptional(boolean newOptional)
  {
    setMin(newOptional ? 0 : 1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean isFeature()
  {
    String id = getName();
    return id != null && id.endsWith(FEATURE_SUFFIX);
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

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public RequirementType getType()
  {
    String name = getName();
    if (name != null)
    {
      if (name.endsWith(FEATURE_SUFFIX))
      {
        return RequirementType.FEATURE;

      }

      if (name.endsWith(PROJECT_SUFFIX))
      {
        return RequirementType.PROJECT;
      }
    }

    return RequirementType.NONE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getMin()
  {
    return min;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setMin(int newMin)
  {
    boolean oldOptional = isOptional();
    int oldMin = min;
    min = newMin;
    boolean newOptional = isOptional();
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.REQUIREMENT__MIN, oldMin, min));
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.REQUIREMENT__OPTIONAL, oldOptional, newOptional));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getMax()
  {
    return max;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMax(int newMax)
  {
    int oldMax = max;
    max = newMax;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.REQUIREMENT__MAX, oldMax, max));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDescription(String newDescription)
  {
    String oldDescription = description;
    description = newDescription;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.REQUIREMENT__DESCRIPTION, oldDescription, description));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isGreedy()
  {
    return greedy;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setGreedy(boolean newGreedy)
  {
    boolean oldGreedy = greedy;
    greedy = newGreedy;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.REQUIREMENT__GREEDY, oldGreedy, greedy));
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
      case P2Package.REQUIREMENT__GREEDY:
        return isGreedy();
      case P2Package.REQUIREMENT__FILTER:
        return getFilter();
      case P2Package.REQUIREMENT__TYPE:
        return getType();
      case P2Package.REQUIREMENT__MIN:
        return getMin();
      case P2Package.REQUIREMENT__MAX:
        return getMax();
      case P2Package.REQUIREMENT__DESCRIPTION:
        return getDescription();
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
      case P2Package.REQUIREMENT__GREEDY:
        setGreedy((Boolean)newValue);
        return;
      case P2Package.REQUIREMENT__FILTER:
        setFilter((String)newValue);
        return;
      case P2Package.REQUIREMENT__MIN:
        setMin((Integer)newValue);
        return;
      case P2Package.REQUIREMENT__MAX:
        setMax((Integer)newValue);
        return;
      case P2Package.REQUIREMENT__DESCRIPTION:
        setDescription((String)newValue);
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
      case P2Package.REQUIREMENT__GREEDY:
        setGreedy(GREEDY_EDEFAULT);
        return;
      case P2Package.REQUIREMENT__FILTER:
        setFilter(FILTER_EDEFAULT);
        return;
      case P2Package.REQUIREMENT__MIN:
        setMin(MIN_EDEFAULT);
        return;
      case P2Package.REQUIREMENT__MAX:
        setMax(MAX_EDEFAULT);
        return;
      case P2Package.REQUIREMENT__DESCRIPTION:
        setDescription(DESCRIPTION_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case P2Package.REQUIREMENT__OPTIONAL:
        return min == 0;
      case P2Package.REQUIREMENT__MIN:
        return min != 0 && min != 1;
      default:
        return eIsSetGen(featureID);
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("null")
  private boolean eIsSetGen(int featureID)
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
        return isOptional() != OPTIONAL_EDEFAULT;
      case P2Package.REQUIREMENT__GREEDY:
        return greedy != GREEDY_EDEFAULT;
      case P2Package.REQUIREMENT__FILTER:
        return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
      case P2Package.REQUIREMENT__TYPE:
        return getType() != TYPE_EDEFAULT;
      case P2Package.REQUIREMENT__MIN:
        return min != MIN_EDEFAULT;
      case P2Package.REQUIREMENT__MAX:
        return max != MAX_EDEFAULT;
      case P2Package.REQUIREMENT__DESCRIPTION:
        return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
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
    result.append(namespace);
    result.append(':');
    result.append(name);

    if (versionRange != null && !versionRange.equals(VersionRange.emptyRange))
    {
      result.append(" "); //$NON-NLS-1$
      result.append(versionRange);
    }

    if (min != 1)
    {
      result.append(" (min=" + min + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    if (max != 1)
    {
      result.append(" (max=" + max + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    if (greedy && min == 0)
    {
      result.append(" (greedy)"); //$NON-NLS-1$
    }

    if (filter != null)
    {
      result.append(" (filter=" + filter + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    if (description != null)
    {
      result.append(" (description=" + description + ")"); //$NON-NLS-1$ //$NON-NLS-2$
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
