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
	
	import spark.components.HGroup;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.update.MindMapNodeUpdateProcessor;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellAwareProcessor;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public class DiagramEditorFrontend extends EditorFrontend {
		
		public var diagramShell:DiagramShell;
		
		override protected function createChildren():void {	
			// toolbar
			var toolbarsArea:HGroup = new HGroup();
			toolbarsArea.percentWidth = 100;
			toolbarsArea.verticalAlign = "middle";
			toolbarsArea.gap = 10;
			toolbarsArea.paddingLeft = 5;
			addElement(toolbarsArea);	
			
			var scroller:InfiniteScroller = new InfiniteScroller();
			scroller.percentWidth = 100;
			scroller.percentHeight = 100;
			addElement(scroller);
			
			var diagramRenderer:DiagramRenderer = new DiagramRenderer();
			diagramRenderer.useGrid = false;		
			scroller.viewport = diagramRenderer;
			diagramRenderer.horizontalScrollPosition = diagramRenderer.verticalScrollPosition = 0;
			
			diagramShell = createDiagramShell();
			diagramShell.registry = CorePlugin.getInstance().nodeTypeDescriptorRegistry;
			diagramShell.typeProvider = CorePlugin.getInstance().nodeTypeProvider;
			diagramShell.diagramRenderer = diagramRenderer;
			diagramShell.rootModel = new Node(editorInput);
			
			actionProvider.composedActionProviderProcessors.push(new DiagramShellAwareProcessor(diagramShell));
			
			diagramShell.selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangedHandler);
			
			super.createChildren();		
		}
		
		protected function createDiagramShell():DiagramShell {
			throw new Error("Must provide a diagram shell!");
		}
		
		override public function getContext():DiagramShellContext {
			return diagramShell.getNewDiagramShellContext();
		}
		
		override protected function subscribeResultCallback(rootNode:Node):void {
			super.subscribeResultCallback(rootNode);
			MindMapNodeUpdateProcessor(updateProcessor).requestChildren(getContext(), null);
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