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
	import mx.core.UIComponent;
	
	/**
	 * Interface that needs to be implemented in order to provide graphical support for <code>ViewLayoutData</code>.
	 * Objects that implements this interface must provide functionality for the following methods:
	 * <ul>
	 * 	<li> getId() - gets the id for view provider. Needed when creating views.
	 * 	<li> getView() - creates and returns a graphical component corresponding to a <code>ViewLayoutData</code>;
	 * 					 this graphical component will be inserted in a tab navigator. 
	 * 	<li> geTitle() - returns the title for a <code>ViewLayoutData</code>
	 *  <li> geIcon() - returns the icon for a <code>ViewLayoutData</code>
	 * 	<li> getTabCustomizer() - returns the tab customizer for a <code>ViewLayoutData</code>
	 * 	<li> getViewPopupWindow() - returns the popup window instance to be used when opening a view in a window.
	 * </ul>
	 * 
	 * @author Sorin
	 * @author Cristina
	 * 
	 */
	public interface IViewProvider {
		
		
		function getId():String;
		
		/**		  
		 * 
		 */		 
		function createView(viewLayoutData:ViewLayoutData):UIComponent;
		 
		/**
		 * 
		 */
		function getTitle(viewLayoutData:ViewLayoutData = null):String;
		 
		/**
		 * 
		 */
		function getIcon(viewLayoutData:ViewLayoutData = null):Object;
		 
		 /**
		  * 
		  */
		function getTabCustomizer(viewLayoutData:ViewLayoutData):Object;
		
		function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent;
		 
	}
	
}