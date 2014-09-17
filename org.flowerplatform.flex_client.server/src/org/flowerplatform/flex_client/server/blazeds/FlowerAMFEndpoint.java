package org.flowerplatform.flex_client.server.blazeds;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.flowerplatform.core.CorePlugin;

import flex.messaging.FlexContext;
import flex.messaging.endpoints.AMFEndpoint;
import flex.messaging.messages.CommandMessage;
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
		if (message instanceof CommandMessage) {
			CommandMessage cmd = (CommandMessage) message;
			switch (cmd.getOperation()) {
			case CommandMessage.LOGIN_OPERATION:
			case CommandMessage.LOGOUT_OPERATION:
				// sync for login/logout operations
				setCurrentUserHeader(ack);
				setJSessionIDHeader(ack);
				break;
			default:
				// nothing to do
			}
		}
		// sync if the session id was invalid
		HttpServletRequest req = CorePlugin.getInstance().getRequestThreadLocal().get();
		String requestedSessionId = req.getRequestedSessionId();
		String currentSessionId = req.getSession().getId();
		if (!currentSessionId.equals(requestedSessionId)) {
			setCurrentUserHeader(ack);
			setJSessionIDHeader(ack);
		}
		return ack;
	}
	
	private void setCurrentUserHeader(Message ack) {
		Principal userPrincipal = FlexContext.getUserPrincipal();
		String currentUser = "";
		if (userPrincipal != null) {
			currentUser = userPrincipal.getName();
		}
		ack.setHeader("currentUser", currentUser);
	}

	private void setJSessionIDHeader(Message ack) {
		ack.setHeader("jsessionid", FlexContext.getHttpRequest().getSession().getId());
	}
}
