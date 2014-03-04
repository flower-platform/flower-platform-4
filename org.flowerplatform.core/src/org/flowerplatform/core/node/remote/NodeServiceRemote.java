package org.flowerplatform.core.node.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;

/**
 * @see NodeService
 * @author Cristina Constantinescu
 * @author Cristian Spiescu
 */
public class NodeServiceRemote {
	
	public List<Node> getChildren(String fullNodeId, boolean populateProperties) {
		return CorePlugin.getInstance().getNodeService().getChildren(new Node(fullNodeId), populateProperties);		
	}

	public List<PropertyDescriptor> getPropertyDescriptors(String fullNodeId) {
		return CorePlugin.getInstance().getNodeService().getPropertyDescriptors(new Node(fullNodeId));	
	}
	
	public void setProperty(String fullNodeId, String property, Object value) {
		CorePlugin.getInstance().getNodeService().setProperty(new Node(fullNodeId), property, value);	
	}
		
	public void unsetProperty(String fullNodeId, String property) {
		CorePlugin.getInstance().getNodeService().unsetProperty(new Node(fullNodeId), property);	
	}
	
	public void addChild(String parentFullNodeId, Map<String, Object> properties, String insertBeforeFullNodeId) {
		Node child = new Node((String) properties.get(CorePlugin.TYPE_KEY), (String) properties.get(CorePlugin.RESOURCE_KEY), null, null);
		CorePlugin.getInstance().getNodeService().addChild(new Node(parentFullNodeId), child, insertBeforeFullNodeId != null ? new Node(insertBeforeFullNodeId) : null);	
	}
	
	public void removeChild(String parentFullNodeId, String childFullNodeId) {
		CorePlugin.getInstance().getNodeService().removeChild(new Node(parentFullNodeId), new Node(childFullNodeId));
	}
	
	public Node getRootNode(String fullNodeId) {			
		return CorePlugin.getInstance().getNodeService().getRootNode(new Node(fullNodeId));
	}
	
	public Map<String, List<AddChildDescriptor>> getAddChildDescriptors() {
		return CorePlugin.getInstance().getNodeService().getAddChildDescriptors();
	}
	
	public Set<String> getRegisteredTypes() {
		return CorePlugin.getInstance().getNodeService().getRegisteredTypes();
	}
	
	/**
	 * Sends a subtree to the client, based on the status of the client. Status of the client (i.e. <code>query</code> parameter)
	 * means the tree that the client is actually seeing (based on what nodes are expanded and collapsed).
	 */
	public NodeWithChildren refresh(FullNodeIdWithChildren query) {
		NodeWithChildren response = new NodeWithChildren();
		Node node = new Node(query.getFullNodeId());
		response.setNode(node);
		
		// forces population of properties
		node.getOrPopulateProperties();
		
		if (query.getVisibleChildren() == null) { 
			// no visible children on client => node not expanded, so don't continue
			// OR dummy query created for a child that isn't found in query's list of children
			return response;
		}
		
		for (Node child : getChildren(query.getFullNodeId(), false)) { // get new children list
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
	
	private FullNodeIdWithChildren getChildQueryFromQuery(FullNodeIdWithChildren query, String fullChildNodeId) {
		for (FullNodeIdWithChildren child : query.getVisibleChildren()) {
			if (child.getFullNodeId().equals(fullChildNodeId)) {
				return child;
			}
		}
		return null;
	}
	
}
