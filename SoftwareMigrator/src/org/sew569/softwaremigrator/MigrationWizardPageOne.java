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
	
	private void addBrowseButton(Text textField, String description, String[] fileExtensions) {
        Button button = new Button(container, SWT.PUSH);
        button.setText("Browse");
        button.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {

               FileDialog fileDialog = new FileDialog(container.getShell(), SWT.OPEN);         
               fileDialog.setText(description);
               fileDialog.setFilterExtensions(fileExtensions);
               String path = fileDialog.open();

               String selectedFile = fileDialog.getFileName();
               textField.setText(path);
               
               if (!netlistText.getText().isEmpty() 
               		&& !configText.getText().isEmpty()
               		&& !outputText.getText().isEmpty()) {
                   setPageComplete(true);
               }
         }

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
     });
	}
	
	private Text addTextField(String labelText) {
        Label label = new Label(container, SWT.NONE);
        label.setText(labelText);

        Text text = new Text(container, SWT.BORDER | SWT.SINGLE);
        text.setText("");
        text.addKeyListener(new KeyListener() {

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
        return text;
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 3;
        
        netlistText = addTextField("Netlist:");
        
        String[] xmlExtensions = new String[1];
        xmlExtensions[0] = "*.xml";
        addBrowseButton(netlistText, "Please select a netlist file (.xml)", xmlExtensions);

        configText = addTextField("Config:");
        
        addBrowseButton(configText, "Please select a config file (.xml)", xmlExtensions);

        outputText = addTextField("Output file:");
        
        String[] cppExtensions = new String[1];
        cppExtensions[0] = "*.cpp";
        addBrowseButton(outputText, "Please select an output file (.cpp)", cppExtensions);
        
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
    
    public String getOutputText() {
    	return outputText.getText();
    }
}
