package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.DEFAULT_MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.DEFAULT_MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;

import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapPropertiesProvider extends PersistencePropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {
		super.populateWithProperties(node);
		
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		NodeSizeModel nodeSizeModel = NodeSizeModel.getModel(rawNodeData);
				
		if (nodeSizeModel != null && nodeSizeModel.getMinNodeWidth() != NodeSizeModel.NOT_SET) { // property set by user, use it
			node.getProperties().put(MIN_WIDTH, nodeSizeModel.getMinNodeWidth());
		} else { // otherwise, use default value
			node.getProperties().put(MIN_WIDTH, DEFAULT_MIN_WIDTH);
		}
		if (nodeSizeModel != null && nodeSizeModel.getMaxNodeWidth() != NodeSizeModel.NOT_SET) { // property set by user, use it
			node.getProperties().put(MAX_WIDTH, nodeSizeModel.getMaxNodeWidth());
		} else { // otherwise, use default value
			node.getProperties().put(MAX_WIDTH, DEFAULT_MAX_WIDTH);
		}
	}

}
