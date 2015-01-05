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
	import flash.events.Event;
	
	import mx.collections.IList;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class EmptyList implements IList {
		
		public static const INSTANCE:EmptyList = new EmptyList();
		
		public function get length():int {
			return 0;
		}
		
		public function addItem(item:Object):void {
			throw new Error("Unsupported operation");
		}
		
		public function addItemAt(item:Object, index:int):void {
			throw new Error("Unsupported operation");
		}
		
		public function getItemAt(index:int, prefetch:int=0):Object	{
			throw new Error("Unsupported operation");
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