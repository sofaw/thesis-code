package org.sew569.softwaremigrator;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MigrationWizardPageOne extends WizardPage {
    private Text netlistText;
    private Text configText;
    private Text outputText;
    private Composite container;
	
	public MigrationWizardPageOne() {
		super("Configure Migration");
		setTitle("Configure Migration");
	    setDescription("Migration Wizard: Configure Migration");
	}

	@Override
	public void createControl(Composite parent) {
		// TODO: implement this based on design for wizard
		container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        Label netlistLabel = new Label(container, SWT.NONE);
        netlistLabel.setText("Netlist:");

        netlistText = new Text(container, SWT.BORDER | SWT.SINGLE);
        netlistText.setText("");
        netlistText.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!netlistText.getText().isEmpty() 
                		&& !configText.getText().isEmpty()
                		&& !outputText.getText().isEmpty()) {
                    setPageComplete(true);
                }
            }

        });
        
        Label configLabel = new Label(container, SWT.NONE);
        configLabel.setText("Config:");

        configText = new Text(container, SWT.BORDER | SWT.SINGLE);
        configText.setText("");
        configText.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!netlistText.getText().isEmpty() 
                		&& !configText.getText().isEmpty()
                		&& !outputText.getText().isEmpty()) {
                    setPageComplete(true);
                }
            }

        });
        
        Label outputLabel = new Label(container, SWT.NONE);
        outputLabel.setText("Output file:");

        outputText = new Text(container, SWT.BORDER | SWT.SINGLE);
        outputText.setText("");
        outputText.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!netlistText.getText().isEmpty() 
                		&& !configText.getText().isEmpty()
                		&& !outputText.getText().isEmpty()) {
                    setPageComplete(true);
                }
            }

        });
        
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        netlistText.setLayoutData(gd);
        configText.setLayoutData(gd);
        outputText.setLayoutData(gd);
        // required to avoid an error in the system
        setControl(container);
        setPageComplete(false);

    }

    public String getNetlistText() {
        return netlistText.getText();
    }

    public String getConfigText() {
        return configText.getText();
    }
}
