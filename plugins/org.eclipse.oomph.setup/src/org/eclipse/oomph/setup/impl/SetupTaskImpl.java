/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.setup.EAnnotationConstants;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.SetupTaskImpl#getID <em>ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.SetupTaskImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.SetupTaskImpl#getScopeType <em>Scope Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.SetupTaskImpl#getExcludedTriggers <em>Excluded Triggers</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.SetupTaskImpl#isDisabled <em>Disabled</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.SetupTaskImpl#getPredecessors <em>Predecessors</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.SetupTaskImpl#getSuccessors <em>Successors</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.SetupTaskImpl#getRestrictions <em>Restrictions</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.SetupTaskImpl#getFilter <em>Filter</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class SetupTaskImpl extends ModelElementImpl implements SetupTask
{
  private static Map<EClass, Set<Trigger>> TRIGGERS = Collections.synchronizedMap(new HashMap<EClass, Set<Trigger>>());

  /**
   * The default value of the '{@link #getID() <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getID()
   * @generated
   * @ordered
   */
  protected static final String ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getID() <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getID()
   * @generated
   * @ordered
   */
  protected String iD = ID_EDEFAULT;

  /**
   * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDescription()
   * @generated
   * @ordered
   */
  protected static final String DESCRIPTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDescription()
   * @generated
   * @ordered
   */
  protected String description = DESCRIPTION_EDEFAULT;

  /**
   * The default value of the '{@link #getScopeType() <em>Scope Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getScopeType()
   * @generated
   * @ordered
   */
  protected static final ScopeType SCOPE_TYPE_EDEFAULT = ScopeType.NONE;

  /**
   * The cached value of the '{@link #getExcludedTriggers() <em>Excluded Triggers</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExcludedTriggers()
   * @generated
   * @ordered
   */
  protected Set<Trigger> excludedTriggers;

  /**
   * The default value of the '{@link #isDisabled() <em>Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isDisabled()
   * @generated
   * @ordered
   */
  protected static final boolean DISABLED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isDisabled() <em>Disabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isDisabled()
   * @generated
   * @ordered
   */
  protected boolean disabled = DISABLED_EDEFAULT;

  /**
   * The cached value of the '{@link #getPredecessors() <em>Predecessors</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPredecessors()
   * @generated
   * @ordered
   */
  protected EList<SetupTask> predecessors;

  /**
   * The cached value of the '{@link #getSuccessors() <em>Successors</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSuccessors()
   * @generated
   * @ordered
   */
  protected EList<SetupTask> successors;

  /**
   * The cached value of the '{@link #getRestrictions() <em>Restrictions</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRestrictions()
   * @generated
   * @ordered
   */
  protected EList<Scope> restrictions;

  /**
   * The default value of the '{@link #getFilter() <em>Filter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilter()
   * @generated
   * @ordered
   */
  protected static final String FILTER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFilter() <em>Filter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilter()
   * @generated
   * @ordered
   */
  protected String filter = FILTER_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected SetupTaskImpl()
  {
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return SetupPackage.Literals.SETUP_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getID()
  {
    return iD;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setID(String newID)
  {
    String oldID = iD;
    iD = newID;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.SETUP_TASK__ID, oldID, iD));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public EList<SetupTask> getPredecessors()
  {
    if (predecessors == null)
    {
      predecessors = new EObjectResolvingEList<SetupTask>(SetupTaskImpl.class, this, SetupPackage.SETUP_TASK__PREDECESSORS);
    }
    return predecessors;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public EList<SetupTask> getSuccessors()
  {
    if (successors == null)
    {
      successors = new EObjectResolvingEList<SetupTask>(SetupTaskImpl.class, this, SetupPackage.SETUP_TASK__SUCCESSORS);
    }
    return successors;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Scope> getRestrictions()
  {
    if (restrictions == null)
    {
      restrictions = new EObjectResolvingEList<Scope>(Scope.class, this, SetupPackage.SETUP_TASK__RESTRICTIONS);
    }
    return restrictions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getFilter()
  {
    return filter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFilter(String newFilter)
  {
    String oldFilter = filter;
    filter = newFilter;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.SETUP_TASK__FILTER, oldFilter, filter));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public ScopeType getScopeType()
  {
    return getScope(this);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Set<Trigger> getExcludedTriggers()
  {
    return excludedTriggers == null ? Collections.<Trigger> emptySet() : excludedTriggers;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExcludedTriggersGen(Set<Trigger> newExcludedTriggers)
  {
    Set<Trigger> oldExcludedTriggers = excludedTriggers;
    excludedTriggers = newExcludedTriggers;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS, oldExcludedTriggers, excludedTriggers));
    }
  }

  public void setExcludedTriggers(Set<Trigger> newExcludedTriggers)
  {
    setExcludedTriggersGen(newExcludedTriggers == null || newExcludedTriggers.isEmpty() ? null : newExcludedTriggers);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDescription(String newDescription)
  {
    String oldDescription = description;
    description = newDescription;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.SETUP_TASK__DESCRIPTION, oldDescription, description));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Scope getScope()
  {
    for (EObject container = eContainer(); container != null; container = container.eContainer())
    {
      if (container instanceof Scope)
      {
        return (Scope)container;
      }
    }

    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isDisabled()
  {
    return disabled;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDisabled(boolean newDisabled)
  {
    boolean oldDisabled = disabled;
    disabled = newDisabled;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.SETUP_TASK__DISABLED, oldDisabled, disabled));
    }
  }

  private ScopeType getScope(EObject object)
  {
    if (object instanceof ProjectCatalog)
    {
      return ScopeType.PROJECT_CATALOG;
    }

    if (object instanceof Installation)
    {
      return ScopeType.INSTALLATION;
    }

    if (object instanceof Workspace)
    {
      return ScopeType.WORKSPACE;
    }

    if (object instanceof ProductCatalog)
    {
      return ScopeType.PRODUCT_CATALOG;
    }

    if (object instanceof Product)
    {
      return ScopeType.PRODUCT;
    }

    if (object instanceof ProductVersion)
    {
      return ScopeType.PRODUCT_VERSION;
    }

    if (object instanceof Project)
    {
      return ScopeType.PROJECT;
    }

    if (object instanceof Stream)
    {
      return ScopeType.STREAM;
    }

    if (object instanceof User)
    {
      return ScopeType.USER;
    }

    EObject container = object.eContainer();
    if (container == null)
    {
      return ScopeType.NONE;
    }

    return getScope(container);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean requires(SetupTask setupTask)
  {
    return visitPredecessors(setupTask, new HashSet<SetupTask>()) || ((SetupTaskImpl)setupTask).visitSuccessors(this, new HashSet<SetupTask>());
  }

  public boolean requiresFast(SetupTask setupTask)
  {
    return visitPredecessorsFast(setupTask, new HashSet<SetupTask>()) || ((SetupTaskImpl)setupTask).visitSuccessorsFast(this, new HashSet<SetupTask>());
  }

  private static Set<Trigger> getTriggers(EClass eClass)
  {
    Set<Trigger> result = TRIGGERS.get(eClass);
    if (result == null)
    {
      String triggers = EcoreUtil.getAnnotation(eClass, EAnnotationConstants.ANNOTATION_VALID_TRIGGERS, EAnnotationConstants.KEY_TRIGGERS);
      if (triggers != null)
      {
        String[] triggerValueLiterals = triggers.split("\\s");
        Trigger[] triggerValues = new Trigger[triggerValueLiterals.length];
        for (int i = 0; i < triggerValueLiterals.length; ++i)
        {
          triggerValues[i] = Trigger.get(triggerValueLiterals[i]);
        }

        result = Trigger.toSet(triggerValues);
      }
      else
      {
        result = Trigger.ALL_TRIGGERS;
      }
      TRIGGERS.put(eClass, result);
    }

    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public final Set<Trigger> getValidTriggers()
  {
    return getTriggers(eClass());
  }

  public int getPriority()
  {
    return PRIORITY_DEFAULT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public final Set<Trigger> getTriggers()
  {
    Set<Trigger> excludedTriggers = getExcludedTriggers();
    if (excludedTriggers == null || excludedTriggers.isEmpty())
    {
      return getValidTriggers();
    }

    Set<Trigger> result = new HashSet<Trigger>(getValidTriggers());
    result.removeAll(excludedTriggers);
    return Trigger.intern(result);
  }

  private boolean visitPredecessorsFast(SetupTask setupTask, Set<SetupTask> visited)
  {
    if (visited.add(this))
    {
      if (setupTask == this)
      {
        return true;
      }

      if (predecessors != null)
      {
        SetupTaskImpl[] data = (SetupTaskImpl[])((BasicEList<SetupTask>)predecessors).data();
        if (data != null)
        {
          for (int i = 0, length = data.length; i < length; ++i)
          {
            SetupTaskImpl requirement = data[i];
            if (requirement == null)
            {
              break;
            }

            if (requirement.visitPredecessorsFast(setupTask, visited))
            {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  private boolean visitSuccessorsFast(SetupTask setupTask, Set<SetupTask> visited)
  {
    if (visited.add(this))
    {
      if (setupTask == this)
      {
        return true;
      }

      if (successors != null)
      {
        SetupTaskImpl[] data = (SetupTaskImpl[])((BasicEList<SetupTask>)successors).data();
        if (data != null)
        {
          for (int i = 0, length = data.length; i < length; ++i)
          {
            SetupTaskImpl requirement = data[i];
            if (requirement == null)
            {
              break;
            }

            if (requirement.visitSuccessorsFast(setupTask, visited))
            {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  private boolean visitPredecessors(SetupTask setupTask, Set<SetupTask> visited)
  {
    if (visited.add(this))
    {
      if (setupTask == this)
      {
        return true;
      }

      if (predecessors != null && !predecessors.isEmpty())
      {
        for (SetupTask requirement : getPredecessors())
        {
          if (((SetupTaskImpl)requirement).visitPredecessors(setupTask, visited))
          {
            return true;
          }
        }
      }
    }

    return false;
  }

  private boolean visitSuccessors(SetupTask setupTask, Set<SetupTask> visited)
  {
    if (visited.add(this))
    {
      if (setupTask == this)
      {
        return true;
      }

      if (successors != null && !successors.isEmpty())
      {
        for (SetupTask requirement : successors)
        {
          if (((SetupTaskImpl)requirement).visitSuccessors(setupTask, visited))
          {
            return true;
          }
        }
      }
    }

    return false;
  }

  protected final Object createToken(String value)
  {
    return new TypedStringToken(eClass(), value);
  }

  /**
   * Subclasses may override to indicate that this task overrides another task with the same token.
   *
   * @see #createToken(String)
   */
  public Object getOverrideToken()
  {
    return this;
  }

  public void overrideFor(SetupTask overriddenSetupTask)
  {
    EList<SetupTask> overriddenPredecessors = overriddenSetupTask.getPredecessors();
    if (!overriddenPredecessors.isEmpty())
    {
      getPredecessors().addAll(overriddenPredecessors);
    }

    EList<SetupTask> overriddenSuccessors = overriddenSetupTask.getSuccessors();
    if (!overriddenSuccessors.isEmpty())
    {
      getSuccessors().addAll(overriddenSuccessors);
    }

    EList<Scope> overriddenRestrictions = overriddenSetupTask.getRestrictions();
    if (!overriddenRestrictions.isEmpty())
    {
      getRestrictions().addAll(overriddenRestrictions);
    }
  }

  public void consolidate()
  {
  }

  public int getProgressMonitorWork()
  {
    return 1;
  }

  /**
   * Subclasses may override to reset this task to its initial state.
   */
  public void dispose()
  {
    // TODO Move all these framework hooks out of the public API
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
      case SetupPackage.SETUP_TASK__ID:
        return getID();
      case SetupPackage.SETUP_TASK__DESCRIPTION:
        return getDescription();
      case SetupPackage.SETUP_TASK__SCOPE_TYPE:
        return getScopeType();
      case SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS:
        return getExcludedTriggers();
      case SetupPackage.SETUP_TASK__DISABLED:
        return isDisabled();
      case SetupPackage.SETUP_TASK__PREDECESSORS:
        return getPredecessors();
      case SetupPackage.SETUP_TASK__SUCCESSORS:
        return getSuccessors();
      case SetupPackage.SETUP_TASK__RESTRICTIONS:
        return getRestrictions();
      case SetupPackage.SETUP_TASK__FILTER:
        return getFilter();
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
      case SetupPackage.SETUP_TASK__ID:
        setID((String)newValue);
        return;
      case SetupPackage.SETUP_TASK__DESCRIPTION:
        setDescription((String)newValue);
        return;
      case SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS:
        setExcludedTriggers((Set<Trigger>)newValue);
        return;
      case SetupPackage.SETUP_TASK__DISABLED:
        setDisabled((Boolean)newValue);
        return;
      case SetupPackage.SETUP_TASK__PREDECESSORS:
        getPredecessors().clear();
        getPredecessors().addAll((Collection<? extends SetupTask>)newValue);
        return;
      case SetupPackage.SETUP_TASK__SUCCESSORS:
        getSuccessors().clear();
        getSuccessors().addAll((Collection<? extends SetupTask>)newValue);
        return;
      case SetupPackage.SETUP_TASK__RESTRICTIONS:
        getRestrictions().clear();
        getRestrictions().addAll((Collection<? extends Scope>)newValue);
        return;
      case SetupPackage.SETUP_TASK__FILTER:
        setFilter((String)newValue);
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
      case SetupPackage.SETUP_TASK__ID:
        setID(ID_EDEFAULT);
        return;
      case SetupPackage.SETUP_TASK__DESCRIPTION:
        setDescription(DESCRIPTION_EDEFAULT);
        return;
      case SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS:
        setExcludedTriggers((Set<Trigger>)null);
        return;
      case SetupPackage.SETUP_TASK__DISABLED:
        setDisabled(DISABLED_EDEFAULT);
        return;
      case SetupPackage.SETUP_TASK__PREDECESSORS:
        getPredecessors().clear();
        return;
      case SetupPackage.SETUP_TASK__SUCCESSORS:
        getSuccessors().clear();
        return;
      case SetupPackage.SETUP_TASK__RESTRICTIONS:
        getRestrictions().clear();
        return;
      case SetupPackage.SETUP_TASK__FILTER:
        setFilter(FILTER_EDEFAULT);
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
      case SetupPackage.SETUP_TASK__ID:
        return ID_EDEFAULT == null ? iD != null : !ID_EDEFAULT.equals(iD);
      case SetupPackage.SETUP_TASK__DESCRIPTION:
        return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
      case SetupPackage.SETUP_TASK__SCOPE_TYPE:
        return getScopeType() != SCOPE_TYPE_EDEFAULT;
      case SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS:
        return excludedTriggers != null;
      case SetupPackage.SETUP_TASK__DISABLED:
        return disabled != DISABLED_EDEFAULT;
      case SetupPackage.SETUP_TASK__PREDECESSORS:
        return predecessors != null && !predecessors.isEmpty();
      case SetupPackage.SETUP_TASK__SUCCESSORS:
        return successors != null && !successors.isEmpty();
      case SetupPackage.SETUP_TASK__RESTRICTIONS:
        return restrictions != null && !restrictions.isEmpty();
      case SetupPackage.SETUP_TASK__FILTER:
        return FILTER_EDEFAULT == null ? filter != null : !FILTER_EDEFAULT.equals(filter);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (iD: ");
    result.append(iD);
    result.append(", description: ");
    result.append(description);
    result.append(", excludedTriggers: ");
    result.append(excludedTriggers);
    result.append(", disabled: ");
    result.append(disabled);
    result.append(", filter: ");
    result.append(filter);
    result.append(')');
    return result.toString();
  }

  protected URI createResolvedURI(String uri)
  {
    if (uri == null)
    {
      return null;
    }

    URI result = URI.createURI(uri);
    if (result.isRelative() && result.hasRelativePath())
    {
      Resource resource = eResource();
      URI baseURI = resource.getURI();
      if (baseURI != null && baseURI.isHierarchical() && !baseURI.isRelative())
      {
        return result.resolve(baseURI);
      }
    }

    return result;
  }

  protected final void performUI(final SetupTaskContext context, final RunnableWithContext runnable) throws Exception
  {
    final Exception[] exception = { null };

    UserCallback callback = context.getPrompter().getUserCallback();
    callback.execInUI(false, new Runnable()
    {
      public void run()
      {
        try
        {
          runnable.run(context);
        }
        catch (Exception ex)
        {
          exception[0] = ex;
        }
      }
    });

    if (exception[0] != null)
    {
      throw exception[0];
    }
  }

  /**
   * @author Eike Stepper
   */
  protected interface RunnableWithContext
  {
    public void run(SetupTaskContext context) throws Exception;
  }

  /**
   * @author Eike Stepper
   */
  protected static final class TypedStringToken
  {
    private final Object type;

    private final String value;

    public TypedStringToken(Object type, String value)
    {
      this.type = type;
      this.value = value;
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + (type == null ? 0 : type.hashCode());
      result = prime * result + (value == null ? 0 : value.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (this == obj)
      {
        return true;
      }

      if (obj == null)
      {
        return false;
      }

      if (getClass() != obj.getClass())
      {
        return false;
      }

      TypedStringToken other = (TypedStringToken)obj;
      if (type == null)
      {
        if (other.type != null)
        {
          return false;
        }
      }
      else if (!type.equals(other.type))
      {
        return false;
      }

      if (value == null)
      {
        if (other.value != null)
        {
          return false;
        }
      }
      else if (!value.equals(other.value))
      {
        return false;
      }

      return true;
    }
  }
} // SetupTaskImpl
