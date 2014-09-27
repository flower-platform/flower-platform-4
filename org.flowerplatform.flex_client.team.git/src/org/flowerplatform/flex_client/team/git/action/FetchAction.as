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
		
		public static var ID:String = "org.flowerplatform.flex_client.team.git.action.FetchAction";
		
		public function FetchAction() {
			super();

			icon = Resources.fetchIcon;
			orderIndex = 250;
		}
			
		override public function get visible(): Boolean {
			var node:Node = Node(selection.getItemAt(0));
			if (node.type == GitConstants.GIT_REPO_TYPE && !node.getPropertyValue(GitConstants.IS_GIT_REPOSITORY)) {
				// not a git repository
				return false;
			}	
			if (node.type != GitConstants.GIT_REMOTE_TYPE) {
				label = Resources.getMessage("flex_client.team.git.ui.FetchAction.fetchOnGit");
				return true;
			} else {
				label = Resources.getMessage("flex_client.team.git.ui.FetchAction.fetchOnRemote");
				return true;
			}
			return false;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			
			if (node.type == GitConstants.GIT_REMOTE_TYPE) {
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
			} else {				
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
			}
		}
	}
}