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
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Vlad Bogdan Manica
	 */
	public class AddSiblingActionProvider implements IActionProvider {
		
		public function getActions(selection:IList):Vector.<IAction> {
			if (selection == null || selection.length != 1 || !(selection.getItemAt(0) is Node)) {
				return null;
			}
			
			var result:Vector.<IAction> = new Vector.<IAction>();
				
			var child:Node = Node(selection.getItemAt(0));			
			var parent:Node = Node(child).parent;
			
			// If the current node doesn't have a parent in the focused view then we don't create sibling action for it.
			if (parent == null) {
				return null;
			}
			
			// get the type of the selected parent			
			var parentType:String = parent.type;
						
			// get the descriptors for the selected parent type from the core dictionary
			var descriptors:IList = CorePlugin.getInstance().nodeTypeDescriptorRegistryProvider.getTypeDescriptorRegistry(parent)
				.getAdditiveControllers(CoreConstants.ADD_CHILD_DESCRIPTOR, parent);
			if (descriptors != null) {
				for each (var descriptor:AddChildDescriptor in descriptors) {
					result.push(new AddNodeAction(descriptor, child.nodeUri));
				}
			}
			return result;
		}
	}
}