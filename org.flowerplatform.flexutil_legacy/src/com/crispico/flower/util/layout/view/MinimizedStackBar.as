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
package  com.crispico.flower.util.layout.view {
	import com.crispico.flower.flexdiagram.util.common.FlowerLinkButton;
	import com.crispico.flower.util.UtilAssets;
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import flash.events.MouseEvent;
	
	import mx.containers.Box;
	import mx.containers.BoxDirection;
	import mx.controls.Button;
	import mx.controls.LinkButton;
	import mx.core.UIComponent;
	
	/**
	 * Represents a graphical minimized stack.
	 * 
	 * 
	 */
	public class MinimizedStackBar extends Box {
				
		/**
		 * 
		 */
		private var workbench:Workbench;
		
		/**
		 * 
		 */
		public var stackLayoutData:StackLayoutData;
		
		/**
		 * 
		 */
		private var restoreButton:Button;
		
		/**		
		 * 
		 */
		public function MinimizedStackBar(orientation:Number, stackLayoutData:StackLayoutData, workbench:Workbench) {
			this.stackLayoutData = stackLayoutData;
			this.workbench = workbench;
			this.setStyle("horizontalGap", 0);
			this.setStyle("verticalGap", 0);
									
			if (orientation == StackLayoutData.LEFT || orientation == StackLayoutData.RIGHT) {
				this.direction = BoxDirection.VERTICAL;
			} else {
				this.direction = BoxDirection.HORIZONTAL;
			}
		}
		
		/**
		 * For each stack layout data child, adds a button with corresponding icon.
		 * 
		 */
		protected override function createChildren():void {			
			super.createChildren();
						
			restoreButton = new LinkButton();
		 	
		 	restoreButton.setStyle("icon", UtilAssets.INSTANCE.tabRes);
		 	restoreButton.setStyle("paddingLeft", 2);
		 	restoreButton.setStyle("paddingRight", 2);
			restoreButton.tabEnabled = false;
			
		 	restoreButton.addEventListener(MouseEvent.CLICK, restoreButtonHandler);		 	
		 	addChild(restoreButton);
		 	
		 	for each (var child:ViewLayoutData in stackLayoutData.children) {
		 		var linkBtn:FlowerLinkButton = new FlowerLinkButton();
				linkBtn.addEventListener(MouseEvent.CLICK, restoreButtonHandler);
		 		linkBtn.setStyle("iconURL", workbench.viewProvider.getIcon(child));
		 		linkBtn.toolTip = workbench.viewProvider.getTitle(child);
		 		linkBtn.setStyle("paddingLeft", 2);
		 		linkBtn.setStyle("paddingRight", 2);
				linkBtn.tabEnabled = false;
				
		 		addChild(linkBtn);
		 	}		 	
		}
		
		/**
		 * Restores the stack graphical component.
		 * <p>
		 * Sets the active view to be the restored component.
		 * 
		 * 
		 */
		private function restoreButtonHandler(event:MouseEvent):void {
			workbench.restore(stackLayoutData);
			
			var tabNavigator:LayoutTabNavigator = LayoutTabNavigator(workbench.layoutDataToComponent[stackLayoutData]);
			if (tabNavigator.selectedIndex != -1) {			
				workbench.activeViewList.setActiveView(UIComponent(tabNavigator.getChildAt(tabNavigator.selectedIndex)));
			}
		}		
	}
	
}