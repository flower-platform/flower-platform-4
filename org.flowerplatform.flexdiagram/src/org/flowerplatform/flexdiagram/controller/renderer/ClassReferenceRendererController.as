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
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ClassReferenceRendererController extends RendererController {
		
		public var rendererClassFactory:ClassFactoryWithConstructor;
		
		public var removeRendererIfModelIsDisposed:Boolean;
		
		public function ClassReferenceRendererController(rendererClassFactory:ClassFactoryWithConstructor = null, orderIndex:int = 0, removeRendererIfModelIsDisposed:Boolean = false) {	
			super(orderIndex);
			this.rendererClassFactory = rendererClassFactory;
			this.removeRendererIfModelIsDisposed = removeRendererIfModelIsDisposed;
		}
		
		public function getRendererClass(context:Object, model:Object):Class {
			return rendererClassFactory.generator;
		}

		override public function geUniqueKeyForRendererToRecycle(context:Object, model:Object):Object {
			return getRendererClass(context, model);
		}
		
		override public function createRenderer(context:Object, model:Object):IVisualElement {
			var rendererClass:Class = getRendererClass(context, model);
			if (rendererClass != rendererClassFactory.generator) {
				// i.e. a different class (than the class from the factory) has been returned; use it to instantiate
				return new rendererClass();
			} else {
				return rendererClassFactory.newInstance();
			}
		}
		
		override public function associatedModelToRenderer(context:Object, model:Object, renderer:IVisualElement):void {
		}
		
		/**
		 * @author Mariana Gheorghe
		 */
		override public function unassociatedModelFromRenderer(context:Object, model:Object, renderer:IVisualElement, modelIsDisposed:Boolean):void {
			if (modelIsDisposed && removeRendererIfModelIsDisposed) {
				if (renderer != null) {
					IVisualElementContainer(renderer.parent).removeElement(renderer);
				}
			}
		}
		
	}
}
