package org.sew569.softwaremigrator;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class MigrationWizard extends Wizard {
    protected MigrationWizardPageOne one;
    protected MigrationWizardPageTwo two;
	
	@Override
	public boolean performFinish() {
		Migrator m = new Migrator(one.getNetlistText(), one.getConfigText(), one.getOutputText());
		try {
			m.runTransform();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.getShell().close();
		return false;
	}

	@Override
    public void addPages() {
        one = new MigrationWizardPageOne();
        two = new MigrationWizardPageTwo();
        addPage(one);
        addPage(two);
    }
	
	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {
	    if (currentPage == one && one.isPageComplete()) {
	        return two;
	    }
	    return null;
	}
}
