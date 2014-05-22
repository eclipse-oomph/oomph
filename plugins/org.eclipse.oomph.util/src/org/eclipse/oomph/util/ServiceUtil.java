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
package org.eclipse.oomph.util;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class ServiceUtil
{
  private static Map<Object, ServiceReference<?>> services = new IdentityHashMap<Object, ServiceReference<?>>();

  private ServiceUtil()
  {
  }

  public static <T> T getService(BundleContext bundleContext, Class<T> serviceClass)
  {
    String serviceName = serviceClass.getName();
    ServiceReference<?> serviceRef = bundleContext.getServiceReference(serviceName);
    if (serviceRef == null)
    {
      throw new IllegalStateException("Missing OSGi service " + serviceName);
    }

    @SuppressWarnings("unchecked")
    T service = (T)bundleContext.getService(serviceRef);
    services.put(service, serviceRef);
    return service;
  }

  public static void ungetService(BundleContext bundleContext, Object service)
  {
    if (service != null)
    {
      ServiceReference<?> serviceRef = services.remove(service);
      if (serviceRef != null)
      {
        bundleContext.ungetService(serviceRef);
      }
    }
  }
}
