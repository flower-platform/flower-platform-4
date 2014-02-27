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
		
		public function SampleMindMapModelController(diagramShell:DiagramShell) {
			super(diagramShell);
		}

		public function setParent(model:Object, value:Object):void {
			var oldParent:Object = model.parent;
			var newParent:SampleMindMapModel = SampleMindMapModel(value);
			if (!isRoot(newParent) && newParent.side != SampleMindMapModel(model).side) {
				setSide(model, newParent.side);				
			}
			SampleMindMapModel(model).parent = newParent;
			IEventDispatcher(model).dispatchEvent(PropertyChangeEvent.createUpdateEvent(model, "parent", oldParent, newParent));
			diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
		}
				
		public function getChildren(model:Object):IList {			
			return SampleMindMapModel(model).children;
		}
				
		public function getExpanded(model:Object):Boolean {
			return SampleMindMapModel(model).expanded;
		}
		
		public function setExpanded(model:Object, value:Boolean):void {
			SampleMindMapModel(model).expanded = value;
			if (value) {
				updateModelHandler(model);
			} else {
				disposeModelHandlerRecursive(model);
			}
		}
		
		public function getSide(model:Object):int {
			return SampleMindMapModel(model).side;
		}
		
		public function setSide(model:Object, value:int):void {
			SampleMindMapModel(model).side = value;
			for (var i:int = 0; i < getChildren(model).length; i++) {
				setSide(getChildren(model).getItemAt(i), model.side);
			}
		}
		
		public function isRoot(model:Object):Boolean {			
			return SampleMindMapModel(model).parent == null;
		}
		
		private function getDynamicObject(model:Object):Object {
			return DynamicModelExtraInfoController(diagramShell.getControllerProvider(model).getModelExtraInfoController(model)).getDynamicObject(model);
		}
		
		public function updateModelHandler(model:Object):void {
			MindMapDiagramShell(diagramShell).refreshRootModelChildren();
			if (diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(model) != null) {
				MindMapDiagramShell(diagramShell).refreshModelPositions(model);
			} else {
				var rootModel:Object = MindMapDiagramShell(diagramShell).getRoot();
				MindMapDiagramShell(diagramShell).refreshModelPositions(rootModel);
			}
			MindMapDiagramShell(diagramShell).shouldRefreshVisualChildren(diagramShell.rootModel);
		}
		
		public function disposeModelHandlerRecursive(model:Object, disposeModel:Boolean = false):void {
			if (SampleMindMapModel(model).hasChildren) {
				for (var i:int=0; i < SampleMindMapModel(model).children.length; i++) {
					disposeModelHandlerRecursive(SampleMindMapModel(model).children.getItemAt(i), true);
				}
			}
			if (disposeModel) {
				disposeModelHandler(model);
			}
		}
		
		private function disposeModelHandler(model:Object):void {			
			diagramShell.unassociateModelFromRenderer(model, diagramShell.getRendererForModel(model), true);
			
			MindMapDiagramShell(diagramShell).refreshRootModelChildren();
			
			if (diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(model) is SampleMindMapModel) {
				MindMapDiagramShell(diagramShell).refreshModelPositions(diagramShell.getControllerProvider(model).getModelChildrenController(model).getParent(model));
			} else {
				var rootModel:Object = diagramShell.getControllerProvider(diagramShell.rootModel).getModelChildrenController(diagramShell.rootModel).getChildren(diagramShell.rootModel).getItemAt(0);
				MindMapDiagramShell(diagramShell).refreshModelPositions(rootModel);
			}
			
			MindMapDiagramShell(diagramShell).shouldRefreshVisualChildren(diagramShell.rootModel);
		}
		
	}
}