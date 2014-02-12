package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapPropertySetter extends MindMapBasicPropertySetter {

	@Override
	public void setProperty(Node node, String property, Object value) {
		super.setProperty(node, property, value);
		
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

}
