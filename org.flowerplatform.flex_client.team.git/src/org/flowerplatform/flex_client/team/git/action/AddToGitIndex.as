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
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.GitConstants;
	import org.flowerplatform.flex_client.team.git.GitStagingView;
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * @author Marius Iacob
	 */
	public class AddToGitIndex extends ActionBase {
		
		private var gitStagingView:GitStagingView;
	
		public function AddToGitIndex(gitStagingView:GitStagingView) {						
			super();				
			label = Resources.getMessage("team.git.action.AddToGitIndex");
			this.gitStagingView = gitStagingView;			
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length > 0;
		}	
		
		override public function run():void {
			var list:ArrayCollection = new ArrayCollection();;
			for (var i:int = 0; i < selection.length; i++) {
				list.addItem(Node(selection.getItemAt(i)).getPropertyValue(GitConstants.FILE_PATH));
			}
			gitStagingView.addToGitIndex(list);
		}
		
	}
}