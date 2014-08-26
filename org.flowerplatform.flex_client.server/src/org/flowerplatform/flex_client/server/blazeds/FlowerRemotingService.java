/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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