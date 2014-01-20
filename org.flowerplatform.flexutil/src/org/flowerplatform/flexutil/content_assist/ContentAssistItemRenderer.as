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
package org.flowerplatform.flexutil.content_assist {
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	import spark.components.IconItemRenderer;
	import spark.components.Label;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ContentAssistItemRenderer extends IconItemRenderer {
		
		protected var extraLabel:Label;
		
		public var contentAssistProvider:IContentAssistProvider;
		
		public function ContentAssistItemRenderer() {
			super();
			
			labelField = "mainString";
			iconFunction = function(value:Object):Object {
				return contentAssistProvider.getResource(value.iconUrl);
			};
			
			// set explicitely to avoid a bug where width & height are NaN after measure()
			iconWidth = 16;
			iconHeight = 16;
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				minHeight = 22;
			}
			setStyle("verticalAlign", "middle");
			setStyle("paddingLeft", 1);
			cacheAsBitmap = true;
		}
		
		override protected function createChildren():void {
			super.createChildren();
			extraLabel = new Label();
			extraLabel.setStyle("color", "#808080");
			extraLabel.setStyle("verticalAlign", "middle");
			extraLabel.percentWidth = 100;
			addChild(extraLabel);
		}
		
		override protected function layoutContents(unscaledWidth:Number, unscaledHeight:Number):void {
			super.layoutContents(unscaledWidth, unscaledHeight);
			var elementWidth:int = getElementPreferredWidth(extraLabel);
			var elementHeight:int = labelDisplay ? labelDisplay.height : getElementPreferredHeight(extraLabel);
			setElementSize(extraLabel, elementWidth, elementHeight);
			var elementX:int = labelDisplay ? labelDisplay.x + labelDisplay.textWidth + 5 : iconWidth;
			var elementY:int = labelDisplay ? labelDisplay.y : unscaledHeight / 2 - extraLabel.height / 2;
			setElementPosition(extraLabel, elementX, elementY);
		}
		
		override protected function measure():void {
			super.measure();
			measuredWidth += extraLabel.width;
		}
		
		override public function set data(value:Object):void {
			super.data = value;
			
			if (value && value.extraString) {
				extraLabel.text = " - " + value.extraString;
			} else {
				extraLabel.text = null;
			}
		}
		
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {			
		}
	}
}