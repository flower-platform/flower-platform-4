package org.flowerplatform.flex_client.mindmap.renderer {
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class NodeMindMapRenderer extends MindMapRenderer {
		
		public function NodeMindMapRenderer() {
			super();
			featureForValuesProvider = CoreConstants.MIND_MAP_FEATURE_FOR_VALUES_PROVIDER;
		}
	}
}