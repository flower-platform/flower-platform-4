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
package org.flowerplatform.flexdiagram {
	
	public class FlexDiagramConstants {
		
		//////////////////////////////////
		// Controllers
		//////////////////////////////////
		
		public static const MODEL_EXTRA_INFO_CONTROLLER:String = "ModelExtraInfoController";
		public static const MODEL_CHILDREN_CONTROLLER:String = "ModelChildrenController";
		
		public static const RENDERER_CONTROLLER:String = "RendererController";
		public static const VISUAL_CHILDREN_CONTROLLER:String = "VisualChildrenController";
		public static const SELECTION_CONTROLLER:String = "SelectionController";
		public static const INPLACE_EDITOR_CONTROLLER:String = "InplaceEditorController";
		public static const DRAG_CONTROLLER:String = "DragController";
		public static const SELECT_OR_DRAG_TO_CREATE_ELEMENT_CONTROLLER:String = "SelectOrDragToCreateElementController";
		public static const DRAG_TO_CREATE_RELATION_CONTROLLER:String = "DragToCreateRelationController";
		public static const RESIZE_CONTROLLER:String = "ResizeController";
		public static const ABSOLUTE_LAYOUT_RECTANGLE_CONTROLLER:String = "AbsoluteLayoutRectangleController";
		
		// Mindmap
		
		public static const MINDMAP_MODEL_CONTROLLER:String = "MindMapModelController";
		
		// EDGE STYLES
		public static const EDGE_SMOOTHLY_CURVED:String = "bezier";
		public static const EDGE_HIDE:String = "hide_edge";
		public static const EDGE_HORIZONTAL:String = "horizontal";
		public static const EDGE_LINEAR:String = "linear";
		
		// visual properties supported by the BaseRenderer
		public static const BASE_RENDERER_FONT_FAMILY:String = "baseRenderer.fontFamily";
		public static const BASE_RENDERER_FONT_SIZE:String = "baseRenderer.fontSize";
		public static const BASE_RENDERER_FONT_BOLD:String = "baseRenderer.fontBold";
		public static const BASE_RENDERER_FONT_ITALIC:String = "baseRenderer.fontItalic";
		public static const BASE_RENDERER_TEXT:String = "baseRenderer.text";
		public static const BASE_RENDERER_TEXT_COLOR:String = "baseRenderer.textColor";
		public static const BASE_RENDERER_BACKGROUND_COLOR:String = "baseRenderer.backgroundColor";
		public static const BASE_RENDERER_ICONS:String = "baseRenderer.icons";
		public static const BASE_RENDERER_MIN_WIDTH:String = "baseRenderer.minWidth";
		public static const BASE_RENDERER_MAX_WIDTH:String = "baseRenderer.maxWidth";
		
		// visual properties supported by the MindMapNodeRenderer
		public static const MIND_MAP_RENDERER_CLOUD_TYPE:String = "mindMapRenderer.cloudType";
		public static const MIND_MAP_RENDERER_CLOUD_TYPE_RECTANGLE:String = "RECT";
		public static const MIND_MAP_RENDERER_CLOUD_TYPE_ROUNDED_RECTANGLE:String = "ROUND_RECT";
		public static const MIND_MAP_RENDERER_CLOUD_TYPE_ARC:String = "ARC";
		public static const MIND_MAP_RENDERER_CLOUD_TYPE_STAR:String = "STAR";
		
		public static const MIND_MAP_RENDERER_CLOUD_COLOR:String = "mindMapRenderer.cloudColor";
		public static const MIND_MAP_RENDERER_HAS_CHILDREN:String = "mindMapRenderer.hasChildren";
		// for the moment, this is not a feature of this renderer; the implementation from FD/samples and FP
		// do it; but in the future, we should move the logic entirely into FD
		public static const MIND_MAP_RENDERER_SIDE:String = "mindMapRenderer.side";
	}
}
