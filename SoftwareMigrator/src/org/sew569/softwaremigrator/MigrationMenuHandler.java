package org.sew569.softwaremigrator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

public class MigrationMenuHandler extends AbstractHandler {
	IWorkbenchWindow activeWindow = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Testing");
		return null;
	}

}
