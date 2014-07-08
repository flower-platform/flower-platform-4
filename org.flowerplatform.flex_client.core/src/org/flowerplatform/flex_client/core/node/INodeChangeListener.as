package org.flowerplatform.flex_client.core.node {
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;

	public interface INodeChangeListener {
		
		function nodeRemoved(node:Node):void;
		
		function nodeAdded(node:Node):void;
		
		function nodeUpdated(node:Node, property:String, oldValue:Object, newValue:Object):void;
		
	}
}