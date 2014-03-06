package org.flowerplatform.flex_client.properties.property_renderer {
	import flash.events.MouseEvent;
	
	import mx.binding.utils.BindingUtils;
	
	import spark.components.Button;
	import spark.components.CheckBox;
	import spark.components.Label;
	import spark.components.TextInput;
	
	import org.flowerplatform.flex_client.properties.remote.PropertyDescriptor;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;

	public class StringWithButtonPropertyRenderer extends BasicPropertyRenderer implements IDialogResultHandler {
		
		[Bindable]
		public var propertyValue:TextInput;
		
		[Bindable]
		public var changeCheckBox:CheckBox;
		[Bindable]
		public var changeLabel:Label;
		
		public var button:Button;
		
		public var clickHandler:Function;
		
		/**
		 * Signature: function getNewPropertyValueHandler(dialogResult:Object):String
		 * @author Cristina Constantinescu
		 */ 
		public var getNewPropertyValueHandler:Function;
		
		public function StringWithButtonPropertyRenderer() {
			super();
		}
		
		override public function set data(value:Object):void {
			super.data = value;
			propertyValue.text = data.value;
			propertyValue.editable = !data.readOnly;
			
			if (!data.readOnly) {
				BindingUtils.bindProperty( data, "value", propertyValue, "text" );
			}
			
			changeCheckBox.visible = PropertyDescriptor(data).hasChangeCheckbox;
			changeCheckBox.enabled = changeCheckBox.selected;
			changeLabel.visible = PropertyDescriptor(data).hasChangeCheckbox;
		}
		
		override protected function createChildren():void {
			super.data = data;
			super.createChildren();
			
			propertyValue = new TextInput();
			button = new Button();
			
			propertyValue.percentWidth = 100;
			propertyValue.percentHeight = 100;		

			button.percentHeight = 100;
			button.label = "...";
			button.addEventListener(MouseEvent.CLICK, clickHandlerInternal);
			
			addElement(propertyValue);
			addElement(button);
			
			changeCheckBox = new CheckBox();
			addElement(changeCheckBox);
			changeLabel = new Label();
			changeLabel.text = "Change";
			addElement(changeLabel);
			
		}
		
		private function clickHandlerInternal(event:MouseEvent):void {
			clickHandler(this, data.name, data.value);
		}
		
		public function handleDialogResult(result:Object):void {
			// set new value after closing dialog
			propertyValue.text = getNewPropertyValueHandler(result);
			saveProperty(null);			
		}
		
		
	}
}