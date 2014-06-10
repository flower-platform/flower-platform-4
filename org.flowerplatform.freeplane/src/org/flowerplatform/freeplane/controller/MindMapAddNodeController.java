package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IAddNodeController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapAddNodeController extends AbstractController implements IAddNodeController {

	@Override
	public void addNode(Node node, Node child, ServiceContext<NodeService> context) {
		NodeModel parentRawNodeData = ((NodeModel) node.getRawNodeData());
		NodeModel currentModelAtInsertionPoint = null;
		String insertBeforeFullNodeId = (String) context.get(CoreConstants.INSERT_BEFORE_FULL_NODE_ID);
		if (insertBeforeFullNodeId != null) {
			String insertBeforeId = Utils.getFragment(insertBeforeFullNodeId);
			currentModelAtInsertionPoint = parentRawNodeData.getMap().getNodeForID(insertBeforeId);
		}
		NodeModel newNodeModel = new NodeModel("", parentRawNodeData.getMap());
		newNodeModel.setLeft(parentRawNodeData.isLeft());
		
		parentRawNodeData.insert(newNodeModel, currentModelAtInsertionPoint != null ? parentRawNodeData.getChildPosition(currentModelAtInsertionPoint) : parentRawNodeData.getChildCount());
		parentRawNodeData.getMap().setSaved(false);
		
		// set the id on the node instance
		String scheme = Utils.getScheme(node.getNodeUri());
		String ssp = Utils.getSchemeSpecificPart(node.getNodeUri());
		child.setNodeUri(Utils.getUri(scheme, ssp, newNodeModel.createID()));
		child.setRawNodeData(newNodeModel);
		
		// populate also with initial properties
		child.getOrPopulateProperties();
	}

}
