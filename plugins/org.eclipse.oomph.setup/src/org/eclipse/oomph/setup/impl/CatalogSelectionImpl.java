/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.DelegatingEcoreEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import java.lang.reflect.Array;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Catalog Selection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl#getProductCatalogs <em>Product Catalogs</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl#getProjectCatalogs <em>Project Catalogs</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl#getDefaultProductVersions <em>Default Product Versions</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl#getDefaultStreams <em>Default Streams</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl#getSelectedStreams <em>Selected Streams</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CatalogSelectionImpl extends ModelElementImpl implements CatalogSelection
{
  /**
   * The cached value of the '{@link #getProductCatalogs() <em>Product Catalogs</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProductCatalogs()
   * @generated
   * @ordered
   */
  protected EList<ProductCatalog> productCatalogs;

  /**
   * The cached value of the '{@link #getProjectCatalogs() <em>Project Catalogs</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProjectCatalogs()
   * @generated
   * @ordered
   */
  protected EList<ProjectCatalog> projectCatalogs;

  /**
   * The cached value of the '{@link #getDefaultProductVersions() <em>Default Product Versions</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultProductVersions()
   * @generated
   * @ordered
   */
  protected EMap<Product, ProductVersion> defaultProductVersions;

  /**
   * The cached value of the '{@link #getDefaultStreams() <em>Default Streams</em>}' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultStreams()
   * @generated
   * @ordered
   */
  protected EMap<Project, Stream> defaultStreams;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CatalogSelectionImpl()
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
    return SetupPackage.Literals.CATALOG_SELECTION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ProductCatalog> getProductCatalogs()
  {
    if (productCatalogs == null)
    {
      productCatalogs = new EObjectResolvingEList<>(ProductCatalog.class, this, SetupPackage.CATALOG_SELECTION__PRODUCT_CATALOGS);
    }
    return productCatalogs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ProjectCatalog> getProjectCatalogs()
  {
    if (projectCatalogs == null)
    {
      projectCatalogs = new EObjectResolvingEList<>(ProjectCatalog.class, this, SetupPackage.CATALOG_SELECTION__PROJECT_CATALOGS);
    }
    return projectCatalogs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EMap<Product, ProductVersion> getDefaultProductVersions()
  {
    if (defaultProductVersions == null)
    {
      defaultProductVersions = new EcoreEMap<>(SetupPackage.Literals.PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY,
          ProductToProductVersionMapEntryImpl.class, this, SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS);
    }
    return defaultProductVersions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EMap<Project, Stream> getDefaultStreams()
  {
    if (defaultStreams == null)
    {
      defaultStreams = new EcoreEMap<>(SetupPackage.Literals.PROJECT_TO_STREAM_MAP_ENTRY, ProjectToStreamMapEntryImpl.class, this,
          SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS);
    }
    return defaultStreams;
  }

  protected EList<Stream> selectedStreams;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public EList<Stream> getSelectedStreams()
  {
    if (selectedStreams == null)
    {
      getDefaultStreams();

      selectedStreams = new DelegatingEcoreEList<Stream>(this)
      {
        private static final long serialVersionUID = 1L;

        @Override
        protected List<Stream> delegateList()
        {
          return null;
        }

        @Override
        protected List<Stream> delegateBasicList()
        {
          return new AbstractSequentialList<>()
          {
            @Override
            public ListIterator<Stream> listIterator(int index)
            {
              return basicListIterator(index);
            }

            @Override
            public int size()
            {
              return delegateSize();
            }
          };
        }

        @Override
        protected Iterator<Stream> delegateIterator()
        {
          return iterator();
        }

        @Override
        protected ListIterator<Stream> delegateListIterator()
        {
          return listIterator();
        }

        protected ProjectToStreamMapEntryImpl wrap(Stream stream)
        {
          ProjectToStreamMapEntryImpl entry = (ProjectToStreamMapEntryImpl)SetupFactory.eINSTANCE.create(SetupPackage.Literals.PROJECT_TO_STREAM_MAP_ENTRY);
          entry.setKey(stream.getProject());
          entry.setValue(stream);
          entry.setSelection(true);
          return entry;
        }

        @Override
        protected void delegateAdd(int index, Stream stream)
        {
          ProjectToStreamMapEntryImpl entry = getEntry(stream, true);
          entry.setSelection(true);
        }

        private ProjectToStreamMapEntryImpl getEntry(Stream stream, boolean demandCreate)
        {
          int index = defaultStreams.indexOfKey(stream.getProject());
          if (index != -1)
          {
            ProjectToStreamMapEntryImpl entry = (ProjectToStreamMapEntryImpl)defaultStreams.get(index);
            return entry;
          }

          if (demandCreate)
          {
            ProjectToStreamMapEntryImpl entry = wrap(stream);
            defaultStreams.add(entry);
            return entry;
          }

          return null;
        }

        @Override
        protected void delegateClear()
        {
          for (Stream stream : new ArrayList<>(this))
          {
            ProjectToStreamMapEntryImpl entry = getEntry(stream, true);
            entry.setSelection(false);
          }
        }

        @Override
        protected void delegateAdd(Stream stream)
        {
          ProjectToStreamMapEntryImpl entry = getEntry(stream, true);
          entry.setSelection(true);
        }

        @Override
        protected boolean delegateContains(Object object)
        {
          for (Stream stream : this)
          {
            if (object == stream)
            {
              return true;
            }
          }

          return false;
        }

        @Override
        protected boolean delegateContainsAll(Collection<?> collection)
        {
          for (Object object : collection)
          {
            if (!delegateContains(object))
            {
              return false;
            }
          }

          return true;
        }

        @Override
        protected boolean delegateEquals(Object object)
        {
          if (object instanceof List<?>)
          {
            List<?> list = (List<?>)object;
            if (list.size() == delegateSize())
            {
              for (Iterator<?> i = list.iterator(), j = iterator(); i.hasNext();)
              {
                if (i.next() != j.next())
                {
                  return false;
                }
              }

              return true;
            }
          }

          return false;
        }

        @Override
        protected Stream delegateGet(int index)
        {
          int count = 0;
          for (Map.Entry<Project, Stream> entry : defaultStreams)
          {
            ProjectToStreamMapEntryImpl projectToStreamMapEntry = (ProjectToStreamMapEntryImpl)entry;
            if (projectToStreamMapEntry.isSelection())
            {
              if (count++ == index)
              {
                return projectToStreamMapEntry.getValue();
              }
            }
          }

          throw new IndexOutOfBoundsException("size=" + size() + " index=" + index); //$NON-NLS-1$ //$NON-NLS-2$
        }

        @Override
        protected int delegateHashCode()
        {
          int hashCode = 1;
          for (Stream stream : this)
          {
            hashCode = 31 * hashCode + (stream == null ? 0 : stream.hashCode());
          }

          return hashCode;
        }

        @Override
        protected int delegateIndexOf(Object object)
        {
          int index = 0;
          for (Stream stream : this)
          {
            if (object == stream)
            {
              return index;
            }

            ++index;
          }

          return -1;
        }

        @Override
        protected boolean delegateIsEmpty()
        {
          return !iterator().hasNext();
        }

        @Override
        protected int delegateLastIndexOf(Object object)
        {
          return delegateIndexOf(object);
        }

        @Override
        protected Stream delegateRemove(int index)
        {
          Stream stream = get(index);
          ProjectToStreamMapEntryImpl entry = getEntry(stream, false);
          entry.setSelection(false);
          return stream;
        }

        @Override
        protected Stream delegateSet(int index, Stream stream)
        {
          throw new UnsupportedOperationException();
        }

        @Override
        protected int delegateSize()
        {
          int size = 0;
          for (Map.Entry<Project, Stream> entry : defaultStreams)
          {
            ProjectToStreamMapEntryImpl projectToStreamMapEntry = (ProjectToStreamMapEntryImpl)entry;
            if (projectToStreamMapEntry.isSelection())
            {
              ++size;
            }
          }

          return size;
        }

        @Override
        protected Object[] delegateToArray()
        {
          int size = delegateSize();
          Object[] result = new Object[size];

          int index = 0;
          for (Stream stream : this)
          {
            result[index++] = stream;
          }

          return result;
        }

        @Override
        protected <T> T[] delegateToArray(T[] array)
        {
          int size = delegateSize();
          if (array.length < size)
          {
            @SuppressWarnings("unchecked")
            T[] newArray = (T[])Array.newInstance(array.getClass().getComponentType(), size);
            array = newArray;
          }

          if (array.length > size)
          {
            array[size] = null;
          }

          int index = 0;
          for (Stream stream : this)
          {
            @SuppressWarnings("unchecked")
            T rawType = (T)stream;
            array[index++] = rawType;
          }

          return array;
        }

        @Override
        protected String delegateToString()
        {
          StringBuffer stringBuffer = new StringBuffer();
          stringBuffer.append("["); //$NON-NLS-1$
          boolean first = true;
          for (Stream stream : this)
          {
            if (!first)
            {
              stringBuffer.append(", "); //$NON-NLS-1$
            }
            else
            {
              first = false;
            }

            stringBuffer.append(String.valueOf(stream));
          }

          stringBuffer.append("]"); //$NON-NLS-1$
          return stringBuffer.toString();
        }

        @Override
        protected boolean isInstance(Object object)
        {
          return object instanceof Stream;
        }

        @Override
        public int getFeatureID()
        {
          return EcorePackage.ECLASS__ESUPER_TYPES;
        }

        @Override
        protected boolean useEquals()
        {
          return false;
        }

        @Override
        protected boolean canContainNull()
        {
          return false;
        }

        @Override
        protected boolean isUnique()
        {
          return true;
        }

        @Override
        protected boolean hasInverse()
        {
          return false;
        }

        @Override
        protected boolean hasManyInverse()
        {
          return false;
        }

        @Override
        protected boolean hasNavigableInverse()
        {
          return false;
        }

        @Override
        protected boolean isEObject()
        {
          return true;
        }

        @Override
        protected boolean isContainment()
        {
          return false;
        }

        @Override
        protected boolean hasProxies()
        {
          return false;
        }

        @Override
        protected boolean hasInstanceClass()
        {
          return true;
        }

        @Override
        public boolean isSet()
        {
          return !isEmpty();
        }

        @Override
        protected NotificationImpl createNotification(int eventType, Object oldObject, Object newObject, int index, boolean wasSet)
        {
          // The notification for this list is being thrown by the
          // delegating list
          //
          return null;
        }

        @Override
        protected void dispatchNotification(Notification notification)
        {
          // Do nothing
        }
      };
    }

    return selectedStreams;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS:
        return ((InternalEList<?>)getDefaultProductVersions()).basicRemove(otherEnd, msgs);
      case SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS:
        return ((InternalEList<?>)getDefaultStreams()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case SetupPackage.CATALOG_SELECTION__PRODUCT_CATALOGS:
        return getProductCatalogs();
      case SetupPackage.CATALOG_SELECTION__PROJECT_CATALOGS:
        return getProjectCatalogs();
      case SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS:
        if (coreType)
        {
          return getDefaultProductVersions();
        }
        else
        {
          return getDefaultProductVersions().map();
        }
      case SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS:
        if (coreType)
        {
          return getDefaultStreams();
        }
        else
        {
          return getDefaultStreams().map();
        }
      case SetupPackage.CATALOG_SELECTION__SELECTED_STREAMS:
        return getSelectedStreams();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SetupPackage.CATALOG_SELECTION__PRODUCT_CATALOGS:
        getProductCatalogs().clear();
        getProductCatalogs().addAll((Collection<? extends ProductCatalog>)newValue);
        return;
      case SetupPackage.CATALOG_SELECTION__PROJECT_CATALOGS:
        getProjectCatalogs().clear();
        getProjectCatalogs().addAll((Collection<? extends ProjectCatalog>)newValue);
        return;
      case SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS:
        ((EStructuralFeature.Setting)getDefaultProductVersions()).set(newValue);
        return;
      case SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS:
        ((EStructuralFeature.Setting)getDefaultStreams()).set(newValue);
        return;
      case SetupPackage.CATALOG_SELECTION__SELECTED_STREAMS:
        getSelectedStreams().clear();
        getSelectedStreams().addAll((Collection<? extends Stream>)newValue);
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
      case SetupPackage.CATALOG_SELECTION__PRODUCT_CATALOGS:
        getProductCatalogs().clear();
        return;
      case SetupPackage.CATALOG_SELECTION__PROJECT_CATALOGS:
        getProjectCatalogs().clear();
        return;
      case SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS:
        getDefaultProductVersions().clear();
        return;
      case SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS:
        getDefaultStreams().clear();
        return;
      case SetupPackage.CATALOG_SELECTION__SELECTED_STREAMS:
        getSelectedStreams().clear();
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
      case SetupPackage.CATALOG_SELECTION__PRODUCT_CATALOGS:
        return productCatalogs != null && !productCatalogs.isEmpty();
      case SetupPackage.CATALOG_SELECTION__PROJECT_CATALOGS:
        return projectCatalogs != null && !projectCatalogs.isEmpty();
      case SetupPackage.CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS:
        return defaultProductVersions != null && !defaultProductVersions.isEmpty();
      case SetupPackage.CATALOG_SELECTION__DEFAULT_STREAMS:
        return defaultStreams != null && !defaultStreams.isEmpty();
      case SetupPackage.CATALOG_SELECTION__SELECTED_STREAMS:
        return !getSelectedStreams().isEmpty();
    }
    return super.eIsSet(featureID);
  }

} // CatalogSelectionImpl
