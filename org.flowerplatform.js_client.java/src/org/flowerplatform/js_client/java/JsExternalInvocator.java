package org.flowerplatform.js_client.java;

import java.util.concurrent.Callable;

import org.flowerplatform.core.node.remote.FullNodeIdWithChildren;
import org.flowerplatform.core.node.remote.ServiceContext;

public class JsExternalInvocator {

	public JsList<?> getNewListInstance() {
		return new JsList<>();
	}

	public FullNodeIdWithChildren getNewFullNodeIdWithChildrenInstance() {
		return new FullNodeIdWithChildren();
	}

	public ServiceContext<?> getServiceContextInstance() {
		return new ServiceContext<>();
	}

	public void showMessageBox(String titleKeyMessage, String textKeyMessage, String[] textParams) {
		System.out.println("showMessageBox(" + titleKeyMessage + " " + textKeyMessage + " " + textParams);
	}

	public Object createUpdateEvent(Object source, String property, Object oldValue, Object newValue) {
		return new JsPropertyChangeEvent(source, property, oldValue, newValue);
	}
	
	public void addEventListener(Object source, String eventType, Callable<?> handler) {
		System.out.println("addEventListener(" + source + " " + eventType + " " + handler);
	}
	
	public void removeEventListener(Object source, String eventType, Callable<?> handler) {
		System.out.println("removeEventListener(" + source + " " + eventType + " " + handler);
	}
	
}
