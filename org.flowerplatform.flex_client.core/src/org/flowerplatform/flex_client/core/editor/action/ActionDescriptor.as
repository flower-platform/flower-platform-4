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
/**
 * Wraps the ID of an action in a controller. Usage: 
 * <code>
 * 	<pre>[TypeDescriptorRegistry]
 * 	.getOrCreateTypeDescriptor([node_type])
 * 	.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, 
 * 		new ActionDescriptor([action_class_name].ID));
 * 	</pre>
 * </code>
 * 
 * For finding the descriptor when displaying actions of a selected node, the 
 * <code>[action_class_name]</code> should be registered with 
 * <code>FlexUtilGlobals.getInstance().registerAction([action_class_name]);</code>
 * @author Alina Bratu
 */
package org.flowerplatform.flex_client.core.editor.action
{
	import org.flowerplatform.flexutil.controller.AbstractController;

	public class ActionDescriptor extends AbstractController {
		
		private var _actionId:String;
		
		public function ActionDescriptor(s:String) {
			_actionId = s;
		}
		
		public function get actionId():String {
			return _actionId;
		}
		
	}
}