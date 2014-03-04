package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.DEFAULT_MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.DEFAULT_MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.ICONS;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.icon.MindIcon;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapPropertySetter extends PersistencePropertySetter {

	private static final Pattern ICON_URL_PATTERN = Pattern.compile("((.*?/)+)(.*?).png");
	
	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper wrapper) {
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		
		boolean isPropertySet = false;
		switch (property) {			
			case MIN_WIDTH:
				Integer newMinValue = NodeSizeModel.NOT_SET;
				if (wrapper.getPropertyValue() == null) {
					wrapper.setPropertyValue(DEFAULT_MIN_WIDTH);					
				} else {
					newMinValue = (Integer) wrapper.getPropertyValue();
				}
				NodeSizeModel.createNodeSizeModel(rawNodeData).setMinNodeWidth(newMinValue);		
				isPropertySet = true;
				break;
			case MAX_WIDTH:	
				Integer newMaxValue = NodeSizeModel.NOT_SET;
				if (wrapper.getPropertyValue() == null) {
					wrapper.setPropertyValue(DEFAULT_MAX_WIDTH);					
				} else {
					newMaxValue = (Integer) wrapper.getPropertyValue();
				}
				NodeSizeModel.createNodeSizeModel(rawNodeData).setMaxNodeWidth(newMaxValue);	
				isPropertySet = true;
				break;
			case ICONS:
				String icons = (String) wrapper.getPropertyValue();
				rawNodeData.getIcons().clear();
				if (icons != null) {					
					String[] array = icons.split("\\|");
					for (String icon : array) {
						Matcher matcher = ICON_URL_PATTERN.matcher(icon);
						if (matcher.find()) {
							rawNodeData.addIcon(new MindIcon(matcher.group(3)));	
						}											
					}
				}
				isPropertySet = true;
				break;
		}
				
		if (!isPropertySet) {
			super.setProperty(node, property, wrapper);
		} else {
			// set the property on the node instance too
			node.getOrPopulateProperties().put(property, wrapper.getPropertyValue());
		}
	}
		
}