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
package org.flowerplatform.flexdiagram.samples {
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.controller.model_children.ParentAwareArrayListModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.BasicSelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
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
	import org.flowerplatform.flexdiagram.samples.controller.BasicTypeProvider;
	import org.flowerplatform.flexdiagram.samples.renderer.SubModelIconItemRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.SelectOrDragToCreateElementController;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SamplesDiagramShell extends DiagramShell {
		
		public function SamplesDiagramShell() {
			super();
			
			typeProvider = new BasicTypeProvider();
			registry = new TypeDescriptorRegistry();
			registry.typeProvider = typeProvider;

			registry.getOrCreateTypeDescriptor("basicModel")
				.addSingleController(FlexDiagramConstants.ABSOLUTE_LAYOUT_RECTANGLE_CONTROLLER, new BasicModelAbsoluteLayoutRectangleController())
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new BasicModelModelChildrenController())
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())
				.addSingleController(FlexDiagramConstants.VISUAL_CHILDREN_CONTROLLER, new SequentialLayoutVisualChildrenController())
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new BasicModelRendererController())
				.addSingleController(FlexDiagramConstants.SELECTION_CONTROLLER, new BasicSelectionController(StandardAnchorsSelectionRenderer))
				.addSingleController(FlexDiagramConstants.RESIZE_CONTROLLER, new BasicModelResizeController())
				.addSingleController(FlexDiagramConstants.DRAG_TO_CREATE_RELATION_CONTROLLER, new BasicModelDragToCreateRelationController())
				.addSingleController(FlexDiagramConstants.DRAG_CONTROLLER, new BasicModelDragController())
				.addSingleController(FlexDiagramConstants.SELECT_OR_DRAG_TO_CREATE_ELEMENT_CONTROLLER, new SelectOrDragToCreateElementController());
			
			registry.getOrCreateTypeDescriptor("basicSubModel")
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new BasicSubModelChildrenProvider())
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())				
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new ClassReferenceRendererController(new ClassFactoryWithConstructor(SubModelIconItemRenderer)))
				.addSingleController(FlexDiagramConstants.SELECTION_CONTROLLER, new BasicSubModelSelectionController())
				.addSingleController(FlexDiagramConstants.INPLACE_EDITOR_CONTROLLER, new BasicSubModelInplaceEditorController())				
				.addSingleController(FlexDiagramConstants.SELECT_OR_DRAG_TO_CREATE_ELEMENT_CONTROLLER, new SelectOrDragToCreateElementController());
			
			registry.getOrCreateTypeDescriptor("basicConnection")				
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())				
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new BasicConnectionRendererController(new ClassFactoryWithConstructor(ConnectionRenderer)))					
				.addSingleController(FlexDiagramConstants.SELECT_OR_DRAG_TO_CREATE_ELEMENT_CONTROLLER, new SelectOrDragToCreateElementController());
			
			registry.getOrCreateTypeDescriptor("diagram")				
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new ParentAwareArrayListModelChildrenController(true))	
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())	
				.addSingleController(FlexDiagramConstants.VISUAL_CHILDREN_CONTROLLER, new AbsoluteLayoutVisualChildrenController())								
				.addSingleController(FlexDiagramConstants.SELECT_OR_DRAG_TO_CREATE_ELEMENT_CONTROLLER, new SelectOrDragToCreateElementController());
		}
	
	}
}