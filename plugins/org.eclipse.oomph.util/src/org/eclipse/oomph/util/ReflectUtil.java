/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Christian W. Damus (CEA) - bug 376620: switch on primitive types
 */
package org.eclipse.oomph.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Various static helper methods for dealing with Java reflection.
 *
 * @author Eike Stepper
 */
public final class ReflectUtil
{
  private ReflectUtil()
  {
  }

  public static <T> Constructor<T> getConstructor(Class<T> c, Class<?>... parameterTypes)
  {
    try
    {
      return c.getDeclaredConstructor(parameterTypes);
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new ReflectionException(ex);
    }
  }

  public static <T> T newInstance(Constructor<T> constructor, Object... arguments)
  {
    boolean accessible = constructor.isAccessible();
    if (!accessible)
    {
      constructor.setAccessible(true);
    }

    try
    {
      return constructor.newInstance(arguments);
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new ReflectionException(ex);
    }
    finally
    {
      if (!accessible)
      {
        constructor.setAccessible(false);
      }
    }
  }

  public static Method getMethod(Class<?> c, String methodName, Class<?>... parameterTypes)
  {
    try
    {
      try
      {
        return c.getDeclaredMethod(methodName, parameterTypes);
      }
      catch (NoSuchMethodException ex)
      {
        Class<?> superclass = c.getSuperclass();
        if (superclass != null)
        {
          return getMethod(superclass, methodName, parameterTypes);
        }

        throw ex;
      }
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new ReflectionException(ex);
    }
  }

  public static Object invokeMethod(Method method, Object target, Object... arguments)
  {
    boolean accessible = method.isAccessible();
    if (!accessible)
    {
      method.setAccessible(true);
    }

    try
    {
      return method.invoke(target, arguments);
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new ReflectionException(ex);
    }
    finally
    {
      if (!accessible)
      {
        method.setAccessible(false);
      }
    }
  }

  public static Field getField(Class<?> c, String fieldName)
  {
    try
    {
      try
      {
        return c.getDeclaredField(fieldName);
      }
      catch (NoSuchFieldException ex)
      {
        Class<?> superclass = c.getSuperclass();
        if (superclass != null)
        {
          return getField(superclass, fieldName);
        }

        return null;
      }
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new ReflectionException(ex);
    }
  }

  public static Object getValue(Field field, Object target)
  {
    if (!field.isAccessible())
    {
      field.setAccessible(true);
    }

    try
    {
      return field.get(target);
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new ReflectionException(ex);
    }
  }

  public static void setValue(Field field, Object target, Object value)
  {
    setValue(field, target, value, false);
  }

  public static void setValue(Field field, Object target, Object value, boolean force)
  {
    if (!field.isAccessible())
    {
      field.setAccessible(true);
    }

    Field modifiersField = null;
    boolean[] finalModified = { false };

    try
    {
      if ((field.getModifiers() & Modifier.FINAL) != 0 && force)
      {
        modifiersField = Field.class.getDeclaredField("modifiers");
        if (!modifiersField.isAccessible())
        {
          modifiersField.setAccessible(true);
        }

        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        finalModified[0] = true;
      }

      Class<?> type = field.getType();
      if (type.isPrimitive())
      {
        if (boolean.class == type)
        {
          field.setBoolean(target, (Boolean)value);
        }
        else if (char.class == type)
        {
          field.setChar(target, (Character)value);
        }
        else if (byte.class == type)
        {
          field.setByte(target, (Byte)value);
        }
        else if (short.class == type)
        {
          field.setShort(target, (Short)value);
        }
        else if (int.class == type)
        {
          field.setInt(target, (Integer)value);
        }
        else if (long.class == type)
        {
          field.setLong(target, (Long)value);
        }
        else if (float.class == type)
        {
          field.setFloat(target, (Float)value);
        }
        else if (double.class == type)
        {
          field.setDouble(target, (Double)value);
        }
      }
      else
      {
        field.set(target, value);
      }
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new ReflectionException(ex);
    }
    finally
    {
      if (finalModified[0])
      {
        try
        {
          modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL);
        }
        catch (IllegalAccessException ex)
        {
          throw new ReflectionException(ex);
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ReflectionException extends RuntimeException
  {
    private static final long serialVersionUID = 1L;

    public ReflectionException()
    {
    }

    public ReflectionException(String message, Throwable cause)
    {
      super(message, cause);
    }

    public ReflectionException(String message)
    {
      super(message);
    }

    public ReflectionException(Throwable cause)
    {
      super(cause);
    }
  }
}
