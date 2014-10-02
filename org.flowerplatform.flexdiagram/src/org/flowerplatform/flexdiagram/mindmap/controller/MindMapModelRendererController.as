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
package org.flowerplatform.flexdiagram.mindmap.controller {
	import mx.collections.IList;
	import mx.core.IVisualElement;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.mindmap.GenericMindMapConnector;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapModelRendererController extends ClassReferenceRendererController {
		
		public var mindMapConnectorClass:Class;
		
		public function MindMapModelRendererController(rendererClassFactory:ClassFactoryWithConstructor, mindMapConnectorClass:Class, orderIndex:int = 0) {
			super(rendererClassFactory, orderIndex);
			removeRendererIfModelIsDisposed = true;
			this.mindMapConnectorClass = mindMapConnectorClass;
		}
		
		public function rendererModelChangedHandler(context:DiagramShellContext, renderer:IVisualElement, model:Object, event:PropertyChangeEvent):void {
		}
		
		override public function associatedModelToRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {			
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Cristian Spiescu
		 */
		override public function unassociatedModelFromRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {		
			if (isModelDisposed) {
				removeConnector(context, model);
			} else {
				// when a renderer is recycled, it's OK to leave the connector; but if this happens
				// right after an expand, it's possible that the model has a new position, outside the screen.
				// then it is recycled (i.e. here). We need to tell the connector to redraw, because it's highly
				// probable that it was drawn using the previous position of the model
				var dynamicObject:Object = context.diagramShell.getDynamicObject(context, model);
				if (dynamicObject.connector != null) {
					GenericMindMapConnector(dynamicObject.connector).invalidateDisplayList();
				}
			}
			super.unassociatedModelFromRenderer(context, model, renderer, isModelDisposed);
		}
				
		private function removeConnector(context:DiagramShellContext, model:Object):void {		
			var dynamicObject:Object = context.diagramShell.getDynamicObject(context, model);
			var connector:GenericMindMapConnector = dynamicObject.connector;
			if (connector != null) {
				context.diagramShell.diagramRenderer.removeElement(connector);
				delete dynamicObject.connector;
			}
		}
		
		/**
		 * @author Sebastian Solomon
		 */
		protected function createMindMapConnector():GenericMindMapConnector {
			return new mindMapConnectorClass();
		}
		
		private function addConnector(context:DiagramShellContext, model:Object):void {
			var modelParent:Object = ControllerUtils.getModelChildrenController(context, model).getParent(context, model);
			if (modelParent == null) { // root node, no connectors
				return;
			}
			var connector:GenericMindMapConnector = createMindMapConnector().setSource(model).setTarget(modelParent).setContext(context);
			context.diagramShell.getDynamicObject(context, model).connector = connector;
			context.diagramShell.diagramRenderer.addElementAt(connector, 0);		
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Cristian Spiescu
		 */
		public function updateConnectors(context:DiagramShellContext, model:Object):void {			
//			var dynamicObject:Object = context.diagramShell.getDynamicObject(context, model);
//			if (dynamicObject.connector == null) {
//				addConnector(context, model);				
//			}
//						
//			// refresh connector to parent
//			if (dynamicObject.connector != null) {
//				dynamicObject.connector.invalidateDisplayList();
//			}
//
//			// refresh connectors to children
//			var children:IList = MindMapDiagramShell(context.diagramShell).getModelController(context, model).getChildren(context, model);
//			if (children != null) {
//				for (var i:int = 0; i < children.length; i++) {
//					var child:Object = children.getItemAt(i);			
//					var childDynamicObject:Object = MindMapDiagramShell(context.diagramShell).getDynamicObject(context, child);
//					if (childDynamicObject.connector != null) {
//						childDynamicObject.connector.invalidateDisplayList();
//					}
//				}
//			}
		}
			
	}
}
