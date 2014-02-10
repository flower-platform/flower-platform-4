package org.flowerplatform.core.mindmap;


/**
 * @author Cristina Constantinescu
 */
public class NodeUpdateDAO {
//
//	private static Map<String, List<Update>> nodeIdToPropertyUpdates = new HashMap<String, List<Update>>();	
//	private static List<ChildrenListUpdate> childrenListUpdates = new ArrayList<ChildrenListUpdate>();
//		
//	class Update {
//		public long timestamp;
//		public String propertyName;
//		public Object propertyValue;
//		
//		public Update setTimestamp(long timestamp) {
//			this.timestamp = timestamp;
//			return this;
//		}		
//		public Update setPropertyName(String propertyName) {
//			this.propertyName = propertyName;
//			return this;
//		}
//		public Update setPropertyValue(Object propertyValue) {
//			this.propertyValue = propertyValue;
//			return this;
//		}	
//	}
//		
//	public void addPropertyUpdate(String nodeId, long timestamp, String propertyName, Object propertyValue) {
//		if (!nodeIdToPropertyUpdates.containsKey(nodeId)) {
//			nodeIdToPropertyUpdates.put(nodeId, new ArrayList<Update>());
//		}
//		nodeIdToPropertyUpdates.get(nodeId).add(new Update()
//											.setPropertyName(propertyName)
//											.setPropertyValue(propertyValue)
//											.setTimestamp(timestamp));
//	}
//	
//	public void addChildrenListUpdate(String nodeId, long timestamp, String type, int index, Object childNode) {
//		childrenListUpdates.add(new ChildrenListUpdate()
//														.setParentNodeIdAs(nodeId)
//														.setTypeAs(type)
//														.setIndexAs(index)
//														.setNodeAs(childNode)
//														.setTimestamp(timestamp));
//	}
//	
//	public Map<String, Object> getPropertyUpdates(String nodeId, long startingWithTimestamp) {
//		if (nodeIdToPropertyUpdates.containsKey(nodeId)) {
//			List<Update> updatesStartingWithTs = new ArrayList<Update>();
//			for (Update update : nodeIdToPropertyUpdates.get(nodeId)) {
//				if (update.timestamp > startingWithTimestamp) {
//					updatesStartingWithTs.add(update);
//				}
//			}
//			Collections.sort(updatesStartingWithTs, new Comparator<Update>() {
//				public int compare(Update u1, Update u2) {
//					return Long.compare(u1.timestamp, u2.timestamp);
//	            }
//			});			
//			Map<String, Object> propertiesUpdates = new HashMap<String, Object>();
//			for (Update update : updatesStartingWithTs) {
//				propertiesUpdates.put(update.propertyName, update.propertyValue);
//			}			
//			return propertiesUpdates;
//		}
//		return null;
//	}
//	
//	public List<ChildrenListUpdate> getChildrenListUpdates(String nodeId, long startingWithTimestamp) {
//		List<ChildrenListUpdate> listUpdates = new ArrayList<ChildrenListUpdate>();
//		for (ChildrenListUpdate update : childrenListUpdates) {
//			if (update.getParentNodeId().equals(nodeId) && update.getTimestamp1() > startingWithTimestamp) {
//				listUpdates.add(update);
//			}
//		}				
//		return listUpdates;
//	}
	
}
