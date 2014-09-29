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
package org.flowerplatform.flex_client.core {

	import flash.external.ExternalInterface;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	import mx.messaging.ChannelSet;
	import mx.messaging.channels.AMFChannel;
	
	import org.flowerplatform.flex_client.core.editor.BasicEditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.ContentTypeRegistry;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.UpdateTimer;
	import org.flowerplatform.flex_client.core.editor.action.ActionDescriptor;
	import org.flowerplatform.flex_client.core.editor.action.DownloadAction;
	import org.flowerplatform.flex_client.core.editor.action.ForceUpdateAction;
	import org.flowerplatform.flex_client.core.editor.action.NodeTypeActionProvider;
	import org.flowerplatform.flex_client.core.editor.action.OpenAction;
	import org.flowerplatform.flex_client.core.editor.action.OpenWithEditorComposedAction;
	import org.flowerplatform.flex_client.core.editor.action.RedoAction;
	import org.flowerplatform.flex_client.core.editor.action.RemoveNodeAction;
	import org.flowerplatform.flex_client.core.editor.action.RenameAction;
	import org.flowerplatform.flex_client.core.editor.action.UndoAction;
	import org.flowerplatform.flex_client.core.editor.action.UploadAction;
	import org.flowerplatform.flex_client.core.editor.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.NodeWithChildren;
	import org.flowerplatform.flex_client.core.editor.remote.PreferencePropertyWrapper;
	import org.flowerplatform.flex_client.core.editor.remote.PropertyWrapper;
	import org.flowerplatform.flex_client.core.editor.remote.StylePropertyWrapper;
	import org.flowerplatform.flex_client.core.editor.remote.SubscriptionInfo;
	import org.flowerplatform.flex_client.core.editor.remote.update.ChildrenUpdate;
	import org.flowerplatform.flex_client.core.editor.remote.update.PropertyUpdate;
	import org.flowerplatform.flex_client.core.editor.remote.update.Update;
	import org.flowerplatform.flex_client.core.editor.ui.AboutView;
	import org.flowerplatform.flex_client.core.editor.ui.OpenNodeView;
	import org.flowerplatform.flex_client.core.link.ILinkHandler;
	import org.flowerplatform.flex_client.core.link.LinkView;
	import org.flowerplatform.flex_client.core.node.FlexHostInvocator;
	import org.flowerplatform.flex_client.core.node.FlexHostResourceOperationsHandler;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.ResourceDebugControllers;
	import org.flowerplatform.flex_client.core.node.controller.TypeDescriptorRegistryDebugControllers;
	import org.flowerplatform.flex_client.core.node.remote.GenericValueDescriptor;
	import org.flowerplatform.flex_client.core.node_tree.GenericNodeTreeViewProvider;
	import org.flowerplatform.flex_client.core.node_tree.NodeTreeAction;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.core.service.UpdatesProcessingServiceLocator;
	import org.flowerplatform.flex_client.core.shortcut.AssignHotKeyAction;
	import org.flowerplatform.flex_client.core.users.UserAuthenticationManager;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.controller.ITypeProvider;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Pair;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.ClassFactoryActionProvider;
	import org.flowerplatform.flexutil.action.ComposedAction;
	import org.flowerplatform.flexutil.action.VectorActionProvider;
	import org.flowerplatform.flexutil.controller.AbstractController;
	import org.flowerplatform.flexutil.controller.TypeDescriptor;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRemote;
	import org.flowerplatform.flexutil.iframe.FlowerIFrameViewProvider;
	import org.flowerplatform.flexutil.iframe.IFrameOpenUrl;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.layout.Perspective;
	import org.flowerplatform.flexutil.service.ServiceLocator;
	import org.flowerplatform.flexutil.spinner.ModalSpinner;
	import org.flowerplatform.js_client.common_js_as.node.IHostServiceInvocator;

	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class CorePlugin extends AbstractFlowerFlexPlugin {
			
		/**
		 * Stores the main page name. Used to create the application's full url.
		 * @see getAppUrl()
		 */ 
		private static const MAIN_PAGE:String = "main.jsp";
		
		protected static var INSTANCE:CorePlugin;
		
		public var serviceLocator:ServiceLocator;
		
		public var perspectives:Vector.<Perspective> = new Vector.<Perspective>();
		
		public var editorClassFactoryActionProvider:ClassFactoryActionProvider = new ClassFactoryActionProvider();
		
		// actions per type registry: stores for each actionId an action factory 
		public var nodeTypeActionProvider:NodeTypeActionProvider = new NodeTypeActionProvider();

		public var updateTimer:UpdateTimer;
		
		public var nodeTypeDescriptorRegistry:TypeDescriptorRegistry = new TypeDescriptorRegistry();

		public var nodeTypeProvider:ITypeProvider;
		
		public var contentTypeRegistry:ContentTypeRegistry = new ContentTypeRegistry();
								
		public var globalMenuActionProvider:VectorActionProvider = new VectorActionProvider();
				
		public var lastUpdateTimestampOfServer:Number = -1;
		public var lastUpdateTimestampOfClient:Number = -1;
		
		public var channelSet:ChannelSet;
		
		public var userAuthenticationManager:UserAuthenticationManager = new UserAuthenticationManager();
		
		public static function getInstance():CorePlugin {
			return INSTANCE;
		}

		/**
		 * The underlying variable is from the global namespace, defined within the js file.
		 */
		public function get nodeRegistryManager():* {
			return _nodeRegistryManager;
		}
		
		public function get resourceNodesManager():FlexHostResourceOperationsHandler {
			return FlexHostResourceOperationsHandler(nodeRegistryManager.resourceOperationsManager.resourceOperationsHandler);
		}
		
		/**
		 * key = command name as String (e.g. "openResources")
		 * value = parameters as String (e.g. text://file1,file2,file3)
		 */ 
		public var linkHandlers:Dictionary;
		
		/**
		 * @author Alina Bratu
		 */
		public function getCustomResourceUrl(resource:String):String {
			return "servlet/load/" + resource;
		}
		
		/**
		 * @author Sebastian Solomon
		 */
		public function getEditorClassFactoryActionProvider():ClassFactoryActionProvider {
			return editorClassFactoryActionProvider;
		}
			
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
				
			correspondingJavaPlugin = "org.flowerplatform.core";
						
			channelSet = new ChannelSet();
			channelSet.addChannel(new AMFChannel(null, FlexUtilGlobals.getInstance().rootUrl + 'messagebroker/remoting-amf'));
		
			serviceLocator = new UpdatesProcessingServiceLocator(channelSet);
			serviceLocator.addService("coreService");
			serviceLocator.addService("nodeService");
			serviceLocator.addService("resourceService");
			serviceLocator.addService("downloadService");
			serviceLocator.addService("uploadService");
			serviceLocator.addService("preferenceService");
			serviceLocator.addService("userService");
			
			var resourceOperationsHandler:FlexHostResourceOperationsHandler = new FlexHostResourceOperationsHandler();
			_nodeRegistryManager = new NodeRegistryManager(resourceOperationsHandler, IHostServiceInvocator(serviceLocator), new FlexHostInvocator());
			
 			updateTimer = new UpdateTimer(5000);
			
			FlexUtilGlobals.getInstance().registerAction(RemoveNodeAction);
			FlexUtilGlobals.getInstance().registerAction(RenameAction);
			FlexUtilGlobals.getInstance().registerAction(OpenAction);
			FlexUtilGlobals.getInstance().registerAction(OpenWithEditorComposedAction);
		
			FlexUtilGlobals.getInstance().registerAction(NodeTreeAction);
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new GenericNodeTreeViewProvider());
			
			editorClassFactoryActionProvider.addActionClass(UndoAction);
			editorClassFactoryActionProvider.addActionClass(RedoAction);
			
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new FlowerIFrameViewProvider());
			
			// check version compatibility with server side
			serviceLocator.invoke("coreService.getVersions", null, 
				function (result:Object):void {		
					// disable app version check
//					if (_appVersion != result[0]) { // application version is old 
//						FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
//						.setTitle(Resources.getMessage('version.error'))
//						.setText(Resources.getMessage('version.error.message', [_appVersion, result[0], Resources.getMessage('version.error.details')]))
//						.setWidth(400)
//						.setHeight(300)
//						.showMessageBox();						
//					} else
					if (_apiVersion != result[1]) { // API version is old
						FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setTitle(Resources.getMessage('version.error'))
						.setText(Resources.getMessage('version.error.message', [_apiVersion, result[1], Resources.getMessage('version.error.details')]))
						.setWidth(400)
						.setHeight(300)
						.showMessageBox();
					} else { // versions ok 
						_appVersion = result[0];
						_apiVersion = result[1];						
					}
					
					// INITIALIZATION SPINNER END
					ModalSpinner.removeGlobalModalSpinner();
				}
			);

			serviceLocator.invoke("nodeService.getRegisteredTypeDescriptors", null,
				function(result:Object):void {
					var list:ArrayCollection = ArrayCollection(result);
					for (var i:int = 0; i < list.length; i++) {
						var remote:TypeDescriptorRemote = TypeDescriptorRemote(list.getItemAt(i));
						
						// create new type descriptor with remote type
						var descriptor:TypeDescriptor = null;
						if (Utils.beginsWith(remote.type, FlexUtilConstants.CATEGORY_PREFIX)) {
							descriptor = nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(remote.type);
						} else {
							descriptor = nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(remote.type);
						}
						
						// add static categories
						for each (var category:String in remote.categories) {
							descriptor.addCategory(category);
						}
						
						// add single controllers
						for (var singleControllerType:String in remote.singleControllers) {
							descriptor.addSingleController(singleControllerType, remote.singleControllers[singleControllerType]);
						}
						
						// add additive controllers
						for (var additiveControllerType:String in remote.additiveControllers) {
							for each (var additiveController:AbstractController in remote.additiveControllers[additiveControllerType]) {
								descriptor.addAdditiveController(additiveControllerType, additiveController);
							}
						}
					}
				}
			);
			
			nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(FlexUtilConstants.CATEGORY_ALL)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(NodeTreeAction.ID))
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(OpenAction.ID))
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(OpenWithEditorComposedAction.ID))
				.addSingleController(CoreConstants.NODE_TITLE_PROVIDER, new GenericValueProviderFromDescriptor(CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR))
				.addSingleController(CoreConstants.NODE_ICONS_PROVIDER, new GenericValueProviderFromDescriptor(CoreConstants.PROPERTY_FOR_ICONS_DESCRIPTOR));
						
			nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CoreConstants.FILE_NODE_TYPE)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(RenameAction.ID))
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(RemoveNodeAction.ID));
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				FlexUtilGlobals.getInstance().registerAction(DownloadAction);
				FlexUtilGlobals.getInstance().registerAction(UploadAction);
				
				nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CoreConstants.FILE_SYSTEM_NODE_TYPE)
					.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(DownloadAction.ID))
					.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(UploadAction.ID));
				
				nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CoreConstants.FILE_NODE_TYPE)
					.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(DownloadAction.ID))
					.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(UploadAction.ID));				
			}
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				FlexUtilGlobals.getInstance().registerAction(DownloadAction);
				FlexUtilGlobals.getInstance().registerAction(UploadAction);
				
				nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CoreConstants.FILE_SYSTEM_NODE_TYPE)
					.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(DownloadAction.ID))
					.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(UploadAction.ID));
				
				nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CoreConstants.FILE_NODE_TYPE)
					.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(DownloadAction.ID))
					.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(UploadAction.ID));				
			}
			
			new TypeDescriptorRegistryDebugControllers().registerControllers();
			new ResourceDebugControllers().registerControllers();
			
			linkHandlers = new Dictionary();
			
			if (ExternalInterface.available) {
				// on mobile, it's not available
				ExternalInterface.addCallback("handleLink", handleLink);
			}			
			
			// add actions to global menu
			
			globalMenuActionProvider.addAction(new ComposedAction().setLabel(Resources.getMessage("menu.file")).setId(CoreConstants.FILE_MENU_ID).setOrderIndex(10));
			
			registerActionToGlobalMenu(resourceNodesManager.saveAction);
			registerActionToGlobalMenu(resourceNodesManager.saveAllAction);
			registerActionToGlobalMenu(resourceNodesManager.reloadAction);
									
			// Navigate Menu
			globalMenuActionProvider.addAction(new ComposedAction().setLabel(Resources.getMessage("menu.navigate")).setId(CoreConstants.NAVIGATE_MENU_ID).setOrderIndex(20));
						
			// get/follow link action
			registerActionToGlobalMenu(new ActionBase()
				.setLabel(Resources.getMessage("link.title"))
				.setIcon(Resources.externalLinkIcon)
				.setId("global_org.flowerplatform.flex_client.core.LinkAction")
				.setParentId(CoreConstants.NAVIGATE_MENU_ID)
				.setFunctionDelegate(function ():void {
					FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
					.setViewContent(new LinkView())
					.setWidth(500)
					.setHeight(250)
					.setTitle(Resources.getMessage("link.title"))
					.setIcon(Resources.externalLinkIcon)
					.show();
				}));
			
			// open node action
			registerActionToGlobalMenu(new ActionBase()
				.setLabel(Resources.getMessage("open.node.action.label"))
				.setIcon(Resources.openResourceIcon)
				.setId("global_org.flowerplatform.flex_client.core.OpenAction")
				.setParentId(CoreConstants.NAVIGATE_MENU_ID)
				.setFunctionDelegate(function ():void {
					FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
					.setViewContent(new OpenNodeView())
					.setTitle(Resources.getMessage("open.node.action.label"))
					.setIcon(Resources.openResourceIcon)
					.setWidth(400)
					.setHeight(150)
					.show();
				}));
			
			registerActionToGlobalMenu(new IFrameOpenUrl()
				.setLabel(Resources.getMessage("iframe.title"))
				.setIcon(Resources.urlIcon)
				.setParentId(CoreConstants.NAVIGATE_MENU_ID)
			);
							
			// Debug Menu
			globalMenuActionProvider.addAction(new ComposedAction().setLabel(Resources.getMessage("menu.debug")).setId(CoreConstants.DEBUG).setOrderIndex(100));	
						
			// force update		
			registerActionToGlobalMenu(new ForceUpdateAction());
			
			// open root action
			registerActionToGlobalMenu(new ActionBase()
				.setLabel(Resources.getMessage("open.root.action.label"))
				.setIcon(Resources.openIcon)
				.setParentId(CoreConstants.DEBUG)
				.setFunctionDelegate(function ():void {
					CorePlugin.getInstance().handleLinkForCommand(CoreConstants.OPEN_RESOURCES, "virtual:user/repo|root");
				}));
			
			// Tools menu
			globalMenuActionProvider.addAction(new ComposedAction().setLabel(Resources.getMessage("menu.tools")).setId(CoreConstants.TOOLS_MENU_ID).setOrderIndex(30));	
			
			// assign hot key action
			registerActionToGlobalMenu(new AssignHotKeyAction());
			
			// Help menu
			globalMenuActionProvider.addAction(new ComposedAction().setLabel(Resources.getMessage("menu.help")).setId(CoreConstants.HELP).setOrderIndex(500));
			
			registerActionToGlobalMenu(new ActionBase()
				.setLabel(Resources.getMessage("about.flower.action.label"))
				.setIcon(Resources.flowerIcon)
				.setParentId(CoreConstants.HELP)
				.setFunctionDelegate(function ():void {
					FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
					.setViewContent(new AboutView())
					.setWidth(300)
					.setHeight(250)
					.show();
				}));			
			
			registerActionToGlobalMenu(new ActionBase()
				.setLabel("Show Current User")
				.setParentId(CoreConstants.DEBUG)
				.setFunctionDelegate(function():void {
					userAuthenticationManager.getCurrentUser();
				}));
			
			registerActionToGlobalMenu(new ActionBase()
				.setLabel("Logout")
				.setParentId(CoreConstants.DEBUG)
				.setFunctionDelegate(function():void {
					userAuthenticationManager.logout();
				}));
		}
				
		override protected function registerClassAliases():void {		
			super.registerClassAliases();
			registerClassAliasFromAnnotation(Node);
			registerClassAliasFromAnnotation(Update);
			registerClassAliasFromAnnotation(PropertyUpdate);
			registerClassAliasFromAnnotation(ChildrenUpdate);
			registerClassAliasFromAnnotation(NodeWithChildren);
			registerClassAliasFromAnnotation(FullNodeIdWithChildren);
			registerClassAliasFromAnnotation(PropertyWrapper);
			registerClassAliasFromAnnotation(StylePropertyWrapper);
			registerClassAliasFromAnnotation(PreferencePropertyWrapper);
			registerClassAliasFromAnnotation(SubscriptionInfo);
			registerClassAliasFromAnnotation(PropertyWrapper);
			registerClassAliasFromAnnotation(StylePropertyWrapper);
			registerClassAliasFromAnnotation(TypeDescriptorRemote);
			registerClassAliasFromAnnotation(GenericValueDescriptor);
			registerClassAliasFromAnnotation(AddChildDescriptor);
			registerClassAliasFromAnnotation(Pair);			
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
		
		public function getPerspective(id:String):Perspective {
			for (var i:int = 0; i < perspectives.length; i++) {
				if (perspectives[i].id == id) {
					return perspectives[i];
				}
			}
			return null;
		}


		/**
		 * @author Mariana Gheorghe
		 * @author Claudiu Matei
		 * @author Cristina Constantinescu
		 */
		public function getSubscribableResource(node:Node, contentType:String = null):Pair {			
			if (!node.properties[CoreConstants.USE_NODE_URI_ON_NEW_EDITOR]) {
				var subscribableResources:ArrayCollection = node == null ? null : ArrayCollection(node.properties[CoreConstants.SUBSCRIBABLE_RESOURCES]);
				if (subscribableResources != null && subscribableResources.length > 0) {
					var index:int = 0;
					if (contentType != null) {
						for (index = 0; index < subscribableResources.length; index++) {
							if (Pair(subscribableResources.getItemAt(index)).b == contentType) {															
								break;
							}
						}
					}
					return Pair(subscribableResources.getItemAt(index));
				}
			}		
			return null;
		}
		
		/**
		 * @author Mariana Gheorghe
		 * @author Claudiu Matei
		 * @author Cristina Constantinescu
		 */
		public function openEditor(node:Node, ct:String = null, addEditorInRight:Boolean = false):UIComponent {
			var sr:Pair = getSubscribableResource(node, ct);
			var resourceUri:String = sr == null ? node.nodeUri : sr.a as String;
			var contentType:String = sr == null ? (ct == null ? contentTypeRegistry.defaultContentType : ct) : sr.b as String;
			if (contentType == null) {
				contentType = contentTypeRegistry.defaultContentType;
			}
			
			var editorDescriptor:BasicEditorDescriptor = contentTypeRegistry[contentType];
			return editorDescriptor.openEditor(resourceUri, true, false, false, false, addEditorInRight);
		}

		/**
		 * @author Cristina Constantinescu
		 */
		public function handleLink(queryString:String):void {			
			var commands:Object = parseQueryStringParameters(queryString); // map command -> parameters
			for (var object:String in commands) {	
				handleLinkForCommand(object, commands[object]);
			}
		}
		
		public function handleLinkForCommand(command:String, parameters:String):void {
			var linkHandler:ILinkHandler = ILinkHandler(linkHandlers[command]);
			if (linkHandler != null) {	
				linkHandler.handleLink(command, parameters);					
			}
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function handleLinkWithQueryStringDecoded(queryStringDecoded:Object):void {			
			for (var object:String in queryStringDecoded) {	
				var linkHandler:ILinkHandler = ILinkHandler(linkHandlers[object]);
				if (linkHandler != null) {	
					linkHandler.handleLink(object, queryStringDecoded[object]);					
				}
			}
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function parseQueryStringParameters(url:String):Object {
			var query:String;
			if (url.indexOf("?") != -1) { // no parameters passed in the url
				query = url.substr(url.indexOf("?") + 1);
			} else {
				query = url;
			}			
			
			var parameters:Object = new Object();
			for each (var parameterWithValue:String in query.split("&")) { // spliting by group separator p1=v1&p2=v2
				var parameter:String = null;
				var value:String = null;
				
				var indexOfEqualSign:int = parameterWithValue.indexOf("=");
				if (indexOfEqualSign < 0) { // No value, just key
					parameter = parameterWithValue;
					value = null;
				} else {
					parameter = parameterWithValue.substring(0, indexOfEqualSign);
					value = parameterWithValue.substring(indexOfEqualSign + 1); // the rest represents the value, even though it contains an = character 
				}
				parameters[parameter] = value;
			}
			return parameters;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function registerActionToGlobalMenu(action:ActionBase):void {
			FlexUtilGlobals.getInstance().registerActionInstance(action);
			globalMenuActionProvider.addAction(action);
		}
		
		public function getAppUrl():String {
			return FlexUtilGlobals.getInstance().rootUrl + MAIN_PAGE;	
		}
				
		/**
		 * @return a list with all <code>EditorFrontend</code>s found on workbench.
		 */ 
		public function getAllEditorFrontends():Array {			
			var editors:Array = new Array();
			
			var components:ArrayCollection = new ArrayCollection();
			FlexUtilGlobals.getInstance().workbench.getAllEditorViews(null, components);
			
			for each (var component:UIComponent in components) {								
				if (component is EditorFrontend) {
					editors.push(component);
				}
			}
			return editors;
		}
		
		/**
		 * @author Valentina-Camelia Bojan
		 */
		public function getRepository(nodeUri:String):String {
			var index:int = nodeUri.indexOf("|");
			if (index < 0) {
				index = nodeUri.length;
			}
			return nodeUri.substring(nodeUri.indexOf(":") + 1, index);
		}
		
		/**
		 * @author Sebastian Solomon
		 */
		public function selectNode(diagramShellContext:DiagramShellContext, fullNodeId:String):void {
			var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;			
			var editor:EditorFrontend = EditorFrontend(workbench.getEditorFromViewComponent(workbench.getActiveView()));
			var childNode:Node = editor.nodeRegistry.getNodeById(fullNodeId);
			
			MindMapDiagramShell(diagramShellContext.diagramShell).selectedItems.resetSelection();
			MindMapDiagramShell(diagramShellContext.diagramShell).selectedItems.addItem(childNode);
		}

		/**
		 * @author Diana Balutoiu
		 */
		public function createNodeUriWithRepo(scheme:String, repoPath:String, schemeSpecificPart:String):String {
			return scheme + ":"+ repoPath + "|" + schemeSpecificPart;
		}
			
	}
}

include "../../../../../../org.flowerplatform.js_client.common_js_as/WebContent/js/ResourceOperationsManager.js";	
include "../../../../../../org.flowerplatform.js_client.common_js_as/WebContent/js/NodeRegistryManager.js";	
include "../../../../../../org.flowerplatform.js_client.common_js_as/WebContent/js/NodeRegistry.js";
