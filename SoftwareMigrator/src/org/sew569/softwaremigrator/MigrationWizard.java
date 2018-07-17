package org.sew569.softwaremigrator;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class MigrationWizard extends Wizard {
	protected MigrationWizardPageOne one;
	protected MigrationWizardPageTwo two;
	protected MigrationData md;

	@Override
	public boolean performFinish() {
		// TODO: update config file
		try {
			Migrator m = new Migrator(one.getNetlistText(), one.getConfigText(), one.getOutputText(), two.getSelectedLibraries());
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
		}
		return next;
	}

	@Override
	public boolean canFinish() {
		return getContainer().getCurrentPage() != one && two != null && two.isPageComplete();
	}
}
