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
	import flash.events.IEventDispatcher;
	
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.samples.renderer.BasicModelRendererWithChildren;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class BasicModelRendererController extends ClassReferenceRendererController {
		
		public function BasicModelRendererController(orderIndex:int = 0) {
			super(new ClassFactoryWithConstructor(BasicModelRendererWithChildren), orderIndex);
		}
		
		override public function associatedModelToRenderer(context:Object, model:Object, renderer:IVisualElement):void {
			IEventDispatcher(model).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
		}
		
		override public function unassociatedModelFromRenderer(context:Object, model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {
			if (isModelDisposed) {
				if (renderer != null) {
					IVisualElementContainer(renderer.parent).removeElement(renderer);
				}
				IEventDispatcher(model).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, function (event:PropertyChangeEvent):void {modelChangedHandler(event, DiagramShellContext(context));});
			} else {
				// weak referenced. In theory, this is not needed, but to be sure...
				// The only case where it would make sense: if the model children controller fails to inform us of a disposal;
				// but in this case, the stray model may be as well left on the diagram 
				IEventDispatcher(model).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, function (event:PropertyChangeEvent):void {modelChangedHandler(event, DiagramShellContext(context));}, false, 0, true);
			}
		}
		
		private function modelChangedHandler(event:PropertyChangeEvent, context:DiagramShellContext):void {
			if (event.property == "x" || event.property == "y" || event.property == "height" || event.property == "width") {
				context.diagramShell.shouldRefreshVisualChildren(context, context.diagramShell.rootModel);
			}
		}
	}
}
