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
package org.flowerplatform.flexutil.renderer {
	
	import mx.managers.IFocusManagerComponent;
	
	import spark.components.IconItemRenderer;
	import spark.components.LabelItemRenderer;
	
	/**
	 * Item renderer used only on web platform.
	 * 
	 * <p>
	 * Implements <code>IFocusManagerComponent</code>
	 * to dispatch focus on parent hierarchy.
	 * Needed to activate the view in workbench when the user
	 * selects an item from list.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class FocusableListItemRenderer extends IconItemRenderer implements IFocusManagerComponent {
		
		public function FocusableListItemRenderer() {
			minHeight = 22;
			setStyle("verticalAlign", "middle");
			setStyle("paddingLeft", 5);
		}
		
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {			
		}
		
	}
}