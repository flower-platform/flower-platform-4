package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.CoreConstants.EXECUTE_ONLY_FOR_UPDATER;
import static org.flowerplatform.mindmap.MindMapConstants.CLOUD_COLOR;
import static org.flowerplatform.mindmap.MindMapConstants.CLOUD_SHAPE;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_TEXT;
import static org.flowerplatform.mindmap.MindMapConstants.DEFAULT_MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.DEFAULT_MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_BOLD;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_FAMILY;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_ITALIC;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_SIZE;
import static org.flowerplatform.mindmap.MindMapConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_ROUND_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.STYLE_NAME;
import static org.flowerplatform.mindmap.MindMapConstants.TEXT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.core.util.ColorUtils;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.cloud.CloudModel.Shape;
import org.freeplane.features.icon.MindIcon;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;
import org.freeplane.features.nodestyle.NodeStyleModel;
import org.freeplane.features.styles.IStyle;
import org.freeplane.features.styles.LogicalStyleModel;
import org.freeplane.features.styles.MapStyleModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapPropertySetter extends PersistencePropertySetter {

	private static final Pattern ICON_URL_PATTERN = Pattern.compile("((.*?/)+)(.*?).png");
	
	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper wrapper, ServiceContext context) {
		if (context.getValue(CoreConstants.EXECUTE_ONLY_FOR_UPDATER)) {
			return;
		}
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		
		boolean isPropertySet = false;
		// if null -> no additional updates
		// if empty -> additional updates for all properties
		List<String> addAdditionalSetPropertyUpdatesFor = null;
		
		switch (property) {
			case TEXT:
				rawNodeData.setText((String) wrapper.getPropertyValue());
				isPropertySet = true;
				break;
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
			case CoreConstants.ICONS:
				String icons = (String) wrapper.getPropertyValue();
				rawNodeData.getIcons().clear();
				if (icons != null) {					
					String[] array = icons.split(CoreConstants.ICONS_SEPARATOR);
					for (String icon : array) {
						Matcher matcher = ICON_URL_PATTERN.matcher(icon);
						if (matcher.find()) {
							rawNodeData.addIcon(new MindIcon(matcher.group(3)));	
						}											
					}
				}
				
				isPropertySet = true;
				break;
			case "note":
				String note = (String) wrapper.getPropertyValue();
				if (note.length() > 0) {
					// TODO
				} else {
					// TODO
				}
				node.getProperties().put("note", note);
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
				String color = (String) wrapper.getPropertyValue();				
				NodeStyleModel.createNodeStyleModel(rawNodeData).setColor(ColorUtils.stringToColor(color));				
				isPropertySet = true;
				break;
			case COLOR_BACKGROUND:	
				String backgroundColor = (String) wrapper.getPropertyValue();				
				NodeStyleModel.createNodeStyleModel(rawNodeData).setBackgroundColor(ColorUtils.stringToColor(backgroundColor));				
				isPropertySet = true;
				break;
			case CLOUD_COLOR:
				String cloudColor = (String) wrapper.getPropertyValue();				
				CloudModel.createModel(rawNodeData).setColor(ColorUtils.stringToColor(cloudColor));				
				isPropertySet = true;
				break;
			case CLOUD_SHAPE:
				String cloudShape = (String) wrapper.getPropertyValue();
				Shape shape = null;
				// get shape correspondence from freeplane
				switch (cloudShape) {
					case SHAPE_RECTANGLE:
						shape = Shape.RECT;
						break;
					case SHAPE_ROUND_RECTANGLE:
						shape = Shape.ROUND_RECT;
						break;				
				}
				if (shape != null) {
					CloudModel.createModel(rawNodeData).setShape(shape);
				} else { // no shape -> remove cloud from node
					rawNodeData.removeExtension(CloudModel.class);
				}
				isPropertySet = true;
				break;
			case STYLE_NAME:
				String styleName = (String) wrapper.getPropertyValue();
				MapModel mapModel = (MapModel) CorePlugin.getInstance().getResourceService().getRawResourceData(node.getResource());
				
				Set<IStyle> styles = MapStyleModel.getExtension(mapModel).getStyles();
				IStyle style = null;
				for(IStyle availableStyle : styles) {
					if (availableStyle.toString().equals(styleName)) {
						style = availableStyle;
						break;
					}
				}	
				if (style != null) {
					LogicalStyleModel model = LogicalStyleModel.createExtension(rawNodeData);
					model.setStyle(style);
				} else {
					rawNodeData.removeExtension(LogicalStyleModel.class);
				}
				isPropertySet = true;
				addAdditionalSetPropertyUpdatesFor = new ArrayList<String>();
				break;
		}
				
		if (!isPropertySet) {
			super.setProperty(node, property, wrapper, context);
		} else {
			rawNodeData.getMap().setSaved(false);
			
			// set the property on the node instance too
			node.getOrPopulateProperties().put(property, wrapper.getPropertyValue());
		}
		
		if (addAdditionalSetPropertyUpdatesFor != null) {
			if (addAdditionalSetPropertyUpdatesFor.isEmpty()) {
				for (Map.Entry<String, Object> entry : node.getOrPopulateProperties().entrySet()) {
					CorePlugin.getInstance().getNodeService().setProperty(node, entry.getKey(), entry.getValue(), new ServiceContext().add(EXECUTE_ONLY_FOR_UPDATER, true));
				}	 
			} else {
				for (String entry : addAdditionalSetPropertyUpdatesFor) {
					CorePlugin.getInstance().getNodeService().setProperty(node, entry, node.getOrPopulateProperties().get(entry), new ServiceContext().add(EXECUTE_ONLY_FOR_UPDATER, true));
				}
			}
		}
	}
	
	@Override
	public void unsetProperty(Node node, String property, ServiceContext serviceContext) {
		if (serviceContext.getValue(CoreConstants.EXECUTE_ONLY_FOR_UPDATER)) {
			return;
		}
		
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		
		boolean isPropertyUnset = false;
		// if null -> no additional updates
		// if empty -> additional updates for all properties
		List<String> addAdditionalUnsetPropertyUpdatesFor = null;
				
		switch (property) {			
			case MIN_WIDTH:
				((NodeSizeModel)rawNodeData.getExtension(NodeSizeModel.class)).setMinNodeWidth(NodeSizeModel.NOT_SET);
				isPropertyUnset = true;
				break;
			case MAX_WIDTH:
				((NodeSizeModel)rawNodeData.getExtension(NodeSizeModel.class)).setMaxNodeWidth(NodeSizeModel.NOT_SET);
				isPropertyUnset = true;
				break;
			case COLOR_BACKGROUND:
				((NodeStyleModel)rawNodeData.getExtension(NodeStyleModel.class)).setBackgroundColor(null);
				isPropertyUnset = true;
				break;
			case COLOR_TEXT:
				((NodeStyleModel)rawNodeData.getExtension(NodeStyleModel.class)).setColor(null);
				isPropertyUnset = true;
				break;
			case FONT_BOLD:
				((NodeStyleModel)rawNodeData.getExtension(NodeStyleModel.class)).setBold(null);
				isPropertyUnset = true;
				break;
			case FONT_FAMILY:
				((NodeStyleModel)rawNodeData.getExtension(NodeStyleModel.class)).setFontFamilyName(null);
				isPropertyUnset = true;
				break;
			case FONT_ITALIC:
				((NodeStyleModel)rawNodeData.getExtension(NodeStyleModel.class)).setItalic(null);
				isPropertyUnset = true;
				break;
			case FONT_SIZE:
				((NodeStyleModel)rawNodeData.getExtension(NodeStyleModel.class)).setFontSize(null);
				isPropertyUnset = true;
				break;
			case CLOUD_COLOR:
				CloudModel.createModel(rawNodeData).setColor(null);			
				isPropertyUnset = true;
				break;
			case CLOUD_SHAPE:
				rawNodeData.removeExtension(CloudModel.class);
				isPropertyUnset = true;
				break;
			case CoreConstants.ICONS:
				int length = rawNodeData.getIcons().size();
				for (int i = 0; i < length; i++) {
					rawNodeData.removeIcon();
				}
				isPropertyUnset = true;
				break;
			case STYLE_NAME:
				rawNodeData.removeExtension(LogicalStyleModel.class);
				addAdditionalUnsetPropertyUpdatesFor = new ArrayList<>();
				isPropertyUnset = true;
				break;
			default:
				break;
		}
		if (!isPropertyUnset) {
			super.unsetProperty(node, property, serviceContext);
		} else {
			rawNodeData.getMap().setSaved(false);			
			node.getOrPopulateProperties();
		}
		
		if (addAdditionalUnsetPropertyUpdatesFor != null) {
			if (addAdditionalUnsetPropertyUpdatesFor.isEmpty()) {
				for (Map.Entry<String, Object> entry : node.getOrPopulateProperties().entrySet()) {
					CorePlugin.getInstance().getNodeService().unsetProperty(node, entry.getKey(), new ServiceContext().add(EXECUTE_ONLY_FOR_UPDATER, true));
				}	 
			} else {
				for (String entry : addAdditionalUnsetPropertyUpdatesFor) {
					CorePlugin.getInstance().getNodeService().unsetProperty(node, entry, new ServiceContext().add(EXECUTE_ONLY_FOR_UPDATER, true));
				}
			}
		}
	}
		
}
