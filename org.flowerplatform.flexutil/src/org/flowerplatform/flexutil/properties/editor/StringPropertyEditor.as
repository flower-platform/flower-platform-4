/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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