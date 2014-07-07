/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ClassReferenceRendererController extends RendererController {
		
		public var rendererClass:Class;
		
		public var removeRendererIfModelIsDisposed:Boolean;
		
		public function ClassReferenceRendererController(rendererClass:Class = null, orderIndex:int = 0) {	
			super(orderIndex);
			this.rendererClass = rendererClass;
		}
		
		override public function geUniqueKeyForRendererToRecycle(context:DiagramShellContext, model:Object):Object {
			return rendererClass;
		}
		
		override public function createRenderer(context:DiagramShellContext, model:Object):IVisualElement {
			return new rendererClass();
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