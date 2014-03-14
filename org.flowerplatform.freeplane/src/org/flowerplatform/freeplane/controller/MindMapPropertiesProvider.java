package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.CLOUD_COLOR;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.CLOUD_SHAPE;
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
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.NONE;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.RECTANGLE;
import static org.flowerplatform.mindmap.MindMapNodePropertiesConstants.ROUND_RECTANGLE;

import java.awt.Color;
import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.mindmap.MindMapPlugin;
import org.freeplane.core.util.ColorUtils;
import org.freeplane.features.cloud.CloudController;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.cloud.CloudModel.Shape;
import org.freeplane.features.icon.MindIcon;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;
import org.freeplane.features.nodestyle.NodeStyleController;

/**
 * @author Cristina Constantinescu
 */
public class MindMapPropertiesProvider extends PersistencePropertiesProvider {
	
	@Override
	public void populateWithProperties(Node node) {
		super.populateWithProperties(node);
		
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		NodeSizeModel nodeSizeModel = NodeSizeModel.getModel(rawNodeData);
				
		if (nodeSizeModel != null && nodeSizeModel.getMinNodeWidth() != NodeSizeModel.NOT_SET) { // property set by user, use it
			node.getProperties().put(MIN_WIDTH, nodeSizeModel.getMinNodeWidth());
		} else { // otherwise, use default value
			node.getProperties().put(MIN_WIDTH, DEFAULT_MIN_WIDTH);
		}
		if (nodeSizeModel != null && nodeSizeModel.getMaxNodeWidth() != NodeSizeModel.NOT_SET) { // property set by user, use it
			node.getProperties().put(MAX_WIDTH, nodeSizeModel.getMaxNodeWidth());
		} else { // otherwise, use default value
			node.getProperties().put(MAX_WIDTH, DEFAULT_MAX_WIDTH);
		}
				
		List<MindIcon> icons = rawNodeData.getIcons();
		if (icons != null) {
			StringBuilder sb = new StringBuilder();
			for (MindIcon icon : icons) {
				sb.append(MindMapPlugin.getInstance().getResourceUrl(icon.getPath()));
				sb.append("|");
			}
			if (sb.length() > 0) { // remove last "|"
				node.getProperties().put(ICONS, sb.substring(0, sb.length() - 1));
			}
		}

		// get styles from node if available, or from node's style if available, or from default style
		node.getProperties().put(FONT_FAMILY, NodeStyleController.getController().getFontFamilyName(rawNodeData));
		node.getProperties().put(FONT_SIZE, NodeStyleController.getController().getFontSize(rawNodeData));
		node.getProperties().put(FONT_BOLD, NodeStyleController.getController().isBold(rawNodeData));
		node.getProperties().put(FONT_ITALIC, NodeStyleController.getController().isItalic(rawNodeData));
		
		// get text color -> sets the default color if none)
		Color color = NodeStyleController.getController().getColor(rawNodeData);
		node.getProperties().put(COLOR_TEXT, ColorUtils.colorToString(color));
		
		// get background color -> is null if no color set (doesn't get the default style value)
		color = NodeStyleController.getController().getBackgroundColor(rawNodeData);
		node.getProperties().put(COLOR_BACKGROUND, color == null ? ColorUtils.colorToString(Color.WHITE) : ColorUtils.colorToString(color));	
		
		// cloud		
		String cloudShape = NONE;
		String cloudColor = ColorUtils.colorToString(CloudController.getStandardColor());
		
		CloudModel cloudModel = CloudController.getController().getCloud(rawNodeData);		
		if (cloudModel != null) {
			cloudColor = ColorUtils.colorToString(cloudModel.getColor());
			
			Shape shape = cloudModel.getShape();
			if (Shape.RECT.equals(shape)) {
				cloudShape = RECTANGLE;
			} else if (Shape.ROUND_RECT.equals(shape)) {
				cloudShape = ROUND_RECTANGLE;
			}
		}		
		node.getProperties().put(CLOUD_COLOR, cloudColor);
		node.getProperties().put(CLOUD_SHAPE, cloudShape);
	}

}
