/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.flex_client.core.node.controller {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.remote.GenericDescriptor;
	import org.flowerplatform.flexutil.controller.AbstractController;
	import org.flowerplatform.flexutil.controller.TypeDescriptor;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class GenericDescriptorValueProvider extends AbstractController implements IGenericValueProvider {
		
		private var genericDescriptorName:String;
		
		public function GenericDescriptorValueProvider(genericDescriptorName:String) {
			this.genericDescriptorName = genericDescriptorName;
		}
		
		public function getValue(node:Node):Object {
			var propertyName:String = getPropertyNameFromGenericDescriptor(node);
			if (propertyName == null) {
				return null;
			}
			return node.properties[propertyName];
		}
		
		public function getPropertyNameFromGenericDescriptor(node:Node):String {
			var typeDescriptor:TypeDescriptor = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getExpectedTypeDescriptor(node.type);
			if (typeDescriptor == null) {
				return null;
			}
			
			// find the generic descriptor registered for this node
			var genericDescriptor:GenericDescriptor = GenericDescriptor(typeDescriptor.getSingleController(genericDescriptorName, node));
			if (genericDescriptor == null) {
				return null;
			}
			
			return String(genericDescriptor.value);
		}
	}
}