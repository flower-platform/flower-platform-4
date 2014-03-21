package org.flowerplatform.flex_client.core.editor {
	import org.flowerplatform.flex_client.core.editor.update.NodeUpdateProcessor;
	
	/**
	 * @see SaveResourceNodesView
	 * @author Cristina Constantinescu
	 */
	[Bindable]
	public class ResourceNode {
		
		public var resourceNodeId:String;
		
		public var selected:Boolean;
		
		public function ResourceNode(resourceNodeId:String, selected:Boolean) {
			this.resourceNodeId = resourceNodeId;			
			this.selected = selected;
		}
		
	}
}