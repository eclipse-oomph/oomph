/*
 * Copyright (c) 2015 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.internal.ui.OomphPropertySheetPage.OomphPropertyDescriptor;
import org.eclipse.oomph.util.SuppressHint;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IViewerNotification;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.NotifyChangedToViewerRefresh;
import org.eclipse.emf.edit.ui.provider.PropertySource;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * @author Ed Merks
 */
public class OomphAdapterFactoryContentProvider extends AdapterFactoryContentProvider
{
  public OomphAdapterFactoryContentProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  @Override
  public void notifyChanged(Notification notification)
  {
    if (viewer != null && viewer.getControl() != null && !viewer.getControl().isDisposed())
    {
      if (notification instanceof IViewerNotification)
      {
        if (viewerRefresh == null)
        {
          viewerRefresh = new ViewerRefresh(viewer)
          {
            int count = 0;

            @Override
            public synchronized boolean addNotification(IViewerNotification notification)
            {
              if (super.addNotification(notification))
              {
                count = 0;
                return true;
              }

              // When there are more than 30 notifications, it's probably cheaper simply to refresh the overall view.
              if (count > 30)
              {
                super.addNotification(new ViewerNotification(notification, null, true, true));
              }

              ++count;
              return false;
            }
          };
        }

        if (viewerRefresh.addNotification((IViewerNotification)notification))
        {
          viewer.getControl().getDisplay().asyncExec(viewerRefresh);
        }
      }
      else
      {
        NotifyChangedToViewerRefresh.handleNotifyChanged(viewer, notification.getNotifier(), notification.getEventType(), notification.getFeature(),
            notification.getOldValue(), notification.getNewValue(), notification.getPosition());
      }
    }
  }

  @Override
  protected IPropertySource createPropertySource(Object object, IItemPropertySource itemPropertySource)
  {
    return new PropertySource(object, itemPropertySource)
    {
      @Override
      protected IPropertyDescriptor createPropertyDescriptor(IItemPropertyDescriptor itemPropertyDescriptor)
      {
        return new OomphPropertyDescriptor(object, itemPropertyDescriptor);
      }
    };
  }

  @Override
  public Object[] getElements(Object object)
  {
    Object[] elements = super.getElements(object);
    int count = countSuppressed(elements);
    if (count > 0)
    {
      elements = suppress(elements, count);
    }

    return elements;
  }

  @Override
  public Object[] getChildren(Object object)
  {
    Object[] children = super.getChildren(object);
    int count = countSuppressed(children);
    if (count > 0)
    {
      children = suppress(children, count);
    }

    return children;
  }

  @Override
  public boolean hasChildren(Object object)
  {
    boolean hasChildren = super.hasChildren(object);
    if (hasChildren)
    {
      Object[] children = super.getChildren(object);
      int count = countSuppressed(children);
      return children.length - count > 0;
    }

    return hasChildren;
  }

  private int countSuppressed(Object[] objects)
  {
    int count = 0;
    for (int i = 0; i < objects.length; i++)
    {
      Object object = objects[i];
      if (SuppressHint.isSuppressed(object))
      {
        ++count;
      }
    }

    return count;
  }

  private Object[] suppress(Object[] objects, int count)
  {
    Object[] result = new Object[objects.length - count];
    int r = 0;

    for (int i = 0; i < objects.length; i++)
    {
      Object object = objects[i];
      if (!SuppressHint.isSuppressed(object))
      {
        result[r++] = object;
      }
    }

    return result;
  }
}
