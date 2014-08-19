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
package org.flowerplatform.flex_client.codesync.regex.action {
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexConstants;
	import org.flowerplatform.flex_client.resources.Resources;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ShowMatchesGroupedByRegexAction extends ShowOrderedMatchesAction {
		
		public function ShowMatchesGroupedByRegexAction() {			
			label = Resources.getMessage("regex.action.grouped");
			icon = Resources.bricksIcon;
		}
					
		override protected function getExpandContext():Object {
			var context:Object = super.getExpandContext();
			context[CodeSyncRegexConstants.SHOW_GROUPED_BY_REGEX] = true;
			return context;
		}
		
	}
}