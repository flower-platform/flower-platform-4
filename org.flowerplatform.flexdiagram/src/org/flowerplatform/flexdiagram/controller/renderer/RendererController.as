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
		 * renderers. For the latter case, the controller should listen the model. When its state changes and
		 * it needs a new renderer => it should inform the system (i.e. "shouldRefresh...").
		 * 
		 * <p>
		 * For AbsoluteLayout, with the same renderer type that has several states: the key should be
		 * a string composed of the elements that form the state. E.g. "MyRenderer.hasDetails.hasProperties".
		 * No listening needed by this class; however the renderer should listen the model and change the state.
		 */
		public function geUniqueKeyForRendererToRecycle(context:Object, model:Object):Object {
			throw new Error("This method needs to be implemented.");
		}
		
		public function createRenderer(context:Object, model:Object):IVisualElement {
			throw new Error("This method needs to be implemented.");
		}
		
		public function associatedModelToRenderer(context:Object, model:Object, renderer:IVisualElement):void {
		}
		
		public function unassociatedModelFromRenderer(context:Object, model:Object, renderer:IVisualElement, modelIsDisposed:Boolean):void {
		}
		
	}
}
