package org.flowerplatform.flex_client.core.mindmap.remote {
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	[RemoteClass(alias="org.flowerplatform.core.node.remote.NodeWithVisibleChildren")]
	public class NodeWithVisibleChildren {
	
		public var node:Node;
		
		public var visibleChildren:ArrayCollection;
		
	}
}