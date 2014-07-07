/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.codesync.node.renderer {
	
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.codesync.CodeSyncPlugin;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.NodeControllerUtils;
	import org.flowerplatform.flex_client.core.node.event.NodeUpdatedEvent;
	import org.flowerplatform.flex_client.mindmap.renderer.NodeRenderer;
	import org.flowerplatform.flexutil.FlowerArrayList;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncNodeRenderer extends NodeRenderer {
		
		override protected function nodeUpdatedHandler(event:NodeUpdatedEvent = null):void {
			super.nodeUpdatedHandler(event);
			
			if (NodeControllerUtils.hasPropertyChanged(node, CodeSyncConstants.SYNC, event) ||
				NodeControllerUtils.hasPropertyChanged(node, CodeSyncConstants.CHILDREN_SYNC, event) ||
				NodeControllerUtils.hasPropertyChanged(node, CodeSyncConstants.CONFLICT, event) ||
				NodeControllerUtils.hasPropertyChanged(node, CodeSyncConstants.CHILDREN_CONFLICT, event)) {
				// a sync property has changed, redecorate the original icon
				composeIconWithSyncMarkers();
			}
		}
		
		protected function composeIconWithSyncMarkers():void {
			var iconsProvider:GenericValueProviderFromDescriptor =  NodeControllerUtils.getIconsProvider(diagramShellContext.diagramShell.registry, node);
			var icon:String = iconsProvider.getValue(node) as String;
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
			
			icons = new FlowerArrayList([composedUrl]);
		}
		
		private function append(composedUrl:String, marker:String):String {
			return CodeSyncPlugin.getInstance().getImageComposerUrl(composedUrl, getSyncMarkerPath(marker));
		}
		
		private function getSyncMarkerPath(marker:String):String {
			return "org.flowerplatform.resources/images/codesync/sync-markers/" + marker;
		}
		
	}
}
