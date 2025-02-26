/*
 * Copyright (c) 202t Eclipse contributor and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.presentation.NotificationViewPart;
import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import java.util.List;

public class NotificationsHandler extends AbstractHandler
{
  public NotificationsHandler()
  {
  }

  @Override
  public boolean isEnabled()
  {
    return super.isEnabled();
  }

  @Override
  public void setEnabled(Object evaluationContext)
  {
    List<Annotation> notifications = SetupPropertyTester.getNotifications();
    setBaseEnabled(!notifications.isEmpty());
  }

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    List<Annotation> notifications = SetupPropertyTester.getNotifications();
    try
    {
      int count = 0;
      for (Annotation annotation : notifications)
      {
        NotificationViewPart notificationView = (NotificationViewPart)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
            NotificationViewPart.VIEW_ID, "ID" + ++count, //$NON-NLS-1$
            IWorkbenchPage.VIEW_VISIBLE);
        notificationView.getSite().getPage().bringToTop(notificationView);
        notificationView.setUrl(annotation.getDetails().get(AnnotationConstants.KEY_URI));
      }
    }
    catch (Exception ex)
    {
      SetupEditorPlugin.INSTANCE.log(ex, IStatus.WARNING);
    }

    return Status.OK;
  }
}
