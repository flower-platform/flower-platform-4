/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.flex_client.text {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class TextPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:TextPlugin;
		
		public static function getInstance():TextPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			var textEditorDescriptor:TextEditorDescriptor = new TextEditorDescriptor();
			CorePlugin.getInstance().contentTypeRegistry[TextConstants.TEXT_CONTENT_TYPE] = textEditorDescriptor;
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(textEditorDescriptor);
		}
		
		override protected function registerClassAliases():void	{
			
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
		
	}
}