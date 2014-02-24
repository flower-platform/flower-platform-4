package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.NodePropertiesConstants.TEXT;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.DEFAULT_MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.DEFAULT_MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.ICONS;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplanePlugin;
import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.icon.MindIcon;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapBasicPropertySetter extends PropertySetter {

	private static final Pattern ICON_URL_PATTERN = Pattern.compile("((.*?/)+)(.*?).png");
		
	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper wrapper) {
		NodeModel nodeModel = getNodeModel(node);	
		
		switch (property) {
			case TEXT:
				nodeModel.setText((String) wrapper.getPropertyValue());	
				return;
			case MIN_WIDTH:
				Integer newMinValue = NodeSizeModel.NOT_SET;
				if (wrapper.getPropertyValue() == null) {
					wrapper.setPropertyValue(DEFAULT_MIN_WIDTH);					
				} else {
					newMinValue = (Integer) wrapper.getPropertyValue();
				}
				NodeSizeModel.createNodeSizeModel(nodeModel).setMinNodeWidth(newMinValue);				
				return;
			case MAX_WIDTH:	
				Integer newMaxValue = NodeSizeModel.NOT_SET;
				if (wrapper.getPropertyValue() == null) {
					wrapper.setPropertyValue(DEFAULT_MAX_WIDTH);					
				} else {
					newMaxValue = (Integer) wrapper.getPropertyValue();
				}
				NodeSizeModel.createNodeSizeModel(nodeModel).setMaxNodeWidth(newMaxValue);								
				return;
			case ICONS:
				String icons = (String) wrapper.getPropertyValue();
				nodeModel.getIcons().clear();
				if (icons != null) {					
					String[] array = icons.split("\\|");
					for (String icon : array) {
						Matcher matcher = ICON_URL_PATTERN.matcher(icon);
						if (matcher.find()) {
							nodeModel.addIcon(new MindIcon(matcher.group(3)));	
						}											
					}
				}				
				return;
		}
		
		// persist the property value in the attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);		
		if (attributeTable == null) {
			attributeTable = new NodeAttributeTableModel(nodeModel);
			nodeModel.addExtension(attributeTable);
		}		
		
		boolean set = false;
		for (Attribute attribute : attributeTable.getAttributes()) {
			if (attribute.getName().equals(property)) {
				// there was already an attribute with this value; overwrite it
				attribute.setValue(wrapper.getPropertyValue());
				set = true;
				break;
			}
		}
		if (!set) {
			// new attribute; add it
			attributeTable.getAttributes().add(new Attribute(property, wrapper.getPropertyValue()));
		}
		
		// set the property on the node instance too
		node.getOrPopulateProperties().put(property, wrapper.getPropertyValue());
	}
	
	protected NodeModel getNodeModel(Node node) {
		return FreeplanePlugin.getInstance().getFreeplaneUtils().getNodeModel(node.getIdWithinResource());
	}

	@Override
	public void unsetProperty(Node node, String property) {
		NodeModel nodeModel = getNodeModel(node);
		
		// remove attribute from the attributes table
		NodeAttributeTableModel attributeTable = (NodeAttributeTableModel) nodeModel.getExtension(NodeAttributeTableModel.class);		
		for (Attribute attribute : attributeTable.getAttributes()) {
			if (attribute.getName().equals(property)) {
				attributeTable.getAttributes().remove(attribute);
				break;
			}
		}
		
		// remove the property from the node instance too
		node.getOrPopulateProperties().remove(property);
	}

}
