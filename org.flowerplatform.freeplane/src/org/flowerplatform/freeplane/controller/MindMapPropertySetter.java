package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapPropertySetter extends PropertySetter {

	@Override
	public void setProperty(Node node, String property, Object value) {
		NodeModel nodeModel = getNodeModel(node);		
		switch (property) {
			case "min_width":
				Integer newMinWidth;
				try {
					newMinWidth = Integer.valueOf((String) value);
				} catch (Exception e) {
					newMinWidth = NodeSizeModel.NOT_SET;
				}
				NodeSizeModel.createNodeSizeModel(nodeModel).setMinNodeWidth(newMinWidth);				
				break;
			case "max_width":	
				Integer newMaxWidth;
				try {
					newMaxWidth = Integer.valueOf((String) value);
				} catch (Exception e) {
					newMaxWidth = NodeSizeModel.NOT_SET;
				}
				NodeSizeModel.createNodeSizeModel(nodeModel).setMaxNodeWidth(newMaxWidth);								
				break;
		}
	}

	@Override
	public void unsetProperty(Node node, String property) {		
	}
	
	protected NodeModel getNodeModel(Node node) {
		return FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());
	}

}
