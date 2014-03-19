package org.flowerplatform.flex_client.core.editor {
	
	[Bindable]
	public class ResourceNodeData {
		
		public var resourceNodeId:String;
		
		public var selected:Boolean;
		
		public function ResourceNodeData(resourceNodeId:String, selected:Boolean) {
			this.resourceNodeId = resourceNodeId;
			this.selected = selected;
		}
		
	}
}