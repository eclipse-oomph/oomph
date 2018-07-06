/*
 * Copyright (c) 2014, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package @QualifiedPackageName@.impl;

import @QualifiedPackageName@.@TaskName@Task;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.emf.ecore.EClass;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>@Task Name@ Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class @TaskName@TaskImpl extends SetupTaskImpl implements @TaskName@Task
{
  private static final int PRIORITY = @Priority@;
  
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected @TaskName@TaskImpl()
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
    return @TaskName@Package.Literals.@TASK_NAME@_TASK;
  }

  @Override
  public int getPriority()
  {
    return PRIORITY;
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    // TODO Implement @TaskName@TaskImpl.isNeeded()
    return true;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    // TODO Implement @TaskName@TaskImpl.perform()
    context.log("Implement @TaskName@TaskImpl.perform()");
  }

  @Override
  public void dispose()
  {
    // TODO Implement @TaskName@TaskImpl.dispose() or remove this override if not needed
  }

} // @TaskName@TaskImpl
