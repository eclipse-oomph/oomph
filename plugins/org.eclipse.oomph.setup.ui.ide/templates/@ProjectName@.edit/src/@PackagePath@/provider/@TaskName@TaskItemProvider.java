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
package @PackageName@.provider;

import @PackageName@.@TaskName@Task;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;

/**
 * This is the item provider adapter for a {@link @PackageName@.@TaskName@Task} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class @TaskName@TaskItemProvider extends SetupTaskItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public @TaskName@TaskItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTextGen(Object object)
  {
    String label = ((@TaskName@Task)object).toString();
    return label == null || label.length() == 0 ? getString("_UI_@TaskName@Task_type") : getString("_UI_@TaskName@Task_type") + " " +label;
  }

  @Override
  public String getText(Object object)
  {
    String label = getTextGen(object);
    
    String type = getString("_UI_@TaskName@Task_type");
    return label.startsWith(type + " ") && !label.equals(type) ? label.substring(type.length()).trim() : label;
  }
  
}
