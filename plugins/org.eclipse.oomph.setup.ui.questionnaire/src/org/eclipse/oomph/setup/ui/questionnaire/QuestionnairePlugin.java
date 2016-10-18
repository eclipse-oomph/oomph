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
package org.eclipse.oomph.setup.ui.questionnaire;

import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.preferences.util.PreferencesUtil.PreferenceProperty;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.ui.recorder.RecorderTransaction;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.Pair;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;

import org.eclipse.swt.widgets.Shell;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Eike Stepper
 */
public final class QuestionnairePlugin extends OomphUIPlugin
{
  public static final QuestionnairePlugin INSTANCE = new QuestionnairePlugin();

  public static final String PLUGIN_ID = INSTANCE.getSymbolicName();

  private static Implementation plugin;

  public QuestionnairePlugin()
  {
    super(new ResourceLocator[] { org.eclipse.oomph.setup.ui.SetupUIPlugin.INSTANCE });
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  public static void performQuestionnaire(final Shell parentShell, boolean force)
  {
    RecorderTransaction transaction = RecorderTransaction.open();

    try
    {
      SetupTaskContainer rootObject = transaction.getRootObject();
      if (rootObject instanceof User)
      {
        User user = (User)rootObject;
        if (user.getQuestionnaireDate() == null || force)
        {
          final Map<URI, Pair<String, String>> preferences = new HashMap<URI, Pair<String, String>>();
          UIUtil.syncExec(new Runnable()
          {
            public void run()
            {
              GearShell shell = new GearShell(parentShell);
              Map<URI, Pair<String, String>> result = shell.openModal();
              if (result != null)
              {
                preferences.putAll(result);
              }
            }
          });

          URI uri = PreferencesFactory.eINSTANCE.createURI(GearAnimator.RECORDER_PREFERENCE_KEY);
          if (preferences.containsKey(uri))
          {
            boolean enabled = Boolean.parseBoolean(preferences.remove(uri).getElement2());
            user.setPreferenceRecorderDefault(enabled);
          }

          if (!preferences.isEmpty())
          {
            for (Entry<URI, Pair<String, String>> entry : preferences.entrySet())
            {
              String path = PreferencesFactory.eINSTANCE.convertURI(entry.getKey());
              transaction.setPolicy(path, true);

              if (!SetupUtil.INSTALLER_APPLICATION)
              {
                PreferenceProperty property = new PreferencesUtil.PreferenceProperty(path);
                property.set(entry.getValue().getElement2());
              }
            }

            transaction.setPreferences(preferences);
          }

          user.setQuestionnaireDate(new Date());
          transaction.setForceDirty(true);
          transaction.commit();
        }
      }
    }
    finally
    {
      transaction.close();
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipseUIPlugin
  {
    public Implementation()
    {
      plugin = this;
    }
  }
}
