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
package org.flowerplatform.flex_client.properties.action {
	
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.controller.ITypeDescriptorRegistryProvider;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class AddChildActionProvider implements IActionProvider {
		
		public function getActions(selection:IList):Vector.<IAction> {
			if (selection == null || selection.length != 1 || !(selection.getItemAt(0) is Node)) {
				return null;
			}
			
			var result:Vector.<IAction> = new Vector.<IAction>();
			
			// get the selected parent
			var parent:Node = Node(selection.getItemAt(0));
			
			// get the descriptors for the selected parent from the core registry
			result = result.concat(getActionsFromRegistry(CorePlugin.getInstance().nodeTypeDescriptorRegistry, parent));
			
			// get local registries for the selected parent
			var providers:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistry
				.getAdditiveControllers(FlexUtilConstants.TYPE_DESCRIPTOR_REGISTRY_PROVIDER, parent);
			for each (var provider:ITypeDescriptorRegistryProvider in providers) {
				// get the descriptors from a local registry
				var registry:TypeDescriptorRegistry = provider.getTypeDescriptorRegistry(parent);
				result = result.concat(getActionsFromRegistry(registry, parent));
			}
			return result;
		}
		
		private function getActionsFromRegistry(registry:TypeDescriptorRegistry, parent:Node):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			var descriptors:IList = registry.getAdditiveControllers(CoreConstants.ADD_CHILD_DESCRIPTOR, parent);
			if (descriptors != null) {
				for each (var descriptor:AddChildDescriptor in descriptors) {
					result.push(new AddNodeAction(descriptor));
				}
			}
			return result;
		}
	}
}