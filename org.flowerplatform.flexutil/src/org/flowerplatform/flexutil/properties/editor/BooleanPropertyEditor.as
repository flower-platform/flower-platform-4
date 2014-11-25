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

