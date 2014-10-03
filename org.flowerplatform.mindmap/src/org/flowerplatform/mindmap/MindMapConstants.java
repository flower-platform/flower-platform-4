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
package org.flowerplatform.mindmap;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.util.UtilConstants;

/**
 * @author Cristina Constantinescu
 */
public final class MindMapConstants {
	
	private MindMapConstants() {
	}
	
	// clean up by CS: begin
	
	/**
	 * Contains nodes that belong to a general purpose/original mind maps. I.e. mind maps that do only the
	 * original mind map cases (add, remove, set). E.g. Freeplane, FreeMind, etc.
	 * 
	 * @author Cristian Spiescu
	 */
	public static final String GENERAL_PURPOSE_MIND_MAP_CATEGORY = UtilConstants.CATEGORY_PREFIX + "generalPurposeMindMap";
	
	// Constants from Flex: FlexDiagramConstants
	public static final String BASE_RENDERER_FONT_FAMILY= "baseRenderer.fontFamily";
	public static final String BASE_RENDERER_FONT_SIZE= "baseRenderer.fontSize";
	public static final String BASE_RENDERER_FONT_BOLD= "baseRenderer.fontBold";
	public static final String BASE_RENDERER_FONT_ITALIC= "baseRenderer.fontItalic";
	public static final String BASE_RENDERER_TEXT= "baseRenderer.text";
	public static final String BASE_RENDERER_TEXT_COLOR= "baseRenderer.textColor";
	public static final String BASE_RENDERER_BACKGROUND_COLOR= "baseRenderer.backgroundColor";
	public static final String BASE_RENDERER_ICONS= "baseRenderer.icons";
	public static final String BASE_RENDERER_MIN_WIDTH = "baseRenderer.minWidth";
	public static final String BASE_RENDERER_MAX_WIDTH = "baseRenderer.maxWidth";
	
	public static final String MIND_MAP_RENDERER_CLOUD_TYPE= "mindMapRenderer.cloudType";
	public static final String MIND_MAP_RENDERER_CLOUD_COLOR= "mindMapRenderer.cloudColor";
	public static final String MIND_MAP_RENDERER_HAS_CHILDREN= "mindMapRenderer.hasChildren";
	public static final String MIND_MAP_RENDERER_SIDE = "mindMapRenderer.side";
	
	// Constants from Flex: MindMapConstants
	public static final String MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX = "mindMap.";

	// clean up by CS: end
	
	//////////////////////////////////
	// Node types
	//////////////////////////////////
	
	public static final String FREEPLANE_PERSISTENCE_NODE_TYPE_KEY = "nodeType";
	public static final String MINDMAP_NODE_TYPE = "freeplaneNode";
	
	//////////////////////////////////
	// Node properties
	//////////////////////////////////
	
	public static final String NOTE = "note";
	
	public static final String NODE_DETAILS = "nodeDetails";
	
	public static final List<Integer> FONT_SIZES = new ArrayList<>();
	static {
		for (int i = 1; i <= 72; i++) {
			FONT_SIZES.add(i);
		}
	}
	
	// CLOUD SHAPES
	public static final String SHAPE_NONE = "NONE";		
	public static final String SHAPE_RECTANGLE = "RECT";
	public static final String SHAPE_ROUND_RECTANGLE = "ROUND_RECT";
	public static final String SHAPE_ARC = "ARC";
	public static final String SHAPE_STAR = "STAR";
			
	public static final int DEFAULT_MIN_WIDTH = 1;
	public static final int DEFAULT_MAX_WIDTH = 600;
	
	public static final String TEXT = "text";
	
	public static final String MIN_WIDTH = "minWidth";
	public static final String MAX_WIDTH = "maxWidth";
	public static final String FONT_FAMILY = "fontFamily";
	public static final String FONT_SIZE = "fontSize";
	public static final String FONT_BOLD = "fontBold";
	public static final String FONT_ITALIC = "fontItalic";
	
	public static final String EDGE_WIDTH = "edgeWidth";
	public static final String EDGE_STYLE = "edgeStyle";
	public static final String EDGE_COLOR = "edgeColor";
	
	// EDGE STYLES
	public static final String EDGE_SMOOTHLY_CURVED = "bezier";
	public static final String EDGE_HIDE = "hide_edge";
	public static final String EDGE_HORIZONTAL = "horizontal";
	public static final String EDGE_LINEAR = "linear";
	public static final String EDGE_SHARP_LINEAR = "sharp_linear";
	public static final String EDGE_SHARPLY_CURVED = "sharp_bezier";
	
	public static final String COLOR_TEXT = "colorText";
	public static final String COLOR_BACKGROUND = "colorBackground";
	
	public static final String CLOUD_COLOR = "cloudColor";
	public static final String CLOUD_SHAPE = "cloudShape";
	
	public static final String STYLE_NAME = "styleName";
	
	public static final String MINDMAP_ICONS_WITH_BUTTON_DESCRIPTOR_TYPE = "MindMapIconsWithButton";
	public static final String MINDMAP_STYLE_NAME_DESCRIPTOR_TYPE = "MindMapStyles";
		
	//////////////////////////////////
	// Resource
	//////////////////////////////////
	
	public static final String FREEPLANE_MINDMAP_RESOURCE_KEY = "fpm";
	public static final String FREEPLANE_PERSISTENCE_RESOURCE_KEY = "fpp";
	public static final String FREEPLANE_MINDMAP_CATEGORY = CoreConstants.CATEGORY_RESOURCE_PREFIX + FREEPLANE_MINDMAP_RESOURCE_KEY;
	public static final String FREEPLANE_PERSISTENCE_CATEGORY = CoreConstants.CATEGORY_RESOURCE_PREFIX + FREEPLANE_PERSISTENCE_RESOURCE_KEY;
	
	//////////////////////////////////
	// Content types
	//////////////////////////////////
	
	public static final String MINDMAP_CONTENT_TYPE = "mindmap";
	
}
