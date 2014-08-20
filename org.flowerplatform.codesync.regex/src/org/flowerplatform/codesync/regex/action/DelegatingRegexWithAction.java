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

import org.flowerplatform.util.regex.AbstractRegexWithAction;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Cristina Constantinescu
 */
public class DelegatingRegexWithAction extends AbstractRegexWithAction {
	
	protected List<RegexAction> actions = new ArrayList<RegexAction>();
	
	protected String regex;
		
	protected int numberOfCaptureGroups = -1;
	
	protected String name;
	protected String regexWithMacros; 

	public void setRegex(String regex){
		this.regex = regex;
	}
	
	@Override
	public String getRegex() {
		return regex;
	} 

	public void setNumberOfCaptureGroups(int numberOfCaptureGroups){
		this.numberOfCaptureGroups = numberOfCaptureGroups;
	}
	
	@Override
	public int getNumberOfCaptureGroups() {
		return numberOfCaptureGroups;
	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name; 
	}

	public void setRegexWithMacros(String regexWithMacros) {
		this.regexWithMacros = regexWithMacros;
	}

	public String getRegexWithMacros() {
		return  regexWithMacros;
	}
	
	public void setRegexActions(List<RegexAction> actions) {
		this.actions = actions;
	}

	public List<RegexAction> getRegexActions() {
		return actions;
	}
	
	@Override
	public void executeAction(RegexProcessingSession session) {
		List<RegexAction> listOfRegexActionsAvailable = getRegexActions();
		session.DO_NOT_EXECUTE_OTHER_ACTIONS = false;
		for (RegexAction listItem : listOfRegexActionsAvailable) {
			if(session.DO_NOT_EXECUTE_OTHER_ACTIONS){
				break;
			}
			listItem.executeAction(session);
		}
	}
	
}
