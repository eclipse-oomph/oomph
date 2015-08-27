/**
 */
package org.eclipse.oomph.setup.sync.util;

import org.eclipse.oomph.setup.sync.RemoteSyncItem;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncItem;
import org.eclipse.oomph.setup.sync.SyncPackage;
import org.eclipse.oomph.setup.sync.SyncSnapshot;
import org.eclipse.oomph.setup.sync.SyncState;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.sync.SyncPackage
 * @generated
 */
public class SyncAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static SyncPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SyncAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = SyncPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SyncSwitch<Adapter> modelSwitch = new SyncSwitch<Adapter>()
  {
    @Override
    public Adapter caseSyncState(SyncState object)
    {
      return createSyncStateAdapter();
    }

    @Override
    public Adapter caseSyncSnapshot(SyncSnapshot object)
    {
      return createSyncSnapshotAdapter();
    }

    @Override
    public Adapter caseSyncItem(SyncItem object)
    {
      return createSyncItemAdapter();
    }

    @Override
    public Adapter caseRemoteSyncItem(RemoteSyncItem object)
    {
      return createRemoteSyncItemAdapter();
    }

    @Override
    public Adapter caseSyncAction(SyncAction object)
    {
      return createSyncActionAdapter();
    }

    @Override
    public Adapter caseSyncDelta(SyncDelta object)
    {
      return createSyncDeltaAdapter();
    }

    @Override
    public Adapter defaultCase(EObject object)
    {
      return createEObjectAdapter();
    }
  };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.sync.SyncState <em>State</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.sync.SyncState
   * @generated
   */
  public Adapter createSyncStateAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.sync.SyncSnapshot <em>Snapshot</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.sync.SyncSnapshot
   * @generated
   */
  public Adapter createSyncSnapshotAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.sync.SyncItem <em>Item</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.sync.SyncItem
   * @generated
   */
  public Adapter createSyncItemAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.sync.RemoteSyncItem <em>Remote Sync Item</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.sync.RemoteSyncItem
   * @generated
   */
  public Adapter createRemoteSyncItemAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.sync.SyncAction <em>Action</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.sync.SyncAction
   * @generated
   */
  public Adapter createSyncActionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.sync.SyncDelta <em>Delta</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.sync.SyncDelta
   * @generated
   */
  public Adapter createSyncDeltaAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} // SyncAdapterFactory
