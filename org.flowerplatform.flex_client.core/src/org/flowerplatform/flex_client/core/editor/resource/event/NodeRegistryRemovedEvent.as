package org.flowerplatform.flex_client.core.editor.resource.event {
	
	import flash.events.Event;
	
	/**
	 * Dispatched by <code>NodeRegistry</code> when it is removed/unlinked from resourceNodeIds to nodeRegistries map.
	 * 
	 * <p>
	 * Listeners can add additional behavior like closing the editor corresponding to a node registry.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class NodeRegistryRemovedEvent extends Event {
		
		public static const REMOVED:String = "NodeRegistryRemovedEvent";
		
		public function NodeRegistryRemovedEvent() {
			super(REMOVED, false, false);
		}
		
	}
}