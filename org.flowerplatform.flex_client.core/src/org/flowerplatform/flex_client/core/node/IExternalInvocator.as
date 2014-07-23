package org.flowerplatform.flex_client.core.node {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.editor.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.core.node.remote.ServiceContext;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public interface IExternalInvocator {
	
		function getNewListInstance():ArrayCollection;
		
		function getNewFullNodeIdWithChildrenInstance():FullNodeIdWithChildren;
		
		function getServiceContextInstance():ServiceContext;
		
		function showMessageBox(titleKeyMessage:String, textKeyMessage:String, textParams:Array):void;
		
		function createUpdateEvent(source:Object, property:String, oldValue:Object, newValue:Object):Object;
		
		function addEventListener(source:Object, eventType:String, handler:Function):void;
		
		function removeEventListener(source:Object, eventType:String, handler:Function):void;
			
	}
}