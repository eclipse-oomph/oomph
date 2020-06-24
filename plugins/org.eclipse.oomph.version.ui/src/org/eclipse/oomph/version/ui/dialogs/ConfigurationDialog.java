/*
 * Copyright (c) 2014-2016, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version.ui.dialogs;

import org.eclipse.oomph.internal.version.IVersionBuilderArguments;
import org.eclipse.oomph.internal.version.VersionBuilderArguments;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ConfigurationDialog extends TitleAreaDialog implements IVersionBuilderArguments
{
  private VersionBuilderArguments values;

  private Text releasePathText;

  private Button ignoreMalformedVersionsButton;

  private Button ignoreFeatureNatureButton;

  private Button ignoreSchemaBuilderButton;

  private Button ignoreDebugOptionsButton;

  private Button ignoreAutomaticModuleNameButton;

  private Button ignoreMissingDependencyRangesButton;

  private Button ignoreLaxLowerBoundDependencyButton;

  private Button ignoreMissingExportVersionsButton;

  private Button ignoreFeatureContentChangesButton;

  private Button ignoreFeatureContentRedundancyButton;

  private Button checkFeatureClosureCompletenessButton;

  private Button checkFeatureClosureContentButton;

  private Button checkMavenPomButton;

  public ConfigurationDialog(Shell parentShell, VersionBuilderArguments defaults)
  {
    super(parentShell);
    setHelpAvailable(false);
    values = new VersionBuilderArguments(defaults);
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    newShell.setText(Messages.ConfigurationDialog_title);
    super.configureShell(newShell);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    setTitle(Messages.ConfigurationDialog_title);
    setMessage(Messages.ConfigurationDialog_message);

    Composite dialogArea = (Composite)super.createDialogArea(parent);

    Composite composite = new Composite(dialogArea, SWT.NONE);
    composite.setLayout(new GridLayout());
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));

    new Label(composite, SWT.NONE).setText(Messages.ConfigurationDialog_pathLabel + ' ');

    releasePathText = new Text(composite, SWT.BORDER);
    releasePathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    String releasePath = values.getReleasePath();
    if (releasePath != null)
    {
      releasePathText.setText(releasePath);
    }

    releasePathText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        validate();
      }
    });

    SelectionListener buttonListener = new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        validate();
      }
    };

    ignoreMalformedVersionsButton = new Button(composite, SWT.CHECK);
    ignoreMalformedVersionsButton.setText(Messages.ConfigurationDialog_ignoreMalformedVersionsButton_text);
    ignoreMalformedVersionsButton.setSelection(values.isIgnoreMalformedVersions());
    ignoreMalformedVersionsButton.addSelectionListener(buttonListener);

    ignoreFeatureNatureButton = new Button(composite, SWT.CHECK);
    ignoreFeatureNatureButton.setText(Messages.ConfigurationDialog_ignoreFeatureNatureButton_text);
    ignoreFeatureNatureButton.setSelection(values.isIgnoreFeatureNature());
    ignoreFeatureNatureButton.addSelectionListener(buttonListener);

    ignoreSchemaBuilderButton = new Button(composite, SWT.CHECK);
    ignoreSchemaBuilderButton.setText(Messages.ConfigurationDialog_ignoreSchemaBuilderButton_text);
    ignoreSchemaBuilderButton.setSelection(values.isIgnoreSchemaBuilder());
    ignoreSchemaBuilderButton.addSelectionListener(buttonListener);

    ignoreDebugOptionsButton = new Button(composite, SWT.CHECK);
    ignoreDebugOptionsButton.setText(Messages.ConfigurationDialog_ignoreDebugOptionsButton_text);
    ignoreDebugOptionsButton.setSelection(values.isIgnoreDebugOptions());
    ignoreDebugOptionsButton.addSelectionListener(buttonListener);

    ignoreAutomaticModuleNameButton = new Button(composite, SWT.CHECK);
    ignoreAutomaticModuleNameButton.setText(Messages.ConfigurationDialog_ignoreAutomaticModuleNameButton_text);
    ignoreAutomaticModuleNameButton.setSelection(values.isIgnoreAutomaticModuleName());
    ignoreAutomaticModuleNameButton.addSelectionListener(buttonListener);

    ignoreMissingDependencyRangesButton = new Button(composite, SWT.CHECK);
    ignoreMissingDependencyRangesButton.setText(Messages.ConfigurationDialog_ignoreMissingDependencyRangesButton_text);
    ignoreMissingDependencyRangesButton.setSelection(values.isIgnoreMissingDependencyRanges());
    ignoreMissingDependencyRangesButton.addSelectionListener(buttonListener);

    ignoreLaxLowerBoundDependencyButton = new Button(composite, SWT.CHECK);
    ignoreLaxLowerBoundDependencyButton.setText(Messages.ConfigurationDialog_ignoreLaxLowerBoundDependencyButton_text);
    ignoreLaxLowerBoundDependencyButton.setSelection(values.isIgnoreLaxLowerBoundDependencyVersions());
    ignoreLaxLowerBoundDependencyButton.addSelectionListener(buttonListener);

    ignoreMissingExportVersionsButton = new Button(composite, SWT.CHECK);
    ignoreMissingExportVersionsButton.setText(Messages.ConfigurationDialog_ignoreMissingExportVersionsButton_text);
    ignoreMissingExportVersionsButton.setSelection(values.isIgnoreMissingExportVersions());
    ignoreMissingExportVersionsButton.addSelectionListener(buttonListener);

    ignoreFeatureContentChangesButton = new Button(composite, SWT.CHECK);
    ignoreFeatureContentChangesButton.setText(Messages.ConfigurationDialog_ignoreFeatureContentChangesButton_text);
    ignoreFeatureContentChangesButton.setSelection(values.isIgnoreFeatureContentChanges());
    ignoreFeatureContentChangesButton.addSelectionListener(buttonListener);

    ignoreFeatureContentRedundancyButton = new Button(composite, SWT.CHECK);
    ignoreFeatureContentRedundancyButton.setText(Messages.ConfigurationDialog_ignoreFeatureContentRedundancyButton_text);
    ignoreFeatureContentRedundancyButton.setSelection(values.isIgnoreFeatureContentRedundancy());
    ignoreFeatureContentRedundancyButton.addSelectionListener(buttonListener);

    checkFeatureClosureCompletenessButton = new Button(composite, SWT.CHECK);
    checkFeatureClosureCompletenessButton.setText(Messages.ConfigurationDialog_checkFeatureClosureCompletenessButton_text);
    checkFeatureClosureCompletenessButton.setSelection(values.isCheckFeatureClosureCompleteness());
    checkFeatureClosureCompletenessButton.addSelectionListener(buttonListener);

    checkFeatureClosureContentButton = new Button(composite, SWT.CHECK);
    checkFeatureClosureContentButton.setText(Messages.ConfigurationDialog_checkFeatureClosureContentButton_text);
    checkFeatureClosureContentButton.setSelection(values.isCheckFeatureClosureContent());
    checkFeatureClosureContentButton.addSelectionListener(buttonListener);

    checkMavenPomButton = new Button(composite, SWT.CHECK);
    checkMavenPomButton.setText(Messages.ConfigurationDialog_checkMavenPomButton_text);
    checkMavenPomButton.setSelection(values.isCheckMavenPom());
    checkMavenPomButton.addSelectionListener(buttonListener);

    validate();
    return dialogArea;
  }

  protected void validate()
  {
    if (releasePathText.getText().trim().length() == 0)
    {
      setErrorMessage(Messages.ConfigurationDialog_emptyPathToSpecFile);
      return;
    }

    boolean redundancyCheck = !ignoreFeatureContentRedundancyButton.getSelection();
    boolean completenessCheck = checkFeatureClosureCompletenessButton.getSelection();
    if (redundancyCheck && completenessCheck)
    {
      setErrorMessage(Messages.ConfigurationDialog_error_cantDoRedundancyAndCompletenessChecksAtSameTime);
      return;
    }

    setErrorMessage(null);
  }

  @Override
  protected void okPressed()
  {
    values.setReleasePath(releasePathText.getText());
    values.setIgnoreMalformedVersions(ignoreMalformedVersionsButton.getSelection());
    values.setIgnoreFeatureNature(ignoreFeatureNatureButton.getSelection());
    values.setIgnoreSchemaBuilder(ignoreSchemaBuilderButton.getSelection());
    values.setIgnoreDebugOptions(ignoreDebugOptionsButton.getSelection());
    values.setIgnoreAutomaticModuleName(ignoreAutomaticModuleNameButton.getSelection());
    values.setIgnoreMissingDependencyRanges(ignoreMissingDependencyRangesButton.getSelection());
    values.setIgnoreLaxLowerBoundDependencyVersions(ignoreLaxLowerBoundDependencyButton.getSelection());
    values.setIgnoreMissingExportVersions(ignoreMissingExportVersionsButton.getSelection());
    values.setIgnoreFeatureContentChanges(ignoreFeatureContentChangesButton.getSelection());
    values.setIgnoreFeatureContentRedundancy(ignoreFeatureContentRedundancyButton.getSelection());
    values.setCheckFeatureClosureCompleteness(checkFeatureClosureCompletenessButton.getSelection());
    values.setCheckFeatureClosureContent(checkFeatureClosureContentButton.getSelection());
    values.setCheckMavenPom(checkMavenPomButton.getSelection());
    super.okPressed();
  }

  public String getReleasePath()
  {
    return values.getReleasePath();
  }

  public String getValidatorClassName()
  {
    return values.getValidatorClassName();
  }

  public boolean isIgnoreMalformedVersions()
  {
    return values.isIgnoreMalformedVersions();
  }

  public boolean isIgnoreFeatureNature()
  {
    return values.isIgnoreFeatureNature();
  }

  public boolean isIgnoreSchemaBuilder()
  {
    return values.isIgnoreSchemaBuilder();
  }

  public boolean isIgnoreDebugOptions()
  {
    return values.isIgnoreDebugOptions();
  }

  public boolean isIgnoreAutomaticModuleName()
  {
    return values.isIgnoreAutomaticModuleName();
  }

  public boolean isIgnoreMissingDependencyRanges()
  {
    return values.isIgnoreMissingDependencyRanges();
  }

  public boolean isIgnoreLaxLowerBoundDependencyVersions()
  {
    return values.isIgnoreLaxLowerBoundDependencyVersions();
  }

  public boolean isIgnoreMissingExportVersions()
  {
    return values.isIgnoreMissingExportVersions();
  }

  public boolean isIgnoreFeatureContentChanges()
  {
    return values.isIgnoreFeatureContentChanges();
  }

  public boolean isIgnoreFeatureContentRedundancy()
  {
    return values.isIgnoreFeatureContentRedundancy();
  }

  public boolean isCheckFeatureClosureCompleteness()
  {
    return values.isCheckFeatureClosureCompleteness();
  }

  public boolean isCheckFeatureClosureContent()
  {
    return values.isCheckFeatureClosureContent();
  }

  public boolean isCheckMavenPom()
  {
    return values.isCheckMavenPom();
  }

  public void applyTo(IProject project) throws CoreException
  {
    values.applyTo(project);
  }

  public int size()
  {
    return values.size();
  }

  public boolean isEmpty()
  {
    return values.isEmpty();
  }

  public String get(Object key)
  {
    return values.get(key);
  }

  public boolean containsKey(Object key)
  {
    return values.containsKey(key);
  }

  public String put(String key, String value)
  {
    return values.put(key, value);
  }

  public void putAll(Map<? extends String, ? extends String> m)
  {
    values.putAll(m);
  }

  public String remove(Object key)
  {
    return values.remove(key);
  }

  public void clear()
  {
    values.clear();
  }

  public boolean containsValue(Object value)
  {
    return values.containsValue(value);
  }

  public Set<String> keySet()
  {
    return values.keySet();
  }

  public Collection<String> values()
  {
    return values.values();
  }

  public Set<java.util.Map.Entry<String, String>> entrySet()
  {
    return values.entrySet();
  }

  @Override
  public boolean equals(Object o)
  {
    return values.equals(o);
  }

  @Override
  public int hashCode()
  {
    return values.hashCode();
  }
}
