package org.sew569.softwaremigrator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.epsilon.egl.EglTemplateFactory;
import org.eclipse.epsilon.egl.EglTemplateFactoryModuleAdapter;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.types.EolPrimitiveType;

public class EglRunnerModelToCode extends EpsilonRunner {
	private String configXml;
	private String outputFile;
	private Set<String> libraries;
	private String projectFolder;
	private String parallaxLibs;
	private String mainFile;

	public EglRunnerModelToCode(String outputFile, String configXml, Set<String> libraries, 
			String projectFolder, String parallaxLibs, String mainFile) {
		super();
		this.outputFile = outputFile;
		this.configXml = configXml;
		this.libraries = libraries;
		this.projectFolder = projectFolder;
		this.parallaxLibs = parallaxLibs;
		this.mainFile = mainFile;
		
		List<Variable> params = new ArrayList<Variable>();
		// Only want last part of projectFolder path
		String[] projectFolderSplit = projectFolder.split("/");	
		Variable projectVar = new Variable("project", projectFolderSplit[projectFolderSplit.length - 1], EolPrimitiveType.String);
		Variable parallaxLibsVar = new Variable("libs", parallaxLibs, EolPrimitiveType.String);
		// Only want filename of mainFile path
		String[] mainFileSplit = mainFile.split("/");
		Variable mainFileVar = new Variable("mainFile", mainFileSplit[mainFileSplit.length - 1], EolPrimitiveType.String);
		params.add(projectVar);
		params.add(parallaxLibsVar);
		params.add(mainFileVar);
		setParameters(params);
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
		models.add(createCdtModel("Source", "S", "case_study_parallax", true, true, false)); // TODO: update to use value from wizard

		return models;
	}

	@Override
	public void postProcess() {
		// Write result to output file
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(outputFile));
			for(String lib : libraries) {
				if(lib != "<none>") {
					writer.write("#include \"" + lib + "\"\n");
				}
			}
			writer.write((String) result);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
