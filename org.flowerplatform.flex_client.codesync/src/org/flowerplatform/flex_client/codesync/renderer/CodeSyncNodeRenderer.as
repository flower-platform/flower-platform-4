package org.flowerplatform.flex_client.codesync.renderer {
	
	import org.flowerplatform.flex_client.codesync.CodeSyncPlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.renderer.NodeRenderer;
	import org.flowerplatform.flexutil.FlowerArrayList;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncNodeRenderer extends NodeRenderer {
		
		override public function set data(value:Object):void {
			super.data = value;
			
			if (value == null) {
				return;
			}
			
			var node:Node = Node(value);
			
			// compose the image with decorators
			var icon:String = node.properties.icon;
			var composedUrl:String = CodeSyncPlugin.getInstance().getImageComposerUrl(icon);
			if (node.properties.sync == false) {
				composedUrl = CodeSyncPlugin.getInstance().getImageComposerUrl(composedUrl, "org.flowerplatform.codesync/images/sync-markers/syncMarker_red.gif");
			} else if (node.properties.childrenSync == false) {
				composedUrl = CodeSyncPlugin.getInstance().getImageComposerUrl(composedUrl, "org.flowerplatform.codesync/images/sync-markers/syncMarker_orange.gif");
			} else {
				composedUrl = CodeSyncPlugin.getInstance().getImageComposerUrl(composedUrl, "org.flowerplatform.codesync/images/sync-markers/syncMarker_green.gif");
			}
			
			icons = new FlowerArrayList([composedUrl]);
		}
		
	}
}