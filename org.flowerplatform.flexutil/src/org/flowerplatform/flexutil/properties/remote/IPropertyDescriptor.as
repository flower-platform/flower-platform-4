package org.flowerplatform.flexutil.properties.remote {
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Diana Balutoiu
	 */
	public interface IPropertyDescriptor {
		
		function get name():String;
		function set name(value:String):void;
		
		function get title():String;
		function set title(value:String):void;
		
		function get type():String;
		function set type(value:String):void;
		
		function get category():String;
		function set category(value:String):void;
		
		function get propertyLineRenderer():String;
		function set propertyLineRenderer(value:String):void;
		
		function get contributesToCreation():Boolean;
		function set contributesToCreation(value:Boolean):void;
		
		function get mandatory():Boolean;
		function set mandatory(value:Boolean):void;
		
		function get defaultValue():Object;
		function set defaultValue(value:Object):void;
		
		function get possibleValues():ArrayCollection;
		function set possibleValues(value:ArrayCollection):void;
		
		function get readOnly():Boolean;
		function set readOnly(value:Boolean):void;
		
		function get orderIndex():int;
		function set orderIndex(value:int):void;
	}
}