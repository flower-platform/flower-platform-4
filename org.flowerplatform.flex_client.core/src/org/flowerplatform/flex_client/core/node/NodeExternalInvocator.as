package org.flowerplatform.flex_client.core.node {
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flex_client.core.editor.remote.FullNodeIdWithChildren;
	import org.flowerplatform.flex_client.core.node.remote.ServiceContext;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeExternalInvocator implements IExternalInvocator {
				
		public function showMessageBox(titleKeyMessage:String, textKeyMessage:String, textParams:Array):void {			
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(Resources.getMessage(textKeyMessage, textParams))
				.setTitle(Resources.getMessage(titleKeyMessage))
				.setWidth(300)
				.setHeight(200)
				.showMessageBox();
		}
		
		public function addEventListener(source:Object, eventType:String, handler:Function):void {
			IEventDispatcher(source).addEventListener(eventType, handler);
		}
		
		public function createUpdateEvent(source:Object, property:String, oldValue:Object, newValue:Object):Object {			
			return PropertyChangeEvent.createUpdateEvent(source, property, oldValue, newValue);
		}
		
		public function getNewFullNodeIdWithChildrenInstance():FullNodeIdWithChildren {			
			return new FullNodeIdWithChildren();
		}
		
		public function getNewListInstance():ArrayCollection {			
			return new ArrayCollection();
		}
		
		public function getServiceContextInstance():ServiceContext {			
			return new ServiceContext();
		}
		
		public function removeEventListener(source:Object, eventType:String, handler:Function):void {
			IEventDispatcher(source).removeEventListener(eventType, handler);
		}
		
	}
}