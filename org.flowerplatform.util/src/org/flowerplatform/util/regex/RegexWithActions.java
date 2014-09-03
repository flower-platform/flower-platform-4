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
package org.flowerplatform.util.regex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristina Constantinescu
 */
public class RegexWithActions extends AbstractRegexWithActions {

	protected List<RegexAction> actions = new ArrayList<RegexAction>();

	public RegexWithActions() {
	}
	
	public RegexWithActions(String name, String regex) {
		super();
		setName(name);
		setRegex(regex);
	}	


	public void setRegexActions(List<RegexAction> actions) {
		this.actions = actions;
	}

	public List<RegexAction> getRegexActions() {
		return actions;
	}

	@Override
	public void executeActions(RegexProcessingSession session) {
		List<RegexAction> listOfRegexActionsAvailable = getRegexActions();
		session.context.put("DO_NOT_EXECUTE_OTHER_ACTIONS", false);
		for (RegexAction listItem : listOfRegexActionsAvailable) {
			if ((boolean)session.context.get("DO_NOT_EXECUTE_OTHER_ACTIONS")) {
				break;
			}
			listItem.executeAction(session);
		}
	}

}
