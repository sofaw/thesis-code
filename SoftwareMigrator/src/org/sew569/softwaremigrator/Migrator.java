package org.sew569.softwaremigrator;

import java.util.HashSet;

import com.google.common.collect.ArrayListMultimap;

public class Migrator {
	private String inputFile;
	private String configFile;
	private String outputFile;
	private ArrayListMultimap<String, String> partToLibs;

	public Migrator(String inputFile, String configFile, String outputFile,
			ArrayListMultimap<String, String> partToLibs) {
		this.inputFile = inputFile;
		this.configFile = configFile;
		this.outputFile = outputFile;
		this.partToLibs = partToLibs;
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
		if (partToLibs == null) {
			throw new Exception("Libraries could not be accessed");
		}

		// Convert xml to model
		new EtlRunnerXmlToModel(inputFile, configFile).execute();

		// Generate cpp from model
		new EglRunnerModelToCode(outputFile, configFile, new HashSet<String>(partToLibs.values())).execute();
		ConfigHandler c = new ConfigHandler(configFile);
		c.updateConfigFile(partToLibs);
	}

	

	// TODO: delete
	/*public static void main(String[] args) {
		ArrayListMultimap<String, String> amm = ArrayListMultimap.create();
		amm.put("led", "none");
		amm.put("led", "val");
		amm.put("humidity and temp sensor", "DHT.h");

		Migrator m = new Migrator(
				"/Users/sophie/eclipse-projects/thesis/transform_v2/test/input_xml/case_study_arduino_netlist.xml",
				"/Users/sophie/eclipse-projects/thesis/transform_v2/test/test_config.xml",
				"/Users/sophie/eclipse-projects/thesis/transform_v2/output.cpp", amm);
		try {
			m.runTransform();
		} catch (Exception e) { // TODO Auto-generated
			e.printStackTrace();
		}

	}*/
}
