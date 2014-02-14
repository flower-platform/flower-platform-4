package org.flowerplatform.flex_client.server.blazeds;

import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceRegistry;

import flex.messaging.services.RemotingService;
import flex.messaging.services.remoting.RemotingDestination;

/**
 * @author Sebastian Solomon
 */
public class FlowerRemotingService extends RemotingService {

	@Override
	public void start() {
		RemotingDestination destination;
		ServiceRegistry serviceRegistry = CorePlugin.getInstance()
				.getServiceRegistry();
		FlowerFlexFactory factory = new FlowerFlexFactory();

		for (Map.Entry<String, Object> entry : serviceRegistry.getMap().entrySet()) {
			destination = (RemotingDestination) 
					super.createDestination(entry.getKey());
			destination.setFactory(factory);
			destination.setSource(entry.getKey());
		}

		super.start();
	}

}
