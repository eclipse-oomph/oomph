/*
 * Copyright (c) 2019 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.p2.util;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ed Merks
 */
public class MarketPlaceListing
{
  private static final String MARKET_PLACE_ANNOTATION = "https://marketplace.eclipse.org"; //$NON-NLS-1$

  private static final Pattern MARKET_PLACE_CONTENT_PATTERN = Pattern.compile("(https?://marketplace\\.eclipse\\.org/content/[^/?#]*+)"); //$NON-NLS-1$

  private static final Pattern MARKET_PLACE_INSTALL_PATTERN = Pattern
      .compile("https?://marketplace\\.eclipse\\.org/marketplace-client-intro\\?mpc_install=([0-9]+)"); //$NON-NLS-1$

  private static final Pattern IU_PATTERN = Pattern.compile(" *<iu( ?[^>]*)>([^<]+)</iu>"); //$NON-NLS-1$

  private static final Pattern UPDATE_URL_PATTERN = Pattern.compile(" *<updateurl>([^<]+)</updateurl>"); //$NON-NLS-1$

  private static final Pattern NODE_PATTERN = Pattern.compile(" *<node.*name=\"([^\"]*)\".*url=\"([^\"]*)\".*>"); //$NON-NLS-1$

  private static final Map<URI, MarketPlaceListing> MARKET_PLACE_LISTINGS = Collections.synchronizedMap(new WeakHashMap<URI, MarketPlaceListing>());

  private final String label;

  private final URI updateSite;

  private final URI listing;

  private final IOException exception;

  private final List<Requirement> requirements = new ArrayList<>();

  private MarketPlaceListing(URI uri, URIConverter uriConverter)
  {
    URI updateSite = null;
    URI listing = null;
    String label = uri.toString();
    InputStream inputStream = null;
    IOException exception = null;
    try
    {
      inputStream = uriConverter.createInputStream(uri);
      List<String> lines = IOUtil.readLines(inputStream, "UTF-8"); //$NON-NLS-1$
      if (!lines.contains("<marketplace>")) //$NON-NLS-1$
      {
        throw new IOException("Missing market place entry: " + (lines.isEmpty() ? "<no-content>" : lines.get(0))); //$NON-NLS-1$ //$NON-NLS-2$
      }

      for (String line : lines)
      {
        Matcher iuMatcher = IU_PATTERN.matcher(line);
        if (iuMatcher.matches())
        {
          String iu = iuMatcher.group(2);
          Requirement requirement = P2Factory.eINSTANCE.createRequirement(iu.endsWith(Requirement.FEATURE_SUFFIX) ? iu : iu + Requirement.FEATURE_SUFFIX);
          requirements.add(requirement);

          String attributes = iuMatcher.group(1);
          if (attributes.contains("required=\"TRUE\"")) //$NON-NLS-1$
          {
            setRequired(requirement);
          }

          if (attributes.contains("selected=\"TRUE\"")) //$NON-NLS-1$
          {
            setSelected(requirement);
          }
        }

        Matcher updateURLMatcher = UPDATE_URL_PATTERN.matcher(line);
        if (updateURLMatcher.matches())
        {
          updateSite = URI.createURI(updateURLMatcher.group(1));
        }

        Matcher nodeMatcher = NODE_PATTERN.matcher(line);
        if (nodeMatcher.matches())
        {
          label = nodeMatcher.group(1);
          String listingValue = nodeMatcher.group(2);
          if (listingValue.startsWith("http:")) //$NON-NLS-1$
          {
            listingValue = "https:" + listingValue.substring("http:".length()); //$NON-NLS-1$ //$NON-NLS-2$
          }

          listing = URI.createURI(listingValue);
        }
      }
    }
    catch (IOException ex)
    {
      exception = ex;
      listing = uri;
    }
    finally
    {
      IOUtil.closeSilent(inputStream);
    }

    this.updateSite = updateSite;
    this.listing = listing;
    this.label = label;
    this.exception = exception;
  }

  public IOException getException()
  {
    return exception;
  }

  public URI getUpdateSite()
  {
    return updateSite;
  }

  public URI getListing()
  {
    return listing;
  }

  public String getLabel()
  {
    return label;
  }

  public List<Requirement> getRequirements()
  {
    return new ArrayList<>(EcoreUtil.copyAll(requirements));
  }

  public static boolean isRequired(Requirement requirement)
  {
    Annotation annotation = requirement.getAnnotation(MARKET_PLACE_ANNOTATION);
    return annotation != null && "true".equals(annotation.getDetails().get("required")); //$NON-NLS-1$ //$NON-NLS-2$
  }

  private static void setRequired(Requirement requirement)
  {
    Annotation annotation = requirement.getAnnotation(MARKET_PLACE_ANNOTATION);
    if (annotation == null)
    {
      annotation = BaseFactory.eINSTANCE.createAnnotation();
      annotation.setSource(MARKET_PLACE_ANNOTATION);
      requirement.getAnnotations().add(annotation);
    }
    annotation.getDetails().put("required", "true"); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static boolean isSelected(Requirement requirement)
  {
    Annotation annotation = requirement.getAnnotation(MARKET_PLACE_ANNOTATION);
    return annotation != null && "true".equals(annotation.getDetails().get("selected")); //$NON-NLS-1$ //$NON-NLS-2$
  }

  private static void setSelected(Requirement requirement)
  {
    Annotation annotation = requirement.getAnnotation(MARKET_PLACE_ANNOTATION);
    if (annotation == null)
    {
      annotation = BaseFactory.eINSTANCE.createAnnotation();
      annotation.setSource(MARKET_PLACE_ANNOTATION);
      requirement.getAnnotations().add(annotation);
    }
    annotation.getDetails().put("selected", "true"); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static boolean isMarketPlaceListing(URI uri)
  {
    return getMarketPlaceListingURI(uri) != null;
  }

  public static MarketPlaceListing getMarketPlaceListing(URI uri, URIConverter uriConverter)
  {
    MarketPlaceListing marketPlaceListing = MARKET_PLACE_LISTINGS.get(uri);
    if (marketPlaceListing == null)
    {
      URI marketPlaceListingURI = getMarketPlaceListingURI(uri);
      if (marketPlaceListingURI != null)
      {
        marketPlaceListing = MARKET_PLACE_LISTINGS.get(marketPlaceListingURI);
        if (marketPlaceListing == null)
        {
          marketPlaceListing = new MarketPlaceListing(marketPlaceListingURI, uriConverter);
          MARKET_PLACE_LISTINGS.put(uri, marketPlaceListing);
          MARKET_PLACE_LISTINGS.put(marketPlaceListingURI, marketPlaceListing);
          MARKET_PLACE_LISTINGS.put(marketPlaceListing.getListing(), marketPlaceListing);
        }
      }
    }

    return marketPlaceListing;
  }

  public static void flush()
  {
    MARKET_PLACE_LISTINGS.clear();
  }

  private static URI getMarketPlaceListingURI(URI uri)
  {
    String uriLiteral = uri.toString();
    Matcher matcher = MARKET_PLACE_CONTENT_PATTERN.matcher(uriLiteral);
    if (matcher.matches())
    {
      return URI.createURI(matcher.group(1) + "/api/p"); //$NON-NLS-1$
    }

    matcher = MARKET_PLACE_INSTALL_PATTERN.matcher(uriLiteral);
    if (matcher.matches())
    {
      String listing2 = matcher.group(1);
      return URI.createURI("https://marketplace.eclipse.org/node/" + listing2 + "/api/p"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    return null;
  }
}
