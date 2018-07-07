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

		// TODO: run ant task
		// TODO: set variables in build file

		File buildFile = new File("transform_resources/build.xml");
		Project p = new Project();
		p.setUserProperty("ant.file", buildFile.getAbsolutePath());
		p.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		p.addReference("ant.projectHelper", helper);
		helper.parse(p, buildFile);
		p.executeTarget(p.getDefaultTarget());
	}
	
	public static void main(String args[]) {
		Migrator m = new Migrator("a","a","a");
		try {
			m.runTransform();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
