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
package org.flowerplatform.flexdiagram.controller.renderer {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.controller.AbstractController;
	
	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class RendererController extends AbstractController {
		
		public function RendererController(orderIndex:int = 0) {
			super(orderIndex);
		}
		
		/**
		 * This usually returns the Class of the renderer. MUST return the Class of the renderer as key:
		 * for Sequential Layout mechanism, or for Absolute Layout, when a model can have multiple types of
		 * renderers.
		 */
		public function geUniqueKeyForRendererToRecycle(context:DiagramShellContext, model:Object):Object {
			throw new Error("This method needs to be implemented.");
		}
		
		public function createRenderer(context:DiagramShellContext, model:Object):IVisualElement {
			throw new Error("This method needs to be implemented.");
		}
		
		public function associatedModelToRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {
			throw new Error("This method needs to be implemented.");
		}
		
		public function unassociatedModelFromRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement, modelIsDisposed:Boolean):void {
			throw new Error("This method needs to be implemented.");
		}
		
	}
}
