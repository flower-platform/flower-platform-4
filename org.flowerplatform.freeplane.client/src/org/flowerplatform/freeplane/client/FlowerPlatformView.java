package org.flowerplatform.freeplane.client;

import org.flowerplatform.js_client.java.JsClientJavaUtils;
import org.flowerplatform.js_client.java.node.ClientNode;
import org.freeplane.features.map.INodeView;
import org.freeplane.features.map.MapChangeEvent;
import org.freeplane.features.map.NodeChangeEvent;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.NodeModel.NodeChangeType;

/**
 * @author Valentina Bojan
 */
public class FlowerPlatformView implements INodeView {

	/**
	 * Method called when a node was changed.
	 * @author Valentina Bojan
	 */
	public void nodeChanged(NodeChangeEvent event) {
		FlowerPlatformManager flowermManager = FlowerPlatformManager.getController();
		NodeModel node = event.getNode();
		Object property = event.getProperty();
		ClientNode clientNode = node.getExtension(ClientNodeModel.class).getNode();
		
		if (property == NodeChangeType.FOLDING) {
			if (node.isFolded()) {
				// remove all children from the parent node
				flowermManager.removeAllChildren(node);
				JsClientJavaUtils.invokeJsFunction(FlowerPlatformSubscribe.nodeRegistry, "collapse", clientNode);
				
				// add a dummy child node
				node.insert(new NodeModel(node.getMap()));
			} else {
				// remove the dummy child node
				node.remove(node.getChildCount() - 1);
				
				// add the children to the parent node
				JsClientJavaUtils.invokeJsFunction(FlowerPlatformSubscribe.nodeRegistry, "expand", clientNode, null);
				flowermManager.addChildrenToParent(clientNode.getChildren(), node);
				flowermManager.addViewerToChildren(node, new FlowerPlatformView());
			}
		}
	}

	/**
	 * Method called when a map was changed.
	 * @author Valentina Bojan
	 */
	public void mapChanged(MapChangeEvent event) {
	}

	/**
	 * Method called when a node was removed.
	 * @author Valentina Bojan
	 */
	public void onNodeDeleted(NodeModel parent, NodeModel child, int index) {
	}

	/**
	 * Method called when a new node was added.
	 * @author Valentina Bojan
	 */
	public void onNodeInserted(NodeModel parent, NodeModel child, int newIndex) {
	}

	/**
	 * Method called when a node was moved.
	 * @author Valentina Bojan
	 */
	public void onNodeMoved(NodeModel oldParent, int oldIndex, NodeModel newParent, NodeModel child, int newIndex) {
	}

	/**
	 * @author Valentina Bojan
	 */
	public void onPreNodeMoved(NodeModel oldParent, int oldIndex, NodeModel newParent, NodeModel child, int newIndex) {
	}

	/**
	 * @author Valentina Bojan
	 */
	public void onPreNodeDelete(NodeModel oldParent, NodeModel selectedNode, int index) {
	}
}
