package org.flowerplatform.flex_client.core.editor.remote.update {
	import org.flowerplatform.flex_client.core.editor.remote.Node;
		
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.node.update.remote.ChildrenUpdate")]
	public class ChildrenUpdate extends Update {
		
		public var type:String;
		
		public var targetNode:Node;
		
		public var fullTargetNodeAddedBeforeId:String;

	}
}