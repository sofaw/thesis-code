package org.sew569.softwaremigrator;

import org.eclipse.jface.wizard.Wizard;

public class MigrationWizard extends Wizard {
    protected MigrationWizardPageOne one;
	
	@Override
	public boolean performFinish() {
		// TODO implement code generation here and close window
		System.out.println(one.getNetlistText());
		System.out.println(one.getConfigText());
		System.out.println(one.getOutputText());
		System.out.println("wizard finished");
		this.getShell().close();
		return false;
	}

	@Override
    public void addPages() {
        one = new MigrationWizardPageOne();
        addPage(one);
    }
}
