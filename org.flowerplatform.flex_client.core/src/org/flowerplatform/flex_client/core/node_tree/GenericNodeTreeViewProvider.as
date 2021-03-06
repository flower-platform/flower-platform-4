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
package org.flowerplatform.flex_client.core.node_tree {

	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.layout.AbstractViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Claudiu Matei
	 */
	public class GenericNodeTreeViewProvider extends AbstractViewProvider {
		
		public static const ID:String = "nodeTree";
		
		override public function getId():String {
			return ID;
		}
		
		override public function createView(viewLayoutData:ViewLayoutData):UIComponent {			
			return new NodeTree();
		}
		
		override public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			return Resources.getMessage("nodeTree.view");
		}
		
		override public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return Resources.propertiesIcon;
		}
	
	}
}