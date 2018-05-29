/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
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
   * Displays the installation location rule that determines where installations are installed.
   * <br/>{@link #promptPageExtras() @1}<br/>
   * In this case, each installation will end up in a uniquely named subfolder of the specified root folder.
   * @callout
   * Displays the uniquely named subfolder for the installation.
   * <br/>{@link #promptPageExtras() @2}<br/>
   * This may be a relative path within the root folder.
   * @callout
   * Displays the root folder for all installations.
   * <br/>{@link #promptPageExtras() @3 @4}<br/>
   * In this case the installation will end up in the 'D:/sandbox/oomph' folder.
   * @callout
   * Displays the workspace location rule that determines where workspaces are provisioned.
   * <br/>{@link #promptPageExtras() @5}<br/>
   * In this case, the workspace will be located in a folder named 'ws' nested in the installation folder.
   * @callout
   * Displays the Git location rule that determines where Git clones are provisioned.
   * <br/>{@link #promptPageExtras() @6}<br/>
   * In this case the clone is stored in the "git" subfolder of the installation folder with a name derived from the repository URI.
   * @callout
   * Displays the target platform choice.
   * <br/>{@link #promptPageExtras() @7}<br/>
   * Project authors are encouraged to make use of this common variable so that multiple projects will materialize a cohesive target platform.
   * @callout
   * Displays the choice of Oomph's Git remote URI.
   * <br/>{@link #promptPageExtras() @8}<br/>
   * There are typically several different URIs for accessing the same underlying repository
   * depending on whether one wants Gerrit access or direct Git access,
   * and whether ones wants to use SSH, HTTPS, or anonymous access.
   * In this case, Gerrit access via SSH is chosen.
   * @callout
   * Displays the JRE 1.5 location.
   * <br/>{@link #promptPageExtras() @9 @10}<br/>
   * Standard variables are defined for various levels of the JDK.
   * The value specified JRE should be compatible with the version specified in the label.
   * Generally a JDK is preferred over a JRE.
   * In this case, a Java 1.7 JDK is specified.
   * @callout
   * Displays the Bugzilla/Hudson ID.
   * <br/>{@link #promptPageExtras() @11}<br/>
   * This is generally an email address.
   * If one doesn't have such a registered ID, 'anonymous' should be specified.
   * @callout
   * Displays the obscured Eclipse password.
   * <br/>{@link #promptPageExtras() @12 @13}<br/>
   * Authenticates that the password is valid with respect to the Bugzilla/Hudson ID and the Git/ID.
   * @callout
   * Displays the Git/Gerrit user ID.
   * <br/>{@link #promptPageExtras() @14}<br/>
   * If one doesn't have such a registered ID, 'anonymous' should be specified.
   * @callout
   * Determines whether all variables are displayed
   * or just the ones that are strictly required to proceed.
   * In this case, all variables are being shown.
   */
  public static Image[] promptPage()
  {
    DocVariablePage.CapturePromptPage instance = CapturePromptPage.getInstance();
    return new Image[] { instance.progressPage, instance.installationRuleLabel, instance.installationIDLabel, instance.installationRootLabel,
        instance.workspaceRuleLabel, instance.gitCloneRuleLabel, instance.targetPlatformLabel, instance.oomphRemoteURILabel, instance.jreLocationLabel,
        instance.bugzillaLabel, instance.passwordLabel, instance.userIDLabel, instance.showAll };
  }

  /**
   * @snippet image VariablePageExtras.images
   */
  public static Image[] promptPageExtras()
  {
    DocVariablePage.CapturePromptPage instance = CapturePromptPage.getInstance();
    return new Image[] { null, instance.installationRuleControl, instance.installationIDControl, instance.installationRootControl,
        instance.installationRootHelper, instance.workspaceRuleControl, instance.gitCloneRuleControl, instance.targetPlatformControl,
        instance.oomphRemoteURIControl, instance.jreLocationControl, instance.jreLocationHelper, instance.bugzillaControl, instance.passwordControl,
        instance.passwordHelper, instance.userIDControl };
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
