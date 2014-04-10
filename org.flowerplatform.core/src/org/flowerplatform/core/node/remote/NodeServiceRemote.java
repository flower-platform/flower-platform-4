package org.flowerplatform.core.node.remote;

import static org.flowerplatform.core.CoreConstants.INSERT_BEFORE_FULL_NODE_ID;
import static org.flowerplatform.core.CoreConstants.INSERT_BEFORE_NODE;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;
import static org.flowerplatform.core.CoreConstants.TYPE_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.util.controller.TypeDescriptorRemote;

/**
 * @see NodeService
 * @author Cristina Constantinescu
 * @author Cristian Spiescu
 */
public class NodeServiceRemote {
	
	public List<Node> getChildren(String fullNodeId, boolean populateProperties) {
		return CorePlugin.getInstance().getNodeService().getChildren(new Node(fullNodeId), new ServiceContext().add(POPULATE_WITH_PROPERTIES, populateProperties));		
	}
	
	public void setProperty(String fullNodeId, String property, Object value) {
		CorePlugin.getInstance().getNodeService().setProperty(new Node(fullNodeId), property, value, new ServiceContext());	
	}
		
	public void unsetProperty(String fullNodeId, String property) {
		CorePlugin.getInstance().getNodeService().unsetProperty(new Node(fullNodeId), property, new ServiceContext());	
	}
	
	/**
	 * @author Cristina Constantinescu
	 * @author Sebastian Solomon
	 */
	public String addChild(String parentFullNodeId, Map<String, Object> properties, String insertBeforeFullNodeId) {
		Node parent = new Node(parentFullNodeId);
		
		Node child = new Node((String) properties.get(TYPE_KEY), parent.getType().equals(CoreConstants.FILE_SYSTEM_NODE_TYPE) ? parent.getFullNodeId() : parent.getResource(), null, null);
		ServiceContext context = new ServiceContext();

		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			if (entry.getKey().equals(INSERT_BEFORE_FULL_NODE_ID)) {
				context.add(INSERT_BEFORE_NODE, new Node((String)entry.getValue()));
			} else {
				context.add(entry.getKey(), entry.getValue());
			}
		}
		CorePlugin.getInstance().getNodeService().addChild(parent, child, context);
		return child.getFullNodeId();
	}
	
	public void removeChild(String parentFullNodeId, String childFullNodeId) {
		CorePlugin.getInstance().getNodeService().removeChild(new Node(parentFullNodeId), new Node(childFullNodeId), new ServiceContext());
	}
	
	public List<TypeDescriptorRemote> getRegisteredTypeDescriptors() {
		return CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getTypeDescriptorsRemote();
	}
	
	public void saveResource(String resourceNodeId) {
		System.out.println("saveResource " + resourceNodeId);
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
	
}
