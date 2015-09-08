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
package org.eclipse.oomph.predicates.impl;

import org.eclipse.oomph.predicates.AndPredicate;
import org.eclipse.oomph.predicates.BuilderPredicate;
import org.eclipse.oomph.predicates.CommentPredicate;
import org.eclipse.oomph.predicates.FilePredicate;
import org.eclipse.oomph.predicates.ImportedPredicate;
import org.eclipse.oomph.predicates.LocationPredicate;
import org.eclipse.oomph.predicates.NamePredicate;
import org.eclipse.oomph.predicates.NaturePredicate;
import org.eclipse.oomph.predicates.NotPredicate;
import org.eclipse.oomph.predicates.OrPredicate;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.predicates.PredicatesFactory;
import org.eclipse.oomph.predicates.PredicatesPackage;
import org.eclipse.oomph.predicates.RepositoryPredicate;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

import java.io.File;
import java.util.Arrays;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PredicatesFactoryImpl extends EFactoryImpl implements PredicatesFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static PredicatesFactory init()
  {
    try
    {
      PredicatesFactory thePredicatesFactory = (PredicatesFactory)EPackage.Registry.INSTANCE.getEFactory(PredicatesPackage.eNS_URI);
      if (thePredicatesFactory != null)
      {
        return thePredicatesFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new PredicatesFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PredicatesFactoryImpl()
  {
    super();
  }

  @Override
  public EObject create(EClass eClass)
  {
    if (eClass == PredicatesPackage.Literals.PREDICATE)
    {
      return new PredicateImpl()
      {
        @Override
        public boolean matches(IResource resource)
        {
          return false;
        }
      };
    }

    return createGen(eClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EObject createGen(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case PredicatesPackage.NAME_PREDICATE:
        return createNamePredicate();
      case PredicatesPackage.COMMENT_PREDICATE:
        return createCommentPredicate();
      case PredicatesPackage.LOCATION_PREDICATE:
        return createLocationPredicate();
      case PredicatesPackage.REPOSITORY_PREDICATE:
        return createRepositoryPredicate();
      case PredicatesPackage.AND_PREDICATE:
        return createAndPredicate();
      case PredicatesPackage.OR_PREDICATE:
        return createOrPredicate();
      case PredicatesPackage.NOT_PREDICATE:
        return createNotPredicate();
      case PredicatesPackage.NATURE_PREDICATE:
        return createNaturePredicate();
      case PredicatesPackage.BUILDER_PREDICATE:
        return createBuilderPredicate();
      case PredicatesPackage.FILE_PREDICATE:
        return createFilePredicate();
      case PredicatesPackage.IMPORTED_PREDICATE:
        return createImportedPredicate();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case PredicatesPackage.PROJECT:
        return createProjectFromString(eDataType, initialValue);
      case PredicatesPackage.RESOURCE:
        return createResourceFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case PredicatesPackage.PROJECT:
        return convertProjectToString(eDataType, instanceValue);
      case PredicatesPackage.RESOURCE:
        return convertResourceToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NamePredicate createNamePredicate()
  {
    NamePredicateImpl namePredicate = new NamePredicateImpl();
    return namePredicate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CommentPredicate createCommentPredicate()
  {
    CommentPredicateImpl commentPredicate = new CommentPredicateImpl();
    return commentPredicate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LocationPredicate createLocationPredicate()
  {
    LocationPredicateImpl locationPredicate = new LocationPredicateImpl();
    return locationPredicate;
  }

  public NamePredicate createNamePredicate(String pattern)
  {
    NamePredicate result = createNamePredicate();
    result.setPattern(pattern);
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RepositoryPredicate createRepositoryPredicate()
  {
    RepositoryPredicateImpl repositoryPredicate = new RepositoryPredicateImpl();
    return repositoryPredicate;
  }

  public RepositoryPredicate createRepositoryPredicate(IProject project)
  {
    RepositoryPredicate result = createRepositoryPredicate();
    result.setProject(project);
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AndPredicate createAndPredicate()
  {
    AndPredicateImpl andPredicate = new AndPredicateImpl();
    return andPredicate;
  }

  public AndPredicate createAndPredicate(Predicate... operands)
  {
    AndPredicate result = createAndPredicate();
    result.getOperands().addAll(Arrays.asList(operands));
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OrPredicate createOrPredicate()
  {
    OrPredicateImpl orPredicate = new OrPredicateImpl();
    return orPredicate;
  }

  public OrPredicate createOrPredicate(Predicate... operands)
  {
    OrPredicate result = createOrPredicate();
    result.getOperands().addAll(Arrays.asList(operands));
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotPredicate createNotPredicate()
  {
    NotPredicateImpl notPredicate = new NotPredicateImpl();
    return notPredicate;
  }

  public NotPredicate createNotPredicate(Predicate operand)
  {
    NotPredicate result = createNotPredicate();
    result.setOperand(operand);
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NaturePredicate createNaturePredicate()
  {
    NaturePredicateImpl naturePredicate = new NaturePredicateImpl();
    return naturePredicate;
  }

  public NaturePredicate createNaturePredicate(String nature)
  {
    NaturePredicate result = createNaturePredicate();
    result.setNature(nature);
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BuilderPredicate createBuilderPredicate()
  {
    BuilderPredicateImpl builderPredicate = new BuilderPredicateImpl();
    return builderPredicate;
  }

  public BuilderPredicate createBuilderPredicate(String builder)
  {
    BuilderPredicate result = createBuilderPredicate();
    result.setBuilder(builder);
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FilePredicate createFilePredicate()
  {
    FilePredicateImpl filePredicate = new FilePredicateImpl();
    return filePredicate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ImportedPredicate createImportedPredicate()
  {
    ImportedPredicateImpl importedPredicate = new ImportedPredicateImpl();
    return importedPredicate;
  }

  public FilePredicate createFilePredicate(String filePattern)
  {
    FilePredicate result = createFilePredicate();
    result.setFilePattern(filePattern);
    return result;
  }

  public FilePredicate createFilePredicate(String filePattern, String contentPattern)
  {
    FilePredicate result = createFilePredicate(filePattern);
    result.setContentPattern(contentPattern);
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public IProject createProjectFromString(EDataType eDataType, String initialValue)
  {
    if (initialValue == null)
    {
      return null;
    }

    return ResourcesPlugin.getWorkspace().getRoot().getProject(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String convertProjectToString(EDataType eDataType, Object instanceValue)
  {
    if (instanceValue == null)
    {
      return null;
    }

    IProject project = (IProject)instanceValue;
    return project.getName();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public IResource createResourceFromString(EDataType eDataType, String initialValue)
  {
    if (initialValue == null)
    {
      return null;
    }

    if (initialValue.endsWith("/"))
    {
      return ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(initialValue));
    }

    return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(initialValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String convertResourceToString(EDataType eDataType, Object instanceValue)
  {
    if (instanceValue == null)
    {
      return null;
    }

    IResource resource = (IResource)instanceValue;
    if (resource.getType() == IResource.FILE)
    {
      return resource.getFullPath().toString();
    }

    return resource.getFullPath() + "/";
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public File createFileFromString(EDataType eDataType, String initialValue)
  {
    return new File(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String convertFileToString(EDataType eDataType, Object instanceValue)
  {
    return ((File)instanceValue).getAbsolutePath();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PredicatesPackage getPredicatesPackage()
  {
    return (PredicatesPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static PredicatesPackage getPackage()
  {
    return PredicatesPackage.eINSTANCE;
  }

} // PredicatesFactoryImpl
