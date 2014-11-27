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

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.FULL_REGEX;
import static org.flowerplatform.core.CoreConstants.NAME;

import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexWithActions;

/**
 * @author Elena Posea
 */
public class RegexWithActionsProcessor extends AbstractController implements IConfigNodeProcessor<RegexWithActions, RegexConfiguration> {

	@Override
	public RegexWithActions processConfigNode(Node node, RegexConfiguration parentProcessedDataStructure, ServiceContext<NodeService> context) {
		RegexWithActions rwa = new RegexWithActions((String) node.getPropertyValue(NAME),
				(String) node.getPropertyValue(FULL_REGEX));
		parentProcessedDataStructure.add(rwa);
		return rwa;
	}

}
