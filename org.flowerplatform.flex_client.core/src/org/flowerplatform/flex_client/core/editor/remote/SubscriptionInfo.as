package org.flowerplatform.flex_client.core.editor.remote {
	
	/**
	 * @author Mariana Gheorghe
	 */
	[RemoteClass(alias="org.flowerplatform.core.node.remote.SubscriptionInfo")]
	public class SubscriptionInfo {
		
		public var rootNode:Node;
		
		public var resourceNode:Node;
		
		public var resourceSet:String;

	}
}