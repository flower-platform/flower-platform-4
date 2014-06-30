package org.flowerplatform.flex_client.properties.property_line_renderer {
	
	import flash.events.MouseEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.PreferencePropertyWrapper;
	import org.flowerplatform.flex_client.properties.PropertiesConstants;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flex_client.resources.Resources;
	
	import spark.components.HGroup;
	import spark.components.Image;
	import spark.primitives.BitmapImage;
	import spark.skins.spark.FormItemSkin;
	
	/**
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
			var wrapper:PreferencePropertyWrapper = new PreferencePropertyWrapper();
			wrapper.value = node.getPropertyValue(propertyDescriptor.name);
			wrapper.isUsed = false;
			
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.setProperty", 
				[node.nodeUri, property == null ? propertyDescriptor.name : property, wrapper], refreshPreferences);
		}
		
		protected function unsetPreference(property:String = null):void {			
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.unsetProperty", 
				[node.nodeUri, property == null ? propertyDescriptor.name : property], refreshPreferences);
		}
		
		protected function refreshPreferences(obj:Object):void {
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.getNode", [node.nodeUri], 
				function(returnedNode:Node):void {
					node.properties = returnedNode.properties;
			});
		}

	}
}