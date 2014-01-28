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
package org.flowerplatform.flexdiagram.samples.mindmap {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapRootController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapRootModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelDragController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelInplaceEditorController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapRootController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.SampleMindMapModelRenderer;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.SampleMindMapModelSelectionRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.ISelectOrDragToCreateElementController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class SampleMindMapDiagramShell extends MindMapDiagramShell implements IMindMapControllerProvider {
		
		private var mindMapModelController:SampleMindMapModelController;
		private var dynamicModelExtraInfoController:DynamicModelExtraInfoController;
		
		private var absoluteLayoutVisualChildrenController:AbsoluteLayoutVisualChildrenController;
		private var rootModelChildrenController:MindMapRootModelChildrenController;
		private var mindmapModelChildrenController:IModelChildrenController;
		
		private var mindMapModelRendererController:MindMapModelRendererController;
		private var minMapModelAbsoluteLayoutRectangleController:MindMapAbsoluteLayoutRectangleController;
		
		private var mindMapModelSelectionController:SelectionController;
		private var mindMapModelDragController:SampleMindMapModelDragController;
		
		private var mindMapModelInplaceEditorController:IInplaceEditorController;
		private var rootController:SampleMindMapRootController;
				
		public function SampleMindMapDiagramShell() {
			super();
			
			mindMapModelController = new SampleMindMapModelController(this);
			dynamicModelExtraInfoController = new DynamicModelExtraInfoController(this);
			absoluteLayoutVisualChildrenController = new AbsoluteLayoutVisualChildrenController(this);
			rootModelChildrenController = new MindMapRootModelChildrenController(this);
			mindMapModelRendererController = new MindMapModelRendererController(this, SampleMindMapModelRenderer);
			minMapModelAbsoluteLayoutRectangleController = new MindMapAbsoluteLayoutRectangleController(this);
			mindmapModelChildrenController = new SampleMindMapModelChildrenController(this);
			
			mindMapModelInplaceEditorController = new SampleMindMapModelInplaceEditorController(this);
			mindMapModelSelectionController = new SelectionController(this, SampleMindMapModelSelectionRenderer);
			mindMapModelDragController = new SampleMindMapModelDragController(this);
			
			rootController = new SampleMindMapRootController();			
		}
		
		public function getMindMapModelController(model:Object):IMindMapModelController {
			return mindMapModelController;
		}
		
		public function getAbsoluteLayoutRectangleController(model:Object):IAbsoluteLayoutRectangleController {
			if (model is SampleMindMapModel) {
				return minMapModelAbsoluteLayoutRectangleController;
			}
			return null;
		}
		
		public function getDragController(model:Object):IDragController {
			if (model is SampleMindMapModel) {
				return mindMapModelDragController;
			}
			return null;
		}
		
		public function getDragToCreateRelationController(model:Object):IDragToCreateRelationController {
			// TODO Auto Generated method stub
			return null;
		}
		
		public function getInplaceEditorController(model:Object):IInplaceEditorController {
			if (model is SampleMindMapModel) {
				return mindMapModelInplaceEditorController;
			}
			return null;
		}
		
		public function getModelChildrenController(model:Object):IModelChildrenController {
			if (model is SampleMindMapModel) {
				return mindmapModelChildrenController;
			}
			return rootModelChildrenController;
		}
		
		public function getModelExtraInfoController(model:Object):IModelExtraInfoController {
			// TODO Auto Generated method stub
			return dynamicModelExtraInfoController;
		}
		
		public function getRendererController(model:Object):IRendererController	{
			if (model is SampleMindMapModel) {
				return mindMapModelRendererController;
			}
			return null;
		}
		
		public function getResizeController(model:Object):IResizeController {
			// TODO Auto Generated method stub
			return null;
		}
		
		public function getSelectOrDragToCreateElementController(model:Object):ISelectOrDragToCreateElementController {
			// TODO Auto Generated method stub
			return null;
		}
		
		public function getSelectionController(model:Object):ISelectionController {
			if (model is SampleMindMapModel) {
				return mindMapModelSelectionController;
			}
			return null;
		}
		
		public function getVisualChildrenController(model:Object):IVisualChildrenController {
			if (model is ArrayList) {
				return absoluteLayoutVisualChildrenController;
			} return null;
		}
				
		override public function getControllerProvider(model:Object):IControllerProvider {
			return this;
		}
		
		public function getMindMapRootController(model:Object):IMindMapRootController {			
			return rootController;
		}
				
	}
}