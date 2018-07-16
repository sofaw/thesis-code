package org.sew569.softwaremigrator;

public class MigrationData {
	private String inputFile;
	private String configFile;
	private String outputFile;
	private String libsPath;
	
	public String getInputFile() {
		return inputFile;
	}
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	public String getConfigFile() {
		return configFile;
	}
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	public String getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	
	public String getLibsPath() {
		return libsPath;
	}
	public void setLibsPath(String libsPath) {
		this.libsPath = libsPath;
	}	
}
