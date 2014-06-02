package org.flowerplatform.flex_client.core.node.event {
	
	import flash.events.Event;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	/**
	 * Dispatched by <code>NodeRegistry</code> when a node is added as root.
	 * 
	 * <p>
	 * Listeners can add additional behavior like expanded it immediately.
	 * 
	 * @author Cristina Constantinescu
	 */  
	public class RootNodeAddedEvent extends Event {
		
		public static const TYPE:String = "RootNodeAddedEvent";
		
		public var rootNode:Node;
		
		public function RootNodeAddedEvent(rootNode:Node) {
			super(TYPE, false, false);
			this.rootNode = rootNode;
		}
		
	}
}