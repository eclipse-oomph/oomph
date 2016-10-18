/*
 * Copyright (c) 2016 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.base.util;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Ed Merks
 */
public class ArchiveResourceImpl extends ResourceImpl implements BytesResource
{
  private final URIConverter uriConverter;

  public ArchiveResourceImpl(URI uri, URIConverter uriConverter)
  {
    super(uri);
    this.uriConverter = uriConverter;
  }

  @Override
  protected URIConverter getURIConverter()
  {
    return uriConverter != null ? uriConverter : super.getURIConverter();
  }

  @Override
  protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException
  {
    URI archiveURI = getURI();
    ZipInputStream zipInputStream = null;

    zipInputStream = new ZipInputStream(inputStream);
    Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
    EMap<String, String> details = annotation.getDetails();
    for (ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry())
    {
      String name = zipEntry.getName();
      URI path = URI.createURI(name);
      int segmentCount = path.segmentCount();
      if (segmentCount > 2)
      {
        URI uri = URI.createURI(path.segment(0) + "://" + path.segment(1));
        for (int i = 2, length = path.segmentCount(); i < length; ++i)
        {
          uri = uri.appendSegment(path.segment(i));
        }

        URI archiveEntry = URI.createURI("archive:" + archiveURI + "!/" + path);
        handle(details, uri, archiveEntry);
      }
    }

    getContents().add(annotation);
  }

  protected void handle(EMap<String, String> details, URI uri, URI archiveEntry)
  {
    details.put(uri.toString(), archiveEntry.toString());
  }
}
