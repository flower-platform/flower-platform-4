package org.flowerplatform.flex_client.codesync.regex {
	
	import org.flowerplatform.flex_client.codesync.regex.action.GenerateMatchesAction;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_renderer.DropDownListPropertyRenderer;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.Pair;
	import org.flowerplatform.flexutil.Utils;
	
	public class CodeSyncRegexPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CodeSyncRegexPlugin;
		
		public static function getInstance():CodeSyncRegexPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			CorePlugin.getInstance().serviceLocator.addService("codeSyncRegexService");
			
			PropertiesPlugin.getInstance().propertyDescriptorTypeToPropertyRendererFactory[CodeSyncRegexConstants.REGEX_ACTIONS_DESCRIPTOR_TYPE] = new FactoryWithInitialization
				(DropDownListPropertyRenderer, {	
					requestDataProviderHandler: function(node:Node, callbackFunction:Function):void {
						CorePlugin.getInstance().serviceLocator.invoke("codeSyncRegexService.getRegexActions", null, callbackFunction);
					},
					labelFunction: function(item:Object):String {
						return Pair(item).a + " - " + 	Pair(item).b;
					}
				});	
			
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(GenerateMatchesAction);
			
		}
		
		override protected function registerClassAliases():void	{
			
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
		
	}
}