package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.COLOR_BACKGROUND;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.COLOR_TEXT;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.DEFAULT_MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.DEFAULT_MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.FONT_BOLD;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.FONT_FAMILY;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.FONT_ITALIC;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.FONT_SIZE;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.ICONS;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.MIN_WIDTH;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.mindmap.MindMapNodePropertiesConstants;
import org.freeplane.features.icon.MindIcon;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;
import org.freeplane.features.nodestyle.NodeStyleModel;

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
			case FONT_FAMILY:	
				String fontFamily = (String) wrapper.getPropertyValue();
				NodeStyleModel styleModel = NodeStyleModel.createNodeStyleModel(rawNodeData);
				styleModel.setFontFamilyName(fontFamily);	
				isPropertySet = true;
				break;
			case FONT_SIZE:	
				Integer fontSize = Integer.valueOf((String) wrapper.getPropertyValue());				
				NodeStyleModel.createNodeStyleModel(rawNodeData).setFontSize(fontSize);				
				isPropertySet = true;
				break;
			case FONT_BOLD:	
				Boolean fontBold = (Boolean) wrapper.getPropertyValue();				
				NodeStyleModel.createNodeStyleModel(rawNodeData).setBold(fontBold);				
				isPropertySet = true;
				break;
			case FONT_ITALIC:	
				Boolean fontItalic = (Boolean) wrapper.getPropertyValue();				
				NodeStyleModel.createNodeStyleModel(rawNodeData).setItalic(fontItalic);				
				isPropertySet = true;
				break;
			case COLOR_TEXT:	
				int color = (int) wrapper.getPropertyValue();				
				NodeStyleModel.createNodeStyleModel(rawNodeData).setColor(new Color(color));				
				isPropertySet = true;
				break;
			case COLOR_BACKGROUND:	
				int backgroundColor = (int) wrapper.getPropertyValue();				
				NodeStyleModel.createNodeStyleModel(rawNodeData).setBackgroundColor(new Color(backgroundColor));				
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
