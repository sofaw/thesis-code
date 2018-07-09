package org.sew569.softwaremigrator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;
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
		models.add(createEmfModel("M", "resources/test_models/case_study.model",
				"resources/metamodel/metamodel_v2.ecore", true, false));
		
		PlainXmlModel model = new PlainXmlModel();
		// TODO: how to use relative path for this??
	    model.setFile(new File("/Users/sophie/eclipse-projects/thesis/SoftwareMigrator/src/resources/test_models/config.xml"));
	    model.setName("X");
	    model.setReadOnLoad(true);
	    model.setStoredOnDisposal(true);
	    model.load();
	    models.add(model);
		return models;
	}

	public static void main(String[] args) throws Exception {
		new MigratorEol().execute(); // Returns the generated content as a string
	}
}
