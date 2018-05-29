/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Ericsson AB (Julian Enoch) - Bug 425815 - Add authentication to build server
 */
package org.eclipse.oomph.setup.mylyn.impl;

import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.mylyn.MylynPackage;
import org.eclipse.oomph.setup.mylyn.MylynQueriesTask;
import org.eclipse.oomph.setup.mylyn.Query;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.mylyn.commons.net.AuthenticationCredentials;
import org.eclipse.mylyn.commons.net.AuthenticationType;
import org.eclipse.mylyn.internal.tasks.core.RepositoryQuery;
import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.internal.tasks.ui.util.TasksUiInternal;
import org.eclipse.mylyn.tasks.core.AbstractRepositoryConnector;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mylyn Queries Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.impl.MylynQueriesTaskImpl#getConnectorKind <em>Connector Kind</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.impl.MylynQueriesTaskImpl#getRepositoryURL <em>Repository URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.impl.MylynQueriesTaskImpl#getUserID <em>User ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.impl.MylynQueriesTaskImpl#getPassword <em>Password</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.impl.MylynQueriesTaskImpl#getQueries <em>Queries</em>}</li>
 * </ul>
 *
 * @generated
 */
@SuppressWarnings("restriction")
public class MylynQueriesTaskImpl extends SetupTaskImpl implements MylynQueriesTask
{
  /**
   * The default value of the '{@link #getConnectorKind() <em>Connector Kind</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectorKind()
   * @generated
   * @ordered
   */
  protected static final String CONNECTOR_KIND_EDEFAULT = "bugzilla";

  /**
   * The cached value of the '{@link #getConnectorKind() <em>Connector Kind</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectorKind()
   * @generated
   * @ordered
   */
  protected String connectorKind = CONNECTOR_KIND_EDEFAULT;

  /**
   * The default value of the '{@link #getRepositoryURL() <em>Repository URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepositoryURL()
   * @generated
   * @ordered
   */
  protected static final String REPOSITORY_URL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRepositoryURL() <em>Repository URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepositoryURL()
   * @generated
   * @ordered
   */
  protected String repositoryURL = REPOSITORY_URL_EDEFAULT;

  /**
   * The default value of the '{@link #getUserID() <em>User ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUserID()
   * @generated
   * @ordered
   */
  protected static final String USER_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUserID() <em>User ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUserID()
   * @generated
   * @ordered
   */
  protected String userID = USER_ID_EDEFAULT;

  /**
   * The default value of the '{@link #getPassword() <em>Password</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPassword()
   * @generated
   * @ordered
   */
  protected static final String PASSWORD_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPassword() <em>Password</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPassword()
   * @generated
   * @ordered
   */
  protected String password = PASSWORD_EDEFAULT;

  /**
   * The cached value of the '{@link #getQueries() <em>Queries</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQueries()
   * @generated
   * @ordered
   */
  protected EList<Query> queries;

  private TaskRepository repository;

  private Map<Query, RepositoryQuery> repositoryQueries = new HashMap<Query, RepositoryQuery>();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MylynQueriesTaskImpl()
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
    return MylynPackage.Literals.MYLYN_QUERIES_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getConnectorKind()
  {
    return connectorKind;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setConnectorKind(String newConnectorKind)
  {
    String oldConnectorKind = connectorKind;
    connectorKind = newConnectorKind;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MylynPackage.MYLYN_QUERIES_TASK__CONNECTOR_KIND, oldConnectorKind, connectorKind));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRepositoryURL()
  {
    return repositoryURL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRepositoryURL(String newRepositoryURL)
  {
    String oldRepositoryURL = repositoryURL;
    repositoryURL = newRepositoryURL;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MylynPackage.MYLYN_QUERIES_TASK__REPOSITORY_URL, oldRepositoryURL, repositoryURL));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Query> getQueries()
  {
    if (queries == null)
    {
      queries = new EObjectContainmentWithInverseEList<Query>(Query.class, this, MylynPackage.MYLYN_QUERIES_TASK__QUERIES, MylynPackage.QUERY__TASK);
    }
    return queries;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUserID()
  {
    return userID;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUserID(String newUserID)
  {
    String oldUserID = userID;
    userID = newUserID;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MylynPackage.MYLYN_QUERIES_TASK__USER_ID, oldUserID, userID));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPassword(String newPassword)
  {
    String oldPassword = password;
    password = newPassword;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MylynPackage.MYLYN_QUERIES_TASK__PASSWORD, oldPassword, password));
    }
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
      case MylynPackage.MYLYN_QUERIES_TASK__QUERIES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getQueries()).basicAdd(otherEnd, msgs);
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
      case MylynPackage.MYLYN_QUERIES_TASK__QUERIES:
        return ((InternalEList<?>)getQueries()).basicRemove(otherEnd, msgs);
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
      case MylynPackage.MYLYN_QUERIES_TASK__CONNECTOR_KIND:
        return getConnectorKind();
      case MylynPackage.MYLYN_QUERIES_TASK__REPOSITORY_URL:
        return getRepositoryURL();
      case MylynPackage.MYLYN_QUERIES_TASK__USER_ID:
        return getUserID();
      case MylynPackage.MYLYN_QUERIES_TASK__PASSWORD:
        return getPassword();
      case MylynPackage.MYLYN_QUERIES_TASK__QUERIES:
        return getQueries();
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
      case MylynPackage.MYLYN_QUERIES_TASK__CONNECTOR_KIND:
        setConnectorKind((String)newValue);
        return;
      case MylynPackage.MYLYN_QUERIES_TASK__REPOSITORY_URL:
        setRepositoryURL((String)newValue);
        return;
      case MylynPackage.MYLYN_QUERIES_TASK__USER_ID:
        setUserID((String)newValue);
        return;
      case MylynPackage.MYLYN_QUERIES_TASK__PASSWORD:
        setPassword((String)newValue);
        return;
      case MylynPackage.MYLYN_QUERIES_TASK__QUERIES:
        getQueries().clear();
        getQueries().addAll((Collection<? extends Query>)newValue);
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
      case MylynPackage.MYLYN_QUERIES_TASK__CONNECTOR_KIND:
        setConnectorKind(CONNECTOR_KIND_EDEFAULT);
        return;
      case MylynPackage.MYLYN_QUERIES_TASK__REPOSITORY_URL:
        setRepositoryURL(REPOSITORY_URL_EDEFAULT);
        return;
      case MylynPackage.MYLYN_QUERIES_TASK__USER_ID:
        setUserID(USER_ID_EDEFAULT);
        return;
      case MylynPackage.MYLYN_QUERIES_TASK__PASSWORD:
        setPassword(PASSWORD_EDEFAULT);
        return;
      case MylynPackage.MYLYN_QUERIES_TASK__QUERIES:
        getQueries().clear();
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
      case MylynPackage.MYLYN_QUERIES_TASK__CONNECTOR_KIND:
        return CONNECTOR_KIND_EDEFAULT == null ? connectorKind != null : !CONNECTOR_KIND_EDEFAULT.equals(connectorKind);
      case MylynPackage.MYLYN_QUERIES_TASK__REPOSITORY_URL:
        return REPOSITORY_URL_EDEFAULT == null ? repositoryURL != null : !REPOSITORY_URL_EDEFAULT.equals(repositoryURL);
      case MylynPackage.MYLYN_QUERIES_TASK__USER_ID:
        return USER_ID_EDEFAULT == null ? userID != null : !USER_ID_EDEFAULT.equals(userID);
      case MylynPackage.MYLYN_QUERIES_TASK__PASSWORD:
        return PASSWORD_EDEFAULT == null ? password != null : !PASSWORD_EDEFAULT.equals(password);
      case MylynPackage.MYLYN_QUERIES_TASK__QUERIES:
        return queries != null && !queries.isEmpty();
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
    result.append(" (connectorKind: ");
    result.append(connectorKind);
    result.append(", repositoryURL: ");
    result.append(repositoryURL);
    result.append(", userID: ");
    result.append(userID);
    result.append(", password: ");
    result.append(password);
    result.append(')');
    return result.toString();
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    String connectorKind = getConnectorKind();
    String repositoryURL = getRepositoryURL();
    repository = TasksUi.getRepositoryManager().getRepository(connectorKind, repositoryURL);

    EList<Query> queries = getQueries();
    for (Query query : queries)
    {
      context.checkCancelation();

      RepositoryQuery repositoryQuery = getRepositoryQuery(query);
      if (repositoryQuery == null || isQueryDifferent(query, repositoryQuery))
      {
        repositoryQueries.put(query, repositoryQuery);
      }
    }

    return repository == null || !repositoryQueries.isEmpty();
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    String connectorKind = getConnectorKind();
    String repositoryURL = getRepositoryURL();

    if (repository == null)
    {
      context.log("Adding " + connectorKind + " repository: " + repositoryURL);
      repository = new TaskRepository(connectorKind, repositoryURL);
      repository.setCredentials(AuthenticationType.PROXY, null, true);
    }

    String userID = getUserID();
    if (isAuthenticate())
    {
      AuthenticationCredentials credentials = new AuthenticationCredentials(userID, PreferencesUtil.decrypt(getPassword()));
      repository.setCredentials(AuthenticationType.REPOSITORY, credentials, true);
    }

    TasksUi.getRepositoryManager().addRepository(repository);

    if (!repositoryQueries.isEmpty())
    {
      for (Map.Entry<Query, RepositoryQuery> entry : repositoryQueries.entrySet())
      {
        Query query = entry.getKey();
        RepositoryQuery repositoryQuery = entry.getValue();

        String summary = query.getSummary();

        if (repositoryQuery == null)
        {
          context.log("Adding " + connectorKind + " query: " + summary);
          String handle = TasksUiPlugin.getTaskList().getUniqueHandleIdentifier();
          repositoryQuery = new RepositoryQuery(connectorKind, handle);
          repositoryQuery.setSummary(summary);
          entry.setValue(repositoryQuery);

          repositoryQuery.setRepositoryUrl(repositoryURL);
          configureQuery(context, query, repositoryQuery);

          TasksUiPlugin.getTaskList().addQuery(repositoryQuery);
        }
        else
        {
          context.log("Changing " + connectorKind + " query: " + summary);

          repositoryQuery.setRepositoryUrl(repositoryURL);
          configureQuery(context, query, repositoryQuery);
        }
      }

      Set<RepositoryQuery> queries = new HashSet<RepositoryQuery>(repositoryQueries.values());
      TasksUiPlugin.getTaskList().notifyElementsChanged(queries);

      AbstractRepositoryConnector connector = TasksUi.getRepositoryManager().getRepositoryConnector(connectorKind);
      TasksUiInternal.synchronizeQueries(connector, repository, queries, null, true);
    }
  }

  private RepositoryQuery getRepositoryQuery(Query query) throws Exception
  {
    for (RepositoryQuery repositoryQuery : TasksUiPlugin.getTaskList().getQueries())
    {
      if (ObjectUtil.equals(repositoryQuery.getSummary(), query.getSummary()))
      {
        return repositoryQuery;
      }
    }

    return null;
  }

  private boolean isQueryDifferent(Query query, RepositoryQuery repositoryQuery) throws Exception
  {
    String url = StringUtil.safe(query.getURL());
    if (!ObjectUtil.equals(repositoryQuery.getUrl(), url))
    {
      return true;
    }

    Map<String, String> attributes = query.getAttributes().map();
    if (!ObjectUtil.equals(repositoryQuery.getAttributes(), attributes))
    {
      return true;
    }

    return false;
  }

  private void configureQuery(SetupTaskContext context, Query query, RepositoryQuery repositoryQuery)
  {
    String url = StringUtil.safe(query.getURL());
    if (!ObjectUtil.equals(url, repositoryQuery.getUrl()))
    {
      context.log("Setting query URL = " + url);
      repositoryQuery.setUrl(url);
    }

    Map<String, String> repositoryAttributes = repositoryQuery.getAttributes();
    Map<String, String> attributes = query.getAttributes().map();

    for (Entry<String, String> entry : attributes.entrySet())
    {
      String key = entry.getKey();
      String value = entry.getValue();

      String repositoryValue = repositoryAttributes.get(key);
      if (!ObjectUtil.equals(value, repositoryValue))
      {
        context.log("Setting query attribute " + key + " = " + value);
        repositoryQuery.setAttribute(key, value);
      }
    }

    for (String key : new ArrayList<String>(repositoryAttributes.keySet()))
    {
      if (!attributes.containsKey(key))
      {
        context.log("Removing query attribute " + key);
        repositoryQuery.setAttribute(key, null);
      }
    }
  }

  private boolean isAuthenticate()
  {
    return !StringUtil.isEmpty(userID) && !"anonymous".equals(userID) && !StringUtil.isEmpty(password) && !" ".equals(password);
  }
} // MylynQueriesTaskImpl
