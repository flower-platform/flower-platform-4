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
	 * @author Cristian Spiescu
	 */
	public class ClassFactoryActionProvider implements IActionProvider {
		
		public var actionClasses:Vector.<Class> = new Vector.<Class>();
		
		public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			for (var i:int = 0; i < actionClasses.length; i++) {
				var action:IAction = new actionClasses[i]();
				result.push(action);
			}
			return result;
		}
		
		public function addActionClass(actionClass:Class):ClassFactoryActionProvider {
			actionClasses.push(actionClass);
			return this;
		}
	}
}