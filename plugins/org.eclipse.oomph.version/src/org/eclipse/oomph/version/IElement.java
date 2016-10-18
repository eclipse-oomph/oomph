/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version;

import org.osgi.framework.Version;

import java.util.List;
import java.util.Set;

/**
 * A light abstraction of a component, i.e. a plug-in or a feature.
 *
 * @author Eike Stepper
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IElement
{
  public Type getType();

  public String getTag();

  public String getName();

  public Version getVersion();

  public Version getResolvedVersion();

  public boolean isFragment();

  public boolean isLicenseFeature();

  public List<IElement> getChildren();

  public Set<IElement> getAllChildren(IElementResolver resolver, IElementResolver otherResolver);

  public IElement getChild(IElementResolver resolver, IElementResolver otherResolver, IElement key);

  public IElement trimVersion();

  public boolean isVersionUnresolved();

  /**
   * Enumerates the possible types of a {@link IElement component}.
   *
   * @author Eike Stepper
   */
  public static enum Type
  {
    PRODUCT
    {
      @Override
      public String getTag()
      {
        return "products";
      }

      @Override
      public String toString()
      {
        return "Product";
      }
    },

    FEATURE
    {
      @Override
      public String getTag()
      {
        return "includes";
      }

      @Override
      public String toString()
      {
        return "Feature";
      }
    },

    PLUGIN
    {
      @Override
      public String getTag()
      {
        return "plugin";
      }

      @Override
      public String toString()
      {
        return "Plug-in";
      }
    };

    public abstract String getTag();
  }
}
