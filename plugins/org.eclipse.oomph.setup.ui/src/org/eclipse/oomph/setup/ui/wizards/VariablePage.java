/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Ericsson AB (Julian Enoch) - Bug 434525 - Allow prompted variables to be pre-populated
 */
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.ui.AbstractConfirmDialog;
import org.eclipse.oomph.setup.ui.AbstractDialogConfirmer;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.LicenseDialog;
import org.eclipse.oomph.setup.ui.PropertyField;
import org.eclipse.oomph.setup.ui.PropertyField.ValueListener;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.UnsignedContentDialog;
import org.eclipse.oomph.ui.UICallback;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

import java.io.File;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class VariablePage extends SetupWizardPage implements SetupPrompter
{
  public static final Confirmer LICENSE_CONFIRMER = new AbstractDialogConfirmer()
  {
    @Override
    protected AbstractConfirmDialog createDialog(boolean defaultConfirmed, Object info)
    {
      @SuppressWarnings("unchecked")
      Map<ILicense, List<IInstallableUnit>> licensesToIUs = (Map<ILicense, List<IInstallableUnit>>)info;
      return new LicenseDialog(licensesToIUs);
    }
  };

  private Composite composite;

  private ScrolledComposite scrolledComposite;

  private final FieldHolderManager manager = new FieldHolderManager();

  private boolean prompted;

  private boolean fullPrompt;

  private boolean recursiveValidate;

  private Set<SetupTaskPerformer> incompletePerformers = new LinkedHashSet<SetupTaskPerformer>();

  private Set<SetupTaskPerformer> allPromptedPerfomers = new LinkedHashSet<SetupTaskPerformer>();

  private SetupTaskPerformer performer;

  private Control focusControl;

  private FocusListener focusListener = new FocusAdapter()
  {
    @Override
    public void focusGained(FocusEvent e)
    {
      focusControl = (Control)e.widget;
    }
  };

  protected VariablePage()
  {
    super("VariablePage");
    setTitle("Variables");
    setDescription("Enter values for the required variables.");

  }

  @Override
  protected Control createUI(Composite parent)
  {
    Composite mainComposite = new Composite(parent, SWT.NONE);
    mainComposite.setLayout(new GridLayout(1, false));
    mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

    scrolledComposite = new ScrolledComposite(mainComposite, SWT.VERTICAL);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    scrolledComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

    GridLayout layout = new GridLayout(3, false);
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.horizontalSpacing = 10;
    layout.verticalSpacing = 10;

    composite = new Composite(scrolledComposite, SWT.NONE);
    composite.setLayout(layout);
    scrolledComposite.setContent(composite);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));

    ControlAdapter resizeListener = new ControlAdapter()
    {
      @Override
      public void controlResized(ControlEvent event)
      {
        Point size = composite.computeSize(scrolledComposite.getClientArea().width, SWT.DEFAULT);
        scrolledComposite.setMinSize(size);
      }
    };

    scrolledComposite.addControlListener(resizeListener);
    composite.addControlListener(resizeListener);
    composite.notifyListeners(SWT.Resize, new Event());

    return mainComposite;
  }

  @Override
  protected void createCheckButtons()
  {
    final Button fullPromptButton = addCheckButton("Show all variables", "", false, "fullPrompt");
    fullPrompt = fullPromptButton.getSelection();
    fullPromptButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        fullPrompt = fullPromptButton.getSelection();
        UIUtil.asyncExec(new Runnable()
        {
          public void run()
          {
            validate();
          }
        });
      }
    });
  }

  private void updateFields()
  {
    for (FieldHolder fieldHolder : manager)
    {
      fieldHolder.clear();
    }

    for (SetupTaskPerformer setupTaskPerformer : incompletePerformers)
    {
      List<VariableTask> variables = setupTaskPerformer.getUnresolvedVariables();
      for (VariableTask variable : variables)
      {
        VariableTask ruleVariable = setupTaskPerformer.getRuleVariable(variable);
        if (ruleVariable == null)
        {
          manager.getFieldHolder(variable, true);
        }
        else
        {
          FieldHolder fieldHolder = manager.getFieldHolder(ruleVariable, true);
          fieldHolder.add(variable);
          manager.associate(variable, fieldHolder);
        }
      }
    }

    for (FieldHolder fieldHolder : manager)
    {
      if (StringUtil.isEmpty(fieldHolder.getValue()))
      {
        String initialValue = null;
        String initialDefaultValue = null;
        for (VariableTask variable : fieldHolder.getVariables())
        {
          if (initialValue == null)
          {
            String value = variable.getValue();
            if (!StringUtil.isEmpty(value))
            {
              initialValue = value;
            }
          }
          else if (initialDefaultValue == null)
          {
            String defaultValue = variable.getDefaultValue();
            if (!StringUtil.isEmpty(defaultValue))
            {
              initialDefaultValue = defaultValue;
            }
          }
        }

        if (!StringUtil.isEmpty(initialValue))
        {
          fieldHolder.setValue(initialValue);
        }
        else if (!StringUtil.isEmpty(initialDefaultValue))
        {
          fieldHolder.setValue(initialDefaultValue);
        }
      }
    }

    for (FieldHolder fieldHolder : manager)
    {
      fieldHolder.recordInitialValue();
    }

    // Determine the URIs of all the variables actually being used.
    Set<URI> uris = new HashSet<URI>();
    if (performer != null)
    {
      for (VariableTask variable : performer.getUnresolvedVariables())
      {
        uris.add(manager.getURI(variable));
        VariableTask ruleVariable = performer.getRuleVariable(variable);
        if (ruleVariable != null)
        {
          uris.add(manager.getURI(ruleVariable));
        }
      }
    }

    manager.cleanup(uris);

    Composite parent = composite.getParent();
    parent.setRedraw(false);

    // Determine an appropriate field order.

    List<SetupTaskPerformer> allPerformers = new ArrayList<SetupTaskPerformer>(allPromptedPerfomers);
    if (performer != null)
    {
      allPerformers.add(0, performer);
    }

    manager.reorder(allPerformers);

    FieldHolder firstField = null;
    FieldHolder firstEmptyField = null;
    for (FieldHolder fieldHolder : manager)
    {
      if (!fieldHolder.isDisposed())
      {
        if (firstField == null)
        {
          firstField = fieldHolder;
        }

        if (firstEmptyField == null && StringUtil.isEmpty(fieldHolder.getValue()))
        {
          firstEmptyField = fieldHolder;
        }
      }
    }

    if (focusControl != null && !focusControl.isDisposed())
    {
      focusControl.setFocus();
    }
    else
    {
      FieldHolder field = firstEmptyField;
      if (field == null)
      {
        field = firstField;
      }

      if (field != null)
      {
        field.setFocus();
      }
    }

    parent.pack();
    parent.getParent().layout();
    parent.setRedraw(true);

    if (!isPageComplete() && firstEmptyField == null)
    {
      // If the page isn't complete but there are no empty fields, then the last change introduced a new field.
      // So we should validate again to be sure there really needs to be more information prompted from the user.
      if (!recursiveValidate)
      {
        recursiveValidate = true;
        validate();
      }
    }
    else
    {
      recursiveValidate = false;
    }
  }

  private void validate()
  {
    try
    {
      performer = null;

      try
      {
        incompletePerformers.clear();
        allPromptedPerfomers.clear();

        User originalUser = getUser();
        URI uri = originalUser.eResource().getURI();
        final User user = EcoreUtil.copy(originalUser);
        Resource userResource = Resource.Factory.Registry.INSTANCE.getFactory(uri).createResource(uri);
        userResource.getContents().add(user);

        Trigger trigger = getTrigger();
        Installation installation = getInstallation();
        Workspace workspace = getWorkspace();

        URIConverter uriConverter = getResourceSet().getURIConverter();
        SetupContext context = SetupContext.create(installation, workspace, user);

        performer = SetupTaskPerformer.create(uriConverter, this, trigger, context, fullPrompt);
        if (performer != null)
        {
          performer.put(ILicense.class, LICENSE_CONFIRMER);
          performer.put(Certificate.class, UnsignedContentDialog.createUnsignedContentConfirmer(user, false));
        }
      }
      catch (OperationCanceledException ex)
      {
        //$FALL-THROUGH$
      }

      UIUtil.asyncExec(new Runnable()
      {
        public void run()
        {
          updateFields();
        }
      });

      if (performer == null)
      {
        setPageComplete(false);
      }
      else
      {
        setPageComplete(true);

        if (!prompted)
        {
          advanceToNextPage();
        }
      }
    }
    catch (Exception ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }
  }

  @Override
  public void enterPage(boolean forward)
  {
    performer = getWizard().getPerformer();
    if (performer != null && forward)
    {
      performer.setPrompter(this);
      setPageComplete(true);
      advanceToNextPage();
    }
    else
    {
      getWizard().setSetupContext(SetupContext.create(getInstallation(), getWorkspace(), SetupContext.createUserOnly(getResourceSet()).getUser()));
      setPageComplete(false);
      validate();
      if (forward && getPreviousPage() == null && isPageComplete())
      {
        advanceToNextPage();
      }
    }
  }

  @Override
  public void leavePage(boolean forward)
  {
    if (forward)
    {
      List<VariableTask> unresolvedVariables = performer.getUnresolvedVariables();
      for (FieldHolder fieldHolder : manager)
      {
        unresolvedVariables.addAll(fieldHolder.getVariables());
      }

      User user = getUser();
      User copiedUser = EcoreUtil.copy(user);
      URI userResourceURI = user.eResource().getURI();
      Resource userResource = getResourceSet().getResourceFactoryRegistry().getFactory(userResourceURI).createResource(userResourceURI);
      userResource.getContents().add(copiedUser);

      int xxx; // This code is just like in org.eclipse.oomph.setup.ui.wizards.ProgressPage.saveLocalFiles(SetupTaskPerformer)
      Installation installation = getInstallation();
      Resource installationResource = installation.eResource();
      URI installationResourceURI = installationResource.getURI();
      installationResource.setURI(URI.createFileURI(new File(performer.getProductConfigurationLocation(), "org.eclipse.oomph.setup/installation.setup")
          .toString()));

      Workspace workspace = getWorkspace();
      Resource workspaceResource = null;
      URI workspaceResourceURI = null;
      if (workspace != null)
      {
        workspaceResource = workspace.eResource();
        workspaceResourceURI = workspaceResource.getURI();
        workspaceResource.setURI(URI.createFileURI(new File(performer.getWorkspaceLocation(), ".metadata/.plugins/org.eclipse.oomph.setup/workspace.setup")
            .toString()));
      }

      performer.recordVariables(copiedUser);

      installationResource.setURI(installationResourceURI);
      if (workspaceResource != null)
      {
        workspaceResource.setURI(workspaceResourceURI);
      }

      // try
      // {
      // copiedUser.eResource().save(System.err, null);
      // }
      // catch (IOException ex)
      // {
      // ex.printStackTrace();
      // }

      unresolvedVariables.clear();

      getWizard().setSetupContext(SetupContext.create(getInstallation(), getWorkspace(), copiedUser));
      setPerformer(performer);
    }
    else
    {
      setPerformer(null);
    }
  }

  public String getValue(VariableTask variable)
  {
    FieldHolder fieldHolder = manager.getFieldHolder(variable, false);
    if (fieldHolder != null)
    {
      String value = fieldHolder.getValue();
      if (!"".equals(value))
      {
        return value;
      }
    }

    return null;
  }

  public boolean promptVariables(List<? extends SetupTaskContext> contexts)
  {
    prompted = true;

    @SuppressWarnings("unchecked")
    List<SetupTaskPerformer> performers = (List<SetupTaskPerformer>)contexts;
    allPromptedPerfomers.addAll(performers);
    for (SetupTaskPerformer performer : performers)
    {
      boolean resolvedAll = true;
      List<VariableTask> unresolvedVariables = performer.getUnresolvedVariables();
      for (VariableTask variable : unresolvedVariables)
      {
        FieldHolder fieldHolder = manager.getFieldHolder(variable, false);
        if (fieldHolder != null)
        {
          String value = fieldHolder.getValue();
          if (!"".equals(value))
          {
            variable.setValue(value);
          }
          else
          {
            resolvedAll = false;
          }
        }
        else
        {
          resolvedAll = false;
        }
      }

      if (!resolvedAll)
      {
        incompletePerformers.add(performer);
      }
    }

    boolean isComplete = incompletePerformers.isEmpty();
    return isComplete;
  }

  public UserCallback getUserCallback()
  {
    return new UICallback(getShell(), AbstractSetupDialog.SHELL_TEXT);
  }

  /**
   * @author Ed Merks
   */
  private final class FieldHolder implements ValueListener
  {
    private final Set<VariableTask> variables = new LinkedHashSet<VariableTask>();

    private PropertyField field;

    private String initialValue;

    public FieldHolder(VariableTask variable)
    {
      field = PropertyField.createField(variable);
      field.fill(composite);
      field.addValueListener(this);
      field.getControl().addFocusListener(focusListener);
      variables.add(variable);
    }

    public boolean isDisposed()
    {
      return field == null;
    }

    private Control getControl()
    {
      if (field == null)
      {
        return null;
      }

      Control control = field.getControl();
      Control parent = control.getParent();
      if (parent == composite)
      {
        return control;
      }

      return null;
    }

    public void setFocus()
    {
      if (field == null)
      {
        throw new IllegalStateException("Can't set the value of a disposed field");
      }

      field.setFocus();
    }

    public String getValue()
    {
      return field == null ? "" : field.getValue();
    }

    public void setValue(String value)
    {
      if (field == null)
      {
        throw new IllegalStateException("Can't set the value of a disposed field");
      }

      field.setValue(value, false);
    }

    public Set<VariableTask> getVariables()
    {
      return variables;
    }

    public void clear()
    {
      variables.clear();
    }

    public void add(VariableTask variable)
    {
      if (variables.add(variable))
      {
        String value = field.getValue();
        if ("".equals(value))
        {
          variable.setValue(value);
        }
      }
    }

    public void valueChanged(String oldValue, String newValue) throws Exception
    {
      for (VariableTask variable : variables)
      {
        variable.setValue(newValue);
      }

      validate();
    }

    public void recordInitialValue()
    {
      if (initialValue == null)
      {
        initialValue = field.getValue();
      }
    }

    public boolean isDirty()
    {
      return field != null && initialValue != null && !initialValue.equals(field.getValue());
    }

    public void dispose()
    {
      if (field != null)
      {
        field.dispose();
        field = null;
      }
    }

    @Override
    public String toString()
    {
      return field == null ? "<disposed>" : field.toString();
    }
  }

  /**
   * @author Ed Merks
   */
  private static class FieldHolderRecord
  {
    private FieldHolder fieldHolder;

    private final Set<URI> variableURIs = new HashSet<URI>();

    public FieldHolderRecord()
    {
    }

    public FieldHolder getFieldHolder()
    {
      return fieldHolder;
    }

    public void setFieldHolder(FieldHolder fieldHolder)
    {
      this.fieldHolder = fieldHolder;
    }

    public Set<URI> getVariableURIs()
    {
      return variableURIs;
    }

    @Override
    public String toString()
    {
      return variableURIs.toString();
    }
  }

  /**
   * @author Ed Merks
   */
  private class FieldHolderManager implements Iterable<FieldHolder>
  {
    private final EList<FieldHolderRecord> fields = new BasicEList<FieldHolderRecord>();

    public Iterator<FieldHolder> iterator()
    {
      final Iterator<FieldHolderRecord> iterator = fields.iterator();
      return new Iterator<FieldHolder>()
      {
        public boolean hasNext()
        {
          return iterator.hasNext();
        }

        public FieldHolder next()
        {
          return iterator.next().getFieldHolder();
        }

        public void remove()
        {
          throw new UnsupportedOperationException();
        }
      };
    }

    public void reorder(List<SetupTaskPerformer> allPerformers)
    {
      List<Control> controls = new ArrayList<Control>();
      Map<FieldHolderRecord, Set<FieldHolderRecord>> ruleUses = new LinkedHashMap<FieldHolderRecord, Set<FieldHolderRecord>>();
      LOOP: for (FieldHolderRecord fieldHolderRecord : fields)
      {
        FieldHolder fieldHolder = fieldHolderRecord.getFieldHolder();
        Control control = fieldHolder.getControl();
        if (control != null)
        {
          controls.add(control);
          for (VariableTask variable : fieldHolder.getVariables())
          {
            for (SetupTaskPerformer performer : allPerformers)
            {
              EAttribute eAttribute = performer.getAttributeRuleVariableData(variable);
              if (eAttribute != null)
              {
                CollectionUtil.addAll(ruleUses, fieldHolderRecord, Collections.<FieldHolderRecord> emptySet());
                continue LOOP;
              }
            }
          }

          // for (VariableTask variable : fieldHolder.getVariables())
          // {
          // for (SetupTaskPerformer performer : allPerformers)
          // {
          // EStructuralFeature.Setting setting = performer.getImpliedVariableData(variable);
          // if (setting != null)
          // {
          // continue LOOP;
          // }
          // }
          // }

          for (VariableTask variable : fieldHolder.getVariables())
          {
            for (SetupTaskPerformer performer : allPerformers)
            {
              VariableTask dependantVariable = performer.getRuleVariableData(variable);
              if (dependantVariable != null)
              {
                VariableTask ruleVariable = performer.getRuleVariable(dependantVariable);
                if (ruleVariable != null)
                {
                  FieldHolderRecord dependantFieldHolderRecord = getFieldHolderRecord(getURI(ruleVariable));
                  if (dependantFieldHolderRecord != null)
                  {
                    CollectionUtil.add(ruleUses, dependantFieldHolderRecord, fieldHolderRecord);
                    continue LOOP;
                  }
                }
              }
            }
          }
        }
      }

      int maxPosition = fields.size() - 1;
      for (Map.Entry<FieldHolderRecord, Set<FieldHolderRecord>> entry : ruleUses.entrySet())
      {
        FieldHolderRecord fieldHolderRecord = entry.getKey();
        int index = fields.indexOf(fieldHolderRecord);
        Set<FieldHolderRecord> fieldHolderRecords = entry.getValue();
        for (FieldHolderRecord dependantFieldHolderRecord : fieldHolderRecords)
        {
          fields.move(Math.min(++index, maxPosition), dependantFieldHolderRecord);
        }
      }

      int size = controls.size();
      if (size > 1)
      {
        List<Control> children = Arrays.asList(composite.getChildren());

        int controlOffset = 0;
        for (Control child : children)
        {
          if (controls.contains(child))
          {
            break;
          }

          ++controlOffset;
        }

        Control target = children.get(PropertyField.NUM_COLUMNS - 1);
        int count = 0;
        for (FieldHolder fieldHolder : this)
        {
          Control control = fieldHolder.getControl();
          if (control != null)
          {
            int index = children.indexOf(control) - controlOffset;
            Control newTarget = null;
            for (int j = PropertyField.NUM_COLUMNS - 1; j >= 0; --j)
            {
              Control child = children.get(index + j);
              if (newTarget == null)
              {
                newTarget = child;
                if (index == count)
                {
                  break;
                }
              }

              child.moveBelow(target);
            }

            target = newTarget;
            count += PropertyField.NUM_COLUMNS;
          }
        }
      }
    }

    public void cleanup(Set<URI> uris)
    {
      LOOP: for (FieldHolderRecord fieldHolderRecord : fields)
      {
        for (URI uri : fieldHolderRecord.getVariableURIs())
        {
          if (uris.contains(uri))
          {
            continue LOOP;
          }
        }

        FieldHolder fieldHolder = fieldHolderRecord.getFieldHolder();
        if (fieldHolder.getVariables().isEmpty() && !fieldHolder.isDirty())
        {
          fieldHolder.dispose();
        }
      }
    }

    public URI getURI(VariableTask variable)
    {
      String name = variable.getName();
      URI uri = variable.eResource() == null ? URI.createURI("#") : EcoreUtil.getURI(variable);
      uri = uri.appendFragment(uri.fragment() + "~" + name);
      return uri;
    }

    private FieldHolderRecord getFieldHolderRecord(URI uri)
    {
      for (FieldHolderRecord fieldHolderRecord : fields)
      {
        if (fieldHolderRecord.getVariableURIs().contains(uri))
        {
          return fieldHolderRecord;
        }
      }

      return null;
    }

    public void associate(VariableTask variable, FieldHolder fieldHolder)
    {
      URI uri = getURI(variable);
      for (FieldHolderRecord fieldHolderRecord : fields)
      {
        if (fieldHolderRecord.getFieldHolder() == fieldHolder)
        {
          fieldHolderRecord.getVariableURIs().add(uri);
          break;
        }
      }
    }

    public FieldHolder getFieldHolder(VariableTask variable, boolean demandCreate)
    {
      URI uri = getURI(variable);
      FieldHolderRecord fieldHolderRecord = getFieldHolderRecord(uri);
      FieldHolder fieldHolder = null;
      if (fieldHolderRecord == null)
      {
        if (!demandCreate)
        {
          return null;
        }

        fieldHolderRecord = new FieldHolderRecord();
        fieldHolderRecord.getVariableURIs().add(uri);
        fields.add(fieldHolderRecord);
      }
      else
      {
        fieldHolder = fieldHolderRecord.getFieldHolder();
        if (fieldHolder.isDisposed())
        {
          if (!demandCreate)
          {
            return null;
          }

          fieldHolder = null;
        }
      }

      if (fieldHolder == null)
      {
        fieldHolder = new FieldHolder(variable);
        fieldHolderRecord.setFieldHolder(fieldHolder);
      }
      else
      {
        fieldHolder.add(variable);
      }

      return fieldHolder;
    }

    @Override
    public String toString()
    {
      return fields.toString();
    }
  }
}
