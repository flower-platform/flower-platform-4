/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.flex_client.properties.property_line_renderer {
	
	import mx.utils.ObjectUtil;
	
	import spark.primitives.BitmapImage;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.PreferencePropertyWrapper;
	import org.flowerplatform.flex_client.properties.PropertiesConstants;
	import org.flowerplatform.flex_client.resources.Resources;
	
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
				[node.nodeUri, property == null ? propertyDescriptor.name : property, wrapper]);
		}
		
		protected function unsetPreference(property:String = null):void {			
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.unsetProperty", 
				[node.nodeUri, property == null ? propertyDescriptor.name : property]);
		}

	}
}