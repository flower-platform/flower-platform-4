package org.flowerplatform.flex_client.mindmap.renderer2 {
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class NodeMindMapRenderer extends MindMapRenderer {
		
		public function NodeMindMapRenderer() {
			super();
			featureForValuesProvider = MindMapConstants.MIND_MAP_FEATURE_FOR_VALUES_PROVIDER;
		}
	}
}