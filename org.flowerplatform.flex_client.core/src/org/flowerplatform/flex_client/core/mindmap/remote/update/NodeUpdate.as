package org.flowerplatform.flex_client.core.mindmap.remote.update {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.node.update.remote.NodeUpdate")]
	public class NodeUpdate {
		
		public var node:Node;
		
		public var updatedProperties:Object;
		
		public var fullChildrenList:ArrayCollection; /* of Node */
		
		public var childrenListUpdates:ArrayCollection; /* of ChildrenListUpdate */
		
		public var nodeUpdatesForChildren:ArrayCollection; /* of NodeUpdate */
		
	}
}