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
package org.flowerplatform.flex_client.core.editor.remote.update {
	import org.flowerplatform.flex_client.core.editor.remote.Node;
		
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass(alias="org.flowerplatform.core.node.update.remote.Update")]
	public class Update {
		
		public var type:String;
		
		public var fullNodeId:String;
		
		public var timestamp:Number;
		
		public function apply(nodeRegistry:*, node:Node):void {
			// do nothing
		}
		
	}
}