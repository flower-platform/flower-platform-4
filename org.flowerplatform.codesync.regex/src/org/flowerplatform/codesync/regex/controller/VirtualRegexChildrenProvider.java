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
package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_NAME;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.SKIP_PROVIDER;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public class VirtualRegexChildrenProvider extends AbstractController implements IChildrenProvider {

	/**
	 *@author see class
	 **/
	public VirtualRegexChildrenProvider() {
		// invoked before the persistence providers
		setOrderIndex(-10000);
	}
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {	
		if (context.getBooleanValue(SKIP_PROVIDER)) {
			return null;
		}
		
		ServiceContext<NodeService> internalContext = new ServiceContext<>(context.getService());
		internalContext.add(SKIP_PROVIDER, true);
		internalContext.add(POPULATE_WITH_PROPERTIES, true);
		
		String repo = CoreUtils.getRepoFromNode(node);
		String ssp = CorePlugin.getInstance().getVirtualNodeResourceHandler().getTypeSpecificPartFromNodeUri(node.getNodeUri());
		String baseUri = CoreUtils.createNodeUriWithRepo("fpp", repo, ssp);
		
		List<Node> children = context.getService().getChildren(CorePlugin.getInstance().getResourceService().getResourceNode(baseUri), internalContext);
		List<Node> regexChildren = new ArrayList<Node>();		
		for (Node child : children) {
			if (!((String) child.getPropertyValue(REGEX_NAME)).equals(Utils.getFragment(node.getNodeUri()))) {
				continue;
			}
			regexChildren.add(child);
		}				
		context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		return regexChildren;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {		
		return true;
	}

}