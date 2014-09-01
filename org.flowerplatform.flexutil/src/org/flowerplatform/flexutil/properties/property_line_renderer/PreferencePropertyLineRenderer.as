package org.flowerplatform.flexutil.properties.property_line_renderer {
	
	import spark.primitives.BitmapImage;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.properties.PropertiesConstants;
	
	/**
	 * @author Diana Balutoiu
	 * @author Cristina Constantinescu
	 */
	public class PreferencePropertyLineRenderer extends PropertyLineRenderer {
		
		[SkinPart]
		public var imageDisplay:BitmapImage;		
		
		public function PreferencePropertyLineRenderer() {
			super();
			
			setStyle("skinClass", PreferenceFormItemSkin);			
		}
		
		override protected function createChildren():void {				
			super.createChildren();
			rendererArea.gap = 5;	
		}	
		
		override protected function partAdded(partName:String, instance:Object) : void {
			super.partAdded(partName, instance);
			if (instance == imageDisplay) {
				imageDisplay.source = Resources.preferenceIcon;
			}			
		}
		
		protected function get globalPropertyName():String {
			return propertyDescriptor.name.substr(0, propertyDescriptor.name.lastIndexOf(".")) + PropertiesConstants.GLOBAL_SUFFIX;
		}
		
		protected function get userPropertyName():String {
			return propertyDescriptor.name.substr(0, propertyDescriptor.name.lastIndexOf(".")) + PropertiesConstants.USER_SUFFIX;
		}
		
		protected function get defaultPropertyName():String {
			return propertyDescriptor.name.substr(0, propertyDescriptor.name.lastIndexOf(".")) + PropertiesConstants.DEFAULT_SUFFIX;
		}
		
		protected function setPreference(property:String = null):void {	
			//TODO: needs nodeUri
		}
		
		protected function unsetPreference(property:String = null):void {			
			//TODO: needs nodeUri
		}
	}
}