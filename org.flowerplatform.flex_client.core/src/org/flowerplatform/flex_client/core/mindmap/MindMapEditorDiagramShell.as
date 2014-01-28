/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flex_client.core.mindmap {
	
	import flash.events.Event;
	
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeChildrenController;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeController;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeDragController;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeInplaceEditorController;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeRootController;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.renderer.NodeRenderer;
	import org.flowerplatform.flex_client.core.mindmap.renderer.NodeSelectionRenderer;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.LightweightModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDragTool;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapRootController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapRootModelChildrenController;
	import org.flowerplatform.flexdiagram.tool.InplaceEditorTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.ISelectOrDragToCreateElementController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorDiagramShell extends MindMapDiagramShell implements IMindMapControllerProvider {
		
		private var nodeController:IMindMapModelController;
		private var rootController:IMindMapRootController;
				
		private var nodeDragController:IDragController;
		private var nodeInplaceEditorController:IInplaceEditorController;
		private var nodeSelectionController:ISelectionController;
		
		private var absoluteLayoutVisualChildrenController:IVisualChildrenController;
		private var nodeAbsoluteRectangleController:IAbsoluteLayoutRectangleController;
		
		private var nodeChildrenController:IModelChildrenController;
		private var diagramChildrenController:IModelChildrenController;
		
		private var nodeRendererController:IRendererController;
		
		private var diagramExtraInfoController:IModelExtraInfoController;
		private var nodeExtraInfoController:IModelExtraInfoController;
				
		public function MindMapEditorDiagramShell() {
			super();
			
			nodeController = new NodeController(this);
			nodeAbsoluteRectangleController = new MindMapAbsoluteLayoutRectangleController(this);
			nodeDragController = new NodeDragController(this);
			nodeSelectionController = new SelectionController(this, NodeSelectionRenderer);
			nodeExtraInfoController = new DynamicModelExtraInfoController(this);
			nodeInplaceEditorController = new NodeInplaceEditorController(this);
			absoluteLayoutVisualChildrenController = new AbsoluteLayoutVisualChildrenController(this);
			
			nodeChildrenController = new NodeChildrenController(this);
			diagramChildrenController = new MindMapRootModelChildrenController(this);
			
			nodeRendererController = new MindMapModelRendererController(this, NodeRenderer);
			diagramExtraInfoController = new LightweightModelExtraInfoController(this);
			
			rootController = new NodeRootController(this);		
			
			registerTools([ScrollTool, ZoomTool, SelectOnClickTool, MindMapDragTool, InplaceEditorTool]);
		}
				
		override public function getControllerProvider(model:Object):IControllerProvider {
			return this;
		}		
				
		public function getMindMapModelController(model:Object):IMindMapModelController {
			return nodeController;
		}
		
		public function getAbsoluteLayoutRectangleController(model:Object):IAbsoluteLayoutRectangleController {
			if (model is Node) {
				return nodeAbsoluteRectangleController;
			}
			return null;
		}
		
		public function getDragController(model:Object):IDragController {
			if (model is Node) { 
				return nodeDragController;
			}
			return null;
		}
		
		public function getDragToCreateRelationController(model:Object):IDragToCreateRelationController {			
			return null;
		}
		
		public function getInplaceEditorController(model:Object):IInplaceEditorController {
			if (model is Node) {
				return nodeInplaceEditorController;
			}
			return null;
		}
		
		public function getModelChildrenController(model:Object):IModelChildrenController {	
			if (model is Node) {
				return nodeChildrenController;
			}
			return diagramChildrenController;
		}
		
		public function getModelExtraInfoController(model:Object):IModelExtraInfoController {
			if (model is Node) {
				return nodeExtraInfoController;
			}
			return diagramExtraInfoController;			
		}
		
		public function getRendererController(model:Object):IRendererController {
			if (model is Node) {
				return nodeRendererController;
			}
			return null;
		}
		
		public function getResizeController(model:Object):IResizeController {			
			return null;
		}
		
		public function getSelectOrDragToCreateElementController(model:Object):ISelectOrDragToCreateElementController {			
			return null;
		}
		
		public function getSelectionController(model:Object):ISelectionController {
			if (model is Node) {
				return nodeSelectionController;
			}
			return null;
		}
		
		public function getVisualChildrenController(model:Object):IVisualChildrenController {			
			if (model is Diagram) {
				return absoluteLayoutVisualChildrenController;
			} 
			return null;
		}
		
		public function getMindMapRootController(model:Object):IMindMapRootController {			
			return rootController;
		}
		
	}
}