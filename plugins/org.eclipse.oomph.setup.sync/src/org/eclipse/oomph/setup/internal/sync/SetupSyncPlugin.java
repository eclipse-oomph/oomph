/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;
import org.eclipse.oomph.setup.internal.sync.SynchronizerService.Registry;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OomphPlugin;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.core.runtime.Platform;

import org.osgi.framework.BundleContext;

/**
 * This is the central singleton for the SetupSync model plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated not
 */
public final class SetupSyncPlugin extends OomphPlugin
{
  /**
   * Keep track of the singleton.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final SetupSyncPlugin INSTANCE = new SetupSyncPlugin();

  private static final SynchronizerService ECLIPSE_SERVICE = new SynchronizerService("Eclipse.org", Registry.ECLIPSE_SERVICE_URI,
      IOUtil.newURI("https://dev.eclipse.org/site_login"));

  /**
   * Keep track of the singleton.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static Implementation plugin;

  /**
   * Create the instance.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public SetupSyncPlugin()
  {
    super(new ResourceLocator[] { SetupCorePlugin.INSTANCE });
  }

  /**
   * Returns the singleton instance of the Eclipse plugin.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the singleton instance.
   * @generated
   */
  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  /**
   * Returns the singleton instance of the Eclipse plugin.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the singleton instance.
   * @generated
   */
  public static Implementation getPlugin()
  {
    return plugin;
  }

  /**
   * The actual implementation of the Eclipse <b>Plugin</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static class Implementation extends EclipsePlugin
  {
    /**
     * Creates an instance.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Implementation()
    {
      super();

      // Remember the static instance.
      //
      plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);
      SynchronizerService.Registry.INSTANCE.addService(ECLIPSE_SERVICE);
      ExtensionPointHandler.INSTANCE.start();
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
      ExtensionPointHandler.INSTANCE.stop();
      super.stop(context);
    }
  }

  /**
   * @author Eike Stepper
   */
  private static class ExtensionPointHandler implements IRegistryEventListener
  {
    public static final ExtensionPointHandler INSTANCE = new ExtensionPointHandler();

    private static final String EXTENSION_POINT = SetupSyncPlugin.INSTANCE.getSymbolicName() + ".services";

    public ExtensionPointHandler()
    {
    }

    public void start() throws Exception
    {
      IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
      for (IConfigurationElement configurationElement : extensionRegistry.getConfigurationElementsFor(EXTENSION_POINT))
      {
        added(configurationElement);
      }

      extensionRegistry.addListener(this, EXTENSION_POINT);
    }

    public void stop() throws Exception
    {
      Platform.getExtensionRegistry().removeListener(this);
    }

    public void added(IExtensionPoint[] extensionPoints)
    {
      // Do nothing
    }

    public void removed(IExtensionPoint[] extensionPoints)
    {
      // Do nothing
    }

    public void added(IExtension[] extensions)
    {
      for (IExtension extension : extensions)
      {
        for (IConfigurationElement configurationElement : extension.getConfigurationElements())
        {
          added(configurationElement);
        }
      }
    }

    public void removed(IExtension[] extensions)
    {
      for (IExtension extension : extensions)
      {
        for (IConfigurationElement configurationElement : extension.getConfigurationElements())
        {
          removed(configurationElement);
        }
      }
    }

    private void added(IConfigurationElement configurationElement)
    {
      try
      {
        SynchronizerService service = createService(configurationElement);
        SynchronizerService.Registry.INSTANCE.addService(service);
      }
      catch (Exception ex)
      {
        SetupSyncPlugin.INSTANCE.log(ex);
      }
    }

    private void removed(IConfigurationElement configurationElement)
    {
      try
      {
        SynchronizerService service = createService(configurationElement);
        SynchronizerService.Registry.INSTANCE.removeService(service);
      }
      catch (Exception ex)
      {
        SetupSyncPlugin.INSTANCE.log(ex);
      }
    }

    private SynchronizerService createService(IConfigurationElement configurationElement)
    {
      String label = configurationElement.getAttribute("label");
      String serviceURI = configurationElement.getAttribute("serviceURI");
      String signupURI = configurationElement.getAttribute("signupURI");

      return new SynchronizerService(label, IOUtil.newURI(serviceURI), IOUtil.newURI(signupURI));
    }
  }

}
