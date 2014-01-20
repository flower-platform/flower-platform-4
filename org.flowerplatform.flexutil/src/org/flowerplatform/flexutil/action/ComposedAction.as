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

	/**
	 * @see IComposedAction
	 * @author Cristian Spiescu
	 */
	public class ComposedAction extends ActionBase implements IComposedAction {

		private var _childActions:Vector.<IAction>;

		private var _actAsNormalAction:Boolean;

		public function get childActions():Vector.<IAction> {
			return _childActions;
		}

		public function set childActions(value:Vector.<IAction>):void {
			_childActions = value;
		}
		
		override public function get visible():Boolean {
			if (actAsNormalAction) {
				return super.visible;
			}
			if (childActions == null) {
				return false;
			} else {
				for (var i:int = 0; i < childActions.length; i++) {
					var childAction:IAction = childActions[i];
					childAction.selection = selection;
					childAction.context = context;
					try {
						if (childAction.visible) {
							// at least one visible => the composed action is visible
							return true;
						}
					} finally {
						childAction.selection = null;
						childAction.context = null;
					}
				}
				return false;
			}
		}
		
		public function get actAsNormalAction():Boolean {
			return _actAsNormalAction;
		}
		
		public function set actAsNormalAction(value:Boolean):void {
			_actAsNormalAction = value;
		}
		
	}
}