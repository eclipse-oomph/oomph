/*
 * Copyright (c) 2026 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.targlets.impl;

import org.eclipse.oomph.base.util.BaseResource;
import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseResourceImpl;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.setup.internal.targlets.SetupTargletsPlugin;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;
import org.eclipse.emf.ecore.xmi.impl.XMILoadImpl;
import org.eclipse.emf.ecore.xml.type.AnyType;

import org.eclipse.equinox.p2.metadata.VersionRange;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Ed Merks
 */
public class TargetResourceFactoryImpl extends BaseResourceFactoryImpl
{

  @Override
  public Resource createResource(URI uri)
  {
    return super.createResource(uri);
  }

  @Override
  protected BaseResource basicCreateResource(URI uri)
  {
    return new TargetResourceImpl(uri);
  }

  private static class TargetResourceImpl extends BaseResourceImpl
  {
    public TargetResourceImpl(URI uri)
    {
      super(uri);
    }

    @Override
    protected XMLLoad createXMLLoad()
    {
      return new XMILoadImpl(createXMLHelper())
      {
        @Override
        protected DefaultHandler makeDefaultHandler()
        {
          return new SAXXMIHandler(resource, helper, options)
          {
            {
              deferredExtent = new ArrayList<EObject>();
            }

            @SuppressWarnings("nls")
            @Override
            public void endDocument()
            {
              try
              {
                if (deferredExtent.size() == 1)
                {
                  Set<String> repositoryLocations = new TreeSet<>();
                  Set<Requirement> requirements = new TreeSet<>(Requirement.COMPARATOR);

                  for (Iterator<EObject> eAllContents = deferredExtent.get(0).eAllContents(); eAllContents.hasNext();)
                  {
                    EObject content = eAllContents.next();
                    switch (content.eContainmentFeature().getName())
                    {
                      case "repository":
                      {
                        String location = get(content, "location");
                        if (location != null)
                        {
                          repositoryLocations.add(location);
                        }

                        break;
                      }
                      case "unit":
                      {
                        String id = get(content, "id");
                        if (id != null)
                        {
                          Requirement requirement = P2Factory.eINSTANCE.createRequirement();
                          requirement.setName(id);
                          String version = get(content, "version");
                          requirement.setVersionRange(version == null ? VersionRange.emptyRange : VersionRange.create(version));
                          requirements.add(requirement);
                        }

                        break;
                      }
                    }
                  }

                  Targlet targlet = TargletFactory.eINSTANCE.createTarglet();
                  RepositoryList repositoryList = P2Factory.eINSTANCE.createRepositoryList();
                  targlet.getRepositoryLists().add(repositoryList);

                  EList<Repository> repositories = repositoryList.getRepositories();
                  for (String location : repositoryLocations)
                  {
                    repositories.add(P2Factory.eINSTANCE.createRepository(location));
                  }
                  targlet.getRequirements().addAll(requirements);

                  deferredExtent.add(targlet);

                  if (defaultSaveOptions.size() > 1)
                  {
                    deferredExtent.remove(0);
                  }
                }
              }
              catch (RuntimeException ex)
              {
                SetupTargletsPlugin.INSTANCE.log(ex);
              }

              super.endDocument();
            }

            private <T> T get(EObject eObject, String feature)
            {
              AnyType anyType = (AnyType)eObject;
              FeatureMap anyAttribute = anyType.getAnyAttribute();
              for (FeatureMap.Entry entry : anyAttribute)
              {
                if (entry.getEStructuralFeature().getName().equals(feature))
                {
                  @SuppressWarnings("unchecked")
                  T value = (T)entry.getValue();
                  return value;
                }
              }

              List<Object> values = new ArrayList<Object>();
              for (FeatureMap.Entry entry : anyType.getMixed())
              {
                if (entry.getEStructuralFeature().getName().equals(feature))
                {
                  values.add(entry.getValue());
                }
              }

              if (values.isEmpty())
              {
                return null;
              }

              @SuppressWarnings("unchecked")
              T result = (T)values;
              return result;
            }
          };
        }
      };
    }
  }
}
