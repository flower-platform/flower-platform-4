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
package org.flowerplatform.flex_client.team.git.action
{
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.CloneRepoWizardView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	public class CloneRepoAction extends ActionBase {
		
		public function CloneRepoAction() {
			super();
			label = Resources.getMessage("git.cloneRepo.title");
			icon = Resources.uploadIcon;
//			orderIndex = 310;
		}
		
		override public function get visible():Boolean {
			
			if (selection != null && selection.length == 1 && selection.getItemAt(0) is Node) {
				var type:String = Node(selection.getItemAt(0)).type;
				if (type == GitConstants.GIT_REPO_TYPE) {
					return true;
				}
				return false;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));	
			
			var view:CloneRepoWizardView = new CloneRepoWizardView();
			view.nodeUri = node.nodeUri;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewContent(view)
				.setWidth(500)
				.setHeight(400)
				.setTitle(label)	
				.setIcon(icon)
				.show();			
		}
	}
}