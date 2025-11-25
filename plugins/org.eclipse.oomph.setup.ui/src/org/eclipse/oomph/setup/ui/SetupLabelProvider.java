/*
 * Copyright (c) 2014-2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.provider.SetupEditPlugin;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import java.net.URL;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class SetupLabelProvider extends AdapterFactoryLabelProvider.FontAndColorProvider
{
  private final Color DARK_GRAY;

  public SetupLabelProvider(Viewer viewer)
  {
    this(BaseEditUtil.createAdapterFactory(), viewer);
  }

  public SetupLabelProvider(AdapterFactory adapterFactory, Viewer viewer)
  {
    super(adapterFactory, viewer);
    DARK_GRAY = viewer.getControl().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
  }

  @Override
  public Color getForeground(Object object)
  {
    if (isDisabled(object))
    {
      return DARK_GRAY;
    }

    return super.getForeground(object);
  }

  @Override
  public Font getFont(Object object)
  {
    Font font = super.getFont(object);
    return font == null ? getDefaultFont() : font;
  }

  @Override
  public Image getImage(Object object)
  {
    Image result = super.getImage(object);
    if (isDisabled(object))
    {
      return getImageFromObject(ImageDescriptor.createWithFlags(ImageDescriptor.createFromImage(result), SWT.IMAGE_DISABLE));
    }

    return result;
  }

  public static boolean isDisabled(Object object)
  {
    if (object instanceof EObject)
    {
      EObject eObject = (EObject)object;
      if (eObject instanceof SetupTask)
      {
        SetupTask setupTask = (SetupTask)eObject;
        if (setupTask.isDisabled())
        {
          return true;
        }
      }

      EObject eContainer = eObject.eContainer();
      if (eContainer != null)
      {
        return isDisabled(eContainer);
      }
    }
    else if (object instanceof IWrapperItemProvider)
    {
      return isDisabled(((IWrapperItemProvider)object).getOwner());
    }

    return false;
  }

  public static ImageDescriptor getImageDescriptor(ItemProviderAdapter itemProvider, EObject object)
  {
    Object key = itemProvider.getImage(object);
    if (key instanceof ComposedImage)
    {
      ComposedImage composedImage = (ComposedImage)key;
      List<Object> images = composedImage.getImages();
      key = images.get(0);
    }

    return key instanceof URL ? ImageDescriptor.createFromURL((URL)key) : ExtendedImageRegistry.INSTANCE.getImageDescriptor(key);
  }

  public static String getText(ItemProviderAdapter itemProvider, EObject object)
  {
    if (object == null)
    {
      return ""; //$NON-NLS-1$
    }

    EClass eClass = object.eClass();
    if (eClass == SetupPackage.Literals.USER || eClass == SetupPackage.Literals.INSTALLATION || eClass == SetupPackage.Literals.WORKSPACE)
    {
      return SetupEditPlugin.getPlugin().getString("_UI_" + eClass.getName() + "_type"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    if (object instanceof Index)
    {
      return NLS.bind(Messages.SetupLabelProvider_catalogIndex, itemProvider.getText(object));
    }

    if (object instanceof Product)
    {
      Product product = (Product)object;
      return compose(getText(itemProvider, product.getProductCatalog()), itemProvider.getText(product));
    }

    if (object instanceof ProductVersion)
    {
      ProductVersion version = (ProductVersion)object;
      return compose(getText(itemProvider, version.getProduct()), itemProvider.getText(version));
    }

    if (object instanceof Project)
    {
      Project project = (Project)object;
      return compose(getText(itemProvider, project.getProjectContainer()), itemProvider.getText(project));
    }

    if (object instanceof Stream)
    {
      Stream stream = (Stream)object;
      return compose(getText(itemProvider, stream.getProject()), itemProvider.getText(stream));
    }

    return itemProvider.getText(object);
  }

  private static String compose(String prefix, String suffix)
  {
    if (StringUtil.isEmpty(prefix))
    {
      return suffix;
    }

    return prefix + " - " + suffix; //$NON-NLS-1$
  }
}
