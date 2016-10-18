/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.git;

import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Clone Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.git.GitCloneTask#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.GitCloneTask#getRemoteName <em>Remote Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.GitCloneTask#getRemoteURI <em>Remote URI</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.GitCloneTask#getPushURI <em>Push URI</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.GitCloneTask#getCheckoutBranch <em>Checkout Branch</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.GitCloneTask#isRecursive <em>Recursive</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.GitCloneTask#getConfigSections <em>Config Sections</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.GitCloneTask#isRestrictToCheckoutBranch <em>Restrict To Checkout Branch</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.git.GitPackage#getGitCloneTask()
 * @model annotation="http://www.eclipse.org/emf/2002/EcoreXXX constraints='IDRequired LocationOptional'"
 *        annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface GitCloneTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Location</b></em>' attribute.
   * The default value is <code>""</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Location</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Location</em>' attribute.
   * @see #setLocation(String)
   * @see org.eclipse.oomph.setup.git.GitPackage#getGitCloneTask_Location()
   * @model default="" required="true"
   *        annotation="http://www.eclipse.org/oomph/setup/Variable filter='canonical' type='STRING' label='Git clone location rule' description='The rule for the absolute folder location where the Git clone is located' explicitType='FOLDER' explicitLabel='${@id.description} Git clone location' explicitDescription='The absolute folder location where the ${@id.description} Git clone is located'"
   *        annotation="http://www.eclipse.org/oomph/setup/RuleVariable name='git.container.root' type='FOLDER' label='Root Git-container folder' defaultValue='${user.home}' description='The root Git-container folder where all the Git clones are located' storageURI='scope://'"
   * @generated
   */
  String getLocation();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.GitCloneTask#getLocation <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Location</em>' attribute.
   * @see #getLocation()
   * @generated
   */
  void setLocation(String value);

  /**
   * Returns the value of the '<em><b>Remote Name</b></em>' attribute.
   * The default value is <code>"origin"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Remote Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Remote Name</em>' attribute.
   * @see #setRemoteName(String)
   * @see org.eclipse.oomph.setup.git.GitPackage#getGitCloneTask_RemoteName()
   * @model default="origin" required="true"
   * @generated
   */
  String getRemoteName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.GitCloneTask#getRemoteName <em>Remote Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Remote Name</em>' attribute.
   * @see #getRemoteName()
   * @generated
   */
  void setRemoteName(String value);

  /**
   * Returns the value of the '<em><b>Remote URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Remote URI</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Remote URI</em>' attribute.
   * @see #setRemoteURI(String)
   * @see org.eclipse.oomph.setup.git.GitPackage#getGitCloneTask_RemoteURI()
   * @model required="true"
   * @generated
   */
  String getRemoteURI();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.GitCloneTask#getRemoteURI <em>Remote URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Remote URI</em>' attribute.
   * @see #getRemoteURI()
   * @generated
   */
  void setRemoteURI(String value);

  /**
   * Returns the value of the '<em><b>Push URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Push URI</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Push URI</em>' attribute.
   * @see #setPushURI(String)
   * @see org.eclipse.oomph.setup.git.GitPackage#getGitCloneTask_PushURI()
   * @model
   * @generated
   */
  String getPushURI();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.GitCloneTask#getPushURI <em>Push URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Push URI</em>' attribute.
   * @see #getPushURI()
   * @generated
   */
  void setPushURI(String value);

  /**
   * Returns the value of the '<em><b>Checkout Branch</b></em>' attribute.
   * The default value is <code>"${scope.project.stream.name}"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Checkout Branch</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Checkout Branch</em>' attribute.
   * @see #setCheckoutBranch(String)
   * @see org.eclipse.oomph.setup.git.GitPackage#getGitCloneTask_CheckoutBranch()
   * @model default="${scope.project.stream.name}" required="true"
   * @generated
   */
  String getCheckoutBranch();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.GitCloneTask#getCheckoutBranch <em>Checkout Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Checkout Branch</em>' attribute.
   * @see #getCheckoutBranch()
   * @generated
   */
  void setCheckoutBranch(String value);

  /**
   * Returns the value of the '<em><b>Recursive</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Recursive</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Recursive</em>' attribute.
   * @see #setRecursive(boolean)
   * @see org.eclipse.oomph.setup.git.GitPackage#getGitCloneTask_Recursive()
   * @model default="false"
   * @generated
   */
  boolean isRecursive();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.GitCloneTask#isRecursive <em>Recursive</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Recursive</em>' attribute.
   * @see #isRecursive()
   * @generated
   */
  void setRecursive(boolean value);

  /**
   * Returns the value of the '<em><b>Config Sections</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.git.ConfigSection}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Config Sections</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Config Sections</em>' containment reference list.
   * @see org.eclipse.oomph.setup.git.GitPackage#getGitCloneTask_ConfigSections()
   * @model containment="true"
   * @generated
   */
  EList<ConfigSection> getConfigSections();

  /**
   * Returns the value of the '<em><b>Restrict To Checkout Branch</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Restrict To Checkout Branch</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Restrict To Checkout Branch</em>' attribute.
   * @see #setRestrictToCheckoutBranch(boolean)
   * @see org.eclipse.oomph.setup.git.GitPackage#getGitCloneTask_RestrictToCheckoutBranch()
   * @model default="false"
   * @generated
   */
  boolean isRestrictToCheckoutBranch();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.git.GitCloneTask#isRestrictToCheckoutBranch <em>Restrict To Checkout Branch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Restrict To Checkout Branch</em>' attribute.
   * @see #isRestrictToCheckoutBranch()
   * @generated
   */
  void setRestrictToCheckoutBranch(boolean value);

} // GitCloneTask
