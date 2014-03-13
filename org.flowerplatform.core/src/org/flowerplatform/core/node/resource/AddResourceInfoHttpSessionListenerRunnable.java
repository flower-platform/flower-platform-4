package org.flowerplatform.core.node.resource;

import javax.servlet.ServletContext;

import org.flowerplatform.util.RunnableWithParam;

/**
 * Registers a {@link ResourceInfoSessionListener} as a session listener.
 * 
 * @author Mariana Gheorghe
 */
public class AddResourceInfoHttpSessionListenerRunnable implements RunnableWithParam<Void, ServletContext> {

	@Override
	public Void run(ServletContext context) {
		context.addListener(new ResourceInfoSessionListener());
		return null;
	}

}
