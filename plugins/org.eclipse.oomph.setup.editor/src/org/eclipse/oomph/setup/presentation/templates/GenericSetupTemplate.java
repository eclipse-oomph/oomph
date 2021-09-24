/*
 * Copyright (c) 2015, 2016, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.templates;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.base.util.BaseResourceImpl;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.editor.SetupTemplate;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.StringFilterRegistry;
import org.eclipse.oomph.setup.ui.PropertyField;
import org.eclipse.oomph.ui.LabelDecorator;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Internal;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class GenericSetupTemplate extends SetupTemplate
{
  private static final Pattern STRING_EXPANSION_PATTERN = Pattern.compile("\\$(\\{([^${}|/]+)(\\|([^{}/]+))?([^{}]*)}|\\$)"); //$NON-NLS-1$

  private static final Pattern GIT_REPOSITORY_URL_PATTERN = Pattern.compile("\\s*url\\s*=\\s*([^ ]+)"); //$NON-NLS-1$

  private final URI templateLocation;

  private Composite composite;

  private ModelElement setupModelElement;

  private final Map<VariableTask, PropertyField> fields = new LinkedHashMap<VariableTask, PropertyField>();

  private Set<PropertyField> dirtyFields = new HashSet<PropertyField>();

  private final Map<String, VariableTask> variables = new LinkedHashMap<String, VariableTask>();

  private Map<VariableTask, Set<EStructuralFeature.Setting>> usages;

  private PropertyField focusField;

  private final Map<EObject, Set<EStructuralFeature>> focusUsages = new HashMap<EObject, Set<EStructuralFeature>>();

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
  public String getMessage()
  {
    for (PropertyField field : fields.values())
    {
      if (StringUtil.isEmpty(field.getValue()))
      {
        return ""; //$NON-NLS-1$
      }
    }

    String location = expandString("${setup.location}", null); //$NON-NLS-1$
    Path path = new Path(location);
    String[] segments = path.segments();

    if (segments.length == 0 || path.getDevice() != null)
    {
      return NLS.bind(Messages.GenericSetupTemplate_message_locationIsNotValidProjectPath, location);
    }

    String projectName = segments[0];
    if (!path.isValidSegment(projectName))
    {
      return NLS.bind(Messages.GenericSetupTemplate_message_projectNameIsNotValid, projectName);
    }

    IProject project = EcorePlugin.getWorkspaceRoot().getProject(projectName);
    if (!project.isAccessible())
    {
      return NLS.bind(Messages.GenericSetupTemplate_message_projectIsNotAccessible, projectName);
    }

    IContainer container = project;
    for (int i = 1; i < segments.length; ++i)
    {
      String folderName = segments[i];
      if (!path.isValidSegment(folderName))
      {
        return NLS.bind(Messages.GenericSetupTemplate_message_folderNameIsNotValid, folderName);
      }

      IFile file = container.getFile(new Path(folderName));
      if (file.exists())
      {
        return NLS.bind(Messages.GenericSetupTemplate_message_fileExistsAt, file.getFullPath());
      }

      container = container.getFolder(new Path(folderName));
    }

    String filename = expandString("${setup.filename}", null); //$NON-NLS-1$
    filename = expandString(filename, null);
    if (!path.isValidSegment(filename))
    {
      return NLS.bind(Messages.GenericSetupTemplate_message_fileNameIsNotValid, filename);
    }

    if (!filename.endsWith(".setup")) //$NON-NLS-1$
    {
      return NLS.bind(Messages.GenericSetupTemplate_message_fileNameMissingSetupExtension, filename);
    }

    IFile file = container.getFile(new Path(filename));
    if (file.exists())
    {
      return NLS.bind(Messages.GenericSetupTemplate_message_fileAlreadyExists, file.getFullPath());
    }

    return null;
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
              if ("setup.location".equals(name) || "setup.filename".equals(name)) //$NON-NLS-1$ //$NON-NLS-2$
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
      Set<Setting> settings = usages == null ? null : usages.get(variable);
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
        if ("setup.location".equals(name) || "setup.filename".equals(name)) //$NON-NLS-1$ //$NON-NLS-2$
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
    setupModelElement = (ModelElement)resourceSet.getEObject(templateLocation, true);
    update(setupModelElement);

    final Font normalFont = composite.getFont();
    final Font boldFont = ExtendedFontRegistry.INSTANCE.getFont(normalFont, IItemFontProvider.BOLD_FONT);

    CompoundTask compoundTask = (CompoundTask)setupModelElement.eResource().getEObject("template.variables"); //$NON-NLS-1$
    Control firstControl = null;
    VariableTask firstVariable = null;
    String defaultLocation = getContainer().getDefaultLocation();
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

      if ("setup.location".equals(variable.getName())) //$NON-NLS-1$
      {
        field.setValue(defaultLocation);
      }
    }

    computeTemplateDefaults(true);

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

  private void update(ModelElement modelElement)
  {
    ResourceSet resourceSet = modelElement.eResource().getResourceSet();
    ProjectCatalog eclipseProjectCatalog = null;
    try
    {
      // First try to load the Eclipse Project catalog from the standard location.
      URI eclipseProjectCatalogLogicalURI = SetupContext.INDEX_ROOT_URI.appendSegment("org.eclipse.projects.setup"); //$NON-NLS-1$
      Resource eclipseProjectCatalogResource = resourceSet.getResource(eclipseProjectCatalogLogicalURI, true);
      eclipseProjectCatalog = (ProjectCatalog)EcoreUtil.getObjectByType(eclipseProjectCatalogResource.getContents(), SetupPackage.Literals.PROJECT_CATALOG);
    }
    catch (Exception ex)
    {
      InputStream input = null;
      try
      {
        // If that fails, i.e., the index has been replaced by a custom index, try to load directly from Git.
        URI eclipseProjectCatalogPhysicalURI = URI.createURI("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/org.eclipse.projects.setup"); //$NON-NLS-1$
        input = URIConverter.INSTANCE.createInputStream(eclipseProjectCatalogPhysicalURI);
        Resource eclipseProjectCatalogResource = resourceSet.createResource(eclipseProjectCatalogPhysicalURI);
        eclipseProjectCatalogResource.unload();
        eclipseProjectCatalogResource.load(input, resourceSet.getLoadOptions());
        eclipseProjectCatalog = (ProjectCatalog)EcoreUtil.getObjectByType(eclipseProjectCatalogResource.getContents(), SetupPackage.Literals.PROJECT_CATALOG);
      }
      catch (IOException ex1)
      {
        //$FALL-THROUGH$
      }
      finally
      {
        IOUtil.closeSilent(input);
      }
    }

    if (eclipseProjectCatalog != null)
    {
      // The list of choices in the eclipse target platform variable, provide the key information to dynamic update the templates.
      VariableTask eclipseTargetPlatformVariable = getVariable(eclipseProjectCatalog, "eclipse.target.platform"); //$NON-NLS-1$
      if (eclipseTargetPlatformVariable != null)
      {
        // Update the product template's product release train choices.
        VariableTask productReleaseTrainVariable = getVariable(modelElement, "product.release.train"); //$NON-NLS-1$
        if (productReleaseTrainVariable != null)
        {
          EList<VariableChoice> choices = productReleaseTrainVariable.getChoices();
          choices.clear();
          Collection<VariableChoice> eclipseTargetPlatformChoices = EcoreUtil.copyAll(eclipseTargetPlatformVariable.getChoices());
          for (Iterator<VariableChoice> it = eclipseTargetPlatformChoices.iterator(); it.hasNext();)
          {
            VariableChoice variableChoice = it.next();
            String value = variableChoice.getValue();

            // Omit choices in which Oomph cannot be installed.
            if ("None".equals(value) || "Galileo".equals(value) || "Helios".equals(value) || "Indigo".equals(value)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            {
              it.remove();
            }
          }

          choices.addAll(eclipseTargetPlatformChoices);

          // Make the default the latest version.
          productReleaseTrainVariable.setValue(choices.get(0).getValue());
        }
      }

      if (modelElement instanceof ProjectCatalog)
      {
        // Replace all the tasks that depend on the available release with the copies from the latest Eclipse Project catalog.
        VariableTask targetPlatformVariable = getVariable(modelElement, "eclipse.target.platform"); //$NON-NLS-1$
        if (targetPlatformVariable != null)
        {
          EcoreUtil.replace(targetPlatformVariable, EcoreUtil.copy(eclipseTargetPlatformVariable));
        }

        VariableTask apiBaselineTargetPlatformVariable = getVariable(modelElement, "eclipse.api.baseline.target.platform"); //$NON-NLS-1$
        if (apiBaselineTargetPlatformVariable != null)
        {
          VariableTask eclipseApiBaselineTargetPlatformVariable = getVariable(eclipseProjectCatalog, "eclipse.api.baseline.target.platform"); //$NON-NLS-1$
          if (eclipseApiBaselineTargetPlatformVariable != null)
          {
            EcoreUtil.replace(apiBaselineTargetPlatformVariable, EcoreUtil.copy(eclipseApiBaselineTargetPlatformVariable));
          }
        }
        EObject projectCatalogModularTargletTask = getTargletTask(modelElement, "Modular Target"); //$NON-NLS-1$
        if (projectCatalogModularTargletTask != null)
        {
          EObject eclipseProjectCatalogModularTargletTask = getTargletTask(eclipseProjectCatalog, "Modular Target"); //$NON-NLS-1$
          if (eclipseProjectCatalogModularTargletTask != null)
          {
            EcoreUtil.replace(projectCatalogModularTargletTask, EcoreUtil.copy(eclipseProjectCatalogModularTargletTask));
          }
        }

        EObject projectCatalogModularAPIBaselineTargletTask = getTargletTask(modelElement, "Modular API Baseline Target"); //$NON-NLS-1$
        if (projectCatalogModularAPIBaselineTargletTask != null)
        {
          EObject eclipseProjectCatalogModularAPIBaselineTargletTask = getTargletTask(eclipseProjectCatalog, "Modular API Baseline Target"); //$NON-NLS-1$
          if (eclipseProjectCatalogModularAPIBaselineTargletTask != null)
          {
            EcoreUtil.replace(projectCatalogModularAPIBaselineTargletTask, EcoreUtil.copy(eclipseProjectCatalogModularAPIBaselineTargletTask));
          }
        }
      }
      else
      {
        // Update the modular targlet task with the latest set of available releases.
        EObject projectTargletTask = getTargletTask(modelElement, "Modular Target"); //$NON-NLS-1$
        if (projectTargletTask != null)
        {
          EObject projectTarglet = (EObject)((List<?>)projectTargletTask.eGet(projectTargletTask.eClass().getEStructuralFeature("targlets"))).get(0); //$NON-NLS-1$
          @SuppressWarnings("unchecked")
          List<RepositoryList> repositoryLists = (List<RepositoryList>)projectTarglet.eGet(projectTarglet.eClass().getEStructuralFeature("repositoryLists")); //$NON-NLS-1$
          RepositoryList repositoryList = repositoryLists.get(0);
          repositoryLists.clear();

          VariableTask targetPlatformVariable = getVariable(modelElement, "eclipse.target.platform"); //$NON-NLS-1$

          for (VariableChoice variableChoice : eclipseTargetPlatformVariable.getChoices())
          {
            String trainName = variableChoice.getValue();
            if (!"None".equals(trainName)) //$NON-NLS-1$
            {
              if (targetPlatformVariable != null)
              {
                // Ensure that the default choice is the latest one, and of course do that only once for the first/latest train name.
                targetPlatformVariable.setDefaultValue(trainName);
                targetPlatformVariable = null;
              }

              RepositoryList newRepositoryList = EcoreUtil.copy(repositoryList);
              newRepositoryList.setName(trainName);

              for (Repository repository : newRepositoryList.getRepositories())
              {
                if (repository.getURL().contains("download.eclipse.org/releases")) //$NON-NLS-1$
                {
                  repository.setURL("https://download.eclipse.org/releases/" + trainName.toLowerCase()); //$NON-NLS-1$
                }
              }

              repositoryLists.add(newRepositoryList);
            }
          }
        }
      }
    }
  }

  private VariableTask getVariable(ModelElement modelElement, String variableName)
  {
    for (Iterator<EObject> it = modelElement.eAllContents(); it.hasNext();)
    {
      EObject eObject = it.next();
      if (eObject instanceof VariableTask)
      {
        VariableTask variable = (VariableTask)eObject;
        if (variableName.equals(variable.getName()))
        {
          return variable;
        }
      }
    }

    return null;
  }

  private EObject getTargletTask(ModelElement modelElement, String targetName)
  {
    for (Iterator<EObject> it = modelElement.eAllContents(); it.hasNext();)
    {
      EObject eObject = it.next();
      EClass eClass = eObject.eClass();
      if ("TargletTask".equals(eClass.getName())) //$NON-NLS-1$
      {
        EStructuralFeature targetNameFeature = eClass.getEStructuralFeature("targetName"); //$NON-NLS-1$
        if (targetName.equals(eObject.eGet(targetNameFeature)))
        {
          return eObject;
        }
      }
    }

    return null;
  }

  private void modelChanged(final VariableTask triggerVariable)
  {
    computeTemplateDefaults(false);

    Copier copier = new EcoreUtil.Copier();
    ModelElement copy = (ModelElement)copier.copy(setupModelElement);
    copier.copyReferences();

    Set<Resource> resources = new HashSet<Resource>();
    for (Map.Entry<EObject, EObject> entry : copier.entrySet())
    {
      EObject key = entry.getKey();
      if (key != setupModelElement)
      {
        Internal eDirectResource = ((InternalEObject)key).eDirectResource();
        if (eDirectResource != null)
        {
          Resource resource = new BaseResourceImpl(eDirectResource.getURI());
          resources.add(resource);
          resource.getContents().add(entry.getValue());
        }
      }
    }

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
        EDataType eAttributeType = eAttribute.getEAttributeType();
        Class<?> instanceClass = eAttributeType.getInstanceClass();
        if ((instanceClass == String.class || instanceClass == URI.class) && !eAttribute.isDerived())
        {
          if (!eAttribute.isMany())
          {
            String value = EcoreUtil.convertToString(eAttributeType, eObject.eGet(eAttribute));
            if (value != null)
            {
              Set<VariableTask> usedVariables = new HashSet<VariableTask>();
              String replacement = expandString(value, usedVariables);
              CollectionUtil.addAll(usages, usedVariables, eObject.eSetting(eAttribute));
              eObject.eSet(eAttribute, EcoreUtil.createFromString(eAttributeType, replacement));
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
        if ("template.variables".equals(compoundTask.getID())) //$NON-NLS-1$
        {
          EObject eContainer = compoundTask.eContainer();
          eObjectsToDelete.add(eContainer instanceof Scope ? compoundTask : eContainer);
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

    for (Resource resource : resources)
    {
      URI uri = resource.getURI();
      String expandedURI = expandString(uri.toString(), null);
      resource.setURI(URI.createURI(expandedURI));
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

  private void updateResource(ModelElement setup)
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

    String location = expandString("${setup.location}", null); //$NON-NLS-1$
    String fileName = expandString("${setup.filename}", null); //$NON-NLS-1$
    resource.setURI(URI.createURI("platform:/resource" + new Path(location).makeAbsolute() + "/" + fileName)); //$NON-NLS-1$ //$NON-NLS-2$
  }

  private String expandString(String string, Set<VariableTask> usedVariables)
  {
    if (string == null)
    {
      return null;
    }

    StringBuilder result = new StringBuilder();
    int previous = 0;
    for (Matcher matcher = STRING_EXPANSION_PATTERN.matcher(string); matcher.find();)
    {
      result.append(string.substring(previous, matcher.start()));
      String key = matcher.group(1);
      if ("$".equals(key)) //$NON-NLS-1$
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
              for (String filterName : filters.split("\\|")) //$NON-NLS-1$
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

  private String filter(VariableTask variable, String value, String filterName)
  {
    if (filterName.equals("label")) //$NON-NLS-1$
    {
      for (VariableChoice choice : variable.getChoices())
      {
        if (value.equals(choice.getValue()))
        {
          return choice.getLabel();
        }
      }
    }

    if (filterName.equals("firstSegment")) //$NON-NLS-1$
    {
      URI uri = URI.createURI(value);
      return uri.segmentCount() > 0 ? uri.segment(0) : ""; //$NON-NLS-1$
    }

    if (filterName.equals("not")) //$NON-NLS-1$
    {
      return "false".equals(value) ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    if (filterName.equals("description")) //$NON-NLS-1$
    {
      return value.startsWith("...") ? StringUtil.NL + Messages.GenericSetupTemplate_beforeEnablingTaskReplacePlaceholderWithRepoPath //$NON-NLS-1$
          : ""; //$NON-NLS-1$
    }

    if (filterName.equals("requiredJavaVersion")) //$NON-NLS-1$
    {
      if ("Juno".equals(value)) //$NON-NLS-1$
      {
        return "1.5"; //$NON-NLS-1$
      }

      if ("Kepler".equals(value) || "Luna".equals(value)) //$NON-NLS-1$ //$NON-NLS-2$
      {
        return "1.6"; //$NON-NLS-1$
      }

      if ("Mars".equals(value)) //$NON-NLS-1$
      {
        return "1.7"; //$NON-NLS-1$
      }

      return "1.8"; //$NON-NLS-1$
    }

    if (filterName.equals("isClonePath")) //$NON-NLS-1$
    {
      return filter(variable, value, "clonePath").startsWith("...") ? "false" : "true"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }

    if (filterName.equals("clonePath")) //$NON-NLS-1$
    {
      try
      {
        IProject project = EcorePlugin.getWorkspaceRoot().getProject(new Path(value).segment(0));
        IPath location = project.getLocation();
        StringBuilder path = new StringBuilder();
        if (location != null)
        {
          for (File file = location.toFile(); file != null; file = file.getParentFile())
          {
            File[] gitFile = file.listFiles(new FileFilter()
            {
              public boolean accept(File pathname)
              {
                return ".git".equals(pathname.getName()); //$NON-NLS-1$
              }
            });

            if (gitFile.length == 1)
            {
              return path.toString();
            }

            if (path.length() != 0)
            {
              path.insert(0, '/');
            }

            path.insert(0, file.getName());
          }

          return ".../" + location.segment(0); //$NON-NLS-1$
        }
      }
      catch (Exception ex)
      {
        // Ignore.
      }

      return "..."; //$NON-NLS-1$
    }

    return StringFilterRegistry.INSTANCE.filter(value, filterName);
  }

  private void computeTemplateDefaults(boolean initial)
  {
    if (variables.containsKey("project.name")) //$NON-NLS-1$
    {
      String fileLocation = expandString("${setup.location}", null); //$NON-NLS-1$
      if (fileLocation != null && !fileLocation.equals("${setup.location}")) //$NON-NLS-1$
      {
        Path path = new Path(fileLocation);
        if (path.segmentCount() > 0)
        {
          try
          {
            IProject project = EcorePlugin.getWorkspaceRoot().getProject(path.segment(0));
            IPath location = project.getLocation();
            if (location != null)
            {
              for (File file = location.toFile(); file != null; file = file.getParentFile())
              {
                File[] gitFile = file.listFiles(new FileFilter()
                {
                  public boolean accept(File pathname)
                  {
                    return ".git".equals(pathname.getName()); //$NON-NLS-1$
                  }
                });

                if (gitFile.length == 1)
                {
                  List<String> lines = IOUtil.readLines(new File(gitFile[0], "config"), "UTF-8"); //$NON-NLS-1$ //$NON-NLS-2$
                  for (String line : lines)
                  {
                    Matcher matcher = GIT_REPOSITORY_URL_PATTERN.matcher(line);
                    if (matcher.matches())
                    {
                      URI repositoryURI = URI.createURI(matcher.group(1));
                      String host = repositoryURI.host();
                      List<String> segments = repositoryURI.isHierarchical() ? repositoryURI.segmentsList()
                          : URI.createURI(repositoryURI.opaquePart()).segmentsList();
                      String firstSegment = segments.get(0);
                      String lastSegment = segments.get(segments.size() - 1);
                      List<String> qualifiedName = StringUtil.explode(lastSegment, ".-"); //$NON-NLS-1$
                      String projectRemoteURIs = null;
                      if ("git.eclipse.org".equals(host)) //$NON-NLS-1$
                      {
                        if ("r".equals(firstSegment) || "gitroot".equals(firstSegment)) //$NON-NLS-1$ //$NON-NLS-2$
                        {
                          segments.remove(0);
                        }

                        if ("r".equals(firstSegment) || repositoryURI.port() != null) //$NON-NLS-1$
                        {
                          projectRemoteURIs = "eclipse.git.gerrit.remoteURIs"; //$NON-NLS-1$
                        }

                        if ("org".equals(qualifiedName.get(0))) //$NON-NLS-1$
                        {
                          qualifiedName.remove(0);
                        }

                        if ("eclipse".equals(qualifiedName.get(0))) //$NON-NLS-1$
                        {
                          qualifiedName.remove(0);
                        }
                      }
                      else if ("github.com".equals(host)) //$NON-NLS-1$
                      {
                        if ("eclipse".equals(firstSegment)) //$NON-NLS-1$
                        {
                          projectRemoteURIs = "github.remoteURIs"; //$NON-NLS-1$
                        }
                      }

                      String projectName = StringUtil.implode(qualifiedName, '.');

                      for (ListIterator<String> it = qualifiedName.listIterator(); it.hasNext();)
                      {
                        String nameSegment = it.next();
                        it.set(nameSegment.length() <= 4 ? nameSegment.toUpperCase() : StringUtil.cap(nameSegment));
                      }

                      String projectLabel = StringUtil.implode(qualifiedName, ' ');

                      applyVariableValue("project.label", projectLabel); //$NON-NLS-1$
                      applyVariableValue("project.name", projectName); //$NON-NLS-1$
                      applyVariableValue("project.git.path", StringUtil.implode(segments, '/')); //$NON-NLS-1$
                      if (projectRemoteURIs != null)
                      {
                        applyVariableValue("project.remote.uris", projectRemoteURIs); //$NON-NLS-1$
                      }

                      return;
                    }
                  }
                }
              }
            }
          }
          catch (Exception ex)
          {
            // Ignore.
          }

          String firstSegment = path.segment(0);
          List<String> qualifiedName = StringUtil.explode(firstSegment, ".-_ "); //$NON-NLS-1$
          if (!qualifiedName.isEmpty())
          {
            ArrayList<String> domainPrefixes = new ArrayList<String>(Arrays.asList(Locale.getISOCountries()));
            domainPrefixes.add("COM"); //$NON-NLS-1$
            domainPrefixes.add("ORG"); //$NON-NLS-1$
            if (domainPrefixes.contains(qualifiedName.get(0).toUpperCase()))
            {
              qualifiedName.remove(0);
              if (qualifiedName.size() > 1)
              {
                qualifiedName.remove(0);
              }
            }

            String nameSegment = qualifiedName.get(0);
            String projectName = nameSegment.toLowerCase();
            String projectLabel = nameSegment.length() <= 4 ? nameSegment.toUpperCase() : StringUtil.cap(nameSegment);
            applyVariableValue("project.label", projectLabel); //$NON-NLS-1$
            applyVariableValue("project.name", projectName); //$NON-NLS-1$
            return;
          }
        }
      }

      restoreDefaultValue("project.label"); //$NON-NLS-1$
      restoreDefaultValue("project.name"); //$NON-NLS-1$
      restoreDefaultValue("project.git.path"); //$NON-NLS-1$
      restoreDefaultValue("project.remote.uris"); //$NON-NLS-1$
    }
    else if (initial && templateLocation.lastSegment().contains("Copy") && setupModelElement instanceof Configuration) //$NON-NLS-1$
    {
      Configuration configuration = (Configuration)setupModelElement;
      SetupContext setupContext = SetupContext.create(setupModelElement.eResource().getResourceSet());

      Installation installation = configuration.getInstallation();
      if (installation != null)
      {
        Installation actualInstallation = setupContext.getInstallation();
        if (actualInstallation != null)
        {
          ProductVersion productVersion = actualInstallation.getProductVersion();
          if (productVersion != null)
          {
            Resource eResource = productVersion.eResource();
            if (eResource != null && !"catalog".equals(eResource.getURI().scheme())) //$NON-NLS-1$
            {
              installation.setProductVersion(productVersion);
            }
          }

          copyContents(installation, actualInstallation);
          computeDefaultAttributes(installation, actualInstallation, "installation."); //$NON-NLS-1$
        }
      }

      Workspace workspace = configuration.getWorkspace();
      if (workspace != null)
      {
        Workspace actualWorkspace = setupContext.getWorkspace();
        if (actualWorkspace != null)
        {
          EList<Stream> streams = actualWorkspace.getStreams();
          workspace.getStreams().addAll(streams);
          copyContents(workspace, actualWorkspace);
          computeDefaultAttributes(workspace, actualWorkspace, "workspace."); //$NON-NLS-1$
        }
      }
    }
  }

  private void copyContents(Scope target, Scope source)
  {
    EList<Annotation> annotations = source.getAnnotations();
    EList<SetupTask> setupTasks = source.getSetupTasks();

    EcoreUtil.Copier copier = new EcoreUtil.Copier();
    Collection<Annotation> annotationCopies = copier.copyAll(annotations);
    Collection<SetupTask> setupTaskCopies = copier.copyAll(setupTasks);
    copier.copyReferences();

    target.getAnnotations().addAll(annotationCopies);
    target.getSetupTasks().addAll(setupTaskCopies);

    Set<EObject> eObjectsToDelete = new HashSet<EObject>();
    for (Iterator<EObject> it = target.eAllContents(); it.hasNext();)
    {
      EObject eObject = it.next();
      if (eObject instanceof SetupTask && !((SetupTask)eObject).getRestrictions().isEmpty())
      {
        eObjectsToDelete.add(eObject);
      }
    }

    EcoreUtil.deleteAll(eObjectsToDelete, true);
  }

  private void computeDefaultAttributes(Scope target, Scope source, String variablePrefix)
  {
    String name = source.getName();
    if (!StringUtil.isEmpty(name) && name != null && !(name + ".").equals(variablePrefix)) //$NON-NLS-1$
    {
      applyVariableValue(variablePrefix + "name", name); //$NON-NLS-1$
    }

    String label = source.getLabel();
    if (!StringUtil.isEmpty(label))
    {
      applyVariableValue(variablePrefix + "label", label); //$NON-NLS-1$
    }

    String description = source.getDescription();
    if (!StringUtil.isEmpty(description))
    {
      applyVariableValue(variablePrefix + "description", description); //$NON-NLS-1$
    }
  }

  private void applyVariableValue(String name, String value)
  {
    VariableTask variable = variables.get(name);
    if (variable != null)
    {
      PropertyField field = fields.get(variable);
      if (!dirtyFields.contains(field))
      {
        if (variable.getDefaultValue() == null)
        {
          variable.setDefaultValue(variable.getValue());
        }
        variable.setValue(value);
        field.setValue(value, false);
      }
    }
  }

  private void restoreDefaultValue(String name)
  {
    VariableTask variable = variables.get(name);
    if (variable != null)
    {
      PropertyField field = fields.get(variable);
      if (!dirtyFields.contains(field))
      {
        String defaultValue = variable.getDefaultValue();
        if (defaultValue != null)
        {
          variable.setValue(defaultValue);
          field.setValue(defaultValue, false);
        }
      }
    }
  }
}
