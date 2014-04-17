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
package org.flowerplatform.flexdiagram.controller.selection {
	
	import flash.geom.Rectangle;
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.renderer.selection.AbstractSelectionRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class BasicSelectionController extends SelectionController {
		
		public var selectionRendererClass:Class;
		
		public function BasicSelectionController(selectionRendererClass:Class = null, orderIndex:int = 0) {		
			super(orderIndex);
			this.selectionRendererClass = selectionRendererClass;
		}
		
		override public function setSelectedState(context:DiagramShellContext, model:Object, renderer:IVisualElement, isSelected:Boolean, isMainSelection:Boolean):void {			
			var dynamicObject:Object = context.diagramShell.getDynamicObject(context, model);
			
			if (dynamicObject.selected != isSelected) {
				dynamicObject.selected = isSelected;
			}
			
			if (renderer == null) {
				if (isSelected) {
					// this will trigger renderer to be displayed in diagram
					context.diagramShell.makeModelRendererVisible(model, context);
				}
				return;
			}
			
			// add/delete selectionRenderer
			
			if (isSelected) {		
				var selectionRenderer:AbstractSelectionRenderer = dynamicObject.selectionRenderer;				
				if (selectionRenderer == null) {
					selectionRenderer = new selectionRendererClass();
										
					selectionRenderer.isMainSelection = isMainSelection;
					selectionRenderer.activate(context, model);
					
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
		
		override public function associatedModelToSelectionRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {	
			var dynamicObject:Object = context.diagramShell.getDynamicObject(context, model);			
			setSelectedState(context, model, renderer, dynamicObject.selected, context.diagramShell.mainSelectedItem == model);			
		}
		
		override public function unassociatedModelFromSelectionRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {
			var dynamicObject:Object = context.diagramShell.getDynamicObject(context, model);			
			
			if (dynamicObject.selectionRenderer != null) {
				dynamicObject.selectionRenderer.deactivate(context, model);				
				delete dynamicObject.selectionRenderer;
			}
		}
	}
}