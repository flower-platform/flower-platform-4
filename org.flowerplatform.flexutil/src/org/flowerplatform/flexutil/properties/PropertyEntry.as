package org.flowerplatform.flexutil.properties {
	import flash.events.IEventDispatcher;
	
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;

	/**
	 * Model for the lines in the properties form.
	 * 
	 * @author Cristian Spiescu
	 */
	public class PropertyEntry {
		public var typeDescriptorRegistry:TypeDescriptorRegistry;
		public var model:Object;
		public var eventDispatcher:IEventDispatcher;
		public var descriptor:PropertyDescriptor;
		public var value:Object;
		public var isGroup:Boolean;
		public var context:Object;
	}
}