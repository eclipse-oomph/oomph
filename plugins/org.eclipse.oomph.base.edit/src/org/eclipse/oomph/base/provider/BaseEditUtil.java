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
package org.eclipse.oomph.base.provider;

import org.eclipse.oomph.base.util.EAnnotations;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.ReflectiveItemProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class BaseEditUtil
{
  private static final String TASK_SUFFIX = " Task"; // TODO Can this be made translatable?

  private BaseEditUtil()
  {
  }

  public static IconReflectiveItemProvider replaceReflectiveItemProvider(ComposedAdapterFactory adapterFactory)
  {
    EClass dynamicClass = EcoreFactory.eINSTANCE.createEClass();
    dynamicClass.setName("Dynamic");

    EPackage dynamicPackage = EcoreFactory.eINSTANCE.createEPackage();
    dynamicPackage.setName("dynamic");
    dynamicPackage.setNsPrefix("dynamic");
    dynamicPackage.setNsURI("http://dynamic");
    dynamicPackage.getEClassifiers().add(dynamicClass);

    AdapterFactory factory = adapterFactory.getFactoryForType(EcoreUtil.create(dynamicClass));
    if (factory != null)
    {
      adapterFactory.removeAdapterFactory(factory);
    }

    final IconReflectiveItemProvider[] itemProvider = { null };
    adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory()
    {
      {
        itemProvider[0] = new IconReflectiveItemProvider(this);
        reflectiveItemProviderAdapter = itemProvider[0];
      }
    });

    return itemProvider[0];
  }

  public static ComposedAdapterFactory createAdapterFactory()
  {
    ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
    replaceReflectiveItemProvider(adapterFactory);
    return adapterFactory;
  }

  public static Object getImage(URI uri)
  {
    return RemoteImage.create(uri);
  }

  /**
   *
   * @author Ed Merks
   */
  private static final class RemoteImage extends ComposedImage
  {
    private static final URIConverter URI_CONVERTER;

    private static final Map<?, ?> OPTIONS;

    private static final Constructor<?> IMAGE_DATA_CONSTRUCTOR;

    private static final Method IMAGE_DESCRIPTOR_CREATE_FROM_IMAGE_DATA_METHOD;

    static
    {
      URIConverter uriConverter = null;
      Map<Object, Object> options = null;
      Constructor<?> imageDataConstructor = null;
      Method imageDescriptorCreateFromImageDataMethod = null;

      try
      {
        Class<?> setupCoreUtilClass = CommonPlugin.loadClass("org.eclipse.oomph.setup.core", "org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil");
        Method createResourceSetMethod = setupCoreUtilClass.getMethod("createResourceSet");

        Class<?> imageDataClass = CommonPlugin.loadClass("org.eclipse.swt", "org.eclipse.swt.graphics.ImageData");
        imageDataConstructor = imageDataClass.getConstructor(InputStream.class);

        Class<?> imageDescriptorClass = CommonPlugin.loadClass("org.eclipse.jface", "org.eclipse.jface.resource.ImageDescriptor");
        imageDescriptorCreateFromImageDataMethod = imageDescriptorClass.getMethod("createFromImageData", imageDataClass);

        ResourceSet resourceSet = (ResourceSet)createResourceSetMethod.invoke(null);
        uriConverter = resourceSet.getURIConverter();
        options = resourceSet.getLoadOptions();

        Class<?> ecfURIHandlerImplClass = CommonPlugin.loadClass("org.eclipse.oomph.setup.core",
            "org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl");
        Object ecfCacheHandlingOption = ReflectUtil.getValue(ReflectUtil.getField(ecfURIHandlerImplClass, "OPTION_CACHE_HANDLING"), null);
        Class<?> ecfURIHandlerImplCacheHandlingClass = CommonPlugin.loadClass("org.eclipse.oomph.setup.core",
            "org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl$CacheHandling");
        Object ecfOptionCacheHandlingValue = ReflectUtil.getValue(ReflectUtil.getField(ecfURIHandlerImplCacheHandlingClass, "CACHE_WITHOUT_ETAG_CHECKING"),
            null);
        options.put(ecfCacheHandlingOption, ecfOptionCacheHandlingValue);
      }
      catch (Throwable ex)
      {
        // Ignore.
      }

      URI_CONVERTER = uriConverter;
      OPTIONS = options;
      IMAGE_DATA_CONSTRUCTOR = imageDataConstructor;
      IMAGE_DESCRIPTOR_CREATE_FROM_IMAGE_DATA_METHOD = imageDescriptorCreateFromImageDataMethod;
    }

    public static final Object create(URI uri)
    {
      if (URI_CONVERTER == null)
      {
        return uri;
      }

      return new RemoteImage(uri);
    }

    public RemoteImage(URI uri)
    {
      super(Collections.singletonList(uri));
    }

    @Override
    public boolean equals(Object that)
    {
      return that instanceof RemoteImage && ((RemoteImage)that).images.equals(images);
    }

    @Override
    public List<Object> getImages()
    {
      List<Object> images = super.getImages();
      URI uri = (URI)images.get(0);

      try
      {
        Object imageData = IMAGE_DATA_CONSTRUCTOR.newInstance(URI_CONVERTER.createInputStream(uri, OPTIONS));
        Object imageDescriptor = IMAGE_DESCRIPTOR_CREATE_FROM_IMAGE_DATA_METHOD.invoke(null, imageData);
        return Collections.singletonList(imageDescriptor);
      }
      catch (Throwable ex)
      {
        // Ignore.
      }

      return images;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class IconReflectiveItemProvider extends ReflectiveItemProvider
  {
    public IconReflectiveItemProvider(AdapterFactory adapterFactory)
    {
      super(adapterFactory);
    }

    @Override
    public String getTypeText(Object object)
    {
      String typeText = super.getTypeText(object);
      if (typeText.endsWith(TASK_SUFFIX))
      {
        typeText = typeText.substring(0, typeText.length() - TASK_SUFFIX.length());
      }

      return typeText;
    }

    @Override
    public Object getImage(Object object)
    {
      EObject eObject = (EObject)object;
      EClass eClass = eObject.eClass();

      URI imageURI = EAnnotations.getImageURI(eClass);
      if (imageURI != null)
      {
        return BaseEditUtil.getImage(imageURI);
      }

      return super.getImage(object);
    }
  }
}
