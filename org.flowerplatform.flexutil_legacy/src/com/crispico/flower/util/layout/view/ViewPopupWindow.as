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
package com.crispico.flower.util.layout.view
{
	import com.crispico.flower.flexdiagram.util.common.FlowerLinkButton;
	import com.crispico.flower.util.UtilAssets;
	import com.crispico.flower.util.layout.ArrangeTool;
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.event.DockHandlerEvent;
	import com.crispico.flower.util.layout.event.LayoutDataChangedEvent;
	import com.crispico.flower.util.layout.event.ViewAddedEvent;
	import com.crispico.flower.util.layout.persistence.WorkbenchLayoutData;
	import com.crispico.flower.util.popup.ResizablePopupWindow;
	
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import flexlib.containers.SuperTabNavigator;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Button;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.layout.event.ViewRemovedEvent;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;

	/**
	 * Popup window used by docking/undocking mechanism.
	 * 
	 * <p>
	 * Besides the super functionality, this window has a button in the titlebar.
	 * It is used to dock the view to workbench.
	 * 
	 * Useful methods:
	 * <ul>
	 * 	<li> formCancelHandler() -> handler for "Close" button (additional behavior can be added here)
	 * 	<li> closeForm() -> closes the window
	 * </ul>
	 * 
	 * @author Cristina
	 */ 
	public class ViewPopupWindow extends ResizablePopupWindow {
		
		public var workbench:Workbench;
		
		public var viewLayoutData:ViewLayoutData;
		
		public var component:UIComponent;
		
		private var dockButton:FlowerLinkButton;
					
		override protected function createChildren():void {
			super.createChildren();
			
			// add "dock" button to titlebar
			dockButton = new FlowerLinkButton();
			dockButton.setStyle("icon", UtilAssets.INSTANCE._dockIcon);
			dockButton.addEventListener(MouseEvent.CLICK, dockButtonHandler);
			dockButton.toolTip = UtilAssets.INSTANCE.getMessage("layout.tooltip.dock");
			titleBar.addChild(dockButton);	
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			dockButton.setActualSize(22, 22);
						
			// position the "dock" button 
			var y:int = 3;
			var x:int = this.width - dockButton.getExplicitOrMeasuredWidth() - (mx_internal::closeButton ? mx_internal::closeButton.width : 0) - 2;
			dockButton.move(x, y);
			dockButton.owner = this;
		}
		
		private function dockButtonHandler(event:MouseEvent):void {			
			closeFormInternal();
			
			// prepare view to be added on workbench
			workbench.createView(viewLayoutData, component);
			
			// dispatch event so that custom behavior can be set if wanted
			workbench.dispatchEvent(new DockHandlerEvent(DockHandlerEvent.CLICK, viewLayoutData, component));		
		}
				
		override public function closeForm():void {
			close();
		}	
		
		public function close(dispatchLayoutDataChangedEvent:Boolean = true):void {
			closeFormInternal();
			
			// dispatch remove events
			var viewsRemovedEvent:ViewsRemovedEvent = new ViewsRemovedEvent(new ArrayCollection([component]));			
			workbench.dispatchEvent(viewsRemovedEvent);
			
			component.dispatchEvent(new ViewRemovedEvent());	
			if (dispatchLayoutDataChangedEvent) {
				workbench.dispatchEvent(new LayoutDataChangedEvent());
			}
			// delete data from maps (isn't used anymore)
			delete workbench.layoutDataToComponent[viewLayoutData];
			delete workbench.componentToLayoutData[component];
		}
		
		private function closeFormInternal():void {
			// the view MUST not have a parent
			viewLayoutData.parent = null;
			
			super.closeForm();
			
			// update layout data
			viewLayoutData.isUndocked = false;
			viewLayoutData.dimensions = new ArrayCollection([x, y, getExplicitOrMeasuredWidth(), getExplicitOrMeasuredHeight()]);			
			var index:int = WorkbenchLayoutData(workbench.rootLayout).undockedViews.getItemIndex(viewLayoutData);
			if (index != -1) {
				WorkbenchLayoutData(workbench.rootLayout).undockedViews.removeItemAt(index);
			}
		}
	}	
	
}