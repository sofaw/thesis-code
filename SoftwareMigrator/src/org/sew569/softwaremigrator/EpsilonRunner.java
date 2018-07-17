package org.sew569.softwaremigrator;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.dt.ExtensionPointToolNativeTypeDelegate;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;

public abstract class EpsilonRunner {
	protected IEolExecutableModule module;
	protected List<Variable> parameters = new ArrayList<Variable>();

	protected Object result;

	public abstract IEolExecutableModule createModule();

	public abstract String getSource() throws Exception;

	public abstract List<IModel> getModels() throws Exception;

	public void postProcess() {
	};

	public void preProcess() {
		module.getContext().getNativeTypeDelegates().add(new ExtensionPointToolNativeTypeDelegate());
	}

	public void execute() throws Exception {

		module = createModule();
		module.parse(getFileURI(getSource()));

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
			module.getContext().getFrameStack().putGlobal(parameter);
		}

		preProcess();
		result = execute(module);
		postProcess();

		// module.getContext().getModelRepository().dispose();
	}

	public List<Variable> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<Variable> parameters) {
		this.parameters = parameters;
	}

	protected Object execute(IEolExecutableModule module) throws EolRuntimeException {
		return module.execute();
	}

	protected EmfModel createEmfModel(String name, String model, String metamodel, boolean readOnLoad,
			boolean storeOnDisposal) throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, getFileURI(metamodel).toString());
		properties.put(EmfModel.PROPERTY_MODEL_URI, getFileURI(model).toString());
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);
		return emfModel;
	}

	protected EmfModel createEmfModelByURI(String name, String model, String metamodel, boolean readOnLoad,
			boolean storeOnDisposal) throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_METAMODEL_URI, metamodel);
		properties.put(EmfModel.PROPERTY_MODEL_URI, getFileURI(model).toString());
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);
		return emfModel;
	}

	protected static PlainXmlModel createXmlModel(String name, String file, boolean readOnLoad, boolean storeOnDisposal)
			throws EolModelLoadingException, URISyntaxException {
		PlainXmlModel xmlModel = new PlainXmlModel();
		xmlModel.setFile(new File(file));
		xmlModel.setName(name);
		xmlModel.setReadOnLoad(readOnLoad);
		xmlModel.setStoredOnDisposal(storeOnDisposal);
		xmlModel.load();
		return xmlModel;
	}

	protected URI getFileURI(String fileName) throws URISyntaxException {

		URI binUri = EpsilonRunner.class.getClassLoader().getResource(fileName).toURI();
		URI uri = null;

		if (binUri.toString().indexOf("bin") > -1) {
			uri = new URI(binUri.toString().replaceAll("bin", "src"));
		} else {
			uri = binUri;
		}

		return uri;
	}
}
