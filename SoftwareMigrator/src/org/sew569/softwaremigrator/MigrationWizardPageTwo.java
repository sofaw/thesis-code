package org.sew569.softwaremigrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.sew569.ranking.LibraryRanker;

import com.google.common.collect.ArrayListMultimap;

public class MigrationWizardPageTwo extends WizardPage {
	private Composite c;
	private MigrationData md;
	private ArrayList<TableItem> tableItems;

	public MigrationWizardPageTwo(MigrationData md) {
		super("Select libraries");
		setTitle("Select libraries");
		setDescription("Migration Wizard: Select libraries");
		this.md = md;
		tableItems = new ArrayList<TableItem>();
	}

	public ArrayListMultimap<String, String> getSelectedLibraries() throws Exception {
		ArrayListMultimap<String, String> results = ArrayListMultimap.create();
		for (TableItem i : tableItems) {
			if (i.getChecked()) {
				if (i.getData() != null && i.getData() instanceof String) {
					results.put((String) i.getData(), i.getText());
				} else {
					throw new Exception("Could not update config file.");
				}
			}
		}
		return results;
	}

	private void createLibrarySelectionList(String partName) {
		Label label = new Label(c, SWT.NONE);
		label.setText(partName);

		ConfigHandler ch = new ConfigHandler(md.getConfigFile());
		Map<String, Integer> libUses = ch.getLibrariesAndCount(partName);
		Set<String> lowerCaseKeys = libUses.keySet().stream().map(s -> s.toLowerCase()).collect(Collectors.toSet());

		List<String> libs = getOrderedLibraries(partName);

		Table table = new Table(c, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		for (String l : libs) {
			TableItem item = new TableItem(table, SWT.NONE);
			tableItems.add(item);
			String[] lsplit = l.split("/");
			item.setText(lsplit[lsplit.length - 1]);

			// If lib has previously been chosen, colour it green
			if (lowerCaseKeys.contains(lsplit[lsplit.length - 1].toLowerCase())) {
				item.setBackground(new Color(c.getDisplay(), new RGB(0, 220, 0)));
			}

			// Add partName as data so it can be used for updating the config
			// file
			item.setData(partName);
		}
		GridData gd_table = new GridData(300, 100);
		table.setLayoutData(gd_table);

		table.getItem(0).setChecked(true);
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList<TableItem> selected = new ArrayList<TableItem>();
				for (TableItem t : table.getItems()) {
					if (t.getChecked()) {
						selected.add(t);
					}
				}
				if (selected.size() == 1) {
					// do nothing
				} else if (selected.size() > 1) {
					// Deselect <none> if it is selected
					table.getItem(0).setChecked(false);
				} else {
					// Zero selected, check default value of <none>
					table.getItem(0).setChecked(true);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	private List<String> getOrderedLibraries(String partName) {
		// TODO: also read config file to find out previously chosen libraries
		ArrayList<String> orderedLibs = new ArrayList<String>();
		orderedLibs.add("<none>");
		LibraryRanker l = new LibraryRanker();
		// TODO: update this based on user input
		l.init(md.getLibsPath());
		List<String> headerFiles = null;
		try {
			headerFiles = l.getHeaderFiles();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		ArrayListMultimap<Double, String> rankings = ArrayListMultimap.create();
		for (String h : headerFiles) {
			double tfidf = 0;
			try {
				String[] tokenisedPartName = partName.split("\\s+");

				for (String token : tokenisedPartName) {
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
		for (double o : order) {
			for (String s : rankings.get(o)) {
				orderedLibs.add(s);
			}
		}
		return orderedLibs;
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
			partNames = new EolRunnerGetPartNames(md.getInputFile(), md.getConfigFile()).getPartNames();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (String part : partNames) {
			createLibrarySelectionList(part);
		}

		c.setSize(c.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		sc.setMinSize(c.getSize());
		sc.setContent(c);
		setControl(sc);

		setPageComplete(true);
	}
}
