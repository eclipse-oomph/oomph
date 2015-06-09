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
package org.eclipse.oomph.setup;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Location Catalog</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.LocationCatalog#getInstallations <em>Installations</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.LocationCatalog#getWorkspaces <em>Workspaces</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getLocationCatalog()
 * @model
 * @generated
 */
public interface LocationCatalog extends EObject
{
  /**
   * Returns the value of the '<em><b>Installations</b></em>' map.
   * The key is of type {@link org.eclipse.oomph.setup.Installation},
   * and the value is of type list of {@link org.eclipse.oomph.setup.Workspace},
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Installations</em>' map isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Installations</em>' map.
   * @see org.eclipse.oomph.setup.SetupPackage#getLocationCatalog_Installations()
   * @model mapType="org.eclipse.oomph.setup.InstallationToWorkspacesMapEntry<org.eclipse.oomph.setup.Installation, org.eclipse.oomph.setup.Workspace>"
   *        extendedMetaData="name='installation'"
   * @generated
   */
  EMap<Installation, EList<Workspace>> getInstallations();

  /**
   * Returns the value of the '<em><b>Workspaces</b></em>' map.
   * The key is of type {@link org.eclipse.oomph.setup.Workspace},
   * and the value is of type list of {@link org.eclipse.oomph.setup.Installation},
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Workspaces</em>' map isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Workspaces</em>' map.
   * @see org.eclipse.oomph.setup.SetupPackage#getLocationCatalog_Workspaces()
   * @model mapType="org.eclipse.oomph.setup.WorkspaceToInstallationsMapEntry<org.eclipse.oomph.setup.Workspace, org.eclipse.oomph.setup.Installation>"
   *        extendedMetaData="name='workspace'"
   * @generated
   */
  EMap<Workspace, EList<Installation>> getWorkspaces();

} // LocationCatalog
