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
package org.flowerplatform.flexdiagram.mindmap {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.tool.DragTool;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class MindMapDragTool extends DragTool {
		
		public function MindMapDragTool(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		override protected function getAcceptedDraggableModelsFromSelection(selection:IList):IList {
			var models:IList = super.getAcceptedDraggableModelsFromSelection(selection);
			var acceptedDraggableModels:IList = new ArrayList();
			for (var i:int = 0; i < models.length; i++) {
				var model:Object = models.getItemAt(i);
				
				if (!isParentInAcceptedDraggableModelsList(model, acceptedDraggableModels)) {					
					for (var j:int = acceptedDraggableModels.length - 1; j >= 0 ; j--) {
						var acceptedModel:Object = acceptedDraggableModels.getItemAt(j);
						if (isParentOfAcceptedDraggableModel(model, acceptedModel)) {
							acceptedDraggableModels.removeItemAt(j);							
						}
					}
					acceptedDraggableModels.addItem(model);					
				}
			}
			return acceptedDraggableModels;
		}
		
		private function isParentInAcceptedDraggableModelsList(model:Object, acceptedDraggableModels:IList):Boolean {			
			var parent:Object = diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(model);
			if (parent == null) {
				return false;
			}
			if (acceptedDraggableModels.getItemIndex(parent) != -1) {
				return true;
			} 
			return isParentInAcceptedDraggableModelsList(parent, acceptedDraggableModels);			
		}
				
		private function isParentOfAcceptedDraggableModel(model:Object, acceptedDraggableModel:Object):Boolean {			
			var parent:Object = diagramShell.getControllerProvider(acceptedDraggableModel).
				getModelChildrenController(acceptedDraggableModel).getParent(acceptedDraggableModel);
			if (parent == null) {
				return false;
			}
			if (parent == model) {
				return true;
			} 
			return isParentOfAcceptedDraggableModel(model, parent);		
		}
		
	}
}