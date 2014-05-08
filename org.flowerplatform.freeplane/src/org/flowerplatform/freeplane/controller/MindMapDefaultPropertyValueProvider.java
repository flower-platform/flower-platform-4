package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS;
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
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_NONE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_ROUND_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.STYLE_NAME;

import java.awt.Color;
import java.util.Enumeration;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.DefaultPropertyValueProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.freeplane.core.util.ColorUtils;
import org.freeplane.features.cloud.CloudController;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.cloud.CloudModel.Shape;
import org.freeplane.features.edge.EdgeController;
import org.freeplane.features.edge.EdgeModel;
import org.freeplane.features.edge.EdgeStyle;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;
import org.freeplane.features.nodestyle.NodeStyleController;
import org.freeplane.features.styles.MapStyleModel;

/**
 * @author Sebastian Solomon
 */
public class MindMapDefaultPropertyValueProvider extends DefaultPropertyValueProvider {
	
	public final static String DEFAULT_STYLE = "Default";
	
	public MindMapDefaultPropertyValueProvider() {
		setOrderIndex(-10000);
	}
	
	@Override
	public Object getDefaultValue(Node node, String property, ServiceContext<NodeService> serviceContext) {
		serviceContext.add(DONT_PROCESS_OTHER_CONTROLLERS, true);
		NodeModel nodeModel =  (NodeModel) node.getOrRetrieveRawNodeData();
		String styleName = (String) node.getProperties().get(STYLE_NAME);
		
		NodeModel styleNodeModel = getStyleNodeModel(nodeModel, styleName);
		
		Object stylePropertyValue;
		switch (property) {
		case COLOR_BACKGROUND:
		case COLOR_TEXT:
		case FONT_BOLD:
		case FONT_FAMILY:
		case FONT_ITALIC:
		case FONT_SIZE:
			stylePropertyValue = getNodeFontDefaultProperty(property, styleNodeModel, nodeModel);
			break;
		case MAX_WIDTH:
		case MIN_WIDTH:
			stylePropertyValue = getNodeSizeDefaultProperty(property, styleNodeModel, nodeModel);
			break;
		case CLOUD_COLOR:
		case CLOUD_SHAPE:
			stylePropertyValue = getNodeCloudDefaultProperty(property, styleNodeModel, nodeModel);
			break;
		case EDGE_COLOR:
		case EDGE_STYLE:
		case EDGE_WIDTH:
			stylePropertyValue = getNodeEdgeDefaultProperty(property, styleNodeModel, nodeModel);
			break;
		default:
			stylePropertyValue = null;
			break;
		}
		return stylePropertyValue;
	}
	
	private NodeModel getStyleNodeModel(NodeModel nodeModel, String styleName ) {
		final MapModel map = nodeModel.getMap();
		MapModel styleMap = MapStyleModel.getExtension(map).getStyleMap();
		
		if(styleMap == null){
			styleMap = map;
		}
		Enumeration<NodeModel> enumeration = styleMap.getRootNode().children();
		
		while(enumeration.hasMoreElements()) {
			for (NodeModel styleNodeModel : enumeration.nextElement().getChildren()) {
				if (styleNodeModel.getText().equals(styleName) ) {
					return styleNodeModel;
				}
			}
		}
		return null;
	}
	
	private Object getNodeSizeDefaultProperty(String property, NodeModel styleNodeModel, NodeModel nodeModel) {
		Object defaultPropertyValue = null;
		if (styleNodeModel == null) {
			styleNodeModel = getStyleNodeModel(nodeModel, DEFAULT_STYLE);
		}
		NodeSizeModel nodeSizeModel = ((NodeSizeModel)styleNodeModel.getExtensions().get(NodeSizeModel.class));
		if (MIN_WIDTH.equals(property)) {
			defaultPropertyValue = nodeSizeModel == null ? null : nodeSizeModel.getMinNodeWidth();
		} else if (MAX_WIDTH.equals(property)) {
			defaultPropertyValue = nodeSizeModel == null ? null : nodeSizeModel.getMaxNodeWidth();
		}
		
		if ((defaultPropertyValue == null || (int)defaultPropertyValue == NodeSizeModel.NOT_SET) && !styleNodeModel.getText().equals(DEFAULT_STYLE)) {
			NodeModel defaultStyleNodeModel = getStyleNodeModel(nodeModel, DEFAULT_STYLE);
			defaultPropertyValue = getNodeSizeDefaultProperty(property, defaultStyleNodeModel, nodeModel);
		}
		
		// no values set in style or default style, use our defaults
		if (defaultPropertyValue == null) {
			if (MIN_WIDTH.equals(property)) {
				defaultPropertyValue = 1;
			} else if (MAX_WIDTH.equals(property)) {
				defaultPropertyValue = 600;
			}
		}
		return defaultPropertyValue;
	}
	
	private Object getNodeCloudDefaultProperty(String property, NodeModel styleNodeModel, NodeModel nodeModel) {
		Object defaultPropertyValue = null;
		CloudModel cloudModel = null;
		if (styleNodeModel == null || ((cloudModel = CloudController.getController().getCloud(styleNodeModel)) == null )) {
			if (CLOUD_COLOR.equals(property)) {
				return ColorUtils.colorToString(CloudController.getStandardColor());
			} else if (CLOUD_SHAPE.equals(property)) {
				return SHAPE_NONE;
			}
		}
		
		if (CLOUD_COLOR.equals(property)) {
			defaultPropertyValue = ColorUtils.colorToString(cloudModel.getColor());
		} else if (CLOUD_SHAPE.equals(property)) {
			Shape shape = cloudModel.getShape();
			switch (shape) {
			case RECT:
				defaultPropertyValue = SHAPE_RECTANGLE;
				break;
			case ROUND_RECT:
				defaultPropertyValue = SHAPE_ROUND_RECTANGLE;
				break;
			// TODO add "ARC & STAR" when this are supported. 
			default:
				defaultPropertyValue = SHAPE_NONE;
				break;
			}
		}
		
		return defaultPropertyValue;
	}
	
	private Object getNodeFontDefaultProperty(String property, NodeModel styleNodeModel, NodeModel nodeModel) {
		Object defaultPropertyValue = null;
		
		if (styleNodeModel == null) {
			styleNodeModel = getStyleNodeModel(nodeModel, DEFAULT_STYLE);
		}
		
		switch (property) {
		case COLOR_BACKGROUND:
			Color backgroundColor = NodeStyleController.getController().getBackgroundColor(styleNodeModel);
			defaultPropertyValue = backgroundColor == null ? ColorUtils.colorToString(Color.WHITE) : ColorUtils.colorToString(backgroundColor);
			break;
		case COLOR_TEXT:
			Color color = NodeStyleController.getController().getColor(styleNodeModel);
			defaultPropertyValue = ColorUtils.colorToString(color);
			break;
		case FONT_BOLD:
			defaultPropertyValue = NodeStyleController.getController().isBold(styleNodeModel);
			break;
		case FONT_FAMILY:
			defaultPropertyValue = NodeStyleController.getController().getFontFamilyName(styleNodeModel);
			break;
		case FONT_ITALIC:
			defaultPropertyValue = NodeStyleController.getController().isItalic(styleNodeModel);
			break;
		case FONT_SIZE:
			defaultPropertyValue = NodeStyleController.getController().getFontSize(styleNodeModel);
			break;
		}
		return defaultPropertyValue;
	}
	
	private Object getNodeEdgeDefaultProperty(String property,
			NodeModel styleNodeModel, NodeModel nodeModel) {
		Object defaultPropertyValue = null;
		EdgeModel edgeModel = null;
		if (styleNodeModel != null) {
			edgeModel = (EdgeModel)styleNodeModel.getExtension(EdgeModel.class);
		}
		
		switch (property) {
		case EDGE_COLOR:
			Color styleColor = edgeModel == null ? null : edgeModel.getColor();
			if (styleColor == null) {// get color from parent
				if (nodeModel.getParentNode() != null) {
					defaultPropertyValue = ColorUtils.colorToString(EdgeController.getController().getColor(nodeModel.getParentNode()));
				} else { //root node, return the default value
					defaultPropertyValue =  "#808080";
				}
			} else {
				defaultPropertyValue =  ColorUtils.colorToString(styleColor);
			}
			break;
		case EDGE_STYLE:
			EdgeStyle styleEdgeStyle = edgeModel == null ? null : edgeModel.getStyle();
			if (styleEdgeStyle == null) {// get style from parent
				if (nodeModel.getParentNode() != null) {
					defaultPropertyValue = EdgeController.getController().getStyle(nodeModel.getParentNode()).toString();
				} else { // the root node style has no edgeStyle, return the default edge style
					defaultPropertyValue = EdgeStyle.EDGESTYLE_BEZIER.toString();
				}
			} else {
				defaultPropertyValue = styleEdgeStyle.toString();
			}
			break;
		case EDGE_WIDTH:
			Integer styleEdgeWidth = edgeModel == null ? -1 : edgeModel.getWidth();
			if (styleEdgeWidth == -1) { // get edge_width from parent
				if (nodeModel.getParentNode() != null) {
					defaultPropertyValue = EdgeController.getController().getWidth(nodeModel.getParentNode());
				} else {
					defaultPropertyValue = 0;
				}
			} else {
				defaultPropertyValue =  styleEdgeWidth;
			}
			break;
		}
		return defaultPropertyValue;
	}

}
