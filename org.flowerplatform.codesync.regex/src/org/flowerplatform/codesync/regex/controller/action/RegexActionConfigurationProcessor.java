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
package org.flowerplatform.codesync.regex.controller.action;

import org.flowerplatform.core.config_processor.IConfigNodeProcessor;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexWithActions;

/**
 * @author Mariana Gheorghe
 */
public abstract class RegexActionConfigurationProcessor extends AbstractController implements
		IConfigNodeProcessor<RegexAction, RegexWithActions> {

	@Override
	public RegexAction processConfigNode(Node node, RegexWithActions parent, ServiceContext<NodeService> context) {
		RegexAction action = createRegexAction(node);
		parent.getRegexActions().add(action);
		return action;
	}
	
	/**
	 * Create a {@link RegexAction} with the parameters from <code>node</code>.
	 */
	protected abstract RegexAction createRegexAction(Node node);
}
