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
package com.crispico.flower.util.tree.checkboxtree {
	
	import mx.controls.listClasses.ListBase;
	import mx.controls.treeClasses.TreeListData;

	/**	 
	 * @see http://www.sephiroth.it/file_detail.php?id=151#
	 * 
	 * @author Cristina
	 */
	public class CheckBoxTreeListData extends TreeListData {
		
		public var checkedState: uint = 0;
		
		public function CheckBoxTreeListData(text: String, uid: String, owner: ListBase, rowIndex: int = 0, columnIndex: int = 0) {
			super(text, uid, owner, rowIndex, columnIndex);
		}		
	}
	
}