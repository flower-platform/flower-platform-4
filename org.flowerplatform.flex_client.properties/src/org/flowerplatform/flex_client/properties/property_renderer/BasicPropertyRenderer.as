package org.flowerplatform.flex_client.properties.property_renderer {
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import mx.binding.utils.BindingUtils;
	
	import spark.components.CheckBox;
	import spark.components.DataRenderer;
	import spark.components.Label;
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
		
		public var changeCheckBox:CheckBox = new CheckBox();
		
		public var changeLabel:Label = new Label();
		
		public var savePropertyEnabled:Boolean = false;
		
		public var defaultValue:Object;
		
		public function BasicPropertyRenderer() {
			super();
			layout = new HorizontalLayout;
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			addElement(changeCheckBox);
			addElement(changeLabel)
			changeLabel.text = Resources.getMessage("change");
			changeLabel.setStyle("paddingTop", 4);
		}
		
		override public function set data(value:Object):void {
			super.data = value;
			changeCheckBox.visible = PropertyDescriptor(data).hasChangeCheckbox;
			changeCheckBox.includeInLayout = PropertyDescriptor(data).hasChangeCheckbox;
			changeLabel.visible = PropertyDescriptor(data).hasChangeCheckbox;
			changeLabel.includeInLayout = PropertyDescriptor(data).hasChangeCheckbox;
			
			if(PropertyDescriptor(data).hasChangeCheckbox) {
				changeCheckBox.addEventListener(Event.CHANGE, changeCheckboxClickHandler);	
			}
				
			if (!data.readOnly) {
				BindingUtils.bindProperty(changeCheckBox, "enabled", changeCheckBox, "selected" );
				BindingUtils.bindSetter(updateCheckBox, data, "value")
			}
		}
		
		
		private function changeCheckboxClickHandler(event:Event):void {
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.unsetProperty", 
				[Node(PropertiesPlugin.getInstance().currentSelection.getItemAt(0)).fullNodeId, data.name]);
		}
		
		public function updateCheckBox(val:String):void {
			changeCheckBox.selected = !(data.value == defaultValue || ((data.value == null || data.value == "")  && defaultValue == null));
		}
		
		protected function validPropertyValue():Boolean {
			return true;
		}
		
		protected function saveProperty():void {			
			if (!savePropertyEnabled) {
				if (!data.readOnly) {
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
