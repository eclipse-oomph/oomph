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
package org.eclipse.oomph.setup.internal.core;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.impl.InstallationTaskImpl;
import org.eclipse.oomph.setup.util.StringExpander;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.OfflineMode;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ITouchpointData;
import org.eclipse.equinox.p2.metadata.ITouchpointInstruction;
import org.eclipse.equinox.p2.query.QueryUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public abstract class AbstractSetupTaskContext extends StringExpander implements SetupTaskContext, SetupProperties
{
  private static final Map<String, StringFilter> STRING_FILTER_REGISTRY = new HashMap<String, StringFilter>();

  private static final Pattern setLauncherNamePattern = Pattern.compile("setLauncherName\\(name:([^)]*)\\)");

  private SetupPrompter prompter;

  private Trigger trigger;

  private SetupContext setupContext;

  private boolean performing;

  private boolean mirrors = true;

  private Set<String> restartReasons = new LinkedHashSet<String>();

  private URIConverter uriConverter;

  private Map<Object, Object> map = new LinkedHashMap<Object, Object>();

  private String launcherName;

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

    for (Map.Entry<String, String> entry : CONTROL_CHARACTER_VALUES.entrySet())
    {
      put(entry.getKey(), entry.getValue());
    }
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
    // Make sure to change this plugin (so that the build qualifier is incremented) when the return type of OfflineMode.setEnabled() changes.
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
    if (!StringUtil.isEmpty(uri))
    {
      try
      {
        return redirect(URI.createURI(uri)).toString();
      }
      catch (RuntimeException ex)
      {
        // Ignore.
      }
    }

    return uri;
  }

  public URIConverter getURIConverter()
  {
    return uriConverter;
  }

  public OS getOS()
  {
    OS os = (OS)get(OS.class);
    if (os != null)
    {
      return os;
    }

    return OS.INSTANCE;
  }

  public File getProductLocation()
  {
    File installationLocation = getInstallationLocation();
    if (installationLocation == null)
    {
      return null;
    }

    String relativeProductFolder = getRelativeProductFolder();
    return new File(installationLocation, relativeProductFolder);
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

  public String getRelativeProductFolder()
  {
    String productFolderName = getProductFolderName();
    return getOS().getRelativeProductFolder(productFolderName);
  }

  private String getProductFolderName()
  {
    Installation installation = getInstallation();
    ProductVersion productVersion = installation.getProductVersion();

    OS os = getOS();
    String osgiOS = os.getOsgiOS();
    String osgiWS = os.getOsgiWS();
    String osgiArch = os.getOsgiArch();
    String[] keys = new String[] { //
    "folderName." + osgiOS + '.' + osgiWS + '.' + osgiArch, //
        "folderName." + osgiOS + '.' + osgiWS, //
        "folderName." + osgiOS, //
        "folderName", //
    };

    return getProductFolderName(productVersion, keys);
  }

  private String getProductFolderName(Scope scope, String[] keys)
  {
    if (scope == null)
    {
      return "";
    }

    Annotation annotation = scope.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
    if (annotation != null)
    {
      EMap<String, String> details = annotation.getDetails();

      for (String key : keys)
      {
        String folderName = details.get(key);
        if (folderName != null)
        {
          return folderName;
        }
      }
    }

    return getProductFolderName(scope.getParentScope(), keys);
  }

  public String getLauncherName()
  {
    if (launcherName == null)
    {
      IProfile profile = (IProfile)get(IProfile.class);
      if (profile == null)
      {
        profile = P2Util.getAgentManager().getCurrentAgent().getCurrentProfile();
      }

      launcherName = getLauncherName(profile);
    }

    return launcherName;
  }

  private static String getLauncherName(IProfile profile)
  {
    for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUAnyQuery(), null)))
    {
      Collection<ITouchpointData> touchpointDatas = iu.getTouchpointData();
      if (touchpointDatas != null)
      {
        for (ITouchpointData touchpointData : touchpointDatas)
        {
          ITouchpointInstruction instruction = touchpointData.getInstruction("configure");
          if (instruction != null)
          {
            String body = instruction.getBody();
            if (body != null)
            {
              Matcher matcher = setLauncherNamePattern.matcher(body);
              if (matcher.matches())
              {
                return matcher.group(1);
              }
            }
          }
        }
      }
    }

    SetupCorePlugin.INSTANCE.log("Could not determine the launcher name from " + profile.getProfileId(), IStatus.ERROR);

    return "eclipse";
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
    StringFilter filter = STRING_FILTER_REGISTRY.get(filterName.toLowerCase());
    if (filter != null)
    {
      return filter.filter(value);
    }

    return value;
  }

  private static void registerFilter(String filterName, StringFilter filter)
  {
    STRING_FILTER_REGISTRY.put(filterName.toLowerCase(), filter);
  }

  static
  {
    registerFilter("uri", new StringFilter()
    {
      public String filter(String value)
      {
        return URI.createFileURI(value).toString();
      }
    });

    registerFilter("uriLastSegment", new StringFilter()
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

    registerFilter("gitRepository", new StringFilter()
    {
      public String filter(String value)
      {
        URI uri = URI.createURI(value);
        if (!uri.isHierarchical())
        {
          uri = URI.createURI(uri.opaquePart());
        }

        String result = URI.decode(uri.lastSegment());
        if (result.endsWith(".git"))
        {
          result = result.substring(0, result.length() - 4);
        }

        return result;
      }
    });

    registerFilter("username", new StringFilter()
    {
      public String filter(String value)
      {
        return URI.encodeSegment(value, false).replace("@", "%40");
      }
    });

    registerFilter("canonical", new StringFilter()
    {
      public String filter(String value)
      {
        // Don't canonicalize the value if it contains a unexpanded variable reference.
        if (STRING_EXPANSION_PATTERN.matcher(value).find())
        {
          return value;
        }

        File file = new File(value).getAbsoluteFile();
        try
        {
          return file.getCanonicalPath();
        }
        catch (IOException ex)
        {
          return file.toString();
        }
      }
    });

    registerFilter("preferenceNode", new StringFilter()
    {
      public String filter(String value)
      {
        return value.replaceAll("/", "\\\\2f");
      }
    });

    registerFilter("upper", new StringFilter()
    {
      public String filter(String value)
      {
        return value.toUpperCase();
      }
    });

    registerFilter("lower", new StringFilter()
    {
      public String filter(String value)
      {
        return value.toLowerCase();
      }
    });

    registerFilter("cap", new StringFilter()
    {
      public String filter(String value)
      {
        return StringUtil.cap(value);
      }
    });

    registerFilter("allCap", new StringFilter()
    {
      public String filter(String value)
      {
        return StringUtil.capAll(value);
      }
    });

    registerFilter("property", new StringFilter()
    {
      public String filter(String value)
      {
        return value.replaceAll("\\\\", "\\\\\\\\");
      }
    });

    registerFilter("path", new StringFilter()
    {
      public String filter(String value)
      {
        return value.replaceAll("\\\\", "/");
      }
    });

    registerFilter("basePath", new StringFilter()
    {
      public String filter(String value)
      {
        value = value.replaceAll("\\\\", "/");
        int pos = value.lastIndexOf('/');
        if (pos == -1)
        {
          return "";
        }

        return value.substring(0, pos);
      }
    });

    registerFilter("lastSegment", new StringFilter()
    {
      public String filter(String value)
      {
        int pos = Math.max(value.lastIndexOf('/'), value.lastIndexOf('\\'));
        if (pos == -1)
        {
          return value;
        }

        return value.substring(pos + 1);
      }
    });

    registerFilter("fileExtension", new StringFilter()
    {
      public String filter(String value)
      {
        int pos = value.lastIndexOf('.');
        if (pos == -1)
        {
          return "";
        }

        return value.substring(pos + 1);
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
