package com.crispico.flower.flexdiagram.action {
	import mx.collections.ArrayCollection;

	/**
	 * @author Cristian Spiescu
	 */
	public class DelegatingAction extends BaseAction {
		
		/**
		 * function (action:DelegatingAction, selectedEditParts:ArrayCollection):void
		 */
		protected var isVisibleFunction:Function;

		/**
		 * function (action:DelegatingAction, element:Object):Boolean
		 */
		protected var isVisibleForElementFunction:Function;
		
		protected var isVisibleForModelClasses:Array;
		
		protected var isVisibleForModelClassesMultipleSelection:Boolean;
		
		protected var isVisibleForModelClassesField:String;
		
		/**
		 * function (action:DelegatingAction, selectedEditParts:ArrayCollection):void
		 */
		protected var runFunction:Function;

		public function DelegatingAction(label:String, image:Object, sortIndex:int, isVisibleForModelClasses:Array, runFunction:Function,
				isVisibleForModelClassesField:String = null, isVisibleForModelClassesMultipleSelection:Boolean = false, isVisibleForElementFunction:Function = null, isVisibleFunction:Function = null) {
			super();
			this.label = label;
			this.image = image;
			this.sortIndex = sortIndex;
			this.runFunction = runFunction;
			this.isVisibleForModelClasses = isVisibleForModelClasses;
			this.isVisibleForModelClassesMultipleSelection = isVisibleForModelClassesMultipleSelection;
			this.isVisibleFunction = isVisibleFunction;
			this.isVisibleForElementFunction = isVisibleForElementFunction;
			this.isVisibleForModelClassesField = isVisibleForModelClassesField;
		}
	
		override public function isVisible(selectedEditParts:ArrayCollection):Boolean {
			if (isVisibleFunction != null) {
				isVisibleFunction.call(null, this, selectedEditParts);
			} else if (isVisibleForModelClasses != null) {
				for each (var ep:Object in selectedEditParts) {
					var ok:Boolean = false;
					var first:Boolean = true;
					if (isVisibleForElementFunction != null) {
						// delegate to function
						if (!isVisibleForElementFunction.call(null, this, ep.getModel())) {
							return false;
						}
					} else {
						// check with "instanceof"
						for each (var clazz:Class in isVisibleForModelClasses) {
							if (first) {
								first = false;
							} else if (!isVisibleForModelClassesMultipleSelection) {
								// i.e. I only want single selection, but we have several selected elements
								return false;
							}
							
							var candidate:Object = isVisibleForModelClassesField != null ? ep.getModel()[isVisibleForModelClassesField] : ep.getModel();
							if (candidate is clazz) {
								ok = true;
								break;
							}
						}
						if (!ok) {
							return false;
						}
					}
				}
				
				// either all elements "passed" the exam, or we have no elements at all
				return selectedEditParts.length > 0;
			}
			return super.isVisible(selectedEditParts);				
		}
		
		override public function run(selectedEditParts:ArrayCollection):void {
			if (runFunction != null) {
				runFunction.call(null, this, selectedEditParts);
			} else {
				super.run(selectedEditParts);				
			}
		}
		
	}
			
}