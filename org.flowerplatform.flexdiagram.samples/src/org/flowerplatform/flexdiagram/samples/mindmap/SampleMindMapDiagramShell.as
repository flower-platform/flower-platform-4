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
package org.flowerplatform.flexdiagram.samples.mindmap {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.flexdiagram.controller.AbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.model_children.ModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.ModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.RendererController;
	import org.flowerplatform.flexdiagram.controller.selection.BasicSelectionController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.VisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapRootModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelDragController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelInplaceEditorController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapTypeProvider;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.SampleMindMapModelRenderer;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.SampleMindMapModelSelectionRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.DragToCreateRelationController;
	import org.flowerplatform.flexdiagram.tool.controller.InplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.ResizeController;
	import org.flowerplatform.flexdiagram.tool.controller.SelectOrDragToCreateElementController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.DragController;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class SampleMindMapDiagramShell extends MindMapDiagramShell {
						
		public function SampleMindMapDiagramShell() {
			super();
			
			addTypeProvider(new SampleMindMapTypeProvider());
			
			registry = new TypeDescriptorRegistry();
			registry.getOrCreateTypeDescriptor("mindmap")
				.addSingleController(MindMapModelController.TYPE, new SampleMindMapModelController())
				.addSingleController(AbsoluteLayoutRectangleController.TYPE, new MindMapAbsoluteLayoutRectangleController())
				.addSingleController(ModelChildrenController.TYPE, new SampleMindMapModelChildrenController())
				.addSingleController(ModelExtraInfoController.TYPE, new DynamicModelExtraInfoController())				
				.addSingleController(RendererController.TYPE, new MindMapModelRendererController(SampleMindMapModelRenderer))
				.addSingleController(SelectionController.TYPE,  new BasicSelectionController(SampleMindMapModelSelectionRenderer))	
				.addSingleController(InplaceEditorController.TYPE,  new SampleMindMapModelInplaceEditorController())	
				.addSingleController(DragController.TYPE, new SampleMindMapModelDragController());
			
			registry.getOrCreateTypeDescriptor("diagram")
				.addSingleController(MindMapModelController.TYPE, new SampleMindMapModelController())			
				.addSingleController(ModelChildrenController.TYPE, new MindMapRootModelChildrenController())
				.addSingleController(ModelExtraInfoController.TYPE, new DynamicModelExtraInfoController())				
				.addSingleController(VisualChildrenController.TYPE,  new AbsoluteLayoutVisualChildrenController());			
		}
				
	}
}