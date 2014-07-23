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
package org.flowerplatform.flex_client.core.node.controller {
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.core.mx_internal;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexutil.controller.AbstractController;
	import org.flowerplatform.flexutil.controller.ControllerEntry;
	import org.flowerplatform.flexutil.controller.TypeDescriptor;

	use namespace mx_internal;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class TypeDescriptorRegistryDebugControllers {
		
		public const TYPES:String = "debugFlexTypes";
		public const TYPE:String = "debugFlexType";
		
		public const CATEGORY:String = "debugFlexCategory";
		public const CONTROLLER_KEY_SINGLE:String = "debugFlexControllerKeySingle";
		public const CONTROLLER_KEY_ADDITIVE:String = "debugFlexControllerKeyAdditive";
		
		public const CONTROLLER_SINGLE:String = "debugFlexControllerSingle";
		public const CONTROLLER_ADDITIVE:String = "debugFlexControllerAdditive";
		
		public function registerControllers():void {
			
			///////////////////////////////////////////////////
			// add types to "Flex Types"
			///////////////////////////////////////////////////
			
			var nodeController:FlexTypesNodeController = new FlexTypesNodeController(this);
			
			addNodeController(TYPES, nodeController);
			addNodeController(TYPE, nodeController);
			addNodeController(CATEGORY, nodeController);
			addNodeController(CONTROLLER_KEY_SINGLE, nodeController);
			addNodeController(CONTROLLER_KEY_ADDITIVE, nodeController);
			addNodeController(CONTROLLER_SINGLE, nodeController);
			addNodeController(CONTROLLER_ADDITIVE, nodeController);
		}
		
		private function addNodeController(type:String, controller:FlexTypesNodeController):void {
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(type)
				.addSingleController(FlexDiagramConstants.MINDMAP_MODEL_CONTROLLER, controller);
		}
		
		public function getFlexTypes():ArrayCollection {
			var types:ArrayCollection = new ArrayCollection();
			for (var type:String in CorePlugin.getInstance().nodeTypeDescriptorRegistry.typeDescriptors) {
				types.addItem(type);
			}
			// show them in alphabetical order for better readability
			types.source.sort();
			
			var children:ArrayCollection = new ArrayCollection();
			for each (var type:String in types) {
				children.addItem(createNode(TYPE, TYPES, type, type, 
					Resources.getResourceUrl("/images/mindmap/icons/idea.png")));
			}
			return children;
		}
		
		public function getCategoriesAndControllerKeys(type:String):ArrayCollection {
			var descriptor:TypeDescriptor = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getExpectedTypeDescriptor(type);
			var children:ArrayCollection = new ArrayCollection();
			
			// get categories
			for each (var category:String in descriptor.categories.toArray()) {
				children.addItem(createNode(CATEGORY, type, category, category, 
					Resources.getResourceUrl("/images/mindmap/icons/folder.png"), false));
			}
			
			// get single controllers keys
			for (var singleControllersKey:String in descriptor.singleControllers) {
				var child:Node = createNode(CONTROLLER_KEY_SINGLE, type, singleControllersKey, singleControllersKey,
					Resources.getResourceUrl("/images/mindmap/icons/full-1.png"));
				var entry:ControllerEntry = descriptor.singleControllers[singleControllersKey];
				if (entry.wasCached) {
					child.properties[CoreConstants.ICONS] += CoreConstants.ICONS_SEPARATOR +
						Resources.getResourceUrl("/images/mindmap/icons/flag.png");
				}
				children.addItem(child);
			}
			
			// get additive controllers keys
			for (var additiveControllersKey:String in descriptor.additiveControllers) {
				var child:Node = createNode(CONTROLLER_KEY_ADDITIVE, type, additiveControllersKey, additiveControllersKey,
					Resources.getResourceUrl("/images/mindmap/icons/positive.png"));
				var entry:ControllerEntry = descriptor.additiveControllers[additiveControllersKey];
				if (entry.wasCached) {
					child.properties[CoreConstants.ICONS] += CoreConstants.ICONS_SEPARATOR +
						Resources.getResourceUrl("/images/mindmap/icons/flag.png");
				}
				children.addItem(child);
			}
			
			return children;
		}
		
		public function getSingleControllers(node:Node, type:String, controllerKey:String):ArrayCollection {
			var descriptor:TypeDescriptor = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getExpectedTypeDescriptor(type);
			var children:ArrayCollection = new ArrayCollection();
			
			var entry:ControllerEntry = descriptor.singleControllers[controllerKey];
			var cachedController:AbstractController = descriptor.getCachedSingleController(controllerKey, null, false, false);
			var selfController:AbstractController = AbstractController(entry.selfValue);
			
			// add cached controller
			if (cachedController != null) {
				var id:String = cachedController.toString();
				var child:Node = createNode(CONTROLLER_SINGLE, node.nodeUri, id, id, 
					Resources.getResourceUrl("/images/mindmap/icons/executable.png"), false);
				if (cachedController != selfController) {
					// override
					child.properties[CoreConstants.ICONS] += CoreConstants.ICONS_SEPARATOR +
						Resources.getResourceUrl("/images/mindmap/icons/attach.png");
				}
				children.addItem(child);
			}
			
			// add self controller - only if different from cached
			if (selfController != null && selfController != cachedController) {
				var id:String = selfController.toString();
				children.addItem(createNode(CONTROLLER_SINGLE, node.nodeUri, id, id,
					Resources.getResourceUrl("/images/mindmap/icons/button_cancel.png"), false));
			}
			
			return children;
		}
		
		public function getAdditiveControllers(node:Node, type:String, controllerKey:String):ArrayCollection {
			var descriptor:TypeDescriptor = CorePlugin.getInstance().nodeTypeDescriptorRegistry.getExpectedTypeDescriptor(type);
			var children:ArrayCollection = new ArrayCollection();
			
			var entry:ControllerEntry = descriptor.additiveControllers[controllerKey];
			var cachedControllers:IList = descriptor.getCachedAdditiveControllers(controllerKey, null, false, false);
			var selfControllers:IList = IList(entry.selfValue);
			
			// add controllers
			for each (var cachedController:AbstractController in cachedControllers) {
				var id:String = cachedController.toString();
				var child:Node = createNode(CONTROLLER_ADDITIVE, node.nodeUri, id, id,
					Resources.getResourceUrl("/images/mindmap/icons/executable.png"), false);
				if (selfControllers.getItemIndex(cachedController) < 0) {
					// contributed
					child.properties[CoreConstants.ICONS] += CoreConstants.ICONS_SEPARATOR +
						Resources.getResourceUrl("/images/mindmap/icons/attach.png");
				}
				children.addItem(child);
			}
			
			return children;
		}
		
		private function createNode(type:String, ssp:String, fragment:String, name:String, icons:String, hasChildren:Boolean = true):Node {
			var node:Node = new Node(type + ":" + ssp + "#" + fragment);
			node.type = type;			
			node.properties[CoreConstants.NAME] = name;
			node.properties[CoreConstants.ICONS] = icons;
			node.properties[CoreConstants.HAS_CHILDREN] = hasChildren;
			return node;
		}
	}
}
import mx.collections.ArrayCollection;
import mx.collections.IList;
import mx.core.mx_internal;

import org.flowerplatform.flex_client.core.editor.remote.Node;
import org.flowerplatform.flex_client.core.node.controller.TypeDescriptorRegistryDebugControllers;
import org.flowerplatform.flexdiagram.DiagramShellContext;
import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;

use namespace mx_internal;

class FlexTypesNodeController extends MindMapModelController {
	
	private var debug:TypeDescriptorRegistryDebugControllers;
	
	public function FlexTypesNodeController(debug:TypeDescriptorRegistryDebugControllers) {
		// override the default NodeController
		orderIndex = -100;
		
		this.debug = debug;
	}
	
	override public function getChildren(context:DiagramShellContext, model:Object):IList {
		return Node(model).children;
	}
	
	override public function getExpanded(context:DiagramShellContext, model:Object):Boolean {
		return Node(model).children != null && Node(model).children.length > 0;
	}
	
	override public function setExpanded(context:DiagramShellContext, model:Object, value:Boolean):void {
		var nodeRegistry:* = Object(context.diagramShell).nodeRegistry;
		var node:Node = Node(model);
		if (value) {
			var children:ArrayCollection;
			if (node.type == debug.TYPES) {
				children = debug.getFlexTypes();
			} else if (node.type == debug.TYPE) {
				children = debug.getCategoriesAndControllerKeys(node.fragment);
			} else if (node.type == debug.CONTROLLER_KEY_SINGLE) {
				children = debug.getSingleControllers(node, node.schemeSpecificPart, node.fragment);
			} else if (node.type == debug.CONTROLLER_KEY_ADDITIVE) {
				children = debug.getAdditiveControllers(node, node.schemeSpecificPart, node.fragment);
			}
			nodeRegistry.expandCallbackHandler(node, children);
		} else {
			nodeRegistry.collapse(node);
		}
	}
	
	override public function getSide(context:DiagramShellContext, model:Object):int {
		// default side
		return MindMapDiagramShell.POSITION_RIGHT;
	}
	
	override public function setSide(context:DiagramShellContext, model:Object, value:int):void {
	}
	
	override public function isRoot(context:DiagramShellContext, model:Object):Boolean {
		return Node(model).parent == null;
	}
	
}
