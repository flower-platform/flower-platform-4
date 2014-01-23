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
package org.flowerplatform.flexdiagram.samples.controller {
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	import org.flowerplatform.flexdiagram.ui.ResizeAnchor;
	
	public class BasicModelResizeController extends ControllerBase implements IResizeController {
		
		public function BasicModelResizeController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function activate(model:Object):void {
			var resizePlaceHolder:MoveResizePlaceHolder = new MoveResizePlaceHolder();
			resizePlaceHolder.x = BasicModel(model).x;
			resizePlaceHolder.y = BasicModel(model).y;
			resizePlaceHolder.width = BasicModel(model).width;
			resizePlaceHolder.height = BasicModel(model).height;
			
			diagramShell.modelToExtraInfoMap[model].resizePlaceHolder = resizePlaceHolder;
			diagramShell.diagramRenderer.addElement(resizePlaceHolder);
			
			diagramShell.modelToExtraInfoMap[model].initialX = resizePlaceHolder.x;
			diagramShell.modelToExtraInfoMap[model].initialY = resizePlaceHolder.y;
			diagramShell.modelToExtraInfoMap[model].initialWidth = resizePlaceHolder.width;
			diagramShell.modelToExtraInfoMap[model].initialHeight = resizePlaceHolder.height;
		}
			
		public function drag(model:Object, deltaX:Number, deltaY:Number, type:String):void {	
			var resizePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].resizePlaceHolder;
			
			var newX:int = resizePlaceHolder.x, newY:int = resizePlaceHolder.y;
			var newWidth:int = resizePlaceHolder.width, newHeight:int = resizePlaceHolder.height;
			switch(type) {
				case ResizeAnchor.LEFT_UP: 
					newX = diagramShell.modelToExtraInfoMap[model].initialX + deltaX;
					newY = diagramShell.modelToExtraInfoMap[model].initialY + deltaY;					
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth - (newX - diagramShell.modelToExtraInfoMap[model].initialX);
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight - (newY - diagramShell.modelToExtraInfoMap[model].initialY);
					break;
				case ResizeAnchor.LEFT_MIDDLE:
					newX = diagramShell.modelToExtraInfoMap[model].initialX + deltaX;
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth - (newX - diagramShell.modelToExtraInfoMap[model].initialX);
					break;
				case ResizeAnchor.LEFT_DOWN:
					newX = diagramShell.modelToExtraInfoMap[model].initialX + deltaX; 
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth - (newX - diagramShell.modelToExtraInfoMap[model].initialX);
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight + deltaY;
					break;
				case ResizeAnchor.MIDDLE_DOWN:
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight + deltaY;
					break;
				case ResizeAnchor.MIDDLE_UP:
					newY = diagramShell.modelToExtraInfoMap[model].initialY + deltaY;
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight - (newY - diagramShell.modelToExtraInfoMap[model].initialY);
					break;
				case ResizeAnchor.RIGHT_DOWN:
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth + deltaX;
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight + deltaY;
					break;
				case ResizeAnchor.RIGHT_MIDDLE:
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth + deltaX;
					break;
				case ResizeAnchor.RIGHT_UP:
					newY = diagramShell.modelToExtraInfoMap[model].initialY + deltaY;
					newWidth = diagramShell.modelToExtraInfoMap[model].initialWidth + deltaX;
					newHeight = diagramShell.modelToExtraInfoMap[model].initialHeight - (newY - diagramShell.modelToExtraInfoMap[model].initialY);
					break;
			}
//			var fig:IDraggableAndResizableFigure = IDraggableAndResizableFigure(editPart.getFigure());
//			if (newWidth < fig.getAcceptedMinWidth()) {
//				if (newX != diagramShell.modelToExtraInfoMap[model].initialX)
//					newX += newWidth - fig.getAcceptedMinWidth();
//				newWidth = fig.getAcceptedMinWidth();
//			}
//			if (newHeight < fig.getAcceptedMinHeight()) {
//				if (newY != diagramShell.modelToExtraInfoMap[model].initialY)
//					newY += newHeight - fig.getAcceptedMinHeight()
//				newHeight = fig.getAcceptedMinHeight();
//			}
			// correction needed when x and y values are negative
			if (newX < 0) {
				newWidth += newX;
				newX = 0; 
			}
			if (newY < 0) {
				newHeight += newY;
				newY = 0;
			}
			resizePlaceHolder.x = newX;
			resizePlaceHolder.y = newY;
			resizePlaceHolder.width = newWidth;
			resizePlaceHolder.height = newHeight;
		}
		
		public function drop(model:Object):void {	
			var resizePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].resizePlaceHolder;
			
			BasicModel(model).x = resizePlaceHolder.x;
			BasicModel(model).y = resizePlaceHolder.y;
			BasicModel(model).width = resizePlaceHolder.width;
			BasicModel(model).height = resizePlaceHolder.height;
		}
		
		public function deactivate(model:Object):void {
			diagramShell.diagramRenderer.removeElement(diagramShell.modelToExtraInfoMap[model].resizePlaceHolder);			
			delete diagramShell.modelToExtraInfoMap[model].resizePlaceHolder;
			
			delete diagramShell.modelToExtraInfoMap[model].initialX;
			delete diagramShell.modelToExtraInfoMap[model].initialY;
			delete diagramShell.modelToExtraInfoMap[model].initialWidth;
			delete diagramShell.modelToExtraInfoMap[model].initialHeight;
		}
	}
}