package org.flowerplatform.core.node.update;

import javax.servlet.ServletContext;

import org.flowerplatform.core.node.resource.ResourceInfoSessionListener;
import org.flowerplatform.util.RunnableWithParam;

/**
 * Registers a {@link ResourceInfoSessionListener} as a session listener.
 * 
 * @author Mariana Gheorghe
 */
public class AddRootNodeInfoHttpSessionListenerRunnable implements RunnableWithParam<Void, ServletContext> {

	@Override
	public Void run(ServletContext context) {
		context.addListener(new ResourceInfoSessionListener());
		return null;
	}

}
