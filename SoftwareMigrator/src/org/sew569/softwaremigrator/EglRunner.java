package org.sew569.softwaremigrator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.eol.dt.ExtensionPointToolNativeTypeDelegate;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;

public class EglRunner {
	private String configXml;
	private String outputFile;
	
	protected EglTemplateFactoryModuleAdapter module;
	protected List<Variable> parameters = new ArrayList<Variable>();

	protected Object result;

	public EglRunner(String outputFile, String configXml) {
		this.outputFile = outputFile;
		this.configXml = configXml;
	}

	public EglTemplateFactoryModuleAdapter createModule() {
		return new EglTemplateFactoryModuleAdapter(new EglTemplateFactory());
	}

	public String getSource() throws Exception {
		return "resources/model_to_cpp/generate_skeleton.egl";
	}

	public List<IModel> getModels() throws Exception {
		List<IModel> models = new ArrayList<IModel>();
		models.add(ModelLoader.createEmfModel("M", "resources/transform_output/out.model",
				"resources/metamodel/metamodel_v2.ecore", true, false));
		models.add(ModelLoader.createXmlModel("X", configXml, true, true));

		return models;
	}
	
	public void preProcess() {
		module.getContext().getNativeTypeDelegates().add(new ExtensionPointToolNativeTypeDelegate());
	}
	
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
	
	public void execute() throws Exception {
		module = createModule();
		module.parse(Helper.getFileURI(getSource()));

		if (module.getParseProblems().size() > 0) {
			System.err.println("Parse errors occured...");
			for (ParseProblem problem : module.getParseProblems()) {
				System.err.println(problem.toString());
			}
			return;
		}

		for (IModel model : getModels()) {
			module.getContext().getModelRepository().addModel(model);
		}

		for (Variable parameter : parameters) {
			module.getContext().getFrameStack().put(parameter);
		}

		preProcess();
		result = execute(module);
		postProcess();

		//module.getContext().getModelRepository().dispose();
	}

	public List<Variable> getParameters() {
		return parameters;
	}

	protected Object execute(EglTemplateFactoryModuleAdapter module) throws EolRuntimeException {
		return module.execute();
	}

	public static void main(String[] args) throws Exception {
		new EglRunner("/Users/sophie/eclipse-projects/thesis/SoftwareMigrator/src/resources/test_models/output.cpp",
				"/Users/sophie/eclipse-projects/thesis/SoftwareMigrator/src/resources/test_models/config.xml")
						.execute();
	}
}
