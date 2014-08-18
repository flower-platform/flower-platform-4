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
package org.flowerplatform.flex_client.team.git.action {
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.ResetView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

/**
 * @author Diana Balutoiu
 */	
	public class ResetAction extends ActionBase{
		
		public function ResetAction() {
			super();
			label = Resources.getMessage('flex_client.team.git.action.reset');
			icon = Resources.resetIcon;
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				if(node.type == GitConstants.GIT_LOCAL_BRANCHES_TYPE ||
					node.type == GitConstants.GIT_REMOTE_BRANCHES_TYPE ||
					node.type == GitConstants.GIT_TAGS_TYPE){
					return true;
				}
			}	
			return false;
			
			
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var resetView:ResetView = new ResetView();
			resetView.node = Node(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(resetView)
				.setWidth(500)
				.setHeight(530)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}