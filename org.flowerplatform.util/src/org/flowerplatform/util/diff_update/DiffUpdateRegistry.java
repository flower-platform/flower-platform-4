package org.flowerplatform.util.diff_update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class DiffUpdateRegistry {

	private Map<String, List<DiffUpdate>> updatesMap = new HashMap<String, List<DiffUpdate>>();
	
	
	/**
	 * 
	 * @param update
	 */
	public void addUpdate(String notificationChannel, DiffUpdate update) {
		List<DiffUpdate> updates = updatesMap.get(notificationChannel);
		if (updates == null) {
			return;
		}
		updates.add(update);
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
			return null;
		}
		ArrayList<DiffUpdate> res = new ArrayList<>();
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
				res.add(res.size(), update);
			}
			i--;
		}
		return res;
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

}
