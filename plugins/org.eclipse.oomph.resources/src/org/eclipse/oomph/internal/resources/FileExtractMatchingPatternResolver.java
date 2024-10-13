/*
 * Copyright (c) 2024 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.internal.resources;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.eclipse.osgi.util.NLS;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileExtractMatchingPatternResolver implements IDynamicVariableResolver
{
  @Override
  public String resolveValue(IDynamicVariable variable, String argument) throws CoreException
  {
    return new Evaluator(variable, argument).evaluate();
  }

  private static class Evaluator
  {
    private final IDynamicVariable variable;

    private final String argument;

    public Evaluator(IDynamicVariable variable, String argument)
    {
      this.variable = variable;
      this.argument = argument;
    }

    public String evaluate() throws CoreException
    {
      if (argument == null)
      {
        throw coreException(NLS.bind(Messages.FileExtractMatchingPatternResolver_UnspecifedArguments_exception, variable()), null);
      }
      else
      {
        StringBuilder result = new StringBuilder();
        String[] arguments = argument.split("(?<!\\\\),", -1); //$NON-NLS-1$
        Path path = path(argument(arguments, 0));
        String content = read(path, charset(argument(arguments, 1)));
        Pattern pattern = pattern(argument(arguments, 2));
        String defaultValue = argument(arguments, 4);
        String substitution = argument(arguments, 3);

        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
        {
          matcher = pattern.matcher(content.substring(matcher.start()));
          matcher.find();
          matcher.appendReplacement(result, substitution);
        }
        else
        {
          result.append(defaultValue);
        }

        return result.toString();
      }

    }

    @SuppressWarnings("nls")
    private String decode(String value)
    {
      return value.replace("\\,", ",");
    }

    private Pattern pattern(String pattern) throws CoreException
    {
      if (pattern.isBlank())
      {
        throw coreException(NLS.bind(Messages.FileExtractMatchingPatternResolver_InvalidBlankPattern_exception, variable(), pattern), null);
      }

      try
      {
        return Pattern.compile(pattern);
      }
      catch (Exception ex)
      {
        throw coreException(NLS.bind(Messages.FileExtractMatchingPatternResolver_InvalidPattern_exception, variable(), pattern), ex);
      }
    }

    private String read(Path path, Charset charset) throws CoreException
    {
      try
      {
        return Files.readString(path, charset);
      }
      catch (IOException ex)
      {
        throw coreException(NLS.bind(Messages.FileExtractMatchingPatternResolver_InvalidRead_excpetion, variable(), path), null);
      }
    }

    private String argument(String[] arguments, int i) throws CoreException
    {
      if (i < arguments.length)
      {
        return decode(arguments[i]);
      }

      throw coreException(NLS.bind(Messages.FileExtractMatchingPatternResolver_MissingArgument_exception, variable(), i + 1), null);
    }

    private Charset charset(String charset) throws CoreException
    {
      try
      {
        return Charset.forName(charset);
      }
      catch (Exception ex)
      {
        throw coreException(NLS.bind(Messages.FileExtractMatchingPatternResolver_InvalidCharset_exception, variable(), charset), ex);
      }
    }

    private Path path(String path) throws CoreException
    {
      try
      {
        return Path.of(path);
      }
      catch (Exception ex)
      {
        throw coreException(NLS.bind(Messages.FileExtractMatchingPatternResolver_InvalidPath_exception, variable(), path), ex);
      }
    }

    @SuppressWarnings("nls")
    private CoreException coreException(String message, Throwable throwable) throws CoreException
    {
      try
      {
        if (throwable != null)
        {
          String exceptionMessage = throwable.getLocalizedMessage();
          if (exceptionMessage == null || exceptionMessage.isBlank())
          {
            exceptionMessage = throwable.getMessage();
            if (exceptionMessage == null || exceptionMessage.isBlank())
            {
              exceptionMessage = throwable.getClass().getSimpleName();
            }
          }

          throw new CoreException(new Status(IStatus.ERROR, getClass(), message + ":\n  " + exceptionMessage.replaceAll(":$", ""), throwable));
        }

        throw new CoreException(new Status(IStatus.ERROR, getClass(), message));
      }
      catch (CoreException ex)
      {
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace())
        {
          if ("getProgramArguments".equals(stackTraceElement.getMethodName())
              && "org.eclipse.m2e.internal.launch.MavenLaunchDelegate".equals(stackTraceElement.getClassName()))
          {
            ResourcesPlugin.INSTANCE.log(ex.getStatus());
          }
        }

        throw ex;
      }
    }

    @SuppressWarnings("nls")
    private String variable()
    {
      return "${" + variable.getName() + (argument == null ? "" : ":" + argument) + "}";
    }
  }
}
