package org.flowerplatform.flex_client.codesync.action {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.codesync.CodeSyncPlugin;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ReloadConfigurationAction extends DiagramShellAwareActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.codesync.action.ReloadConfigurationAction";
		
		public function ReloadConfigurationAction() {
			super();
			label = Resources.getMessage("codesync.action.reloadConfig");
			icon = Resources.reloadIcon;
			preferShowOnActionBar = true;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			CorePlugin.getInstance().serviceLocator.invoke("codeSyncOperationsService.reloadConfiguration", [node.nodeUri], function(result:Object):void {
				
				CorePlugin.getInstance().serviceLocator.invoke("codeSyncOperationsService.getCodeSyncConfigurationRemote", [node.nodeUri], function(result:Object):void {
					var registry:TypeDescriptorRegistry = new TypeDescriptorRegistry();
					registry.addTypeDescriptorsRemote(ArrayCollection(result));
					var path:String = CorePlugin.getInstance().getSchemeSpecificPart(node.nodeUri);
					path = path.replace("|", "/");
					path = path.substring(0, path.lastIndexOf("/"));
					CodeSyncPlugin.getInstance().codeSyncConfigs[path] = registry;
				});
				
			});
		}
	}
}