package org.flowerplatform.flexutil {
	import flash.utils.Dictionary;

	/**
	 * Useful for debugging memory leaks. Counts instances, using weak references.
	 * 
	 * @author Cristian Spiescu
	 */
	public class DebugWeakReferenceCounter {
		
		protected var container:Dictionary = new Dictionary(true);

		public function add(instance:Object):void {
			container[instance] = this;
		}
		
		public function getCount():int {
			var result:int = 0;
			for (var key:Object in container) {
				result++;
			}
			return result;
		}
	}
}