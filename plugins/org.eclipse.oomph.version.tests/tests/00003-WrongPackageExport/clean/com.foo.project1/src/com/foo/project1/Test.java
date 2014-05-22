package com.foo.project1;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * @author Eike Stepper
 */
public class Test
{
  public Test()
  {
    IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
    System.out.println(extensionRegistry);
  }
}
