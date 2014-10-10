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
package org.flowerplatform.flex_client.mindmap {
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.ActionDescriptor;
	import org.flowerplatform.flex_client.core.editor.action.RemoveNodeAction;
	import org.flowerplatform.flex_client.core.editor.action.RenameAction;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.link.LinkHandler;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.mindmap.action.EditNodeDetailsInDialogAction;
	import org.flowerplatform.flex_client.mindmap.action.EditNoteInDialogAction;
	import org.flowerplatform.flex_client.mindmap.action.NodeDownAction;
	import org.flowerplatform.flex_client.mindmap.action.NodeLeftAction;
	import org.flowerplatform.flex_client.mindmap.action.NodePageDownAction;
	import org.flowerplatform.flex_client.mindmap.action.NodePageUpAction;
	import org.flowerplatform.flex_client.mindmap.action.NodeRightAction;
	import org.flowerplatform.flex_client.mindmap.action.NodeUpAction;
	import org.flowerplatform.flex_client.mindmap.action.RefreshAction;
	import org.flowerplatform.flex_client.mindmap.controller.NodeChildrenController;
	import org.flowerplatform.flex_client.mindmap.controller.NodeController;
	import org.flowerplatform.flex_client.mindmap.controller.NodeDragController;
	import org.flowerplatform.flex_client.mindmap.controller.NodeInplaceEditorController;
	import org.flowerplatform.flex_client.mindmap.controller.NodeRendererController;
	import org.flowerplatform.flex_client.mindmap.renderer.NodeMindMapRenderer;
	import org.flowerplatform.flex_client.mindmap.renderer.NodeSelectionRenderer;
	import org.flowerplatform.flex_client.mindmap.ui.MindMapIconsView;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_renderer.DropDownListPropertyRenderer;
	import org.flowerplatform.flex_client.properties.property_renderer.IconsWithButtonPropertyRenderer;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.controller.selection.BasicSelectionController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.mindmap.MultiConnectorModel;
	import org.flowerplatform.flexdiagram.mindmap.MultiConnectorRenderer;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapRootModelChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MultiConnectorAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
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

			CorePlugin.getInstance().serviceLocator.addService("mindMapService");
			CorePlugin.getInstance().serviceLocator.addService("freeplaneService");
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(MindMapRootModelWrapper.TYPE)			
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new MindMapRootModelChildrenController(-10))
				.addSingleController(FlexDiagramConstants.VISUAL_CHILDREN_CONTROLLER, new MindMapAbsoluteLayoutVisualChildrenController(-10))
				.addSingleController(FlexDiagramConstants.DRAG_CONTROLLER, new NullController(-10))
				.addSingleController(FlexDiagramConstants.SELECTION_CONTROLLER, new NullController(-10))
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new NullController(-10))
				.addSingleController(FlexDiagramConstants.INPLACE_EDITOR_CONTROLLER, new NullController(-10));	
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(MultiConnectorModel.TYPE)
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())
				.addSingleController(FlexDiagramConstants.SELECTION_CONTROLLER, new NullController())
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new ClassReferenceRendererController(new ClassFactoryWithConstructor(MultiConnectorRenderer), 0, true))
				.addSingleController(FlexDiagramConstants.ABSOLUTE_LAYOUT_RECTANGLE_CONTROLLER, new MultiConnectorAbsoluteLayoutRectangleController(-100)); // we want it before the one registered for ALL
						
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(FlexUtilConstants.CATEGORY_ALL)
				.addSingleController(CoreConstants.MIND_MAP_FEATURE_FOR_VALUES_PROVIDER, new NodeValuesProvider(CoreConstants.MIND_MAP_VALUES_PROVIDER_FEATURE_PREFIX))
				.addSingleController(FlexDiagramConstants.MODEL_CHILDREN_CONTROLLER, new NodeChildrenController())
				.addSingleController(FlexDiagramConstants.MINDMAP_MODEL_CONTROLLER, new NodeController())				
				.addSingleController(FlexDiagramConstants.MODEL_EXTRA_INFO_CONTROLLER, new DynamicModelExtraInfoController())				
				.addSingleController(FlexDiagramConstants.ABSOLUTE_LAYOUT_RECTANGLE_CONTROLLER, new MindMapAbsoluteLayoutRectangleController())
				.addSingleController(FlexDiagramConstants.DRAG_CONTROLLER, new NodeDragController())
				.addSingleController(FlexDiagramConstants.SELECTION_CONTROLLER, new BasicSelectionController(NodeSelectionRenderer))
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, new NodeRendererController(new ClassFactoryWithConstructor(NodeMindMapRenderer), 0, true))
				.addSingleController(FlexDiagramConstants.INPLACE_EDITOR_CONTROLLER, new NodeInplaceEditorController());		
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(MindMapConstants.MINDMAP_NODE_TYPE)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(EditNodeDetailsInDialogAction.ID))
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(EditNoteInDialogAction.ID))
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(RenameAction.ID))
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(RemoveNodeAction.ID));	

			// register PropertiesPlugin Renderer
			PropertiesPlugin.getInstance().propertyDescriptorTypeToPropertyRendererFactory[MindMapConstants.MINDMAP_ICONS_WITH_BUTTON_DESCRIPTOR_TYPE] = new ClassFactoryWithConstructor
				(IconsWithButtonPropertyRenderer, {
					clickHandler: function(itemRendererHandler:IDialogResultHandler, propertyName:String, propertyValue:Object,selection:Node):void {
						var dialog:MindMapIconsView = new MindMapIconsView();
						dialog.showOnlyRepositoryCustomIcons = false;
						dialog.setResultHandler(itemRendererHandler);
						dialog.icons = propertyValue;
						dialog.repoPath = CorePlugin.getInstance().getRepository(selection.nodeUri);
						
						FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
						.setViewContent(dialog)						
						.setWidth(370)
						.setHeight(340)
						.show();
					},
					
					getNewIconsPropertyHandler: function (dialogResult:Object):String {
						return dialogResult.icons;
					}					
					
				});
			PropertiesPlugin.getInstance().propertyDescriptorTypeToPropertyRendererFactory[MindMapConstants.MINDMAP_STYLE_NAME_DESCRIPTOR_TYPE] = new ClassFactoryWithConstructor
				(DropDownListPropertyRenderer, {	
					requestDataProviderHandler: function(node:Node, callbackFunction:Function):void {
						CorePlugin.getInstance().serviceLocator.invoke("freeplaneService.getStyles", [node.nodeUri], callbackFunction);
					}
			});				

			FlexUtilGlobals.getInstance().registerAction(RefreshAction);
			FlexUtilGlobals.getInstance().registerAction(EditNodeDetailsInDialogAction);
			FlexUtilGlobals.getInstance().registerAction(EditNoteInDialogAction);
			FlexUtilGlobals.getInstance().registerAction(NodeDownAction);
			FlexUtilGlobals.getInstance().registerAction(NodeLeftAction);
			FlexUtilGlobals.getInstance().registerAction(NodePageDownAction);
			FlexUtilGlobals.getInstance().registerAction(NodePageUpAction);
			FlexUtilGlobals.getInstance().registerAction(NodeRightAction);
			FlexUtilGlobals.getInstance().registerAction(NodeUpAction);
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(FlexUtilConstants.CATEGORY_ALL)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(RefreshAction.ID));
			
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
