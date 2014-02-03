package org.flowerplatform.flex_client.core.mindmap.remote.update {
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.mindmap.remote.update.ClientNodeStatus")]
	public class ClientNodeStatus {
		
		public var nodeId:String;
		
		public var timestamp:Number;
		
		public var visibleChildren:ArrayCollection; /* of ClientNodeStatus */ 
		
	}	
}