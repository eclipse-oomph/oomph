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
package org.eclipse.oomph.resources.impl;

import org.eclipse.oomph.internal.resources.ExternalProject.Description;
import org.eclipse.oomph.resources.MavenProjectFactory;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.emf.ecore.EClass;

import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Maven Project Factory</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class MavenProjectFactoryImpl extends XMLProjectFactoryImpl implements MavenProjectFactory
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MavenProjectFactoryImpl()
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
    return ResourcesPackage.Literals.MAVEN_PROJECT_FACTORY;
  }

  @Override
  protected String getXMLFileName()
  {
    return "pom.xml";
  }

  @Override
  protected void fillDescription(final Description description, Element rootElement) throws Exception
  {
    XMLUtil.handleChildElements(rootElement, new XMLUtil.ElementHandler()
    {
      public void handleElement(Element element) throws Exception
      {
        if ("artifactId".equals(element.getTagName()))
        {
          String name = element.getTextContent().trim();
          description.internalSetName(name);
        }
        else if ("name".equals(element.getTagName()))
        {
          String comment = element.getTextContent().trim();
          description.internalSetComment(comment);
        }
      }
    });
  }

} // MavenProjectFactoryImpl
