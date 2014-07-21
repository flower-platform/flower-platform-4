
package org.flowerplatform.flex_client.codesync.sdiff.controller{
	import org.flowerplatform.flex_client.mindmap.controller.NodeRendererController;
	
	public class PathNodeController extends NodeRendererController {
		
		public function PathNodeController(rendererClass:Class, orderIndex:int = -1) {
			super(rendererClass, orderIndex);
		}
	}
}