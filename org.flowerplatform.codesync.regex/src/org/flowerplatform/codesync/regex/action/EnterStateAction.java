package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.codesync.regex.State;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Add to current node to the top of the state stack, with to current nesting level.
 * 
 * @author Elena Posea
 */
public class EnterStateAction extends RegexAction {

	@Override
	public void executeAction(RegexProcessingSession param) {
		Node currentNode = (Node) param.context.get(CodeSyncRegexConstants.CURRENT_NODE);
		int currentNestingLevel = (int) param.context.get(CodeSyncRegexConstants.CURRENT_NESTING_LEVEL);
		@SuppressWarnings("unchecked")
		ArrayList<Object> stateStack = (ArrayList<Object>) param.context.get(CodeSyncRegexConstants.STATE_STACK);
		stateStack.add(0, new State(currentNestingLevel, currentNode));
	}

}
