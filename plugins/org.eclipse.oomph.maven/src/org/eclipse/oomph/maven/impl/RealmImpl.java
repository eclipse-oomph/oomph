/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.maven.Coordinate;
import org.eclipse.oomph.maven.Dependency;
import org.eclipse.oomph.maven.MavenFactory;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Parent;
import org.eclipse.oomph.maven.Project;
import org.eclipse.oomph.maven.Property;
import org.eclipse.oomph.maven.PropertyReference;
import org.eclipse.oomph.maven.Realm;
import org.eclipse.oomph.maven.util.POMXMLUtil;
import org.eclipse.oomph.predicates.impl.RepositoryPredicateImpl;
import org.eclipse.oomph.resources.ProjectHandler;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Realm</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.impl.RealmImpl#getSourceLocators <em>Source Locators</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.impl.RealmImpl#getProjects <em>Projects</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RealmImpl extends ModelElementImpl implements Realm
{
  private static final Set<Path> IGNORED_TARGET_FOLDERS = Set.of(Path.of("target"), Path.of("bin")); //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * The cached value of the '{@link #getSourceLocators() <em>Source Locators</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSourceLocators()
   * @generated
   * @ordered
   */
  protected EList<SourceLocator> sourceLocators;

  /**
   * The cached value of the '{@link #getProjects() <em>Projects</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProjects()
   * @generated
   * @ordered
   */
  protected EList<Project> projects;

  private final Map<Coordinate, Project> projectsByCoordinate = new TreeMap<>(Coordinate.COMPARATOR);

  private final Map<Coordinate, Project> projectsByCoordinateIgnoreVersion = new TreeMap<>(Coordinate.COMPARATOR_IGNORE_VERSION);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RealmImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return MavenPackage.Literals.REALM;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<SourceLocator> getSourceLocators()
  {
    if (sourceLocators == null)
    {
      sourceLocators = new EObjectContainmentEList<>(SourceLocator.class, this, MavenPackage.REALM__SOURCE_LOCATORS);
    }
    return sourceLocators;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Project> getProjects()
  {
    if (projects == null)
    {
      projects = new EObjectContainmentWithInverseEList<>(Project.class, this, MavenPackage.REALM__PROJECTS, MavenPackage.PROJECT__REALM);
    }
    return projects;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public void reconcile()
  {
    Collection<SourceLocator> sourceLocators = getSourceLocators();
    if (sourceLocators.isEmpty())
    {
      LinkedHashSet<Path> roots = new LinkedHashSet<>();
      for (IProject project : EcorePlugin.getWorkspaceRoot().getProjects())
      {
        Path location = RepositoryPredicateImpl.getRepositoryLocation(project);
        if (location == null || location.getParent() == null)
        {
          IPath projectLocation = project.getLocation();
          if (projectLocation != null)
          {
            location = projectLocation.toPath();
          }
        }

        if (location != null)
        {
          roots.add(location);
        }
      }

      sourceLocators = new ArrayList<>();
      for (Path path : roots)
      {
        SourceLocator sourceLocator = ResourcesFactory.eINSTANCE.createSourceLocator(path.toString());
        sourceLocator.setLocateNestedProjects(true);
        sourceLocator.getProjectFactories().add(ResourcesFactory.eINSTANCE.createMavenProjectFactory());
        sourceLocators.add(sourceLocator);
      }
    }
    else
    {
      sourceLocators = EcoreUtil.copyAll(sourceLocators);
      for (SourceLocator sourceLocator : sourceLocators)
      {
        sourceLocator.setRootFolder(StringUtil.performStringSubstitution(sourceLocator.getRootFolder()));
      }
    }

    EList<Project> projects = getProjects();

    projects.clear();
    projectsByCoordinate.clear();
    projectsByCoordinateIgnoreVersion.clear();

    List<IProject> iProjects = new ArrayList<>();
    for (SourceLocator sourceLocator : sourceLocators)
    {
      ProjectHandler projectHandler = (project, backendContainer) -> {
        synchronized (iProjects)
        {
          iProjects.add(project);
        }
      };

      MultiStatus status = new MultiStatus("_", 0, "", null); //$NON-NLS-1$ //$NON-NLS-2$
      sourceLocator.handleProjects(ECollections.emptyEList(), projectHandler, status, new NullProgressMonitor());
    }

    List<Project> reconciledProjects = new ArrayList<>();
    for (IProject project : iProjects)
    {
      IPath location = project.getLocation();
      if (location != null)
      {
        Path pomLocation = location.toPath().resolve("pom.xml"); //$NON-NLS-1$
        if (Files.isRegularFile(pomLocation) && !isInProjectTarget(pomLocation))
        {
          try
          {
            Document document = POMXMLUtil.parseDocument(pomLocation);

            Project mavenProject = MavenFactory.eINSTANCE.createProject();
            mavenProject.setLocation(pomLocation.toString());
            Element pom = document.getDocumentElement();
            mavenProject.setElement(pom);

            Element parentElement = POMXMLUtil.getElement(pom, POMXMLUtil.xpath(Parent.PARENT));
            if (parentElement != null)
            {
              Parent parent = MavenFactory.eINSTANCE.createParent();
              parent.setElement(parentElement);
              mavenProject.setParent(parent);
            }

            if (Boolean.TRUE)
            {
              List<Element> dependencyElements = POMXMLUtil.getElements(pom, POMXMLUtil.xpath(Dependency.DEPENDENCIES, Dependency.DEPENDENCY));
              EList<Dependency> dependencies = mavenProject.getDependencies();
              for (Element dependencyElement : dependencyElements)
              {
                Dependency dependency = MavenFactory.eINSTANCE.createDependency();
                dependency.setElement(dependencyElement);
                dependencies.add(dependency);
              }

              List<Element> managedDependencyElements = POMXMLUtil.getElements(pom,
                  POMXMLUtil.xpath(Dependency.DEPENDENCY_MANAGEMENT, Dependency.DEPENDENCIES, Dependency.DEPENDENCY));
              EList<Dependency> managedDependencies = mavenProject.getManagedDependencies();
              for (Element dependencyElement : managedDependencyElements)
              {
                Dependency dependency = MavenFactory.eINSTANCE.createDependency();
                dependency.setElement(dependencyElement);
                managedDependencies.add(dependency);
              }

              List<Element> propertyElements = POMXMLUtil.getElements(pom, POMXMLUtil.xpath(Property.PROPERTIES, "*")); //$NON-NLS-1$
              EList<Property> properties = mavenProject.getProperties();
              for (Element element : propertyElements)
              {
                Property property = MavenFactory.eINSTANCE.createProperty();
                property.setElement(element);
                properties.add(property);
              }

              properties.add(createProperty(document, "project.groupId", mavenProject.getGroupId())); //$NON-NLS-1$
              properties.add(createProperty(document, "project.artifactId", mavenProject.getArtifactId())); //$NON-NLS-1$
              properties.add(createProperty(document, "project.version", mavenProject.getVersion())); //$NON-NLS-1$

              ECollections.sort(properties, Property.COMPARATOR);
            }

            reconciledProjects.add(mavenProject);
          }
          catch (SAXException | IOException ex)
          {
            //$FALL-THROUGH$
          }
        }
      }
    }

    Collections.sort(reconciledProjects, Coordinate.COMPARATOR);
    projects.addAll(reconciledProjects);

    for (Project project : projects)
    {
      Parent parent = project.getParent();
      if (parent != null)
      {
        parent.setResolvedProject(getProjectIgnoreVersion(parent));

        Document document = project.getElement().getOwnerDocument();
        EList<Property> properties = project.getProperties();
        properties.add(createProperty(document, "project.parent.groupId", project.getGroupId())); //$NON-NLS-1$
        properties.add(createProperty(document, "project.parent.artifactId", project.getArtifactId())); //$NON-NLS-1$
        ECollections.sort(properties, Property.COMPARATOR);
      }
    }

    for (Project project : projects)
    {
      EList<Dependency> dependencies = project.getDependencies();
      for (Dependency dependency : dependencies)
      {
        dependency.getExpandedGroupId();
        dependency.getExpandedVersion();
      }
      ECollections.sort(dependencies, Coordinate.COMPARATOR);

      EList<Dependency> managedDependencies = project.getManagedDependencies();
      for (Dependency managedDependency : managedDependencies)
      {
        managedDependency.getExpandedGroupId();
        managedDependency.getExpandedVersion();
      }
      ECollections.sort(managedDependencies, Coordinate.COMPARATOR);

      for (Dependency dependency : dependencies)
      {
        Dependency managedDependency = project.getManagedDependency(dependency);
        if (managedDependency != null)
        {
          dependency.setResolvedManagedDependency(managedDependency);
        }
      }

      Parent parent = project.getParent();
      if (parent != null)
      {
        Project resolvedProject = parent.getResolvedProject();
        if (resolvedProject != null)
        {
          for (Dependency managedDependency : managedDependencies)
          {
            Dependency parentMmanagedDependency = resolvedProject.getManagedDependency(managedDependency);
            if (managedDependency != null)
            {
              managedDependency.setResolvedManagedDependency(parentMmanagedDependency);
            }
          }
        }
      }

      ECollections.sort(project.getIncomingParentReferences(), Coordinate.COMPARATOR);
      ECollections.sort(project.getIncomingDependencyReferences(), Coordinate.COMPARATOR);
    }

    Set<Property> allProperties = new LinkedHashSet<>();
    for (TreeIterator<EObject> it = eAllContents(); it.hasNext();)
    {
      EObject eObject = it.next();

      if (eObject instanceof Coordinate coordinate)
      {
        coordinate.getExpandedGroupId();
        coordinate.getExpandedVersion();
      }

      if (eObject instanceof Property property)
      {
        // property.getExpandedValue();
        allProperties.add(property);
      }

      if (eObject instanceof Dependency dependency)
      {
        dependency.setResolvedProject(getProjectIgnoreVersion(dependency));
      }

    }

    Set<Property> allReferencedProperties = new LinkedHashSet<>();
    for (TreeIterator<EObject> it = eAllContents(); it.hasNext();)
    {
      EObject eObject = it.next();
      if (eObject instanceof PropertyReference propertyReference)
      {
        allReferencedProperties.add(propertyReference.getResolvedProperty());
      }
    }

    if (Boolean.TRUE)
    {
      allProperties.removeAll(allReferencedProperties);
      EcoreUtil.deleteAll(allProperties, true);
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Project getProject(Coordinate coordinate)
  {
    EList<Project> projects = getProjects();
    if (!projects.isEmpty() && projectsByCoordinate.isEmpty())
    {
      for (Project project : projects)
      {
        projectsByCoordinate.put(project, project);
      }
    }

    return projectsByCoordinate.get(coordinate);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Project getProjectIgnoreVersion(Coordinate coordinate)
  {
    if (!projects.isEmpty() && projectsByCoordinateIgnoreVersion.isEmpty())
    {
      for (Project project : projects)
      {
        projectsByCoordinateIgnoreVersion.put(project, project);
      }
    }

    return projectsByCoordinateIgnoreVersion.get(coordinate);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case MavenPackage.REALM__PROJECTS:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getProjects()).basicAdd(otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case MavenPackage.REALM__SOURCE_LOCATORS:
        return ((InternalEList<?>)getSourceLocators()).basicRemove(otherEnd, msgs);
      case MavenPackage.REALM__PROJECTS:
        return ((InternalEList<?>)getProjects()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case MavenPackage.REALM__SOURCE_LOCATORS:
        return getSourceLocators();
      case MavenPackage.REALM__PROJECTS:
        return getProjects();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case MavenPackage.REALM__SOURCE_LOCATORS:
        getSourceLocators().clear();
        getSourceLocators().addAll((Collection<? extends SourceLocator>)newValue);
        return;
      case MavenPackage.REALM__PROJECTS:
        getProjects().clear();
        getProjects().addAll((Collection<? extends Project>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case MavenPackage.REALM__SOURCE_LOCATORS:
        getSourceLocators().clear();
        return;
      case MavenPackage.REALM__PROJECTS:
        getProjects().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case MavenPackage.REALM__SOURCE_LOCATORS:
        return sourceLocators != null && !sourceLocators.isEmpty();
      case MavenPackage.REALM__PROJECTS:
        return projects != null && !projects.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case MavenPackage.REALM___RECONCILE:
        reconcile();
        return null;
      case MavenPackage.REALM___GET_PROJECT__COORDINATE:
        return getProject((Coordinate)arguments.get(0));
      case MavenPackage.REALM___GET_PROJECT_IGNORE_VERSION__COORDINATE:
        return getProjectIgnoreVersion((Coordinate)arguments.get(0));
    }
    return super.eInvoke(operationID, arguments);
  }

  private boolean isInProjectTarget(Path path)
  {
    for (Path parent = path.getParent(); parent != null; parent = parent.getParent())
    {
      Path fileName = parent.getFileName();
      if (fileName != null && IGNORED_TARGET_FOLDERS.contains(fileName) && Files.isRegularFile(parent.resolve("../pom.xml"))) //$NON-NLS-1$
      {
        return true;
      }
    }

    return false;
  }

  private static Property createProperty(Document document, String key, String value)
  {
    Element propertyElement = document.createElementNS("", key); //$NON-NLS-1$
    propertyElement.appendChild(document.createTextNode(value));
    Property property = MavenFactory.eINSTANCE.createProperty();
    property.setElement(propertyElement);
    return property;
  }

  public State getState()
  {
    return new State(new ArrayList<>(projects), new LinkedHashMap<>(projectsByCoordinate), new LinkedHashMap<>(projectsByCoordinateIgnoreVersion));
  }

  public void setState(State state)
  {
    EList<Project> projects = getProjects();
    projects.clear();
    projects.addAll(state.projects);

    projectsByCoordinate.clear();
    projectsByCoordinate.putAll(state.projectsByCoordinate);

    projectsByCoordinateIgnoreVersion.clear();
    projectsByCoordinateIgnoreVersion.putAll(state.projectsByCoordinateIgnoreVersion);
  }

  public record State(List<? extends Project> projects, Map<Coordinate, Project> projectsByCoordinate,
      Map<Coordinate, Project> projectsByCoordinateIgnoreVersion)
  {
  }

} // RealmImpl
