package org.sew569;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFieldReference;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNamedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.index.IIndexManager;
import org.eclipse.cdt.core.index.IIndexName;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.epsilon.emc.cdt.CdtUtilities;

public class ParallaxDependencyResolver extends ASTVisitor {
	private IIndex index;
	private ICProject project;
	private String libPath = "/Users/sophie/SimpleIDE/Learn/Simple Libraries/";
	
	HashMap<ITranslationUnit, IASTTranslationUnit> projectASTCache;
	
	private Set<IASTNode> platformDependentDeclarations;

	// static initializer: executed when the class is loaded
	{
		shouldVisitNames 					= true;
		shouldVisitExpressions				= true;
		shouldVisitParameterDeclarations	= true;
		shouldVisitDeclarations 			= true;

		projectASTCache = new HashMap<ITranslationUnit, IASTTranslationUnit>();
		
		platformDependentDeclarations = new HashSet<IASTNode>();
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
	
	public Set<IASTNode> getPlatformDependentDeclarations() {
		return this.platformDependentDeclarations;
	}

	@Override
	public int visit(IASTExpression exp) {
		if (!(exp instanceof IASTFunctionCallExpression))
			return PROCESS_CONTINUE;
		IASTFunctionCallExpression funcCallExp = (IASTFunctionCallExpression) exp;

		IASTExpression funcExpression = funcCallExp.getFunctionNameExpression();

		IASTName name = null;
		
		if (funcExpression instanceof IASTFieldReference) {
			// get the field reference for this: e.g., xmlDoc.LoadFile
			IASTFieldReference fieldRef = (IASTFieldReference) funcExpression;
			// get the name
			name = fieldRef.getFieldName();
		} else if (funcExpression instanceof IASTIdExpression) {
			// get the function name: e.g., printf
			IASTIdExpression idExp = (IASTIdExpression) funcExpression;
			// get the name
			name = idExp.getName();
		}

		if (name != null) {
			try {
				checkIsPlatformDependent(name, IIndex.FIND_DECLARATIONS_DEFINITIONS, libPath);
			} catch (InterruptedException | CoreException e) {
				e.printStackTrace();
			}
		}
		return PROCESS_CONTINUE;
	}

	/*@Override
	public int visit(IASTParameterDeclaration pDecl) {
		if (!(pDecl.getDeclSpecifier() instanceof IASTNamedTypeSpecifier))
			return PROCESS_CONTINUE;

		IASTName declSpecifierName = ((IASTNamedTypeSpecifier) pDecl.getDeclSpecifier()).getName();

		try {
			checkIsPlatformDependent(declSpecifierName, IIndex.FIND_DECLARATIONS_DEFINITIONS, libPath);
		} catch (InterruptedException | CoreException e) {
			e.printStackTrace();
		}
		
		return PROCESS_CONTINUE;
	}*/

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
			checkIsPlatformDependent(declSpecifierName, IIndex.FIND_DECLARATIONS_DEFINITIONS, libPath);
		} catch (InterruptedException | CoreException e) {
			e.printStackTrace();
		}
		return PROCESS_CONTINUE;
	}
	
	private void checkIsPlatformDependent(IASTName name, int indexFlags, String libPath) throws InterruptedException, CoreException {
		IBinding binding = name.resolveBinding();
		
		index.acquireReadLock();
		IIndexName[] defs = index.findNames(binding, indexFlags);
		
		if(defs.length > 0) {
			String originFile = defs[0].getFileLocation().getFileName();
			if(originFile.startsWith(libPath)) {
				IASTNode node = null;
				if(defs[0].isDeclaration()) {
					node = findNodeFromIndex(defs[0], IASTSimpleDeclaration.class);
				} else {
					node = findNodeFromIndex(defs[0], IASTFunctionDefinition.class);
				}
				platformDependentDeclarations.add(node);
			}
		}

		index.releaseReadLock();
	}
	
	//https://github.com/gerasimou/SoftwareObsolescence/blob/master/org.spg.refactoring/src/org/spg/refactoring/RefactoringProject.java#L313
	@SuppressWarnings("rawtypes")
	protected IASTNode findNodeFromIndex(IIndexName indexName, Class...classes){
		try {
			//find translation unit & corresponding ast, cache ast if necessary
			ITranslationUnit tu;
			tu = CdtUtilities.getTranslationUnitFromIndexName(indexName);
			IASTTranslationUnit ast = tu.getAST();
			if (projectASTCache.containsKey(tu)){
				ast = projectASTCache.get(tu);
			} else{//then it's a native library (e.g., stdio.h); do we need it in the AST?
				//if (locked)
				//	return null;
				ast = tu.getAST(index, ITranslationUnit.AST_SKIP_INDEXED_HEADERS);
				projectASTCache.put(tu, ast);
			}
			
			//find 
			IASTNode node = ast.getNodeSelector(null).findEnclosingNode(indexName.getNodeOffset(), indexName.getNodeLength());
//			IASTName name = (IASTName)node;
			
			while ( (node != null) && !(nodeIsInstance(classes, node)) ){
				node =  node.getParent();
			}
			assert (nodeIsInstance(classes, node));
			return node;
		} 
		catch (CoreException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private boolean nodeIsInstance (Class [] classes, IASTNode node){
		for (Class clazz: classes){
			if (clazz.isInstance(node))
				return true;
		}
		return false;
	}
}
