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
package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.codesync.regex.State;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.UtilConstants;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Stop execution of the remaining actions if the current state is not one of the
 * accepted {@link #states}.
 * 
 * @author Elena Posea
 */
public class CheckStateAction extends RegexAction {
	
	private String[] states;

	public CheckStateAction(String csvListOfStates) {
		this.states = csvListOfStates.split(",");
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		@SuppressWarnings("unchecked")
		ArrayList<Object> stateStack = (ArrayList<Object>) param.context.get(CodeSyncRegexConstants.STATE_STACK);
		State top = (State) stateStack.get(0);
		String currentStateType = ((Node) top.node).getType();
		boolean stop = true;
		for (String state : states) {
			if (currentStateType.equals(state)) {
				stop = false;
				break;
			}
		}
		param.context.put(UtilConstants.DO_NOT_EXECUTE_OTHER_ACTIONS, stop);
	}

}
