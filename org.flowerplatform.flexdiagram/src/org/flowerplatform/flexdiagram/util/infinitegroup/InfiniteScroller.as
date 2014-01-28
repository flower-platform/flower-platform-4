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
package org.flowerplatform.flexdiagram.util.infinitegroup {
	import flash.events.Event;
	
	import mx.core.IFactory;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.core.InteractionMode;
	import mx.core.mx_internal;
	import mx.events.PropertyChangeEvent;
	import mx.managers.IFocusManagerComponent;
	
	import spark.components.Group;
	import spark.components.supportClasses.SkinnableComponent;
	import spark.core.ContentRequest;

	use namespace mx_internal;
	
	[ResourceBundle("components")]
	[DefaultProperty("viewport")]
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InfiniteScroller extends SkinnableComponent implements IVisualElementContainer, IFocusManagerComponent {
						
		/////////////////
		
		[SkinPart(required="false", type="com.crispico.flower.flexdiagram.infinitegroup.InfiniteVScrollBar")]
		public var verticalScrollBarFactory:IFactory;
		
		[SkinPart(required="false")]
		[Bindable]    
		public var verticalScrollBar:InfiniteVScrollBar;
		
		/////////////////
		
		[SkinPart(required="false", type="com.crispico.flower.flexdiagram.infinitegroup.InfiniteHScrollBar")]		
		public var horizontalScrollBarFactory:IFactory;
		
		[SkinPart(required="false")]
		[Bindable]    
		public var horizontalScrollBar:InfiniteHScrollBar;
						
		/////////////////
		
		[SkinPart(required="false", type="spark.components.Button")]
		public var goBackButtonFactory:IFactory;
		
		[SkinPart(required="false")]
		[Bindable]    
		public var goBackButton:GoBackButton;
						
		/////////////////
		
		private var _viewport:InfiniteDataRenderer;
		
		[Bindable(event="viewportChanged")]
		
		public function get viewport():InfiniteDataRenderer {       
			return _viewport;
		}
		
		public function set viewport(value:InfiniteDataRenderer):void {
			if (value == _viewport)
				return;
			
			uninstallViewport();
			_viewport = value;
			installViewport();
			dispatchEvent(new Event("viewportChanged"));
		}
		
		public function InfiniteScroller() {
			setStyle("skinClass", InfiniteScrollerSkin);
		}
		
		private function installViewport():void	{
			if (skin && viewport) {
				viewport.clipAndEnableScrolling = true;
				Group(skin).addElementAt(viewport, 0);
				viewport.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, viewport_propertyChangeHandler);
			}
			if (verticalScrollBar)
				verticalScrollBar.viewport = viewport;
			if (horizontalScrollBar)
				horizontalScrollBar.viewport = viewport;
			if (goBackButton)
				goBackButton.viewport = viewport;
		}
		
		private function uninstallViewport():void {
			if (horizontalScrollBar)
				horizontalScrollBar.viewport = null;
			if (verticalScrollBar)
				verticalScrollBar.viewport = null;  
			if (goBackButton)
				goBackButton.viewport = null; 
			if (skin && viewport) {
				viewport.clipAndEnableScrolling = false;
				Group(skin).removeElement(viewport);
				viewport.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, viewport_propertyChangeHandler);				
			}
		}
		
		////////////////////////
		
		private function ensureDeferredGoBackButtonCreated():void {			
			if (!goBackButton && goBackButtonFactory) {
				goBackButton = GoBackButton(createDynamicPartInstance("goBackButtonFactory"));
				Group(this.skin).addElement(goBackButton);
				partAdded("goBackButton", goBackButton);
			}
		}
		
		private function ensureDeferredHScrollBarCreated():void {
			if (!horizontalScrollBar && horizontalScrollBarFactory) {
				horizontalScrollBar = InfiniteHScrollBar(createDynamicPartInstance("horizontalScrollBarFactory"));
				Group(this.skin).addElement(horizontalScrollBar);
				partAdded("horizontalScrollBar", horizontalScrollBar);
			}
		}
				
		private function ensureDeferredVScrollBarCreated():void {
			if (!verticalScrollBar && verticalScrollBarFactory) {
				verticalScrollBar = InfiniteVScrollBar(createDynamicPartInstance("verticalScrollBarFactory"));
				Group(this.skin).addElement(verticalScrollBar);
				partAdded("verticalScrollBar", verticalScrollBar);
			}
		}
				
		private function viewport_propertyChangeHandler(event:PropertyChangeEvent):void {
			switch(event.property) {			
				case "contentRect":					
					viewport.setLayoutBoundsSize(viewport.contentRect.width, viewport.contentRect.height);
					viewport.setLayoutBoundsPosition(viewport.contentRect.x, viewport.contentRect.y);
					
					viewport.setContentSize(viewport.contentRect.width, viewport.contentRect.height);
										
					verticalScrollBar.minimum = viewport.contentRect.y;
					verticalScrollBar.maximum = viewport.contentRect.height - Math.abs(viewport.contentRect.y);
					
					horizontalScrollBar.minimum = viewport.contentRect.x;
					horizontalScrollBar.maximum = viewport.contentRect.width - Math.abs(viewport.contentRect.x);
				case "scrollByToolInProgress":
				case "verticalScrollPosition": 			
				case "horizontalScrollPosition":					
					goBackButton.calculatePosition();
					skin.invalidateDisplayList();		
			}
		}
		
		override protected function attachSkin():void {
			super.attachSkin();
			
			ensureDeferredHScrollBarCreated();
			ensureDeferredVScrollBarCreated();
			ensureDeferredGoBackButtonCreated();
			
			Group(skin).layout = new InfiniteScrollerLayout();
			installViewport();			
		}
				
		override protected function detachSkin():void {    
			uninstallViewport();
			
			Group(skin).layout = null;	
			
			super.detachSkin();
		}
		
		override protected function partAdded(partName:String, instance:Object):void {
			super.partAdded(partName, instance);
						
			const inTouchMode:Boolean = (getStyle("interactionMode") == InteractionMode.TOUCH);
			
			if (instance == verticalScrollBar) {
				verticalScrollBar.viewport = viewport;				
				// We don't support directly interacting with the scrollbars in touch mode
				if (inTouchMode) {
					verticalScrollBar.mouseEnabled = false;
					verticalScrollBar.mouseChildren = false;
				}
				
			} else if (instance == horizontalScrollBar) {
				horizontalScrollBar.viewport = viewport;				
				// We don't support directly interacting with the scrollbars in touch mode
				if (inTouchMode) {
					horizontalScrollBar.mouseEnabled = false;
					horizontalScrollBar.mouseChildren = false;
				}
			} else if (instance == goBackButton) {
				goBackButton.viewport = viewport;	
			}
		}
		
		override protected function partRemoved(partName:String, instance:Object):void {
			super.partRemoved(partName, instance);
			
			if (instance == verticalScrollBar) {
				verticalScrollBar.viewport = null;
			} else if (instance == horizontalScrollBar) {
				horizontalScrollBar.viewport = null;
			} else if (instance == goBackButton) {
				goBackButton.viewport = null;
			}
		}
		
		//--------------------------------------------------------------------------
		//
		//  Methods: IVisualElementContainer
		//
		//--------------------------------------------------------------------------
		
		public function get numElements():int {
			return viewport ? 1 : 0;
		}

		public function getElementAt(index:int):IVisualElement {
			if (viewport && index == 0) {
				return viewport;
			} else {
				throw new RangeError(resourceManager.getString("components", "indexOutOfRange", [index]));
			}
		}

		public function getElementIndex(element:IVisualElement):int {
			if (element != null && element == viewport) {
				return 0;
			} else {
				throw ArgumentError(resourceManager.getString("components", "elementNotFoundInScroller", [element]));
			}
		}
		
		/**
		 * 
		 *  This operation is not supported in Scroller.  
		 *  A Scroller control has only one child. 
		 *  Use the <code>viewport</code> property to manipulate 
		 *  it.		
		 */
		public function addElement(element:IVisualElement):IVisualElement {
			throw new ArgumentError(resourceManager.getString("components", "operationNotSupported"));
		}

		public function addElementAt(element:IVisualElement, index:int):IVisualElement {
			throw new ArgumentError(resourceManager.getString("components", "operationNotSupported"));
		}

		public function removeElement(element:IVisualElement):IVisualElement {
			throw new ArgumentError(resourceManager.getString("components", "operationNotSupported"));
		}

		public function removeElementAt(index:int):IVisualElement {
			throw new ArgumentError(resourceManager.getString("components", "operationNotSupported"));
		}
		
		public function removeAllElements():void {
			throw new ArgumentError(resourceManager.getString("components", "operationNotSupported"));
		}

		public function setElementIndex(element:IVisualElement, index:int):void {
			throw new ArgumentError(resourceManager.getString("components", "operationNotSupported"));
		}
		
		public function swapElements(element1:IVisualElement, element2:IVisualElement):void {
			throw new ArgumentError(resourceManager.getString("components", "operationNotSupported"));
		}
		
		public function swapElementsAt(index1:int, index2:int):void {
			throw new ArgumentError(resourceManager.getString("components", "operationNotSupported"));
		}
	}
}