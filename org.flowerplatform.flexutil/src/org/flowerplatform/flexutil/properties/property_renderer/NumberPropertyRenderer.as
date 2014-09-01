package org.flowerplatform.flexutil.properties.property_renderer {
	import mx.events.FlexEvent;
	import mx.events.ValidationResultEvent;
	import mx.validators.ValidationResult;
	
	import spark.validators.NumberValidator;
	
	import flashx.textLayout.formats.TextAlign;
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Balutoiu Diana
	 * @author Cristina Constantinescu
	 * @author Mariana Gheorghe
	 */
	public class NumberPropertyRenderer extends StringPropertyRenderer {
		
		private var numberValidator:NumberValidator;
		
		public function NumberPropertyRenderer() {
			super();
			
			addEventListener(FlexEvent.CREATION_COMPLETE, 
				function(event:FlexEvent):void {
					numberValidator = new NumberValidator();				
					restrict = "0-9\\-";
					setStyle("textAlign", TextAlign.RIGHT);
				}
			);
		}
		
		override public function isValidValue():Boolean {	
			var value:Object = valueToCommit;
			if (value == null) {
				return true;
			}
			if (value != 0) { // done to alow '0', see: https://forums.adobe.com/thread/1038745
				var validationResultEvent:ValidationResultEvent = numberValidator.validate(value);			
				if (validationResultEvent.type == ValidationResultEvent.INVALID) {	
					// show validation error to client
					FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setTitle(Resources.getMessage("properties.view"))
						.setText(ValidationResult(validationResultEvent.results[0]).errorMessage)
						.setWidth(300)
						.setHeight(200)					
						.showMessageBox();					
					return false;
				}
			}
			return true;
		}
		
		override public function get valueToCommit():Object {
			var value:Object = super.valueToCommit;
			if (value == "") {
				return null;
			}
			return new Number(value);
		}
	}
}