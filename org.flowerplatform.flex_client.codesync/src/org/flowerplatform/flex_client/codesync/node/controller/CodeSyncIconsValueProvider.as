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
package org.flowerplatform.flex_client.codesync.node.controller {
	
	import org.flowerplatform.flex_client.codesync.CodeSyncPlugin;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.node.controller.GenericDescriptorValueProvider;
	
	/**
	 * Responsible with decorating the base icon with sync markers.
	 * 
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncIconsValueProvider extends GenericDescriptorValueProvider {
		
		public function CodeSyncIconsValueProvider() {
			super(CorePlugin.PROPERTY_FOR_ICONS_DESCRIPTOR);
		}
		
		override public function getValue(node:Node):Object {
			var icon:String = String(super.getValue(node));
			var composedUrl:String = CodeSyncPlugin.getInstance().getImageComposerUrl(icon);
			if (node.properties.conflict == true) {
				composedUrl = append(composedUrl, "syncMarker_conflict.gif");
			} else if (node.properties.childrenConflict == true) {
				composedUrl = append(composedUrl, "syncMarker_childrenConflict.gif");
			} else if (node.properties.added == true) {
				composedUrl = append(composedUrl, "syncMarker_added.gif");
			} else if (node.properties.removed == true) {
				composedUrl = append(composedUrl, "syncMarker_deleted.gif");
			} else if (node.properties.sync == false) {
				composedUrl = append(composedUrl, "syncMarker_red.gif");
			} else if (node.properties.childrenSync == false) {
				composedUrl = append(composedUrl, "syncMarker_orange.gif");
			} else {
				composedUrl = append(composedUrl, "syncMarker_green.gif");
			}
			return composedUrl;
		}
		
		private function append(composedUrl:String, marker:String):String {
			return CodeSyncPlugin.getInstance().getImageComposerUrl(composedUrl, 
				"org.flowerplatform.codesync/images/sync-markers/" + marker);
		}
		
	}
}