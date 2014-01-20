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
package com.crispico.flower.util.popup {
	
	import com.crispico.flower.util.spinner.ModalSpinner;
	
	import flash.display.DisplayObject;
	
	import mx.collections.IList;
	import mx.containers.ControlBar;
	
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */ 
	public class ResizablePopupWindowViewHost extends ResizablePopupWindow implements IViewHost	{
		
		protected var _viewContent:IViewContent;
		
		public function ResizablePopupWindowViewHost(viewContent:IViewContent = null) {
			super();			
			_viewContent = viewContent;
			_viewContent.percentHeight = 100;
			_viewContent.percentWidth = 100;
		}
	
		public function get activeViewContent():IViewContent {
			return _viewContent;
		}
		
		public function setActiveViewContent(value:IViewContent, viaFocusIn:Boolean = false):void {
		}

		public function selectionChanged():IList {
			// not supported		
			return null;
		}
		
		public function setIcon(value:Object):void {
			titleIconURL = String(value);
		}
		
		public function setLabel(value:String):void {
			title = value;
		}
		
		/**
		 * @author Mariana
		 */
		public function displayCloseButton(value:Boolean):void {
			showCloseButton = value;
		}
		
		/**
		 * @author Mariana
		 */
		public function addToControlBar(value:Object):void {
			if (controlBar == null) {
				controlBar = new ControlBar();
				controlBar.document = this;
				controlBar.enabled = true;
				
				var cBar:ControlBar = ControlBar(controlBar);
				cBar.setStyle("horizontalAlign", "center");
				cBar.setStyle("verticalAlign", "middle");
				addChild(cBar);
			}
			
			ControlBar(controlBar).addChild(DisplayObject(value));
		}
		
		public function openMenu(x:Number, y:Number, context:Object, parentActionId:String = null):Boolean {
			// doesn't support this	
			return false;
		}	
		
		public function showSpinner(text:String):void {	
			ModalSpinner.addModalSpinner(this, text);
		}
	
		public function hideSpinner():void {		
			ModalSpinner.removeModalSpinner(this);
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function getCachedActions():Vector.<IAction> {	
			// doesn't support this	
			return null;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function getCachedSelection():IList {
			// doesn't support this	
			return null;
		}	
		
		override protected function createChildren():void {
			if (activeViewContent == null) {
				throw new Error("Illegal state. The viewContent shouldn't be null.");
			}
			super.createChildren();		
			
			activeViewContent.viewHost = this;
			addElement(activeViewContent);
		}
		
	}
}