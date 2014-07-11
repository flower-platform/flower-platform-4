/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
	
	import flash.events.MouseEvent;
	
	import spark.components.Button;
	import spark.components.CheckBox;
	
	import org.flowerplatform.flex_client.core.editor.remote.PreferencePropertyWrapper;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class PreferenceUserPropertyLineRenderer extends PreferencePropertyLineRenderer {
		
		protected var usedCheckBox:CheckBox;
		
		protected var copyToGlobal:Button;
				
		override protected function createChildren():void {			
			usedCheckBox = new CheckBox();
			usedCheckBox.label = Resources.getMessage("used");
			usedCheckBox.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {				
				if (!usedCheckBox.selected) {					
					unsetPreference();
				} else {
					setPreference();
				}	
			});
			
			copyToGlobal = new Button();
			copyToGlobal.toolTip = Resources.getMessage("preference.copy.to.global");
			copyToGlobal.setStyle("icon", Resources.propertiesIcon);
			copyToGlobal.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {setPreference(globalPropertyName)});
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				copyToGlobal.width = 22;
				copyToGlobal.height = 22
			}
			
			super.createChildren();
			
			rendererArea.addElement(usedCheckBox);		
			rendererArea.addElement(copyToGlobal);					
		}		
	
		override public function nodeUpdated():void {
			super.nodeUpdated();
			if (usedCheckBox) {
				usedCheckBox.selected = PreferencePropertyWrapper(node.getPropertyValueOrWrapper(propertyDescriptor.name)).isUsed;
			}
		}
			
	}
}