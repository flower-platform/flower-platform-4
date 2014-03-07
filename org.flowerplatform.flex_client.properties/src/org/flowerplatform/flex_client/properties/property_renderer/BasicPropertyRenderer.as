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
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;

	/**
	 * @author Razvan Tache
	 * @author Cristina Constantinescu
	 * @author Sebastian Solomon
	 */
	public class BasicPropertyRenderer extends DataRenderer {
		
		protected var oldValue:Object;
		
		public var changeCheckBox:CheckBox = new CheckBox();
		
		public var changeLabel:Label = new Label();
		
		public function BasicPropertyRenderer() {
			super();
			layout = new HorizontalLayout;
		}
		
		override protected function createChildren():void {
			// TODO Auto Generated method stub
			super.createChildren();
			
			addElement(changeCheckBox);
			addElement(changeLabel)
			changeLabel.text = "Change";
			changeLabel.setStyle("paddingTop", 4);
		}
		
		override public function set data(value:Object):void {
			super.data = value;			
			changeCheckBox.visible = PropertyDescriptor(data).hasChangeCheckbox;
			changeCheckBox.includeInLayout = PropertyDescriptor(data).hasChangeCheckbox;
			changeLabel.includeInLayout = PropertyDescriptor(data).hasChangeCheckbox;
			changeLabel.visible = PropertyDescriptor(data).hasChangeCheckbox;
			
			if(PropertyDescriptor(data).hasChangeCheckbox) {
				changeCheckBox.addEventListener(Event.CHANGE, hangeCheckboxClickHandler);	
			}
				
			if (!data.readOnly) {
				BindingUtils.bindProperty( changeCheckBox, "enabled", changeCheckBox, "selected" );
				BindingUtils.bindSetter(updateCheckBox, data, "value")
			}
		}
		
		
		private function hangeCheckboxClickHandler(event:Event):void {
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.unsetProperty", 
				[Node(PropertiesPlugin.getInstance().currentSelection.getItemAt(0)).fullNodeId, data.name]);
		}
		public function updateCheckBox(val:String):void {
			changeCheckBox.selected = (data.value != data.defaultValue);
			changeLabel.enabled = (data.value != data.defaultValue);
		}
		
		protected function validPropertyValue():Boolean {
			return true;
		}
		
		protected function saveProperty():void {			
			if (!data.readOnly) {
				if (!validPropertyValue()) {					
					return;
				}
				var value:Object = getValue();
				if (oldValue != value) {
					oldValue = value;
					CorePlugin.getInstance().serviceLocator.invoke(
						"nodeService.setProperty", 
						[Node(PropertiesPlugin.getInstance().currentSelection.getItemAt(0)).fullNodeId, data.name, value]);
				}
			}
		}

		protected function getValue():Object {
			return PropertyDescriptor(data).value;	
		}
		
		protected function keyDownHandler1(event:KeyboardEvent):void {
			if (event.keyCode == Keyboard.ENTER) {
				saveProperty();
			}
		}
		
	}
}
