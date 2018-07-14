package org.sew569.softwaremigrator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class MigrationMenuHandler extends AbstractHandler {
	IWorkbenchWindow activeWindow = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		Wizard w = new MigrationWizard();
		w.setForcePreviousAndNextButtons(true);
		WizardDialog dialog = new WizardDialog(window.getShell(), w);
		dialog.open();
		return null;
	}

}
