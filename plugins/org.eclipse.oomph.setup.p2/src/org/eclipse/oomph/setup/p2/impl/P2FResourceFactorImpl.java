/*
 * Copyright (c) 2026 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.p2.impl;

import org.eclipse.oomph.base.util.BaseResource;
import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseResourceImpl;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.setup.internal.p2.SetupP2Plugin;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;
import org.eclipse.emf.ecore.xmi.impl.XMILoadImpl;
import org.eclipse.emf.ecore.xml.type.AnyType;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Ed Merks
 */
public class P2FResourceFactorImpl extends BaseResourceFactoryImpl
{

  @Override
  public Resource createResource(URI uri)
  {
    return super.createResource(uri);
  }

  @Override
  protected BaseResource basicCreateResource(URI uri)
  {
    return new P2FResourceImpl(uri);
  }

  private static class P2FResourceImpl extends BaseResourceImpl
  {
    public P2FResourceImpl(URI uri)
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
                  EObject eObject = deferredExtent.get(0);
                  List<AnyType> ius = get(eObject, "ius", "iu");
                  Map<java.net.URI, Set<IInstallableUnit>> locationsToIUs = new TreeMap<>();
                  for (AnyType iu : ius)
                  {
                    String id = get(iu, "id");
                    String version = get(iu, "version");
                    InstallableUnitDescription installableUnitDescription = new InstallableUnitDescription();
                    installableUnitDescription.setId(id);
                    installableUnitDescription.setVersion(Version.create(version));
                    IInstallableUnit unit = MetadataFactory.createInstallableUnit(installableUnitDescription);

                    List<AnyType> repositories = get(iu, "repositories", "repository");
                    for (AnyType repository : repositories)
                    {
                      var location = java.net.URI.create(((String)get(repository, "location")).replaceAll("/$", ""));
                      locationsToIUs.computeIfAbsent(location, it -> new TreeSet<>()).add(unit);
                    }
                  }

                  deferredExtent.add(
                      create(new TreeSet<>(locationsToIUs.values().stream().flatMap(Collection::stream).collect(Collectors.toSet())), locationsToIUs.keySet()));
                  for (var entry : locationsToIUs.entrySet())
                  {
                    deferredExtent.add(create(entry.getValue(), Set.of(entry.getKey())));
                  }

                  if (defaultSaveOptions.size() > 1)
                  {
                    deferredExtent.remove(0);
                  }
                }
              }
              catch (RuntimeException ex)
              {
                SetupP2Plugin.INSTANCE.log(ex);
              }

              super.endDocument();
            }

            private P2Task create(Set<IInstallableUnit> ius, Set<java.net.URI> locations)
            {
              P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
              EList<Repository> repositories2 = p2Task.getRepositories();
              for (var location : locations)
              {
                repositories2.add(P2Factory.eINSTANCE.createRepository(location.toString()));
              }

              EList<Requirement> requirements = p2Task.getRequirements();
              for (var iu : ius)
              {
                Requirement requirement = P2Factory.eINSTANCE.createRequirement(iu.getId(), iu.getVersion());
                requirements.add(requirement);
              }

              return p2Task;
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

            private List<AnyType> get(EObject eObject, String feature, String childFeature)
            {
              List<AnyType> result = new ArrayList<AnyType>();

              List<AnyType> values = get(eObject, feature);
              if (values != null)
              {
                for (AnyType anyType : values)
                {
                  List<AnyType> childValues = get(anyType, childFeature);
                  result.addAll(childValues);
                }
              }

              return result;
            }
          };
        }
      };
    }
  }
}
