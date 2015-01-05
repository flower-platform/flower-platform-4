package org.flowerplatform.flexutil.properties.editor {
	import flash.events.Event;
	
	import mx.events.FlexEvent;
	
	import spark.components.TextInput;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.properties.PropertyEntry;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class StringPropertyEditor extends TextInput implements IPropertyEditor {
		
		public function StringPropertyEditor() {
			super();
			addEventListener(FlexEvent.ENTER, enterHandler);
		}
		
		public function set propertyEntry(entry:PropertyEntry):void {
			text = entry.value as String;
			focusEnabled = !entry.descriptor.isReadOnlyDependingOnMode(Utils.getPropertySafe(entry.context, FlexUtilConstants.PROPERTIES_CONTEXT_IS_CREATE_MODE));
			Utils.makePseudoDisabled(this, !focusEnabled);
		}
		
		public function get valueToCommit():Object {
			return text;
		}
		
		protected function enterHandler(event:FlexEvent):void {
			dispatchEvent(new Event(FlexUtilConstants.EVENT_COMMIT_PROPERTY));
		}

	}
}