/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public abstract class Questionnaire
{
  private static final String EXTENSION_POINT_ID = SetupUIPlugin.INSTANCE.getSymbolicName() + ".questionnaire";

  private static Questionnaire instance;

  private static boolean initialized;

  protected Questionnaire()
  {
  }

  protected abstract void doPerform(Shell parentShell, boolean force);

  public static boolean exists()
  {
    return get() != null;
  }

  public static void perform(Shell parentShell, boolean force)
  {
    Questionnaire questionnaire = get();
    if (questionnaire != null)
    {
      questionnaire.doPerform(parentShell, force);
    }
  }

  private static synchronized Questionnaire get()
  {
    if (!initialized)
    {
      long topPriority = Long.MAX_VALUE;
      IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();

      for (IConfigurationElement configurationElement : extensionRegistry.getConfigurationElementsFor(EXTENSION_POINT_ID))
      {
        try
        {
          int priority = Integer.parseInt(configurationElement.getAttribute("priority"));
          if (priority < topPriority)
          {
            instance = (Questionnaire)configurationElement.createExecutableExtension("class");
            topPriority = priority;
          }
        }
        catch (Exception ex)
        {
          SetupUIPlugin.INSTANCE.log(ex);
        }
      }

      initialized = true;
    }

    return instance;
  }
}
