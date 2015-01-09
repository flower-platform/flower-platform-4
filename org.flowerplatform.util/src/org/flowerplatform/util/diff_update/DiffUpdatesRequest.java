package org.flowerplatform.util.diff_update;

import java.util.Map;


/**
 * 
 * @author Claudiu Matei
 *
 */
public class DiffUpdatesRequest {

	private Map<String, Object>[] notificationChannelsData;

	public Map<String, Object>[] getNotificationChannelsData() {
		return notificationChannelsData;
	}

	public void setNotificationChannelsData(Map<String, Object>[] notificationChannelsData) {
		this.notificationChannelsData = notificationChannelsData;
	} 
	
}
