/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
import org.flowerplatform.codesync.line_information_provider.ILineProvider;
import org.flowerplatform.util.Pair;

/**
 * Mapped to {@link MethodDeclaration}.
 * 
 * @author Mariana Gheorghe
 */
public class JavaOperationLineProvider implements ILineProvider {

	@Override
	public Pair<Integer, Integer> getStartEndLines(Object model, IDocument document) {
		MethodDeclaration node = getNode(model);
		if (node.getBody() == null) {
			return null;
		}
		CompilationUnit cu = getRoot(node);
		int startOffset = node.getBody().getStartPosition();
		return new Pair<Integer, Integer>(
				cu.getLineNumber(startOffset), 
				cu.getLineNumber(startOffset + node.getBody().getLength()));
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	protected MethodDeclaration getNode(Object model) {
		return (MethodDeclaration) model;
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	protected CompilationUnit getRoot(ASTNode node) {
		return (CompilationUnit) node.getRoot();
	}

	@Override
	public boolean canHandle(Object model) {
		return model instanceof MethodDeclaration;
	}

}