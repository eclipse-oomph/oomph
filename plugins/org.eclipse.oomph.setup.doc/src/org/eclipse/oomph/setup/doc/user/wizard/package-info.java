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

/**
 * Wizards and Wizard Pages
 * <p>
 * Oomph provides three wizards to drive the automated installation and provisioning process.
 * These wizards reuse the same underlying pages as follows:
 * <figure>
 * {@image wizards.png}
 * </figure>
 * They makes heavy use of Internet-hosted {@linkplain org.eclipse.oomph.setup.doc.concepts.DocSetupResource resources},
 * but these resources are cached locally on each download,
 * so once cached,
 * the wizards can function offline based on these cached instances.
 * </p>
 *
 * @number 3
 */
package org.eclipse.oomph.setup.doc.user.wizard;

