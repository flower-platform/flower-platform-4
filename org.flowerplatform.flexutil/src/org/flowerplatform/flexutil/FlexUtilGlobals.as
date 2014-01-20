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
package org.flowerplatform.flexutil {
	import mx.collections.ArrayCollection;
	import mx.utils.LoaderUtil;
	
	import org.flowerplatform.flexutil.context_menu.ContextMenuManager;
	import org.flowerplatform.flexutil.layout.ComposedViewProvider;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
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
		
	}
}