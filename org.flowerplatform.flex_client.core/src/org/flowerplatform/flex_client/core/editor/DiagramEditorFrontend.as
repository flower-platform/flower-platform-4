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
package org.flowerplatform.flex_client.core.editor {

	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellAwareProcessor;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	import spark.components.HGroup;
	
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
									
			var diagramRenderer:DiagramRenderer = new DiagramRenderer();
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
			diagramShell.rootModel = new Node(editorInput);
			
			actionProvider.composedActionProviderProcessors.push(new DiagramShellAwareProcessor(diagramShell));			
			diagramShell.selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangedHandler);
						
			addElement(editorArea);				
		}
		
		protected function createDiagramShell():DiagramShell {
			throw new Error("Must provide a diagram shell!");
		}
		
		override protected function subscribeResultCallback(resourceNode:Node):void {
			super.subscribeResultCallback(resourceNode);
			nodeUpdateProcessor.requestChildren(diagramShell.getNewDiagramShellContext(), null);
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
