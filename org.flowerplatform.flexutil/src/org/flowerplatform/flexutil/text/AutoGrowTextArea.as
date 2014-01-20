/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.flexutil.text {
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import mx.core.mx_internal;
	import mx.skins.spark.SparkSkinForHalo;
	
	import spark.components.RichEditableText;
	import spark.components.TextArea;
	
	use namespace mx_internal;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class AutoGrowTextArea extends TextArea {
				
		public function AutoGrowTextArea()	{
			super();	
			setStyle("skinClass", AutoGrowSkinnableTextBaseSkin);
		}
		
		protected function kewDownHandler1(event:KeyboardEvent):void {
			if (event.keyCode == Keyboard.ENTER && !event.ctrlKey) {
				event.preventDefault();				
				return;
			}
			if (event.keyCode == Keyboard.ENTER && event.ctrlKey) {
				heightInLines++;
				textDisplay.appendText("\n");
			}
		}
		
		override protected function partAdded(partName:String, instance:Object):void {
			super.partAdded(partName, instance);
			if (instance == textDisplay) {
				textDisplay.addEventListener(KeyboardEvent.KEY_DOWN, kewDownHandler1, false, 50);
			}
		}

	}
}