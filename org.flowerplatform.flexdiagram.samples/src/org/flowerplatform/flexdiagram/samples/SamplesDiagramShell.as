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
package org.flowerplatform.flexdiagram.samples {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_children.ParentAwareArrayListModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.IModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.LightweightModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.renderer.IRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.ISelectionController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.SequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.renderer.connection.ConnectionRenderer;
	import org.flowerplatform.flexdiagram.renderer.selection.StandardAnchorsSelectionRenderer;
	import org.flowerplatform.flexdiagram.samples.controller.BasicConnectionRendererController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelDragController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelRendererController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicModelResizeController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicSubModelChildrenProvider;
	import org.flowerplatform.flexdiagram.samples.controller.BasicSubModelInplaceEditorController;
	import org.flowerplatform.flexdiagram.samples.controller.BasicSubModelSelectionController;
	import org.flowerplatform.flexdiagram.samples.model.BasicConnection;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	import org.flowerplatform.flexdiagram.samples.model.BasicSubModel;
	import org.flowerplatform.flexdiagram.samples.renderer.SubModelIconItemRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.ISelectOrDragToCreateElementController;
	import org.flowerplatform.flexdiagram.tool.controller.SelectOrDragToCreateElementController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SamplesDiagramShell extends DiagramShell implements IControllerProvider {
		
		private var lightweightModelExtraInfoController:LightweightModelExtraInfoController;
		private var dynamicModelExtraInfoController:DynamicModelExtraInfoController;
		
		private var absoluteLayoutVisualChildrenController:AbsoluteLayoutVisualChildrenController;
		private var arrayListModelChildrenController:ParentAwareArrayListModelChildrenController;
		
		private var basicModelAbsoluteLayoutRectangleController:BasicModelAbsoluteLayoutRectangleController;
		private var basicModelRendererController:BasicModelRendererController;
		private var basicModelModelChildrenController:IModelChildrenController;
		private var sequentialLayoutVisualChildrenController:IVisualChildrenController;
		
		private var basicSubModelRendererController:ClassReferenceRendererController;
		private var basicSubModelChildrenProvider:IModelChildrenController = new BasicSubModelChildrenProvider();
		
		private var basicModelSelectionController:SelectionController;		
		private var basicSubModelSelectionController:BasicSubModelSelectionController;
		
		private var basicSubModelInplaceEditorController:BasicSubModelInplaceEditorController;
		private var basicModelResizeController:BasicModelResizeController;
		private var basicModelDragToCreateRelationController:BasicModelDragToCreateRelationController;
		private var basicModelDragController:BasicModelDragController;
		private var selectOrgDragToCreateElementgController:SelectOrDragToCreateElementController;
			
		// connections
		private var connectionModelRendererController:ClassReferenceRendererController;
		
		public function SamplesDiagramShell() {
			lightweightModelExtraInfoController = new LightweightModelExtraInfoController(this);
			dynamicModelExtraInfoController = new DynamicModelExtraInfoController(this);
			
			absoluteLayoutVisualChildrenController = new AbsoluteLayoutVisualChildrenController(this);
			arrayListModelChildrenController = new ParentAwareArrayListModelChildrenController(this, true);
			
			basicModelAbsoluteLayoutRectangleController = new BasicModelAbsoluteLayoutRectangleController(this);
			basicModelRendererController = new BasicModelRendererController(this);
			basicModelModelChildrenController = new BasicModelModelChildrenController(this);
			sequentialLayoutVisualChildrenController = new SequentialLayoutVisualChildrenController(this);
			
			basicSubModelRendererController = new ClassReferenceRendererController(this, SubModelIconItemRenderer);
			
			basicModelSelectionController = new SelectionController(this, StandardAnchorsSelectionRenderer);		
			basicSubModelSelectionController = new BasicSubModelSelectionController(this);
			
			basicSubModelInplaceEditorController = new BasicSubModelInplaceEditorController(this);
			basicModelResizeController = new BasicModelResizeController(this);
			basicModelDragToCreateRelationController = new BasicModelDragToCreateRelationController(this);
			basicModelDragController = new BasicModelDragController(this);
			selectOrgDragToCreateElementgController = new SelectOrDragToCreateElementController(this);
					
			// connections
			connectionModelRendererController = new BasicConnectionRendererController(this, ConnectionRenderer);
		}
		
		public function getAbsoluteLayoutRectangleController(model:Object):IAbsoluteLayoutRectangleController {
			if (model is BasicModel) {
				return basicModelAbsoluteLayoutRectangleController;
			}
			return null;
		}
		
		public function getModelChildrenController(model:Object):IModelChildrenController {
			if (model is ArrayList) {
				return arrayListModelChildrenController;
			} else if (model is BasicModel) {
				return basicModelModelChildrenController;
			} else if (model is BasicSubModel) {
				return basicSubModelChildrenProvider;
			}
			return null;
		}
		
		public function getModelExtraInfoController(model:Object):IModelExtraInfoController {			
			return dynamicModelExtraInfoController;
		}
		
		public function getRendererController(model:Object):IRendererController {
			if (model is BasicModel) {
				return basicModelRendererController;
			} else if (model is BasicSubModel) {
				return basicSubModelRendererController;
			} else if (model is BasicConnection) {
				return connectionModelRendererController;
			}
			return null;
		}
		
		public function getVisualChildrenController(model:Object):IVisualChildrenController {
			if (model is ArrayList) {
				return absoluteLayoutVisualChildrenController;
			} else if (model is BasicModel) {
				return sequentialLayoutVisualChildrenController;
			}
			return null;
		}
		
		public function getSelectionController(model:Object):ISelectionController {
			if (model is BasicModel) {
				return basicModelSelectionController;
			} else if (model is BasicSubModel) {
				return basicSubModelSelectionController;
			}
			return null;
		}
		
		public function getInplaceEditorController(model:Object):IInplaceEditorController {
			if (model is BasicSubModel) {
				return basicSubModelInplaceEditorController;
			}
			return null;
		}
		
		public function getResizeController(model:Object):IResizeController {
			if (model is BasicModel) {
				return basicModelResizeController;
			}
			return null;
		}
		
		public function getDragToCreateRelationController(model:Object):IDragToCreateRelationController {
			if (model is BasicModel) {
				return basicModelDragToCreateRelationController;
			}
			return null;
		}
		
		public function getDragController(model:Object):IDragController {
			if (model is BasicModel) {
				return basicModelDragController;
			}
			return null;
		}
		
		public function getSelectOrDragToCreateElementController(model:Object):ISelectOrDragToCreateElementController {
			return selectOrgDragToCreateElementgController;
		}
		
		override public function getControllerProvider(model:Object):IControllerProvider {
			return this;
		}
		
		
	}
}