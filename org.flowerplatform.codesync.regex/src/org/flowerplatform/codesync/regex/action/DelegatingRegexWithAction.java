/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.regex.AbstractRegexWithAction;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Cristina Constantinescu
 */
public class DelegatingRegexWithAction extends AbstractRegexWithAction {
	
	protected Node node;
	
	// protected RegexAction actions;
	// protected List<RegexAction> actions = new ArrayList<RegexAction>();
	
	protected String regex;
		
	protected int numberOfCaptureGroups = -1;
	
	@Override
	public String getRegex() {
		if (regex == null) {
			regex = (String) node.getPropertyValue(CodeSyncRegexConstants.FULL_REGEX);
		}
		return regex;
	} 

	@Override
	public int getNumberOfCaptureGroups() {
		if (numberOfCaptureGroups == -1) {
			numberOfCaptureGroups = Pattern.compile(getRegex()).matcher("").groupCount();
		}
		return numberOfCaptureGroups;
	}
	
	@Override
	public String getName() {
		return (String) node.getPropertyValue(CoreConstants.NAME);
	}
	
	public String getRegexWithMacros() {
		return (String) node.getPropertyValue(CodeSyncRegexConstants.REGEX_WITH_MACROS);
	}
	
	@Override
	public List<RegexAction> getRegexActions() {
		ServiceContext<NodeService>context = new ServiceContext<NodeService>();
		List<Node> actionNodes = context.getService().getChildren(node, context);
		List<RegexAction> actions = new ArrayList<RegexAction>();

			for (Node actionNode : actionNodes) {
				switch (actionNode.getType()) {
				case CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE:
					String type = (String) actionNode.getPropertyValue("name");
					List<String> properties = (List<String>) actionNode.getPropertyValue("properties");

					// props
					actions.add(new CreateNodeAction(type, properties));
					break;
//				case ACTION_KEEP_SPECIFIC_INFO:
//					break;
			}
		}
		return actions;
	}
	
	public DelegatingRegexWithAction setRegexAction(RegexAction action) {
		// this.actions = action; ? // set childrens accordingly to the list of RegexActions?
		return this;
	}

	public DelegatingRegexWithAction setNode(Node node) {
		this.node = node;
		return this;
	}

	@Override
	public void executeAction(RegexProcessingSession session) {
		List<RegexAction> listOfRegexActionsAvailable = getRegexActions();
		for(RegexAction listItem : listOfRegexActionsAvailable){
			listItem.executeAction(session);
		}
	}
	
}
