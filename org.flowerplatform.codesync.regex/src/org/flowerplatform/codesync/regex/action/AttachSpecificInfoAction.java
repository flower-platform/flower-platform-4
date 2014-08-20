package org.flowerplatform.codesync.regex.action;

import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Elena Posea
 */
public class AttachSpecificInfoAction extends RegexAction {

	String attachInfoKey;
	Boolean isContainment;

	public AttachSpecificInfoAction(String attachInfoKey, Boolean isContainment) {
		this.attachInfoKey = attachInfoKey;
		this.isContainment = isContainment;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		Node currentNode = (Node) param.currentNode;
		ServiceContext<NodeService> serviceContext;
		if (isContainment) {
			// attach info as children
			Object currentValue = param.specificInfo.get(attachInfoKey);
			if(currentValue instanceof List){
				List<Object> listOfChildrenToBeAdded = (List<Object>) currentValue;
				for(Object child : listOfChildrenToBeAdded){
					serviceContext = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
					//serviceContext.getContext().putAll(((Node)child).getProperties());
					serviceContext.setContext(((Node)child).getProperties());
					CorePlugin.getInstance().getNodeService().addChild(currentNode, (Node) child, serviceContext);
				}
			}
		} else {
			// attach info as properties in the properties map
			serviceContext = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
			CorePlugin.getInstance().getNodeService().setProperty(currentNode, attachInfoKey, param.specificInfo.get(attachInfoKey), serviceContext);
		}
	}

}
