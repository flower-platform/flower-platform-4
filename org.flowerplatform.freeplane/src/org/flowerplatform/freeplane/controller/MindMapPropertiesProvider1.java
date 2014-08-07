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
public class MindMapPropertiesProvider1 extends MindMapPropertiesProvider {
		
	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		super.populateWithProperties(node, context);
		node.getProperties().put("test", "test");
	}
		
}

