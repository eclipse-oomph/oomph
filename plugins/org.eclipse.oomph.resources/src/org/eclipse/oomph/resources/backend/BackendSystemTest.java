/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.resources.backend;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.oomph.resources.backend.BackendResource.Type;
import org.eclipse.oomph.tests.AbstractTest;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.Path;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public abstract class BackendSystemTest extends AbstractTest
{
  public static final Set<String> EMPTY = Collections.emptySet();

  public BackendSystemTest()
  {
  }

  protected abstract Set<String> getNames();

  protected abstract BackendSystem createBackendSystem();

  @Test
  public void testGetParent()
  {
    BackendSystem system = createBackendSystem();
    BackendContainer parent = system.getParent();
    assertThat(parent, isNull());
  }

  @Test
  public void testGetMembers()
  {
    BackendSystem system = createBackendSystem();
    BackendResource[] members = system.getMembers(null);
    assertThat(members.length, is(5));

    Set<String> names = getNames();
    int folders = 3;
    int files = 2;

    for (BackendResource member : members)
    {
      names.remove(member.getName());
      if (member.getType() == Type.FOLDER)
      {
        --folders;
      }

      if (member.getType() == Type.FILE)
      {
        --files;
      }

      BackendContainer parent = member.getParent();
      assertThat(parent, is((BackendContainer)system));

      BackendContainer parentParent = parent.getParent();
      assertThat(parentParent, isNull());
    }

    assertThat(names, is(EMPTY));
    assertThat(folders, is(0));
    assertThat(files, is(0));
  }

  @Test
  public void testFindMember()
  {
    BackendSystem system = createBackendSystem();

    BackendResource folder1 = system.findMember(new Path("folder1"), null);
    assertThat(folder1.getName(), is("folder1"));
    assertThat(folder1.getParent(), is((BackendContainer)system));
    assertThat(folder1.getSystemRelativeURI(), is(URI.createURI("folder1")));
    assertThat(folder1.exists(LOGGER), is(true));
    assertThat(folder1.getType(), is(BackendResource.Type.FOLDER));

    BackendResource file1 = system.findMember(new Path("file1.txt"), null);
    assertThat(file1.getName(), is("file1.txt"));
    assertThat(file1.getParent(), is((BackendContainer)system));
    assertThat(file1.getSystemRelativeURI(), is(URI.createURI("file1.txt")));
    assertThat(file1.exists(LOGGER), is(true));
    assertThat(file1.getType(), is(BackendResource.Type.FILE));

    BackendResource self = system.findMember(Path.EMPTY, null);
    assertThat(self.getName(), is(""));
    assertThat(self.getParent(), isNull());
    assertThat(self.getSystemRelativeURI(), is(URI.createURI("")));
    assertThat(self.exists(LOGGER), is(true));
    assertThat(self.getType(), is(BackendResource.Type.SYSTEM));

    try
    {
      BackendResource member = system.findMember(new Path("resourceX"), null);
      System.out.println(member);
      Assert.fail("BackendException expected");
    }
    catch (BackendException expected)
    {
      // SUCCESS
    }
  }

  @Test
  public void testGetFolder()
  {
    BackendSystem system = createBackendSystem();

    BackendFolder folder1 = system.getFolder(new Path("folder1"));
    assertThat(folder1.getName(), is("folder1"));
    assertThat(folder1.getParent(), is((BackendContainer)system));
    assertThat(folder1.getSystemRelativeURI(), is(URI.createURI("folder1")));
    assertThat(folder1.exists(LOGGER), is(true));

    BackendFolder folderX = system.getFolder(new Path("folderX"));
    assertThat(folderX.getName(), is("folderX"));
    assertThat(folderX.getParent(), is((BackendContainer)system));
    assertThat(folderX.getSystemRelativeURI(), is(URI.createURI("folderX")));
    assertThat(folderX.exists(LOGGER), is(false));
  }

  @Test
  public void testGetFile()
  {
    BackendSystem system = createBackendSystem();

    BackendFile file1 = system.getFile(new Path("file1.txt"));
    assertThat(file1.getName(), is("file1.txt"));
    assertThat(file1.getParent(), is((BackendContainer)system));
    assertThat(file1.getSystemRelativeURI(), is(URI.createURI("file1.txt")));
    assertThat(file1.exists(LOGGER), is(true));

    BackendFile fileX = system.getFile(new Path("fileX.txt"));
    assertThat(fileX.getName(), is("fileX.txt"));
    assertThat(fileX.getParent(), is((BackendContainer)system));
    assertThat(fileX.getSystemRelativeURI(), is(URI.createURI("fileX.txt")));
    assertThat(fileX.exists(LOGGER), is(false));
  }

  /**
   * @author Eike Stepper
   */
  public static class Local extends BackendSystemTest
  {
    private static File root;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
      root = createTempFolder();

      for (File folder : createTree(root))
      {
        if (folder.isDirectory())
        {
          createTree(folder);
        }
      }
    }

    private static File[] createTree(File parent)
    {
      File folder1 = createFolder(parent, "folder1");
      File folder2 = createFolder(parent, "folder2");
      File folder3 = createFolder(parent, "folder3");
      File file1 = createFile(parent, "file1.txt");
      File file2 = createFile(parent, "file2.txt");
      return new File[] { folder1, folder2, folder3, file1, file2 };
    }

    private static File createFolder(File parent, String name)
    {
      File folder = new File(parent, name);
      folder.mkdirs();
      return folder;
    }

    private static File createFile(File parent, String name)
    {
      File file = new File(parent, name);
      IOUtil.writeLines(file, "UTF-8", Collections.singletonList("A single line of text in " + name));
      return file;
    }

    @Override
    protected Set<String> getNames()
    {
      Set<String> names = new HashSet<String>();
      for (File file : root.listFiles())
      {
        names.add(file.getName());
      }

      return names;
    }

    @Override
    protected BackendSystem createBackendSystem()
    {
      return (BackendSystem)BackendResource.get(root.getAbsolutePath());
    }
  }
}
