package org.flowerplatform.codesync.as.asdoc;

import org.apache.flex.compiler.asdoc.IASDocComment;
import org.apache.flex.compiler.asdoc.IASParserASDocDelegate;
import org.apache.flex.compiler.asdoc.IMetadataParserASDocDelegate;
import org.apache.flex.compiler.tree.as.IDocumentableDefinitionNode;

import antlr.Token;

/**
 * Stateful implementation that collects asdoc {@link Token}s. As soon as
 * a {@link IDocumentableDefinitionNode} is created, if there is a collected
 * token, it creates an {@link AsDocComment} that will be attached to the node.
 * 
 * @author Mariana Gheorghe
 */
public class AsParserAsDocDelegate implements IASParserASDocDelegate {

	private Token asDocToken;
	
	private IMetadataParserASDocDelegate metadataParserDelegate = new MetadataParserAsDocDelegate();
	
	@Override
	public void beforeVariable() {
		// nothing to do
	}

	@Override
	public void afterVariable() {
		// nothing to do
	}

	@Override
	public void setCurrentASDocToken(Token asDocToken) {
		this.asDocToken = asDocToken;
	}

	@Override
	public IASDocComment afterDefinition(IDocumentableDefinitionNode definitionNode) {
		if (asDocToken == null) {
			return null;
		}
		IASDocComment doc = new AsDocComment(asDocToken.getText());
		// reset
		asDocToken = null;
		return doc;
	}

	@Override
	public IMetadataParserASDocDelegate getMetadataParserASDocDelegate() {
		return metadataParserDelegate;
	}

}
