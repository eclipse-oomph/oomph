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
package org.eclipse.oomph.version.ui.quickfixes;

import org.eclipse.oomph.version.Markers;
import org.eclipse.oomph.version.ui.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.IDocument;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class ReplaceResolution extends AbstractDocumentResolution
{
  private String problemType;

  private String replacement;

  public ReplaceResolution(IMarker marker, String problemType, String replacement)
  {
    super(marker, getLabel(marker, problemType, replacement), replacement == null ? Activator.CORRECTION_DELETE_GIF : Activator.CORRECTION_CHANGE_GIF);
    this.problemType = problemType;
    this.replacement = replacement == null ? "" : replacement; //$NON-NLS-1$
  }

  private static String getLabel(IMarker marker, String problemType, String replacement)
  {
    if (Markers.SCHEMA_BUILDER_PROBLEM.equals(problemType))
    {
      return replacement == null ? Messages.ReplaceResolution_label_removeSchemaBuilder : Messages.ReplaceResolution_label_addSchemaBuilder;
    }

    if (Markers.FEATURE_NATURE_PROBLEM.equals(problemType))
    {
      return Messages.ReplaceResolution_label_addFeatureBuilder;
    }

    if (Markers.FEATURE_CLOSURE_PROBLEM.equals(problemType))
    {
      return Messages.ReplaceResolution_label_addPluginReference;
    }

    if (Markers.DEBUG_OPTION_PROBLEM.equals(problemType))
    {
      return Messages.ReplaceResolution_label_changeDebugOption;
    }

    if (Markers.AUTOMATIC_MODULE_NAME_PROBLEM.equals(problemType))
    {
      return Messages.ReplaceResolution_label_changeAutomaticModuleName;
    }

    if (Markers.MAVEN_POM_PROBLEM.equals(problemType))
    {
      String message = Markers.getAttribute(marker, IMarker.MESSAGE);
      if (message != null)
      {
        if (message.startsWith("Maven artifactId")) //$NON-NLS-1$
        {
          return Messages.ReplaceResolution_label_changeMavenArtifactId;
        }

        if (message.startsWith("Maven version")) //$NON-NLS-1$
        {
          return Messages.ReplaceResolution_label_changeMavenVersion;
        }
      }
    }

    return replacement == null ? Messages.ReplaceResolution_label_removeReference
        : Markers.getQuickFixAlternativeReplacement(marker) == null ? Messages.ReplaceResolution_label_changeVersion
            : Messages.ReplaceResolution_label_changeToExactVersion;
  }

  @Override
  public String getDescription()
  {
    if (replacement.length() != 0)
    {
      return getLabel() + ' ' + Messages.ReplaceResolution_label_to + ' ' + replacement;
    }

    return super.getDescription();
  }

  @Override
  protected boolean isApplicable(IMarker marker)
  {
    if (!problemType.equals(Markers.getProblemType(marker)))
    {
      return false;
    }

    if (Markers.getQuickFixPattern(marker) == null)
    {
      return false;
    }

    boolean expectedReplacement = replacement.length() != 0;
    boolean actualReplacement = getQuickFixReplacement(marker) != null;
    return actualReplacement == expectedReplacement;
  }

  protected String getQuickFixReplacement(IMarker marker)
  {
    return Markers.getQuickFixReplacement(marker);
  }

  @Override
  protected boolean apply(IMarker marker, IDocument document) throws Exception
  {
    String content = document.get();

    String regEx = Markers.getQuickFixPattern(marker);
    String replacement = getQuickFixReplacement(marker);

    Pattern pattern = Pattern.compile(regEx, Pattern.MULTILINE | Pattern.DOTALL);
    Matcher matcher = pattern.matcher(content);
    if (matcher.find())
    {
      int start;
      int end;
      if (replacement != null && replacement.length() != 0)
      {
        start = matcher.start(1);
        end = matcher.end(1);

        for (int i = 1; i < 9; i++)
        {
          String insertTag = "\\" + i; //$NON-NLS-1$
          if (replacement.contains(insertTag))
          {
            int offset = matcher.start(i);
            int length = matcher.end(i) - offset;
            String found = document.get(offset, length);
            replacement = replacement.replace(insertTag, found);
          }
        }
      }
      else
      {
        start = matcher.start();
        end = matcher.end();
        replacement = ""; //$NON-NLS-1$
      }

      document.replace(start, end - start, replacement);
      return true;
    }

    return false;
  }
}
