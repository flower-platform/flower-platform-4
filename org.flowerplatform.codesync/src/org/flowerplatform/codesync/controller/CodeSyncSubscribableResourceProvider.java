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
package org.flowerplatform.codesync.controller;

import static org.flowerplatform.core.CoreConstants.AUTO_SUBSCRIBE_ON_EXPAND;
import static org.flowerplatform.core.CoreConstants.SUBSCRIBABLE_RESOURCES;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncSubscribableResourceProvider extends AbstractController implements IPropertiesProvider, IChildrenProvider {

	private String resource;
	
	/**
	 *@author see class
	 **/
	public CodeSyncSubscribableResourceProvider(String resource) {
		super();
		this.resource = resource;
	}
	
	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		String resourceUri = getResourceUri(node);
		String contentType = "mindmap";
		
		@SuppressWarnings("unchecked")
		List<Pair<String, String>> subscribableResources = (List<Pair<String, String>>) 
				node.getProperties().get(SUBSCRIBABLE_RESOURCES);
		if (subscribableResources == null) {
			subscribableResources = new ArrayList<Pair<String, String>>();
			node.getProperties().put(SUBSCRIBABLE_RESOURCES, subscribableResources);
		}
		Pair<String, String> subscribableResource = new Pair<String, String>(resourceUri, contentType);
		subscribableResources.add(0, subscribableResource);
		
		node.getProperties().put(CoreConstants.USE_NODE_URI_ON_NEW_EDITOR, true);
		node.getProperties().put(AUTO_SUBSCRIBE_ON_EXPAND, true);
	}

	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		Node resourceNode = CorePlugin.getInstance().getResourceService().getNode(getResourceUri(node));
		return context.getService().getChildren(resourceNode, context);
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
		return true;
	}

	/**
	 *@author see class
	 **/
	protected String getResourceUri(Node node) {
		String repo = CoreUtils.getRepoFromNode(node);
		return CoreUtils.createNodeUriWithRepo("fpp", repo, resource);
	}
	
}