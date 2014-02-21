package org.flowerplatform.core.node.update;

import javax.servlet.ServletContext;

import org.flowerplatform.util.RunnableWithParam;

/**
 * @author Mariana Gheorghe
 */
public class AddRootNodeInfoHttpSessionListenerRunnable implements RunnableWithParam<Void, ServletContext> {

	@Override
	public Void run(ServletContext context) {
		context.addListener(new RootNodeInfoSessionListener());
		return null;
	}

}
