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
package org.flowerplatform.flex_client.mindmap {
	
	import mx.utils.ObjectUtil;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.DiagramEditorFrontend;
	import org.flowerplatform.flex_client.core.editor.action.InplaceEditorAction;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.resource.event.ResourceNodeRemovedEvent;
	import org.flowerplatform.flex_client.core.node.event.RefreshEvent;
	import org.flowerplatform.flex_client.core.node.event.RootNodeAddedEvent;
	import org.flowerplatform.flex_client.mindmap.action.NodeDownAction;
	import org.flowerplatform.flex_client.mindmap.action.NodeLeftAction;
	import org.flowerplatform.flex_client.mindmap.action.NodePageDownAction;
	import org.flowerplatform.flex_client.mindmap.action.NodePageUpAction;
	import org.flowerplatform.flex_client.mindmap.action.NodeRightAction;
	import org.flowerplatform.flex_client.mindmap.action.NodeUpAction;
	import org.flowerplatform.flex_client.mindmap.ui.MindMapIconsBar;
	import org.flowerplatform.flex_client.properties.action.AddChildActionProvider;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.VectorActionProvider;

	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorFrontend extends DiagramEditorFrontend {
			
		public function MindMapEditorFrontend() {
			super();
			
			actionProvider.actionProviders.push(CorePlugin.getInstance().editorClassFactoryActionProvider);
			actionProvider.actionProviders.push(new AddChildActionProvider());
			
			var shortcutsActionProvider:VectorActionProvider = new VectorActionProvider();
			shortcutsActionProvider.addAction(new NodeUpAction());
			shortcutsActionProvider.addAction(new NodeDownAction());
			shortcutsActionProvider.addAction(new NodePageDownAction());
			shortcutsActionProvider.addAction(new NodePageUpAction());
			shortcutsActionProvider.addAction(new NodeLeftAction());
			shortcutsActionProvider.addAction(new NodeRightAction());
			shortcutsActionProvider.addAction(new NodeUpAction(true));
			shortcutsActionProvider.addAction(new NodeDownAction(true));
			shortcutsActionProvider.addAction(new NodePageDownAction(true));
			shortcutsActionProvider.addAction(new NodePageUpAction(true));
			shortcutsActionProvider.addAction(new NodeLeftAction(true));
			shortcutsActionProvider.addAction(new NodeRightAction(true));
			
			shortcutsActionProvider.addAction(new InplaceEditorAction());
			
			actionProvider.actionProviders.push(shortcutsActionProvider);		
						
			nodeRegistry.addEventListener(RefreshEvent.REFRESH, refreshCHandler);
			nodeRegistry.addEventListener(ResourceNodeRemovedEvent.REMOVED, resourceNodeRemovedHandler);
			nodeRegistry.addEventListener(RootNodeAddedEvent.TYPE, rootNodeAddedHandler);
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Sebastian Solomon
		 */
		override protected function createDiagramShell():DiagramShell {
			var diagramShell:MindMapEditorDiagramShell = new MindMapEditorDiagramShell();
			diagramShell.showRootModelAsRootNode = !hideRootNode;
			diagramShell.nodeRegistry = nodeRegistry;
			
			return diagramShell;
		}
		
		override protected function createChildren():void {			
			super.createChildren();
									
//			nodeRegistry.startingNode = Node(MindMapRootModelWrapper(MindMapEditorDiagramShell(diagramShell).rootModel).model);			
//			nodeRegistry.useStartingNodeAsRootNode = MindMapEditorDiagramShell(diagramShell).showRootModelAsRootNode;
			
			var iconSideBar:MindMapIconsBar = new MindMapIconsBar();
			iconSideBar.diagramShell = diagramShell;
			editorArea.addElementAt(iconSideBar, 0);
		}	
				
		override protected function subscribeResultCallback(rootNode:Node):void {
			super.subscribeResultCallback(rootNode);
			
			var mindmapDiagramShell:MindMapDiagramShell = MindMapDiagramShell(diagramShell);
			var rootNode:Node = Node(mindmapDiagramShell.getRoot(diagramShell.getNewDiagramShellContext()));
			// refresh rootNode only if it has no properties
			// properties needed to set renderer's data if showRootModelAsRootNode is true
			if (rootNode != null && ObjectUtil.getClassInfo(rootNode.properties).properties.length == 0) {
				nodeRegistry.refresh(rootNode);	
			}
			
			nodeRegistry.requestChildren(null);
		}
		
		protected function rootNodeAddedHandler(event:RootNodeAddedEvent):void {
			var mindmapDiagramShell:MindMapDiagramShell = MindMapDiagramShell(diagramShell);
			
			mindmapDiagramShell.addModelInRootModelChildrenList(diagramShell.getNewDiagramShellContext(), event.rootNode, true);	
			
			ControllerUtils.getMindMapModelController(diagramShell.getNewDiagramShellContext(), event.rootNode).setExpanded(diagramShell.getNewDiagramShellContext(), event.rootNode, true);
		}
		
		protected function refreshCHandler(event:RefreshEvent):void {
			MindMapDiagramShell(diagramShell).refreshRootModelChildren(diagramShell.getNewDiagramShellContext());
			MindMapDiagramShell(diagramShell).refreshModelPositions(diagramShell.getNewDiagramShellContext(), event.node);
		}
				
		protected function resourceNodeRemovedHandler(event:ResourceNodeRemovedEvent):void {
			var rootModel:MindMapRootModelWrapper = MindMapRootModelWrapper(diagramShell.rootModel);
			if (Node(rootModel.model).nodeUri == event.resourceNodeId) {
				// remove the editor
				FlexUtilGlobals.getInstance().workbench.closeView(this);
			} else {
				// collapse the node
				nodeRegistry.removeChildren(nodeRegistry.getNodeById(event.resourceNodeId), true);
			}
		}
		
	}
}
