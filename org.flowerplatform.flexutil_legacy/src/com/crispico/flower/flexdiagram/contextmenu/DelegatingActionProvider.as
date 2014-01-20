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
package com.crispico.flower.flexdiagram.contextmenu {
	
	import com.crispico.flower.flexdiagram.action.ActionContext;
	import com.crispico.flower.flexdiagram.action.IActionProvider2;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	
	public class DelegatingActionProvider implements IActionProvider2 {
		
		private var getContextFunction:Function;
		
		private var fillContextMenuFunction:Function;
		
		public function DelegatingActionProvider(getContextFunction:Function, fillContextMenuFunction:Function) {
			this.getContextFunction = getContextFunction;
			this.fillContextMenuFunction = fillContextMenuFunction;
		}

		public function getContext():ActionContext {
			if (this.getContextFunction != null)
				return this.getContextFunction();
			return new ActionContext();
		}

		public function fillContextMenu(contextMenu:FlowerContextMenu):void {
			this.fillContextMenuFunction(contextMenu);
		}
	}
}