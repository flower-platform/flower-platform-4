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
package org.flowerplatform.flex_client.codesync {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.controller.AbstractController;
	import org.flowerplatform.flexutil.controller.ITypeDescriptorRegistryProvider;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncConfigTypeDescriptorRegistryProvider extends AbstractController implements ITypeDescriptorRegistryProvider {
		
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