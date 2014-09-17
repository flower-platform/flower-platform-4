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