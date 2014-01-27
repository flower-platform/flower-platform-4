package org.flowerplatform.flex_client.properties.property_renderer {
	import flash.events.FocusEvent;
	
	import mx.binding.utils.BindingUtils;
	
	import spark.components.TextInput;

	/**
	 * @author Razvan Tache
	 */
	public class StringPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		public var propertyValue:TextInput;
		
		public function StringPropertyRenderer() {
			super();
		}
		
		override public function set data(value:Object):void {
			if (value == null) {
				return;
			}
			super.data = value;
			propertyValue.text = data.value;
			propertyValue.editable = !data.readOnly;
			
			if (!data.readOnly) {
				BindingUtils.bindProperty( data, "value", propertyValue, "text" );
				handleListeningOnEvent(FocusEvent.FOCUS_OUT, this, propertyValue);
				propertyValue.addEventListener(FocusEvent.FOCUS_IN, function(event:FocusEvent):void {
					trace("focus in");
				});
			}
			
		}
		
		override protected function createChildren():void {
			super.data = data;
			super.createChildren();
					
			propertyValue = new TextInput();
			propertyValue.percentWidth = 100;
			propertyValue.percentHeight = 100;		

			addElement(propertyValue);
		}
	}
}