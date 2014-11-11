package org.flowerplatform.flexutil.list {
	import flash.events.Event;
	
	import mx.collections.IList;
	import mx.utils.LinkedList;
	import mx.utils.LinkedListNode;
	
	/**
	 * Wraps a LinkedList, allowing only sequential iteration (based on internal state).
	 * 
	 * @author Cristian Spiescu
	 */
	public class LinkedListWrapper implements IList {
		
		protected var linkedList:LinkedList;
		
		protected var lastIterationIndex:int = 0;
		
		protected var lastIterationNode:LinkedListNode;
		
		public function LinkedListWrapper(linkedList:LinkedList) {
			this.linkedList = linkedList;
		}
		
		public function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void {
			throw new Error("This method is not supported");
		}
		
		public function dispatchEvent(event:Event):Boolean {
			throw new Error("This method is not supported");
		}
		
		public function hasEventListener(type:String):Boolean {
			throw new Error("This method is not supported");
		}
		
		public function removeEventListener(type:String, listener:Function, useCapture:Boolean=false):void {
			throw new Error("This method is not supported");
		}
		
		public function willTrigger(type:String):Boolean {
			throw new Error("This method is not supported");
		}
		
		public function get length():int {
			return linkedList.length
		}
		
		public function addItem(item:Object):void {
			throw new Error("This method is not supported");
		}
		
		public function addItemAt(item:Object, index:int):void {
			throw new Error("This method is not supported");
		}
		
		public function getItemAt(index:int, prefetch:int=0):Object {
			if (index >= linkedList.length) {
				throw new Error("Index out of bounds. Length = " + length + " and requiredIndex = " + index);
			} else if (index == 0) {
				// i.e. begining of iteration
				lastIterationIndex = 0;
				lastIterationNode = linkedList.head;
			} else if (index == lastIterationIndex + 1) {
				// i.e. continuing iteration
				lastIterationIndex++;
				lastIterationNode = lastIterationNode.next;
			} else {
				throw new Error("Only sequential iteration is allowed. Random access is not possible. lastIterationIndex = " + lastIterationIndex + ", requiredIndex = " + index);
			}
			return lastIterationNode.value;
		}
		
		public function getItemIndex(item:Object):int {
			throw new Error("This method is not supported");
		}
		
		public function itemUpdated(item:Object, property:Object=null, oldValue:Object=null, newValue:Object=null):void {
			throw new Error("This method is not supported");
		}
		
		public function removeAll():void {
			throw new Error("This method is not supported");
		}
		
		public function removeItemAt(index:int):Object {
			throw new Error("This method is not supported");
		}
		
		public function setItemAt(item:Object, index:int):Object {
			throw new Error("This method is not supported");
		}
		
		public function toArray():Array {
			throw new Error("This method is not supported");
		}
		
	}
}