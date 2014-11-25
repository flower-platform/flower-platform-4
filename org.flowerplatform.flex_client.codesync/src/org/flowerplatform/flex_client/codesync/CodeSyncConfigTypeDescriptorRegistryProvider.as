package org.flowerplatform.flex_client.codesync {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.controller.ITypeDescriptorRegistryProvider;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncConfigTypeDescriptorRegistryProvider implements ITypeDescriptorRegistryProvider {
		
		public function getTypeDescriptorRegistry(model:Object):TypeDescriptorRegistry {
			if (model is Node) {
				var node:Node = Node(model);
				
				// get resource uri
				var index:int = node.nodeUri.indexOf("#");
				var resourceUri:String = index < 0 ? node.nodeUri : node.nodeUri.substr(0, index);
				
				// get resource node
				var resourceSet:String = CorePlugin.getInstance().nodeRegistryManager.resourceUriToResourceSet[resourceUri];
				var nodeRegistry:* = CorePlugin.getInstance().nodeRegistryManager.getNodeRegistriesForResourceSet(resourceSet)[0];
				if (nodeRegistry == null) {
					return null;
				}
				var resourceNode:Node = nodeRegistry.getNodeById(resourceUri);
				
				// get config dirs property
				var configDirs:String = resourceNode.properties[CodeSyncConstants.CODE_SYNC_CONFIG_PROPERTY_DIRS_KEY];
				if (configDirs != null) {
					return CodeSyncPlugin.getInstance().codeSyncConfigs[configDirs];
				}
			}
			return null;
		}
	}
}