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
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.AbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.model_children.ModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_children.ParentAwareArrayListModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.ModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.renderer.RendererController;
	import org.flowerplatform.flexdiagram.controller.selection.BasicSelectionController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.SequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.VisualChildrenController;
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
	import org.flowerplatform.flexdiagram.tool.controller.DragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.InplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.ResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.SelectOrDragToCreateElementController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.DragController;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SamplesDiagramShell extends DiagramShell {
		
		public function SamplesDiagramShell() {
			super();
			
			typeProvider = new BasicTypeProvider();
			
			registry = new TypeDescriptorRegistry();
			registry.getOrCreateTypeDescriptor("basicModel")
				.addSingleController(AbsoluteLayoutRectangleController.TYPE, new BasicModelAbsoluteLayoutRectangleController())
				.addSingleController(ModelChildrenController.TYPE, new BasicModelModelChildrenController())
				.addSingleController(ModelExtraInfoController.TYPE, new DynamicModelExtraInfoController())
				.addSingleController(VisualChildrenController.TYPE, new SequentialLayoutVisualChildrenController())
				.addSingleController(RendererController.TYPE, new BasicModelRendererController())
				.addSingleController(SelectionController.TYPE, new BasicSelectionController(StandardAnchorsSelectionRenderer))
				.addSingleController(ResizeController.TYPE, new BasicModelResizeController())
				.addSingleController(DragToCreateRelationController.TYPE, new BasicModelDragToCreateRelationController())
				.addSingleController(DragController.TYPE, new BasicModelDragController())
				.addSingleController(SelectOrDragToCreateElementController.TYPE, new SelectOrDragToCreateElementController());
			
			registry.getOrCreateTypeDescriptor("basicSubModel")
				.addSingleController(ModelChildrenController.TYPE, new BasicSubModelChildrenProvider())
				.addSingleController(ModelExtraInfoController.TYPE, new DynamicModelExtraInfoController())				
				.addSingleController(RendererController.TYPE, new ClassReferenceRendererController(SubModelIconItemRenderer))
				.addSingleController(SelectionController.TYPE, new BasicSubModelSelectionController())
				.addSingleController(InplaceEditorController.TYPE, new BasicSubModelInplaceEditorController())				
				.addSingleController(SelectOrDragToCreateElementController.TYPE, new SelectOrDragToCreateElementController());
			
			registry.getOrCreateTypeDescriptor("basicConnection")				
				.addSingleController(ModelExtraInfoController.TYPE, new DynamicModelExtraInfoController())				
				.addSingleController(RendererController.TYPE, new BasicConnectionRendererController(ConnectionRenderer))					
				.addSingleController(SelectOrDragToCreateElementController.TYPE, new SelectOrDragToCreateElementController());
			
			registry.getOrCreateTypeDescriptor("diagram")				
				.addSingleController(ModelChildrenController.TYPE, new ParentAwareArrayListModelChildrenController(true))	
				.addSingleController(ModelExtraInfoController.TYPE, new DynamicModelExtraInfoController())	
				.addSingleController(VisualChildrenController.TYPE, new AbsoluteLayoutVisualChildrenController())								
				.addSingleController(SelectOrDragToCreateElementController.TYPE, new SelectOrDragToCreateElementController());
		}
	
	}
}