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
package org.eclipse.oomph.jreinfo;

import org.eclipse.core.runtime.Platform;

/**
 * @author Stepper
 */
public final class JREInfo {
	private static final OSType OS_TYPE = getOSType();

	public String javaHome;

	public int jdk;

	public JREInfo next;

	public static JREInfo getAll() {
		switch (OS_TYPE) {
		case Win:
			return getAllWin();

		case Mac:
			return getAllMac();

		case Linux:
			return getAllLinux();

		default:
			//$FALL-THROUGH$
		}

		return null;
	}

	private static native JREInfo getAllWin();

	private static JREInfo getAllMac() {
		// TODO Auto-generated method stub
		return null;
	}

	private static JREInfo getAllLinux() {
		// TODO Auto-generated method stub
		return null;
	}

	private static OSType getOSType() {
		String os = Platform.getOS();

		if (Platform.OS_WIN32.equals(os)) {
			System.loadLibrary("jreinfo.dll");
			return OSType.Win;
		}

		if (Platform.OS_MACOSX.equals(os)) {
			return OSType.Mac;
		}

		if (Platform.OS_LINUX.equals(os)) {
			return OSType.Linux;
		}

		return OSType.Unsupported;
	}
}