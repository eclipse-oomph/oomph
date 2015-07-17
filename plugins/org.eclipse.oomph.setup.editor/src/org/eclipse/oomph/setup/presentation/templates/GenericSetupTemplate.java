/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.templates;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.editor.SetupTemplate;
import org.eclipse.oomph.setup.ui.PropertyField;
import org.eclipse.oomph.setup.util.StringExpander;
import org.eclipse.oomph.ui.LabelDecorator;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class GenericSetupTemplate extends SetupTemplate
{
  private final URI templateLocation;

  private Composite composite;

  private Scope setupScope;

  private Map<VariableTask, PropertyField> fields = new LinkedHashMap<VariableTask, PropertyField>();

  private Set<PropertyField> dirtyFields = new HashSet<PropertyField>();

  private Map<String, VariableTask> variables = new LinkedHashMap<String, VariableTask>();

  private Map<VariableTask, Set<EStructuralFeature.Setting>> usages;

  private PropertyField focusField;

  private Map<EObject, Set<EStructuralFeature>> focusUsages = new HashMap<EObject, Set<EStructuralFeature>>();

  private LabelDecorator decorator;

  public GenericSetupTemplate(String label, URI templateLocation)
  {
    super(label);
    this.templateLocation = templateLocation;
  }

  @Override
  public Control createControl(Composite parent)
  {
    composite = new Composite(parent, SWT.NONE);

    GridLayout layout = new GridLayout(3, false);
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.horizontalSpacing = 10;
    layout.verticalSpacing = 10;
    composite.setLayout(layout);

    return composite;
  }

  @Override
  public boolean isValid()
  {
    for (PropertyField field : fields.values())
    {
      if (StringUtil.isEmpty(field.getValue()))
      {
        return false;
      }
    }

    return true;
  }

  @Override
  public LabelDecorator getDecorator()
  {
    if (decorator == null)
    {
      decorator = new LabelDecorator()
      {
        @Override
        public Font decorateFont(Font font, Object element)
        {
          if (focusUsages.containsKey(element))
          {
            return ExtendedFontRegistry.INSTANCE.getFont(font, IItemFontProvider.BOLD_FONT);
          }

          if (element instanceof EStructuralFeature.Setting)
          {
            EStructuralFeature.Setting setting = (Setting)element;
            Set<EStructuralFeature> eStructuralFeatures = focusUsages.get(setting.getEObject());
            if (eStructuralFeatures != null && eStructuralFeatures.contains(setting.getEStructuralFeature()))
            {
              return ExtendedFontRegistry.INSTANCE.getFont(font, IItemFontProvider.BOLD_FONT);
            }
          }
          else if (element instanceof Resource)
          {
            VariableTask focusVariable = getFocusVariable();
            if (focusVariable != null)
            {
              String name = focusVariable.getName();
              if ("setup.location".equals(name) || "setup.filename".equals(name))
              {
                return ExtendedFontRegistry.INSTANCE.getFont(font, IItemFontProvider.BOLD_FONT);
              }
            }
          }

          return super.decorateFont(font, element);
        }
      };
    }

    return decorator;
  }

  private VariableTask getFocusVariable()
  {
    for (Map.Entry<VariableTask, PropertyField> entry : fields.entrySet())
    {
      if (entry.getValue() == focusField)
      {
        return entry.getKey();
      }
    }

    return null;
  }

  @Override
  public void updatePreview()
  {
    VariableTask focusVariable = getFocusVariable();
    if (focusVariable != null)
    {
      updateSelection(focusVariable);
    }
  }

  protected void updateSelection(VariableTask variable)
  {
    TreeViewer previewer = getContainer().getPreviewer();
    if (previewer != null)
    {
      focusUsages.clear();
      Set<Setting> settings = usages.get(variable);
      if (settings != null)
      {
        for (Setting setting : settings)
        {
          CollectionUtil.add(focusUsages, setting.getEObject(), setting.getEStructuralFeature());
        }
      }

      previewer.refresh(true);

      if (focusUsages.isEmpty())
      {
        String name = variable.getName();
        if ("setup.location".equals(name) || "setup.filename".equals(name))
        {
          previewer.setSelection(new StructuredSelection(getResource()), true);
        }
      }
      else
      {
        previewer.setSelection(new StructuredSelection(focusUsages.keySet().toArray()), true);
      }
    }
  }

  @Override
  protected void init()
  {
    super.init();

    Resource resource = getResource();
    ResourceSet resourceSet = resource.getResourceSet();
    setupScope = (Scope)resourceSet.getEObject(templateLocation, true);

    final Font normalFont = composite.getFont();
    final Font boldFont = ExtendedFontRegistry.INSTANCE.getFont(normalFont, IItemFontProvider.BOLD_FONT);

    CompoundTask compoundTask = (CompoundTask)setupScope.eResource().getEObject("template.variables");
    Control firstControl = null;
    VariableTask firstVariable = null;
    for (SetupTask setupTask : compoundTask.getSetupTasks())
    {
      final VariableTask variable = (VariableTask)setupTask;
      final PropertyField field = PropertyField.createField(variable);
      field.fill(composite);
      field.setValue(variable.getValue(), false);
      field.addValueListener(new PropertyField.ValueListener()
      {
        public void valueChanged(String oldValue, String newValue) throws Exception
        {
          dirtyFields.add(field);
          modelChanged(variable);
        }
      });

      field.getControl().addFocusListener(new FocusAdapter()
      {
        @Override
        public void focusGained(FocusEvent e)
        {
          if (focusField != null)
          {
            if (focusField != field)
            {
              focusField.getLabel().setFont(normalFont);
            }
          }

          if (focusField != field)
          {
            focusField = field;
            field.getLabel().setFont(boldFont);

            updateSelection(variable);
          }
        }
      });

      field.getLabel().setFont(boldFont);

      if (firstControl == null)
      {
        firstControl = field.getControl();
        firstVariable = variable;
      }

      variables.put(variable.getName(), variable);
      fields.put(variable, field);

      if ("setup.location".equals(variable.getName()))
      {
        field.setValue(getContainer().getDefaultLocation());
      }
    }

    Composite parent = composite.getParent();
    int currentHeight = composite.getSize().y;
    int newHeight = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
    GridData data = UIUtil.applyGridData(parent);
    data.heightHint = newHeight;

    if (currentHeight < newHeight)
    {
      Shell shell = parent.getShell();
      Point size = shell.getSize();
      shell.setSize(size.x, size.y + newHeight - currentHeight);
    }

    parent.setRedraw(false);
    parent.pack();
    parent.getParent().layout();

    for (PropertyField field : fields.values())
    {
      field.getLabel().setFont(normalFont);
    }

    parent.setRedraw(true);

    modelChanged(firstVariable);

    if (firstControl instanceof Text)
    {
      Text text = (Text)firstControl;
      text.selectAll();
    }

    firstControl.setFocus();
  }

  private void modelChanged(final VariableTask triggerVariable)
  {
    Scope copy = EcoreUtil.copy(setupScope);

    Set<PropertyField> originalDirtyPropertyFields = new HashSet<PropertyField>(dirtyFields);
    for (VariableTask variable : variables.values())
    {
      PropertyField field = fields.get(variable);
      if (!dirtyFields.contains(field))
      {
        String value = variable.getValue();
        if (!StringUtil.isEmpty(value))
        {
          value = expandString(value, null);
          field.setValue(value, false);
        }

        dirtyFields.add(field);
      }
    }

    usages = new HashMap<VariableTask, Set<EStructuralFeature.Setting>>();
    Set<EObject> eObjectsToDelete = new HashSet<EObject>();
    Set<Annotation> featureSubstitutions = new LinkedHashSet<Annotation>();
    for (Iterator<EObject> it = EcoreUtil.getAllContents(Collections.singleton(copy)); it.hasNext();)
    {
      InternalEObject eObject = (InternalEObject)it.next();
      for (EAttribute eAttribute : eObject.eClass().getEAllAttributes())
      {
        if (eAttribute.getEType().getInstanceClass() == String.class && !eAttribute.isDerived())
        {
          if (!eAttribute.isMany())
          {
            String value = (String)eObject.eGet(eAttribute);
            if (value != null)
            {
              Set<VariableTask> usedVariables = new HashSet<VariableTask>();
              String replacement = expandString(value, usedVariables);
              CollectionUtil.addAll(usages, usedVariables, eObject.eSetting(eAttribute));
              eObject.eSet(eAttribute, replacement);
            }
          }
        }
      }

      if (eObject instanceof Annotation)
      {
        Annotation annotation = (Annotation)eObject;
        if (AnnotationConstants.ANNOTATION_FEATURE_SUBSTITUTION.equals(annotation.getSource()))
        {
          featureSubstitutions.add(annotation);
          eObjectsToDelete.add(annotation);
        }
      }
      else if (eObject instanceof CompoundTask)
      {
        CompoundTask compoundTask = (CompoundTask)eObject;
        if ("template.variables".equals(compoundTask.getID()))
        {
          eObjectsToDelete.add(compoundTask);
        }
      }
    }

    for (Annotation annotation : featureSubstitutions)
    {
      ModelElement modelElement = annotation.getModelElement();
      EClass eClass = modelElement.eClass();
      for (Map.Entry<String, String> detail : annotation.getDetails())
      {
        EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(detail.getKey());
        if (eStructuralFeature instanceof EAttribute)
        {
          try
          {
            modelElement.eSet(eStructuralFeature, EcoreUtil.createFromString(((EAttribute)eStructuralFeature).getEAttributeType(), detail.getValue()));
            for (Map.Entry<VariableTask, Set<Setting>> entry : usages.entrySet())
            {
              Set<Setting> settings = entry.getValue();
              for (Setting setting : settings)
              {
                if (setting.getEObject() == detail && setting.getEStructuralFeature() == BasePackage.Literals.STRING_TO_STRING_MAP_ENTRY__VALUE)
                {
                  settings.add(((InternalEObject)modelElement).eSetting(eStructuralFeature));
                  break;
                }
              }
            }
          }
          catch (RuntimeException ex)
          {
            // Ignore.
          }
        }
      }
    }

    for (EObject eObject : eObjectsToDelete)
    {
      EcoreUtil.delete(eObject);
    }

    final Resource resource = getResource();

    final List<String> strings = new ArrayList<String>();
    final TreeViewer previewer = getContainer().getPreviewer();
    if (previewer != null)
    {
      for (Object object : previewer.getExpandedElements())
      {
        if (object instanceof EObject)
        {
          EObject eObject = (EObject)object;
          strings.add(resource.getURIFragment(eObject));
        }
      }

      previewer.getControl().setRedraw(false);
      updateResource(copy);

      UIUtil.asyncExec(new Runnable()
      {
        public void run()
        {
          if (!previewer.getControl().isDisposed())
          {
            List<EObject> eObjects = new ArrayList<EObject>();
            for (String fragment : strings)
            {
              EObject eObject = resource.getEObject(fragment);
              if (eObject != null)
              {
                eObjects.add(eObject);
              }
            }

            previewer.setExpandedElements(eObjects.toArray());
            updateSelection(triggerVariable);

            previewer.getControl().setRedraw(true);
          }
        }
      });
    }
    else
    {
      updateResource(copy);
    }

    dirtyFields = originalDirtyPropertyFields;

    getContainer().validate();
  }

  private void updateResource(Scope setup)
  {
    final Resource resource = getResource();

    EList<EObject> contents = resource.getContents();
    if (contents.isEmpty())
    {
      contents.add(setup);
    }
    else
    {
      contents.set(0, setup);
    }

    String location = expandString("${setup.location}", null);
    String fileName = expandString("${setup.filename}", null);
    resource.setURI(URI.createURI("platform:/resource" + location + "/" + fileName));
  }

  private String expandString(String string, Set<VariableTask> usedVariables)
  {
    if (string == null)
    {
      return null;
    }

    StringBuilder result = new StringBuilder();
    int previous = 0;
    for (Matcher matcher = StringExpander.STRING_EXPANSION_PATTERN.matcher(string); matcher.find();)
    {
      result.append(string.substring(previous, matcher.start()));
      String key = matcher.group(1);
      if ("$".equals(key))
      {
        result.append('$');
      }
      else
      {
        key = matcher.group(2);
        String suffix = matcher.group(5);
        VariableTask variable = variables.get(key);
        if (variable == null)
        {
          result.append(matcher.group());
        }
        else
        {
          if (usedVariables != null)
          {
            usedVariables.add(variable);
          }

          PropertyField field = fields.get(variable);
          String value = dirtyFields.contains(field) ? field.getValue() : variable.getValue();
          if (StringUtil.isEmpty(value))
          {
            result.append(matcher.group());
          }
          else
          {
            String filters = matcher.group(4);
            if (filters != null)
            {
              for (String filterName : filters.split("\\|"))
              {
                value = filter(variable, value, filterName);
              }
            }

            result.append(value);
            result.append(suffix);
          }
        }
      }

      previous = matcher.end();
    }

    result.append(string.substring(previous));
    return result.toString();
  }

  private String filter(VariableTask variable, String value, String filter)
  {
    if (filter.equals("qualifiedName"))
    {
      return value.trim().replaceAll("[^\\p{Alnum}]+", ".").toLowerCase();
    }

    if (filter.equals("camel"))
    {
      Matcher matcher = Pattern.compile("(?:[^\\p{Alnum}]+|^)(\\p{Lower})?").matcher(value);
      StringBuffer result = new StringBuffer();
      while (matcher.find())
      {
        String group = matcher.group(1);
        matcher.appendReplacement(result, group == null ? "" : group.toUpperCase());
      }

      matcher.appendTail(result);

      return result.toString();
    }

    if (filter.equals("label"))
    {
      for (VariableChoice choice : variable.getChoices())
      {
        if (value.equals(choice.getValue()))
        {
          return choice.getLabel();
        }
      }
    }

    return value;
  }
}
