package org.flowerplatform.flex_client.mindmap {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
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
	import org.flowerplatform.flexdiagram.controller.visual_children.AbsoluteLayoutVisualChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.VisualChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapRootModelChildrenController;
	import org.flowerplatform.flexdiagram.tool.controller.InplaceEditorController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.DragController;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.AllDynamicCategoryProvider;
	import org.flowerplatform.flexutil.controller.NullController;
	import org.flowerplatform.flexutil.controller.TypeDescriptor;
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
				
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(MindMapRootModelWrapper.ID)
				.addSingleController(ModelChildrenController.TYPE, new MindMapRootModelChildrenController(-10))
				.addSingleController(VisualChildrenController.TYPE, new AbsoluteLayoutVisualChildrenController(-10))
				.addSingleController(DragController.TYPE, new NullController(-10))
				.addSingleController(SelectionController.TYPE, new NullController(-10))
				.addSingleController(RendererController.TYPE, new NullController(-10))
				.addSingleController(InplaceEditorController.TYPE, new NullController(-10));	
						
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(AllDynamicCategoryProvider.CATEGORY_ALL)
				.addSingleController(ModelChildrenController.TYPE, new NodeChildrenController())
				.addSingleController(MindMapModelController.TYPE, new NodeController())				
				.addSingleController(ModelExtraInfoController.TYPE, new DynamicModelExtraInfoController())				
				.addSingleController(AbsoluteLayoutRectangleController.TYPE, new MindMapAbsoluteLayoutRectangleController())
				.addSingleController(DragController.TYPE, new NodeDragController())
				.addSingleController(SelectionController.TYPE, new BasicSelectionController(NodeSelectionRenderer))
				.addSingleController(RendererController.TYPE, new NodeRendererController(MindMapNodeRenderer))
				.addSingleController(InplaceEditorController.TYPE, new NodeInplaceEditorController());		
			
			// register PropertiesPlugin Renderer
			PropertiesPlugin.getInstance().propertyRendererClasses["MindMapIconsWithButton"] = new FactoryWithInitialization
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
			
			CorePlugin.getInstance().iconSideBarClass = MindMapIconsBar;
		
		}
				
	}
}
