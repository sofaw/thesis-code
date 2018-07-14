package org.sew569.softwaremigrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.models.IModel;

public class EolRunner extends EpsilonRunner {
	private String inputFile;
	private String configFile;

	public EolRunner(String inputFile, String configFile) {
		super();
		this.inputFile = inputFile;
		this.configFile = configFile;
	}

	@Override
	public EolModule createModule() {
		return new EolModule();
	}

	@Override
	public List<IModel> getModels() throws Exception {
		List<IModel> models = new ArrayList<IModel>();
		models.add(createXmlModel("X", inputFile, true, false));
		models.add(createXmlModel("CONFIG", configFile, true, false));
		return models;
	}

	@Override
	public String getSource() throws Exception {
		return "resources/model_to_cpp/get_part_names.eol";
	}

	public Set<String> getPartNames() throws Exception {
		execute();
		return (Set<String>) result;
	}
}
