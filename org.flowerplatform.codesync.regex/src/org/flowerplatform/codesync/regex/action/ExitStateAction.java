package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;

import org.flowerplatform.codesync.regex.State;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class ExitStateAction  extends RegexAction {

	@Override
	public void executeAction(RegexProcessingSession param) {
		int currentNestingLevel = (int) param.context.get("currentNestingLevel");
		ArrayList<Object> stateStack = (ArrayList<Object>) param.context.get("stateStack");
		State top = (State) stateStack.get(0);
		if (currentNestingLevel == top.level) {
			stateStack.remove(0);
		}
	}

}
