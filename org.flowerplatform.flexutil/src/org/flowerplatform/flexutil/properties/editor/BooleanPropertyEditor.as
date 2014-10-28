package org.flowerplatform.flexutil.properties.editor {
	import flash.events.MouseEvent;
	
	import mx.controls.CheckBox;
	import mx.core.IDataRenderer;
	import mx.events.FlexEvent;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class BooleanPropertyEditor extends CheckBox implements IDataRenderer {
		
		public function BooleanPropertyEditor() {
			super();
			addEventListener(MouseEvent.CLICK, changeHandler);
		}
		
		protected function changeHandler(event:MouseEvent):void {
			data = selected;
			dispatchEvent(new FlexEvent(FlexEvent.VALUE_COMMIT));
		}
		
	}
}

