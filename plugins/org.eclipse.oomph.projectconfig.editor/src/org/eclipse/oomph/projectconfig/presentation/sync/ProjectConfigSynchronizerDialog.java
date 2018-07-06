/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.projectconfig.presentation.sync;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.preferences.provider.PreferencesEditPlugin;
import org.eclipse.oomph.projectconfig.PreferenceFilter;
import org.eclipse.oomph.projectconfig.PreferenceProfile;
import org.eclipse.oomph.projectconfig.Project;
import org.eclipse.oomph.projectconfig.WorkspaceConfiguration;
import org.eclipse.oomph.projectconfig.presentation.ProjectConfigEditorPlugin;
import org.eclipse.oomph.projectconfig.provider.ProjectConfigEditPlugin;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class ProjectConfigSynchronizerDialog extends TitleAreaDialog
{
  private WorkspaceConfiguration workspaceConfiguration;

  private InputItem managedPropertiesInput;

  private TreeViewer managedPropertiesViewer;

  private InputItem unmanagedPropertiesInput;

  private TreeViewer unmanagedPropertiesViewer;

  private Composite container;

  private boolean propagate = ProjectConfigSynchronizerPreferences.isPropagate();

  private boolean edit = ProjectConfigSynchronizerPreferences.isEdit();

  public ProjectConfigSynchronizerDialog(Shell parentShell)
  {
    super(parentShell);

    setShellStyle(SWT.TITLE | SWT.MAX | SWT.RESIZE | SWT.BORDER | SWT.APPLICATION_MODAL);
  }

  public boolean hasUnmanagedProperties()
  {
    return unmanagedPropertiesInput != null;
  }

  public boolean hasManagedProperties()
  {
    return managedPropertiesInput != null;
  }

  @Override
  public int open()
  {
    if (managedPropertiesInput != null || unmanagedPropertiesInput != null)
    {
      return super.open();
    }

    return Dialog.OK;
  }

  @Override
  protected Point getInitialSize()
  {
    Point size = getParentShell().getSize();
    size.x = size.x * 3 / 4;
    size.y = size.y * 3 / 4;
    return size;
  }

  @Override
  protected Point getInitialLocation(Point initialSize)
  {
    Point size = getParentShell().getSize();
    size.x = size.x / 4;
    size.y = size.y / 4;
    return size;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
  }

  public void setWorkspaceConfiguration(WorkspaceConfiguration workspaceConfiguration)
  {
    this.workspaceConfiguration = workspaceConfiguration;

    if (managedPropertiesInput != null)
    {
      managedPropertiesInput.setWorkspaceConfiguration(workspaceConfiguration);
    }

    if (unmanagedPropertiesInput != null)
    {
      unmanagedPropertiesInput.setWorkspaceConfiguration(workspaceConfiguration);
    }
  }

  public void unmanagedProperty(Property property, Property oldProperty)
  {
    if (unmanagedPropertiesInput == null)
    {
      unmanagedPropertiesInput = new InputItem(workspaceConfiguration);
    }

    unmanagedPropertiesInput.getProperty(property, oldProperty);

    if (unmanagedPropertiesViewer != null)
    {
      unmanagedPropertiesViewer.setInput(unmanagedPropertiesInput);
      unmanagedPropertiesViewer.expandAll();

    }
    else if (container != null)
    {
      for (Control child : container.getChildren())
      {
        child.dispose();
      }

      createUI(container);
      container.layout();
    }
  }

  public void managedProperty(Property managedProperty, Property managingProperty)
  {
    if (managedPropertiesInput == null)
    {
      managedPropertiesInput = new InputItem(workspaceConfiguration);
    }

    managedPropertiesInput.getProperty(managedProperty, managingProperty);

    if (managedPropertiesViewer != null)
    {
      managedPropertiesViewer.setInput(managedPropertiesInput);
      managedPropertiesViewer.expandAll();
    }
    else if (container != null)
    {
      for (Control child : container.getChildren())
      {
        child.dispose();
      }

      createUI(container);
      container.layout();
    }
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Shell shell = getShell();
    shell.setText("Preference Modification Problem");
    shell.setImage(ExtendedImageRegistry.INSTANCE.getImage(ProjectConfigEditorPlugin.INSTANCE.getImage("full/obj16/ProjectConfigModelFile")));

    setTitle("bar");

    Composite area = (Composite)super.createDialogArea(parent);

    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.verticalSpacing = 0;

    container = new Composite(area, SWT.NONE);
    container.setLayout(layout);
    container.setLayoutData(new GridData(GridData.FILL_BOTH));

    setTitle("Project-specific Preference Modification");
    setErrorMessage("Project-specific properties that are not directly managed by a preference profile of that project have been modified\n");

    createUI(container);

    shell.setActive();

    return area;
  }

  private static final class DialogLabelDecorator extends LabelProvider implements ILabelDecorator
  {
    public Image decorateImage(Image image, Object element)
    {
      return image;
    }

    public String decorateText(String text, Object element)
    {
      return text;
    }
  }

  protected void createUI(Composite container)
  {
    if (managedPropertiesInput != null)
    {
      Label label = new Label(container, SWT.NONE);
      label.setText("Modified Managed Properties:");
      label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

      {
        Composite composite = new Composite(container, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        Button overWriteButton = new Button(composite, SWT.RADIO);
        overWriteButton.setText("Overwrite with managing property");
        if (!propagate)
        {
          overWriteButton.setSelection(true);
        }
        overWriteButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            propagate = false;
            ProjectConfigSynchronizerPreferences.setPropagate(false);
            ProjectConfigSynchronizerPreferences.flush();
          }
        });

        Button propogateButton = new Button(composite, SWT.RADIO);
        propogateButton.setText("Propagate to managing property");
        if (propagate)
        {
          propogateButton.setSelection(true);
        }
        propogateButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            propagate = true;
            ProjectConfigSynchronizerPreferences.setPropagate(true);
            ProjectConfigSynchronizerPreferences.flush();
          }
        });
      }

      managedPropertiesViewer = new TreeViewer(container, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);

      ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
      AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
      managedPropertiesViewer.setLabelProvider(labelProvider);
      managedPropertiesViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

      final Tree tree = managedPropertiesViewer.getTree();
      tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
      tree.setLinesVisible(true);
      tree.setHeaderVisible(true);

      managedPropertiesViewer.setColumnProperties(new String[] { "property", "newValue", "profile", "profileValue" });

      final TreeViewerColumn propertyViewerColumn = new TreeViewerColumn(managedPropertiesViewer, SWT.NONE);
      final TreeColumn propertyColumn = propertyViewerColumn.getColumn();
      propertyViewerColumn.setLabelProvider(new DecoratingColumLabelProvider(labelProvider, new DialogLabelDecorator()));
      propertyColumn.setText("Property");
      propertyColumn.setResizable(true);

      final TreeViewerColumn newValueViewerColumn = new TreeViewerColumn(managedPropertiesViewer, SWT.NONE);
      newValueViewerColumn.setLabelProvider(new DecoratingColumLabelProvider(labelProvider, new DialogLabelDecorator())
      {
        @Override
        public String getText(Object element)
        {
          if (element instanceof PropertyItem)
          {
            PropertyItem propertyItem = (PropertyItem)element;
            return PreferencesFactory.eINSTANCE.convertEscapedString(propertyItem.getProperty().getValue());
          }

          return "";
        }

        @Override
        public Image getImage(Object element)
        {
          return null;
        }
      });

      final TreeColumn newValueColumn = newValueViewerColumn.getColumn();
      newValueColumn.setText("New Value");
      newValueColumn.setResizable(true);

      final TreeViewerColumn profileViewerColumn = new TreeViewerColumn(managedPropertiesViewer, SWT.NONE);
      profileViewerColumn.setLabelProvider(new DecoratingColumLabelProvider(labelProvider, new DialogLabelDecorator())
      {
        @Override
        public String getText(Object element)
        {
          if (element instanceof PropertyItem)
          {
            PropertyItem propertyItem = (PropertyItem)element;
            Property managingProperty = propertyItem.getOtherProperty();
            PreferenceFilter managingFilter = getManagingFilter(managingProperty);
            if (managingFilter != null)
            {
              PreferenceProfile preferenceProfile = managingFilter.getPreferenceProfile();
              Project project = preferenceProfile.getProject();
              return project.getPreferenceNode().getName() + "/" + preferenceProfile.getName();
            }
          }

          return "";
        }

        private PreferenceFilter getManagingFilter(Property managingProperty)
        {
          String name = managingProperty.getName();
          PreferenceNode preferenceNode = managingProperty.getParent();
          WorkspaceConfiguration workspaceConfiguration = (WorkspaceConfiguration)managingProperty.eResource().getContents().get(0);
          String projectName = managingProperty.getScope().getName();
          Project project = workspaceConfiguration.getProject(projectName);
          for (PreferenceProfile preferenceProfile : project.getPreferenceProfiles())
          {
            for (PreferenceFilter preferenceFilter : preferenceProfile.getPreferenceFilters())
            {
              if (preferenceFilter.getPreferenceNode() == preferenceNode && preferenceFilter.matches(name))
              {
                return preferenceFilter;
              }
            }
          }

          return null;
        }

        @Override
        public Image getImage(Object element)
        {
          if (element instanceof PropertyItem)
          {
            PropertyItem propertyItem = (PropertyItem)element;
            PreferenceFilter managingFilter = getManagingFilter(propertyItem.getOtherProperty());
            if (managingFilter != null)
            {
              return labelProvider.getImage(managingFilter.getPreferenceProfile());
            }
          }

          return null;
        }
      });

      final TreeColumn profileColumn = profileViewerColumn.getColumn();
      profileColumn.setText("Profile");
      profileColumn.setResizable(true);

      final TreeViewerColumn profileValueViewerColumn = new TreeViewerColumn(managedPropertiesViewer, SWT.NONE);
      profileValueViewerColumn.setLabelProvider(new DecoratingColumLabelProvider(labelProvider, new DialogLabelDecorator())
      {
        @Override
        public String getText(Object element)
        {
          if (element instanceof PropertyItem)
          {
            PropertyItem propertyItem = (PropertyItem)element;
            return PreferencesFactory.eINSTANCE.convertEscapedString(propertyItem.getOtherProperty().getValue());
          }

          return "";
        }

        @Override
        public Image getImage(Object element)
        {
          return null;
        }
      });

      final TreeColumn profileValueColumn = profileValueViewerColumn.getColumn();
      profileValueColumn.setText("Profile Value");
      profileValueColumn.setResizable(true);

      final ControlAdapter columnResizer = new ControlAdapter()
      {
        @Override
        public void controlResized(ControlEvent e)
        {
          int width = tree.getSize().x;
          ScrollBar bar = tree.getVerticalBar();
          if (bar != null && bar.isVisible())
          {
            width -= bar.getSize().x;
          }

          int columnWidth = width * 4 / 9;
          propertyColumn.setWidth(columnWidth);

          int remainingWidth = width - columnWidth;
          columnWidth = remainingWidth * 4 / 9;
          profileColumn.setWidth(columnWidth);

          remainingWidth -= columnWidth;
          newValueColumn.setWidth(remainingWidth / 2);
          profileValueColumn.setWidth(remainingWidth - remainingWidth / 2);

          tree.removeControlListener(this);
        }
      };

      tree.addControlListener(columnResizer);

      managedPropertiesViewer.setInput(managedPropertiesInput);
      managedPropertiesViewer.expandAll();

      Label separator = new Label(container, SWT.HORIZONTAL | SWT.SEPARATOR);
      separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    }

    if (unmanagedPropertiesInput != null)
    {

      Label label = new Label(container, SWT.NONE);
      label.setText("Unmanaged Properties:");
      label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

      Button editButton = new Button(container, SWT.CHECK);
      editButton.setText("Edit");
      editButton.setSelection(edit);
      editButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          edit = !edit;
          ProjectConfigSynchronizerPreferences.setEdit(edit);
          ProjectConfigSynchronizerPreferences.flush();
        }
      });
      editButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

      unmanagedPropertiesViewer = new TreeViewer(container, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);

      ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
      AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
      unmanagedPropertiesViewer.setLabelProvider(labelProvider);
      unmanagedPropertiesViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

      final Tree tree = unmanagedPropertiesViewer.getTree();
      tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
      tree.setLinesVisible(true);
      tree.setHeaderVisible(true);

      unmanagedPropertiesViewer.setColumnProperties(new String[] { "property", "newValue", "oldValue" });

      final TreeViewerColumn propertyViewerColumn = new TreeViewerColumn(unmanagedPropertiesViewer, SWT.NONE);
      final TreeColumn propertyColumn = propertyViewerColumn.getColumn();
      propertyViewerColumn.setLabelProvider(new DecoratingColumLabelProvider(labelProvider, new DialogLabelDecorator()));
      propertyColumn.setText("Property");
      propertyColumn.setResizable(true);

      final TreeViewerColumn newValueViewerColumn = new TreeViewerColumn(unmanagedPropertiesViewer, SWT.NONE);
      newValueViewerColumn.setLabelProvider(new DecoratingColumLabelProvider(labelProvider, new DialogLabelDecorator())
      {
        @Override
        public String getText(Object element)
        {
          if (element instanceof PropertyItem)
          {
            PropertyItem propertyItem = (PropertyItem)element;
            return PreferencesFactory.eINSTANCE.convertEscapedString(propertyItem.getProperty().getValue());
          }

          return "";
        }

        @Override
        public Image getImage(Object element)
        {
          return null;
        }
      });

      final TreeColumn newValueColumn = newValueViewerColumn.getColumn();
      newValueColumn.setText("New Value");
      newValueColumn.setResizable(true);

      final TreeViewerColumn oldValueValuerColumn = new TreeViewerColumn(unmanagedPropertiesViewer, SWT.NONE);
      oldValueValuerColumn.setLabelProvider(new DecoratingColumLabelProvider(labelProvider, new DialogLabelDecorator())
      {
        @Override
        public String getText(Object element)
        {
          if (element instanceof PropertyItem)
          {
            PropertyItem propertyItem = (PropertyItem)element;
            Property otherProperty = propertyItem.getOtherProperty();
            if (otherProperty != null)
            {
              return PreferencesFactory.eINSTANCE.convertEscapedString(otherProperty.getValue());
            }
          }

          return "";
        }

        @Override
        public Image getImage(Object element)
        {
          return null;
        }
      });

      final TreeColumn oldValueColumn = oldValueValuerColumn.getColumn();
      oldValueColumn.setText("Old Value");
      oldValueColumn.setResizable(true);

      final ControlAdapter columnResizer = new ControlAdapter()
      {
        @Override
        public void controlResized(ControlEvent e)
        {
          int width = tree.getSize().x;
          ScrollBar bar = tree.getVerticalBar();
          if (bar != null && bar.isVisible())
          {
            width -= bar.getSize().x;
          }

          int columnWidth = width / 2;
          propertyColumn.setWidth(columnWidth);

          int remainingWidth = width - columnWidth;
          columnWidth = remainingWidth * 2;
          newValueColumn.setWidth(remainingWidth / 2);
          oldValueColumn.setWidth(remainingWidth - remainingWidth / 2);

          tree.removeControlListener(this);
        }
      };

      tree.addControlListener(columnResizer);

      unmanagedPropertiesViewer.setInput(unmanagedPropertiesInput);
      unmanagedPropertiesViewer.expandAll();

      Label separator = new Label(container, SWT.HORIZONTAL | SWT.SEPARATOR);
      separator.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    }
  }

  public static class InputItem extends ItemProvider
  {
    private WorkspaceConfiguration workspaceConfiguration;

    public void setWorkspaceConfiguration(WorkspaceConfiguration workspaceConfiguration)
    {
      this.workspaceConfiguration = workspaceConfiguration;
    }

    public InputItem(WorkspaceConfiguration workspaceConfiguration)
    {
      this.workspaceConfiguration = workspaceConfiguration;
    }

    public ProjectItem getProject(Project project)
    {
      String name = project.getPreferenceNode().getName();
      EList<Object> children = getChildren();
      for (Object child : children)
      {
        ProjectItem projectItem = (ProjectItem)child;
        if (name.equals(projectItem.getText()))
        {
          return projectItem;
        }
      }

      ProjectItem projectItem = new ProjectItem(project);
      getChildren().add(projectItem);
      return projectItem;
    }

    public PreferenceNodeItem getPreferenceNode(PreferenceNode preferenceNode)
    {
      PreferenceNode projectPreferenceNode = preferenceNode.getScope();
      PreferenceNode parentPreferenceNode = preferenceNode.getParent();

      ItemProvider parentItem;
      if (projectPreferenceNode == parentPreferenceNode)
      {
        parentItem = getProject(workspaceConfiguration.getProject(projectPreferenceNode.getName()));
      }
      else
      {
        parentItem = getPreferenceNode(parentPreferenceNode);
      }

      String name = preferenceNode.getName();
      EList<Object> children = parentItem.getChildren();
      for (Object child : children)
      {
        if (child instanceof PreferenceNodeItem)
        {
          PreferenceNodeItem preferenceNodeItem = (PreferenceNodeItem)child;
          if (name.equals(preferenceNodeItem.getText()))
          {
            return preferenceNodeItem;
          }
        }
      }

      PreferenceNodeItem preferenceNodeItem = new PreferenceNodeItem(preferenceNode);
      children.add(preferenceNodeItem);
      return preferenceNodeItem;
    }

    public PropertyItem getProperty(Property property, Property otherProperty)
    {
      PreferenceNodeItem preferenceNodeItem = getPreferenceNode(property.getParent());

      String name = property.getName();
      EList<Object> children = preferenceNodeItem.getChildren();
      for (Object child : children)
      {
        if (child instanceof PropertyItem)
        {
          PropertyItem propertyItem = (PropertyItem)child;
          if (name.equals(propertyItem.getText()))
          {
            return propertyItem;
          }
        }
      }

      PropertyItem propertyItem = new PropertyItem(property, otherProperty);
      children.add(propertyItem);
      return propertyItem;
    }
  }

  public static class ProjectItem extends ItemProvider
  {
    private static final Object IMAGE = ProjectConfigEditPlugin.INSTANCE.getImage("full/obj16/Project");

    private Project project;

    public ProjectItem(Project project)
    {
      super(project.getPreferenceNode().getName(), IMAGE);

      this.project = project;
    }

    public Project getProject()
    {
      return project;
    }
  }

  public static class PreferenceNodeItem extends ItemProvider
  {
    private static final Object IMAGE = PreferencesEditPlugin.INSTANCE.getImage("full/obj16/PreferenceNode");

    private PreferenceNode preferenceNode;

    public PreferenceNodeItem(PreferenceNode preferenceNode)
    {
      super(preferenceNode.getName(), IMAGE);

      this.preferenceNode = preferenceNode;
    }

    public PreferenceNode getPreferenceNode()
    {
      return preferenceNode;
    }
  }

  public static class PropertyItem extends ItemProvider
  {
    private static final Object IMAGE = PreferencesEditPlugin.INSTANCE.getImage("full/obj16/Property");

    private Property property;

    private Property otherProperty;

    public PropertyItem(Property property, Property otherProperty)
    {
      super(property.getName(), IMAGE);

      this.property = property;
      this.otherProperty = otherProperty;
    }

    public Property getProperty()
    {
      return property;
    }

    public Property getOtherProperty()
    {
      return otherProperty;
    }
  }
}
