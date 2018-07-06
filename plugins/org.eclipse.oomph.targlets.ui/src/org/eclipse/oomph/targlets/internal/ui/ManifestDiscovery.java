/*
 * Copyright (c) 2017, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.pde.TargetPlatformListener;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginModelFactory;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.internal.core.ICoreConstants;
import org.eclipse.pde.internal.core.bundle.BundlePluginBase;
import org.eclipse.pde.internal.core.bundle.BundlePluginModelBase;
import org.eclipse.pde.internal.core.ibundle.IBundle;
import org.eclipse.pde.internal.core.ibundle.IBundleModel;
import org.eclipse.pde.internal.core.plugin.AbstractPluginModelBase;
import org.eclipse.pde.internal.core.plugin.PluginBase;
import org.eclipse.pde.internal.core.text.bundle.Bundle;
import org.eclipse.pde.internal.core.text.bundle.ImportPackageHeader;
import org.eclipse.pde.internal.core.text.bundle.ImportPackageObject;
import org.eclipse.pde.internal.core.text.plugin.PluginBaseNode;
import org.eclipse.pde.internal.core.text.plugin.PluginDocumentNodeFactory;
import org.eclipse.pde.internal.ui.IPDEUIConstants;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.editor.context.InputContextManager;
import org.eclipse.pde.internal.ui.editor.plugin.BundleInputContext;
import org.eclipse.pde.internal.ui.editor.plugin.DependenciesPage;
import org.eclipse.pde.internal.ui.editor.plugin.ManifestEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import org.osgi.framework.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class ManifestDiscovery
{
  public static final ManifestDiscovery INSTANCE = new ManifestDiscovery();

  private static final Set<DiscoverListener> DISCOVERY_LISTENERS = new HashSet<DiscoverListener>();

  private static List<ITargletContainer> targletContainers;

  private final IWindowListener windowListener = new IWindowListener()
  {
    public void windowOpened(IWorkbenchWindow window)
    {
      window.addPageListener(pageListener);
    }

    public void windowClosed(IWorkbenchWindow window)
    {
      window.removePageListener(pageListener);
    }

    public void windowActivated(IWorkbenchWindow window)
    {
      // Ignore.
    }

    public void windowDeactivated(IWorkbenchWindow window)
    {
      // Ignore.
    }
  };

  private final IPageListener pageListener = new IPageListener()
  {
    public void pageOpened(IWorkbenchPage page)
    {
      page.addPartListener(partListener);
    }

    public void pageClosed(IWorkbenchPage page)
    {
      page.removePartListener(partListener);
    }

    public void pageActivated(IWorkbenchPage page)
    {
      // Ignore.
    }
  };

  private final IPartListener partListener = new IPartListener()
  {
    public void partOpened(IWorkbenchPart part)
    {
      try
      {
        if (part instanceof ManifestEditor)
        {
          ManifestEditor manifestEditor = (ManifestEditor)part;
          handleManifestEditor(manifestEditor);
          manifestEditor.addPageChangedListener(pageChangedListener);
        }
      }
      catch (Throwable ex)
      {
        TargletsUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
      }
    }

    public void partClosed(IWorkbenchPart part)
    {
      // Ignore.
    }

    public void partActivated(IWorkbenchPart part)
    {
      // Ignore.
    }

    public void partDeactivated(IWorkbenchPart part)
    {
      // Ignore.
    }

    public void partBroughtToTop(IWorkbenchPart part)
    {
      // Ignore.
    }
  };

  private final IPageChangedListener pageChangedListener = new IPageChangedListener()
  {
    public void pageChanged(PageChangedEvent event)
    {
      Object page = event.getSelectedPage();
      handleDependenciesPage(page);
    }
  };

  private ManifestDiscovery()
  {
  }

  public void start()
  {
    final IWorkbench workbench = PlatformUI.getWorkbench();

    UIUtil.asyncExec(workbench.getDisplay(), new Runnable()
    {
      public void run()
      {
        try
        {
          for (IWorkbenchWindow window : workbench.getWorkbenchWindows())
          {
            for (IWorkbenchPage page : window.getPages())
            {
              for (IEditorReference editorReference : page.getEditorReferences())
              {
                if (IPDEUIConstants.MANIFEST_EDITOR_ID.equals(editorReference.getId()))
                {
                  IEditorPart editorPart = editorReference.getEditor(false);
                  if (editorPart instanceof ManifestEditor)
                  {
                    ManifestEditor manifestEditor = (ManifestEditor)editorPart;
                    handleManifestEditor(manifestEditor);
                    manifestEditor.addPageChangedListener(pageChangedListener);
                  }
                }
              }

              page.addPartListener(partListener);
            }

            window.addPageListener(pageListener);
          }

          workbench.addWindowListener(windowListener);

          ITargetDefinition activeTargetDefinition = TargetPlatformUtil.getActiveTargetDefinition();
          handleTargetDefinition(workbench, activeTargetDefinition);

          TargetPlatformUtil.addListener(new TargetPlatformListener()
          {
            public void targetDefinitionActivated(ITargetDefinition oldTargetDefinition, ITargetDefinition newTargetDefinition) throws Exception
            {
              handleTargetDefinition(workbench, newTargetDefinition);
            }
          });
        }
        catch (Throwable ex)
        {
          TargletsUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
        }
      }
    });
  }

  private void handleManifestEditor(final ManifestEditor manifestEditor)
  {
    Object pageInstance = manifestEditor.getActivePageInstance();
    handleDependenciesPage(pageInstance);
  }

  private void handleDependenciesPage(Object page)
  {
    try
    {
      if (page instanceof DependenciesPage)
      {
        DependenciesPage dependenciesPage = (DependenciesPage)page;
        Control control = dependenciesPage.getPartControl();
        ScrolledForm scrolledForm = (ScrolledForm)control;
        Form form = scrolledForm.getForm();
        Composite layoutComposite = (Composite)form.getChildren()[1];

        {
          Composite composite1 = (Composite)layoutComposite.getChildren()[0];
          Section section = (Section)composite1.getChildren()[0];
          Composite composite2 = (Composite)section.getChildren()[2];
          Composite composite3 = (Composite)composite2.getChildren()[1];
          Button button = (Button)composite3.getChildren()[1];
          if (!(button.getData() instanceof DiscoverPluginsListener))
          {
            FormToolkit toolkit = dependenciesPage.getManagedForm().getToolkit();
            Button newButton = toolkit != null ? toolkit.createButton(composite3, "Discover...", SWT.PUSH) : new Button(composite3, SWT.PUSH);
            newButton.moveAbove(button);
            composite3.layout();

            new DiscoverPluginsListener(newButton, dependenciesPage);
          }
        }

        {
          Composite composite1 = (Composite)layoutComposite.getChildren()[1];
          Section section = (Section)composite1.getChildren()[0];
          Composite composite2 = (Composite)section.getChildren()[2];
          Composite composite3 = (Composite)composite2.getChildren()[1];
          Button button = (Button)composite3.getChildren()[1];
          if (!(button.getData() instanceof DiscoverPackagesListener))
          {
            FormToolkit toolkit = dependenciesPage.getManagedForm().getToolkit();
            Button newButton = toolkit != null ? toolkit.createButton(composite3, "Discover...", SWT.PUSH) : new Button(composite3, SWT.PUSH);
            newButton.moveAbove(button);
            composite3.layout();

            new DiscoverPackagesListener(newButton, dependenciesPage);
          }
        }
      }
    }
    catch (Throwable ex)
    {
      TargletsUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
    }
  }

  private void handleTargetDefinition(IWorkbench workbench, final ITargetDefinition targetDefinition)
  {
    targletContainers = getTargletContainers(targetDefinition);

    workbench.getDisplay().syncExec(new Runnable()
    {
      public void run()
      {
        for (DiscoverListener listener : DISCOVERY_LISTENERS)
        {
          listener.updateVisibility();
        }
      }
    });
  }

  private List<ITargletContainer> getTargletContainers(ITargetDefinition targetDefinition)
  {
    List<ITargletContainer> result = new ArrayList<ITargletContainer>();

    if (targetDefinition != null)
    {
      ITargetLocation[] targetLocations = targetDefinition.getTargetLocations();
      if (targetLocations != null)
      {
        for (ITargetLocation targetLocation : targetLocations)
        {
          if (targetLocation instanceof ITargletContainer)
          {
            ITargletContainer targletContainer = (ITargletContainer)targetLocation;
            result.add(targletContainer);
          }
        }
      }
    }

    return result;
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class DiscoverListener extends SelectionAdapter
  {
    public final GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);

    private final Button button;

    protected final String namespace;

    protected final DependenciesPage dependenciesPage;

    public DiscoverListener(Button button, String namespace, DependenciesPage dependenciesPage)
    {
      this.button = button;
      this.namespace = namespace;
      this.dependenciesPage = dependenciesPage;

      button.setText("Discover...");
      button.setLayoutData(gridData);
      button.setData(this);

      DISCOVERY_LISTENERS.add(this);
      button.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          DISCOVERY_LISTENERS.remove(DiscoverListener.this);
        }
      });

      button.addSelectionListener(this);
      updateVisibility();
    }

    public void updateVisibility()
    {
      boolean visible = targletContainers != null && !targletContainers.isEmpty();
      button.setVisible(visible);

      gridData.exclude = !visible;
      button.getParent().layout();
    }

    @Override
    public void widgetSelected(SelectionEvent e)
    {
      ManifestDiscoveryDialog dialog = new ManifestDiscoveryDialog(PDEPlugin.getActiveWorkbenchShell(), namespace, targletContainers, true);
      dialog.create();
      if (dialog.open() == ManifestDiscoveryDialog.OK)
      {
        Object[] results = dialog.getResult();
        if (results != null && results.length != 0)
        {
          Requirement[] requirements = new Requirement[results.length];
          for (int i = 0; i < results.length; i++)
          {
            requirements[i] = (Requirement)results[i];

            Version version = requirements[i].getVersionRange().getMinimum();
            if (version != null && !version.equals(Version.emptyVersion))
            {
              VersionRange versionRange = P2Factory.eINSTANCE.createVersionRange(version, VersionSegment.MINOR, true);
              requirements[i].setVersionRange(versionRange);
            }
          }

          selected(requirements);
        }
      }
    }

    protected abstract void selected(Requirement[] requirements);
  }

  /**
   * @author Eike Stepper
   */
  private static final class DiscoverPluginsListener extends DiscoverListener
  {
    public DiscoverPluginsListener(Button button, DependenciesPage dependenciesPage)
    {
      super(button, "osgi.bundle", dependenciesPage);
    }

    @Override
    protected void selected(Requirement[] requirements)
    {
      IPluginModelBase model = (IPluginModelBase)dependenciesPage.getModel();
      IPluginModelFactory pluginFactory = model.getPluginFactory();
      IPluginImport[] imports = new IPluginImport[requirements.length];

      try
      {
        for (int i = 0; i < requirements.length; i++)
        {
          imports[i] = createImport(pluginFactory, requirements[i].getName());
          imports[i].setVersion(requirements[i].getVersionRange().toString());
        }

        addImports(model.getPluginBase(), imports);
      }
      catch (Exception ex)
      {
        TargletsUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
      }
    }

    private IPluginImport createImport(IPluginModelFactory factory, String id)
    {
      if (factory instanceof AbstractPluginModelBase)
      {
        return ((AbstractPluginModelBase)factory).createImport(id);
      }

      if (factory instanceof BundlePluginModelBase)
      {
        return ((BundlePluginModelBase)factory).createImport(id);
      }

      if (factory instanceof PluginDocumentNodeFactory)
      {
        return ((PluginDocumentNodeFactory)factory).createImport(id);
      }

      return null;
    }

    private void addImports(IPluginBase base, IPluginImport[] imports) throws CoreException
    {
      if (base instanceof BundlePluginBase)
      {
        ((BundlePluginBase)base).add(imports);
      }
      else if (base instanceof PluginBase)
      {
        ((PluginBase)base).add(imports);
      }
      else if (base instanceof PluginBaseNode)
      {
        ((PluginBaseNode)base).add(imports);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class DiscoverPackagesListener extends DiscoverListener
  {
    public DiscoverPackagesListener(Button button, DependenciesPage dependenciesPage)
    {
      super(button, "java.package", dependenciesPage);
    }

    @Override
    protected void selected(Requirement[] requirements)
    {
      Bundle bundle = (Bundle)getBundle();
      String versionAttribute = getVersionAttribute(bundle);

      ImportPackageHeader header = (ImportPackageHeader)bundle.getManifestHeader(Constants.IMPORT_PACKAGE);
      if (header != null)
      {
        Set<String> names = new HashSet<String>();
        for (Requirement requirement : requirements)
        {
          String name = requirement.getName();
          if (names.add(name))
          {
            ImportPackageObject packageImport = new ImportPackageObject(header, name, requirement.getVersionRange().toString(), versionAttribute);
            header.addPackage(packageImport);
          }
        }
      }
      else
      {
        bundle.setHeader(Constants.IMPORT_PACKAGE, getValue(requirements, versionAttribute));
      }
    }

    private BundleInputContext getBundleContext()
    {
      InputContextManager manager = dependenciesPage.getPDEEditor().getContextManager();
      return (BundleInputContext)manager.findContext(BundleInputContext.CONTEXT_ID);
    }

    private IBundleModel getBundleModel()
    {
      BundleInputContext context = getBundleContext();
      return context != null ? (IBundleModel)context.getModel() : null;

    }

    private IBundle getBundle()
    {
      IBundleModel model = getBundleModel();
      return model != null ? model.getBundle() : null;
    }

    private String getVersionAttribute(IBundle bundle)
    {
      int manifestVersion = BundlePluginBase.getBundleManifestVersion(bundle);
      return manifestVersion < 2 ? ICoreConstants.PACKAGE_SPECIFICATION_VERSION : Constants.VERSION_ATTRIBUTE;
    }

    private String getValue(Object[] requirements, String versionAttribute)
    {
      StringBuilder builder = new StringBuilder();
      String lineDelimiter = getLineDelimiter();

      for (Object object : requirements)
      {
        if (builder.length() > 0)
        {
          builder.append(",");
          builder.append(lineDelimiter);
          builder.append(" ");
        }

        Requirement requirement = (Requirement)object;
        builder.append(requirement.getName());
        builder.append(";");
        builder.append(versionAttribute);
        builder.append("=\"");
        builder.append(requirement.getVersionRange());
        builder.append("\"");
      }

      return builder.toString();
    }

    private String getLineDelimiter()
    {
      BundleInputContext inputContext = getBundleContext();
      if (inputContext != null)
      {
        return inputContext.getLineDelimiter();
      }

      return StringUtil.NL;
    }
  }

  // /**
  // * @author Eike Stepper
  // */
  // private static abstract class DiscoverButton extends Button
  // {
  // public final GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
  //
  // protected final String namespace;
  //
  // protected final DependenciesPage dependenciesPage;
  //
  // public DiscoverButton(Composite parent, int style, String namespace, DependenciesPage dependenciesPage)
  // {
  // super(parent, style);
  // setText("Discover...");
  // setLayoutData(gridData);
  //
  // this.namespace = namespace;
  // this.dependenciesPage = dependenciesPage;
  //
  // DISCOVERY_LISTENERS.add(this);
  // addDisposeListener(new DisposeListener()
  // {
  // public void widgetDisposed(DisposeEvent e)
  // {
  // DISCOVERY_LISTENERS.remove(DiscoverButton.this);
  // }
  // });
  //
  // addSelectionListener(new SelectionAdapter()
  // {
  // @Override
  // public void widgetSelected(SelectionEvent e)
  // {
  // selected();
  // }
  // });
  //
  // updateVisibility();
  // }
  //
  // public void updateVisibility()
  // {
  // setVisible(targletContainers != null && !targletContainers.isEmpty());
  // gridData.exclude = !isVisible();
  // getParent().layout();
  // }
  //
  // private void selected()
  // {
  // ManifestDiscoveryDialog dialog = new ManifestDiscoveryDialog(PDEPlugin.getActiveWorkbenchShell(), namespace, targletContainers, true);
  // dialog.create();
  // if (dialog.open() == ManifestDiscoveryDialog.OK)
  // {
  // Object[] results = dialog.getResult();
  // if (results != null && results.length != 0)
  // {
  // Requirement[] requirements = new Requirement[results.length];
  // for (int i = 0; i < results.length; i++)
  // {
  // requirements[i] = (Requirement)results[i];
  //
  // Version version = requirements[i].getVersionRange().getMinimum();
  // if (version != null && !version.equals(Version.emptyVersion))
  // {
  // VersionRange versionRange = P2Factory.eINSTANCE.createVersionRange(version, VersionSegment.MINOR, true);
  // requirements[i].setVersionRange(versionRange);
  // }
  // }
  //
  // selected(requirements);
  // }
  // }
  // }
  //
  // protected abstract void selected(Requirement[] requirements);
  //
  // @Override
  // protected void checkSubclass()
  // {
  // // Allow subclassing.
  // }
  // }
  //
  // /**
  // * @author Eike Stepper
  // */
  // private static final class DiscoverPluginsButton extends DiscoverButton
  // {
  // public DiscoverPluginsButton(Composite parent, int style, DependenciesPage dependenciesPage)
  // {
  // super(parent, style, "osgi.bundle", dependenciesPage);
  // }
  //
  // @Override
  // protected void selected(Requirement[] requirements)
  // {
  // IPluginModelBase model = (IPluginModelBase)dependenciesPage.getModel();
  // IPluginModelFactory pluginFactory = model.getPluginFactory();
  // IPluginImport[] imports = new IPluginImport[requirements.length];
  //
  // try
  // {
  // for (int i = 0; i < requirements.length; i++)
  // {
  // imports[i] = createImport(pluginFactory, requirements[i].getName());
  // imports[i].setVersion(requirements[i].getVersionRange().toString());
  // }
  //
  // addImports(model.getPluginBase(), imports);
  // }
  // catch (Exception ex)
  // {
  // TargletsUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
  // }
  // }
  //
  // private IPluginImport createImport(IPluginModelFactory factory, String id)
  // {
  // if (factory instanceof AbstractPluginModelBase)
  // {
  // return ((AbstractPluginModelBase)factory).createImport(id);
  // }
  //
  // if (factory instanceof BundlePluginModelBase)
  // {
  // return ((BundlePluginModelBase)factory).createImport(id);
  // }
  //
  // if (factory instanceof PluginDocumentNodeFactory)
  // {
  // return ((PluginDocumentNodeFactory)factory).createImport(id);
  // }
  //
  // return null;
  // }
  //
  // private void addImports(IPluginBase base, IPluginImport[] imports) throws CoreException
  // {
  // if (base instanceof BundlePluginBase)
  // {
  // ((BundlePluginBase)base).add(imports);
  // }
  // else if (base instanceof PluginBase)
  // {
  // ((PluginBase)base).add(imports);
  // }
  // else if (base instanceof PluginBaseNode)
  // {
  // ((PluginBaseNode)base).add(imports);
  // }
  // }
  // }
  //
  // /**
  // * @author Eike Stepper
  // */
  // private static final class DiscoverPackagesButton extends DiscoverButton
  // {
  // public DiscoverPackagesButton(Composite parent, int style, DependenciesPage dependenciesPage)
  // {
  // super(parent, style, "java.package", dependenciesPage);
  // }
  //
  // @Override
  // protected void selected(Requirement[] requirements)
  // {
  // Bundle bundle = (Bundle)getBundle();
  // String versionAttribute = getVersionAttribute(bundle);
  //
  // ImportPackageHeader header = (ImportPackageHeader)bundle.getManifestHeader(Constants.IMPORT_PACKAGE);
  // if (header != null)
  // {
  // Set<String> names = new HashSet<String>();
  // for (Requirement requirement : requirements)
  // {
  // String name = requirement.getName();
  // if (names.add(name))
  // {
  // ImportPackageObject packageImport = new ImportPackageObject(header, name, requirement.getVersionRange().toString(), versionAttribute);
  // header.addPackage(packageImport);
  // }
  // }
  // }
  // else
  // {
  // bundle.setHeader(Constants.IMPORT_PACKAGE, getValue(requirements, versionAttribute));
  // }
  // }
  //
  // private BundleInputContext getBundleContext()
  // {
  // InputContextManager manager = dependenciesPage.getPDEEditor().getContextManager();
  // return (BundleInputContext)manager.findContext(BundleInputContext.CONTEXT_ID);
  // }
  //
  // private IBundleModel getBundleModel()
  // {
  // BundleInputContext context = getBundleContext();
  // return context != null ? (IBundleModel)context.getModel() : null;
  //
  // }
  //
  // private IBundle getBundle()
  // {
  // IBundleModel model = getBundleModel();
  // return model != null ? model.getBundle() : null;
  // }
  //
  // private String getVersionAttribute(IBundle bundle)
  // {
  // int manifestVersion = BundlePluginBase.getBundleManifestVersion(bundle);
  // return manifestVersion < 2 ? ICoreConstants.PACKAGE_SPECIFICATION_VERSION : Constants.VERSION_ATTRIBUTE;
  // }
  //
  // private String getValue(Object[] requirements, String versionAttribute)
  // {
  // StringBuilder builder = new StringBuilder();
  // String lineDelimiter = getLineDelimiter();
  //
  // for (Object object : requirements)
  // {
  // if (builder.length() > 0)
  // {
  // builder.append(",");
  // builder.append(lineDelimiter);
  // builder.append(" ");
  // }
  //
  // Requirement requirement = (Requirement)object;
  // builder.append(requirement.getName());
  // builder.append(";");
  // builder.append(versionAttribute);
  // builder.append("=\"");
  // builder.append(requirement.getVersionRange());
  // builder.append("\"");
  // }
  //
  // return builder.toString();
  // }
  //
  // private String getLineDelimiter()
  // {
  // BundleInputContext inputContext = getBundleContext();
  // if (inputContext != null)
  // {
  // return inputContext.getLineDelimiter();
  // }
  //
  // return StringUtil.NL;
  // }
  // }
}
