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
package com.crispico.flower.flexdiagram.action {
	import com.crispico.flower.flexdiagram.action.IMenuEntrySortable;
	
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.Button;
	import mx.controls.HRule;
	import mx.controls.Spacer;
	
	/**
	 * @author Daniela
	 */ 
	public class HRuleContextMenuEntry extends VBox implements IMenuEntrySortable {
		
		private var _sortIndex:int;
		
		protected static const WHITE_SPACE_PADDING_TOP:int = 1;
		
		protected static const WHITE_SPACE_PADDING_BOTTOM:int = 1;
		
		protected static const RULE_THICKNESS:int = 2;
		
		public function HRuleContextMenuEntry(sortIndex:int = int.MAX_VALUE) {
			super();
			_sortIndex = sortIndex;

			this.percentWidth = 100;
			this.setStyle("verticalGap", 0);
			
			var spacer:Spacer = new Spacer();
			spacer.minHeight = 0;
			spacer.height = WHITE_SPACE_PADDING_TOP;
			this.addChild(spacer);
			
			var hRule:HRule = new HRule();
			hRule.minHeight = 0;
			hRule.height = RULE_THICKNESS;
			hRule.percentWidth = 100;
			hRule.setStyle("strokeWidth", RULE_THICKNESS);
			hRule.setStyle("shadowColor", 0xFFFFFF);
			this.addChild(hRule);
			spacer = new Spacer();
			spacer.minHeight = 0;
			spacer.height = WHITE_SPACE_PADDING_BOTTOM;
			this.addChild(spacer);
		}
		
		public function get sortIndex():int {
			return _sortIndex;
		}
	}
}