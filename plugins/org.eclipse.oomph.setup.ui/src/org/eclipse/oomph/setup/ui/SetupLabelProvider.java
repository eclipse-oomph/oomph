/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

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
      return getImageFromObject(new DisabledImageDescriptor(result));
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
      return "";
    }

    EClass eClass = object.eClass();
    if (eClass == SetupPackage.Literals.USER || eClass == SetupPackage.Literals.INSTALLATION || eClass == SetupPackage.Literals.WORKSPACE)
    {
      return SetupEditPlugin.getPlugin().getString("_UI_" + eClass.getName() + "_type");
    }

    if (object instanceof Index)
    {
      return "Catalog Index " + itemProvider.getText(object);
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

    return prefix + " - " + suffix;
  }

  /**
   * @author Ed Merks
   */
  public static final class DisabledImageDescriptor extends ImageDescriptor
  {
    private final Image image;

    public DisabledImageDescriptor(Image image)
    {
      this.image = image;
    }

    @Override
    public Image createImage()
    {
      return new Image(image.getDevice(), image, SWT.IMAGE_GRAY);
    }

    @Override
    public ImageData getImageData()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode()
    {
      return image.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj != null && obj.getClass() == DisabledImageDescriptor.class)
      {
        DisabledImageDescriptor other = (DisabledImageDescriptor)obj;
        if (other.image == image)
        {
          return true;
        }
      }

      return false;
    }
  }
}
