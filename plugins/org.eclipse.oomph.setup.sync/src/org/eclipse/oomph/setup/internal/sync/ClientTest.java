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

import org.eclipse.oomph.util.PropertiesUtil;

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
class ClientTest
{
  private static final boolean LOCAL_SERVER = true;

  private static final File SYNC_FOLDER = new File(PropertiesUtil.getUserHome(), ".eclipse/org.eclipse.oomph.setup.sync");

  public static void main(String[] args) throws Exception
  {
    RemoteDataProvider dataProvider = createRemoteDataProvider();
    Snapshot remoteSnapshot = new Snapshot(dataProvider, SYNC_FOLDER);

    File workingCopy = remoteSnapshot.getNewFile();
    edit(workingCopy);

    boolean committed = remoteSnapshot.commit();
    System.out.println(committed);
  }

  private static RemoteDataProvider createRemoteDataProvider() throws Exception
  {
    if (LOCAL_SERVER)
    {
      Credentials credentials = new UsernamePasswordCredentials("stepper", "123");
      return new RemoteDataProvider(new URI("http://localhost:8765/protected/service.php"), credentials);
    }

    Credentials credentials = new LoginDialog().getCredentials();
    if (credentials == null)
    {
      System.exit(0);
    }

    return new RemoteDataProvider(new URI("https://dev.eclipse.org/oomph/"), credentials);
  }

  private static void edit(File file)
  {
    file.setLastModified(System.currentTimeMillis());
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
          credentials = new UsernamePasswordCredentials(userField.getText(), new String(passwordField.getPassword()));
          setVisible(false);
        }
      });

      JButton cancelButton = new JButton("Cancel");
      cancelButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          setVisible(false);
        }
      });

      contentPane.add(okButton);
      contentPane.add(cancelButton);

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
  }
}
