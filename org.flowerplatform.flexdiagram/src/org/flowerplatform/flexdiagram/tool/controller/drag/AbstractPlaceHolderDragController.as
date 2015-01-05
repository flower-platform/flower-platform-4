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
package org.flowerplatform.flexdiagram.tool.controller.drag {
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class AbstractPlaceHolderDragController extends DragController {
				
		protected function getInitialBounds(context:DiagramShellContext, model:Object):Rectangle {
			throw new Error("This method should be implemented");
		}
						
		override public function activate(context:DiagramShellContext, model:Object, initialX:Number, initialY:Number):void {
			var movePlaceHolder:MoveResizePlaceHolder = new MoveResizePlaceHolder();
			var rect:Rectangle = getInitialBounds(context, model);
			
			movePlaceHolder.x = rect.x;
			movePlaceHolder.y = rect.y;
			movePlaceHolder.width = rect.width;
			movePlaceHolder.height = rect.height;
			
			context.diagramShell.modelToExtraInfoMap[model].movePlaceHolder = movePlaceHolder;
			context.diagramShell.diagramRenderer.addElement(movePlaceHolder);
			
//			movePlaceHolder.setLayoutBoundsPosition(rect.x, rect.y);
//			movePlaceHolder.setLayoutBoundsSize(rect.width, rect.height);
			
			context.diagramShell.modelToExtraInfoMap[model].initialX = movePlaceHolder.x;
			context.diagramShell.modelToExtraInfoMap[model].initialY = movePlaceHolder.y;
		}
		
		override public function drag(context:DiagramShellContext, model:Object, deltaX:Number, deltaY:Number):void {
			var movePlaceHolder:MoveResizePlaceHolder = context.diagramShell.modelToExtraInfoMap[model].movePlaceHolder;
			
			movePlaceHolder.x = context.diagramShell.modelToExtraInfoMap[model].initialX + deltaX;
			movePlaceHolder.y = context.diagramShell.modelToExtraInfoMap[model].initialY + deltaY;		
		}
		
		override public function drop(context:DiagramShellContext, model:Object):void {
			throw new Error("This method should be implemented");
		}
		
		override public function deactivate(context:DiagramShellContext, model:Object):void {
			var movePlaceHolder:MoveResizePlaceHolder = context.diagramShell.modelToExtraInfoMap[model].movePlaceHolder;			
			context.diagramShell.diagramRenderer.removeElement(movePlaceHolder);
			
			delete context.diagramShell.modelToExtraInfoMap[model].movePlaceHolder;			
			delete context.diagramShell.modelToExtraInfoMap[model].initialX;
			delete context.diagramShell.modelToExtraInfoMap[model].initialY;			
		}		
		
	}
}