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
package org.flowerplatform.flex_client.core.mindmap.remote {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[Bindable]
	[RemoteClass(alias="org.flowerplatform.core.node.remote.Node")]
	public class Node {
		
		public var id:String;
				
		public var type:String;
		
		public var properties:Object = new Object();
		
		[Transient]
		public var parent:Node;
		
		[Transient]
		public var children:ArrayCollection;
		
		[Transient]
		public var side:int = MindMapDiagramShell.POSITION_RIGHT;
			
	}
}