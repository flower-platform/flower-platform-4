package org.flowerplatform.flex_client.core.editor.resource.event {
	
	import flash.events.Event;
	
	/**
	 * Dispatched by <code>NodeRegistry</code> when a resource node is removed/unlinked from resourceNodeIds to nodeRegistries map.
	 * 
	 * <p>
	 * Listeners can add additional behavior like collapsing a node in case of a mind map structure.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class ResourceNodeRemovedEvent extends Event {
		
		public static const REMOVED:String = "ResourceNodeRemoved";
		
		public var resourceNodeId:String;
		
		public function ResourceNodeRemovedEvent(resourceNodeId:String) {
			super(REMOVED, false, false);
			this.resourceNodeId = resourceNodeId;
		}
		
	}
}