package org.flowerplatform.flexutil.selection {
	import mx.collections.IList;

	/**
	 * Should be implemented by <code>IViewContent</code>s that are able to
	 * send the selection to server, i.e. convert it into lightweight objects
	 * that can be sent to the server.
	 * 
	 * @author Cristian Spiescu
	 */
	public interface ISelectionForServerProvider {
		function convertSelectionToSelectionForServer(selection:IList):IList;
	}
}