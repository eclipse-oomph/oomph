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
package org.eclipse.oomph.setup.doc.concepts;

import org.eclipse.oomph.setup.doc.concepts.DocTask.DocP2Task;
import org.eclipse.oomph.setup.doc.concepts.DocTask.DocTargletTask;

/**
 * Bundle Pools
 * <p>
 * Eclipse's p2 technology supports the concept of a <a href="http://wiki.eclipse.org/Equinox/p2/Getting_Started#Bundle_pooling">bundle pool</a>.
 * Bundle pools help to dramatically reduce disk footprint
 * and to eliminate repeated downloads of the same bundles and features,
 * thereby dramatically improving the performance of software updates and target platform provisioning.
 * Oomph makes heavy use of this technology for both its {@link DocP2Task p2} tasks and it {@link DocTargletTask targlet} tasks.
 * Further,
 * Oomph provides a technology layer on top of p2
 * to improve the behavior and performance of caching
 * allowing Oomph to provide offline support for installing and updating installations and target platforms.
 * </p>
 *
 * @number 900
 */
public abstract class DocBundlePool
{
}
