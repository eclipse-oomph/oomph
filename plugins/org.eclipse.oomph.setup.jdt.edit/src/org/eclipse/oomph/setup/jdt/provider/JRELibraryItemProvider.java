/*
 * Copyright (c) 2017 Adrian Price and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Adrian Price <aprice@tibco.com> - initial API and implementation
 */
package org.eclipse.oomph.setup.jdt.provider;

import org.eclipse.oomph.setup.jdt.JRELibrary;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.jdt.JRELibrary} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class JRELibraryItemProvider extends ItemProviderAdapter
    implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JRELibraryItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String getText(Object object)
  {
    JRELibrary jreLibrary = (JRELibrary)object;
    String libraryPath = jreLibrary.getLibraryPath();
    if (StringUtil.isEmpty(libraryPath))
    {
      return getString("_UI_JRELibrary_type");
    }

    String label = libraryPath;

    String externalAnnotationsPath = jreLibrary.getExternalAnnotationsPath();
    if (externalAnnotationsPath != null)
    {
      if (externalAnnotationsPath.length() == 0)
      {
        label += " ~ \"\"";
      }
      else
      {
        label += " ~ " + externalAnnotationsPath;
      }
    }
    return label;
  }

}
