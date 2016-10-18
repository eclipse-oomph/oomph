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
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.util.EditUIUtil;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class SetupEditorSupport
{
  /**
   * The job family used by the setup editor for loading the model using a resource mirror.
   */
  public static final Object FAMILY_MODEL_LOAD = new Object();

  public static final String EDITOR_ID = "org.eclipse.oomph.setup.presentation.SetupEditorID";

  public static final String TEXT_EDITOR_ID = "org.eclipse.ui.DefaultTextEditor";

  public static final String PREF_TEXT_EDITOR_ID = "preferred.text.editor";

  public static IEditorPart getEditor(final IWorkbenchPage page, final URI uri, boolean force, LoadHandler... loadHandlers)
  {
    URIConverter uriConverter = SetupCoreUtil.createResourceSet().getURIConverter();
    URI trimmedURI = uri.trimFragment();
    final URI normalizedURI = uriConverter.normalize(trimmedURI);

    final IEditorInput editorInput = getEditorInput(normalizedURI, trimmedURI);
    return getEditor(page, editorInput, uri, uriConverter, force, loadHandlers);
  }

  public static IEditorPart getEditor(final IWorkbenchPage page, IEditorInput editorInput, URI uri, boolean force, LoadHandler... loadHandlers)
  {
    URIConverter uriConverter = SetupCoreUtil.createResourceSet().getURIConverter();
    return getEditor(page, editorInput, uri, uriConverter, force, loadHandlers);
  }

  private static IEditorPart getEditor(final IWorkbenchPage page, final IEditorInput editorInput, URI uri, URIConverter uriConverter, boolean force,
      LoadHandler... loadHandlers)
  {
    try
    {
      final String fragment = uri.fragment();

      IEditorPart editor = findEditor(EDITOR_ID, page, uriConverter, editorInput);
      if (editor != null)
      {
        page.activate(editor);
      }
      else if (force)
      {
        editor = page.openEditor(editorInput, EDITOR_ID, true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
      }

      // If there is an editor...
      if (editor != null)
      {
        // Convert the handlers to a list, and if there is a fragment, add a handler for selecting the object associated with that fragment.
        final List<LoadHandler> handlers = new ArrayList<LoadHandler>(Arrays.asList(loadHandlers));
        if (fragment != null)
        {
          handlers.add(0, new LoadHandler()
          {
            @Override
            protected void loaded(IEditorPart editor, EditingDomain domain, Resource resource)
            {
              EObject eObject = resource.getEObject(fragment);
              if (eObject != null)
              {
                ((IViewerProvider)editor).getViewer().setSelection(new StructuredSelection(eObject), true);
              }
            }
          });
        }

        // If there are handlers...
        if (!handlers.isEmpty())
        {
          final IEditorPart finalEditor = editor;
          Job job = new Job("Loading Setup Editor")
          {
            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
              try
              {
                // Wait for the model load jobs to complete.
                Job.getJobManager().join(SetupEditorSupport.FAMILY_MODEL_LOAD, monitor);

                Shell shell = page.getWorkbenchWindow().getShell();
                if (!shell.isDisposed())
                {
                  shell.getDisplay().asyncExec(new Runnable()
                  {
                    public void run()
                    {
                      URI uri = EditUIUtil.getURI(editorInput);
                      for (LoadHandler handler : handlers)
                      {
                        handler.loaded(finalEditor, uri);
                      }
                    }
                  });
                }

                return Status.OK_STATUS;
              }
              catch (Exception ex)
              {
                return SetupUIPlugin.INSTANCE.getStatus(ex);
              }
            }
          };

          // Hide the job and schedule it.
          job.setSystem(true);
          job.schedule();
        }

        return editor;
      }
    }
    catch (Exception ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }

    return null;
  }

  private static String getTextEditorID()
  {
    try
    {
      String editorID = SetupUIPlugin.INSTANCE.getPreferenceStore().getString(PREF_TEXT_EDITOR_ID);
      if (!StringUtil.isEmpty(editorID))
      {
        IEditorDescriptor descriptor = PlatformUI.getWorkbench().getEditorRegistry().findEditor(editorID);
        if (descriptor != null)
        {
          return editorID;
        }
      }
    }
    catch (Exception ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }

    return TEXT_EDITOR_ID;
  }

  public static IEditorPart getTextEditor(final IWorkbenchPage page, final URI uri)
  {
    URIConverter uriConverter = SetupCoreUtil.createResourceSet().getURIConverter();
    URI trimmedURI = uri.trimFragment();
    URI normalizedURI = uriConverter.normalize(trimmedURI);
    IEditorInput editorInput = getTextEditorInput(normalizedURI, trimmedURI);
    return getTextEditor(page, uriConverter, editorInput);
  }

  public static IEditorPart getTextEditor(final IWorkbenchPage page, IEditorInput editorInput)
  {
    URIConverter uriConverter = SetupCoreUtil.createResourceSet().getURIConverter();
    return getTextEditor(page, uriConverter, editorInput);
  }

  public static IEditorPart getTextEditor(final IWorkbenchPage page, URIConverter uriConverter, IEditorInput editorInput)
  {
    try
    {
      String editorID = getTextEditorID();

      IEditorPart editor = findEditor(editorID, page, uriConverter, editorInput);
      if (editor != null)
      {
        page.activate(editor);
      }
      else
      {
        editor = page.openEditor(editorInput, editorID, true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
      }

      return editor;
    }
    catch (Exception ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }

    return null;
  }

  private static IEditorPart findEditor(String editorID, IWorkbenchPage page, URIConverter uriConverter, IEditorInput input)
  {
    URI resourceURI = EditUIUtil.getURI(input);

    for (IEditorReference editorReference : page.getEditorReferences())
    {
      if (editorReference.getId().equals(editorID))
      {
        try
        {
          IEditorInput editorInput = editorReference.getEditorInput();
          URI editorResourceURI = getURI(editorInput, uriConverter);
          if (editorResourceURI != null)
          {
            editorResourceURI = uriConverter.normalize(editorResourceURI).trimFragment();
            if (editorResourceURI.equals(uriConverter.normalize(resourceURI)))
            {
              return editorReference.getEditor(true);
            }
          }
        }
        catch (PartInitException ex)
        {
          SetupUIPlugin.INSTANCE.log(ex);
        }
      }
    }

    return null;
  }

  private static IEditorInput getTextEditorInput(final URI normalizedURI, URI originalURI)
  {
    IEditorInput editorInput = getEditorInput(normalizedURI, originalURI);
    if (editorInput instanceof URIEditorInput)
    {
      URIEditorInput uriEditorInput = (URIEditorInput)editorInput;
      final URI uri = SetupContext.resolve(uriEditorInput.getURI());
      if (uri.isFile())
      {
        IFileStore store = EFS.getLocalFileSystem().getStore(new Path(uri.toFileString()));
        return new FileStoreEditorInput(store);
      }

      return new IStorageEditorInput()
      {
        public <T> T getAdapter(Class<T> adapter)
        {
          return null;
        }

        public String getToolTipText()
        {
          return uri.toString();
        }

        public IPersistableElement getPersistable()
        {
          return null;
        }

        public String getName()
        {
          return uri.lastSegment();
        }

        public ImageDescriptor getImageDescriptor()
        {
          return null;
        }

        public boolean exists()
        {
          return true;
        }

        public IStorage getStorage() throws CoreException
        {
          return new IStorage()
          {
            public <T> T getAdapter(Class<T> adapter)
            {
              return null;
            }

            public boolean isReadOnly()
            {
              return true;
            }

            public String getName()
            {
              return uri.lastSegment();
            }

            public IPath getFullPath()
            {
              return new Path(uri.toString());
            }

            public InputStream getContents() throws CoreException
            {
              URIConverter uriConverter = SetupCoreUtil.createResourceSet().getURIConverter();
              try
              {
                return uriConverter.createInputStream(uri);
              }
              catch (IOException ex)
              {
                SetupUIPlugin.INSTANCE.coreException(ex);
                return null;
              }
            }
          };
        }
      };
    }

    return editorInput;
  }

  private static IEditorInput getEditorInput(URI normalizedURI, URI originalURI)
  {
    normalizedURI = SetupContext.resolve(normalizedURI);

    if (normalizedURI.isFile() && !normalizedURI.isRelative())
    {
      FileEditorInput editorInput = getFileEditorInput(normalizedURI);
      if (editorInput != null)
      {
        return editorInput;
      }
    }

    if (normalizedURI.isPlatformResource())
    {
      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(normalizedURI.toPlatformString(true)));
      if (file.isAccessible())
      {
        return new FileEditorInput(file);
      }
    }

    return new URIEditorInput(originalURI, originalURI.lastSegment());
  }

  private static FileEditorInput getFileEditorInput(URI uri)
  {
    if (uri.isFile() && !uri.isRelative())
    {
      try
      {
        IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(new java.net.URI(uri.toString()));
        for (IFile file : files)
        {
          if (file.isAccessible())
          {
            return new FileEditorInput(file);
          }
        }
      }
      catch (URISyntaxException ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }
    }

    return null;
  }

  private SetupEditorSupport()
  {
  }

  /**
   * @author Ed Merks
   */
  public static class LoadHandler
  {
    public void loaded(IEditorPart editor, URI uri)
    {
      loaded(editor, ((IEditingDomainProvider)editor).getEditingDomain(), uri);
    }

    protected void loaded(IEditorPart editorPart, EditingDomain domain, URI uri)
    {
      Resource resource = domain.getResourceSet().getResource(uri, false);
      if (resource != null)
      {
        loaded(editorPart, domain, resource);
      }
    }

    protected void loaded(IEditorPart editor, EditingDomain domain, Resource resource)
    {
    }
  }

  public static URI getURI(IEditorInput editorInput, URIConverter uriConverter)
  {
    try
    {
      return EditUIUtil.getURI(editorInput, uriConverter);
    }
    catch (NoSuchMethodError ex)
    {
      return EditUIUtil.getURI(editorInput);
    }
  }
}
