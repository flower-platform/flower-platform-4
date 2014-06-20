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
package org.flowerplatform.flex_client.mindmap {
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.link.LinkHandler;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.mindmap.action.EditNodeDetailsInDialogAction;
	import org.flowerplatform.flex_client.mindmap.action.EditNoteInDialogAction;
	import org.flowerplatform.flex_client.mindmap.action.RefreshAction;
	import org.flowerplatform.flex_client.mindmap.controller.MindMapNodeTypeProvider;
	import org.flowerplatform.flex_client.mindmap.controller.NodeAbsoluteLayoutRectangleController;
	import org.flowerplatform.flex_client.mindmap.controller.NodeChildrenController;
	import org.flowerplatform.flex_client.mindmap.controller.NodeController;
	import org.flowerplatform.flex_client.mindmap.controller.NodeDragController;
	import org.flowerplatform.flex_client.mindmap.controller.NodeInplaceEditorController;
	import org.flowerplatform.flex_client.mindmap.controller.NodeRendererController;
	import org.flowerplatform.flex_client.mindmap.renderer.MindMapNodeWithDetailsRenderer;
	import org.flowerplatform.flex_client.mindmap.renderer.NodeSelectionRenderer;
	import org.flowerplatform.flex_client.mindmap.ui.MindMapIconsView;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_renderer.DropDownListPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.IconsWithButtonPropertyRenderer;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.selection.BasicSelectionController;
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapRootModelChildrenController;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.NullController;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;

	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapPlugin extends AbstractFlowerFlexPlugin {
						
		protected static var INSTANCE:MindMapPlugin;
				
		public static function getInstance():MindMapPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			this.correspondingJavaPlugin = "org.flowerplatform.mindmap";

			CorePlugin.getInstance().nodeTypeProvider = new MindMapNodeTypeProvider();
			CorePlugin.getInstance().serviceLocator.addService("mindmapService");
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(MindMapRootModelWrapper.ID)			
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new MindMapRootModelChildrenController(-10))
				.addSingleController(FlexDiagramConstants.VISUAL_CHILDREN_CONTROLLER, new AbsoluteLayoutVisualChildrenController(-10))
				.addSingleController(FlexDiagramConstants.DRAG_CONTROLLER, new NullController(-10))
				.addSingleController(FlexDiagramConstants.SELECTION_CONTROLLER, new NullController(-10))
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new NullController(-10))
				.addSingleController(FlexDiagramConstants.INPLACE_EDITOR_CONTROLLER, new NullController(-10));	
						
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(FlexUtilConstants.CATEGORY_ALL)
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new NodeChildrenController())
				.addSingleController(FlexDiagramConstants.MINDMAP_MODEL_CONTROLLER, new NodeController())				
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())				
				.addSingleController(FlexDiagramConstants.ABSOLUTE_LAYOUT_RECTANGLE_CONTROLLER, new NodeAbsoluteLayoutRectangleController())
				.addSingleController(FlexDiagramConstants.DRAG_CONTROLLER, new NodeDragController())
				.addSingleController(FlexDiagramConstants.SELECTION_CONTROLLER, new BasicSelectionController(NodeSelectionRenderer))
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new NodeRendererController(MindMapNodeWithDetailsRenderer))
				.addSingleController(FlexDiagramConstants.INPLACE_EDITOR_CONTROLLER, new NodeInplaceEditorController());		
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(MindMapConstants.MINDMAP_NODE_TYPE)
				.addSingleController(MindMapConstants.NODE_SIDE_PROVIDER, new GenericValueProviderFromDescriptor(MindMapConstants.PROPERTY_FOR_SIDE_DESCRIPTOR));
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(MindMapConstants.MINDMAP_NODE_TYPE)
				.addSingleController(MindMapConstants.NODE_SIDE_PROVIDER, new GenericValueProviderFromDescriptor(MindMapConstants.PROPERTY_FOR_SIDE_DESCRIPTOR));
			
			// register PropertiesPlugin Renderer
			PropertiesPlugin.getInstance().propertyRendererClasses[MindMapConstants.MINDMAP_ICONS_WITH_BUTTON_DESCRIPTOR_TYPE] = new FactoryWithInitialization
				(IconsWithButtonPropertyRenderer, {
					clickHandler: function(itemRendererHandler:IDialogResultHandler, propertyName:String, propertyValue:Object):void {
						var dialog:MindMapIconsView = new MindMapIconsView();
						dialog.setResultHandler(itemRendererHandler);
						dialog.icons = propertyValue;
						
						FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
						.setViewContent(dialog)						
						.setWidth(300)
						.setHeight(320)
						.show();
					},
					
					getNewIconsPropertyHandler: function (dialogResult:Object):String {
						return dialogResult.icons;
					}					
					
				});
			PropertiesPlugin.getInstance().propertyRendererClasses[MindMapConstants.MINDMAP_STYLE_NAME_DESCRIPTOR_TYPE] = new FactoryWithInitialization
				(DropDownListPropertyRenderer, {	
					requestDataProviderHandler: function(node:Node, callbackFunction:Function):void {
						CorePlugin.getInstance().serviceLocator.invoke("mindmapService.getStyles", [node.nodeUri], callbackFunction);
					}
			});	
			
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(RefreshAction);
			
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(EditNodeDetailsInDialogAction);
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(EditNoteInDialogAction);
			
			CorePlugin.getInstance().linkHandlers[CoreConstants.OPEN_RESOURCES] = new LinkHandler(MindMapConstants.MINDMAP_CONTENT_TYPE);
			
			var mindMapEditorDescriptor:MindMapEditorDescriptor = new MindMapEditorDescriptor();
			CorePlugin.getInstance().contentTypeRegistry.defaultContentType = MindMapConstants.MINDMAP_CONTENT_TYPE;
			CorePlugin.getInstance().contentTypeRegistry[MindMapConstants.MINDMAP_CONTENT_TYPE] = mindMapEditorDescriptor;
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(mindMapEditorDescriptor);			
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
	}
}
