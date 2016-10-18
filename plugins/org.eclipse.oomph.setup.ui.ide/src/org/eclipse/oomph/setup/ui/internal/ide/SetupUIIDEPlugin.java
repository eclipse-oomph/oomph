/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.internal.ide;

import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.SimpleTemplateVariableResolver;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateVariableResolver;

import org.osgi.framework.BundleContext;

import java.util.Iterator;

/**
 * @author Eike Stepper
 */
public final class SetupUIIDEPlugin extends OomphUIPlugin
{
  public static final SetupUIIDEPlugin INSTANCE = new SetupUIIDEPlugin();

  private static Implementation plugin;

  public SetupUIIDEPlugin()
  {
    super(new ResourceLocator[] {});
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipseUIPlugin
  {
    public Implementation()
    {
      plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);

      // Modify the JDT's variable resolvers so that ${User} expands to a property author name,
      // not simply to the System.getProperty("user.name") which is the account name and generally not appropriate as the author name.
      if (!StringUtil.isEmpty(System.getProperty("jdt.user.name")))
      {
        try
        {
          Class<?> javaUIPluginClass = CommonPlugin.loadClass("org.eclipse.jdt.ui", "org.eclipse.jdt.internal.ui.JavaPlugin");
          Object javaUIPlugin = ReflectUtil.invokeMethod("getDefault", javaUIPluginClass);
          ContextTypeRegistry codeTemplateContextRegistry = ReflectUtil.invokeMethod("getCodeTemplateContextRegistry", javaUIPlugin);

          for (@SuppressWarnings("unchecked")
          Iterator<TemplateContextType> it = codeTemplateContextRegistry.contextTypes(); it.hasNext();)
          {
            TemplateContextType templateContextType = it.next();
            for (@SuppressWarnings("unchecked")
            Iterator<TemplateVariableResolver> it2 = templateContextType.resolvers(); it2.hasNext();)
            {
              TemplateVariableResolver templateVariableResolver = it2.next();
              if ("user".equals(templateVariableResolver.getType()))
              {
                templateContextType
                    .addResolver(new SimpleTemplateVariableResolver(templateVariableResolver.getType(), templateVariableResolver.getDescription())
                    {
                      @Override
                      protected String resolve(TemplateContext context)
                      {
                        return System.getProperty("jdt.user.name", System.getProperty("user.name"));
                      }
                    });
              }
            }
          }
        }
        catch (Exception ex)
        {
        }
      }
    }
  }
}
