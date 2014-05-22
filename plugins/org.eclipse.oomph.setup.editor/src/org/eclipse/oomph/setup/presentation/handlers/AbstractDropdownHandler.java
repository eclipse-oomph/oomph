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
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.menus.UIElement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public abstract class AbstractDropdownHandler extends AbstractHandler implements IElementUpdater
{
  private static final Map<String, ActionDescriptor> ACTION_DESCRIPTORS = new HashMap<String, ActionDescriptor>();

  private static final ThreadLocal<Boolean> EAGER_LOAD = new ThreadLocal<Boolean>();

  private final String commandID;

  public AbstractDropdownHandler(String commandID)
  {
    this.commandID = commandID;
  }

  public final String getCommandID()
  {
    return commandID;
  }

  public void updateElement(UIElement element, @SuppressWarnings("rawtypes") Map parameters)
  {
    ActionDescriptor actionDescriptor = getActionDescriptor();

    ImageDescriptor imageDescriptor = actionDescriptor.getImageDescriptor();
    if (imageDescriptor != null)
    {
      element.setIcon(imageDescriptor);
    }

    String text = actionDescriptor.getText();
    element.setText(text);
    element.setTooltip(text);
  }

  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    if (Boolean.TRUE.equals(EAGER_LOAD.get()))
    {
      return null;
    }

    ActionDescriptor actionDescriptor = getActionDescriptor();
    Runnable runnable = actionDescriptor.getRunnable();
    if (runnable != null)
    {
      runnable.run();
    }

    return null;
  }

  protected abstract ActionDescriptor createActionDescriptor() throws Exception;

  private ActionDescriptor getActionDescriptor()
  {
    ActionDescriptor actionDescriptor = ACTION_DESCRIPTORS.get(commandID);
    if (actionDescriptor == null)
    {
      try
      {
        actionDescriptor = createActionDescriptor();
        ACTION_DESCRIPTORS.put(commandID, actionDescriptor);
      }
      catch (Exception ex)
      {
        SetupEditorPlugin.getPlugin().log(ex);
        return new ActionDescriptor(null, "Error: " + ex.getLocalizedMessage(), null);
      }
    }

    return actionDescriptor;
  }

  public static void setAction(String commandID, ImageDescriptor imageDescriptor, String text, Runnable runnable)
  {
    ActionDescriptor actionDescriptor = new ActionDescriptor(imageDescriptor, text, runnable);
    ACTION_DESCRIPTORS.put(commandID, actionDescriptor);

    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

    try
    {
      EAGER_LOAD.set(Boolean.TRUE);

      IHandlerService handlerService = (IHandlerService)window.getService(IHandlerService.class);
      handlerService.executeCommand(commandID, null);
    }
    catch (Exception ex)
    {
      SetupEditorPlugin.getPlugin().log(ex);
    }
    finally
    {
      EAGER_LOAD.remove();
    }

    ICommandService commandService = (ICommandService)window.getService(ICommandService.class);
    commandService.refreshElements(commandID, null);
  }

  /**
   * @author Eike Stepper
   */
  public static final class ActionDescriptor
  {
    private final ImageDescriptor imageDescriptor;

    private final String text;

    private final Runnable runnable;

    public ActionDescriptor(ImageDescriptor imageDescriptor, String text, Runnable runnable)
    {
      this.imageDescriptor = imageDescriptor;
      this.text = text;
      this.runnable = runnable;
    }

    public ImageDescriptor getImageDescriptor()
    {
      return imageDescriptor;
    }

    public String getText()
    {
      return text;
    }

    public Runnable getRunnable()
    {
      return runnable;
    }
  }
}
