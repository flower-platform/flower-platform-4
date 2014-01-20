package org.flowerplatform.flexutil.selection {
	import mx.collections.IList;

	/**
	 * Should be implemented by the <code>IViewContent</code>s that have
	 * a selection.
	 * 
	 * <p>
	 * The VC should listen for selection change. When it happens => should call
	 * <code>FlexUtilGlobals.getInstance().selectionManager.selectionChanged(this)</code>.
	 * 
	 * @author Cristian Spiescu
	 */
	public interface ISelectionProvider {
		function getSelection():IList;
	}
}