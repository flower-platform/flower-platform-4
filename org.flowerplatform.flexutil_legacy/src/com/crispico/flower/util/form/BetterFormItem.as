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
package com.crispico.flower.util.form {
	import mx.containers.FormItem;
	import mx.controls.Text;
	import mx.styles.CSSStyleDeclaration;
	import mx.styles.StyleManager;
	
	/**
	 *  Written by Nicholas Bilyk
	 *  http://www.nbilyk.com
	 *  This class lets you do a few things that the native FormItem cannot.
	 *  - Multiline labels
	 *  - Selectable labels
	 *  - Max width labels
	 */
	public class BetterFormItem extends FormItem {
		public var maxLabelWidth:Number = 200;
		public var text:Text;
		public var selectable:Boolean = false;
		public var multiline:Boolean = false;
		
		public function BetterFormItem() {
			super();
		}
		override protected function createChildren():void {
			super.createChildren();
			itemLabel.maxWidth = maxLabelWidth;
			if (multiline) {
				itemLabel.visible = false;
				text = new Text();
				
				text.setStyle("textAlign", "right");
				text.selectable = selectable;
				
				var labelStyleName:String = getStyle("labelStyleName");
				if (labelStyleName) {
					var styleDecl:CSSStyleDeclaration = StyleManager.getStyleDeclaration("." + labelStyleName);
					if (styleDecl) text.styleDeclaration = styleDecl;
				}
				rawChildren.addChild(text);
			} else {
				itemLabel.selectable = selectable;
			}
		}
		override protected function commitProperties():void {
			super.commitProperties();
			if (multiline) {
				text.text = itemLabel.text;
			}
		}
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if (multiline) {
				text.explicitWidth = itemLabel.width;
				text.validateNow();
				text.setActualSize(itemLabel.width, text.measuredHeight + 3);
				text.validateSize();
			}
		}
		override protected function measure():void {
			super.measure();
			if (multiline) {
				measuredMinHeight = Math.max(measuredMinHeight, text.measuredMinHeight);
				measuredHeight = Math.max(measuredHeight, text.measuredHeight);
			}
		}
	}
}
