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
package org.eclipse.oomph.version.digest;

import java.io.Serializable;

/**
 * @author Eike Stepper
 */
public class DigestValidatorState implements Serializable, Comparable<DigestValidatorState>
{
  private static final long serialVersionUID = 1L;

  private String name;

  private byte[] digest;

  private DigestValidatorState parent;

  private DigestValidatorState[] children;

  public DigestValidatorState()
  {
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public byte[] getDigest()
  {
    return digest;
  }

  public void setDigest(byte[] digest)
  {
    this.digest = digest;
  }

  public DigestValidatorState getParent()
  {
    return parent;
  }

  public void setParent(DigestValidatorState parent)
  {
    this.parent = parent;
  }

  public DigestValidatorState[] getChildren()
  {
    return children;
  }

  public void setChildren(DigestValidatorState[] children)
  {
    this.children = children;
  }

  public DigestValidatorState getChild(String name)
  {
    if (children == null)
    {
      return null;
    }

    for (DigestValidatorState child : children)
    {
      if (name.equals(child.getName()))
      {
        return child;
      }
    }

    return null;
  }

  public int compareTo(DigestValidatorState o)
  {
    return name.compareTo(o.name);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == this)
    {
      return true;
    }

    if (obj instanceof DigestValidatorState)
    {
      DigestValidatorState that = (DigestValidatorState)obj;
      return name.equals(that.name);
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    return name.hashCode();
  }

  @Override
  public String toString()
  {
    return name;
  }
}
