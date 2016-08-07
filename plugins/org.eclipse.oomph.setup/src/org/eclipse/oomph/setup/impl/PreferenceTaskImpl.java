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
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.preferences.util.PreferencesUtil.PreferenceProperty;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.impl.PreferenceTaskImpl.PreferenceHandler.Factory;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.CommonUtil;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;

import org.osgi.service.prefs.Preferences;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eclipse Preference Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.PreferenceTaskImpl#getKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.PreferenceTaskImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PreferenceTaskImpl extends SetupTaskImpl implements PreferenceTask
{
  /**
   * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKey()
   * @generated
   * @ordered
   */
  protected static final String KEY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKey()
   * @generated
   * @ordered
   */
  protected String key = KEY_EDEFAULT;

  /**
   * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected static final String VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected String value = VALUE_EDEFAULT;

  private transient PreferencesUtil.PreferenceProperty preferenceProperty;

  private transient PreferenceHandler preferenceHandler;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PreferenceTaskImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return SetupPackage.Literals.PREFERENCE_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getKey()
  {
    return key;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setKeyGen(String newKey)
  {
    String oldKey = key;
    key = newKey;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.PREFERENCE_TASK__KEY, oldKey, key));
    }
  }

  public void setKey(String newKey)
  {
    setKeyGen(CommonUtil.intern(newKey));
    preferenceHandler = null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setValue(String newValue)
  {
    String oldValue = value;
    value = newValue;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.PREFERENCE_TASK__VALUE, oldValue, value));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case SetupPackage.PREFERENCE_TASK__KEY:
        return getKey();
      case SetupPackage.PREFERENCE_TASK__VALUE:
        return getValue();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SetupPackage.PREFERENCE_TASK__KEY:
        setKey((String)newValue);
        return;
      case SetupPackage.PREFERENCE_TASK__VALUE:
        setValue((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case SetupPackage.PREFERENCE_TASK__KEY:
        setKey(KEY_EDEFAULT);
        return;
      case SetupPackage.PREFERENCE_TASK__VALUE:
        setValue(VALUE_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case SetupPackage.PREFERENCE_TASK__KEY:
        return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
      case SetupPackage.PREFERENCE_TASK__VALUE:
        return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (key: ");
    result.append(key);
    result.append(", value: ");
    result.append(value);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getPriority()
  {
    return getPreferenceHandler().getPriority();
  }

  private PreferenceHandler getPreferenceHandler()
  {
    if (preferenceHandler == null)
    {
      preferenceHandler = PreferenceHandler.getHandler(getKey());
    }

    return preferenceHandler;
  }

  @Override
  public Object getOverrideToken()
  {
    return createToken(getKey());
  }

  @Override
  public void overrideFor(SetupTask overriddenSetupTask)
  {
    super.overrideFor(overriddenSetupTask);

    PreferenceTask overriddenPeferenceTask = (PreferenceTask)overriddenSetupTask;
    PreferenceHandler preferenceHandler = getPreferenceHandler();
    if (preferenceHandler.isNeeded(overriddenPeferenceTask.getValue(), getValue()))
    {
      setValue(preferenceHandler.merge());
    }
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 0;
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    if (StringUtil.isEmpty(key))
    {
      return false;
    }

    preferenceProperty = new PreferencesUtil.PreferenceProperty(key);

    String oldValue = preferenceProperty.getEffectiveProperty().get(null);
    String newValue = getValue();
    return getPreferenceHandler().isNeeded(oldValue, newValue);
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    final String key = getKey();

    // Ignore project-specific preferences for projects that don't exist in the workspace.
    URI uri = PreferencesFactory.eINSTANCE.createURI(key);
    if ("project".equals(uri.authority()))
    {
      if (!ResourcesPlugin.getWorkspace().getRoot().getProject(URI.decode(uri.segment(0))).isAccessible())
      {
        context.log("Ignoring preference " + key + " = " + value);
        return;
      }
    }

    final String value = getValue();
    if (value != null && value.indexOf('\n') != -1)
    {
      context.log(value);
    }

    performUI(context, new RunnableWithContext()
    {
      public void run(SetupTaskContext context) throws Exception
      {
        String mergedValue = preferenceHandler.merge();
        preferenceProperty.set(mergedValue);
        preferenceHandler.apply(context);
      }
    });

    Preferences node = preferenceProperty.getNode();
    node.flush();
  }

  public boolean execute(final UserCallback callback) throws Exception
  {
    SetupTaskContext context = new SetupTaskContext()
    {
      public boolean isCanceled()
      {
        return false;
      }

      public void log(String line)
      {
        // Do nothing.
      }

      public void log(String line, Severity severity)
      {
        // Do nothing.
      }

      public void log(String line, boolean filter)
      {
        // Do nothing.
      }

      public void log(String line, boolean filter, Severity severity)
      {
        // Do nothing.
      }

      public void log(IStatus status)
      {
        // Do nothing.
      }

      public void log(Throwable t)
      {
        // Do nothing.
      }

      public void task(SetupTask setupTask)
      {
        // Do nothing.
      }

      public void setTerminating()
      {
        // Do nothing.
      }

      public IProgressMonitor getProgressMonitor(boolean working)
      {
        return new NullProgressMonitor();
      }

      public SetupPrompter getPrompter()
      {
        return new SetupPrompter()
        {
          public OS getOS()
          {
            return OS.INSTANCE;
          }

          public String getVMPath()
          {
            return null;
          }

          public UserCallback getUserCallback()
          {
            return callback;
          }

          public String getValue(VariableTask variable)
          {
            throw new UnsupportedOperationException();
          }

          public boolean promptVariables(List<? extends SetupTaskContext> performers)
          {
            throw new UnsupportedOperationException();
          }
        };
      }

      public Trigger getTrigger()
      {
        throw new UnsupportedOperationException();
      }

      public void checkCancelation()
      {
        // Do nothing.
      }

      public boolean isSelfHosting()
      {
        throw new UnsupportedOperationException();
      }

      public boolean isPerforming()
      {
        throw new UnsupportedOperationException();
      }

      public boolean isOffline()
      {
        throw new UnsupportedOperationException();
      }

      public boolean isMirrors()
      {
        throw new UnsupportedOperationException();
      }

      public boolean isRestartNeeded()
      {
        throw new UnsupportedOperationException();
      }

      public void setRestartNeeded(String reason)
      {
        // Do nothing.
      }

      public User getUser()
      {
        throw new UnsupportedOperationException();
      }

      public Workspace getWorkspace()
      {
        throw new UnsupportedOperationException();
      }

      public Installation getInstallation()
      {
        throw new UnsupportedOperationException();
      }

      public File getInstallationLocation()
      {
        throw new UnsupportedOperationException();
      }

      public File getProductLocation()
      {
        throw new UnsupportedOperationException();
      }

      public File getProductConfigurationLocation()
      {
        throw new UnsupportedOperationException();
      }

      public File getWorkspaceLocation()
      {
        throw new UnsupportedOperationException();
      }

      public String getRelativeProductFolder()
      {
        throw new UnsupportedOperationException();
      }

      public OS getOS()
      {
        throw new UnsupportedOperationException();
      }

      public URIConverter getURIConverter()
      {
        throw new UnsupportedOperationException();
      }

      public URI redirect(URI uri)
      {
        return uri;
      }

      public String redirect(String uri)
      {
        return uri;
      }

      public Object get(Object key)
      {
        return null;
      }

      public Object put(Object key, Object value)
      {
        throw new UnsupportedOperationException();
      }

      public Set<Object> keySet()
      {
        return Collections.emptySet();
      }

      public String getLauncherName()
      {
        throw new UnsupportedOperationException();
      }

      public boolean matchesFilterContext(String filter)
      {
        return false;
      }
    };

    if (isNeeded(context))
    {
      perform(context);
      return true;
    }

    return false;
  }

  /**
   * Pathological list preferences:
   * <ul>
   * <li> //instance/org.eclipse.ui.browser/browsers (order matters for //instance/org.eclipse.ui.browser/browser-choice)
   * <li> //instance/org.eclipse.jdt.core/org.eclipse.jdt.core.compiler.taskTags (order matters for //instance/org.eclipse.jdt.core/org.eclipse.jdt.core.compiler.taskPriorities)
   * <li> //instance/org.eclipse.jdt.ui/org.eclipse.jdt.ui.typefilter.disabled (enabling an item does not remove the item)
   * <li> //instance/org.eclipse.jdt.ui/org.eclipse.jdt.ui.typefilter.enabled (disabling an item does not remove the item)
   * </ul>
   *
   * @author Ed Merks
   */
  public static class PreferenceHandler
  {
    protected URI key;

    protected String oldValue;

    protected String newValue;

    public PreferenceHandler(URI key)
    {
      this.key = key;
    }

    public int getPriority()
    {
      return key != null && "project".equals(key.authority()) ? PRIORITY_LATE : PRIORITY_EARLY;
    }

    public boolean isIgnored()
    {
      return false;
    }

    public final boolean isNeeded(String oldValue, String newValue)
    {
      this.oldValue = oldValue;
      this.newValue = newValue;
      return isNeeded();
    }

    protected boolean isNeeded()
    {
      if (isExcluded())
      {
        return false;
      }

      String mergedValue = merge();
      return !ObjectUtil.equals(mergedValue, oldValue);
    }

    protected boolean isExcluded()
    {
      // If the old value is an XML blob...
      if (oldValue != null && oldValue.startsWith("<?xml "))
      {
        try
        {
          DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
          documentBuilder.setErrorHandler(new ErrorHandler()
          {
            public void warning(SAXParseException exception) throws SAXException
            {
            }

            public void fatalError(SAXParseException exception) throws SAXException
            {
              throw exception;
            }

            public void error(SAXParseException exception) throws SAXException
            {
            }
          });
          Document document = documentBuilder.parse(new InputSource(new StringReader(oldValue)));
          NodeList childNodes = document.getDocumentElement().getChildNodes();
          for (int i = 0, length = childNodes.getLength(); i < length; ++i)
          {
            Node item = childNodes.item(i);
            String nodeValue = item.getNodeValue();

            // If there are any children that aren't simply whitespace content, then it's non-empty XML.
            if (nodeValue == null || nodeValue.trim().length() != 0)
            {
              return true;
            }
          }
        }
        catch (Throwable throwable)
        {
          //$FALL-THROUGH$
        }
      }

      return false;
    }

    public String merge()
    {
      return newValue;
    }

    public String delta()
    {
      return newValue;
    }

    public void apply(SetupTaskContext context)
    {
    }

    public static void main(String[] args)
    {
      PreferenceHandlerFactoryRegistry uriMappingRegistryImpl = (PreferenceHandlerFactoryRegistry)Factory.Registry.INSTANCE;
      uriMappingRegistryImpl.put(URI.createURI("//"), null);
      uriMappingRegistryImpl.put(URI.createURI("///"), null);
      uriMappingRegistryImpl.put(URI.createURI("//instance/foo/bar/"), null);
      uriMappingRegistryImpl.put(URI.createURI("//instance"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return null;
        }
      });

      uriMappingRegistryImpl.getFactory(URI.createURI("//instance/foo/bar/myproperty"));
      uriMappingRegistryImpl.getFactory(URI.createURI("//instance/fudge/bar/myproperty"));

      {
        PreferenceHandler handler = new ListPreferenceHandler(URI.createURI(""), "(?s)([^\\n]+\\n[0-9]+)", "([^\n]+)", "\n");
        if (handler.isNeeded("a\n2\n", "b\n2\na\n1\n"))
        {
          String result = handler.merge();
          System.out.println(result);
        }
        else
        {
          System.out.println("has conflict");
        }
      }

      {
        PreferenceHandler handler = new ListPreferenceHandler(URI.createURI(""), "(?s)([^\\n]+\\n[0-9]+)", "([^ \n]+) ([^\n]+)", "\n");
        if (handler.isNeeded("a b\n2\n", "a b\n3\nb a\n2\na a\n1\n"))
        {
          String result = handler.merge();
          System.out.println(result);
        }
        else
        {
          System.out.println("has conflict");
        }
      }
    }

    public static PreferenceHandler getHandler(String key)
    {
      URI keyURI = PreferencesFactory.eINSTANCE.createURI(key);
      return getHandler(keyURI);
    }

    public static PreferenceHandler getHandler(URI key)
    {
      Factory factory = Factory.Registry.INSTANCE.getFactory(key);
      if (factory == null)
      {
        return new PreferenceHandler(key);
      }

      return factory.create(key);
    }

    protected static void registerListHandlerFactory(String key, char itemSeparator, char keySeparator)
    {
      final String itemSeparatorStr = new String(new char[] { itemSeparator });
      final String itemPattern = "([^" + itemSeparatorStr + "]+)";
      final String keyPattern = keySeparator == (char)0 ? ".*" : "([^" + new String(new char[] { keySeparator }) + "]+)";

      PreferenceHandlerFactoryRegistry registry = (PreferenceHandlerFactoryRegistry)Factory.Registry.INSTANCE;
      registry.put(URI.createURI(key), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new ListPreferenceHandler(key, itemPattern, keyPattern, itemSeparatorStr);
        }
      });
    }

    protected static void registerListHandlerFactory(String key, char itemSeparator)
    {
      registerListHandlerFactory(key, itemSeparator, (char)0);
    }

    static
    {
      PreferenceHandlerFactoryRegistry registry = (PreferenceHandlerFactoryRegistry)Factory.Registry.INSTANCE;
      registry.put(URI.createURI("//"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new PreferenceHandler(key);
        }
      });

      registerListHandlerFactory("//instance/org.eclipse.ui.workbench/ENABLED_DECORATORS", ',', ':');

      // See Javadoc on PreferenceHandler:
      // registerListHandlerFactory("//instance/org.eclipse.jdt.ui/org.eclipse.jdt.ui.typefilter.disabled", ';');
      // registerListHandlerFactory("//instance/org.eclipse.jdt.ui/org.eclipse.jdt.ui.typefilter.enabled", ';');

      registry.put(URI.createURI("//instance/org.eclipse.ui.workbench/"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          String lastSegment = key.lastSegment();
          if ("editors".equals(lastSegment))
          {
            return new IgnoredPreferenceHandler(key);
          }

          if ("resourcetypes".equals(lastSegment))
          {
            return new XMLPreferenceHandler(key, "info", new String[] { "extension", "name" }, null);
          }

          if ("org.eclipse.ui.commands".equals(lastSegment))
          {
            return new PreferenceHandler(key)
            {
              @Override
              protected boolean isExcluded()
              {
                return false;
              }
            };
          }

          return new PreferenceHandler(key);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.core.runtime/content-types/"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          if (key.segmentCount() == 4)
          {
            String property = key.lastSegment();
            if (property.equals("file-extensions") || property.equals("file-names"))
            {
              return new ContentTypeFileExtensionPreferenceHandler(key);
            }

            if (property.equals("charset"))
            {
              return new ContentTypeCharsetPreferenceHandler(key);
            }
          }

          return new PreferenceHandler(key);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.jdt.core/"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          String lastSegment = key.lastSegment();
          if (lastSegment.startsWith("org.eclipse.jdt.core.formatter."))
          {
            return new IgnoredPreferenceHandler(key);
          }

          return new PreferenceHandler(key);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.jdt.ui/"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          String lastSegment = key.lastSegment();
          if (lastSegment.startsWith("cleanup."))
          {
            return new PreferenceHandler(key)
            {
              @Override
              public boolean isIgnored()
              {
                return true;
              }
            };
          }

          return new PreferenceHandler(key);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.jdt.ui/org.eclipse.jdt.ui.formatterprofiles"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new XMLPreferenceHandler(key, "profile", new String[] { "name" }, null);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.jdt.ui/formatter_profile"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new JDTProfileChoicePreferenceHandler(key, "formatter");
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.jdt.ui/org.eclipse.jdt.ui.cleanupprofiles"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new XMLPreferenceHandler(key, "profile", new String[] { "name" }, null);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.jdt.ui/cleanup_profile"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new JDTProfileChoicePreferenceHandler(key, "cleanup");
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.team.core/file_types"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new TeamFileModePreferenceHandler(key);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.team.core/cvs_mode_for_file_without_extensions"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new TeamFileModePreferenceHandler(key);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.jdt.ui/org.eclipse.jdt.ui.text.custom_templates"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new XMLPreferenceHandler(key, "template", new String[] { "id" }, null);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.jdt.ui/org.eclipse.jdt.ui.text.custom_code_templates"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new XMLPreferenceHandler(key, "template", new String[] { "context", "description", "name" }, null);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.ant.ui/org.eclipse.ant.ui.customtemplates"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new XMLPreferenceHandler(key, "template", new String[] { "context", "description", "name" }, new String[] { "id" });
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.core.variables/org.eclipse.core.variables.valueVariables"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new XMLPreferenceHandler(key, "valueVariable", new String[] { "name" }, null);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.m2e.editor.xml/org.eclipse.m2e.editor.xml.templates"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new XMLPreferenceHandler(key, "template", new String[] { "context", "description", "name" }, new String[] { "id" });
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.cdt.ui/org.eclipse.cdt.ui.formatterprofiles"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new XMLPreferenceHandler(key, "profile", new String[] { "name" }, null);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.cdt.ui/formatter_profile"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          return new CDTProfileChoicePreferenceHandler(key);
        }
      });

      registry.put(URI.createURI("//instance/org.eclipse.cdt.core/"), new Factory()
      {
        public PreferenceHandler create(URI key)
        {
          String lastSegment = key.lastSegment();
          if (lastSegment.startsWith("org.eclipse.cdt.core.formatter."))
          {
            return new IgnoredPreferenceHandler(key);
          }

          return new PreferenceHandler(key);
        }
      });
    }

    /**
     * @author Ed Merks
     */
    public interface Factory
    {
      public PreferenceHandler create(URI key);

      /**
       * @author Ed Merks
       */
      public interface Registry
      {
        public Registry INSTANCE = new PreferenceHandlerFactoryRegistry();

        public Factory getFactory(URI key);
      }
    }
  }

  /**
   * @author Ed Merks
   */
  public static class IgnoredPreferenceHandler extends PreferenceHandler
  {
    public IgnoredPreferenceHandler(URI key)
    {
      super(key);
    }

    @Override
    public boolean isIgnored()
    {
      return true;
    }
  }

  /**
   * @author Ed Merks
   */
  public static class ListPreferenceHandler extends PreferenceHandler
  {
    protected final Pattern itemPattern;

    protected final Pattern keyPattern;

    protected final String separator;

    protected final Set<String> changedItems = new LinkedHashSet<String>();

    protected final Map<String, Item> oldItems = new LinkedHashMap<String, Item>();

    protected final Map<String, Item> newItems = new LinkedHashMap<String, Item>();

    protected final Map<String, Item> mergedItems = new LinkedHashMap<String, Item>();

    private boolean keyCollision;

    public ListPreferenceHandler(URI key, String itemPattern, String keyPattern, String separator)
    {
      super(key);
      this.itemPattern = Pattern.compile(itemPattern);
      this.keyPattern = Pattern.compile(keyPattern);
      this.separator = separator;
    }

    @Override
    protected boolean isExcluded()
    {
      return false;
    }

    @Override
    protected boolean isNeeded()
    {
      return super.isNeeded() && !keyCollision;
    }

    @Override
    public String merge()
    {
      keyCollision = false;
      changedItems.clear();
      oldItems.clear();
      newItems.clear();
      mergedItems.clear();

      oldItems.putAll(getItems(oldValue));
      newItems.putAll(getItems(newValue));
      if (oldItems.isEmpty())
      {
        changedItems.addAll(newItems.keySet());
        mergedItems.putAll(newItems);
        return newValue;
      }

      mergedItems.putAll(oldItems);
      for (Map.Entry<String, Item> entry : newItems.entrySet())
      {
        String entryKey = entry.getKey();
        Item newItem = entry.getValue();
        Item oldItem = mergedItems.put(entryKey, newItem);
        if (oldItem == null || !oldItem.getValue().equals(newItem.getValue()))
        {
          changedItems.add(entryKey);
        }
      }

      List<Item> oldItemValues = new ArrayList<Item>(oldItems.values());
      StringBuilder result = new StringBuilder(oldItemValues.get(0).getHead());
      for (Iterator<Item> it = mergedItems.values().iterator(); it.hasNext();)
      {
        Item item = it.next();
        result.append(item.getValue());
        if (it.hasNext())
        {
          result.append(separator);
        }
      }

      result.append(oldItemValues.get(oldItemValues.size() - 1).getTail());

      return result.toString();
    }

    @Override
    public String delta()
    {
      merge();

      if (oldItems.isEmpty())
      {
        return newValue;
      }

      List<Item> itemValues = new ArrayList<Item>(oldItems.values());
      StringBuilder result = new StringBuilder(itemValues.get(0).getHead());
      for (Iterator<String> it = changedItems.iterator(); it.hasNext();)
      {
        Item item = mergedItems.get(it.next());
        result.append(item.getValue());
        if (it.hasNext())
        {
          result.append(separator);
        }
      }

      result.append(itemValues.get(itemValues.size() - 1).getTail());

      return result.toString();
    }

    private Map<String, Item> getItems(String value)
    {
      Map<String, Item> items = new LinkedHashMap<String, Item>();
      if (value != null)
      {
        for (Matcher matcher = itemPattern.matcher(value); matcher.find();)
        {
          String group = matcher.group(1);
          Matcher keyMatcher = keyPattern.matcher(group);
          if (keyMatcher.find())
          {
            StringBuilder key = new StringBuilder();
            for (int i = 1, count = keyMatcher.groupCount(); i <= count; ++i)
            {
              if (i > 1)
              {
                key.append("_._");
              }

              key.append(keyMatcher.group(i));
            }

            Item oldItem = items.put(key.toString(), new Item(value, matcher.start(1), matcher.end(1)));
            if (oldItem != null)
            {
              keyCollision = true;
            }
          }
        }
      }

      return items;
    }

    /**
     * @author Ed Merks
     */
    public static final class Item
    {
      private final String value;

      private final int start;

      private final int end;

      public Item(String value, int start, int end)
      {
        this.value = value;
        this.start = start;
        this.end = end;
      }

      public String getValue()
      {
        return value.substring(start, end);
      }

      public String getHead()
      {
        return value.substring(0, start);
      }

      public String getTail()
      {
        return value.substring(end);
      }

      @Override
      public String toString()
      {
        return getValue();
      }
    }
  }

  /**
   * @author Ed Merks
   */
  public static class XMLPreferenceHandler extends ListPreferenceHandler
  {
    public XMLPreferenceHandler(URI key, String tag, String[] requiredAttributes, String[] optionalAttributes)
    {
      super(key, getItemPattern(tag), getKeyPattern(requiredAttributes, optionalAttributes), "\n");
    }

    private static String getItemPattern(String tag)
    {
      return "(?s)(<" + tag + " (?:[^>]*/>|.*?</" + tag + ">))";
    }

    private static String getKeyPattern(String[] requiredAttributes, String[] optionalAttributes)
    {
      Set<String> attributes = new TreeSet<String>(Arrays.asList(requiredAttributes));
      List<String> optional = optionalAttributes == null ? Collections.<String> emptyList() : Arrays.asList(optionalAttributes);
      attributes.addAll(optional);

      StringBuilder result = new StringBuilder();
      for (String attribute : attributes)
      {
        int length = result.length();

        boolean isOptional = optional.contains(attribute);
        if (isOptional)
        {
          result.append("(?:");
        }

        if (length != 0)
        {
          result.append("[^>]+");
        }

        result.append(attribute);
        result.append("=\"([^\"]+)\"");

        if (isOptional)
        {
          result.append(")?");
        }
      }

      return result.toString();
    }
  }

  /**
   * @author Ed Merks
   */
  public static class TeamFileModePreferenceHandler extends ListPreferenceHandler
  {
    protected static final Set<String> DEFAULT_NAME_MAPPINGS = new LinkedHashSet<String>();

    protected static final Set<String> DEFAULT_EXTENSION_MAPPINGS = new LinkedHashSet<String>();

    protected static final Object FILE_CONTENT_MANAGER;

    protected boolean isExtension;

    static
    {
      Object fileContentManager = null;
      try
      {
        Class<?> teamClass = CommonPlugin.loadClass("org.eclipse.team.core", "org.eclipse.team.core.Team");
        fileContentManager = ReflectUtil.invokeMethod("getFileContentManager", teamClass);
        Object[] defaultNameMappings = (Object[])ReflectUtil.invokeMethod("getDefaultNameMappings", fileContentManager);
        populateMappings(DEFAULT_NAME_MAPPINGS, defaultNameMappings);
        Object[] defaultExtensionMappings = (Object[])ReflectUtil.invokeMethod("getDefaultExtensionMappings", fileContentManager);
        populateMappings(DEFAULT_EXTENSION_MAPPINGS, defaultExtensionMappings);
      }
      catch (Throwable throwable)
      {
        //$FALL-THROUGH$
      }

      FILE_CONTENT_MANAGER = fileContentManager;
    }

    public TeamFileModePreferenceHandler(URI key)
    {
      super(key, "(?s)([^\\n]+\\n[0-9]+)", "([^\n]+)", "\n");
      isExtension = "file_types".equals(key.lastSegment());
    }

    @Override
    public String delta()
    {
      if (StringUtil.isEmpty(oldValue))
      {
        StringBuilder result = new StringBuilder(super.delta());
        for (String mapping : isExtension ? DEFAULT_EXTENSION_MAPPINGS : DEFAULT_NAME_MAPPINGS)
        {
          int index = result.indexOf(mapping);
          if (index == 0 || index > 0 && result.charAt(index - 1) == '\n')
          {
            result.delete(index, index + mapping.length());
          }
        }

        return result.toString();
      }

      return super.delta();
    }

    @Override
    public void apply(SetupTaskContext context)
    {
      if (FILE_CONTENT_MANAGER != null)
      {
        int size = changedItems.size();
        String[] names = new String[size];
        int[] types = new int[size];
        int index = 0;
        for (String changedItem : changedItems)
        {
          names[index] = changedItem;
          String value = mergedItems.get(changedItem).getValue();
          int offset = value.indexOf('\n');
          types[index] = Integer.parseInt(value.substring(offset + 1));
          ++index;
        }

        ReflectUtil.invokeMethod(
            ReflectUtil.getMethod(FILE_CONTENT_MANAGER.getClass(), isExtension ? "addExtensionMappings" : "addNameMappings", String[].class, int[].class),
            FILE_CONTENT_MANAGER, names, types);
      }
    }

    private static void populateMappings(Set<String> target, Object[] mappings)
    {
      for (Object mapping : mappings)
      {
        Object string = ReflectUtil.invokeMethod("getString", mapping);
        int type = (Integer)ReflectUtil.invokeMethod("getType", mapping);
        target.add(string + "\n" + type + "\n");
      }
    }
  }

  /**
   * @author Ed Merks
   */
  public static class ContentTypeFileExtensionPreferenceHandler extends ListPreferenceHandler
  {
    private final int type;

    public ContentTypeFileExtensionPreferenceHandler(URI key)
    {
      super(key, "([^,]+)", "([^,]+)", ",");
      type = "file-extensions".equals(key.lastSegment()) ? IContentType.FILE_EXTENSION_SPEC : IContentType.FILE_NAME_SPEC;
    }

    @Override
    public void apply(SetupTaskContext context)
    {
      String contentTypeName = key.segment(key.segmentCount() - 2);
      IContentType contentType = Platform.getContentTypeManager().getContentType(contentTypeName);
      if (contentType != null)
      {
        for (String key : changedItems)
        {
          try
          {
            contentType.addFileSpec(key, type);
          }
          catch (CoreException ex)
          {
            context.log(ex);
          }
        }
      }
    }
  }

  /**
   * @author Ed Merks
   */
  public static class ContentTypeCharsetPreferenceHandler extends PreferenceHandler
  {
    public ContentTypeCharsetPreferenceHandler(URI key)
    {
      super(key);
    }

    @Override
    public void apply(SetupTaskContext context)
    {
      String contentTypeName = key.segment(key.segmentCount() - 2);
      IContentType contentType = Platform.getContentTypeManager().getContentType(contentTypeName);
      if (contentType != null)
      {
        try
        {
          contentType.setDefaultCharset(newValue);
        }
        catch (CoreException ex)
        {
          context.log(ex);
        }
      }
    }
  }

  /**
   * @author Ed Merks
   */
  public static abstract class ProfileChoicePreferenceHandler extends PreferenceHandler
  {
    private static final Pattern SETTING_PATTERN = Pattern.compile("<setting id=\"([^\"]+)\" value=\"([^\"]+)\"");

    public ProfileChoicePreferenceHandler(URI key)
    {
      super(key);
    }

    @Override
    public int getPriority()
    {
      return PRIORITY_EARLY + 1;
    }

    protected abstract PreferenceProperty getProfilesPreferenceProperty();

    protected abstract PreferenceProperty getKeyPreferenceProperty(String propertyKey);

    @Override
    public void apply(SetupTaskContext context)
    {
      PreferenceProperty profiles = getProfilesPreferenceProperty();
      Pattern pattern = Pattern.compile("(?s)(<profile[^\n]*name=\"" + newValue.substring(1) + "\".*?</profile>)");
      String value = profiles.get(null);
      if (value != null)
      {
        Matcher matcher = pattern.matcher(value);
        if (matcher.find())
        {
          String profile = matcher.group(1);
          for (Matcher settingMatcher = SETTING_PATTERN.matcher(profile); settingMatcher.find();)
          {
            String propertyKey = settingMatcher.group(1);
            String propertyValue = settingMatcher.group(2);
            PreferenceProperty property = getKeyPreferenceProperty(propertyKey);
            property.set(propertyValue);
          }
        }
      }
    }
  }

  public static class JDTProfileChoicePreferenceHandler extends ProfileChoicePreferenceHandler
  {
    private String profileType;

    public JDTProfileChoicePreferenceHandler(URI key, String profileType)
    {
      super(key);
      this.profileType = profileType;
    }

    @Override
    protected PreferenceProperty getProfilesPreferenceProperty()
    {
      return new PreferencesUtil.PreferenceProperty("/instance/org.eclipse.jdt.ui/org.eclipse.jdt.ui." + profileType + "profiles");
    }

    @Override
    protected PreferenceProperty getKeyPreferenceProperty(String propertyKey)
    {
      return new PreferencesUtil.PreferenceProperty("/instance/org.eclipse.jdt.core/" + propertyKey);
    }
  }

  public static class CDTProfileChoicePreferenceHandler extends ProfileChoicePreferenceHandler
  {
    public CDTProfileChoicePreferenceHandler(URI key)
    {
      super(key);
    }

    @Override
    protected PreferenceProperty getProfilesPreferenceProperty()
    {
      return new PreferencesUtil.PreferenceProperty("/instance/org.eclipse.cdt.ui/org.eclipse.cdt.ui.formatterprofiles");
    }

    @Override
    protected PreferenceProperty getKeyPreferenceProperty(String propertyKey)
    {
      return new PreferencesUtil.PreferenceProperty("/instance/org.eclipse.cdt.core/" + propertyKey);
    }
  }

  /**
   * @author Ed Merks
   */
  public static class PreferenceHandlerFactoryRegistry extends BasicEMap<URI, PreferenceHandler.Factory> implements PreferenceHandler.Factory.Registry
  {
    private static final URI ROOT_PREFIX = URI.createURI("//");

    private static final long serialVersionUID = 1L;

    protected BasicEList<List<Map.Entry<URI, PreferenceHandler.Factory>>> prefixMaps = new BasicEList<List<Map.Entry<URI, PreferenceHandler.Factory>>>();

    public PreferenceHandlerFactoryRegistry()
    {
      super();
    }

    @Override
    protected Entry<URI, PreferenceHandler.Factory> newEntry(int hash, URI key, PreferenceHandler.Factory value)
    {
      validateKey(key);
      validateValue(value);
      return new MappingEntryImpl(hash, key, value);
    }

    /**
     * @author Ed Merks
     */
    protected class MappingEntryImpl extends EntryImpl
    {
      public boolean isPrefixMapEntry;

      public MappingEntryImpl(int hash, URI key, PreferenceHandler.Factory value)
      {
        super(hash, key, value);
        determineEntryType();
      }

      public void determineEntryType()
      {
        isPrefixMapEntry = "".equals(key.authority()) ? key.segmentCount() == 0
            : key.segmentCount() == 0 ? key.hasAbsolutePath() : key.hasTrailingPathSeparator();
      }
    }

    public PreferenceHandler.Factory getFactory(URI uri)
    {
      PreferenceHandler.Factory result = get(uri);
      if (result == null)
      {
        if (prefixMaps != null)
        {
          for (int i = Math.min(prefixMaps.size() - 1, uri.segmentCount()); i >= 0; --i)
          {
            List<Map.Entry<URI, PreferenceHandler.Factory>> prefixes = prefixMaps.get(i);
            LOOP: for (int j = prefixes.size() - 1; j >= 0; --j)
            {
              Map.Entry<URI, PreferenceHandler.Factory> entry = prefixes.get(j);
              URI key = entry.getKey();
              if (ROOT_PREFIX.equals(key))
              {
                return entry.getValue();
              }

              for (int k = key.segmentCount() - 2; k >= 0; --k)
              {
                if (!key.segment(k).equals(uri.segment(k)))
                {
                  continue LOOP;
                }
              }

              if (key.authority().equals(uri.authority()))
              {
                return entry.getValue();
              }
            }
          }
        }
      }

      return result;
    }

    @Override
    protected void validateKey(URI key)
    {
      if (key == null)
      {
        throw new RuntimeException("Key may not be null");
      }

      if (!key.isHierarchical())
      {
        throw new RuntimeException("Key is not hierarchical " + key);
      }

      if (key.authority() == null)
      {
        throw new RuntimeException("Key has no authority " + key);
      }

      if (key.scheme() != null)
      {
        throw new RuntimeException("Key should not have a scheme " + key);
      }
    }

    @Override
    protected void validateValue(Factory value)
    {
      if (value == null)
      {
        throw new RuntimeException("Value may not be null");
      }
    }

    @Override
    protected void didAdd(Entry<URI, PreferenceHandler.Factory> entry)
    {
      if (((MappingEntryImpl)entry).isPrefixMapEntry)
      {
        int length = entry.getKey().segmentCount();
        if (prefixMaps == null)
        {
          prefixMaps = new BasicEList<List<Map.Entry<URI, PreferenceHandler.Factory>>>();
        }

        for (int i = prefixMaps.size() - 1; i <= length; ++i)
        {
          prefixMaps.add(new BasicEList<Map.Entry<URI, PreferenceHandler.Factory>>());
        }

        prefixMaps.get(length).add(entry);
      }
    }

    @Override
    protected void didModify(Entry<URI, PreferenceHandler.Factory> entry, PreferenceHandler.Factory oldValue)
    {
      didRemove(entry);
      ((MappingEntryImpl)entry).determineEntryType();
      didAdd(entry);
    }

    @Override
    protected void didRemove(Entry<URI, PreferenceHandler.Factory> entry)
    {
      if (((MappingEntryImpl)entry).isPrefixMapEntry)
      {
        int length = entry.getKey().segmentCount();
        prefixMaps.get(length).remove(entry);
      }
    }

    @Override
    protected void didClear(BasicEList<Entry<URI, PreferenceHandler.Factory>>[] oldEntryData)
    {
      prefixMaps = null;
    }
  }
} // PreferenceTaskImpl
