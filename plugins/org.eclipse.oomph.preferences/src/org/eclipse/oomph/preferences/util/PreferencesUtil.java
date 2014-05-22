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
package org.eclipse.oomph.preferences.util;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.Property;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Eike Stepper
 */
public final class PreferencesUtil
{
  /**
   * A resource load option to load an instance using {@link #getRootPreferenceNode(boolean) PreferencesUtil.getRootPreferenceNode(true)}.
   * The resource must be {@link Resource#unload() unloaded}, to avoid dangling listeners.
   */
  public static final String OPTION_SYNCHRONIZED_PREFERENCES = "SYNCHRONIZED_PREFERENCES";

  private static final IEclipsePreferences ROOT = Platform.getPreferencesService().getRootNode();

  public static final URI ROOT_PREFERENCE_NODE_URI = URI.createURI("preference:/");

  private static class PreferencesAdapter extends AdapterImpl implements IEclipsePreferences.INodeChangeListener, IEclipsePreferences.IPreferenceChangeListener
  {
    protected IEclipsePreferences preferences;

    public PreferencesAdapter(IEclipsePreferences preferences)
    {
      this.preferences = preferences;

      preferences.addNodeChangeListener(this);
      preferences.addPreferenceChangeListener(this);
    }

    @Override
    public boolean isAdapterForType(Object type)
    {
      return type == PreferencesAdapter.class;
    }

    public void preferenceChange(PreferenceChangeEvent event)
    {
      PreferenceNode preferenceNode = (PreferenceNode)target;
      String name = event.getKey();
      Object value = event.getNewValue();
      EList<Property> properties = preferenceNode.getProperties();
      for (int i = 0, size = properties.size(); i < size; ++i)
      {
        Property property = properties.get(i);
        int comparison = property.getName().compareTo(name);
        if (comparison == 0)
        {
          if (value == null)
          {
            properties.remove(i);
          }
          else
          {
            property.setValue(value.toString());
          }
          return;
        }
        else if (comparison > 0)
        {
          if (value != null)
          {
            property = PreferencesFactory.eINSTANCE.createProperty();
            property.setName(name);
            property.setValue(value.toString());
            properties.add(i, property);
          }
          return;
        }
      }
      Property property = PreferencesFactory.eINSTANCE.createProperty();
      property.setName(name);
      property.setValue(value.toString());
      properties.add(property);
    }

    public void added(NodeChangeEvent event)
    {
      PreferenceNode preferenceNode = (PreferenceNode)target;
      Preferences childNode = event.getChild();
      PreferenceNode childPreferenceNode = PreferencesFactory.eINSTANCE.createPreferenceNode();
      String name = childNode.name();
      childPreferenceNode.setName(name);
      traverse(childPreferenceNode, childNode, true);
      EList<PreferenceNode> children = preferenceNode.getChildren();
      for (int i = 0, size = children.size(); i < size; ++i)
      {
        PreferenceNode otherChildPreferenceNode = children.get(i);
        if (otherChildPreferenceNode.getName().compareTo(name) >= 0)
        {
          children.add(i, childPreferenceNode);
          return;
        }
      }
      children.add(childPreferenceNode);
    }

    public void removed(NodeChangeEvent event)
    {
      PreferenceNode preferenceNode = (PreferenceNode)target;
      Preferences childNode = event.getChild();
      String name = childNode.name();
      EList<PreferenceNode> children = preferenceNode.getChildren();
      for (int i = 0, size = children.size(); i < size; ++i)
      {
        PreferenceNode childPreferenceNode = children.get(i);
        if (childPreferenceNode.getName().equals(name))
        {
          children.remove(i);
          return;
        }
      }
    }

    @Override
    public void unsetTarget(Notifier oldTarget)
    {
      super.unsetTarget(oldTarget);

      preferences.removeNodeChangeListener(this);
      preferences.removePreferenceChangeListener(this);
    }
  }

  public static PreferenceNode getRootPreferenceNode()
  {
    return getRootPreferenceNode(false);
  }

  public static PreferenceNode getRootPreferenceNode(boolean isSynchronized)
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    Resource resource = resourceSet.createResource(ROOT_PREFERENCE_NODE_URI.appendSegment("*.preferences"));
    PreferenceNode root = PreferencesFactory.eINSTANCE.createPreferenceNode();
    traverse(root, ROOT, isSynchronized);

    int index = 0;
    for (String name : new String[] { "bundle_defaults", "default", "configuration", "instance", "project" })
    {
      PreferenceNode node = root.getNode(name);
      if (node != null)
      {
        root.getChildren().move(index++, node);
      }
    }

    resource.getContents().add(root);

    return root;
  }

  private static void traverse(PreferenceNode preferenceNode, Preferences node, boolean isSynchronized)
  {
    try
    {
      if (isSynchronized && node instanceof IEclipsePreferences)
      {
        preferenceNode.eAdapters().add(new PreferencesAdapter((IEclipsePreferences)node));
      }

      preferenceNode.setName(node.name());

      EList<PreferenceNode> children = preferenceNode.getChildren();
      String[] childrenNames = node.childrenNames();
      Arrays.sort(childrenNames);
      for (String name : childrenNames)
      {
        Preferences childNode = node.node(name);
        PreferenceNode childPreferenceNode = PreferencesFactory.eINSTANCE.createPreferenceNode();
        traverse(childPreferenceNode, childNode, isSynchronized);
        children.add(childPreferenceNode);
      }
      EList<Property> properties = preferenceNode.getProperties();
      String[] keys = node.keys();
      Arrays.sort(keys);
      for (String name : keys)
      {
        Property property = PreferencesFactory.eINSTANCE.createProperty();
        property.setName(name);
        property.setValue(node.get(name, null));
        properties.add(property);
      }
    }
    catch (BackingStoreException ex)
    {
      // Ignore
    }
  }

  public static Preferences getPreferences(PreferenceNode preferenceNode, boolean demandCreate) throws BackingStoreException
  {
    if (preferenceNode == null)
    {
      return ROOT;
    }

    Preferences parentPreferences = getPreferences(preferenceNode.getParent(), demandCreate);
    if (parentPreferences != null)
    {
      String name = preferenceNode.getName();
      if (demandCreate || parentPreferences.nodeExists(name))
      {
        return parentPreferences.node(name);
      }
    }
    return null;
  }

  public static IPath getLocation(Preferences preferences)
  {
    if (preferences == null)
    {
      return null;
    }

    try
    {
      Method getLocationMethod = preferences.getClass().getDeclaredMethod("getLocation");
      getLocationMethod.setAccessible(true);
      IPath location = (IPath)getLocationMethod.invoke(preferences);
      return location;
    }
    catch (Exception ex)
    {
      // Ignore
    }

    return null;
  }

  public static class PreferenceProperty
  {
    private Preferences node;

    private String property;

    public PreferenceProperty(String preferencePropertyPath)
    {
      node = Platform.getPreferencesService().getRootNode();

      String[] segments = preferencePropertyPath.split("/");
      StringBuilder property = null;
      boolean startProperty = false;
      for (int i = 0; i < segments.length - 1; i++)
      {
        String segment = segments[i];
        if (property != null)
        {
          if (startProperty)
          {
            property.append('/');
          }
          else
          {
            startProperty = true;
          }

          property.append(segment);
        }
        else if (i != 0 && segment.length() == 0)
        {
          property = new StringBuilder();
        }
        else
        {
          node = node.node(segment);
        }
      }

      if (property == null)
      {
        this.property = segments[segments.length - 1];
      }
      else
      {
        this.property = property.append('/').append(segments[segments.length - 1]).toString();
      }
    }

    public Preferences getNode()
    {
      return node;
    }

    public String getProperty()
    {
      return property;
    }

    public void set(String value)
    {
      if (value == null)
      {
        remove();
      }
      node.put(property, value);
    }

    public String get(String defaultValue)
    {
      return node.get(property, defaultValue);
    }

    public void remove()
    {
      node.remove(property);
    }

    public void setInt(int value)
    {
      node.putInt(property, value);
    }

    public int getInt(int defaultValue)
    {
      return node.getInt(property, defaultValue);
    }

    public void setLong(long value)
    {
      node.putLong(property, value);
    }

    public long getLong(long defaultValue)
    {
      return node.getLong(property, defaultValue);
    }

    public void setBoolean(boolean value)
    {
      node.putBoolean(property, value);
    }

    public boolean getBoolean(boolean defaultValue)
    {
      return node.getBoolean(property, defaultValue);
    }

    public void setFloat(float value)
    {
      node.putFloat(property, value);
    }

    public float getFloat(float defaultValue)
    {
      return node.getFloat(property, defaultValue);
    }

    public void setDouble(double value)
    {
      node.putDouble(property, value);
    }

    public double getDouble(double defaultValue)
    {
      return node.getDouble(property, defaultValue);
    }

    public void setByteArray(byte[] value)
    {
      if (value == null)
      {
        remove();
      }
      else
      {
        node.putByteArray(property, value);
      }
    }

    public byte[] getByteArray(byte[] defaultValue)
    {
      return node.getByteArray(property, defaultValue);
    }
  }
}
