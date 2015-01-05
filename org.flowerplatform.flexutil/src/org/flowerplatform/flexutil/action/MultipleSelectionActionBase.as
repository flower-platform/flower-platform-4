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
package org.flowerplatform.flexutil.action {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * Useful base class for actions that are enabled on a multiple selection.
	 * Subclasses should implement <code>isVisibleForSelectedElement()</code>.
	 * 
	 * @see #isVisible()
	 * @see #isVisibleForSelectedElement()
	 * 
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class MultipleSelectionActionBase extends ActionBase	{
		
		/**
		 * Iterates over the selected elements and invokes <code>isVisibleForSelectedElement()</code>.
		 * If the selection doesn't contain elements, returns <code>false</code>.
		 */
		override public function get visible():Boolean {	
			if (selection.length == 0) {
				return false;
			}
			
			for (var i:int = 0; i < selection.length; i++) {
				if (!isVisibleForSelectedElement(selection.getItemAt(i))) {
					return false;
				}
			}
			return true;
		}
		
		/**
		 * Should return <code>true</code> if the current element is selected and
		 * <code>false</code> otherwise. It is called once for every element from
		 * the selection. A single <code>false</code> value among these has a veto role, i.e.
		 * the action is not visible.
		 * 
		 * <p>
		 * By default, returns <code>true</code>.
		 */
		protected function isVisibleForSelectedElement(element:Object):Boolean {
			return true;
		}
		
	}
}