/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.extractor.lib;

import java.io.IOException;

/**
 * @author Eike Stepper
 */
public final class JREValidator
{
  public static void main(String[] args) throws IOException
  {
    JREData jre = new JREData();
    if (args.length == 0)
    {
      System.out.println(jre);
    }
    else
    {
      JREData requirement = new JREData(args);
      if (!jre.satisfies(requirement))
      {
        System.exit(-1);
      }
    }
  }
}
