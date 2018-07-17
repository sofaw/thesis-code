package org.sew569.softwaremigrator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.etl.EtlModule;

public class EtlRunnerXmlToModel extends EpsilonRunner {
	private String inputXml;
	private String configXml;

	public EtlRunnerXmlToModel(String inputXml, String configXml) {
		super();
		this.inputXml = inputXml;
		this.configXml = configXml;
	}

	@Override
	public EtlModule createModule() {
		return new EtlModule();
	}

	@Override
	public List<IModel> getModels() throws Exception {
		List<IModel> models = new ArrayList<IModel>();
		models.add(createEmfModel("OUT", "resources/transform_output/out.model",
				"resources/metamodel/metamodel_v2.ecore", false, true));
		models.add(createXmlModel("CONFIG", configXml, true, true));
		models.add(createXmlModel("XML", inputXml, true, false));

		return models;
	}

	@Override
	public String getSource() throws Exception {
		return "resources/xml_to_model/xml_to_model.etl";
	}
}
