package org.sew569.softwaremigrator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.models.IModel;

public class MigratorEol extends EolRunner {

	@Override
	public IEolModule createModule() {
		return new EolModule();
	}

	@Override
	public String getSource() throws Exception {
		return "resources/model_to_cpp/code_generation_queries.eol";
	}

	@Override
	public List<IModel> getModels() throws Exception {
		List<IModel> models = new ArrayList<IModel>();
		models.add(ModelLoader.createEmfModel("M", "resources/test_models/case_study.model",
				"resources/metamodel/metamodel_v2.ecore", true, false));
		models.add(ModelLoader.createXmlModel("X", 
				"/Users/sophie/eclipse-projects/thesis/SoftwareMigrator/src/resources/test_models/config.xml", true, true));
		return models;
	}

	public static void main(String[] args) throws Exception {
		new MigratorEol().execute();
	}
}
