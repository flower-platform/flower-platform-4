package org.flowerplatform.flex_client.core.editor.remote {
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	[RemoteClass(alias="org.flowerplatform.core.node.remote.FullNodeIdWithChildren")]
	public class FullNodeIdWithChildren {
	
		public var fullNodeId:String;
		
		public var visibleChildren:ArrayCollection; /* of FullNodeIdWithChildren */
		
	}
}