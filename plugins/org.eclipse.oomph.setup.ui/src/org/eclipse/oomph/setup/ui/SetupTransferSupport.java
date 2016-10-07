/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.base.util.ArchiveResourceFactoryImpl;
import org.eclipse.oomph.internal.ui.OomphDropAdapter;
import org.eclipse.oomph.internal.ui.OomphEditingDomain;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.EMFEditPlugin;
import org.eclipse.emf.edit.command.AbstractOverrideableCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.command.DragAndDropFeedback;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceSetItemProvider;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class SetupTransferSupport
{
  public static final List<? extends OomphTransferDelegate> USER_RESOLVING_DELEGATES;

  static
  {
    List<? extends OomphTransferDelegate> delegates = OomphTransferDelegate.merge(OomphTransferDelegate.DELEGATES,
        new OomphTransferDelegate.FileTransferDelegate()
        {
          @Override
          protected void gather(EditingDomain domain, URI uri)
          {
            super.gather(domain, SetupContext.isUserScheme(uri.scheme()) ? SetupContext.resolve(uri) : uri);
          }
        }, //
        new OomphTransferDelegate.URLTransferDelegate()
        {
          @Override
          protected void gather(EditingDomain domain, URI uri)
          {
            super.gather(domain, SetupContext.isUserScheme(uri.scheme()) ? SetupContext.resolve(uri) : uri);
          }
        });

    USER_RESOLVING_DELEGATES = Collections.unmodifiableList(delegates);
  }

  public static final List<? extends Transfer> USER_RESOLVING_TRANSFERS = OomphTransferDelegate.asTransfers(USER_RESOLVING_DELEGATES);

  public static Transfer[] userResolvingTransfers()
  {
    return USER_RESOLVING_TRANSFERS.toArray(new Transfer[USER_RESOLVING_TRANSFERS.size()]);
  }

  private final List<DropListener> dropListeners = new UniqueEList<SetupTransferSupport.DropListener>();

  private final List<DropTarget> dropTargets = new ArrayList<DropTarget>();

  private final OomphEditingDomain editingDomain;

  private final OomphDropAdapter dropListener;

  private List<Resource> resources = null;

  public SetupTransferSupport()
  {
    ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
    composedAdapterFactory.insertAdapterFactory(new ResourceItemProviderAdapterFactory()
    {
      @Override
      public Adapter createResourceSetAdapter()
      {
        return new ResourceSetItemProvider(this)
        {
          @Override
          protected Command factorAddCommand(EditingDomain domain, CommandParameter commandParameter)
          {
            return createDragAndDropCommand(domain, commandParameter.getOwner(), 0.0F, 0, 0, commandParameter.getCollection());
          }

          @Override
          protected Command createDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation,
              final Collection<?> collection)
          {
            Command result = new AddToResourceCommand(domain, collection);
            if (!result.canExecute())
            {
              result = new LoadResourceCommand(domain, collection);
            }

            return result;
          }
        };
      }
    });

    final BasicCommandStack commandStack = new BasicCommandStack()
    {
      @Override
      protected void handleError(Exception exception)
      {
        // Ignore
      }
    };

    editingDomain = new OomphEditingDomain(composedAdapterFactory, commandStack, null, USER_RESOLVING_DELEGATES)
    {
      @Override
      public Command createCommand(Class<? extends Command> commandClass, CommandParameter commandParameter)
      {
        if (commandClass == CopyCommand.class)
        {
          return new IdentityCommand(commandParameter.getOwner());
        }

        return super.createCommand(commandClass, commandParameter);
      }
    };

    final ResourceSet resourceSet = editingDomain.getResourceSet();
    SetupCoreUtil.configureResourceSet(resourceSet);
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("zip", new ArchiveResourceFactoryImpl());

    commandStack.addCommandStackListener(new CommandStackListener()
    {
      public void commandStackChanged(EventObject event)
      {
        Command mostRecentCommand = commandStack.getMostRecentCommand();
        if (mostRecentCommand != null)
        {
          Set<Resource> droppedResources = new LinkedHashSet<Resource>();
          Collection<?> affectedObjects = mostRecentCommand.getAffectedObjects();
          for (Object object : affectedObjects)
          {
            if (object instanceof Resource)
            {
              Resource resource = (Resource)object;
              droppedResources.add(resource);
            }
          }

          resourceSet.getResources().clear();

          if (resources == null)
          {
            resourcesDropped(droppedResources);
          }
          else
          {
            resources.addAll(droppedResources);
          }
        }
      }
    });

    dropListener = new OomphDropAdapter(editingDomain, null, USER_RESOLVING_DELEGATES);
  }

  public void addControl(Control control)
  {
    int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
    DropTarget dropTarget = new DropTarget(control, dndOperations);
    dropTarget.setTransfer(userResolvingTransfers());
    dropTarget.addDropListener(dropListener);
    dropTargets.add(dropTarget);
  }

  public void excludeControl(Control control)
  {
    int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
    DropTarget dropTarget = new DropTarget(control, dndOperations);
    dropTarget.setTransfer(userResolvingTransfers());
    dropTarget.addDropListener(new DropTargetAdapter()
    {
      @Override
      public void dragEnter(DropTargetEvent event)
      {
        event.detail = DND.DROP_NONE;
      }
    });

    dropTargets.add(dropTarget);
  }

  public void removeControls()
  {
    for (DropTarget dropTarget : dropTargets)
    {
      dropTarget.dispose();
    }

    dropTargets.clear();
  }

  public Collection<? extends Resource> getResources()
  {
    try
    {
      resources = new ArrayList<Resource>();
      Command command = editingDomain.createCommand(PasteFromClipboardCommand.class, new CommandParameter(editingDomain.getResourceSet()));
      editingDomain.getCommandStack().execute(command);
      return resources;
    }
    catch (RuntimeException exception)
    {
      return resources;
    }
    finally
    {
      resources = null;
    }
  }

  public void addDropListener(DropListener dropListener)
  {
    dropListeners.add(dropListener);
  }

  public void removeDropListener(DropListener dropListener)
  {
    dropListeners.remove(dropListener);
  }

  public void resourcesDropped(Collection<? extends Resource> resources)
  {
    for (DropListener dropListener : dropListeners)
    {
      dropListener.resourcesDropped(resources);
    }
  }

  public interface DropListener
  {
    public void resourcesDropped(Collection<? extends Resource> resources);
  }
}

abstract class ResourceCommand extends AbstractOverrideableCommand implements AbstractCommand.NonDirtying, DragAndDropFeedback
{
  protected Collection<?> collection;

  protected ResourceCommand(EditingDomain domain, Collection<?> collection)
  {
    super(domain);
    this.collection = collection == null ? Collections.emptyList() : collection;
  }

  protected List<Resource> resources;

  @Override
  protected abstract boolean prepare();

  @Override
  public abstract void doExecute();

  @Override
  public void doUndo()
  {
    domain.getResourceSet().getResources().removeAll(resources);
    resources = null;
  }

  @Override
  public void doRedo()
  {
    doExecute();
  }

  @Override
  public Collection<?> doGetAffectedObjects()
  {
    return resources == null ? Collections.singleton(domain.getResourceSet()) : resources;
  }

  @Override
  public String doGetDescription()
  {
    return EMFEditPlugin.INSTANCE.getString("_UI_LoadResources_description");
  }

  @Override
  public String doGetLabel()
  {
    return EMFEditPlugin.INSTANCE.getString("_UI_LoadResources_label");
  }

  public boolean validate(Object owner, float location, int operations, int operation, Collection<?> collection)
  {
    return false;
  }

  public int getFeedback()
  {
    return 0;
  }

  public int getOperation()
  {
    return DROP_COPY;
  }
}

class AddToResourceCommand extends ResourceCommand
{
  protected AddToResourceCommand(EditingDomain domain, Collection<?> collection)
  {
    super(domain, collection);
  }

  @Override
  protected boolean prepare()
  {
    for (Object object : collection)
    {
      if (object instanceof EObject)
      {
        EObject eObject = (EObject)object;
        if (eObject.eContainer() == null)
        {
          continue;
        }
      }

      return false;
    }

    return true;
  }

  @Override
  public void doExecute()
  {
    resources = new ArrayList<Resource>();
    ResourceSet resourceSet = domain.getResourceSet();
    for (Object object : collection)
    {
      EObject eObject = (EObject)object;
      Resource originalResource = eObject.eResource();
      URI uri = originalResource == null ? URI.createURI("dummy:/*.xmi") : originalResource.getURI();
      Resource resource = resourceSet.getResource(uri, false);
      if (resource == null)
      {
        resource = resourceSet.createResource(uri);
      }

      EcoreUtil.Copier copier = new EcoreUtil.Copier(false, true);
      EObject copy = copier.copy(eObject);
      copier.copyReferences();
      resource.getContents().add(copy);
      resources.add(resource);
    }
  }
}

class LoadResourceCommand extends ResourceCommand
{
  protected LoadResourceCommand(EditingDomain domain, Collection<?> collection)
  {
    super(domain, collection);
  }

  @Override
  protected boolean prepare()
  {
    for (Object object : collection)
    {
      if (!(object instanceof URI))
      {
        return false;
      }

      URI uri = (URI)object;
      String fileExtension = uri.fileExtension();
      if (!"setup".equals(fileExtension) && !"zip".equals(fileExtension))
      {
        return false;
      }
    }

    return true;
  }

  @Override
  public void doExecute()
  {
    resources = new ArrayList<Resource>();
    ResourceSet resourceSet = domain.getResourceSet();
    for (Object object : collection)
    {
      URI uri = (URI)object;
      Resource resource = resourceSet.getResource(uri, false);
      if (resource == null)
      {
        resource = resourceSet.createResource(uri);
      }

      resources.add(resource);
    }
  }
}
