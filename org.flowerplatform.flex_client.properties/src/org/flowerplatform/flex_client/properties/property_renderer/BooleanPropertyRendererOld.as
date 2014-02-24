package org.flowerplatform.flex_client.properties.property_renderer {
	import flash.events.FocusEvent;
	
	import mx.binding.utils.BindingUtils;
	
	import spark.components.CheckBox;
	import spark.components.HGroup;

	/**
	 * @author Razvan Tache
	 */
	public class BooleanPropertyRendererOld extends BasicPropertyRenderer {
		
		[Bindable]
		public var checkBox:CheckBox;
		
//		public var checkBoxContainer:HGroup;
		
		public function BooleanPropertyRendererOld() {
			super();	
		}
		
		override public function set data(value:Object):void {
			if (value == null) {
				return;
			}
			super.data = value;
			if (data.value == null) {
				checkBox.selected = false;
			} else {
				checkBox.selected = data.value;
			}
			checkBox.enabled = !data.readOnly;
			
//			if (!data.readOnly) {
//				BindingUtils.bindProperty(data, "value", checkBox, "selected");
//				handleListeningOnEvent(FocusEvent.FOCUS_OUT, this, checkBox);
//			}
		}
		
//		override protected function createChildren():void {
//			super.data = data;
//			super.createChildren();
//
//			checkBox = new CheckBox();
//			checkBoxContainer =  new HGroup();
//			
//			checkBoxContainer.percentHeight = 100;
//			checkBoxContainer.percentWidth = 100;
//			checkBoxContainer.horizontalAlign = "left";
//			checkBoxContainer.verticalAlign = "middle";
//			checkBoxContainer.paddingLeft = 5;
//			
//			checkBoxContainer.addElement(checkBox);
//			addElement(checkBoxContainer);
//		}
	}
}