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
package org.flowerplatform.flex_client.core.mindmap.controller {
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.geom.Point;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeDragController extends ControllerBase implements IDragController {
				
		public function activate(context:DiagramShellContext, model:Object, initialX:Number, initialY:Number):void {		
			if (MindMapDiagramShell(context.diagramShell).getModelController(model).isRoot(context, model)) { 
				// don't drag the root node
				return;
			}
			var dynamicObject:Object = getDynamicObject(context, model);
			dynamicObject.initialX = initialX;
			dynamicObject.initialY = initialY;
		}
		
		public function drag(context:DiagramShellContext, model:Object, deltaX:Number, deltaY:Number):void {
//			getDynamicObject(model).finalX = getDynamicObject(model).initialX + deltaX;
//			getDynamicObject(model).finalY = getDynamicObject(model).initialY + deltaY;
//			
//			var point:Point = new Point(getDynamicObject(model).finalX, getDynamicObject(model).finalY);
//			var renderer:IVisualElement = getRendererFromCoordinates(point);			
//			var dropModel:Object = IDataRenderer(renderer).data;
//						
//			if (!isDropAccepted(renderer, model, dropModel)) { 
//				// or not accepted edit part
//				deletePlaceHolder(model);
//				return;
//			}
//			
//			var placeHolder:MoveResizePlaceHolder = getPlaceHolder(model);
//			// set default values
//			var rect:Rectangle = diagramShell.getControllerProvider(dropModel).getAbsoluteLayoutRectangleController(dropModel).getBounds(dropModel);
//			placeHolder.x = rect.x;
//			placeHolder.y = rect.y;
//			placeHolder.width = rect.width;
//			placeHolder.height = rect.height;		
//			placeHolder.colors = [0x000000, 0xFFFFFF];
//			
//			var side:int = MindMapDiagramShell.NONE;
//			if (getModelController(dropModel).getSide(dropModel) == MindMapDiagramShell.LEFT) { 
//				if (new Rectangle(rect.x, rect.y, rect.width / 2, rect.height).containsPoint(point)) {	
//					// set styles for left rectangle			
//					placeHolder.width = rect.width / 2;
//					placeHolder.gradientBoxRotation = 0;
//					side =  MindMapDiagramShell.LEFT;
//				} else { 
//					// set styles for top rectangle
//					placeHolder.gradientBoxRotation = Math.PI / 2;
//				}
//			} else if (getModelController(dropModel).getSide(dropModel) == MindMapDiagramShell.RIGHT) { 
//				if (new Rectangle(rect.x + rect.width / 2, rect.y, rect.width, rect.height).containsPoint(point)) {	
//					// set styles for right rectangle
//					placeHolder.x = rect.x + rect.width / 2;
//					placeHolder.width = rect.width / 2;
//					placeHolder.colors = [0xFFFFFF, 0x000000];
//					placeHolder.gradientBoxRotation = 0;
//					side =  MindMapDiagramShell.RIGHT;
//				} else {	
//					// set styles for top rectangle			
//					placeHolder.gradientBoxRotation = Math.PI / 2;
//				}
//			} else { // root model	
//				// set styles for right rectangle					
//				placeHolder.x = rect.x + rect.width / 2;
//				placeHolder.width = rect.width / 2;
//				placeHolder.colors = [0xFFFFFF, 0x000000];
//				side =  MindMapDiagramShell.RIGHT;				
//			}
//			getDynamicObject(model).side = side;
		}
		
		public function drop(context:DiagramShellContext, model:Object):void {
//			var dropPoint:Point = new Point(getDynamicObject(model).finalX, getDynamicObject(model).finalY);
//			var renderer:IVisualElement = getRendererFromCoordinates(dropPoint);
//			
//			var dropModel:Object = IDataRenderer(renderer).data;
//			if (!isDropAccepted(renderer, model, dropModel)) { 
//				// or not accepted edit part
//				return;
//			}
//			
//			deletePlaceHolder(model);
//						
//			var side:int = getDynamicObject(model).side;
//			
//			// remove model from current parent
//			var dragParentModel:Object = diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(model);		
//
//			// calculate new parent and position based on side
//			var dropParentModel:Object = (side != MindMapDiagramShell.NONE) ? dropModel : diagramShell.getControllerProvider(dropModel).getModelChildrenController(dropModel).getParent(dropModel);	
//			var children:IList;
//			if (diagramShell.getControllerProvider(dropParentModel).getModelChildrenController(dropParentModel).getParent(dropParentModel) == null) {
//				children = MindMapDiagramShell(diagramShell).getModelController(dropParentModel).getChildren(dropParentModel, side);
//			} else {
//				children = MindMapDiagramShell(diagramShell).getModelController(dropParentModel).getChildren(dropParentModel);	
//			}
//					
//			var index:int = -1;
//			if (side != MindMapDiagramShell.NONE) {
//				// left/right => put as last child in list
//				index = children != null ? children.length : -2;
//			} else { 
//				// top
//				var moveDragModelDownInSameParent:Boolean = false;
//				for (var i:int = 0; i < children.length; i++) {
//					if (i == children.length - 1 && children.getItemAt(i - 1) == model) {
//						// if dragModel = last but one & dropModel = last => don't allow move (index remains -1)
//						break;
//					}
//					if (children.getItemAt(i) == model) {
//						// dragModel & dropModel are siblings => index must be decreased with 1 at the end
//						moveDragModelDownInSameParent = true;
//					}
//					if (children.getItemAt(i) == dropModel) {
//						// found dropModel index
//						index = moveDragModelDownInSameParent ? i - 1 : i;
//						break;
//					}
//				}
//			}
//			
//			// TODO CC: to implement on server side
//			CorePlugin.getInstance().serviceLocator.invoke("nodeService.move", [model.id, dropParentModel.id, index]);
//						
//			// select model or parent 
//			diagramShell.selectedItems.removeAll();
		}
		
		private function selectModelAfterDroppingHandler(result:Object):void {
//			NotationMindMapDiagramShell(diagramShell).editorStatefulClient.service_setSide(
//				result.getItemAt(0).id, 
//				result.getItemAt(1));
		}
		
		public function deactivate(context:DiagramShellContext, model:Object):void {
			delete getDynamicObject(context, model).initialX;
			delete getDynamicObject(context, model).initialY;
			delete getDynamicObject(context, model).finalX;
			delete getDynamicObject(context, model).finalY;
			delete getDynamicObject(context, model).side;
		}
		
		private function getDynamicObject(context:DiagramShellContext, model:Object):Object {
			return DynamicModelExtraInfoController(context.diagramShell.getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(context, model);
		}
		
		private function getModelController(context:DiagramShellContext, model:Object):IMindMapModelController {
			return MindMapDiagramShell(context.diagramShell).getModelController(model);
		}
		
		private function deletePlaceHolder(context:DiagramShellContext, model:Object):void {
			var placeHolder:MoveResizePlaceHolder = getDynamicObject(context, model).placeHolder;
			if (placeHolder != null) {
				context.diagramShell.diagramRenderer.removeElement(placeHolder);
				delete getDynamicObject(context, model).placeHolder;		
			}
		}
		
		private function getPlaceHolder(context:DiagramShellContext, model:Object):MoveResizePlaceHolder {
			var dynamicObject:Object = getDynamicObject(context, model);
			var placeHolder:MoveResizePlaceHolder = dynamicObject.placeHolder;
			if (placeHolder == null) {
				placeHolder = new MoveResizePlaceHolder();				
				placeHolder.alphas = [0.4, 0.4];
				placeHolder.ratios = [0, 255];				
				dynamicObject.placeHolder = placeHolder;
				context.diagramShell.diagramRenderer.addElement(placeHolder);
			}
			return placeHolder;
		}
		
		protected function getRendererFromCoordinates(context:DiagramShellContext, point:Point):IVisualElement {
			var stage:Stage = DisplayObject(context.diagramShell.diagramRenderer).stage;
			var arr:Array = stage.getObjectsUnderPoint(new Point(stage.mouseX, stage.mouseY));
			
			var renderer:IVisualElement;
			var i:int;
			for (i = arr.length - 1; i >= 0;  i--) {
				renderer = getRendererFromDisplay(context, arr[i]);
				if (renderer != null) {					
					return renderer;
				}
			}
			return null;
		}
		
		protected function getRendererFromDisplay(context:DiagramShellContext, obj:Object):IVisualElement {			
			// in order for us to traverse its hierrarchy
			// it has to be a DisplayObject
			if (!(obj is DisplayObject) || obj is MoveResizePlaceHolder) {
				return null;
			}
			
			// traverse all the obj's hierarchy	
			while (obj != null) {
				if (obj is DiagramRenderer) {
					return IVisualElement(obj);
				}
				if (obj is IDataRenderer && context.diagramShell.modelToExtraInfoMap[IDataRenderer(obj).data] != null) {
					// found it
					return IVisualElement(obj);					
				}
				obj = DisplayObject(obj).parent;
			}
			
			// no found on the obj's hierarchy
			return null;
		}
		
		private function isDropAccepted(context:DiagramShellContext, renderer:Object, dragModel:Object, dropModel:Object):Boolean {
			if (renderer is DiagramRenderer // don't drop over diagram 
				|| !dragModelIsParentForDropModel(context, dragModel, dropModel)) { // or children structure 				
				return false;
			}
			return true;
		}
		
		private function dragModelIsParentForDropModel(context:DiagramShellContext, dragModel:Object, dropModel:Object):Boolean {
			if (dragModel == dropModel) {
				return false;
			}
			if (context.diagramShell.getControllerProvider(dropModel).getModelChildrenController(dropModel).getParent(context, dropModel) == null) {
				return true;
			}
			return dragModelIsParentForDropModel(context, dragModel, context.diagramShell.getControllerProvider(dropModel).getModelChildrenController(dropModel).getParent(context, dropModel));
		}
	}
}