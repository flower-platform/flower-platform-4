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
package org.flowerplatform.flex_client.mindmap {
	
	import flash.events.Event;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.DiagramEditorFrontend;
	import org.flowerplatform.flex_client.core.editor.action.InplaceEditorAction;
	import org.flowerplatform.flex_client.core.editor.action.OpenWithEditorActionProvider;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.action.NodeDownAction;
	import org.flowerplatform.flex_client.mindmap.action.NodeLeftAction;
	import org.flowerplatform.flex_client.mindmap.action.NodePageDownAction;
	import org.flowerplatform.flex_client.mindmap.action.NodePageUpAction;
	import org.flowerplatform.flex_client.mindmap.action.NodeRightAction;
	import org.flowerplatform.flex_client.mindmap.action.NodeUpAction;
	import org.flowerplatform.flex_client.mindmap.ui.MindMapIconsBar;
	import org.flowerplatform.flex_client.properties.action.AddChildActionProvider;
	import org.flowerplatform.flex_client.properties.action.AddSiblingActionProvider;
	import org.flowerplatform.flex_client.resources.ActionOrderIndexes;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramRenderer;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.ComposedAction;
	import org.flowerplatform.flexutil.action.VectorActionProvider;

	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorFrontend extends DiagramEditorFrontend {
		
		protected var showPropertiesInRenderer:int = 1;
		
		public var showPropertiesInRendererInternal:Boolean = true;
			
		public function MindMapEditorFrontend() {
			super();
			
			actionProvider.actionProviders.push(CorePlugin.getInstance().editorClassFactoryActionProvider);
			actionProvider.actionProviders.push(new AddChildActionProvider());
			actionProvider.actionProviders.push(new OpenWithEditorActionProvider());
			actionProvider.actionProviders.push(new AddSiblingActionProvider());
			
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
			
			shortcutsActionProvider.addAction(new ComposedAction().setId(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER).setLabel(Resources.getMessage("mindmap.editor.showPropertiesInRenderer")).setIcon(Resources.tableGear).setOrderIndex(ActionOrderIndexes.SHOW_PROPERTIES_IN_RENDERER));
			shortcutsActionProvider.addAction(new ActionBase().setOrderIndex(0).setParentId(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER).setLabel(Resources.getMessage("mindmap.editor.showPropertiesInRenderer.none")).setIcon(Resources.tableGear)
				.setFunctionDelegate(function ():void { showPropertiesInRenderer = 0; dispatchEvent(new Event(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER)) } ));
			shortcutsActionProvider.addAction(new ActionBase().setOrderIndex(1).setParentId(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER).setLabel(Resources.getMessage("mindmap.editor.showPropertiesInRenderer.descriptor")).setIcon(Resources.tableGear)
				.setFunctionDelegate(function ():void { showPropertiesInRenderer = 1; showPropertiesInRendererInternal = false; dispatchEvent(new Event(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER)) } ));
			shortcutsActionProvider.addAction(new ActionBase().setOrderIndex(2).setParentId(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER).setLabel(Resources.getMessage("mindmap.editor.showPropertiesInRenderer.descriptor.internal")).setIcon(Resources.tableGear)
				.setFunctionDelegate(function ():void { showPropertiesInRenderer = 1; showPropertiesInRendererInternal = true; dispatchEvent(new Event(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER)) } ));
			shortcutsActionProvider.addAction(new ActionBase().setOrderIndex(3).setParentId(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER).setLabel(Resources.getMessage("mindmap.editor.showPropertiesInRenderer.all")).setIcon(Resources.tableGear)
				.setFunctionDelegate(function ():void { showPropertiesInRenderer = 2; showPropertiesInRendererInternal = false; dispatchEvent(new Event(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER)) } ));
			shortcutsActionProvider.addAction(new ActionBase().setOrderIndex(4).setParentId(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER).setLabel(Resources.getMessage("mindmap.editor.showPropertiesInRenderer.all.internal")).setIcon(Resources.tableGear)
				.setFunctionDelegate(function ():void { showPropertiesInRenderer = 2; showPropertiesInRendererInternal = true; dispatchEvent(new Event(MindMapConstants.EVENT_SHOW_PROPERTIES_IN_RENDERER)) } ));
			
			actionProvider.actionProviders.push(shortcutsActionProvider);			
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Sebastian Solomon
		 */
		override protected function createDiagramShell():DiagramShell {
			var diagramShell:MindMapEditorDiagramShell = new MindMapEditorDiagramShell();			
			diagramShell.nodeRegistry = nodeRegistry;
			diagramShell.editorFrontend = this;
			
			return diagramShell;
		}
		
		override protected function createDiagramRenderer():DiagramRenderer {
			return new MindMapDiagramRenderer();
		}
		
		override protected function createChildren():void {			
			super.createChildren();

			var iconSideBar:MindMapIconsBar = new MindMapIconsBar();
			iconSideBar.diagramShell = diagramShell;
			editorArea.addElementAt(iconSideBar, 0);
		}	
		
		override protected function subscribeResultCallback(rootNode:Node, resourceNode:Node):void {
			super.subscribeResultCallback(rootNode, resourceNode);
			
			if (resourceNode == null) {
				CorePlugin.getInstance().nodeRegistryManager.expand(nodeRegistry, rootNode, null);
			} else {
				nodeRegistry.expand(rootNode, null);
			}
		}
		
		override public function resourceNodeRemoved(resourceNodeUri:String, nodeRegistry:*):void {
			super.resourceNodeRemoved(resourceNodeUri, nodeRegistry);
			if (this.nodeRegistry == nodeRegistry) {
				var rootModel:MindMapRootModelWrapper = MindMapRootModelWrapper(diagramShell.rootModel);
				if (Node(rootModel.model).nodeUri == resourceNodeUri) {
					// remove the editor
					FlexUtilGlobals.getInstance().workbench.closeView(this, true, true);
				} else {
					// collapse the node
					nodeRegistry.collapse(nodeRegistry.getNodeById(resourceNodeUri), true);
				}
			}
		}
		
		override public function getSelection():IList {
			var selection:IList = new ArrayList(diagramShell.selectedItems.source);
			for (var i:int = 0; i < diagramShell.selectedItems.length; i++) {
				var obj:Object = diagramShell.selectedItems.getItemAt(i);
				if (obj is MindMapRootModelWrapper) {					
					selection.setItemAt(MindMapRootModelWrapper(obj).model, i);
				}				
			}
			
			return selection;
		}
		
		public function shouldDisplayPropertiesInRenderer(model:Node):Boolean {
			if (showPropertiesInRenderer == 0) {
				return false;
			} else if (showPropertiesInRenderer == 2) {
				return true;
			} else {
				return CorePlugin.getInstance().nodeTypeDescriptorRegistry.getSingleController(MindMapConstants.FEATURE_SHOW_PROPERTIES_IN_RENDERER, model) != null; 
			}
		}
		
	}
}
