package org.sew569.softwaremigrator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.dt.ExtensionPointToolNativeTypeDelegate;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;

public class EolRunner {
	private String configXml;

	protected EolModule module;
	protected List<Variable> parameters = new ArrayList<Variable>();

	protected Object result;

	public EolRunner(String configXml) {
		this.configXml = configXml;
	}

	public EolModule createModule() {
		return new EolModule();
	}

	public List<IModel> getModels() throws Exception {
		List<IModel> models = new ArrayList<IModel>();
		models.add(ModelLoader.createEmfModel("M", "resources/transform_output/out.model",
				"resources/metamodel/metamodel_v2.ecore", true, false));
		models.add(ModelLoader.createXmlModel("X", configXml, true, true));

		return models;
	}

	public String getSource() throws Exception {
		return "resources/model_to_cpp/get_part_names.eol";
	}

	public void preProcess() {
		module.getContext().getNativeTypeDelegates().add(new ExtensionPointToolNativeTypeDelegate());
	}

	public void postProcess() {
	}

	public Object execute() throws Exception {
		module = createModule();
		module.parse(Helper.getFileURI(getSource()));

		if (module.getParseProblems().size() > 0) {
			System.err.println("Parse errors occured...");
			for (ParseProblem problem : module.getParseProblems()) {
				System.err.println(problem.toString());
			}
			return null;
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

		return result;
		//module.getContext().getModelRepository().dispose();

	}

	public List<Variable> getParameters() {
		return parameters;
	}

	protected Object execute(EolModule module) throws EolRuntimeException {
		return module.execute();
	}
}
