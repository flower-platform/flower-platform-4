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
package org.flowerplatform.flexutil.shortcut {
	
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlexUtilConstants;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.action.ComposedActionProvider;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.view_content_host.IViewHostAware;
	
	/**
	 * This class binds shortcuts to actions (or functions).
	 * 
	 * This class uses a global event handler as described here:
	 * http://cookbooks.adobe.com/post_Global_keyboard_event_handling-304.html
	 * 
	 * As key down event does not work well in internet explorer version 8 or older, this uses key up event.
	 * http://stackoverflow.com/questions/9507200/why-keydown-listener-doesnt-work-in-ie
	 * 
	 * @author Florin
	 * @author Daniela
	 * @author Cristina Constantinescu 
	 */
	public class KeyBindings {

		private var keyBindings:Dictionary = new Dictionary();

		public var filterShortcuts:Dictionary = new Dictionary();
		
		public var actionIdsToShortcuts:Dictionary = new Dictionary();
		
		public var allowKeyBindingsToProcessEvents:Boolean = true;
		
		public var learnShortcutOnNextActionInvocation:Boolean = false;
		
		public var additionalActionProviders:ComposedActionProvider = new ComposedActionProvider();
		
		public function KeyBindings() {
			if (UIComponent(FlexGlobals.topLevelApplication).stage != null) {
				registerKeyListener();
			} else {
				UIComponent(FlexGlobals.topLevelApplication).addEventListener(Event.ADDED_TO_STAGE, registerKeyListener);
			}
			// initial filterShortcuts
			// other filterShortcut must be added by corresponding keyboard action
			filterShortcuts[FlexUtilConstants.CONTROL] = Keyboard.CONTROL;
			filterShortcuts[FlexUtilConstants.COMMAND] = Keyboard.COMMAND;
			filterShortcuts[FlexUtilConstants.SHIFT] = Keyboard.SHIFT;
			filterShortcuts[FlexUtilConstants.ALTERNATE] = Keyboard.ALTERNATE;
		}
		
		protected function registerKeyListener(event:Event = null):void {
			UIComponent(FlexGlobals.topLevelApplication).removeEventListener(Event.ADDED_TO_STAGE, registerKeyListener);
			UIComponent(FlexGlobals.topLevelApplication).stage.addEventListener(KeyboardEvent.KEY_UP, onKeyUp);			
		}

		public function registerBinding(shortcut:Shortcut, handler:Object, addToFilterShortcutsIfNecessary:Boolean = false):void {
			if (!(handler is IAction) && !(handler is Function) && !(handler is String)) {
				// not what we expected
				throw new Error("The handler must be an action, an action id or a function!");
			}
			
			// check if shortcut already exists					
			if (keyBindings.hasOwnProperty(shortcut.toString())) {
				return;
			}
			
			keyBindings[shortcut.toString()] = handler;
			
			// register in actionIdsToShortcuts if string 
			if (handler is String) {
				if (actionIdsToShortcuts == null) {
					actionIdsToShortcuts = new Dictionary();
				}
				actionIdsToShortcuts[handler] = shortcut;
			}
			
			if (addToFilterShortcutsIfNecessary && !shortcut.ctrlKey && !shortcut.altKey && !shortcut.shiftKey) { 
				filterShortcuts[Utils.getKeyNameFromKeyCode(shortcut.keyCode)] = shortcut.keyCode;
			}
		}

		private function onKeyUp(event:KeyboardEvent):void {		
			if (!canProcessEvent(event)) {
				return;
			}			
			var shortcut:Shortcut = new Shortcut(event.ctrlKey, event.shiftKey, event.altKey, event.keyCode);
			if (!keyBindings.hasOwnProperty(shortcut.toString())) { // no shortcut registered for this event
				return;
			}
			
			var action:IAction;
			var handler:Object = keyBindings[shortcut.toString()];
			
			if (handler is IAction) {
				// check if visible & enabled, then run it
				action = IAction(handler);
				if (action.visible && action.enabled) {
					action.run(); 
				}
			} else if (handler is Function) {
				// execute function
				handler();
			} else {		
//				var actions:Vector.<IAction> = additionalActionProviders.getActions(null);
				
				// search actionId also in active's view list of available actions	
				var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;			
				var view:UIComponent = workbench.getEditorFromViewComponent(workbench.getActiveView());
				if (view != null && view is IActionProvider) {
					var selection:IList = null;
					if (view is IViewHostAware) {
						selection = IViewHostAware(view).viewHost.getCachedSelection();
					}
					
//					var actions:Vector.<IAction> = IActionProvider(view).getActions(selection);	
//					trace("in keybindings : "+viewActions);
//					for (i = 0; i < viewActions.length; i++) {
//						actions.push(viewActions[i]);
//					}
				}
				
//				if (actions == null) {
//					return;
//				}
//				for (var i:int = 0; i < actions.length; i++) {
					action = FlexUtilGlobals.getInstance().actionRegistry[handler].newInstance();
//					if (action.id == handler) {
						try {
							action.selection = selection;
							if (action.visible && action.enabled) {								
								action.run(); 
							}
						} finally {
							action.selection = null;
						}						
//						break;
//					}
//				}								
			}
		}
	
		public function getRegisteredHandler(shortcut:Shortcut):Object {			
			return keyBindings[shortcut.toString()];
		}
		
		private function canProcessEvent(event:KeyboardEvent):Boolean {
			return !learnShortcutOnNextActionInvocation && allowKeyBindingsToProcessEvents &&
				(filterShortcuts.hasOwnProperty(FlexUtilConstants.CONTROL) || filterShortcuts.hasOwnProperty(FlexUtilConstants.COMMAND) ||
				filterShortcuts.hasOwnProperty(FlexUtilConstants.ALTERNATE) ||filterShortcuts.hasOwnProperty(FlexUtilConstants.SHIFT) ||
				filterShortcuts.hasOwnProperty(Utils.getKeyNameFromKeyCode(event.keyCode)));				
		}
		
	}
}
