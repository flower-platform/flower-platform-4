package org.flowerplatform.flex_client.core.node.event {
	import flash.events.Event;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	/**
	 * Dispatched by <code>NodeRegistry</code> before removing children from a node.
	 * <p>
	 * If <code>dontRemoveChildren</code> is changed by a listener to <code>true</code> then the remove children behavior will not be executed further.
	 * 
	 * <p>
	 * Listeners can add additional behavior like showing the save dialog for dirty resource nodes before removing children.
	 * 
	 * @author Cristina Constantinescu
	 */  
	public class BeforeRemoveNodeChildrenEvent extends Event {
		
		public static const BEFORE_REMOVED:String = "BeforeRemoveChildrenEvent";
		
		public var node:Node;
		public var refreshChildren:Boolean;
		
		public var dontRemoveChildren:Boolean = false;
		
		public function BeforeRemoveNodeChildrenEvent(node:Node, refreshChildren:Boolean) {
			super(BEFORE_REMOVED, false, false);
			this.node = node;
			this.refreshChildren = refreshChildren;
		}
		
	}
}