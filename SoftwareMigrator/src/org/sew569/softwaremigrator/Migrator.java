package org.sew569.softwaremigrator;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

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
		// Generate cpp from model
		new EglRunner(outputFile, configFile).execute();

	}
}
