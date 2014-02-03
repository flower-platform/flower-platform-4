package org.flowerplatform.flex_client.core.mindmap.remote.update {
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.mindmap.remote.update.NodeUpdate")]
	public class NodeUpdate {
		
		public var nodeId:String;
		
		public var updatedProperties:Object;
		
		public var fullChildrenList:ArrayCollection; /* of Node */
		
		public var childrenListUpdates:ArrayCollection; /* of ChildrenListUpdate */
		
		public var nodeUpdatesForChildren:ArrayCollection; /* of NodeUpdate */
		
	}
}