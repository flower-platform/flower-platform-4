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
package org.flowerplatform.flex_client.team.git.history {
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.history.ui.GitHistoryView;
	import org.flowerplatform.flexutil.layout.AbstractViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 *	@author Vlad Bogdan Manica
	 */ 
	public class GitHistoryViewProvider extends AbstractViewProvider {
		
		public static const ID:String = "gitHistory";
		
		override public function getId():String {
			return ID;
		}
		
		override public function createView(viewLayoutData:ViewLayoutData):UIComponent {		
			return new GitHistoryView();
		}	
		
		override public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return Resources.getMessage("gitHistory.view");
		}		
		
		override public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return Resources.gitHistoryIcon;
		}	
	}
}