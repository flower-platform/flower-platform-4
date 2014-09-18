package org.flowerplatform.flex_client.server.blazeds;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.core.CorePlugin;

import flex.messaging.FlexContext;
import flex.messaging.endpoints.AMFEndpoint;
import flex.messaging.messages.Message;

/**
 * Logic necessary to synchronize the authentication info between the Flex client
 * and the embedded JavaScript client.
 * 
 * @author Mariana Gheorghe
 */
public class FlowerAMFEndpoint extends AMFEndpoint {

	@Override
	public Message serviceMessage(Message message) {
		Message ack = super.serviceMessage(message);
		// sync if the session id was invalid
		HttpServletRequest req = CorePlugin.getInstance().getRequestThreadLocal().get();
		String requestedSessionId = req.getRequestedSessionId();
		String currentSessionId = req.getSession().getId();
		if (!currentSessionId.equals(requestedSessionId)) {
			setJSessionIDHeader(ack);
		}
		return ack;
	}
	
	private void setJSessionIDHeader(Message ack) {
		ack.setHeader("jsessionid", FlexContext.getHttpRequest().getSession().getId());
	}
}
