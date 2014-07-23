package org.flowerplatform.flex_client.core.node {
	import mx.collections.IList;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public interface IResourceOperationsHandler	{
		
		function get nodeRegistryManager():*;
		function set nodeRegistryManager(value:*):void;
		
		function updateGlobalDirtyState(someResourceNodeHasBecomeDirty:Boolean):void;
		
		function showSaveDialog(nodeRegistries:Array = null, dirtyResourceNodes:IList = null, handler:Function = null):void;
		function showReloadDialog(nodeRegistries:Array = null, resourceSets:Array = null):void;
			
	}
}