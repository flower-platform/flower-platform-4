package org.flowerplatform.codesync.code.java.line_information_provider;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.flowerplatform.codesync.line_information_provider.ILineInformationProvider;

/**
 * Mapped to {@link MethodDeclaration}.
 * 
 * @author Mariana Gheorghe
 */
public class JavaOperationLineInformationProvider implements ILineInformationProvider {

	@Override
	public int getStartLine(Object model) {
		MethodDeclaration node = getNode(model);
		if (node.getBody() == null) {
			return -1;
		}
		CompilationUnit cu = getRoot(node);
		return cu.getLineNumber(node.getBody().getStartPosition());
	}

	@Override
	public int getEndLine(Object model) {
		MethodDeclaration node = getNode(model);
		if (node.getBody() == null) {
			return -1;
		}
		CompilationUnit cu = getRoot(node);
		return cu.getLineNumber(node.getBody().getStartPosition() + node.getBody().getLength());
	}
	
	protected MethodDeclaration getNode(Object model) {
		return (MethodDeclaration) model;
	}
	
	protected CompilationUnit getRoot(ASTNode node) {
		return (CompilationUnit) node.getRoot();
	}

	@Override
	public boolean canHandle(Object model) {
		return model instanceof MethodDeclaration;
	}

}
