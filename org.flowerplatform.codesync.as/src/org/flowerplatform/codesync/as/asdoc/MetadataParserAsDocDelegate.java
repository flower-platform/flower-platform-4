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
package org.flowerplatform.codesync.as.asdoc;

import org.apache.flex.compiler.asdoc.IASDocComment;
import org.apache.flex.compiler.asdoc.IMetadataParserASDocDelegate;
import org.apache.flex.compiler.tree.as.IDocumentableDefinitionNode;

import antlr.Token;

/**
 * @author Mariana Gheorghe
 */
public class MetadataParserAsDocDelegate implements IMetadataParserASDocDelegate {

	@Override
	public void setCurrentASDocToken(Token asDocToken) {
		// nothing to do
	}

	@Override
	public IASDocComment afterDefinition(IDocumentableDefinitionNode definitionNode) {
		return null;
	}

	@Override
	public void clearMetadataComment(String metaDataTagName) {
		// nothing to do
	}

	@Override
	public void afterMetadata(int metaDataEndOffset) {
		// nothing to do
	}

}