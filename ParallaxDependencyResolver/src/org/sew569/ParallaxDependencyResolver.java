package org.sew569;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFieldReference;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNamedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IScope;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPNamespaceScope;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.index.IIndexManager;
import org.eclipse.cdt.core.index.IIndexName;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.internal.core.dom.parser.cpp.ICPPUnknownBinding;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

public class ParallaxDependencyResolver extends ASTVisitor {
	private IIndex index;
	private ICProject project;
	private String libPath = "/Users/sophie/SimpleIDE/Learn/Simple Libraries/";
	
	private List<IASTNode> platformDependentNodes;
	// TODO: delete
	private List<IASTName> names;
	private List<IASTNode> allNodes;

	// static initializer: executed when the class is loaded
	{
		shouldVisitNames 					= true;
		shouldVisitExpressions				= true;
		shouldVisitParameterDeclarations	= true;
		shouldVisitDeclarations 			= true;

		platformDependentNodes = new ArrayList<IASTNode>();
		names = new ArrayList<IASTName>();
		allNodes = new ArrayList<IASTNode>();
	}
	
	public void init(String projectName, String libPath) {
		this.libPath = libPath;
		this.project = CoreModel.getDefault().getCModel().getCProject(projectName);
		CCorePlugin.getIndexManager().reindex(project);
		CCorePlugin.getIndexManager().joinIndexer(IIndexManager.FOREVER, new NullProgressMonitor());
		try {
			this.index = CCorePlugin.getIndexManager().getIndex(project);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public List<IASTNode> getPlatformDependentNodes() {
		return this.platformDependentNodes;
	}
	
	public List<IASTName> getNames() {
		return this.names;
	}
	
	public List<IASTNode> getAllNodes() {
		return this.allNodes;
	}

	@Override
	public int visit(IASTExpression exp) {
		if (!(exp instanceof IASTFunctionCallExpression))
			return PROCESS_CONTINUE;
		IASTFunctionCallExpression funcCallExp = (IASTFunctionCallExpression) exp;

		IASTExpression funcExpression = funcCallExp.getFunctionNameExpression();

		IASTName name = null;
		// TODO: delete
		IASTNode node = null; 
		
		if (funcExpression instanceof IASTFieldReference) {
			// get the field reference for this: e.g., xmlDoc.LoadFile
			IASTFieldReference fieldRef = (IASTFieldReference) funcExpression;
			// get the name
			name = fieldRef.getFieldName();
			node = fieldRef;
		} else if (funcExpression instanceof IASTIdExpression) {
			// get the function name: e.g., printf
			IASTIdExpression idExp = (IASTIdExpression) funcExpression;
			// get the name
			name = idExp.getName();
			node = idExp;
		}

		if (name != null) {
			try {
				checkIsPlatformDependent(exp, name, IIndex.FIND_DECLARATIONS_DEFINITIONS, libPath);
			} catch (InterruptedException | CoreException e) {
				e.printStackTrace();
			}
		}
		return PROCESS_CONTINUE;
	}

	@Override
	public int visit(IASTParameterDeclaration pDecl) {
		if (!(pDecl.getDeclSpecifier() instanceof IASTNamedTypeSpecifier))
			return PROCESS_CONTINUE;

		IASTName declSpecifierName = ((IASTNamedTypeSpecifier) pDecl.getDeclSpecifier()).getName();

		try {
			checkIsPlatformDependent(pDecl, declSpecifierName, IIndex.FIND_DECLARATIONS_DEFINITIONS, libPath);
		} catch (InterruptedException | CoreException e) {
			e.printStackTrace();
		}
		
		return PROCESS_CONTINUE;
	}

	@Override
	public int visit(IASTDeclaration decl) {
		if (!(decl instanceof IASTSimpleDeclaration))
			return PROCESS_CONTINUE;
		IASTSimpleDeclaration simpleDecl = (IASTSimpleDeclaration) decl;

		// check if there are any declarators: they should, otherwise there
		// would be a compilation error (e.g., int ;)
		if (!(simpleDecl.getDeclSpecifier() instanceof IASTNamedTypeSpecifier))
			return PROCESS_CONTINUE;

		IASTName declSpecifierName = ((IASTNamedTypeSpecifier) simpleDecl.getDeclSpecifier()).getName();

		try {
			checkIsPlatformDependent(decl, declSpecifierName, IIndex.FIND_DECLARATIONS_DEFINITIONS, libPath);
		} catch (InterruptedException | CoreException e) {
			e.printStackTrace();
		}
		return PROCESS_CONTINUE;
	}
	
	private void checkIsPlatformDependent(IASTNode node, IASTName name, int indexFlags, String libPath) throws InterruptedException, CoreException {
		names.add(name);
		allNodes.add(node);
		
		IBinding binding = name.resolveBinding();
		
		index.acquireReadLock();
		IIndexName[] defs = index.findNames(binding, indexFlags);
		
		if(defs.length > 0) {
			String originFile = defs[0].getFileLocation().getFileName();
			if(originFile.startsWith(libPath)) {
				platformDependentNodes.add(node);
			}
		}

		index.releaseReadLock();
	}
}
