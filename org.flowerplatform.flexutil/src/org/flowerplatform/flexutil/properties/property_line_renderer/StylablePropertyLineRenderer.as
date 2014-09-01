package org.flowerplatform.flexutil.properties.property_line_renderer {
	import spark.components.CheckBox;
	
	/**
	 * @author Diana Balutoiu
	 * @author Cristina Constantinescu
	 */
	public class StylablePropertyLineRenderer extends PropertyLineRenderer {
		
		protected var changedCheckBox:CheckBox;
		
		override protected function createChildren():void {			
			//TODO: needs node uri
		}		
		
		override public function nodeObjectUpdated():void {
			super.nodeObjectUpdated();
			
			//TODO: needs node property
		}
		
		override protected function prepareCommit(propertyValueOrWrapper:Object, newPropertyValue:Object):Object {
			var newPropertyValueOrWrapper:Object = super.prepareCommit(propertyValueOrWrapper, newPropertyValue);
			
			//TODO: add .core to buildPath?
			//StylePropertyWrapper(newPropertyValueOrWrapper).isDefault = false;
			
			return newPropertyValueOrWrapper;
		}
	}
}