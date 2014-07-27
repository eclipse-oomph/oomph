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
package org.eclipse.oomph.internal.ui;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * @author Ed Merks
 */
public class OomphBinaryTransfer extends ByteArrayTransfer
{
  private static final String OOMPH_EOBJECT_TYPE = "OomphEObjects";

  private static final int OOMPH_EOBJECT_ID = registerType(OOMPH_EOBJECT_TYPE);

  private static final OomphBinaryTransfer instance = new OomphBinaryTransfer();

  private OomphBinaryTransfer()
  {
  }

  public static OomphBinaryTransfer getInstance()
  {
    return instance;
  }

  @Override
  public Object nativeToJava(TransferData transferData)
  {
    return super.nativeToJava(transferData);
  }

  @Override
  protected int[] getTypeIds()
  {
    return new int[] { OOMPH_EOBJECT_ID };
  }

  @Override
  protected String[] getTypeNames()
  {
    return new String[] { OOMPH_EOBJECT_TYPE };
  }
}
