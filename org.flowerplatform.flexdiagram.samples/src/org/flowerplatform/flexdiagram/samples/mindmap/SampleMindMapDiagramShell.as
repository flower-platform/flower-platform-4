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
package org.flowerplatform.flexdiagram.samples.mindmap {
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.selection.BasicSelectionController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapNodeRenderer;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapRootModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelDragController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelInplaceEditorController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapNodeRendererController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapTypeProvider;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.SampleMindMapModelSelectionRenderer;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	import org.flowerplatform.flexutil.controller.SingleValueDescriptor;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class SampleMindMapDiagramShell extends MindMapDiagramShell {
						
		public function SampleMindMapDiagramShell() {
			super();
			
			typeProvider = new SampleMindMapTypeProvider();
			registry = new TypeDescriptorRegistry();
			registry.typeProvider = typeProvider;

			registry.getOrCreateTypeDescriptor("mindmap")
				.addSingleController(FlexDiagramConstants.MINDMAP_MODEL_CONTROLLER, new SampleMindMapModelController())
				.addSingleController(FlexDiagramConstants.ABSOLUTE_LAYOUT_RECTANGLE_CONTROLLER, new MindMapAbsoluteLayoutRectangleController())
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new SampleMindMapModelChildrenController())
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())				
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new SampleMindMapNodeRendererController(new ClassFactoryWithConstructor(MindMapNodeRenderer, { featureForValuesProvider: "mindMapValuesProvider" })))
				.addSingleController(FlexDiagramConstants.SELECTION_CONTROLLER,  new BasicSelectionController(SampleMindMapModelSelectionRenderer))	
				.addSingleController(FlexDiagramConstants.INPLACE_EDITOR_CONTROLLER,  new SampleMindMapModelInplaceEditorController())	
				.addSingleController(FlexDiagramConstants.DRAG_CONTROLLER, new SampleMindMapModelDragController())
			
				.addSingleController("mindMapValuesProvider", new ValuesProvider())
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_FONT_FAMILY, new SingleValueDescriptor("fontFamily"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_FONT_SIZE, new SingleValueDescriptor("fontSize"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_FONT_BOLD, new SingleValueDescriptor("fontBold"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_FONT_ITALIC, new SingleValueDescriptor("fontItalic"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_TEXT, new SingleValueDescriptor("text"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_TEXT_COLOR, new SingleValueDescriptor("textColor"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_BACKGROUND_COLOR, new SingleValueDescriptor("backgroundColor"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_ICONS, new SingleValueDescriptor("icons"))
				.addSingleController(FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE, new SingleValueDescriptor("cloudType"))
				.addSingleController(FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_COLOR, new SingleValueDescriptor("cloudColor"))
				.addSingleController(FlexDiagramConstants.MIND_MAP_RENDERER_HAS_CHILDREN, new SingleValueDescriptor("hasChildren"))
				.addSingleController("mindMapNodeRenderer.detailsText", new SingleValueDescriptor("details"));
							
			registry.getOrCreateTypeDescriptor(MindMapRootModelWrapper.ID)
				.addSingleController(FlexDiagramConstants.MINDMAP_MODEL_CONTROLLER, new SampleMindMapModelController())			
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new MindMapRootModelChildrenController())
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())				
				.addSingleController(FlexDiagramConstants.VISUAL_CHILDREN_CONTROLLER,  new MindMapAbsoluteLayoutVisualChildrenController());			
		}
				
	}
}