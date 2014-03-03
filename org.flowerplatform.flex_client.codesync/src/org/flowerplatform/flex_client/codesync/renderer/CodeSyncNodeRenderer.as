package org.flowerplatform.flex_client.codesync.renderer {
	
	import org.flowerplatform.flex_client.codesync.CodeSyncPlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.renderer.NodeRenderer;
	import org.flowerplatform.flex_client.core.mindmap.update.event.NodeUpdatedEvent;
	import org.flowerplatform.flexutil.FlowerArrayList;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncNodeRenderer extends NodeRenderer {
		
		override public function set data(value:Object):void {
			super.data = value;
			composeIconWithSyncMarkers();			
		}
		
		override protected function nodeUpdatedHandler(event:NodeUpdatedEvent = null):void {
			super.nodeUpdatedHandler(event);
			composeIconWithSyncMarkers();
		}
		
		protected function composeIconWithSyncMarkers():void {
			if (data == null) {
				return;
			}
			
			var node:Node = Node(data);
			
			var icon:String = node.properties.icon;
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
			return "org.flowerplatform.codesync/images/sync-markers/" + marker;
		}
		
	}
}