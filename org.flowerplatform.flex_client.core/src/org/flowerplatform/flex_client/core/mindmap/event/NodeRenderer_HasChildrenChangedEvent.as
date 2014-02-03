package org.flowerplatform.flex_client.core.mindmap.event {
	
	import flash.events.Event;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRenderer_HasChildrenChangedEvent extends Event {
		
		public static const HAS_CHILDREN_CHANGED:String = "HasChildrenChangedEvent";
				
		public function NodeRenderer_HasChildrenChangedEvent() {
			super(HAS_CHILDREN_CHANGED, false, false);
		}
		
	}	
}