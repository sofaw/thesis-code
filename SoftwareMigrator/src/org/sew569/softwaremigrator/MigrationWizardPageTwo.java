package org.sew569.softwaremigrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.sew569.ranking.LibraryRanker;
import com.google.common.collect.ArrayListMultimap;

public class MigrationWizardPageTwo extends WizardPage {
	private Composite c;
	private MigrationData md;
	private ArrayList<Combo> selections;

	public MigrationWizardPageTwo(MigrationData md) {
		super("Select libraries");
		setTitle("Select libraries");
		setDescription("Migration Wizard: Select libraries");
		selections = new ArrayList<Combo>();
		this.md = md;
	}

	public void createLibrarySelectionList(String partName) {
		Label label = new Label(c, SWT.NONE);
		label.setText(partName);

		Combo libraryList = new Combo(c, SWT.DROP_DOWN);
		libraryList.add("<none>");
		libraryList.select(0); // Set default selection to <none>
		selections.add(libraryList);
		
		libraryList.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean completed = true;
				for(Combo list : selections) {
					if(list.getText().isEmpty() || list.getText() == "") {
						completed = false;
					}
				}
				setPageComplete(completed);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}});
		
		LibraryRanker l = new LibraryRanker();
		// TODO: update this based on user input
		l.init("/Applications/Eclipse.app/Contents/Eclipse/arduinoPlugin/libraries");
		List<String> headerFiles = null;
		try {
			headerFiles = l.getHeaderFiles();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println(partName);
		ArrayListMultimap<Double, String> rankings = ArrayListMultimap.create();
		for(String h : headerFiles) {
			double tfidf = 0;
			try {
			    String[] tokenisedPartName = partName.split("\\s+");
			    
			    for(String token : tokenisedPartName) {
			      tfidf = tfidf + l.calculateTfIdfWeight(token, h); 
			    }
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			rankings.put(tfidf, h);
		}
		List<Double> order = new ArrayList<Double>();
		order.addAll(rankings.keySet());
		Collections.sort(order, Collections.reverseOrder());
		for(double o : order) {
			for(String s : rankings.get(o)) {
				libraryList.add(s);
				System.out.println("Value for " + s + " = " + o);
			}

		}
	}
	
	public void populateParts() {

	}

	@Override
	public void createControl(Composite parent) {
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.V_SCROLL);
		c = new Composite(sc, SWT.NONE);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		c.setLayout(layout);

		// Add labels for each column
		Label firstColumn = new Label(c, SWT.NONE);
		firstColumn.setText("Components: ");

		Label secondColumn = new Label(c, SWT.NONE);
		secondColumn.setText("Choose a library: ");

		Set<String> partNames = null;
		try {
			partNames = new EolRunner(md.getInputFile(), md.getConfigFile()).getPartNames();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(String part : partNames) {
			createLibrarySelectionList(part);
		}

		c.setSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		sc.setMinSize(c.getSize());
		sc.setContent(c);
		setControl(sc);

		setPageComplete(true);
	}
}
