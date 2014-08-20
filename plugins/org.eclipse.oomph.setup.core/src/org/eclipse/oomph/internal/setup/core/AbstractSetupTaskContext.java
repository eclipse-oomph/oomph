/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Ericsson AB (Julian Enoch) - Bug 425815 - Add support for secure context variables
 *    Ericsson AB (Julian Enoch) - Bug 434512 - Disable prompt for master password recovery information
 */
package org.eclipse.oomph.internal.setup.core;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.impl.InstallationTaskImpl;
import org.eclipse.oomph.setup.util.OS;
import org.eclipse.oomph.setup.util.StringExpander;
import org.eclipse.oomph.util.OfflineMode;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.OperationCanceledException;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public abstract class AbstractSetupTaskContext extends StringExpander implements SetupTaskContext, SetupProperties
{
  private static final String packageNameShouldBe = "!!!!!!!!!!!! org.eclipse.oomph.setup.internal.core !!!!!!!!!!";

  private static final Map<String, StringFilter> STRING_FILTER_REGISTRY = new HashMap<String, StringFilter>();

  private SetupPrompter prompter;

  private Trigger trigger;

  private SetupContext setupContext;

  private boolean performing;

  private boolean offline;

  private boolean mirrors;

  private Set<String> restartReasons = new LinkedHashSet<String>();

  private URIConverter uriConverter;

  private Map<Object, Object> map = new LinkedHashMap<Object, Object>();

  protected AbstractSetupTaskContext(URIConverter uriConverter, SetupPrompter prompter, Trigger trigger, SetupContext setupContext)
  {
    this.uriConverter = uriConverter;
    this.prompter = prompter;
    this.trigger = trigger;

    initialize(setupContext);
  }

  private void initialize(SetupContext setupContext)
  {
    setSetupContext(setupContext);

    // put("os", Platform.getOS());
    // put("os.arch", Platform.getOSArch());
    // put("ws", Platform.getWS());

    for (Map.Entry<String, String> entry : System.getenv().entrySet())
    {
      put(entry.getKey(), entry.getValue());
    }

    for (Map.Entry<Object, Object> entry : System.getProperties().entrySet())
    {
      put(entry.getKey(), entry.getValue());
    }

    // Do this late because \ is replaced by / when looking at this property.
    put(PROP_UPDATE_URL, SetupCorePlugin.UPDATE_URL);
  }

  public Map<Object, Object> getMap()
  {
    return map;
  }

  public SetupPrompter getPrompter()
  {
    return prompter;
  }

  public void setPrompter(SetupPrompter prompter)
  {
    this.prompter = prompter;
  }

  public Trigger getTrigger()
  {
    return trigger;
  }

  public void checkCancelation()
  {
    if (isCanceled())
    {
      throw new OperationCanceledException();
    }
  }

  public boolean isOffline()
  {
    return OfflineMode.isEnabled();
  }

  public void setOffline(boolean offline)
  {
    OfflineMode.setEnabled(offline);
  }

  public boolean isMirrors()
  {
    return mirrors;
  }

  public void setMirrors(boolean mirrors)
  {
    this.mirrors = mirrors;
  }

  public boolean isPerforming()
  {
    return performing;
  }

  public boolean isRestartNeeded()
  {
    return !restartReasons.isEmpty();
  }

  public void setRestartNeeded(String reason)
  {
    restartReasons.add(reason);
  }

  public Set<String> getRestartReasons()
  {
    return restartReasons;
  }

  public URI redirect(URI uri)
  {
    if (uri == null)
    {
      return null;
    }

    return getURIConverter().normalize(uri);
  }

  public String redirect(String uri)
  {
    if (StringUtil.isEmpty(uri))
    {
      return null;
    }

    return redirect(URI.createURI(uri)).toString();
  }

  public URIConverter getURIConverter()
  {
    return uriConverter;
  }

  public OS getOS()
  {
    return OS.INSTANCE;
  }

  public File getProductLocation()
  {
    File installationLocation = getInstallationLocation();
    if (installationLocation == null)
    {
      return null;
    }

    return new File(installationLocation, getOS().getEclipseDir());
  }

  public File getProductConfigurationLocation()
  {
    File productLocation = getProductLocation();
    if (productLocation == null)
    {
      return null;
    }

    return new File(productLocation, InstallationTaskImpl.CONFIGURATION_FOLDER_NAME);
  }

  public Workspace getWorkspace()
  {
    return setupContext.getWorkspace();
  }

  public SetupContext getSetupContext()
  {
    return setupContext;
  }

  protected final void setSetupContext(SetupContext setupContext)
  {
    this.setupContext = setupContext;
  }

  public User getUser()
  {
    return setupContext.getUser();
  }

  public Installation getInstallation()
  {
    return setupContext.getInstallation();
  }

  protected final void setPerforming(boolean performing)
  {
    this.performing = performing;
  }

  public Object get(Object key)
  {
    Object value = map.get(key);
    if (value == null && key instanceof String)
    {
      String name = (String)key;
      if (name.indexOf('.') != -1)
      {
        name = name.replace('.', '_');
        value = map.get(name);
      }
    }

    return value;
  }

  public Object put(Object key, Object value)
  {
    return map.put(key, value);
  }

  public Set<Object> keySet()
  {
    return map.keySet();
  }

  protected String lookup(String key)
  {
    Object object = get(key);
    if (object != null)
    {
      return object.toString();
    }

    return null;
  }

  @Override
  protected String filter(String value, String filterName)
  {
    StringFilter filter = STRING_FILTER_REGISTRY.get(filterName);
    if (filter != null)
    {
      return filter.filter(value);
    }

    return value;
  }

  static
  {
    STRING_FILTER_REGISTRY.put("uri", new StringFilter()
    {
      public String filter(String value)
      {
        return URI.createFileURI(value).toString();
      }
    });

    STRING_FILTER_REGISTRY.put("uriLastSegment", new StringFilter()
    {
      public String filter(String value)
      {
        URI uri = URI.createURI(value);
        if (!uri.isHierarchical())
        {
          uri = URI.createURI(uri.opaquePart());
        }

        return URI.decode(uri.lastSegment());
      }
    });

    STRING_FILTER_REGISTRY.put("preferenceNode", new StringFilter()
    {
      public String filter(String value)
      {
        return value.replaceAll("/", "\\\\2f");
      }
    });

    STRING_FILTER_REGISTRY.put("upper", new StringFilter()
    {
      public String filter(String value)
      {
        return value.toUpperCase();
      }
    });

    STRING_FILTER_REGISTRY.put("lower", new StringFilter()
    {
      public String filter(String value)
      {
        return value.toLowerCase();
      }
    });

    STRING_FILTER_REGISTRY.put("cap", new StringFilter()
    {
      public String filter(String value)
      {
        return StringUtil.cap(value);
      }
    });

    STRING_FILTER_REGISTRY.put("allCap", new StringFilter()
    {
      public String filter(String value)
      {
        return StringUtil.capAll(value);
      }
    });

    STRING_FILTER_REGISTRY.put("property", new StringFilter()
    {
      public String filter(String value)
      {
        return value.replaceAll("\\\\", "\\\\\\\\");
      }
    });

    STRING_FILTER_REGISTRY.put("path", new StringFilter()
    {
      public String filter(String value)
      {
        return value.replaceAll("\\\\", "/");
      }
    });
  }

  /**
   * @author Eike Stepper
   */
  public interface StringFilter
  {
    public String filter(String value);
  }

}
