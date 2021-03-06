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
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_NAME;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.SHOW_GROUPED_BY_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.SKIP_PROVIDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.VIRTUAL_REGEX_TYPE;
import static org.flowerplatform.core.CoreConstants.HAS_CHILDREN;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Cristina Constantinescu
 */
public class RegexMatchesChildrenProvider extends AbstractController implements IChildrenProvider {

	/**
	 *@author see class
	 **/
	public RegexMatchesChildrenProvider() {
		// invoked before the persistence providers
		setOrderIndex(-10000);
	}
	
	@Override
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
		if (context.getBooleanValue(SKIP_PROVIDER)) {
			return null;
		}
		
		node.getOrPopulateProperties(new ServiceContext<>(CorePlugin.getInstance().getNodeService()));
		
		if ((boolean) node.getPropertyValue(SHOW_GROUPED_BY_REGEX)) { // the other children provider will be executed
			return null;
		}
		
		ServiceContext<NodeService> internalContext = new ServiceContext<>(context.getService());
		internalContext.add(SKIP_PROVIDER, true);
		internalContext.add(POPULATE_WITH_PROPERTIES, true);
		
		String ssp = CoreUtils.getSchemeSpecificPartWithoutRepo(node.getNodeUri());
		String repo = CoreUtils.getRepoFromNodeUri(node.getNodeUri());
		String baseUriWithRegexScheme = CorePlugin.getInstance().getVirtualNodeResourceHandler().createVirtualNodeUri(repo, VIRTUAL_REGEX_TYPE, ssp + "#");
		
		List<Node> children = context.getService().getChildren(node, internalContext);
		
		Collections.sort(children, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				return ((String) o1.getPropertyValue(REGEX_NAME)).compareTo((String) o2.getPropertyValue(REGEX_NAME));
			}		
		});				
		
		String regexRegex = "";
		List<Node> regexChildren = new ArrayList<Node>();		
		for (Node child : children) {
			if (((String) child.getPropertyValue(REGEX_NAME)).equals(regexRegex)) {
				continue;
			}
			regexRegex = (String) child.getPropertyValue(REGEX_NAME);
			String childUri = baseUriWithRegexScheme + child.getPropertyValue(REGEX_NAME);
			Node regexNode = new Node(childUri, VIRTUAL_REGEX_TYPE);
			regexNode.getProperties().put(REGEX_NAME, regexRegex);
			regexNode.getProperties().put(FULL_REGEX, child.getPropertyValue(FULL_REGEX));
			regexNode.getProperties().put(HAS_CHILDREN, true);
			regexChildren.add(regexNode);
		}
		
		context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
		return regexChildren;
	}

	@Override
	public boolean hasChildren(Node node, ServiceContext<NodeService> context) {	
		if (context.getBooleanValue(SKIP_PROVIDER)) {
			return false;
		}
		return true;
	}

}