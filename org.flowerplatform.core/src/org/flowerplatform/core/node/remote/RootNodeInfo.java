package org.flowerplatform.core.node.remote;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.update.remote.Update;

/**
 * @author Cristina Constantinescu
 */
public class RootNodeInfo {

	private List<Update> updates;

	public List<Update> getUpdates() {
		if (updates == null) {
			updates = new ArrayList<Update>();
		}
		return updates;
	}
	
	public void addUpdate(Update update) {
		getUpdates().add(update);
	}
	
}
