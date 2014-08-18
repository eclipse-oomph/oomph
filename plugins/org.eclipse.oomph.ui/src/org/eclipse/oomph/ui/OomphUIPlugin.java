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
package org.eclipse.oomph.ui;

import org.eclipse.oomph.util.OomphPlugin;

import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @author Eike Stepper
 */
public abstract class OomphUIPlugin extends OomphPlugin
{
  protected OomphUIPlugin(ResourceLocator[] delegateResourceLocators)
  {
    super(delegateResourceLocators);
  }

  @Override
  protected AbstractUIPlugin getEclipsePlugin()
  {
    return (AbstractUIPlugin)getPluginResourceLocator();
  }

  public final IWorkbench getWorkbench()
  {
    return getEclipsePlugin().getWorkbench();
  }

  public final IPreferenceStore getPreferenceStore()
  {
    return getEclipsePlugin().getPreferenceStore();
  }

  public final IDialogSettings getDialogSettings()
  {
    return getEclipsePlugin().getDialogSettings();
  }

  public final IDialogSettings getDialogSettings(String sectionName)
  {
    IDialogSettings settings = getDialogSettings();
    return UIUtil.getOrCreateSection(settings, sectionName);
  }

  public final Image getSWTImage(ImageDescriptor descriptor)
  {
    return ExtendedImageRegistry.INSTANCE.getImage(descriptor);
  }

  public final Image getSWTImage(String key)
  {
    return ExtendedImageRegistry.INSTANCE.getImage(getImage(key));
  }

  public final ImageDescriptor getImageDescriptor(String key)
  {
    return ExtendedImageRegistry.INSTANCE.getImageDescriptor(getImage(key));
  }

  public static Font getFont(Font baseFont, Object object)
  {
    return ExtendedFontRegistry.INSTANCE.getFont(baseFont, object);
  }

  public static Font getNormalFont(Font baseFont)
  {
    return getFont(baseFont, IItemFontProvider.NORMAL_FONT);
  }

  public static Font getBoldFont(Font baseFont)
  {
    return getFont(baseFont, IItemFontProvider.BOLD_FONT);
  }

  public static Font getItalicFont(Font baseFont)
  {
    return getFont(baseFont, IItemFontProvider.ITALIC_FONT);
  }

  public static Font getBoldItalicFont(Font baseFont)
  {
    return getFont(baseFont, IItemFontProvider.BOLD_ITALIC_FONT);
  }
}
