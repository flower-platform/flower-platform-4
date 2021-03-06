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
package org.flowerplatform.flexdiagram.samples.controller {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.samples.renderer.SubModelIconItemRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class BasicSubModelSelectionController extends SelectionController {
				
		override public function setSelectedState(context:DiagramShellContext, model:Object, renderer:IVisualElement, isSelected:Boolean, isMainSelection:Boolean):void {
			var modelExtraInfoController:DynamicModelExtraInfoController = 
				DynamicModelExtraInfoController(ControllerUtils.getModelExtraInfoController(context, model));
			
			if (modelExtraInfoController.getDynamicObject(context, model).selected != isSelected) {
				modelExtraInfoController.getDynamicObject(context, model).selected = isSelected;
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
		
		override public function associatedModelToSelectionRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {		
			var modelExtraInfoController:DynamicModelExtraInfoController = 
				DynamicModelExtraInfoController(ControllerUtils.getModelExtraInfoController(context, model));
			
			setSelectedState(context, model, renderer, 
				modelExtraInfoController.getDynamicObject(context, model).selected, 
				context.diagramShell.mainSelectedItem == model);
		}
		
		override public function unassociatedModelFromSelectionRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {
			
		}
	}
}