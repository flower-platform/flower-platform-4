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
