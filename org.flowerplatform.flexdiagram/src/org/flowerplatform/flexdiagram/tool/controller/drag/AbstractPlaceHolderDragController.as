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
package org.flowerplatform.flexdiagram.tool.controller.drag {
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class AbstractPlaceHolderDragController extends ControllerBase implements IDragController {
		
		public function AbstractPlaceHolderDragController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		protected function getInitialBounds(model:Object):Rectangle {
			throw new Error("This method should be implemented");
		}
						
		public function activate(model:Object, initialX:Number, initialY:Number):void {
			var movePlaceHolder:MoveResizePlaceHolder = new MoveResizePlaceHolder();
			var rect:Rectangle = getInitialBounds(model);
			
			movePlaceHolder.x = rect.x;
			movePlaceHolder.y = rect.y;
			movePlaceHolder.width = rect.width;
			movePlaceHolder.height = rect.height;
			
			diagramShell.modelToExtraInfoMap[model].movePlaceHolder = movePlaceHolder;
			diagramShell.diagramRenderer.addElement(movePlaceHolder);
			
//			movePlaceHolder.setLayoutBoundsPosition(rect.x, rect.y);
//			movePlaceHolder.setLayoutBoundsSize(rect.width, rect.height);
			
			diagramShell.modelToExtraInfoMap[model].initialX = movePlaceHolder.x;
			diagramShell.modelToExtraInfoMap[model].initialY = movePlaceHolder.y;
		}
		
		public function drag(model:Object, deltaX:Number, deltaY:Number):void {
			var movePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].movePlaceHolder;
			
			movePlaceHolder.x = diagramShell.modelToExtraInfoMap[model].initialX + deltaX;
			movePlaceHolder.y = diagramShell.modelToExtraInfoMap[model].initialY + deltaY;		
		}
		
		public function drop(model:Object):void {
			throw new Error("This method should be implemented");
		}
		
		public function deactivate(model:Object):void {
			var movePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].movePlaceHolder;			
			diagramShell.diagramRenderer.removeElement(movePlaceHolder);
			
			delete diagramShell.modelToExtraInfoMap[model].movePlaceHolder;			
			delete diagramShell.modelToExtraInfoMap[model].initialX;
			delete diagramShell.modelToExtraInfoMap[model].initialY;			
		}		
		
	}
}