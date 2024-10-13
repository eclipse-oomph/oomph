/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.impl;

import org.eclipse.oomph.maven.Coordinate;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.util.POMXMLUtil;

import org.eclipse.emf.ecore.EClass;

import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Coordinate</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.impl.CoordinateImpl#getGroupId <em>Group Id</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.CoordinateImpl#getArtifactId <em>Artifact Id</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.CoordinateImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.CoordinateImpl#getExpandedGroupId <em>Expanded Group Id</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.CoordinateImpl#getExpandedVersion <em>Expanded Version</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class CoordinateImpl extends DOMElementImpl implements Coordinate
{
  /**
   * The default value of the '{@link #getGroupId() <em>Group Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGroupId()
   * @generated
   * @ordered
   */
  protected static final String GROUP_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getGroupId() <em>Group Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGroupId()
   * @generated
   * @ordered
   */
  protected String groupId = GROUP_ID_EDEFAULT;

  /**
   * The default value of the '{@link #getArtifactId() <em>Artifact Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArtifactId()
   * @generated
   * @ordered
   */
  protected static final String ARTIFACT_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getArtifactId() <em>Artifact Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArtifactId()
   * @generated
   * @ordered
   */
  protected String artifactId = ARTIFACT_ID_EDEFAULT;

  /**
   * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
  protected static final String VERSION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
  protected String version = VERSION_EDEFAULT;

  /**
   * The default value of the '{@link #getExpandedGroupId() <em>Expanded Group Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpandedGroupId()
   * @generated
   * @ordered
   */
  protected static final String EXPANDED_GROUP_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getExpandedGroupId() <em>Expanded Group Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpandedGroupId()
   * @generated
   * @ordered
   */
  protected String expandedGroupId = EXPANDED_GROUP_ID_EDEFAULT;

  /**
   * The default value of the '{@link #getExpandedVersion() <em>Expanded Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpandedVersion()
   * @generated
   * @ordered
   */
  protected static final String EXPANDED_VERSION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getExpandedVersion() <em>Expanded Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpandedVersion()
   * @generated
   * @ordered
   */
  protected String expandedVersion = EXPANDED_VERSION_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CoordinateImpl()
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
    return MavenPackage.Literals.COORDINATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getGroupId()
  {
    if (groupId == null)
    {
      groupId = getElementTextWithParent(GROUP_ID);
    }

    return groupId;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getArtifactId()
  {
    if (artifactId == null)
    {
      artifactId = getElementText(POMXMLUtil.xpath(ARTIFACT_ID));
    }

    return artifactId;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getVersion()
  {
    if (version == null)
    {
      version = getElementTextWithParent(VERSION);
    }

    return version;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getExpandedGroupId()
  {
    if (expandedGroupId == null)
    {
      Element groupIdElement = getElementWithParent(GROUP_ID);
      if (groupIdElement != null)
      {
        expandedGroupId = expandProperties(groupIdElement, groupIdElement.getTextContent().strip());
      }
      else
      {
        expandedGroupId = ""; //$NON-NLS-1$
      }
    }

    return expandedGroupId;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getExpandedVersion()
  {
    if (expandedVersion == null && getVersion() != null)
    {
      Element versionElement = getElementWithParent(VERSION);
      if (versionElement != null)
      {
        expandedVersion = expandProperties(versionElement, versionElement.getTextContent().strip());
      }
      else
      {
        expandedVersion = ""; //$NON-NLS-1$
      }
    }

    return expandedVersion;
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
      case MavenPackage.COORDINATE__GROUP_ID:
        return getGroupId();
      case MavenPackage.COORDINATE__ARTIFACT_ID:
        return getArtifactId();
      case MavenPackage.COORDINATE__VERSION:
        return getVersion();
      case MavenPackage.COORDINATE__EXPANDED_GROUP_ID:
        return getExpandedGroupId();
      case MavenPackage.COORDINATE__EXPANDED_VERSION:
        return getExpandedVersion();
    }
    return super.eGet(featureID, resolve, coreType);
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
      case MavenPackage.COORDINATE__GROUP_ID:
        return GROUP_ID_EDEFAULT == null ? groupId != null : !GROUP_ID_EDEFAULT.equals(groupId);
      case MavenPackage.COORDINATE__ARTIFACT_ID:
        return ARTIFACT_ID_EDEFAULT == null ? artifactId != null : !ARTIFACT_ID_EDEFAULT.equals(artifactId);
      case MavenPackage.COORDINATE__VERSION:
        return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
      case MavenPackage.COORDINATE__EXPANDED_GROUP_ID:
        return EXPANDED_GROUP_ID_EDEFAULT == null ? expandedGroupId != null : !EXPANDED_GROUP_ID_EDEFAULT.equals(expandedGroupId);
      case MavenPackage.COORDINATE__EXPANDED_VERSION:
        return EXPANDED_VERSION_EDEFAULT == null ? expandedVersion != null : !EXPANDED_VERSION_EDEFAULT.equals(expandedVersion);
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
    result.append(" (groupId: "); //$NON-NLS-1$
    result.append(groupId);
    result.append(", artifactId: "); //$NON-NLS-1$
    result.append(artifactId);
    result.append(", version: "); //$NON-NLS-1$
    result.append(version);
    result.append(", expandedGroupId: "); //$NON-NLS-1$
    result.append(expandedGroupId);
    result.append(", expandedVersion: "); //$NON-NLS-1$
    result.append(expandedVersion);
    result.append(')');
    return result.toString();
  }

} // CoordinateImpl
