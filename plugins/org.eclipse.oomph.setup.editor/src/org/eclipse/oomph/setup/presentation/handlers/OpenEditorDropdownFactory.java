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
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.actions.CompoundContributionItem;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class OpenEditorDropdownFactory extends CompoundContributionItem
{
  public OpenEditorDropdownFactory()
  {
  }

  @Override
  protected final IContributionItem[] getContributionItems()
  {
    return new IContributionItem[] { new DropDownItem() };
  }

  /**
   * @author Eike Stepper
   */
  private final class DropDownItem extends ContributionItem
  {
    public DropDownItem()
    {
      super(OpenEditorDropdownFactory.class.getSimpleName());
    }

    @Override
    public void fill(Menu menu, int index)
    {
      try
      {
        Set<EObject> eObjects = new HashSet<EObject>();
        final Set<EObject> parents = new LinkedHashSet<EObject>();

        final ComposedAdapterFactory adapterFactory = EMFUtil.createAdapterFactory();

        SetupContext setupContext = SetupContext.getSelf();

        User user = setupContext.getUser();
        createMenuItem(menu, adapterFactory, user);
        eObjects.add(user);

        if ("true".equals(PropertiesUtil.getProperty("org.eclipse.oomph.setup.legacy")))
        {
          Index legacyIndex = SetupFactory.eINSTANCE.createIndex();
          ((InternalEObject)legacyIndex).eSetProxyURI(URI
              .createURI("http://git.eclipse.org/c/cdo/cdo.git/plain/plugins/org.eclipse.emf.cdo.releng.setup/Configuration.setup"));
          createMenuItem(menu, adapterFactory, legacyIndex).setText("Legacy Catalog");
        }

        Installation installation = setupContext.getInstallation();
        if (installation != null)
        {
          createMenuItem(menu, adapterFactory, installation);
          eObjects.add(installation);
        }

        Workspace workspace = setupContext.getWorkspace();
        if (workspace != null)
        {
          createMenuItem(menu, adapterFactory, workspace);
          eObjects.add(workspace);
        }

        if (installation != null)
        {
          ProductVersion productVersion = installation.getProductVersion();
          if (productVersion != null)
          {
            new MenuItem(menu, SWT.SEPARATOR);
            createMenuItem(menu, adapterFactory, productVersion);
            eObjects.add(productVersion);
            addParents(parents, productVersion);
          }
        }

        if (workspace != null)
        {
          new MenuItem(menu, SWT.SEPARATOR);
          for (Stream stream : workspace.getStreams())
          {
            createMenuItem(menu, adapterFactory, stream);
            eObjects.add(stream);
            addParents(parents, stream);
          }
        }

        parents.removeAll(eObjects);
        if (!parents.isEmpty())
        {
          new MenuItem(menu, SWT.SEPARATOR);

          final Menu subMenu = new Menu(menu);
          subMenu.addMenuListener(new MenuAdapter()
          {
            @Override
            public void menuShown(MenuEvent e)
            {
              Menu m = (Menu)e.widget;
              MenuItem[] items = m.getItems();
              for (int i = 0; i < items.length; i++)
              {
                items[i].dispose();
              }

              for (EObject eObject : parents)
              {
                createMenuItem(subMenu, adapterFactory, eObject);
              }
            }
          });

          MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
          menuItem.setText("Parent Models");
          menuItem.setMenu(subMenu);
        }
      }
      catch (Exception ex)
      {
        SetupEditorPlugin.getPlugin().log(ex);
      }
    }

    private void addParents(Set<EObject> parentResources, EObject object)
    {
      EObject parent = object.eContainer();
      if (parent != null)
      {
        addParents(parentResources, parent);
      }

      if (object instanceof Scope || object instanceof Index)
      {
        parentResources.add(object);
      }
    }

    private MenuItem createMenuItem(Menu menu, ComposedAdapterFactory adapterFactory, final EObject object)
    {
      ItemProviderAdapter itemProvider = (ItemProviderAdapter)adapterFactory.adapt(object, IItemLabelProvider.class);

      final ImageDescriptor imageDescriptor = OpenEditorDropdownHandler.getLabelImage(itemProvider, object);
      final String text = OpenEditorDropdownHandler.getLabelText(itemProvider, object);

      MenuItem item = new MenuItem(menu, SWT.PUSH);
      item.setImage(ExtendedImageRegistry.INSTANCE.getImage(imageDescriptor));
      item.setText(text);
      item.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          Runnable runnable = new Runnable()
          {
            public void run()
            {
              OpenEditorDropdownHandler.openEditor(object);
            }
          };

          OpenEditorDropdownHandler.setAction(OpenEditorDropdownHandler.COMMAND_ID, imageDescriptor, text, runnable);
          runnable.run();
        }
      });

      return item;
    }
  }
}
