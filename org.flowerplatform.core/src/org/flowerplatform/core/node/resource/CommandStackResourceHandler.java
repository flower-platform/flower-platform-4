package org.flowerplatform.core.node.resource;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Utils;

/**
 * @author Claudiu Matei
 */
public class CommandStackResourceHandler implements IResourceHandler {

	@Override
	public String getResourceUri(String nodeUri) {
		return Utils.getUri(CoreConstants.COMMAND_STACK_SCHEME, Utils.getSchemeSpecificPart(nodeUri), null);
	}

	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {
		return null;
	}

	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {
		String type = Utils.getFragment(nodeUri) == null ? CoreConstants.COMMAND_STACK_TYPE : CoreConstants.COMMAND_TYPE; 
		return new Node(nodeUri, type);
	}

	@Override
	public Object load(String resourceUri) throws Exception {
		return null;
	}

	@Override
	public void save(Object resourceData) throws Exception {
		// nothing to do
	}

	@Override
	public boolean isDirty(Object resourceData) {
		return false;
	}

	@Override
	public void unload(Object resourceData) throws Exception {
		// nothing to do
	}

	/**
	 * @author Claudiu Matei
	 */
	public Node createCommandStackNode(String resourceSet) {
		String commandStackUri = Utils.getUri(CoreConstants.COMMAND_STACK_SCHEME, resourceSet);
		Node commandStackNode = new Node(commandStackUri, CoreConstants.COMMAND_STACK_TYPE);
		return commandStackNode;
	}
	
	/**
	 * @author Claudiu Matei
	 */
	public Node createCommandNode(String resourceSet, String commandId) {
		String commandUri = Utils.getUri(CoreConstants.COMMAND_STACK_SCHEME, resourceSet, commandId);
		Node commandNode = new Node(commandUri, CoreConstants.COMMAND_TYPE);
		return commandNode;
	}

	/**
	 * @author Claudiu Matei
	 */
	public String getResourceSetFromCommandStackNode(Node node) {
		return node.getNodeUri().substring(CoreConstants.COMMAND_STACK_SCHEME.length() + 1);
	}

}
