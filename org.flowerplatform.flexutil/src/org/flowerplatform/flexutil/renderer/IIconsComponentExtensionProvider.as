package org.flowerplatform.flexutil.renderer {
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlowerArrayList;
	
	/**
	 * @see IconsComponentExtension
	 * @author Cristina Constantinescu
	 */ 
	public interface IIconsComponentExtensionProvider {
		
		function get icons():FlowerArrayList;		
		function set icons(value:FlowerArrayList):void;
		
		/**
		 * @return - index in list of component's children for a new icon. 
		 */ 
		function newIconIndex():int;
		
		function getIconParentComponent():UIComponent;
		
		/**
		 * Validate methods must be overridden to validate also the icons from extension.
		 */ 
		function validateDisplayList():void;		
		function validateProperties():void;		
		function validateSize(recursive:Boolean = false):void;
		
	}
}