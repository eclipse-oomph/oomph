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
package org.eclipse.oomph.setup.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.oomph.setup.util.StringExpander;
import org.eclipse.oomph.tests.AbstractTest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO Test with Unix file separator.
 *
 * @author Eike Stepper
 */
@FixMethodOrder(MethodSorters.JVM)
public class StringExpanderTests extends AbstractTest
{
  private final StringExpander expander = new WindowsStringExpander();

  @Test
  public void testNull() throws Exception
  {
    assertThat(expander.expandString(null), isNull());
  }

  @Test
  public void testVariable() throws Exception
  {
    assertThat(expander.expandString("START${windows.path}END"), is("STARTC:\\develop\\java-latestEND"));
  }

  @Test
  public void testVariableSlash() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/}END"), is("STARTC:\\develop\\java-latest\\END"));
  }

  @Test
  public void testVariableSlashSlash() throws Exception
  {
    assertThat(expander.expandString("START${windows.path//}END"), is("STARTC:\\develop\\java-latest\\\\END"));
  }

  @Test
  public void testVariableSlashPipe() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/|}END"), is("STARTC:\\develop\\java-latest\\END"));
  }

  @Test
  public void testVariablePipe() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|}END"), is("STARTC:\\develop\\java-latestEND"));
  }

  @Test
  public void testVariablePipePipe() throws Exception
  {
    assertThat(expander.expandString("START${windows.path||}END"), is("STARTC:\\develop\\java-latestEND"));
  }

  @Test
  public void testVariablePipeSlash() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|/}END"), is("STARTC:\\develop\\java-latest\\END"));
  }

  @Test
  public void testVariableFilter() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|property}END"), is("STARTC:\\\\develop\\\\java-latestEND"));
  }

  @Test
  public void testVariableUnresolvedFilter() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|unknown}END"), is("STARTC:\\develop\\java-latestEND"));
  }

  @Test
  public void testVariablePath() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/ws}END"), is("STARTC:\\develop\\java-latest\\wsEND"));
  }

  @Test
  public void testVariableFilterPath() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|property/ws}END"), is("STARTC:\\\\develop\\\\java-latest\\wsEND"));
  }

  @Test
  public void testVariableUnresolvedFilterPath() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|unknown/ws}END"), is("STARTC:\\develop\\java-latest\\wsEND"));
  }

  @Test
  public void testVariablePathFilter() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/ws|property}END"), is("STARTC:\\\\develop\\\\java-latest\\\\wsEND"));
  }

  @Test
  public void testVariablePathUnresolvedFilter() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/ws|unknown}END"), is("STARTC:\\develop\\java-latest\\wsEND"));
  }

  @Test
  public void testUnresolvedVariable() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path}END"), is("START${unknown.path}END"));
  }

  @Test
  public void testUnresolvedVariableFilter() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path|property}END"), is("START${unknown.path|property}END"));
  }

  @Test
  public void testUnresolvedVariableUnresolvedFilter() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path|unknown}END"), is("START${unknown.path|unknown}END"));
  }

  @Test
  public void testUnresolvedVariablePath() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path/ws}END"), is("START${unknown.path/ws}END"));
  }

  @Test
  public void testUnresolvedVariableFilterPath() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path|property/ws}END"), is("START${unknown.path|property/ws}END"));
  }

  @Test
  public void testUnresolvedVariablePathFilter() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path/ws|property}END"), is("START${unknown.path/ws|property}END"));
  }

  @Test
  public void testTwice() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/ws|property}${windows.path/ws|property}END"),
        is("STARTC:\\\\develop\\\\java-latest\\\\wsC:\\\\develop\\\\java-latest\\\\wsEND"));
  }

  @Test
  public void testTwiceWithText() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/ws|property}MIDDLE${windows.path/ws|property}END"),
        is("STARTC:\\\\develop\\\\java-latest\\\\wsMIDDLEC:\\\\develop\\\\java-latest\\\\wsEND"));
  }

  @Test
  public void testDollar() throws Exception
  {
    assertThat(expander.expandString("START$$END"), is("START$END"));
  }

  @Test
  public void testEscaped() throws Exception
  {
    assertThat(expander.expandString("START$${windows.path/ws|property}END"), is("START${windows.path/ws|property}END"));
  }

  @Test
  public void testEscapedUnescaped() throws Exception
  {
    assertThat(expander.expandString("START$${windows.path/ws|property}${windows.path/ws|property}END"),
        is("START${windows.path/ws|property}C:\\\\develop\\\\java-latest\\\\wsEND"));
  }

  /**
   * @author Eike Stepper
   */
  private static final class WindowsStringExpander extends StringExpander
  {
    private final Map<String, String> variables = new HashMap<String, String>();

    public WindowsStringExpander()
    {
      variables.put("windows.path", "C:\\develop\\java-latest");
      variables.put("unix.path", "/develop/java-latest");
      variables.put("user.name", "stepper");
    }

    @Override
    protected String getFileSeparator()
    {
      return "\\";
    }

    @Override
    protected String resolve(String key)
    {
      return variables.get(key);
    }

    @Override
    protected boolean isUnexpanded(String key)
    {
      return !variables.containsKey(key);
    }

    @Override
    protected String filter(String value, String filterName)
    {
      if ("property".equals(filterName))
      {
        return value.replaceAll("\\\\", "\\\\\\\\");
      }

      return value;
    }
  }
}
