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
package org.flowerplatform.flex_client.codesync {
	
	import org.flowerplatform.flex_client.core.editor.NodeTypeProvider;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncConfigTypeProvider extends NodeTypeProvider {
		
		override public function getType(model:Object):String {
			if (model is Node) {
				var type:String = Node(model).properties.template; // TODO rename this prop?
				if (type != null) {
					return type;
				}
			}			
			return super.getType(model);
		}
	}
}