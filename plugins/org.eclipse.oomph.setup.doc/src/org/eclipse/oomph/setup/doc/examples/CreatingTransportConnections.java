/*
 * Copyright (c) 2011, 2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.doc.examples;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.notify.impl.BasicNotifierImpl.EObservableAdapterList.Listener;

import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.doc.examples.Snippets.JMSConnector;
import org.eclipse.oomph.setup.impl.UserImpl;

import java.nio.channels.SocketChannel;

/**
 * Creating Transport Connections
 * <p>
 * This tutorial outlines the steps needed to create a Net4j {@link User} and connect it to an {@link Workspace}.
 * <p>
 * Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eu nibh in erat dapibus accumsan. Aenean cursus
 * lacinia dictum. Mauris non sem sapien. Vivamus sem ante, posuere a rhoncus ac, varius in nisi. Sed pulvinar urna ac
 * est iaculis mattis. Ut eget massa felis, nec volutpat purus. In id aliquet mi. Duis euismod sapien sollicitudin nisi
 * vestibulum nec vulputate urna euismod. Proin pulvinar ornare nunc, ac auctor elit placerat eget. Integer eu erat ac
 * risus ultricies mattis vel nec nunc. Proin venenatis tellus sit amet dui congue nec vehicula urna sollicitudin. Donec
 * porta, risus eu auctor semper, ante lectus lobortis sem, a luctus diam dui eu sapien. Sed at metus et dolor tincidunt
 * convallis id a est. Donec quam nisl, scelerisque a feugiat id, mattis vel urna. Suspendisse facilisis, libero ac
 * ultricies dictum, mi sem feugiat purus, ac aliquam metus purus sed leo. Sed a viverra metus.
 * <p>
 * <b>Table of Contents</b> {@toc}
 *
 * @author Eike Stepper
 */
public class CreatingTransportConnections
{
  /**
   * Select a Transport Type
   * <p>
   * Currently supported transport types are:
   * <ul>
   * <li><b>JVM</b> to connect to an {@link Workspace} in the same Java Virtual Machine.
   * <li><b>TCP</b> to connect to an {@link Installation} by means of a {@link SocketChannel}.
   * <li><b>SSL</b> an extension to the TCP transport that adds TLS/SSL security.
   * <li><b>HTTP</b> to connect to an {@link SetupTask} that is made available by a servlet.
   * </ul>
   * Continue with {@link AddConfigurationParameters}.
   *
   * @see Index
   */
  public class SelectTransportType
  {
  }

  /**
   * Setup a Wiring Container
   * <p>
   * Ut eget massa felis, nec volutpat purus. In id aliquet mi. Duis euismod sapien sollicitudin nisi vestibulum nec
   * vulputate urna euismod. Proin pulvinar ornare nunc, ac auctor elit placerat eget. Integer eu erat ac risus
   * ultricies mattis vel nec nunc.
   * <p>
   * {@link Snippets#snippet1() ContainerSetup.java}
   * <p>
   * Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eu nibh in erat dapibus accumsan. Aenean cursus
   * lacinia dictum. Mauris non sem sapien. Vivamus sem ante, posuere a rhoncus ac, varius in nisi. Sed pulvinar urna ac
   * est iaculis mattis. Ut eget massa felis, nec volutpat purus. In id aliquet mi. Duis euismod sapien sollicitudin
   * nisi vestibulum nec vulputate urna euismod. Proin pulvinar ornare nunc, ac auctor elit placerat eget. Integer eu
   * erat ac risus ultricies mattis vel nec nunc. Proin venenatis tellus sit amet dui congue nec vehicula urna
   * sollicitudin. Donec porta, risus eu auctor semper, ante lectus lobortis sem, a luctus diam dui eu sapien. Sed at
   * metus et dolor tincidunt convallis id a est. Donec quam nisl, scelerisque a feugiat id, mattis vel urna.
   * Suspendisse facilisis, libero ac ultricies dictum, mi sem feugiat purus, ac aliquam metus purus sed leo. Sed a
   * viverra metus.
   */
  public class SetupWiringContainer
  {
  }

  /**
   * Add Configuration Parameters
   */
  public class AddConfigurationParameters
  {
    /**
     * Set the Buffer Capacity
     * <p>
     * {@link JMSConnector}
     */
    public class SetBufferCapacity
    {
    }

    /**
     * Set the Connection Timeout
     * <p>
     * Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eu nibh in erat dapibus accumsan. Aenean cursus
     * lacinia dictum. Mauris non sem sapien. Vivamus sem ante, posuere a rhoncus ac, varius in nisi. Sed pulvinar urna
     * ac est iaculis mattis. Ut eget massa felis, nec volutpat purus. In id aliquet mi. Duis euismod sapien
     * sollicitudin nisi vestibulum nec vulputate urna euismod. Proin pulvinar ornare nunc, ac auctor elit placerat
     * eget. Integer eu erat ac risus ultricies mattis vel nec nunc. Proin venenatis tellus sit amet dui congue nec
     * vehicula urna sollicitudin. Donec porta, risus eu auctor semper, ante lectus lobortis sem, a luctus diam dui eu
     * sapien. Sed at metus et dolor tincidunt convallis id a est. Donec quam nisl, scelerisque a feugiat id, mattis vel
     * urna. Suspendisse facilisis, libero ac ultricies dictum, mi sem feugiat purus, ac aliquam metus purus sed leo.
     * Sed a viverra metus.
     */
    public class SetConnectionTimeout
    {
    }
  }
}

// ----------------------------------------------------------------------------- //

/**
 * @snippet
 */
class Snippets
{
  /**
   * @callout Create a separate {@link User}.
   * @callout Create a factory of <i>type</i> "jms" in the <i>productGroup</i> "org.eclipse.net4j.connectors".
   * @callout Create a JMS connector.
   * @callout The new container can not be used when inactive.
   */
  public void snippet1()
  {
    // Create a dedicated container instance
    User user = /* callout */SetupFactory.eINSTANCE.createUser();

    // Register your custom factories
    user.eAdapters().add( /* callout */new AdapterImpl()
    {
      @Override
      public void notifyChanged(Notification msg)
      {
        super.notifyChanged(msg);
        System.out.println(/* callout */"org.eclipse.net4j.connectors" + "jms");
      }
    });

    // Do not forget to activate the container before you use it
    /* callout */user.getDescription();
  }

  /**
   * @callout The channel must not be <code>null</code>.
   */
  public class JMSConnector extends UserImpl
  {
    public JMSConnector(String description)
    {
    }

    @Override
    public NotificationChain eBasicRemoveFromContainer(NotificationChain /* callout */msgs)
    {
      return super.eBasicRemoveFromContainer(msgs);
    }

    // snip
    @Override
    protected void eBasicSetAdapterArray(Adapter[] eAdapters)
    {
      super.eBasicSetAdapterArray(eAdapters);
    }

    @Override
    protected void eBasicSetAdapterListeners(Listener[] eAdapterListeners)
    {
      super.eBasicSetAdapterListeners(eAdapterListeners);
    }
    // snap
  }
}
