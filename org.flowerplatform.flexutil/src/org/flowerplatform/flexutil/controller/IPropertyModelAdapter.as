package org.flowerplatform.flexutil.controller {
	
	/**
	 * @author Diana Balutoiu
	 */
	public interface IPropertyModelAdapter {
		
		function getProperties(model:Object):Object;
		function getPropertyValue(model:Object, property:String):Object;
	}
}