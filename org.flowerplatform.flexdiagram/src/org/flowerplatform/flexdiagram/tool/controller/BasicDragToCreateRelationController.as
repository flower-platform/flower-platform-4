/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexdiagram.tool.controller {

	import flash.display.DisplayObject;
	import flash.geom.Rectangle;
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.event.ExecuteDragToCreateEvent;
	import org.flowerplatform.flexdiagram.renderer.connection.ConnectionRenderer;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public class BasicDragToCreateRelationController extends DragToCreateRelationController {
	
		public function BasicDragToCreateRelationController(orderIndex:int = 0) {
			super(orderIndex);
		}
		
		override public function activate(context:DiagramShellContext, model:Object):void {
			// create temp connection
			var connection:ConnectionRenderer = new ConnectionRenderer();
			var modelRenderer:IVisualElement = context.diagramShell.getRendererForModel(context, model);
			var rect:Rectangle = DisplayObject(modelRenderer).getBounds(DisplayObject(context.diagramShell.diagramRenderer));
			var x:int = rect.x + rect.width / 2;
			var y:int = rect.y + rect.height / 2;
			connection._sourcePoint.x = connection._targetPoint.x = x;
			connection._sourcePoint.y = connection._targetPoint.y = y;
			
			// add to map
			context.diagramShell.modelToExtraInfoMap[model].tempConnection = connection;
			
			// add to diagram
			context.diagramShell.diagramRenderer.addElement(connection);
		}
		
		override public function drag(context:DiagramShellContext, model:Object, deltaX:Number, deltaY:Number):void {
			var connection:ConnectionRenderer = context.diagramShell.modelToExtraInfoMap[model].tempConnection;
			
			connection._targetPoint.x = deltaX; 
			connection._targetPoint.y = deltaY;
		}
		
		override public function drop(context:DiagramShellContext, sourceModel:Object, targetModel:Object):void {
			if (targetModel != null) {				
				// the tool will be deactivated later, so wait until then
				context.diagramShell.modelToExtraInfoMap[sourceModel].waitingToDeactivateDragTool = true;
				
				// create context
				var toolContext:Object = new Object();
				toolContext.sourceModel = sourceModel;
				toolContext.targetModel =  targetModel;	
				// dispatch event in order to let others implement the creation behavior
				context.diagramShell.dispatchEvent(new ExecuteDragToCreateEvent(toolContext, true));
			} else {
				context.diagramShell.mainToolFinishedItsJob();
			}
		}
		
		override public function deactivate(context:DiagramShellContext, model:Object):void {
			var connection:ConnectionRenderer = context.diagramShell.modelToExtraInfoMap[model].tempConnection;
			context.diagramShell.diagramRenderer.removeElement(connection);
			
			delete context.diagramShell.modelToExtraInfoMap[model].tempConnection;
			delete context.diagramShell.modelToExtraInfoMap[model].waitingToDeactivateDragTool;
		}
	}
}