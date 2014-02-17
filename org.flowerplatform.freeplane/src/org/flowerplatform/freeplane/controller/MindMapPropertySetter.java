package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapPlugin.DEFAULT_MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapPlugin.DEFAULT_MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapPlugin.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapPlugin.MIN_WIDTH;

import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapPropertySetter extends MindMapBasicPropertySetter {

	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper wrapper) {
		super.setProperty(node, property, wrapper);
						
		NodeModel nodeModel = getNodeModel(node);		
		switch (property) {
			case MIN_WIDTH:
				Integer newMinValue = NodeSizeModel.NOT_SET;
				if (wrapper.getPropertyValue() == null) {
					wrapper.setPropertyValue(DEFAULT_MIN_WIDTH);					
				} else {
					newMinValue = (Integer) wrapper.getPropertyValue();
				}
				NodeSizeModel.createNodeSizeModel(nodeModel).setMinNodeWidth(newMinValue);				
				break;
			case MAX_WIDTH:	
				Integer newMaxValue = NodeSizeModel.NOT_SET;
				if (wrapper.getPropertyValue() == null) {
					wrapper.setPropertyValue(DEFAULT_MAX_WIDTH);					
				} else {
					newMaxValue = (Integer) wrapper.getPropertyValue();
				}
				NodeSizeModel.createNodeSizeModel(nodeModel).setMaxNodeWidth(newMaxValue);								
				break;			
		}
	} 

}
