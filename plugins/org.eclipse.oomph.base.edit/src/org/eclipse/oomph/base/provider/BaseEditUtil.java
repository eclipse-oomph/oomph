/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.base.provider;

import org.eclipse.oomph.base.util.EAnnotationConstants;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;

/**
 * @author Eike Stepper
 */
public final class BaseEditUtil
{
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

  /**
   * @author Eike Stepper
   */
  public static final class IconReflectiveItemProvider extends ReflectiveItemProvider
  {
    private static final String TASK_SUFFIX = " Task"; // TODO Can this be made translatable?

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

      String uri = EcoreUtil.getAnnotation(eClass, EAnnotationConstants.ANNOTATION_LABEL_PROVIDER, EAnnotationConstants.KEY_IMAGE_URI);
      if (!StringUtil.isEmpty(uri))
      {
        URI imageURI = URI.createURI(uri);
        if (imageURI.isRelative())
        {
          URI imageBaseURI = getImageBaseURI(eClass);
          if (imageBaseURI != null)
          {
            return imageURI.resolve(imageBaseURI);
          }
        }

        return imageURI;
      }

      URI imageBaseURI = getImageBaseURI(eClass);
      if (imageBaseURI != null)
      {
        return imageBaseURI.appendSegment(eClass.getName() + ".gif");
      }

      return super.getImage(object);
    }

    private URI getImageBaseURI(EClass eClass)
    {
      EPackage ePackage = eClass.getEPackage();
      String uri = EcoreUtil.getAnnotation(ePackage, EAnnotationConstants.ANNOTATION_LABEL_PROVIDER, EAnnotationConstants.KEY_IMAGE_BASE_URI);
      if (!StringUtil.isEmpty(uri))
      {
        return URI.createURI(uri);
      }

      return null;
    }
  }
}
