/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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

import static org.flowerplatform.core.CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES;
import static org.flowerplatform.core.CoreConstants.PNG_EXTENSION;
import static org.flowerplatform.mindmap.MindMapConstants.CLOUD_COLOR;
import static org.flowerplatform.mindmap.MindMapConstants.CLOUD_SHAPE;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_BACKGROUND;
import static org.flowerplatform.mindmap.MindMapConstants.COLOR_TEXT;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_COLOR;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_HIDE;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_HORIZONTAL;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_LINEAR;
import static org.flowerplatform.mindmap.MindMapConstants.EDGE_SMOOTHLY_CURVED;
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
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_ROUND_RECTANGLE;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_ARC;
import static org.flowerplatform.mindmap.MindMapConstants.SHAPE_STAR;
import static org.flowerplatform.mindmap.MindMapConstants.STYLE_NAME;
import static org.flowerplatform.mindmap.MindMapConstants.TEXT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyWrapper;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.remote.StylePropertyWrapper;
import org.flowerplatform.core.node.update.controller.UpdateController;
import org.flowerplatform.mindmap.MindMapConstants;
import org.freeplane.core.util.ColorUtils;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.cloud.CloudModel.Shape;
import org.freeplane.features.edge.EdgeModel;
import org.freeplane.features.edge.EdgeStyle;
import org.freeplane.features.icon.MindIcon;
import org.freeplane.features.icon.UserIcon;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.nodestyle.NodeSizeModel;
import org.freeplane.features.nodestyle.NodeStyleModel;
import org.freeplane.features.note.NoteModel;
import org.freeplane.features.styles.IStyle;
import org.freeplane.features.styles.LogicalStyleModel;
import org.freeplane.features.styles.MapStyleModel;
import org.freeplane.features.text.DetailTextModel;

/**
 * @author Cristina Constantinescu
 */
public class MindMapPropertySetter extends PersistencePropertySetter {

	private static final Pattern ICON_URL_PATTERN = Pattern.compile("servlet/public-resources/((.*?/)+)(.*?).png");
	private static final Pattern CUSTOM_ICON_URL_PATTERN = Pattern.compile("servlet/load/(.*?).png");
	
	@Override
	public void setProperties(Node node, Map<String, Object> properties, ServiceContext<NodeService> context) {
		NodeModel rawNodeData = ((NodeModel) node.getRawNodeData());
		
		boolean isPropertySet = false;
		// if null -> no additional updates
		// if empty -> additional updates for all properties
		List<String> addAdditionalSetPropertyUpdatesFor = null;
		
		for (String property : properties.keySet()) {
			Object value = properties.get(property);

			Object propertyValue = value instanceof PropertyWrapper ? ((PropertyWrapper) value).getValue() : value;
			switch (property) {
//				case MindMapConstants.SIDE:
//					rawNodeData.setLeft(((Integer) propertyValue).intValue() == MindMapConstants.POSITION_LEFT);
//					isPropertySet = true;
//					break;
				case TEXT:
					rawNodeData.setText((String) propertyValue);
					isPropertySet = true;
					break;
				case MIN_WIDTH:
					Integer newMinValue = propertyValue == null ? NodeSizeModel.NOT_SET : (Integer) propertyValue;
					if (newMinValue < 1) {
						context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
						context.getService().setProperty(node, property, new StylePropertyWrapper().setIsDefaultAs(true)
								.setValueAs(MindMapConstants.DEFAULT_MIN_WIDTH), new ServiceContext<NodeService>(context.getService()));
						return;				
					} else {
						NodeSizeModel.createNodeSizeModel(rawNodeData).setMinNodeWidth(newMinValue);		
						isPropertySet = true;
					}				
					break;
				case MAX_WIDTH:	
					Integer newMaxValue = propertyValue == null ? NodeSizeModel.NOT_SET : (Integer) propertyValue;
					if (newMaxValue < 1) {
						context.add(CoreConstants.DONT_PROCESS_OTHER_CONTROLLERS, true);
						context.getService().setProperty(node, property, new PropertyWrapper()
						.setValueAs(MindMapConstants.DEFAULT_MAX_WIDTH), new ServiceContext<NodeService>(context.getService()));
						return;								
					} else {
						newMaxValue = (Integer) propertyValue;
						NodeSizeModel.createNodeSizeModel(rawNodeData).setMaxNodeWidth(newMaxValue);	
						isPropertySet = true;
					}
					break;
				case CoreConstants.ICONS:
					String icons = (String) propertyValue;
					rawNodeData.getIcons().clear();
					if (icons != null) {					
						String[] array = icons.split(CoreConstants.ICONS_SEPARATOR);
						for (String icon : array) {
							Matcher matcher = ICON_URL_PATTERN.matcher(icon);
							if (matcher.find()) {
								rawNodeData.addIcon(new MindIcon(matcher.group(3)));	
							} else {
								Matcher matcher2 = CUSTOM_ICON_URL_PATTERN.matcher(icon);
								if (matcher2.find()) {
									Object iconFile;
									try {
										iconFile = FileControllerUtils.getFileAccessController().getFile(matcher2.group(1) + PNG_EXTENSION);
									} catch (Exception e) {
										throw new RuntimeException(e);
									}								
									if (FileControllerUtils.getFileAccessController().isDirectory(iconFile)) {
										continue;
									}
									String path = FileControllerUtils.getFileAccessController().getPath(iconFile);
													
									rawNodeData.addIcon(new UserIcon(FilenameUtils.removeExtension(path), path, FilenameUtils.removeExtension(path)));
								}
								
							}
						}
					}				
					isPropertySet = true;
					break;
				case NOTE:
					String note = (String) propertyValue;
					NoteModel.createNote(rawNodeData).setXml(note);
									
					isPropertySet = true;
					if (addAdditionalSetPropertyUpdatesFor == null) {
						addAdditionalSetPropertyUpdatesFor = new ArrayList<String>();
					}				
					addAdditionalSetPropertyUpdatesFor.add(CoreConstants.ICONS);
					break;
				case NODE_DETAILS:
					String nodeDetails = (String) propertyValue;
					DetailTextModel.createDetailText(rawNodeData).setXml(nodeDetails);
					isPropertySet = true;
					break;
				case FONT_FAMILY:	
					String fontFamily = (String) propertyValue;
					NodeStyleModel.createNodeStyleModel(rawNodeData).setFontFamilyName(fontFamily);
					isPropertySet = true;
					break;
				case FONT_SIZE:	
					Integer fontSize = (Integer) propertyValue;				
					NodeStyleModel.createNodeStyleModel(rawNodeData).setFontSize(fontSize);				
					isPropertySet = true;
					break;
				case FONT_BOLD:	
					Boolean fontBold = (Boolean) propertyValue;				
					NodeStyleModel.createNodeStyleModel(rawNodeData).setBold(fontBold);				
					isPropertySet = true;
					break;
				case FONT_ITALIC:	
					Boolean fontItalic = (Boolean) propertyValue;				
					NodeStyleModel.createNodeStyleModel(rawNodeData).setItalic(fontItalic);				
					isPropertySet = true;
					break;
				case COLOR_TEXT:	
					String color = (String) propertyValue;				
					NodeStyleModel.createNodeStyleModel(rawNodeData).setColor(ColorUtils.stringToColor(color));				
					isPropertySet = true;
					break;
				case COLOR_BACKGROUND:	
					String backgroundColor = (String) propertyValue;				
					NodeStyleModel.createNodeStyleModel(rawNodeData).setBackgroundColor(ColorUtils.stringToColor(backgroundColor));				
					isPropertySet = true;
					break;
				case EDGE_COLOR:
					String edgeColor = (String) propertyValue;
					EdgeModel.createEdgeModel(rawNodeData).setColor(ColorUtils.stringToColor(edgeColor));
					isPropertySet = true;
					break;
				case EDGE_WIDTH:
					int edgeWidth = (int) propertyValue;
					EdgeModel.createEdgeModel(rawNodeData).setWidth(edgeWidth);
					isPropertySet = true;
					break;
				case EDGE_STYLE:
					String edgeStyleProperty = (String) propertyValue;
					EdgeStyle edgeStyle = null;
					switch (edgeStyleProperty) {
					case EDGE_SMOOTHLY_CURVED:
						edgeStyle = EdgeStyle.EDGESTYLE_BEZIER;
						break;
					case EDGE_LINEAR:
						edgeStyle = EdgeStyle.EDGESTYLE_LINEAR;
						break;
					case EDGE_HORIZONTAL:
						edgeStyle = EdgeStyle.EDGESTYLE_HORIZONTAL;
						break;
					case EDGE_HIDE:
						edgeStyle = EdgeStyle.EDGESTYLE_HIDDEN;
						break;
					default:
						edgeStyle = EdgeStyle.EDGESTYLE_BEZIER;
						break;
					}
					EdgeModel.createEdgeModel(rawNodeData).setStyle(edgeStyle);
					isPropertySet = true;
					break;
					
				case CLOUD_COLOR:
					String cloudColor = (String) propertyValue;				
					CloudModel.createModel(rawNodeData).setColor(ColorUtils.stringToColor(cloudColor));				
					isPropertySet = true;
					break;
				case CLOUD_SHAPE:
					String cloudShape = (String) propertyValue;
					Shape shape = null;
					// get shape correspondence from freeplane
					switch (cloudShape) {
						case SHAPE_RECTANGLE:
							shape = Shape.RECT;
							break;
						case SHAPE_ROUND_RECTANGLE:
							shape = Shape.ROUND_RECT;
							break;
						case SHAPE_ARC:
							shape = Shape.ARC;
							break;
						case SHAPE_STAR:
							shape = Shape.STAR;
							break;
					default:
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
					String styleName = (String) propertyValue;
					MapModel mapModel = ((NodeModel) node.getRawNodeData()).getMap();
					
					Set<IStyle> styles = MapStyleModel.getExtension(mapModel).getStyles();
					IStyle style = null;
					for (IStyle availableStyle : styles) {
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
					if (addAdditionalSetPropertyUpdatesFor == null) {
						addAdditionalSetPropertyUpdatesFor = new ArrayList<String>();
					}
					break;
			default:
				break;

			}
					
			if (!isPropertySet) {
				super.setProperties(node, Collections.singletonMap(property, value), context);
			} else {
				rawNodeData.getMap().setSaved(false);			
			}
		}
		
		if (addAdditionalSetPropertyUpdatesFor != null) {
			if (addAdditionalSetPropertyUpdatesFor.isEmpty()) {				
				for (Map.Entry<String, Object> entry : node.getOrPopulateProperties(new ServiceContext<NodeService>(context.getService())).entrySet()) {
					context.getService().setProperty(node, entry.getKey(), entry.getValue(), new ServiceContext<NodeService>(context.getService())
							.add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
				}	 
			} else {
				for (String entry : addAdditionalSetPropertyUpdatesFor) {
					context.getService().setProperty(node, entry, node.getPropertyValue(entry), new ServiceContext<NodeService>(context.getService())
							.add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
				}
			}
		}
	}
	
	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> serviceContext) {
		
		NodeModel rawNodeData = ((NodeModel) node.getRawNodeData());
		
		boolean isPropertyUnset = false;
		// if null -> no additional updates
		// if empty -> additional updates for all properties
		List<String> addAdditionalUnsetPropertyUpdatesFor = null;
				
		switch (property) {			
			case MIN_WIDTH:
				((NodeSizeModel) rawNodeData.getExtension(NodeSizeModel.class)).setMinNodeWidth(NodeSizeModel.NOT_SET);
				isPropertyUnset = true;
				break;
			case MAX_WIDTH:
				((NodeSizeModel) rawNodeData.getExtension(NodeSizeModel.class)).setMaxNodeWidth(NodeSizeModel.NOT_SET);
				isPropertyUnset = true;
				break;
			case COLOR_BACKGROUND:
				((NodeStyleModel) rawNodeData.getExtension(NodeStyleModel.class)).setBackgroundColor(null);
				isPropertyUnset = true;
				break;
			case COLOR_TEXT:
				((NodeStyleModel) rawNodeData.getExtension(NodeStyleModel.class)).setColor(null);
				isPropertyUnset = true;
				break;
			case FONT_BOLD:
				((NodeStyleModel) rawNodeData.getExtension(NodeStyleModel.class)).setBold(null);
				isPropertyUnset = true;
				break;
			case FONT_FAMILY:
				((NodeStyleModel) rawNodeData.getExtension(NodeStyleModel.class)).setFontFamilyName(null);
				isPropertyUnset = true;
				break;
			case FONT_ITALIC:
				((NodeStyleModel) rawNodeData.getExtension(NodeStyleModel.class)).setItalic(null);
				isPropertyUnset = true;
				break;
			case FONT_SIZE:
				((NodeStyleModel) rawNodeData.getExtension(NodeStyleModel.class)).setFontSize(null);
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
			// edge	
			case EDGE_WIDTH:
				((EdgeModel) rawNodeData.getExtension(EdgeModel.class)).setWidth(EdgeModel.DEFAULT_WIDTH);
				isPropertyUnset = true;
				break;
			case EDGE_STYLE:
				((EdgeModel) rawNodeData.getExtension(EdgeModel.class)).setStyle(null);
				isPropertyUnset = true;
				break;
			case EDGE_COLOR:
				rawNodeData.getExtension(EdgeModel.class).setColor(null);
				isPropertyUnset = true;
				break;
			default:
				break;
		}
		if (!isPropertyUnset) {
			super.unsetProperty(node, property, serviceContext);
		} else {
			rawNodeData.getMap().setSaved(false);			
		}
				
		if (addAdditionalUnsetPropertyUpdatesFor != null) {
			if (addAdditionalUnsetPropertyUpdatesFor.isEmpty()) {
				for (Map.Entry<String, Object> entry : node.getOrPopulateProperties(new ServiceContext<NodeService>(serviceContext.getService())).entrySet()) {
					serviceContext.getService().unsetProperty(node, entry.getKey(), new ServiceContext<NodeService>(serviceContext.getService())
							.add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
				}	 
			} else {
				for (String entry : addAdditionalUnsetPropertyUpdatesFor) {
					serviceContext.getService().unsetProperty(node, entry, new ServiceContext<NodeService>(serviceContext.getService())
							.add(INVOKE_ONLY_CONTROLLERS_WITH_CLASSES, Collections.singletonList(UpdateController.class)));
				}
			}
		}
	}
		
}
