package org.flowerplatform.flex_client.codesync.regex {
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.codesync.regex.action.ColorTextEditorAction;
	import org.flowerplatform.flex_client.codesync.regex.action.GenerateMatchesAction;
	import org.flowerplatform.flex_client.codesync.regex.action.ShowMatchesGroupedByRegexAction;
	import org.flowerplatform.flex_client.codesync.regex.action.ShowOrderedMatchesAction;
	import org.flowerplatform.flex_client.codesync.regex.action.ShowTextEditorAction;
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.action.ActionDescriptor;
	import org.flowerplatform.flex_client.core.editor.action.RemoveNodeAction;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_renderer.DropDownListPropertyRenderer;
	import org.flowerplatform.flex_client.text.TextConstants;
	import org.flowerplatform.flex_client.text.TextEditorFrontend;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Pair;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristina Constantinescu
	 */
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
			
//			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(GenerateMatchesAction);
//			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(ShowOrderedMatchesAction);
//			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(ShowMatchesGroupedByRegexAction);
//			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(ShowTextEditorAction);
//			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(ColorTextEditorAction);
			
//			CorePlugin.getInstance().actionRegistry[GenerateMatchesAction.ID] = new FactoryWithInitialization(GenerateMatchesAction).newInstance();
//			CorePlugin.getInstance().actionRegistry[ShowOrderedMatchesAction.ID] = new FactoryWithInitialization(ShowOrderedMatchesAction).newInstance();
//			CorePlugin.getInstance().actionRegistry[ShowMatchesGroupedByRegexAction.ID] = new FactoryWithInitialization(ShowMatchesGroupedByRegexAction).newInstance();
//			CorePlugin.getInstance().actionRegistry[ShowTextEditorAction.ID] = new FactoryWithInitialization(ShowTextEditorAction).newInstance();
//			CorePlugin.getInstance().actionRegistry[ColorTextEditorAction.ID] = new FactoryWithInitialization(ColorTextEditorAction).newInstance();
			
			FlexUtilGlobals.getInstance().registerAction(GenerateMatchesAction);
			FlexUtilGlobals.getInstance().registerAction(ShowOrderedMatchesAction);
			FlexUtilGlobals.getInstance().registerAction(ShowMatchesGroupedByRegexAction);
			FlexUtilGlobals.getInstance().registerAction(ShowTextEditorAction);
			FlexUtilGlobals.getInstance().registerAction(ColorTextEditorAction);
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CodeSyncRegexConstants.REGEX_CONFIG_TYPE)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR,new ActionDescriptor(GenerateMatchesAction.ID));
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CodeSyncRegexConstants.REGEX_MACRO_TYPE)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR,new ActionDescriptor(GenerateMatchesAction.ID));
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CodeSyncRegexConstants.REGEX_TYPE)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR,new ActionDescriptor(GenerateMatchesAction.ID));
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CodeSyncRegexConstants.REGEX_MATCHES_TYPE)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR,new ActionDescriptor(ShowOrderedMatchesAction.ID))
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR,new ActionDescriptor(ShowTextEditorAction.ID));
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(FlexUtilConstants.CATEGORY_ALL)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR,new ActionDescriptor(ShowMatchesGroupedByRegexAction.ID));
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CodeSyncRegexConstants.REGEX_MATCH_TYPE)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR,new ActionDescriptor(ShowTextEditorAction.ID))
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR,new ActionDescriptor(ColorTextEditorAction.ID));
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CodeSyncRegexConstants.VIRTUAL_REGEX_TYPE)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR,new ActionDescriptor(ShowTextEditorAction.ID))
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR,new ActionDescriptor(ColorTextEditorAction.ID));
		}
		
		override protected function registerClassAliases():void	{
			
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
		
		public function getTextEditorFrontend(matchesEditorFrontend:EditorFrontend, showIfNecessary:Boolean = false):TextEditorFrontend {
			var resourceNodeUri:String = CorePlugin.getInstance().nodeRegistryManager.getResourceUrisForNodeRegistry(matchesEditorFrontend.nodeRegistry)[0];
			var resourceNode:Node = matchesEditorFrontend.nodeRegistry.getNodeById(resourceNodeUri);
			
			var textEditorResourceNodeUri:String = resourceNode.getPropertyValue(CodeSyncRegexConstants.RESOURCE_URI);
			
			var components:ArrayCollection = new ArrayCollection();
			FlexUtilGlobals.getInstance().workbench.getAllEditorViews(null, components);
			
			var textEditorFrontend:TextEditorFrontend;
			for each (var component:UIComponent in components) {								
				if (component is TextEditorFrontend && CorePlugin.getInstance().nodeRegistryManager.getResourceUrisForNodeRegistry(TextEditorFrontend(component).nodeRegistry)[0] == textEditorResourceNodeUri) {
					if (showIfNecessary) {
						FlexUtilGlobals.getInstance().workbench.setActiveView(UIComponent(TextEditorFrontend(component).viewHost), true);
					}
					textEditorFrontend = TextEditorFrontend(component);
					break;
				}
			}
			if (textEditorFrontend == null && showIfNecessary) {
				textEditorFrontend = TextEditorFrontend(FlexUtilGlobals.getInstance().workbench.getEditorFromViewComponent(CorePlugin.getInstance().openEditor(new Node(textEditorResourceNodeUri), TextConstants.TEXT_CONTENT_TYPE)));
			}
			return textEditorFrontend;
		}
		
	}
}