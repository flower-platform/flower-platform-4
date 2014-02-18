package org.flowerplatform.flex_client.server.blazeds;

import javax.servlet.ServletContext;

import org.flowerplatform.util.RunnableWithParam;

import flex.messaging.HttpFlexSession;

/**
 * Adds {@link HttpFlexSession} as a listener. Needed to unregister a flex client when its session expires.
 * 
 * @author Mariana Gheorghe
 */
public class AddHttpFlexSessionListenerRunnable implements RunnableWithParam<Void, ServletContext> {

	@Override
	public Void run(ServletContext context) {
		context.addListener(new HttpFlexSession());
		return null;
	}

}
