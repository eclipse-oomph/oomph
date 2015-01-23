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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.core.AgentManagerElement;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public abstract class PersistentMap<E>
{
  private final File file;

  private final File tempFile;

  private final File lockFile;

  private final Map<String, E> elements = new HashMap<String, E>();

  public PersistentMap(File file)
  {
    this.file = file;

    if (file != null)
    {
      File folder = file.getParentFile();
      folder.mkdirs();

      String name = file.getName();
      tempFile = new File(folder, name + ".temp");
      lockFile = new File(folder, name + ".lock");
    }
    else
    {
      tempFile = null;
      lockFile = null;
    }
  }

  public final void load()
  {
    final boolean[] needsSave = { false };

    if (file != null && file.exists())
    {
      FileWriter lock = lock();

      try
      {
        load(new KeyHandler()
        {
          public void handleKey(String key, String extraInfo) throws Exception
          {
            E element = createElement(key, extraInfo);
            if (element instanceof AgentManagerElement)
            {
              AgentManagerElement agentManagerElement = (AgentManagerElement)element;
              if (!agentManagerElement.isValid())
              {
                needsSave[0] = true;
                return;
              }
            }

            elements.put(key, element);
          }
        });
      }
      finally
      {
        unlock(lock);
      }
    }
    else
    {
      initializeFirstTime();
      needsSave[0] = true;
    }

    if (needsSave[0])
    {
      save(null, null);
    }
  }

  public final File getFile()
  {
    return file;
  }

  public final synchronized Set<String> getElementKeys()
  {
    return new HashSet<String>(elements.keySet());
  }

  public final synchronized Collection<E> getElements()
  {
    return new ArrayList<E>(elements.values());
  }

  public final synchronized E getElement(String key)
  {
    return elements.get(key);
  }

  public final synchronized boolean containsElement(String key)
  {
    return elements.containsKey(key);
  }

  public final synchronized E addElement(String key, String extraInfo)
  {
    E element = elements.get(key);
    if (element == null)
    {
      element = createElement(key, extraInfo);
      if (element != null)
      {
        elements.put(key, element);
        save(key, null);
      }
    }

    return element;
  }

  public final synchronized void removeElement(String key)
  {
    E element = elements.remove(key);
    if (element != null)
    {
      save(null, key);
    }
  }

  public final synchronized boolean refresh()
  {
    return reconcile(null, null);
  }

  /**
   * @return the new element or <code>null</code> if no element should be created.
   */
  protected abstract E createElement(String key, String extraInfo);

  protected void initializeFirstTime()
  {
  }

  private void load(KeyHandler handler)
  {
    FileReader infoReader = null;

    try
    {
      infoReader = new FileReader(file);
      BufferedReader bufferedReader = new BufferedReader(infoReader);

      String line;
      while ((line = bufferedReader.readLine()) != null)
      {
        try
        {
          String key;
          String extraInfo;

          int pos = line.indexOf('|');
          if (pos == -1)
          {
            key = line;
            extraInfo = null;
          }
          else
          {
            key = line.substring(0, pos);
            extraInfo = line.substring(pos + 1);
          }

          handler.handleKey(key, extraInfo);
        }
        catch (Exception ex)
        {
          P2CorePlugin.INSTANCE.log(ex);
        }
      }

      IOUtil.close(bufferedReader);
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
    finally
    {
      IOUtil.closeSilent(infoReader);
    }
  }

  private void save(String addedKey, final String removedKey)
  {
    if (file != null)
    {
      FileWriter lock = lock();
      FileWriter tempWriter = null;

      try
      {
        if (addedKey != null || removedKey != null)
        {
          if (file.exists())
          {
            reconcile(addedKey, removedKey);
          }
        }

        tempWriter = new FileWriter(tempFile);
        BufferedWriter bufferedWriter = new BufferedWriter(tempWriter);

        List<String> sortedKeys = new ArrayList<String>(elements.keySet());
        Collections.sort(sortedKeys);

        for (String key : sortedKeys)
        {
          bufferedWriter.write(key);

          E element = elements.get(key);
          if (element instanceof ExtraInfoProvider)
          {
            String extraInfo = ((ExtraInfoProvider)element).getExtraInfo();

            bufferedWriter.write('|');
            bufferedWriter.write(extraInfo);
          }

          bufferedWriter.write(StringUtil.NL);
        }

        bufferedWriter.flush();
        IOUtil.closeSilent(tempWriter);
        tempWriter = null;

        file.delete();
        tempFile.renameTo(file);
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
      finally
      {
        IOUtil.closeSilent(tempWriter);
        unlock(lock);
      }
    }
  }

  private boolean reconcile(String addedKey, final String removedKey)
  {
    final boolean[] changed = { false };

    final Set<String> fileKeys = new HashSet<String>();
    fileKeys.add(addedKey);

    load(new KeyHandler()
    {
      public void handleKey(String key, String extraInfo) throws Exception
      {
        fileKeys.add(key);

        if (!key.equals(removedKey) && !elements.containsKey(key))
        {
          E element = createElement(key, extraInfo);
          if (element != null)
          {
            elements.put(key, element);
            changed[0] = true;
          }
        }
      }
    });

    for (Iterator<String> it = elements.keySet().iterator(); it.hasNext();)
    {
      String key = it.next();
      if (!fileKeys.contains(key))
      {
        it.remove();
        changed[0] = true;
      }
    }

    return changed[0];
  }

  private FileWriter lock()
  {
    long start = System.currentTimeMillis();

    for (;;)
    {
      try
      {
        return new FileWriter(lockFile);
      }
      catch (IOException ex)
      {
        if (System.currentTimeMillis() - start >= 1000L)
        {
          throw new RuntimeException("Acquisition of lock file " + lockFile + " timed out", ex);
        }
      }

      try
      {
        Thread.sleep(10L);
      }
      catch (InterruptedException ex)
      {
        throw new RuntimeException("Acquisition of lock file " + lockFile + " interrupted", ex);
      }
    }
  }

  private void unlock(FileWriter lock)
  {
    try
    {
      IOUtil.close(lock);
    }
    catch (Exception ex)
    {
      P2CorePlugin.INSTANCE.log(ex);
    }

    try
    {
      lockFile.delete();
    }
    catch (Exception ex)
    {
      // Ignore
    }
  }

  /**
   * @author Eike Stepper
   */
  private interface KeyHandler
  {
    public void handleKey(String key, String extraInfo) throws Exception;
  }

  /**
   * @author Eike Stepper
   */
  public interface ExtraInfoProvider
  {
    public String getExtraInfo();
  }
}
