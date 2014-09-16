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
package org.flowerplatform.flex_client.codesync.regex.ui {
	
	import mx.events.FlexEvent;
	
	import spark.events.IndexChangeEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.components.TextInputWithAutoCompleteLabelComponent;
	import org.flowerplatform.flexutil.dialog.SelectObjListPopup;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class GenerateMatchFileView extends SelectObjListPopup {
		
		private var textInput:TextInputWithAutoCompleteLabelComponent;
		
		public var node:Node;
		
		public function GenerateMatchFileView() {
			super();
		}
		
		override protected function creationCompleteHandler(event:FlexEvent):void {	
			super.creationCompleteHandler(event);
			
			var changeItemListHandler:Function = function(event:IndexChangeEvent = null):void {
				textInput.inputText = Resources.getMessage('regex.default.file', [Utils.getBaseName(list.selectedItem)]);				
			};
			
			list.addEventListener(IndexChangeEvent.CHANGE, changeItemListHandler); 
			
			changeItemListHandler();
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			textInput = new TextInputWithAutoCompleteLabelComponent();
			textInput.percentWidth = 100;
			textInput.labelText = Resources.getMessage("regex.file");			
			textInput.autoCompleteLabelFormat = CorePlugin.getInstance().getRepository(node.nodeUri) + Resources.getMessage('regex.fileNameLabelText', [Resources.getMessage('regex.location')]);
			textInput.showOverride = true;
			
			addElement(textInput);
		}
		
		override protected function getViewResult():Object {
			return [super.getViewResult(), textInput.fullText, textInput.override];				
		}
		
	}
}