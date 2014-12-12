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
	public void addUpdate(String resourceSet, DiffUpdate update) {
		List<DiffUpdate> updates = updatesMap.get(resourceSet);
		if (updates == null) {
			return;
		}
		updates.add(update);
	}
	
	/**
	 * 
	 * @param resourceSet
	 * @param firstId
	 * @param lastId
	 */
	public List<DiffUpdate> getUpdates(String resourceSet, long firstId, long lastId) {
		List<DiffUpdate> updates = updatesMap.get(resourceSet);
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
	 * @param resourceSet
	 */
	public void registerResourceSet(String resourceSet) {
		if (!updatesMap.containsKey(resourceSet)) {
			updatesMap.put(resourceSet, new ArrayList<DiffUpdate>());
		}
	}

	/**
	 * 
	 * @param resourceSet
	 */
	public void unregisterResourceSet(String resourceSet) {
		updatesMap.remove(resourceSet);
	}

}
