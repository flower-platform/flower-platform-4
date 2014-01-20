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
package com.crispico.flower.util.ui_inheritance {
	import mx.core.UIComponent;
	import mx.states.State;
	
	/**
	 * Allows MXML components to inherit from other MXML components. 
	 * 
	 * <p>
	 * More precisely,
	 * allows the definition of states. If a state with the same name already exists,
	 * (e.g. already defined in one of the superclasses),
	 * we consider that we want to override that state, so the old state is renamed, and the new
	 * state will be based on the existing state.
	 * 
	 * <p>
	 * Constraint: all states need to be based on another state. Exception: the default state, and of
	 * course the states that override existing states.
	 * 
	 * @author Cristi
	 */
	public class InheritableStates {
		
		public static const DEFAULT_STATE_NAME:String = "default";
		
		public static const OVERRIDDEN:String = "_ovr_";
		
		private var _inheritableStates:Array;
		
		private var _component:UIComponent;
		
		private var _currentState:String;
		
		protected var merged:Boolean;
		
		protected static var idFactory:int = 0;
		
		[Inspectable(arrayType="mx.states.State")]
		[ArrayElementType("mx.states.State")]
		/**
		 * We cannot use the name "states" because it confuses the FB IDE, which would
		 * throw an error. For the same reason, this property cannot be made default.
		 */
		public function get inheritableStates():Array {
			return _inheritableStates;
		}

		public function set inheritableStates(value:Array):void	{
			_inheritableStates = value;
			mergeStates();
		}

		public function get component():UIComponent {
			return _component;
		}

		public function set component(value:UIComponent):void {
			_component = value;
			mergeStates();
		}
		
		public function get currentState():String {
			return _currentState;
		}
		
		public function set currentState(value:String):void {
			_currentState = value;
			if (merged) {
				component.currentState = currentState;
			} 
			// else not yet merged; after merge this value will be used
		}

		/**
		 * We don't kwnow in which order the 2 fields are called. That's why both
		 * setters call this, and this method checks until both fields are filled.
		 */
		protected function mergeStates():void {
			if (merged || component == null || inheritableStates == null) {
				return;
			}
			while (mergeState(inheritableStates, component.states)) {
				// do nothing
			}

			var originalCurrentState:String = component.currentState;
			
			// set to null to force a refresh (for the case e.g. state1 -> null -> state1 (overridden)
			component.currentState = null;
			if (currentState != null) {
				component.currentState = currentState;
			} else if (originalCurrentState != null) {
				// this may be overriding a component that already has InheritableStates with "currentState" set
				component.currentState = originalCurrentState;
			}
			
			merged = true;
		}
		
		/**
		 * Adds the last state in inheritableStates, in state, performing
		 * verifications, and renaming the existing state if needed.
		 */
		protected function mergeState(inheritableStates:Array, states:Array):Boolean {
			var state:State = State(inheritableStates.pop());
			var foundStateBasedOn:Boolean = false;
			for each (var currentState:State in inheritableStates) {
				if (state.name == currentState.name) {
					throw "2 states with same name are defined: " + state.name;
				} else if (state.basedOn == currentState.name) {
					foundStateBasedOn = true;
				}
			}
			
			var ancestorState:State = null;
			
			for each (currentState in states) {
				if (state == currentState) {
					// found itself in the list; here, it's not normal
					throw "State instance exists in both arrays, inheritableStates and states: " + state.name;
				} else if (state.name == currentState.name) {
					ancestorState = currentState;
				} else if (state.basedOn == currentState.name) {
					foundStateBasedOn = true;
				}
			}
			
			if (ancestorState != null) {
				// a state is overridden
				if (state.basedOn != null) {
					throw "It's illegal to override a state: " + state.name + " and specify 'basedOn': " + state.basedOn;
				}
				var nameForAncestorState:String = state.name + OVERRIDDEN + idFactory++;
				ancestorState.name = nameForAncestorState;
				state.basedOn = nameForAncestorState;
			} else if (!foundStateBasedOn && state.name != DEFAULT_STATE_NAME) {
				throw "State should be based on another state OR should be named 'default': " + state.name;
			}
			// else state doesn't override (i.e. is new) and is based on another state (or default)
			states.push(state);
			return inheritableStates.length > 0;
		}

	}
}