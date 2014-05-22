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
package org.eclipse.oomph.internal.version;

import org.eclipse.oomph.version.IElement;
import org.eclipse.oomph.version.IElementResolver;
import org.eclipse.oomph.version.IReleaseManager;
import org.eclipse.oomph.version.VersionUtil;

import org.eclipse.pde.core.IModel;

import org.osgi.framework.Version;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class Element implements IElement
{
  private Element.Type type;

  private String name;

  private Version version;

  private boolean licenseFeature;

  private List<IElement> children = new ArrayList<IElement>();

  private Set<IElement> allChildren;

  public Element(Element.Type type, String name, Version version)
  {
    this.type = type;
    this.name = name;
    this.version = VersionUtil.normalize(version);
  }

  public Element(Element.Type type, String name, String version)
  {
    this(type, name, new Version(version));
  }

  public Element(Type type, String name)
  {
    this.type = type;
    this.name = name;
    version = Version.emptyVersion;
  }

  public Type getType()
  {
    return type;
  }

  public String getTag()
  {
    return type == Type.PLUGIN ? Release.PLUGIN_TAG : Release.FEATURE_TAG;
  }

  public String getName()
  {
    return name;
  }

  public Version getVersion()
  {
    return version;
  }

  public boolean isLicenseFeature()
  {
    return licenseFeature;
  }

  public void setLicenseFeature(boolean licenseFeature)
  {
    this.licenseFeature = licenseFeature;
  }

  public List<IElement> getChildren()
  {
    return children;
  }

  public Set<IElement> getAllChildren(IElementResolver resolver, IElementResolver otherResolver)
  {
    if (allChildren == null)
    {
      allChildren = new HashSet<IElement>();
      for (IElement child : children)
      {
        recurseChildren(resolver, otherResolver, child);
      }
    }

    return allChildren;
  }

  private void recurseChildren(IElementResolver resolver, IElementResolver otherResolver, IElement element)
  {
    if (allChildren.add(element))
    {
      IElement topElement = resolver.resolveElement(element);
      if (topElement == null)
      {
        // If we fail to find it with an exact version, we try it with an omni version and use that for the children.
        //
        topElement = resolver.resolveElement(element.trimVersion());
        if (topElement == null)
        {
          return;
        }
      }

      IElement otherTopElement = otherResolver.resolveElement(element);
      if (otherTopElement == null)
      {
        // If we fail to find it with an exact version, we try it with an omni version and use that for the children.
        //
        otherTopElement = resolver.resolveElement(element.trimVersion());
        if (otherTopElement == null)
        {
          return;
        }
      }

      if (otherTopElement.isVersionUnresolved())
      {
        return;
      }

      for (IElement child : topElement.getChildren())
      {
        if (!child.isLicenseFeature())
        {
          recurseChildren(resolver, otherResolver, child);
        }
      }
    }
  }

  public IElement getChild(IElementResolver resolver, IElementResolver otherResolver, IElement key)
  {
    Set<IElement> allChildren = getAllChildren(resolver, otherResolver);
    for (IElement child : allChildren)
    {
      if (child.equals(key))
      {
        return child;
      }
    }

    return null;
  }

  @Override
  public String toString()
  {
    return "Element[type=" + type + ", name=" + name + ", version=" + version + "]";
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (name == null ? 0 : name.hashCode());
    result = prime * result + (getType() == null ? 0 : getType().hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (!(obj instanceof Element))
    {
      return false;
    }

    Element other = (Element)obj;
    if (name == null)
    {
      if (other.name != null)
      {
        return false;
      }
    }
    else if (!name.equals(other.name))
    {
      return false;
    }

    if (getType() != other.getType())
    {
      return false;
    }

    if (!version.equals(Version.emptyVersion) && !other.getVersion().equals(Version.emptyVersion))
    {
      if (!version.equals(other.getVersion()))
      {
        return false;
      }
    }

    return true;
  }

  public IElement trimVersion()
  {
    return new Element(type, name);
  }

  public boolean isVersionUnresolved()
  {
    return version.equals(Version.emptyVersion);
  }

  void resolveVersion()
  {
    version = getResolvedVersion();
  }

  public Version getResolvedVersion()
  {
    if (isVersionUnresolved())
    {
      IModel componentModel = IReleaseManager.INSTANCE.getComponentModel(this);
      if (componentModel != null)
      {
        return VersionUtil.getComponentVersion(componentModel);
      }
    }

    return version;
  }
}
