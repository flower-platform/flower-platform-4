package org.flowerplatform.freeplane.controller;

import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.cloud.CloudModel.Shape;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class FreeplanePropertySetter extends PropertySetter {

	@Override
	public void setProperty(Node node, String property, Object value) {
		NodeModel nodeModel = FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());		
		switch (property) {
			case "body": 
				nodeModel.setText((String) value);
				break;
			// TODO CC: remove
			case "could_shape":
				CloudModel cloud = CloudModel.createModel(nodeModel);
				switch ((String) value) {
					case "ARC": 
						cloud.setShape(Shape.ARC);
						break;
					case "RECT": 
						cloud.setShape(Shape.RECT);
						break;
					case "STAR": 
						cloud.setShape(Shape.STAR);
						break;
				}
				break;
		}		
	}

}
