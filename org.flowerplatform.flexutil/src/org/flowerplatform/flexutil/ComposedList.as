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
package org.flowerplatform.flexutil {
	import flash.events.Event;
	
	import mx.collections.IList;
	
	public class ComposedList implements IList {
		
		protected var lists:Array;
		
		public function ComposedList(lists:Array) {
			this.lists = lists;
		}
		
		public function get length():int {
			var length:int;
			for (var i:int = 0; i < lists.length; i++) {
				var currentList:IList = IList(lists[i]);
				if (currentList != null) {
					length += currentList.length;
				}
			}
			return length;
		}
		
		public function addItem(item:Object):void {
			throw new Error("Unsupported operation");
		}
		
		public function addItemAt(item:Object, index:int):void {
			throw new Error("Unsupported operation");
		}
		
		public function getItemAt(index:int, prefetch:int=0):Object	{
			var length:int;
			for (var i:int = 0; i < lists.length; i++) {
				var currentList:IList = IList(lists[i]);
				if (currentList != null && index < length + currentList.length) {
					// found the right list!
					return currentList.getItemAt(index - length, prefetch);
				} else {
					// element is in one of the next lists
					length += IList(lists[i]).length;
				}
			} 
			throw new Error("Index out of bounds = " + index);
		}
		
		public function getItemIndex(item:Object):int {
			throw new Error("Unsupported operation");
		}
		
		public function itemUpdated(item:Object, property:Object=null, oldValue:Object=null, newValue:Object=null):void {
			throw new Error("Unsupported operation");
		}
		
		public function removeAll():void {
			throw new Error("Unsupported operation");
		}
		
		public function removeItemAt(index:int):Object {
			throw new Error("Unsupported operation");
		}
		
		public function setItemAt(item:Object, index:int):Object {
			throw new Error("Unsupported operation");
		}
		
		public function toArray():Array {
			throw new Error("Unsupported operation");
		}
		
		public function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void {
			throw new Error("Unsupported operation");
		}
		
		public function removeEventListener(type:String, listener:Function, useCapture:Boolean=false):void {
			throw new Error("Unsupported operation");
		}
		
		public function dispatchEvent(event:Event):Boolean {
			throw new Error("Unsupported operation");
		}
		
		public function hasEventListener(type:String):Boolean {
			throw new Error("Unsupported operation");
		}
		
		public function willTrigger(type:String):Boolean {
			throw new Error("Unsupported operation");
		}
	}
}