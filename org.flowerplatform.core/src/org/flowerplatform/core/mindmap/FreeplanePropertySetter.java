package org.flowerplatform.core.mindmap;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.cloud.CloudModel.Shape;
import org.freeplane.features.map.NodeModel;

/**
 * @author Cristina Constantinescu
 */
public class FreeplanePropertySetter extends PropertySetter {

	@Override
	public void setProperty(Node node, String property, Object value) {
		NodeModel nodeModel = CorePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getId());		
		switch (property) {
			case "body": 
			case "name":
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
		NodeAttributeTableModel attributeTable = NodeAttributeTableModel.getModel(nodeModel);
		boolean set = false;
		for (Attribute attribute : attributeTable.getAttributes()) {
			if (attribute.getName().equals(property)) {
				// there was already an attribute with this value; overwrite it
				attribute.setValue(property);
				set = true;
			}
		}
		if (!set) {
			// new attribute; add it
			attributeTable.getAttributes().add(new Attribute(property, property));
		}
		
		node.getOrCreateProperties().put(property, value);
	}

}
