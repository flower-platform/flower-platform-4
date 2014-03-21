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
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	import mx.messaging.ChannelSet;
	import mx.messaging.channels.AMFChannel;
	
	import org.flowerplatform.flex_client.core.editor.ContentTypeRegistry;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.ResourceNodeIdsToNodeUpdateProcessors;
	import org.flowerplatform.flex_client.core.editor.ResourceNodesManager;
	import org.flowerplatform.flex_client.core.editor.action.OpenAction;
	import org.flowerplatform.flex_client.core.editor.text.TextEditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.update.UpdateTimer;
	import org.flowerplatform.flex_client.core.event.GlobalActionProviderChangedEvent;
	import org.flowerplatform.flex_client.core.link.ILinkHandler;
	import org.flowerplatform.flex_client.core.link.LinkHandler;
	import org.flowerplatform.flex_client.core.link.LinkView;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDescriptor;
	import org.flowerplatform.flex_client.core.mindmap.action.AddNodeAction;
	import org.flowerplatform.flex_client.core.mindmap.action.RefreshAction;
	import org.flowerplatform.flex_client.core.mindmap.action.ReloadAction;
	import org.flowerplatform.flex_client.core.mindmap.action.RemoveNodeAction;
	import org.flowerplatform.flex_client.core.mindmap.action.RenameAction;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeTypeProvider;
	import org.flowerplatform.flex_client.core.mindmap.layout.MindMapPerspective;
	import org.flowerplatform.flex_client.core.mindmap.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.core.mindmap.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flex_client.core.mindmap.remote.NodeWithChildren;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.ChildrenUpdate;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.PropertyUpdate;
	import org.flowerplatform.flex_client.core.mindmap.remote.update.Update;
	import org.flowerplatform.flex_client.core.node.controller.GenericDescriptorValueProvider;
	import org.flowerplatform.flex_client.core.node.remote.GenericDescriptor;
	import org.flowerplatform.flex_client.core.node.remote.TypeDescriptorRemote;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.core.service.UpdatesProcessingServiceLocator;
	import org.flowerplatform.flexdiagram.controller.ITypeProvider;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.ClassFactoryActionProvider;
	import org.flowerplatform.flexutil.action.ComposedAction;
	import org.flowerplatform.flexutil.action.VectorActionProvider;
	import org.flowerplatform.flexutil.controller.AbstractController;
	import org.flowerplatform.flexutil.controller.AllDynamicCategoryProvider;
	import org.flowerplatform.flexutil.controller.TypeDescriptor;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.layout.Perspective;
	import org.flowerplatform.flexutil.resources.ResourceUpdatedEvent;
	import org.flowerplatform.flexutil.resources.ResourcesUtils;
	import org.flowerplatform.flexutil.service.ServiceLocator;
	
	import spark.components.Application;
	
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
		
		public static const FILE_MENU_ID:String = "file";
		public static const NAVIGATE_MENU_ID:String = "navigate";
		
		protected static var INSTANCE:CorePlugin;
		
		public var serviceLocator:ServiceLocator;
		
		public var perspectives:Vector.<Perspective> = new Vector.<Perspective>();
		
		public var mindmapEditorClassFactoryActionProvider:ClassFactoryActionProvider = new ClassFactoryActionProvider();

		public var resourceNodesManager:ResourceNodesManager;

		public var resourceNodeIdsToNodeUpdateProcessors:ResourceNodeIdsToNodeUpdateProcessors = new ResourceNodeIdsToNodeUpdateProcessors();
		
		public var updateTimer:UpdateTimer = new UpdateTimer(5000);
		
		public var nodeTypeDescriptorRegistry:TypeDescriptorRegistry = new TypeDescriptorRegistry();

		public var nodeTypeProvider:ITypeProvider = new NodeTypeProvider();
		
		public var contentTypeRegistry:ContentTypeRegistry = new ContentTypeRegistry();
		
		public static const PROPERTY_FOR_TITLE_DESCRIPTOR:String = "propertyForTitleDescriptor";
		public static const PROPERTY_FOR_ICONS_DESCRIPTOR:String = "propertyForIconDescriptor";
		
		public static const NODE_TITLE_PROVIDER:String = "nodeTitleProvider";
		public static const NODE_ICONS_PROVIDER:String = "nodeIconProvider";
		
		/**
		 * @author Sebastian Solomon
		 */
		// TODO to delete when mm classes from core will be moved in .mindmap project
		public var iconSideBarClass:Class;
				
		public var globalMenuActionProvider:VectorActionProvider = new VectorActionProvider();
				
		public static function getInstance():CorePlugin {
			return INSTANCE;
		}
				
//		public static const VERSION:String = "2.0.0.M2_2013-06-04";
//				
		/**
		 * key = command name as String (e.g. "openResources")
		 * value = parameters as String (e.g. text://file1,file2,file3)
		 */ 
		public var linkHandlers:Dictionary;
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
					
			resourceNodesManager = new ResourceNodesManager();
			
			var channelSet:ChannelSet = new ChannelSet();
			channelSet.addChannel(new AMFChannel(null, FlexUtilGlobals.getInstance().rootUrl + 'messagebroker/remoting-amf'));
			
			serviceLocator = new UpdatesProcessingServiceLocator(channelSet);
			serviceLocator.addService("nodeService");
			serviceLocator.addService("resourceInfoService");
			serviceLocator.addService("freeplaneService");
			
			var textEditorDescriptor:TextEditorDescriptor = new TextEditorDescriptor();
			contentTypeRegistry[TextEditorDescriptor.ID] = textEditorDescriptor;
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(textEditorDescriptor);
			
			var mindMapEditorDescriptor:MindMapEditorDescriptor = new MindMapEditorDescriptor();
			contentTypeRegistry.defaultContentType = MindMapEditorDescriptor.ID;
			contentTypeRegistry[MindMapEditorDescriptor.ID] = mindMapEditorDescriptor;
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(mindMapEditorDescriptor);			
			perspectives.push(new MindMapPerspective());
		
			mindmapEditorClassFactoryActionProvider.addActionClass(AddNodeAction);
			mindmapEditorClassFactoryActionProvider.addActionClass(RemoveNodeAction);			
			mindmapEditorClassFactoryActionProvider.addActionClass(RenameAction);			
			mindmapEditorClassFactoryActionProvider.addActionClass(ReloadAction);
			mindmapEditorClassFactoryActionProvider.addActionClass(RefreshAction);			
			mindmapEditorClassFactoryActionProvider.addActionClass(OpenAction);
			
			serviceLocator.invoke("nodeService.getRegisteredTypeDescriptors", null,
				function(result:Object):void {
					var list:ArrayCollection = ArrayCollection(result);
					for (var i:int = 0; i < list.length; i++) {
						var remote:TypeDescriptorRemote = TypeDescriptorRemote(list.getItemAt(i));
						
						// create new type descriptor with remote type
						var descriptor:TypeDescriptor = null;
						if (Utils.beginsWith(remote.type, TypeDescriptor.CATEGORY_PREFIX)) {
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
			
			nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(AllDynamicCategoryProvider.CATEGORY_ALL)
				.addSingleController(NODE_TITLE_PROVIDER, new GenericDescriptorValueProvider(PROPERTY_FOR_TITLE_DESCRIPTOR))
				.addSingleController(NODE_ICONS_PROVIDER, new GenericDescriptorValueProvider(PROPERTY_FOR_ICONS_DESCRIPTOR));
			
			linkHandlers = new Dictionary();
			linkHandlers[LinkHandler.OPEN_RESOURCES] = new LinkHandler(MindMapEditorDescriptor.ID);

			if (ExternalInterface.available) {
				// on mobile, it's not available
				ExternalInterface.addCallback("handleLink", handleLink);
			}			
			
			// when adding actions to global menu, the messages must be fully loaded
			Application(FlexGlobals.topLevelApplication).addEventListener(ResourceUpdatedEvent.RESOURCE_UPDATED, messageResourceUpdatedHandler);
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
			registerClassAliasFromAnnotation(GenericDescriptor);
			registerClassAliasFromAnnotation(AddChildDescriptor);
		}
		
		/**
		 * Overriden to add FlexGlobals.topLevelApplication as <code>object</code> parameter.
		 * Needed in <code>ResourceUtils</code> to dispatch <code>ResourceUpdatedEvent</code>.
		 */ 
		override protected function registerMessageBundle():void {
			ResourcesUtils.registerMessageBundle("en_US", resourcesUrl, getResourceUrl(MESSAGES_FILE), FlexGlobals.topLevelApplication);
		}
		
		private function messageResourceUpdatedHandler(event:ResourceUpdatedEvent):void {		
			if (event.resourceURL != getResourceUrl(MESSAGES_FILE)) {
				return;
			}
			
			// message resource is loaded
			
			Application(FlexGlobals.topLevelApplication).removeEventListener(ResourceUpdatedEvent.RESOURCE_UPDATED, messageResourceUpdatedHandler);
			
			// add actions to global menu
			
			addActionToGlobalMenuActionProvider(getMessage("menu.file"), null, FILE_MENU_ID);
			globalMenuActionProvider.addAction(resourceNodesManager.saveAction);
			globalMenuActionProvider.addAction(resourceNodesManager.saveAllAction);
			
			addActionToGlobalMenuActionProvider(getMessage("menu.navigate"), null, NAVIGATE_MENU_ID); 
			addActionToGlobalMenuActionProvider(getMessage("link.title"), getResourceUrl('images/external_link.png'), null, NAVIGATE_MENU_ID, 
				function ():void {
					FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
					.setViewContent(new LinkView())
					.setWidth(400)
					.setHeight(450)
					.show();
				}
			);			
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
		 * Creates and adds an action to the the actionProvider
		 * 
		 * @author Mircea Negreanu
		 * @author Cristina Constantinescu
		 */
		public function addActionToGlobalMenuActionProvider(label:String, icon:String = null, id:String = null, parentId:String = null, functionDelegate:Function = null):void {
			var action:ActionBase;
			if (id != null) {
				action = new ComposedAction();				
			} else {
				action = new ActionBase();
			}
			
			action.label = label;
			action.id = id;
			action.parentId = parentId;			
			action.icon = icon != null ? FlexUtilGlobals.getInstance().createAbsoluteUrl(icon) : null;
			action.functionDelegate = functionDelegate;
			globalMenuActionProvider.addAction(action);
			
			Application(FlexGlobals.topLevelApplication).dispatchEvent(new GlobalActionProviderChangedEvent());
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
