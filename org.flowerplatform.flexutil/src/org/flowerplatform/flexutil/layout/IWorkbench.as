/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexutil.layout {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;

	public interface IWorkbench {
		function addEditorView(viewLayoutData:ViewLayoutData, setFocusOnView:Boolean = false, existingComponent:UIComponent = null, addViewInOtherStack:Boolean = false):UIComponent;
		
		function getComponentById(viewId:String, customData:String = null):UIComponent;
		function getComponent(viewLayoutData:ViewLayoutData):UIComponent;
		
		function closeViews(views:ArrayCollection /* of UIComponent */, shouldDispatchEvent:Boolean = true, canPreventDefault:Boolean = true):void;		
		function closeView(view:IEventDispatcher, shouldDispatchEvent:Boolean = true, canPreventDefault:Boolean = true):void;
		
		function getActiveView():UIComponent;
		function setActiveView(newActiveView:UIComponent, setFocusOnNewView:Boolean = true, dispatchActiveViewChangedEvent:Boolean = true, restoreIfMinimized:Boolean = true):void;
		
		function load(layoutData:Object, reuseExistingViews:Boolean = false, keepNewLayoutEditors:Boolean = false):void;
		function refreshLabels(viewLayoutData:ViewLayoutData = null):void;
		
		function getAllEditorViews(root:LayoutData, array:ArrayCollection):void;
		function getEditorFromViewComponent(viewComponent:UIComponent):UIComponent;		
		function getViewComponentForEditor(editor:UIComponent):UIComponent;
	}
}
