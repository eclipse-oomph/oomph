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
package org.eclipse.oomph.setup;

import org.eclipse.emf.common.util.Enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Trigger</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.SetupPackage#getTrigger()
 * @model
 * @generated
 */
public enum Trigger implements Enumerator
{
  /**
   * The '<em><b>BOOTSTRAP</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BOOTSTRAP_VALUE
   * @generated
   * @ordered
   */
  BOOTSTRAP(0, "BOOTSTRAP", "BOOTSTRAP"),

  /**
   * The '<em><b>STARTUP</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STARTUP_VALUE
   * @generated
   * @ordered
   */
  STARTUP(1, "STARTUP", "STARTUP"),

  /**
   * The '<em><b>MANUAL</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #MANUAL_VALUE
   * @generated
   * @ordered
   */
  MANUAL(2, "MANUAL", "MANUAL");

  /**
   * The '<em><b>BOOTSTRAP</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>BOOTSTRAP</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #BOOTSTRAP
   * @model
   * @generated
   * @ordered
   */
  public static final int BOOTSTRAP_VALUE = 0;

  /**
   * The '<em><b>STARTUP</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>STARTUP</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #STARTUP
   * @model
   * @generated
   * @ordered
   */
  public static final int STARTUP_VALUE = 1;

  /**
   * The '<em><b>MANUAL</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>MANUAL</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #MANUAL
   * @model
   * @generated
   * @ordered
   */
  public static final int MANUAL_VALUE = 2;

  /**
   * An array of all the '<em><b>Trigger</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final Trigger[] VALUES_ARRAY = new Trigger[] { BOOTSTRAP, STARTUP, MANUAL, };

  /**
   * A public read-only list of all the '<em><b>Trigger</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<Trigger> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Trigger</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Trigger get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Trigger result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Trigger</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Trigger getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      Trigger result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Trigger</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static Trigger get(int value)
  {
    switch (value)
    {
      case BOOTSTRAP_VALUE:
        return BOOTSTRAP;
      case STARTUP_VALUE:
        return STARTUP;
      case MANUAL_VALUE:
        return MANUAL;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private Trigger(int value, String name, String literal)
  {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLiteral()
  {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    return literal;
  }

  public static Set<Trigger> toSet(Trigger... triggers)
  {
    return intern(new HashSet<Trigger>(Arrays.asList(triggers)));
  }

  public static Set<Trigger> intern(Set<Trigger> triggerSet)
  {
    return CANONICAL_TRIGGER_SETS.get(triggerSet);
  }

  private static final Map<Set<Trigger>, Set<Trigger>> CANONICAL_TRIGGER_SETS = new HashMap<Set<Trigger>, Set<Trigger>>();

  public static final Map<Set<Trigger>, String> LITERALS;

  static
  {
    Map<Set<Trigger>, String> literals = new LinkedHashMap<Set<Trigger>, String>();
    for (int i = 0; i < 2; ++i)
    {
      for (int j = 0; j < 2; ++j)
      {
        for (int k = 0; k < 2; ++k)
        {
          Set<Trigger> value = new LinkedHashSet<Trigger>()
          {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString()
            {
              StringBuilder result = new StringBuilder();
              for (Trigger trigger : this)
              {
                if (result.length() != 0)
                {
                  result.append(' ');
                }

                result.append(trigger);
              }

              return result.toString();
            }
          };

          if (i == 1)
          {
            value.add(Trigger.BOOTSTRAP);
          }

          if (j == 1)
          {
            value.add(Trigger.STARTUP);
          }

          if (k == 1)
          {
            value.add(Trigger.MANUAL);
          }

          value = Collections.unmodifiableSet(value);
          literals.put(value, value.toString());
          CANONICAL_TRIGGER_SETS.put(value, value);
        }
      }
    }

    LITERALS = Collections.unmodifiableMap(literals);
  }

  public static final Set<Trigger> ALL_TRIGGERS = toSet(values());

  public static final Set<Trigger> IDE_TRIGGERS = toSet(Trigger.STARTUP, Trigger.MANUAL);

} // Trigger
