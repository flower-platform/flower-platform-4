package org.flowerplatform.flexutil.properties.editor {
	import flash.events.Event;
	
	import mx.core.IDataRenderer;
	import mx.events.FlexEvent;
	
	import spark.components.TextInput;
	import spark.events.TextOperationEvent;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class StringPropertyEditor extends TextInput implements IDataRenderer {
		
		private var _data:String;
		
		public function StringPropertyEditor() {
			super();
			addEventListener(TextOperationEvent.CHANGE, changeHandler);
			addEventListener(FlexEvent.ENTER, enterHandler);
		}
		
		protected function changeHandler(event:TextOperationEvent):void {
			_data = text;
		}
		
		protected function enterHandler(event:FlexEvent):void {
			dispatchEvent(new Event(FlexUtilConstants.EVENT_COMMIT_PROPERTY));
		}

		public function get data():Object {
			return _data;
		}

		public function set data(value:Object):void {
			_data = value as String;
			text = _data;
		}

	}
}