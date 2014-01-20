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
package com.crispico.flower.util.tree.checkboxtree {
	import flash.events.Event;
	
	import mx.controls.Tree;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.treeClasses.TreeListData;
	import mx.core.ClassFactory;
	import mx.core.mx_internal;
	import mx.events.TreeEvent;
	
	use namespace mx_internal;

	[Event(name="checkFieldChanged", type="flash.events.Event")]
	
	[Event(name="checkFunctionChanged", type="flash.events.Event")]
	
	[Event(name="itemCheck", type="mx.events.TreeEvent")]
	
	/**
	 * Customization of the default Flex Tree component.
	 * 
	 * <p>
	 * This component simply add a checkbox on every tree item.
	 * The checkbox allows 3 states: selected, unselected, partially selected.
	 * Then you can control the state of checkbox nodes within the code.
	 * 
	 * <p>
	 * Based on <code>_checkEnabled</code> value, the tree acts
	 * like a normal tree or like one with a check box.
	 * By default, the value is <code>false</code>.
	 * 
	 * @see http://www.sephiroth.it/file_detail.php?id=151#
	 * 
	 * @author Cristina
	 */
	public class CheckBoxTree extends Tree {
		
		public static const SELECTED:int = 1;
		
		public static const UNSELECTED:int = 0;
		
		public static const PARTIALLY_SELECTED:int = 2;
		
		public var propagateCheckedStatus:Boolean = true;
		
		protected var _checkField:String;
		
		protected var _childrenField:String;
		
		// Check function was disabled for the moment because the updateChildren/updateParents
		// functionality was moved from GenericTree here and it was a little bit difficult 
		// to take it in consideration in mentioned functions.	
		// private var _checkFunction:Function;
		
		private var _checkEnabled:Boolean;
		
		public function CheckBoxTree() {
			super();			
			this.itemRenderer = new ClassFactory(CheckBoxTreeItemRenderer);	
			addEventListener("itemCheck", checkHandler);				
		}
		
	    mx_internal function isBranch(item: Object): Boolean {
	        if (item != null)
	            return _dataDescriptor.isBranch(item, iterator.view);
	        return false;
	    }		
		
 		private function checkHandler(event: TreeEvent): void {
	    	var value: int;
	        var state: int = (event.itemRenderer as CheckBoxTreeItemRenderer).checkBox.checkState;
	        var middle: Boolean = (state & 2 << 1) == (2 << 1);
	        var selected: Boolean = (state & 1 << 1) == (1 << 1);

			if (isBranch(event.item)) {
				middle = false;
			}  
	        
	        if (middle) {
	            value = PARTIALLY_SELECTED;
	        } else {
	            value = selected ? SELECTED : UNSELECTED;
	        }
	        
	        var data:Object = event.item;

	        if (data == null) {
	            return;
	        }
	
	        if (checkField) {
	            if (data is XML) {
	                try {
	                   data[checkField] = value;
	                } catch (e: Error) {
	                }
	            } else if (data is Object) {
	                try {
	                    data[checkField] = value;
	                } catch(e: Error) {
	                }
	            }
	            if (childrenField && propagateCheckedStatus) {
		            updateParents(data, state);
					updateChildren(data, state);					
	            }
	         }
	
	        if (data is String) {
	            data = String(value);
	        }
	      commitProperties();
	    }
				
	    override protected function makeListData(data: Object, uid: String, rowNum: int): BaseListData {
	        var treeListData: TreeListData = new CheckBoxTreeListData(itemToLabel(data), uid, this, rowNum);
	        initListData(data, treeListData);
	        return treeListData;
	    }
	    
	    override protected function initListData(item: Object, treeListData: TreeListData): void {
	    	super.initListData(item, treeListData);
	    	
	        if (item == null)
	            return;
	
	        (treeListData as CheckBoxTreeListData).checkedState = itemToCheck(item);
	    }	    
		
    	[Bindable("checkFieldChanged")]
    	[Inspectable(category="Data", defaultValue="checked")]		
	    public function get checkField(): String {
	        return _checkField;
	    }
	
	    public function set checkField(value: String): void {
	        _checkField = value;
	        itemsSizeChanged = true;
	        invalidateDisplayList();
	        dispatchEvent(new Event("checkFieldChanged"));
	    }
	    	   
	    public function get childrenField(): String {
	        return _childrenField;
	    }
	
	    public function set childrenField(value: String): void {
	        _childrenField = value;	        
	    }
	    
	    public function itemToCheck(data: Object): int {
	        if (data == null)
	            return 0;
	
//	        if (checkFunction != null)
//	            return checkFunction(data);
//	
	        if (data is XML) {
	            try {
	                if (data[checkField].length() != 0)
	                    data = data[checkField];
	            } catch(e: Error) {
	            }
	        }
	        else if (data is Object) {
	            try {
	                if (data[checkField] != null)
	                    data = data[checkField];
	            } catch(e: Error) {
	            }
	        }
	
	        if (data is String)
	            return parseInt(String(data));
	
	        try {
	            return parseInt(String(data));
	        } catch(e: Error) {
	        }
	        return 0;
	    }
	    
//	    [Bindable("checkFunctionChanged")]
//	    [Inspectable(category="Data")]	
//	    public function get checkFunction(): Function {
//	        return _checkFunction;
//	    }
//	
//	    public function set checkFunction(value: Function): void {
//	        _checkFunction = value;
//	        itemsSizeChanged = true;
//	        invalidateDisplayList();
//	        dispatchEvent(new Event("checkFunctionChanged"));
//	    }
	    
	    [Bindable("checkEnabledChanged")]
    	[Inspectable(category="Data", defaultValue="false")]		
	    public function get checkEnabled(): Boolean {
	        return _checkEnabled;
	    }
	
	    public function set checkEnabled(value: Boolean): void {
	        _checkEnabled = value;	 
	        ClassFactory(this.itemRenderer).properties = {showCheckBox:_checkEnabled};
	        dispatchEvent(new Event("checkEnabledChanged"));
	    }
	   			
        private function updateChildren(item:Object, value: uint):void {
			var middle:Boolean = (value & 2 << 1) == (2 << 1);
		    var selected:Boolean = (value & 1 << 1) == (1 << 1);
	        
            if (item[childrenField] != null && item[childrenField].length > 0 && !middle) {
            	for each (var child:Object in item[childrenField]) {
                	child[checkField] = value == (1 << 1 | 2 << 1) ? PARTIALLY_SELECTED : value == (1 << 1) ? SELECTED : UNSELECTED;
                    updateChildren(child, value);
                }
            }
        }
            
        private function updateParents(item:Object, value:uint): void {
        	var checkValue:int = (value == (1 << 1 | 2 << 1) ? PARTIALLY_SELECTED : value == (1 << 1) ? SELECTED : UNSELECTED);
            var parentNode:Object = getParentItem(item);
            if (parentNode) {
            	for each (var child:Object in parentNode[childrenField]) {
                	if (child[checkField] != checkValue) {
                    	checkValue = 2;
                    	break;
                    }
                }
                parentNode[checkField] = checkValue;
                updateParents(parentNode, value);
            }
        }	    		
	}
	
}