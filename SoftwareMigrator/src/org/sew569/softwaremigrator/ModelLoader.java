package org.sew569.softwaremigrator;

import java.io.File;
import java.net.URISyntaxException;

import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.plainxml.PlainXmlModel;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;

public class ModelLoader {
	protected static EmfModel createEmfModel(String name, String model, String metamodel, boolean readOnLoad,
			boolean storeOnDisposal) throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, Helper.getFileURI(metamodel).toString());
		properties.put(EmfModel.PROPERTY_MODEL_URI, model);
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);
		return emfModel;
	}

	protected static EmfModel createEmfModelByURI(String name, String model, String metamodel, boolean readOnLoad,
			boolean storeOnDisposal) throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_METAMODEL_URI, metamodel);
		properties.put(EmfModel.PROPERTY_MODEL_URI, Helper.getFileURI(model).toString());
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
}
