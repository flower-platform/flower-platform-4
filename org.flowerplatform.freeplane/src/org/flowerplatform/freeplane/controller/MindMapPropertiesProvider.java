package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.mindmap.MindMapConstants.CLOUD_COLOR;
import static org.flowerplatform.mindmap.MindMapConstants.CLOUD_SHAPE;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_TEXT;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_BOLD;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_FAMILY;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_ITALIC;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_SIZE;
import static org.flowerplatform.mindmap.MindMapConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_NONE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_ROUND_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.TEXT;

import java.awt.Color;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.resources.ResourcesPlugin;
import org.freeplane.core.util.ColorUtils;
import org.freeplane.features.cloud.CloudController;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.cloud.CloudModel.Shape;
import org.freeplane.features.icon.MindIcon;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;
import org.freeplane.features.nodestyle.NodeStyleController;
import org.freeplane.features.note.NoteModel;
import org.freeplane.features.styles.LogicalStyleModel;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public class MindMapPropertiesProvider extends PersistencePropertiesProvider {
	
	@Override
	public void populateWithProperties(Node node, ServiceContext context) {
		super.populateWithProperties(node, context);
		
		NodeModel rawNodeData = ((NodeModel) node.getOrRetrieveRawNodeData());
		
		node.getProperties().put(TEXT, rawNodeData.getText());
		node.getProperties().put(CoreConstants.SIDE, rawNodeData.isLeft() ? CoreConstants.POSITION_LEFT : CoreConstants.POSITION_RIGHT);
		
		NodeSizeModel nodeSizeModel = NodeSizeModel.getModel(rawNodeData);
		
		String styleName;	
		if (rawNodeData.getExtensions().get(LogicalStyleModel.class) != null) {
			styleName = ((LogicalStyleModel)rawNodeData.getExtensions().get(LogicalStyleModel.class)).getStyle().toString();
			node.getProperties().put("styleName", styleName);
		} else { //for style nodes
			node.getProperties().put("styleName", node.getProperties().get(TEXT));
		}
		
		if (nodeSizeModel != null && nodeSizeModel.getMinNodeWidth() != NodeSizeModel.NOT_SET) { // property set by user, use it
			node.getProperties().put(MIN_WIDTH, nodeSizeModel.getMinNodeWidth());
		} else { // otherwise, use style value
			node.getProperties().put(MIN_WIDTH, node.getPropertyValue(MIN_WIDTH));
		}
		
		if (nodeSizeModel != null && nodeSizeModel.getMaxNodeWidth() != NodeSizeModel.NOT_SET) { // property set by user, use it
			node.getProperties().put(MAX_WIDTH, nodeSizeModel.getMaxNodeWidth());
		} else { // otherwise, use style value
			node.getProperties().put(MAX_WIDTH, node.getPropertyValue(MAX_WIDTH));
		} 
				
		List<MindIcon> icons = rawNodeData.getIcons();
		if (icons != null) {
			StringBuilder sb = new StringBuilder();
			for (MindIcon icon : icons) {
				sb.append(ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/" + icon.getFileName()));
				sb.append(CoreConstants.ICONS_SEPARATOR);
			}
			
			// notes icon
			if (NoteModel.getNoteText(rawNodeData) != null && NoteModel.getNoteText(rawNodeData).length() > 0) {
				sb.append(ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/" + "knotes.png"));
				sb.append(CoreConstants.ICONS_SEPARATOR);
			}
			
			if (sb.length() > 0) { // remove last icons separator
				node.getProperties().put(CoreConstants.ICONS, sb.substring(0, sb.length() - 1));
			} else {
				node.getProperties().put(CoreConstants.ICONS, null);
			}
		} else {
			node.getProperties().put(CoreConstants.ICONS, node.getPropertyValue(CoreConstants.ICONS));
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
		
		if (color != null) {
			node.getProperties().put(COLOR_BACKGROUND, ColorUtils.colorToString(color));
		} else {
			node.getProperties().put(COLOR_BACKGROUND, node.getPropertyValue(COLOR_BACKGROUND));
		}
		// cloud		
		String cloudShape = SHAPE_NONE;
		String standardColor = ColorUtils.colorToString(CloudController.getStandardColor());
		CloudModel cloudModel = CloudController.getController().getCloud(rawNodeData);		
		if (cloudModel != null) {
			String cloudColor = ColorUtils.colorToString(cloudModel.getColor());
			if (!standardColor.equals(cloudColor)) {
				node.getProperties().put(CLOUD_COLOR, cloudColor);
			}
			
			Shape shape = cloudModel.getShape();
			if (Shape.RECT.equals(shape)) {
				cloudShape = SHAPE_RECTANGLE;
			} else if (Shape.ROUND_RECT.equals(shape)) {
				cloudShape = SHAPE_ROUND_RECTANGLE;
			}
			node.getProperties().put(CLOUD_SHAPE, cloudShape);
		}
		String propertyColor = (String)node.getPropertyValue(CLOUD_COLOR);
		node.getProperties().put(CLOUD_COLOR, propertyColor == null ? standardColor : propertyColor );
		node.getProperties().put(CLOUD_SHAPE, node.getPropertyValue(CLOUD_SHAPE));
		
		// note
		String text = NoteModel.getNoteText(rawNodeData);
		if (text != null) {
			node.getProperties().put("note", text);
		}
	}

}
