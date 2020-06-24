/*
 * Copyright (c) 2019 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.urischeme.IOperatingSystemRegistration;
import org.eclipse.urischeme.IScheme;
import org.eclipse.urischeme.ISchemeInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class URISchemeUtil
{
  private static final Preference PREF_WEB_LINKS = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("web-links"); //$NON-NLS-1$

  private static final IOperatingSystemRegistration OPERATING_SYSTEM_REGISTRATION = ReflectUtil.invokeMethod("getInstance", IOperatingSystemRegistration.class); //$NON-NLS-1$

  private static final String INSTALLER_SCHEME = PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_INSTALLER_URI_SCHEME + "installer", "eclipse+installer"); //$NON-NLS-1$ //$NON-NLS-2$

  private static final String MARKETPLACE_SCHEME = PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_INSTALLER_URI_SCHEME + "mpc", "?"); //$NON-NLS-1$ //$NON-NLS-2$

  private static final String FAKE_ECLIPSE_HOME = PropertiesUtil.getProperty("oomph.setup.installer.uri.scheme.eclipse.home", null); //$NON-NLS-1$

  private static final Collection<IScheme> SCHEMES;

  static
  {
    Collection<IScheme> schemes = new ArrayList<IScheme>();
    for (final String scheme : new String[] { INSTALLER_SCHEME, MARKETPLACE_SCHEME })
    {
      if (URI.validScheme(scheme))
      {
        schemes.add(new IScheme()
        {
          public String getName()
          {
            return scheme;
          }

          public String getDescription()
          {
            return scheme;
          }
        });
      }
    }

    SCHEMES = schemes;
  }

  private URISchemeUtil()
  {
  }

  /**
   * Converts the argument to a URI, resolving the URI if it's one of the registered web link schemes.
   * It first tries to interpret the argument as a file system file, checking if that is really a file that can be read;
   * in that case it will yield a file: URI.
   * Otherwise it's interpreted as a URI, except on Windows if the result has a scheme with a single letter, in which case
   * it will also yield a file: URI.
   * If the URI uses a web link scheme, the URI is resolved to yield a normalized URI that can be used directly as normal.
   */
  public static URI getResourceURI(String argument)
  {
    try
    {
      File file = new File(argument);
      if (file.isFile() && file.canRead())
      {
        return URI.createFileURI(IOUtil.getCanonicalFile(file).toString());
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    URI uri = URI.createURI(argument);
    String scheme = uri.scheme();
    if (scheme == null || OS.INSTANCE.isWin() && scheme.length() == 1)
    {
      uri = URI.createFileURI(IOUtil.getCanonicalFile(new File(argument)).toString());
    }

    if (INSTALLER_SCHEME.equals(uri.scheme()) && uri.opaquePart() != null)
    {
      uri = URI.createURI(uri.opaquePart()).appendQuery(uri.query()).appendFragment(uri.fragment());
    }

    return uri;
  }

  /**
   * Returns this installer's launcher, or the empty string.
   * The latter is always yielded for a debug launch.
   */
  public static String getSelfLauncher()
  {
    Runnable restore = setEclipseHome(FAKE_ECLIPSE_HOME);
    String eclipseLauncher = OPERATING_SYSTEM_REGISTRATION.getEclipseLauncher();
    if (restore != null)
    {
      restore.run();
    }
    return StringUtil.isEmpty(eclipseLauncher) || eclipseLauncher.startsWith("file:") ? "" : eclipseLauncher; //$NON-NLS-1$ //$NON-NLS-2$
  }

  private static Runnable setEclipseHome(String eclipseHome)
  {
    if (!StringUtil.isEmpty(eclipseHome))
    {
      final String originalHomeLocation = System.getProperty("eclipse.home.location"); //$NON-NLS-1$
      System.setProperty("eclipse.home.location", eclipseHome); //$NON-NLS-1$
      return new Runnable()
      {
        public void run()
        {
          System.setProperty("eclipse.home.location", originalHomeLocation); //$NON-NLS-1$
        }
      };
    }

    return null;
  }

  /**
   * Registers this installer for any scheme that does not have a conflicting registration.
   */
  public static void registerDefault()
  {
    boolean isSet = PREF_WEB_LINKS.get((String)null) != null;
    if (!isSet && canRegister())
    {
      Map<String, String> conflictingRegistrations = getConflictingRegistrations();
      for (IScheme scheme : SCHEMES)
      {
        String schemeName = scheme.getName();
        if (!conflictingRegistrations.containsKey(schemeName))
        {
          setRegistrations(Collections.singletonMap(schemeName, getSelfLauncher()));
        }
      }
    }
  }

  /**
   * Returns true only if the registry is accessible, if the self launcher is known, and the installer is one at a permanent disk location.
   */
  private static boolean canRegister()
  {
    try
    {
      OPERATING_SYSTEM_REGISTRATION.getSchemesInformation(SCHEMES);
    }
    catch (Exception ex)
    {
      return false;
    }

    return !StringUtil.isEmpty(getSelfLauncher()) && !KeepInstallerUtil.canKeepInstaller();
  }

  /**
   * Returns true if there is at least one scheme is actually registered for this installer.
   */
  public static boolean isRegistered()
  {
    String eclipseLauncher = getSelfLauncher();
    if (StringUtil.isEmpty(eclipseLauncher))
    {
      return false;
    }

    try
    {
      List<ISchemeInformation> schemesInformation = OPERATING_SYSTEM_REGISTRATION.getSchemesInformation(SCHEMES);
      for (ISchemeInformation schemeInformation : schemesInformation)
      {
        String handlerInstanceLocation = schemeInformation.getHandlerInstanceLocation();
        if (eclipseLauncher.equals(handlerInstanceLocation))
        {
          return true;
        }
      }
    }
    catch (Exception ex)
    {
    }

    return false;
  }

  /**
   * Updates the specified registrations in the system registry.
   */
  public static void setRegistrations(Map<String, String> newRegistrations)
  {
    Map<String, String> existingRegistrations = getRegistrations();
    for (Map.Entry<String, String> entry : newRegistrations.entrySet())
    {
      String schemeRegistration = entry.getKey();
      String existingLauncher = existingRegistrations.get(schemeRegistration);
      String newLauncher = entry.getValue();
      if (!newLauncher.equals(existingLauncher))
      {
        for (IScheme scheme : SCHEMES)
        {
          if (scheme.getName().equals(schemeRegistration))
          {
            try
            {
              if (OS.INSTANCE.isMac())
              {
                // The Mac implementation refuses unregister a conflicting registration so force it.
                Runnable restore = setEclipseHome(existingLauncher);
                try
                {
                  OPERATING_SYSTEM_REGISTRATION.handleSchemes(Collections.<IScheme> emptyList(), Collections.singleton(scheme));
                }
                catch (Exception ex)
                {
                  SetupInstallerPlugin.INSTANCE.log(ex);
                }
                finally
                {
                  if (restore != null)
                  {
                    restore.run();
                  }
                }
              }
              else
              {
                OPERATING_SYSTEM_REGISTRATION.handleSchemes(Collections.<IScheme> emptyList(), Collections.singleton(scheme));
              }

              if (!StringUtil.isEmpty(newLauncher))
              {
                Runnable restore = setEclipseHome(FAKE_ECLIPSE_HOME);
                OPERATING_SYSTEM_REGISTRATION.handleSchemes(Collections.singleton(scheme), Collections.<IScheme> emptyList());
                if (restore != null)
                {
                  restore.run();
                }
              }
            }
            catch (Exception ex)
            {
              SetupInstallerPlugin.INSTANCE.log(ex);
            }

            break;
          }
        }
      }
    }
  }

  /**
   * Registers or unregisters this installer's registrations and remembers this state in the web link preference.
   */
  public static void setRegistered(boolean registered)
  {
    String eclipseLauncher = getSelfLauncher();
    if (!StringUtil.isEmpty(eclipseLauncher))
    {
      try
      {
        if (!registered)
        {
          Set<String> selfRegistrations = getSelfRegistrations();
          for (IScheme scheme : SCHEMES)
          {
            if (selfRegistrations.contains(scheme.getName()))
            {
              OPERATING_SYSTEM_REGISTRATION.handleSchemes(Collections.<IScheme> emptyList(), Collections.singleton(scheme));
            }
          }
        }

        if (registered)
        {
          OPERATING_SYSTEM_REGISTRATION.handleSchemes(SCHEMES, Collections.<IScheme> emptyList());
        }

        PREF_WEB_LINKS.set(registered);
      }
      catch (Exception ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }
  }

  /**
   * The result of a registration confirmation.
   */
  enum RegistrationConfirmation
  {
    /**
     * The installer should be kept before registration is possible.
     */
    KEEP_INSTALLER,
    /**
     * It's okay to do the registration.
     */
    OK,
    /**
     * The registration is canceled.
     */
    CANCEL,
    /**
     * The registration has already been completed.
     */
    DONE
  }

  /**
   * Prompts for saving the installer to a permanent location if it is transient returning that result.
   * Otherwise prompts for the choices of which schemes to register, returning that result.
   */
  public static RegistrationConfirmation manageRegistrations(Shell shell)
  {
    if (KeepInstallerUtil.isTransientInstaller())
    {
      if (MessageDialog.openConfirm(shell, Messages.URISchemeUtil_title, Messages.URISchemeUtil_description))
      {
        return RegistrationConfirmation.KEEP_INSTALLER;
      }

      return RegistrationConfirmation.CANCEL;
    }

    URISchemeDialog uriSchemeDialog = new URISchemeDialog(shell);
    if (uriSchemeDialog.open() == Window.CANCEL)
    {
      return RegistrationConfirmation.CANCEL;
    }

    return RegistrationConfirmation.DONE;
  }

  /**
   * Used when enabling registration via the toggle button in simple mode.
   * Prompts for saving the installer to a permanent location if it is transient returning that result.
   * Otherwise, if there are conflicting registrations,
   * prompts for the choices of which schemes to register, returning that result.
   * Otherwise returns OK, i.e., that it's okay to call setRegistered(true) in order to register this installer.
   */
  public static RegistrationConfirmation confirmRegistration(Shell shell)
  {
    if (KeepInstallerUtil.isTransientInstaller())
    {
      if (MessageDialog.openConfirm(shell, Messages.URISchemeUtil_title, Messages.URISchemeUtil_description))
      {
        return RegistrationConfirmation.KEEP_INSTALLER;
      }

      return RegistrationConfirmation.CANCEL;
    }

    Map<String, String> conflictingRegistrations = URISchemeUtil.getConflictingRegistrations();
    if (!conflictingRegistrations.isEmpty())
    {
      URISchemeDialog uriSchemeDialog = new URISchemeDialog(shell);
      if (uriSchemeDialog.open() == Window.CANCEL)
      {
        return RegistrationConfirmation.CANCEL;
      }

      return RegistrationConfirmation.DONE;
    }

    return RegistrationConfirmation.OK;
  }

  /**
   * Returns a map from web link schemes to the current conflicting launcher for that scheme.
   */
  public static Map<String, String> getConflictingRegistrations()
  {
    Map<String, String> result = new LinkedHashMap<String, String>();
    try
    {
      List<ISchemeInformation> schemesInformation = OPERATING_SYSTEM_REGISTRATION.getSchemesInformation(SCHEMES);
      String eclipseLauncher = getSelfLauncher();
      for (ISchemeInformation schemeInformation : schemesInformation)
      {
        String handlerInstanceLocation = schemeInformation.getHandlerInstanceLocation();
        if (!StringUtil.isEmpty(handlerInstanceLocation) && !handlerInstanceLocation.equals(eclipseLauncher))
        {
          result.put(schemeInformation.getName(), handlerInstanceLocation);
        }
      }
    }
    catch (Exception ex)
    {
    }

    return result;
  }

  /**
   * Returns the web link schemes currently registered for this installer.
   */
  public static Set<String> getSelfRegistrations()
  {
    Set<String> result = new LinkedHashSet<String>();
    String eclipseLauncher = getSelfLauncher();
    if (!StringUtil.isEmpty(eclipseLauncher))
    {
      try
      {
        List<ISchemeInformation> schemesInformation = OPERATING_SYSTEM_REGISTRATION.getSchemesInformation(SCHEMES);
        for (ISchemeInformation schemeInformation : schemesInformation)
        {
          String handlerInstanceLocation = schemeInformation.getHandlerInstanceLocation();
          if (eclipseLauncher.equals(handlerInstanceLocation))
          {
            result.add(schemeInformation.getName());
          }
        }
      }
      catch (Exception ex)
      {
      }
    }

    return result;
  }

  /**
   * Returns a map from web link scheme to the currently registered launcher for that scheme.
   */
  public static Map<String, String> getRegistrations()
  {
    Map<String, String> result = new LinkedHashMap<String, String>();
    try
    {
      List<ISchemeInformation> schemesInformation = OPERATING_SYSTEM_REGISTRATION.getSchemesInformation(SCHEMES);
      for (ISchemeInformation schemeInformation : schemesInformation)
      {
        String handlerInstanceLocation = schemeInformation.getHandlerInstanceLocation();
        result.put(schemeInformation.getName(), handlerInstanceLocation);
      }
    }
    catch (Exception ex)
    {
    }

    return result;
  }
}
