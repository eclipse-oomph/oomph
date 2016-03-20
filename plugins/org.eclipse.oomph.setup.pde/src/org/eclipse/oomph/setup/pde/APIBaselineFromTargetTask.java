/**
 */
package org.eclipse.oomph.setup.pde;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>API Baseline From Target Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.pde.APIBaselineFromTargetTask#getTargetName <em>Target Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.pde.PDEPackage#getAPIBaselineFromTargetTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface APIBaselineFromTargetTask extends AbstractAPIBaselineTask
{
  /**
   * Returns the value of the '<em><b>Target Name</b></em>' attribute.
   * The default value is <code>""</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target Name</em>' attribute.
   * @see #setTargetName(String)
   * @see org.eclipse.oomph.setup.pde.PDEPackage#getAPIBaselineFromTargetTask_TargetName()
   * @model default="" required="true"
   *        annotation="http://www.eclipse.org/oomph/setup/Variable type='STRING' label='API baseline location rule' description='The rule for the absolute folder location where the API baseline is located' explicitType='FOLDER' explicitLabel='${@id.name}-${@id.version} API baseline location' explicitDescription='The absolute folder location where the ${@id.name}-${@id.version} API baseline is located'"
   *        annotation="http://www.eclipse.org/oomph/setup/RuleVariable name='api.baselines.root' type='FOLDER' label='Root API baselines folder' description='The root API baselines folder where all the API baselines are located' storageURI='scope://'"
   * @generated
   */
  String getTargetName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.pde.APIBaselineFromTargetTask#getTargetName <em>Target Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target Name</em>' attribute.
   * @see #getTargetName()
   * @generated
   */
  void setTargetName(String value);

} // APIBaselineFromTargetTask
