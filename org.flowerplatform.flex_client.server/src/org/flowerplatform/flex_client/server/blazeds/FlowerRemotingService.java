package org.flowerplatform.flex_client.server.blazeds;

import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceRegistry;

import flex.messaging.services.RemotingService;
import flex.messaging.services.remoting.RemotingDestination;

/**
 * Overrides start() method to provide services from our service registry.
 * 
 * @author Sebastian Solomon
 */
public class FlowerRemotingService extends RemotingService {

	@Override
	public void start() {
		RemotingDestination destination;
		
		adapterClasses.put("java-object", "org.flowerplatform.flex_client.server.blazeds.FlowerJavaAdapter");
		addDefaultChannel("remoting-amf");
		setDefaultAdapter("java-object");
		
		ServiceRegistry serviceRegistry = CorePlugin.getInstance()
				.getServiceRegistry();
		FlowerFlexFactory factory = new FlowerFlexFactory();

		for (Map.Entry<String, Object> entry : serviceRegistry.getMap()
				.entrySet()) {
			destination = (RemotingDestination) createDestination(entry
					.getKey());
			destination.setFactory(factory);
			destination.setSource(entry.getKey());
		}

		super.start();
	}

}
