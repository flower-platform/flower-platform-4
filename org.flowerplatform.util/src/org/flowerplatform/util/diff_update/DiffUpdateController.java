package org.flowerplatform.util.diff_update;

import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class DiffUpdateController {
	
	private INotificationChannelProvider notificationChannelProvider;	
	
	private DiffUpdateRegistry diffUpdateRegistry;
	
	public DiffUpdateController(INotificationChannelProvider notificationChannelProvider) {
		this.notificationChannelProvider = notificationChannelProvider;
		this.diffUpdateRegistry = new DiffUpdateRegistry();
	}
	
	/**
	 * 
	 * @param update
	 */
	public void addUpdate(DiffUpdate update) {
		String entityUid = update.getEntityUid();
		List<String> updateChannels = notificationChannelProvider.getNotificationChannels(entityUid);
		if (updateChannels == null) {
			return;
		}
		for (String updateChannel : updateChannels) {
			diffUpdateRegistry.addUpdate(updateChannel, update);
		}
	}

	/**
	 * 
	 * @param updateChannel
	 * @param firstId
	 * @param lastId
	 */
	public void getUpdates(String updateChannel, long firstId, long lastId) {
		diffUpdateRegistry.getUpdates(updateChannel, firstId, lastId);
	}

	/**
	 * 
	 * @param notificationChannel
	 */
	public void registerNotificationChannel(String notificationChannel) {
		diffUpdateRegistry.registerNotificationChannel(notificationChannel);
	}

	/**
	 * 
	 * @param notificationChannel
	 */
	public void unregisterNotificationChannel(String notificationChannel) {
		diffUpdateRegistry.unregisterNotificationChannel(notificationChannel);
	}
	
}
