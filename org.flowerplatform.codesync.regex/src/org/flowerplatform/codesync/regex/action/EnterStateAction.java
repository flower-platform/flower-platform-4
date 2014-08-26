package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;

import org.flowerplatform.codesync.regex.State;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class EnterStateAction extends RegexAction {

	@Override
	public void executeAction(RegexProcessingSession param) {
		// node
		Node currentNode = (Node)param.context.get("currentNode");
		// Object currentStateNode = param.stateStack.get(0).node;
		// CorePlugin.getInstance().getNodeService().addChild((Node)currentStateNode, (Node)currentNode, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		int currentNestingLevel = (int) param.context.get("currentNestingLevel");
		ArrayList<Object> stateStack = (ArrayList<Object>) param.context.get("stateStack");
		stateStack.add(0, new State(currentNestingLevel, currentNode));
	}

}
