package org.flowerplatform.codesync.regex.action;

import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;
import org.flowerplatform.util.regex.State;

/**
 * @author Elena Posea
 */
public class EnterStateAction extends RegexAction {

	@Override
	public void executeAction(RegexProcessingSession param) {
		// node
		Object currentNode = param.currentNode; 
		// Object currentStateNode = param.stateStack.get(0).node;
		// CorePlugin.getInstance().getNodeService().addChild((Node)currentStateNode, (Node)currentNode, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		param.stateStack.add(0, new State(param.currentNestingLevel, currentNode));
	}

}
