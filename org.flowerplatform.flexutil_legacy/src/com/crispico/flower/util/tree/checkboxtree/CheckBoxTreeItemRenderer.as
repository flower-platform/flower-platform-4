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
	
	import com.crispico.flower.util.tree.checkboxtree.CheckBoxExtended;
	import com.crispico.flower.util.tree.checkboxtree.CheckBoxTree;
	import com.crispico.flower.util.tree.checkboxtree.CheckBoxTreeListData;
	
	import flash.events.MouseEvent;
	
	import mx.controls.treeClasses.TreeItemRenderer;
	import mx.core.mx_internal;
	import mx.events.TreeEvent;
	
	use namespace mx_internal;

	/**
	 * Item renderer for <code>CheckBoxTree</code>.
	 * Based on the <code>showCheckBox</code> property value,
	 * displayes or not a checkbox near the icon in tree.
	 *  
	 * @see http://www.sephiroth.it/file_detail.php?id=151#
	 * 
	 * @author Cristina
	 */
	public class CheckBoxTreeItemRenderer extends TreeItemRenderer {
		
		protected var _checkbox: CheckBoxExtended;
		protected var _listOwner: CheckBoxTree;
		
		public var showCheckBox:Boolean = false;
		
		public function CheckBoxTreeItemRenderer() {
			super();
		}
		
		protected function onCheckboxClick(event:MouseEvent):void {
			if (_listOwner.isBranch(this.data) && checkBox.middle) {
				checkBox.middle = false;
				checkBox.selected = !checkBox.selected;
			}
			_listOwner.dispatchEvent(new TreeEvent("itemCheck", false, false, data, this));
		}
		
		override protected function createChildren( ): void {
			super.createChildren();
			if (!showCheckBox) {
				return;
			}
			if (!_checkbox) {
				_checkbox = new CheckBoxExtended();
				_checkbox.allow3StateForUser = false;
				_checkbox.addEventListener(MouseEvent.CLICK, onCheckboxClick);
				addChild(_checkbox);
			}
		}
		
		override protected function measure():void {
			super.measure();
			
			if (!showCheckBox) {
				return;
			}
			
			var w:Number = data ? (listData as CheckBoxTreeListData).indent : 0;
	
			if (disclosureIcon)
				w += disclosureIcon.width;
	
			if (icon)
				w += icon.measuredWidth;
	
			if (label.width < 4 || label.height < 4) {
				label.width  = 4;
				label.height = 16;
			}
			
			if (isNaN(explicitWidth)) {
				w += label.getExplicitOrMeasuredWidth();
				w += _checkbox.getExplicitOrMeasuredWidth();	
				measuredWidth  = w;
				measuredHeight = Math.max(_checkbox.getExplicitOrMeasuredHeight(), label.getExplicitOrMeasuredHeight());
			} else {
				label.width = Math.max(explicitWidth - (w + _checkbox.getExplicitOrMeasuredWidth()), 4);
				measuredHeight = Math.max(_checkbox.getExplicitOrMeasuredHeight(), label.getExplicitOrMeasuredHeight());
				
				if (icon && icon.measuredHeight > measuredHeight)
					measuredHeight = icon.measuredHeight;
			}
		}
		
		override protected function commitProperties( ): void {
			super.commitProperties();
			
			if (data != null) {
				_listOwner = (listData.owner as CheckBoxTree);
				if (showCheckBox) {				
					_checkbox.middle   = (listData as CheckBoxTreeListData).checkedState == 2;
					_checkbox.selected = (listData as CheckBoxTreeListData).checkedState > 0;
				}
			}
		}
		
		override protected function updateDisplayList(unscaledWidth: Number, unscaledHeight: Number): void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			if (!showCheckBox) {
				return;
			}
			
			var startx: Number = data ? (listData as CheckBoxTreeListData).indent : 0;
			
			if (disclosureIcon) {
				disclosureIcon.x = startx;
				startx = disclosureIcon.x + disclosureIcon.width;
				disclosureIcon.setActualSize( disclosureIcon.width, disclosureIcon.height );
				disclosureIcon.visible = data ? (listData as CheckBoxTreeListData).hasChildren : false;
			}
			
			if(checkBox) {
				_checkbox.x = startx;
				startx = checkBox.x + checkBox.measuredWidth;
				checkBox.setActualSize(checkBox.measuredWidth, checkBox.measuredHeight);
			}
			
			if (icon)	{
				icon.x = startx;
				startx = icon.x + icon.measuredWidth;
				icon.setActualSize(icon.measuredWidth, icon.measuredHeight);
			}
						
			label.x = startx;
			label.setActualSize(unscaledWidth - startx, measuredHeight);
	
			var verticalAlign:String = getStyle("verticalAlign");
			
			if (verticalAlign == "top")	{
				label.y = 0;
				_checkbox.y = 0;
				
				if (icon)
					icon.y = 0;
				if (disclosureIcon)
					disclosureIcon.y = 0;
			} else if (verticalAlign == "bottom") {
				label.y = unscaledHeight - label.height + 2; // 2 for gutter
				_checkbox.y = unscaledHeight - checkBox.height + 2; // 2 for gutter
				if (icon)
					icon.y = unscaledHeight - icon.height;
				if (disclosureIcon)
					disclosureIcon.y = unscaledHeight - disclosureIcon.height;
			} else {
				label.y = (unscaledHeight - label.height) / 2;
				checkBox.y = (unscaledHeight - checkBox.height) / 2;
				if (icon)
					icon.y = (unscaledHeight - icon.height) / 2;
				if (disclosureIcon)
					disclosureIcon.y = (unscaledHeight - disclosureIcon.height) / 2;
			}
		}		
		
		public function get checkBox( ): CheckBoxExtended {
			return _checkbox;
		}
	}
}