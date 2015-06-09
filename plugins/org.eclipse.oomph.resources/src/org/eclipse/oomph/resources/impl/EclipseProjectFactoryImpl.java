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
import org.eclipse.oomph.resources.EclipseProjectFactory;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.emf.ecore.EClass;

import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eclipse Project Factory</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class EclipseProjectFactoryImpl extends XMLProjectFactoryImpl implements EclipseProjectFactory
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EclipseProjectFactoryImpl()
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
    return ResourcesPackage.Literals.ECLIPSE_PROJECT_FACTORY;
  }

  @Override
  protected String getXMLFileName()
  {
    return ".project";
  }

  @Override
  protected void fillDescription(final Description description, Element rootElement) throws Exception
  {
    XMLUtil.handleChildElements(rootElement, new XMLUtil.ElementHandler()
    {
      public void handleElement(Element element) throws Exception
      {
        if ("name".equals(element.getTagName()))
        {
          String name = element.getTextContent().trim();
          description.internalSetName(name);
        }
        else if ("comment".equals(element.getTagName()))
        {
          String comment = element.getTextContent().trim();
          description.internalSetComment(comment);
        }
        else if ("buildSpec".equals(element.getTagName()))
        {
          XMLUtil.handleChildElements(element, new XMLUtil.ElementHandler()
          {
            public void handleElement(Element buildCommandElement) throws Exception
            {
              XMLUtil.handleChildElements(buildCommandElement, new XMLUtil.ElementHandler()
              {
                public void handleElement(Element nameElement) throws Exception
                {
                  String builderName = nameElement.getTextContent().trim();
                  description.internalAddCommand(builderName);
                }
              });
            }
          });
        }
        else if ("natures".equals(element.getTagName()))
        {
          XMLUtil.handleChildElements(element, new XMLUtil.ElementHandler()
          {
            public void handleElement(Element natureElement) throws Exception
            {
              String natureID = natureElement.getTextContent().trim();
              description.internalAddNatureID(natureID);
            }
          });
        }
      }
    });
  }

} // EclipseProjectFactoryImpl
