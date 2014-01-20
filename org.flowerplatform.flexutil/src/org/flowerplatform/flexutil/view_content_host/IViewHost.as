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
package org.flowerplatform.flexutil.view_content_host {
	import mx.collections.IList;
	
	/**
	 * Can wrap a single IViewContent (web), or multiple <code>IViewContent</code>s (mobile, where 2 VCs are visible and 
	 * others (e.g. editors) may exist, but are currently hidden).
	 * 
	 * <p>
	 * The VH should detect when the active VH is changed. 
	 * When such a thing happens => invoke <code>setActiveViewContent()</code>. E.g.: 
	 * 
	 * <ul>
	 * <li>on first display</li>
	 * <li>if there are multiple VCs (e.g. mobile) => on focus in or when the current VC is changed 
	 * (e.g. editor switch, open resource from tree, switch to right view)</li>
	 * <li>if there are several <code>IViewHost</code> on screen, catch the corresponding "activated" event 
	 * (e.g. for web/Workbench)</li>
	 * </ul>
	 * 
	 * <p>
	 * The VH should detect when the active VC is closed, or when the VH is closed. When this happens =>
	 * invoke <code>FlexUtilGlobals.getInstance().selectionManager.viewContentRemoved()</code>.
	 * 
	 * @author Cristian Spiescu
	 */
	public interface IViewHost extends ICachedActionsAndSelectionProvider {
		
		/**
		 * Should not be called directly. This method is
		 * invoked by the <code>ISelectionManager</code>, when the selection changes, or by when
		 * the view is activated (including during init).
		 * 
		 * <p>
		 * This method should get the selection (and probably actions as well), and should return
		 * the selection.
		 * 
		 * <p>
		 * It may probably cache the selection and actions (needed by the context menu framework for web/mobile).
		 */
		function selectionChanged():IList;	

		/**
		 * Should hold the VC (if there is only one), or the active
		 * one (if there are multiple VCs).
		 */
		function get activeViewContent():IViewContent;
		
		/**
		 * Besides setting the corresponding attribute, should invoke 
		 * <code>FlexUtilGlobals.getInstance().selectionManager.viewContentActivated()</code>. If the caller
		 * is a focus in style handler, then <code>viaFocusIn</code> should be set to <code>true</code>.
		 */
		function setActiveViewContent(value:IViewContent, viaFocusIn:Boolean = false):void;
		
		function setLabel(value:String):void;
		function setIcon(value:Object):void;
		
		function displayCloseButton(value:Boolean):void;
		function addToControlBar(value:Object):void;
		
		/**		
		 * The view host should use this method to open a menu
		 * at given coordinates, populated with actions having <code>parentActionId</code>
		 * as their parent.
		 * 
		 * @param x, y - the coordinates where the menu must be open.
		 * @param contextToMerge - context used by actions (IAction.context)
		 * @param parentActionId - if not null, the actions having this id as parentId will be displayed; 
		 * 							if null, all actions structure will be displayed.
		 * 
		 * @author Cristina Constantinescu
		 */ 
		function openMenu(x:Number, y:Number, contextToMerge:Object, parentActionId:String = null):Boolean;
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		function showSpinner(text:String):void;
		function hideSpinner():void;
	}
}
