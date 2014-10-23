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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;

import java.lang.reflect.Method;

/**
 * @author Eike Stepper
 */
public final class CocoaUtil
{
  private static final int ABOUT_ITEM_INDEX = 0;

  private static final int PREFERENCES_ITEM_INDEX = 2;

  private static final int QUIT_ITEM_INDEX = 10;

  private static final String TYPES = "@:@";

  private static Object invoke(Method method, Object target, Object... arguments)
  {
    return ReflectUtil.invokeMethod(method, target, arguments);
  }

  public static void register(Display display, final Runnable about, final Runnable preferences, final Runnable quit)
  {
    try
    {
      final Class<?> NSString = Class.forName("org.eclipse.swt.internal.cocoa.NSString");
      final Method NSString_stringWith = ReflectUtil.getMethod(NSString, "stringWith", String.class);

      final Class<?> Callback = Class.forName("org.eclipse.swt.internal.Callback");
      final Method Callback_getAddress = ReflectUtil.getMethod(Callback, "getAddress");
      final Method Callback_dispose = ReflectUtil.getMethod(Callback, "dispose");

      final Class<?> OS = Class.forName("org.eclipse.swt.internal.cocoa.OS");
      final Method OS_sel_registerName = ReflectUtil.getMethod(OS, "sel_registerName", String.class);
      final Method OS_objc_lookUpClass = ReflectUtil.getMethod(OS, "objc_lookUpClass", String.class);
      final Method OS_class_addMethod = ReflectUtil.getMethod(OS, "class_addMethod", long.class, long.class, long.class, String.class);

      final Class<?> NSApplication = Class.forName("org.eclipse.swt.internal.cocoa.NSApplication");
      final Method NSApplication_sharedApplication = ReflectUtil.getMethod(NSApplication, "sharedApplication");
      final Method NSApplication_mainMenu = ReflectUtil.getMethod(NSApplication, "mainMenu");

      final Class<?> NSMenu = Class.forName("org.eclipse.swt.internal.cocoa.NSMenu");
      final Method NSMenu_itemAtIndex = ReflectUtil.getMethod(NSMenu, "itemAtIndex", long.class);

      final Class<?> NSMenuItem = Class.forName("org.eclipse.swt.internal.cocoa.NSMenuItem");
      final Method NSMenuItem_submenu = ReflectUtil.getMethod(NSMenuItem, "submenu");
      final Method NSMenuItem_setTitle = ReflectUtil.getMethod(NSMenuItem, "setTitle", NSString);
      final Method NSMenuItem_setEnabled = ReflectUtil.getMethod(NSMenuItem, "setEnabled", boolean.class);
      final Method NSMenuItem_setAction = ReflectUtil.getMethod(NSMenuItem, "setAction", long.class);

      final long aboutItemSelected = (Long)invoke(OS_sel_registerName, null, "aboutMenuItemSelected:");
      final long preferencesItemSelected = (Long)invoke(OS_sel_registerName, null, "preferencesMenuItemSelected:");
      final long quitItemSelected = (Long)invoke(OS_sel_registerName, null, "quitMenuItemSelected:");

      Object callbackTarget = new Object()
      {
        @SuppressWarnings("unused")
        long actionProc(long id, long sel, long arg2)
        {
          if (sel == aboutItemSelected)
          {
            if (about != null)
            {
              about.run();
            }
          }
          else if (sel == preferencesItemSelected)
          {
            if (preferences != null)
            {
              preferences.run();
            }
          }
          else if (sel == quitItemSelected)
          {
            if (quit != null)
            {
              quit.run();
            }
          }

          return 0;
        }
      };

      @SuppressWarnings("restriction")
      final org.eclipse.swt.internal.Callback callback = new org.eclipse.swt.internal.Callback(callbackTarget, "actionProc", 3);
      final long callbackPointer = (Long)invoke(Callback_getAddress, callback);
      if (callbackPointer == 0)
      {
        throw new SWTException("No more callbacks");
      }

      final long classPointer = (Long)invoke(OS_objc_lookUpClass, null, "SWTApplicationDelegate");
      invoke(OS_class_addMethod, null, classPointer, aboutItemSelected, callbackPointer, TYPES);
      invoke(OS_class_addMethod, null, classPointer, preferencesItemSelected, callbackPointer, TYPES);
      invoke(OS_class_addMethod, null, classPointer, quitItemSelected, callbackPointer, TYPES);

      final Object application = invoke(NSApplication_sharedApplication, null);
      final Object mainMenu = invoke(NSApplication_mainMenu, application);
      final Object mainItem = invoke(NSMenu_itemAtIndex, mainMenu, (long)0);
      final Object appMenu = invoke(NSMenuItem_submenu, mainItem);

      final Object aboutItem = invoke(NSMenu_itemAtIndex, appMenu, (long)ABOUT_ITEM_INDEX);
      final Object preferencesItem = invoke(NSMenu_itemAtIndex, appMenu, (long)PREFERENCES_ITEM_INDEX);
      final Object quitItem = invoke(NSMenu_itemAtIndex, appMenu, (long)QUIT_ITEM_INDEX);

      final String appName = Display.getAppName();
      if (appName != null)
      {
        final Object aboutLabel = invoke(NSString_stringWith, null, "About " + appName);
        invoke(NSMenuItem_setTitle, aboutItem, aboutLabel);

        final Object quitLabel = invoke(NSString_stringWith, null, "Quit " + appName);
        invoke(NSMenuItem_setTitle, quitItem, quitLabel);
      }

      invoke(NSMenuItem_setAction, aboutItem, aboutItemSelected);
      invoke(NSMenuItem_setAction, preferencesItem, preferencesItemSelected);
      invoke(NSMenuItem_setAction, quitItem, quitItemSelected);

      invoke(NSMenuItem_setEnabled, preferencesItem, true);
      invoke(NSMenuItem_setEnabled, quitItem, true);

      display.disposeExec(new Runnable()
      {
        public void run()
        {
          invoke(Callback_dispose, callback);
        }
      });
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }
  }
}
