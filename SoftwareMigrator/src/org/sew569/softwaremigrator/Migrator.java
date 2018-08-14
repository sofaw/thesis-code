package org.sew569.softwaremigrator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.types.EolPrimitiveType;
import org.eclipse.epsilon.eol.types.EolType;

import com.google.common.collect.ArrayListMultimap;

public class Migrator {
	private String inputFile;
	private String configFile;
	private String outputFile;
	private String projectFolder;
	private String parallaxLibs;
	private ArrayListMultimap<String, String> partToLibs;

	public Migrator(String inputFile, String configFile, String outputFile,
			ArrayListMultimap<String, String> partToLibs, String projectFolder, String parallaxLibs) {
		this.inputFile = inputFile;
		this.configFile = configFile;
		this.outputFile = outputFile;
		this.partToLibs = partToLibs;
		this.projectFolder = projectFolder;
		this.parallaxLibs = parallaxLibs;
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
			throw new Exception("Arduino libraries could not be accessed");
		}
		if(projectFolder == null || projectFolder.isEmpty()) {
			throw new Exception("No source project provided");
		}
		if(parallaxLibs == null || parallaxLibs.isEmpty()) {
			throw new Exception("Source platform libraries could not be accessed");
		}

		// Convert xml to model
		new EtlRunnerXmlToModel(inputFile, configFile).execute();

		// Generate cpp from model
		EglRunnerModelToCode cppGenerator = new EglRunnerModelToCode(outputFile, configFile, new HashSet<String>(partToLibs.values()), projectFolder, parallaxLibs);

		cppGenerator.execute();
		
		
		ConfigHandler c = new ConfigHandler(configFile);
		c.updateConfigFile(partToLibs);
	}
}
