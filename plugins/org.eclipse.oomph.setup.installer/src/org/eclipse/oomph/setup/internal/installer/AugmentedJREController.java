/*
 * Copyright (c) 2020 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.jreinfo.JRE;
import org.eclipse.oomph.jreinfo.JREFilter;
import org.eclipse.oomph.jreinfo.ui.JREController;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.util.Request.Handler;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This augments the JRE controller to provide choices of JREs installed from a p2 update site.
 *
 * @author Ed Merks
 */
public class AugmentedJREController extends JREController
{
  private static Set<Trigger> BOOSTRAP_ONLY_TRIGGER = new LinkedHashSet<Trigger>(Arrays.asList(new Trigger[] { Trigger.STARTUP, Trigger.MANUAL }));

  public AugmentedJREController(Label label, StructuredViewer viewer, Handler downloadHandler)
  {
    super(label, viewer, downloadHandler);
  }

  @Override
  protected JREFilter createJREFilter()
  {
    // Creates a filter that permits descriptor-based JREs.
    return new JREFilter(getJavaVersion(), getBitness(), null, null);
  }

  /**
   * Copies the setup task from the descriptor to the installation, marking it with an annotation so it can be changed or removed.
   */
  protected void updateSetupContext(SetupContext setupContext, JRE jre)
  {
    Installation installation = setupContext.getInstallation();

    // Remove any JRE-related task that may have been added earlier perhaps for a different JRE.
    EList<SetupTask> setupTasks = installation.getSetupTasks();
    for (SetupTask setupTask : setupTasks)
    {
      if (setupTask.getAnnotation(AnnotationConstants.ANNOTATION_JRE) != null)
      {
        EcoreUtil.remove(setupTask);
        break;
      }
    }

    if (jre != null)
    {
      JRE.Descriptor jreDescriptor = jre.getDescriptor();
      if (jreDescriptor != null)
      {
        CompoundTask compoundTask = SetupFactory.eINSTANCE.createCompoundTask();
        compoundTask.setName(jreDescriptor.getLabel());
        EList<SetupTask> additionalSetupTasks = compoundTask.getSetupTasks();

        // Mark it so it can be removed above.
        Annotation annotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_JRE);
        compoundTask.getAnnotations().add(annotation);

        SetupTask setupTask = (SetupTask)jreDescriptor.getData();
        SetupTask setupTaskCopy = EcoreUtil.copy(setupTask);
        setupTaskCopy.setExcludedTriggers(BOOSTRAP_ONLY_TRIGGER);
        Annotation jreSpecificTasks = setupTaskCopy.getAnnotation(AnnotationConstants.ANNOTATION_JRE_SPECIFIC_TASKS);
        if (jreSpecificTasks != null)
        {
          setupTaskCopy.getAnnotations().remove(jreSpecificTasks);
          for (EObject eObject : new ArrayList<EObject>(jreSpecificTasks.getContents()))
          {
            if (eObject instanceof SetupTask)
            {
              SetupTask additionalTask = (SetupTask)eObject;
              additionalTask.setExcludedTriggers(BOOSTRAP_ONLY_TRIGGER);
              additionalSetupTasks.add(additionalTask);
            }
          }
        }

        additionalSetupTasks.add(0, setupTaskCopy);
        setupTasks.add(0, compoundTask);
      }
    }
  }

  @Override
  protected JRE getDefaultJRE(String javaVersion)
  {
    // Because descriptor based JREs can't be set as the default in the JREManager.INSTANCE,
    // we should return the one that was set previously if it matches the filter.
    JRE jre = getJRE();
    if (jre != null && jre.isMatch(createJREFilter()))
    {
      return jre;
    }

    return super.getDefaultJRE(javaVersion);
  }
}
