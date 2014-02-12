package org.flowerplatform.flex_client.core.mindmap.remote.update {
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.node.update.remote.PropertyUpdate")]
	public class PropertyUpdate extends Update {
		
		public var key:String;
		
		public var value:Object;
		
		public var isUnset:Boolean;
		
	}
}