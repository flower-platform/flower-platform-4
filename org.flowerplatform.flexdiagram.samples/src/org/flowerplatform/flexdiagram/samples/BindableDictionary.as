package org.flowerplatform.flexdiagram.samples
{
	import flash.utils.Dictionary;
	
	import mx.utils.ObjectProxy;

	public class BindableDictionary extends ObjectProxy {
		private var _state:Dictionary;
		public function BindableDictionary() {
			_state = new Dictionary();
			super(_state);
		}
			 
	}
}