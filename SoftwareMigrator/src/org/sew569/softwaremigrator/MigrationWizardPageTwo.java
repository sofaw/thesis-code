package org.sew569.softwaremigrator;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MigrationWizardPageTwo extends WizardPage {
    private Composite container;
	
	public MigrationWizardPageTwo() {
		super("Select libraries");
		setTitle("Select libraries");
	    setDescription("Migration Wizard: Select libraries");
	}

	// TODO
	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
        
        Label label = new Label(container, SWT.NONE);
        label.setText("test");
        
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        // required to avoid an error in the system
        setControl(container);
        // TODO: get part names and library choices
        // TODO: set this to false
        setPageComplete(true);
    }
}
