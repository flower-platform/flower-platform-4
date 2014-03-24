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
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	import org.flowerplatform.flexdiagram.tool.controller.ResizeController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	import org.flowerplatform.flexdiagram.ui.ResizeAnchor;
	
	public class BasicModelResizeController extends ResizeController {
		
		override public function activate(context:DiagramShellContext, model:Object):void {
			var extraInfo:Object = context.diagramShell.modelToExtraInfoMap[model];
			
			var resizePlaceHolder:MoveResizePlaceHolder = new MoveResizePlaceHolder();
			resizePlaceHolder.x = BasicModel(model).x;
			resizePlaceHolder.y = BasicModel(model).y;
			resizePlaceHolder.width = BasicModel(model).width;
			resizePlaceHolder.height = BasicModel(model).height;
			
			extraInfo.resizePlaceHolder = resizePlaceHolder;
			context.diagramShell.diagramRenderer.addElement(resizePlaceHolder);
			
			extraInfo.initialX = resizePlaceHolder.x;
			extraInfo.initialY = resizePlaceHolder.y;
			extraInfo.initialWidth = resizePlaceHolder.width;
			extraInfo.initialHeight = resizePlaceHolder.height;
		}
			
		override public function drag(context:DiagramShellContext, model:Object, deltaX:Number, deltaY:Number, type:String):void {	
			var extraInfo:Object = context.diagramShell.modelToExtraInfoMap[model];
			
			var resizePlaceHolder:MoveResizePlaceHolder = extraInfo.resizePlaceHolder;
			
			var newX:int = resizePlaceHolder.x, newY:int = resizePlaceHolder.y;
			var newWidth:int = resizePlaceHolder.width, newHeight:int = resizePlaceHolder.height;
			switch(type) {
				case ResizeAnchor.LEFT_UP: 
					newX = extraInfo.initialX + deltaX;
					newY = extraInfo.initialY + deltaY;					
					newWidth = extraInfo.initialWidth - (newX - extraInfo.initialX);
					newHeight = extraInfo.initialHeight - (newY - extraInfo.initialY);
					break;
				case ResizeAnchor.LEFT_MIDDLE:
					newX = extraInfo.initialX + deltaX;
					newWidth = extraInfo.initialWidth - (newX - extraInfo.initialX);
					break;
				case ResizeAnchor.LEFT_DOWN:
					newX = extraInfo.initialX + deltaX; 
					newWidth = extraInfo.initialWidth - (newX - extraInfo.initialX);
					newHeight = extraInfo.initialHeight + deltaY;
					break;
				case ResizeAnchor.MIDDLE_DOWN:
					newHeight = extraInfo.initialHeight + deltaY;
					break;
				case ResizeAnchor.MIDDLE_UP:
					newY = extraInfo.initialY + deltaY;
					newHeight = extraInfo.initialHeight - (newY - extraInfo.initialY);
					break;
				case ResizeAnchor.RIGHT_DOWN:
					newWidth = extraInfo.initialWidth + deltaX;
					newHeight = extraInfo.initialHeight + deltaY;
					break;
				case ResizeAnchor.RIGHT_MIDDLE:
					newWidth = extraInfo.initialWidth + deltaX;
					break;
				case ResizeAnchor.RIGHT_UP:
					newY = extraInfo.initialY + deltaY;
					newWidth = extraInfo.initialWidth + deltaX;
					newHeight = extraInfo.initialHeight - (newY - extraInfo.initialY);
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
		
		override public function drop(context:DiagramShellContext, model:Object):void {	
			var resizePlaceHolder:MoveResizePlaceHolder = context.diagramShell.modelToExtraInfoMap[model].resizePlaceHolder;
			
			BasicModel(model).x = resizePlaceHolder.x;
			BasicModel(model).y = resizePlaceHolder.y;
			BasicModel(model).width = resizePlaceHolder.width;
			BasicModel(model).height = resizePlaceHolder.height;
		}
		
		override public function deactivate(context:DiagramShellContext, model:Object):void {
			var extraInfo:Object = context.diagramShell.modelToExtraInfoMap[model];
			
			context.diagramShell.diagramRenderer.removeElement(extraInfo.resizePlaceHolder);			
			delete extraInfo.resizePlaceHolder;
			
			delete extraInfo.initialX;
			delete extraInfo.initialY;
			delete extraInfo.initialWidth;
			delete extraInfo.initialHeight;
		}
	}
}