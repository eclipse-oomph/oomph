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
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.p2.LicenseConfirmation;
import org.eclipse.oomph.p2.LicensePrompter;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.VariableType;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.LicenseDialog;
import org.eclipse.oomph.setup.ui.PropertyField;
import org.eclipse.oomph.setup.ui.PropertyField.ValueListener;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UICallback;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionsUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class VariablePage extends SetupWizardPage implements SetupPrompter, LicensePrompter
{
  private Composite composite;

  private ScrolledComposite scrolledComposite;

  private final Map<URI, FieldHolder> fieldHolders = new LinkedHashMap<URI, FieldHolder>();

  private Map<FieldHolder, Set<FieldHolder>> ruleUses = new LinkedHashMap<FieldHolder, Set<FieldHolder>>();

  private boolean focusSet;

  private boolean prompted;

  private boolean fullPrompt;

  private boolean recursiveValidate;

  private Set<SetupTaskPerformer> promptedPerformers = new LinkedHashSet<SetupTaskPerformer>();

  private SetupTaskPerformer performer;

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
        focusSet = false;
        fullPrompt = fullPromptButton.getSelection();
        validate();
      }
    });
  }

  private URI getURI(VariableTask variable)
  {
    String name = variable.getName();
    URI uri = variable.eResource() == null ? URI.createURI("#") : EcoreUtil.getURI(variable);
    uri = uri.appendFragment(uri.fragment() + "~" + name);
    return uri;
  }

  private FieldHolder getFieldHolder(VariableTask variable)
  {
    return fieldHolders.get(getURI(variable));
  }

  private FieldHolder createFieldHolder(VariableTask variable)
  {
    URI uri = getURI(variable);
    FieldHolder fieldHolder = fieldHolders.get(uri);
    if (fieldHolder == null)
    {
      PropertyField field = createField(variable);
      field.fill(composite);

      fieldHolder = new FieldHolder(field, variable);
      fieldHolders.put(uri, fieldHolder);
    }
    else
    {
      fieldHolder.add(variable);
    }

    return fieldHolder;
  }

  private PropertyField createField(final VariableTask variable)
  {
    PropertyField field = createField(variable.getType(), variable.getChoices());

    String label = variable.getLabel();
    if (StringUtil.isEmpty(label))
    {
      label = variable.getName();
    }

    field.setLabelText(label);
    field.setToolTip(variable.getDescription());

    GridData gridData = field.getLabelGridData();
    gridData.widthHint = 150;

    return field;
  }

  private PropertyField createField(VariableType type, List<VariableChoice> choices)
  {
    switch (type)
    {
      case FOLDER:
        PropertyField.FileField fileField = new PropertyField.FileField(choices);
        fileField.setDialogText("Folder Selection");
        fileField.setDialogMessage("Select a folder.");
        return fileField;

      case PASSWORD:
        return new PropertyField.TextField(true);
    }

    return new PropertyField.TextField(choices);
  }

  private void updateFields()
  {
    // Clear out the variables from any of the fields previously created.
    Set<FieldHolder> fieldHolderSet = new LinkedHashSet<FieldHolder>(fieldHolders.values());
    for (FieldHolder fieldHolder : fieldHolderSet)
    {
      fieldHolder.clear();
    }

    Set<FieldHolder> fieldHolderVariableOrder = new LinkedHashSet<FieldHolder>();
    for (SetupTaskPerformer setupTaskPerformer : promptedPerformers)
    {
      List<VariableTask> variables = setupTaskPerformer.getUnresolvedVariables();
      for (VariableTask variable : variables)
      {
        FieldHolder fieldHolder;

        VariableTask ruleVariable = setupTaskPerformer.getRuleVariable(variable);
        if (ruleVariable == null)
        {
          fieldHolder = createFieldHolder(variable);
        }
        else
        {
          fieldHolder = createFieldHolder(ruleVariable);
          fieldHolder.add(variable);

          // Put it into the map under the original variable's key as well.
          fieldHolders.put(getURI(variable), fieldHolder);
        }

        // Ensure that the variables that are visited again are ordered accordingly.
        fieldHolderVariableOrder.remove(fieldHolder);
        fieldHolderVariableOrder.add(fieldHolder);
      }
    }

    // Build a new one because there can be new holders created.
    fieldHolderSet = new LinkedHashSet<FieldHolder>(fieldHolders.values());
    for (FieldHolder fieldHolder : fieldHolderSet)
    {
      for (VariableTask variable : fieldHolder.getVariables())
      {
        String value = variable.getValue();
        if (!StringUtil.isEmpty(value) && StringUtil.isEmpty(fieldHolder.getField().getValue()))
        {
          fieldHolder.getField().setValue(value, false);
          break;
        }
      }
    }

    for (FieldHolder fieldHolder : fieldHolderSet)
    {
      fieldHolder.recordInitialValue();
    }

    // Determine the URIs of all the variables actually being used.
    Set<URI> uris = new HashSet<URI>();
    if (performer != null)
    {
      for (VariableTask variable : performer.getUnresolvedVariables())
      {
        uris.add(getURI(variable));
        VariableTask ruleVariable = performer.getRuleVariable(variable);
        if (ruleVariable != null)
        {
          uris.add(getURI(ruleVariable));
        }
      }
    }

    // Garbage collect any unused fields.
    for (Iterator<Map.Entry<URI, FieldHolder>> it = fieldHolders.entrySet().iterator(); it.hasNext();)
    {
      Entry<URI, FieldHolder> entry = it.next();
      FieldHolder fieldHolder = entry.getValue();
      if (!uris.contains(entry.getKey()) && fieldHolder.getVariables().isEmpty() && !fieldHolder.isDirty())
      {
        fieldHolder.dispose();
        it.remove();

        for (Set<FieldHolder> set : ruleUses.values())
        {
          set.remove(fieldHolder);
        }
      }
    }

    Composite parent = composite.getParent();
    parent.setRedraw(false);

    // Determine an appropriate field order.

    List<SetupTaskPerformer> allPerformers = new ArrayList<SetupTaskPerformer>(promptedPerformers);
    if (performer != null)
    {
      allPerformers.add(0, performer);
    }

    List<Control> controls = new ArrayList<Control>();
    fieldHolderSet = new LinkedHashSet<FieldHolder>(fieldHolders.values());
    fieldHolderVariableOrder.addAll(fieldHolderSet);
    Map<FieldHolder, Set<FieldHolder>> newRuleUses = new LinkedHashMap<FieldHolder, Set<FieldHolder>>();
    LOOP: for (FieldHolder fieldHolder : fieldHolderVariableOrder)
    {
      controls.add(getMainControl(fieldHolder.getField().getControl()));
      for (VariableTask variable : fieldHolder.getVariables())
      {
        for (SetupTaskPerformer performer : allPerformers)
        {
          EAttribute eAttribute = performer.getAttributeRuleVariableData(variable);
          if (eAttribute != null)
          {
            CollectionsUtil.addAll(newRuleUses, fieldHolder, Collections.<FieldHolder> emptySet());
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
              FieldHolder dependentFieldHolder = getFieldHolder(ruleVariable);
              if (dependentFieldHolder != null)
              {
                CollectionsUtil.add(newRuleUses, dependentFieldHolder, fieldHolder);
                continue LOOP;
              }
            }
          }
        }
      }
    }

    Map<FieldHolder, Set<FieldHolder>> consolidatedRuleUses = new LinkedHashMap<FieldHolder, Set<FieldHolder>>();
    CollectionsUtil.addAll(newRuleUses, ruleUses);
    LOOP: for (Map.Entry<FieldHolder, Set<FieldHolder>> entry : newRuleUses.entrySet())
    {
      FieldHolder fieldHolder = entry.getKey();
      if (fieldHolder.getField().getControl().isDisposed())
      {
        for (Map.Entry<FieldHolder, Set<FieldHolder>> otherEntry : newRuleUses.entrySet())
        {
          if (otherEntry != entry)
          {
            for (FieldHolder usedFieldHolder : entry.getValue())
            {
              if (otherEntry.getValue().contains(usedFieldHolder))
              {
                CollectionsUtil.addAll(consolidatedRuleUses, entry.getKey(), otherEntry.getValue());
                CollectionsUtil.addAll(consolidatedRuleUses, entry.getKey(), entry.getValue());
                continue LOOP;
              }
            }
          }
        }
      }

      CollectionsUtil.addAll(consolidatedRuleUses, entry.getKey(), entry.getValue());
    }

    ruleUses = consolidatedRuleUses;

    int size = controls.size();
    Set<FieldHolder> orderedFieldHolders = new LinkedHashSet<FieldHolder>();
    if (size <= 1)
    {
      orderedFieldHolders.addAll(fieldHolderSet);
    }
    else
    {
      List<Control> children = Arrays.asList(composite.getChildren());

      int controlOffset = -1;
      int columnCount = -1;
      for (int i = 0, childrenSize = children.size(); i < childrenSize; ++i)
      {
        if (controls.contains(children.get(i)))
        {
          if (controlOffset == -1)
          {
            controlOffset = i;
          }
          else
          {
            columnCount = i - controlOffset;
            break;
          }
        }
      }

      for (Map.Entry<FieldHolder, Set<FieldHolder>> entry : ruleUses.entrySet())
      {
        FieldHolder key = entry.getKey();
        if (!key.getField().getControl().isDisposed())
        {
          orderedFieldHolders.add(key);
        }

        for (FieldHolder value : entry.getValue())
        {
          if (!value.getField().getControl().isDisposed())
          {
            orderedFieldHolders.add(value);
          }
        }
      }

      orderedFieldHolders.addAll(fieldHolderSet);

      Control target = children.get(columnCount - 1);
      for (FieldHolder fieldHolder : orderedFieldHolders)
      {
        Control mainControl = getMainControl(fieldHolder.getField().getControl());
        int index = children.indexOf(mainControl) - controlOffset;
        Control newTarget = null;
        for (int j = columnCount - 1; j >= 0; --j)
        {
          Control child = children.get(index + j);
          if (newTarget == null)
          {
            newTarget = child;
          }

          if (target == child)
          {
            break;
          }

          child.moveBelow(target);
        }

        target = newTarget;
      }
    }

    PropertyField firstField = null;
    PropertyField firstEmptyField = null;
    for (FieldHolder fieldHolder : orderedFieldHolders)
    {
      PropertyField field = fieldHolder.getField();
      if (firstField == null)
      {
        firstField = field;
      }

      if (firstEmptyField == null && StringUtil.isEmpty(field.getValue()))
      {
        firstEmptyField = field;
      }
    }

    if (!focusSet)
    {
      PropertyField field = firstEmptyField;
      if (field == null)
      {
        field = firstField;
      }

      if (field != null)
      {
        field.setFocus();
        focusSet = true;
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

  private Control getMainControl(Control control)
  {
    Control parent = control.getParent();
    if (parent == composite)
    {
      return control;
    }

    return null;
  }

  private void validate()
  {
    try
    {
      performer = null;

      try
      {
        promptedPerformers.clear();

        User originalUser = getUser();
        URI uri = originalUser.eResource().getURI();
        User user = EcoreUtil.copy(originalUser);
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
          performer.put(LicensePrompter.class, this);
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
    }
  }

  @Override
  public void leavePage(boolean forward)
  {
    if (forward)
    {
      List<VariableTask> unresolvedVariables = performer.getUnresolvedVariables();
      for (FieldHolder fieldHolder : new LinkedHashSet<FieldHolder>(fieldHolders.values()))
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
      installationResource.setURI(URI.createFileURI(new File(performer.getProductLocation(), "configuration/org.eclipse.oomph.setup/installation.setup")
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

      unresolvedVariables.clear();

      getWizard().setSetupContext(SetupContext.create(getInstallation(), getWorkspace(), copiedUser));
      setPerformer(performer);
    }
    else
    {
      setPerformer(null);
    }
  }

  public LicenseConfirmation promptLicenses(final Map<ILicense, List<IInstallableUnit>> licensesToIUs)
  {
    final LicenseConfirmation[] result = { LicenseConfirmation.DECLINE };

    Display.getDefault().syncExec(new Runnable()
    {
      public void run()
      {
        LicenseDialog dialog = new LicenseDialog(null, licensesToIUs);
        if (dialog.open() == LicenseDialog.OK)
        {
          if (dialog.isRememberAcceptedLicenses())
          {
            result[0] = LicenseConfirmation.ACCEPT_AND_REMEMBER;
          }
          else
          {
            result[0] = LicenseConfirmation.ACCEPT;
          }
        }
      }
    });

    return result[0];
  }

  public String getValue(VariableTask variable)
  {
    FieldHolder fieldHolder = getFieldHolder(variable);
    if (fieldHolder != null)
    {
      String value = fieldHolder.getField().getValue();
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
    for (SetupTaskPerformer performer : performers)
    {
      boolean resolvedAll = true;
      for (VariableTask variable : performer.getUnresolvedVariables())
      {
        FieldHolder fieldHolder = getFieldHolder(variable);
        if (fieldHolder != null)
        {
          String value = fieldHolder.getField().getValue();
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
        promptedPerformers.add(performer);
      }
    }

    boolean isComplete = promptedPerformers.isEmpty();
    promptedPerformers.addAll(performers);
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

    private final PropertyField field;

    private String initialValue;

    public FieldHolder(PropertyField field, VariableTask variable)
    {
      this.field = field;
      field.addValueListener(this);
      variables.add(variable);
    }

    public PropertyField getField()
    {
      return field;
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
      // return initialValue != null && (!initialValue.equals(field.getValue()) || initialValue.length() == 0);
      return initialValue != null && !initialValue.equals(field.getValue());
    }

    public void dispose()
    {
      field.dispose();
    }

    @Override
    public String toString()
    {
      return field.toString();
    }
  }
}
