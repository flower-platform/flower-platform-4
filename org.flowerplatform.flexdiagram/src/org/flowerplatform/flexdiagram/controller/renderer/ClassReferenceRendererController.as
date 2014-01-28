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
package org.flowerplatform.flexdiagram.controller.renderer {
	import flash.events.IEventDispatcher;
	
	import mx.core.IInvalidating;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ClassReferenceRendererController extends ControllerBase implements IRendererController {
		
		public var rendererClass:Class;
		
		public var removeRendererIfModelIsDisposed:Boolean;
		
		public function ClassReferenceRendererController(diagramShell:DiagramShell, rendererClass:Class = null) {
			super(diagramShell);
			this.rendererClass = rendererClass;
		}
		
		public function geUniqueKeyForRendererToRecycle(model:Object):Object {
			return rendererClass;
		}
		
		public function createRenderer(model:Object):IVisualElement {
			return new rendererClass();
		}
		
		public function associatedModelToRenderer(model:Object, renderer:IVisualElement):void {
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		public function unassociatedModelFromRenderer(model:Object, renderer:IVisualElement, modelIsDisposed:Boolean):void {
			if (modelIsDisposed && removeRendererIfModelIsDisposed) {
				if (renderer != null) {
					IVisualElementContainer(renderer.parent).removeElement(renderer);
				}
			}
		}
		
	}
}