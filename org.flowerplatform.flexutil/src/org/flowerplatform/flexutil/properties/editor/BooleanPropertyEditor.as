package org.flowerplatform.flexutil.properties.editor {
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.core.IDataRenderer;
	
	import spark.components.CheckBox;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class BooleanPropertyEditor extends CheckBox implements IDataRenderer {
		
		private var _data:Boolean;
		
		public function get data():Object {
			return _data;
		}
		
		public function set data(value:Object):void {
			_data = Boolean(value);
			selected = _data;
		}
		
		public function BooleanPropertyEditor() {
			super();
			addEventListener(MouseEvent.CLICK, changeHandler);
		}
		
		protected function changeHandler(event:MouseEvent):void {
			_data = selected;
			dispatchEvent(new Event(FlexUtilConstants.EVENT_COMMIT_PROPERTY));
		}
		
	}
}

