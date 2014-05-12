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
package org.flowerplatform.flexutil.action {

	/**
	 * @see IComposedAction
	 * @author Cristian Spiescu
	 */
	public class ComposedAction extends ActionBase implements IComposedAction {

		private var _childActions:Vector.<IAction>;
		private var _actAsNormalAction:Boolean;
		private var _delegateIfSingleChild:Boolean;
		
		public function get childActions():Vector.<IAction> {
			return _childActions;
		}

		public function set childActions(value:Vector.<IAction>):void {
			_childActions = value;
		}
		
		public function get actAsNormalAction():Boolean {
			return _actAsNormalAction;
		}
		
		public function set actAsNormalAction(value:Boolean):void {
			_actAsNormalAction = value;
		}
		
		public function get delegateIfSingleChild():Boolean {
			return _delegateIfSingleChild;
		}
		
		public function set delegateIfSingleChild(value:Boolean):void {
			_delegateIfSingleChild = value;
		}
		
		override public function get visible():Boolean {
			if (actAsNormalAction) {
				return super.visible;
			}			
			if (childActions == null) {
				return false;
			} 
			
			var visibleChildActions:Array = getVisibleChildActions();
			if (visibleChildActions.length >= 1) {
				if (delegateIfSingleChild && visibleChildActions.length == 1) {
					label = label + " " + visibleChildActions[0].label;
					id = visibleChildActions[0].id;
					actAsNormalAction = true;	
				}
				return true;
			}
			return false;			
		}	
		
		override public function run():void {			
			if (delegateIfSingleChild) {
				var visibleChildActions:Array = getVisibleChildActions();
				if (visibleChildActions.length == 1) {
					var singleVisibleChildAction:IAction = visibleChildActions[0];
					try {
						singleVisibleChildAction.selection = selection;
						singleVisibleChildAction.context = context;
						singleVisibleChildAction.run();
					} finally {										
						singleVisibleChildAction.selection = null;
						singleVisibleChildAction.context = null;
					}
					return;
				}				
			}
			super.run();						
		}
		
		private function getVisibleChildActions():Array {			
			var visibleChildActions:Array = [];
			for (var i:int = 0; i < childActions.length; i++) {
				var childAction:IAction = childActions[i];
				childAction.selection = selection;
				childAction.context = context;
				try {
					if (childAction.visible) {						
						visibleChildActions.push(childAction);
					}
				} finally {
					childAction.selection = null;
					childAction.context = null;
				}
			}			
			return visibleChildActions;
		}
		
	}
}