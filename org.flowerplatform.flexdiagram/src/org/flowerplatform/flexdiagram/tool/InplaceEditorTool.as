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
package org.flowerplatform.flexdiagram.tool {
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.InplaceEditorController;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InplaceEditorTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "InplaceEditorTool";
		
		public function InplaceEditorTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_RIGHT_CLICK);		
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_DOWN, -1);
			WakeUpTool.wakeMeUpIfEventOccurs(diagramShell, this, WakeUpTool.MOUSE_UP);			
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			context.shellContext = diagramShell.getNewDiagramShellContext();			
			if (eventType == WakeUpTool.MOUSE_RIGHT_CLICK) {
				// right click event performed (opens menu) -> don't activate
				context.wakedByRightClickEvent = true;
			} else if (eventType == WakeUpTool.MOUSE_DOWN) {
				context.wakedByMouseDownEvent = false;
				var renderer:IVisualElement = getRendererFromDisplayCoordinates();
				if (renderer is IDataRenderer && !(renderer is DiagramRenderer)) {
					var model:Object = IDataRenderer(renderer).data;
					if (ControllerUtils.getInplaceEditorController(context.shellContext, model) != null) {
						var selected:Boolean = isSelected(renderer);
						if (!selected || (selected && diagramShell.selectedItems.length > 1)) {
							// if not selected or multiple selection
							return false;
						}
						context.wakedByMouseDownEvent = !initialEvent.ctrlKey && !initialEvent.shiftKey;
						return false;
					}
				}
			} else if (context.wakedByMouseDownEvent && !context.wakedByRightClickEvent) {
				return true;
			}
			return false;
		}
		
		override public function activateDozingMode():void {
			diagramRenderer.stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);			
		}
		
		override public function deactivateDozingMode():void {
			diagramRenderer.stage.removeEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
		}
		
		override public function activateAsMainTool():void {
			context.model = IDataRenderer(getRendererFromDisplayCoordinates()).data;
			
			diagramRenderer.addEventListener(MouseEvent.CLICK, mouseClickHandler);
			
			var inplaceEditorController:InplaceEditorController = ControllerUtils.getInplaceEditorController(context.shellContext, context.model);
			if (inplaceEditorController != null) {
				if (inplaceEditorController.canActivate(context.shellContext, context.model)) {
					inplaceEditorController.activate(context.shellContext, context.model);
					if (context.wakedByMouseDownEvent) {
						diagramRenderer.stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);	
					}
				}
			}
			super.activateAsMainTool();
		}
				
		override public function deactivateAsMainTool():void {
			var inplaceEditorController:InplaceEditorController = ControllerUtils.getInplaceEditorController(context.shellContext, context.model);
			if (inplaceEditorController != null) {
				inplaceEditorController.deactivate(context.shellContext, context.model);
				if (context.wakedByMouseDownEvent) {
					diagramRenderer.stage.removeEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);	
				}
			}
			
			delete context.model;		
			delete context.shellContext;
			
			diagramRenderer.removeEventListener(MouseEvent.CLICK, mouseClickHandler);	
			super.deactivateAsMainTool();
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Mariana Gheorghe
		 */
		private function keyDownHandler(event:KeyboardEvent):void {
			switch (event.keyCode) {
				case Keyboard.F2: // active tool
					var model:Object = IDataRenderer(getRendererFromDisplayCoordinates()).data;
					context.shellContext = diagramShell.getNewDiagramShellContext();
					var inplaceEditorController:InplaceEditorController = ControllerUtils.getInplaceEditorController(context.shellContext, model);
					if (inplaceEditorController != null) {
						diagramShell.mainTool = this;
					}
					break;
				case Keyboard.ENTER: // commit value			
					if (!event.ctrlKey && this == diagramShell.mainTool) {
						ControllerUtils.getInplaceEditorController(context.shellContext, context.model).commit(context.shellContext, context.model);
					}					
					break;
				case Keyboard.ESCAPE: // abort
					if (this == diagramShell.mainTool) {
						ControllerUtils.getInplaceEditorController(context.shellContext, context.model).abort(context.shellContext, context.model);
					}
					break;
			}
		}
		
		private function mouseClickHandler(event:MouseEvent):void {			
			var renderer:IVisualElement = getRendererFromDisplayCoordinates(true);
			if (renderer == null || IDataRenderer(renderer).data != context.model) { // abort if click somewhere else
				if (ControllerUtils.getInplaceEditorController(context.shellContext, context.model).canActivate(context.shellContext, context.model)) {
					ControllerUtils.getInplaceEditorController(context.shellContext, context.model).commit(context.shellContext, context.model);
				}
			}
		}
		
		override public function reset():void {				
			delete context.wakedByMouseDownEvent;
			delete context.wakedByRightClickEvent;
		}
		
		/**
		 * Walking on parent hierarchy, verifies if a model with selection controller
		 * is selected or not.
		 */ 
		private function isSelected(renderer:IVisualElement):Boolean {
			if (renderer is IDataRenderer) {	
				var model:Object = IDataRenderer(renderer).data;				
				if (ControllerUtils.getSelectionController(context.shellContext, model) != null) {
					return diagramShell.selectedItems.getItemIndex(model) != -1;
				}								
			}			 	
			var parent:IVisualElement = IVisualElement(renderer.parent);
			if (parent is DiagramRenderer) {
				return false;
			}	
			return isSelected(parent);			
		}
		
	}
	
}
