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
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flexutil.controller.TypeDescriptor;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class NodeControllerUtils {
		
		public static function getTitleProvider(registry:TypeDescriptorRegistry, node:Object):GenericValueProviderFromDescriptor {
			return getValueProvider(registry, node, CoreConstants.NODE_TITLE_PROVIDER);
		}
		
		public static function getIconsProvider(registry:TypeDescriptorRegistry, node:Object):GenericValueProviderFromDescriptor {
			return getValueProvider(registry, node, CoreConstants.NODE_ICONS_PROVIDER);
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public static function getSideProvider(registry:TypeDescriptorRegistry, node:Object):GenericValueProviderFromDescriptor {
			return getValueProvider(registry, node, CoreConstants.NODE_SIDE_PROVIDER);
		}
		
		public static function getValueProvider(registry:TypeDescriptorRegistry, 
												   node:Object, controllerType:String):GenericValueProviderFromDescriptor {
			var typeDescriptor:TypeDescriptor = registry.getExpectedTypeDescriptor(node.type);
			if (typeDescriptor == null) {
				return null;
			}
			return GenericValueProviderFromDescriptor(typeDescriptor.getSingleController(controllerType, node));
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public static function hasPropertyChanged(node:Node, property:String, event:NodeUpdatedEvent = null):Boolean {			
			return (event != null && event.updatedProperties != null) ? event.updatedProperties.getItemIndex(property) != -1 : node.properties.hasOwnProperty(property);
		}
		
	}
}