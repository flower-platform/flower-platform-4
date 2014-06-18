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
package org.flowerplatform.flex_client.properties.property_renderer {
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import mx.binding.utils.BindingUtils;
	
	import spark.components.CheckBox;
	import spark.components.DataRenderer;
	import spark.layouts.HorizontalLayout;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flex_client.resources.Resources;

	/**
	 * @author Razvan Tache
	 * @author Cristina Constantinescu
	 * @author Sebastian Solomon
	 */
	public class BasicPropertyRenderer extends DataRenderer {
		
		protected var oldValue:Object;
		public var defaultValue:Object;
		
		public var savePropertyEnabled:Boolean = true;
		
		public var changeCheckBox:CheckBox = new CheckBox();
					
		public function BasicPropertyRenderer() {
			super();
			layout = new HorizontalLayout;
			HorizontalLayout(layout).verticalAlign = "middle";
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			addElement(changeCheckBox);
			changeCheckBox.label = Resources.getMessage("change");

			changeCheckBox.addEventListener(Event.CHANGE, changeCheckboxClickHandler);
			BindingUtils.bindProperty(changeCheckBox, "enabled", changeCheckBox, "selected" );
		}
		
		override public function set data(value:Object):void {
			super.data = value;
			changeCheckBox.visible = propertyDescriptor.hasChangeCheckbox;
			changeCheckBox.includeInLayout = propertyDescriptor.hasChangeCheckbox;
			
			if (!propertyDescriptor.readOnly) {
				BindingUtils.bindSetter(updateCheckBox, propertyDescriptor, "value");
			}
		}
		
		private function changeCheckboxClickHandler(event:Event):void {
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.unsetProperty", 
				[Node(PropertiesPlugin.getInstance().currentSelection.getItemAt(0)).fullNodeId, propertyDescriptor.name]);
		}
		
		public function updateCheckBox(val:String):void {
			changeCheckBox.selected = !(propertyDescriptor.value == defaultValue || ((propertyDescriptor.value == null || propertyDescriptor.value == "")  && defaultValue == null));
		}
		
		protected function validPropertyValue():Boolean {
			return true;
		}
		
		protected function saveProperty():void {			
			if (savePropertyEnabled && !propertyDescriptor.readOnly) {				
				if (!validPropertyValue()) {					
					return;
				}
				if (oldValue != getValue()) {
					oldValue = getValue();
					CorePlugin.getInstance().serviceLocator.invoke(
						"nodeService.setProperty", 
						[Node(PropertiesPlugin.getInstance().currentSelection.getItemAt(0)).fullNodeId, propertyDescriptor.name, getValue()]);
				}
			}			
		}

		protected function getValue():Object {
			return propertyDescriptor.value;	
		}
		
		protected function get propertyDescriptor():PropertyDescriptor {
			return PropertyDescriptor(data);	
		}
		
		protected function keyDownHandler1(event:KeyboardEvent):void {
			if (event.keyCode == Keyboard.ENTER) {
				saveProperty();
			}
		}
		
	}
}