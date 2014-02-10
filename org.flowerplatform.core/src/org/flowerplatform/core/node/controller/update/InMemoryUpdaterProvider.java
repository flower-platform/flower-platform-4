package org.flowerplatform.core.node.controller.update;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.update.remote.ChildrenListUpdate;

public class InMemoryUpdaterProvider extends UpdaterPersistenceProvider {

	private static Map<String, List<Update>> nodeIdToPropertyUpdates = new HashMap<String, List<Update>>();	
	private static List<ChildrenListUpdate> childrenListUpdates = new ArrayList<ChildrenListUpdate>();
		
	class Update {
		public long timestamp;
		public String propertyName;
		public Object propertyValue;
		
		public Update setTimestamp(long timestamp) {
			this.timestamp = timestamp;
			return this;
		}		
		public Update setPropertyName(String propertyName) {
			this.propertyName = propertyName;
			return this;
		}
		public Update setPropertyValue(Object propertyValue) {
			this.propertyValue = propertyValue;
			return this;
		}	
	}
		
	public void addPropertyUpdate(Node node, long timestamp, String property, Object value) {
		if (!nodeIdToPropertyUpdates.containsKey(node.getId())) {
			nodeIdToPropertyUpdates.put(node.getId(), new ArrayList<Update>());
		}
		nodeIdToPropertyUpdates.get(node.getId()).add(new Update()
											.setPropertyName(property)
											.setPropertyValue(value)
											.setTimestamp(timestamp));
	}
	
	public void addChildrenListUpdate(Node node, long timestamp, String type, int index, Node childNode) {
		childrenListUpdates.add(new ChildrenListUpdate()
														.setParentNodeAs(node)
														.setTypeAs(type)
														.setIndexAs(index)
														.setNodeAs(childNode)
														.setTimestamp(timestamp));
	}
	
	public Map<String, Object> getPropertyUpdates(Node node, long startingWithTimestamp) {
		if (nodeIdToPropertyUpdates.containsKey(node.getId())) {
			List<Update> updatesStartingWithTs = new ArrayList<Update>();
			for (Update update : nodeIdToPropertyUpdates.get(node.getId())) {
				if (update.timestamp > startingWithTimestamp) {
					updatesStartingWithTs.add(update);
				}
			}
			Collections.sort(updatesStartingWithTs, new Comparator<Update>() {
				public int compare(Update u1, Update u2) {
					return Long.compare(u1.timestamp, u2.timestamp);
	            }
			});			
			Map<String, Object> propertiesUpdates = new HashMap<String, Object>();
			for (Update update : updatesStartingWithTs) {
				propertiesUpdates.put(update.propertyName, update.propertyValue);
			}			
			return propertiesUpdates;
		}
		return null;
	}
	
	public List<ChildrenListUpdate> getChildrenListUpdates(Node node, long startingWithTimestamp) {
		List<ChildrenListUpdate> listUpdates = new ArrayList<ChildrenListUpdate>();
		for (ChildrenListUpdate update : childrenListUpdates) {
			if (update.getParentNode().getId().equals(node.getId()) && update.getTimestamp1() > startingWithTimestamp) {
				listUpdates.add(update);
			}
		}				
		return listUpdates;
	}

}
