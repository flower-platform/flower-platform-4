package org.flowerplatform.core.session;

import javax.servlet.ServletContext;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.util.RunnableWithParam;
import org.flowerplatform.util.servlet.ServletUtils;

/**
 * Registers a {@link ComposedSessionListener} as a session listener.
 * 
 * @author Mariana Gheorghe
 * @author Cristina Constantinescu
 */
public class ServletBridgeRegistryListenerRunnable implements RunnableWithParam<Void, ServletContext> {

	@Override
	public Void run(ServletContext context) {
		context.addListener(CorePlugin.getInstance().getComposedSessionListener());
				
		ServletUtils.addServletContextAdditionalAttributes(ServletUtils.PROP_USE_FILES_FROM_TEMPORARY_DIRECTORY, CorePlugin.getInstance().getFlowerProperties().getProperty(ServletUtils.PROP_USE_FILES_FROM_TEMPORARY_DIRECTORY));
		
		return null;
	}

}
