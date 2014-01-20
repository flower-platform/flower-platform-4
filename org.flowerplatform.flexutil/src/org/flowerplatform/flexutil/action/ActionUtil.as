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
	import flash.utils.Dictionary;
	
	import mx.collections.IList;

	/**
	 * @author Cristian Spiescu
	 */
	public class ActionUtil {

		/**
		 * Selects all the visible actions from the <code>actions</code> list, for a give
		 * <code>parentActionId</code> (or level, which can be <code>null</code> for the root level). For each
		 * visible action, the callback is invoked (which probably does UI related stuff).
		 * 
		 * @author Cristian Spiescu
		 * @author Cristina Constantinescu
		 */
		public static function processAndIterateActions(parentActionId:String, actions:Vector.<IAction>, selection:IList, context:Object, forEachCallbackObject:Object, forEachCallbackFunction:Function):void {
			if (actions == null) {
				return;
			}
			var actionsForCurrentParentActionId:Vector.<IAction>;
			// process the main list of subactions; i.e. display the first level of actions
			var parentActionIdToActions:Dictionary = new Dictionary();
			for (var i:int = 0; i < actions.length; i++) {
				var action:IAction = actions[i];
				actionsForCurrentParentActionId = parentActionIdToActions[action.parentId];
				if (actionsForCurrentParentActionId == null) {
					actionsForCurrentParentActionId = new Vector.<IAction>();
					parentActionIdToActions[action.parentId] = actionsForCurrentParentActionId;
				}
				actionsForCurrentParentActionId.push(action);
			}
			actionsForCurrentParentActionId = parentActionIdToActions[parentActionId];	
			if (actionsForCurrentParentActionId == null) {
				return;
			}
			
			// order actions
			var array:Array = new Array(actionsForCurrentParentActionId.length);
			for (i = 0; i < actionsForCurrentParentActionId.length; i++) {
				array[i] = actionsForCurrentParentActionId[i];
			}			
			array.sortOn("orderIndex", Array.NUMERIC);
			actionsForCurrentParentActionId = new Vector.<IAction>();
			for (i = 0; i < array.length; i++) {
				actionsForCurrentParentActionId.push(array[i]);
			}	
			
			for (i = 0; i < actionsForCurrentParentActionId.length; i++) {
				action = actionsForCurrentParentActionId[i];
				
				setChildActionsForActionIfNecessary(parentActionIdToActions, action);
				
				try {
					action.selection = selection;
					action.context = context;
					if (action.visible) {
						forEachCallbackFunction.call(forEachCallbackObject, action);
					}
				} finally {
					action.selection = null;
					action.context = null;
				}
			}

		}
		
		/**
		 * Set <code>childActions</code> recursively, for each <code>ComposedAction</code> found.
		 *
		 * Note: this way, multi level actions visibility is calculated correctly.
		 * @author Cristina Constantinescu
		 */
		private static function setChildActionsForActionIfNecessary(parentActionIdToActions:Dictionary, action:IAction):void {
			if (!isComposedAction(action)) {
				return;
			}
			IComposedAction(action).childActions = parentActionIdToActions[action.id];
			if (IComposedAction(action).childActions != null) {
				for (var i:int = 0; i < IComposedAction(action).childActions.length; i++) {					
					setChildActionsForActionIfNecessary(parentActionIdToActions, IComposedAction(action).childActions[i]);					
				}
			}
		}
		
		public static function isComposedAction(action:IAction):Boolean {
			return action is IComposedAction && !IComposedAction(action).actAsNormalAction;
		}
	}
}
