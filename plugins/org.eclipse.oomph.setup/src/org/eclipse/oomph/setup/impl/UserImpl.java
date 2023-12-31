/*
 * Copyright (c) 2014, 2015, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.CertificateInfo;
import org.eclipse.oomph.setup.CertificatePolicy;
import org.eclipse.oomph.setup.LicenseInfo;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.UnsignedPolicy;
import org.eclipse.oomph.setup.User;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;
import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Preferences</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.UserImpl#getAttributeRules <em>Attribute Rules</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.UserImpl#getAcceptedLicenses <em>Accepted Licenses</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.UserImpl#getAcceptedCertificates <em>Accepted Certificates</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.UserImpl#getUnsignedPolicy <em>Unsigned Policy</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.UserImpl#getCertificatePolicy <em>Certificate Policy</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.UserImpl#getQuestionnaireDate <em>Questionnaire Date</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.UserImpl#isPreferenceRecorderDefault <em>Preference Recorder Default</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UserImpl extends ScopeImpl implements User
{
  /**
   * The cached value of the '{@link #getAttributeRules() <em>Attribute Rules</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAttributeRules()
   * @generated
   * @ordered
   */
  protected EList<AttributeRule> attributeRules;

  /**
   * The cached value of the '{@link #getAcceptedLicenses() <em>Accepted Licenses</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAcceptedLicenses()
   * @generated
   * @ordered
   */
  protected EList<LicenseInfo> acceptedLicenses;

  /**
   * The cached value of the '{@link #getAcceptedCertificates() <em>Accepted Certificates</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAcceptedCertificates()
   * @generated
   * @ordered
   */
  protected EList<CertificateInfo> acceptedCertificates;

  /**
   * The default value of the '{@link #getUnsignedPolicy() <em>Unsigned Policy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnsignedPolicy()
   * @generated
   * @ordered
   */
  protected static final UnsignedPolicy UNSIGNED_POLICY_EDEFAULT = UnsignedPolicy.PROMPT;

  /**
   * The cached value of the '{@link #getUnsignedPolicy() <em>Unsigned Policy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnsignedPolicy()
   * @generated
   * @ordered
   */
  protected UnsignedPolicy unsignedPolicy = UNSIGNED_POLICY_EDEFAULT;

  /**
   * The default value of the '{@link #getCertificatePolicy() <em>Certificate Policy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCertificatePolicy()
   * @generated
   * @ordered
   */
  protected static final CertificatePolicy CERTIFICATE_POLICY_EDEFAULT = CertificatePolicy.PROMPT;

  /**
   * The cached value of the '{@link #getCertificatePolicy() <em>Certificate Policy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCertificatePolicy()
   * @generated
   * @ordered
   */
  protected CertificatePolicy certificatePolicy = CERTIFICATE_POLICY_EDEFAULT;

  /**
   * The default value of the '{@link #getQuestionnaireDate() <em>Questionnaire Date</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQuestionnaireDate()
   * @generated
   * @ordered
   */
  protected static final Date QUESTIONNAIRE_DATE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getQuestionnaireDate() <em>Questionnaire Date</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQuestionnaireDate()
   * @generated
   * @ordered
   */
  protected Date questionnaireDate = QUESTIONNAIRE_DATE_EDEFAULT;

  /**
   * The default value of the '{@link #isPreferenceRecorderDefault() <em>Preference Recorder Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPreferenceRecorderDefault()
   * @generated
   * @ordered
   */
  protected static final boolean PREFERENCE_RECORDER_DEFAULT_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isPreferenceRecorderDefault() <em>Preference Recorder Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPreferenceRecorderDefault()
   * @generated
   * @ordered
   */
  protected boolean preferenceRecorderDefault = PREFERENCE_RECORDER_DEFAULT_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected UserImpl()
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
    return SetupPackage.Literals.USER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<LicenseInfo> getAcceptedLicenses()
  {
    if (acceptedLicenses == null)
    {
      acceptedLicenses = new EDataTypeUniqueEList<>(LicenseInfo.class, this, SetupPackage.USER__ACCEPTED_LICENSES);
    }
    return acceptedLicenses;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<CertificateInfo> getAcceptedCertificates()
  {
    if (acceptedCertificates == null)
    {
      acceptedCertificates = new EDataTypeUniqueEList<>(CertificateInfo.class, this, SetupPackage.USER__ACCEPTED_CERTIFICATES);
    }
    return acceptedCertificates;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public UnsignedPolicy getUnsignedPolicy()
  {
    return unsignedPolicy;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setUnsignedPolicy(UnsignedPolicy newUnsignedPolicy)
  {
    UnsignedPolicy oldUnsignedPolicy = unsignedPolicy;
    unsignedPolicy = newUnsignedPolicy == null ? UNSIGNED_POLICY_EDEFAULT : newUnsignedPolicy;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.USER__UNSIGNED_POLICY, oldUnsignedPolicy, unsignedPolicy));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CertificatePolicy getCertificatePolicy()
  {
    return certificatePolicy;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setCertificatePolicy(CertificatePolicy newCertificatePolicy)
  {
    CertificatePolicy oldCertificatePolicy = certificatePolicy;
    certificatePolicy = newCertificatePolicy == null ? CERTIFICATE_POLICY_EDEFAULT : newCertificatePolicy;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.USER__CERTIFICATE_POLICY, oldCertificatePolicy, certificatePolicy));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Date getQuestionnaireDate()
  {
    return questionnaireDate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setQuestionnaireDate(Date newQuestionnaireDate)
  {
    Date oldQuestionnaireDate = questionnaireDate;
    questionnaireDate = newQuestionnaireDate;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.USER__QUESTIONNAIRE_DATE, oldQuestionnaireDate, questionnaireDate));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isPreferenceRecorderDefault()
  {
    return preferenceRecorderDefault;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setPreferenceRecorderDefault(boolean newPreferenceRecorderDefault)
  {
    boolean oldPreferenceRecorderDefault = preferenceRecorderDefault;
    preferenceRecorderDefault = newPreferenceRecorderDefault;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.USER__PREFERENCE_RECORDER_DEFAULT, oldPreferenceRecorderDefault,
          preferenceRecorderDefault));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<AttributeRule> getAttributeRules()
  {
    if (attributeRules == null)
    {
      attributeRules = new EObjectContainmentEList.Resolving<>(AttributeRule.class, this, SetupPackage.USER__ATTRIBUTE_RULES);
    }
    return attributeRules;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public ScopeType getType()
  {
    return ScopeType.USER;
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
      case SetupPackage.USER__ATTRIBUTE_RULES:
        return ((InternalEList<?>)getAttributeRules()).basicRemove(otherEnd, msgs);
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
      case SetupPackage.USER__ATTRIBUTE_RULES:
        return getAttributeRules();
      case SetupPackage.USER__ACCEPTED_LICENSES:
        return getAcceptedLicenses();
      case SetupPackage.USER__ACCEPTED_CERTIFICATES:
        return getAcceptedCertificates();
      case SetupPackage.USER__UNSIGNED_POLICY:
        return getUnsignedPolicy();
      case SetupPackage.USER__CERTIFICATE_POLICY:
        return getCertificatePolicy();
      case SetupPackage.USER__QUESTIONNAIRE_DATE:
        return getQuestionnaireDate();
      case SetupPackage.USER__PREFERENCE_RECORDER_DEFAULT:
        return isPreferenceRecorderDefault();
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
      case SetupPackage.USER__ATTRIBUTE_RULES:
        getAttributeRules().clear();
        getAttributeRules().addAll((Collection<? extends AttributeRule>)newValue);
        return;
      case SetupPackage.USER__ACCEPTED_LICENSES:
        getAcceptedLicenses().clear();
        getAcceptedLicenses().addAll((Collection<? extends LicenseInfo>)newValue);
        return;
      case SetupPackage.USER__ACCEPTED_CERTIFICATES:
        getAcceptedCertificates().clear();
        getAcceptedCertificates().addAll((Collection<? extends CertificateInfo>)newValue);
        return;
      case SetupPackage.USER__UNSIGNED_POLICY:
        setUnsignedPolicy((UnsignedPolicy)newValue);
        return;
      case SetupPackage.USER__CERTIFICATE_POLICY:
        setCertificatePolicy((CertificatePolicy)newValue);
        return;
      case SetupPackage.USER__QUESTIONNAIRE_DATE:
        setQuestionnaireDate((Date)newValue);
        return;
      case SetupPackage.USER__PREFERENCE_RECORDER_DEFAULT:
        setPreferenceRecorderDefault((Boolean)newValue);
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
      case SetupPackage.USER__ATTRIBUTE_RULES:
        getAttributeRules().clear();
        return;
      case SetupPackage.USER__ACCEPTED_LICENSES:
        getAcceptedLicenses().clear();
        return;
      case SetupPackage.USER__ACCEPTED_CERTIFICATES:
        getAcceptedCertificates().clear();
        return;
      case SetupPackage.USER__UNSIGNED_POLICY:
        setUnsignedPolicy(UNSIGNED_POLICY_EDEFAULT);
        return;
      case SetupPackage.USER__CERTIFICATE_POLICY:
        setCertificatePolicy(CERTIFICATE_POLICY_EDEFAULT);
        return;
      case SetupPackage.USER__QUESTIONNAIRE_DATE:
        setQuestionnaireDate(QUESTIONNAIRE_DATE_EDEFAULT);
        return;
      case SetupPackage.USER__PREFERENCE_RECORDER_DEFAULT:
        setPreferenceRecorderDefault(PREFERENCE_RECORDER_DEFAULT_EDEFAULT);
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
      case SetupPackage.USER__ATTRIBUTE_RULES:
        return attributeRules != null && !attributeRules.isEmpty();
      case SetupPackage.USER__ACCEPTED_LICENSES:
        return acceptedLicenses != null && !acceptedLicenses.isEmpty();
      case SetupPackage.USER__ACCEPTED_CERTIFICATES:
        return acceptedCertificates != null && !acceptedCertificates.isEmpty();
      case SetupPackage.USER__UNSIGNED_POLICY:
        return unsignedPolicy != UNSIGNED_POLICY_EDEFAULT;
      case SetupPackage.USER__CERTIFICATE_POLICY:
        return certificatePolicy != CERTIFICATE_POLICY_EDEFAULT;
      case SetupPackage.USER__QUESTIONNAIRE_DATE:
        return QUESTIONNAIRE_DATE_EDEFAULT == null ? questionnaireDate != null : !QUESTIONNAIRE_DATE_EDEFAULT.equals(questionnaireDate);
      case SetupPackage.USER__PREFERENCE_RECORDER_DEFAULT:
        return preferenceRecorderDefault != PREFERENCE_RECORDER_DEFAULT_EDEFAULT;
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
    result.append(" (acceptedLicenses: "); //$NON-NLS-1$
    result.append(acceptedLicenses);
    result.append(", acceptedCertificates: "); //$NON-NLS-1$
    result.append(acceptedCertificates);
    result.append(", unsignedPolicy: "); //$NON-NLS-1$
    result.append(unsignedPolicy);
    result.append(", certificatePolicy: "); //$NON-NLS-1$
    result.append(certificatePolicy);
    result.append(", questionnaireDate: "); //$NON-NLS-1$
    result.append(questionnaireDate);
    result.append(", preferenceRecorderDefault: "); //$NON-NLS-1$
    result.append(preferenceRecorderDefault);
    result.append(')');
    return result.toString();
  }

} // PreferencesImpl
