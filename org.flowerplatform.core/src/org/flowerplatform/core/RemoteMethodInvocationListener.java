package org.flowerplatform.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.update.Command;
import org.flowerplatform.core.node.update.remote.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invoked by the remote method invocation backend (e.g. Flex/BlazeDS, Rest/JSON), when a call from the client arrives.
 * 
 * @author Sebastian Solomon
 * @author Cristina Constantinescu
 * @author Mariana Gheorghe
 */
public class RemoteMethodInvocationListener {

	private final static Logger logger = LoggerFactory.getLogger(RemoteMethodInvocationListener.class);

	/**
	 * Compares the list of resources the client has with the list of resources that the client is subscribed to. For any
	 * resource that the client is not subscribed to anymore => re-subscribe.
	 * 
	 * <p>
	 * If a resource cannot be reloaded, the result of this invocation will be enriched with a list of {@link CoreConstants#RESOURCE_NODE_IDS_NOT_FOUND},
	 * so the client can react (e.g. close the obsolete editors).
	 */
	public void preInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
//		TempDeleteAfterGH279AndCo.INSTANCE.addNewNode();
		remoteMethodInvocationInfo.setStartTimestamp(new Date().getTime());

		String sessionId = CorePlugin.getInstance().getRequestThreadLocal().get().getSession().getId();
		List<String> clientResourceNodeIds = remoteMethodInvocationInfo.getResourceNodeIds();
		
		if (clientResourceNodeIds != null) {
			List<String> serverResourceNodeIds = CorePlugin.getInstance().getResourceService().getResourcesSubscribedBySession(sessionId);
			List<String> notFoundResourceNodeIds = new ArrayList<String>();
			for (String clientResourceNodeId : clientResourceNodeIds) {
				if (serverResourceNodeIds.contains(clientResourceNodeId)) {
					continue;
				}
				
				// the client is not subscribed to this resource anymore, maybe he went offline?
				// subscribe the client to this resource
				try {
					CorePlugin.getInstance().getResourceService().sessionSubscribedToResource(clientResourceNodeId, sessionId, new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
				} catch (Exception e) {
					// the resource could not be loaded; inform the client
					notFoundResourceNodeIds.add(clientResourceNodeId);
				}
			}
			
			if (notFoundResourceNodeIds.size() > 0) {
				remoteMethodInvocationInfo.getEnrichedReturnValue().put(CoreConstants.RESOURCE_NODE_IDS_NOT_FOUND, notFoundResourceNodeIds);
			}
		}
	}

	/**
	 * Changes {@link RemoteMethodInvocationInfo#getReturnValue()} to a map containing:
	 * <ul>
	 * 	<li> {@link CoreConstants#MESSAGE_RESULT} -> message invocation result
	 *  <li> {@link CoreConstants#UPDATES} -> map from fullResourceNodeId to a list of recent updates = all updates registered after {@link CoreConstants#LAST_UPDATE_TIMESTAMP}
	 *  <li> {{@link CoreConstants#LAST_UPDATE_TIMESTAMP} -> server timestamp of this request
	 * </ul>
	 * 
	 */
	public void postInvoke(RemoteMethodInvocationInfo remoteMethodInvocationInfo) {
		if (logger.isDebugEnabled()) {
			long endTime = new Date().getTime();
			long difference = endTime - remoteMethodInvocationInfo.getStartTimestamp();
			String serviceId = remoteMethodInvocationInfo.getServiceId();
			String methodName = remoteMethodInvocationInfo.getMethodName();
			logger.debug("[{}ms] {}.{}() invoked", new Object[] { difference, serviceId, methodName });
		}
		
		// prepare result
		remoteMethodInvocationInfo.getEnrichedReturnValue().put(CoreConstants.MESSAGE_RESULT, remoteMethodInvocationInfo.getReturnValue());
		
		// get info from header
		List<String> resourceNodeIds = remoteMethodInvocationInfo.getResourceNodeIds();
				
		if (resourceNodeIds != null) {
			// only request updates if the client is subscribed to some resource
			long timestampOfLastRequest = remoteMethodInvocationInfo.getTimestampOfLastRequest();
			long timestamp = new Date().getTime();
			remoteMethodInvocationInfo.getEnrichedReturnValue().put(CoreConstants.LAST_UPDATE_TIMESTAMP, timestamp);
			
			Map<String, List<Update>> resourceNodeIdToUpdates = new HashMap<String, List<Update>>();
			for (String resourceNodeId : resourceNodeIds) {
				List<Update> updates = CorePlugin.getInstance().getResourceService().getUpdates(resourceNodeId, timestampOfLastRequest, timestamp);
				resourceNodeIdToUpdates.put(resourceNodeId, updates);
				if (logger.isDebugEnabled()) {
					int size = -1;
					if (updates != null) {
						size = updates.size();
					}
					logger.debug("For resource = {} sending {} updates = {}", new Object[] { resourceNodeId, size, updates });
				}
			}
			if (resourceNodeIdToUpdates.size() > 0) {
				remoteMethodInvocationInfo.getEnrichedReturnValue().put(CoreConstants.UPDATES, resourceNodeIdToUpdates);
			}
		}
			
		remoteMethodInvocationInfo.setReturnValue(remoteMethodInvocationInfo.getEnrichedReturnValue());
	}

	
	/**
	 * @author Claudiu Matei 
	 * 
	 * Trebuie mutata de aici - poate intr-o clasa Util
	 * 
	 */
	public static String escapeFullNodeId(String fullNodeId) {
		return fullNodeId
				.replaceAll("\\(", "[")
				.replaceAll("\\)", "]")
				.replaceAll("\\|", "*");
	}
	
	public static Node createCommandNode(Command command) {
		Node node = new Node(CoreConstants.COMMAND_TYPE, null, command.getId(), null);
		String label=command.getTitle();
		if (label==null) label = node.getIdWithinResource();
		node.getProperties().put("name", label);
		return node;
	}
	
	public static void addNewNode(String resourceId) {
		Command command=new Command();
		CorePlugin.getInstance().getResourceService().addCommand(resourceId, command);
	}

	
	
}
