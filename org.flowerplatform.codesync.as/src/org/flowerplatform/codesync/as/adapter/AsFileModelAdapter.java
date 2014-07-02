package org.flowerplatform.codesync.as.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.flex.compiler.internal.projects.DefinitionPriority;
import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.internal.projects.SourcePathManager;
import org.apache.flex.compiler.internal.scopes.MXMLFileScope;
import org.apache.flex.compiler.internal.tree.mxml.MXMLFileNode;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.compiler.units.requests.IRequest;
import org.apache.flex.compiler.units.requests.ISyntaxTreeRequestResult;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.codesync.as.asdoc.AsDocDelegate;
import org.flowerplatform.codesync.code.adapter.AbstractFileModelAdapter;
import org.flowerplatform.core.CorePlugin;

/**
 * @author Mariana Gheorghe
 */
public class AsFileModelAdapter extends AbstractFileModelAdapter {

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element,
			Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncAsConstants.STATEMENTS.equals(feature)) {
			ICompilationUnit cu = (ICompilationUnit) getOrCreateFileInfo(element);
			if (cu == null) {
				return Collections.emptyList();
			}
			IRequest<ISyntaxTreeRequestResult, ICompilationUnit> astRequest = cu.getSyntaxTreeRequest();
			IFileNode ast;
			try {
				ast = (IFileNode) astRequest.get().getAST();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			if (ast == null) {
				return Collections.emptyList();
			}
			if (ast instanceof MXMLFileNode) {
				return Arrays.asList(((MXMLFileScope) ast.getScope()).getMainClassDefinition());
			}
			return Arrays.asList(ast.getTopLevelDefinitions(true, true));
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * The file info must be a {@link ICompilationUnit}, not the AST root, because the 
	 * compilation units are refered through {@link WeakReference}s, and will be garbage
	 * collected during sync.
	 */
	@Override
	protected Object createFileInfo(Object file) {
		Workspace ws = new Workspace();
		ws.setASDocDelegate(new AsDocDelegate());
		FlexProject project = new FlexProject(ws);
		
		String path = null;
		try {
			path = SourcePathManager.computeQName((File) CorePlugin.getInstance().getFileAccessController().getFile(null), (File) file);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return project.getSourceCompilationUnitFactory().createCompilationUnit((File) file, 
				DefinitionPriority.BasePriority.SOURCE_PATH, 0, path, null);
	}

	@Override
	protected TextEdit rewrite(Document document, Object fileInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
