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
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ClassReferenceRendererController extends RendererController {
		
		private var _rendererClass:Class;
		
		public var removeRendererIfModelIsDisposed:Boolean;
		
		public function ClassReferenceRendererController(rendererClass:Class = null, orderIndex:int = 0) {	
			super(orderIndex);
			setRendererClass(rendererClass);
		}
		
		public function getRendererClass(context:DiagramShellContext, model:Object):Class {
			return _rendererClass;
		}

		public function setRendererClass(value:Class):void {
			_rendererClass = value;
		}

		override public function geUniqueKeyForRendererToRecycle(context:DiagramShellContext, model:Object):Object {
			return getRendererClass(context, model);
		}
		
		override public function createRenderer(context:DiagramShellContext, model:Object):IVisualElement {
			return new (getRendererClass(context, model))();
		}
		
		override public function associatedModelToRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		override public function unassociatedModelFromRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement, modelIsDisposed:Boolean):void {
			if (modelIsDisposed && removeRendererIfModelIsDisposed) {
				if (renderer != null) {
					IVisualElementContainer(renderer.parent).removeElement(renderer);
				}
			}
		}
		
	}
}