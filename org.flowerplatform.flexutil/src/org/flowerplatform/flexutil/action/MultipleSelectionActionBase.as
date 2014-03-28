package org.flowerplatform.flexutil.action {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
	 * Useful base class for actions that are enabled on a multiple selection.
	 * Subclasses should implement <code>isVisibleForSelectedElement()</code>.
	 * 
	 * @see #isVisible()
	 * @see #isVisibleForSelectedElement()
	 * 
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class MultipleSelectionActionBase extends ActionBase	{
		
		/**
		 * Iterates over the selected elements and invokes <code>isVisibleForSelectedElement()</code>.
		 * If the selection doesn't contain elements, returns <code>false</code>.
		 */
		override public function get visible():Boolean {	
			if (selection.length == 0) {
				return false;
			}
			
			for (var i:int = 0; i < selection.length; i++) {
				if (!isVisibleForSelectedElement(selection.getItemAt(i))) {
					return false;
				}
			}
			return true;
		}
		
		/**
		 * Should return <code>true</code> if the current element is selected and
		 * <code>false</code> otherwise. It is called once for every element from
		 * the selection. A single <code>false</code> value among these has a veto role, i.e.
		 * the action is not visible.
		 * 
		 * <p>
		 * By default, returns <code>true</code>.
		 */
		protected function isVisibleForSelectedElement(element:Object):Boolean {
			return true;
		}
		
	}
}