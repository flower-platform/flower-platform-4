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
package org.flowerplatform.flexdiagram.samples.mindmap.controller {
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.geom.Point;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.drag.DragController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class SampleMindMapModelDragController extends DragController {
		
		override public function activate(context:DiagramShellContext, model:Object, initialX:Number, initialY:Number):void {		
			if (model.parent == null) { // don't drag the root node
				return;
			}
			getDynamicObject(context, model).initialX = initialX;
			getDynamicObject(context, model).initialY = initialY;
		}
		
		override public function drag(context:DiagramShellContext, model:Object, deltaX:Number, deltaY:Number):void {
//			getDynamicObject(model).finalX = getDynamicObject(model).initialX + deltaX;
//			getDynamicObject(model).finalY = getDynamicObject(model).initialY + deltaY;
//			
//			var point:Point = new Point(getDynamicObject(model).finalX, getDynamicObject(model).finalY);
//			var renderer:IVisualElement = getRendererFromCoordinates(point);			
//			var dropModel:Object = IDataRenderer(renderer).data;
//						
//			if (renderer is DiagramRenderer || !dragModelIsParentForDropModel(model, dropModel)) {
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
//			if (dropModel.side == MindMapDiagramShell.LEFT) { 
//				if (new Rectangle(rect.x, rect.y, rect.width / 2, rect.height).containsPoint(point)) {	// set styles for left rectangle			
//					placeHolder.width = rect.width / 2;
//					placeHolder.gradientBoxRotation = 0;
//					side =  MindMapDiagramShell.LEFT;
//				} else { // set styles for top rectangle
//					placeHolder.gradientBoxRotation = Math.PI / 2;
//				}
//			} else if (dropModel.side == MindMapDiagramShell.RIGHT) { 
//				if (new Rectangle(rect.x + rect.width / 2, rect.y, rect.width, rect.height).containsPoint(point)) {	// set styles for right rectangle
//					placeHolder.x = rect.x + rect.width / 2;
//					placeHolder.width = rect.width / 2;
//					placeHolder.colors = [0xFFFFFF, 0x000000];
//					placeHolder.gradientBoxRotation = 0;
//					side =  MindMapDiagramShell.RIGHT;
//				} else {	// set styles for top rectangle			
//					placeHolder.gradientBoxRotation = Math.PI / 2;
//				}
//			} else { // root model
//				if (new Rectangle(rect.x, rect.y, rect.width / 2, rect.height).containsPoint(point)) {	 // set styles for left rectangle
//					placeHolder.x = rect.x;
//					placeHolder.width = rect.width / 2;					
//					placeHolder.gradientBoxRotation = 0;
//					side =  MindMapDiagramShell.LEFT;
//				} else { // set styles for right rectangle					
//					placeHolder.x = rect.x + rect.width / 2;
//					placeHolder.width = rect.width / 2;
//					placeHolder.colors = [0xFFFFFF, 0x000000];
//					side =  MindMapDiagramShell.RIGHT;
//				}				
//			}
//			getDynamicObject(model).side = side;
		}
		
		override public function drop(context:DiagramShellContext, model:Object):void {
//			var dropPoint:Point = new Point(getDynamicObject(model).finalX, getDynamicObject(model).finalY);
//			var renderer:IVisualElement = getRendererFromCoordinates(dropPoint);
//			
//			var dropModel:Object = IDataRenderer(renderer).data;
//			if (renderer is DiagramRenderer || !dragModelIsParentForDropModel(model, dropModel)) { // don't drop over diagram or same model
//				return;
//			}
//			
//			deletePlaceHolder(model);
//						
//			var side:int = getDynamicObject(model).side;
//			
//			// remove model from current parent
//			var dragParentModel:Object = diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(model);;		
//			ArrayList(getModelController(dragParentModel).getChildren(dragParentModel)).removeItem(model);	
//			SampleMindMapModelController(IMindMapControllerProvider(diagramShell.getControllerProvider(model)).getMindMapModelController(model)).disposeModelHandlerRecursive(model, true);			
//			SampleMindMapModelController(IMindMapControllerProvider(diagramShell.getControllerProvider(dragParentModel)).getMindMapModelController(dragParentModel)).updateModelHandler(dragParentModel);
//			
//			// calculate new parent and position based on side
//			var dropParentModel:Object = (side != MindMapDiagramShell.NONE) ? dropModel : diagramShell.getControllerProvider(dropModel).getModelChildrenController(dropModel).getParent(dropModel);	
//			var children:ArrayList = ArrayList(getModelController(dropParentModel).getChildren(dropParentModel));	
//			var index:Number = (side != MindMapDiagramShell.NONE) ? children.length : children.getItemIndex(dropModel);
//			
//			// add model in new parent
//			ArrayList(getModelController(dropParentModel).getChildren(dropParentModel)).addItemAt(model, index);
//			dropParentModel.hasChildren = true;
//			getModelController(model).setSide(model, (side != MindMapDiagramShell.NONE) ? side : getModelController(dropModel).getSide(dropModel));
//			SampleMindMapModelController(getModelController(model)).setParent(model, dropParentModel);	
//			SampleMindMapModelController(IMindMapControllerProvider(diagramShell.getControllerProvider(dropParentModel)).getMindMapModelController(dropParentModel)).updateModelHandler(dropParentModel);
//						
//			// select model or parent 
//			diagramShell.selectedItems.removeAll();
//			if (getModelController(dropParentModel).getExpanded(dropParentModel)) {
//				diagramShell.selectedItems.addItem(model);
//			} else {
//				diagramShell.selectedItems.addItem(dropParentModel);
//			}
		}
		
		override public function deactivate(context:DiagramShellContext, model:Object):void {
			delete getDynamicObject(context, model).initialX;
			delete getDynamicObject(context, model).initialY;
			delete getDynamicObject(context, model).finalX;
			delete getDynamicObject(context, model).finalY;
			delete getDynamicObject(context, model).side;
		}
		
		private function getDynamicObject(context:DiagramShellContext, model:Object):Object {
			return DynamicModelExtraInfoController(ControllerUtils.getModelExtraInfoController(context, model)).getDynamicObject(context, model);
		}
		
		private function getModelController(context:DiagramShellContext, model:Object):MindMapModelController {
			return MindMapDiagramShell(context.diagramShell).getModelController(context, model);
		}
		
		private function deletePlaceHolder(context:DiagramShellContext, model:Object):void {
			var placeHolder:MoveResizePlaceHolder = getDynamicObject(context, model).placeHolder;
			if (placeHolder != null) {
				context.diagramShell.diagramRenderer.removeElement(placeHolder);
				delete getDynamicObject(context, model).placeHolder;		
			}
		}
		
		private function getPlaceHolder(context:DiagramShellContext, model:Object):MoveResizePlaceHolder {
			var placeHolder:MoveResizePlaceHolder = getDynamicObject(context, model).placeHolder;
			if (placeHolder == null) {
				placeHolder = new MoveResizePlaceHolder();				
				placeHolder.alphas = [0.4, 0.4];
				placeHolder.ratios = [0, 255];				
				getDynamicObject(context, model).placeHolder = placeHolder;
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
		
		private function dragModelIsParentForDropModel(context:DiagramShellContext, dragModel:Object, dropModel:Object):Boolean {
			if (dragModel == dropModel) {
				return false;
			}
			if (ControllerUtils.getModelChildrenController(context, dropModel).getParent(context, dropModel) == null) {
				return true;
			}
			return dragModelIsParentForDropModel(context, dragModel, ControllerUtils.getModelChildrenController(context, dropModel).getParent(context, dropModel));
		}
	}
}