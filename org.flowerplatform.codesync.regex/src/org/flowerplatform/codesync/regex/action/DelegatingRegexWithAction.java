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
package org.flowerplatform.codesync.regex.action;

import java.util.regex.Pattern;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.codesync.regex.CodeSyncRegexPlugin;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.AbstractRegexWithAction;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Cristina Constantinescu
 */
public class DelegatingRegexWithAction extends AbstractRegexWithAction {
	
	protected Node node;
	
	protected RegexAction action;
	
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
	public RegexAction getRegexAction() {
		if (action == null) {
			action = CodeSyncRegexPlugin.getInstance().getActions().get((String) node.getPropertyValue(CodeSyncRegexConstants.ACTION));
		}
		return action;
	}
	
	/**
	 *@author see class
	 **/
	public DelegatingRegexWithAction setRegexAction(RegexAction actionValue) {
		this.action = actionValue;
		return this;
	}

	/**
	 *@author see class
	 **/
	public DelegatingRegexWithAction setNode(Node givenNode) {
		this.node = givenNode;
		return this;
	}

	@Override
	public void executeAction(RegexProcessingSession session) {
		getRegexAction().executeAction(session);
	}
	
}