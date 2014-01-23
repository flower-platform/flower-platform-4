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
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	
	public class BasicModelDragToCreateRelationController extends ControllerBase implements IDragToCreateRelationController	{
		
		public function BasicModelDragToCreateRelationController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function activate(model:Object):void {
			trace("startDragging");
		}
		
		public function drag(model:Object, deltaX:Number, deltaY:Number):void {
			trace("update");
		}
		
		public function drop(sourceModel:Object, targetModel:Object):void {
			trace("endDragging");		
			
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {
			trace("deactivate");
			
		}
	}
}