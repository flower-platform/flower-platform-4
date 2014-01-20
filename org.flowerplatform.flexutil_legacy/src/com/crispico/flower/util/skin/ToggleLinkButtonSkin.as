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
package com.crispico.flower.util.skin {
	
	import mx.skins.halo.LinkButtonSkin;
	
	/**
	 * Skin for a toggle <code>LinkButton</code>.
	 * http://blog.flexexamples.com/2008/09/06/creating-a-toggleable-linkbutton-control-in-flex/
	 * 
	 * @author Cristi
	 * @author Cristina	
	 */ 
	public class ToggleLinkButtonSkin extends LinkButtonSkin {
		
        override protected function updateDisplayList(w:Number, h:Number):void {
            super.updateDisplayList(w, h);

            var cornerRadius:Number = getStyle("cornerRadius");
            var rollOverColor:uint = getStyle("rollOverColor");
            var selectionColor:uint = getStyle("selectionColor");

            graphics.clear();

            switch (name) {
                case "upSkin":
                    // Draw invisible shape so we have a hit area.
                    drawRoundRect(0, 0, w, h, cornerRadius, 0, 0);
                    break;
                case "selectedUpSkin":
                case "selectedOverSkin":
                case "overSkin":
                    drawRoundRect(0, 0, w, h, cornerRadius, rollOverColor, 1);
                    break;
                case "selectedDownSkin":
                case "downSkin":
                    drawRoundRect(0, 0, w, h, cornerRadius, selectionColor, 1);
                    break;
                case "selectedDisabledSkin":
                case "disabledSkin":
                    // Draw invisible shape so we have a hit area.
                    drawRoundRect(0, 0, w, h, cornerRadius, 0, 0);
                    break;
            }
        }

	}
}