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
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.event.ExecuteDragToCreateEvent;
	import org.flowerplatform.flexdiagram.renderer.connection.ConnectionRenderer;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public class DragToCreateRelationController extends ControllerBase implements IDragToCreateRelationController {
	
		public function DragToCreateRelationController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function activate(model:Object):void {
			// create temp connection
			var connection:ConnectionRenderer = new ConnectionRenderer();
			var modelRenderer:IVisualElement = diagramShell.getRendererForModel(model);
			var rect:Rectangle = DisplayObject(modelRenderer).getBounds(DisplayObject(diagramShell.diagramRenderer));
			var x:int = rect.x + rect.width / 2;
			var y:int = rect.y + rect.height / 2;
			connection._sourcePoint.x = connection._targetPoint.x = x;
			connection._sourcePoint.y = connection._targetPoint.y = y;
			
			// add to map
			diagramShell.modelToExtraInfoMap[model].tempConnection = connection;
			
			// add to diagram
			diagramShell.diagramRenderer.addElement(connection);
		}
		
		public function drag(model:Object, deltaX:Number, deltaY:Number):void {
			var connection:ConnectionRenderer = diagramShell.modelToExtraInfoMap[model].tempConnection;
			
			connection._targetPoint.x = deltaX; 
			connection._targetPoint.y = deltaY;
		}
		
		public function drop(sourceModel:Object, targetModel:Object):void {
			if (targetModel != null) {				
				// the tool will be deactivated later, so wait until then
				diagramShell.modelToExtraInfoMap[sourceModel].waitingToDeactivateDragTool = true;
				
				// create context
				var context:Object = new Object();
				context.sourceModel = sourceModel;
				context.targetModel =  targetModel;	
				// dispatch event in order to let others implement the creation behavior
				diagramShell.dispatchEvent(new ExecuteDragToCreateEvent(context, true));
			} else {
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		public function deactivate(model:Object):void {
			var connection:ConnectionRenderer = diagramShell.modelToExtraInfoMap[model].tempConnection;
			diagramShell.diagramRenderer.removeElement(connection);
			
			delete diagramShell.modelToExtraInfoMap[model].tempConnection;
			delete diagramShell.modelToExtraInfoMap[model].waitingToDeactivateDragTool;
		}
	}
}