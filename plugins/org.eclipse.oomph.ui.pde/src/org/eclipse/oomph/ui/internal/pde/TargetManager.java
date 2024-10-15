/*
 * Copyright (c) 2024 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui.internal.pde;

import org.eclipse.oomph.ui.internal.pde.TargetManager.Event.Kind;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.edit.provider.IDisposable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.pde.core.target.TargetEvents;

import org.osgi.service.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author Eike Stepper
 */
public final class TargetManager implements IDisposable
{
  private static final Target[] NO_TARGETS = {};

  private static final ThreadLocal<List<Event>> EVENTS = new InheritableThreadLocal<>();

  private static Method TARGET_DEFINITION_WRITE_METHOD = getTargetDefinitionMethod("write", OutputStream.class); //$NON-NLS-1$

  private static Method TARGET_DEFINITION_SET_CONTENTS_METHOD = getTargetDefinitionMethod("setContents", InputStream.class); //$NON-NLS-1$

  private final EventHandler targetPlatformEventHandler = event -> {
    String topic = event.getTopic();
    Object data = event.getProperty(IEventBroker.DATA);

    if (TargetEvents.TOPIC_TARGET_SAVED.equals(topic))
    {
      targetSaved((ITargetHandle)data, false);
    }
    else if (TargetEvents.TOPIC_TARGET_DELETED.equals(topic))
    {
      targetDeleted((ITargetHandle)data);
    }
    else if (TargetEvents.TOPIC_WORKSPACE_TARGET_CHANGED.equals(topic))
    {
      ITargetHandle handle = ((ITargetDefinition)data).getHandle();
      activeTargetChanged(handle);
    }
  };

  private final List<Listener> listeners = new CopyOnWriteArrayList<>();

  private final Map<ITargetHandle, Target> targets = new HashMap<>();

  private Target[] sortedTargets = NO_TARGETS;

  private Target activeTarget;

  private Target monitoredTarget;

  private String defaultTargetName;

  private boolean autoSnapshot;

  public TargetManager()
  {
    try
    {
      TargetPlatformUtil.runWithTargetPlatformService(service -> {
        for (ITargetHandle handle : service.getTargets(new NullProgressMonitor()))
        {
          ITargetDefinition definition = handle.getTargetDefinition();
          targets.put(handle, new Target(TargetManager.this, handle, definition.getName()));
        }

        sortedTargets = sortTargets(targets);

        ITargetHandle handle = service.getWorkspaceTargetHandle();
        activeTarget = targets.get(handle);

        ITargetDefinition defaultTarget = service.newDefaultTarget();
        defaultTargetName = defaultTarget.getName();

        return null;
      });
    }
    catch (CoreException ex)
    {
      throw new WrappedException(ex);
    }

    withEventBroker(broker -> {
      broker.subscribe(TargetEvents.TOPIC_TARGET_SAVED, targetPlatformEventHandler);
      broker.subscribe(TargetEvents.TOPIC_TARGET_DELETED, targetPlatformEventHandler);
      broker.subscribe(TargetEvents.TOPIC_WORKSPACE_TARGET_CHANGED, targetPlatformEventHandler);
    });
  }

  @Override
  public void dispose()
  {
    listeners.clear();
    withEventBroker(broker -> broker.unsubscribe(targetPlatformEventHandler));
  }

  public void addListener(Listener listener)
  {
    listeners.add(listener);
  }

  public void removeListener(Listener listener)
  {
    listeners.remove(listener);
  }

  public boolean isAutoSnapshot()
  {
    return autoSnapshot;
  }

  public void setAutoSnapshot(boolean autoSnapshot)
  {
    if (this.autoSnapshot != autoSnapshot)
    {
      this.autoSnapshot = autoSnapshot;

      if (autoSnapshot)
      {
        Target target = monitoredTarget;
        if (target != null && target.getCurrentSnapshot() == null)
        {
          targetSaved(target.getHandle(), false);
        }
      }
    }
  }

  public Target getTarget(ITargetHandle handle)
  {
    synchronized (targets)
    {
      return targets.get(handle);
    }
  }

  public Target[] getTargets()
  {
    return sortedTargets;
  }

  public Target getActiveTarget()
  {
    return activeTarget;
  }

  public Target getMonitoredTarget()
  {
    return monitoredTarget;
  }

  boolean monitorTarget(Target target)
  {
    return modify(events -> {
      if (target != monitoredTarget)
      {
        monitoredTarget = target;
        events.accept(Kind.MonitoredTargetChanged, monitoredTarget);

        if (monitoredTarget != null && monitoredTarget.getSnapshots().length == 0)
        {
          ITargetHandle handle = target.getHandle();
          targetSaved(handle, false);
        }
      }
    });
  }

  String getDefaultTargetName()
  {
    return defaultTargetName;
  }

  void deleteSnapshots(Target target, Collection<TargetSnapshot> snapshots)
  {
    modify(events -> {
      target.internalDeleteSnapshots(snapshots);

      for (TargetSnapshot snapshot : snapshots)
      {
        events.accept(Kind.TargetSnapshotRemoved, snapshot);
      }
    });
  }

  void snapshotResolutionStarted(TargetSnapshot snapshot, ITargetDefinition definition)
  {
    modify(events -> {
      snapshot.setResolving();
      events.accept(Kind.TargetSnapshotResolutionStarted, snapshot);
    });
  }

  void snapshotResolutionFinished(TargetSnapshot snapshot, ITargetDefinition definition)
  {
    modify(events -> {
      snapshot.doUpdate(definition);
      events.accept(Kind.TargetSnapshotResolutionFinished, snapshot);
    });
  }

  boolean restoreSnapshot(TargetSnapshot snapshot)
  {
    String xml = snapshot.getXML();
    if (xml != null)
    {
      try
      {
        TargetPlatformUtil.runWithTargetPlatformService(service -> {
          ITargetDefinition definition = snapshot.getTarget().getHandle().getTargetDefinition();

          if (setXML(definition, xml))
          {
            service.saveTargetDefinition(definition);
            return true;
          }

          return false;
        });
      }
      catch (Exception ex)
      {
        UIPDEPlugin.INSTANCE.log(ex);
      }
    }

    return false;
  }

  TargetSnapshot targetSaved(ITargetHandle handle, boolean forced)
  {
    TargetSnapshot[] result = { null };

    ITargetDefinition definition = getDefinition(handle);
    if (definition != null)
    {
      String name = definition.getName();
      String xml = getXML(definition);

      modify(events -> {
        Target target = targets.get(handle);
        if (target == null)
        {
          target = new Target(this, handle, name);
          targets.put(handle, target);
          sortedTargets = sortTargets(targets);
          events.accept(Kind.TargetAdded, target);
        }
        else if (!Objects.equals(target.getName(), name))
        {
          target.setName(name);
          events.accept(Kind.TargetNameChanged, target);
        }

        if (forced || autoSnapshot)
        {
          result[0] = target.internalTakeSnapshot(xml);
          events.accept(Kind.TargetSnapshotAdded, result[0]);

          if (result[0].update(definition))
          {
            events.accept(Kind.TargetSnapshotResolutionFinished, result[0]);
          }
        }
      });
    }

    return result[0];
  }

  private void targetDeleted(ITargetHandle handle)
  {
    modify(events -> {
      Target target = targets.remove(handle);
      if (target != null)
      {
        sortedTargets = sortTargets(targets);
        events.accept(Kind.TargetRemoved, target);

        if (target == monitoredTarget)
        {
          monitoredTarget = null;
          events.accept(Kind.MonitoredTargetChanged, this);
        }
      }
    });
  }

  private void activeTargetChanged(ITargetHandle handle)
  {
    modify(events -> {
      Target target = targets.get(handle);
      if (target != activeTarget)
      {
        activeTarget = target;
        events.accept(Kind.ActiveTargetChanged, this);
      }
    });
  }

  private boolean modify(Modifier modifier)
  {
    List<Event> events = EVENTS.get();

    boolean outermost = events == null;
    if (outermost)
    {
      events = new ArrayList<>();
      EVENTS.set(events);
    }

    int initialEventsSize = events.size();

    try
    {
      List<Event> eventList = events;

      synchronized (targets)
      {
        modifier.run((kind, subject) -> eventList.add(new Event(kind, subject)));
      }

      if (outermost)
      {
        if (!eventList.isEmpty())
        {
          for (Listener listener : listeners)
          {
            try
            {
              listener.handleTargetManagerEvents(eventList);
            }
            catch (Exception ex)
            {
              UIPDEPlugin.INSTANCE.log(ex);
            }
          }
        }
      }
    }
    finally
    {
      if (outermost)
      {
        EVENTS.remove();
      }
    }

    return events.size() > initialEventsSize;
  }

  private static ITargetDefinition getDefinition(ITargetHandle handle)
  {
    try
    {
      return handle.getTargetDefinition();
    }
    catch (Exception ex)
    {
      UIPDEPlugin.INSTANCE.log(ex);
      return null;
    }
  }

  private static String getXML(ITargetDefinition definition)
  {
    if (TARGET_DEFINITION_WRITE_METHOD != null)
    {
      try
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ReflectUtil.invokeMethod(TARGET_DEFINITION_WRITE_METHOD, definition, baos);

        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
      }
      catch (Exception ex)
      {
        UIPDEPlugin.INSTANCE.log(ex);
      }
    }

    return null;
  }

  private static boolean setXML(ITargetDefinition definition, String xml)
  {
    if (TARGET_DEFINITION_SET_CONTENTS_METHOD != null)
    {
      try
      {
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        ReflectUtil.invokeMethod(TARGET_DEFINITION_SET_CONTENTS_METHOD, definition, bais);
        return true;
      }
      catch (Exception ex)
      {
        UIPDEPlugin.INSTANCE.log(ex);
      }
    }

    return false;
  }

  private static void withEventBroker(Consumer<IEventBroker> consumer)
  {
    IEclipseContext context = EclipseContextFactory.getServiceContext(UIPDEPlugin.INSTANCE.getBundleContext());
    IEventBroker broker = context.get(IEventBroker.class);
    if (broker != null)
    {
      consumer.accept(broker);
    }
  }

  private static Target[] sortTargets(Map<ITargetHandle, Target> targets)
  {
    Target[] sortedTargets = targets.values().toArray(new Target[targets.size()]);
    Arrays.sort(sortedTargets);
    return sortedTargets;
  }

  @SuppressWarnings("restriction")
  private static Method getTargetDefinitionMethod(String methodName, Class<?>... parameterTypes)
  {
    try
    {
      return ReflectUtil.getMethod(org.eclipse.pde.internal.core.target.TargetDefinition.class, methodName, parameterTypes);
    }
    catch (Throwable ex)
    {
      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  @FunctionalInterface
  private interface Modifier
  {
    public void run(BiConsumer<Kind, Object> events);
  }

  /**
   * @author Eike Stepper
   */
  public static final class Event
  {
    private final Kind kind;

    private final Object subject;

    Event(Kind kind, Object subject)
    {
      this.kind = kind;
      this.subject = subject;
    }

    public Kind getKind()
    {
      return kind;
    }

    public TargetManager getManager()
    {
      if (subject instanceof TargetManager)
      {
        return (TargetManager)subject;
      }

      if (subject instanceof Target)
      {
        return ((Target)subject).getManager();
      }

      if (subject instanceof TargetSnapshot)
      {
        return ((TargetSnapshot)subject).getTarget().getManager();
      }

      return null;
    }

    public Target getTarget()
    {
      if (subject instanceof Target)
      {
        return (Target)subject;
      }

      if (subject instanceof TargetSnapshot)
      {
        return ((TargetSnapshot)subject).getTarget();
      }

      return null;
    }

    public TargetSnapshot getSnapshot()
    {
      if (subject instanceof TargetSnapshot)
      {
        return (TargetSnapshot)subject;
      }

      return null;
    }

    @Override
    public String toString()
    {
      return kind + "[" + subject + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
    * @author Eike Stepper
    */
    public enum Kind
    {
      TargetAdded, //
      TargetRemoved, //
      TargetNameChanged, //
      ActiveTargetChanged, //
      MonitoredTargetChanged, //
      TargetSnapshotAdded, //
      TargetSnapshotRemoved, //
      TargetSnapshotResolutionStarted, //
      TargetSnapshotResolutionFinished;
    }
  }

  /**
   * @author Eike Stepper
   */
  @FunctionalInterface
  public interface Listener
  {
    public void handleTargetManagerEvents(List<Event> events);
  }
}
