package org.sew569.softwaremigrator;

import org.eclipse.jface.wizard.Wizard;

public class MigrationWizard extends Wizard {
    protected MigrationWizardPageOne one;
	
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
        addPage(one);
    }
}
