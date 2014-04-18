package org.flowerplatform.flex_client.core.node.remote {
	
	import flash.utils.Dictionary;

	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.node.remote.ServiceContext")]
	public class ServiceContext {
		
		public var context:Object = new Object(); /* as a dictionary -> context[key] = value; key MUST be String */
		
		public function add(key:String, value:Object):ServiceContext {
			context[key] = value;
			return this;
		}
		
	}
}