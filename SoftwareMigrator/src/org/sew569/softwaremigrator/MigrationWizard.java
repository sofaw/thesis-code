package org.sew569.softwaremigrator;

import org.eclipse.jface.wizard.Wizard;

public class MigrationWizard extends Wizard {
    protected MigrationWizardPageOne one;
	
	@Override
	public boolean performFinish() {
		// TODO implement code generation here and close window
		System.out.println("wizard finished");
		return false;
	}

	@Override
    public void addPages() {
        one = new MigrationWizardPageOne();
        addPage(one);
    }
}
