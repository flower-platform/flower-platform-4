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
package org.flowerplatform.flex_client.team.git.action.history.ui {
	
	import flash.utils.Dictionary;
	
	import spark.components.Form;
	import spark.components.Group;
	import spark.components.Scroller;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	/**
	 * Component can be used to display a node's properties.
	 * 
	 * <p>
	 * Use <code>refreshForm</code> to populate with data and <code>clearForm</code> to clear data.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class GitHistoryComponent extends Scroller {
		
		protected var propertiesForm:Form;
		
		protected var currentNode:Node;
		
		public function GitHistoryComponent() {
			super();
		}
	
		public function refreshForm(node:Node, includeRawProperties:Boolean = false):void {
		}
		
		public function clearForm():void {
			propertiesForm.removeAllElements();
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			var group:Group = new Group();
			group.percentHeight = 100;
			group.percentWidth = 100;
			viewport = group;
			
			propertiesForm = new Form();
			propertiesForm.percentHeight = 100;
			propertiesForm.percentWidth = 100;
			group.addElement(propertiesForm);
		}
					
		private function getPropertiesToDisplay(node:Node, includeRawProperties:Boolean = false):Array {
			var properties:Array = new Array();
			var categories:Dictionary = new Dictionary();
		
			return properties;			
		}	
	}
}