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
package org.flowerplatform.flex_client.text {
	
	import flash.events.Event;
	
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.node.NodeRegistry;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.iframe.FlowerIFrame;
	import org.flowerplatform.flexutil.iframe.IFlowerIFrame;
	import org.flowerplatform.flexutil.iframe.StageWebViewUIComponent;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class TextEditorFrontend extends EditorFrontend {
		
		private var node:Node;
		private var readOnly:Boolean;	
//		private var creationComplete:Boolean;
		
		private static const EDITOR_PAGE:String = "codemirror/codeMirrorEditor.html";
		
		protected var editor:UIComponent;
		
		public function TextEditorFrontend() {			
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			if (FlexUtilGlobals.getInstance().isMobile) {					
				editor = new StageWebViewUIComponent();					
			} else {
				editor = UIComponent(new FlowerIFrame("editor"));					
			}	
			editor.percentHeight = 100;
			editor.percentWidth = 100;
			addElement(editor);
		}
		
		protected function creationCompleteHandler(event:FlexEvent):void {			
			IFlowerIFrame(editor).addViewCompleteHandler(orionEditor_frameLoadHandler);			
		}
		
		override protected function subscribeResultCallback(rootNode:Node, resourceNode:Node):void {
			this.node = rootNode;
			
			IFlowerIFrame(editor).url = FlexUtilGlobals.getInstance().createAbsoluteUrl(TextPlugin.getInstance().getResourceUrl(getURL()));
		}
			
		protected function getURL():String {
			var lastDotIndex:int = node.nodeUri.lastIndexOf('.');
			if (lastDotIndex >= 0) {
				// has an extension
				var extension:String = node.nodeUri.substring(lastDotIndex + 1);
				return EDITOR_PAGE + "?extension=" + extension + "&isMobile=" + FlexUtilGlobals.getInstance().isMobile;
			}
			return EDITOR_PAGE;
		}
		
		protected function orionEditor_frameLoadHandler(event:Event):void {
			if (node != null) {	
				// initialize code mirror editor read only
				IFlowerIFrame(editor).callJSFunction("initialize", null, escape(node.properties[TextConstants.CONTENT]), "id", readOnly);
				// listen for text changes
				IFlowerIFrame(editor).addCallback("codeMirrorEditorChangedHandler", textEditorChangedHandler);
			}
		}
		
		public function setContent(content:String):void {
			IFlowerIFrame(editor).callJSFunction("setContent", null, escape(content));
		}
		
		public function getContent(callback:Function):void {
			IFlowerIFrame(editor).callJSFunction("getContent", callback, null);
		}
		
//		public function disableEditing():void {
//			if (creationComplete) {
//				ICodeMirrorEditor(editor).callJavaScriptMethod("disableEditing");
//			} else {
//				readOnly = true;
//			}				
//		}
		
//		public function enableEditing():void {
//			if (creationComplete) {
//				ICodeMirrorEditor(editor).callJavaScriptMethod("enableEditing");
//			} else {
//				readOnly = false;
//			}				
//		}
		
		protected function updateText(offset:int, oldTextLength:int, newText:String):void {
			IFlowerIFrame(editor).callJSFunction("updateText", null, offset, oldTextLength, escape(newText));
		}
		
		public function colorText(ranges:Array):void {
			IFlowerIFrame(editor).callJSFunction("colorText", null, ranges);
		}
		
//		public function executeContentUpdateLogic(content:Object, isFullContent:Boolean):void {				
//			var updates:ArrayCollection = ArrayCollection(content);
//			if (isFullContent) {
//				// Taking the first because probably only one exists with fullText.
//				var update:TextEditorUpdate = TextEditorUpdate(updates[0]); 
//				this.content = update.newText;
//			} else { 
//				// apply the updates
//				for each (var update:TextEditorUpdate in updates) {						
//					updateText(update.offset, update.oldTextLength, update.newText);
//				}
//			}
//		}	
		
		protected function textEditorChangedHandler(statefulClientId:String, offset:String, oldText:String, newText:String):void {
//			var updates:ArrayCollection = new ArrayCollection();
//			
//			var update:TextEditorUpdate = new TextEditorUpdate();
//			update.offset = int(offset);
//			update.oldTextLength = oldText.length;
//			update.newText = newText;
//			updates.addItem(update);
//			
//			// here we must know where to send those updates
//			// ind the statefulClient based on the id sent at code mirror initialisation
//			var editorStatefulClient:EditorStatefulClient = 
//				EditorStatefulClient(CommunicationPlugin.getInstance().statefulClientRegistry.getStatefulClientById(statefulClientId));
//			
//			// for the moment the last editorFontend is used -> problems when using multiple editorFrontends
//			// TO DISCUSS: how can we know the exact editorFrontend
//			editorStatefulClient.attemptUpdateContent(
//				editorStatefulClient.editorFrontends[editorStatefulClient.editorFrontends.length - 1], updates);
		}
	}
}