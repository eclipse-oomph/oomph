/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven;

import org.eclipse.emf.common.CommonPlugin;

import java.util.Comparator;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coordinate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.Coordinate#getGroupId <em>Group Id</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Coordinate#getArtifactId <em>Artifact Id</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Coordinate#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Coordinate#getExpandedGroupId <em>Expanded Group Id</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Coordinate#getExpandedVersion <em>Expanded Version</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.maven.MavenPackage#getCoordinate()
 * @model abstract="true"
 * @generated
 */
public interface Coordinate extends DOMElement
{
  String GROUP_ID = "groupId"; //$NON-NLS-1$

  String ARTIFACT_ID = "artifactId"; //$NON-NLS-1$

  String VERSION = "version"; //$NON-NLS-1$

  Comparator<Coordinate> COMPARATOR = new Comparator<>()
  {
    private final Comparator<String> stringComparator = CommonPlugin.INSTANCE.getComparator();

    @Override
    public int compare(Coordinate o1, Coordinate o2)
    {
      int result = stringComparator.compare(o1.getExpandedGroupId(), o2.getExpandedGroupId());
      if (result == 0)
      {
        result = stringComparator.compare(o1.getArtifactId(), o2.getArtifactId());
        if (result == 0)
        {
          result = stringComparator.compare(o1.getExpandedVersion(), o2.getExpandedVersion());
        }
      }

      return result;
    }
  };

  Comparator<Coordinate> COMPARATOR_IGNORE_VERSION = new Comparator<>()
  {
    private final Comparator<String> stringComparator = CommonPlugin.INSTANCE.getComparator();

    @Override
    public int compare(Coordinate o1, Coordinate o2)
    {
      int result = stringComparator.compare(o1.getExpandedGroupId(), o2.getExpandedGroupId());
      if (result == 0)
      {
        result = stringComparator.compare(o1.getArtifactId(), o2.getArtifactId());
      }

      return result;
    }
  };

  /**
   * Returns the value of the '<em><b>Group Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Group Id</em>' attribute.
   * @see org.eclipse.oomph.maven.MavenPackage#getCoordinate_GroupId()
   * @model required="true" transient="true" changeable="false" derived="true"
   * @generated
   */
  String getGroupId();

  /**
   * Returns the value of the '<em><b>Artifact Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Artifact Id</em>' attribute.
   * @see org.eclipse.oomph.maven.MavenPackage#getCoordinate_ArtifactId()
   * @model required="true" transient="true" changeable="false" derived="true"
   * @generated
   */
  String getArtifactId();

  /**
   * Returns the value of the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Version</em>' attribute.
   * @see org.eclipse.oomph.maven.MavenPackage#getCoordinate_Version()
   * @model transient="true" changeable="false" derived="true"
   * @generated
   */
  String getVersion();

  /**
   * Returns the value of the '<em><b>Expanded Group Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expanded Group Id</em>' attribute.
   * @see org.eclipse.oomph.maven.MavenPackage#getCoordinate_ExpandedGroupId()
   * @model required="true" transient="true" changeable="false" derived="true"
   * @generated
   */
  String getExpandedGroupId();

  /**
   * Returns the value of the '<em><b>Expanded Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expanded Version</em>' attribute.
   * @see org.eclipse.oomph.maven.MavenPackage#getCoordinate_ExpandedVersion()
   * @model changeable="false" derived="true"
   * @generated
   */
  String getExpandedVersion();

} // Coordinate
