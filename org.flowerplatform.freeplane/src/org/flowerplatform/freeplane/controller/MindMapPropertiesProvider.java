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
import java.util.List;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.mindmap.MindMapPlugin;
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
		node.getProperties().put(COLOR_TEXT, color.getRGB());
		
		// get background color -> is null if no color set (doesn't get the default style value)
		color = NodeStyleController.getController().getBackgroundColor(rawNodeData);
		node.getProperties().put(COLOR_BACKGROUND, color == null ? Color.WHITE.getRGB() : color.getRGB());		
	}

}
