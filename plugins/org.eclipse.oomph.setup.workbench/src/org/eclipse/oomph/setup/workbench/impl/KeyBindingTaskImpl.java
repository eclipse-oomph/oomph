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
package org.eclipse.oomph.setup.workbench.impl;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.workbench.CommandParameter;
import org.eclipse.oomph.setup.workbench.KeyBindingContext;
import org.eclipse.oomph.setup.workbench.KeyBindingTask;
import org.eclipse.oomph.setup.workbench.WorkbenchPackage;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.Scheme;
import org.eclipse.jface.bindings.keys.KeyBinding;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.keys.IBindingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Key Binding Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl#getScheme <em>Scheme</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl#getContexts <em>Contexts</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl#getPlatform <em>Platform</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl#getLocale <em>Locale</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl#getKeys <em>Keys</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl#getCommand <em>Command</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.KeyBindingTaskImpl#getCommandParameters <em>Command Parameters</em>}</li>
 * </ul>
 *
 * @generated
 */
public class KeyBindingTaskImpl extends SetupTaskImpl implements KeyBindingTask
{
  /**
   * The default value of the '{@link #getScheme() <em>Scheme</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getScheme()
   * @generated
   * @ordered
   */
  protected static final String SCHEME_EDEFAULT = "org.eclipse.ui.defaultAcceleratorConfiguration";

  /**
   * The cached value of the '{@link #getScheme() <em>Scheme</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getScheme()
   * @generated
   * @ordered
   */
  protected String scheme = SCHEME_EDEFAULT;

  /**
   * The cached value of the '{@link #getContexts() <em>Contexts</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContexts()
   * @generated
   * @ordered
   */
  protected EList<KeyBindingContext> contexts;

  /**
   * The default value of the '{@link #getPlatform() <em>Platform</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPlatform()
   * @generated
   * @ordered
   */
  protected static final String PLATFORM_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPlatform() <em>Platform</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPlatform()
   * @generated
   * @ordered
   */
  protected String platform = PLATFORM_EDEFAULT;

  /**
   * The default value of the '{@link #getLocale() <em>Locale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocale()
   * @generated
   * @ordered
   */
  protected static final String LOCALE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLocale() <em>Locale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocale()
   * @generated
   * @ordered
   */
  protected String locale = LOCALE_EDEFAULT;

  /**
   * The default value of the '{@link #getKeys() <em>Keys</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKeys()
   * @generated
   * @ordered
   */
  protected static final String KEYS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getKeys() <em>Keys</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKeys()
   * @generated
   * @ordered
   */
  protected String keys = KEYS_EDEFAULT;

  /**
   * The default value of the '{@link #getCommand() <em>Command</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCommand()
   * @generated
   * @ordered
   */
  protected static final String COMMAND_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getCommand() <em>Command</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCommand()
   * @generated
   * @ordered
   */
  protected String command = COMMAND_EDEFAULT;

  /**
   * The cached value of the '{@link #getCommandParameters() <em>Command Parameters</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCommandParameters()
   * @generated
   * @ordered
   */
  protected EList<CommandParameter> commandParameters;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected KeyBindingTaskImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return WorkbenchPackage.Literals.KEY_BINDING_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getScheme()
  {
    return scheme;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setScheme(String newScheme)
  {
    String oldScheme = scheme;
    scheme = newScheme;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, WorkbenchPackage.KEY_BINDING_TASK__SCHEME, oldScheme, scheme));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<KeyBindingContext> getContexts()
  {
    if (contexts == null)
    {
      contexts = new EObjectContainmentEList<KeyBindingContext>(KeyBindingContext.class, this, WorkbenchPackage.KEY_BINDING_TASK__CONTEXTS);
    }
    return contexts;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPlatform()
  {
    return platform;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPlatform(String newPlatform)
  {
    String oldPlatform = platform;
    platform = newPlatform;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, WorkbenchPackage.KEY_BINDING_TASK__PLATFORM, oldPlatform, platform));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLocale()
  {
    return locale;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocale(String newLocale)
  {
    String oldLocale = locale;
    locale = newLocale;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, WorkbenchPackage.KEY_BINDING_TASK__LOCALE, oldLocale, locale));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getKeys()
  {
    return keys;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setKeys(String newKeys)
  {
    String oldKeys = keys;
    keys = newKeys;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, WorkbenchPackage.KEY_BINDING_TASK__KEYS, oldKeys, keys));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getCommand()
  {
    return command;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCommand(String newCommand)
  {
    String oldCommand = command;
    command = newCommand;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, WorkbenchPackage.KEY_BINDING_TASK__COMMAND, oldCommand, command));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<CommandParameter> getCommandParameters()
  {
    if (commandParameters == null)
    {
      commandParameters = new EObjectContainmentEList<CommandParameter>(CommandParameter.class, this, WorkbenchPackage.KEY_BINDING_TASK__COMMAND_PARAMETERS);
    }
    return commandParameters;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case WorkbenchPackage.KEY_BINDING_TASK__CONTEXTS:
        return ((InternalEList<?>)getContexts()).basicRemove(otherEnd, msgs);
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND_PARAMETERS:
        return ((InternalEList<?>)getCommandParameters()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case WorkbenchPackage.KEY_BINDING_TASK__SCHEME:
        return getScheme();
      case WorkbenchPackage.KEY_BINDING_TASK__CONTEXTS:
        return getContexts();
      case WorkbenchPackage.KEY_BINDING_TASK__PLATFORM:
        return getPlatform();
      case WorkbenchPackage.KEY_BINDING_TASK__LOCALE:
        return getLocale();
      case WorkbenchPackage.KEY_BINDING_TASK__KEYS:
        return getKeys();
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND:
        return getCommand();
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND_PARAMETERS:
        return getCommandParameters();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case WorkbenchPackage.KEY_BINDING_TASK__SCHEME:
        setScheme((String)newValue);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__CONTEXTS:
        getContexts().clear();
        getContexts().addAll((Collection<? extends KeyBindingContext>)newValue);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__PLATFORM:
        setPlatform((String)newValue);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__LOCALE:
        setLocale((String)newValue);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__KEYS:
        setKeys((String)newValue);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND:
        setCommand((String)newValue);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND_PARAMETERS:
        getCommandParameters().clear();
        getCommandParameters().addAll((Collection<? extends CommandParameter>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case WorkbenchPackage.KEY_BINDING_TASK__SCHEME:
        setScheme(SCHEME_EDEFAULT);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__CONTEXTS:
        getContexts().clear();
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__PLATFORM:
        setPlatform(PLATFORM_EDEFAULT);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__LOCALE:
        setLocale(LOCALE_EDEFAULT);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__KEYS:
        setKeys(KEYS_EDEFAULT);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND:
        setCommand(COMMAND_EDEFAULT);
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND_PARAMETERS:
        getCommandParameters().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case WorkbenchPackage.KEY_BINDING_TASK__SCHEME:
        return SCHEME_EDEFAULT == null ? scheme != null : !SCHEME_EDEFAULT.equals(scheme);
      case WorkbenchPackage.KEY_BINDING_TASK__CONTEXTS:
        return contexts != null && !contexts.isEmpty();
      case WorkbenchPackage.KEY_BINDING_TASK__PLATFORM:
        return PLATFORM_EDEFAULT == null ? platform != null : !PLATFORM_EDEFAULT.equals(platform);
      case WorkbenchPackage.KEY_BINDING_TASK__LOCALE:
        return LOCALE_EDEFAULT == null ? locale != null : !LOCALE_EDEFAULT.equals(locale);
      case WorkbenchPackage.KEY_BINDING_TASK__KEYS:
        return KEYS_EDEFAULT == null ? keys != null : !KEYS_EDEFAULT.equals(keys);
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND:
        return COMMAND_EDEFAULT == null ? command != null : !COMMAND_EDEFAULT.equals(command);
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND_PARAMETERS:
        return commandParameters != null && !commandParameters.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (scheme: ");
    result.append(scheme);
    result.append(", platform: ");
    result.append(platform);
    result.append(", locale: ");
    result.append(locale);
    result.append(", keys: ");
    result.append(keys);
    result.append(", command: ");
    result.append(command);
    result.append(')');
    return result.toString();
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    IBindingService bindingService = (IBindingService)PlatformUI.getWorkbench().getService(IBindingService.class);
    Binding[] bindings = bindingService.getBindings();

    for (KeyBindingContext keyBindingContext : getContexts())
    {
      if (isNeeded(keyBindingContext, bindings))
      {
        return true;
      }
    }

    return false;
  }

  private boolean isNeeded(KeyBindingContext keyBindingContext, Binding[] bindings) throws Exception
  {
    for (int i = 0; i < bindings.length; i++)
    {
      Binding binding = bindings[i];
      if (binding instanceof KeyBinding)
      {
        KeyBinding keyBinding = (KeyBinding)binding;
        if (isEqual(keyBindingContext, keyBinding))
        {
          return false;
        }
      }
    }

    return true;
  }

  private boolean isEqual(KeyBindingContext keyBindingContext, KeyBinding keyBinding) throws Exception
  {
    if (!ObjectUtil.equals(keyBinding.getSchemeId(), getScheme()))
    {
      return false;
    }

    if (!ObjectUtil.equals(keyBinding.getContextId(), keyBindingContext.getID()))
    {
      return false;
    }

    if (!ObjectUtil.equals(keyBinding.getPlatform(), getPlatform()))
    {
      return false;
    }

    if (!ObjectUtil.equals(keyBinding.getLocale(), getLocale()))
    {
      return false;
    }

    KeySequence keySequence = KeySequence.getInstance(getKeys());
    if (!ObjectUtil.equals(keyBinding.getKeySequence(), keySequence))
    {
      return false;
    }

    ParameterizedCommand parameterizedCommand = keyBinding.getParameterizedCommand();
    if (parameterizedCommand == null || !ObjectUtil.equals(parameterizedCommand.getId(), getCommand()))
    {
      return false;
    }

    if (!ObjectUtil.equals(parameterizedCommand.getParameterMap(), getCommandParameterMap()))
    {
      return false;
    }

    return true;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    final IBindingService bindingService = (IBindingService)PlatformUI.getWorkbench().getService(IBindingService.class);
    final List<Binding> bindings = new ArrayList<Binding>(Arrays.asList(bindingService.getBindings()));

    for (KeyBindingContext keyBindingContext : getContexts())
    {
      KeyBinding binding = new KeyBinding(KeySequence.getInstance(getKeys()), getParameterizedCommand(), getScheme(), keyBindingContext.getID(), getLocale(),
          getPlatform(), null, Binding.USER);
      bindings.add(binding);
    }

    final Exception[] exception = { null };
    Display.getDefault().syncExec(new Runnable()
    {
      public void run()
      {
        try
        {
          Scheme activeScheme = bindingService.getActiveScheme();
          bindingService.savePreferences(activeScheme, bindings.toArray(new Binding[bindings.size()]));
        }
        catch (IOException ex)
        {
          exception[0] = ex;
        }
      }
    });

    if (exception[0] != null)
    {
      throw exception[0];
    }
  }

  private ParameterizedCommand getParameterizedCommand() throws NotDefinedException
  {
    ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
    Command command = commandService.getCommand(getCommand());

    @SuppressWarnings("rawtypes")
    Map params = getCommandParameterMap();

    return ParameterizedCommand.generateCommand(command, params);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private Map getCommandParameterMap()
  {
    Map params = new HashMap();
    for (CommandParameter commandParameter : getCommandParameters())
    {
      params.put(commandParameter.getID(), commandParameter.getValue());
    }

    return params;
  }

} // KeyBindingTaskImpl
