package org.sew569.softwaremigrator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.etl.EtlModule;

public class EtlRunner {
	private String inputXml;
	private String outputModel;
	private String configXml;

	protected EtlModule module;
	protected List<Variable> parameters = new ArrayList<Variable>();

	protected Object result;

	public EtlRunner(String inputXml, String outputModel, String configXml) {
		this.inputXml = inputXml;
		this.outputModel = outputModel;
		this.configXml = configXml;
	}

	public EtlModule createModule() {
		return new EtlModule();
	}

	public List<IModel> getModels() throws Exception {
		List<IModel> models = new ArrayList<IModel>();
		models.add(
				ModelLoader.createEmfModel("OUT", outputModel, "resources/metamodel/metamodel_v2.ecore", false, true));
		models.add(ModelLoader.createXmlModel("CONFIG", configXml, true, true));
		models.add(ModelLoader.createXmlModel("XML", inputXml, true, false));

		return models;
	}

	public String getSource() throws Exception {
		return "resources/xml_to_model/xml_to_model.etl";
	}

	public void preProcess() {
	};

	public void postProcess() {
	};

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

		module.getContext().getModelRepository().dispose();
	}

	public List<Variable> getParameters() {
		return parameters;
	}

	protected Object execute(EtlModule module) throws EolRuntimeException {
		return module.execute();
	}
}
