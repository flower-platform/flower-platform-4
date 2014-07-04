/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.codesync.code.java.line_information_provider;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.IDocument;
import org.flowerplatform.codesync.line_information_provider.ILineInformationProvider;

/**
 * Mapped to {@link MethodDeclaration}.
 * 
 * @author Mariana Gheorghe
 */
public class JavaOperationLineInformationProvider implements ILineInformationProvider {

	@Override
	public int getStartLine(Object model, IDocument document) {
		MethodDeclaration node = getNode(model);
		if (node.getBody() == null) {
			return -1;
		}
		CompilationUnit cu = getRoot(node);
		return cu.getLineNumber(node.getBody().getStartPosition());
	}

	@Override
	public int getEndLine(Object model, IDocument document) {
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