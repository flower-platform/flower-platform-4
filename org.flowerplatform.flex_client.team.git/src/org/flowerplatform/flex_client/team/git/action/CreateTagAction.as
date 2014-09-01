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
	import org.flowerplatform.flex_client.team.git.ui.CreateTagView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Brinza
	 */
	public class CreateTagAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.team.git.action.CreateTagAction";
		
		public function CreateTagAction() {
			super();
			
			label = Resources.getMessage("flex_client.team.git.action.createTag");
			icon = Resources.createTag;
			orderIndex = 240;
		}
		
		override public function get visible():Boolean {
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node && Node(selection.getItemAt(0)).type == GitConstants.GIT_TAGS_TYPE;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var view:CreateTagView = new CreateTagView();
			
			view.nodeUri = node.nodeUri;
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(view)
				.setWidth(450)
				.setHeight(390)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}