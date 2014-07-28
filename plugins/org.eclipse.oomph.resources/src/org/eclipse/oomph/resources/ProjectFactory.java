/**
 */
package org.eclipse.oomph.resources;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.resources.backend.BackendContainer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project Factory</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.oomph.resources.ResourcesPackage#getProjectFactory()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ProjectFactory extends ModelElement
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model dataType="org.eclipse.oomph.predicates.Project" backendContainerDataType="org.eclipse.oomph.resources.BackendContainer" monitorDataType="org.eclipse.oomph.resources.ProgressMonitor"
   * @generated
   */
  IProject createProject(BackendContainer backendContainer, IProgressMonitor monitor);

} // ProjectFactory
