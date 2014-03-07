package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;
import org.freeplane.features.styles.LogicalStyleModel;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public class MindMapPropertiesProvider extends PersistencePropertiesProvider {

	@Override
	public void populateWithProperties(Node node) {
		super.populateWithProperties(node);
		
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		NodeSizeModel nodeSizeModel = NodeSizeModel.getModel(rawNodeData);
		String styleName;	
		if (rawNodeData.getExtensions().get(LogicalStyleModel.class) != null) {
			styleName = ((LogicalStyleModel)rawNodeData.getExtensions().get(LogicalStyleModel.class)).getStyle().toString();
			node.getProperties().put("styleName", styleName);
		} else {
			styleName = null;
		}
		
		Object minWidthStyleValue = CorePlugin.getInstance().getNodeService().getStylePropertyValue(node, MIN_WIDTH);
		Object maxWidthStyleValue = CorePlugin.getInstance().getNodeService().getStylePropertyValue(node, MAX_WIDTH);
		
		if (nodeSizeModel != null && nodeSizeModel.getMinNodeWidth() != NodeSizeModel.NOT_SET) { // property set by user, use it
			node.getProperties().put(MIN_WIDTH, nodeSizeModel.getMinNodeWidth());
			
		} else { // otherwise, use style value
				node.getProperties().put(MIN_WIDTH, minWidthStyleValue);
		}
		
		if (nodeSizeModel != null && nodeSizeModel.getMaxNodeWidth() != NodeSizeModel.NOT_SET) { // property set by user, use it
			node.getProperties().put(MAX_WIDTH, nodeSizeModel.getMaxNodeWidth());
		} else { // otherwise, use style value
			node.getProperties().put(MAX_WIDTH, maxWidthStyleValue);
		} 
	}

}
