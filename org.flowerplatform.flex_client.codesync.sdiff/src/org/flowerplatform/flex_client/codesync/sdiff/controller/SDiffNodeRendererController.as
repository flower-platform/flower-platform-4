package org.flowerplatform.flex_client.codesync.sdiff.controller
{
	import org.flowerplatform.flex_client.mindmap.MindMapConnector;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;

	public class SDiffNodeRendererController extends MindMapModelRendererController {
		
		public function SDiffNodeRendererController(rendererClass:Class, orderIndex:int = 0) {
			super(rendererClass, MindMapConnector, orderIndex);
		}
	}
}