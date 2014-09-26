package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.core.ITargletContainer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import java.io.IOException;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class TargletContainerResourceFactory implements Factory
{
  public static final String PROTOCOL_NAME = "targlet_container";

  public static final String OPTION_MIRRORS = "MIRRORS";

  public TargletContainerResourceFactory()
  {
  }

  public Resource createResource(URI uri)
  {
    final String id = uri.opaquePart();
    final ITargletContainer targletContainer = TargletContainerDescriptorManager.getContainer(id);
    final org.eclipse.oomph.targlets.TargletContainer wrapper = TargletFactory.eINSTANCE.createTargletContainer();
    wrapper.setID(id);

    Resource resource = new ResourceImpl(uri)
    {
      @Override
      public void save(Map<?, ?> options) throws IOException
      {
        if (targletContainer != null)
        {
          boolean mirrors = options != null && options.containsKey(OPTION_MIRRORS) ? (Boolean)options.get(OPTION_MIRRORS)
              : defaultSaveOptions != null ? (Boolean)defaultSaveOptions.get(OPTION_MIRRORS) : Boolean.TRUE;

          try
          {
            targletContainer.setTarglets(wrapper.getTarglets());
            targletContainer.forceUpdate(false, mirrors, new NullProgressMonitor());
          }
          catch (CoreException ex)
          {
            TargletsCorePlugin.INSTANCE.log(ex);
          }
        }
      }
    };

    if (targletContainer != null)
    {
      wrapper.getTarglets().addAll(targletContainer.getTarglets());
    }

    resource.getContents().add(wrapper);
    return resource;
  }
}
