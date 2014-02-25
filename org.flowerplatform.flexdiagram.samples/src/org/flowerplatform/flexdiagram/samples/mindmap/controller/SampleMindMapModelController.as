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
package org.flowerplatform.flexdiagram.samples.mindmap.controller {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexdiagram.util.ParentAwareArrayList;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class SampleMindMapModelController extends ControllerBase implements IMindMapModelController {
		
		public function setParent(context:DiagramShellContext, model:Object, value:Object):void {
			var oldParent:Object = model.parent;
			var newParent:SampleMindMapModel = SampleMindMapModel(value);
			if (!isRoot(context, newParent) && newParent.side != SampleMindMapModel(model).side) {
				setSide(context, model, newParent.side);				
			}
			SampleMindMapModel(model).parent = newParent;
			IEventDispatcher(model).dispatchEvent(PropertyChangeEvent.createUpdateEvent(model, "parent", oldParent, newParent));
			context.diagramShell.shouldRefreshVisualChildren(context.diagramShell.rootModel);
		}
				
		public function getChildren(context:DiagramShellContext, model:Object):IList {			
			return SampleMindMapModel(model).children;
		}
				
		public function getExpanded(context:DiagramShellContext, model:Object):Boolean {
			return SampleMindMapModel(model).expanded;
		}
		
		public function setExpanded(context:DiagramShellContext, model:Object, value:Boolean):void {
			SampleMindMapModel(model).expanded = value;
			if (value) {
				updateModelHandler(context, model);
			} else {
				disposeModelHandlerRecursive(context, model);
			}
		}
		
		public function getSide(context:DiagramShellContext, model:Object):int {
			return SampleMindMapModel(model).side;
		}
		
		public function setSide(context:DiagramShellContext, model:Object, value:int):void {
			SampleMindMapModel(model).side = value;
			for (var i:int = 0; i < getChildren(context, model).length; i++) {
				setSide(context, getChildren(context, model).getItemAt(i), model.side);
			}
		}
		
		public function isRoot(context:DiagramShellContext, model:Object):Boolean {			
			return SampleMindMapModel(model).parent == null;
		}
		
		private function getDynamicObject(context:DiagramShellContext, model:Object):Object {
			return DynamicModelExtraInfoController(context.diagramShell.getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(context, model);
		}
		
		public function updateModelHandler(context:DiagramShellContext, model:Object):void {
			MindMapDiagramShell(context.diagramShell).refreshRootModelChildren();
			if (context.diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(context, model) != null) {
				MindMapDiagramShell(context.diagramShell).refreshModelPositions(model);
			} else {
				var rootModel:Object = MindMapDiagramShell(context.diagramShell).getRoot();
				MindMapDiagramShell(context.diagramShell).refreshModelPositions(rootModel);
			}
			MindMapDiagramShell(context.diagramShell).shouldRefreshVisualChildren(context.diagramShell.rootModel);
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
			context.diagramShell.unassociateModelFromRenderer(model, context.diagramShell.getRendererForModel(model), true);
			
			MindMapDiagramShell(context.diagramShell).refreshRootModelChildren();
			
			if (context.diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(context, model) is SampleMindMapModel) {
				MindMapDiagramShell(context.diagramShell).refreshModelPositions(context.diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(context, model));
			} else {
				var rootModel:Object = context.diagramShell.getControllerProvider(context.diagramShell.rootModel).getModelChildrenController(context.diagramShell.rootModel).getChildren(context, context.diagramShell.rootModel).getItemAt(0);
				MindMapDiagramShell(context.diagramShell).refreshModelPositions(rootModel);
			}
			
			MindMapDiagramShell(context.diagramShell).shouldRefreshVisualChildren(context.diagramShell.rootModel);
		}
		
	}
}