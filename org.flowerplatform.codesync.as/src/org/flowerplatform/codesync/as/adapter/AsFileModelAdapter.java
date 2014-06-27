package org.flowerplatform.codesync.as.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.flex.compiler.internal.projects.DefinitionPriority;
import org.apache.flex.compiler.internal.projects.FlexProject;
import org.apache.flex.compiler.internal.workspaces.Workspace;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IFileNode;
import org.apache.flex.compiler.units.ICompilationUnit;
import org.apache.flex.compiler.units.requests.IRequest;
import org.apache.flex.compiler.units.requests.ISyntaxTreeRequestResult;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
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
			IFileNode ast = (IFileNode) getOrCreateFileInfo(element);
			return Arrays.asList(ast.getTopLevelDefinitions(true, true));
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable);
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object createFileInfo(Object file) {
		String content = CorePlugin.getInstance().getFileAccessController().readFileToString(file);
		if ("mxml".equals(CorePlugin.getInstance().getFileAccessController().getFileExtension(file))) {
//			MxmlScanner scanner = new MxmlScanner(new ByteArrayInputStream(content.getBytes()), true);
//			Token token = null;
//			do {
//				token = scanner.getNextToken();
//				if (token instanceof CDATANode) {
//					CDATANode script = (CDATANode) token;
//					content = script.image;
//					break;
//				}
//			} while (token != null);
		}
		
		Workspace ws = new Workspace();
		FlexProject project = new FlexProject(ws);
		
		ICompilationUnit cu = project.getSourceCompilationUnitFactory().createCompilationUnit((File) file, 
				DefinitionPriority.BasePriority.SOURCE_PATH, 0, "test", "en-US");
		IRequest<ISyntaxTreeRequestResult, ICompilationUnit> req = cu.getSyntaxTreeRequest();
		IASNode ast;
		try {
			ast = req.get().getAST();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return ast;
	}

	@Override
	protected TextEdit rewrite(Document document, Object fileInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
