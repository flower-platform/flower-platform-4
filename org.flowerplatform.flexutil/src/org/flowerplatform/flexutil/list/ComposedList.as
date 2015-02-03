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
package org.flowerplatform.flexutil.list {
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ComposedList extends EventDispatcher implements IList {
		
		protected var lists:Array = [];
		
		public function ComposedList(lists:Array = null) {
			if (lists == null) {
				return;
			}
			for (var i:int = 0; i < lists.length; i++) {
				var current:IList = lists[i] as IList; 
				addList(current, false);
			}
			childListCollectionChangeHandler(null);
		}
		
		public function addList(list:IList, shouldDispatchEvent:Boolean = true):void {
			if (list == null || removeList(list, false) != null) {
				// i.e. list already exists
				return;
			}
			
			lists.push(list);
			list.addEventListener(CollectionEvent.COLLECTION_CHANGE, childListCollectionChangeHandler);
			if (shouldDispatchEvent) {
				childListCollectionChangeHandler(null);
			}
		}
		
		public function removeList(list:IList, shouldRemove:Boolean = true, shouldDispatchEvent:Boolean = true):IList {
			if (list == null) {
				return null;
			}
			for (var i:int = 0; i < lists.length; i++) {
				var current:IList = lists[i] as IList; 
				if (current == list) {
					if (shouldRemove) {
						current.removeEventListener(CollectionEvent.COLLECTION_CHANGE, childListCollectionChangeHandler);
						lists.splice(i, 1);
						if (shouldDispatchEvent) {
							childListCollectionChangeHandler(null);
						}
					}
					return current;					
				}
			}
			return null;
		}
		
		public function removeAllLists(shouldDispatchEvent:Boolean = true):void {
			lists = [];
			if (shouldDispatchEvent) {
				childListCollectionChangeHandler(null);
			}
		}
		
		protected function childListCollectionChangeHandler(event:CollectionEvent):void {
			if (event != null && event.kind == CollectionEventKind.UPDATE) {
				dispatchEvent(event);
			} else {
				dispatchEvent(new CollectionEvent(CollectionEvent.COLLECTION_CHANGE, false, false, CollectionEventKind.RESET));
			}
		}
		
		public function getList(index:int):IList {
			if (index >= lists.length) {
				return null;
			} else {
				return lists[index];
			}
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
		
	}
}