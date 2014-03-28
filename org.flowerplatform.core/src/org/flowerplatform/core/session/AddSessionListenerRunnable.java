package org.flowerplatform.core.session;

import javax.servlet.ServletContext;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.util.RunnableWithParam;

/**
 * Registers a {@link ComposedSessionListener} as a session listener.
 * 
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class AddSessionListenerRunnable implements RunnableWithParam<Void, ServletContext> {

	@Override
	public Void run(ServletContext context) {
		context.addListener(CorePlugin.getInstance().getComposedSessionListener());
		return null;
	}

}
