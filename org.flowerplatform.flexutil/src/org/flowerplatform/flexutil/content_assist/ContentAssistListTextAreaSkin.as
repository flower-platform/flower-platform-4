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
	
	import flash.display.DisplayObject;
	import flash.geom.Point;
	
	import mx.core.FlexGlobals;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;
	
	import org.flowerplatform.flexutil.text.AutoGrowSkinnableTextBaseSkin;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ContentAssistListTextAreaSkin extends AutoGrowSkinnableTextBaseSkin {

		protected var contentAssist:ContentAssistList;
		
		public var contentAssistProvider:IContentAssistProvider;
		
		public function ContentAssistListTextAreaSkin() {
			super();
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			contentAssist = new ContentAssistList();
			contentAssist.setDispatcher(hostComponent);
			contentAssist.setContentAssistProvider(contentAssistProvider);
			
			PopUpManager.addPopUp(contentAssist, DisplayObject(FlexGlobals.topLevelApplication));
			contentAssist.addEventListener(FlexEvent.SHOW, showContentAssistHandler);
			hostComponent.addEventListener(FlexEvent.REMOVE, elementRemovedHandler);
		}
		
		protected function showContentAssistHandler(evt:FlexEvent):void {
			var textWidth:int = measureText(hostComponent.text.substr(0, hostComponent.selectionActivePosition)).width;
			// if the text area is multiline
			textWidth %= hostComponent.width;
			var globalPosition:Point = hostComponent.localToGlobal(new Point(textWidth, hostComponent.height));
			contentAssist.x = globalPosition.x;
			contentAssist.y = globalPosition.y;
		}
		
		protected function elementRemovedHandler(evt:FlexEvent):void {
			PopUpManager.removePopUp(contentAssist);
		}
		
	}
}