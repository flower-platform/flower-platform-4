package org.flowerplatform.flex_client.core.mindmap.remote.update {
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.node.update.remote.ChildrenListUpdate")]
	public class ChildrenListUpdate {
	
		public static const ADDED:String = "ADDED";
		public static const REMOVED:String = "REMOVED";
		
		public var parentNode:Node;
		
		public var type:String;
		
		public var index:int;
		
		public var node:Object;
		
	}
}