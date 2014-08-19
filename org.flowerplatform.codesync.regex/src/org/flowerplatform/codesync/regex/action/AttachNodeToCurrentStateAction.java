package org.flowerplatform.codesync.regex.action;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class AttachNodeToCurrentStateAction extends RegexAction{
	
	@Override
	public void executeAction(RegexProcessingSession param) {
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
//		serviceContext.getContext().putAll(((Node)param.currentNode).getProperties());
		serviceContext.setContext(((Node)param.currentNode).getProperties());
		CorePlugin.getInstance().getNodeService().addChild((Node) param.stateStack.get(0).node, (Node) param.currentNode, serviceContext);
	}

}
