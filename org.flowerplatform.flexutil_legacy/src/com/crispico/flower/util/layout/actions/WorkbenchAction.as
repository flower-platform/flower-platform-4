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
package com.crispico.flower.util.layout.actions {
	import com.crispico.flower.util.layout.Workbench;
	
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * Base class for actions that affects/use the workbench
	 * 
	 * @author Mircea Negreanu
	 */
	public class WorkbenchAction extends ActionBase {
		private var _workbench:Workbench;
		
		public function WorkbenchAction(bench:Workbench) {
			super();
			
			this.workbench = bench;
		}
		
		public function get workbench():Workbench {
			return _workbench;
		}
		
		public function set workbench(bench:Workbench):void {
			_workbench = bench;
		}
	}
}