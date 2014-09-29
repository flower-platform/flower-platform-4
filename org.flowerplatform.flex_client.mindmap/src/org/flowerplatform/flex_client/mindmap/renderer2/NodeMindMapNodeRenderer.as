package org.flowerplatform.flex_client.mindmap.renderer2 {
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flexdiagram.mindmap.MindMapNodeRenderer;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class NodeMindMapNodeRenderer extends MindMapNodeRenderer {
		
		public function NodeMindMapNodeRenderer() {
			super();
			featureForValuesProvider = MindMapConstants.MIND_MAP_FEATURE_FOR_VALUES_PROVIDER;
		}
	}
}