package org.flowerplatform.codesync.as.adapter;

import java.io.ByteArrayInputStream;
import java.util.List;

import macromedia.asc.parser.ClassDefinitionNode;
import macromedia.asc.parser.FunctionDefinitionNode;
import macromedia.asc.parser.Node;
import macromedia.asc.parser.Parser;
import macromedia.asc.parser.ProgramNode;
import macromedia.asc.parser.VariableDefinitionNode;
import macromedia.asc.util.Context;
import macromedia.asc.util.ContextStatics;

import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.as.CodeSyncAsConstants;
import org.flowerplatform.codesync.code.adapter.AbstractFileModelAdapter;
import org.flowerplatform.core.CorePlugin;

import flex2.compiler.mxml.Token;
import flex2.compiler.mxml.dom.CDATANode;
import flex2.compiler.mxml.dom.MxmlScanner;

/**
 * @author Mariana Gheorghe
 */
public class AsFileModelAdapter extends AbstractFileModelAdapter {

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element,
			Object feature, Iterable<?> correspondingIterable) {
		if (CodeSyncAsConstants.STATEMENTS.equals(feature)) {
			ProgramNode program = (ProgramNode) getOrCreateFileInfo(element);
			return new FilteredIterable<Node, Node>(program.statements.items.iterator()) {

				@Override
				protected boolean isAccepted(Node node) {
					return node instanceof ClassDefinitionNode ||
							node instanceof VariableDefinitionNode ||
							node instanceof FunctionDefinitionNode;
				}
			};
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
			MxmlScanner scanner = new MxmlScanner(new ByteArrayInputStream(content.getBytes()), true);
			Token token = null;
			do {
				token = scanner.getNextToken();
				if (token instanceof CDATANode) {
					CDATANode script = (CDATANode) token;
					content = script.image;
					break;
				}
			} while (token != null);
		}
		Context cx = new Context(new ContextStatics());
		String origin = "";
		Parser parser = new Parser(cx, content, origin, true, true);
		return parser.parseProgram();
	}

	@Override
	protected TextEdit rewrite(Document document, Object fileInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
