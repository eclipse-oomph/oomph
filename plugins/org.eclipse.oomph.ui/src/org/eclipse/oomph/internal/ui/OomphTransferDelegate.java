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
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseResourceImpl;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectInputStream;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectOutputStream;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicInternalEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xml.namespace.XMLNamespacePackage;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.dnd.URLTransfer;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Ed Merks
 */
public abstract class OomphTransferDelegate
{
  private static final List<OomphTransferDelegate> MODIFIABLE_DELEGATES = new ArrayList<OomphTransferDelegate>();

  public static final List<? extends OomphTransferDelegate> DELEGATES = Collections.unmodifiableList(MODIFIABLE_DELEGATES);

  public static final List<? extends Transfer> TRANSFERS = asTransfers(MODIFIABLE_DELEGATES);

  public static Transfer[] transfers()
  {
    return TRANSFERS.toArray(new Transfer[TRANSFERS.size()]);
  }

  public static List<? extends Transfer> asTransfers(final List<? extends OomphTransferDelegate> delegates)
  {
    return new AbstractSequentialList<Transfer>()
    {
      @Override
      public ListIterator<Transfer> listIterator(final int index)
      {
        return new ListIterator<Transfer>()
        {
          ListIterator<? extends OomphTransferDelegate> delegate = delegates.listIterator(index);

          public boolean hasNext()
          {
            return delegate.hasNext();
          }

          public Transfer next()
          {
            return delegate.next().getTransfer();
          }

          public boolean hasPrevious()
          {
            return delegate.hasPrevious();
          }

          public Transfer previous()
          {
            return delegate.previous().getTransfer();
          }

          public int nextIndex()
          {
            return delegate.nextIndex();
          }

          public int previousIndex()
          {
            return delegate.previousIndex();
          }

          public void remove()
          {
            throw new UnsupportedOperationException();
          }

          public void set(Transfer o)
          {
            throw new UnsupportedOperationException();
          }

          public void add(Transfer o)
          {
            throw new UnsupportedOperationException();
          }
        };
      }

      @Override
      public int size()
      {
        return delegates.size();
      }
    };
  }

  private static void register(OomphTransferDelegate delegate, List<OomphTransferDelegate> delegates)
  {
    int priority = delegate.priority();
    for (ListIterator<OomphTransferDelegate> it = delegates.listIterator(); it.hasNext();)
    {
      if (it.next().priority() > priority)
      {
        it.add(delegate);
        return;
      }
    }

    delegates.add(delegate);
  }

  public static void register(OomphTransferDelegate delegate)
  {
    register(delegate, MODIFIABLE_DELEGATES);
  }

  public static OomphTransferDelegate getDelegate(Transfer transfer, Collection<? extends OomphTransferDelegate> delegates)
  {
    for (OomphTransferDelegate delegate : delegates)
    {
      if (delegate.getTransfer() == transfer)
      {
        return delegate;
      }
    }

    return null;
  }

  public static List<? extends OomphTransferDelegate> merge(Collection<? extends OomphTransferDelegate> delegates,
      Collection<? extends OomphTransferDelegate> overridingDelegates)
  {
    return merge(delegates, overridingDelegates.toArray(new OomphTransferDelegate[overridingDelegates.size()]));
  }

  public static List<? extends OomphTransferDelegate> merge(Collection<? extends OomphTransferDelegate> delegates, OomphTransferDelegate... overridingDelegates)
  {
    List<OomphTransferDelegate> result = new ArrayList<OomphTransferDelegate>();
    LOOP: for (OomphTransferDelegate delegate : delegates)
    {
      Transfer transfer = delegate.getTransfer();
      for (OomphTransferDelegate overridingDelegate : overridingDelegates)
      {
        if (overridingDelegate.getTransfer() == transfer)
        {
          register(overridingDelegate, result);
          continue LOOP;
        }
      }

      register(delegate, result);
    }

    for (OomphTransferDelegate overridingDelegate : overridingDelegates)
    {
      if (!result.contains(overridingDelegate))
      {
        register(overridingDelegate, result);
      }
    }

    return result;
  }

  public abstract Transfer getTransfer();

  public boolean isSupportedType(TransferData transferData)
  {
    return getTransfer().isSupportedType(transferData);
  }

  public TransferData[] getSupportedTypes()
  {
    return getTransfer().getSupportedTypes();
  }

  public abstract Collection<?> getData(EditingDomain domain, TransferData transferData);

  public abstract Collection<?> getValue(EditingDomain domain, Object data);

  public abstract boolean setSelection(EditingDomain domain, ISelection selection);

  public abstract Object getData();

  public abstract void clear();

  protected int priority()
  {
    return 0;
  }

  @Override
  public String toString()
  {
    return super.toString() + " " + Arrays.asList(getTransfer().getSupportedTypes());
  }

  public abstract static class SelectionTransferDelegate extends OomphTransferDelegate
  {
    protected Collection<?> extractSelectedObjects(Object... objects)
    {
      List<Object> result = new ArrayList<Object>();
      for (Object object : objects)
      {
        Object filteredObject = filter(object);
        if (filteredObject != null)
        {
          result.add(filteredObject);
        }
      }

      return result;
    }

    protected Object filter(Object object)
    {
      if (object instanceof Resource)
      {
        Resource resource = (Resource)object;
        return resource.getURI();
      }
      else if (EMFPlugin.IS_RESOURCES_BUNDLE_AVAILABLE)
      {
        URI uri = EclipseHelper.getURI(object);
        if (uri != null)
        {
          return uri;
        }
      }

      return object;
    }
  }

  public abstract static class ResourceSetTransferDelegate extends OomphTransferDelegate
  {
    protected static final Resource.Factory RESOURCE_FACTORY = new BaseResourceFactoryImpl();

    protected ResourceSet createResourceSet(EditingDomain domain)
    {
      final ResourceSet resourceSet = domain.getResourceSet();
      ResourceSet result = new ResourceSetImpl()
      {
        @Override
        protected Resource delegatedGetResource(URI uri, boolean loadOnDemand)
        {
          Resource result = super.delegatedGetResource(uri, loadOnDemand);
          if (result == null)
          {
            resourceSet.getResource(uri, false);
          }
          return result;
        }
      };

      result.setURIConverter(resourceSet.getURIConverter());
      result.setPackageRegistry(new EPackageRegistryImpl(resourceSet.getPackageRegistry()));
      result.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", RESOURCE_FACTORY);

      return result;
    }
  }

  public static class LocalTransferDelegate extends SelectionTransferDelegate
  {
    protected final LocalTransfer localTransfer = LocalTransfer.getInstance();

    protected ISelection selection;

    @Override
    public Transfer getTransfer()
    {
      return localTransfer;
    }

    @Override
    public Collection<?> getData(EditingDomain domain, TransferData transferData)
    {
      return getValue(domain, localTransfer.nativeToJava(transferData));
    }

    @Override
    public Collection<?> getValue(EditingDomain domain, Object data)
    {
      if (data instanceof IStructuredSelection)
      {
        IStructuredSelection selection = (IStructuredSelection)data;
        return extractSelectedObjects(selection.toArray());
      }

      return Collections.emptyList();
    }

    @Override
    public boolean setSelection(EditingDomain domain, ISelection selection)
    {
      if (!selection.isEmpty())
      {
        this.selection = selection;
        return true;
      }

      return false;
    }

    @Override
    public Object getData()
    {
      return selection;
    }

    @Override
    public void clear()
    {
      selection = null;
    }
  }

  static
  {
    register(new LocalTransferDelegate());
  }

  public static class LocalSelectionTransferDelegate extends SelectionTransferDelegate
  {
    protected final LocalSelectionTransfer localSelectionTransfer = LocalSelectionTransfer.getTransfer();

    @Override
    public Transfer getTransfer()
    {
      return localSelectionTransfer;
    }

    @Override
    public Collection<?> getData(EditingDomain domain, TransferData transferData)
    {
      return getValue(domain, localSelectionTransfer.nativeToJava(transferData));
    }

    @Override
    public Collection<?> getValue(EditingDomain domain, Object data)
    {
      if (data instanceof IStructuredSelection)
      {
        IStructuredSelection selection = (IStructuredSelection)data;
        return extractSelectedObjects(selection.toArray());
      }

      return Collections.emptyList();
    }

    @Override
    public boolean setSelection(EditingDomain domain, ISelection selection)
    {
      if (!selection.isEmpty())
      {
        localSelectionTransfer.setSelection(selection);
        localSelectionTransfer.setSelectionSetTime(System.currentTimeMillis());
        return true;
      }

      return false;
    }

    @Override
    public Object getData()
    {
      return localSelectionTransfer.getSelection();
    }

    @Override
    public void clear()
    {
      localSelectionTransfer.setSelection(null);
    }
  }

  static
  {
    register(new LocalSelectionTransferDelegate());
  }

  public static class OomphBinaryTransferDelegate extends ResourceSetTransferDelegate
  {
    protected final OomphBinaryTransfer oomphBinaryTransfer = OomphBinaryTransfer.getInstance();

    protected Set<EObject> eObjects;

    protected byte[] data;

    @Override
    public Transfer getTransfer()
    {
      return oomphBinaryTransfer;
    }

    @Override
    public Collection<?> getData(EditingDomain domain, TransferData transferData)
    {
      return getValue(domain, oomphBinaryTransfer.nativeToJava(transferData));
    }

    @Override
    public Collection<?> getValue(EditingDomain domain, Object data)
    {
      if (data instanceof byte[])
      {
        byte[] bytes = (byte[])data;
        try
        {
          final ResourceSet transferResourceSet = createResourceSet(domain);

          // Create a stream to load the binary representation.
          ByteArrayInputStream in = new ByteArrayInputStream(bytes);
          EObjectInputStream eObjectInputStream = new BinaryResourceImpl.EObjectInputStream(in, null)
          {
            {
              resourceSet = transferResourceSet;
            }
          };

          // Read all the packages first.
          InternalEList<InternalEObject> ePackages = new BasicInternalEList<InternalEObject>(EPackage.class);
          eObjectInputStream.loadEObjects(ePackages);

          // Process the packages, putting each one in a resource in a resource set.
          Registry packageRegistry = transferResourceSet.getPackageRegistry();
          for (EPackage ePackage : ePackages.toArray(new EPackage[ePackages.size()]))
          {
            Resource resource = transferResourceSet.createResource(URI.createURI("*.ecore"));
            resource.getContents().add(ePackage);
            resource.setURI(ePackage.eResource().getURI());

            // Check if there is a registered package available already.
            String nsURI = ePackage.getNsURI();
            EPackage registeredEPackage = packageRegistry.getEPackage(nsURI);
            if (registeredEPackage == null)
            {
              // If not, register this dynamic package.
              packageRegistry.put(nsURI, ePackage);
            }
            else
            {
              // If so, convert all the objects to proxies and so dynamic packages will extend the generated packages.
              Map<EObject, String> fragments = new LinkedHashMap<EObject, String>();
              for (Iterator<EObject> it = resource.getAllContents(); it.hasNext();)
              {
                EObject eObject = it.next();
                fragments.put(eObject, resource.getURIFragment(eObject));
              }

              URI uri = URI.createURI(nsURI);
              for (Map.Entry<EObject, String> entry : fragments.entrySet())
              {
                ((InternalEObject)entry.getKey()).eSetProxyURI(uri.appendFragment(entry.getValue()));
              }
            }
          }

          // Read in all the objects, which will use the appropriate registered packages.
          InternalEList<InternalEObject> eObjects = new BasicInternalEList<InternalEObject>(EObject.class);
          eObjectInputStream.loadEObjects(eObjects);
          Resource resource = transferResourceSet.createResource(URI.createURI("*.xmi"));
          resource.getContents().addAll(eObjects);

          return eObjects;
        }
        catch (IOException ex)
        {
          SWTException swtException = new SWTException(DND.ERROR_INVALID_DATA, ex.getMessage());
          swtException.initCause(ex);
          throw swtException;
        }
      }

      return Collections.emptyList();
    }

    @Override
    public boolean setSelection(EditingDomain domain, ISelection selection)
    {
      if (!selection.isEmpty() && selection instanceof IStructuredSelection)
      {
        eObjects = new LinkedHashSet<EObject>();
        for (Object object : ((IStructuredSelection)selection).toArray())
        {
          gather(domain, object);
        }

      }

      return !eObjects.isEmpty();
    }

    protected void gather(EditingDomain domain, Object object)
    {
      Object unwrappedObject = AdapterFactoryEditingDomain.unwrap(object);
      if (unwrappedObject instanceof EObject)
      {
        EObject eObject = (EObject)unwrappedObject;
        eObjects.add(eObject);
      }
      else if (unwrappedObject instanceof Resource)
      {
        Resource resource = (Resource)unwrappedObject;
        eObjects.addAll(resource.getContents());
      }
    }

    @Override
    public Object getData()
    {
      if (!eObjects.isEmpty())
      {
        // Determine the packages of all the instances that will be copied.
        EList<EPackage> ePackages = new UniqueEList<EPackage>();
        for (Iterator<EObject> it = EcoreUtil.getAllContents(eObjects); it.hasNext();)
        {
          EObject eObject = it.next();
          ePackages.add(eObject.eClass().getEPackage());
        }

        // Determine the closure of all packages.
        for (int i = 0; i < ePackages.size(); ++i)
        {
          EPackage ePackage = ePackages.get(i);
          for (Iterator<EObject> it = ePackage.eAllContents(); it.hasNext();)
          {
            EObject eObject = it.next();
            for (EObject eCrossReference : eObject.eCrossReferences())
            {
              EObject rootContainer = EcoreUtil.getRootContainer(eCrossReference);
              if (rootContainer instanceof EPackage)
              {
                ePackages.add((EPackage)rootContainer);
              }
            }
          }
        }

        // Ignore the ubiquitously available packages.
        ePackages.remove(EcorePackage.eINSTANCE);
        ePackages.remove(XMLTypePackage.eINSTANCE);
        ePackages.remove(XMLNamespacePackage.eINSTANCE);

        // Create a specialized copier for copying the packages and the EObjects.
        class Copier extends ProxifyingCopier
        {
          private static final long serialVersionUID = 1L;

          private Map<EObject, EObject> packageCopies;

          public Copier()
          {
            super(true, true);
          }

          @Override
          protected EClass getTarget(EClass eClass)
          {
            // Map to the copied package metadata.
            if (packageCopies != null)
            {
              EClass result = (EClass)packageCopies.get(eClass);
              if (result != null)
              {
                return result;
              }
            }

            return eClass;
          }

          @Override
          protected EStructuralFeature getTarget(EStructuralFeature eStructuralFeature)
          {
            // Map to the copied package metadata.
            if (packageCopies != null)
            {
              EStructuralFeature result = (EStructuralFeature)packageCopies.get(eStructuralFeature);
              if (result != null)
              {
                return result;
              }
            }

            return eStructuralFeature;
          }

          @Override
          protected void copyAttribute(EAttribute eAttribute, EObject eObject, EObject copyEObject)
          {
            if (eAttribute.getEAttributeType().isSerializable())
            {
              super.copyAttribute(eAttribute, eObject, copyEObject);
            }
          }

          @Override
          protected void copyAttributeValue(EAttribute eAttribute, EObject eObject, Object value, Setting setting)
          {
            if (value != null)
            {
              // Do data conversion to a string representation.
              EDataType eAttributeType = eAttribute.getEAttributeType();
              EDataType eType = (EDataType)setting.getEStructuralFeature().getEType();
              Class<?> instanceClass = eType.getInstanceClass();
              Class<?> instanceClass2 = eAttributeType.getInstanceClass();
              if (instanceClass != instanceClass2 || instanceClass == null)
              {
                if (eAttribute.isMany())
                {
                  List<Object> values = new ArrayList<Object>();
                  for (Object element : (Collection<?>)value)
                  {
                    values.add(EcoreUtil.createFromString(eType, EcoreUtil.convertToString(eAttributeType, element)));
                  }

                  value = values;
                }
                else
                {
                  value = EcoreUtil.createFromString(eType, EcoreUtil.convertToString(eAttributeType, value));
                }
              }
            }

            super.copyAttributeValue(eAttribute, eObject, value, setting);
          }

          public Collection<EPackage> copyEPackages(Collection<? extends EPackage> ePackages)
          {
            // Copy all the packages.
            Collection<EPackage> result = new BasicInternalEList<EPackage>(EPackage.class, copyAll(ePackages));
            copyReferences();

            // Remember the state of the map at this time.
            packageCopies = new HashMap<EObject, EObject>(this);

            return result;
          }

          public Collection<EObject> copyEObjects(Collection<? extends EObject> eObjects)
          {
            // Don't use original references for the copied objects.
            useOriginalReferences = false;

            // Copy all the objects.
            Collection<EObject> result = new BasicInternalEList<EObject>(InternalEObject.class, copyAll(eObjects));

            // Remove the already-processed references from the map, before processing the references for the copied objects.
            keySet().removeAll(packageCopies.keySet());
            copyReferences();

            return result;
          }
        }

        Copier copier = new Copier();

        // Copy all the packages.
        Collection<EPackage> ePackageCopies = copier.copyEPackages(ePackages);

        // Transform all the data types to be java.lang.String.
        for (EPackage ePackage : ePackageCopies)
        {
          for (EClassifier eClassifier : ePackage.getEClassifiers())
          {
            String instanceClassName = eClassifier.getInstanceClassName();
            if (instanceClassName != null)
            {
              if (eClassifier instanceof EDataType)
              {
                eClassifier.setInstanceClass(String.class);
              }
              else
              {
                eClassifier.setInstanceClassName(null);
                eClassifier.setInstanceClass(null);
              }
            }
          }
        }

        // Copy the objects to be instances of the dynamic packages.
        Collection<EObject> eObjectCopies = copier.copyEObjects(eObjects);

        try
        {
          // Create a stream for producing a binary representation.
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          EObjectOutputStream eObjectOutputStream = new BinaryResourceImpl.EObjectOutputStream(out, null, BinaryResourceImpl.BinaryIO.Version.VERSION_1_1);

          // Save all the packages.
          @SuppressWarnings("unchecked")
          InternalEList<? extends InternalEObject> internalEPackages = (InternalEList<? extends InternalEObject>)(InternalEList<?>)ePackageCopies;
          eObjectOutputStream.saveEObjects(internalEPackages, BinaryResourceImpl.EObjectOutputStream.Check.CONTAINER);

          // Save all the objects.
          @SuppressWarnings("unchecked")
          InternalEList<? extends InternalEObject> internalEObjects = (InternalEList<? extends InternalEObject>)(InternalEList<?>)eObjectCopies;
          eObjectOutputStream.saveEObjects(internalEObjects, BinaryResourceImpl.EObjectOutputStream.Check.CONTAINER);

          eObjectOutputStream.flush();

          // Convert the bytes to a native representation.
          return out.toByteArray();
        }
        catch (IOException ex)
        {
          SWTException swtException = new SWTException(DND.ERROR_INVALID_DATA, ex.getMessage());
          swtException.initCause(ex);
          throw swtException;
        }
      }

      return null;
    }

    @Override
    public void clear()
    {
      eObjects.clear();
    }
  }

  static
  {
    register(new OomphBinaryTransferDelegate());
  }

  public static class FileTransferDelegate extends OomphTransferDelegate
  {
    protected final FileTransfer fileTransfer = FileTransfer.getInstance();

    protected Set<String> files;

    @Override
    public Transfer getTransfer()
    {
      return fileTransfer;
    }

    @Override
    public Collection<?> getData(EditingDomain domain, TransferData transferData)
    {
      return getValue(domain, fileTransfer.nativeToJava(transferData));
    }

    @Override
    public Collection<?> getValue(EditingDomain domain, Object data)
    {
      if (data instanceof String[])
      {
        List<URI> result = new ArrayList<URI>();
        for (String file : (String[])data)
        {
          result.add(URI.createFileURI(file));
        }
        return result;
      }

      return Collections.emptyList();
    }

    @Override
    public boolean setSelection(EditingDomain domain, ISelection selection)
    {
      files = new LinkedHashSet<String>();
      if (!selection.isEmpty() && selection instanceof IStructuredSelection)
      {
        for (Object object : ((IStructuredSelection)selection).toArray())
        {
          gather(domain, object);
        }
      }

      return !files.isEmpty();
    }

    protected void gather(EditingDomain domain, Object object)
    {
      Object unwrappedObject = AdapterFactoryEditingDomain.unwrap(object);
      if (unwrappedObject instanceof Resource)
      {
        Resource resource = (Resource)unwrappedObject;
        gather(domain, resource.getURI());
      }
    }

    protected void gather(EditingDomain domain, URI uri)
    {
      URI normalizedURI = domain.getResourceSet().getURIConverter().normalize(uri);
      URI resolvedURI = CommonPlugin.resolve(normalizedURI);
      if (resolvedURI.isFile())
      {
        files.add(new File(resolvedURI.toFileString()).getAbsolutePath());
      }
    }

    @Override
    public Object getData()
    {
      return files.isEmpty() ? null : files.toArray(new String[files.size()]);
    }

    @Override
    public void clear()
    {
      files = null;
    }
  }

  static
  {
    register(new FileTransferDelegate());
  }

  public static class URLTransferDelegate extends OomphTransferDelegate
  {
    protected final URLTransfer urlTransfer = URLTransfer.getInstance();

    protected String url;

    @Override
    public Transfer getTransfer()
    {
      return urlTransfer;
    }

    @Override
    public Collection<?> getData(EditingDomain domain, TransferData transferData)
    {
      return getValue(domain, urlTransfer.nativeToJava(transferData));
    }

    @Override
    public Collection<?> getValue(EditingDomain domain, Object data)
    {
      if (data instanceof String)
      {
        String value = data.toString().trim();
        try
        {
          return Collections.singleton(URI.createURI(value));
        }
        catch (RuntimeException ex)
        {
          // Ignore.
        }
      }

      return Collections.emptyList();
    }

    @Override
    public boolean setSelection(EditingDomain domain, ISelection selection)
    {
      if (!selection.isEmpty() && selection instanceof IStructuredSelection)
      {
        for (Object object : ((IStructuredSelection)selection).toArray())
        {
          gather(domain, object);

          if (url != null)
          {
            return true;
          }
        }
      }

      return false;
    }

    protected void gather(EditingDomain domain, Object object)
    {
      Object unwrappedObject = AdapterFactoryEditingDomain.unwrap(object);
      if (unwrappedObject instanceof Resource)
      {
        Resource resource = (Resource)unwrappedObject;
        gather(domain, resource.getURI());
      }
    }

    protected void gather(EditingDomain domain, URI uri)
    {
      URI normalizedURI = domain.getResourceSet().getURIConverter().normalize(uri);
      URI resolvedURI = CommonPlugin.resolve(normalizedURI);
      url = resolvedURI.toString();
    }

    @Override
    public Object getData()
    {
      return url;
    }

    @Override
    public void clear()
    {
      url = null;
    }
  }

  static
  {
    register(new URLTransferDelegate());
  }

  public static class TextTransferDelegate extends ResourceSetTransferDelegate
  {
    protected final TextTransfer textTransfer = TextTransfer.getInstance();

    protected Set<EObject> eObjects;

    protected List<String> text;

    @Override
    public Transfer getTransfer()
    {
      return textTransfer;
    }

    @Override
    public Collection<?> getData(EditingDomain domain, TransferData transferData)
    {
      return getValue(domain, textTransfer.nativeToJava(transferData));
    }

    @Override
    public Collection<?> getValue(EditingDomain domain, Object data)
    {
      if (data instanceof String)
      {
        String value = data.toString().trim();
        Collection<? extends EObject> result = fromString(domain, value);
        if (result != null)
        {
          return result;
        }

        String[] uriStrings = value.split("\r?\n");
        Set<URI> uris = new LinkedHashSet<URI>();
        for (String uriString : uriStrings)
        {
          try
          {
            String trimmedURIString = uriString.trim().replace(" ", "%20");
            URI uri = URI.createURI(trimmedURIString);
            if (uri.scheme() != null && uri.scheme().length() > 1)
            {
              // Make sure it's a valid according to Java's URI implementation as well.
              new java.net.URI(trimmedURIString);
              uris.add(uri);
            }
          }
          catch (Throwable throwable)
          {
            // Ignore.
          }
        }

        return uris;
      }

      return Collections.emptyList();
    }

    /**
     * Looks for a leading XML header tag and the trailing element end tag in a larger string value
     * and calls {@link #fromXML(String) fromXML} for that substring.
     * @param domain
     */
    protected Collection<? extends EObject> fromString(EditingDomain domain, String value)
    {
      if (value != null)
      {
        int start = value.indexOf("<?xml");
        if (start != -1)
        {
          int end = value.lastIndexOf('>');
          if (end != -1)
          {
            Collection<? extends EObject> eObjects = fromXML(domain, value.substring(start, end + 1));
            if (eObjects != null)
            {
              return eObjects;
            }
          }
        }
      }

      return null;
    }

    protected Collection<? extends EObject> fromXML(EditingDomain domain, String xml)
    {
      ResourceSet resourceSet = createResourceSet(domain);

      try
      {
        String name = XMLTypeFactory.eINSTANCE.convertHexBinary(IOUtil.getSHA1(xml));
        XMLResource resource = (XMLResource)resourceSet.createResource(URI.createURI("dummy:/" + name + ".xmi"));
        resource.load(new InputSource(new StringReader(xml)), null);
        return resource.getContents();
      }
      catch (IOException ex)
      {
        // Ignore.
      }
      catch (NoSuchAlgorithmException ex)
      {
        // Ignore.
      }

      return null;
    }

    @Override
    public boolean setSelection(EditingDomain domain, ISelection selection)
    {
      eObjects = new LinkedHashSet<EObject>();
      text = new ArrayList<String>();
      if (!selection.isEmpty() && selection instanceof IStructuredSelection)
      {
        for (Object object : ((IStructuredSelection)selection).toArray())
        {
          gather(domain, object);
        }
      }

      return !eObjects.isEmpty() || !text.isEmpty();
    }

    protected void gather(EditingDomain domain, Object object)
    {
      Object unwrappedObject = AdapterFactoryEditingDomain.unwrap(object);
      if (unwrappedObject instanceof EObject)
      {
        EObject eObject = (EObject)unwrappedObject;
        eObjects.add(eObject);
      }
      else if (unwrappedObject instanceof Resource)
      {
        Resource resource = (Resource)unwrappedObject;
        eObjects.addAll(resource.getContents());
      }
      else if (unwrappedObject instanceof String)
      {
        text.add((String)unwrappedObject);
      }
    }

    @Override
    public Object getData()
    {
      if (!eObjects.isEmpty())
      {
        XMLResource resource = (XMLResource)RESOURCE_FACTORY.createResource(URI.createURI("dummy:/*.xmi"));

        ProxifyingCopier copier = new ProxifyingCopier(true, false);
        Collection<EObject> eObjectCopies = copier.copyAll(eObjects);
        copier.copyReferences();
        resource.getContents().addAll(eObjectCopies);

        StringWriter writer = new StringWriter();
        try
        {
          resource.save(writer, null);
          writer.flush();
          return writer.toString();
        }
        catch (IOException ex)
        {
          // Ignore.
        }
      }
      else if (!text.isEmpty())
      {
        StringBuilder result = new StringBuilder();
        for (String value : text)
        {
          if (result.length() > 0)
          {
            result.append(System.getProperty("line.separator"));
          }

          result.append(value);
        }

        return result.toString();
      }

      return null;
    }

    @Override
    public void clear()
    {
      eObjects = null;
      text = null;
    }
  }

  static
  {
    register(new TextTransferDelegate());
  }

  private static class EclipseHelper
  {
    public static URI getURI(Object object)
    {
      if (object instanceof IResource)
      {
        IResource resource = (IResource)object;
        return URI.createPlatformResourceURI(resource.getFullPath().toString(), true);
      }

      return null;
    }
  }

  private static class ProxifyingCopier extends EcoreUtil.Copier
  {
    private static final long serialVersionUID = 1L;

    private final BaseResourceImpl.BaseHelperImpl helper = new BaseResourceImpl.BaseHelperImpl(null);

    private final Collection<Resource> excludedResources = new HashSet<Resource>();

    public ProxifyingCopier(boolean resolveProxies, boolean useOriginalReferences)
    {
      super(resolveProxies, useOriginalReferences);
    }

    @Override
    public EObject get(Object key)
    {
      EObject eObject = super.get(key);
      if (eObject == null && !useOriginalReferences)
      {
        InternalEObject originalEObject = (InternalEObject)key;
        if (originalEObject.eIsProxy())
        {
          InternalEObject proxy = (InternalEObject)EcoreUtil.create(getTarget(originalEObject.eClass()));
          proxy.eSetProxyURI(originalEObject.eProxyURI());
          return proxy;
        }

        Resource resource = originalEObject.eResource();
        if (resource != null && !excludedResources.contains(resource))
        {
          URI uri = helper.getHREF(resource, originalEObject);
          if (!uri.isCurrentDocumentReference())
          {
            InternalEObject proxy = (InternalEObject)EcoreUtil.create(getTarget(originalEObject.eClass()));
            proxy.eSetProxyURI(uri);
            return proxy;
          }
        }
      }

      return eObject;
    }

    @Override
    public <T> Collection<T> copyAll(Collection<? extends T> eObjects)
    {
      for (Object object : eObjects)
      {
        excludedResources.add(((EObject)object).eResource());
      }

      return super.copyAll(eObjects);
    }
  }
}
