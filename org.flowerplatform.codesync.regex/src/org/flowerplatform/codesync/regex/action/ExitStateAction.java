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
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Remove the top node from the state stack if the current nesting level matches the top of the stack.
 * 
 * @author Elena Posea
 */
public class ExitStateAction  extends RegexAction {

	@Override
	public void executeAction(RegexProcessingSession param) {
		int currentNestingLevel = (int) param.context.get(CodeSyncRegexConstants.CURRENT_NESTING_LEVEL);
		@SuppressWarnings("unchecked")
		ArrayList<Object> stateStack = (ArrayList<Object>) param.context.get(CodeSyncRegexConstants.STATE_STACK);
		State top = (State) stateStack.get(0);
		if (currentNestingLevel == top.level) {
			stateStack.remove(0);
		}
	}

}
