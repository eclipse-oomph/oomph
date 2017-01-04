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
package org.eclipse.oomph.base.util;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.SAXXMIHandler;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;
import org.eclipse.emf.ecore.xmi.impl.XMILoadImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMISaveImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLString;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource </b> associated with the package.
 * @implements org.eclipse.oomph.setup.util.SetupResource
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.base.util.BaseResourceFactoryImpl
 * @generated
 */
public class BaseResourceImpl extends XMIResourceImpl implements org.eclipse.oomph.base.util.BaseResource
{
  /**
   * Creates an instance of the resource.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param uri the URI of the new resource.
   * @generated
   */
  public BaseResourceImpl(URI uri)
  {
    super(uri);
  }

  @Override
  public void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException
  {
    if (options.get(OPTION_EXTENDED_META_DATA) instanceof Boolean)
    {
      ResourceSet resourceSet = getResourceSet();
      Map<Object, Object> effectiveOptions = new HashMap<Object, Object>(options);
      effectiveOptions.put(OPTION_EXTENDED_META_DATA,
          new BasicExtendedMetaData(resourceSet == null ? EPackage.Registry.INSTANCE : resourceSet.getPackageRegistry())
          {
            @Override
            public EStructuralFeature getElement(EClass eClass, String namespace, String name)
            {
              EStructuralFeature eStructuralFeature = super.getElement(eClass, namespace, name);
              if (eStructuralFeature == null)
              {
                eStructuralFeature = super.getElement(eClass, namespace, name.substring(0, name.length() - 1));
              }

              if (eStructuralFeature == null)
              {
                eStructuralFeature = eClass.getEStructuralFeature(name);
              }

              return eStructuralFeature;
            }
          });
      options = effectiveOptions;
    }

    super.doLoad(inputStream, options);
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
          @Override
          protected String getLocation()
          {
            String result = super.getLocation();
            URI uri = getURI();
            if (uri != null)
            {
              String normalizedURI = getURIConverter().normalize(uri).toString();
              if (!result.equals(normalizedURI))
              {
                result += " -> " + normalizedURI;
              }
            }

            return result;
          }

          @Override
          public InputSource resolveEntity(String publicId, String systemId) throws SAXException
          {
            URI uri = getURI();
            String message = "Unexpected entity: publicId=" + publicId + ", systemId=" + systemId + ", uri=" + uri;

            URI normalizedURI = getURIConverter().normalize(uri);
            if (!normalizedURI.equals(uri))
            {
              message += ", normalizedURI=" + normalizedURI;
            }

            throw new SAXException(message);
          }

          @Override
          protected void setFeatureValue(EObject object, EStructuralFeature feature, Object value)
          {
            if (value != null && feature.getEType() == BasePackage.Literals.TEXT)
            {
              // Split the string value into lines.
              String stringValue = value.toString();
              String[] lines = stringValue.split("\n", Integer.MAX_VALUE);
              if (lines.length > 1)
              {
                // Ignore the first line if it's only white space and the last line if it's only white space.
                int start = lines[0].trim().length() == 0 ? 1 : 0;
                String lastLine = lines[lines.length - 1];
                int end = lastLine.trim().length() == 0 ? lines.length - 1 : lines.length;

                if (start < end)
                {
                  // If the first line starts on the same line as the element start tag, ignore that line in terms of indentation computation.
                  // Compute the minimum indentation from the first non white space character of each line.
                  int minIndent = Integer.MAX_VALUE;
                  for (int i = start == 0 ? 1 : start; i < end; ++i)
                  {
                    String line = lines[i];
                    String trimmedLine = line.trim();
                    if (trimmedLine.length() > 0)
                    {
                      int indent = line.indexOf(trimmedLine);
                      if (minIndent > indent)
                      {
                        minIndent = indent;
                      }
                    }
                  }

                  // If there are only blank lines, we're a bit confused.
                  if (minIndent == Integer.MAX_VALUE)
                  {
                    minIndent = lastLine.length();

                    // Use the depth of the stack to determine how much of the whitespace in the last line can be attributed to nesting.
                    // Assume that the XML is consistently formatted/indented and that a consistent fraction of the indentation is attributable to each nesting
                    // level such that the current nesting level would need one more such fractional increment.
                    int depth = elements.size();
                    minIndent += minIndent / depth;
                  }

                  // If the first line starts on the same line as the element start tag, just add it as is.
                  StringBuilder s = new StringBuilder();
                  if (start == 0)
                  {
                    s.append(lines[0]);
                    if (++start < end)
                    {
                      s.append("\n");
                    }
                  }

                  // For all the other lines, trim off the leading indentation.
                  for (int i = start; i < end;)
                  {
                    String line = lines[i];
                    int length = line.length();
                    if (length > minIndent)
                    {
                      s.append(line, minIndent, length);
                    }

                    if (++i < end)
                    {
                      s.append("\n");
                    }
                  }

                  value = s.toString();
                }
              }
            }

            super.setFeatureValue(object, feature, value);
          }
        };
      }
    };
  }

  private static final Method GET_ELEMENT_INDENT_METHOD;

  static
  {
    GET_ELEMENT_INDENT_METHOD = ReflectUtil.getMethod(XMLString.class, "getElementIndent", int.class);
    GET_ELEMENT_INDENT_METHOD.setAccessible(true);
  }

  @Override
  protected XMLSave createXMLSave()
  {
    return new XMISaveImpl(createXMLHelper())
    {
      @Override
      protected String getDatatypeValue(Object value, EStructuralFeature f, boolean isAttribute)
      {
        // If we're serializing the a Text value as an element.
        if (value != null && !isAttribute && f.getEType() == BasePackage.Literals.TEXT)
        {
          // Split what we know is a String value into lines.
          String stringValue = value.toString();
          String[] lines = stringValue.split("\r?\n", Integer.MAX_VALUE);
          if (lines.length == 1)
          {
            // If there are no line separators, serialize just the string value.
            //
            return escape == null ? stringValue : escape.convertText(stringValue);
          }

          // Compute the target format that will serialize nested under the element start/end tabs by one indentation level.
          String elementIdent = getElementIndent(1);
          String contentIndent = getElementIndent(2);
          StringBuilder s = new StringBuilder("\n");
          for (int i = 0; i < lines.length; ++i)
          {
            // Only if the line isn't empty, serialize the indentation followed by the line content.
            String line = lines[i];
            if (line.length() != 0)
            {
              s.append(contentIndent);
              s.append(line);
            }

            // Always add the line separator.
            s.append("\n");
          }

          // Add enough indentation so the closing element is indented correctly too.
          s.append(elementIdent);

          return escape == null ? s.toString() : escape.convertText(s.toString());
        }

        return super.getDatatypeValue(value, f, isAttribute);
      }

      private String getElementIndent(int extraIndent)
      {
        try
        {
          return GET_ELEMENT_INDENT_METHOD.invoke(doc, extraIndent).toString();
        }
        catch (Throwable ex)
        {
          return "";
        }
      }
    };
  }

  @Override
  protected XMLHelper createXMLHelper()
  {
    return new BaseHelperImpl(this);
  }

  /**
   * @author Ed Merks
   */
  public static class BaseHelperImpl extends XMIHelperImpl
  {
    public BaseHelperImpl(XMLResource resource)
    {
      super(resource);
    }

    @Override
    public URI getHREF(Resource otherResource, EObject obj)
    {
      // Walk up the containers.
      InternalEObject internalEObject = (InternalEObject)obj;

      EObject basisObject = null;
      boolean checkedID = false;
      String id = null;
      for (InternalEObject container = (InternalEObject)internalEObject.eContainer(); container != null; container = (InternalEObject)container.eContainer())
      {
        // If the object is contained by this helper's resource, we want a normal "downward" pointing href.
        Internal eDirectResource = container.eDirectResource();
        if (eDirectResource == resource)
        {
          return super.getHREF(otherResource, obj);
        }

        if (eDirectResource != null && basisObject == null)
        {
          if (!checkedID)
          {
            checkedID = true;
            id = EcoreUtil.getID(obj);
          }

          if (id != null && container instanceof ModelElement)
          {
            basisObject = container;
          }
        }

        internalEObject = container;
      }

      Resource rootContainerResource = internalEObject.eResource();
      if (rootContainerResource != otherResource && rootContainerResource != null)
      {
        String fragmentPath = EcoreUtil.getRelativeURIFragmentPath(internalEObject, basisObject == null ? obj : basisObject);
        URI proxyURI = super.getHREF(rootContainerResource, internalEObject);
        StringBuilder fragment = new StringBuilder(proxyURI.fragment());
        fragment.append('/');
        fragment.append(fragmentPath);
        if (basisObject != null)
        {
          fragment.append("/'");
          encode(fragment, id);
          fragment.append('\'');
        }

        return proxyURI.appendFragment(fragment.toString());
      }

      return super.getHREF(otherResource, obj);
    }

    @Override
    public URI deresolve(URI uri)
    {
      return super.deresolve(BaseUtil.resolveBogusURI(uri));
    }
  }

  private static final String[] ESCAPE = { "%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07", "%08", "%09", "%0A", "%0B", "%0C", "%0D", "%0E", "%0F",
      "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17", "%18", "%19", "%1A", "%1B", "%1C", "%1D", "%1E", "%1F", "%20", null, "%22", "%23", null, "%25",
      "%26", "%27", null, null, null, null, "%2C", null, null, "%2F", null, null, null, null, null, null, null, null, null, null, "%3A", null, "%3C", null,
      "%3E", null, };

  private static void encode(StringBuilder result, String value)
  {
    int length = value.length();
    boolean encode = false;
    for (int i = 0; i < length; ++i)
    {
      char character = value.charAt(i);
      if (character < ESCAPE.length)
      {
        String escape = ESCAPE[character];
        if (escape != null)
        {
          if (!encode)
          {
            encode = true;
            result.append(value, 0, i);
          }

          result.append(escape);
          continue;
        }
      }

      if (encode)
      {
        result.append(character);
      }
    }

    if (!encode)
    {
      result.append(value);
    }
  }
} // SetupResourceImpl
