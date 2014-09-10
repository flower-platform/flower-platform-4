package org.flowerplatform.js_client.java;

import org.flowerplatform.core.node.remote.FullNodeIdWithChildren;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Cristina Constantinescu
 */
public class JsExternalInvocator {

	/**
	 * Used to instantiate <code>children</code> in node object.
	 */
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
		// TODO implement
		System.out.println("showMessageBox(" + titleKeyMessage + " " + textKeyMessage + " " + textParams);
	}
	
}
