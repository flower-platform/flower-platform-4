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
package org.flowerplatform.flexutil.layout {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;

	public interface IWorkbench {
		function addEditorView(viewLayoutData:ViewLayoutData, setFocusOnView:Boolean = false, existingComponent:UIComponent = null):UIComponent;
		function closeViews(views:ArrayCollection /* of UIComponent */, shouldDispatchEvent:Boolean = true):void;
		function getComponent(viewId:String, customData:String = null):UIComponent;
		function closeView(view:IEventDispatcher, shouldDispatchEvent:Boolean = true):void;
		function setActiveView(newActiveView:UIComponent, setFocusOnNewView:Boolean = true, dispatchActiveViewChangedEvent:Boolean = true, restoreIfMinimized:Boolean = true):void;
	}
}