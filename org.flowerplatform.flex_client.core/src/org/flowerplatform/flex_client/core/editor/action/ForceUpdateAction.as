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
package org.flowerplatform.flex_client.core.editor.action {
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	import spark.formatters.DateTimeFormatter;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ForceUpdateAction extends ActionBase {
		
		public function ForceUpdateAction() {
			super();
			
			parentId = CoreConstants.DEBUG;
			updateLabel();			
		}
		
		public function updateLabel():void {
			if (CorePlugin.getInstance().lastUpdateTimestampOfClient == -1) {
				label = "No resource updates requested yet";
			} else {
				var formatter:DateTimeFormatter = new DateTimeFormatter();
				formatter.dateTimePattern = "yyyy-MM-dd HH:mm:ss";							
				label = "Last update: " + formatter.format(new Date(CorePlugin.getInstance().lastUpdateTimestampOfClient));
			}
		}
		
		override public function run():void {
			CorePlugin.getInstance().serviceLocator.invoke("resourceService.ping");
		}
		
	}
}
