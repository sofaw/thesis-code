package org.sew569.softwaremigrator;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class MigrationWizard extends Wizard {
	protected MigrationWizardPageOne one;
	protected MigrationWizardPageTwo two;
	protected MigrationWizardPageThree three;
	protected MigrationData md;

	@Override
	public boolean performFinish() {
		// TODO: delete hardcoded vals
		try {
			Migrator m = new Migrator(one.getNetlistText(), one.getConfigText(), 
					one.getOutputText(), two.getSelectedLibraries(),
					three.getProjectText(), three.getLibsText());
			m.runTransform();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.getShell().close();
		return false;
	}

	@Override
	public void addPages() {
		md = new MigrationData();
		one = new MigrationWizardPageOne(md);
		addPage(one);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
		IWizardPage next = null;
		if (currentPage == one && one.isPageComplete()) {
			two = new MigrationWizardPageTwo(md);
			next = two;
			addPage(next);
		} else if(currentPage == two && two.isPageComplete()) {
			three = new MigrationWizardPageThree(md);
			next = three;
			addPage(next);
		}
		return next;
	}

	@Override
	public boolean canFinish() {
		return getContainer().getCurrentPage() != one && getContainer().getCurrentPage() != two
				&& three != null && three.isPageComplete();
	}
}
