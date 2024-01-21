/**
 */
package org.eclipse.oomph.setup.git.impl;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.git.ConfigSection;
import org.eclipse.oomph.setup.git.GitConfigurationTask;
import org.eclipse.oomph.setup.git.GitPackage;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configuration Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitConfigurationTaskImpl#getRemoteURIPattern <em>Remote URI Pattern</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.git.impl.GitConfigurationTaskImpl#getConfigSections <em>Config Sections</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GitConfigurationTaskImpl extends SetupTaskImpl implements GitConfigurationTask
{
  /**
   * The default value of the '{@link #getRemoteURIPattern() <em>Remote URI Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteURIPattern()
   * @generated
   * @ordered
   */
  protected static final String REMOTE_URI_PATTERN_EDEFAULT = ""; //$NON-NLS-1$

  /**
   * The cached value of the '{@link #getRemoteURIPattern() <em>Remote URI Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRemoteURIPattern()
   * @generated
   * @ordered
   */
  protected String remoteURIPattern = REMOTE_URI_PATTERN_EDEFAULT;

  /**
   * The cached value of the '{@link #getConfigSections() <em>Config Sections</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConfigSections()
   * @generated
   * @ordered
   */
  protected EList<ConfigSection> configSections;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GitConfigurationTaskImpl()
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
    return GitPackage.Literals.GIT_CONFIGURATION_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getRemoteURIPattern()
  {
    return remoteURIPattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setRemoteURIPattern(String newRemoteURIPattern)
  {
    String oldRemoteURIPattern = remoteURIPattern;
    remoteURIPattern = newRemoteURIPattern;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, GitPackage.GIT_CONFIGURATION_TASK__REMOTE_URI_PATTERN, oldRemoteURIPattern, remoteURIPattern));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ConfigSection> getConfigSections()
  {
    if (configSections == null)
    {
      configSections = new EObjectContainmentEList<>(ConfigSection.class, this, GitPackage.GIT_CONFIGURATION_TASK__CONFIG_SECTIONS);
    }
    return configSections;
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
      case GitPackage.GIT_CONFIGURATION_TASK__CONFIG_SECTIONS:
        return ((InternalEList<?>)getConfigSections()).basicRemove(otherEnd, msgs);
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
      case GitPackage.GIT_CONFIGURATION_TASK__REMOTE_URI_PATTERN:
        return getRemoteURIPattern();
      case GitPackage.GIT_CONFIGURATION_TASK__CONFIG_SECTIONS:
        return getConfigSections();
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
      case GitPackage.GIT_CONFIGURATION_TASK__REMOTE_URI_PATTERN:
        setRemoteURIPattern((String)newValue);
        return;
      case GitPackage.GIT_CONFIGURATION_TASK__CONFIG_SECTIONS:
        getConfigSections().clear();
        getConfigSections().addAll((Collection<? extends ConfigSection>)newValue);
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
      case GitPackage.GIT_CONFIGURATION_TASK__REMOTE_URI_PATTERN:
        setRemoteURIPattern(REMOTE_URI_PATTERN_EDEFAULT);
        return;
      case GitPackage.GIT_CONFIGURATION_TASK__CONFIG_SECTIONS:
        getConfigSections().clear();
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
  @SuppressWarnings("null")
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case GitPackage.GIT_CONFIGURATION_TASK__REMOTE_URI_PATTERN:
        return REMOTE_URI_PATTERN_EDEFAULT == null ? remoteURIPattern != null : !REMOTE_URI_PATTERN_EDEFAULT.equals(remoteURIPattern);
      case GitPackage.GIT_CONFIGURATION_TASK__CONFIG_SECTIONS:
        return configSections != null && !configSections.isEmpty();
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

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (remoteURIPattern: "); //$NON-NLS-1$
    result.append(remoteURIPattern);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getPriority()
  {
    return PRIORITY_EARLY;
  }

  @Override
  public Object getOverrideToken()
  {
    return super.getOverrideToken();
  }

  @Override
  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    String remoteURIPattern = getRemoteURIPattern();
    if (!StringUtil.isEmpty(remoteURIPattern))
    {
      try
      {
        Pattern.compile(remoteURIPattern);
        getGitConfigurationTasks(context).add(this);
      }
      catch (RuntimeException ex)
      {
        //$FALL-THROUGH$
      }
    }

    return false;
  }

  @Override
  public void perform(SetupTaskContext context) throws Exception
  {
  }

  private static final String GIT_CONFIGURATION_TASKS = "oomph.setup.git.configuration.tasks"; //$NON-NLS-1$

  static List<GitConfigurationTask> getGitConfigurationTasks(SetupTaskContext context)
  {
    @SuppressWarnings("unchecked")
    List<GitConfigurationTask> result = (List<GitConfigurationTask>)context.get(GIT_CONFIGURATION_TASKS);
    if (result == null)
    {
      result = new ArrayList<>();
      context.put(GIT_CONFIGURATION_TASKS, result);
    }

    return result;
  }

} // GitConfigurationTaskImpl
