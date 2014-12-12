package org.flowerplatform.codesync.regex.action;

import java.util.List;

import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * Attach the value stored in the context under {@link #attachInfoKey} to the current node, 
 * either as a property in the properties map, or to the children list, depending on the
 * {@link #isContainment} flag.
 * 
 * @author Elena Posea
 * @author Mariana Gheorghe
 */
public class AttachSpecificInfoAction extends RegexAction {

	private String attachInfoKey;
	private Boolean isContainment;

	public AttachSpecificInfoAction(String attachInfoKey, Boolean isContainment) {
		this.attachInfoKey = attachInfoKey;
		this.isContainment = isContainment;
	}

	@Override
	public void executeAction(RegexProcessingSession param) {
		Node currentNode = (Node) param.context.get(CodeSyncRegexConstants.CURRENT_NODE);
		Object value = param.context.get(attachInfoKey);
		if (isContainment) {
			// attach info as children
			if (value instanceof List) {
				// add the whole list to the children list
				@SuppressWarnings("unchecked")
				List<Object> listOfChildrenToBeAdded = (List<Object>) value;
				for (Object child : listOfChildrenToBeAdded) {
					addChild(currentNode, (Node) child);
				}
			} else {
				addChild(currentNode, (Node) value);
			}
		} else {
			// add info in the properties map
			ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
			CorePlugin.getInstance().getNodeService().setProperty(currentNode, attachInfoKey, value, serviceContext);
		}
	}
	
	private void addChild(Node parent, Node child) {
		ServiceContext<NodeService> serviceContext = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		// make sure to copy properties
		serviceContext.setContext(child.getProperties());
		CorePlugin.getInstance().getNodeService().addChild(parent, (Node) child, serviceContext);
	}

}
