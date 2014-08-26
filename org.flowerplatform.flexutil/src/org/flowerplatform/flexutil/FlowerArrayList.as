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
package org.flowerplatform.flexutil {
	
	import mx.collections.ArrayList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class FlowerArrayList extends ArrayList {
		
		public function FlowerArrayList(source:Array=null) {
			super(source);
		}
		
		/**
		 * If the source wasn't empty, 
		 * dispatches REMOVE event for each old item.
		 */
		override public function set source(s:Array):void {
			var oldSource:Array = source != null ? source.concat() : null;
			
			super.source = s;
			
			if (oldSource != null)	{
				var len:int = length;
				for (var i:int = 0; i < oldSource.length; i++) {
					dispatchRemoveEvent(oldSource[i], i);
				}
			}    
		}
		
		/**
		 * Dispatches REMOVE event for each removed item.
		 */ 
		override public function removeAll():void {
			var oldSource:Array = source != null ? source.concat() : null;
			
			super.removeAll();
			
			if (oldSource != null)	{
				var len:int = length;
				for (var i:int = 0; i < oldSource.length; i++) {
					dispatchRemoveEvent(oldSource[i], i);					
				}
			}    
		}
		
		private function dispatchRemoveEvent(item:Object, location:int):void {
			var event:CollectionEvent = new CollectionEvent(CollectionEvent.COLLECTION_CHANGE);
			event.kind = CollectionEventKind.REMOVE;
			event.items.push(item);
			event.location = location;
			dispatchEvent(event);
		}
	}
}