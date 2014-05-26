package org.flowerplatform.core.node.remote;

import static org.flowerplatform.core.CoreConstants.TYPE_KEY;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.RemoteMethodInvocationListener;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.util.controller.TypeDescriptorRemote;

/**
 * @see NodeService
 * @author Cristina Constantinescu
 * @author Cristian Spiescu
 */
public class NodeServiceRemote {
	
	public List<Node> getChildren(String fullNodeId, ServiceContext<NodeService> context) {
		if (context == null) {
			context = new ServiceContext<NodeService>(getNodeService());
		} else {
			context.setService(getNodeService());
		}
		return getNodeService().getChildren(new Node(fullNodeId), context);		
	}

	/**
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 * @author Claudiu Matei
	 */
	public void setProperty(String fullNodeId, String property, Object value) {
		Node node = new Node(fullNodeId);
		CorePlugin.getInstance().getResourceService().startCommand(node.getResource(), "Set " + property + " to " + value);
		getNodeService().setProperty(node, property, value, new ServiceContext<NodeService>(getNodeService()));
	}
		
	public void unsetProperty(String fullNodeId, String property) {
		getNodeService().unsetProperty(new Node(fullNodeId), property, new ServiceContext<NodeService>(getNodeService()));	
	}
	
	/**
	 * @author Cristina Constantinescu
	 * @author Sebastian Solomon
	 * @author Claudiu Matei
	 */
	public String addChild(String parentFullNodeId, ServiceContext<NodeService> context) {
		if (context == null) {
			context = new ServiceContext<NodeService>(getNodeService());
		} else {
			context.setService(getNodeService());
		}
				
		Node parent = new Node(parentFullNodeId);
		String childType = (String) context.get(TYPE_KEY);
		if (childType == null) {
			throw new RuntimeException("Type for new child node must be provided in context!");
		}
		Node child = new Node(childType, parent.getResource(), null, null);

		CorePlugin.getInstance().getResourceService().startCommand(parent.getResource(), "Create " + childType + " node");
		
		getNodeService().addChild(parent, child, context);
		return child.getFullNodeId();
	}
	
	/**
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 * @author Claudiu Matei
	 */
	public void removeChild(String parentFullNodeId, String childFullNodeId) {
		Node childNode = new Node(childFullNodeId);
		CorePlugin.getInstance().getResourceService().startCommand(childNode.getResource(), "Remove node " + childNode.getIdWithinResource());
		getNodeService().removeChild(new Node(parentFullNodeId), childNode, new ServiceContext<NodeService>(getNodeService()));
	}
	
	public List<TypeDescriptorRemote> getRegisteredTypeDescriptors() {
		return CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getTypeDescriptorsRemote();
	}
		
	/**
	 * Sends a subtree to the client, based on the status of the client. Status of the client (i.e. <code>query</code> parameter)
	 * means the tree that the client is actually seeing (based on what nodes are expanded and collapsed).
	 */
	public NodeWithChildren refresh(FullNodeIdWithChildren query) {
		NodeWithChildren response = new NodeWithChildren();
		Node node = getNode(query.getFullNodeId());
		response.setNode(node);
		
		if (query.getVisibleChildren() == null) { 
			// no visible children on client => node not expanded, so don't continue
			// OR dummy query created for a child that isn't found in query's list of children
			return response;
		}
		
		for (Node child : getChildren(query.getFullNodeId(), null)) { // get new children list
			// search corresponding child in query
			FullNodeIdWithChildren childQuery = getChildQueryFromQuery(query, child.getFullNodeId());
			if (childQuery == null) { 
				// not found, so this node is probably newly added;
				// create a dummy query and populate it only with the fullNodeId
				// this way, the recursive algorithm will go only one level deep,
				// the node's properties will be populated, and the recurssion will stop
				childQuery = new FullNodeIdWithChildren();
				childQuery.setFullNodeId(child.getFullNodeId());
			}
			NodeWithChildren childResponse = refresh(childQuery);
			if (response.getChildren() == null) {
				response.setChildren(new ArrayList<NodeWithChildren>());
			}
			response.getChildren().add(childResponse);
		}
		return response;
	}
	
	public Node getNode(String fullNodeId) {
		Node node = new Node(fullNodeId);
		// forces population of properties
		node.getOrPopulateProperties();
		return node;
	}
	
	private FullNodeIdWithChildren getChildQueryFromQuery(FullNodeIdWithChildren query, String fullChildNodeId) {
		for (FullNodeIdWithChildren child : query.getVisibleChildren()) {
			if (child.getFullNodeId().equals(fullChildNodeId)) {
				return child;
			}
		}
		return null;
	}
	
	private NodeService getNodeService() {
		return CorePlugin.getInstance().getNodeService();
	}
	
	public void tempDeleteAfterGH279AndCo(String resourceId) {
//		TempDeleteAfterGH279AndCo.INSTANCE.addNewNode();
		RemoteMethodInvocationListener.addNewNode(resourceId);
	}
	
}
