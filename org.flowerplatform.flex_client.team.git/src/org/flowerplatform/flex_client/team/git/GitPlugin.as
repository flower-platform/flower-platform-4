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
package org.flowerplatform.flex_client.team.git {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.team.git.action.CreateStructureDiffFromGitCommitsAction;
	import org.flowerplatform.flexutil.Utils;

	/**
	 * @author Valentina-Camelia Bojan
	 */
	public class GitPlugin  extends AbstractFlowerFlexPlugin {
		protected static var INSTANCE:GitPlugin;
		
		public static function getInstance():GitPlugin {
			return INSTANCE;
		}
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) +
								" already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			CorePlugin.getInstance().serviceLocator.addService("GitService");
			
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(CreateStructureDiffFromGitCommitsAction);
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
	}
}