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