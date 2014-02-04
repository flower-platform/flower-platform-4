package org.flowerplatform.flex_client.server.blazeds;

import java.util.Date;

import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flex.messaging.messages.Message;
import flex.messaging.messages.RemotingMessage;
import flex.messaging.services.remoting.adapters.JavaAdapter;

/**
 * @author Sebastian Solomon
 */
public class FlowerJavaAdapter extends JavaAdapter {
	
	private final static Logger logger = LoggerFactory.getLogger(AbstractFlowerJavaPlugin.class);

	 public Object invoke(Message message) {
		long startTime, endTime, difference;
		startTime = new Date().getTime();
		
		Object object =super.invoke(message);
		
		endTime = new Date().getTime();
		difference = endTime - startTime;
		String destination = message.getDestination();
		String operation = "";
		if (message instanceof RemotingMessage) {
			operation = ((RemotingMessage)message).getOperation();
		}
		logger.info("service: "+ destination + ", method: " + operation + ", " + difference + "ms");	
		return object;
	 }
}
