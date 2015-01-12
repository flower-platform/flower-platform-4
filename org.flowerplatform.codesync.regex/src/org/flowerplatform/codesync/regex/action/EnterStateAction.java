package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.codesync.regex.State;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Add the current node to the top of the state stack, with current nesting level,
 * if {@link #property} is set and not empty in the node's properties.
 * 
 * @author Elena Posea
 * @author Mariana Gheorghe
 */
public class EnterStateAction extends RegexAction {

	private String property;
	
	public EnterStateAction(String property) {
		this.property = property;
	}
	
	@Override
	public void executeAction(RegexProcessingSession param) {
		Node currentNode = (Node) param.context.get(CodeSyncRegexConstants.CURRENT_NODE);
		if (property != null && nullOrEmpty(currentNode)) {
			return;
		}
		
		int currentNestingLevel = (int) param.context.get(CodeSyncRegexConstants.CURRENT_NESTING_LEVEL);
		@SuppressWarnings("unchecked")
		ArrayList<Object> stateStack = (ArrayList<Object>) param.context.get(CodeSyncRegexConstants.STATE_STACK);
		stateStack.add(0, new State(currentNestingLevel, currentNode));
	}

	private boolean nullOrEmpty(Node currentNode) {
		if (!currentNode.getProperties().containsKey(property)) {
			return true;
		}
		Object value = currentNode.getProperties().get(property);
		if (value == null || "".equals(value)) {
			return true;
		}
		return false;
	}
	
}
