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
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.StylePropertyWrapper;
	import org.flowerplatform.flex_client.resources.Resources;
	
	import spark.components.CheckBox;

	/**
	 * @author Cristina Constantinescu
	 */
	public class StylablePropertyLineRenderer extends PropertyLineRenderer {
		
		protected var changedCheckBox:CheckBox;
				
		override protected function createChildren():void {			
			changedCheckBox = new CheckBox();
			changedCheckBox.label = Resources.getMessage("change");
			changedCheckBox.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
				if (!changedCheckBox.selected) {
					CorePlugin.getInstance().serviceLocator.invoke(
						"nodeService.unsetProperty", 
						[node.nodeUri, propertyDescriptor.name]);
				}
			});
			
			super.createChildren();
			
			rendererArea.addElement(changedCheckBox);	
		}		
		
		override public function nodeUpdated():void {
			super.nodeUpdated();
			
			if (changedCheckBox != null) {				
				changedCheckBox.selected = !StylePropertyWrapper(node.getPropertyValueOrWrapper(propertyDescriptor.name)).isDefault;
			}
		}
		
		override protected function prepareCommit(propertyValueOrWrapper:Object, newPropertyValue:Object):Object {
			var newPropertyValueOrWrapper:Object = super.prepareCommit(propertyValueOrWrapper, newPropertyValue);
			
			StylePropertyWrapper(newPropertyValueOrWrapper).isDefault = false;
			
			return newPropertyValueOrWrapper;
		}
		
	}
}