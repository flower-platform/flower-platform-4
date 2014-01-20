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
package com.crispico.flower.util.layout.view.activeview
{
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.event.ActiveViewChangedEvent;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	import com.crispico.flower.util.layout.view.LayoutTabNavigator;
	
	import mx.collections.ArrayCollection;
	import mx.core.INavigatorContent;
	import mx.core.UIComponent;
	import mx.managers.FocusManager;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.flexutil.layout.LayoutData;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * Provides functionality for active view mechanism used on workbench.
	 * It stores a history of active views in order to restore them.
	 * Cases when the active view changes:
	 * <ul>
	 * 	<li> minimizing or closing the current active view will trigger the previous view to be the active one.
	 * 	<li> restoring a view from minimized state
	 * 	<li> changing focus on other <code>LayoutTabNavigator</code>
	 * 	<li> changing tabs on the same <code>LayoutTabNavigator</code>
	 * </ul>
	 * Each time the active view changes, an <code>ActiveViewChangedEvent</code> is dispatched
	 * to notify listeners about it.
	 * 
	 * @see Workbench
	 * @see ActiveViewChangedEvent
	 * 
	 * @author Cristina
	 */ 
	public class ActiveViewList	{
		
		/**
		 * Stores a list of views in the same order they are seen as active.
		 * The last one is the current active view. 
		 */ 
		private var views:ArrayCollection = new ArrayCollection(); /* of UIComponent */
		
		private var workbench:Workbench;
		
		public function ActiveViewList(workbench:Workbench):void {
			this.workbench = workbench;		
		}
		
		/**
		 * Returns the current active view.
		 * If no active view found, returns null.
		 */ 
		public function getActiveView():UIComponent {
			if (views.length == 0) {
				return null;
			}
			return UIComponent(views.getItemAt(views.length - 1));
		}
		
		/**
		 * Sets the new active view.
		 * If <code>setFocusOnNewView</code> is true, calls the <code>setFocus</code> for corresponding parent container
		 * to update its styles.
		 * <p>
		 * Dispatches an event to notify about changes.
		 */ 
		public function setActiveView(newActiveView:UIComponent, setFocusOnNewView:Boolean = true, dispatchActiveViewChangedEvent:Boolean = true, restoreIfMinimized:Boolean = true):void {		
			if (newActiveView == null) {
				return;
			}
			
			if (restoreIfMinimized) {
				// if minimized, restore it
				var viewLayoutData:ViewLayoutData = ViewLayoutData(workbench.componentToLayoutData[newActiveView]);
				if (StackLayoutData(viewLayoutData.parent).mrmState == StackLayoutData.USER_MINIMIZED ||
					StackLayoutData(viewLayoutData.parent).mrmState == StackLayoutData.FORCED_MINIMIZED) {
					workbench.restore(StackLayoutData(viewLayoutData.parent));
				}
			}
		
			var oldActiveView:UIComponent = getActiveView();			
			if (newActiveView == oldActiveView) {
				return;
			}		
			if (oldActiveView != null && oldActiveView.focusManager != null) {
				oldActiveView.focusManager.deactivate();
			}
			if (views.contains(newActiveView)) {
				views.removeItemAt(views.getItemIndex(newActiveView));
			}
			
			views.addItem(newActiveView);
			
			if (setFocusOnNewView) {				
				LayoutTabNavigator(newActiveView.parent).selectedChild = INavigatorContent(newActiveView);
				LayoutTabNavigator(newActiveView.parent).setStyles();
			}
			if (dispatchActiveViewChangedEvent) {
				var event:ActiveViewChangedEvent = new ActiveViewChangedEvent(newActiveView, oldActiveView); 
				newActiveView.dispatchEvent(event);
				// it seems that an event cannot be redispatched; I had a strange error like: 
				// "Event" cannot be coerced to ActiveViewChangedEvent 
				event = new ActiveViewChangedEvent(newActiveView, oldActiveView); 
				workbench.dispatchEvent(event);
			}
		}
		
		/**
		 * Removes the current active view from history stack. The previous one will be considered the active view.
		 * If <setFocusOnPreviouslyView</code> is true, calls the <code>setFocus</code> for corresponding parent container to set its styles.
		 */ 		
		public function removeActiveView(setFocusOnPreviouslyView:Boolean = true):void {
			if (views.length == 0) {
				return;
			}
			var currentView:UIComponent = getActiveView();
			var viewLayoutData:ViewLayoutData = ViewLayoutData(workbench.componentToLayoutData[currentView]);
			
			LayoutTabNavigator.clearAllActiveViewStyles(workbench);
			
			if (currentView != null && currentView.focusManager != null) {
				currentView.focusManager.deactivate();
			}
			views.removeItemAt(views.length - 1);
			if (setFocusOnPreviouslyView) {
				var activeView:UIComponent = getActiveView();
				if (activeView == null) { // no more previous active views										
					if (viewLayoutData.parent.children.length > 0) { // select the tab next to removed view
						setActiveView(UIComponent(LayoutTabNavigator(workbench.layoutDataToComponent[viewLayoutData.parent]).selectedChild), true, false);
					} else { // no tab, select the first view from workbench
						var views:ArrayCollection = new ArrayCollection();
						workbench.getAllViewLayoutData(workbench.rootLayout, views);
						if (views.length > 0) {								
							setActiveView(workbench.layoutDataToComponent[views.getItemAt(0)], true, false);
						}
					}
				} else {
					// add focus only if exists
					LayoutTabNavigator(getActiveView().parent).selectedChild = INavigatorContent(getActiveView());
					LayoutTabNavigator(getActiveView().parent).setStyles();	
				}
			}
			var event:ActiveViewChangedEvent = new ActiveViewChangedEvent(getActiveView(), currentView); 
			if (getActiveView() != null) {
				getActiveView().dispatchEvent(event);
				// it seems that an event cannot be redispatched; I had a strange error like: 
				// "Event" cannot be coerced to ActiveViewChangedEvent 
				event = new ActiveViewChangedEvent(getActiveView(), currentView);
			}
			workbench.dispatchEvent(event);
		}
		
		public function getPreviousActiveViews():ArrayCollection {
			return views;
		}
		
		public function clear():void {
			views.removeAll();
		}
	}
}