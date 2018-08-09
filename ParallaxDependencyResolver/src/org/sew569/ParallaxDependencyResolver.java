package org.sew569;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.index.IIndexManager;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

public class ParallaxDependencyResolver {
	private IIndex index;
	private ICProject project;
	
	public ParallaxDependencyResolver(ICProject project) {
		this.project = project;
		
		// TODO: resolve index on setup?
	}
	
	public void resolveIndex() throws CoreException {
		CCorePlugin.getIndexManager().reindex(project);
		CCorePlugin.getIndexManager().joinIndexer(IIndexManager.FOREVER, new NullProgressMonitor()); // wait for indexing to complete.
		this.index 		= CCorePlugin.getIndexManager().getIndex(project);
	}
	
	public boolean isParallaxFunction(IBinding binding) {
		// TODO: use binding to look up in index whether function is parallax dependent
		// Might depend on IBinding subtype what needs to be done here...
		return false;
	}
}
