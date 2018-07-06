/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.doc.concepts;

import org.eclipse.oomph.setup.doc.concepts.DocTask.DocTrigger;
import org.eclipse.oomph.setup.doc.user.wizard.DocConfirmationPage;

/**
 * Task Execution
 * <p>
 * The {@link DocTaskComposition gathered} task list is prepared to {@link DocTask#performance perform} the specified tasks.
 * This list is presented to the user on the setup wizard's {@link DocConfirmationPage progressPage page}.
 * Initially,
 * each task in the list is visited
 * to determine if it needs to perform.
 * That determination is generally influenced by the {@link DocTrigger trigger}.
 * Tasks that don't need to perform are logically removed from the list
 * to induce the so called needed task list.
 * The progressPage page allows the user to see either the full list or the needed task list.
 *
 * Each task remaining in the reduced list of needed tasks
 * is then performed.
 * </p>
 *
 * @number 600
 */
public class DocTaskExecution
{
}
