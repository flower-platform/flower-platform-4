/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexutil.selection {
	import mx.collections.IList;

	/**
	 * Should be implemented by the <code>IViewContent</code>s that have
	 * a selection.
	 * 
	 * <p>
	 * The VC should listen for selection change. When it happens => should call
	 * <code>FlexUtilGlobals.getInstance().selectionManager.selectionChanged(this)</code>.
	 * 
	 * @author Cristian Spiescu
	 */
	public interface ISelectionProvider {
		function getSelection():IList;
	}
}