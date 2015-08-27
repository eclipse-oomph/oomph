/**
 */
package org.eclipse.oomph.setup.sync;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.sync.SyncPackage
 * @generated
 */
public interface SyncFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SyncFactory eINSTANCE = org.eclipse.oomph.setup.sync.impl.SyncFactoryImpl.init();

  /**
   * Returns a new object of class '<em>State</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>State</em>'.
   * @generated
   */
  SyncState createSyncState();

  /**
   * Returns a new object of class '<em>Snapshot</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Snapshot</em>'.
   * @generated
   */
  SyncSnapshot createSyncSnapshot();

  /**
   * Returns a new object of class '<em>Item</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Item</em>'.
   * @generated
   */
  SyncItem createSyncItem();

  /**
   * Returns a new object of class '<em>Remote Sync Item</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Remote Sync Item</em>'.
   * @generated
   */
  RemoteSyncItem createRemoteSyncItem();

  /**
   * Returns a new object of class '<em>Action</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Action</em>'.
   * @generated
   */
  SyncAction createSyncAction();

  /**
   * Returns a new object of class '<em>Delta</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Delta</em>'.
   * @generated
   */
  SyncDelta createSyncDelta();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  SyncPackage getSyncPackage();

} // SyncFactory
