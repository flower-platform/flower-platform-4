package org.flowerplatform.flex_client.mindmap {
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_renderer.IconsWithButtonPropertyRenderer;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
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
		
		}
		
		
	}
}
