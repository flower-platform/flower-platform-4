package org.flowerplatform.flexutil.samples.properties {
	import mx.utils.ObjectProxy;

	/**
	 * @author Cristian Spiescu
	 */
	[Bindable]
	public class SamplePropertiesModel {
		public var text:String;
		
		public var properties:ObjectProxy = new ObjectProxy({ 
			dynamicProperty1: "dynamicProperty1", 
			dynamicProperty2: "dynamicProperty2",
			hasGroupWithoutGroupDescriptor1: "value"
		});
		
		public function toString():String {
			return text;
		}
	}
}