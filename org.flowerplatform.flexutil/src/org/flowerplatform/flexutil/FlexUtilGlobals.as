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
package org.flowerplatform.flexutil {
	import flash.utils.Dictionary;
	
	import mx.utils.LoaderUtil;
	
	import spark.core.ContentCache;
	
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.ActionHelper;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.context_menu.ContextMenuManager;
	import org.flowerplatform.flexutil.layout.ComposedViewProvider;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.plugin.FlexPluginManager;
	import org.flowerplatform.flexutil.popup.IMessageBoxFactory;
	import org.flowerplatform.flexutil.popup.IPopupHandlerFactory;
	import org.flowerplatform.flexutil.popup.IProgressMonitorFactory;
	import org.flowerplatform.flexutil.selection.SelectionManager;
	import org.flowerplatform.flexutil.shortcut.KeyBindings;

	public class FlexUtilGlobals {

		protected static var INSTANCE:FlexUtilGlobals = new FlexUtilGlobals();
		
		public static function getInstance():FlexUtilGlobals {
			return INSTANCE;
		}
		
		public var workbench:IWorkbench;
		
		private var _keyBindings:KeyBindings;

		public function get keyBindings():KeyBindings {
			if (_keyBindings == null) {
				_keyBindings = new KeyBindings();
			}
			return _keyBindings;
		}
		
		public var composedViewProvider:ComposedViewProvider = new ComposedViewProvider();
		
		public var messageBoxFactory:IMessageBoxFactory;
		
		public var popupHandlerFactory:IPopupHandlerFactory;
		
		/**
		 * @author Cristina Contantinescu
		 */
		public var progressMonitorHandlerFactory:IProgressMonitorFactory;
		
		public var clientCommunicationErrorViewContent:Class;
		
		public var isMobile:Boolean;
		
		public var contextMenuManager:ContextMenuManager;
		
		/**
		 * The normal app, shouldn't do anything.
		 * The embeded app should initialize using LoaderUtil.normalizeURL(info);
		 * The mobile app should provide the explicit URL.
		 */
		public var rootUrl:String = "";
		
		public var selectionManager:SelectionManager = new SelectionManager();
		
		public var flexPluginManager:FlexPluginManager = new FlexPluginManager();
		
		/**
		 * Dictionary that maps action class instances to their ID.
		 * @author Alina Bratu
		 */
		public var actionRegistry:Dictionary = new Dictionary();
		
		
		/**
		 * @author Cristina Contantinescu
		 */
		public var actionHelper:ActionHelper = new ActionHelper();
		
		/**
		 * @author Cristina Contantinescu
		 */
		public var imageContentCache:ContentCache;
		
		public function createAbsoluteUrl(url:String):String {
			if (rootUrl.length > 0) {
				return LoaderUtil.createAbsoluteURL(rootUrl, url);
			}
			return url;
		}
		
		public function adjustImageBeforeDisplaying(image:Object):Object {
			if (!(image is String)) {
				// probably an embeded image
				return image;
			} else {
				// an URL
				return createAbsoluteUrl(String(image));
			}
		}
		
		/**
		 * Adds an instance of the <code>generator</code> in <code>actionRegistry</code>. The instance is 
		 * mapped at the generator's ID.
		 * 
		 * @author Alina Bratu
		 */
		public function registerAction(generator:Class):void {
			actionRegistry[Object(generator).ID] = new FactoryWithInitialization(generator);
		}
			
		public function registerActionInstance(instance:IAction):void {
			actionRegistry[instance.id] = instance;
		}
		
		public function getActionInstanceFromRegistry(actionId:String):IAction {
			var obj:Object = actionRegistry[actionId];
			if (obj != null) {
				if (obj is ActionBase) {
					return ActionBase(obj);
				}
				if (obj is FactoryWithInitialization) {
					return FactoryWithInitialization(obj).newInstance();
				}
			}
			return null;
		}
		
	}
}
