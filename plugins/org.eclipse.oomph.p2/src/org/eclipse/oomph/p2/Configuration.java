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
package org.eclipse.oomph.p2;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.p2.Configuration#getWS <em>WS</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Configuration#getOS <em>OS</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Configuration#getArch <em>Arch</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.p2.P2Package#getConfiguration()
 * @model
 * @generated
 */
public interface Configuration extends ModelElement
{
  /**
   * Returns the value of the '<em><b>WS</b></em>' attribute.
   * The default value is <code>"ANY"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>WS</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>WS</em>' attribute.
   * @see #setWS(String)
   * @see org.eclipse.oomph.p2.P2Package#getConfiguration_WS()
   * @model default="ANY" required="true"
   *        extendedMetaData="kind='attribute' name='ws'"
   * @generated
   */
  String getWS();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Configuration#getWS <em>WS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>WS</em>' attribute.
   * @see #getWS()
   * @generated
   */
  void setWS(String value);

  /**
   * Returns the value of the '<em><b>OS</b></em>' attribute.
   * The default value is <code>"ANY"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>OS</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>OS</em>' attribute.
   * @see #setOS(String)
   * @see org.eclipse.oomph.p2.P2Package#getConfiguration_OS()
   * @model default="ANY" required="true"
   *        extendedMetaData="kind='attribute' name='os'"
   * @generated
   */
  String getOS();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Configuration#getOS <em>OS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>OS</em>' attribute.
   * @see #getOS()
   * @generated
   */
  void setOS(String value);

  /**
   * Returns the value of the '<em><b>Arch</b></em>' attribute.
   * The default value is <code>"ANY"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Arch</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Arch</em>' attribute.
   * @see #setArch(String)
   * @see org.eclipse.oomph.p2.P2Package#getConfiguration_Arch()
   * @model default="ANY" required="true"
   * @generated
   */
  String getArch();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Configuration#getArch <em>Arch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Arch</em>' attribute.
   * @see #getArch()
   * @generated
   */
  void setArch(String value);

  /**
   * @author Eike Stepper
   */
  public static final class Choices
  {
    public static Set<String> forWS()
    {
      return getChoices(Platform.knownWSValues(), "org.eclipse.pde.ws.extra");
    }

    public static Set<String> forOS()
    {
      return getChoices(Platform.knownOSValues(), "org.eclipse.pde.os.extra");
    }

    public static Set<String> forArch()
    {
      return getChoices(Platform.knownOSArchValues(), "org.eclipse.pde.arch.extra");
    }

    private static Set<String> getChoices(String[] values, String extraValuesPreference)
    {
      Set<String> result = new HashSet<String>();
      result.addAll(Arrays.asList(values));

      IEclipsePreferences node = InstanceScope.INSTANCE.getNode("org.eclipse.pde.core");
      if (node != null)
      {
        String extraValues = node.get(extraValuesPreference, null);
        if (!StringUtil.isEmpty(extraValues))
        {
          StringTokenizer tokenizer = new StringTokenizer(extraValues, ",");
          while (tokenizer.hasMoreTokens())
          {
            String extraValue = tokenizer.nextToken().trim();
            result.add(extraValue);
          }
        }
      }

      return result;
    }
  }

} // Configuration
