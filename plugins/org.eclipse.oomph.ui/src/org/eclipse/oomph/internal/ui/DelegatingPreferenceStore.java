/*
 * Copyright (c) 2014 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

/**
 * @author Ed Merks
 */
public class DelegatingPreferenceStore implements IPreferenceStore
{
  protected IPreferenceStore delegate;

  public DelegatingPreferenceStore(IPreferenceStore delegate)
  {
    this.delegate = delegate;
  }

  @Override
  public void addPropertyChangeListener(IPropertyChangeListener listener)
  {
    delegate.addPropertyChangeListener(listener);
  }

  @Override
  public boolean contains(String name)
  {
    return delegate.contains(name);
  }

  @Override
  public void firePropertyChangeEvent(String name, Object oldValue, Object newValue)
  {
    delegate.firePropertyChangeEvent(name, oldValue, newValue);
  }

  @Override
  public boolean getBoolean(String name)
  {
    return delegate.getBoolean(name);
  }

  @Override
  public boolean getDefaultBoolean(String name)
  {
    return delegate.getDefaultBoolean(name);
  }

  @Override
  public double getDefaultDouble(String name)
  {
    return delegate.getDefaultDouble(name);
  }

  @Override
  public float getDefaultFloat(String name)
  {
    return delegate.getDefaultFloat(name);
  }

  @Override
  public int getDefaultInt(String name)
  {
    return delegate.getDefaultInt(name);
  }

  @Override
  public long getDefaultLong(String name)
  {
    return delegate.getDefaultLong(name);
  }

  @Override
  public String getDefaultString(String name)
  {
    return delegate.getDefaultString(name);
  }

  @Override
  public double getDouble(String name)
  {
    return delegate.getDouble(name);
  }

  @Override
  public float getFloat(String name)
  {
    return delegate.getFloat(name);
  }

  @Override
  public int getInt(String name)
  {
    return delegate.getInt(name);
  }

  @Override
  public long getLong(String name)
  {
    return delegate.getLong(name);
  }

  @Override
  public String getString(String name)
  {
    return delegate.getString(name);
  }

  @Override
  public boolean isDefault(String name)
  {
    return delegate.isDefault(name);
  }

  @Override
  public boolean needsSaving()
  {
    return delegate.needsSaving();
  }

  @Override
  public void putValue(String name, String value)
  {
    delegate.putValue(name, value);
  }

  @Override
  public void removePropertyChangeListener(IPropertyChangeListener listener)
  {
    delegate.removePropertyChangeListener(listener);
  }

  @Override
  public void setDefault(String name, double value)
  {
    delegate.setDefault(name, value);
  }

  @Override
  public void setDefault(String name, float value)
  {
    delegate.setDefault(name, value);
  }

  @Override
  public void setDefault(String name, int value)
  {
    delegate.setDefault(name, value);
  }

  @Override
  public void setDefault(String name, long value)
  {
    delegate.setDefault(name, value);
  }

  @Override
  public void setDefault(String name, String defaultObject)
  {
    delegate.setDefault(name, defaultObject);
  }

  @Override
  public void setDefault(String name, boolean value)
  {
    delegate.setDefault(name, value);
  }

  @Override
  public void setToDefault(String name)
  {
    delegate.setToDefault(name);
  }

  @Override
  public void setValue(String name, double value)
  {
    delegate.setValue(name, value);
  }

  @Override
  public void setValue(String name, float value)
  {
    delegate.setValue(name, value);
  }

  @Override
  public void setValue(String name, int value)
  {
    delegate.setValue(name, value);
  }

  @Override
  public void setValue(String name, long value)
  {
    delegate.setValue(name, value);
  }

  @Override
  public void setValue(String name, String value)
  {
    delegate.setValue(name, value);
  }

  @Override
  public void setValue(String name, boolean value)
  {
    delegate.setValue(name, value);
  }

}
