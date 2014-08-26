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
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.ui.FetchView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Brinza
	 */
	public class FetchAction extends ActionBase {
		
		public function FetchAction() {
			super();

			icon = Resources.fetchIcon;
		}
	
		
		override public function get visible(): Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is Node) {
				var node:Node = Node(selection.getItemAt(0));
				
				if (CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type).categories.getItemIndex(GitConstants.GIT_CATEGORY) >= 0
					&& node.type != GitConstants.GIT_REMOTE_TYPE) {
					label = Resources.getMessage("flex_client.team.git.ui.FetchAction.fetchOnGit");
					return true;
				} else if (node.type == GitConstants.GIT_REMOTE_TYPE) {
					label = Resources.getMessage("flex_client.team.git.ui.FetchAction.fetchOnRemote");
					return true;
				}
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			
			if (CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(node.type).categories.getItemIndex(GitConstants.GIT_CATEGORY) >= 0
				&& node.type != GitConstants.GIT_REMOTE_TYPE) {
				// show wizard
				var fetchView:FetchView = new FetchView();
				
				fetchView.nodeUri = node.nodeUri;
				
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
					.setViewContent(fetchView)
					.setWidth(500)
					.setHeight(400)
					.setTitle(label)	
					.setIcon(icon)
					.show();
			} else {				
				// call action
				CorePlugin.getInstance().serviceLocator.invoke("GitService.fetch", [node.nodeUri, null, null],
					function (result:String):void {
						FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setText(result)
						.setTitle(Resources.getMessage('flex_client.team.git.ui.FetchView.fetchResult'))
						.setWidth(300)
						.setHeight(200)
						.showMessageBox();
					});
			}
		}
	}
}