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
package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIGS_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_EXTENSION;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_FILES_FOLDER;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Elena Posea
 *
 */
public class RegexTestMatchesController extends CodeSyncRegexSubscribableResourceProvider {
	
	@Override
	protected String getResourceUri(Node node) {
		String repo = CoreUtils.getRepoFromNode(node);
		String testFilePath = CorePlugin.getInstance().getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
		testFilePath = testFilePath.replace("$", "/" + REGEX_MATCH_FILES_FOLDER + "/") + REGEX_MATCH_EXTENSION;
		return CoreUtils.createNodeUriWithRepo("fpp", repo, REGEX_CONFIGS_FOLDER + "/" + testFilePath);
	}

}
