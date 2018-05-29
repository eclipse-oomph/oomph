/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.preferences.util;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.PreferencesPackage;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.preferences.util.PreferencesUtil.PreferenceProperty;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.Pair;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class PreferencesRecorder extends EContentAdapter
{
  private static final String INSTANCE_SCOPE = "instance";

  private final Map<Property, URI> paths = new HashMap<Property, URI>();

  private final Map<URI, Pair<String, String>> values = new HashMap<URI, Pair<String, String>>();

  private PreferenceNode rootPreferenceNode;

  public PreferencesRecorder()
  {
    rootPreferenceNode = getRootPreferenceNode();
    rootPreferenceNode.eAdapters().add(this);
  }

  public Map<URI, Pair<String, String>> done()
  {
    if (rootPreferenceNode != null)
    {
      for (Iterator<EObject> it = rootPreferenceNode.eResource().getAllContents(); it.hasNext();)
      {
        it.next().eAdapters().clear();
      }

      rootPreferenceNode = null;
    }

    paths.clear();
    for (Iterator<Pair<String, String>> it = values.values().iterator(); it.hasNext();)
    {
      Pair<String, String> pair = it.next();
      if (ObjectUtil.equals(pair.getElement1(), pair.getElement2()))
      {
        it.remove();
      }
    }

    return values;
  }

  protected PreferenceNode getRootPreferenceNode()
  {
    return PreferencesUtil.getRootPreferenceNode(Collections.singleton(PreferencesUtil.INSTANCE_NODE), true);
  }

  @Override
  protected void setTarget(EObject target)
  {
    super.setTarget(target);
    if (target instanceof Property)
    {
      Property property = (Property)target;
      URI absolutePath = property.getAbsolutePath();
      String scope = absolutePath.authority();
      if (INSTANCE_SCOPE.equals(scope))
      {
        paths.put(property, absolutePath);
      }
    }
  }

  @Override
  public void notifyChanged(Notification notification)
  {
    super.notifyChanged(notification);

    if (rootPreferenceNode != null && !notification.isTouch())
    {
      switch (notification.getEventType())
      {
        case Notification.SET:
          if (notification.getFeature() == PreferencesPackage.Literals.PROPERTY__VALUE)
          {
            Property property = (Property)notification.getNotifier();
            String oldValue = notification.getOldStringValue();
            notifyChanged(property, oldValue, property.getValue());
          }
          break;

        case Notification.ADD:
          if (notification.getFeature() == PreferencesPackage.Literals.PREFERENCE_NODE__PROPERTIES)
          {
            Property property = (Property)notification.getNewValue();
            String value = property.getValue();
            notifyChanged(property, null, value);
          }
          break;

        case Notification.REMOVE:
          if (notification.getFeature() == PreferencesPackage.Literals.PREFERENCE_NODE__PROPERTIES)
          {
            Property property = (Property)notification.getOldValue();

            // Record the effective value of this property, which may be non-null because it's specified with a default value in the default or bundle-defaults
            // scope.
            URI uri = paths.get(property);
            String path = PreferencesFactory.eINSTANCE.convertURI(uri);
            PreferenceProperty preferenceProperty = new PreferencesUtil.PreferenceProperty(path);
            String effectiveValue = preferenceProperty.getEffectiveProperty().get(null);
            notifyChanged(property, property.getValue(), effectiveValue);
          }
          break;
      }
    }
  }

  protected void notifyChanged(Property property, String oldValue, String newValue)
  {
    URI absolutePath = paths.get(property);
    if (absolutePath != null)
    {
      notifyChanged(absolutePath, oldValue, newValue);
    }
  }

  protected void notifyChanged(URI absolutePath, String oldValue, String newValue)
  {
    Pair<String, String> pair = values.get(absolutePath);
    if (pair == null)
    {
      values.put(absolutePath, new Pair<String, String>(oldValue, newValue));
    }
    else
    {
      pair.setElement2(newValue);
    }
  }
}
