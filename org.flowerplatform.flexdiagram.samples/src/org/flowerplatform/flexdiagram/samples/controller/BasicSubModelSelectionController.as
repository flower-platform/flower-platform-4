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
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.samples.renderer.SubModelIconItemRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class BasicSubModelSelectionController extends ControllerBase implements ISelectionController {
		
		public function BasicSubModelSelectionController(diagramShell:DiagramShell)	{
			super(diagramShell);
		}
		
		public function setSelectedState(model:Object, renderer:IVisualElement, isSelected:Boolean, isMainSelection:Boolean):void {
			var modelExtraInfoController:DynamicModelExtraInfoController = 
				DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model));
			
			if (modelExtraInfoController.getDynamicObject(model).selected != isSelected) {
				modelExtraInfoController.getDynamicObject(model).selected = isSelected;
			}
			
			if (renderer == null) {
				return;
			}
			if (isSelected) {
				SubModelIconItemRenderer(renderer).setStyle("alternatingItemColors", [isMainSelection ? "green" : "gray"]);
			} else {
				SubModelIconItemRenderer(renderer).setStyle("alternatingItemColors", []);
			}
		}
		
		public function associatedModelToSelectionRenderer(model:Object, renderer:IVisualElement):void {		
			var modelExtraInfoController:DynamicModelExtraInfoController = 
				DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model));
			
			setSelectedState(model, renderer, 
				modelExtraInfoController.getDynamicObject(model).selected, 
				diagramShell.mainSelectedItem == model);
		}
		
		public function unassociatedModelFromSelectionRenderer(model:Object, renderer:IVisualElement):void {
			
		}
	}
}