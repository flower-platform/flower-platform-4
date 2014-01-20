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
package org.flowerplatform.flexutil.samples.global_menu
{
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * Sample action for the GlobalMenu sample.
	 * 
	 * <p>It becomes invisible when hideOn is selected</p>
	 * 
	 * @author Mircea Negreanu
	 */
	public class SampleActionGlobalMenu1 extends ActionBase {
		public var hideOn:String = null;
		public var showOn:String = null;
		
		public function SampleActionGlobalMenu1() {
			super();
		}
		
		/**
		 * <ul>
		 * 	<li>if hideOn is set: if selection is equal to hideOn return false, else return true</li>
		 *  <li>if showOn is set: if selection is equal to showOn return true, else return false</li>
		 * 	<li>return super.visible</li>
		 * </ul>
		 */
		override public function get visible():Boolean {
			if (selection == null || selection.length == 0) {
				return super.visible;
			}
			if (hideOn == null && showOn == null) {
				return super.visible;
			} else if (hideOn != null) {
				if (selection.getItemAt(0) == hideOn) {
					return false;
				}
				
				return true;
			} else if (showOn != null) {
				if (selection.getItemAt(0) == showOn) {
					return true;
				}
				
				return false;
			}
			
			return super.visible;
		}
		
		override public function get label():String {
			if (super.label != null) {
				return super.label;
			}
			if (selection == null || selection.length == 0) {
				return "Action for: empty";
			} else {
				var sel:Object = selection.getItemAt(0);
				var result:Object;
				if (sel is XML) {
					result = "XML";
				} else {
					result = sel;
				}
				return "Action for: " + result;
			}
		}
		
		override public function run():void {
			trace("run for " + id + " (" + label + ")");
		}
	}
}