package org.flowerplatform.flex_client.core.mindmap.remote.update {
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.mindmap.remote.update.ChildrenListUpdate")]
	public class ChildrenListUpdate {
	
		public static const ADDED:String = "ADDED";
		public static const REMOVED:String = "REMOVED";
		
		public var parentNodeId:String;
		
		public var type:String;
		
		public var index:int;
		
		public var node:Object;
		
	}
}