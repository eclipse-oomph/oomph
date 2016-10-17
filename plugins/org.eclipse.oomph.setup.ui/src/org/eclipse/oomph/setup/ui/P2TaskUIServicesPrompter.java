/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.setup.p2.util.P2TaskUISevices;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OomphPlugin.Preference;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import java.util.Set;

/**
 * @author Ed Merks
 */
public class P2TaskUIServicesPrompter extends P2TaskUISevices
{
  /**
   * The action to take for missing requirements.
   *
   * @author Ed Merks
   */
  public enum Action
  {
    PROMPT, ALWAYS, NEVER
  }

  public static Action getActionPreference()
  {
    Preference preference = SetupUIPlugin.INSTANCE.getInstancePreference(SetupUIPlugin.PREF_P2_STARTUP_TASKS);
    String value = preference.get(Action.PROMPT.name());
    return Action.valueOf(value);
  }

  public static void setActionPreference(Action action)
  {
    Preference preference = SetupUIPlugin.INSTANCE.getInstancePreference(SetupUIPlugin.PREF_P2_STARTUP_TASKS);
    preference.set(action.name());
  }

  @Override
  public boolean handleUnsatisfiedRequirements(Set<Requirement> unsatisifiedRequirements, Set<IInstallableUnit> availableIUs)
  {
    Action action = getActionPreference();
    switch (action)
    {
      case ALWAYS:
      {
        return true;
      }
      case NEVER:
      {
        return false;
      }
      case PROMPT:
      default:
      {
        return promptUnsatisifiedRequirements(unsatisifiedRequirements, availableIUs);
      }
    }
  }

  public boolean promptUnsatisifiedRequirements(final Set<Requirement> unsatisifiedRequirements, final Set<IInstallableUnit> availableIUs)
  {
    final boolean[] result = new boolean[1];
    result[0] = true;
    UIUtil.syncExec(new Runnable()
    {
      public void run()
      {
        Shell shell = getShell();
        if (shell != null)
        {
          UnsatisifiedRequirementsPrompter unsatisifiedRequirementsPrompter = new UnsatisifiedRequirementsPrompter(shell, unsatisifiedRequirements,
              availableIUs);
          int open = unsatisifiedRequirementsPrompter.open();
          boolean install = open == IDialogConstants.OK_ID;
          result[0] = install;
          if (unsatisifiedRequirementsPrompter.isRemember())
          {
            setActionPreference(install ? Action.ALWAYS : Action.NEVER);
          }
        }
      }
    });

    return result[0];
  }

  protected Shell getShell()
  {
    try
    {
      return UIUtil.getShell();
    }
    catch (Throwable ex)
    {
      return null;
    }
  }

  protected static class UnsatisifiedRequirementsPrompter extends AbstractConfirmDialog
  {
    private final Set<Requirement> unsatisifiedRequirements;

    private final Set<IInstallableUnit> availableIUs;

    public UnsatisifiedRequirementsPrompter(Shell parentShell, Set<Requirement> unsatisifiedRequirements, Set<IInstallableUnit> availableIUs)
    {
      super(parentShell, "Requirements Update", 700, 500, "Remember my decision");
      this.unsatisifiedRequirements = unsatisifiedRequirements;
      this.availableIUs = availableIUs;
    }

    @Override
    protected String getDefaultMessage()
    {
      return "The installation does not satisfy the requirements list below.";
    }

    @Override
    protected String getShellText()
    {
      return "Eclipse Update";
    }

    @Override
    protected void createUI(Composite parent)
    {
      TreeViewer treeViewer = new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL);
      treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

      AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
      AdapterFactoryContentProvider contentProvider = new AdapterFactoryContentProvider(adapterFactory);
      AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
      treeViewer.setContentProvider(contentProvider);
      treeViewer.setLabelProvider(labelProvider);
      ItemProvider input = new ItemProvider();
      EList<Object> inputChildren = input.getChildren();
      for (Requirement requirement : unsatisifiedRequirements)
      {
        String id = requirement.getName();
        ItemProvider requirementItem = new ItemProvider(labelProvider.getText(requirement), labelProvider.getImage(requirement));
        EList<Object> requirementChildren = requirementItem.getChildren();
        inputChildren.add(requirementItem);

        for (IInstallableUnit iu : availableIUs)
        {
          if (id.equals(iu.getId()))
          {
            Requirement fakeIU = P2Factory.eINSTANCE.createRequirement(id);
            Version version = iu.getVersion();
            String label = labelProvider.getText(fakeIU) + " " + version;
            ItemProvider iuItem = new ItemProvider(label, labelProvider.getImage(fakeIU));
            requirementChildren.add(iuItem);
          }
        }
      }

      treeViewer.setInput(input);
    }

    @Override
    protected void doCreateButtons(Composite parent)
    {
      createButton(parent, IDialogConstants.OK_ID, "Install", false);
      createButton(parent, IDialogConstants.CANCEL_ID, "Skip", true);
    }
  }
}
