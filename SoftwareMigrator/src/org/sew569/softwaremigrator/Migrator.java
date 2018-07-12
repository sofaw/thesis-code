package org.sew569.softwaremigrator;

import java.util.Set;

public class Migrator {
	private String inputFile;
	private String configFile;
	private String outputFile;

	public Migrator(String inputFile, String configFile, String outputFile) {
		this.inputFile = inputFile;
		this.configFile = configFile;
		this.outputFile = outputFile;
	}

	public void runTransform() throws Exception {
		if (inputFile == null || inputFile.isEmpty()) {
			throw new Exception("Input file not initialised");
		}
		if (configFile == null || configFile.isEmpty()) {
			throw new Exception("Config file not initialised");
		}
		if (outputFile == null || outputFile.isEmpty()) {
			throw new Exception("Output file not initialised");
		}

		// Convert xml to model
		new EtlRunner(inputFile, configFile).execute();
		// TODO: Generate library choices here
		// First get partNames
		/*Set<String> partNames = (Set<String>) new EolRunner(configFile).execute();
		for(String s : partNames) {
			System.out.println(s);
		}*/
		// Second launch ui to allow users to choose libraries

		// Generate cpp from model
		new EglRunner(outputFile, configFile).execute();

	}

	public static void main(String[] args) {
		Migrator m = new Migrator(
				"/Users/sophie/eclipse-projects/thesis/transform_v2/test/input_xml/case_study_arduino_netlist.xml",
				"/Users/sophie/eclipse-projects/thesis/transform_v2/test/test_config.xml",
				"/Users/sophie/eclipse-projects/thesis/transform_v2/output.cpp");
		try {
			m.runTransform();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
