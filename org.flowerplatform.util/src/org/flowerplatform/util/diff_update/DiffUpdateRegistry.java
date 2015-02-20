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
	
	// TODO CC: temporary code
	public void clearDiffUpdates() {
		for (Map.Entry<String, List<DiffUpdate>> entry : updatesMap.entrySet()) {
			entry.setValue(new ArrayList<DiffUpdate>());
		}
	}

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
