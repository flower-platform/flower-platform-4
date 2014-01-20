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
package  org.flowerplatform.flexutil.layout {
	import mx.collections.ArrayCollection;
	
	/**
	 * Represents the <code>LayoutData</code> entity without children.
	 * <p>
	 * Stores information used for entity identification.
	 *  
	 * @author Cristina
	 * 
	 */ 
	public class ViewLayoutData extends LayoutData {
		
		/**
		 * Represents the id of this layout.
		 * 
		 */ 
		public var viewId:String;
		
		/**
		 * For the moment, could be the node id corresponding to the file to be opened for an editor.
		 * 
		 */
		public var customData:String;
		
		/**
		 * 
		 */
		public var isEditor:Boolean;
		
		/**
		 * A view is "undocked" when it is displayed as a popup.
		 * It isn't contained in the workspace data.
		 * 
		 * @see ViewPopupWindow
		 */ 
		public var isUndocked:Boolean;
		
		/**
		 * Stores the coordonates and dimensions of an undock view.
		 * 
		 * <p>
		 * Format: [x, y, width, height]
		 */ 
		public var dimensions:ArrayCollection = new ArrayCollection();
		
		/**
		 * 
		 */
		public function ViewLayoutData(viewId:String=null, customData:String=null) {
			this.viewId = viewId;
			this.customData = customData;
		}
		
	}
	
}