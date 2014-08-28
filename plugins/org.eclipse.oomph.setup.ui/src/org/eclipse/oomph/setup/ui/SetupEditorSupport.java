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
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.SetupUtil;

import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.util.EditUIUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

import java.net.URISyntaxException;

/**
 * @author Eike Stepper
 */
public final class SetupEditorSupport
{
  public static final String EDITOR_ID = "org.eclipse.oomph.setup.presentation.SetupEditorID";

  public static IEditorPart findEditor(IWorkbenchPage page, URI uri)
  {
    URIConverter uriConverter = SetupUtil.createResourceSet().getURIConverter();
    URI normalizedURI = uriConverter.normalize(uri).trimFragment();

    for (IEditorReference editorReference : page.getEditorReferences())
    {
      if (editorReference.getId().equals(EDITOR_ID))
      {
        try
        {
          IEditorInput input = editorReference.getEditorInput();
          URI resourceURI = EditUIUtil.getURI(input);
          if (resourceURI != null)
          {
            resourceURI = uriConverter.normalize(resourceURI).trimFragment();
            if (resourceURI.equals(normalizedURI))
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

  public static void openEditor(final IWorkbenchPage page, URI uri)
  {
    openEditor(page, uri, null);
  }

  public static void openEditor(final IWorkbenchPage page, final URI uri, final Callback callback)
  {
    Display display = page.getWorkbenchWindow().getShell().getDisplay();
    display.asyncExec(new Runnable()
    {
      public void run()
      {
        try
        {
          IEditorPart editor = findEditor(page, uri);
          if (editor != null)
          {
            callback.modelCreated(editor);
          }
          else
          {
            final URI normalizedURI = SetupUtil.createResourceSet().getURIConverter().normalize(uri);
            IEditorInput editorInput = getEditorInput(normalizedURI, new Callback()
            {
              public void modelCreated(IEditorPart editor)
              {
                postOpen(editor, normalizedURI, callback);
              }
            });

            page.openEditor(editorInput, EDITOR_ID);
          }
        }
        catch (Exception ex)
        {
          SetupUIPlugin.INSTANCE.log(ex);
        }
      }
    });
  }

  private static void postOpen(IEditorPart editor, URI normalizedURI, Callback callback)
  {
    if (editor instanceof IEditingDomainProvider && editor instanceof IViewerProvider && normalizedURI.fragment() != null)
    {
      Viewer viewer = ((IViewerProvider)editor).getViewer();
      ResourceSet resourceSet = ((IEditingDomainProvider)editor).getEditingDomain().getResourceSet();
      EObject object = resourceSet.getEObject(normalizedURI, true);
      if (object != null)
      {
        viewer.setSelection(new StructuredSelection(object));

        Control control = viewer.getControl();
        if (control instanceof Tree)
        {
          Tree tree = (Tree)control;
          ScrollBar scrollBar = tree.getHorizontalBar();
          if (scrollBar != null && !scrollBar.isDisposed() && scrollBar.isVisible())
          {
            scrollBar.setSelection(0);
          }
        }
      }
    }

    if (callback != null)
    {
      callback.modelCreated(editor);
    }
  }

  private static IEditorInput getEditorInput(final URI normalizedURI, Callback callback)
  {
    if ("user".equals(normalizedURI.scheme()))
    {
      URI uri = normalizedURI.replacePrefix(SetupContext.GLOBAL_SETUPS_URI, SetupContext.GLOBAL_SETUPS_LOCATION_URI.appendSegment("")).trimQuery();
      FileEditorInput editorInput = getFileEditorInput(uri, callback);
      if (editorInput != null)
      {
        return editorInput;
      }
    }

    if (normalizedURI.isFile())
    {
      FileEditorInput editorInput = getFileEditorInput(normalizedURI, callback);
      if (editorInput != null)
      {
        return editorInput;
      }
    }

    return new URIEditorInputWithCallback(normalizedURI, normalizedURI.lastSegment(), callback);
  }

  private static FileEditorInput getFileEditorInput(URI uri, Callback callback)
  {
    if (uri.isFile())
    {
      try
      {
        IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(new java.net.URI(uri.toString()));
        for (IFile file : files)
        {
          if (file.isAccessible())
          {
            return new FileEditorInputWithCallback(files[0], callback);
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
   * @author Eike Stepper
   */
  public interface Callback
  {
    public void modelCreated(IEditorPart editor);

    /**
     * @author Eike Stepper
     */
    public interface Provider
    {
      public Callback getCallback();
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class FileEditorInputWithCallback extends FileEditorInput implements Callback.Provider
  {
    private final transient Callback callback;

    public FileEditorInputWithCallback(IFile file, Callback callback)
    {
      super(file);
      this.callback = callback;
    }

    public Callback getCallback()
    {
      return callback;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class URIEditorInputWithCallback extends URIEditorInput implements Callback.Provider
  {
    private final transient Callback callback;

    public URIEditorInputWithCallback(IMemento memento)
    {
      super(memento);
      callback = null;
    }

    public URIEditorInputWithCallback(URI uri, String name, Callback callback)
    {
      super(uri, name);
      this.callback = callback;
    }

    public URIEditorInputWithCallback(URI uri, Callback callback)
    {
      super(uri);
      this.callback = callback;
    }

    public Callback getCallback()
    {
      return callback;
    }

    @Override
    protected String getBundleSymbolicName()
    {
      return SetupUIPlugin.INSTANCE.getSymbolicName();
    }
  }
}
