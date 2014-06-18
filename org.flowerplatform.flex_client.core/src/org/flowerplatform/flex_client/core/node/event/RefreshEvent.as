package org.flowerplatform.flex_client.core.node.event {
	
	import flash.events.Event;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	/**
	 * Dispatched by <code>NodeRegistry</code> when a visual refresh is needed.
	 * 
	 * @author Cristina Constantinescu
	 */   
	public class RefreshEvent extends Event {
		
		public static const REFRESH:String = "RefreshEvent";
		
		public var node:Node;
		
		public function RefreshEvent(node:Node = null) {
			super(REFRESH, false, false);
			this.node = node;
		}
		
	}
}