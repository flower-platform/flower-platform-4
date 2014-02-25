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
package org.flowerplatform.flexdiagram.mindmap.controller {
	import flash.utils.getDefinitionByName;
	
	import mx.collections.IList;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapConnector;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapModelRendererController extends ClassReferenceRendererController {
			
		public function MindMapModelRendererController(rendererClass:Class) {
			super(rendererClass);
			removeRendererIfModelIsDisposed = true;
		}
		
		override public function associatedModelToRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement):void {			
		}
		
		override public function unassociatedModelFromRenderer(context:DiagramShellContext, model:Object, renderer:IVisualElement, isModelDisposed:Boolean):void {		
			if (isModelDisposed) {
				removeConnector(context, model);
			}
			super.unassociatedModelFromRenderer(context, model, renderer, isModelDisposed);
		}
				
		private function removeConnector(context:DiagramShellContext, model:Object):void {		
			var dynamicObject:Object = MindMapDiagramShell(context.diagramShell).getDynamicObject(model);
			var connector:MindMapConnector = dynamicObject.connector;
			if (connector != null) {
				context.diagramShell.diagramRenderer.removeElement(connector);
				delete dynamicObject.connector;
			}
		}
		
		private function addConnector(context:DiagramShellContext, model:Object):void {
			var modelParent:Object = context.diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(context, model);
			if (modelParent == null) { // root node, no connectors
				return;
			}
			var connector:MindMapConnector = new MindMapConnector().setSource(model).setTarget(modelParent);
			MindMapDiagramShell(context.diagramShell).getDynamicObject(model).connector = connector;
			context.diagramShell.diagramRenderer.addElementAt(connector, 0);			
		}
		
		public function updateConnectors(context:DiagramShellContext, model:Object):void {			
			var dynamicObject:Object = MindMapDiagramShell(context.diagramShell).getDynamicObject(model);
			if (dynamicObject.connector == null) {
				addConnector(context, model);				
			}
						
			// refresh connector to parent
			if (context.diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(context, model) != null) {	
				if (dynamicObject.connector != null) {
					dynamicObject.connector.invalidateDisplayList();
				}
			}
			// refresh connectors to children
			var children:IList = MindMapDiagramShell(context.diagramShell).getModelController(model).getChildren(context, model);
			if (children != null) {
				for (var i:int = 0; i < children.length; i++) {
					var child:Object = children.getItemAt(i);			
					var childDynamicObject:Object = MindMapDiagramShell(context.diagramShell).getDynamicObject(child);
					if (childDynamicObject.connector != null) {
						childDynamicObject.connector.invalidateDisplayList();
					}
				}
			}
		}
			
	}
}