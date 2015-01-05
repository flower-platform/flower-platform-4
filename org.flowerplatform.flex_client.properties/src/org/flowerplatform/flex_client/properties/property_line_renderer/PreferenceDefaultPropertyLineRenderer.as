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
	
	import flash.events.MouseEvent;
	import flash.sampler.getInvocationCount;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.PreferencePropertyWrapper;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	
	import spark.components.Button;
	import spark.components.CheckBox;
	import spark.components.Image;
	import spark.primitives.BitmapImage;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class PreferenceDefaultPropertyLineRenderer extends PreferencePropertyLineRenderer {
				
		protected var copyToUser:Button;
		protected var copyToGlobal:Button;
		
		override protected function createChildren():void {			
			copyToGlobal = new Button();
			copyToGlobal.toolTip = Resources.getMessage("preference.copy.to.global");
			copyToGlobal.setStyle("icon", Resources.propertiesIcon);
			copyToGlobal.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {setPreference(globalPropertyName)});
					
			copyToUser = new Button();
			copyToUser.toolTip = Resources.getMessage("preference.copy.to.user");
			copyToUser.setStyle("icon", Resources.propertiesIcon);
			copyToUser.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {setPreference(userPropertyName)});
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				copyToUser.width = copyToGlobal.width = 22;
				copyToUser.height = copyToGlobal.height = 22
			}
			super.createChildren()
				
			rendererArea.addElement(copyToGlobal);			
			rendererArea.addElement(copyToUser);				
		}		
	
	}
}