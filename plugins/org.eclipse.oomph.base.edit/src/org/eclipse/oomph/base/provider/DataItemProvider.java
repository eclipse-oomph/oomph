/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.base.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ItemProvider;

import java.util.Collection;

/**
 * @author Eike Stepper
 */
public class DataItemProvider extends ItemProvider
{
  private Object data;

  public DataItemProvider()
  {
  }

  public DataItemProvider(AdapterFactory adapterFactory, Collection<?> children)
  {
    super(adapterFactory, children);
  }

  public DataItemProvider(AdapterFactory adapterFactory, String text, Collection<?> children)
  {
    super(adapterFactory, text, children);
  }

  public DataItemProvider(AdapterFactory adapterFactory, String text, Object image, Collection<?> children)
  {
    super(adapterFactory, text, image, children);
  }

  public DataItemProvider(AdapterFactory adapterFactory, String text, Object image, Object parent, Collection<?> children)
  {
    super(adapterFactory, text, image, parent, children);
  }

  public DataItemProvider(AdapterFactory adapterFactory, String text, Object image, Object parent)
  {
    super(adapterFactory, text, image, parent);
  }

  public DataItemProvider(AdapterFactory adapterFactory, String text, Object image)
  {
    super(adapterFactory, text, image);
  }

  public DataItemProvider(AdapterFactory adapterFactory, String text)
  {
    super(adapterFactory, text);
  }

  public DataItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  public DataItemProvider(Collection<?> children)
  {
    super(children);
  }

  public DataItemProvider(String text, Collection<?> children)
  {
    super(text, children);
  }

  public DataItemProvider(String text, Object image, Collection<?> children)
  {
    super(text, image, children);
  }

  public DataItemProvider(String text, Object image, Object parent, Collection<?> children)
  {
    super(text, image, parent, children);
  }

  public DataItemProvider(String text, Object image, Object parent)
  {
    super(text, image, parent);
  }

  public DataItemProvider(String text, Object image)
  {
    super(text, image);
  }

  public DataItemProvider(String text)
  {
    super(text);
  }

  public Object getData()
  {
    return data;
  }

  public void setData(Object data)
  {
    this.data = data;
  }
}
