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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MigrationWizardPageThree extends WizardPage {
	private Text libsText;
    private Text projectText;
    private Composite container;
    private MigrationData md;
	
	public MigrationWizardPageThree(MigrationData md) {
		super("Refactoring Project Configuration:");
		setTitle("Refactoring Project Configuration:");
	    setDescription("Migration Wizard: Refactoring Project Configuration");
	    this.md = md;
	}
	
	private void completePage() {
		// TODO: need to update any data in md?
		/*md.setInputFile(getNetlistText());
		md.setConfigFile(getConfigText());
		md.setOutputFile(getOutputText());
		md.setLibsPath(getLibsText());*/
		setPageComplete(true);
	}
	
	// TODO: modify this for projects/folders
	private void addBrowseButton(Text textField, String description) {
        Button button = new Button(container, SWT.PUSH);
        button.setText("Browse");
        button.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {

               DirectoryDialog directoryDialog = new DirectoryDialog(container.getShell(), SWT.OPEN);         
               directoryDialog.setText(description);
               String path = directoryDialog.open();

               // TODO: delete?
               //String selectedFile = fileDialog.getFileName();
               textField.setText(path);
               
               if (!libsText.getText().isEmpty() 
               		&& !projectText.getText().isEmpty()) {
                   completePage();
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
                if (!libsText.getText().isEmpty() 
                		&& !projectText.getText().isEmpty()) {
                    completePage();
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
        
        projectText = addTextField("Parallax Project:");
        addBrowseButton(projectText, "Please select the Parallax project to refactor");

        libsText = addTextField("Parallax Library Root:");
        addBrowseButton(libsText, "Please select the root directory for the Parallax libraries");
        
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        projectText.setLayoutData(gd);
        libsText.setLayoutData(gd);
        // required to avoid an error in the system
        setControl(container);

        setPageComplete(false);
    }

    public String getProjectText() {
        return projectText.getText();
    }
    
    public String getLibsText() {
    	return libsText.getText();
    }
}
