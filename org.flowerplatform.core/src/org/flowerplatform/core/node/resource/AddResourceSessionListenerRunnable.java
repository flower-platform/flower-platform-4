package org.flowerplatform.core.node.resource;

import javax.servlet.ServletContext;

import org.flowerplatform.util.RunnableWithParam;

/**
 * Registers a {@link ResourceSessionListener} as a session listener.
 * 
 * @author Mariana Gheorghe
 */
public class AddResourceSessionListenerRunnable implements RunnableWithParam<Void, ServletContext> {

	@Override
	public Void run(ServletContext context) {
		context.addListener(new ResourceSessionListener());
		return null;
	}

}
