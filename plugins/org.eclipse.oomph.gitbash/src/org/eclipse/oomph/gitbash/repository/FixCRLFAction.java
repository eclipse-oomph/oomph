/*
 * Copyright (c) 2019 Ed Merks (Beralin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.gitbash.repository;

import org.eclipse.oomph.gitbash.AbstractAction;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.CoreConfig.AutoCRLF;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

@SuppressWarnings("nls")
public class FixCRLFAction extends AbstractAction<Repository>
{
  private Dialog dialog;

  private Text text;

  public FixCRLFAction()
  {
    super(Repository.class);
  }

  @SuppressWarnings("restriction")
  @Override
  protected void run(Shell shell, Repository repository)
  {
    createDialog(shell);

    int fixCount = 0;

    ProgressMonitor monitor = new ProgressMonitor()
    {
      @Override
      public void update(int completed)
      {
        processDisplay();
      }

      @Override
      public void start(int totalTasks)
      {
        processDisplay();
      }

      @Override
      public boolean isCancelled()
      {
        processDisplay();
        return false;
      }

      @Override
      public void endTask()
      {
        processDisplay();
      }

      @Override
      public void beginTask(String title, int totalWork)
      {
        processDisplay();
      }
    };

    Repository targetRepository = null;
    try
    {
      File directory = repository.getDirectory();
      File workTree = repository.getWorkTree();

      log("Processing " + workTree);

      File targetWorkTree = new File(workTree.getPath() + ".fix.crlf");
      if (!targetWorkTree.mkdir())
      {
        log("Failed to create target folder:" + targetWorkTree);
      }
      else
      {
        File targetDirectory = new File(targetWorkTree, ".git");
        log("Copying " + directory + " to " + targetDirectory);
        IOUtil.copyTree(directory, targetDirectory);

        log("Opening Git repository " + targetDirectory);
        Git git = Git.open(targetDirectory);
        targetRepository = git.getRepository();

        log("Configuring AutoCRL to false " + targetDirectory);
        StoredConfig config = targetRepository.getConfig();
        config.setEnum(ConfigConstants.CONFIG_CORE_SECTION, null, ConfigConstants.CONFIG_KEY_AUTOCRLF, AutoCRLF.FALSE);
        config.save();

        log("Resetting to create the working tree " + targetDirectory);
        ResetCommand resetCommand = new ResetCommand(targetRepository);
        resetCommand.setProgressMonitor(monitor);
        resetCommand.setMode(ResetType.HARD);
        resetCommand.call();

        log("Visiting tree " + targetDirectory);
        for (FileTreeIterator fileTreeIterator = new FileTreeIterator(targetRepository); !fileTreeIterator.eof(); fileTreeIterator.next(1))
        {
          fixCount += visit(fileTreeIterator.getEntryFile());
        }

        git.status().call();

      }
    }
    catch (Exception ex)
    {
      log("Failure");
      StringWriter out = new StringWriter();
      PrintWriter stackTrace = new PrintWriter(out);
      ex.printStackTrace(stackTrace);
      stackTrace.close();
      log(out.toString());
      return;
    }
    finally
    {
      if (targetRepository != null)
      {
        log("Adding to repository view " + targetRepository.getWorkTree());
        org.eclipse.egit.core.RepositoryUtil repositoryUtil = org.eclipse.egit.core.RepositoryUtil.INSTANCE;
        repositoryUtil.addConfiguredRepository(targetRepository.getDirectory());
        targetRepository.close();
      }
    }

    if (fixCount == 0)
    {
      log("Done");
      log("No files needed fixing");
    }
    else
    {
      log(fixCount + " files fixed");
    }
  }

  private void log(final String message)
  {
    text.append(message + "\n");
    processDisplay();
  }

  private void processDisplay()
  {
    while (text.getDisplay().readAndDispatch())
    {
    }
  }

  private void createDialog(Shell shell)
  {
    final Point size = shell.getSize();
    dialog = new Dialog(shell)
    {
      @Override
      protected void configureShell(Shell newShell)
      {
        super.configureShell(newShell);
        newShell.setText("Fix CRLF");
      }

      @Override
      protected Control createDialogArea(Composite parent)
      {
        Composite composite = (Composite)super.createDialogArea(parent);
        text = new Text(composite, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.widthHint = size.x / 2;
        gridData.heightHint = size.y / 2;
        text.setLayoutData(gridData);
        return composite;
      }
    };

    dialog.setBlockOnOpen(false);
    dialog.open();
  }

  private int visit(File file)
  {
    int result = 0;
    if (file.isDirectory())
    {
      for (File child : file.listFiles())
      {
        result += visit(child);
      }
    }
    else
    {
      byte[] bytes = IOUtil.readFile(file);
      if (RawText.isBinary(bytes))
      {
        log("Ignoring binary " + file);
      }
      else
      {
        boolean modified = false;
        ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length);
        for (int i = 0; i < bytes.length; i++)
        {
          byte b = bytes[i];
          if (b == '\r' && i + 1 < bytes.length && bytes[i + 1] == '\n')
          {
            out.write('\n');
            ++i;
            modified = true;
            continue;
          }

          out.write(b);
        }

        if (modified)
        {
          log("Fixing " + file);
          ++result;
          IOUtil.writeFile(file, out.toByteArray());
        }
      }
    }

    return result;
  }
}
