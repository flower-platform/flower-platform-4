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
package org.flowerplatform.flexutil.view_content_host {
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.net.drm.AddToDeviceGroupSetting;
	
	import mx.collections.IList;
	import mx.events.FlexEvent;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	
	import spark.components.Button;
	import spark.components.Group;
	import spark.components.HGroup;
	import spark.layouts.VerticalLayout;
	
	/**
	 * Basic implementation of <code>IViewContent</code>.
	 * 
	 * Adds ok, cancel buttons to control bar and sets vertical layout to group.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class BasicViewContent extends Group implements IViewContent, IFocusManagerComponent	{
		
		protected var _viewHost:IViewHost;
		
		protected var okButton:Button;
		protected var cancelButton:Button;
		
		protected var buttonBar:HGroup;
		
		public function BasicViewContent() {
			super();
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
			addEventListener(FlexEvent.INITIALIZE, initializeHandler);
		}
		
		public function get viewHost():IViewHost {
			return _viewHost;
		}
		
		public function set viewHost(value:IViewHost):void {
			_viewHost = value;
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			return null;
		}
		
		public function additionalCloseHandler():void {
		}		
		
		protected function initializeHandler(event:FlexEvent):void {
			var layout:VerticalLayout = new VerticalLayout();
			layout.paddingTop = 10;
			layout.paddingLeft = 5;
			layout.paddingRight = 5;
			layout.paddingBottom = 5;
			
			this.layout = layout;
		}
		
		protected function creationCompleteHandler(event:FlexEvent):void {
		}
		
		protected function okHandler(event:Event = null):void {			
		}
		
		protected function cancelHandler(event:Event = null):void {			
			FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(this);
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			buttonBar = new HGroup();
			buttonBar.percentWidth = 100;
			buttonBar.horizontalAlign = "right";
			buttonBar.verticalAlign = "middle";
			
			okButton = new Button();
			okButton.label = FlexUtilAssets.INSTANCE.getMessage('dialog.ok');
			okButton.addEventListener(MouseEvent.CLICK, okHandler);
			
			cancelButton = new Button();
			cancelButton.label = FlexUtilAssets.INSTANCE.getMessage('dialog.cancel');
			cancelButton.addEventListener(MouseEvent.CLICK, cancelHandler);
			
			buttonBar.addElement(okButton);
			buttonBar.addElement(cancelButton);
			
			viewHost.addToControlBar(buttonBar);
		}
				
	}
}