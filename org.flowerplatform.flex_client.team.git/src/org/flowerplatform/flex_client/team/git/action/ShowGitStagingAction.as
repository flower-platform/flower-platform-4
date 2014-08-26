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
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitStagingViewProvider;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Marius Iacob
	 */	
	public class ShowGitStagingAction extends ActionBase {
		
		public static var ID:String = "org.flowerplatform.flex_client.team.git.action.ShowGitStagingAction";
		
		public function ShowGitStagingAction() {
			super();
			label = Resources.getMessage("team.git.action.GitStagingAction");
			icon = Resources.gitStagingIcon;
		}
		
		override public function run():void {
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewIdInWorkbench(GitStagingViewProvider.ID)
				.setWidth(700)
				.setHeight(600)
				.show();
		}
		
	}
}