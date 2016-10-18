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
package org.eclipse.oomph.preferences.util;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.DESCipherImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.IPreferenceNodeVisitor;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
  private static final Cipher CIPHER = new Cipher();

  /**
   * A resource load option to load an instance using {@link #getRootPreferenceNode(boolean) PreferencesUtil.getRootPreferenceNode(true)}.
   * The resource must be {@link Resource#unload() unloaded}, to avoid dangling listeners.
   */
  public static final String OPTION_SYNCHRONIZED_PREFERENCES = "SYNCHRONIZED_PREFERENCES";

  public static final String BUNDLE_DEFAULTS_NODE = "bundle_defaults";

  public static final String PREFERENCE_SCHEME = "preference";

  public static final URI ROOT_PREFERENCE_NODE_URI = URI.createURI(PREFERENCE_SCHEME + ":/");

  public static final String DEFAULT_NODE = "default";

  public static final String CONFIRGURATION_NODE = "configuration";

  public static final String INSTANCE_NODE = "instance";

  public static final String PROJECT_NODE = "project";

  public static final String SECURE_NODE = "secure";

  public static final Set<String> ALL_CHILD_NODES = Collections.unmodifiableSet(new LinkedHashSet<String>(
      Arrays.asList(new String[] { SECURE_NODE, BUNDLE_DEFAULTS_NODE, DEFAULT_NODE, CONFIRGURATION_NODE, INSTANCE_NODE, PROJECT_NODE })));

  private static final IEclipsePreferences ROOT = Platform.getPreferencesService().getRootNode();

  private static final Map<ISecurePreferences, ISecurePreferences> WRAPPERS = new HashMap<ISecurePreferences, ISecurePreferences>();

  @SuppressWarnings("restriction")
  public static ISecurePreferences getSecurePreferences()
  {
    ISecurePreferences defaultSecurePreferences = SecurePreferencesFactory.getDefault();
    if (defaultSecurePreferences instanceof org.eclipse.equinox.internal.security.storage.SecurePreferencesWrapper)
    {
      synchronized (WRAPPERS)
      {
        // We create our own wrapper for the secure preferences; one that will reload the storage when it's changed by another process.
        ISecurePreferences securePreferences = WRAPPERS.get(defaultSecurePreferences);
        if (securePreferences == null)
        {
          try
          {
            // Extract the node and container of the existing wrapper
            org.eclipse.equinox.internal.security.storage.SecurePreferences node = ReflectUtil.getValue("node", defaultSecurePreferences);
            org.eclipse.equinox.internal.security.storage.SecurePreferencesContainer container = ReflectUtil.getValue("container", defaultSecurePreferences);

            // Extract the options and root of the existing container.
            Map<Object, Object> options = ReflectUtil.getValue("options", container);
            org.eclipse.equinox.internal.security.storage.SecurePreferencesRoot root = ReflectUtil.getValue("root", container);

            // Create a container that creates our specialized wrappers instead of the basic ones.
            container = new org.eclipse.equinox.internal.security.storage.SecurePreferencesContainer(root, options)
            {
              final Map<org.eclipse.equinox.internal.security.storage.SecurePreferences, ISecurePreferences> myWrappers = ReflectUtil.getValue("wrappers",
                  this);

              @Override
              public ISecurePreferences wrapper(org.eclipse.equinox.internal.security.storage.SecurePreferences node)
              {
                synchronized (myWrappers)
                {
                  ISecurePreferences wrapper = myWrappers.get(node);
                  if (wrapper == null)
                  {
                    wrapper = new AutoRefreshSecurePreferencesWrapper(node, this);
                    myWrappers.put(node, wrapper);
                  }

                  return wrapper;
                }
              }
            };

            securePreferences = new AutoRefreshSecurePreferencesWrapper(node, container);
          }
          catch (RuntimeException ex)
          {
            return defaultSecurePreferences;
          }

          WRAPPERS.put(defaultSecurePreferences, securePreferences);
        }

        defaultSecurePreferences = securePreferences;
      }
    }

    return defaultSecurePreferences;
  }

  public static PreferenceNode getRootPreferenceNode()
  {
    return getRootPreferenceNode(false);
  }

  public static PreferenceNode getRootPreferenceNode(boolean isSynchronized)
  {
    return getRootPreferenceNode(ALL_CHILD_NODES, isSynchronized);
  }

  public static PreferenceNode getRootPreferenceNode(Set<String> childNodes, boolean isSynchronized)
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    Resource resource = resourceSet.createResource(ROOT_PREFERENCE_NODE_URI.appendSegment("*.preferences"));
    PreferenceNode root = PreferencesFactory.eINSTANCE.createPreferenceNode();
    resource.getContents().add(root);

    traverse(root, childNodes == ALL_CHILD_NODES ? null : childNodes, ROOT, isSynchronized);

    if (childNodes.size() > 1)
    {
      int index = 0;
      for (String name : childNodes)
      {
        PreferenceNode node = root.getNode(name);
        if (node != null)
        {
          root.getChildren().move(index++, node);
        }
      }
    }

    if (childNodes.contains(SECURE_NODE))
    {
      PreferenceNode secureRoot = PreferencesFactory.eINSTANCE.createPreferenceNode();
      ISecurePreferences securePreferences = getSecurePreferences();
      if (securePreferences != null)
      {
        try
        {
          traverse(secureRoot, null, SecurePreferenceWapper.create(securePreferences), false);
        }
        catch (Throwable ex)
        {
          // Ignore
        }
      }

      secureRoot.setName(SECURE_NODE);
      root.getChildren().add(0, secureRoot);
    }

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
      else if (preferences == ROOT && SECURE_NODE.equals(name))
      {
        result.addAll(reconcile(child, SecurePreferenceWapper.create(getSecurePreferences())));
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
      String value = property.getSecureValue();
      if (propertyNames.remove(name))
      {
        try
        {
          String oldValue = preferences.get(name, null);
          if (!ObjectUtil.equals(value, oldValue))
          {
            isModified = true;
            preferences.put(name, value);
          }
        }
        catch (RuntimeException ex)
        {
          // Do nothing if we can't get the old value.
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
      String value = property.getSecureValue();
      if (value != null)
      {
        String name = property.getName();
        if (childPreferences instanceof ISecurePreferences)
        {
          ISecurePreferences securePreferences = (ISecurePreferences)childPreferences;
          try
          {
            securePreferences.put(name, value, property.isSecure());
          }
          catch (StorageException ex)
          {
            throw new RuntimeException(ex);
          }
        }
        else
        {
          childPreferences.put(name, value);
        }
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
      else if (parentPreferences == ROOT && SECURE_NODE.equals(name))
      {
        return SecurePreferenceWapper.create(getSecurePreferences());
      }
    }

    return null;
  }

  private static void traverse(PreferenceNode preferenceNode, Set<String> childNodes, Preferences node, boolean isSynchronized)
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
        if (childNodes == null || childNodes.contains(name))
        {
          Preferences childNode = node.node(name);
          PreferenceNode childPreferenceNode = PreferencesFactory.eINSTANCE.createPreferenceNode();
          children.add(childPreferenceNode);
          traverse(childPreferenceNode, null, childNode, isSynchronized);
        }
      }

      EList<Property> properties = preferenceNode.getProperties();
      String[] keys = node.keys();
      Arrays.sort(keys);
      for (String name : keys)
      {
        Property property = PreferencesFactory.eINSTANCE.createProperty();
        property.setName(name);

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

        String value = null;
        try
        {
          // This can throw runtime exceptions.
          // We should ignore any exception that might result when fetching the value.
          value = node.get(name, null);
        }
        catch (RuntimeException ex)
        {
          // Ignore.
        }

        property.setValue(value == null ? "" : value);

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

  public static String encrypt(String value)
  {
    return CIPHER.encrypt(value);
  }

  public static String decrypt(String value)
  {
    return CIPHER.decrypt(value);
  }

  private static boolean isEncryptedValue(String value)
  {
    return CIPHER.isEncryptedValue(value);
  }

  /**
   * A specialized secure preferences wrapper that auto refreshes, i.e., the reloads the secure store what it has changed in the file system, presumably because another process has changed it.
   * @author Ed Merks
   */
  @SuppressWarnings("restriction")
  private static final class AutoRefreshSecurePreferencesWrapper extends org.eclipse.equinox.internal.security.storage.SecurePreferencesWrapper
  {
    private AutoRefreshSecurePreferencesWrapper(org.eclipse.equinox.internal.security.storage.SecurePreferences node,
        org.eclipse.equinox.internal.security.storage.SecurePreferencesContainer container)
    {
      super(node, container);
    }

    @Override
    public String absolutePath()
    {
      refresh();
      return super.absolutePath();
    }

    @Override
    public String[] childrenNames()
    {
      refresh();
      return super.childrenNames();
    }

    @Override
    public void clear()
    {
      refresh();
      super.clear();
    }

    @Override
    public String[] keys()
    {
      refresh();
      return super.keys();
    }

    @Override
    public void remove(String key)
    {
      refresh();
      super.remove(key);
    }

    @Override
    public ISecurePreferences node(String pathName)
    {
      refresh();
      return super.node(pathName);
    }

    @Override
    public ISecurePreferences parent()
    {
      refresh();
      return super.parent();
    }

    @Override
    public boolean nodeExists(String pathName)
    {
      refresh();
      return super.nodeExists(pathName);
    }

    @Override
    public void removeNode()
    {
      refresh();
      super.removeNode();
    }

    @Override
    public String get(String key, String def) throws StorageException
    {
      refresh();
      return super.get(key, def);
    }

    @Override
    public void put(String key, String value, boolean encrypt) throws StorageException
    {
      refresh();
      super.put(key, value, encrypt);
    }

    @Override
    public boolean getBoolean(String key, boolean def) throws StorageException
    {
      refresh();
      return super.getBoolean(key, def);
    }

    @Override
    public void putBoolean(String key, boolean value, boolean encrypt) throws StorageException
    {
      refresh();
      super.putBoolean(key, value, encrypt);
    }

    @Override
    public int getInt(String key, int def) throws StorageException
    {
      refresh();
      return super.getInt(key, def);
    }

    @Override
    public void putInt(String key, int value, boolean encrypt) throws StorageException
    {
      refresh();
      super.putInt(key, value, encrypt);
    }

    @Override
    public float getFloat(String key, float def) throws StorageException
    {
      refresh();
      return super.getFloat(key, def);
    }

    @Override
    public void putFloat(String key, float value, boolean encrypt) throws StorageException
    {
      refresh();
      super.putFloat(key, value, encrypt);
    }

    @Override
    public long getLong(String key, long def) throws StorageException
    {
      refresh();
      return super.getLong(key, def);
    }

    @Override
    public void putLong(String key, long value, boolean encrypt) throws StorageException
    {
      refresh();
      super.putLong(key, value, encrypt);
    }

    @Override
    public double getDouble(String key, double def) throws StorageException
    {
      refresh();
      return super.getDouble(key, def);
    }

    @Override
    public void putDouble(String key, double value, boolean encrypt) throws StorageException
    {
      refresh();
      super.putDouble(key, value, encrypt);
    }

    @Override
    public byte[] getByteArray(String key, byte[] def) throws StorageException
    {
      refresh();
      return super.getByteArray(key, def);
    }

    @Override
    public void putByteArray(String key, byte[] value, boolean encrypt) throws StorageException
    {
      refresh();
      super.putByteArray(key, value, encrypt);
    }

    @Override
    public boolean isEncrypted(String key) throws StorageException
    {
      refresh();
      return super.isEncrypted(key);
    }

    @Override
    public String getModule(String key)
    {
      refresh();
      return super.getModule(key);
    }

    @Override
    public boolean passwordChanging(String moduleID)
    {
      refresh();
      return super.passwordChanging(moduleID);
    }

    private void refresh()
    {
      // Fetch the root node and check if it has been modified, i.e., whether it is dirty from unsaved changes.
      Object root = ReflectUtil.invokeMethod("getRoot", node);
      boolean modified = (Boolean)ReflectUtil.invokeMethod("isModified", root);
      if (!modified)
      {
        // If it's not dirty, check if the expected time stamp is different from the timestamp on disk.
        long lastModified = (Long)ReflectUtil.invokeMethod("getLastModified", root);
        long timestamp = ReflectUtil.getValue("timestamp", root);
        if (lastModified != timestamp)
        {
          // If so, reload the secure storage from disk.
          ReflectUtil.invokeMethod("load", root);
        }
      }
    }
  }

  /**
   * @author Ed Merks
   */
  public static class PreferenceProperty
  {
    private Preferences node;

    private String property;

    public PreferenceProperty(String preferencePropertyPath)
    {
      IEclipsePreferences rootNode = Platform.getPreferencesService().getRootNode();
      node = rootNode;

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
          if (node == rootNode && "secure".equals(segment))
          {
            rootNode = SecurePreferenceWapper.create(getSecurePreferences());
            node = rootNode;
          }
          else
          {
            node = node.node(segment);
          }
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

    private PreferenceProperty(Preferences node, String property)
    {
      this.node = node;
      this.property = property;
    }

    public Preferences getNode()
    {
      return node;
    }

    public String getProperty()
    {
      return property;
    }

    public PreferenceProperty getEffectiveProperty()
    {
      try
      {
        for (String key : node.keys())
        {
          if (key.equals(property))
          {
            return this;
          }
        }
      }
      catch (BackingStoreException ex1)
      {
        //$FALL-THROUGH$
      }

      Preferences rootNode = Platform.getPreferencesService().getRootNode();
      for (String path = nextScope(node.absolutePath()); path != null; path = nextScope(path))
      {
        try
        {
          if (rootNode.nodeExists(path))
          {
            return new PreferenceProperty(rootNode.node(path), property).getEffectiveProperty();
          }
        }
        catch (BackingStoreException ex)
        {
          //$FALL-THROUGH$
        }
      }

      return this;
    }

    public PreferenceProperty getEffectiveDefaultProperty()
    {
      Preferences rootNode = Platform.getPreferencesService().getRootNode();
      for (String path = nextScope(node.absolutePath()); path != null; path = nextScope(path))
      {
        try
        {
          if (rootNode.nodeExists(path))
          {
            return new PreferenceProperty(rootNode.node(path), property).getEffectiveProperty();
          }
        }
        catch (BackingStoreException ex)
        {
          //$FALL-THROUGH$
        }
      }

      return null;
    }

    private String nextScope(String path)
    {
      if (path.startsWith("/instance/"))
      {
        return "/default/" + path.substring("/instance/".length());
      }

      if (path.startsWith("/default/"))
      {
        return "/bundle-defaults/" + path.substring("/default/".length());
      }

      return null;
    }

    public void set(String value)
    {
      if (value == null)
      {
        remove();
      }
      else
      {
        if (node instanceof SecurePreferenceWapper)
        {
          SecurePreferenceWapper securePreferenceWapper = (SecurePreferenceWapper)node;
          try
          {
            // Check if the property is already marked as encrypted.
            boolean encrypted = securePreferenceWapper.preferences.isEncrypted(property);

            // If not, but the value itself is marked as encrypted, then we want to be sure to store it as encrypted.
            if (!encrypted && PreferencesUtil.isEncryptedValue(value))
            {
              encrypted = true;
            }

            // If we are going to store it as encrypted, then we'd better remove our own in-memory encryption first.
            if (encrypted)
            {
              value = decrypt(value);
            }

            securePreferenceWapper.preferences.put(property, value, encrypted);
            return;
          }
          catch (StorageException ex)
          {
            return;
          }
        }

        node.put(property, value);
      }
    }

    public String get(String defaultValue)
    {
      String value = node.get(property, defaultValue);
      if (node instanceof SecurePreferenceWapper)
      {
        SecurePreferenceWapper securePreferenceWapper = (SecurePreferenceWapper)node;
        try
        {
          boolean encrypted = securePreferenceWapper.preferences.isEncrypted(property);
          if (encrypted)
          {
            return PreferencesUtil.encrypt(value);
          }
        }
        catch (StorageException ex)
        {
          return defaultValue;
        }
      }

      return value;
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

  /**
   * @author Ed Merks
   */
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
        builder.append('/');
        builder.append(SECURE_NODE);
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

  /**
   * @author Ed Merks
   */
  private static class Cipher extends DESCipherImpl
  {
    private static final String ENCRYPTION_PREFIX = "#!";

    public Cipher()
    {
      super(EcoreUtil.generateUUID());
    }

    public boolean isEncryptedValue(String value)
    {
      return value != null && value.startsWith(ENCRYPTION_PREFIX);
    }

    public String encrypt(String value)
    {
      if (value == null)
      {
        return null;
      }
      else if ("".equals(value))
      {
        return "";
      }

      try
      {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        OutputStream out = encrypt(bytes);
        out.write(value.getBytes());
        out.close();
        return ENCRYPTION_PREFIX + XMLTypeFactory.eINSTANCE.convertBase64Binary(bytes.toByteArray());
      }
      catch (Exception ex)
      {
        throw new IORuntimeException(ex);
      }
    }

    public String decrypt(String value)
    {
      if (value == null)
      {
        return null;
      }
      else if ("".equals(value))
      {
        return "";
      }

      if (value.startsWith(ENCRYPTION_PREFIX))
      {
        value = value.substring(2);
      }

      ByteArrayInputStream byteValue = new ByteArrayInputStream(XMLTypeFactory.eINSTANCE.createBase64Binary(value));
      try
      {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        InputStream in = decrypt(byteValue);
        IOUtil.copy(in, bytes);
        return new String(bytes.toByteArray());
      }
      catch (Exception ex)
      {
        throw new IORuntimeException(ex);
      }
    }
  }

  /**
   * @author Ed Merks
   */
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
      for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace())
      {
        String methodName = stackTraceElement.getMethodName();
        if ("initializeDefaultPreferences".equals(methodName))
        {
          // Ignore preferences created during initialization.
          return;
        }
        else if ("start".equals(methodName) && "org.eclipse.osgi.internal.framework.BundleContextImpl".equals(stackTraceElement.getClassName()))
        {
          // Ignore preferences created by bundle start.
          return;
        }
        else if ("createExecutableExtension".equals(methodName)
            && "org.eclipse.core.internal.registry.ConfigurationElementHandle".equals(stackTraceElement.getClassName()))
        {
          return;
        }
        else if ("showPage".equals(methodName) && "org.eclipse.ui.internal.dialogs.FilteredPreferenceDialog".equals(stackTraceElement.getClassName()))
        {
          return;
        }
      }

      PreferenceNode preferenceNode = (PreferenceNode)target;
      Resource resource = preferenceNode.eResource();
      if (resource == null)
      {
        handlePreferenceChange(event, preferenceNode);
      }
      else
      {
        ResourceSet resourceSet = resource.getResourceSet();
        synchronized (resource)
        {
          synchronized (resourceSet)
          {
            handlePreferenceChange(event, preferenceNode);
          }
        }
      }
    }

    private void handlePreferenceChange(PreferenceChangeEvent event, PreferenceNode preferenceNode)
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

      if (value != null)
      {
        Property property = PreferencesFactory.eINSTANCE.createProperty();
        property.setName(name);
        property.setValue(value.toString());
        properties.add(property);
      }
    }

    public void added(NodeChangeEvent event)
    {
      PreferenceNode preferenceNode = (PreferenceNode)target;
      Resource resource = preferenceNode.eResource();
      if (resource == null)
      {
        handleAdded(event, preferenceNode);
      }
      else
      {
        synchronized (resource)
        {
          ResourceSet resourceSet = resource.getResourceSet();
          synchronized (resourceSet)
          {
            handleAdded(event, preferenceNode);
          }
        }
      }
    }

    private void handleAdded(NodeChangeEvent event, PreferenceNode preferenceNode)
    {
      Preferences childNode = event.getChild();
      String name = childNode.name();
      if (preferenceNode.getNode(name) == null)
      {
        PreferenceNode childPreferenceNode = PreferencesFactory.eINSTANCE.createPreferenceNode();
        childPreferenceNode.setName(name);
        EList<PreferenceNode> children = preferenceNode.getChildren();
        int index = 0;
        for (int size = children.size(); index < size; ++index)
        {
          PreferenceNode otherChildPreferenceNode = children.get(index);
          if (otherChildPreferenceNode.getName().compareTo(name) >= 0)
          {
            break;
          }
        }

        children.add(index, childPreferenceNode);
        traverse(childPreferenceNode, null, childNode, true);
      }
    }

    public void removed(NodeChangeEvent event)
    {
      PreferenceNode preferenceNode = (PreferenceNode)target;
      Resource resource = preferenceNode.eResource();
      if (resource == null)
      {
        handleRemoved(event, preferenceNode);
      }
      else
      {
        ResourceSet resourceSet = resource.getResourceSet();
        synchronized (resource)
        {
          synchronized (resourceSet)
          {
            handleRemoved(event, preferenceNode);
          }
        }
      }
    }

    private void handleRemoved(NodeChangeEvent event, PreferenceNode preferenceNode)
    {
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
}
