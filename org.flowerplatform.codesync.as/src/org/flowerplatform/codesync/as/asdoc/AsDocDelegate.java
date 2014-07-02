package org.flowerplatform.codesync.as.asdoc;

import org.apache.flex.compiler.asdoc.IASDocComment;
import org.apache.flex.compiler.asdoc.IASDocDelegate;
import org.apache.flex.compiler.asdoc.IASParserASDocDelegate;
import org.apache.flex.compiler.asdoc.IPackageDITAParser;
import org.apache.flex.compiler.common.ISourceLocation;
import org.apache.flex.compiler.definitions.IDocumentableDefinition;

/**
 * @author Mariana Gheorghe
 */
public class AsDocDelegate implements IASDocDelegate {

	private IASParserASDocDelegate parserDelegate = new AsParserAsDocDelegate();
	
	@Override
	public IASParserASDocDelegate getASParserASDocDelegate() {
		return parserDelegate;
	}

	@Override
	public IASDocComment createASDocComment(ISourceLocation location, IDocumentableDefinition definition) {
		return null;
	}

	@Override
	public IPackageDITAParser getPackageDitaParser() {
		return null;
	}

}
