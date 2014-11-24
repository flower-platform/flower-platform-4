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
package org.flowerplatform.flex_client.codesync.node.renderer {
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.codesync.CodeSyncPlugin;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.renderer.NodeMindMapRenderer;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristian Spiescu
	 */
	public class CodeSyncNodeRenderer extends NodeMindMapRenderer {
		
		/**
		 * @author Cristina Constantinescu
		 */
		private static const SYNC_PROPERTIES:Array = [CodeSyncConstants.SYNC, CodeSyncConstants.CHILDREN_SYNC, CodeSyncConstants.CONFLICT, CodeSyncConstants.CHILDREN_CONFLICT];
		
		override protected function modelChangedHandler(event:PropertyChangeEvent):void {
			super.modelChangedHandler(event);
			
			if (event == null || (SYNC_PROPERTIES.indexOf(event.property) >= 0)) {
				// a sync property has changed, redecorate the original icon
				composeIconWithSyncMarkers();
			}
		}
		
		protected function composeIconWithSyncMarkers():void {
			var node:Node = Node(data);
			var icons:IList = 
					IList(CorePlugin.getInstance().getNodeValuesProviderForMindMap(typeDescriptorRegistry, node)
							.getValue(typeDescriptorRegistry, node, FlexDiagramConstants.BASE_RENDERER_ICONS));
			var initialUrl:String = null, composedUrl:String = null;
			if (icons.length > 0) {
				initialUrl = String(icons.getItemAt(0));
			}
			if (node.properties.conflict == true) {
				composedUrl = append(initialUrl, "syncMarker_conflict.gif");
			} else if (node.properties.childrenConflict == true) {
				composedUrl = append(initialUrl, "syncMarker_childrenConflict.gif");
			} else if (node.properties.added == true) {
				composedUrl = append(initialUrl, "syncMarker_added.gif");
			} else if (node.properties.removed == true) {
				composedUrl = append(initialUrl, "syncMarker_deleted.gif");
			} else if (node.properties.sync == false) {
				composedUrl = append(initialUrl, "syncMarker_red.gif");
			} else if (node.properties.childrenSync == false) {
				composedUrl = append(initialUrl, "syncMarker_orange.gif");
			} else {
				composedUrl = append(initialUrl, "syncMarker_green.gif");
			}
			
			this.icons = new ArrayList([composedUrl]);
		}
		
		private function append(composedUrl:String, marker:String):String {
			return CodeSyncPlugin.getInstance().getImageComposerUrl(composedUrl, "org.flowerplatform.resources/images/codesync/sync-markers/" + marker);
		}
		
	}
}
