package org.sew569.softwaremigrator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.models.IModel;

public class EolRunner extends EpsilonRunner {
	private String configXml;

	public EolRunner(String configXml) {
		super();
		this.configXml = configXml;
	}

	@Override
	public EolModule createModule() {
		return new EolModule();
	}

	@Override
	public List<IModel> getModels() throws Exception {
		List<IModel> models = new ArrayList<IModel>();
		models.add(createEmfModel("M", "resources/transform_output/out.model", "resources/metamodel/metamodel_v2.ecore",
				true, false));
		models.add(createXmlModel("X", configXml, true, true));

		return models;
	}

	@Override
	public String getSource() throws Exception {
		return "resources/model_to_cpp/get_part_names.eol";
	}
}
