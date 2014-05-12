/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flexutil.samples.context_menu {
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.samples.renderer.MultipleIconItemRendererSample;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class SampleAction1 extends ActionBase {
		public function SampleAction1() {
			super();
			icon = MultipleIconItemRendererSample.infoImage;
			preferShowOnActionBar = true;
		}
		
		override public function get label():String {
			if (selection == null || selection.length == 0) {
				return "Action for: empty";
			} else {
				return "Action for: " + selection.getItemAt(0);
			}
		}
		
	}
}