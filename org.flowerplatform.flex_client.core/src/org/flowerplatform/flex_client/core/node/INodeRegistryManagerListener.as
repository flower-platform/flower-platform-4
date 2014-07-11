package org.flowerplatform.flex_client.core.node {
	
	/**
	 * @author Cristina Constantinescu
	 */
	public interface INodeRegistryManagerListener {
		
		function nodeRegistryRemoved(nodeRegistry:NodeRegistry):void;
		
		function resourceNodeRemoved(resourceNodeUri:String, nodeRegistry:NodeRegistry):void;
		
	}
}