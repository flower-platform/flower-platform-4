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
package org.flowerplatform.flexutil.global_menu {
	import mx.collections.ArrayCollection;
	import mx.collections.ICollectionView;
	import mx.collections.IList;
	import mx.controls.menuClasses.IMenuDataDescriptor;
	
	import org.flowerplatform.flexutil.action.ActionUtil;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	
	/**
	 * IMenuDataDescriptor that know how to work with actions
	 * 
	 * <p>Assumptions:
	 * <ul>
	 * 	<li> only a IComposedAction can hava children.</li>
	 *  <li> if an IComposedAction is interogated it means it is visible
	 * (otherwise it would have been filtered out by the 
	 * ActionUtil.processAndIterateActions)</li>
	 * </ul></p>
	 * 
	 * @author Mircea Negreanu
	 */
	public class WebMenuDataDescriptor implements IMenuDataDescriptor {
		
		protected var _actionProvider:IActionProvider;
		
		protected var _selection:IList;
		
		/**
		 * List of actions returned by getChildren, so we know to remove the selection from them 
		 * when the menu closes
		 */
		protected var childrenActions:Vector.<IAction> = new Vector.<IAction>();
		
		public function WebMenuDataDescriptor(ap:IActionProvider = null, sel:IList = null) {
			super();
			
			_actionProvider = ap;
			_selection = sel;
		}
		
		public function set actionProvider(ap:IActionProvider):void {
			_actionProvider = ap;	
		}
		
		public function get actionProvider():IActionProvider {
			return _actionProvider;
		}
		
		public function set selection(sel:IList):void {
			_selection = sel;
		}
		
		public function get selection():IList {
			return _selection;
		}
		
		/**
		 * Gets the id from the node and returns the list of children as an ArrayCollection.
		 * <p>Also adds the selection to the action so that the label, icon, enabled are correctly
		 * set</p>
		 */
		public function getChildren(node:Object, model:Object=null):ICollectionView {
			var id:String = null;
			if (node is IAction) {
				id = node.id;
			}
			
			var children:ArrayCollection = new ArrayCollection();
			
			ActionUtil.processAndIterateActions(id, 
				actionProvider.getActions(selection), 
				selection, 
				null, 
				this, 
				function(action:IAction):void {
					children.addItem(action);
					childrenActions.push(action);
				}
			);

			// add the selection so we can have the correct labels/enabled/icon
			for each (var action:IAction in children) {
				action.selection = selection;
			}
			
			return children;
		}
		
		/**
		 * Gets the id from node and checks if the associated action is IComposedActions
		 * 
		 * @return true if the action is IComposedAction, false otherwise
		 */
		public function hasChildren(node:Object, model:Object=null):Boolean {
			if (node is IAction) {
				return (ActionUtil.isComposedAction(IAction(node)));
			}
			return false;
		}
		
		public function getData(node:Object, model:Object=null):Object {
			return Object(node);
		}
		
		/**
		 * @return true if the action is IComposedAction, false otherwise
		 */
		public function isBranch(node:Object, model:Object=null):Boolean {
			if (node is IAction) {
				return (ActionUtil.isComposedAction(IAction(node)));
			}
			return false;
		}
		
		/**
		 * Normal menu (other possible types: separator, check or radio)
		 */
		public function getType(node:Object):String {
			return "normal";
		}
		
		public function isEnabled(node:Object):Boolean {
			if (node is IAction) {
				return IAction(node).enabled;
			}
			
			return false;
		}
		
		/**
		 * When the menu closes, this is used to purge the selection from 
		 * created children.
		 */
		public function clearSelectionFromChildren():void {
			for each (var action:IAction in childrenActions) {
				action.selection = null;
			}
			
			// prepare a new vector.
			childrenActions = new Vector.<IAction>();
		}
		
		// not interested in this -> set them to default
		
		public function addChildAt(parent:Object, newChild:Object, index:int, model:Object=null):Boolean {
			return false;
		}
		
		public function removeChildAt(parent:Object, child:Object, index:int, model:Object=null):Boolean {
			return false;
		}
		
		public function setEnabled(node:Object, value:Boolean):void {
		}
		
		public function isToggled(node:Object):Boolean {
			return false;
		}
		
		public function setToggled(node:Object, value:Boolean):void {
		}
		
		public function getGroupName(node:Object):String {
			return null;
		}
	}
}