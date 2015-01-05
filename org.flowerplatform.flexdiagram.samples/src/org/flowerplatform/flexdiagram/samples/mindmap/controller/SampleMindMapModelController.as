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
	
	import mx.collections.IList;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class SampleMindMapModelController extends MindMapModelController {
		
		public function setParent(context:DiagramShellContext, model:Object, value:Object):void {
			var oldParent:Object = model.parent;
			var newParent:SampleMindMapModel = SampleMindMapModel(value);
			if (!isRoot(context, newParent) && newParent.side != SampleMindMapModel(model).side) {
				setSide(context, model, newParent.side);				
			}
			SampleMindMapModel(model).parent = newParent;
			IEventDispatcher(model).dispatchEvent(PropertyChangeEvent.createUpdateEvent(model, "parent", oldParent, newParent));
			context.diagramShell.shouldRefreshVisualChildren(context, context.diagramShell.rootModel);
		}
				
		override public function getChildren(context:DiagramShellContext, model:Object):IList {			
			return SampleMindMapModel(model).children;
		}
				
		override public function getExpanded(context:DiagramShellContext, model:Object):Boolean {
			return SampleMindMapModel(model).expanded;
		}
		
		override public function setExpanded(context:DiagramShellContext, model:Object, value:Boolean):void {
			SampleMindMapModel(model).expanded = value;
			MindMapDiagramShell(context.diagramShell).refreshRootModelChildren(context);
			if (value) {
				updateModelHandler(context, model);
			} else {
				disposeModelHandlerRecursive(context, model);
			}
		}
		
		override public function getSide(context:DiagramShellContext, model:Object):int {
			return SampleMindMapModel(model).side;
		}
		
		override public function setSide(context:DiagramShellContext, model:Object, value:int):void {
			SampleMindMapModel(model).side = value;
			for (var i:int = 0; i < getChildren(context, model).length; i++) {
				setSide(context, getChildren(context, model).getItemAt(i), model.side);
			}
		}
		
		override public function isRoot(context:DiagramShellContext, model:Object):Boolean {			
			return SampleMindMapModel(model).parent == null;
		}
				
		public function updateModelHandler(context:DiagramShellContext, model:Object):void {	
			MindMapDiagramShell(context.diagramShell).shouldRefreshModelPositions(context, context.diagramShell.rootModel);
			MindMapDiagramShell(context.diagramShell).shouldRefreshVisualChildren(context, context.diagramShell.rootModel);
		}
		
		public function disposeModelHandlerRecursive(context:DiagramShellContext, model:Object, disposeModel:Boolean = false):void {
			if (SampleMindMapModel(model).hasChildren) {
				for (var i:int=0; i < SampleMindMapModel(model).children.length; i++) {
					disposeModelHandlerRecursive(context, SampleMindMapModel(model).children.getItemAt(i), true);
				}
			}
			if (disposeModel) {
				disposeModelHandler(context, model);
			}
		}
		
		private function disposeModelHandler(context:DiagramShellContext, model:Object):void {			
			context.diagramShell.unassociateModelFromRenderer(context, model, context.diagramShell.getRendererForModel(context, model), true);
			
			MindMapDiagramShell(context.diagramShell).shouldRefreshModelPositions(context, context.diagramShell.rootModel);
			MindMapDiagramShell(context.diagramShell).shouldRefreshVisualChildren(context, context.diagramShell.rootModel);
		}
		
	}
}
