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
package org.eclipse.oomph.extractor.lib.tests;

import org.eclipse.oomph.extractor.lib.JREData;

/**
 * @author Eike Stepper
 */
public class JRETest
{
  public static void main(String[] args) throws Exception
  {
    JREData jre = new JREData();
    System.out.println("major:   " + jre.getMajor());
    System.out.println("minor:   " + jre.getMinor());
    System.out.println("micro:   " + jre.getMicro());
    System.out.println("bitness: " + jre.getBitness());

    System.out.println();
    System.out.println(new JREData().satisfies(new JREData(1, 6, 0, 32)));
    System.out.println(new JREData(1, 5, 0, 32).satisfies(new JREData(1, 6, 0, 32)));
  }
}
