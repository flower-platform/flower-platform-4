package org.flowerplatform.flex_client.mindmap {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeChildrenController;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeController;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeDragController;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeInplaceEditorController;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeRendererController;
	import org.flowerplatform.flex_client.core.mindmap.controller.ResourceTypeDynamicCategoryProvider;
	import org.flowerplatform.flex_client.core.mindmap.renderer.NodeSelectionRenderer;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.mindmap.renderer.MindMapNodeRenderer;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_renderer.IconsWithButtonPropertyRenderer;
	import org.flowerplatform.flexdiagram.controller.AbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.model_children.ModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.DynamicModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.model_extra_info.ModelExtraInfoController;
	import org.flowerplatform.flexdiagram.controller.renderer.RendererController;
	import org.flowerplatform.flexdiagram.controller.selection.BasicSelectionController;
	import org.flowerplatform.flexdiagram.controller.selection.SelectionController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	import org.flowerplatform.flexdiagram.tool.controller.InplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.DragController;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.AllDynamicCategoryProvider;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;

	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapPlugin extends AbstractFlowerFlexPlugin {
						
		public static const FREEPLANE_MINDMAP_RESOURCE_KEY:String = "freePlaneMindMap";
		public static const FREEPLANE_PERSISTENCE_RESOURCE_KEY:String = "freePlanePersistence";
		
		public static const FREEPLANE_MINDMAP_CATEGORY:String = ResourceTypeDynamicCategoryProvider.CATEGORY_RESOURCE_PREFIX + FREEPLANE_MINDMAP_RESOURCE_KEY;
		public static const FREEPLANE_PERSISTENCE_CATEGORY:String = ResourceTypeDynamicCategoryProvider.CATEGORY_RESOURCE_PREFIX + FREEPLANE_PERSISTENCE_RESOURCE_KEY;
				
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
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor("root2").addCategory(FREEPLANE_MINDMAP_CATEGORY);
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor("repo").addCategory(FREEPLANE_MINDMAP_CATEGORY);
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor("fileSystem").addCategory(FREEPLANE_MINDMAP_CATEGORY);
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor("fileNode").addCategory(FREEPLANE_MINDMAP_CATEGORY);
			
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(AllDynamicCategoryProvider.CATEGORY_ALL)
				.addSingleController(ModelChildrenController.TYPE, new NodeChildrenController())
				.addSingleController(MindMapModelController.TYPE, new NodeController())
				.addSingleController(DragController.TYPE, new NodeDragController())
				.addSingleController(SelectionController.TYPE, new BasicSelectionController(NodeSelectionRenderer))
				.addSingleController(ModelExtraInfoController.TYPE, new DynamicModelExtraInfoController())
				.addSingleController(InplaceEditorController.TYPE, new NodeInplaceEditorController())
				.addSingleController(RendererController.TYPE, new NodeRendererController(MindMapNodeRenderer))
				.addSingleController(AbsoluteLayoutRectangleController.TYPE, new MindMapAbsoluteLayoutRectangleController());
			
			// register PropertiesPlugin Renderer
			PropertiesPlugin.getInstance().propertyRendererClasses["MindMapIconsWithButton"] = new FactoryWithInitialization
				(IconsWithButtonPropertyRenderer, {
					clickHandler: function(itemRendererHandler:IDialogResultHandler, propertyName:String, propertyValue:Object):void {
						var dialog:IconsView = new IconsView();
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
			
			CorePlugin.getInstance().iconSideBarClass = IconsSideBar;
		
		}
		
		
	}
}
