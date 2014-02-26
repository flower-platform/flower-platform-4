package org.flowerplatform.core.node.update;

import javax.servlet.ServletContext;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.util.RunnableWithParam;

/**
 * Registers a {@link RootNodeInfoSessionListener} as a session listener.
 * 
 * @author Mariana Gheorghe
 */
public class AddRootNodeInfoHttpSessionListenerRunnable implements RunnableWithParam<Void, ServletContext> {

	@Override
	public Void run(ServletContext context) {
		context.addListener(new RootNodeInfoSessionListener(
				CorePlugin.getInstance().getNodeService().getRootNodeInfoDAO()));
		return null;
	}

}
