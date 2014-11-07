package org.flowerplatform.flexutil.properties.editor {
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import spark.components.CheckBox;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.properties.PropertyEntry;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class BooleanPropertyEditor extends CheckBox implements IPropertyEditor {
		
		public function BooleanPropertyEditor() {
			super();
			addEventListener(MouseEvent.CLICK, changeHandler);
		}
		
		public function set propertyEntry(entry:PropertyEntry):void {
			selected = Boolean(entry.value);
			enabled = focusEnabled = !entry.descriptor.isReadOnlyDependingOnMode(Utils.getPropertySafe(entry.context, FlexUtilConstants.PROPERTIES_CONTEXT_IS_CREATE_MODE));
		}
		
		public function get valueToCommit():Object {
			return selected;
		}
		
		protected function changeHandler(event:MouseEvent):void {
			dispatchEvent(new Event(FlexUtilConstants.EVENT_COMMIT_PROPERTY));
		}
		
	}
}

