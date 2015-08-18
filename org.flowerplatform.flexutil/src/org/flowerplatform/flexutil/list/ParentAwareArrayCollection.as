package org.flowerplatform.flexutil.list {
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ParentAwareArrayCollection extends ArrayCollection {
		
		public var parent:Object;
		
		public function ParentAwareArrayCollection(parent:Object, source:Array=null) {
			super(source);
			this.parent = parent;
		}
	}
}