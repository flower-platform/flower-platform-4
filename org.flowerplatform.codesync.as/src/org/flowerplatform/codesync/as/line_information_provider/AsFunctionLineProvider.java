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
package org.flowerplatform.codesync.as.line_information_provider;

import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.apache.flex.compiler.tree.as.IASNode;
import org.apache.flex.compiler.tree.as.IBlockNode;
import org.apache.flex.compiler.tree.as.IDefinitionNode;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.flowerplatform.codesync.line_information_provider.ILineProvider;
import org.flowerplatform.util.Pair;

/**
 * @author Mariana Gheorghe
 */
public class AsFunctionLineProvider implements ILineProvider {

	@Override
	public Pair<Integer, Integer> getStartEndLines(Object model, IDocument document) {
		IDefinitionNode node = getFunction(model).getNode();
		IBlockNode block = getBlock(node);
		if (block == null) {
			return null;
		}
		try {
			return new Pair<Integer, Integer>(
					document.getLineOfOffset(block.getStart()),
					document.getLineOfOffset(block.getEnd()));
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	private IFunctionDefinition getFunction(Object model) {
		return (IFunctionDefinition) model;
	}
	
	private IBlockNode getBlock(IDefinitionNode node) {
		for (int i = 0; i < node.getChildCount(); i++) {
			IASNode child = node.getChild(i);
			if (child instanceof IBlockNode) {
				return (IBlockNode) child;
			}
		}
		return null;
	}

	@Override
	public boolean canHandle(Object model) {
		return model instanceof IFunctionDefinition;
	}

}