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
package org.eclipse.oomph.setup.doc.user.wizard;

import org.eclipse.oomph.setup.doc.concepts.DocTask.DocVariableTask;
import org.eclipse.oomph.setup.doc.concepts.DocTaskComposition;
import org.eclipse.oomph.setup.internal.installer.InstallerDialog;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * Variables Page
 * <p>
 * The primary purpose of the variables page is to specify the values for {@link DocVariableTask variables}.
 * At this point in the wizard's work flow,
 * a task list has been {@link DocTaskComposition gathered}.
 * That process induces variables and evaluates and expands variables.
 * Any variable with an empty value requires the user's input.
 * All variables must specify a non-empty value in order to advance to the {@link DocConfirmationPage confirmation page}.
 * Information related to those variables is displayed on this page in a three column format:
 * {@link #promptPage()}
 * </p>
 *
 * @number 600
 */
public class DocVariablePage
{
  /**
   * @snippet image PromptPage.images
   * @style box
   * @description
   * The page has the following controls:
   * @callout
   * Displays the label for the rule that determines where installations are installed.
   * @callout
   * Displays the value for the installation location rule.
   * In this case, each installation will end up in a uniquely named subfolder of the specified root folder.
   * @callout
   * Displays the label for the uniquely named subfolder for the installation.
   * @callout
   * Displays the value for the subfolder name.
   * @callout
   * Displays the label for the root folder for all installations.
   * @callout
   * Displays the value for the root folder.
   * In this case the installation will end up in the 'D:/sandbox/oomph' folder.
   * @callout
   * Browses the file system for a root folder location.
   * @callout
   * Displays the label for the rule that determines where workspaces are provisioned.
   * @callout
   * Displays the value of the workspace location rule.
   * In this case, the workspace will be located in a folder named 'ws' nested in the installation folder.
   * @callout
   * Displays the label for the rule that determines where Git clones will be provisioned.
   * @callout
   * Displays the value of the git location rule.
   * In this case the clone is stored in the "git" subfolder of the installation folder with a name derived from the repository URI.
   * @callout
   * Displays the label for the target platform choice.
   * @callout
   * Displays the value for the target platform.
   * Project authors are encouraged to make use of this common variable so that multiple projects will materialize a cohesive target platform.
   * @callout
   * Displays the label for the choice of Oomph's Git remote URI.
   * There are typically several different URIs for accessing the same underlying repository
   * depending on whether one wants Gerrit access or direct Git access,
   * whether ones wants to use SSH, HTTPS, or anonymous access.
   * @callout
   * Displays the value for the remote URI choice.
   * In this case, Gerrit access via SSH is chosen.
   * @callout
   * Displays the label for the JRE location.
   * Standard variables are defined for various levels of the JDK.
   * @callout
   * Displays the value for the JRE location.
   * The value specicified should be compatible with the verion specified in the label.
   * Generally a JDK is preferred over a JRE.
   * In this case, a Java 1.7 JDK is specified.
   * @callout
   * Browses the file system for a JRE or JDK location.
   * @callout
   * Displays the label for the Bugzilla/Hudson ID.
   * @callout
   * Displays the value for the ID.
   * This is generally an email address.
   * If one doesn't have such a registered ID, 'anonymous' should be specified.
   * @callout
   * Displays the label for the Eclipse password.
   * @callout
   * Displays the obscurred value of the password.
   * @callout
   * Authenticates that the password is valid with respect to the Bugzilla/Hudson ID and the Git/ID.
   * @callout
   * Displays the label for the Git/Gerrit user ID.
   * @callout
   * Displays the value of the ID.
   * If one doesn't have such a registered ID, 'anonymous' should be specified.
   * @callout
   * Determines whether all variables are displayed
   * or just the ones that are strictly required to proceed.
   * In this case, all variables are being shown.
   */
  public static Image[] promptPage()
  {
    DocVariablePage.CapturePromptPage instance = CapturePromptPage.getInstance();
    return new Image[] { instance.progressPage, instance.installationRuleLabel, instance.installationRuleControl, instance.installationIDLabel,
        instance.installationIDControl, instance.installationRootLabel, instance.installationRootControl, instance.installationRootHelper,
        instance.workspaceRuleLabel, instance.workspaceRuleControl, instance.gitCloneRuleLabel, instance.gitCloneRuleControl, instance.targetPlatformLabel,
        instance.targetPlatformControl, instance.oomphRemoteURILabel, instance.oomphRemoteURIControl, instance.jreLocationLabel, instance.jreLocationControl,
        instance.jreLocationHelper, instance.bugzillaLabel, instance.bugzillaControl, instance.passwordLabel, instance.passwordControl,
        instance.passwordHelper, instance.userIDLabel, instance.userIDControl, instance.showAll };
  }

  /**
   * @ignore
   */
  public static class CapturePromptPage extends CaptureSetupWizard
  {
    private static DocVariablePage.CapturePromptPage instance;

    private Image progressPage;

    private Image showAll;

    private Image installationIDLabel;

    private Image installationIDControl;

    private Image gitCloneRuleLabel;

    private Image gitCloneRuleControl;

    private Image installationRuleLabel;

    private Image installationRuleControl;

    private Image installationRootLabel;

    private Image installationRootControl;

    private Image workspaceRuleLabel;

    private Image workspaceRuleControl;

    private Image installationRootHelper;

    private Image targetPlatformLabel;

    private Image targetPlatformControl;

    private Image oomphRemoteURILabel;

    private Image oomphRemoteURIControl;

    private Image jreLocationLabel;

    private Image jreLocationControl;

    private Image jreLocationHelper;

    private Image bugzillaLabel;

    private Image bugzillaControl;

    private Image passwordLabel;

    private Image passwordControl;

    private Image passwordHelper;

    private Image userIDLabel;

    private Image userIDControl;

    public static DocVariablePage.CapturePromptPage getInstance()
    {
      if (instance == null)
      {
        instance = new CapturePromptPage();
        instance.progressPage = instance.capture();
      }
      return instance;
    }

    @Override
    protected WizardDialog create(Shell shell)
    {
      return new InstallerDialog(shell, false);
    }

    @Override
    protected void postProcess(WizardDialog wizardDialog)
    {
      super.postProcess(wizardDialog);

      postProcessProductPage(wizardDialog);

      advanceToNextPage(wizardDialog);

      postProcessProjectPage(wizardDialog);

      advanceToNextPage(wizardDialog);

      postProcessVariablePage(wizardDialog, "oomph");
    }

    @Override
    protected Image capture(WizardDialog wizardDialog)
    {
      IWizardPage page = wizardDialog.getCurrentPage();

      Image result = capture(page, null);

      installationRuleLabel = getImage(wizardDialog, "InstallationTask.label");
      installationRuleControl = getImage(wizardDialog, "InstallationTask.control");

      installationIDLabel = getImage(wizardDialog, "installation.id.label");
      installationIDControl = getImage(wizardDialog, "installation.id.control");

      installationRootLabel = getImage(wizardDialog, "install.root.label");
      installationRootControl = getImage(wizardDialog, "install.root.control");
      installationRootHelper = getImage(wizardDialog, "install.root.helper");

      workspaceRuleLabel = getImage(wizardDialog, "WorkspaceTask.label");
      workspaceRuleControl = getImage(wizardDialog, "WorkspaceTask.control");

      gitCloneRuleLabel = getImage(wizardDialog, "GitCloneTask.label");
      gitCloneRuleControl = getImage(wizardDialog, "GitCloneTask.control");

      targetPlatformLabel = getImage(wizardDialog, "eclipse.target.platform.label");
      targetPlatformControl = getImage(wizardDialog, "eclipse.target.platform.control");

      oomphRemoteURILabel = getImage(wizardDialog, "git.clone.oomph.remoteURI.label");
      oomphRemoteURIControl = getImage(wizardDialog, "git.clone.oomph.remoteURI.control");

      jreLocationLabel = getImage(wizardDialog, "jre.location-1.5.label");
      jreLocationControl = getImage(wizardDialog, "jre.location-1.5.control");
      jreLocationHelper = getImage(wizardDialog, "jre.location-1.5.helper");

      bugzillaLabel = getImage(wizardDialog, "bugzilla.id.label");
      bugzillaControl = getImage(wizardDialog, "bugzilla.id.control");

      passwordLabel = getImage(wizardDialog, "eclipse.user.password.label");
      passwordControl = getImage(wizardDialog, "eclipse.user.password.control");
      passwordHelper = getImage(wizardDialog, "eclipse.user.password.helper");

      userIDLabel = getImage(wizardDialog, "git.user.id.label");
      userIDControl = getImage(wizardDialog, "git.user.id.control");

      showAll = getImage(wizardDialog, "showAll");

      return result;
    }
  }
}
