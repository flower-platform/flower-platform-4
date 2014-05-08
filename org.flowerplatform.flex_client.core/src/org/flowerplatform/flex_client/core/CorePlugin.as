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
package org.flowerplatform.flex_client.core {

	import flash.external.ExternalInterface;
	import flash.ui.Keyboard;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	import mx.messaging.ChannelSet;
	import mx.messaging.channels.AMFChannel;
	
	import org.flowerplatform.flex_client.core.editor.BasicEditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.ContentTypeRegistry;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.action.DownloadAction;
	import org.flowerplatform.flex_client.core.editor.action.ForceUpdateAction;
	import org.flowerplatform.flex_client.core.editor.action.OpenAction;
	import org.flowerplatform.flex_client.core.editor.action.RemoveNodeAction;
	import org.flowerplatform.flex_client.core.editor.action.RenameAction;
	import org.flowerplatform.flex_client.core.editor.action.UploadAction;
	import org.flowerplatform.flex_client.core.editor.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.remote.NodeWithChildren;
	import org.flowerplatform.flex_client.core.editor.remote.update.ChildrenUpdate;
	import org.flowerplatform.flex_client.core.editor.remote.update.PropertyUpdate;
	import org.flowerplatform.flex_client.core.editor.remote.update.Update;
	import org.flowerplatform.flex_client.core.editor.resource.ResourceNodeIdsToNodeUpdateProcessors;
	import org.flowerplatform.flex_client.core.editor.resource.ResourceNodesManager;
	import org.flowerplatform.flex_client.core.editor.text.TextEditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.ui.OpenNodeView;
	import org.flowerplatform.flex_client.core.editor.update.UpdateTimer;
	import org.flowerplatform.flex_client.core.link.ILinkHandler;
	import org.flowerplatform.flex_client.core.link.LinkView;
	import org.flowerplatform.flex_client.core.node.controller.GenericValueProviderFromDescriptor;
	import org.flowerplatform.flex_client.core.node.controller.TypeDescriptorRegistryDebugControllers;
	import org.flowerplatform.flex_client.core.node.remote.GenericValueDescriptor;
	import org.flowerplatform.flex_client.core.node.remote.ServiceContext;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.core.service.UpdatesProcessingServiceLocator;
	import org.flowerplatform.flex_client.core.shortcut.AssignHotKeyAction;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.controller.ITypeProvider;
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
	import org.flowerplatform.flexutil.layout.Perspective;
	import org.flowerplatform.flexutil.service.ServiceLocator;
	import org.flowerplatform.flexutil.spinner.ModalSpinner;

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

		public var resourceNodesManager:ResourceNodesManager;

		public var resourceNodeIdsToNodeUpdateProcessors:ResourceNodeIdsToNodeUpdateProcessors = new ResourceNodeIdsToNodeUpdateProcessors();
		
		public var updateTimer:UpdateTimer;
		
		public var nodeTypeDescriptorRegistry:TypeDescriptorRegistry = new TypeDescriptorRegistry();

		public var nodeTypeProvider:ITypeProvider;
		
		public var contentTypeRegistry:ContentTypeRegistry = new ContentTypeRegistry();
			
		public var debug_forceUpdateAction:ForceUpdateAction;
					
		public var globalMenuActionProvider:VectorActionProvider = new VectorActionProvider();
				
		public static function getInstance():CorePlugin {
			return INSTANCE;
		}
				
		/**
		 * key = command name as String (e.g. "openResources")
		 * value = parameters as String (e.g. text://file1,file2,file3)
		 */ 
		public var linkHandlers:Dictionary;
		
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
			resourceNodesManager = new ResourceNodesManager();
			
			var channelSet:ChannelSet = new ChannelSet();
			channelSet.addChannel(new AMFChannel(null, FlexUtilGlobals.getInstance().rootUrl + 'messagebroker/remoting-amf'));
		
			serviceLocator = new UpdatesProcessingServiceLocator(channelSet);
			serviceLocator.addService("coreService");
			serviceLocator.addService("nodeService");
			serviceLocator.addService("resourceService");
			serviceLocator.addService("downloadService");
			serviceLocator.addService("uploadService");
			
 			updateTimer = new UpdateTimer(5000);
			
			var textEditorDescriptor:TextEditorDescriptor = new TextEditorDescriptor();
			contentTypeRegistry[CoreConstants.TEXT_CONTENT_TYPE] = textEditorDescriptor;
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(textEditorDescriptor);
								
			editorClassFactoryActionProvider.addActionClass(RemoveNodeAction);			
			editorClassFactoryActionProvider.addActionClass(RenameAction);			
			editorClassFactoryActionProvider.addActionClass(OpenAction);
			
			if (!FlexUtilGlobals.getInstance().isMobile) {
				editorClassFactoryActionProvider.addActionClass(DownloadAction);
				editorClassFactoryActionProvider.addActionClass(UploadAction);				
			}
			
			// check version compatibility with server side
			serviceLocator.invoke("coreService.getVersions", null, 
				function (result:Object):void {					
					if (_appVersion != result[0]) { // application version is old 
						FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setTitle(Resources.getMessage('version.error'))
						.setText(Resources.getMessage('version.error.message', [_appVersion, result[0], Resources.getMessage('version.error.details')]))
						.setWidth(400)
						.setHeight(300)
						.showMessageBox();						
					} else if (_apiVersion != result[1]) { // API version is old
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
				.addSingleController(CoreConstants.NODE_TITLE_PROVIDER, new GenericValueProviderFromDescriptor(CoreConstants.PROPERTY_FOR_TITLE_DESCRIPTOR))
				.addSingleController(CoreConstants.NODE_SIDE_PROVIDER, new GenericValueProviderFromDescriptor(CoreConstants.PROPERTY_FOR_SIDE_DESCRIPTOR))
				.addSingleController(CoreConstants.NODE_ICONS_PROVIDER, new GenericValueProviderFromDescriptor(CoreConstants.PROPERTY_FOR_ICONS_DESCRIPTOR));
			
			new TypeDescriptorRegistryDebugControllers().registerControllers();
			
			linkHandlers = new Dictionary();
			
			if (ExternalInterface.available) {
				// on mobile, it's not available
				ExternalInterface.addCallback("handleLink", handleLink);
			}			
			
			// add actions to global menu
			
			globalMenuActionProvider.addAction(new ComposedAction().setLabel(Resources.getMessage("menu.file")).setId(CoreConstants.FILE_MENU_ID).setOrderIndex(10));
			globalMenuActionProvider.addAction(resourceNodesManager.saveAction);
			globalMenuActionProvider.addAction(resourceNodesManager.saveAllAction);
			globalMenuActionProvider.addAction(resourceNodesManager.reloadAction);
			
			globalMenuActionProvider.addAction(new ComposedAction().setLabel(Resources.getMessage("menu.navigate")).setId(CoreConstants.NAVIGATE_MENU_ID).setOrderIndex(20));
			globalMenuActionProvider.addAction(new ActionBase()
				.setLabel(Resources.getMessage("link.title"))
				.setIcon(Resources.externalLinkIcon)
				.setParentId(CoreConstants.NAVIGATE_MENU_ID)
				.setFunctionDelegate(function ():void {
					FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
					.setViewContent(new LinkView())
					.setWidth(500)
					.setHeight(250)
					.show();
				})
			);
			
			debug_forceUpdateAction = new ForceUpdateAction();
			globalMenuActionProvider.addAction(debug_forceUpdateAction);
			globalMenuActionProvider.addAction(new ComposedAction().setLabel(Resources.getMessage("menu.debug")).setId(CoreConstants.DEBUG).setOrderIndex(100));	
			
			globalMenuActionProvider.addAction(new ActionBase()
				.setLabel(Resources.getMessage("open.node.action.label"))
				.setIcon(Resources.openResourceIcon)
				.setParentId(CoreConstants.NAVIGATE_MENU_ID)
				.setFunctionDelegate(function ():void {
					FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
					.setViewContent(new OpenNodeView())
					.setWidth(400)
					.setHeight(110)
					.show();
				})
			);
				
			globalMenuActionProvider.addAction(new ActionBase()
				.setLabel(Resources.getMessage("open.root.action.label"))
				.setIcon(Resources.openIcon)
				.setParentId(CoreConstants.DEBUG)
				.setFunctionDelegate(function ():void {
					CorePlugin.getInstance().handleLinkForCommand(CoreConstants.OPEN_RESOURCES, "(root||)");
				})
			);
					
			globalMenuActionProvider.addAction(new ComposedAction().setLabel(Resources.getMessage("menu.tools")).setId(CoreConstants.TOOLS_MENU_ID).setOrderIndex(30));	
			globalMenuActionProvider.addAction(new AssignHotKeyAction());
			
			// initial filterShortcuts
			// other filterShortcut must be added by corresponding keyboard action
			FlexUtilGlobals.getInstance().keyBindings.filterShortcuts = [Keyboard.CONTROL, Keyboard.COMMAND, Keyboard.SHIFT, Keyboard.ALTERNATE];		
		}
				
		override protected function registerClassAliases():void {		
			super.registerClassAliases();
			registerClassAliasFromAnnotation(Node);
			registerClassAliasFromAnnotation(Update);
			registerClassAliasFromAnnotation(PropertyUpdate);
			registerClassAliasFromAnnotation(ChildrenUpdate);
			registerClassAliasFromAnnotation(NodeWithChildren);
			registerClassAliasFromAnnotation(FullNodeIdWithChildren);
		
			registerClassAliasFromAnnotation(TypeDescriptorRemote);
			registerClassAliasFromAnnotation(GenericValueDescriptor);
			registerClassAliasFromAnnotation(AddChildDescriptor);
			registerClassAliasFromAnnotation(Pair);
			registerClassAliasFromAnnotation(ServiceContext);
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
		 */
		public function openEditor(node:Node):void {
			var contentType:String = node.properties[CoreConstants.CONTENT_TYPE];
			if (contentType == null) {
				contentType = contentTypeRegistry.defaultContentType;
			}
			var hideRootNode:Boolean = node.properties[CoreConstants.HIDE_ROOT_NODE];
			
			var editorDescriptor:BasicEditorDescriptor = contentTypeRegistry[contentType];
			editorDescriptor.openEditor(node.fullNodeId, true, hideRootNode);
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
			
	}
}
