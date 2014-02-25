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
package org.flowerplatform.flexdiagram.controller.selection
{
	import mx.core.IVisualElement;
	import mx.events.DynamicEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexdiagram.renderer.selection.AbstractSelectionRenderer;
	
	public class SelectionController extends ControllerBase implements ISelectionController {
		
		public var selectionRendererClass:Class;
		
		public function SelectionController(selectionRendererClass:Class = null) {		
			this.selectionRendererClass = selectionRendererClass;
		}
		
		public function setSelectedState(context:DiagramShellContext, model:Object, renderer:IVisualElement, isSelected:Boolean, isMainSelection:Boolean):void {
			var modelExtraInfoController:DynamicModelExtraInfoController = 
				DynamicModelExtraInfoController(context.diagramShell.getControllerProvider(model).getModelExtraInfoController(model));
			
			var dynamicObject:Object = modelExtraInfoController.getDynamicObject(context, model);
			
			if (dynamicObject.selected != isSelected) {
				dynamicObject.selected = isSelected;
			}
			
			if (renderer == null) {
				return;
			}
			
			// add/delete selectionRenderer
			
			if (isSelected) {		
				var selectionRenderer:AbstractSelectionRenderer = dynamicObject.selectionRenderer;				
				if (selectionRenderer == null) {
					selectionRenderer = new selectionRendererClass();
					
					if (selectionRenderer is IDiagramShellAware) {
						IDiagramShellAware(selectionRenderer).diagramShell = context.diagramShell;
					}
					
					selectionRenderer.isMainSelection = isMainSelection;
					selectionRenderer.activate(model);
					
					dynamicObject.selectionRenderer = selectionRenderer;
				} else {
					if (selectionRenderer.isMainSelection != isMainSelection) {
						selectionRenderer.isMainSelection = isMainSelection;
					}
				}					
			} else {
				unassociatedModelFromSelectionRenderer(context, model, renderer);
			}
		}
		
		public function associatedModelToSelectionRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {	
			var modelExtraInfoController:DynamicModelExtraInfoController = 
				DynamicModelExtraInfoController(context.diagramShell.getControllerProvider(model).getModelExtraInfoController(model));
			
			setSelectedState(context, model, renderer, modelExtraInfoController.getDynamicObject(context, model).selected, context.diagramShell.mainSelectedItem == model);			
		}
		
		public function unassociatedModelFromSelectionRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {
			var modelExtraInfoController:DynamicModelExtraInfoController = 
				DynamicModelExtraInfoController(context.diagramShell.getControllerProvider(model).getModelExtraInfoController(model));
			
			var selectionRenderer:AbstractSelectionRenderer = modelExtraInfoController.getDynamicObject(context, model).selectionRenderer;
			
			if (selectionRenderer != null) {
				selectionRenderer.deactivate(model);				
				delete modelExtraInfoController.getDynamicObject(context, model).selectionRenderer;
			}
		}
	}
}