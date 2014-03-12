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
	
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeTypeProvider;
	import org.flowerplatform.flex_client.core.mindmap.update.MindMapNodeUpdateProcessor;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDragTool;
	import org.flowerplatform.flexdiagram.tool.InplaceEditorTool;
	import org.flowerplatform.flexdiagram.tool.ScrollTool;
	import org.flowerplatform.flexdiagram.tool.SelectOnClickTool;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorDiagramShell extends MindMapDiagramShell {
		
		public static const MINDMAP_ROOT_NODE_TYPE = "mindmapRootNode";
		
		public var updateProcessor:MindMapNodeUpdateProcessor;
//		
//		private var nodeController:IMindMapModelController;
//			
//		private var nodeDragController:IDragController;
//		private var nodeInplaceEditorController:IInplaceEditorController;
//		private var nodeSelectionController:ISelectionController;
//		
//		private var absoluteLayoutVisualChildrenController:IVisualChildrenController;
//		private var nodeAbsoluteRectangleController:IAbsoluteLayoutRectangleController;
//		
//		private var nodeChildrenController:IModelChildrenController;
//		private var rootModelChildrenController:IModelChildrenController;
//		
//		private var nodeRendererController:IRendererController;
//		private var nodeExtraInfoController:IModelExtraInfoController;
//				
		public function MindMapEditorDiagramShell() {
			super();
			
			addTypeProvider(new NodeTypeProvider());
			
//			nodeController = new NodeController();
//			nodeAbsoluteRectangleController = new MindMapAbsoluteLayoutRectangleController();
//			nodeDragController = new NodeDragController();
//			nodeSelectionController = new SelectionController(NodeSelectionRenderer);
//			nodeExtraInfoController = new DynamicModelExtraInfoController();
//			nodeInplaceEditorController = new NodeInplaceEditorController();
//			absoluteLayoutVisualChildrenController = new AbsoluteLayoutVisualChildrenController();
//			
//			nodeChildrenController = new NodeChildrenController();
//			rootModelChildrenController = new MindMapRootModelChildrenController();
//			
//			nodeRendererController = new NodeRendererController(CorePlugin.getInstance().mindmapNodeRendererControllerClass);
//				
			registerTools([ScrollTool, ZoomTool, SelectOnClickTool, MindMapDragTool, InplaceEditorTool]);
		}
		
//		public function getMindMapModelController(model:Object):IMindMapModelController {
//			return nodeController;
//		}
//		
//		public function getAbsoluteLayoutRectangleController(model:Object):IAbsoluteLayoutRectangleController {
//			if (model != rootModel) {
//				return nodeAbsoluteRectangleController;
//			}
//			return null;
//		}
//		
//		public function getDragController(model:Object):IDragController {
//			if (model != rootModel) { 
//				return nodeDragController;
//			}
//			return null;
//		}
//		
//		public function getDragToCreateRelationController(model:Object):IDragToCreateRelationController {			
//			return null;
//		}
//		
//		public function getInplaceEditorController(model:Object):IInplaceEditorController {
//			if (model != rootModel) {
//				return nodeInplaceEditorController;
//			}
//			return null;
//		}
//		
//		public function getModelChildrenController(model:Object):IModelChildrenController {	
//			if (model == rootModel) {
//				return rootModelChildrenController;
//			}
//			return nodeChildrenController;
//		}
//		
//		public function getModelExtraInfoController(model:Object):IModelExtraInfoController {
//			if (model is Node) {
//				return nodeExtraInfoController;
//			}
//			return null;			
//		}
//		
//		public function getRendererController(model:Object):IRendererController {
//			if (model != rootModel) {
//				return nodeRendererController;
//			}
//			return null;
//		}
//		
//		public function getResizeController(model:Object):IResizeController {			
//			return null;
//		}
//		
//		public function getSelectOrDragToCreateElementController(model:Object):ISelectOrDragToCreateElementController {			
//			return null;
//		}
//		
//		public function getSelectionController(model:Object):ISelectionController {
//			if (model != rootModel) {
//				return nodeSelectionController;
//			}
//			return null;
//		}
//		
//		public function getVisualChildrenController(model:Object):IVisualChildrenController {			
//			if (model == rootModel) {
//				return absoluteLayoutVisualChildrenController;
//			} 
//			return null;
//		}
				
	}
}
