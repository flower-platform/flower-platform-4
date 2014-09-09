package org.flowerplatform.flex_client.core.node {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.editor.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.core.node.remote.ServiceContext;
	
	/**
	 * Implemented within the host system. Some of the methods are factory methods, i.e.
	 * the host implementation creates instances that will be managed by the JS code. And
	 * other methods perform various tasks within the host system.
	 * 
	 * @author Cristina Constantinescu
	 */
	// TODO CS: rename IHostInvocator; de redenumit get* in create*
	public interface IExternalInvocator {
		
		// factory methods
		
		function getNewListInstance():ArrayCollection;
		
		function getNewFullNodeIdWithChildrenInstance():FullNodeIdWithChildren;
		
		function getServiceContextInstance():ServiceContext;
		
		function createUpdateEvent(source:Object, property:String, oldValue:Object, newValue:Object):Object;

		// other methods
		
		function showMessageBox(titleKeyMessage:String, textKeyMessage:String, textParams:Array):void;
		
		function addEventListener(source:Object, eventType:String, handler:Function):void;
		
		function removeEventListener(source:Object, eventType:String, handler:Function):void;
		
	}
}