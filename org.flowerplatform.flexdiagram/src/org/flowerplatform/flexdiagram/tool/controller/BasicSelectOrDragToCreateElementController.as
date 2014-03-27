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
package org.flowerplatform.flexdiagram.tool.controller {
	
	import flash.display.DisplayObject;
	import flash.geom.Rectangle;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.AbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.event.ExecuteDragToCreateEvent;
	import org.flowerplatform.flexdiagram.tool.SelectOrDragToCreateElementTool;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class BasicSelectOrDragToCreateElementController extends SelectOrDragToCreateElementController {
				
		private static const DRAG_TO_CREATE_MIN_WIDTH_DEFAULT:int = 30;
		private var _dragToCreateMinWidth:Number = DRAG_TO_CREATE_MIN_WIDTH_DEFAULT;
		
		private static const DRAG_TO_CREATE_MIN_HEIGHT_DEFAULT:int = 30;
		private var _dragToCreateMinHeight:Number = DRAG_TO_CREATE_MIN_HEIGHT_DEFAULT;
		
		private static const DRAG_TO_CREATE_ALPHA_DEFAULT:Number = 0.4;		
		private var _dragToCreateAlpha:Number = DRAG_TO_CREATE_ALPHA_DEFAULT;
		
		private static const DRAG_TO_SELECT_COLOR_DEFAULT:uint = 0x00007F;		
		private var _dragToSelectColor:uint = DRAG_TO_SELECT_COLOR_DEFAULT;
		
		private static const DRAG_TO_CREATE_COLOR_DEFAULT:uint = 0x007F00;		
		private var _dragToCreateColor:uint = DRAG_TO_CREATE_COLOR_DEFAULT;
		
		private static const DRAG_TO_SELECT_ALPHA_DEFAULT:Number = 0.4;		
		private var _dragToSelectAlpha:Number = DRAG_TO_SELECT_ALPHA_DEFAULT;
		
		public function get dragToSelectColor():uint {
			return _dragToSelectColor;
		}
		
		public function set dragToSelectColor(value:uint):void {
			_dragToSelectColor = value;
		}
		
		public function get dragToCreateColor():uint {
			return _dragToCreateColor;
		}
		
		public function set dragToCreateColor(value:uint):void {
			_dragToCreateColor = value;
		}
				
		public function get dragToSelectAlpha():Number {
			return _dragToSelectAlpha;
		} 
		
		public function set dragToSelectAlpha(value:Number):void {
			_dragToSelectAlpha = value;
		}
		
		public function get dragToCreateAlpha():Number {
			return _dragToCreateAlpha;
		}
		
		public function set dragToCreateAlpha(value:Number):void {
			_dragToCreateAlpha = value;
		}
			
		public function get dragToCreateMinWidth():int {
			return _dragToCreateMinWidth;
		}
				
		public function set dragToCreateMinWidth(value:int):void {
			_dragToCreateMinWidth = value;
		}
			
		public function get dragToCreateMinHeight():int {
			return _dragToCreateMinHeight;
		}
		
		public function set dragToCreateMinHeight(value:int):void {
			_dragToCreateMinHeight = value;
		}
				
		override public function activate(context:DiagramShellContext, model:Object, initialX:Number, initialY:Number, mode:String):void {
			// create placeholder
			var selectDragToCreatePlaceHolder:MoveResizePlaceHolder = new MoveResizePlaceHolder();
			selectDragToCreatePlaceHolder.x = initialX;
			selectDragToCreatePlaceHolder.y = initialY;
			
			// put placeholder in model's extra info
			context.diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder = selectDragToCreatePlaceHolder;
			context.diagramShell.modelToExtraInfoMap[model].selectDragToCreateMode = mode;
			
			// add it to diagram
			context.diagramShell.diagramRenderer.addElement(selectDragToCreatePlaceHolder);			
		}
		
		override public function drag(context:DiagramShellContext, model:Object, deltaX:Number, deltaY:Number):void {	
			// update placeholder dimensions
			var selectDragToCreatePlaceHolder:MoveResizePlaceHolder = context.diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder;
			
			selectDragToCreatePlaceHolder.width = deltaX;
			selectDragToCreatePlaceHolder.height = deltaY;
			
			var models:IList = getModelsUnderPlaceHolder(context, model, selectDragToCreatePlaceHolder);
				
			if (models.length != 0 || 
				selectDragToCreatePlaceHolder.width < _dragToCreateMinWidth || 
				selectDragToCreatePlaceHolder.height < _dragToCreateMinHeight) { 
				// SELECT MODE
				selectDragToCreatePlaceHolder.colors = [_dragToSelectColor];
				selectDragToCreatePlaceHolder.alphas = [_dragToSelectAlpha];					
			} else { 
				// DRAG MODE
				selectDragToCreatePlaceHolder.colors = [_dragToCreateColor];
				selectDragToCreatePlaceHolder.alphas = [_dragToCreateAlpha];			
			}			
		}
		
		override public function drop(context:DiagramShellContext, model:Object):void	{
			var selectDragToCreatePlaceHolder:MoveResizePlaceHolder = context.diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder;			
			var mode:String = context.diagramShell.modelToExtraInfoMap[model].selectDragToCreateMode;
			
			var models:IList = getModelsUnderPlaceHolder(context, model, selectDragToCreatePlaceHolder);
			
			if (models.length == 0) {
				// the tool will be deactivated later, so wait until then
				context.diagramShell.modelToExtraInfoMap[model].waitingToDeactivateDragTool = true;
				
				// create context
				var toolContext:Object = new Object();
				toolContext.rectangle = selectDragToCreatePlaceHolder.getRect(DisplayObject(context.diagramShell.diagramRenderer));				
				// get rectangle relative to application
				toolContext.rectangle = context.diagramShell.convertCoordinates(toolContext.rectangle, UIComponent(context.diagramShell.diagramRenderer), UIComponent(FlexGlobals.topLevelApplication));
				// dispatch event in order to let others implement the creation behavior
				context.diagramShell.dispatchEvent(new ExecuteDragToCreateEvent(toolContext, true));
			} else {
				// select/deselect models
				for (var i:int = 0; i < models.length; i++) {
					var obj:Object = models.getItemAt(i);
					if (mode == SelectOrDragToCreateElementTool.SELECT_MODE_ADD) {
						context.diagramShell.selectedItems.addItem(obj);
					} else if (mode == SelectOrDragToCreateElementTool.SELECT_MODE_SUBSTRACT) {
						if (context.diagramShell.selectedItems.getItemIndex(obj) != -1) {
							context.diagramShell.selectedItems.removeItem(obj);
						} else {
							context.diagramShell.selectedItems.addItem(obj);
						}
					}
				}
				// done
				context.diagramShell.mainToolFinishedItsJob();
			}			
		}
		
		override public function deactivate(context:DiagramShellContext, model:Object):void {	
			var selectDragToCreatePlaceHolder:MoveResizePlaceHolder = context.diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder;
			if (selectDragToCreatePlaceHolder != null) {
				// remove placeholder from diagram
				context.diagramShell.diagramRenderer.removeElement(context.diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder);
				
				// remove placeholder from model's extra info
				delete context.diagramShell.modelToExtraInfoMap[model].selectDragToCreatePlaceHolder;
				delete context.diagramShell.modelToExtraInfoMap[model].selectDragToCreateMode;
				delete context.diagramShell.modelToExtraInfoMap[model].waitingToDeactivateDragTool;
			}
		}
		
		private function getModelsUnderPlaceHolder(context:DiagramShellContext, model:Object, selectDragToCreatePlaceHolder:MoveResizePlaceHolder):IList {
			var models:ArrayList = new ArrayList();
			var children:IList = ControllerUtils.getModelChildrenController(context, model).getChildren(context, model);
			for (var i:int = 0; i < children.length; i++) {
				var child:Object = children.getItemAt(i);
				var absLayoutRectangleController:AbsoluteLayoutRectangleController = ControllerUtils.getAbsoluteLayoutRectangleController(context, child);
				if (absLayoutRectangleController != null) {
					var placeHolderBounds:Rectangle = selectDragToCreatePlaceHolder.getBounds(DisplayObject(context.diagramShell.diagramRenderer));
					if (placeHolderBounds.intersects(absLayoutRectangleController.getBounds(context, child))) { 
						// intersects hit area
						models.addItem(child);
					}
				}
			}
			return models;
		}
	}
}
