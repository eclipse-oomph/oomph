/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.workbench.provider;

import org.eclipse.oomph.base.provider.BaseEditPlugin;
import org.eclipse.oomph.setup.provider.SetupEditPlugin;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.commands.contexts.Context;
import org.eclipse.jface.bindings.Scheme;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.keys.IBindingService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This is the central singleton for the Workbench edit plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class WorkbenchEditPlugin extends EMFPlugin
{
  /**
   * Keep track of the singleton.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final WorkbenchEditPlugin INSTANCE = new WorkbenchEditPlugin();

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
   * @generated
   */
  public WorkbenchEditPlugin()
  {
    super(new ResourceLocator[] { BaseEditPlugin.INSTANCE, SetupEditPlugin.INSTANCE, });
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
  }

  public static Set<String> getSchemes()
  {
    Set<String> result = new TreeSet<>();
    IBindingService service = PlatformUI.getWorkbench().getService(IBindingService.class);
    for (Scheme schemeId : service.getDefinedSchemes())
    {
      result.add(schemeId.getId());
    }

    return result;
  }

  public static Map<String, String> getContexts()
  {
    Map<String, String> result = new TreeMap<>();
    for (Context context : PlatformUI.getWorkbench().getService(IContextService.class).getDefinedContexts())
    {
      String id = context.getId();
      try
      {
        String name = context.getName();
        result.put(id, name);
      }
      catch (NotDefinedException ex)
      {
        result.put(id, null);
      }
    }

    return result;
  }

  public static Map<String, String> getCommands()
  {
    Map<String, String> result = new TreeMap<>();
    ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
    for (Command command : commandService.getDefinedCommands())
    {
      try
      {
        result.put(command.getId(), command.getName());
      }
      catch (NotDefinedException ex)
      {
        result.put(command.getId(), command.getId());
      }
    }

    return result;
  }

  public static Map<String, String> getCommandParameters(String commandId)
  {
    Map<String, String> result = new LinkedHashMap<>();
    if (!StringUtil.isEmpty(commandId))
    {
      ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
      Command command = commandService.getCommand(commandId);
      if (command != null)
      {
        try
        {
          IParameter[] parameters = command.getParameters();
          if (parameters != null)
          {
            for (IParameter parameter : parameters)
            {
              String id = parameter.getId();
              String name = parameter.getName();
              result.put(id, name);
            }
          }
        }
        catch (NotDefinedException ex)
        {
          //$FALL-THROUGH$
        }
      }
    }

    return result;
  }

}
