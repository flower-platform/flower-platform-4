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
package org.flowerplatform.flexutil.shortcut {
	
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	import flash.utils.Dictionary;
	
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
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

		public var filterShortcuts:Array = [];
		
		public var actionIdsToShortcuts:Dictionary = new Dictionary();
		
		public var allowKeyBindingsToProcessEvents:Boolean = true;
		
		public var learnShortcutOnNextActionInvocation:Boolean = false;
		
		public function KeyBindings() {
			if (UIComponent(FlexGlobals.topLevelApplication).stage != null) {
				registerKeyListener();
			} else {
				UIComponent(FlexGlobals.topLevelApplication).addEventListener(Event.ADDED_TO_STAGE, registerKeyListener);
			}
		}
		
		protected function registerKeyListener(event:Event = null):void {
			UIComponent(FlexGlobals.topLevelApplication).removeEventListener(Event.ADDED_TO_STAGE, registerKeyListener);
			UIComponent(FlexGlobals.topLevelApplication).stage.addEventListener(KeyboardEvent.KEY_UP, onKeyUp);			
		}

		public function registerBinding(shortcut:Shortcut, handler:Object):Boolean {
			if (!(handler is IAction) && !(handler is Function) && !(handler is String)) {
				// not what we expected
				throw new Error("The handler must be an action, an action id or a function!");
			}
			
			// check if shortcut already exists					
			if (getRegisteredShortcut(shortcut)) {
				return false;
			}
			
			keyBindings[shortcut] = handler;
			
			// register in actionIdsToShortcuts if string 
			if (handler is String) {
				if (actionIdsToShortcuts == null) {
					actionIdsToShortcuts = new Dictionary();
				}
				actionIdsToShortcuts[handler] = shortcut;
			}
			
			if (!shortcut.ctrlKey && !shortcut.altKey && !shortcut.shiftKey) { 
				filterShortcuts.push(shortcut.keyCode);
			}
			
			return true;
		}

		private function onKeyUp(event:KeyboardEvent):void {		
			if (!canProcessEvent(event)) {
				return;
			}			
			var shortcut:Shortcut = getRegisteredShortcut(new Shortcut(event.ctrlKey, event.shiftKey, event.altKey, event.keyCode));
			if (shortcut == null) { // no shortcut registered for this event
				return;
			}
			
			var action:IAction;
			var handler:Object = keyBindings[shortcut];
			
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
				// search actionId in active's view list of available actions; if found, run it				
				var workbench:IWorkbench = FlexUtilGlobals.getInstance().workbench;			
				var view:UIComponent = workbench.getEditorFromViewComponent(workbench.getActiveView());
				
				if (view != null && view is IViewContent) {
					var selection:IList = null;
					if (view is IViewHostAware) {
						selection = IViewHostAware(view).viewHost.getCachedSelection();
					}
					var actions:Vector.<IAction> = IViewContent(view).getActions(selection);
					if (actions != null) {
						for (var i:int = 0; i < actions.length; i++) {
							action = actions[i];
							if (action.id == handler) {
								try {
									action.selection = selection;
									if (action.visible && action.enabled) {								
										action.run(); 
									}
								} finally {
									action.selection = null;
								}
								break;
							}
						}
					}
				}
			}
		}

		private function getRegisteredShortcut(shortcut:Shortcut):Shortcut {
			for (var obj:Object in keyBindings) {				
				if (shortcut.equals(obj)) {
					return Shortcut(obj);
				}
			}
			return null;
		}
	
		private function canProcessEvent(event:KeyboardEvent):Boolean {
			if (!learnShortcutOnNextActionInvocation && allowKeyBindingsToProcessEvents &&
				(event.ctrlKey && (filterShortcuts.indexOf(Keyboard.CONTROL) != -1 || filterShortcuts.indexOf(Keyboard.COMMAND) != -1) ||
				event.altKey && (filterShortcuts.indexOf(Keyboard.ALTERNATE) != -1) ||
				event.shiftKey && (filterShortcuts.indexOf(Keyboard.SHIFT) != -1) ||
				event.keyCode && (filterShortcuts.indexOf(event.keyCode) != -1))) {				
				return true;
			}
			return false;
		}
		
	}
}
