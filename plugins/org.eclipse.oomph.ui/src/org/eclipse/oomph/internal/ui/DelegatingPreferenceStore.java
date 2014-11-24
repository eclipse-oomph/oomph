/*
 * Copyright (c) 2014 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

  public void addPropertyChangeListener(IPropertyChangeListener listener)
  {
    delegate.addPropertyChangeListener(listener);
  }

  public boolean contains(String name)
  {
    return delegate.contains(name);
  }

  public void firePropertyChangeEvent(String name, Object oldValue, Object newValue)
  {
    delegate.firePropertyChangeEvent(name, oldValue, newValue);
  }

  public boolean getBoolean(String name)
  {
    return delegate.getBoolean(name);
  }

  public boolean getDefaultBoolean(String name)
  {
    return delegate.getDefaultBoolean(name);
  }

  public double getDefaultDouble(String name)
  {
    return delegate.getDefaultDouble(name);
  }

  public float getDefaultFloat(String name)
  {
    return delegate.getDefaultFloat(name);
  }

  public int getDefaultInt(String name)
  {
    return delegate.getDefaultInt(name);
  }

  public long getDefaultLong(String name)
  {
    return delegate.getDefaultLong(name);
  }

  public String getDefaultString(String name)
  {
    return delegate.getDefaultString(name);
  }

  public double getDouble(String name)
  {
    return delegate.getDouble(name);
  }

  public float getFloat(String name)
  {
    return delegate.getFloat(name);
  }

  public int getInt(String name)
  {
    return delegate.getInt(name);
  }

  public long getLong(String name)
  {
    return delegate.getLong(name);
  }

  public String getString(String name)
  {
    return delegate.getString(name);
  }

  public boolean isDefault(String name)
  {
    return delegate.isDefault(name);
  }

  public boolean needsSaving()
  {
    return delegate.needsSaving();
  }

  public void putValue(String name, String value)
  {
    delegate.putValue(name, value);
  }

  public void removePropertyChangeListener(IPropertyChangeListener listener)
  {
    delegate.removePropertyChangeListener(listener);
  }

  public void setDefault(String name, double value)
  {
    delegate.setDefault(name, value);
  }

  public void setDefault(String name, float value)
  {
    delegate.setDefault(name, value);
  }

  public void setDefault(String name, int value)
  {
    delegate.setDefault(name, value);
  }

  public void setDefault(String name, long value)
  {
    delegate.setDefault(name, value);
  }

  public void setDefault(String name, String defaultObject)
  {
    delegate.setDefault(name, defaultObject);
  }

  public void setDefault(String name, boolean value)
  {
    delegate.setDefault(name, value);
  }

  public void setToDefault(String name)
  {
    delegate.setToDefault(name);
  }

  public void setValue(String name, double value)
  {
    delegate.setValue(name, value);
  }

  public void setValue(String name, float value)
  {
    delegate.setValue(name, value);
  }

  public void setValue(String name, int value)
  {
    delegate.setValue(name, value);
  }

  public void setValue(String name, long value)
  {
    delegate.setValue(name, value);
  }

  public void setValue(String name, String value)
  {
    delegate.setValue(name, value);
  }

  public void setValue(String name, boolean value)
  {
    delegate.setValue(name, value);
  }

}
