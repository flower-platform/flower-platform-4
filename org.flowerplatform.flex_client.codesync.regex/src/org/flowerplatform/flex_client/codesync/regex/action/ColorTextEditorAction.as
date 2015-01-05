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
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexConstants;
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexPlugin;
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.IEditorFrontendAware;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.text.TextEditorFrontend;
	import org.flowerplatform.flexutil.action.MultipleSelectionActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ColorTextEditorAction extends MultipleSelectionActionBase implements IEditorFrontendAware {
		
		private var _editorFrontend:EditorFrontend;
		public static const ID:String = "org.flowerplatform.flex_client.codesync.regex.action.ColorTextEditorAction";
				
		public function ColorTextEditorAction() {
			super();
		}
		
		public function set editorFrontend(value:EditorFrontend):void {
			_editorFrontend = value;
		}
		
		public function get editorFrontend():EditorFrontend {			
			return _editorFrontend;
		}		
				
		override public function get visible():Boolean {
			if (super.visible) {				
				colorTextEditorFrontend(selection);
			}
			return false;
		}
				
		private function colorTextEditorFrontend(selection:IList):void {			
			var array:Array = new Array();
			for (var i:int = 0; i < selection.length; i++) {						
				var node:Node = Node(selection.getItemAt(i));
				computeMatches(node, array);
			}
			
			var textEditorFrontend:TextEditorFrontend = CodeSyncRegexPlugin.getInstance().getTextEditorFrontend(editorFrontend);
			if (textEditorFrontend != null) {
				textEditorFrontend.colorText(array);
			}					
		}
		
		private function computeMatches(node:Node, result:Array):void {	
			if (node.type == CodeSyncRegexConstants.REGEX_MATCH_TYPE) {
				result.push(createObj(node));
			}
			if (node.children != null) {
				for each (var child:Node in node.children) {
					computeMatches(child, result);					
				}
			}			
		}
		
		private function createObj(node:Node):Object {
			var obj:Object = new Object();
			obj.startLine = node.getPropertyValue(CodeSyncRegexConstants.START_L);
			obj.startChar = node.getPropertyValue(CodeSyncRegexConstants.START_C);
			obj.endLine = node.getPropertyValue(CodeSyncRegexConstants.END_L);
			obj.endChar = node.getPropertyValue(CodeSyncRegexConstants.END_C);
			
			obj.children = new Array();
			if (node.children != null) {
				for each (var child:Node in node.children) {
					obj.children.push(createObj(child));
				}
			}
			return obj;
		}
		
	}
}
