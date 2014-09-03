package org.flowerplatform.flexutil.controller {
	
	
	/**
	 * @author Diana Balutoiu
	 */
	public interface IPropertyModelAdapter {
		
		function getProperties(model:Object):Object;
		function getPropertyValue(model:Object, property:String):Object;
		function getPropertyValueOrWrapper(model:Object,property:String):*;
		function commitPropertyValue(model:Object,propertyValueOrWrapper:Object, value:Object,propertyDescriptorName:String, callbackHandler:Function = null):void;
	}
}