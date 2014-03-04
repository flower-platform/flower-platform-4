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
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.controller.AbstractController;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class SelectionController extends AbstractController {
		
		public static const TYPE:String = "SelectionController";
		
		public function SelectionController(orderIndex:int = 0) {
			super(orderIndex);
		}
		
		public function setSelectedState(context:DiagramShellContext, model:Object, renderer:IVisualElement, isSelected:Boolean, isMainSelection:Boolean):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function associatedModelToSelectionRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function unassociatedModelFromSelectionRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {
			throw new Error("This method needs to be implemented.");
		}
		
	}	
}