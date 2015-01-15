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
  }
}
