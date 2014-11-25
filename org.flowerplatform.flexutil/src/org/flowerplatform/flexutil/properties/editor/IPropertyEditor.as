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
	import org.flowerplatform.flexutil.properties.PropertyEntry;

	/**
	 * A property editor should always have prepared the value to commit, which may
	 * be called by the <code>PropertyEntryRenderer</code> (e.g. on focus out).
	 * 
	 * <p>
	 * The component may dispatch an event: <code>FlexUtilConstants.EVENT_COMMIT_PROPERTY</code>
	 * (e.g. on click enter).
	 * 
	 * @author Cristian Spiescu
	 */
	public interface IPropertyEditor {
		function get valueToCommit():Object;
		function set propertyEntry(entry:PropertyEntry):void;
	}
}