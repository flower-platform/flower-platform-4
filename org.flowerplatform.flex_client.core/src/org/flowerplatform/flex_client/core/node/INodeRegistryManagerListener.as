package org.flowerplatform.flex_client.core.node {
	
	/**
	 * @author Cristina Constantinescu
	 */
	public interface INodeRegistryManagerListener {
		
		function nodeRegistryRemoved(nodeRegistry:*):void;
		
		function resourceNodeRemoved(resourceNodeUri:String, nodeRegistry:*):void;
		
	}
}