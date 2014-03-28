package org.flowerplatform.flex_client.core.editor.remote {
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	[RemoteClass(alias="org.flowerplatform.core.node.remote.NodeWithChildren")]
	public class NodeWithChildren {
		
		public var node:Node;
		
		public var children:ArrayCollection; /* of NodeWithChildren */
		
	}
}