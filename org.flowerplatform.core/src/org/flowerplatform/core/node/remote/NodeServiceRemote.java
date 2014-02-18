package org.flowerplatform.core.node.remote;

import java.util.List;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;

/**
 * @author Cristina Constantinescu
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
		CorePlugin.getInstance().getNodeService().addChild(new Node(parentFullNodeId), properties, insertBeforeFullNodeId != null ? new Node(insertBeforeFullNodeId) : null);	
	}
	
	public void removeChild(String parentFullNodeId, String childFullNodeId) {
		CorePlugin.getInstance().getNodeService().removeChild(new Node(parentFullNodeId), new Node(childFullNodeId));
	}
	
	public Node getRootNode(String fullNodeId) {			
		return CorePlugin.getInstance().getNodeService().getRootNode(new Node(fullNodeId));
	}
	
//	public NodeWithVisibleChildren refresh(NodeWithVisibleChildren currentNodeWithVisibleChildren) {	
//		Node currentNode = currentNodeWithVisibleChildren.getNode();
//		boolean currentNodeHasVisibleChildren = currentNodeWithVisibleChildren.getVisibleChildren() != null;
//		
//		// get new data
//		NodeWithVisibleChildren newNodeWithVisibleChildren = computeNodeWithVisibleChildren(currentNode, currentNodeHasVisibleChildren);
//		
//		if (currentNodeHasVisibleChildren) {
//			// verify if node exists in list of new visibleChildren
//			for (NodeWithVisibleChildren currentChildWithVisibleChildren : currentNodeWithVisibleChildren.getVisibleChildren()) {				
//				for (int i = 0; i < newNodeWithVisibleChildren.getVisibleChildren().size(); i++) {
//					Node newChildNode = newNodeWithVisibleChildren.getVisibleChildren().get(i).getNode();
//					if (newChildNode.getIdWithinResource().equals(currentChildWithVisibleChildren.getNode().getIdWithinResource())) {	// node exists in new list -> refresh its structure also			
//						newNodeWithVisibleChildren.getVisibleChildren().set(i, refresh(currentChildWithVisibleChildren));
//						break;
//					}
//				}
//			}
//		}
//		return newNodeWithVisibleChildren;
//	}
//		
//	private NodeWithVisibleChildren computeNodeWithVisibleChildren(Node node, boolean  populateWithChildren) {	
//		NodeWithVisibleChildren newNodeWithVisibleChildren = new NodeWithVisibleChildren();
//		newNodeWithVisibleChildren.setNode(node);
//		
//		node.getOrPopulateProperties();
//			
//		// get new visible children
//		List<NodeWithVisibleChildren> visibleChildren = new ArrayList<NodeWithVisibleChildren>();
//		if (populateWithChildren) {			
//			for (Node child : getChildren(node.getFullNodeId(), false)) { // get children without properties
//				visibleChildren.add(computeNodeWithVisibleChildren(child, false)); // here we populate it
//			}
//		}
//		newNodeWithVisibleChildren.setVisibleChildren(visibleChildren);
//		return newNodeWithVisibleChildren;
//	}	
//	
}
