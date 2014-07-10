package org.flowerplatform.flex_client.core.node {
	import org.flowerplatform.flex_client.core.editor.resource.NodeRegistryManager;
	
	public interface IResourceOperationsHandler	{
		
		function get nodeRegistryManager():NodeRegistryManager;
		
		function updateGlobalDirtyState(someResourceNodeHasBecomeDirty:Boolean):void;
		
		function showSaveDialog(nodeRegistries:Array = null, dirtyResourceNodeIds:Array = null, handler:Function = null):void;
		function showReloadDialog(nodeRegistries:Array = null, resourceSets:Array = null):void;
			
	}
}