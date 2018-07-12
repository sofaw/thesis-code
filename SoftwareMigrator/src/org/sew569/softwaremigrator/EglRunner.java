package org.sew569.softwaremigrator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.eol.models.IModel;

public class EglRunner extends EpsilonRunner {
	private String configXml;
	private String outputFile;

	public EglRunner(String outputFile, String configXml) {
		super();
		this.outputFile = outputFile;
		this.configXml = configXml;
	}

	@Override
	public EglTemplateFactoryModuleAdapter createModule() {
		return new EglTemplateFactoryModuleAdapter(new EglTemplateFactory());
	}

	@Override
	public String getSource() throws Exception {
		return "resources/model_to_cpp/generate_skeleton.egl";
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
	public void postProcess() {
		// Write result to output file
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(outputFile));
			writer.write((String) result);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
