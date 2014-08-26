package org.flowerplatform.codesync.regex.action;

import java.util.ArrayList;

import org.flowerplatform.codesync.regex.State;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class AttachNodeToCurrentStateAction extends RegexAction {

	@Override
	public void executeAction(RegexProcessingSession param) {
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		// serviceContext.getContext().putAll(((Node)param.currentNode).getProperties());
		Node currentNode = (Node) param.context.get("currentNode");
		serviceContext.setContext(currentNode.getProperties());
		State top = (State) ((ArrayList<Object>) param.context.get("stateStack")).get(0);
		// ((ArrayList<Object>)session.context.get("stateStack")).add(0, new
		// State(0, child));
		CorePlugin.getInstance().getNodeService().addChild((Node) top.node, currentNode, serviceContext);
	}

}
