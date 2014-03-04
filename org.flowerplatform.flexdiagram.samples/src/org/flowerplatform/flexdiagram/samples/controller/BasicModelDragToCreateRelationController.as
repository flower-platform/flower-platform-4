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
	import org.flowerplatform.flexdiagram.tool.controller.DragToCreateRelationController;
	
	public class BasicModelDragToCreateRelationController extends DragToCreateRelationController {
				
		override public function activate(context:DiagramShellContext, model:Object):void {
			trace("startDragging");
		}
		
		override public function drag(context:DiagramShellContext, model:Object, deltaX:Number, deltaY:Number):void {
			trace("update");
		}
		
		override public function drop(context:DiagramShellContext, sourceModel:Object, targetModel:Object):void {
			trace("endDragging");		
			
			context.diagramShell.mainToolFinishedItsJob();
		}
		
		override public function deactivate(context:DiagramShellContext, model:Object):void {
			trace("deactivate");
			
		}
	}
}