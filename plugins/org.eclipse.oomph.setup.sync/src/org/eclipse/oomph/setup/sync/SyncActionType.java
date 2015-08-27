/**
 */
package org.eclipse.oomph.setup.sync;

import org.eclipse.emf.common.util.Enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Action Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.sync.SyncPackage#getSyncActionType()
 * @model
 * @generated
 */
public enum SyncActionType implements Enumerator
{
  /**
   * The '<em><b>None</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NONE_VALUE
   * @generated
   * @ordered
   */
  NONE(0, "None", "None"),

  /**
   * The '<em><b>Set Local</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SET_LOCAL_VALUE
   * @generated
   * @ordered
   */
  SET_LOCAL(1, "SetLocal", "SetLocal"),

  /**
   * The '<em><b>Set Remote</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SET_REMOTE_VALUE
   * @generated
   * @ordered
   */
  SET_REMOTE(2, "SetRemote", "SetRemote"),

  /**
   * The '<em><b>Remove</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #REMOVE_VALUE
   * @generated
   * @ordered
   */
  REMOVE(3, "Remove", "Remove"),

  /**
   * The '<em><b>Conflict</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CONFLICT_VALUE
   * @generated
   * @ordered
   */
  CONFLICT(4, "Conflict", "Conflict"),

  /**
   * The '<em><b>Exclude</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #EXCLUDE_VALUE
   * @generated
   * @ordered
   */
  EXCLUDE(5, "Exclude", "Exclude");

  /**
   * The '<em><b>None</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>None</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #NONE
   * @model name="None"
   * @generated
   * @ordered
   */
  public static final int NONE_VALUE = 0;

  /**
   * The '<em><b>Set Local</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Set Local</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SET_LOCAL
   * @model name="SetLocal"
   * @generated
   * @ordered
   */
  public static final int SET_LOCAL_VALUE = 1;

  /**
   * The '<em><b>Set Remote</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Set Remote</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SET_REMOTE
   * @model name="SetRemote"
   * @generated
   * @ordered
   */
  public static final int SET_REMOTE_VALUE = 2;

  /**
   * The '<em><b>Remove</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Remove</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #REMOVE
   * @model name="Remove"
   * @generated
   * @ordered
   */
  public static final int REMOVE_VALUE = 3;

  /**
   * The '<em><b>Conflict</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Conflict</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #CONFLICT
   * @model name="Conflict"
   * @generated
   * @ordered
   */
  public static final int CONFLICT_VALUE = 4;

  /**
   * The '<em><b>Exclude</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Exclude</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #EXCLUDE
   * @model name="Exclude"
   * @generated
   * @ordered
   */
  public static final int EXCLUDE_VALUE = 5;

  /**
   * An array of all the '<em><b>Action Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final SyncActionType[] VALUES_ARRAY = new SyncActionType[] { NONE, SET_LOCAL, SET_REMOTE, REMOVE, CONFLICT, EXCLUDE, };

  /**
   * A public read-only list of all the '<em><b>Action Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<SyncActionType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Action Type</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static SyncActionType get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      SyncActionType result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Action Type</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static SyncActionType getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      SyncActionType result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Action Type</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static SyncActionType get(int value)
  {
    switch (value)
    {
      case NONE_VALUE:
        return NONE;
      case SET_LOCAL_VALUE:
        return SET_LOCAL;
      case SET_REMOTE_VALUE:
        return SET_REMOTE;
      case REMOVE_VALUE:
        return REMOVE;
      case CONFLICT_VALUE:
        return CONFLICT;
      case EXCLUDE_VALUE:
        return EXCLUDE;
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
  private SyncActionType(int value, String name, String literal)
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

} // SyncActionType
