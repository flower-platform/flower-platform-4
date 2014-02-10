package org.flowerplatform.flex_client.core.mindmap.remote.update {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.node.update.remote.ClientNodeStatus")]
	public class ClientNodeStatus {
		
		public var node:Node;
		
		public var timestamp:Number;
		
		public var visibleChildren:ArrayCollection; /* of ClientNodeStatus */ 
		
	}	
}