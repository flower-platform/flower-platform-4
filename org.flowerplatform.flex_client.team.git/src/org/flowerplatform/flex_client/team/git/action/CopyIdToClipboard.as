/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.team.git.action {
	import flash.desktop.Clipboard;
	import flash.desktop.ClipboardFormats;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Andreea Tita
	 */	
	public class CopyIdToClipboard extends ActionBase {
		
		public function CopyIdToClipboard() {
			super();
			label = Resources.getMessage("gitHistory.action.CopyId");
			icon = Resources.copy;
		}
		
		override public function  get visible():Boolean {
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				return true;
			}
			return false;
		}
		
		override public function run():void {	
			Clipboard.generalClipboard.setData(ClipboardFormats.TEXT_FORMAT, Node(selection.getItemAt(0)).getPropertyValue(GitConstants.ID));	
		}
	}
}