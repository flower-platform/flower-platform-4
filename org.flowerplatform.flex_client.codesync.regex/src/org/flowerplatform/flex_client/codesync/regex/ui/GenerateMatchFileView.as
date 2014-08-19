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