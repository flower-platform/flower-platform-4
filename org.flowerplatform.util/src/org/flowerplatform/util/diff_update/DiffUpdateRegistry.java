/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.util.diff_update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class DiffUpdateRegistry {

	protected static final Logger LOGGER = LoggerFactory.getLogger(DiffUpdateRegistry.class);
	
	private Map<String, List<DiffUpdate>> updatesMap = new ConcurrentHashMap<String, List<DiffUpdate>>();
	
	/**
	 * 
	 * @param update
	 */
	public void addUpdate(String notificationChannel, DiffUpdate update) {
		List<DiffUpdate> updates = updatesMap.get(notificationChannel);
		if (updates == null) {
			throw new IllegalArgumentException("For inexistent notificationChannel =" + notificationChannel + ", trying to add update =" + update);
		}
		synchronized (updates) {
			long lastUpdateId = getLastUpdateId(notificationChannel);
			update.setId(lastUpdateId + 1);
			LOGGER.debug("For notificationChannel = {}, adding update = {}", notificationChannel, update);
			updates.add(update);
		}
	}
	
	/**
	 * 
	 * @param notificationChannel
	 * @param firstId
	 * @param lastId
	 */
	public List<DiffUpdate> getUpdates(String notificationChannel, long firstId, long lastId) {
		List<DiffUpdate> updates = updatesMap.get(notificationChannel);
		if (updates == null) {
			return Collections.emptyList();
		}
		synchronized (updates) {
			ArrayList<DiffUpdate> res = new ArrayList<DiffUpdate>();
			boolean foundFirst = false, foundLast = (lastId == -1);
			int i = updates.size() - 1;
			while (!foundFirst && i >= 0) {
				DiffUpdate update = updates.get(i);
				if (update.getId() == lastId) {
					foundLast = true;
				}
				if (update.getId() == firstId) {
					foundFirst = true;
				}
				if (foundLast) {
					res.add(0, update);
				}
				i--;
			}
			return res;
		}
	}

	/**
	 * 
	 * @param notificationChannel
	 */
	public void registerNotificationChannel(String notificationChannel) {
		if (!updatesMap.containsKey(notificationChannel)) {
			updatesMap.put(notificationChannel, new ArrayList<DiffUpdate>());
		}
	}

	/**
	 * 
	 * @param notificationChannel
	 */
	public void unregisterNotificationChannel(String notificationChannel) {
		updatesMap.remove(notificationChannel);
	}

	/**
	 * @author see class
	 */
	public long getLastUpdateId(String notificationChannel) {
		List<DiffUpdate> updates = updatesMap.get(notificationChannel);
		if (updates == null) {
			return 0;
		}
		synchronized (updates) {
			if (updates.size() == 0) {
				return 0;
			}
			DiffUpdate lastUpdate = updates.get(updates.size() - 1);
			return lastUpdate.getId();
		}
	}
	
}
