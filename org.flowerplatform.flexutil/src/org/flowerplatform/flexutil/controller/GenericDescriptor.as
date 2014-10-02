package org.flowerplatform.flexutil.controller {
	
	/**
	 * @author Cristian Spiescu
	 */
	[RemoteClass(alias="org.flowerplatform.util.controller.GenericDescriptor")]
	public class GenericDescriptor extends AbstractController {

		public var value:Object;
		
		/**
		 * An optional map.
		 */
		public var extraInfo:Object;
		
		public function GenericDescriptor(value:Object=null, extraInfo:Object=null, orderIndex:int=0) {
			super(orderIndex);
			this.value = value;
			this.extraInfo = extraInfo;
		}
		
		public function getExtraInfoProperty(property:String):Object {
			if (extraInfo == null) {
				return null;
			}
			return extraInfo[property];
		}
		
		override public function toString():String {
			return super.toString() + "[value = " + value + "]";
		}		
	
		public function addExtraInfoProperty(key:String, value:String):GenericDescriptor {
			if (extraInfo == null) {
				extraInfo = new Object();
			}
			extraInfo[key] = value;
			return this;
		}
	}
}