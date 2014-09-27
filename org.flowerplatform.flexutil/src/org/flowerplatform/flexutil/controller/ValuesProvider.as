package org.flowerplatform.flexutil.controller {
	import flash.events.IEventDispatcher;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ValuesProvider extends AbstractController {
		
		public var featurePrefix:String = "";
		
		public function ValuesProvider(orderIndex:int=0) {
			super(orderIndex);
		}
		
		public function getFeature(typeDescriptorRegistry:TypeDescriptorRegistry, object:IEventDispatcher, key:String):String {
			return featurePrefix + key;
		}
		
		public function getPropertyName(typeDescriptorRegistry:TypeDescriptorRegistry, object:IEventDispatcher, key:String):String {
			var feature:String = getFeature(typeDescriptorRegistry, object, key);
			var singleValueDescriptor:SingleValueDescriptor = SingleValueDescriptor(typeDescriptorRegistry.getExpectedTypeDescriptor(typeDescriptorRegistry.typeProvider.getType(object)).getSingleController(feature, object));
			if (singleValueDescriptor == null) {
				return null;
			} else {
				return String(singleValueDescriptor.value);				
			}
		}
		
		public function getActualObject(object:IEventDispatcher):IEventDispatcher {
			return object;
		}
		
		public function getValue(typeDescriptorRegistry:TypeDescriptorRegistry, object:IEventDispatcher, key:String):Object {
			var propertyName:String = getPropertyName(typeDescriptorRegistry, object, key);
			if (propertyName == null) {
				return null;
			} else {
				return getActualObject(object)[propertyName];				
			}
		}
		
	}
}