package org.flowerplatform.flex_client.core.mindmap.remote.update {
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.node.update.remote.ChildrenUpdate")]
	public class ChildrenUpdate extends Update {
		
		public static const ADDED:String = "ADDED";
		public static const REMOVED:String = "REMOVED";
		
		public var type:String;
		
		public var targetNode:Node;
		
		public var fullTargetNodeAddedBeforeId:String;

	}
}