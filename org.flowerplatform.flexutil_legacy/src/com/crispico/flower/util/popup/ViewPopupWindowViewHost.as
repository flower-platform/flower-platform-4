/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
 * license-end
 */
package com.crispico.flower.util.popup {
	import com.crispico.flower.util.layout.view.ViewPopupWindow;
	
	import flash.display.DisplayObject;
	import flash.events.MouseEvent;
	
	import mx.collections.IList;
	import mx.containers.ControlBar;
	
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.spinner.ModalSpinner;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	import spark.components.Button;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ViewPopupWindowViewHost extends ViewPopupWindow implements IViewHost {
	
		public var viewContent:IViewContent;
		
		public function ViewPopupWindowViewHost() {
			super();
		}
		
		public function get activeViewContent():IViewContent {
			return viewContent;
		}
		
		public function setActiveViewContent(value:IViewContent, viaFocusIn:Boolean = false):void {
			// doesn't support this	
		}
		
		public function selectionChanged():IList {
			// doesn't support this			
			return null;
		}
		
		public function setIcon(value:Object):void {
			titleIconURL = value;
		}
		
		public function setLabel(value:String):void {
			title = value;
		}
		
		public function displayCloseButton(value:Boolean):void {
			showCloseButton = value;
		}
	
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
		
		public function getCachedActions():Vector.<IAction> {	
			// doesn't support this	
			return null;
		}
		
		public function getCachedSelection():IList {
			// doesn't support this	
			return null;
		}	
		
		override protected function createChildren():void {
			super.createChildren();
			
			// add close button by default in controlBar
			var closeButton:Button = new Button();
			closeButton.label = "Close";
			closeButton.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
				closeForm();
			});
			addToControlBar(closeButton);
		}
		
	}
}