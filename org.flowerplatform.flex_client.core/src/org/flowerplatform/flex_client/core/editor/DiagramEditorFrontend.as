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
package org.flowerplatform.flex_client.core.editor {

	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	
	import spark.components.HGroup;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellAwareProcessor;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public class DiagramEditorFrontend extends EditorFrontend {
		
		public var diagramShell:DiagramShell;
		
		protected var toolbarArea:HGroup;
		protected var editorArea:HGroup;
		
		override protected function createChildren():void {	
			super.createChildren();	
			
			toolbarArea = new HGroup();
			toolbarArea.percentWidth = 100;
			toolbarArea.verticalAlign = "middle";
			toolbarArea.gap = 10;
			toolbarArea.paddingLeft = 5;
			addElement(toolbarArea);	
						
			editorArea = new HGroup();
			editorArea.percentWidth = 100;
			editorArea.percentHeight = 100;
			editorArea.gap=1;
			editorArea.paddingRight = 0;
									
			var diagramRenderer:DiagramRenderer = createDiagramRenderer();
			diagramRenderer.useGrid = false;			
			diagramRenderer.horizontalScrollPosition = diagramRenderer.verticalScrollPosition = 0;
			
			var scroller:InfiniteScroller = new InfiniteScroller();
			scroller.percentWidth = 100;
			scroller.percentHeight = 100;
			scroller.viewport = diagramRenderer;
			editorArea.addElement(scroller);
			
			diagramShell = createDiagramShell();
			diagramShell.registry = CorePlugin.getInstance().nodeTypeDescriptorRegistry;
			diagramShell.typeProvider = CorePlugin.getInstance().nodeTypeProvider;
			diagramShell.diagramRenderer = diagramRenderer;
			
			actionProvider.composedActionProviderProcessors.push(new DiagramShellAwareProcessor(diagramShell));			
			diagramShell.selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangedHandler);
						
			addElement(editorArea);				
		}
		
		protected function createDiagramShell():DiagramShell {
			throw new Error("Must provide a diagram shell!");
		}
		
		protected function createDiagramRenderer():DiagramRenderer {
			return new DiagramRenderer();
		}
		
		override protected function subscribeResultCallback(rootNode:Node, resourceNode:Node):void {
			super.subscribeResultCallback(rootNode, resourceNode);
			diagramShell.rootModel = rootNode;
		}
		
		protected function selectionChangedHandler(e:CollectionEvent):void {
			// CollectionEvent.COLLECTION_CHANGE will be triggered even when an item is updated (CollectionEventKind.UPDATE), so ignore it
			if (!diagramShell.selectedItems.eventsCanBeIgnored && e.kind != CollectionEventKind.UPDATE) { // catch events only if necessary
				FlexUtilGlobals.getInstance().selectionManager.selectionChanged(viewHost, this);
			}
		}
		
		override public function getSelection():IList {			
			return diagramShell.selectedItems;
		}
		
	}
}
