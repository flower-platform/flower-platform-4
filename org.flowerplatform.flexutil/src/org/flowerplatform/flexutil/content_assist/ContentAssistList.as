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
	
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.events.TouchEvent;
	import flash.geom.Point;
	import flash.ui.Keyboard;
	import flash.utils.Timer;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.ClassFactory;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexutil.popup.ScreenSizeAwarePopup;
	
	import spark.components.List;
	import spark.components.TextArea;
	import spark.components.TextInput;
	import spark.components.supportClasses.SkinnableTextBase;
	import spark.events.IndexChangeEvent;
	import spark.events.TextOperationEvent;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ContentAssistList extends ScreenSizeAwarePopup {
		
		private var list:List;
		
		/**
		 * TextArea or TextInput.
		 */
		private var dispatcher:SkinnableTextBase;
		
		private var contentAssistProvider:IContentAssistProvider;
		
		private var previousPattern:String;
		
		public var contentAssistItemsDelay:Number = 500;
		
		private var getContentAssistItemsTimer:Timer;
		
		public function ContentAssistList() {
			super();
			
			list = new List();
			list.itemRenderer = new ClassFactory(ContentAssistItemRenderer);
			list.percentWidth = 100;
			list.percentHeight = 100;
			addElement(list);
			list.addEventListener(MouseEvent.CLICK, itemClickHandler);
			
			getContentAssistItemsTimer = new Timer(contentAssistItemsDelay);
			getContentAssistItemsTimer.addEventListener(TimerEvent.TIMER, getContentAssistItems);
			
			width = 200;
			height = 200;
			visible = false;
			includeInLayout = false;
		}
		
		public function setDispatcher(dispatcher:SkinnableTextBase):void {
			this.dispatcher = dispatcher;
			dispatcher.addEventListener(KeyboardEvent.KEY_UP, displayContentAssistHandler);
		}
		
		public function setContentAssistProvider(provider:IContentAssistProvider):void {
			this.contentAssistProvider = provider;
			var properties:Object = new Object();
			properties['contentAssistProvider'] = provider;
			ClassFactory(list.itemRenderer).properties = properties;
		}
		
		override protected function get verticalOffset():Number	{
			return dispatcher.height;
		}
		
		
		/**
		 * Listens for arrow keys to navigate through the content assist items.
		 * Works for physical keyboards, and only for ENTER for soft keyboards.
		 * 
		 * <p>
		 * Should only be registered when the list is visible.
		 * @see displayHideContentAssist()
		 */
		protected function manageContentAssistHandler(evt:KeyboardEvent):void {
			trace ("key down - key ", evt.keyCode);
			
			var offset:int = 0;
			switch (evt.keyCode) {
				case Keyboard.DOWN: {
					offset = 1;
					break;
				}
				case Keyboard.UP: {
					offset = -1;
					break;
				}
				case Keyboard.PAGE_DOWN: {
					offset = 10;
					break;
				}
				case Keyboard.PAGE_UP: {
					offset = -10;
					break;
				}
				case Keyboard.ENTER: {
					selectContentAssistItem();
					// don't exit the text input
					evt.stopImmediatePropagation();
					return;
				}
				case Keyboard.ESCAPE: {
					displayHideContentAssist(false);
					// don't exit the text input
					evt.stopImmediatePropagation();
					return;
				}
			}
			
			if (offset != 0) {
				list.selectedIndex += offset;
				var length:int = list.dataProvider.length;
				if (list.selectedIndex < 0) {
					list.selectedIndex = length + offset;
				}
				if (list.selectedIndex >= length) {
					list.selectedIndex = length - list.selectedIndex;
				}
				list.ensureIndexIsVisible(list.selectedIndex);
				trace ("navigate through list");
				evt.stopImmediatePropagation();
			}
		}
		
		/**
		 * Listens for user input or selection change to update the content assist list litems.
		 * 
		 * <p>
		 * Should only be registered when the list is visible.
		 * @see displayHideContentAssist()
		 */
		protected function updateContentAssistListItems(evt:Event):void {
			if (visible) {
				trace ("update content asssist items");
				getContentAssistItemsTimer.start();
			}
		}
		
		/**
		 * Listens for triggers to display the content assist items.
		 */
		protected function displayContentAssistHandler(evt:KeyboardEvent):void {
			trace ("key up - char ", String.fromCharCode(evt.charCode));
			
			if (contentAssistProvider.getTriggerCharacters().contains(evt.charCode)) {
				getContentAssistItems();				// trigger character, e.g. :
			}
			if (evt.ctrlKey && evt.charCode == Keyboard.SPACE) {	// CTRL + SPACE
				getContentAssistItems();
			}
		}
		
		public function itemClickHandler(evt:MouseEvent):void {
			trace (evt.target, evt.currentTarget);
			// stop event propagation so it doesn't interfere with diagram logic
			evt.stopImmediatePropagation();
			var crt:Object = evt.target;
			while (crt != this) {
				if (crt == list.dataGroup) {
					// only if an item was selected
					selectContentAssistItem();
					return;
				}
				crt = crt.parent;
			}
		}
		
		private function selectContentAssistItem():void {
			if (list.selectedIndex > -1) {
				var selectedItem:ContentAssistItem = ContentAssistItem(list.selectedItem);
				// hide
				displayHideContentAssist(false);
				
				var text:String = dispatcher.text;
				var index:int = getDelimiterIndex();
				if (index == -1) {
					throw new Error("Invalid input for content assist");
				}
				
				index++;
				dispatcher.text = 
					text.substr(0, index) +
					selectedItem.item +
					text.substr(dispatcher.selectionActivePosition);
				index += selectedItem.item.length;
				dispatcher.selectRange(index, index);
			}
		}
		
		/**
		 * Gets the content assist items from the <code>contentAssistProvider</code> if
		 * the new <code>pattern</code> is different from the <code>previousPattern</code>.
		 */
		public function getContentAssistItems(evt:TimerEvent = null):void {
			getContentAssistItemsTimer.stop();
			var pattern:String = getTextFromDispatcher();
			if (pattern != null && pattern != previousPattern) {
				trace ("get content assist items -", pattern, evt); 
				previousPattern = pattern;
				contentAssistProvider.getContentAssistItems(pattern, setContentAssistItems);
			}
		}
		
		public function setContentAssistItems(items:IList):void {
			trace ("set content assist items -", items.length);
			list.dataProvider = items;
			list.selectedIndex = 0;
			displayHideContentAssist(true);
		}
		
		/**
		 * Returns the string from the first delimiter to the left to the end of the text.
		 */
		private function getTextFromDispatcher():String {
			var text:String = dispatcher.text;
			var n:int = dispatcher.selectionActivePosition;
			return text.substring(getDelimiterIndex() + 1, n); 
		}
		
		private function getDelimiterIndex():int {
			var text:String = dispatcher.text;
			var i:int;
			var n:int = dispatcher.selectionActivePosition;
			for(i = n - 1; i >= 0; i--) {
				var char:String = text.charAt(i);
				if ((char == ' ') || (char == ':') || (char == ',') 
					|| (char == '(') || (char == ')'))
					break;
			}
			return i;
		}
		
		private function displayHideContentAssist(display:Boolean):void {
			if (visible == display) {
				return;
			}
			
			trace (display ? "show content assist" : "hide content assist");
			
			visible = display;
			if (display) {
				dispatcher.removeEventListener(KeyboardEvent.KEY_UP, displayContentAssistHandler);
				
				dispatcher.addEventListener(KeyboardEvent.KEY_DOWN, manageContentAssistHandler, true);
				// handle text changes for both desktop and mobile
				dispatcher.addEventListener(TextOperationEvent.CHANGE, updateContentAssistListItems);
				// handle selection changes for desktop (using arrow keys or the mouse)
				dispatcher.addEventListener(FlexEvent.SELECTION_CHANGE, updateContentAssistListItems);
				
			} else {
				list.dataProvider = null;
				previousPattern = null;
				dispatcher.removeEventListener(KeyboardEvent.KEY_DOWN, manageContentAssistHandler, true);
				dispatcher.removeEventListener(TextOperationEvent.CHANGE, updateContentAssistListItems);
				dispatcher.removeEventListener(FlexEvent.SELECTION_CHANGE, updateContentAssistListItems);
				
				dispatcher.addEventListener(KeyboardEvent.KEY_UP, displayContentAssistHandler);
			}
		}
	}
}