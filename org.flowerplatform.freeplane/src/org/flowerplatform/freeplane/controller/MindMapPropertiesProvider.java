/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.CoreConstants.ICONS;
import static org.flowerplatform.mindmap.MindMapConstants.CLOUD_COLOR;
import static org.flowerplatform.mindmap.MindMapConstants.CLOUD_SHAPE;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_TEXT;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_COLOR;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_STYLE;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_BOLD;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_FAMILY;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_ITALIC;
import static org.flowerplatform.mindmap.MindMapConstants.FONT_SIZE;
import static org.flowerplatform.mindmap.MindMapConstants.MAX_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.MIN_WIDTH;
import static org.flowerplatform.mindmap.MindMapConstants.NODE_DETAILS;
import static org.flowerplatform.mindmap.MindMapConstants.NOTE;
import static org.flowerplatform.mindmap.MindMapConstants.POSITION_LEFT;
import static org.flowerplatform.mindmap.MindMapConstants.POSITION_RIGHT;
import static org.flowerplatform.mindmap.MindMapConstants.SIDE;
import static org.flowerplatform.mindmap.MindMapConstants.STYLE_NAME;
import static org.flowerplatform.mindmap.MindMapConstants.TEXT;

import java.awt.Color;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.remote.StylePropertyWrapper;
import org.flowerplatform.resources.ResourcesPlugin;
import org.freeplane.core.util.ColorUtils;
import org.freeplane.features.cloud.CloudController;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.edge.EdgeController;
import org.freeplane.features.edge.EdgeModel;
import org.freeplane.features.edge.EdgeStyle;
import org.freeplane.features.icon.MindIcon;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;
import org.freeplane.features.nodestyle.NodeStyleController;
import org.freeplane.features.nodestyle.NodeStyleModel;
import org.freeplane.features.note.NoteModel;
import org.freeplane.features.styles.IStyle;
import org.freeplane.features.styles.LogicalStyleModel;
import org.freeplane.features.styles.MapStyleModel;
import org.freeplane.features.text.DetailTextModel;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public class MindMapPropertiesProvider extends PersistencePropertiesProvider {
		
	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		super.populateWithProperties(node, context);
		
		NodeModel rawNodeData = ((NodeModel) node.getRawNodeData());
		
		node.getProperties().put(TEXT, rawNodeData.getText());
		node.getProperties().put(SIDE, rawNodeData.isLeft() ? POSITION_LEFT : POSITION_RIGHT);
		
		// style name
		IStyle style = LogicalStyleModel.getStyle(rawNodeData);
		node.getProperties().put(STYLE_NAME, style == null ? MapStyleModel.DEFAULT_STYLE.toString() : style.toString());
		addProperty(node, STYLE_NAME, style == null ? MapStyleModel.DEFAULT_STYLE.toString() : style.toString(), style != null);	
	
		NodeSizeModel nodeSizeModel = NodeSizeModel.getModel(rawNodeData);		
		int width = nodeSizeModel != null ? nodeSizeModel.getMaxNodeWidth() : NodeSizeModel.NOT_SET;
		int viewWidth = NodeStyleController.getController().getMaxWidth(rawNodeData);
		addProperty(node, MAX_WIDTH, viewWidth, width != NodeSizeModel.NOT_SET);
				
		width = nodeSizeModel != null ? nodeSizeModel.getMinNodeWidth() : NodeSizeModel.NOT_SET;
		viewWidth = NodeStyleController.getController().getMinWidth(rawNodeData);
		addProperty(node, MIN_WIDTH, viewWidth, width != NodeSizeModel.NOT_SET);		
	
		String fontFamilyName = NodeStyleModel.getFontFamilyName(rawNodeData);
		String viewFontFamilyName = NodeStyleController.getController().getFontFamilyName(rawNodeData);
		addProperty(node, FONT_FAMILY, viewFontFamilyName, fontFamilyName != null);		
	
		Integer fontSize = NodeStyleModel.getFontSize(rawNodeData);
		Integer viewfontSize = NodeStyleController.getController().getFontSize(rawNodeData);
		addProperty(node, FONT_SIZE, viewfontSize, fontSize != null);	
		
		Boolean bold = NodeStyleModel.isBold(rawNodeData);
		Boolean viewbold = NodeStyleController.getController().isBold(rawNodeData);
		addProperty(node, FONT_BOLD, viewbold, bold != null);
				
		Boolean italic = NodeStyleModel.isItalic(rawNodeData);
		Boolean viewitalic = NodeStyleController.getController().isItalic(rawNodeData);
		addProperty(node, FONT_ITALIC, viewitalic, italic != null);				
	
		Color nodeColor = NodeStyleModel.getColor(rawNodeData);
		Color viewNodeColor = NodeStyleController.getController().getColor(rawNodeData);
		addProperty(node, COLOR_TEXT, ColorUtils.colorToString(viewNodeColor), nodeColor != null);		
				
		Color color = NodeStyleModel.getBackgroundColor(rawNodeData);
		Color viewColor = NodeStyleController.getController().getBackgroundColor(rawNodeData);
		addProperty(node, COLOR_BACKGROUND, ColorUtils.colorToString(viewColor != null ? viewColor : Color.WHITE), color != null);
		
		CloudController cloudController = CloudController.getController();
		CloudModel cloudModel = CloudModel.getModel(rawNodeData);
		Color viewCloudColor = cloudController.getColor(rawNodeData);
		CloudModel.Shape viewCloudShape = cloudController.getShape(rawNodeData);
		
		addProperty(node, CLOUD_COLOR, ColorUtils.colorToString(viewCloudColor), cloudModel != null);		
		addProperty(node, CLOUD_SHAPE, viewCloudShape != null ? viewCloudShape.toString() : CloudModel.Shape.ARC.toString(), cloudModel != null);
		
		EdgeController edgeController = EdgeController.getController();
		EdgeModel edgeModel = EdgeModel.getModel(rawNodeData);
		
		Color edgeColor = edgeModel != null ? edgeModel.getColor() : null;
		viewColor = edgeController.getColor(rawNodeData);
		addProperty(node, EDGE_COLOR, ColorUtils.colorToString(viewColor), edgeColor != null);
				
		EdgeStyle edgeStyle = edgeModel != null ? edgeModel.getStyle() : null;
		EdgeStyle viewStyle = edgeController.getStyle(rawNodeData);
		addProperty(node, EDGE_STYLE, viewStyle.toString(), edgeStyle != null);
				
		int edgeWidth = edgeModel != null ? edgeModel.getWidth() : EdgeModel.DEFAULT_WIDTH;
		int viewEdgeWidth = edgeController.getWidth(rawNodeData);
		addProperty(node, EDGE_WIDTH, viewEdgeWidth, edgeWidth != EdgeModel.DEFAULT_WIDTH);

		List<MindIcon> value = rawNodeData.getIcons();		
		StringBuilder sb = new StringBuilder();
		if (value != null) {
			for (MindIcon icon : value) {
				sb.append(ResourcesPlugin.getInstance().getResourceUrl("/images/mindmap/icons/" + icon.getFileName()));
				sb.append(CoreConstants.ICONS_SEPARATOR);
			}
		}
		node.getProperties().put(ICONS, sb.length() > 0 ? sb.substring(0, sb.length() - 1) : null);	
		
		// note
		String text = NoteModel.getNoteText(rawNodeData);
		if (text != null && text.length() > 0) {
			node.getProperties().put(NOTE, text);
		}
		
		// note details
		text = DetailTextModel.getDetailTextText(rawNodeData);
		if (text != null && text.length() > 0) {
			node.getProperties().put(NODE_DETAILS, text);
		}
	}
		
	private void addProperty(Node node, String property, Object value, boolean isChanged) {
		node.getProperties().put(property, new StylePropertyWrapper().setIsDefaultAs(!isChanged).setValueAs(value));
	}
	
}

