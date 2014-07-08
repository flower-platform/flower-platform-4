package org.flowerplatform.flex_client.core.node {
	import org.flowerplatform.flex_client.core.editor.remote.Node;

	public interface INodeFactory {
		
		function createNode(sourceNode:Node):Node;
		
	}
}