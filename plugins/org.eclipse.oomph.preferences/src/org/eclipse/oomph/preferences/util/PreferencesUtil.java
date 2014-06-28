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
import org.eclipse.oomph.util.ObjectUtil;

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
import org.eclipse.core.runtime.preferences.IPreferenceNodeVisitor;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.equinox.security.storage.provider.IProviderHints;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

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

  private static final SecurePreferenceWapper SECURE_ROOT = SecurePreferenceWapper.create(getSecurePreferences());

  public static final URI ROOT_PREFERENCE_NODE_URI = URI.createURI("preference:/");

  public static ISecurePreferences getSecurePreferences()
  {
    Map<Object, Object> options = new HashMap<Object, Object>();
    options.put(IProviderHints.PROMPT_USER, Boolean.FALSE);

    try
    {
      return SecurePreferencesFactory.open(null, options);
    }
    catch (IOException ex)
    {
      // log(ex);
    }

    return null;
  }

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
      Resource resource = preferenceNode.eResource();
      ResourceSet resourceSet = resource.getResourceSet();
      synchronized (resource)
      {
        synchronized (resourceSet)
        {
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
      }
    }

    public void added(NodeChangeEvent event)
    {
      PreferenceNode preferenceNode = (PreferenceNode)target;
      Resource resource = preferenceNode.eResource();
      ResourceSet resourceSet = resource.getResourceSet();
      synchronized (resource)
      {
        synchronized (resourceSet)
        {
          Preferences childNode = event.getChild();
          String name = childNode.name();
          if (preferenceNode.getNode(name) == null)
          {
            PreferenceNode childPreferenceNode = PreferencesFactory.eINSTANCE.createPreferenceNode();
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
        }
      }
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
    resource.getContents().add(root);

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

    PreferenceNode secureRoot = PreferencesFactory.eINSTANCE.createPreferenceNode();
    ISecurePreferences securePreferences = getSecurePreferences();
    if (securePreferences != null)
    {
      try
      {
        traverse(secureRoot, SecurePreferenceWapper.create(securePreferences), false);
      }
      catch (Throwable ex)
      {
        // Ignore
      }
    }

    secureRoot.setName("secure");
    root.getChildren().add(0, secureRoot);

    return root;
  }

  /**
   * Reconciles the preferences as specified by the preference node with the real Eclipse preferences
   * and returns the root nodes that need to be {@link IEclipsePreferences#flush() flushed} to store the changes to the backing store.
   */
  public static Collection<? extends IEclipsePreferences> reconcile(PreferenceNode preferenceNode) throws BackingStoreException
  {
    IEclipsePreferences preferences = getPreferences(preferenceNode);
    if (preferences == null)
    {
      throw new BackingStoreException("The preference node is not backed by a real Eclipse preference" + preferenceNode);
    }

    return reconcile(preferenceNode, preferences);
  }

  private static Collection<? extends IEclipsePreferences> reconcile(PreferenceNode preferenceNode, IEclipsePreferences preferences)
      throws BackingStoreException
  {
    boolean isModified = false;
    Set<IEclipsePreferences> result = new LinkedHashSet<IEclipsePreferences>();
    Set<String> childNames = new HashSet<String>(Arrays.asList(preferences.childrenNames()));
    for (PreferenceNode child : preferenceNode.getChildren())
    {
      String name = child.getName();
      if (childNames.remove(name))
      {
        result.addAll(reconcile(child, (IEclipsePreferences)preferences.node(name)));
      }
      else if (preferences == ROOT && "secure".equals(name))
      {
        result.addAll(reconcile(child, SECURE_ROOT));
      }
      else
      {
        isModified = true;
        create(child, preferences);
      }
    }

    for (String name : childNames)
    {
      isModified = true;
      preferences.node(name).removeNode();
    }

    Set<String> propertyNames = new HashSet<String>(Arrays.asList(preferences.keys()));
    for (Property property : preferenceNode.getProperties())
    {
      String name = property.getName();
      String value = property.getValue();
      if (propertyNames.remove(name))
      {
        if (!ObjectUtil.equals(value, preferences.get(name, null)))
        {
          isModified = true;
          preferences.put(name, value);
        }
      }
      else
      {
        isModified = true;
        if (property.isSecure() && preferences instanceof ISecurePreferences)
        {
          ISecurePreferences securePreferences = (ISecurePreferences)preferences;
          try
          {
            securePreferences.put(name, value, true);
          }
          catch (StorageException ex)
          {
            throw new BackingStoreException(ex.getMessage(), ex);
          }
        }
        else
        {
          preferences.put(name, value);
        }
      }
    }

    for (String name : propertyNames)
    {
      isModified = true;
      preferences.remove(name);
    }

    return isModified ? Collections.singleton(preferences) : result;
  }

  private static void create(PreferenceNode preferenceNode, IEclipsePreferences preferences)
  {
    IEclipsePreferences childPreferences = (IEclipsePreferences)preferences.node(preferenceNode.getName());
    for (PreferenceNode child : preferenceNode.getChildren())
    {
      create(child, childPreferences);
    }

    for (Property property : preferenceNode.getProperties())
    {
      String value = property.getValue();
      if (value != null)
      {
        String name = property.getName();
        childPreferences.put(name, value);
      }
    }
  }

  private static IEclipsePreferences getPreferences(PreferenceNode preferenceNode) throws BackingStoreException
  {
    if (preferenceNode == null)
    {
      return ROOT;
    }

    IEclipsePreferences parentPreferences = getPreferences(preferenceNode.getParent());
    if (parentPreferences != null)
    {
      String name = preferenceNode.getName();
      if (parentPreferences.nodeExists(name))
      {
        return (IEclipsePreferences)parentPreferences.node(name);
      }
      else if (parentPreferences == ROOT && "secure".equals(name))
      {
        return SECURE_ROOT;
      }
    }

    return null;
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
        String value = node.get(name, null);
        property.setValue(value == null ? "" : value);

        if (node instanceof ISecurePreferences)
        {
          ISecurePreferences securePreferences = (ISecurePreferences)node;
          try
          {
            boolean encrypted = securePreferences.isEncrypted(name);
            property.setSecure(encrypted);
          }
          catch (StorageException ex)
          {
            // Ignore.
          }
        }

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
      boolean exists = false;
      if (name != null)
      {
        try
        {
          exists = parentPreferences.nodeExists(name);
        }
        catch (Throwable throwable)
        {
          // Ignore.
        }
      }

      if (demandCreate || exists)
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
      else
      {
        node.put(property, value);
      }
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

  private static class SecurePreferenceWapper implements IEclipsePreferences, ISecurePreferences
  {
    private static final WeakHashMap<ISecurePreferences, SecurePreferenceWapper> WRAPPERS = new WeakHashMap<ISecurePreferences, PreferencesUtil.SecurePreferenceWapper>();

    public static SecurePreferenceWapper create(ISecurePreferences preferences)
    {
      if (preferences == null)
      {
        return null;
      }

      synchronized (WRAPPERS)
      {
        SecurePreferenceWapper securePreferenceWapper = WRAPPERS.get(preferences);
        if (securePreferenceWapper == null)
        {
          securePreferenceWapper = new SecurePreferenceWapper(preferences);
        }
        return securePreferenceWapper;
      }
    }

    private final ISecurePreferences preferences;

    public void put(String key, String value, boolean encrypt) throws StorageException
    {
      preferences.put(key, value, encrypt);
    }

    public void putInt(String key, int value, boolean encrypt) throws StorageException
    {
      preferences.putInt(key, value, encrypt);
    }

    public void putLong(String key, long value, boolean encrypt) throws StorageException
    {
      preferences.putLong(key, value, encrypt);
    }

    public void putBoolean(String key, boolean value, boolean encrypt) throws StorageException
    {
      preferences.putBoolean(key, value, encrypt);
    }

    public void putFloat(String key, float value, boolean encrypt) throws StorageException
    {
      preferences.putFloat(key, value, encrypt);
    }

    public void putDouble(String key, double value, boolean encrypt) throws StorageException
    {
      preferences.putDouble(key, value, encrypt);
    }

    public void putByteArray(String key, byte[] value, boolean encrypt) throws StorageException
    {
      preferences.putByteArray(key, value, encrypt);
    }

    public SecurePreferenceWapper(ISecurePreferences preferences)
    {
      this.preferences = preferences;
    }

    public boolean isEncrypted(String key)
    {
      try
      {
        return preferences.isEncrypted(key);
      }
      catch (StorageException ex)
      {
        return true;
      }
    }

    protected RuntimeException create(Exception exception)
    {
      return new RuntimeException(exception);
    }

    public void put(String key, String value)
    {
      try
      {
        preferences.put(key, value, isEncrypted(key));
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public String get(String key, String def)
    {
      try
      {
        return preferences.get(key, def);
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public void remove(String key)
    {
      preferences.remove(key);
    }

    public void addNodeChangeListener(INodeChangeListener listener)
    {
    }

    public void removeNodeChangeListener(INodeChangeListener listener)
    {
    }

    public void clear()
    {
      preferences.clear();
    }

    public void putInt(String key, int value)
    {
      try
      {
        preferences.putInt(key, value, isEncrypted(key));
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public void addPreferenceChangeListener(IPreferenceChangeListener listener)
    {
    }

    public void removePreferenceChangeListener(IPreferenceChangeListener listener)
    {
    }

    public int getInt(String key, int def)
    {
      try
      {
        return preferences.getInt(key, def);
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public void removeNode()
    {
      preferences.removeNode();
    }

    public SecurePreferenceWapper node(String path)
    {
      return create(preferences.node(path));
    }

    public void putLong(String key, long value)
    {
      try
      {
        preferences.putLong(key, value, isEncrypted(key));
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public void accept(IPreferenceNodeVisitor visitor) throws BackingStoreException
    {
      if (visitor.visit(this))
      {
        for (String name : childrenNames())
        {
          node(name).accept(visitor);
        }
      }
    }

    public long getLong(String key, long def)
    {
      try
      {
        return preferences.getLong(key, def);
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public void putBoolean(String key, boolean value)
    {
      try
      {
        preferences.putBoolean(key, value, isEncrypted(key));
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public boolean getBoolean(String key, boolean def)
    {
      try
      {
        return preferences.getBoolean(key, def);
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public void putFloat(String key, float value)
    {
      try
      {
        preferences.putFloat(key, value, isEncrypted(key));
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public float getFloat(String key, float def)
    {
      try
      {
        return preferences.getFloat(key, def);
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public void putDouble(String key, double value)
    {
      try
      {
        preferences.putDouble(key, value, isEncrypted(key));
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public double getDouble(String key, double def)
    {
      try
      {
        return preferences.getDouble(key, def);
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public void putByteArray(String key, byte[] value)
    {
      try
      {
        preferences.putByteArray(key, value, isEncrypted(key));
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public byte[] getByteArray(String key, byte[] def)
    {
      try
      {
        return preferences.getByteArray(key, def);
      }
      catch (StorageException ex)
      {
        throw create(ex);
      }
    }

    public String[] keys()
    {
      return preferences.keys();
    }

    public String[] childrenNames()
    {
      return preferences.childrenNames();
    }

    public SecurePreferenceWapper parent()
    {
      return create(preferences.parent());
    }

    public boolean nodeExists(String pathName)
    {
      return preferences.nodeExists(pathName);
    }

    public String name()
    {
      return preferences.name();
    }

    public String absolutePath()
    {
      return preferences.absolutePath();
    }

    public void flush()
    {
      try
      {
        preferences.flush();
      }
      catch (IOException ex)
      {
        throw create(ex);
      }
    }

    public void sync() throws BackingStoreException
    {
      flush();
    }

    private StringBuilder toString(ISecurePreferences preferences)
    {
      ISecurePreferences parent = preferences.parent();
      StringBuilder builder;
      if (parent != null)
      {
        builder = toString(parent);
        builder.append('/');
      }
      else
      {
        builder = new StringBuilder();
      }

      String name = preferences.name();
      if (name == null)
      {
        builder.append("/secure");
      }
      else
      {
        builder.append(name);
      }

      return builder;
    }

    @Override
    public String toString()
    {
      return toString(preferences).toString();
    }
  }
}
