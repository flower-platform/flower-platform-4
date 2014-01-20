/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.flexutil.action {
	import mx.collections.IList;
	
	/**
	 * Simple action provider that contains a vector.
	 * 
	 * @author Mircea negreanu
	 */
	public class VectorActionProvider implements IActionProvider {
		private var _actions:Vector.<IAction>;
		
		public function VectorActionProvider() {
			_actions = new Vector.<IAction>();
		}

		/**
		 * Does not process the vector in any way, so you can say
		 * getActions(null).push(action)
		 */
		public function getActions(selection:IList):Vector.<IAction> {
			return _actions;
		}
	}
}