/*
 * Copyright (c) 2014, 2016, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OomphPlugin;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerFactory;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.filetransfer.IRemoteFileSystemBrowserContainerAdapter;
import org.eclipse.ecf.filetransfer.IRetrieveFileTransferContainerAdapter;
import org.eclipse.ecf.provider.filetransfer.browse.MultiProtocolFileSystemBrowserAdapter;
import org.eclipse.ecf.provider.filetransfer.browse.MultiProtocolFileSystemBrowserAdapterFactory;
import org.eclipse.ecf.provider.filetransfer.retrieve.MultiProtocolRetrieveAdapter;
import org.eclipse.ecf.provider.filetransfer.retrieve.MultiProtocolRetrieveAdapterFactory;
import org.eclipse.osgi.util.NLS;

import org.osgi.framework.BundleContext;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class P2CorePlugin extends OomphPlugin
{
  public static final P2CorePlugin INSTANCE = new P2CorePlugin();

  private static Implementation plugin;

  public P2CorePlugin()
  {
    super(new ResourceLocator[] {});
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  public static File getUserStateFolder(File userHome)
  {
    File folder = new File(userHome, ".eclipse/org.eclipse.oomph.p2"); //$NON-NLS-1$

    try
    {
      // TODO Remove this legacy migration for 1.0 release
      if (!folder.exists())
      {
        File oldFolder = new File(folder.getAbsolutePath() + ".core"); //$NON-NLS-1$
        if (oldFolder.isDirectory())
        {
          IOUtil.copyTree(oldFolder, folder);

          String message = NLS.bind(Messages.P2CorePlugin_OldFolder_message, folder.getName());
          IOUtil.writeFile(new File(oldFolder, "readme.txt"), message.getBytes()); //$NON-NLS-1$
        }
      }
    }
    catch (Exception ex)
    {
      INSTANCE.log(ex);
    }

    return folder;
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipsePlugin
  {
    public Implementation()
    {
      plugin = this;
    }

    @SuppressWarnings("restriction")
    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);

      // In org.eclipse.equinox.internal.p2.transport.ecf.FileInfoReader.sendBrowseRequest(URI, IProgressMonitor) it creates an adapter for the container,
      // and it uses org.eclipse.ecf.filetransfer.IRemoteFileSystemBrowserContainerAdapter.setConnectContextForAuthentication(IConnectContext) to set the
      // credentials.
      // But unfortunately org.eclipse.ecf.provider.filetransfer.browse.MultiProtocolFileSystemBrowserAdapterFactory.getContainerAdapter(IContainer, Class)
      // returns a single instance for the entire JVM.
      // So it's not possible to have multiple threads using different credentials and it's quite likely that credentials and URLs will get all mixed up.
      try
      {
        IContainer container = ContainerFactory.getDefault().createContainer();
        MultiProtocolFileSystemBrowserAdapterFactory browserAdapter = new MultiProtocolFileSystemBrowserAdapterFactory()
        {
          @Override
          protected Object getContainerAdapter(IContainer container, @SuppressWarnings("rawtypes") Class adapterType)
          {
            if (adapterType.equals(IRemoteFileSystemBrowserContainerAdapter.class))
            {
              return new MultiProtocolFileSystemBrowserAdapter();
            }

            return null;
          }
        };

        MultiProtocolRetrieveAdapterFactory retrieveAdapter = new MultiProtocolRetrieveAdapterFactory()
        {
          @Override
          protected Object getContainerAdapter(IContainer container, @SuppressWarnings("rawtypes") Class adapterType)
          {
            if (adapterType.equals(IRetrieveFileTransferContainerAdapter.class))
            {
              return new MultiProtocolRetrieveAdapter();
            }

            return null;
          }
        };

        // Register our adapter.
        org.eclipse.core.internal.runtime.AdapterManager adapterManager = (org.eclipse.core.internal.runtime.AdapterManager)org.eclipse.ecf.internal.core.ECFPlugin
            .getDefault().getAdapterManager();
        adapterManager.registerAdapters(browserAdapter, container.getClass());
        adapterManager.registerAdapters(retrieveAdapter, container.getClass());

        // Make sure it's used as the default.
        // Call like this because the signature of the method has changed.
        Map<String, List<IAdapterFactory>> factories = ReflectUtil.invokeMethod("getFactories", adapterManager); //$NON-NLS-1$
        for (Iterator<List<IAdapterFactory>> it = factories.values().iterator(); it.hasNext();)
        {
          List<IAdapterFactory> list = it.next();
          if (list.remove(browserAdapter))
          {
            list.add(0, browserAdapter);
          }

          if (list.remove(retrieveAdapter))
          {
            list.add(0, retrieveAdapter);
          }
        }
      }
      catch (LinkageError ex)
      {
        // If the platform changes their API we'll have to find a new way to ensure that our adapters are used.
        log("Failed to register the thread safe credentials providers: " + ex.getLocalizedMessage()); //$NON-NLS-1$
      }
      catch (ContainerCreateException ex)
      {
        // If we can't create the container, ECF is probably pretty broken and downstream things will fail, e.g., loading remote repositories, but at least this
        // bundle starts.
      }
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
      AgentManager instance = AgentManagerImpl.instance;
      if (instance != null)
      {
        ((AgentManagerImpl)instance).dispose();
      }
      super.stop(context);
    }
  }
}
