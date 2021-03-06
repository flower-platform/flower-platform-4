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
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.controller.AbstractController;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DragController extends AbstractController {
		
		public function DragController(orderIndex:int = 0) {
			super(orderIndex);
		}
		
		public function activate(context:DiagramShellContext, model:Object, initialX:Number, initialY:Number):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function drag(context:DiagramShellContext, model:Object, deltaX:Number, deltaY:Number):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function drop(context:DiagramShellContext, model:Object):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function deactivate(context:DiagramShellContext, model:Object):void {
			throw new Error("This method needs to be implemented.");
		}
		
	}
}