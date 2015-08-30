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
package org.eclipse.oomph.setup.sync.tests;

import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;

/**
 * @author Eike Stepper
 */
public class TestServer
{
  private static final boolean LOCALHOST = true;

  private static RemoteDataProvider remoteDataProvider;

  public static synchronized RemoteDataProvider getRemoteDataProvider() throws Exception
  {
    if (remoteDataProvider == null)
    {
      remoteDataProvider = createRemoteDataProvider();
    }

    return remoteDataProvider;
  }

  private static RemoteDataProvider createRemoteDataProvider() throws Exception
  {
    if (LOCALHOST)
    {
      return new RemoteDataProvider(new URI("http://localhost:8765/protected/"), new UsernamePasswordCredentials("stepper", "123"));
    }

    Credentials credentials;

    File file = new File(PropertiesUtil.getUserHome(), "oomph.test.credentials");
    if (file.exists())
    {
      System.out.println("Reading credentials from file " + file);
      credentials = (Credentials)IOUtil.readObject(file, UsernamePasswordCredentials.class.getClassLoader());
    }
    else
    {
      credentials = new LoginDialog().getCredentials();
      if (credentials == null)
      {
        System.exit(0);
      }

      System.out.println("Storing credentials in file " + file);
      IOUtil.writeObject(file, credentials);
    }

    return new RemoteDataProvider(new URI("https://dev.eclipse.org/oomph/"), credentials);
  }

  /**
   * @author Eike Stepper
   */
  private static class LoginDialog extends JDialog
  {
    private static final long serialVersionUID = 1L;

    private Credentials credentials;

    public LoginDialog()
    {
      setTitle("Login");
      setModal(true);

      final JTextField userField = new JTextField(15);
      final JPasswordField passwordField = new JPasswordField(15);

      Container contentPane = getContentPane();
      contentPane.setLayout(new GridLayout(3, 2));
      contentPane.add(new JLabel("Username:"));
      contentPane.add(userField);
      contentPane.add(new JLabel("Password:"));
      contentPane.add(passwordField);

      JButton okButton = new JButton("OK");
      okButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          String username = userField.getText();
          String password = new String(passwordField.getPassword());
          if (!StringUtil.isEmpty(username) && !StringUtil.isEmpty(password))
          {
            credentials = new UsernamePasswordCredentials(username, password);
          }

          close();
        }
      });

      JButton cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          close();
        }
      });

      contentPane.add(okButton);
      contentPane.add(cancelButton);

      userField.setText("stepper@esc-net.de");

      pack();
      setCenterLocation();
      setVisible(true);
    }

    public Credentials getCredentials()
    {
      return credentials;
    }

    private void setCenterLocation()
    {
      try
      {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices())
        {
          Rectangle bounds = device.getDefaultConfiguration().getBounds();
          if (bounds.contains(mousePoint))
          {
            setLocation((bounds.width - getWidth()) / 2 + bounds.x, (bounds.height - getHeight()) / 2 + bounds.y);
            break;
          }
        }
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }
    }

    private void close()
    {
      setVisible(false);
      dispose();
    }
  }
}
