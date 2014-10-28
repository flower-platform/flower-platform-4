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
package org.flowerplatform.flexdiagram.samples.mindmap {
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.BasicSelectionController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.mindmap.MultiConnectorModel;
	import org.flowerplatform.flexdiagram.mindmap.MultiConnectorRenderer;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapRootModelChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MultiConnectorAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelDragController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapModelRendererController;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleMindMapTypeProvider;
	import org.flowerplatform.flexdiagram.samples.mindmap.controller.SampleSequentialLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.SampleMindMapModelSelectionRenderer;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.SampleMindMapRenderer;
	import org.flowerplatform.flexutil.properties.PropertyEntryRendererController;
	import org.flowerplatform.flexutil.samples.properties.SamplePropertyCommitController;
	import org.flowerplatform.flexdiagram.samples.renderer.SubModelIconItemRenderer;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.controller.GenericDescriptor;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	import org.flowerplatform.flexutil.properties.DelegatingPropertyCommitController;
	import org.flowerplatform.flexutil.properties.PropertiesHelper;
	import org.flowerplatform.flexutil.value_converter.CsvToListValueConverter;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Cristian Spiescu
	 */
	public class SampleMindMapDiagramShell extends MindMapDiagramShell {
						
		public function SampleMindMapDiagramShell() {
			super();
			
			typeProvider = new SampleMindMapTypeProvider();
			registry = new TypeDescriptorRegistry();
			registry.typeProvider = typeProvider;
			PropertiesHelper.registerPropertyRenderers(registry);
			
			registry.getOrCreateTypeDescriptor(FlexUtilConstants.NOTYPE_VALUE_CONVERTERS)
				.addSingleController(FlexUtilConstants.VALUE_CONVERTER_CSV_TO_LIST, new CsvToListValueConverter());

			registry.getOrCreateTypeDescriptor("mindmap")
				.addSingleController(FlexDiagramConstants.MINDMAP_MODEL_CONTROLLER, new SampleMindMapModelController())
				.addSingleController(FlexDiagramConstants.ABSOLUTE_LAYOUT_RECTANGLE_CONTROLLER, new MindMapAbsoluteLayoutRectangleController())
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new SampleMindMapModelChildrenController())
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())				
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new SampleMindMapModelRendererController(new ClassFactoryWithConstructor(SampleMindMapRenderer, { featureForValuesProvider: "mindMapValuesProvider" })))
				.addSingleController(FlexDiagramConstants.SELECTION_CONTROLLER,  new BasicSelectionController(SampleMindMapModelSelectionRenderer))	
//				.addSingleController(FlexDiagramConstants.INPLACE_EDITOR_CONTROLLER,  new SampleMindMapModelInplaceEditorController())	
				.addSingleController(FlexDiagramConstants.DRAG_CONTROLLER, new SampleMindMapModelDragController())
				
				// properties
				.addSingleController(FlexDiagramConstants.VISUAL_CHILDREN_CONTROLLER, new SampleSequentialLayoutVisualChildrenController(this))
				.addSingleController(FlexUtilConstants.FEATURE_PROPERTY_COMMIT_CONTROLLER, new SamplePropertyCommitController())
				
				.addSingleController("mindMapValuesProvider", new ValuesProvider())
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_FONT_FAMILY, new GenericDescriptor("fontFamily"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_FONT_SIZE, new GenericDescriptor("fontSize"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_FONT_BOLD, new GenericDescriptor("fontBold"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_FONT_ITALIC, new GenericDescriptor("fontItalic"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_TEXT, new GenericDescriptor("text"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_TEXT_COLOR, new GenericDescriptor("textColor"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_BACKGROUND_COLOR, new GenericDescriptor("backgroundColor"))
				.addSingleController(FlexDiagramConstants.BASE_RENDERER_ICONS, new GenericDescriptor("icons")
					.addExtraInfoProperty(FlexUtilConstants.EXTRA_INFO_VALUE_CONVERTER, FlexUtilConstants.VALUE_CONVERTER_CSV_TO_LIST)
					.addExtraInfoProperty(FlexUtilConstants.EXTRA_INFO_CSV_TO_LIST_PREFIX, "../../org.flowerplatform.flexdiagram.samples/icons/")
					.addExtraInfoProperty(FlexUtilConstants.EXTRA_INFO_CSV_TO_LIST_SUFFIX, ".png"))
				.addSingleController(FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_TYPE, new GenericDescriptor("cloudType"))
				.addSingleController(FlexDiagramConstants.MIND_MAP_RENDERER_CLOUD_COLOR, new GenericDescriptor("cloudColor"))
				.addSingleController(FlexDiagramConstants.MIND_MAP_RENDERER_HAS_CHILDREN, new GenericDescriptor("hasChildren"))
				.addSingleController("mindMapNodeRenderer.detailsText", new GenericDescriptor("details"));
			
			registry.getOrCreateTypeDescriptor("propertyEntry")
				// WARNING: because these wrappers are always recreated on ".getChildren()" => no MODEL_EXTRA_INFO_CONTROLLER should be registered for this type!
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new PropertyEntryRendererController(new DelegatingPropertyCommitController()));
							
			registry.getOrCreateTypeDescriptor(MindMapRootModelWrapper.TYPE)
				.addSingleController(FlexDiagramConstants.MINDMAP_MODEL_CONTROLLER, new SampleMindMapModelController())			
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new MindMapRootModelChildrenController())
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())				
				.addSingleController(FlexDiagramConstants.VISUAL_CHILDREN_CONTROLLER, new MindMapAbsoluteLayoutVisualChildrenController());
			
			registry.getOrCreateTypeDescriptor(MultiConnectorModel.TYPE)
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new ClassReferenceRendererController(new ClassFactoryWithConstructor(MultiConnectorRenderer), 0, true))
				.addSingleController(FlexDiagramConstants.ABSOLUTE_LAYOUT_RECTANGLE_CONTROLLER, new MultiConnectorAbsoluteLayoutRectangleController());
		}
				
	}
}
