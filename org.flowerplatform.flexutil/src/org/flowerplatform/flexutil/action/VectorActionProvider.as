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
	import flash.events.EventDispatcher;
	
	import mx.collections.IList;
	import mx.events.PropertyChangeEvent;
	
	/**
	 * Simple action provider that contains a vector.
	 * 
	 * @author Mircea negreanu
	 * @author Cristina Constantinescu
	 */
	public class VectorActionProvider extends EventDispatcher implements IActionProvider {
		private var _actions:Vector.<IAction>;
		
		public function VectorActionProvider() {
			_actions = new Vector.<IAction>();
		}

		public function getActions(selection:IList):Vector.<IAction> {
			return _actions;
		}
		
		public function addAction(action:IAction):void {
			var oldActions:Vector.<IAction> = _actions;
			
			_actions.push(action);
			
			dispatchEvent(PropertyChangeEvent.createUpdateEvent(this, "_actions", oldActions, _actions));
		}
		
	}
}