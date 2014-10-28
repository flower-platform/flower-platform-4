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
package org.flowerplatform.flexdiagram.samples.mindmap.controller {
	import flash.events.IEventDispatcher;
	
	import mx.core.IVisualElement;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.SampleMindMapNodeRendererWithDetails;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	
	/**
	 * @author AlexandraTopoloaga
	 * @author Cristian Spiescu
	 */
	public class SampleMindMapModelRendererController extends ClassReferenceRendererController {
		
		protected var cachedContext:DiagramShellContext;
		
		public function SampleMindMapModelRendererController(rendererClassFactory:ClassFactoryWithConstructor, orderIndex:int = 0) {
			super(rendererClassFactory, orderIndex);
			removeRendererIfModelIsDisposed = true;
		}
		
		public function modelChangedHandler(event:PropertyChangeEvent):void {
			var model:Object = event.target;
			var renderer:IVisualElement = cachedContext.diagramShell.getRendererForModel(cachedContext, model);
			if (renderer == null) {
				return;
			}
			var actualRendererClass:Class = Class(Object(renderer).constructor);
			if (event != null && "details" == event.property && !(actualRendererClass == getRendererClass(cachedContext, model))) {
				// the value of this property dictates the renderer; 
				// it has been changed, and the proposed renderer class != the actual class
				cachedContext.diagramShell.shouldRefreshVisualChildren(cachedContext, cachedContext.diagramShell.rootModel);
			}
		}
		
		override public function associatedModelToRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {
			super.associatedModelToRenderer(context, model, renderer);
			cachedContext = context;
			IEventDispatcher(model).addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
		}
		
		override public function unassociatedModelFromRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement, modelIsDisposed:Boolean):void {
			IEventDispatcher(model).removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, modelChangedHandler);
			super.unassociatedModelFromRenderer(context, model, renderer, modelIsDisposed);
		}
		
		
		override public function getRendererClass(context:DiagramShellContext, model:Object):Class {
			var data:SampleMindMapModel = SampleMindMapModel(model);
			if (data.details != null && data.details != "") {
				return SampleMindMapNodeRendererWithDetails;
			}
			return super.getRendererClass(context, model);
		}
		
	}
}