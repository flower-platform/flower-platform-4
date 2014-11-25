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
