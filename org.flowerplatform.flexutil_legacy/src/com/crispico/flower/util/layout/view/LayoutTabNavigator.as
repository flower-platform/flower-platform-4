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
	import com.crispico.flower.flexdiagram.contextmenu.ContextMenuManager;
	import com.crispico.flower.flexdiagram.util.tabNavigator.FlowerSuperTab;
	import com.crispico.flower.flexdiagram.util.tabNavigator.FlowerSuperTabNavigator;
	import com.crispico.flower.util.UtilAssets;
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import flash.display.DisplayObject;
	import flash.display.Graphics;
	import flash.events.Event;
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.system.Capabilities;
	import flash.utils.Dictionary;
	
	import flexlib.controls.SuperTabBar;
	import flexlib.events.SuperTabEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.ArrayCollection;
	import mx.containers.BoxDirection;
	import mx.containers.DividedBox;
	import mx.controls.Button;
	import mx.controls.LinkButton;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	import mx.events.IndexChangedEvent;
	import mx.events.ResizeEvent;

	use namespace mx_internal;
	/**
	 * SuperTabNavigator used by layout mechanism.
	 * 
	 * <p>
	 * Besides the super functionality:
	 * <ul>
	 * 	<li> has 2 buttons in right (near popup button): minimize & maximize/restore.
	 * 	<li> provides additional code for close tab handler
	 * 	<li> sets the active view & corresponding styles based on focusIn/focusOut events.
	 * </ul>
	 * 
	 * @author Cristina
	 * 
	 */ 
	public class LayoutTabNavigator extends FlowerSuperTabNavigator {
		
		/**
		 * Color used on an active view.
		 * 
		 */ 
		private static const ACTIVE_VIEW_COLOR:String = "#99CCFF";
					
		/**
		 * 
		 */
		private var minimizeButton:Button;
		
		/**
		 * 
		 */
		private var maximizeRestoreButton:Button;
		
		/**
		 * 
		 */
		private var workbench:Workbench;
		
		/**
		 * 
		 */
		private var stackLayoutData:StackLayoutData;
		
		/**
		 * Stores the user applied styles before changing them.
		 * 
		 */ 		
		private var cachedStyles:ArrayCollection;	
							
		/**
		 * 
		 */
		public function LayoutTabNavigator(workbench:Workbench, stackLayoutData:StackLayoutData) {
			this.workbench = workbench;
			this.stackLayoutData = stackLayoutData;
												
			dragEnabled = false;
			dropEnabled = false;			
							
			minHeight = 50;			
			setStyle("paddingTop", 0);
			setStyle("tabHeight", 24);
			
			addEventListener(Event.ADDED_TO_STAGE, addedToStageHandler);			
			addEventListener(IndexChangedEvent.CHANGE, changeIndexHandler);
		
			addEventListener(SuperTabEvent.TAB_CLOSE, closeTabHandler);
			addEventListener(MouseEvent.CLICK, tabClickHadler, true);
			addEventListener(MouseEvent.DOUBLE_CLICK, tabDoubleClickHadler);
			
			if (isSuitableFlashPlayerVersionForMiddleClickEvent()) {
				addEventListener(MouseEvent.MIDDLE_CLICK, tabMiddleClickHadler);
			}
		}		
	    
		/**
		 * Middle click event is available starting from Flash Player 11.2.
		 */ 
		private function isSuitableFlashPlayerVersionForMiddleClickEvent():Boolean {
			var flashVersion:Object = new Object();// Object to hold the Flash version details			
			var versionArray:Array = Capabilities.version.split(",");// Split it up
			var osAndVersion:Array = versionArray[0].split(" ");// The main version contains the OS (e.g. WIN), so we split that off as well.
			
			flashVersion["major"] = parseInt(osAndVersion[1]);
			flashVersion["minor"] = parseInt(versionArray[1]);
			
			return (flashVersion["major"] > 11) || (flashVersion["major"] == 11 && flashVersion["minor"] >= 2);			
		}
		
		/**
		 * Sets event listeners.
		 * 
		 */ 
		private function addedToStageHandler(event:Event):void {
			BindingUtils.bindSetter(updatePercent, DividedBox(parent), "direction");
			BindingUtils.bindSetter(updateState, stackLayoutData, "mrmState");				
		}
		
		/**
		 * Refreshes the current selection for the workbench (which will use it for the context menu)
		 * 
		 * @author Cristina Constantinescu
		 * @author Mircea Negreanu
		 */ 
		private function tabClickHadler(event:MouseEvent):void {
			if (event.target.parent is SuperTabBar) {
				var viewLayoutData:ViewLayoutData = ViewLayoutData(stackLayoutData.children.getItemAt(SuperTabBar(event.target.parent).getChildIndex(DisplayObject(event.target))));
				
				workbench.selectedViewLayoutData = viewLayoutData;
			}			
		}
		
		/**
		 * Maximize/Restores the active stack based on its current state.
		 * 
		 * @see Workbench#maximizeRestoreActiveStackLayoutData()
		 */ 
		private function tabDoubleClickHadler(event:MouseEvent):void {
			if (event.target != null && DisplayObject(event.target).parent is FlowerSuperTab)
				workbench.maximizeRestoreActiveStackLayoutData();
		}
		
		private function tabMiddleClickHadler(event:MouseEvent):void {
			if (event.target != null && DisplayObject(event.target).parent is FlowerSuperTab) {				
				var viewLayoutData:ViewLayoutData = ViewLayoutData(stackLayoutData.children.getItemAt(tabBar.getChildIndex(DisplayObject(event.target).parent)));
				workbench.closeView(workbench.layoutDataToComponent[viewLayoutData]);
			}
		}
		
		/**
		 * When the parent's direction changes, sets the correponding percentWidth/percentHeight to 100. <br>
		 * For a vertical box direction, all children must have 100 percentWidth. <br>
		 * For a horizontal box direction, all children must have 100 percentHeight.
		 * 
		 * 
		 */ 
		private function updatePercent(value:Object):void {
			if (value == BoxDirection.HORIZONTAL) {
				this.percentHeight = 100;
			} else {
				this.percentWidth = 100;
			}
		}

		/**
		 * When the <code>stackLayoutData</code> mrmState changes, the method makes the corresponding graphical changes:
		 * <p>
		 * If new state is minimized the tab navigator will not be included in layout.
		 * <p>
		 * The <code>maximizeRestoreButton</code> icon changes based on the new state.
		 * <p>
		 * The parent is notificated about changes by calling the LayoutDividedBox#computeMinimized().
		 * 
		 * 
		 */ 
		private function updateState(value:Object):void {
			var minimized:Boolean = (value == StackLayoutData.FORCED_MINIMIZED || value == StackLayoutData.USER_MINIMIZED);
			this.includeInLayout = !minimized;
			this.visible = !minimized;
			
			LayoutDividedBox(parent).computeMinimized(minimized);
			
			if (maximizeRestoreButton == null) {
				return;
			}
			
			if (value == StackLayoutData.NORMAL) {
				maximizeRestoreButton.setStyle("icon", UtilAssets.INSTANCE.tabMax);
			} else {
				maximizeRestoreButton.setStyle("icon", UtilAssets.INSTANCE.tabRes);				
			}			
		}
	
		override protected function createChildren():void {					
			super.createChildren();	
	       	      	      		      	    	       	
		 	minimizeButton = new LinkButton();			
		 	minimizeButton.setStyle("paddingLeft", 0);
		 	minimizeButton.setStyle("paddingRight", 0);		 	
		 	minimizeButton.setStyle("icon", UtilAssets.INSTANCE.tabMin);		
		 	minimizeButton.addEventListener(MouseEvent.CLICK, minimizeButtonHandler);	
			minimizeButton.tabEnabled = false;
		 	canvas.addChild(minimizeButton);
		 	
		 	maximizeRestoreButton = new LinkButton();		
		 	maximizeRestoreButton.setStyle("paddingLeft", 0);
		 	maximizeRestoreButton.setStyle("paddingRight", 0);
		 	maximizeRestoreButton.setStyle("icon", UtilAssets.INSTANCE.tabMax);			
		 	maximizeRestoreButton.addEventListener(MouseEvent.CLICK, maximizeRestoreButtonHandler);		
			maximizeRestoreButton.tabEnabled = false;
		 	canvas.addChild(maximizeRestoreButton);		
		 	// listener added to recalculate positions and dimensions
		 	canvas.addEventListener(ResizeEvent.RESIZE, resizeCanvasHandler); 
	
			popupButton.tabEnabled = false;
		}
		
		/**
		 * Each time a ResizeEvent is dispatched, the minimize/maximize buttons must be repositioned based on canvas width.
		 * Also the tab navigator <code>minWidth</code> is recalculated so that minimize/maximize buttons and all tabs to be visible.
		 * 
		 */ 
		private function resizeCanvasHandler(event:ResizeEvent):void {
			minimizeButton.x = canvas.width - minimizeButton.measuredWidth*2;
			maximizeRestoreButton.x = canvas.width - maximizeRestoreButton.measuredWidth;
			
			// TODO Cristina: de vazut cum calculam minWidth pentru tab navigator
			this.minWidth = popupButton.width + minimizeButton.width + maximizeRestoreButton.width;			
		}
		
		/**
		 * When pressing the minimize button, Workbench#resize method is called with USER_MINIMIZED parameter 
		 * to operate on the layoutdata and on this graphical component.
		 * <p>
		 * The active view will be considered to be the previous one.
		 * 
		 * 
		 */
		private function minimizeButtonHandler(event:MouseEvent):void {
			workbench.minimize(stackLayoutData);			
		}
		
		/**
		 * Removes the corresponding tab layout data from layout structure.
		 * The graphical component is also updated.
		 * 
		 * <p>
		 * If the component is an editor, dispatch the <code>ViewClosedEvent</code>
		 * as closing the editors is prevented to allow the user to save any changes.
		 * 
		 * <p>
		 * Note : the <code>preventDefault</code> is called because the graphical component is
		 * deleted along with the layout, so the default event listener doesn't need to delete it anymore.
		 * 
		 * 
		 */ 
		private function closeTabHandler(event:SuperTabEvent):void {			
			var viewLayoutData:ViewLayoutData = ViewLayoutData(stackLayoutData.children.getItemAt(event.tabIndex));
			
			workbench.closeView(workbench.layoutDataToComponent[viewLayoutData]);	
			
			event.preventDefault();			
		}
		
		/**
		 * Depending on the #mrmState property value, if :
		 * - MAXIMIZED - it will restore this graphical component
		 * - NORMAL - it will maximize this graphical component
		 * 
		 * 
		 */
		private function maximizeRestoreButtonHandler(event:MouseEvent):void {
			if (stackLayoutData.mrmState == StackLayoutData.NORMAL) {
				workbench.maximize(stackLayoutData);				
			} else {										
				workbench.restore(stackLayoutData);					
			}			
		}
		
		/**
		 * Besides the super functionality, the method sets the selected tab to be the new active view.
		 * <p>
		 * In order not to lose the user applied styles, they are saved before any style modification.
		 * 
		 * 
		 */ 
		protected override function focusInHandler(event:FocusEvent):void {				
			// verify if mouse is under workbench area
			var rect:Rectangle = new Rectangle(workbench.x, workbench.y, workbench.width, workbench.height);			
			if (stage != null && !rect.contains(stage.mouseX, stage.mouseY)) {
				return; // don't change focus
			}	
						
			if (selectedIndex == -1) { // only if tab selected
				return;
			}
			
			var selectedObject:DisplayObject = getChildAt(selectedIndex);		
			var oldActiveView:UIComponent = workbench.activeViewList.getActiveView();        	
			if (selectedObject == oldActiveView) { // already has focus
        		return;
        	}
			
			var result:Object = workbench.arrangeTool.getLayoutDetailsUnderMouse(new Point(stage.mouseX, stage.mouseY));
			if (!(result.layout != null && (stackLayoutData.children.contains(result.layout) || stackLayoutData == result.layout))) {
				return;
			}
			
			super.focusInHandler(event); 
			
			workbench.activeViewList.setActiveView(UIComponent(selectedObject));
		}
  		
		public function setStyles():void {			
			clearAllActiveViewStyles(workbench);
					
			// set styles on new active view
			var activeTab:Button = this.getTabAt(this.getChildIndex(DisplayObject(selectedChild)));
			saveOriginalStyles(activeTab);
			setActiveViewStyles(activeTab);		
		}
  		
  		/**
  		 * When the tab index changes, then the active view styles applied are removed
  		 * and a <code>setFocus</code> is called to update the active view component.
  		 * 
  		 */ 
  		protected function changeIndexHandler(event:IndexChangedEvent):void {	
			workbench.activeViewList.setActiveView(UIComponent(this.getChildAt(event.newIndex)));
  		}
  		
  		/**
  		 * Sets styles for an active view component by coloring the <code>activeTab</code> and the content border.
  		 * 
  		 */  		  		
  		private function setActiveViewStyles(activeTab:UIComponent):void {  			
  			setStyle("borderThickness", 2);
        	setStyle("borderColor", ACTIVE_VIEW_COLOR);       	
        	activeTab.setStyle("borderColor", ACTIVE_VIEW_COLOR);                	
        	activeTab.setStyle("backgroundColor", ACTIVE_VIEW_COLOR);	
  		}
  		
  		/**
  		 * Clears the styles applied on the active view.
  		 * 
  		 */ 
  		public function clearActiveViewStyles(activeTab:UIComponent = null):void {			
  			if (cachedStyles == null) {
  				return;
  			}
  			setStyle("borderThickness", cachedStyles[0]["borderThickness"]); 
  			setStyle("borderColor", cachedStyles[0]["borderColor"]);  
        	if (activeTab != null) {
				activeTab.setStyle("borderColor", cachedStyles[1]["borderColor"]);        	        	
        		activeTab.setStyle("backgroundColor", cachedStyles[1]["backgroundColor"]);
			}
  		}  	
		
		public static function clearAllActiveViewStyles(workbench:Workbench):void {  	
			var stacks:ArrayCollection = new ArrayCollection();
			workbench.getAllStackLayoutData(workbench.rootLayout, stacks);
			for each (var stack:StackLayoutData in stacks) {
				var component:LayoutTabNavigator = LayoutTabNavigator(workbench.layoutDataToComponent[stack]);
				if (component != null) {					
					if (component.selectedIndex != -1) {
						component.clearActiveViewStyles(component.getTabAt(component.selectedIndex));
					} else {
						component.clearActiveViewStyles();
					}
				}
			}			
		}  	
  		
  		/**
  		 * Saves the user applied styles in order to be restored later.
  		 * 
  		 */ 
  		private function saveOriginalStyles(activeTab:UIComponent):void {
  			cachedStyles = new ArrayCollection();
  			var styles:Dictionary = new Dictionary();  			  		
  			styles["borderThickness"] = getStyle("borderThickness");
  			styles["borderColor"] = getStyle("borderColor");
  			cachedStyles.addItem(styles);
  			
  			styles = new Dictionary();
  			styles["borderColor"] = activeTab.getStyle("borderColor");
  			styles["backgroundColor"] = activeTab.getStyle("backgroundColor");
  			cachedStyles.addItem(styles);		
  		}
		
		/**
		 * Adds a round rectangle to frame the tabs.
		 */ 
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			var g:Graphics = graphics;
			g.clear();		
			g.lineStyle(1, 0xcccccc, 1, true);
			g.drawRoundRectComplex(holder.x, holder.y, unscaledWidth - 1, tabBarHeight + 1, 5, 5, 0, 0);
			g.endFill();
			
			super.updateDisplayList(unscaledWidth, unscaledHeight);
		}
	}
	
}
